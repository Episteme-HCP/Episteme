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

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client for MLflow Tracking Server.
 * 
 * Allows logging runs, params, and metrics from the grid.
 */
public class MlflowClient {

    private static final Logger LOG = LoggerFactory.getLogger(MlflowClient.class);

    private final String trackingUri;
    private final HttpClient httpClient;
    private final String experimentId;

    public MlflowClient(String trackingUri, String experimentName) {
        this.trackingUri = trackingUri;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.experimentId = getOrCreateExperiment(experimentName);
    }

    private String getOrCreateExperiment(String name) {
        // Simplified: In reality, call MLflow API to get ID by name or create
        return "0"; // Default experiment
    }

    public CompletableFuture<String> startRun(String runName) {
        // Mock implementation for demo
        String runId = "run-" + System.currentTimeMillis();
        LOG.info("MLflow: Started run {} ({})", runName, runId);
        return CompletableFuture.completedFuture(runId);
    }

    public void logParam(String runId, String key, String value) {
        String json = String.format("{\"run_id\": \"%s\", \"key\": \"%s\", \"value\": \"%s\"}", runId, key, value);
        postAsync("/api/2.0/mlflow/runs/log-parameter", json);
    }

    public void logMetric(String runId, String key, double value, long timestamp) {
        String json = String.format("{\"run_id\": \"%s\", \"key\": \"%s\", \"value\": %f, \"timestamp\": %d}", runId,
                key, value, timestamp);
        postAsync("/api/2.0/mlflow/runs/log-metric", json);
    }

    public void endRun(String runId, String status) {
        String json = String.format("{\"run_id\": \"%s\", \"status\": \"%s\"}", runId, status);
        postAsync("/api/2.0/mlflow/runs/update", json);
        LOG.info("MLflow: Ended run {} with status {}", runId, status);
    }

    private void postAsync(String path, String jsonBody) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(trackingUri + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.discarding())
                .exceptionally(e -> {
                    LOG.warn("Failed to send to MLflow: {}", e.getMessage());
                    return null;
                });
    }
}


