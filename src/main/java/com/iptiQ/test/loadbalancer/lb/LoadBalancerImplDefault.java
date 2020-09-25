package com.iptiQ.test.loadbalancer.lb;

import com.iptiQ.test.loadbalancer.lb.strategy.LoadBalancingStrategy;
import com.iptiQ.test.loadbalancer.model.SimpleRequest;
import com.iptiQ.test.loadbalancer.model.SimpleResponse;

/**
 * Default implementation of the LoadBalancer. It does not know anything about provider's
 * throughput.
 */
public class LoadBalancerImplDefault implements LoadBalancer {

  private LoadBalancingStrategy loadBalancingStrategy;

  public LoadBalancerImplDefault(LoadBalancingStrategy loadBalancingStrategy) {
    this.loadBalancingStrategy = loadBalancingStrategy;
  }

  @Override
  public SimpleResponse processRequest(SimpleRequest simpleRequest) {
    return loadBalancingStrategy.getProvider().process(simpleRequest);
  }
}
