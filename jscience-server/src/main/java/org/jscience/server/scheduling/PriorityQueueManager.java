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

package org.jscience.server.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Priority Queue Manager for task scheduling.
 * 
 * Features:
 * - Multiple priority levels (CRITICAL, HIGH, NORMAL, LOW, BATCH)
 * - Fair scheduling within priority levels
 * - Starvation prevention with aging
 * - Queue metrics and monitoring
 */
public class PriorityQueueManager<T> {

    private static final Logger LOG = LoggerFactory.getLogger(PriorityQueueManager.class);

    /**
     * Priority levels from highest to lowest.
     */
    public enum Priority {
        CRITICAL(0, 8), // 8x weight
        HIGH(1, 4), // 4x weight
        NORMAL(2, 2), // 2x weight
        LOW(3, 1), // 1x weight
        BATCH(4, 1); // 1x weight, background

        private final int level;
        private final int weight;

        Priority(int level, int weight) {
            this.level = level;
            this.weight = weight;
        }

        public int getLevel() {
            return level;
        }

        public int getWeight() {
            return weight;
        }
    }

    /**
     * Wrapper for prioritized items with timestamp for aging.
     */
    public static class PrioritizedItem<T> implements Comparable<PrioritizedItem<T>> {
        private final T item;
        private final Priority priority;
        private final long timestamp;
        private final long sequenceNumber;
        private int ageBoost = 0;

        public PrioritizedItem(T item, Priority priority, long sequenceNumber) {
            this.item = item;
            this.priority = priority;
            this.timestamp = System.currentTimeMillis();
            this.sequenceNumber = sequenceNumber;
        }

        public T getItem() {
            return item;
        }

        public Priority getPriority() {
            return priority;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public long getAge() {
            return System.currentTimeMillis() - timestamp;
        }

        public void applyAgeBoost(int boost) {
            this.ageBoost += boost;
        }

        public int getEffectivePriority() {
            return Math.max(0, priority.getLevel() - ageBoost);
        }

        @Override
        public int compareTo(PrioritizedItem<T> other) {
            // Lower effective priority = higher precedence
            int cmp = Integer.compare(this.getEffectivePriority(), other.getEffectivePriority());
            if (cmp != 0)
                return cmp;
            // Same priority: FIFO by sequence number
            return Long.compare(this.sequenceNumber, other.sequenceNumber);
        }
    }

    private final PriorityBlockingQueue<PrioritizedItem<T>> queue;
    private final AtomicLong sequenceCounter = new AtomicLong(0);
    private final Map<Priority, AtomicLong> submitCounts = new EnumMap<>(Priority.class);
    private final Map<Priority, AtomicLong> completeCounts = new EnumMap<>(Priority.class);
    private final ScheduledExecutorService ageScheduler;
    private final long agingIntervalMs;
    private final long starvationThresholdMs;

    /**
     * Create priority queue with default settings.
     */
    public PriorityQueueManager() {
        this(30000, 60000); // Age check every 30s, starve after 60s
    }

    /**
     * Create priority queue with custom aging settings.
     */
    public PriorityQueueManager(long agingIntervalMs, long starvationThresholdMs) {
        this.queue = new PriorityBlockingQueue<>();
        this.agingIntervalMs = agingIntervalMs;
        this.starvationThresholdMs = starvationThresholdMs;
        this.ageScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "priority-queue-aging");
            t.setDaemon(true);
            return t;
        });

        // Initialize counters
        for (Priority p : Priority.values()) {
            submitCounts.put(p, new AtomicLong(0));
            completeCounts.put(p, new AtomicLong(0));
        }

        // Start aging process
        ageScheduler.scheduleAtFixedRate(this::applyAging,
                agingIntervalMs, agingIntervalMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Submit an item with priority.
     */
    public void submit(T item, Priority priority) {
        PrioritizedItem<T> prioritized = new PrioritizedItem<>(
                item, priority, sequenceCounter.incrementAndGet());
        queue.offer(prioritized);
        submitCounts.get(priority).incrementAndGet();
        LOG.debug("Ã°Å¸â€œÂ¥ Submitted {} priority task, queue size: {}", priority, queue.size());
    }

    /**
     * Take the next highest priority item (blocking).
     */
    public T take() throws InterruptedException {
        PrioritizedItem<T> item = queue.take();
        completeCounts.get(item.getPriority()).incrementAndGet();
        return item.getItem();
    }

    /**
     * Poll the next highest priority item (non-blocking).
     */
    public T poll() {
        PrioritizedItem<T> item = queue.poll();
        if (item != null) {
            completeCounts.get(item.getPriority()).incrementAndGet();
            return item.getItem();
        }
        return null;
    }

    /**
     * Poll with timeout.
     */
    public T poll(long timeout, TimeUnit unit) throws InterruptedException {
        PrioritizedItem<T> item = queue.poll(timeout, unit);
        if (item != null) {
            completeCounts.get(item.getPriority()).incrementAndGet();
            return item.getItem();
        }
        return null;
    }

    /**
     * Apply aging to prevent starvation of low-priority items.
     */
    private void applyAging() {
        int boosted = 0;
        for (PrioritizedItem<T> item : queue) {
            if (item.getAge() > starvationThresholdMs && item.getEffectivePriority() > 0) {
                item.applyAgeBoost(1);
                boosted++;
            }
        }
        if (boosted > 0) {
            LOG.debug("Ã¢ÂÂ³ Applied age boost to {} items", boosted);
        }
    }

    /**
     * Get current queue size.
     */
    public int size() {
        return queue.size();
    }

    /**
     * Get queue size by priority.
     */
    public Map<Priority, Long> getSizeByPriority() {
        Map<Priority, Long> sizes = new EnumMap<>(Priority.class);
        for (Priority p : Priority.values()) {
            sizes.put(p, queue.stream()
                    .filter(item -> item.getPriority() == p)
                    .count());
        }
        return sizes;
    }

    /**
     * Get submit statistics.
     */
    public Map<Priority, Long> getSubmitStats() {
        Map<Priority, Long> stats = new EnumMap<>(Priority.class);
        submitCounts.forEach((k, v) -> stats.put(k, v.get()));
        return stats;
    }

    /**
     * Get completion statistics.
     */
    public Map<Priority, Long> getCompleteStats() {
        Map<Priority, Long> stats = new EnumMap<>(Priority.class);
        completeCounts.forEach((k, v) -> stats.put(k, v.get()));
        return stats;
    }

    /**
     * Check if queue is empty.
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Get the aging interval in milliseconds.
     * 
     * @return aging interval configuration
     */
    public long getAgingIntervalMs() {
        return agingIntervalMs;
    }

    /**
     * Get the starvation threshold in milliseconds.
     * 
     * @return starvation threshold configuration
     */
    public long getStarvationThresholdMs() {
        return starvationThresholdMs;
    }

    /**
     * Shutdown the queue manager.
     */
    public void shutdown() {
        ageScheduler.shutdown();
        LOG.info("Priority queue manager shutdown");
    }
}
