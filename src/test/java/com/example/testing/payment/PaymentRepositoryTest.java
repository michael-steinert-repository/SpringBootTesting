package com.example.testing.payment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/* To trigger the Annotations from Customer the following Property is necessary */
/* For Example @Column(nullable = false) */
@DataJpaTest(properties = "spring.jpa.properties.javax.persistence.validation.mode=none")
class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    void itShouldSavePayment() {
        /* Given */
        Long paymentId = 1L;
        Payment payment = new Payment(paymentId, UUID.randomUUID(), new BigDecimal("42.00"), Currency.getInstance("EUR"), "card1234", "Donation");
        /* When */
        paymentRepository.save(payment);
        /* Then */
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        assertThat(optionalPayment)
                .isPresent()
                .hasValueSatisfying(p -> {
                    assertThat(p.getId()).isEqualTo(payment.getId());
                    assertThat(p.getCustomerId()).isEqualTo(payment.getCustomerId());
                    assertThat(p.getAmount()).isEqualTo(payment.getAmount());
                    assertThat(p.getCurrency()).isEqualTo(payment.getCurrency());
                    assertThat(p.getSource()).isEqualTo(payment.getSource());
                    assertThat(p.getDescription()).isEqualTo(payment.getDescription());
                });
    }
}