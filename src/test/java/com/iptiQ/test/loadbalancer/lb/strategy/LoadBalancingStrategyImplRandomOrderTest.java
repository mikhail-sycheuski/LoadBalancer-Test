package com.iptiQ.test.loadbalancer.lb.strategy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIn.in;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.List;

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
public class LoadBalancingStrategyImplRandomOrderTest {

  @Mock Provider provider1;
  @Mock Provider provider2;
  @Mock Provider provider3;

  @Mock ProviderMetadata providerMeta1;
  @Mock ProviderMetadata providerMeta2;
  @Mock ProviderMetadata providerMeta3;

  @Mock ProviderRegistry providerRegistry;
  @InjectMocks @Spy LoadBalancingStrategyImplRandomOrder loadBalancingStrategyImplRandomOrder;

  List<Provider> providers;
  List<ProviderMetadata> providerMetadata;

  @BeforeEach
  public void setUp() {
    lenient().when(providerRegistry.getRegisteredProvidersMetadata())
      .thenReturn(List.of(providerMeta1, providerMeta2, providerMeta3));

    lenient().when(providerMeta1.getLinkedProvider()).thenReturn(provider1);
    lenient().when(providerMeta2.getLinkedProvider()).thenReturn(provider2);
    lenient().when(providerMeta3.getLinkedProvider()).thenReturn(provider3);

    lenient().when(providerMeta1.isIncludedInLoadBalancing()).thenReturn(true);
    lenient().when(providerMeta2.isIncludedInLoadBalancing()).thenReturn(true);
    lenient().when(providerMeta3.isIncludedInLoadBalancing()).thenReturn(true);

    providers = List.of(provider1, provider2, provider3);
    providerMetadata = List.of(providerMeta1, providerMeta2, providerMeta3);
  }

  @Test
  public void getProvider_happyPath_shouldReturnAny() {
    // given
    // all included in load balancing

    // when
    Provider resolvedProvider = loadBalancingStrategyImplRandomOrder.getProvider();

    // then
    assertThat(resolvedProvider, in(providers));
  }

  @Test
  public void getProvider_allExcludedFromBalancing_shouldThrowException() {
    // given
    when(providerMeta1.isIncludedInLoadBalancing()).thenReturn(false);
    when(providerMeta2.isIncludedInLoadBalancing()).thenReturn(false);
    when(providerMeta3.isIncludedInLoadBalancing()).thenReturn(false);

    // when
    Executable executable = () -> loadBalancingStrategyImplRandomOrder.getProvider();

    // then
    assertThrows(NoAvailableProviderException.class, executable);
  }
}
