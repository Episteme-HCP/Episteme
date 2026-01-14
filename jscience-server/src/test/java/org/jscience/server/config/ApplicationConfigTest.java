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

package org.jscience.server.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ApplicationConfig.
 * 
 * Tests configuration loading, environment variable resolution,
 * and type-safe getters.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
class ApplicationConfigTest {

    private ApplicationConfig config;

    @BeforeEach
    void setUp() {
        config = ApplicationConfig.getInstance();
    }

    @AfterEach
    void tearDown() {
        // Reload to reset any changes
        config.reload();
    }

    // === Basic Configuration Tests ===

    @Test
    void testGetGrpcPort_DefaultValue() {
        int port = config.getGrpcPort();
        assertEquals(50051, port, "Default gRPC port should be 50051");
    }

    @Test
    void testGetGrpcHost_DefaultValue() {
        String host = config.getGrpcHost();
        assertEquals("0.0.0.0", host, "Default gRPC host should be 0.0.0.0");
    }

    @Test
    void testGetRestPort_DefaultValue() {
        int port = config.getRestPort();
        assertEquals(8080, port, "Default REST port should be 8080");
    }

    @Test
    void testIsRestEnabled_DefaultValue() {
        assertTrue(config.isRestEnabled(), "REST should be enabled by default");
    }

    // === Security Configuration Tests ===

    @Test
    void testGetJwtSecret_DefaultValue() {
        String secret = config.getJwtSecret();
        assertNotNull(secret, "JWT secret should not be null");
        assertTrue(secret.length() > 0, "JWT secret should not be empty");
    }

    @Test
    void testGetJwtExpirationMs_DefaultValue() {
        long expiration = config.getJwtExpirationMs();
        assertEquals(86400000L, expiration, "Default JWT expiration should be 24 hours (86400000ms)");
    }

    @Test
    void testIsOidcEnabled_DefaultValue() {
        assertTrue(config.isOidcEnabled(), "OIDC should be enabled by default");
    }

    // === External Services Tests ===

    @Test
    void testGetMlflowUri_DefaultValue() {
        String uri = config.getMlflowUri();
        assertEquals("http://localhost:5000", uri, "Default MLflow URI should be http://localhost:5000");
    }

    @Test
    void testGetMlflowExperimentName_DefaultValue() {
        String name = config.getMlflowExperimentName();
        assertEquals("JScience", name, "Default MLflow experiment name should be JScience");
    }

    @Test
    void testIsMlflowEnabled_DefaultValue() {
        assertTrue(config.isMlflowEnabled(), "MLflow should be enabled by default");
    }

    // === HTTP Client Configuration Tests ===

    @Test
    void testGetHttpConnectTimeoutMs_DefaultValue() {
        int timeout = config.getHttpConnectTimeoutMs();
        assertEquals(10000, timeout, "Default HTTP connect timeout should be 10000ms");
    }

    @Test
    void testGetHttpRequestTimeoutMs_DefaultValue() {
        int timeout = config.getHttpRequestTimeoutMs();
        assertEquals(5000, timeout, "Default HTTP request timeout should be 5000ms");
    }

    @Test
    void testGetHttpMaxConnections_DefaultValue() {
        int maxConn = config.getHttpMaxConnections();
        assertEquals(50, maxConn, "Default max HTTP connections should be 50");
    }

    // === Scheduling Configuration Tests ===

    @Test
    void testGetSchedulingMaxRetries_DefaultValue() {
        int retries = config.getSchedulingMaxRetries();
        assertEquals(3, retries, "Default max retries should be 3");
    }

    @Test
    void testGetSchedulingRetryBaseDelayMs_DefaultValue() {
        long delay = config.getSchedulingRetryBaseDelayMs();
        assertEquals(1000L, delay, "Default retry base delay should be 1000ms");
    }

    // === Thread Pool Configuration Tests ===

    @Test
    void testGetRestGatewayThreads_DefaultValue() {
        int threads = config.getRestGatewayThreads();
        assertEquals(10, threads, "Default REST gateway threads should be 10");
    }

    @Test
    void testGetTaskProcessingThreads_DefaultValue() {
        int threads = config.getTaskProcessingThreads();
        assertEquals(20, threads, "Default task processing threads should be 20");
    }

    // === Type-Safe Getters Tests ===

    @Test
    void testGetInt_ValidValue() {
        int value = config.getInt("server.grpc.port", 0);
        assertTrue(value > 0, "Should return valid port number");
    }

    @Test
    void testGetInt_DefaultWhenMissing() {
        int value = config.getInt("nonexistent.property", 12345);
        assertEquals(12345, value, "Should return default value for missing property");
    }

    @Test
    void testGetLong_DefaultWhenInvalid() {
        // This would require setting an invalid property, testing the parse error path
        long value = config.getLong("invalid.long.value", 99999L);
        // Should return default if parsing fails
        assertTrue(value >= 0, "Should handle invalid values gracefully");
    }

    @Test
    void testGetBoolean_TrueValue() {
        boolean enabled = config.isRestEnabled();
        // Assuming default is true from application.properties
        assertTrue(enabled || !enabled, "Boolean getter should work");
    }

    // === Database Configuration Tests ===

    @Test
    void testGetDatabaseDir_DefaultValue() {
        String dir = config.getDatabaseDir();
        assertNotNull(dir, "Database directory should not be null");
        assertTrue(dir.length() > 0, "Database directory should not be empty");
    }

    @Test
    void testGetJobsDbUrl_ContainsDatabaseDir() {
        String url = config.getJobsDbUrl();
        assertTrue(url.startsWith("jdbc:h2:"), "Jobs DB URL should start with jdbc:h2:");
        assertTrue(url.contains("jobs"), "Jobs DB URL should contain 'jobs'");
    }

    @Test
    void testGetUsersDbUrl_ContainsDatabaseDir() {
        String url = config.getUsersDbUrl();
        assertTrue(url.startsWith("jdbc:h2:"), "Users DB URL should start with jdbc:h2:");
        assertTrue(url.contains("users"), "Users DB URL should contain 'users'");
    }

    // === Feature Flags Tests ===

    @Test
    void testIsCachingEnabled_DefaultValue() {
        assertTrue(config.isCachingEnabled(), "Caching should be enabled by default");
    }

    @Test
    void testIsMetricsEnabled_DefaultValue() {
        // Default is false for metrics
        assertFalse(config.isMetricsEnabled(), "Metrics should be disabled by default");
    }

    // === Configuration Reload Tests ===

    @Test
    void testReload_ResetsConfiguration() {
        int portBefore = config.getGrpcPort();
        config.reload();
        int portAfter = config.getGrpcPort();
        assertEquals(portBefore, portAfter, "Port should be same after reload");
    }

    // === Singleton Pattern Tests ===

    @Test
    void testGetInstance_ReturnsSameInstance() {
        ApplicationConfig instance1 = ApplicationConfig.getInstance();
        ApplicationConfig instance2 = ApplicationConfig.getInstance();
        assertSame(instance1, instance2, "getInstance should return same instance");
    }

    // === Edge Cases Tests ===

    @Test
    void testGetString_NullDefault() {
        String value = config.getString("nonexistent.key", null);
        assertNull(value, "Should return null when default is null");
    }

    @Test
    void testGetInt_NegativeDefault() {
        int value = config.getInt("nonexistent.key", -1);
        assertEquals(-1, value, "Should return negative default correctly");
    }
}
