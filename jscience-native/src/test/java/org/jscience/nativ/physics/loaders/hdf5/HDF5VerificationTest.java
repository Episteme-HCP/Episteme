/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.physics.loaders.hdf5;

import org.jscience.nativ.mathematics.linearalgebra.matrices.NativeMatrix;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verification test for HDF5 Panama backend.
 */
public class HDF5VerificationTest {

    @Test
    public void testWriteAndReadMatrix(@TempDir Path tempDir) throws Exception {
        Path h5File = tempDir.resolve("test_matrix.h5");
        String filePath = h5File.toString();

        System.out.println("Testing HDF5 I/O to: " + filePath);

        // 1. Create a NativeMatrix
        int rows = 10;
        int cols = 10;
        NativeMatrix original = new NativeMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                original.setDouble(i, j, i * cols + j + 0.5);
            }
        }

        // 2. Write to HDF5
        try (NativeHDF5Writer writer = new NativeHDF5Writer(h5File)) {
            writer.writeMatrix("test_data", original);
        } catch (UnsupportedOperationException e) {
            System.err.println("Skipping HDF5 test: Native library not found.");
            return; // Skip test if native lib is missing
        } catch (Throwable t) {
             // In case of unsatisfied link error or other native issues
             if (t.getMessage() != null && t.getMessage().contains("native library not found")) {
                 System.err.println("Skipping HDF5 test: " + t.getMessage());
                 return;
             }
             throw t;
        }

        assertTrue(h5File.toFile().exists(), "HDF5 file should exist");

        // 3. Read from HDF5
        try (NativeHDF5Reader reader = new NativeHDF5Reader(h5File)) {
            NativeMatrix loaded = new NativeMatrix(rows, cols);
            reader.readMatrix("test_data", loaded);

            // 4. Verify
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    double expected = original.get(i, j).doubleValue();
                    double actual = loaded.get(i, j).doubleValue();
                    assertEquals(expected, actual, 1e-9, "Value mismatch at " + i + "," + j);
                }
            }
        }
    }

}
