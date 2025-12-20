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
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
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
