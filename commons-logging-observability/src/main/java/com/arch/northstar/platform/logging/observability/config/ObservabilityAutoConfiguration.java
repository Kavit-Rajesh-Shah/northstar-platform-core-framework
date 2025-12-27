/*
 * Copyright (c) 2025 Northstar Platform Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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