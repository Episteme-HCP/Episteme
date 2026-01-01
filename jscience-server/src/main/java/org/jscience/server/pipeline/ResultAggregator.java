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

package org.jscience.server.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

/**
 * Result Aggregator for distributed Map-Reduce operations.
 * 
 * Collects partial results from distributed workers and combines them.
 * Supports:
 * - Sum, Count, Average aggregations
 * - Custom reducers
 * - Grouped aggregations
 */
public class ResultAggregator<K, V> {

    private static final Logger LOG = LoggerFactory.getLogger(ResultAggregator.class);

    private final ConcurrentHashMap<K, List<V>> partialResults = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<K, Integer> expectedCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<K, CompletableFuture<V>> completionFutures = new ConcurrentHashMap<>();
    private final BinaryOperator<V> reducer;

    /**
     * Create aggregator with a reducer function.
     */
    public ResultAggregator(BinaryOperator<V> reducer) {
        this.reducer = reducer;
    }

    /**
     * Register a new aggregation task with expected number of partial results.
     */
    public CompletableFuture<V> register(K taskId, int expectedCount) {
        partialResults.put(taskId, Collections.synchronizedList(new ArrayList<>()));
        expectedCounts.put(taskId, expectedCount);
        CompletableFuture<V> future = new CompletableFuture<>();
        completionFutures.put(taskId, future);
        LOG.debug("Ã°Å¸â€œâ€¹ Registered aggregation: {} expecting {} results", taskId, expectedCount);
        return future;
    }

    /**
     * Submit a partial result.
     */
    public void submit(K taskId, V partialResult) {
        List<V> results = partialResults.get(taskId);
        if (results == null) {
            LOG.warn("Unknown task ID: {}", taskId);
            return;
        }

        results.add(partialResult);
        int received = results.size();
        int expected = expectedCounts.getOrDefault(taskId, 0);

        LOG.debug("Ã°Å¸â€œÂ¥ Received partial result {}/{} for task {}", received, expected, taskId);

        if (received >= expected) {
            // All results received, aggregate
            try {
                V finalResult = aggregate(taskId);
                completionFutures.get(taskId).complete(finalResult);
                LOG.info("Ã¢Å“â€¦ Aggregation complete for task {}", taskId);
            } catch (Exception e) {
                completionFutures.get(taskId).completeExceptionally(e);
                LOG.error("Ã¢ÂÅ’ Aggregation failed for task {}", taskId, e);
            }
        }
    }

    /**
     * Aggregate all partial results for a task.
     */
    public V aggregate(K taskId) {
        List<V> results = partialResults.get(taskId);
        if (results == null || results.isEmpty()) {
            return null;
        }

        V aggregated = results.stream().reduce(reducer).orElse(null);

        // Cleanup
        partialResults.remove(taskId);
        expectedCounts.remove(taskId);

        return aggregated;
    }

    /**
     * Get completion future for a task.
     */
    public CompletableFuture<V> getFuture(K taskId) {
        return completionFutures.get(taskId);
    }

    /**
     * Check if aggregation is complete for a task.
     */
    public boolean isComplete(K taskId) {
        CompletableFuture<V> future = completionFutures.get(taskId);
        return future != null && future.isDone();
    }

    /**
     * Get current progress for a task.
     */
    public int getProgress(K taskId) {
        List<V> results = partialResults.get(taskId);
        int expected = expectedCounts.getOrDefault(taskId, 1);
        int received = results != null ? results.size() : 0;
        return (int) ((received * 100.0) / expected);
    }

    // --- Static factory methods for common aggregations ---

    /**
     * Create a sum aggregator for numbers.
     */
    public static ResultAggregator<String, Double> sumAggregator() {
        return new ResultAggregator<>((a, b) -> (a != null ? a : 0.0) + (b != null ? b : 0.0));
    }

    /**
     * Create a count aggregator.
     */
    public static ResultAggregator<String, Long> countAggregator() {
        return new ResultAggregator<>((a, b) -> (a != null ? a : 0L) + (b != null ? b : 0L));
    }

    /**
     * Create a max aggregator.
     */
    public static <V extends Comparable<V>> ResultAggregator<String, V> maxAggregator() {
        return new ResultAggregator<>((a, b) -> a.compareTo(b) >= 0 ? a : b);
    }

    /**
     * Create a min aggregator.
     */
    public static <V extends Comparable<V>> ResultAggregator<String, V> minAggregator() {
        return new ResultAggregator<>((a, b) -> a.compareTo(b) <= 0 ? a : b);
    }

    /**
     * Create a list aggregator (concatenates all results).
     */
    public static <T> ResultAggregator<String, List<T>> listAggregator() {
        return new ResultAggregator<>((a, b) -> {
            List<T> combined = new ArrayList<>(a);
            combined.addAll(b);
            return combined;
        });
    }

    /**
     * Create a map aggregator (merges all maps).
     */
    public static <K2, V2> ResultAggregator<String, Map<K2, V2>> mapAggregator(BiFunction<V2, V2, V2> mergeFunction) {
        return new ResultAggregator<>((a, b) -> {
            Map<K2, V2> combined = new HashMap<>(a);
            b.forEach((key, value) -> combined.merge(key, value, mergeFunction));
            return combined;
        });
    }
}


