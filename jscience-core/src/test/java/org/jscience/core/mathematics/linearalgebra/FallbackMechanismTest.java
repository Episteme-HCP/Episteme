package org.jscience.core.mathematics.linearalgebra;

import org.jscience.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;
import org.jscience.core.technical.algorithm.OperationContext;
import org.jscience.core.technical.algorithm.ProviderSelector;
import org.jscience.core.mathematics.linearalgebra.providers.StrassenLinearAlgebraProvider;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FallbackMechanismTest {

    @Test
    public void testStrassenFallback() {
        // Strassen only supports multiplication. 
        // Other ops should fallback to CPUDense or whatever else is available.
        
        Real[][] data = new Real[][]{
            {Real.of(1.0), Real.of(2.0)},
            {Real.of(3.0), Real.of(4.0)}
        };
        Matrix<Real> A = DenseMatrix.of(data, Reals.getInstance());
        
        // We force Strassen provider selection if possible, but for inversion it doesn't support it.
        // ProviderSelector.execute should handle the fallback.
        
        OperationContext ctx = new OperationContext.Builder()
                .build();
                
        // Test inversion: Strassen should fail (UOE), and CPUDense should take over.
        @SuppressWarnings({"unchecked", "rawtypes"})
        Matrix<Real> inv = (Matrix<Real>) ProviderSelector.execute(LinearAlgebraProvider.class, ctx, (LinearAlgebraProvider p) -> {
            if (p instanceof StrassenLinearAlgebraProvider) {
                // This will throw UOE because we refactored it to stop extending CPUDense 
                // and it doesn't override inverse()
                return p.inverse(A);
            }
            // Other providers might handle it
            return p.inverse(A);
        });
        
        assertNotNull(inv);
        // Verify it's actually inverted
        Matrix<Real> identity = A.multiply(inv);
        assertEquals(1.0, identity.get(0, 0).doubleValue(), 1e-10);
        assertEquals(0.0, identity.get(0, 1).doubleValue(), 1e-10);
        assertEquals(0.0, identity.get(1, 0).doubleValue(), 1e-10);
        assertEquals(1.0, identity.get(1, 1).doubleValue(), 1e-10);
    }
    
    @Test
    public void testCustomFilterFallback() {
        Real[][] dataA = new Real[][]{{Real.of(1.0), Real.of(2.0)}, {Real.of(3.0), Real.of(4.0)}};
        Real[][] dataB = new Real[][]{{Real.of(5.0), Real.of(6.0)}, {Real.of(7.0), Real.of(8.0)}};
        Matrix<Real> A = DenseMatrix.of(dataA, Reals.getInstance());
        Matrix<Real> B = DenseMatrix.of(dataB, Reals.getInstance());
        
        OperationContext ctx = new OperationContext.Builder()
                .build();
        
        // Exclude Strassen and CARMA, force something else
        @SuppressWarnings({"unchecked", "rawtypes"})
        LinearAlgebraProvider<Real> provider = (LinearAlgebraProvider<Real>) ProviderSelector.select(LinearAlgebraProvider.class, ctx, 
            (LinearAlgebraProvider p) -> !p.getName().contains("Strassen") && !p.getName().contains("CARMA"));
            
        assertFalse(provider.getName().contains("Strassen"));
        assertFalse(provider.getName().contains("CARMA"));
        
        Matrix<Real> C = provider.multiply(A, B);
        assertNotNull(C);
    }
}
