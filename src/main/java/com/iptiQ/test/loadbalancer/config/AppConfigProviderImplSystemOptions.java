package com.iptiQ.test.loadbalancer.config;

import static com.iptiQ.test.loadbalancer.lb.strategy.factory.LoadBalancingType.RANDOM_ORDER;
import static java.lang.System.getProperty;

import java.util.Optional;

import com.iptiQ.test.loadbalancer.lb.strategy.factory.LoadBalancingType;

public class AppConfigProviderImplSystemOptions implements AppConfigProvider {

  private static class AppSupportedSystemOptions {
    public static final String PROVIDER_PARALLELISM_LEVEL = "providerParallelismLevel";
    public static final String NUMBER_OF_PROVIDERS = "numberOfProviders";
    public static final String HEALTH_CHECK_INTERVAL_MS = "healthCheckIntervalMS";
    public static final String NUMBER_OF_CLIENTS_TO_SIMULATE = "numberOfClientsToSimulate";
    public static final String CLIENT_REQUEST_INTERVAL_MS = "clientRequestIntervalMS";
    public static final String CLIENT_REQUEST_PROCESSING_DELAY_MS =
        "clientRequestProcessingDelayMS";
    public static final String LOAD_BALANCING_TYPE = "loadBalancingType";
  }

  @Override
  public AppConfig provide() {
    int providerParallelismLevelValue =
        Optional.ofNullable(getProperty(AppSupportedSystemOptions.PROVIDER_PARALLELISM_LEVEL))
            .map(Integer::valueOf)
            .orElse(3);

    int numberOfProvidersValue =
        Optional.ofNullable(getProperty(AppSupportedSystemOptions.NUMBER_OF_PROVIDERS))
            .map(Integer::valueOf)
            .orElse(10);

    int healthCheckIntervalMSValue =
        Optional.ofNullable(getProperty(AppSupportedSystemOptions.HEALTH_CHECK_INTERVAL_MS))
            .map(Integer::valueOf)
            .orElse(1000);

    int numberOfClientsToSimulateValue =
        Optional.ofNullable(getProperty(AppSupportedSystemOptions.NUMBER_OF_CLIENTS_TO_SIMULATE))
            .map(Integer::valueOf)
            .orElse(35);

    int clientRequestProcessingDelayMSValue =
        Optional.ofNullable(
                getProperty(AppSupportedSystemOptions.CLIENT_REQUEST_PROCESSING_DELAY_MS))
            .map(Integer::valueOf)
            .orElse(500);

    int clientRequestIntervalMSValue =
        Optional.ofNullable(getProperty(AppSupportedSystemOptions.CLIENT_REQUEST_INTERVAL_MS))
            .map(Integer::valueOf)
            .orElse(1000);

    LoadBalancingType loadBalancingTypeValue =
        Optional.ofNullable(getProperty(AppSupportedSystemOptions.LOAD_BALANCING_TYPE))
            .map(value -> LoadBalancingType.fromOptionNameWithFallback(value, RANDOM_ORDER))
            .orElse(RANDOM_ORDER);

    return AppConfig.builder()
        .providerParallelismLevel(providerParallelismLevelValue)
        .numberOfProviders(numberOfProvidersValue)
        .healthCheckIntervalMS(healthCheckIntervalMSValue)
        .numberOfClientsToSimulate(numberOfClientsToSimulateValue)
        .clientRequestIntervalMS(clientRequestIntervalMSValue)
        .clientRequestProcessingDelayMS(clientRequestProcessingDelayMSValue)
        .loadBalancingType(loadBalancingTypeValue)
        .build();
  }
}
