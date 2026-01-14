/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.server.auth;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator; // Fixed import
import com.nimbusds.jose.proc.*;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor; // Explicit import
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OIDCProviderTest {

    private static RSAKey rsaKey;
    private static ConfigurableJWTProcessor<SecurityContext> testProcessor;

    @BeforeAll
    static void setup() throws Exception {
        // Generate a 2048-bit RSA key pair in JWK format
        rsaKey = new RSAKeyGenerator(2048).keyID("123").generate();

        // Create a JWTProcessor that trusts this key
        testProcessor = new DefaultJWTProcessor<>();
        JWKSet jwkSet = new JWKSet(rsaKey);

        // Configure key selector to use our local keys
        JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(
                JWSAlgorithm.RS256,
                (selector, context) -> selector.select(jwkSet) // Simple selector returning our key
        );
        testProcessor.setJWSKeySelector(keySelector);

        // Inject this processor for all providers we test
        OIDCProvider.setJwtProcessorForTest("google", testProcessor);
        OIDCProvider.setJwtProcessorForTest("keycloak", testProcessor);
        OIDCProvider.setJwtProcessorForTest("okta", testProcessor);
    }

    @Test
    void testValidateToken_GoogleAdmin() throws Exception {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject("123")
                .claim("email", "user@admin.com")
                .issuer("https://accounts.google.com")
                .expirationTime(Date.from(Instant.now().plusSeconds(3600)))
                .build();

        String token = createSignedToken(claims);

        OIDCProvider.TokenInfo info = OIDCProvider.validateToken("google", token);

        assertNotNull(info);
        assertEquals("user@admin.com", info.email());
        assertEquals(Roles.ADMIN, info.role());
        assertTrue(info.isAdmin());
    }

    @Test
    void testValidateToken_GoogleScientist() throws Exception {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject("456")
                .claim("email", "user@example.com")
                .issuer("https://accounts.google.com")
                .expirationTime(Date.from(Instant.now().plusSeconds(3600)))
                .build();

        String token = createSignedToken(claims);

        OIDCProvider.TokenInfo info = OIDCProvider.validateToken("google", token);

        assertNotNull(info);
        assertEquals("user@example.com", info.email());
        assertEquals(Roles.SCIENTIST, info.role());
        assertFalse(info.isAdmin());
    }

    @Test
    void testValidateToken_Expired() throws Exception {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject("123")
                .issuer("https://accounts.google.com")
                .expirationTime(Date.from(Instant.now().minusSeconds(3600))) // Expired
                .build();

        String token = createSignedToken(claims);

        // Nimbus processor throws BadJOSEException on expiration
        OIDCProvider.TokenInfo info = OIDCProvider.validateToken("google", token);

        assertNull(info, "Expired token should return null");
    }

    @Test
    void testValidateToken_KeycloakRole() throws Exception {
        // Keycloak uses realm_access.roles
        Map<String, Object> realmAccess = Map.of("roles", List.of("SCIENTIST"));

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject("789")
                .issuer("https://sso.example.com/realms/jscience")
                .claim("realm_access", realmAccess)
                .expirationTime(Date.from(Instant.now().plusSeconds(3600)))
                .build();

        String token = createSignedToken(claims);

        OIDCProvider.TokenInfo info = OIDCProvider.validateToken("keycloak", token);

        assertNotNull(info);
        assertEquals(Roles.SCIENTIST, info.role());
    }

    @Test
    void testValidateToken_Okta() throws Exception {
        // Okta uses groups
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject("111")
                .issuer("https://dev-123.okta.com")
                .claim("groups", List.of("scientist-group"))
                .expirationTime(Date.from(Instant.now().plusSeconds(3600)))
                .build();

        String token = createSignedToken(claims);

        OIDCProvider.TokenInfo info = OIDCProvider.validateToken("okta", token);

        assertNotNull(info);
        assertEquals(Roles.SCIENTIST, info.role());
    }

    private String createSignedToken(JWTClaimsSet claims) throws Exception {
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaKey.getKeyID()).build(),
                claims);
        signedJWT.sign(new RSASSASigner(rsaKey));
        return signedJWT.serialize();
    }
}
