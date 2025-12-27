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
package com.arch.northstar.security.auth.jwt.provider;

import com.arch.northstar.security.auth.jwt.blacklist.JwtBlacklistService;
import com.arch.northstar.security.auth.jwt.config.JwtSecurityProperties;
import com.arch.northstar.security.auth.spi.*;
import com.arch.northstar.security.context.NorthstarSecurityContext;
import com.arch.northstar.security.core.auth.AuthenticationMechanism;
import com.arch.northstar.security.core.principal.NorthstarPrincipal;
import org.springframework.security.oauth2.jwt.*;

import java.util.Set;

public class JwtAuthenticationProvider
        implements NorthstarAuthenticationProvider {

    private final JwtDecoder jwtDecoder;
    private final JwtBlacklistService blacklistService;
    private final JwtSecurityProperties props;

    public JwtAuthenticationProvider(
            JwtDecoder jwtDecoder,
            JwtBlacklistService blacklistService,
            JwtSecurityProperties props) {
        this.jwtDecoder = jwtDecoder;
        this.blacklistService = blacklistService;
        this.props = props;
    }

    @Override
    public boolean supports(AuthenticationRequestContext ctx) {
        return ctx.getHeader("Authorization")
                .map(h -> h.startsWith("Bearer "))
                .orElse(false);
    }

    @Override
    public NorthstarSecurityContext authenticate(
            AuthenticationRequestContext ctx) {

        String token = ctx.getHeader("Authorization")
                .get()
                .substring(7);

        Jwt jwt = jwtDecoder.decode(token);

        if (props.isBlacklistEnabled()
                && jwt.getId() != null
                && blacklistService.isBlacklisted(jwt.getId())) {
            throw new AuthenticationFailureException("JWT is blacklisted");
        }

        NorthstarPrincipal principal =
                new NorthstarPrincipal(
                        jwt.getSubject(),
                        jwt.getClaimAsString(props.getTenantClaim()),
                        Set.copyOf(jwt.getClaimAsStringList(props.getRolesClaim())),
                        Set.copyOf(jwt.getClaimAsStringList(props.getPermissionsClaim())),
                        AuthenticationMechanism.JWT
                );

        return new NorthstarSecurityContext(principal);
    }
}
