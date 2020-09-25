package com.iptiQ.test.loadbalancer.lb.registry.healthcheck.history;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

/** This implementation is not thread safe. */
public class HealthCheckHistoryImplInMemory implements HealthCheckHistory {

  private final TreeSet<HealthCheck> healthChecks;

  public HealthCheckHistoryImplInMemory() {
    this.healthChecks = new TreeSet<>(Comparator.comparing(HealthCheck::getTimePoint));
  }

  @Override
  public void appendHeartbeatCheck(HealthCheck healthCheck) {
    this.healthChecks.add(healthCheck);
  }

  @Override
  public boolean checkNumberConsecutiveTimesUp(int number) {
    return checkNumberConsecutiveTimesStatus(number, HealthCheckStatus.UP);
  }

  @Override
  public boolean checkNumberConsecutiveTimesDown(int number) {
    return checkNumberConsecutiveTimesStatus(number, HealthCheckStatus.DOWN);
  }

  private boolean checkNumberConsecutiveTimesStatus(int number, HealthCheckStatus status) {
    Iterator<HealthCheck> iterator = healthChecks.descendingIterator();

    boolean cumulativeStatus = true;

    int heartbeatCheckIndex = 0;
    while (iterator.hasNext() && heartbeatCheckIndex < number) {
      cumulativeStatus &= iterator.next().getStatus().equals(status);
      heartbeatCheckIndex++;
    }

    // if the overall amount of checks in the history is less then the "number" - not enough data to
    // determine correct status, thus return false as a fallback
    return heartbeatCheckIndex == number && cumulativeStatus;
  }
}
