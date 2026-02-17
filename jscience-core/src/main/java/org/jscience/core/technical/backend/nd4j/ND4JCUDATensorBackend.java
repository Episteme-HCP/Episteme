/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.backend.nd4j;

import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.linearalgebra.tensors.TensorBackend;
import org.jscience.core.technical.backend.HardwareAccelerator;

/**
 * ND4J CUDA (GPU) Tensor Provider.
 */
@AutoService(TensorBackend.class)
public class ND4JCUDATensorBackend extends ND4JBaseTensorBackend {

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.GPU;
    }

    @Override
    protected boolean checkAvailability() {
        if (!checkCommonClasses()) return false;
        try {
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
        return isAvailable() ? 100 : 0;
    }

    @Override
    public boolean supportsGPU() {
        return true;
    }
}
