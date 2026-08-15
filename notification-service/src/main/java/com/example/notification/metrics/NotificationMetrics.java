package com.example.notification.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class NotificationMetrics {

    private static final String METRIC_NOTIFICATION_FAILED = "bank.notification.failed";

    private final MeterRegistry registry;
    private final ConcurrentMap<String, Counter> failedCounters = new ConcurrentHashMap<>();

    public NotificationMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    /** Счётчик невозможностей отправки уведомления с группировкой по логину. */
    public void recordFailed(String login) {
        String safeLogin = (login == null || login.isBlank()) ? "unknown" : login;
        failedCounters
                .computeIfAbsent(safeLogin, l -> Counter.builder(METRIC_NOTIFICATION_FAILED)
                        .description("Number of failed notification deliveries grouped by user login")
                        .tag("login", l)
                        .register(registry))
                .increment();
    }
}
