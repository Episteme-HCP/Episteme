/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.linearalgebra;

import org.jscience.core.technical.algorithm.AlgorithmProvider;
import java.nio.DoubleBuffer;

/**
 * Element-wise operation algorithm provider.
 * <p>
 * Provides vectorized element-wise operations (add, multiply, sin, exp, etc.)
 * with CPU and GPU implementations.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface ElementWiseProvider extends AlgorithmProvider {

    /**
     * Performs element-wise operations.
     *
     * @param operation Operation type: "add", "mul", "sin", "exp", etc.
     * @param input Input buffer
     * @param output Output buffer
     * @param size Number of elements
     */
    void elementWise(String operation, DoubleBuffer input, DoubleBuffer output, int size);

    /**
     * Performs element-wise binary operation.
     *
     * @param operation Operation type: "add", "mul", "sub", "div"
     * @param input1 First input buffer
     * @param input2 Second input buffer
     * @param output Output buffer
     * @param size Number of elements
     */
    void elementWiseBinary(String operation, DoubleBuffer input1, DoubleBuffer input2, 
                          DoubleBuffer output, int size);

    @Override
    default String getAlgorithmType() {
        return "element_wise";
    }
}
