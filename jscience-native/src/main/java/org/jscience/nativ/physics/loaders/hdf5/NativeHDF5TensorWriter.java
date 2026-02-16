/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.physics.loaders.hdf5;

import org.jscience.core.mathematics.linearalgebra.Tensor;
import org.jscience.core.io.ResourceWriter;
import org.jscience.nativ.mathematics.linearalgebra.matrices.NativeMatrix;

/**
 * Adapter to save Tensors to HDF5 files.
 * Currently supports Rank-2 Tensors (Matrices) by converting to NativeMatrix.
 * Future versions will support N-Dimensional arrays directly.
 */
public class NativeHDF5TensorWriter implements ResourceWriter<Tensor<?>> {

    @Override
    public String getName() {
        return "HDF5 Tensor Writer";
    }

    @Override
    public String getDescription() {
        return "Saves Tensors to HDF5 files.";
    }

    @Override
    public String getCategory() {
        return "I/O / Native";
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<Tensor<?>> getResourceType() {
        return (Class<Tensor<?>>) (Class<?>) Tensor.class;
    }

    @Override
    public String getResourcePath() {
        return null;
    }

    @Override
    public String getLongDescription() {
        return "Adapter that allows saving org.jscience.core.mathematics.linearalgebra.Tensor objects directly to HDF5 files by converting them to NativeMatrix representations.";
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[] { "1.0" };
    }

    @Override
    public String[] getSupportedExtensions() {
        return new String[] { ".h5", ".hdf5" };
    }

    @Override
    public void save(Tensor<?> resource, String destination) throws Exception {
        if (resource.rank() != 2) {
            throw new UnsupportedOperationException("Currently only Rank-2 Tensors (Matrices) are supported for HDF5 export.");
        }
        
        int rows = resource.shape()[0];
        int cols = resource.shape()[1];

        // Convert Tensor to NativeMatrix using its own confined arena
        try (NativeMatrix matrix = new NativeMatrix(rows, cols)) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    Object val = resource.get(i, j);
                    if (val instanceof Number n) {
                        matrix.setDouble(i, j, n.doubleValue());
                    } else {
                        throw new IllegalArgumentException("HDF5 export only supports tensors of numeric types. Found: " + (val != null ? val.getClass().getName() : "null"));
                    }
                }
            }

            // Delegate to NativeHDF5Writer
            try (NativeHDF5Writer writer = new NativeHDF5Writer(java.nio.file.Paths.get(destination))) {
                writer.writeMatrix("data", matrix);
            }
        }
    }
}
