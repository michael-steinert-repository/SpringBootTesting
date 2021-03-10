package com.example.testing.payment;

import com.example.testing.customer.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class PaymentIntegrationTest {
    //@Autowired
    //private PaymentRepository paymentRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldCreatePaymentSuccessfully() throws Exception {
        /* Given */
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer(customerId, "Michael", "1234");
        /* Perform a PutRequest to the CustomerController to register a Customer */
        ResultActions customerPutResultActions = mockMvc.perform(put("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(customer)));
        /* When */
        Long paymentId = 1L;
        Payment payment = new Payment(paymentId, customerId, new BigDecimal("42.00"), Currency.getInstance("EUR"), "card1234", "Donation");
        /* Perform a PostRequest to the PaymentController to make a Payment */
        ResultActions paymentPostResultActions = mockMvc.perform(post("/api/v1/payment/{customerId}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(payment)));
        /* Perform a GetRequest to the PaymentController to get a Payment with corresponding PaymentId */
        ResultActions paymentGetResultActions = mockMvc.perform(get("/api/v1/payment/{paymentId}", paymentId));
        /* Then */
        customerPutResultActions.andExpect(status().isOk());
        paymentPostResultActions.andExpect(status().isOk());
        /* Check if Payment is stored in Repository */
        paymentGetResultActions
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectToJson(payment)));
       /*
        MvcResult mvcResult = paymentGetResultActions.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).isEqualTo(objectToJson(payment));
        */
    }

    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail("Failed to convert Object into JSON");
            return null;
        }
    }
}
