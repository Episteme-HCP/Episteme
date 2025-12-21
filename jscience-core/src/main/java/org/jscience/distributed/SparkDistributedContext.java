/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.distributed;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Apache Spark-based implementation of DistributedContext.
 * <p>
 * This class integrates JScience with Apache Spark for large-scale
 * distributed computing.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SparkDistributedContext implements DistributedContext {

    private final JavaSparkContext sparkContext;

    /**
     * Creates a Spark distributed context.
     *
     * @param sparkConf Spark configuration
     */
    public SparkDistributedContext(Object sparkConf) {
        if (!(sparkConf instanceof SparkConf)) {
            throw new IllegalArgumentException("Configuration must be instance of org.apache.spark.SparkConf");
        }
        this.sparkContext = new JavaSparkContext((SparkConf) sparkConf);
    }

    @Override
    public <T extends Serializable> Future<T> submit(Callable<T> task) {
        // Spark is not optimized for single-task submission.
        // We wrap it in a single-element RDD to execute continuously,
        // effectively treating the cluster as a heavy executor.
        // Ideally, users should use invokeAll for batching.
        return CompletableFuture.supplyAsync(() -> {
            List<T> result = sparkContext.parallelize(java.util.Collections.singletonList(task))
                    .map(t -> t.call())
                    .collect();
            if (result.isEmpty())
                throw new RuntimeException("Task yielded no result");
            return result.get(0);
        });
    }

    @Override
    public <T extends Serializable> List<Future<T>> invokeAll(List<Callable<T>> tasks) {
        // execute efficiently in parallel using RDD map
        // Note: Callable::call is executed on workers.
        List<T> results = sparkContext.parallelize(tasks,
                tasks.size() > sparkContext.defaultParallelism() ? tasks.size() : sparkContext.defaultParallelism())
                .map(task -> task.call())
                .collect();

        // Wrap results in completed futures to satisfy interface
        return results.stream()
                .map(CompletableFuture::completedFuture)
                .collect(Collectors.toList());
    }

    @Override
    public int getParallelism() {
        return sparkContext.defaultParallelism();
    }

    @Override
    public void shutdown() {
        sparkContext.close();
    }
}
