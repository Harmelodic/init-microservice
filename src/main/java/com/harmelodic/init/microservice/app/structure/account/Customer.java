package com.harmelodic.init.microservice.app.structure.account;

import java.util.UUID;

record Customer(UUID id,
                String forename,
                String surname) {
}
