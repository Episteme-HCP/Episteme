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
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Stream Processor for real-time data pipelines.
 * 
 * Features:
 * - Map/Filter/Reduce operations
 * - Windowed aggregation
 * - Backpressure handling
 * - Parallel processing
 */
public class StreamProcessor<T> {

    private static final Logger LOG = LoggerFactory.getLogger(StreamProcessor.class);

    private final BlockingQueue<T> inputQueue;
    private final List<Stage<?, ?>> stages = new ArrayList<>();
    private final ExecutorService executor;
    private final int parallelism;
    private volatile boolean running = false;

    /**
     * Processing stage in the pipeline.
     */
    @FunctionalInterface
    public interface Stage<I, O> {
        O process(I input);
    }

    /**
     * Create stream processor with default parallelism (CPU cores).
     */
    public StreamProcessor() {
        this(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Create stream processor with custom parallelism.
     */
    public StreamProcessor(int parallelism) {
        this.parallelism = parallelism;
        this.inputQueue = new LinkedBlockingQueue<>(10000);
        this.executor = Executors.newFixedThreadPool(parallelism, r -> {
            Thread t = new Thread(r, "stream-processor-" + System.nanoTime());
            t.setDaemon(true);
            return t;
        });
    }

    /**
     * Add a map transformation.
     */
    public <R> StreamProcessor<R> flatMap(Function<T, Collection<R>> mapper) {
        stages.add((Stage<Object, Object>) input -> mapper.apply((T) input));
        return (StreamProcessor<R>) this;
    }

    /**
     * Submit data to the stream.
     */
    public boolean submit(T data) {
        return inputQueue.offer(data);
    }

    /**
     * Submit data with blocking if queue is full.
     */
    public void submitBlocking(T data) throws InterruptedException {
        inputQueue.put(data);
    }

    /**
     * Start processing with a consumer for results.
     */
    @SuppressWarnings("unchecked")
    public void start(Consumer<Object> resultConsumer) {
        running = true;
        LOG.info("Ã°Å¸Å¡â‚¬ Stream processor started with {} workers", parallelism);

        for (int i = 0; i < parallelism; i++) {
            executor.submit(() -> {
                while (running) {
                    try {
                        T item = inputQueue.poll(100, TimeUnit.MILLISECONDS);
                        if (item == null)
                            continue;

                        Object result = item;
                        for (Stage<?, ?> stage : stages) {
                            if (result == null)
                                break;

                            if (result instanceof Collection) {
                                // Handle flatMap results
                                List<Object> flattened = new ArrayList<>();
                                for (Object r : (Collection<?>) result) {
                                    Object processed = ((Stage<Object, Object>) stage).process(r);
                                    if (processed != null) {
                                        flattened.add(processed);
                                    }
                                }
                                result = flattened.isEmpty() ? null : flattened;
                            } else {
                                result = ((Stage<Object, Object>) stage).process(result);
                            }
                        }

                        if (result != null) {
                            if (result instanceof Collection) {
                                for (Object r : (Collection<?>) result) {
                                    resultConsumer.accept(r);
                                }
                            } else {
                                resultConsumer.accept(result);
                            }
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    } catch (Exception e) {
                        LOG.error("Stream processing error", e);
                    }
                }
            });
        }
    }

    /**
     * Stop the stream processor.
     */
    public void stop() {
        running = false;
        executor.shutdown();
        LOG.info("Stream processor stopped");
    }

    /**
     * Get current queue size.
     */
    public int getQueueSize() {
        return inputQueue.size();
    }

    /**
     * Check if processor is running.
     */
    public boolean isRunning() {
        return running;
    }

    // --- Static factory methods ---

    /**
     * Create a stream processor from an iterator source.
     */
    public static <T> StreamProcessor<T> fromIterator(Iterator<T> source, int batchSize) {
        StreamProcessor<T> processor = new StreamProcessor<>();
        Thread feeder = new Thread(() -> {
            int count = 0;
            while (source.hasNext()) {
                try {
                    processor.submitBlocking(source.next());
                    count++;
                    if (count % batchSize == 0) {
                        LOG.debug("Fed {} items to stream", count);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            LOG.info("Finished feeding {} items to stream", count);
        }, "stream-feeder");
        feeder.setDaemon(true);
        feeder.start();
        return processor;
    }
}
