package com.example.testing.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public void registerNewCustomer(Customer customer) {
        customerRepository.save(customer);
    }
}
