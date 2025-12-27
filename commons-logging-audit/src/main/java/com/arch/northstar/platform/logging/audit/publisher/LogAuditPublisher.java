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
package com.arch.northstar.platform.logging.audit.publisher;

import com.arch.northstar.platform.logging.core.event.AuditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogAuditPublisher implements AuditPublisher {

    private static final Logger log =
            LoggerFactory.getLogger(LogAuditPublisher.class);

    @Override
    public void publish(AuditEvent event) {
        log.info(
                "AUDIT | tenant={} userId={} action={} service={} success={} durationMs={}",
                event.tenantId(),
                event.userId(),
                event.action(),
                event.service(),
                event.success(),
                event.executionTimeMs()
        );
    }
}
