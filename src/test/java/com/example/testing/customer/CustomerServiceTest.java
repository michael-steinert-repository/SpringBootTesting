package com.example.testing.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        customerService = new CustomerService(customerRepository);
    }

    @Test
    void itShouldSaveNewCustomer() {
        /* Given */
        Customer customer = new Customer(UUID.randomUUID(), "Michael", "1234");
        /* Mocking the Return if Method findCustomerByPhoneNumber() is called */
        given(customerRepository.findCustomerByPhoneNumber(customer.getPhoneNumber())).willReturn(Optional.empty());
        /* When */
        customerService.saveNewCustomer(customer);
        /* Then */
        /* The Mock invoked the Method save() and captured the Save which would be passed to Repository */
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        /* Asserted captured Data from Mock against the original Data from customer */
        assertThat(customerArgumentCaptorValue).isEqualTo(customer);
    }

    @Test
    void itShouldSaveNewCustomerWhennIdIsNull() {
        /* Given */
        Customer customer = new Customer(null, "Michael", "1234");
        /* Mocking the Return if Method findCustomerByPhoneNumber() is called */
        given(customerRepository.findCustomerByPhoneNumber(customer.getPhoneNumber())).willReturn(Optional.empty());
        /* When */
        customerService.saveNewCustomer(customer);
        /* Then */
        /* The Mock invoked the Method save() and captured the Save which would be passed to Repository */
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        /* Asserted captured Data from Mock against the original Data from customer */
        assertThat(customerArgumentCaptorValue).isEqualToIgnoringGivenFields(customer, "id");
        assertThat(customerArgumentCaptorValue.getId()).isNotNull();
    }

    @Test
    void itShouldNotSaveCustomerWhenCustomerExists() {
        /* Given */
        Customer customer = new Customer(UUID.randomUUID(), "Michael", "1234");
        /* Mocking the Return if Method findCustomerByPhoneNumber() is called */
        given(customerRepository.findCustomerByPhoneNumber(customer.getPhoneNumber())).willReturn(Optional.of(customer));
        /* When */
        customerService.saveNewCustomer(customer);
        /* Then */
        then(customerRepository).should(never()).save(any(Customer.class));
        then(customerRepository).should().findCustomerByPhoneNumber(customer.getPhoneNumber());
        then(customerRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void itShouldThrowAnExceptionWhenPhoneNumberIsTaken() {
        /* Given */
        Customer customer1 = new Customer(UUID.randomUUID(), "Michael", "1234");
        Customer customer2 = new Customer(UUID.randomUUID(), "Marie", "1234");
        /* Mocking the Return if Method findCustomerByPhoneNumber() is called */
        given(customerRepository.findCustomerByPhoneNumber(customer2.getPhoneNumber())).willReturn(Optional.of(customer2));
        /* When */
        /* Then */
        assertThatThrownBy(()-> customerService.saveNewCustomer(customer1))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("Phone Number %s is taken ", customer1.getPhoneNumber()));
        then(customerRepository).should(never()).save(any(Customer.class));
    }
}