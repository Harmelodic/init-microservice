package com.harmelodic.init.microservice.account;

import java.util.UUID;

record Customer(UUID id,
                       String forename,
                       String surname) {
}
