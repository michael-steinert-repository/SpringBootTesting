package com.example.testing.customer;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping
public class CustomerController {
    @PutMapping
    public void registerNewCustomer(@RequestBody @Valid Customer customer) {

    }
}
