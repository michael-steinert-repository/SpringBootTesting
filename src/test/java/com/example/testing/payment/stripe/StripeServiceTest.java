package com.example.testing.payment.stripe;

import com.example.testing.payment.CardPaymentCharge;
import com.example.testing.payment.Payment;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class StripeServiceTest {
    @Mock
    private StripeApi stripeApi;
    @Captor
    private ArgumentCaptor<Map<String, Object>> requestMapArgumentCaptor;
    @Captor
    private ArgumentCaptor<RequestOptions> requestOptionsArgumentCaptor;
    private StripeService stripeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        stripeService = new StripeService(stripeApi);
    }

    @Test
    void itShouldChargeCard() throws StripeException {
        /* Given */
        String cardSource = "card1234";
        BigDecimal amount = new BigDecimal("42.00");
        Currency currency = Currency.getInstance("EUR");
        String description = "Donation";
        Charge charge = new Charge();
        charge.setPaid(true);
        given(stripeApi.create(anyMap(), any())).willReturn(charge);
        /* When */
        CardPaymentCharge cardPaymentCharge = stripeService.chargeCard(cardSource, amount, currency, description);
        /* Then */
        then(stripeApi).should().create(requestMapArgumentCaptor.capture(), requestOptionsArgumentCaptor.capture());
        Map<String, Object> requestMap = requestMapArgumentCaptor.getValue();
        assertThat(requestMap.keySet()).hasSize(4);
        assertThat(requestMap.get("" +
                "cardSource")).isEqualTo(cardSource);
        assertThat(requestMap.get("amount")).isEqualTo(amount);
        assertThat(requestMap.get("currency")).isEqualTo(currency.getCurrencyCode());
        assertThat(requestMap.get("description")).isEqualTo(description);
        RequestOptions requestOptions = requestOptionsArgumentCaptor.getValue();
        assertThat(requestOptions).isNotNull();
        assertThat(cardPaymentCharge).isNotNull();
        assertThat(cardPaymentCharge.isCardDebited()).isEqualTo(charge.getPaid());
    }
}