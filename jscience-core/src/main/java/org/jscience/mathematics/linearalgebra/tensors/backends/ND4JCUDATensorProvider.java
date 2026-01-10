/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.mathematics.linearalgebra.tensors.backends;

/**
 * ND4J CUDA (GPU) Tensor Provider.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ND4JCUDATensorProvider extends ND4JBaseTensorProvider {

    @Override
    protected boolean checkAvailability() {
        if (!checkCommonClasses()) return false;
        try {
            // Check specifically for CUDA backend availability
            Class.forName("org.nd4j.linalg.jcublas.JCublasBackend");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public String getId() {
        return "nd4j-cuda";
    }

    @Override
    public String getName() {
        return "ND4J CUDA (GPU)";
    }

    @Override
    public String getDescription() {
        return "ND4J Tensor Provider using CUDA GPU Backend";
    }

    @Override
    public int getPriority() {
        // Higher priority than Native if available
        return isAvailable() ? 100 : 0;
    }

    @Override
    public boolean supportsGPU() {
        return true;
    }
}
