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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Production-ready MLflow Tracking Server client.
 * 
 * Integrates with MLflow REST API to log experiments, runs, parameters,
 * and metrics for distributed scientific computing jobs.
 * 
 * @see <a href="https://mlflow.org/docs/latest/rest-api.html">MLflow REST
 *      API</a>
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MlflowClient {

    private static final Logger LOG = LoggerFactory.getLogger(MlflowClient.class);
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    private final String trackingUri;
    private final HttpClient httpClient;
    private final String experimentId;

    /**
     * Creates a new MLflow client.
     * 
     * @param trackingUri    MLflow tracking server URI (e.g.,
     *                       http://localhost:5000)
     * @param experimentName Name of the experiment to use/create
     */
    public MlflowClient(String trackingUri, String experimentName) {
        this.trackingUri = trackingUri.endsWith("/") ? trackingUri.substring(0, trackingUri.length() - 1) : trackingUri;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.experimentId = getOrCreateExperiment(experimentName);
        LOG.info("MLflow client initialized for experiment '{}' (ID: {}) at {}",
                experimentName, experimentId, trackingUri);
    }

    /**
     * Gets or creates an experiment by name.
     * 
     * @param name Experiment name
     * @return Experiment ID
     */
    private String getOrCreateExperiment(String name) {
        try {
            // Try to get existing experiment by name
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(trackingUri + "/api/2.0/mlflow/experiments/get-by-name"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            String.format("{\"experiment_name\":\"%s\"}", name)))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode json = JSON_MAPPER.readTree(response.body());
                String expId = json.path("experiment").path("experiment_id").asText();
                LOG.debug("Found existing MLflow experiment '{}' with ID: {}", name, expId);
                return expId;
            }

            // Experiment doesn't exist, create it
            request = HttpRequest.newBuilder()
                    .uri(URI.create(trackingUri + "/api/2.0/mlflow/experiments/create"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            String.format("{\"name\":\"%s\"}", name)))
                    .build();

            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode json = JSON_MAPPER.readTree(response.body());
                String expId = json.path("experiment_id").asText();
                LOG.info("Created new MLflow experiment '{}' with ID: {}", name, expId);
                return expId;
            }

            LOG.warn("Failed to create MLflow experiment, using default (0)");
            return "0";

        } catch (Exception e) {
            LOG.warn("MLflow server unreachable, using default experiment ID: {}", e.getMessage());
            return "0"; // Default experiment
        }
    }

    /**
     * Get the experiment ID for this client.
     * 
     * @return MLflow experiment ID
     */
    public String getExperimentId() {
        return experimentId;
    }

    /**
     * Starts a new run within the experiment.
     * 
     * @param runName Descriptive name for the run
     * @return CompletableFuture with run ID
     */
    public CompletableFuture<String> startRun(String runName) {
        String json = String.format(
                "{\"experiment_id\":\"%s\",\"run_name\":\"%s\",\"start_time\":%d}",
                experimentId, runName, System.currentTimeMillis());

        return postRequest("/api/2.0/mlflow/runs/create", json)
                .thenApply(response -> {
                    try {
                        JsonNode jsonNode = JSON_MAPPER.readTree(response);
                        String runId = jsonNode.path("run").path("info").path("run_id").asText();
                        LOG.info("MLflow: Started run '{}' (ID: {})", runName, runId);
                        return runId;
                    } catch (Exception e) {
                        LOG.error("Failed to parse MLflow run creation response", e);
                        return "run-" + System.currentTimeMillis(); // Fallback
                    }
                });
    }

    /**
     * Logs a parameter for a run.
     * 
     * @param runId Run ID
     * @param key   Parameter name
     * @param value Parameter value
     */
    public void logParam(String runId, String key, String value) {
        String json = String.format(
                "{\"run_id\":\"%s\",\"key\":\"%s\",\"value\":\"%s\"}",
                runId, key, escapeJson(value));
        postAsync("/api/2.0/mlflow/runs/log-parameter", json);
        LOG.debug("MLflow: Logged param {}={} for run {}", key, value, runId);
    }

    /**
     * Logs a metric for a run.
     * 
     * @param runId     Run ID
     * @param key       Metric name
     * @param value     Metric value
     * @param timestamp Timestamp in milliseconds
     */
    public void logMetric(String runId, String key, double value, long timestamp) {
        String json = String.format(
                "{\"run_id\":\"%s\",\"key\":\"%s\",\"value\":%f,\"timestamp\":%d}",
                runId, key, value, timestamp);
        postAsync("/api/2.0/mlflow/runs/log-metric", json);
        LOG.debug("MLflow: Logged metric {}={} for run {}", key, value, runId);
    }

    /**
     * Ends a run with a status.
     * 
     * @param runId  Run ID
     * @param status Run status (FINISHED, FAILED, KILLED)
     */
    public void endRun(String runId, String status) {
        String json = String.format(
                "{\"run_id\":\"%s\",\"status\":\"%s\",\"end_time\":%d}",
                runId, status, System.currentTimeMillis());
        postAsync("/api/2.0/mlflow/runs/update", json);
        LOG.info("MLflow: Ended run {} with status {}", runId, status);
    }

    /**
     * Posts a request asynchronously (fire and forget).
     */
    private void postAsync(String path, String jsonBody) {
        postRequest(path, jsonBody)
                .exceptionally(e -> {
                    LOG.warn("Failed to send to MLflow {}: {}", path, e.getMessage());
                    return null;
                });
    }

    /**
     * Posts a request and returns the response.
     */
    private CompletableFuture<String> postRequest(String path, String jsonBody) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(trackingUri + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .timeout(Duration.ofSeconds(5))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    /**
     * Escapes a string for JSON.
     */
    private String escapeJson(String value) {
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    /**
     * Checks if the MLflow server is reachable.
     * 
     * @return true if server responds
     */
    public boolean isServerReachable() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(trackingUri + "/health"))
                    .GET()
                    .timeout(Duration.ofSeconds(2))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }
}
