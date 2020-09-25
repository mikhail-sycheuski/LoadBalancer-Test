package com.iptiQ.test.loadbalancer;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.stream.IntStream;

import com.iptiQ.test.loadbalancer.client.simulator.ClientLoadSimulator;
import com.iptiQ.test.loadbalancer.config.AppConfig;
import com.iptiQ.test.loadbalancer.config.AppConfigProvider;
import com.iptiQ.test.loadbalancer.config.AppConfigProviderImplSystemOptions;
import com.iptiQ.test.loadbalancer.id.UniqueStringIdGenerator;
import com.iptiQ.test.loadbalancer.id.UniqueStringIdGeneratorImplUUID;
import com.iptiQ.test.loadbalancer.lb.LoadBalancer;
import com.iptiQ.test.loadbalancer.lb.LoadBalancerImplCapacityTracking;
import com.iptiQ.test.loadbalancer.lb.registry.HeartbeatAwareProviderRegistry;
import com.iptiQ.test.loadbalancer.lb.registry.ProviderRegistryImplDefault;
import com.iptiQ.test.loadbalancer.lb.registry.healthcheck.HealthCheckManager;
import com.iptiQ.test.loadbalancer.lb.registry.healthcheck.HealthCheckManagerImplRepeatable;
import com.iptiQ.test.loadbalancer.lb.strategy.LoadBalancingStrategy;
import com.iptiQ.test.loadbalancer.lb.strategy.factory.LoadBalancingStrategyFactory;
import com.iptiQ.test.loadbalancer.lb.strategy.factory.LoadBalancingStrategyFactoryImplDefault;
import com.iptiQ.test.loadbalancer.provider.Provider;
import com.iptiQ.test.loadbalancer.provider.StringIdProvider;

/**
 * Test runner which initialize all necessary components and showing the testing run information
 * depending on the AppConfig
 */
public class App {

  private static final int PROGRAM_TEST_DURATION_MS = 30 * 1000;

  public static void main(String[] args) throws InterruptedException {
    AppConfigProvider appConfigProvider = new AppConfigProviderImplSystemOptions();
    AppConfig appConfig = appConfigProvider.provide();

    System.out.println("Starting app initialization with following config:" + appConfig);

    Collection<Provider> providers = initializeProviders(appConfig);
    HeartbeatAwareProviderRegistry providerRegistry = new ProviderRegistryImplDefault();
    providerRegistry.registerProviders(providers);

    LoadBalancingStrategyFactory loadBalancingStrategyFactory =
        new LoadBalancingStrategyFactoryImplDefault();
    LoadBalancingStrategy loadBalancingStrategy =
        loadBalancingStrategyFactory.getStrategy(
            appConfig.getLoadBalancingType(), providerRegistry);

    LoadBalancer loadBalancer =
        new LoadBalancerImplCapacityTracking(
            providerRegistry, loadBalancingStrategy, appConfig.getProviderParallelismLevel());

    HealthCheckManager healthCheckManager =
        new HealthCheckManagerImplRepeatable(
            providerRegistry, appConfig.getHealthCheckIntervalMS());
    healthCheckManager.initialize();

    ClientLoadSimulator clientLoadSimulator =
        new ClientLoadSimulator(
            loadBalancer,
            appConfig.getNumberOfClientsToSimulate(),
            appConfig.getClientRequestIntervalMS());
    clientLoadSimulator.initialize();

    Thread.sleep(PROGRAM_TEST_DURATION_MS);

    clientLoadSimulator.shutdown();
    healthCheckManager.shutdown();

    System.exit(0);
  }

  private static Collection<Provider> initializeProviders(AppConfig appConfig) {
    UniqueStringIdGenerator uniqueStringIdGenerator = new UniqueStringIdGeneratorImplUUID();
    return IntStream.range(0, appConfig.getNumberOfProviders())
        .mapToObj(
            index ->
                new StringIdProvider(
                    uniqueStringIdGenerator.generateNextId(),
                    appConfig.getClientRequestProcessingDelayMS()))
        .collect(toList());
  }
}
