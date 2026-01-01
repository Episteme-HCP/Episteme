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

package org.jscience.server.constants;

import java.time.Duration;

/**
 * HTTP client configuration constants.
 * 
 * Default timeout values, buffer sizes, and connection pool settings
 * for HTTP clients used throughout JScience.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class HttpConstants {

    private HttpConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }

    // === Timeout Configuration ===

    /**
     * Default connection timeout for HTTP requests.
     */
    public static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);

    /**
     * Default read/request timeout for HTTP requests.
     */
    public static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);

    /**
     * Timeout for MLflow server operations (can be longer).
     */
    public static final Duration MLFLOW_TIMEOUT = Duration.ofSeconds(30);

    /**
     * Timeout for World Bank API requests.
     */
    public static final Duration WORLD_BANK_TIMEOUT = Duration.ofSeconds(10);

    /**
     * Timeout for OIDC provider discovery.
     */
    public static final Duration OIDC_DISCOVERY_TIMEOUT = Duration.ofSeconds(5);

    // === Connection Pool Settings ===

    /**
     * Maximum number of concurrent connections in the pool.
     */
    public static final int MAX_CONNECTIONS = 50;

    /**
     * Maximum idle time for pooled connections.
     */
    public static final Duration MAX_IDLE_TIME = Duration.ofMinutes(5);

    /**
     * Connection keep-alive duration.
     */
    public static final Duration KEEP_ALIVE_DURATION = Duration.ofMinutes(2);

    // === Buffer Sizes ===

    /**
     * Default buffer size for reading HTTP responses (8KB).
     */
    public static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * Large buffer size for streaming operations (64KB).
     */
    public static final int LARGE_BUFFER_SIZE = 65536;

    /**
     * Maximum response body size to read into memory (10MB).
     */
    public static final int MAX_RESPONSE_SIZE = 10 * 1024 * 1024;

    // === Retry Configuration ===

    /**
     * Maximum number of retry attempts for failed requests.
     */
    public static final int MAX_RETRIES = 3;

    /**
     * Base delay between retry attempts (exponential backoff).
     */
    public static final Duration RETRY_BASE_DELAY = Duration.ofSeconds(1);

    /**
     * Maximum delay between retries.
     */
    public static final Duration RETRY_MAX_DELAY = Duration.ofSeconds(30);

    // === Rate Limiting ===

    /**
     * World Bank API rate limit (requests per minute).
     * Official limit is 120 req/min, we use 100 to be safe.
     */
    public static final int WORLD_BANK_RATE_LIMIT = 100;

    /**
     * Time window for rate limiting.
     */
    public static final Duration RATE_LIMIT_WINDOW = Duration.ofMinutes(1);

    // === Headers ===

    /**
     * User-Agent header value for JScience HTTP clients.
     */
    public static final String USER_AGENT = "JScience/1.0 (Java HTTP Client)";

    /**
     * Accept header for JSON responses.
     */
    public static final String ACCEPT_JSON = "application/json";

    /**
     * Content-Type header for JSON requests.
     */
    public static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";
}
