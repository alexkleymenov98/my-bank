package com.example.transfer.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class TransferMetrics {
    private static final String METRIC_TRANSFER_FAILED = "bank.transfer.failed";

    private final MeterRegistry registry;
    private final ConcurrentMap<String, Counter> transferFailedCounters = new ConcurrentHashMap<>();

    public TransferMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    /** Счётчик неуспешных переводов с группировкой по логинам отправителя и получателя. */
    public void recordTransferFailed(String fromLogin, String toLogin) {
        String key = fromLogin + "\u001F" + toLogin;  // unit separator, безопасный разделитель
        transferFailedCounters
                .computeIfAbsent(key, k -> Counter.builder(METRIC_TRANSFER_FAILED)
                        .description("Number of failed transfer attempts grouped by sender and receiver logins")
                        .tag("from_login", fromLogin)
                        .tag("to_login", toLogin)
                        .register(registry))
                .increment();
    }
}
