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
package com.arch.northstar.security.core.spi;

import com.arch.northstar.security.core.principal.NorthstarPrincipal;
import java.lang.reflect.Method;

/**
 * Authorization evaluation contract for the Northstar platform.
 *
 * This SPI is deliberately context-agnostic.
 * Runtime layers are responsible for extracting the principal
 * from the execution context before invoking this evaluator.
 */
public interface AuthorizationEvaluator {

    /**
     * Evaluates whether the principal is allowed to invoke the method.
     *
     * @param principal authenticated principal (never null)
     * @param method    method being invoked
     * @param arguments invocation arguments
     * @return true if allowed, false otherwise
     */
    boolean isAllowed(
            NorthstarPrincipal principal,
            Method method,
            Object[] arguments
    );
}