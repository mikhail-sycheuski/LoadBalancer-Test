package com.iptiQ.test.loadbalancer.lb.strategy;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Random;

import com.iptiQ.test.loadbalancer.lb.registry.ProviderMetadata;
import com.iptiQ.test.loadbalancer.lb.registry.ProviderRegistry;
import com.iptiQ.test.loadbalancer.provider.Provider;

public class LoadBalancingStrategyImplRandomOrder implements LoadBalancingStrategy {

  private final ProviderRegistry providerRegistry;

  public LoadBalancingStrategyImplRandomOrder(ProviderRegistry providerRegistry) {
    this.providerRegistry = providerRegistry;
  }

  @Override
  public Provider getProvider() {
    List<ProviderMetadata> registeredProvidersMetadata =
        providerRegistry.getRegisteredProvidersMetadata();

    List<Provider> healthyProviders =
        registeredProvidersMetadata.stream()
            .filter(ProviderMetadata::isIncludedInLoadBalancing)
            .map(ProviderMetadata::getLinkedProvider)
            .collect(toList());

    if (!healthyProviders.isEmpty()) {
      int providerIndex = new Random().nextInt(healthyProviders.size());
      return healthyProviders.get(providerIndex);
    } else {
      throw new NoAvailableProviderException("Provider pool is exhausted");
    }
  }
}
