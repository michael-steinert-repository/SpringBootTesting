package com.example.testing.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/* To trigger the Annotations from Customer the following Property is necessary */
/* For Example @Column(nullable = false) */
@DataJpaTest(properties = "spring.jpa.properties.javax.persistence.validation.mode=none")
class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void itShouldFindCustomerByPhoneNumber() {
        /* Given */
        UUID uuid = UUID.randomUUID();
        Customer customer = new Customer(uuid, "Michael", "1234");
        /* When */
        customerRepository.save(customer);
        /* Then */
        Optional<Customer> customerFromRepository = customerRepository.findCustomerByPhoneNumber(customer.getPhoneNumber());
        assertThat(customerFromRepository).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(customer.getId());
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
        });
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

    @Test
    void itShouldNotSaveCustomerWhenNameIsNull() {
        /* Given */
        UUID uuid = UUID.randomUUID();
        Customer customer = new Customer(uuid, null, "1234");
        /* When */
        /* Then */
        assertThatThrownBy(() -> customerRepository.save(customer)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void itShouldNotSaveCustomerWhenPhoneNumberIsNull() {
        /* Given */
        UUID uuid = UUID.randomUUID();
        Customer customer = new Customer(uuid, "Michael", null);
        /* When */
        /* Then */
        assertThatThrownBy(() -> customerRepository.save(customer)).isInstanceOf(DataIntegrityViolationException.class);
    }
}