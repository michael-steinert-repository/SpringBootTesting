package com.example.testing.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = {"id"}, allowGetters = true)
@Entity
public class Payment {
    @Id
    @GeneratedValue
    private Long id;
    private UUID customerId;
    private BigDecimal amount;
    private Currency currency;
    private String source;
    private String description;
}
