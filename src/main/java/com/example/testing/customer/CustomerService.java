package com.example.testing.customer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public void saveNewCustomer(Customer customer) {
        String phoneNumber = customer.getPhoneNumber();
        Optional<Customer> customerOptional = customerRepository.findCustomerByPhoneNumber(phoneNumber);
        if (customerOptional.isPresent()) {
            Customer customerFromRepository = customerOptional.get();
            if (customerFromRepository.getName().equals(customer.getName())) {
                return;
            }
            throw new IllegalStateException(String.format("Phone Number %s is taken ", phoneNumber));
        }
        if(customer.getId() == null) {
            customer.setId(UUID.randomUUID());
        }
        customerRepository.save(customer);
    }
}
