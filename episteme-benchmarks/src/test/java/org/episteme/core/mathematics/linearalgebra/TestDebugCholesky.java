package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

public class TestDebugCholesky {
    @Test
    public void run() {
        org.episteme.core.mathematics.context.MathContext.setCurrent(
            org.episteme.core.mathematics.context.MathContext.withPrecision(100));
        
        int n = 2;
        Complex[][] data = new Complex[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) {
            data[i][j] = Complex.of(i == j ? n * 3.0 : 0.1, 0);
            System.out.println("A["+i+"]["+j+"] doubleValue: " + data[i][j].abs().doubleValue());
            System.out.println("A["+i+"]["+j+"] bigDecimalValue: " + data[i][j].abs().bigDecimalValue());
        }
        Matrix<Complex> A = Matrix.of(data, (Ring<Complex>) (Object) Complex.of(0, 0).getScalarRing());
        System.out.println("A[0][0] doubleValue from matrix: " + A.get(0,0).abs().doubleValue());
        System.out.println("Test Complete!");
    }
}
