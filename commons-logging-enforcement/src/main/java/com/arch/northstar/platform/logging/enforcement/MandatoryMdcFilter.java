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
package com.arch.northstar.platform.logging.enforcement;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import com.arch.northstar.platform.logging.core.context.LoggingContextKeys;
import org.slf4j.MDC;
import org.slf4j.Marker;

/**
 * Logback TurboFilter that enforces the presence of mandatory MDC keys
 * before allowing a log event to be written.
 *
 * <p>
 * This filter is OPTIONAL and must be explicitly enabled by the
 * client application via logback-spring.xml.
 * </p>
 */
public class MandatoryMdcFilter extends TurboFilter {

    @Override
    public FilterReply decide(
            Marker marker,
            Logger logger,
            Level level,
            String format,
            Object[] params,
            Throwable t) {

        String tenant = MDC.get(LoggingContextKeys.TENANT_ID);
        String correlationId = MDC.get(LoggingContextKeys.CORRELATION_ID);

        if (tenant == null || correlationId == null) {
            // DENY or NEUTRAL depending on policy
            return FilterReply.DENY;
        }

        return FilterReply.NEUTRAL;
    }
}
