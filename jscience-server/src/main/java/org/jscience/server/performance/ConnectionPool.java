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

package org.jscience.server.performance;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Connection Pool Manager for gRPC channels.
 * 
 * Reuses gRPC channels to avoid connection overhead.
 * Features:
 * - Configurable pool size
 * - Automatic channel health checking
 * - Round-robin channel selection
 */
public class ConnectionPool {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionPool.class);

    private final String host;
    private final int port;
    private final int poolSize;
    private final ManagedChannel[] channels;
    private final AtomicInteger roundRobin = new AtomicInteger(0);
    private final ScheduledExecutorService healthChecker;
    private final boolean[] healthy;

    /**
     * Create connection pool with default size (4 channels).
     */
    public ConnectionPool(String host, int port) {
        this(host, port, 4);
    }

    /**
     * Create connection pool with custom size.
     */
    public ConnectionPool(String host, int port, int poolSize) {
        this.host = host;
        this.port = port;
        this.poolSize = poolSize;
        this.channels = new ManagedChannel[poolSize];
        this.healthy = new boolean[poolSize];
        this.healthChecker = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "connection-pool-health");
            t.setDaemon(true);
            return t;
        });

        initializePool();
    }

    private void initializePool() {
        for (int i = 0; i < poolSize; i++) {
            channels[i] = createChannel();
            healthy[i] = true;
        }

        // Start health checking
        healthChecker.scheduleAtFixedRate(this::checkHealth, 30, 30, TimeUnit.SECONDS);

        LOG.info("Ã°Å¸â€â€” Connection pool initialized: {}:{} with {} channels", host, port, poolSize);
    }

    private ManagedChannel createChannel() {
        return ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .maxInboundMessageSize(100 * 1024 * 1024) // 100MB
                .keepAliveTime(30, TimeUnit.SECONDS)
                .keepAliveTimeout(10, TimeUnit.SECONDS)
                .keepAliveWithoutCalls(true)
                .build();
    }

    /**
     * Get a channel from the pool (round-robin).
     */
    public ManagedChannel getChannel() {
        int attempts = 0;
        while (attempts < poolSize) {
            int index = roundRobin.getAndIncrement() % poolSize;
            if (healthy[index]) {
                return channels[index];
            }
            attempts++;
        }

        // All channels unhealthy, return any and hope for the best
        LOG.warn("Ã¢Å¡Â Ã¯Â¸Â All channels unhealthy, returning first available");
        return channels[0];
    }

    /**
     * Get a specific channel by index.
     */
    public ManagedChannel getChannel(int index) {
        return channels[index % poolSize];
    }

    /**
     * Check health of all channels and reconnect if needed.
     */
    private void checkHealth() {
        for (int i = 0; i < poolSize; i++) {
            ManagedChannel channel = channels[i];

            if (channel.isShutdown() || channel.isTerminated()) {
                LOG.warn("Ã°Å¸â€â€ž Reconnecting channel {}", i);
                try {
                    channel.shutdownNow();
                } catch (Exception ignored) {
                }

                channels[i] = createChannel();
                healthy[i] = true;
            } else {
                // Check connectivity state
                io.grpc.ConnectivityState state = channel.getState(false);
                healthy[i] = state != io.grpc.ConnectivityState.TRANSIENT_FAILURE &&
                        state != io.grpc.ConnectivityState.SHUTDOWN;

                if (!healthy[i]) {
                    LOG.debug("Channel {} unhealthy: {}", i, state);
                }
            }
        }
    }

    /**
     * Get number of healthy channels.
     */
    public int getHealthyChannelCount() {
        int count = 0;
        for (boolean h : healthy) {
            if (h)
                count++;
        }
        return count;
    }

    /**
     * Shutdown all channels.
     */
    public void shutdown() {
        healthChecker.shutdown();

        for (ManagedChannel channel : channels) {
            try {
                channel.shutdown();
                channel.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                channel.shutdownNow();
            }
        }

        LOG.info("Connection pool shutdown");
    }

    /**
     * Get pool statistics.
     */
    public PoolStats getStats() {
        int healthyCount = getHealthyChannelCount();
        return new PoolStats(poolSize, healthyCount, poolSize - healthyCount);
    }

    public record PoolStats(int total, int healthy, int unhealthy) {
    }
}


