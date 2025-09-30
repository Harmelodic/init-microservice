package com.harmelodic.init.microservice.telemetry.instrumentation.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Component;

@Component
public class ExampleLogging {
    public static final Logger LOGGER = LoggerFactory.getLogger(ExampleLogging.class);

    void createLogs(String data) {
        LOGGER.info("Simple log");

        LOGGER.atInfo()
                .addKeyValue("dataKey", data)
                .addMarker(MarkerFactory.getMarker("SOME_MARKER"))
                .log("Simple fluent log");

        LOGGER.warn("Warning log");

        LOGGER.error("Warning log");

        LOGGER.debug("Debug log");

        LOGGER.trace("Trace log");
    }
}
