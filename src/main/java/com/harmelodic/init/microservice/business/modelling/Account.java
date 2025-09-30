package com.harmelodic.init.microservice.business.modelling;

import java.util.UUID;

record Account(UUID id,
               String name,
               UUID customerId) {
    // TODO: More advanced modelling, with docs and decisions, attempting to showcase some DDD bits.
}
