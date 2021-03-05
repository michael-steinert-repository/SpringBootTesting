package com.example.testing.customer;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends CrudRepository<Customer, UUID> {
    Optional<Customer> findCustomerByPhoneNumber(String phoneNumber);
}
