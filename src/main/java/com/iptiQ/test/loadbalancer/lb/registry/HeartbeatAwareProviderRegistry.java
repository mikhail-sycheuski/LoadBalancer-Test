package com.iptiQ.test.loadbalancer.lb.registry;

import com.iptiQ.test.loadbalancer.lb.registry.healthcheck.history.HealthCheck;
import com.iptiQ.test.loadbalancer.provider.Provider;

public interface HeartbeatAwareProviderRegistry extends ProviderRegistry {
  void updateProviderStatus(Provider provider, HealthCheck healthCheck);
}
