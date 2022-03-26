package com.harmelodic.init.microservice.account.customer;

import java.util.UUID;

public record Customer(UUID id,
                       String forename,
                       String surname) {
}
