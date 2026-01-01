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

package org.jscience.server.resilience;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * Retry executor with exponential backoff for transient failures.
 * 
 * Features:
 * - Configurable max retries
 * - Exponential backoff with jitter
 * - Retry only specific exceptions
 * - Integration with Circuit Breaker
 */
public class RetryExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(RetryExecutor.class);

    private final int maxRetries;
    private final Duration initialDelay;
    private final double multiplier;
    private final Duration maxDelay;
    private final double jitterFactor;
    private final ScheduledExecutorService scheduler;

    /**
     * Create retry executor with default settings.
     * - 3 max retries
     * - 100ms initial delay
     * - 2x multiplier
     * - 10s max delay
     */
    public RetryExecutor() {
        this(3, Duration.ofMillis(100), 2.0, Duration.ofSeconds(10), 0.1);
    }

    /**
     * Create retry executor with custom settings.
     */
    public RetryExecutor(int maxRetries, Duration initialDelay, double multiplier,
            Duration maxDelay, double jitterFactor) {
        this.maxRetries = maxRetries;
        this.initialDelay = initialDelay;
        this.multiplier = multiplier;
        this.maxDelay = maxDelay;
        this.jitterFactor = jitterFactor;
        this.scheduler = Executors.newScheduledThreadPool(2, r -> {
            Thread t = new Thread(r, "retry-executor");
            t.setDaemon(true);
            return t;
        });
    }

    /**
     * Execute with retry logic (blocking).
     */
    public <T> T execute(Supplier<T> action) throws Exception {
        return execute(action, null);
    }

    /**
     * Execute with retry logic and circuit breaker.
     */
    public <T> T execute(Supplier<T> action, CircuitBreaker circuitBreaker) throws Exception {
        int attempt = 0;
        Exception lastException = null;

        while (attempt <= maxRetries) {
            try {
                if (circuitBreaker != null) {
                    return circuitBreaker.execute(action);
                }
                return action.get();
            } catch (CircuitBreaker.CircuitBreakerOpenException e) {
                // Don't retry if circuit breaker is open
                throw e;
            } catch (Exception e) {
                lastException = e;
                attempt++;

                if (attempt > maxRetries) {
                    LOG.error("Ã¢ Å’ All {} retries exhausted", maxRetries);
                    if (lastException != null) {
                        throw lastException;
                    } else {
                        throw new RuntimeException("Operation exhausted retries with unknown error");
                    }
                }

                long delayMs = calculateDelay(attempt);
                LOG.warn("Ã¢Å¡Â Ã¯Â¸  Attempt {} failed, retrying in {}ms: {}",
                        attempt, delayMs, e.getMessage());

                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted", ie);
                }
            }
        }

        if (lastException != null) {
            throw new RuntimeException("Operation failed after " + maxRetries + " attempts", lastException);
        } else {
            throw new RuntimeException("Operation failed after " + maxRetries + " attempts with unknown error");
        }
    }

    /**
     * Execute with retry logic (async).
     */
    public <T> CompletableFuture<T> executeAsync(Supplier<T> action) {
        return executeAsync(action, null);
    }

    /**
     * Execute with retry logic and circuit breaker (async).
     */
    public <T> CompletableFuture<T> executeAsync(Supplier<T> action, CircuitBreaker circuitBreaker) {
        CompletableFuture<T> future = new CompletableFuture<>();
        executeWithRetryAsync(action, circuitBreaker, 0, future);
        return future;
    }

    private <T> void executeWithRetryAsync(Supplier<T> action, CircuitBreaker circuitBreaker,
            int attempt, CompletableFuture<T> future) {
        CompletableFuture.supplyAsync(() -> {
            try {
                if (circuitBreaker != null) {
                    return circuitBreaker.execute(action);
                }
                return action.get();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }).whenComplete((result, error) -> {
            if (error == null) {
                future.complete(result);
            } else {
                Throwable cause = error instanceof CompletionException ? error.getCause() : error;

                if (cause instanceof CircuitBreaker.CircuitBreakerOpenException) {
                    future.completeExceptionally(cause);
                    return;
                }

                int nextAttempt = attempt + 1;
                if (nextAttempt > maxRetries) {
                    LOG.error("Ã¢ Å’ All {} async retries exhausted", maxRetries);
                    future.completeExceptionally(cause);
                    return;
                }

                long delayMs = calculateDelay(nextAttempt);
                LOG.warn("Ã¢Å¡Â Ã¯Â¸  Async attempt {} failed, retrying in {}ms", nextAttempt, delayMs);

                scheduler.schedule(
                        () -> executeWithRetryAsync(action, circuitBreaker, nextAttempt, future),
                        delayMs, TimeUnit.MILLISECONDS);
            }
        });
    }

    /**
     * Calculate delay with exponential backoff and jitter.
     */
    private long calculateDelay(int attempt) {
        double delay = initialDelay.toMillis() * Math.pow(multiplier, attempt - 1);
        delay = Math.min(delay, maxDelay.toMillis());

        // Add jitter
        double jitter = delay * jitterFactor * (ThreadLocalRandom.current().nextDouble() - 0.5) * 2;
        delay = Math.max(0, delay + jitter);

        return (long) delay;
    }

    /**
     * Shutdown the executor.
     */
    public void shutdown() {
        scheduler.shutdown();
    }

    // --- Builder pattern ---

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int maxRetries = 3;
        private Duration initialDelay = Duration.ofMillis(100);
        private double multiplier = 2.0;
        private Duration maxDelay = Duration.ofSeconds(10);
        private double jitterFactor = 0.1;

        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder initialDelay(Duration initialDelay) {
            this.initialDelay = initialDelay;
            return this;
        }

        public Builder multiplier(double multiplier) {
            this.multiplier = multiplier;
            return this;
        }

        public Builder maxDelay(Duration maxDelay) {
            this.maxDelay = maxDelay;
            return this;
        }

        public Builder jitterFactor(double jitterFactor) {
            this.jitterFactor = jitterFactor;
            return this;
        }

        public RetryExecutor build() {
            return new RetryExecutor(maxRetries, initialDelay, multiplier, maxDelay, jitterFactor);
        }
    }
}

