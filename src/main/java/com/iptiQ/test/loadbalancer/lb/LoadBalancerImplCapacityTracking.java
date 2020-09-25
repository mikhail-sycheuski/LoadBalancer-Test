package com.iptiQ.test.loadbalancer.lb;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

import com.iptiQ.test.loadbalancer.lb.registry.ProviderRegistry;
import com.iptiQ.test.loadbalancer.lb.strategy.LoadBalancingStrategy;
import com.iptiQ.test.loadbalancer.lb.strategy.NoAvailableProviderException;
import com.iptiQ.test.loadbalancer.model.SimpleRequest;
import com.iptiQ.test.loadbalancer.model.SimpleResponse;
import com.iptiQ.test.loadbalancer.provider.Provider;

/**
 * Capacity tracking implementation of the LoadBalancer. It is aware about concurrent provider's
 * throughput limited by 'capacityLimit' and responsible for rerouting request in case of particular
 * provider overload as well as all provider's pool exhaustion.
 */
public class LoadBalancerImplCapacityTracking implements LoadBalancer {

  private final int capacityLimit;
  private final ProviderRegistry providerRegistry;
  private final LoadBalancingStrategy loadBalancingStrategy;
  private final Map<Provider, Semaphore> usageStatistics;

  //Visible for testing
  LoadBalancerImplCapacityTracking(
      ProviderRegistry providerRegistry,
      LoadBalancingStrategy loadBalancingStrategy,
      Map<Provider, Semaphore> usageStatistics,
      int capacityLimit) {
    this.capacityLimit = capacityLimit;
    this.providerRegistry = providerRegistry;
    this.loadBalancingStrategy = loadBalancingStrategy;
    this.usageStatistics = usageStatistics;
  }

  public LoadBalancerImplCapacityTracking(
      ProviderRegistry providerRegistry,
      LoadBalancingStrategy loadBalancingStrategy,
      int capacityLimit) {
    this.capacityLimit = capacityLimit;
    this.providerRegistry = providerRegistry;
    this.loadBalancingStrategy = loadBalancingStrategy;
    this.usageStatistics = initializeUsageStatistics();
  }

  private Map<Provider, Semaphore> initializeUsageStatistics() {
    return providerRegistry.getRegisteredProviders().stream()
        .collect(
            toMap(
                identity(),
                key -> new Semaphore(capacityLimit),
                (key1, key2) -> key1,
                ConcurrentHashMap::new));
  }

  @Override
  public SimpleResponse processRequest(SimpleRequest simpleRequest) {
    while (!checkProvidersOverloaded()) {
      Provider provider = loadBalancingStrategy.getProvider();
      Semaphore providerSemaphore = usageStatistics.get(provider);

      if (providerSemaphore.tryAcquire()) {
        SimpleResponse response = provider.process(simpleRequest);
        providerSemaphore.release();
        return response;
      }
    }

    throw new NoAvailableProviderException("Provider pool is exhausted");
  }

  private boolean checkProvidersOverloaded() {
    return usageStatistics.values().stream()
        .allMatch(semaphore -> semaphore.availablePermits() == 0);
  }
}
