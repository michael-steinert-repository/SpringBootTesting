package com.example.testing.customer;

import com.example.testing.utils.PhoneNumberValidator;
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
    @Mock
    private PhoneNumberValidator phoneNumberValidator;
    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        customerService = new CustomerService(customerRepository, phoneNumberValidator);
    }

    @Test
    void itShouldSaveNewCustomer() {
        /* Given */
        String phoneNumber = "1234";
        Customer customer = new Customer(UUID.randomUUID(), "Michael", phoneNumber);
        /* Mocking the Return if Method save() is called */
        given(customerRepository.findCustomerByPhoneNumber(customer.getPhoneNumber())).willReturn(Optional.empty());
        /* Mocking the Return if Method test() is called */
        given(phoneNumberValidator.test(phoneNumber)).willReturn(true);
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
    void itShouldNotSaveNewCustomerWhenPhoneNumberIsInvalid() {
        /* Given */
        String phoneNumber = "1234";
        Customer customer = new Customer(UUID.randomUUID(), "Michael", phoneNumber);
        /* Mocking the Return if Method test() is called */
        given(phoneNumberValidator.test(phoneNumber)).willReturn(false);
        /* When */
        /* Then */
        assertThatThrownBy(() ->  customerService.saveNewCustomer(customer))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("PhoneNumber %s is not valid.", phoneNumber));
        then(customerRepository).shouldHaveNoInteractions();
    }

    @Test
    void itShouldSaveNewCustomerWhenIdIsNull() {
        /* Given */
        String phoneNumber = "1234";
        Customer customer = new Customer(null, "Michael", phoneNumber);
        /* Mocking the Return if Method findCustomerByPhoneNumber() is called */
        given(customerRepository.findCustomerByPhoneNumber(customer.getPhoneNumber())).willReturn(Optional.empty());
        /* Mocking the Return if Method test() is called */
        given(phoneNumberValidator.test(phoneNumber)).willReturn(true);
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
        String phoneNumber = "1234";
        Customer customer = new Customer(UUID.randomUUID(), "Michael", phoneNumber);
        /* Mocking the Return if Method findCustomerByPhoneNumber() is called */
        given(customerRepository.findCustomerByPhoneNumber(customer.getPhoneNumber())).willReturn(Optional.of(customer));
        /* Mocking the Return if Method test() is called */
        given(phoneNumberValidator.test(phoneNumber)).willReturn(true);
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
        String phoneNumber = "1234";
        Customer customer1 = new Customer(UUID.randomUUID(), "Michael", phoneNumber);
        Customer customer2 = new Customer(UUID.randomUUID(), "Marie", phoneNumber);
        /* Mocking the Return if Method findCustomerByPhoneNumber() is called */
        given(customerRepository.findCustomerByPhoneNumber(customer2.getPhoneNumber())).willReturn(Optional.of(customer2));
        /* Mocking the Return if Method test() is called */
        given(phoneNumberValidator.test(phoneNumber)).willReturn(true);
        /* When */
        /* Then */
        assertThatThrownBy(()-> customerService.saveNewCustomer(customer1))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("Phone Number %s is taken ", phoneNumber));
        then(customerRepository).should(never()).save(any(Customer.class));
    }
}