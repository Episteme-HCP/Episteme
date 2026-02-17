/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.core.technical.algorithm.io;

import org.jscience.core.technical.algorithm.AlgorithmProvider;
import com.google.auto.service.AutoService;

/**
 * HDF5 IO Provider.
 * Wraps HDF5 library for IO benchmarks.
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
@AutoService(AlgorithmProvider.class)
public class HDF5IOProvider implements AlgorithmProvider {

    @Override
    public String getName() {
        return "HDF5 Storage I/O";
    }

    @Override
    public String getAlgorithmType() {
        return "io";
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("io.jhdf.HdfFile");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
