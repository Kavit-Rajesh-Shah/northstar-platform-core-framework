package com.arch.northstar.platform.logging.observability.config;

import com.arch.northstar.platform.logging.observability.micrometer.AuditMetricsPublisher;
import com.arch.northstar.platform.logging.observability.otel.OpenTelemetryBridge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnProperty(
        prefix = "platform.observability",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = false
)
public class ObservabilityAutoConfiguration {

    @Bean
    @ConditionalOnClass(name = "io.micrometer.core.instrument.MeterRegistry")
    @ConditionalOnMissingBean
    public AuditMetricsPublisher auditMetricsPublisher(
            MeterRegistry meterRegistry) {

        return new AuditMetricsPublisher(meterRegistry);
    }

    @Bean
    @ConditionalOnClass(name = "io.opentelemetry.api.trace.Span")
    @ConditionalOnMissingBean
    public OpenTelemetryBridge openTelemetryBridge() {
        return new OpenTelemetryBridge();
    }
}