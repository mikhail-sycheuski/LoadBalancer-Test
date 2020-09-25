package com.iptiQ.test.loadbalancer.lb.strategy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.Iterator;
import java.util.List;

import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.iptiQ.test.loadbalancer.lb.registry.ProviderMetadata;
import com.iptiQ.test.loadbalancer.lb.registry.ProviderRegistry;
import com.iptiQ.test.loadbalancer.provider.Provider;

@ExtendWith(MockitoExtension.class)
public class LoadBalancingStrategyImplRecursiveRoundRobinTest {

  @Mock Provider provider1;
  @Mock Provider provider2;
  @Mock Provider provider3;

  @Mock ProviderMetadata providerMeta1;
  @Mock ProviderMetadata providerMeta2;
  @Mock ProviderMetadata providerMeta3;

  @Mock Iterator<ProviderMetadata> roundRobinIterator;
  @Mock ProviderRegistry providerRegistry;

  @InjectMocks @Spy
  LoadBalancingStrategyImplRecursiveRoundRobin loadBalancingStrategyImplRecursiveRoundRobin;

  List<Provider> providers;
  List<ProviderMetadata> providerMetadata;

  @BeforeEach
  public void setUp() {
    providers = List.of(provider1, provider2, provider3);
    providerMetadata = List.of(providerMeta1, providerMeta2, providerMeta3);

    lenient().when(providerRegistry.getRegisteredProvidersMetadata()).thenReturn(providerMetadata);

    lenient().when(providerMeta1.getLinkedProvider()).thenReturn(provider1);
    lenient().when(providerMeta2.getLinkedProvider()).thenReturn(provider2);
    lenient().when(providerMeta3.getLinkedProvider()).thenReturn(provider3);

    lenient().when(providerMeta1.isIncludedInLoadBalancing()).thenReturn(true);
    lenient().when(providerMeta2.isIncludedInLoadBalancing()).thenReturn(true);
    lenient().when(providerMeta3.isIncludedInLoadBalancing()).thenReturn(true);
  }

  @Test
  public void getProvider_firstExecution_shouldReturnFirstProvider() {
    // given
    roundRobinIterator = null;

    // when
    Provider resolvedProvider = loadBalancingStrategyImplRecursiveRoundRobin.getProvider();

    // then
    assertThat(resolvedProvider, is(provider1));
  }

  @Test
  public void getProvider_thirdExecution_shouldReturnThirdProvider() {
    // given
    when(roundRobinIterator.hasNext()).thenReturn(true);
    when(roundRobinIterator.next()).thenReturn(providerMeta3);

    // when
    Provider resolvedProvider = loadBalancingStrategyImplRecursiveRoundRobin.getProvider();

    // then
    assertThat(resolvedProvider, is(provider3));
  }

  @Test
  public void getProvider_secondExecutionButProviderIsNotInBalancing_shouldReturnThirdProvider() {
    // given
    when(providerMeta2.isIncludedInLoadBalancing()).thenReturn(false);
    when(roundRobinIterator.hasNext()).thenReturn(true, true);
    when(roundRobinIterator.next()).thenReturn(providerMeta2, providerMeta3);

    // when
    Provider resolvedProvider = loadBalancingStrategyImplRecursiveRoundRobin.getProvider();

    // then
    assertThat(resolvedProvider, is(provider3));
  }

  @Test
  public void getProvider_secondExecutionButProviderIsNotInBalancing_shouldRenewIterator() {
    // given
    when(roundRobinIterator.hasNext()).thenReturn(false);

    // when
    Provider resolvedProvider = loadBalancingStrategyImplRecursiveRoundRobin.getProvider();

    // then
    assertThat(
        roundRobinIterator,
        is(IsNot.not(loadBalancingStrategyImplRecursiveRoundRobin.roundRobinIterator)));
  }

  @Test
  public void getProvider_secondExecutionButProviderIsNotInBalancing_shouldReturnFirstProvider() {
    // given
    when(roundRobinIterator.hasNext()).thenReturn(false);

    // when
    Provider resolvedProvider = loadBalancingStrategyImplRecursiveRoundRobin.getProvider();

    // then
    assertThat(resolvedProvider, is(provider1));
  }

  @Test
  public void getProvider_allExcludedFromBalancing_shouldThrowException() {
    // given
    when(providerMeta1.isIncludedInLoadBalancing()).thenReturn(false);
    when(providerMeta2.isIncludedInLoadBalancing()).thenReturn(false);
    when(providerMeta3.isIncludedInLoadBalancing()).thenReturn(false);

    // when
    Executable executable = () -> loadBalancingStrategyImplRecursiveRoundRobin.getProvider();

    // then
    assertThrows(NoAvailableProviderException.class, executable);
  }
}
