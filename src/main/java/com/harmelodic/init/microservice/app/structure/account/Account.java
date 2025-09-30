package com.harmelodic.init.microservice.app.structure.account;

import java.util.UUID;

record Account(UUID id,
               String name,
               UUID customerId) {
}
