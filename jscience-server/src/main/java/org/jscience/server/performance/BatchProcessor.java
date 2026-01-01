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

package org.jscience.server.performance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Batch Task Processor for efficient small task handling.
 * 
 * Groups small tasks together to reduce overhead.
 * Features:
 * - Configurable batch size and timeout
 * - Async batch processing
 * - Batch result distribution
 */
public class BatchProcessor<T, R> {

    private static final Logger LOG = LoggerFactory.getLogger(BatchProcessor.class);

    private final int batchSize;
    private final long batchTimeoutMs;
    private final Function<List<T>, List<R>> batchHandler;
    private final BlockingQueue<BatchItem<T, R>> queue;
    private final ScheduledExecutorService scheduler;
    private volatile boolean running = true;

    /**
     * Item wrapper with completion future.
     */
    private static class BatchItem<T, R> {
        final T item;
        final CompletableFuture<R> future;

        BatchItem(T item) {
            this.item = item;
            this.future = new CompletableFuture<>();
        }
    }

    /**
     * Create batch processor with defaults (100 items, 100ms timeout).
     */
    public BatchProcessor(Function<List<T>, List<R>> batchHandler) {
        this(100, 100, batchHandler);
    }

    /**
     * Create batch processor with custom settings.
     */
    public BatchProcessor(int batchSize, long batchTimeoutMs, Function<List<T>, List<R>> batchHandler) {
        this.batchSize = batchSize;
        this.batchTimeoutMs = batchTimeoutMs;
        this.batchHandler = batchHandler;
        this.queue = new LinkedBlockingQueue<>();
        this.scheduler = Executors.newScheduledThreadPool(2, r -> {
            Thread t = new Thread(r, "batch-processor");
            t.setDaemon(true);
            return t;
        });

        start();
    }

    private void start() {
        // Batch processing thread
        scheduler.submit(() -> {
            List<BatchItem<T, R>> batch = new ArrayList<>();
            long lastFlush = System.currentTimeMillis();

            while (running) {
                try {
                    BatchItem<T, R> item = queue.poll(10, TimeUnit.MILLISECONDS);
                    if (item != null) {
                        batch.add(item);
                    }

                    boolean shouldFlush = batch.size() >= batchSize ||
                            (batch.size() > 0 && System.currentTimeMillis() - lastFlush >= batchTimeoutMs);

                    if (shouldFlush) {
                        processBatch(new ArrayList<>(batch));
                        batch.clear();
                        lastFlush = System.currentTimeMillis();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            // Process remaining
            if (!batch.isEmpty()) {
                processBatch(batch);
            }
        });

        LOG.info("Ã°Å¸â€œÂ¦ Batch processor started (size: {}, timeout: {}ms)", batchSize, batchTimeoutMs);
    }

    private void processBatch(List<BatchItem<T, R>> batch) {
        try {
            List<T> items = batch.stream().map(b -> b.item).toList();
            List<R> results = batchHandler.apply(items);

            // Match results to futures
            for (int i = 0; i < batch.size(); i++) {
                if (i < results.size()) {
                    batch.get(i).future.complete(results.get(i));
                } else {
                    batch.get(i).future.complete(null);
                }
            }

            LOG.debug("Ã°Å¸â€œÂ¦ Processed batch of {} items", batch.size());
        } catch (Exception e) {
            LOG.error("Batch processing failed", e);
            batch.forEach(b -> b.future.completeExceptionally(e));
        }
    }

    /**
     * Submit an item for batch processing.
     */
    public CompletableFuture<R> submit(T item) {
        BatchItem<T, R> batchItem = new BatchItem<>(item);
        queue.offer(batchItem);
        return batchItem.future;
    }

    /**
     * Submit multiple items at once.
     */
    public List<CompletableFuture<R>> submitAll(List<T> items) {
        return items.stream().map(this::submit).toList();
    }

    /**
     * Submit and wait for result (blocking).
     */
    public R submitAndWait(T item, long timeoutMs) throws TimeoutException {
        try {
            return submit(item).get(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Batch processing failed", e);
        }
    }

    /**
     * Get current queue size.
     */
    public int getQueueSize() {
        return queue.size();
    }

    /**
     * Shutdown the processor.
     */
    public void shutdown() {
        running = false;
        scheduler.shutdown();
        try {
            scheduler.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        LOG.info("Batch processor shutdown");
    }

    // --- Static factory methods ---

    /**
     * Create a batch processor for void results (fire-and-forget).
     */
    public static <T> BatchProcessor<T, Void> fireAndForget(int batchSize, Consumer<List<T>> handler) {
        return new BatchProcessor<>(batchSize, 100, items -> {
            handler.accept(items);
            return Collections.nCopies(items.size(), null);
        });
    }

    /**
     * Create a batch processor for database inserts.
     */
    public static <T> BatchProcessor<T, Boolean> forDatabaseInserts(
            int batchSize, Function<List<T>, Integer> insertHandler) {
        return new BatchProcessor<>(batchSize, 50, items -> {
            int inserted = insertHandler.apply(items);
            List<Boolean> results = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                results.add(i < inserted);
            }
            return results;
        });
    }
}


