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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Data Partitioner for distributing work across workers.
 * 
 * Strategies:
 * - Round-robin
 * - Hash-based
 * - Range-based
 * - Custom partitioning
 */
public class DataPartitioner<T> {

    private static final Logger LOG = LoggerFactory.getLogger(DataPartitioner.class);

    /**
     * Partitioning strategy.
     */
    public enum Strategy {
        ROUND_ROBIN,
        HASH,
        RANGE,
        CUSTOM
    }

    private final int numPartitions;
    private final Strategy strategy;
    private Function<T, Integer> customPartitioner;

    public DataPartitioner(int numPartitions) {
        this(numPartitions, Strategy.ROUND_ROBIN);
    }

    public DataPartitioner(int numPartitions, Strategy strategy) {
        this.numPartitions = numPartitions;
        this.strategy = strategy;
    }

    /**
     * Set a custom partitioning function.
     */
    public DataPartitioner<T> withCustomPartitioner(Function<T, Integer> partitioner) {
        this.customPartitioner = partitioner;
        return this;
    }

    /**
     * Partition a collection into chunks.
     */
    public List<List<T>> partition(Collection<T> data) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < numPartitions; i++) {
            partitions.add(new ArrayList<>());
        }

        int index = 0;
        for (T item : data) {
            int partitionId = getPartitionId(item, index++);
            partitions.get(partitionId).add(item);
        }

        LOG.info("Ã°Å¸â€œÂ¦ Partitioned {} items into {} partitions: {}",
                data.size(), numPartitions,
                partitions.stream().map(List::size).collect(Collectors.toList()));

        return partitions;
    }

    /**
     * Partition a list maintaining order within partitions.
     */
    public List<List<T>> partitionOrdered(List<T> data) {
        int chunkSize = (int) Math.ceil((double) data.size() / numPartitions);

        List<List<T>> partitions = IntStream.range(0, numPartitions)
                .mapToObj(i -> {
                    int start = i * chunkSize;
                    int end = Math.min(start + chunkSize, data.size());
                    return start < data.size() ? new ArrayList<>(data.subList(start, end)) : new ArrayList<T>();
                })
                .collect(Collectors.toList());

        LOG.info("Ã°Å¸â€œÂ¦ Ordered partition of {} items into {} chunks", data.size(), numPartitions);
        return partitions;
    }

    /**
     * Partition by a key function (grouping).
     */
    public <K> Map<K, List<T>> partitionByKey(Collection<T> data, Function<T, K> keyExtractor) {
        Map<K, List<T>> grouped = data.stream()
                .collect(Collectors.groupingBy(keyExtractor));

        LOG.info("Ã°Å¸â€œÂ¦ Key-based partition: {} groups from {} items", grouped.size(), data.size());
        return grouped;
    }

    /**
     * Get partition ID for an item.
     */
    public int getPartitionId(T item, int index) {
        return switch (strategy) {
            case ROUND_ROBIN -> index % numPartitions;
            case HASH -> Math.abs(item.hashCode()) % numPartitions;
            case RANGE -> index * numPartitions / (index + 1); // Simplified range
            case CUSTOM -> customPartitioner != null
                    ? customPartitioner.apply(item) % numPartitions
                    : index % numPartitions;
        };
    }

    /**
     * Re-partition data with a new number of partitions.
     */
    public List<List<T>> repartition(List<List<T>> existing, int newNumPartitions) {
        List<T> flattened = existing.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        DataPartitioner<T> newPartitioner = new DataPartitioner<>(newNumPartitions, strategy);
        newPartitioner.customPartitioner = this.customPartitioner;

        return newPartitioner.partition(flattened);
    }

    /**
     * Balance partitions to have roughly equal sizes.
     */
    public List<List<T>> rebalance(List<List<T>> partitions) {
        List<T> all = partitions.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        Collections.shuffle(all); // Randomize to avoid skew
        return partitionOrdered(all);
    }

    // --- Static factory methods ---

    /**
     * Create a hash-based partitioner using a key extractor.
     */
    public static <T, K> DataPartitioner<T> byHash(int numPartitions, Function<T, K> keyExtractor) {
        DataPartitioner<T> partitioner = new DataPartitioner<>(numPartitions, Strategy.CUSTOM);
        partitioner.customPartitioner = item -> Math.abs(keyExtractor.apply(item).hashCode());
        return partitioner;
    }

    /**
     * Create a range-based partitioner for numeric data.
     */
    public static DataPartitioner<Number> byRange(int numPartitions, double min, double max) {
        DataPartitioner<Number> partitioner = new DataPartitioner<>(numPartitions, Strategy.CUSTOM);
        double range = max - min;
        partitioner.customPartitioner = num -> {
            double value = num.doubleValue();
            int partition = (int) ((value - min) / range * numPartitions);
            return Math.max(0, Math.min(numPartitions - 1, partition));
        };
        return partitioner;
    }

    public int getNumPartitions() {
        return numPartitions;
    }

    public Strategy getStrategy() {
        return strategy;
    }
}


