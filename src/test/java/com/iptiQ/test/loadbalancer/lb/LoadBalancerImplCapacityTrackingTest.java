package com.iptiQ.test.loadbalancer.lb;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.iptiQ.test.loadbalancer.lb.registry.ProviderRegistry;
import com.iptiQ.test.loadbalancer.lb.strategy.LoadBalancingStrategy;
import com.iptiQ.test.loadbalancer.lb.strategy.NoAvailableProviderException;
import com.iptiQ.test.loadbalancer.model.SimpleRequest;
import com.iptiQ.test.loadbalancer.provider.Provider;

@ExtendWith(MockitoExtension.class)
public class LoadBalancerImplCapacityTrackingTest {

  @Mock Provider provider1;
  @Mock Provider provider2;

  @Mock Semaphore semaphore1;
  @Mock Semaphore semaphore2;

  @Mock ProviderRegistry providerRegistry;
  @Mock LoadBalancingStrategy loadBalancingStrategy;

  LoadBalancerImplCapacityTracking loadBalancerImplCapacityTracking;

  List<Provider> providers;
  Map<Provider, Semaphore> usageStatistics;
  int capacityLimit = 2;

  SimpleRequest request = new SimpleRequest("Test Request");

  @BeforeEach
  public void setUp() {
    providers = List.of(provider1, provider2);

    lenient().when(semaphore1.tryAcquire()).thenReturn(true);
    lenient().when(semaphore2.tryAcquire()).thenReturn(true);
    lenient().when(semaphore1.availablePermits()).thenReturn(capacityLimit);
    lenient().when(semaphore2.availablePermits()).thenReturn(capacityLimit);

    usageStatistics = new HashMap<>();
    usageStatistics.put(provider1, semaphore1);
    usageStatistics.put(provider2, semaphore2);

    loadBalancerImplCapacityTracking =
        new LoadBalancerImplCapacityTracking(
            providerRegistry, loadBalancingStrategy, usageStatistics, capacityLimit);
  }

  @Test
  public void getProvider_happyPath_shouldCallStrategy1Time() {
    // given
    when(loadBalancingStrategy.getProvider()).thenReturn(provider1);

    // when
    loadBalancerImplCapacityTracking.processRequest(request);

    // then
    verify(loadBalancingStrategy, times(1)).getProvider();
  }

  @Test
  public void getProvider_happyPath_shouldCallProvider1Process() {
    // given
    when(loadBalancingStrategy.getProvider()).thenReturn(provider1);

    // when
    loadBalancerImplCapacityTracking.processRequest(request);

    // then
    verify(provider1, times(1)).process(request);
  }

  @Test
  public void getProvider_provider1IsExhausted_shouldCallStrategy2Time() {
    // given
    when(loadBalancingStrategy.getProvider()).thenReturn(provider1, provider2);
    when(semaphore1.tryAcquire()).thenReturn(false);

    // when
    loadBalancerImplCapacityTracking.processRequest(request);

    // then
    verify(loadBalancingStrategy, times(2)).getProvider();
  }

  @Test
  public void getProvider_provider1IsExhausted_shouldCallProvider2Process() {
    // given
    when(loadBalancingStrategy.getProvider()).thenReturn(provider1, provider2);
    when(semaphore1.tryAcquire()).thenReturn(false);

    // when
    loadBalancerImplCapacityTracking.processRequest(request);

    // then
    verify(provider1, never()).process(request);
    verify(provider2, times(1)).process(request);
  }

  @Test
  public void getProvider_allExcludedFromBalancing_shouldThrowException() {
    // given
    lenient().when(semaphore1.availablePermits()).thenReturn(0);
    lenient().when(semaphore2.availablePermits()).thenReturn(0);

    // when
    Executable executable = () -> loadBalancerImplCapacityTracking.processRequest(request);

    // then
    assertThrows(NoAvailableProviderException.class, executable);
  }
}
