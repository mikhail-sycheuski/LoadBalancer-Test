package com.iptiQ.test.loadbalancer.lb.registry;

import com.iptiQ.test.loadbalancer.lb.registry.healthcheck.history.HealthCheck;
import com.iptiQ.test.loadbalancer.lb.registry.healthcheck.history.HealthCheckHistory;
import com.iptiQ.test.loadbalancer.lb.registry.healthcheck.history.HealthCheckHistoryImplInMemory;
import com.iptiQ.test.loadbalancer.provider.Provider;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Object that stores metadata for particular provider. Since there is only one thread reading the
 * data of this object and only one concurrent thread that is going to modify the state of metadata
 * (the one executing HealthCheckTask) - there is no need in mature synchronization here. Setting
 * shared property "includedInLoadBalancing" to volatile should make everything work fine because of
 * the JVM visibility and happens before guarantee.
 *
 * <p>In case that would change in future we would need to utilize ReadWriteLock/StampedLock here
 * and make HeartbeatHistory threadsafe
 */
@EqualsAndHashCode(exclude = "healthCheckHistory")
@ToString(exclude = "healthCheckHistory")
public class ProviderMetadata {

  private static final int NUMBER_OF_CONSECUTIVE_UPS = 2;

  private final Provider linkedProvider;
  private final HealthCheckHistory healthCheckHistory;
  private volatile boolean includedInLoadBalancing;

  public ProviderMetadata(Provider linkedProvider) {
    this.linkedProvider = linkedProvider;
    this.healthCheckHistory = new HealthCheckHistoryImplInMemory();
    this.includedInLoadBalancing = true;
  }

  public void updateProviderStatus(HealthCheck healthCheck) {
    healthCheckHistory.appendHeartbeatCheck(healthCheck);

    if (healthCheck.isDown()) {
      includedInLoadBalancing = false;
    } else if (healthCheckHistory.checkNumberConsecutiveTimesUp(NUMBER_OF_CONSECUTIVE_UPS)) {
      includedInLoadBalancing = true;
    }
  }

  public boolean isIncludedInLoadBalancing() {
      return includedInLoadBalancing;
  }

  public Provider getLinkedProvider() {
    return linkedProvider;
  }
}
