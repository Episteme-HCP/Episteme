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
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * Circuit Breaker implementation for fault tolerance.
 * 
 * States:
 * - CLOSED: Normal operation, requests flow through
 * - OPEN: Failures exceeded threshold, requests fail fast
 * - HALF_OPEN: Testing if service recovered
 * 
 * Thread-safe implementation using atomic primitives.
 */
public class CircuitBreaker {

    private static final Logger LOG = LoggerFactory.getLogger(CircuitBreaker.class);

    public enum State {
        CLOSED, OPEN, HALF_OPEN
    }

    private final String name;
    private final int failureThreshold;
    private final int successThreshold;
    private final Duration openDuration;

    private final AtomicReference<State> state = new AtomicReference<>(State.CLOSED);
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final AtomicInteger successCount = new AtomicInteger(0);
    private final AtomicReference<Instant> lastFailureTime = new AtomicReference<>(Instant.MIN);

    /**
     * Create a circuit breaker with default settings.
     * - 5 failures to open
     * - 3 successes to close
     * - 30 seconds open duration
     */
    public CircuitBreaker(String name) {
        this(name, 5, 3, Duration.ofSeconds(30));
    }

    /**
     * Create a circuit breaker with custom settings.
     */
    public CircuitBreaker(String name, int failureThreshold, int successThreshold, Duration openDuration) {
        this.name = name;
        this.failureThreshold = failureThreshold;
        this.successThreshold = successThreshold;
        this.openDuration = openDuration;
    }

    /**
     * Execute a supplier with circuit breaker protection.
     */
    public <T> T execute(Supplier<T> action) throws CircuitBreakerOpenException {
        if (!allowRequest()) {
            throw new CircuitBreakerOpenException(name);
        }

        try {
            T result = action.get();
            recordSuccess();
            return result;
        } catch (Exception e) {
            recordFailure();
            throw e;
        }
    }

    /**
     * Execute a runnable with circuit breaker protection.
     */
    public void execute(Runnable action) throws CircuitBreakerOpenException {
        execute(() -> {
            action.run();
            return null;
        });
    }

    /**
     * Check if a request should be allowed through.
     */
    public boolean allowRequest() {
        State currentState = state.get();

        if (currentState == State.CLOSED) {
            return true;
        }

        if (currentState == State.OPEN) {
            // Check if we should transition to half-open
            if (Duration.between(lastFailureTime.get(), Instant.now()).compareTo(openDuration) > 0) {
                if (state.compareAndSet(State.OPEN, State.HALF_OPEN)) {
                    LOG.info("Ã¢Å¡Â¡ Circuit breaker '{}' transitioning to HALF_OPEN", name);
                    successCount.set(0);
                }
                return true;
            }
            return false;
        }

        // HALF_OPEN: allow limited requests
        return true;
    }

    /**
     * Record a successful call.
     */
    public void recordSuccess() {
        State currentState = state.get();

        if (currentState == State.CLOSED) {
            failureCount.set(0);
            return;
        }

        if (currentState == State.HALF_OPEN) {
            int successes = successCount.incrementAndGet();
            if (successes >= successThreshold) {
                if (state.compareAndSet(State.HALF_OPEN, State.CLOSED)) {
                    failureCount.set(0);
                    successCount.set(0);
                    LOG.info("Ã¢Å“â€¦ Circuit breaker '{}' CLOSED - service recovered", name);
                }
            }
        }
    }

    /**
     * Record a failed call.
     */
    public void recordFailure() {
        lastFailureTime.set(Instant.now());
        State currentState = state.get();

        if (currentState == State.HALF_OPEN) {
            if (state.compareAndSet(State.HALF_OPEN, State.OPEN)) {
                LOG.warn("Ã°Å¸â€Â´ Circuit breaker '{}' OPEN - failure in half-open state", name);
            }
            return;
        }

        if (currentState == State.CLOSED) {
            int failures = failureCount.incrementAndGet();
            if (failures >= failureThreshold) {
                if (state.compareAndSet(State.CLOSED, State.OPEN)) {
                    LOG.warn("Ã°Å¸â€Â´ Circuit breaker '{}' OPEN - {} failures", name, failures);
                }
            }
        }
    }

    /**
     * Force the circuit breaker to reset to CLOSED state.
     */
    public void reset() {
        state.set(State.CLOSED);
        failureCount.set(0);
        successCount.set(0);
        LOG.info("Ã°Å¸â€â€ž Circuit breaker '{}' manually reset", name);
    }

    // --- Getters ---

    public String getName() {
        return name;
    }

    public State getState() {
        return state.get();
    }

    public int getFailureCount() {
        return failureCount.get();
    }

    public int getSuccessCount() {
        return successCount.get();
    }

    /**
     * Exception thrown when circuit breaker is open.
     */
    public static class CircuitBreakerOpenException extends RuntimeException {
        public CircuitBreakerOpenException(String name) {
            super("Circuit breaker '" + name + "' is OPEN - failing fast");
        }
    }
}


