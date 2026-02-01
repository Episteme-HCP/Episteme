/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.linearalgebra.algorithms;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.linearalgebra.matrices.SIMDDoubleMatrix;
import org.jscience.core.mathematics.linearalgebra.matrices.TiledMatrix;
import org.jscience.core.distributed.DistributedContext;
import org.jscience.core.ComputeContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Intelligent Planner for Distributed Matrix Multiplication.
 * Selects the optimal algorithm (Cannon, Fox, SUMMA, 2.5D, CARMA) based on runtime metrics.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class MatrixMultiplicationPlanner {

    private static final Logger logger = LoggerFactory.getLogger(MatrixMultiplicationPlanner.class);

    public enum Algorithm {
        STANDARD,
        SUMMA,
        CANNON,
        FOX,
        ALGORITHM_25D,
        CARMA
    }

    private static final int STRASSEN_THRESHOLD = 1024;

    public static Matrix<Real> multiply(Matrix<Real> A, Matrix<Real> B) {
        int n = Math.max(A.rows(), A.cols());
        
        if (A instanceof SIMDDoubleMatrix && B instanceof SIMDDoubleMatrix) {
            if (n >= STRASSEN_THRESHOLD && isPowerOfTwo(n)) {
                return RealDoubleStrassenAlgorithm.multiply((SIMDDoubleMatrix) A, (SIMDDoubleMatrix) B);
            }
            return RealDoubleCARMAAlgorithm.multiply((SIMDDoubleMatrix) A, (SIMDDoubleMatrix) B);
        }
        
        if (n >= STRASSEN_THRESHOLD && isPowerOfTwo(n)) {
            return RealStrassenAlgorithm.multiply(A, B);
        }
        return RealCARMAAlgorithm.multiply(A, B);
    }

    private static boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    /**
     * Selects and executes the best distributed multiplication algorithm.
     */
    public static TiledMatrix multiply(TiledMatrix A, TiledMatrix B) {
        DistributedContext ctx = ComputeContext.current().getDistributedContext();
        int p = ctx.getParallelism();
        Algorithm algo = selectAlgorithm(A, B, p);
        
        logger.info("Planner selected algorithm: {}", algo);
        
        switch (algo) {
            case CANNON:
                return DistributedCannonAlgorithm.multiply(A, B);
            case FOX:
                 // Ensure Fox is valid (square grid)
                if (isSquareGrid(p)) return DistributedFoxAlgorithm.multiply(A, B);
                return DistributedSUMMAAlgorithm.multiply(A, B);
            case ALGORITHM_25D:
                int c = (int) Math.pow(p, 0.33);
                if (c < 1) c = 1;
                return Distributed25DAlgorithm.multiply(A, B, c);
            case CARMA:
                return DistributedCARMAAlgorithm.multiply(A, B);
            case SUMMA:
            default:
                return DistributedSUMMAAlgorithm.multiply(A, B);
        }
    }

    /**
     * Heuristic Selection Logic.
     */
    public static Algorithm selectAlgorithm(TiledMatrix A, TiledMatrix B, int p) {
        long m = A.rows();
        long n = B.cols();
        long k = A.cols();
        
        boolean isSquare = (m == n && n == k);
        boolean isSquareGrid = isSquareGrid(p);
        
        // 1. CARMA is generally best for non-square or recursive arbitrary dims
        if (!isSquare) {
            return Algorithm.CARMA;
        }
        
        // 2. 2.5D if we have 3D-like topology or high communication cost
        if (p >= 64) {
             return Algorithm.ALGORITHM_25D;
        }

        // 3. Cannon/Fox for square grids
        if (isSquareGrid) {
            return Algorithm.CANNON; 
        }
        
        // 4. Fallback
        return Algorithm.SUMMA;
    }
    
    private static boolean isSquareGrid(int p) {
        int sqrt = (int) Math.sqrt(p);
        return sqrt * sqrt == p;
    }
}

