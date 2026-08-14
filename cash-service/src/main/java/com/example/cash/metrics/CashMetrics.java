package com.example.cash.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class CashMetrics {
    private static final String METRIC_WITHDRAW_FAILED = "bank.cash.withdraw.failed";

    private final MeterRegistry registry;
    private final ConcurrentMap<String, Counter> withdrawFailedCounters = new ConcurrentHashMap<>();

    public CashMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    /** Счётчик неуспешных попыток снятия денег с группировкой по логину. */
    public void recordWithdrawFailed(String login) {
        withdrawFailedCounters
                .computeIfAbsent(login, l -> Counter.builder(METRIC_WITHDRAW_FAILED)
                        .description("Number of failed cash withdrawal attempts grouped by user login")
                        .tag("login", l)
                        .register(registry))
                .increment();
    }
}
