import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.episteme.core.mathematics.numbers.real.Real;
import org.junit.jupiter.api.Test;
import java.util.ServiceLoader;
import java.util.Random;

public class BenchQuant {
    @Test
    public void runBench() {
        int SIZE = 1024;
        int ITERS = 5;
        System.out.println("Episteme Performance Quantification (1024x1024 Matrix Multiply)");
        System.out.println("===============================================================");
        
        Random r = new Random(42);
        double[][] dataA = new double[SIZE][SIZE];
        double[][] dataB = new double[SIZE][SIZE];
        for(int i=0; i<SIZE; i++) for(int j=0; j<SIZE; j++) {
            dataA[i][j] = r.nextDouble();
            dataB[i][j] = r.nextDouble();
        }
        
        RealDoubleMatrix A = RealDoubleMatrix.of(dataA);
        RealDoubleMatrix B = RealDoubleMatrix.of(dataB);
        
        @SuppressWarnings("rawtypes")
        ServiceLoader<LinearAlgebraProvider> loader = ServiceLoader.load(LinearAlgebraProvider.class);
        for (LinearAlgebraProvider<?> p : loader) {
            if (!p.isCompatible(org.episteme.core.mathematics.sets.Reals.getInstance())) continue;
            if (!p.isAvailable()) {
                System.out.println("Backend [" + p.getName() + "] is NOT available. Skipping.");
                continue;
            }
            
            System.out.println("Measuring [" + p.getName() + "]...");
            
            // Warmup
            try {
                @SuppressWarnings("unchecked")
                LinearAlgebraProvider<Real> pReal = (LinearAlgebraProvider<Real>) p;
                for(int i=0; i<2; i++) pReal.multiply((Matrix<Real>)A, (Matrix<Real>)B);
            } catch (Throwable t) {
                System.out.println("  Failed during warmup: " + t.getMessage());
                continue;
            }
            
            long start = System.currentTimeMillis();
            try {
                @SuppressWarnings("unchecked")
                LinearAlgebraProvider<Real> pReal = (LinearAlgebraProvider<Real>) p;
                for(int i=0; i<ITERS; i++) {
                   pReal.multiply((Matrix<Real>)A, (Matrix<Real>)B);
                }
                long end = System.currentTimeMillis();
                double avg = (end - start) / (double)ITERS;
                System.out.println("  Average Time: " + String.format("%.2f", avg) + " ms");
            } catch (Throwable t) {
                System.out.println("  Failed during bench: " + t.getMessage());
            }
        }
        System.out.println("===============================================================");
    }
}
