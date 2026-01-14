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

package org.jscience.server.security;

import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Rate Limiter gRPC Interceptor - protects against abuse.
 * 
 * Features:
 * - Per-user rate limiting
 * - Per-method rate limiting
 * - Token bucket algorithm
 * - Configurable limits
 */
public class RateLimiterInterceptor implements ServerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(RateLimiterInterceptor.class);

    private final ConcurrentHashMap<String, TokenBucket> userBuckets = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, TokenBucket> methodBuckets = new ConcurrentHashMap<>();
    private final int defaultRequestsPerSecond;
    private final int defaultBurstSize;
    private final ConcurrentHashMap<String, RateLimitConfig> methodConfigs = new ConcurrentHashMap<>();

    /**
     * Rate limit configuration for a method.
     */
    public static class RateLimitConfig {
        private final int requestsPerSecond;
        private final int burstSize;

        public RateLimitConfig(int requestsPerSecond, int burstSize) {
            this.requestsPerSecond = requestsPerSecond;
            this.burstSize = burstSize;
        }
    }

    /**
     * Token bucket implementation.
     */
    private static class TokenBucket {
        private final int maxTokens;
        private final int refillRate; // tokens per second
        private final AtomicInteger tokens;
        private final AtomicLong lastRefill;

        public TokenBucket(int maxTokens, int refillRate) {
            this.maxTokens = maxTokens;
            this.refillRate = refillRate;
            this.tokens = new AtomicInteger(maxTokens);
            this.lastRefill = new AtomicLong(System.currentTimeMillis());
        }

        public boolean tryConsume() {
            refill();
            return tokens.getAndUpdate(t -> t > 0 ? t - 1 : 0) > 0;
        }

        private void refill() {
            long now = System.currentTimeMillis();
            long elapsed = now - lastRefill.get();

            if (elapsed > 1000) {
                int tokensToAdd = (int) (elapsed / 1000) * refillRate;
                if (tokensToAdd > 0 && lastRefill.compareAndSet(lastRefill.get(), now)) {
                    tokens.updateAndGet(t -> Math.min(maxTokens, t + tokensToAdd));
                }
            }
        }

        public int getAvailableTokens() {
            refill();
            return tokens.get();
        }
    }

    /**
     * Create rate limiter with default limits.
     */
    public RateLimiterInterceptor() {
        this(100, 200); // 100 req/s default, 200 burst
    }

    /**
     * Create rate limiter with custom defaults.
     */
    public RateLimiterInterceptor(int defaultRequestsPerSecond, int defaultBurstSize) {
        this.defaultRequestsPerSecond = defaultRequestsPerSecond;
        this.defaultBurstSize = defaultBurstSize;
    }

    /**
     * Configure rate limit for a specific method.
     */
    public RateLimiterInterceptor configureMethod(String method, int requestsPerSecond, int burstSize) {
        methodConfigs.put(method, new RateLimitConfig(requestsPerSecond, burstSize));
        return this;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String methodName = call.getMethodDescriptor().getFullMethodName();
        String userId = extractUserId(headers);

        // Check user rate limit
        if (userId != null && !checkUserLimit(userId)) {
            LOG.warn("Ã°Å¸Å¡Â« Rate limit exceeded for user: {}", userId);
            call.close(Status.RESOURCE_EXHAUSTED
                    .withDescription("Rate limit exceeded. Please try again later."), headers);
            return new ServerCall.Listener<ReqT>() {
            };
        }

        // Check method rate limit
        if (!checkMethodLimit(methodName)) {
            LOG.warn("Ã°Å¸Å¡Â« Rate limit exceeded for method: {}", methodName);
            call.close(Status.RESOURCE_EXHAUSTED
                    .withDescription("Too many requests. Please try again later."), headers);
            return new ServerCall.Listener<ReqT>() {
            };
        }

        return next.startCall(call, headers);
    }

    private String extractUserId(Metadata headers) {
        // Could extract from JWT token or dedicated header
        Metadata.Key<String> userIdKey = Metadata.Key.of("x-user-id", Metadata.ASCII_STRING_MARSHALLER);
        return headers.get(userIdKey);
    }

    private boolean checkUserLimit(String userId) {
        TokenBucket bucket = userBuckets.computeIfAbsent(userId,
                k -> new TokenBucket(defaultBurstSize, defaultRequestsPerSecond));
        return bucket.tryConsume();
    }

    private boolean checkMethodLimit(String methodName) {
        RateLimitConfig config = methodConfigs.get(methodName);
        int rps = config != null ? config.requestsPerSecond : defaultRequestsPerSecond * 10;
        int burst = config != null ? config.burstSize : defaultBurstSize * 10;

        TokenBucket bucket = methodBuckets.computeIfAbsent(methodName,
                k -> new TokenBucket(burst, rps));
        return bucket.tryConsume();
    }

    /**
     * Get current rate limit status for a user.
     */
    public int getUserAvailableTokens(String userId) {
        TokenBucket bucket = userBuckets.get(userId);
        return bucket != null ? bucket.getAvailableTokens() : defaultBurstSize;
    }

    /**
     * Clear rate limit for a user (e.g., after ban expires).
     */
    public void clearUserLimit(String userId) {
        userBuckets.remove(userId);
    }

    /**
     * Get stats about rate limiting.
     */
    public RateLimitStats getStats() {
        return new RateLimitStats(
                userBuckets.size(),
                methodBuckets.size(),
                userBuckets.values().stream().mapToInt(TokenBucket::getAvailableTokens).sum());
    }

    public record RateLimitStats(int trackedUsers, int trackedMethods, int totalTokens) {
    }
}


