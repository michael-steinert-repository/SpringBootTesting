package com.example.testing.payment.stripe;

import com.example.testing.payment.CardPaymentCharge;
import com.example.testing.payment.CardPaymentCharger;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

/* Spring will initialize this Bean when Property stripe.enabled has the Value true */
@ConditionalOnProperty(value = "stripe.enabled", havingValue = "true")
@AllArgsConstructor
@Service
public class StripeService implements CardPaymentCharger {
    private final static RequestOptions requestOptions = RequestOptions.builder().setApiKey("sk_test_4eC39HqLyjWDarjtT1zdp7dc").build();

    private final StripeApi stripeApi;

    @Override
    public CardPaymentCharge chargeCard(String cardSource, BigDecimal amount, Currency currency, String description) {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency.getCurrencyCode());
        params.put("cardSource", cardSource);
        params.put("description", description);
        try {
            Charge charge = stripeApi.create(params, requestOptions);
            Boolean isCardDebited = charge.getPaid();
            return new CardPaymentCharge(isCardDebited);
        } catch (StripeException e) {
            throw new IllegalStateException("Cannot create Stripe Charge", e);
        }
    }
}
