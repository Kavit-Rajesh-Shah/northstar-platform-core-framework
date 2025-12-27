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
package com.arch.northstar.platform.logging.context.filter;
import com.arch.northstar.platform.logging.context.resolver.TenantResolver;
import com.arch.northstar.platform.logging.context.resolver.UserResolver;
import com.arch.northstar.platform.logging.core.context.LoggingContextKeys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.UUID;

public class LoggingContextFilter extends OncePerRequestFilter {

    private static final String CORRELATION_HEADER = "X-Correlation-Id";

    private final TenantResolver tenantResolver;
    private final UserResolver userResolver;

    public LoggingContextFilter(
            TenantResolver tenantResolver,
            UserResolver userResolver) {
        this.tenantResolver = tenantResolver;
        this.userResolver = userResolver;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws java.io.IOException, jakarta.servlet.ServletException {

        try {
            String tenant = tenantResolver.resolveTenant(request);
            String user = userResolver.resolveUser(request);

            // 🔑 Correlation ID (NEW)
            String correlationId =
                    request.getHeader(CORRELATION_HEADER);

            if (correlationId == null || correlationId.isBlank()) {
                correlationId = UUID.randomUUID().toString();
            }

            if (tenant != null) {
                MDC.put(LoggingContextKeys.TENANT_ID, tenant);
            }
            if (user != null) {
                MDC.put(LoggingContextKeys.USER_ID, user);
            }

            MDC.put(LoggingContextKeys.CORRELATION_ID, correlationId);

            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}