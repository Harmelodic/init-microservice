package com.harmelodic.init.microservice.telemetry.instrumentation.tracing;

import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.springframework.stereotype.Component;

@Component
public class ExampleTracing {

    @WithSpan
    void processSomething(@SpanAttribute String data) {
        // Do some processing, e.g.
        System.out.println(data);
    }
}
