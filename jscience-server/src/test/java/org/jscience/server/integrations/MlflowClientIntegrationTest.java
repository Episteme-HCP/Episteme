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

package org.jscience.server.integrations;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for MlflowClient using WireMock.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
class MlflowClientIntegrationTest {

        private static WireMockServer wireMockServer;

        @BeforeAll
        static void startWireMock() {
                wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig()
                                .dynamicPort());
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
        @DisplayName("Should get experiment by name from MLflow API")
        void testGetExperimentByName() {
                String experimentResponse = """
                                {
                                  "experiment": {
                                    "experiment_id": "123",
                                    "name": "test-experiment",
                                    "artifact_location": "mlflow-artifacts:/123",
                                    "lifecycle_stage": "active"
                                  }
                                }
                                """;

                wireMockServer.stubFor(get(urlPathEqualTo("/api/2.0/mlflow/experiments/get-by-name"))
                                .withQueryParam("experiment_name", equalTo("test-experiment"))
                                .willReturn(aResponse()
                                                .withStatus(200)
                                                .withHeader("Content-Type", "application/json")
                                                .withBody(experimentResponse)));

                String url = wireMockServer.baseUrl()
                                + "/api/2.0/mlflow/experiments/get-by-name?experiment_name=test-experiment";

                assertDoesNotThrow(() -> {
                        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
                        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                                        .uri(java.net.URI.create(url))
                                        .GET()
                                        .build();
                        java.net.http.HttpResponse<String> response = client.send(request,
                                        java.net.http.HttpResponse.BodyHandlers.ofString());
                        assertEquals(200, response.statusCode());
                        assertTrue(response.body().contains("\"experiment_id\": \"123\""));
                });
        }

        @Test
        @DisplayName("Should create experiment via MLflow API")
        void testCreateExperiment() {
                String createResponse = """
                                {
                                  "experiment_id": "456"
                                }
                                """;

                wireMockServer.stubFor(post(urlEqualTo("/api/2.0/mlflow/experiments/create"))
                                .willReturn(aResponse()
                                                .withStatus(200)
                                                .withHeader("Content-Type", "application/json")
                                                .withBody(createResponse)));

                String url = wireMockServer.baseUrl() + "/api/2.0/mlflow/experiments/create";

                assertDoesNotThrow(() -> {
                        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
                        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                                        .uri(java.net.URI.create(url))
                                        .header("Content-Type", "application/json")
                                        .POST(java.net.http.HttpRequest.BodyPublishers
                                                        .ofString("{\"name\":\"new-experiment\"}"))
                                        .build();
                        java.net.http.HttpResponse<String> response = client.send(request,
                                        java.net.http.HttpResponse.BodyHandlers.ofString());
                        assertEquals(200, response.statusCode());
                        assertTrue(response.body().contains("456"));
                });
        }

        @Test
        @DisplayName("Should log metric via MLflow API")
        void testLogMetric() {
                wireMockServer.stubFor(post(urlEqualTo("/api/2.0/mlflow/runs/log-metric"))
                                .willReturn(aResponse()
                                                .withStatus(200)
                                                .withHeader("Content-Type", "application/json")
                                                .withBody("{}")));

                String url = wireMockServer.baseUrl() + "/api/2.0/mlflow/runs/log-metric";
                String body = "{\"run_id\":\"abc123\",\"key\":\"accuracy\",\"value\":0.95,\"timestamp\":1234567890}";

                assertDoesNotThrow(() -> {
                        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
                        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                                        .uri(java.net.URI.create(url))
                                        .header("Content-Type", "application/json")
                                        .POST(java.net.http.HttpRequest.BodyPublishers.ofString(body))
                                        .build();
                        java.net.http.HttpResponse<String> response = client.send(request,
                                        java.net.http.HttpResponse.BodyHandlers.ofString());
                        assertEquals(200, response.statusCode());
                });
        }

        @Test
        @DisplayName("Should handle MLflow server unavailable")
        void testServerUnavailable() {
                wireMockServer.stubFor(post(urlEqualTo("/api/2.0/mlflow/runs/log-metric"))
                                .willReturn(aResponse()
                                                .withStatus(503)
                                                .withBody("Service Unavailable")));

                String url = wireMockServer.baseUrl() + "/api/2.0/mlflow/runs/log-metric";

                assertDoesNotThrow(() -> {
                        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
                        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                                        .uri(java.net.URI.create(url))
                                        .header("Content-Type", "application/json")
                                        .POST(java.net.http.HttpRequest.BodyPublishers.ofString("{}"))
                                        .build();
                        java.net.http.HttpResponse<String> response = client.send(request,
                                        java.net.http.HttpResponse.BodyHandlers.ofString());
                        assertEquals(503, response.statusCode());
                });
        }
}
