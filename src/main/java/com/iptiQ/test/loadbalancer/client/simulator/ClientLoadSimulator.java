package com.iptiQ.test.loadbalancer.client.simulator;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.iptiQ.test.loadbalancer.lb.LoadBalancer;

public class ClientLoadSimulator {

  private final ScheduledExecutorService executorService;
  private final LoadBalancer loadBalancer;

  private final int numberOfClientsToSimulate;
  private final int clientRequestIntervalMS;

  public ClientLoadSimulator(
      LoadBalancer loadBalancer, int numberOfClientsToSimulate, int clientRequestIntervalMS) {
    this.executorService =
        new ScheduledThreadPoolExecutor(numberOfClientsToSimulate, new ClientWorkerThreadFactory());
    this.loadBalancer = loadBalancer;
    this.clientRequestIntervalMS = clientRequestIntervalMS;
    this.numberOfClientsToSimulate = numberOfClientsToSimulate;
  }

  public void initialize() {
    System.out.println("Started ClientLoadSimulator initialization");

    IntStream.range(0, numberOfClientsToSimulate)
        .mapToObj(index -> new ClientLoadTask(loadBalancer))
        .forEach(
            clientLoadTask ->
                executorService.scheduleWithFixedDelay(
                    clientLoadTask,
                    clientRequestIntervalMS,
                    clientRequestIntervalMS,
                    TimeUnit.MILLISECONDS));

    System.out.println("Finalized ClientLoadSimulator initialization");
  }

  public void shutdown() {
    System.out.println("Started  ClientLoadSimulator shutdown");

    executorService.shutdown();

    try {
      if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
        executorService.shutdownNow();
      }
    } catch (InterruptedException exception) {
      executorService.shutdownNow();
    }

    System.out.println("Finalized ClientLoadSimulator shutdown");
  }
}
