package com.iptiQ.test.loadbalancer.lb;

import com.iptiQ.test.loadbalancer.model.SimpleRequest;
import com.iptiQ.test.loadbalancer.model.SimpleResponse;

public interface LoadBalancer {

  SimpleResponse processRequest(SimpleRequest simpleRequest);
}
