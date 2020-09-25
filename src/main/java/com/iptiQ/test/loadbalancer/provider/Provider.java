package com.iptiQ.test.loadbalancer.provider;

import com.iptiQ.test.loadbalancer.lb.registry.healthcheck.history.HealthCheckStatus;
import com.iptiQ.test.loadbalancer.model.SimpleRequest;
import com.iptiQ.test.loadbalancer.model.SimpleResponse;

public interface Provider {

  String getInternalId();

  SimpleResponse process(SimpleRequest simpleRequest);

  HealthCheckStatus healthCheck();
}
