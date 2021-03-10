package com.example.testing.payment;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{customerId}")
    public void makePayment(@PathVariable("customerId") UUID customerId, @RequestBody Payment payment){
        paymentService.chargeCard(customerId, payment);
    }

    @GetMapping("/{paymentId}")
    public Payment searchPayment(@PathVariable("paymentId") Long paymentId) {
        return paymentService.searchPayment(paymentId);
    }
}
