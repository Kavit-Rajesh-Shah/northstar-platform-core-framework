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
package com.arch.northstar.platform.logging.core.event;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a business audit event emitted by the platform.
 *
 * This class is framework-agnostic and must remain stable,
 * as it is shared across multiple platform modules.
 */
public final class AuditEvent {

    private final String action;
    private final String service;
    private final boolean success;
    private final Long executionTimeMs;
    private final Instant timestamp;
    private final Map<String, String> attributes;
    private final String tenantId;
    private final String userId;

    private AuditEvent(Builder builder) {
        this.action = builder.action;
        this.service = builder.service;
        this.success = builder.success;
        this.executionTimeMs = builder.executionTimeMs;
        this.timestamp = builder.timestamp != null
                ? builder.timestamp
                : Instant.now();
        this.attributes = builder.attributes != null
                ? Collections.unmodifiableMap(builder.attributes)
                : Collections.emptyMap();
        this.tenantId = builder.tenantId;
        this.userId = builder.userId;
    }

    public String action() {
        return action;
    }

    public String service() {
        return service;
    }

    public boolean success() {
        return success;
    }

    public Long executionTimeMs() {
        return executionTimeMs;
    }

    public Instant timestamp() {
        return timestamp;
    }

    public Map<String, String> attributes() {
        return attributes;
    }

    public String tenantId() {
        return tenantId;
    }

    public String userId() {
        return userId;
    }

    /* ---------- Builder ---------- */

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String action;
        private String service;
        private boolean success;
        private Long executionTimeMs;
        private Instant timestamp;
        private Map<String, String> attributes;
        private String tenantId;
        private String userId;

        private Builder() {
        }

        public Builder action(String action) {
            this.action = action;
            return this;
        }

        public Builder service(String service) {
            this.service = service;
            return this;
        }

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder executionTimeMs(Long executionTimeMs) {
            this.executionTimeMs = executionTimeMs;
            return this;
        }

        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder attributes(Map<String, String> attributes) {
            this.attributes = attributes;
            return this;
        }

        public AuditEvent build() {
            Objects.requireNonNull(action, "action must not be null");
            return new AuditEvent(this);
        }

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }
    }
}
