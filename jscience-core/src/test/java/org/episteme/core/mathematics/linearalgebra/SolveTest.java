package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.linearalgebra.providers.StrassenLinearAlgebraProvider;

public class SolveTest {
    public static void main(String[] args) {
        double[][] dataA = {{2, 1}, {1, 2}};
        double[] dataB = {3, 3};
        
        RealDoubleMatrix A = RealDoubleMatrix.of(dataA);
        RealDoubleVector B = RealDoubleVector.of(dataB);
        
        StrassenLinearAlgebraProvider<Real> provider = new StrassenLinearAlgebraProvider<>();
        try {
            Vector<Real> x = provider.solve(A, B);
            System.out.println("Result: " + x);
            for (int i = 0; i < x.dimension(); i++) {
                System.out.println("x[" + i + "] = " + x.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
