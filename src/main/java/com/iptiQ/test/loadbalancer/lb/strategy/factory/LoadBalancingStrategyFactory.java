package com.iptiQ.test.loadbalancer.lb.strategy.factory;

import com.iptiQ.test.loadbalancer.lb.registry.ProviderRegistry;
import com.iptiQ.test.loadbalancer.lb.strategy.LoadBalancingStrategy;

public interface LoadBalancingStrategyFactory {

  LoadBalancingStrategy getStrategy(
      LoadBalancingType loadBalancingType, ProviderRegistry providerRegistry);
}
