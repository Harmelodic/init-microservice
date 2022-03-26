package com.harmelodic.init.microservice.account;

import java.util.UUID;

public record Account(
        UUID id,
        String name,
        UUID customerId) {
}
