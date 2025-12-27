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
package com.arch.northstar.platform.logging.performance.config;

import com.arch.northstar.platform.logging.performance.aspect.MethodPerformanceAspect;
import com.arch.northstar.platform.logging.performance.properties.PerformanceProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(PerformanceProperties.class)
@ConditionalOnProperty(
        prefix = "platform.performance",
        name = "enabled",
        havingValue = "true"
)
public class PerformanceAutoConfiguration {

    @Bean
    public MethodPerformanceAspect methodPerformanceAspect() {
        return new MethodPerformanceAspect();
    }
}
