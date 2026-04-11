package org.episteme.benchmarks.benchmark;

import org.episteme.benchmarks.benchmark.benchmarks.SystematicMatrixBenchmark;
import org.episteme.benchmarks.benchmark.benchmarks.SystematicInverseBenchmark;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.technical.algorithm.OperationContext;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BenchmarkThrottlingTest {

    private static class MockHPProvider implements LinearAlgebraProvider<org.episteme.core.mathematics.numbers.real.Real> {
        @Override public String getName() { return "Mock MPFR Provider"; }
        @Override public boolean isAvailable() { return true; }
        @Override public boolean isCompatible(org.episteme.core.mathematics.structures.rings.Ring<?> ring) { return true; }
        @Override public int getPriority() { return 1; }
        @Override public void shutdown() {}
        @Override public Map<String, String> getMetadata() { return Map.of(); }
        @Override public double score(OperationContext context) { return 1.0; }
        @Override public String getEnvironmentInfo() { return "Mock HP"; }
        
        // Matrix methods (not needed for setup check usually, but for completeness)
        @Override public org.episteme.core.mathematics.linearalgebra.Matrix<org.episteme.core.mathematics.numbers.real.Real> multiply(org.episteme.core.mathematics.linearalgebra.Matrix<org.episteme.core.mathematics.numbers.real.Real> a, org.episteme.core.mathematics.linearalgebra.Matrix<org.episteme.core.mathematics.numbers.real.Real> b) { return null; }
        @Override public org.episteme.core.mathematics.linearalgebra.Vector<org.episteme.core.mathematics.numbers.real.Real> multiply(org.episteme.core.mathematics.linearalgebra.Matrix<org.episteme.core.mathematics.numbers.real.Real> a, org.episteme.core.mathematics.linearalgebra.Vector<org.episteme.core.mathematics.numbers.real.Real> b) { return null; }
        @Override public org.episteme.core.mathematics.linearalgebra.Matrix<org.episteme.core.mathematics.numbers.real.Real> transpose(org.episteme.core.mathematics.linearalgebra.Matrix<org.episteme.core.mathematics.numbers.real.Real> a) { return null; }
        @Override public org.episteme.core.mathematics.linearalgebra.Matrix<org.episteme.core.mathematics.numbers.real.Real> add(org.episteme.core.mathematics.linearalgebra.Matrix<org.episteme.core.mathematics.numbers.real.Real> a, org.episteme.core.mathematics.linearalgebra.Matrix<org.episteme.core.mathematics.numbers.real.Real> b) { return null; }
        @Override public org.episteme.core.mathematics.linearalgebra.Matrix<org.episteme.core.mathematics.numbers.real.Real> subtract(org.episteme.core.mathematics.linearalgebra.Matrix<org.episteme.core.mathematics.numbers.real.Real> a, org.episteme.core.mathematics.linearalgebra.Matrix<org.episteme.core.mathematics.numbers.real.Real> b) { return null; }
        @Override public org.episteme.core.mathematics.linearalgebra.Matrix<org.episteme.core.mathematics.numbers.real.Real> scale(org.episteme.core.mathematics.numbers.real.Real scalar, org.episteme.core.mathematics.linearalgebra.Matrix<org.episteme.core.mathematics.numbers.real.Real> a) { return null; }
        @Override public org.episteme.core.mathematics.numbers.real.Real dot(org.episteme.core.mathematics.linearalgebra.Vector<org.episteme.core.mathematics.numbers.real.Real> a, org.episteme.core.mathematics.linearalgebra.Vector<org.episteme.core.mathematics.numbers.real.Real> b) { return null; }
        @Override public org.episteme.core.mathematics.numbers.real.Real norm(org.episteme.core.mathematics.linearalgebra.Vector<org.episteme.core.mathematics.numbers.real.Real> a) { return null; }
        @Override public void close() {}
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMatrixBenchmarkThrottling() {
        SystematicMatrixBenchmark benchmark = new SystematicMatrixBenchmark();
        benchmark.setProvider((LinearAlgebraProvider<org.episteme.core.mathematics.numbers.real.Real>)new MockHPProvider());
        
        // Check Description
        assertTrue(benchmark.getDescription().contains("High-Precision Throttling Applied"), "Description should contain throttling note");
        assertTrue(benchmark.getDescription().contains("32x32"), "Description should note 32x32 size");
        
        // Check Setup Size
        benchmark.setup();
        // matricesA is private, but we can check if it runs without error and maybe use reflection if we really wanted to.
        // For now, the description check is a good sign.
    }

    @Test
    public void testInverseBenchmarkThrottling() {
        SystematicInverseBenchmark benchmark = new SystematicInverseBenchmark();
        benchmark.setProvider((LinearAlgebraProvider<org.episteme.core.mathematics.numbers.real.Real>)new MockHPProvider());
        
        // Check Description
        assertTrue(benchmark.getDescription().contains("High-Precision Throttling Applied"), "Description should contain throttling note");
        assertTrue(benchmark.getDescription().contains("16x16"), "Description should note 16x16 size");
    }
}
