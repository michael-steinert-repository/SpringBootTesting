package com.example.testing.customer;

import com.example.testing.utils.PhoneNumberValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PhoneNumberValidator phoneNumberValidator;

    public void saveNewCustomer(Customer customer) {
        String phoneNumber = customer.getPhoneNumber();
        if (!phoneNumberValidator.test(phoneNumber)) {
            throw new IllegalStateException(String.format("PhoneNumber %s is not valid.", phoneNumber));
        }
        Optional<Customer> customerOptional = customerRepository.findCustomerByPhoneNumber(phoneNumber);
        if (customerOptional.isPresent()) {
            Customer customerFromRepository = customerOptional.get();
            if (customerFromRepository.getName().equals(customer.getName())) {
                return;
            }
            throw new IllegalStateException(String.format("Phone Number %s is taken ", phoneNumber));
        }
        if (customer.getId() == null) {
            customer.setId(UUID.randomUUID());
        }
        customerRepository.save(customer);
    }
}
