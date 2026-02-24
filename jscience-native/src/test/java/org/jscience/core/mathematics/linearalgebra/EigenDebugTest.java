package org.jscience.core.mathematics.linearalgebra;

import org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.jscience.core.mathematics.linearalgebra.matrices.solvers.EigenDecomposition;
import org.jscience.core.mathematics.numbers.real.Real;
import org.junit.jupiter.api.Test;
import java.util.Random;

public class EigenDebugTest {
    public static void main(String[] args) {
        new EigenDebugTest().testNaN();
    }
    
    @Test
    public void testNaN() {
        int SIZE = 50;
        Random rand = new Random(42);
        double[][] data = new double[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                double val = (rand.nextDouble() < 0.2) ? 0.0 : rand.nextDouble() * 2 - 1;
                data[i][j] = val;
            }
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = i; j < SIZE; j++) {
                double val = data[i][j] + data[j][i];
                data[i][j] = val;
                data[j][i] = val;
            }
        }
        RealDoubleMatrix a = RealDoubleMatrix.of(data);
        EigenDecomposition res = EigenDecomposition.decompose(a);
        
        StringBuilder sb = new StringBuilder("EIGENVALUES:\n");
        for(Real val : res.getEigenvalues()) {
            sb.append("Eig: ").append(val).append("\n");
        }
        throw new RuntimeException(sb.toString());
    }
}
