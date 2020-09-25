package com.iptiQ.test.loadbalancer.lb;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.iptiQ.test.loadbalancer.lb.strategy.LoadBalancingStrategy;
import com.iptiQ.test.loadbalancer.model.SimpleRequest;
import com.iptiQ.test.loadbalancer.provider.Provider;

@ExtendWith(MockitoExtension.class)
public class LoadBalancerImplDefaultTest {

  @Mock Provider provider;
  @Mock
  LoadBalancingStrategy loadBalancingStrategy;
  @InjectMocks LoadBalancerImplDefault loadBalancerImplDefault;

  SimpleRequest request = new SimpleRequest("Test Request");

  @Test
  public void getProvider_happyPath_shouldResolveProviderUsingStrategy() {
    // given
    when(loadBalancingStrategy.getProvider()).thenReturn(provider);

    // when
    loadBalancerImplDefault.processRequest(request);

    // then
    verify(loadBalancingStrategy, times(1)).getProvider();
  }

  @Test
  public void getProvider_happyPath_shouldCallProcessOoCorrectProvider() {
    // given
    when(loadBalancingStrategy.getProvider()).thenReturn(provider);

    // when
    loadBalancerImplDefault.processRequest(request);

    // then
    verify(provider, times(1)).process(request);
  }
}
