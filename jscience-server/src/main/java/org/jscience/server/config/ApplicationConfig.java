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

package org.jscience.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Central configuration management for JScience Server.
 * 
 * Loads configuration from multiple sources with the following priority:
 * 1. Environment variables (highest priority)
 * 2. System properties
 * 3. application.properties file (lowest priority)
 * 
 * Usage:
 * 
 * <pre>
 * ApplicationConfig config = ApplicationConfig.getInstance();
 * int port = config.getGrpcPort();
 * String mlflowUri = config.getMlflowUri();
 * </pre>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ApplicationConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationConfig.class);
    private static final String CONFIG_FILE = "/application.properties";

    private static ApplicationConfig INSTANCE;

    private final Properties properties = new Properties();

    /**
     * Private constructor - use getInstance() instead.
     */
    private ApplicationConfig() {
        loadConfiguration();
    }

    /**
     * Get the singleton instance.
     */
    public static synchronized ApplicationConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ApplicationConfig();
        }
        return INSTANCE;
    }

    /**
     * Load configuration from all sources.
     */
    private void loadConfiguration() {
        // 1. Load from classpath resource
        try (InputStream is = getClass().getResourceAsStream(CONFIG_FILE)) {
            if (is != null) {
                properties.load(is);
                LOG.info("✓ Loaded configuration from {}", CONFIG_FILE);
            } else {
                LOG.warn("⚠️  Configuration file {} not found, using defaults", CONFIG_FILE);
            }
        } catch (IOException e) {
            LOG.error("Failed to load configuration from {}", CONFIG_FILE, e);
        }

        // 2. Override with system properties
        properties.putAll(System.getProperties());

        // 3. Process environment variable placeholders (${ENV_VAR:default})
        resolveEnvironmentVariables();

        LOG.info("Configuration loaded successfully");
    }

    /**
     * Resolve ${ENV_VAR:default} placeholders in property values.
     */
    private void resolveEnvironmentVariables() {
        properties.replaceAll((key, value) -> {
            if (value instanceof String) {
                String strValue = (String) value;
                if (strValue.contains("${")) {
                    return resolveValue(strValue);
                }
            }
            return value;
        });
    }

    /**
     * Resolve a value containing ${ENV_VAR:default} placeholder.
     */
    private String resolveValue(String value) {
        while (value.contains("${")) {
            int start = value.indexOf("${");
            int end = value.indexOf("}", start);
            if (end == -1)
                break;

            String placeholder = value.substring(start + 2, end);
            String[] parts = placeholder.split(":", 2);
            String envVar = parts[0];
            String defaultValue = parts.length > 1 ? parts[1] : "";

            String resolved = System.getenv(envVar);
            if (resolved == null) {
                resolved = defaultValue;
            }

            value = value.substring(0, start) + resolved + value.substring(end + 1);
        }
        return value;
    }

    // --- Generic Getters ---

    public String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null)
            return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOG.warn("Invalid integer value for {}: {}, using default: {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    public long getLong(String key, long defaultValue) {
        String value = properties.getProperty(key);
        if (value == null)
            return defaultValue;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            LOG.warn("Invalid long value for {}: {}, using default: {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value == null)
            return defaultValue;
        return Boolean.parseBoolean(value);
    }

    // --- Server Configuration ---

    public int getGrpcPort() {
        return getInt("server.grpc.port", 50051);
    }

    public String getGrpcHost() {
        return getString("server.grpc.host", "0.0.0.0");
    }

    public int getRestPort() {
        return getInt("server.rest.port", 8080);
    }

    public boolean isRestEnabled() {
        return getBoolean("server.rest.enabled", true);
    }

    public int getRestThreads() {
        return getInt("server.rest.threads", 10);
    }

    // --- Security Configuration ---

    public String getJwtSecret() {
        return getString("security.jwt.secret", "INSECURE_DEFAULT");
    }

    public long getJwtExpirationMs() {
        return getLong("security.jwt.expiration.ms", 86400000L);
    }

    public boolean isOidcEnabled() {
        return getBoolean("security.oidc.enabled", true);
    }

    // --- Database Configuration ---

    public String getDatabaseDir() {
        return getString("database.dir", "./data");
    }

    public String getJobsDbUrl() {
        return getString("database.jobs.url", "jdbc:h2:" + getDatabaseDir() + "/jobs");
    }

    public String getUsersDbUrl() {
        return getString("database.users.url", "jdbc:h2:" + getDatabaseDir() + "/users");
    }

    // --- External Services ---

    public String getMlflowUri() {
        return getString("mlflow.tracking.uri", "http://localhost:5000");
    }

    public String getMlflowExperimentName() {
        return getString("mlflow.experiment.name", "JScience");
    }

    public boolean isMlflowEnabled() {
        return getBoolean("mlflow.enabled", true);
    }

    public String getWorldBankApiUrl() {
        return getString("worldbank.api.url", "https://api.worldbank.org/v2");
    }

    public boolean isWorldBankCacheEnabled() {
        return getBoolean("worldbank.cache.enabled", true);
    }

    public int getWorldBankCacheTtlHours() {
        return getInt("worldbank.cache.ttl.hours", 24);
    }

    // --- HTTP Client Configuration ---

    public int getHttpConnectTimeoutMs() {
        return getInt("http.client.connect.timeout.ms", 10000);
    }

    public int getHttpRequestTimeoutMs() {
        return getInt("http.client.request.timeout.ms", 5000);
    }

    public int getHttpMaxConnections() {
        return getInt("http.client.max.connections", 50);
    }

    public boolean isHttpConnectionPoolEnabled() {
        return getBoolean("http.client.connection.pool.enabled", true);
    }

    // --- Task Scheduling Configuration ---

    public int getSchedulingMaxRetries() {
        return getInt("scheduling.max.retries", 3);
    }

    public long getSchedulingRetryBaseDelayMs() {
        return getLong("scheduling.retry.base.delay.ms", 1000L);
    }

    public long getSchedulingAgingIntervalMs() {
        return getLong("scheduling.aging.interval.ms", 60000L);
    }

    public long getSchedulingStarvationThresholdMs() {
        return getLong("scheduling.starvation.threshold.ms", 300000L);
    }

    // --- Thread Pool Configuration ---

    public int getRestGatewayThreads() {
        return getInt("executor.rest.gateway.threads", 10);
    }

    public int getTaskProcessingThreads() {
        return getInt("executor.task.processing.threads", 20);
    }

    public int getMlflowThreads() {
        return getInt("executor.mlflow.threads", 5);
    }

    // --- Service Discovery ---

    public boolean isMdnsEnabled() {
        return getBoolean("discovery.mdns.enabled", true);
    }

    public String getMdnsServiceType() {
        return getString("discovery.mdns.service.type", "_jscience._tcp.local.");
    }

    public String getMdnsServiceName() {
        return getString("discovery.mdns.service.name", "JScience Server");
    }

    // --- Performance Tuning ---

    public boolean isCircuitBreakerEnabled() {
        return getBoolean("performance.circuit.breaker.enabled", true);
    }

    public int getCircuitBreakerFailureThreshold() {
        return getInt("performance.circuit.breaker.failure.threshold", 5);
    }

    public long getCircuitBreakerTimeoutMs() {
        return getLong("performance.circuit.breaker.timeout.ms", 60000L);
    }

    public boolean isRateLimitingEnabled() {
        return getBoolean("performance.rate.limiting.enabled", false);
    }

    // --- Feature Flags ---

    public boolean isCachingEnabled() {
        return getBoolean("features.caching.enabled", true);
    }

    public boolean isMetricsEnabled() {
        return getBoolean("features.metrics.enabled", false);
    }

    public boolean isTracingEnabled() {
        return getBoolean("features.tracing.enabled", false);
    }

    // --- Utility Methods ---

    /**
     * Reload configuration from sources.
     */
    public synchronized void reload() {
        properties.clear();
        loadConfiguration();
        LOG.info("Configuration reloaded");
    }

    /**
     * Get all properties (for debugging).
     */
    public Properties getAllProperties() {
        return new Properties(properties);
    }

    /**
     * Print configuration summary.
     */
    public void printSummary() {
        LOG.info("=== JScience Configuration Summary ===");
        LOG.info("gRPC Server: {}:{}", getGrpcHost(), getGrpcPort());
        LOG.info("REST Gateway: {} (port {})", isRestEnabled() ? "enabled" : "disabled", getRestPort());
        LOG.info("MLflow: {} ({})", isMlflowEnabled() ? "enabled" : "disabled", getMlflowUri());
        LOG.info("Database: {}", getDatabaseDir());
        LOG.info("OIDC: {}", isOidcEnabled() ? "enabled" : "disabled");
        LOG.info("mDNS Discovery: {}", isMdnsEnabled() ? "enabled" : "disabled");
        LOG.info("Circuit Breaker: {}", isCircuitBreakerEnabled() ? "enabled" : "disabled");
        LOG.info("=======================================");
    }
}
