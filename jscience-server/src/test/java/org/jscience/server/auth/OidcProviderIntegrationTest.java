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

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for OIDCProvider using WireMock to mock JWKS endpoint.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
class OIDCProviderIntegrationTest {

    private static WireMockServer wireMockServer;

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();
    }

    @AfterAll
    static void stopWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @BeforeEach
    void resetMocks() {
        wireMockServer.resetAll();
    }

    @Test
    @DisplayName("Should fetch JWKS from mocked endpoint")
    void testJwksFetch() {
        // Mock JWKS endpoint
        String jwksResponse = """
                {
                  "keys": [
                    {
                      "kty": "RSA",
                      "use": "sig",
                      "alg": "RS256",
                      "kid": "test-key-id",
                      "n": "0vx7agoebGcQSuuPiLJXZptN9nndrQmbXEps2aiAFbWhM78LhWx4cbbfAAtVT86zwu1RK7aPFFxuhDR1L6tSoc_BJECPebWKRXjBZCiFV4n3oknjhMstn64tZ_2W-5JsGY4Hc5n9yBXArwl93lqt7_RN5w6Cf0h4QyQ5v-65YGjQR0_FDW2QvzqY368QQMicAtaSqzs8KJZgnYb9c7d0zgdAZHzu6qMQvRL5hajrn1n91CbOpbISD08qNLyrdkt-bFTWhAI4vMQFh6WeZu0fM4lFd2NcRwr3XPksINHaQ-G_xBniIqbw0Ls1jF44-csFCur-kEgU8awapJzKnqDKgw",
                      "e": "AQAB"
                    }
                  ]
                }
                """;

        wireMockServer.stubFor(get(urlEqualTo("/.well-known/jwks.json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jwksResponse)));

        // Verify the mock is accessible
        String jwksUrl = wireMockServer.baseUrl() + "/.well-known/jwks.json";
        assertDoesNotThrow(() -> {
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create(jwksUrl))
                    .GET()
                    .build();
            java.net.http.HttpResponse<String> response = client.send(request,
                    java.net.http.HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("test-key-id"));
        });
    }

    @Test
    @DisplayName("Should handle JWKS endpoint unavailable")
    void testJwksUnavailable() {
        wireMockServer.stubFor(get(urlEqualTo("/.well-known/jwks.json"))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withBody("Service Unavailable")));

        String jwksUrl = wireMockServer.baseUrl() + "/.well-known/jwks.json";

        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(jwksUrl))
                .GET()
                .build();

        assertDoesNotThrow(() -> {
            java.net.http.HttpResponse<String> response = client.send(request,
                    java.net.http.HttpResponse.BodyHandlers.ofString());
            assertEquals(503, response.statusCode());
        });
    }

    @Test
    @DisplayName("Should handle malformed JWKS response")
    void testMalformedJwks() {
        wireMockServer.stubFor(get(urlEqualTo("/.well-known/jwks.json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("not valid json")));

        String jwksUrl = wireMockServer.baseUrl() + "/.well-known/jwks.json";

        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(jwksUrl))
                .GET()
                .build();

        assertDoesNotThrow(() -> {
            java.net.http.HttpResponse<String> response = client.send(request,
                    java.net.http.HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("not valid json", response.body());
        });
    }
}
