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
package com.arch.northstar.security.autoconfigure.filter;

import com.arch.northstar.security.auth.spi.*;
import com.arch.northstar.security.context.*;
import com.arch.northstar.security.http.HttpAuthenticationRequestContextFactory;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class NorthstarAuthenticationFilter extends OncePerRequestFilter {

    private final List<NorthstarAuthenticationProvider> providers;

    public NorthstarAuthenticationFilter(
            List<NorthstarAuthenticationProvider> providers) {
        this.providers = providers;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        var authContext =
                HttpAuthenticationRequestContextFactory.from(request);

        for (NorthstarAuthenticationProvider provider : providers) {
            if (provider.supports(authContext)) {
                NorthstarSecurityContext securityContext =
                        provider.authenticate(authContext);
                NorthstarSecurityContextHolder.setContext(securityContext);
                break;
            }
        }

        filterChain.doFilter(request, response);
    }
}
