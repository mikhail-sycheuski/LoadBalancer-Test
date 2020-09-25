package com.iptiQ.test.loadbalancer.lb.registry.healthcheck.history;

import static java.time.ZoneOffset.UTC;

import java.time.Instant;
import java.time.OffsetDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class HealthCheck {

  private final String providerId;
  private final HealthCheckStatus status;
  private final Instant timePoint;

  public HealthCheck(String providerId, HealthCheckStatus status) {
    this.providerId = providerId;
    this.status = status;
    this.timePoint = OffsetDateTime.now(UTC).toInstant();
  }

  public boolean isUp() {
    return status == HealthCheckStatus.UP;
  }

  public boolean isDown() {
    return status == HealthCheckStatus.DOWN;
  }
}
