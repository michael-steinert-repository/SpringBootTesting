package com.example.testing.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public class CardPaymentCharge {
    private final boolean isCardDebited;
}
