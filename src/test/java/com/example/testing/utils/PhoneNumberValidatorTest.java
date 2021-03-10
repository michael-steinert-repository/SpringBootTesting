package com.example.testing.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class PhoneNumberValidatorTest {

    private PhoneNumberValidator phoneNumberValidator;

    @BeforeEach
    void setUp() {
        phoneNumberValidator = new PhoneNumberValidator();
    }

    @CsvSource({"1234, true", "123, false", "12345, true"})
    @ParameterizedTest
    void itShouldValidatePhoneNumber(String phoneNumber, boolean expectedResult) {
        /* Given */
        /* When */
        boolean isValid = phoneNumberValidator.test(phoneNumber);
        /* Then */
        assertThat(isValid).isEqualTo(expectedResult);
    }

    @DisplayName("Should fail when PhoneNumber is less than 4")
    @Test
    void itShouldValidatePhoneNumberWhenIncorrect() {
        /* Given */
        String phoneNumber = "123";
        /* When */
        boolean isValid = phoneNumberValidator.test(phoneNumber);
        /* Then */
        assertThat(isValid).isFalse();
    }
}
