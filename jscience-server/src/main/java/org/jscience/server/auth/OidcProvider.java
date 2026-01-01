/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

/**
 * Mock OIDC Provider for Enterprise Authentication.
 * 
 * In a real implementation, this would use a library like 'google-api-client'
 * or nimbus-jose-jwt to validate tokens from Google, Keycloak, or Okta.
 */
public class OidcProvider {

    public static TokenInfo validateToken(String provider, String token) {
        // Mock Validation Logic
        if (token == null || token.isEmpty()) {
            return null;
        }

        // Simulate Google ID Token validation
        if ("google".equalsIgnoreCase(provider)) {
            if (token.startsWith("valid-google-token-")) {
                // Extract "user" from mock token "valid-google-token-username"
                String username = token.replace("valid-google-token-", "");
                return new TokenInfo(username, "researcher@gmail.com", "RESEARCHER");
            }
        }

        // Simulate Keycloak Token validation
        if ("keycloak".equalsIgnoreCase(provider)) {
            if (token.startsWith("valid-keycloak-token-")) {
                String username = token.replace("valid-keycloak-token-", "");
                return new TokenInfo(username, username + "@lab.org", "ADMIN"); // Keycloak users are admins for demo
            }
        }

        return null;
    }

    public record TokenInfo(String sub, String email, String role) {
    }
}


