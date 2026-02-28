/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra.backends;

import org.episteme.core.mathematics.linearalgebra.providers.CPUDenseLinearAlgebraProvider;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Automated baseline test for CPUDenseLinearAlgebraProvider.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CPUDenseLinearAlgebraProviderTest {

    @Test
    public void testClassPresence() {
        // Ensure class is reachable
        assertNotNull(CPUDenseLinearAlgebraProvider.class);
    }

    @Test
    public void testRealVectorOperations() {
        org.episteme.core.mathematics.sets.Reals reals = org.episteme.core.mathematics.sets.Reals.getInstance();
        CPUDenseLinearAlgebraProvider<org.episteme.core.mathematics.numbers.real.Real> provider = new CPUDenseLinearAlgebraProvider<>(reals);
        
        // Test Matrix-Vector Multiply
        double[][] matData = {{1.0, 2.0}, {3.0, 4.0}};
        org.episteme.core.mathematics.linearalgebra.Matrix<org.episteme.core.mathematics.numbers.real.Real> matrix = 
            org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix.of(matData);
            
        double[] vecData = {1.0, 1.0};
        org.episteme.core.mathematics.numbers.real.Real[] realVecData = new org.episteme.core.mathematics.numbers.real.Real[2];
        realVecData[0] = org.episteme.core.mathematics.numbers.real.Real.of(vecData[0]);
        realVecData[1] = org.episteme.core.mathematics.numbers.real.Real.of(vecData[1]);
        
        org.episteme.core.mathematics.linearalgebra.Vector<org.episteme.core.mathematics.numbers.real.Real> vector = 
            new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(
                new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(realVecData), 
                provider, reals);
                
        org.episteme.core.mathematics.linearalgebra.Vector<org.episteme.core.mathematics.numbers.real.Real> result = provider.multiply(matrix, vector);
        
        assertEquals(3.0, result.get(0).doubleValue(), 1e-9);
        assertEquals(7.0, result.get(1).doubleValue(), 1e-9);

        // Test Solve
        // A = [[1, 2], [3, 4]], b = [3, 7] -> x = [1, 1]
        org.episteme.core.mathematics.linearalgebra.Vector<org.episteme.core.mathematics.numbers.real.Real> x = provider.solve(matrix, result);
        assertEquals(1.0, x.get(0).doubleValue(), 1e-9);
        assertEquals(1.0, x.get(1).doubleValue(), 1e-9);
    }
}

