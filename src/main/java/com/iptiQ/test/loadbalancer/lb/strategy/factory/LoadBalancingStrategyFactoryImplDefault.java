package com.iptiQ.test.loadbalancer.lb.strategy.factory;

import static java.lang.String.format;

import com.iptiQ.test.loadbalancer.lb.registry.ProviderRegistry;
import com.iptiQ.test.loadbalancer.lb.strategy.LoadBalancingStrategy;
import com.iptiQ.test.loadbalancer.lb.strategy.LoadBalancingStrategyImplRandomOrder;
import com.iptiQ.test.loadbalancer.lb.strategy.LoadBalancingStrategyImplRecursiveRoundRobin;
import com.iptiQ.test.loadbalancer.lb.strategy.LoadBalancingStrategyImplThreadSafetyProxy;

public class LoadBalancingStrategyFactoryImplDefault implements LoadBalancingStrategyFactory {

  @Override
  public LoadBalancingStrategy getStrategy(
      LoadBalancingType loadBalancingType, ProviderRegistry providerRegistry) {
    LoadBalancingStrategy loadBalancingStrategy;

    switch (loadBalancingType) {
      case ROUND_ROBIN:
        loadBalancingStrategy =
            threadSafetyProxy(new LoadBalancingStrategyImplRecursiveRoundRobin(providerRegistry));
        break;
      case RANDOM_ORDER:
        loadBalancingStrategy =
            threadSafetyProxy(new LoadBalancingStrategyImplRandomOrder(providerRegistry));
        break;
      default:
        throw new UnsupportedStrategyException(
            format("Unsupported LoadBalancingType[%s] supplied", loadBalancingType));
    }

    return loadBalancingStrategy;
  }

  private LoadBalancingStrategy threadSafetyProxy(LoadBalancingStrategy loadBalancingStrategy) {
    return new LoadBalancingStrategyImplThreadSafetyProxy(loadBalancingStrategy);
  }
}
