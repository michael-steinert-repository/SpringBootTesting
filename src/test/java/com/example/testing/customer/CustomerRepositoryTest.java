package com.example.testing.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void itShouldFindCustomerByPhoneNumber() {
        /* Given */
        /* When */
        /* Then */
    }

    @Test
    void itShouldSaveCustomer() {
        /* Given */
        UUID uuid = UUID.randomUUID();
        Customer customer = new Customer(uuid, "Michael", "1234");
        /* When */
        customerRepository.save(customer);
        /* Then */
        Optional<Customer> customerFromRepository = customerRepository.findById(uuid);
        assertThat(customerFromRepository).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(customer.getId());
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
        });
    }
}