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
package com.arch.northstar.platform.logging.performance.aspect;

import com.arch.northstar.platform.logging.core.context.LoggingContextKeys;
import com.arch.northstar.platform.logging.performance.annotation.Performance;
import com.arch.northstar.platform.logging.performance.properties.PerformanceProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@Aspect
public class MethodPerformanceAspect {

    private static final Logger log =
            LoggerFactory.getLogger(MethodPerformanceAspect.class);

    @Around("@annotation(performance)")
    public Object measure(
            ProceedingJoinPoint pjp,
            Performance performance) throws Throwable {

        if (!performance.enabled()) {
            return pjp.proceed();
        }

        long startNs = System.nanoTime();
        Object result = null;
        boolean success = true;

        try {
            result = pjp.proceed();
            return result;
        } catch (Throwable ex) {
            success = false;
            throw ex;
        } finally {
            long durationMs =
                    (System.nanoTime() - startNs) / 1_000_000;

            long threshold = performance.thresholdMs();

            boolean shouldLog =
                    threshold <= 0 || durationMs >= threshold;

            if (shouldLog) {
                String tenant =
                        MDC.get(LoggingContextKeys.TENANT_ID);
                String user =
                        MDC.get(LoggingContextKeys.USER_ID);

                log.info(
                        "PERF | tenant={} user={} operation={} durationMs={} success={}",
                        tenant,
                        user,
                        performance.operation(),
                        durationMs,
                        success
                );
            }
        }
    }
}