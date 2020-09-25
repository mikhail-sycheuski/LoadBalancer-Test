package com.iptiQ.test.loadbalancer.lb.strategy.factory;

import java.util.stream.Stream;

public enum LoadBalancingType {
  RANDOM_ORDER("RO"),
  ROUND_ROBIN("RR");

  private final String optionName;

  LoadBalancingType(String optionName) {
    this.optionName = optionName;
  }

  public String getOptionName() {
    return optionName;
  }

  public static LoadBalancingType fromOptionNameWithFallback(
      String optionName, LoadBalancingType fallback) {
    return Stream.of(LoadBalancingType.values())
        .filter(loadBalancingType -> loadBalancingType.optionName.equals(optionName))
        .findFirst()
        .orElse(fallback);
  }
}
