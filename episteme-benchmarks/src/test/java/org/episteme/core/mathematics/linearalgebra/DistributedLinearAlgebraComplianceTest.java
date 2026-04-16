package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.distributed.DistributedCompute;
import org.episteme.core.mathematics.linearalgebra.algorithms.MatrixMultiplicationPlanner;
import org.episteme.core.mathematics.linearalgebra.matrices.TiledMatrix;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.technical.algorithm.AlgorithmManager;
import org.episteme.core.technical.algorithm.TestingAlgorithmService;
import org.episteme.core.technical.backend.distributed.DistributedContext;
import org.episteme.core.distributed.LocalDistributedContext;
import org.episteme.core.mathematics.linearalgebra.providers.DistributedLinearAlgebraProvider;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Dedicated compliance test for Distributed Linear Algebra algorithms.
 */
public class DistributedLinearAlgebraComplianceTest {

    private static final String REPORT_PATH = "../docs/DISTRIBUTED_ALGEBRA_COMPLIANCE_REPORT.md";
    private static final int MATRIX_SIZE = 64; // Size enough to warrant tiling
    private static final int TILE_SIZE = 16;   // 4x4 tiles

    @Test
    public void generateDistributedReport() throws IOException {
        List<DistributedAlgorithmResult> results = new ArrayList<>();
        
        // Setup Distributed Context
        LocalDistributedContext ctx = new LocalDistributedContext(4); // 4 nodes (2x2 grid)
        DistributedCompute.setContext(ctx);
        
        try {
            DistributedLinearAlgebraProvider<Real> provider = new DistributedLinearAlgebraProvider<>();
            Ring<Real> ring = org.episteme.core.mathematics.sets.Reals.getInstance();
            
            // Algorithms to test
            MatrixMultiplicationPlanner.Algorithm[] algorithms = {
                MatrixMultiplicationPlanner.Algorithm.SUMMA,
                MatrixMultiplicationPlanner.Algorithm.CANNON,
                MatrixMultiplicationPlanner.Algorithm.FOX,
                MatrixMultiplicationPlanner.Algorithm.CARMA,
                MatrixMultiplicationPlanner.Algorithm.ALGORITHM_25D
            };

            for (MatrixMultiplicationPlanner.Algorithm algo : algorithms) {
                System.out.println("Testing distributed algorithm: " + algo);
                DistributedAlgorithmResult res = new DistributedAlgorithmResult(algo.name());
                
                System.setProperty("org.episteme.multiply.algorithm", algo.name());
                
                try {
                    testMultiply(provider, ring, res);
                    res.status = "✅ PASS";
                } catch (Throwable t) {
                    res.status = "❌ FAIL (" + t.getMessage() + ")";
                    t.printStackTrace();
                }
                
                results.add(res);
            }
        } finally {
            printMarkdownReport(results);
            System.clearProperty("org.episteme.multiply.algorithm");
            AlgorithmManager.getService().shutdown();
        }
    }

    private void testMultiply(DistributedLinearAlgebraProvider<Real> provider, Ring<Real> ring, DistributedAlgorithmResult res) {
        Random rand = new Random(42);
        TiledMatrix<Real> A = randomTiledMatrix(MATRIX_SIZE, MATRIX_SIZE, rand, ring);
        TiledMatrix<Real> B = randomTiledMatrix(MATRIX_SIZE, MATRIX_SIZE, rand, ring);
        
        Matrix<Real> C = provider.multiply(A, B);
        
        // Verify against local multiplication
        Matrix<Real> expected = A.multiply(B);
        verifyMatrix(expected, C, 1e-12, ring);
    }

    private TiledMatrix<Real> randomTiledMatrix(int rows, int cols, Random rand, Ring<Real> ring) {
        Real[][] data = new Real[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = Real.of(rand.nextDouble());
            }
        }
        Matrix<Real> raw = Matrix.of(data, ring);
        return new TiledMatrix<>(raw, TILE_SIZE, TILE_SIZE);
    }

    private void verifyMatrix(Matrix<Real> expected, Matrix<Real> actual, double tol, Ring<Real> ring) {
        for (int i = 0; i < expected.rows(); i++) {
            for (int j = 0; j < expected.cols(); j++) {
                double diff = expected.get(i, j).subtract(actual.get(i, j)).abs().doubleValue();
                assertTrue(diff < tol, "Mismatch at (" + i + "," + j + "): expected " + expected.get(i, j) + " but got " + actual.get(i, j));
            }
        }
    }

    private void printMarkdownReport(List<DistributedAlgorithmResult> results) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("# Distributed Linear Algebra Algorithms Compliance Report\n\n");
        sb.append("| Algorithm | Status | Details |\n");
        sb.append("| --- | --- | --- |\n");
        for (DistributedAlgorithmResult res : results) {
            sb.append("| ").append(res.name).append(" | ").append(res.status).append(" | ").append(res.details).append(" |\n");
        }
        sb.append("\n*Generated on ").append(new Date()).append("*\n");
        
        String report = sb.toString();
        Files.createDirectories(Paths.get(REPORT_PATH).getParent());
        Files.writeString(Paths.get(REPORT_PATH), report);
        System.out.println(report);
    }

    private static class DistributedAlgorithmResult {
        String name;
        String status;
        String details = "N/A";
        DistributedAlgorithmResult(String name) { this.name = name; }
    }
}
