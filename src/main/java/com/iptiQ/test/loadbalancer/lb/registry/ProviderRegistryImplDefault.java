package com.iptiQ.test.loadbalancer.lb.registry;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toUnmodifiableMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.iptiQ.test.loadbalancer.lb.registry.healthcheck.history.HealthCheck;
import com.iptiQ.test.loadbalancer.provider.Provider;

public class ProviderRegistryImplDefault implements HeartbeatAwareProviderRegistry {

  private Map<Provider, ProviderMetadata> registeredProvidersMap;
  private List<ProviderMetadata> providersMetadata;

  public void registerProviders(Collection<Provider> providers) {
    this.registeredProvidersMap =
        providers.stream().collect(toUnmodifiableMap(identity(), ProviderMetadata::new));
    this.providersMetadata = List.copyOf(registeredProvidersMap.values()); // unmodifiable ordered view
  }

  @Override
  public Set<Provider> getRegisteredProviders() {
    return registeredProvidersMap.keySet();
  }

  @Override
  public List<ProviderMetadata> getRegisteredProvidersMetadata() {
    return providersMetadata;
  }

  @Override
  public void updateProviderStatus(Provider provider, HealthCheck healthCheck) {
    registeredProvidersMap.get(provider).updateProviderStatus(healthCheck);
  }
}
