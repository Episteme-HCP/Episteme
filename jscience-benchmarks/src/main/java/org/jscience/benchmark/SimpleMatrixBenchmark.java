package org.jscience.benchmark;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Benchmarks for JScience DenseMatrix Multiplication.
 * 
 * @author Silvere Martin-Michiellot
 */
public class SimpleMatrixBenchmark implements Benchmark {

    private static final int SIZE = 256;
    private Matrix<Real> A;
    private Matrix<Real> B;

    @Override
    public String getName() {
        return "DenseMatrix Mult (" + SIZE + "x" + SIZE + ")";
    }

    @Override
    public String getDomain() {
        return "Linear Algebra";
    }

    @Override
    public void setup() {
        A = createRandomMatrix(SIZE);
        B = createRandomMatrix(SIZE);
    }

    @Override
    public void run() {
        A.multiply(B);
    }

    @Override
    public void teardown() {
        A = null;
        B = null;
    }

    @Override
    public int getSuggestedIterations() {
        return 10;
    }

    private Matrix<Real> createRandomMatrix(int size) {
        Random r = new Random(42);
        List<List<Real>> rows = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            List<Real> row = new ArrayList<>(size);
            for (int j = 0; j < size; j++) {
                row.add(Real.of(r.nextDouble()));
            }
            rows.add(row);
        }
        return new DenseMatrix<>(rows, org.jscience.mathematics.sets.Reals.getInstance());
    }
}
