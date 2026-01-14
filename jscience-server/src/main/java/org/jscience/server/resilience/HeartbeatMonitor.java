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

package org.jscience.server.resilience;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * Worker Heartbeat Monitor - tracks worker liveness and removes dead workers.
 * 
 * Workers must send heartbeats periodically. If no heartbeat is received
 * within the timeout period, the worker is marked as dead and removed.
 */
public class HeartbeatMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(HeartbeatMonitor.class);

    private final ConcurrentHashMap<String, WorkerInfo> workers = new ConcurrentHashMap<>();
    private final Duration heartbeatTimeout;
    private final Duration checkInterval;
    private final ScheduledExecutorService scheduler;
    private Consumer<String> onWorkerDead;
    private Consumer<String> onWorkerRecovered;

    /**
     * Worker info tracking last heartbeat and status.
     */
    public static class WorkerInfo {
        private final String workerId;
        private volatile Instant lastHeartbeat;
        private volatile boolean alive;
        private volatile int missedHeartbeats;
        private volatile double load;
        private volatile int activeTasks;

        public WorkerInfo(String workerId) {
            this.workerId = workerId;
            this.lastHeartbeat = Instant.now();
            this.alive = true;
            this.missedHeartbeats = 0;
            this.load = 0.0;
            this.activeTasks = 0;
        }

        public String getWorkerId() {
            return workerId;
        }

        public Instant getLastHeartbeat() {
            return lastHeartbeat;
        }

        public boolean isAlive() {
            return alive;
        }

        public int getMissedHeartbeats() {
            return missedHeartbeats;
        }

        public double getLoad() {
            return load;
        }

        public int getActiveTasks() {
            return activeTasks;
        }
    }

    /**
     * Create heartbeat monitor with default settings.
     * - 30 second timeout
     * - 10 second check interval
     */
    public HeartbeatMonitor() {
        this(Duration.ofSeconds(30), Duration.ofSeconds(10));
    }

    /**
     * Create heartbeat monitor with custom settings.
     */
    public HeartbeatMonitor(Duration heartbeatTimeout, Duration checkInterval) {
        this.heartbeatTimeout = heartbeatTimeout;
        this.checkInterval = checkInterval;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "heartbeat-monitor");
            t.setDaemon(true);
            return t;
        });
    }

    /**
     * Start the heartbeat monitoring.
     */
    public void start() {
        scheduler.scheduleAtFixedRate(this::checkWorkers,
                checkInterval.toMillis(), checkInterval.toMillis(), TimeUnit.MILLISECONDS);
        LOG.info("Ã°Å¸â€™â€œ Heartbeat monitor started (timeout: {}s, check: {}s)",
                heartbeatTimeout.toSeconds(), checkInterval.toSeconds());
    }

    /**
     * Stop the heartbeat monitoring.
     */
    public void stop() {
        scheduler.shutdown();
        LOG.info("Heartbeat monitor stopped");
    }

    /**
     * Register a new worker.
     */
    public void registerWorker(String workerId) {
        workers.put(workerId, new WorkerInfo(workerId));
        LOG.info("Ã°Å¸â€â€” Worker registered: {}", workerId);
    }

    /**
     * Unregister a worker.
     */
    public void unregisterWorker(String workerId) {
        workers.remove(workerId);
        LOG.info("Ã°Å¸â€Å’ Worker unregistered: {}", workerId);
    }

    /**
     * Record a heartbeat from a worker.
     */
    public void heartbeat(String workerId) {
        heartbeat(workerId, 0.0, 0);
    }

    /**
     * Record a heartbeat with status info.
     */
    public void heartbeat(String workerId, double load, int activeTasks) {
        WorkerInfo info = workers.get(workerId);
        if (info == null) {
            // Auto-register unknown workers
            info = new WorkerInfo(workerId);
            workers.put(workerId, info);
            LOG.info("Ã°Å¸â€â€” Worker auto-registered on heartbeat: {}", workerId);
        }

        boolean wasAlive = info.alive;
        info.lastHeartbeat = Instant.now();
        info.load = load;
        info.activeTasks = activeTasks;
        info.missedHeartbeats = 0;
        info.alive = true;

        if (!wasAlive) {
            LOG.info("Ã°Å¸â€™Å¡ Worker recovered: {}", workerId);
            if (onWorkerRecovered != null) {
                onWorkerRecovered.accept(workerId);
            }
        }
    }

    /**
     * Check all workers for missed heartbeats.
     */
    private void checkWorkers() {
        Instant now = Instant.now();

        for (WorkerInfo info : workers.values()) {
            Duration since = Duration.between(info.lastHeartbeat, now);

            if (since.compareTo(heartbeatTimeout) > 0) {
                info.missedHeartbeats++;

                if (info.alive) {
                    info.alive = false;
                    LOG.warn("Ã°Å¸â€™â€ Worker dead (no heartbeat for {}s): {}",
                            since.toSeconds(), info.workerId);

                    if (onWorkerDead != null) {
                        onWorkerDead.accept(info.workerId);
                    }
                }
            }
        }
    }

    /**
     * Set callback for when a worker is detected as dead.
     */
    public HeartbeatMonitor onWorkerDead(Consumer<String> callback) {
        this.onWorkerDead = callback;
        return this;
    }

    /**
     * Set callback for when a worker recovers.
     */
    public HeartbeatMonitor onWorkerRecovered(Consumer<String> callback) {
        this.onWorkerRecovered = callback;
        return this;
    }

    /**
     * Get all registered workers.
     */
    public ConcurrentHashMap<String, WorkerInfo> getWorkers() {
        return workers;
    }

    /**
     * Get count of alive workers.
     */
    public long getAliveWorkerCount() {
        return workers.values().stream().filter(WorkerInfo::isAlive).count();
    }

    /**
     * Get count of dead workers.
     */
    public long getDeadWorkerCount() {
        return workers.values().stream().filter(w -> !w.isAlive()).count();
    }

    /**
     * Check if a specific worker is alive.
     */
    public boolean isWorkerAlive(String workerId) {
        WorkerInfo info = workers.get(workerId);
        return info != null && info.alive;
    }
}


