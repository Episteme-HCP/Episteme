/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.linearalgebra.algorithms.tests;

import org.jscience.core.mathematics.linearalgebra.matrices.TiledMatrix;
import org.jscience.core.mathematics.linearalgebra.algorithms.CARMAAlgorithm;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.distributed.LocalDistributedContext;
import org.jscience.core.ComputeContext;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CARMA Algorithm.
 */
public class CARMAAlgorithmTest {

    @Test
    public void testMultiplicationCorrectness() {
        // Setup simple context
        ComputeContext.current().setDistributedContext(new LocalDistributedContext(1));
        
        // 4x4 matrix, tiles 2x2
        TiledMatrix A = createIdentity(4, 2);
        TiledMatrix B = createIdentity(4, 2);
        
        // C = I * I = I
        TiledMatrix C = CARMAAlgorithm.multiply(A, B);
        
        // Verify C is Identity
        for(int i=0; i<4; i++) {
             for(int j=0; j<4; j++) {
                 double expected = (i == j) ? 1.0 : 0.0;
                 assertEquals(expected, C.get(i, j).doubleValue(), 1e-9, "Mismatch at " + i + "," + j);
             }
        }
    }
    
    // Helper to create Identity TiledMatrix
    private TiledMatrix createIdentity(int n, int tileSize) {
        TiledMatrix M = new TiledMatrix(n, n, tileSize);
        for(int i=0; i<n; i++) {
            M.set(i, i, Real.ONE);
        }
        return M;
    }
}
