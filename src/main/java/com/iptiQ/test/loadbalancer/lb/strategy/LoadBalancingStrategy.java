package com.iptiQ.test.loadbalancer.lb.strategy;

import com.iptiQ.test.loadbalancer.provider.Provider;

public interface LoadBalancingStrategy {

  Provider getProvider();
}
