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
package com.arch.northstar.platform.logging.observability.otel;

import io.opentelemetry.api.trace.Span;

public class OpenTelemetryBridge {

    public void enrichSpan(
            String action,
            boolean success,
            Long durationMs) {

        Span span = Span.current();
        if (!span.getSpanContext().isValid()) {
            return;
        }

        span.setAttribute("platform.audit.action", action);
        span.setAttribute("platform.audit.success", success);

        if (durationMs != null) {
            span.setAttribute("platform.audit.duration_ms", durationMs);
        }
    }
}
