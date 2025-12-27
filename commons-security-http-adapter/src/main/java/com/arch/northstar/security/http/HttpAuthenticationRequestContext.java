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
package com.arch.northstar.security.http;

import com.arch.northstar.security.auth.spi.AuthenticationRequestContext;
import jakarta.servlet.http.HttpServletRequest;

import java.util.*;

public class HttpAuthenticationRequestContext
        implements AuthenticationRequestContext {

    private final HttpServletRequest request;

    public HttpAuthenticationRequestContext(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public Optional<String> getHeader(String name) {
        return Optional.ofNullable(request.getHeader(name));
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            headers.put(name, request.getHeader(name));
        }
        return headers;
    }

    @Override
    public Optional<String> getAttribute(String name) {
        Object value = request.getAttribute(name);
        return value == null ? Optional.empty() : Optional.of(value.toString());
    }
}
