package com.iptiQ.test.loadbalancer.client.simulator;

import static java.lang.String.format;

import com.iptiQ.test.loadbalancer.lb.LoadBalancer;
import com.iptiQ.test.loadbalancer.model.SimpleRequest;
import com.iptiQ.test.loadbalancer.model.SimpleResponse;

public class ClientLoadTask implements Runnable {

  private final LoadBalancer loadBalancer;
  private long internalRequestCounter;

  public ClientLoadTask(LoadBalancer loadBalancer) {
    this.loadBalancer = loadBalancer;
  }

  @Override
  public void run() {
    final String clientRequestId =
        format(
            "Client request - %s-%s", Thread.currentThread().getName(), ++internalRequestCounter);

    System.out.printf("Sending new client request [%s]%n", clientRequestId);

    try {
      SimpleResponse response = loadBalancer.processRequest(new SimpleRequest(clientRequestId));
      System.out.printf(
          "Client request [%s] succeeded with response[%s]%n", clientRequestId, response.getPayload());
    } catch (Throwable failure) {
      System.out.printf("Client request [%s] processing has failed: %n", clientRequestId);
      failure.printStackTrace();
    }
  }
}
