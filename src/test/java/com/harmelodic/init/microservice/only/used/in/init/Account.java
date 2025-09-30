package com.harmelodic.init.microservice.only.used.in.init;

import java.util.UUID;

/// Account model class.
///
/// This is a different class from the one in `account` to mimic a real client's behaviour of having its own contained
/// model.
record Account(
        UUID id,
        String name,
        UUID customerId) {
}
