package org.jscience.core.mathematics.linearalgebra;
import org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.jscience.core.mathematics.linearalgebra.matrices.solvers.EigenDecomposition;
import org.jscience.core.mathematics.numbers.real.Real;
import java.util.Random;
public class RunTest {
    public static void main(String[] args) {
        Random rand = new Random(42);
        double[][] data = new double[10][10];
        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) data[i][j] = rand.nextDouble();
        }
        for (int i=0; i<10; i++) {
            for (int j=i; j<10; j++) { double v = data[i][j]+data[j][i]; data[i][j]=v; data[j][i]=v; }
        }
        RealDoubleMatrix a = RealDoubleMatrix.of(data);
        EigenDecomposition res = EigenDecomposition.decompose(a);
        for(Real r : res.getEigenvalues()) { System.out.println("Eig: " + r); }
    }
}
