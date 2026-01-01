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
 * Task scheduling and queue management constants.
 * 
 * Defines thresholds, scoring weights, and timing parameters
 * for the task scheduling system.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class SchedulingConstants {

    private SchedulingConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }

    // === Scheduling Weights ===

    /**
     * Weight for CPU availability in scheduling score (0.0 - 1.0).
     */
    public static final double WEIGHT_CPU = 0.30;

    /**
     * Weight for memory availability in scheduling score (0.0 - 1.0).
     */
    public static final double WEIGHT_MEMORY = 0.25;

    /**
     * Weight for GPU availability in scheduling score (0.0 - 1.0).
     */
    public static final double WEIGHT_GPU = 0.20;

    /**
     * Weight for data locality in scheduling score (0.0 - 1.0).
     */
    public static final double WEIGHT_DATA_LOCALITY = 0.15;

    /**
     * Weight for worker labels match in scheduling score (0.0 - 1.0).
     */
    public static final double WEIGHT_LABELS = 0.10;

    // === Task Aging ===

    /**
     * Interval at which task priorities are aged/boosted.
     */
    public static final Duration AGING_INTERVAL = Duration.ofMinutes(1);

    /**
     * Priority boost per aging cycle for waiting tasks.
     */
    public static final int AGING_PRIORITY_BOOST = 1;

    /**
     * Maximum priority boost from aging.
     */
    public static final int MAX_AGING_BOOST = 10;

    // === Starvation Prevention ===

    /**
     * Time threshold after which a task is considered starving.
     */
    public static final Duration STARVATION_THRESHOLD = Duration.ofMinutes(5);

    /**
     * Priority boost for starving tasks.
     */
    public static final int STARVATION_PRIORITY_BOOST = 20;

    // === Queue Management ===

    /**
     * Maximum queue size before rejecting new tasks.
     */
    public static final int MAX_QUEUE_SIZE = 10000;

    /**
     * Queue size warning threshold (percentage).
     */
    public static final double QUEUE_WARNING_THRESHOLD = 0.75;

    /**
     * Queue size critical threshold (percentage).
     */
    public static final double QUEUE_CRITICAL_THRESHOLD = 0.90;

    // === Task Execution ===

    /**
     * Default task execution timeout.
     */
    public static final Duration DEFAULT_TASK_TIMEOUT = Duration.ofHours(1);

    /**
     * Maximum task execution timeout.
     */
    public static final Duration MAX_TASK_TIMEOUT = Duration.ofHours(24);

    /**
     * Interval for checking task timeout.
     */
    public static final Duration TIMEOUT_CHECK_INTERVAL = Duration.ofSeconds(30);

    // === Worker Health Checks ===

    /**
     * Interval between worker health checks.
     */
    public static final Duration HEALTH_CHECK_INTERVAL = Duration.ofSeconds(30);

    /**
     * Timeout for worker health check response.
     */
    public static final Duration HEALTH_CHECK_TIMEOUT = Duration.ofSeconds(5);

    /**
     * Number of failed health checks before marking worker unhealthy.
     */
    public static final int HEALTH_CHECK_FAILURE_THRESHOLD = 3;

    // === Affinity Scores ===

    /**
     * Perfect affinity score.
     */
    public static final double AFFINITY_PERFECT = 1.0;

    /**
     * Good affinity score.
     */
    public static final double AFFINITY_GOOD = 0.75;

    /**
     * Acceptable affinity score.
     */
    public static final double AFFINITY_ACCEPTABLE = 0.50;

    /**
     * Poor affinity score.
     */
    public static final double AFFINITY_POOR = 0.25;

    /**
     * Minimum acceptable affinity score for task assignment.
     */
    public static final double AFFINITY_MIN_THRESHOLD = 0.10;

    // === Resource Thresholds ===

    /**
     * CPU oversubscription limit (ratio).
     * 1.0 = no oversubscription, 2.0 = allow 2x oversubscription.
     */
    public static final double CPU_OVERSUBSCRIPTION_LIMIT = 1.5;

    /**
     * Memory oversubscription limit (ratio).
     */
    public static final double MEMORY_OVERSUBSCRIPTION_LIMIT = 1.0;

    /**
     * Minimum free memory percentage to keep on worker.
     */
    public static final double MIN_FREE_MEMORY_PERCENT = 0.10;
}
