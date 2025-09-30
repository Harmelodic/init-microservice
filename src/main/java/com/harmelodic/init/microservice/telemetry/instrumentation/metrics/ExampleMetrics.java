package com.harmelodic.init.microservice.telemetry.instrumentation.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class ExampleMetrics {

    private final Counter counter;
    // TODO: Gauge
    // TODO: Histogram
    // TODO: Summary

    public ExampleMetrics(MeterRegistry meterRegistry) {
        this.counter = Counter.builder("example_counter")
                .tag("tagKey", "tagValue")
                .register(meterRegistry);
    }

    void incrementCounter() {
        counter.increment();
    }
}
