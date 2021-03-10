package com.example.testing.payment.stripe;

import com.example.testing.payment.CardPaymentCharge;
import com.example.testing.payment.CardPaymentCharger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;

/* Spring will initialize this Bean when Property stripe.enabled has the Value false */
@ConditionalOnProperty(value = "stripe.enabled", havingValue = "false")
@Service
public class MockStripeService implements CardPaymentCharger {
    @Override
    public CardPaymentCharge chargeCard(String cardSource, BigDecimal amount, Currency currency, String description) {
        return new CardPaymentCharge(true);
    }
}
