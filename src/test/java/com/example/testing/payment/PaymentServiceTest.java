package com.example.testing.payment;

import com.example.testing.customer.Customer;
import com.example.testing.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class PaymentServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private CardPaymentCharger cardPaymentCharger;
    @Captor
    private ArgumentCaptor<Payment> paymentArgumentCaptor;
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        paymentService = new PaymentService(customerRepository, paymentRepository, cardPaymentCharger);
    }

    @Test
    void itShouldChargeCardSuccessfully() {
        /* Given */
        UUID customerId = UUID.randomUUID();
        /* Customer exists: Mocking the Return instead of returning an empty Object like new Customer() */
        given(customerRepository.findById(customerId)).willReturn(Optional.of(mock(Customer.class)));
        Payment payment = new Payment(null, null, new BigDecimal("42.00"), Currency.getInstance("EUR"), "card1234", "Donation");
        /* Card is charged successfully */
        given(cardPaymentCharger.chargeCard(payment.getSource(), payment.getAmount(), payment.getCurrency(), payment.getDescription()))
                .willReturn(new CardPaymentCharge(true));
        /* When */
        paymentService.chargeCard(customerId, payment);
        /* Then */
        then(paymentRepository).should().save(paymentArgumentCaptor.capture());
        Payment paymentArgumentCaptorValue = paymentArgumentCaptor.getValue();
        assertThat(paymentArgumentCaptorValue).isEqualToIgnoringGivenFields(payment, "customerId");
        assertThat(paymentArgumentCaptorValue.getCustomerId()).isEqualTo(customerId);
    }

    @Test
    void itShouldThrowAnExceptionWhenCardIsNotCharged() {
        /* Given */
        UUID customerId = UUID.randomUUID();
        /* Customer exists: Mocking the Return instead of returning an empty Object like new Customer() */
        given(customerRepository.findById(customerId)).willReturn(Optional.of(mock(Customer.class)));
        Payment payment = new Payment(null, null, new BigDecimal("42.00"), Currency.getInstance("EUR"), "card1234", "Donation");
        /* Card is not charged successfully */
        given(cardPaymentCharger.chargeCard(payment.getSource(), payment.getAmount(), payment.getCurrency(), payment.getDescription()))
                .willReturn(new CardPaymentCharge(false));
        /* When */
        /* Then */
        assertThatThrownBy(() -> paymentService.chargeCard(customerId, payment))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("Card not debited for Customer %s.", customerId));
        /* No Interaction with PaymentRepository */
        then(paymentRepository).shouldHaveNoInteractions();
    }

    @Test
    void itShouldNotChargeCardAndThrownAnExceptionWhenCurrencyNotSupported() {
        /* Given */
        UUID customerId = UUID.randomUUID();
        /* Customer exists: Mocking the Return instead of returning an empty Object like new Customer() */
        given(customerRepository.findById(customerId)).willReturn(Optional.of(mock(Customer.class)));
        Payment payment = new Payment(null, null, new BigDecimal("42.00"), Currency.getInstance("GBP"), "card1234", "Donation");
        /* When */
        assertThatThrownBy(() -> paymentService.chargeCard(customerId, payment))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("Currency with CurrencyCode %s is not supported.", payment.getCurrency().getCurrencyCode()));
        /* Then */
        /* No Interaction with CardPaymentCharger */
        then(cardPaymentCharger).shouldHaveNoInteractions();
        /* No Interaction with PaymentRepository */
        then(paymentRepository).shouldHaveNoInteractions();
    }

    @Test
    void itShouldNotChargeAndThrownAnExceptionWhenCustomerNotFound() {
        /* Given */
        UUID customerId = UUID.randomUUID();
        /* Customer not found in Repository */
        given(customerRepository.findById(customerId)).willReturn(Optional.empty());
        /* When */
        /* Then */
        assertThatThrownBy(()->paymentService.chargeCard(customerId, new Payment()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("Customer with ID %s not found.", customerId));
        /* No Interaction with CardPaymentCharger */
        then(cardPaymentCharger).shouldHaveNoInteractions();
        /* No Interaction with PaymentRepository */
        then(paymentRepository).shouldHaveNoInteractions();
    }
}