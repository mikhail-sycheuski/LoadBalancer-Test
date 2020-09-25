package com.iptiQ.test.loadbalancer.provider;

import java.util.Random;

import com.iptiQ.test.loadbalancer.lb.registry.healthcheck.history.HealthCheckStatus;
import com.iptiQ.test.loadbalancer.model.SimpleRequest;
import com.iptiQ.test.loadbalancer.model.SimpleResponse;

import lombok.Value;

@Value
public class StringIdProvider implements Provider {

  String internalId;
  int clientRequestProcessingDelayMS;

  @Override
  public SimpleResponse process(SimpleRequest simpleRequest) {
    System.out.printf(
        "Provider[%s] is processing request from client[%s]%n",
        internalId, simpleRequest.getClientId());

    try {
      Thread.sleep(clientRequestProcessingDelayMS);
    } catch (InterruptedException exception) {
      System.out.printf("Thread[%s] is interrupted%n", Thread.currentThread().getName());
    }

    return new SimpleResponse(internalId);
  }

  @Override
  public HealthCheckStatus healthCheck() {
    int randomStatusIndicator = new Random().nextInt(1000);
    return randomStatusIndicator >= 300 ? HealthCheckStatus.UP : HealthCheckStatus.DOWN;
  }
}
