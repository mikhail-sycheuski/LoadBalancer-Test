package com.iptiQ.test.loadbalancer.lb.registry.healthcheck.history;

public interface HealthCheckHistory {

  void appendHeartbeatCheck(HealthCheck healthCheck);

  boolean checkNumberConsecutiveTimesUp(int number);

  boolean checkNumberConsecutiveTimesDown(int number);
}
