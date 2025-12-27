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
package com.arch.northstar.platform.logging.core.context;

/**
 * Centralized MDC (Mapped Diagnostic Context) keys used across
 * the Northstar logging platform.
 *
 * <p>
 * This class is part of the core module and must remain
 * framework-agnostic and backward-compatible.
 * </p>
 *
 * <p>
 * IMPORTANT:
 * - Do NOT rename existing keys (breaking change)
 * - New keys must be additive only
 * </p>
 */
public final class LoggingContextKeys {

    private LoggingContextKeys() {
        // Utility class
    }

    /** Correlation / request identifier */
    public static final String CORRELATION_ID = "correlationId";

    /** Tenant identifier (multi-tenancy) */
    public static final String TENANT_ID = "tenantId";

    /** User or principal identifier */
    public static final String USER_ID = "userId";

    /** Client application or channel (web, mobile, batch, api) */
    public static final String CHANNEL = "channel";

    /** Calling service name */
    public static final String SERVICE_NAME = "serviceName";

    /** Environment (dev, qa, uat, prod) */
    public static final String ENVIRONMENT = "environment";

    /** Trace ID (for distributed tracing correlation) */
    public static final String TRACE_ID = "traceId";

    /** Span ID (for distributed tracing correlation) */
    public static final String SPAN_ID = "spanId";
}
