package com.harmelodic.init.microservice.telemetry.instrumentation.tracing;

import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.springframework.stereotype.Component;

@Component
class ExampleTracing {

    // TODO: Tests
    @WithSpan
    void processSomething(@SpanAttribute String data) {
        // Do some processing, e.g.
        System.out.println(data);
    }
}
