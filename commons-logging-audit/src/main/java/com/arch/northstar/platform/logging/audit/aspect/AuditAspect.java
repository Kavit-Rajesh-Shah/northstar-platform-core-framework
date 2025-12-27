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
package com.arch.northstar.platform.logging.audit.aspect;

import com.arch.northstar.platform.logging.audit.annotation.Audit;
import com.arch.northstar.platform.logging.audit.publisher.AuditPublisher;
import com.arch.northstar.platform.logging.core.context.LoggingContextKeys;
import com.arch.northstar.platform.logging.core.event.AuditEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;

@Aspect
public class AuditAspect {

    private final AuditPublisher publisher;

    public AuditAspect(AuditPublisher publisher) {
        this.publisher = publisher;
    }

    @Around("@annotation(audit)")
    public Object auditMethod(
            ProceedingJoinPoint pjp,
            Audit audit) throws Throwable {

        long start = System.nanoTime();
        boolean success = true;

        try {
            return pjp.proceed();
        } catch (Throwable ex) {
            success = false;
            throw ex;
        } finally {
            long durationMs =
                    (System.nanoTime() - start) / 1_000_000;

            String tenantId = MDC.get(LoggingContextKeys.TENANT_ID);
            String userId = MDC.get(LoggingContextKeys.USER_ID);

            AuditEvent event = AuditEvent.builder()
                    .action(audit.action())
                    .service(audit.service())
                    .success(success)
                    .executionTimeMs(durationMs)
                    .tenantId(tenantId)
                    .userId(userId)
                    .build();
            publisher.publish(event);
        }
    }
}