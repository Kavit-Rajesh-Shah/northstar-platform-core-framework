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
package com.arch.northstar.platform.logging.context.initializer;

import com.arch.northstar.platform.logging.core.context.LoggingContextKeys;
import org.slf4j.MDC;

public class LoggingContextInitializer {

    public void initialize(String tenant, String user) {
        if (tenant != null) {
            MDC.put(LoggingContextKeys.TENANT_ID, tenant);
        }
        if (user != null) {
            MDC.put(LoggingContextKeys.USER_ID, user);
        }
    }

    public void clear() {
        MDC.clear();
    }
}
