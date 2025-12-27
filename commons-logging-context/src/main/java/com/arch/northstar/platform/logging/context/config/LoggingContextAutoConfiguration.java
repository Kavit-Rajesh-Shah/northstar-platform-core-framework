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
package com.arch.northstar.platform.logging.context.config;

import com.arch.northstar.platform.logging.context.filter.LoggingContextFilter;
import com.arch.northstar.platform.logging.context.initializer.LoggingContextInitializer;
import com.arch.northstar.platform.logging.context.resolver.DefaultUserResolver;
import com.arch.northstar.platform.logging.context.resolver.HeaderTenantResolver;
import com.arch.northstar.platform.logging.context.resolver.TenantResolver;
import com.arch.northstar.platform.logging.context.resolver.UserResolver;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

@AutoConfiguration
public class LoggingContextAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TenantResolver tenantResolver() {
        return new HeaderTenantResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public UserResolver userResolver() {
        return new DefaultUserResolver();
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public FilterRegistrationBean<LoggingContextFilter> loggingContextFilter(
            TenantResolver tenantResolver,
            UserResolver userResolver) {

        LoggingContextFilter filter =
                new LoggingContextFilter(tenantResolver, userResolver);

        FilterRegistrationBean<LoggingContextFilter> registration =
                new FilterRegistrationBean<>();

        registration.setFilter(filter);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.addUrlPatterns("/*");

        return registration;
    }

    @Bean
    @ConditionalOnMissingBean
    public LoggingContextInitializer loggingContextInitializer() {
        return new LoggingContextInitializer();
    }
}
