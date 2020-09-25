package com.iptiQ.test.loadbalancer.lb.strategy;

import static java.util.function.Predicate.not;

import java.util.Iterator;
import java.util.List;

import com.iptiQ.test.loadbalancer.lb.registry.ProviderMetadata;
import com.iptiQ.test.loadbalancer.lb.registry.ProviderRegistry;
import com.iptiQ.test.loadbalancer.provider.Provider;

public class LoadBalancingStrategyImplRecursiveRoundRobin implements LoadBalancingStrategy {

  private ProviderRegistry providerRegistry;
  private Iterator<ProviderMetadata> roundRobinIterator;

  public LoadBalancingStrategyImplRecursiveRoundRobin(ProviderRegistry providerRegistry) {
    this.providerRegistry = providerRegistry;
  }

  /**
   * Since this app is written for testing purpose and no extremely huge number of registered
   * providers is expected - this algorithm is implemented via recursion approach. In case there
   * might be high value of providers another not recursive implementation is needed in order to
   * avoid StackOverflowError.
   *
   * @return resolved provider instance
   */
  @Override
  public Provider getProvider() {
    List<ProviderMetadata> registeredProvidersMetadata =
        providerRegistry.getRegisteredProvidersMetadata();

    boolean allDown =
        registeredProvidersMetadata.stream()
            .allMatch(not(ProviderMetadata::isIncludedInLoadBalancing));
    if (allDown) {
      throw new NoAvailableProviderException("Provider pool is exhausted");
    }

    if (roundRobinIterator == null || !roundRobinIterator.hasNext()) {
      roundRobinIterator = registeredProvidersMetadata.iterator();
    }

    Provider nextProvider = getNextProvider();

    return nextProvider != null ? nextProvider : getProvider();
  }

  private Provider getNextProvider() {
    Provider nextProvider = null;

    if (roundRobinIterator.hasNext()) {
      ProviderMetadata providerMetadata = roundRobinIterator.next();
      if (providerMetadata.isIncludedInLoadBalancing()) {
        nextProvider = providerMetadata.getLinkedProvider();
      } else {
        nextProvider = getNextProvider();
      }
    }

    return nextProvider;
  }
}
