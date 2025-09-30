package com.harmelodic.init.microservice.account;

import java.util.UUID;

record Account(
        UUID id,
        String name,
        UUID customerId) {
}
