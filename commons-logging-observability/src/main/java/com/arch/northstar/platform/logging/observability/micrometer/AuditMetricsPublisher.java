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
package com.arch.northstar.platform.logging.observability.micrometer;

import com.arch.northstar.platform.logging.core.event.AuditEvent;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.util.concurrent.TimeUnit;

public class AuditMetricsPublisher {

    private final MeterRegistry registry;

    public AuditMetricsPublisher(MeterRegistry registry) {
        this.registry = registry;
    }

    public void publish(AuditEvent event) {

        if (event.executionTimeMs() == null) {
            return;
        }

        Timer.builder("platform.audit.execution")
                .tag("action", event.action())
                .tag("service", event.service())
                .tag("success", String.valueOf(event.success()))
                .register(registry)
                .record(event.executionTimeMs(), TimeUnit.MILLISECONDS);
    }
}
