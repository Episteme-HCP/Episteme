package org.episteme.core.mathematics.linearalgebra;
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenDecomposition;
import org.episteme.core.mathematics.numbers.real.Real;
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
        org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<Real> res = EigenDecomposition.decompose(a);
        for (int i = 0; i < res.getEigenvalues().dimension(); i++) {
            Real r = res.getEigenvalues().get(i);
            System.out.println("Eig: " + r);
        }
    }
}
