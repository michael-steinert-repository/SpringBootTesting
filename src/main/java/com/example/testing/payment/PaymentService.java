package com.example.testing.payment;

import com.example.testing.customer.Customer;
import com.example.testing.customer.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class PaymentService {

    private static final List<Currency> ACCEPTED_CURRENCIES = List.of(Currency.getInstance("EUR"), Currency.getInstance("USD"));

    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final CardPaymentCharger cardPaymentCharger;

    void chargeCard(UUID customerId, Payment payment) {
        /* Check if Customer is present */
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (!customerOptional.isPresent()) {
            throw new IllegalStateException(String.format("Customer with ID %s not found.", customerId));
        }

        /* Check if Currency is supported */
        String paymentCurrencyCode = payment.getCurrency().getCurrencyCode();
        boolean isCurrencyAccepted = ACCEPTED_CURRENCIES.stream().anyMatch(c -> c.getCurrencyCode().equals(paymentCurrencyCode));

        if (!isCurrencyAccepted) {
            throw new IllegalStateException(String.format("Currency with CurrencyCode %s is not supported.", paymentCurrencyCode));
        }

        /* Charge Card */
        CardPaymentCharge cardPaymentCharge = cardPaymentCharger.chargeCard(payment.getSource(), payment.getAmount(), payment.getCurrency(), payment.getDescription());

        /* Check if Customer is debited */
        if (!cardPaymentCharge.isCardDebited()) {
            throw new IllegalStateException(String.format("Card not debited for Customer %s.", customerId));
        }

        /* Insert Payment to Customer and save Payment */
        payment.setCustomerId(customerId);
        paymentRepository.save(payment);
    }

    Payment searchPayment(Long paymentId) {
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (!optionalPayment.isPresent()) {
            throw new IllegalStateException(String.format("Payment with PaymentId %s not found.", paymentId));
        }
        return optionalPayment.get();
    }
}
