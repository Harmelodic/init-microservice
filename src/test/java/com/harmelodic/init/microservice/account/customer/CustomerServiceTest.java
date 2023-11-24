package com.harmelodic.init.microservice.account.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    CustomerClient customerClient;

    @InjectMocks
    CustomerService customerService;

    @Test
    void fetchCustomerByIdSuccess() {
        UUID uuid = UUID.randomUUID();
        Customer customer = new Customer(uuid, "Matt", "Smith");
        when(customerClient.fetchCustomer(uuid)).thenReturn(customer);

        Customer actualCustomer = customerService.fetchCustomerById(uuid);

        assertEquals(customer, actualCustomer);
    }

    @Test
    void fetchCustomerByIdFailureNotFound() {
        UUID uuid = UUID.randomUUID();
        when(customerClient.fetchCustomer(uuid)).thenThrow(new RuntimeException("Customer not found."));

        assertThrows(RuntimeException.class, () -> customerService.fetchCustomerById(uuid));
    }

    @Test
    void fetchCustomerByIdFailureHttpException() {
        UUID uuid = UUID.randomUUID();
        when(customerClient.fetchCustomer(uuid))
                .thenThrow(new RestClientResponseException("Internal server error", 500, "Internal server error", null, null, null));

        assertThrows(RestClientException.class, () -> customerService.fetchCustomerById(uuid));
    }
}
