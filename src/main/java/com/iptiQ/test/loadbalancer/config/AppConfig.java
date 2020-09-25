package com.iptiQ.test.loadbalancer.config;

import com.iptiQ.test.loadbalancer.lb.strategy.factory.LoadBalancingType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Builder
@ToString
public class AppConfig {

  int providerParallelismLevel;
  int numberOfProviders;
  int numberOfClientsToSimulate;
  int healthCheckIntervalMS;
  int clientRequestIntervalMS;
  int clientRequestProcessingDelayMS;
  LoadBalancingType loadBalancingType;
}
