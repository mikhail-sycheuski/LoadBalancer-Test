package com.iptiQ.test.loadbalancer.lb.strategy;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.iptiQ.test.loadbalancer.provider.Provider;

/**
 * This proxy is intended to guarantee thread safety in the process of provider obtainment by the
 * client. It ensures that single provider will not be assigned for processing of the request of
 * more than 1 client.
 */
public class LoadBalancingStrategyImplThreadSafetyProxy implements LoadBalancingStrategy {

  private final LoadBalancingStrategy underlyingLoadBalancingStrategy;
  private final Lock lock;

  public LoadBalancingStrategyImplThreadSafetyProxy(
      LoadBalancingStrategy underlyingLoadBalancingStrategy) {
    this.underlyingLoadBalancingStrategy = underlyingLoadBalancingStrategy;
    this.lock = new ReentrantLock();
  }

  @Override
  public Provider getProvider() {
    lock.lock();
    Provider provider = underlyingLoadBalancingStrategy.getProvider();
    lock.unlock();

    return provider;
  }
}
