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
package com.arch.northstar.security.auth.jwt.autoconfig;

import com.arch.northstar.security.auth.jwt.blacklist.*;
import com.arch.northstar.security.auth.jwt.config.JwtSecurityProperties;
import com.arch.northstar.security.auth.jwt.decoder.JwtDecoderFactory;
import com.arch.northstar.security.auth.jwt.provider.JwtAuthenticationProvider;
import com.arch.northstar.security.auth.spi.NorthstarAuthenticationProvider;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@AutoConfiguration
@EnableConfigurationProperties(JwtSecurityProperties.class)
@ConditionalOnProperty(
        prefix = "platform.security.jwt",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true)
public class JwtSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    JwtDecoder jwtDecoder(JwtSecurityProperties props) {
        return JwtDecoderFactory.create(props);
    }

    @Bean
    @ConditionalOnMissingBean
    JwtBlacklistService jwtBlacklistService() {
        return new NoOpJwtBlacklistService();
    }

    @Bean
    NorthstarAuthenticationProvider jwtAuthenticationProvider(
            JwtDecoder decoder,
            JwtBlacklistService blacklistService,
            JwtSecurityProperties props) {

        return new JwtAuthenticationProvider(decoder, blacklistService, props);
    }
}
