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
package com.arch.northstar.platform.logging.audit.config;

import com.arch.northstar.platform.logging.audit.aspect.AuditAspect;
import com.arch.northstar.platform.logging.audit.properties.AuditProperties;
import com.arch.northstar.platform.logging.audit.publisher.AuditPublisher;
import com.arch.northstar.platform.logging.audit.publisher.CompositeAuditPublisher;
import com.arch.northstar.platform.logging.audit.publisher.LogAuditPublisher;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;

import java.util.List;

@AutoConfiguration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(AuditProperties.class)
@ConditionalOnProperty(
        prefix = "platform.audit",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class AuditAutoConfiguration {

    /**
     * Default publisher – ALWAYS created
     */
    @Bean
    public AuditPublisher logAuditPublisher() {
        return new LogAuditPublisher();
    }

    /**
     * Composite publisher – only if multiple publishers exist
     */
    @Bean
    @Primary
    @ConditionalOnBean(AuditPublisher.class)
    public AuditPublisher compositeAuditPublisher(
            List<AuditPublisher> publishers) {

        if (publishers.size() == 1) {
            return publishers.get(0); // avoid empty / recursive composite
        }
        return new CompositeAuditPublisher(publishers);
    }

    @Bean
    public AuditAspect auditAspect(AuditPublisher publisher) {
        return new AuditAspect(publisher);
    }
}
