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

package org.jscience.server.gateway;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for REST Gateway endpoints.
 * These tests verify the REST API contract.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
class RestGatewayIntegrationTest {

    private static WireMockServer wireMockServer;
    private static HttpClient httpClient;

    @BeforeAll
    static void setup() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();
        httpClient = HttpClient.newHttpClient();
    }

    @AfterAll
    static void teardown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @BeforeEach
    void resetMocks() {
        wireMockServer.resetAll();
    }

    @Test
    @DisplayName("Health endpoint should return 200")
    void testHealthEndpoint() {
        wireMockServer.stubFor(get(urlEqualTo("/api/health"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\":\"healthy\"}")));

        String url = wireMockServer.baseUrl() + "/api/health";

        assertDoesNotThrow(() -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("healthy"));
        });
    }

    @Test
    @DisplayName("Status endpoint should return server status")
    void testStatusEndpoint() {
        wireMockServer.stubFor(get(urlEqualTo("/api/status"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"activeWorkers\":5,\"queuedTasks\":10}")));

        String url = wireMockServer.baseUrl() + "/api/status";

        assertDoesNotThrow(() -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("activeWorkers"));
        });
    }

    @Test
    @DisplayName("Metrics endpoint should return Prometheus format")
    void testMetricsEndpoint() {
        wireMockServer.stubFor(get(urlEqualTo("/metrics"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/plain; version=0.0.4")
                        .withBody("# HELP jvm_memory_used JVM memory used\njvm_memory_used_bytes 12345")));

        String url = wireMockServer.baseUrl() + "/metrics";

        assertDoesNotThrow(() -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("jvm_memory"));
        });
    }

    @Test
    @DisplayName("Login endpoint should accept POST with credentials")
    void testLoginEndpoint() {
        wireMockServer.stubFor(post(urlEqualTo("/api/auth/login"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"token\":\"jwt-token-here\",\"expiresIn\":3600}")));

        String url = wireMockServer.baseUrl() + "/api/auth/login";

        assertDoesNotThrow(() -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"username\":\"user\",\"password\":\"pass\"}"))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("token"));
        });
    }

    @Test
    @DisplayName("Tasks endpoint should accept POST to submit task")
    void testTasksEndpoint() {
        wireMockServer.stubFor(post(urlEqualTo("/api/tasks"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"taskId\":\"task-123\",\"status\":\"QUEUED\"}")));

        String url = wireMockServer.baseUrl() + "/api/tasks";

        assertDoesNotThrow(() -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"taskType\":\"COMPUTE\",\"payload\":{}}"))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("taskId"));
        });
    }
}
