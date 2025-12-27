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
package com.arch.northstar.platform.logging.performance.annotation;

import java.lang.annotation.*;

/**
 * Marks a method for performance measurement.
 *
 * <p>
 * This annotation is processed by {@code MethodPerformanceAspect}
 * to measure execution time and emit performance logs/events.
 * </p>
 *
 * <p>
 * Usage is intentionally declarative and framework-agnostic.
 * </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Performance {

    /**
     * Logical operation name.
     * Example: CREATE_ORDER, GET_QUOTE, CALCULATE_PRICE
     */
    String operation();

    /**
     * Whether performance logging is enabled for this method.
     * Allows fine-grained opt-out even when the module is enabled.
     */
    boolean enabled() default true;

    /**
     * Optional threshold in milliseconds.
     * If set (> 0), logs only when execution time exceeds the threshold.
     */
    long thresholdMs() default 0L;
}
