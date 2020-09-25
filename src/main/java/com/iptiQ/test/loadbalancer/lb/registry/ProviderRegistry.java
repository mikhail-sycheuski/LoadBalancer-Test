package com.iptiQ.test.loadbalancer.lb.registry;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.iptiQ.test.loadbalancer.provider.Provider;

public interface ProviderRegistry {
  void registerProviders(Collection<Provider> providers);

  Set<Provider> getRegisteredProviders();

  List<ProviderMetadata> getRegisteredProvidersMetadata();
}
