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
package com.arch.northstar.security.auth.jwt.decoder;

import com.arch.northstar.security.auth.jwt.config.JwtSecurityProperties;
import org.springframework.security.oauth2.jwt.*;

import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public final class JwtDecoderFactory {

    private JwtDecoderFactory() {}

    public static JwtDecoder create(JwtSecurityProperties props) {

        if (props.getJwkSetUri() != null) {
            return NimbusJwtDecoder.withJwkSetUri(props.getJwkSetUri()).build();
        }

        if (props.getPublicKey() != null) {
            return NimbusJwtDecoder
                    .withPublicKey(parsePublicKey(props.getPublicKey()))
                    .build();
        }

        if (props.getSecretKey() != null) {
            return NimbusJwtDecoder
                    .withSecretKey(new SecretKeySpec(
                            props.getSecretKey().getBytes(), "HmacSHA256"))
                    .build();
        }

        throw new IllegalStateException(
                "JWT validation requires secretKey, publicKey, or jwkSetUri");
    }

    private static RSAPublicKey parsePublicKey(String key) {
        try {
            String cleaned = key
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");
            byte[] decoded = Base64.getDecoder().decode(cleaned);
            return (RSAPublicKey) KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(decoded));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid public key", ex);
        }
    }
}
