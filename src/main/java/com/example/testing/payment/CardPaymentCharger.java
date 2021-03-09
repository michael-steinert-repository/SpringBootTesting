package com.example.testing.payment;

import java.math.BigDecimal;
import java.util.Currency;

public interface CardPaymentCharger {
    CardPaymentCharge chargeCard(String cardSource, BigDecimal amount, Currency currency, String description);
}
