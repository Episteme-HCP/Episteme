package org.jscience.core.technical.algorithm;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.jscience.core.mathematics.numbers.real.Real;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BenchmarkRunner {
    private static final Logger logger = LoggerFactory.getLogger(BenchmarkRunner.class);
    private static final int[] SIZES = {128, 512, 1024};
    private static final int WARMUP = 1; // Reduced for speed
    private static final int ITERATIONS = 2; // Reduced for speed

    public static void runAll() {
        if (Boolean.getBoolean("jscience.benchmark.skip")) return;
        logger.info("Starting Linear Algebra Auto-Benchmark...");
        @SuppressWarnings({ "unchecked", "rawtypes" })
        List<LinearAlgebraProvider<Real>> providers = (List) AlgorithmManager.getProviders(LinearAlgebraProvider.class);
        List<BenchmarkResult> results = new ArrayList<>();

        for (LinearAlgebraProvider<Real> provider : providers) {
            if (!provider.isAvailable()) continue;
            
            logger.info("Benchmarking provider: {} ({})", provider.getName(), provider.getEnvironmentInfo());
            Map<Integer, Double> gflopsMap = new LinkedHashMap<>();
            
            try {
                for (int size : SIZES) {
                    double gflops = benchmarkMultiply(provider, size);
                    gflopsMap.put(size, gflops);
                    logger.info("  Size {}: {} GFLOPS", size, String.format("%.2f", gflops));
                }
                results.add(new BenchmarkResult(provider.getName(), provider.getEnvironmentInfo(), gflopsMap));
            } catch (Throwable t) {
                logger.warn("  Failed to benchmark {}: {}", provider.getName(), t.getMessage());
            }
        }

        saveResults(results);
    }

    private static double benchmarkMultiply(LinearAlgebraProvider<Real> provider, int n) {
        // Prepare data
        double[] aData = new double[n * n];
        double[] bData = new double[n * n];
        Random rand = new Random(42);
        for (int i = 0; i < aData.length; i++) {
            aData[i] = rand.nextDouble();
            bData[i] = rand.nextDouble();
        }
        Matrix<Real> A = RealDoubleMatrix.of(aData, n, n);
        Matrix<Real> B = RealDoubleMatrix.of(bData, n, n);

        // Warmup
        for (int i = 0; i < WARMUP; i++) {
            provider.multiply(A, B);
        }

        // Measure
        long totalTime = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            long start = System.nanoTime();
            provider.multiply(A, B);
            totalTime += (System.nanoTime() - start);
        }

        double avgSeconds = (totalTime / (double) ITERATIONS) / 1_000_000_000.0;
        double operations = 2.0 * n * n * n; // Matrix multiply complexity
        return (operations / avgSeconds) / 1_000_000_000.0; // GFLOPS
    }

    private static void saveResults(List<BenchmarkResult> results) {
        try {
            Path path = Paths.get(System.getProperty("user.home"), ".jscience", "benchmarks.json");
            Files.createDirectories(path.getParent());
            
            ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(path.toFile(), results);
            logger.info("Benchmark results saved to {}", path);
        } catch (Exception e) {
            logger.error("Failed to save benchmark results", e);
        }
    }
}
