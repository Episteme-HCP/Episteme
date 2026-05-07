/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.server.server.auth;

import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for OIDCProvider validation and role mapping.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
class OIDCProviderTest {

    private ConfigurableJWTProcessor<SecurityContext> mockProcessor;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        mockProcessor = Mockito.mock(ConfigurableJWTProcessor.class);
    }

    @Test
    void testValidateToken_GoogleAdmin() throws Exception {
        // Given
        String provider = "google";
        String token = "mock-google-token";
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject("google-user-123")
                .claim("email", "admin@admin.com")
                .build();
        
        when(mockProcessor.process(any(String.class), any())).thenReturn(claims);
        OIDCProvider.setJwtProcessorForTest(provider, mockProcessor);

        // When
        OIDCProvider.TokenInfo info = OIDCProvider.validateToken(provider, token);

        // Then
        assertNotNull(info);
        assertEquals("google-user-123", info.sub());
        assertEquals("admin@admin.com", info.email());
        assertEquals(Roles.ADMIN, info.role());
        assertTrue(info.isAdmin());
    }

    @Test
    void testValidateToken_GoogleScientist() throws Exception {
        // Given
        String provider = "google";
        String token = "mock-google-token";
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject("google-user-456")
                .claim("email", "user@gmail.com")
                .build();
        
        when(mockProcessor.process(any(String.class), any())).thenReturn(claims);
        OIDCProvider.setJwtProcessorForTest(provider, mockProcessor);

        // When
        OIDCProvider.TokenInfo info = OIDCProvider.validateToken(provider, token);

        // Then
        assertNotNull(info);
        assertEquals(Roles.SCIENTIST, info.role());
        assertFalse(info.isAdmin());
    }

    @Test
    void testValidateToken_KeycloakScientist() throws Exception {
        // Given
        String provider = "keycloak";
        String token = "mock-keycloak-token";
        
        Map<String, Object> realmAccess = new HashMap<>();
        realmAccess.put("roles", Collections.singletonList("SCIENTIST"));
        
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject("kc-user-1")
                .claim("realm_access", realmAccess)
                .build();
        
        when(mockProcessor.process(any(String.class), any())).thenReturn(claims);
        OIDCProvider.setJwtProcessorForTest(provider, mockProcessor);

        // When
        OIDCProvider.TokenInfo info = OIDCProvider.validateToken(provider, token);

        // Then
        assertNotNull(info);
        assertEquals(Roles.SCIENTIST, info.role());
    }

    @Test
    void testValidateToken_OktaScientist() throws Exception {
        // Given
        String provider = "okta";
        String token = "mock-okta-token";
        
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject("okta-user-1")
                .claim("groups", Collections.singletonList("scientist-group"))
                .build();
        
        when(mockProcessor.process(any(String.class), any())).thenReturn(claims);
        OIDCProvider.setJwtProcessorForTest(provider, mockProcessor);

        // When
        OIDCProvider.TokenInfo info = OIDCProvider.validateToken(provider, token);

        // Then
        assertNotNull(info);
        assertEquals(Roles.SCIENTIST, info.role());
    }

    @Test
    void testValidateToken_InvalidToken() throws Exception {
        // Given
        String provider = "google";
        String token = "invalid-token";
        
        when(mockProcessor.process(any(String.class), any())).thenThrow(new com.nimbusds.jose.JOSEException("Invalid signature"));
        OIDCProvider.setJwtProcessorForTest(provider, mockProcessor);

        // When
        OIDCProvider.TokenInfo info = OIDCProvider.validateToken(provider, token);

        // Then
        assertNull(info);
    }

    @Test
    void testValidateToken_EmptyToken() {
        // When
        OIDCProvider.TokenInfo info = OIDCProvider.validateToken("google", "");

        // Then
        assertNull(info);
    }
}
