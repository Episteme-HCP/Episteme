/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.linearalgebra.algorithms;

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

    /**
     * Selects and executes the best distributed multiplication algorithm.
     *
     * @param A Left Matrix
     * @param B Right Matrix
     * @return Result Matrix
     */
    public static TiledMatrix multiply(TiledMatrix A, TiledMatrix B) {
        DistributedContext ctx = ComputeContext.current().getDistributedContext();
        int p = ctx.getParallelism();
        Algorithm algo = selectAlgorithm(A, B, p);
        
        logger.info("Planner selected algorithm: {}", algo);
        
        switch (algo) {
            case CANNON:
                return CannonAlgorithm.multiply(A, B);
            case FOX:
                 // Ensure Fox is valid (square grid)
                if (isSquareGrid(p)) return FoxAlgorithm.multiply(A, B);
                return SUMMAAlgorithm.multiply(A, B); // Fallback
            case ALGORITHM_25D:
                // Estimate best c (replication)
                // Heuristic: if memory allows, use c=p^(1/3)
                int c = (int) Math.pow(p, 0.33);
                if (c < 1) c = 1;
                return Algorithm25D.multiply(A, B, c);
            case CARMA:
                return CARMAAlgorithm.multiply(A, B);
            case SUMMA:
            default:
                return SUMMAAlgorithm.multiply(A, B);
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
        // Simplified check: if p is large (e.g. > 64) and cube-root-able
        if (p >= 64) {
             return Algorithm.ALGORITHM_25D;
        }

        // 3. Cannon/Fox for square grids
        if (isSquareGrid) {
            return Algorithm.CANNON; // Cannon usually slightly better than Fox for square
        }
        
        // 4. Fallback
        return Algorithm.SUMMA;
    }
    
    private static boolean isSquareGrid(int p) {
        int sqrt = (int) Math.sqrt(p);
        return sqrt * sqrt == p;
    }
}
