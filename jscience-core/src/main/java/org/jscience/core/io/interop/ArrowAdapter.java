/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.io.interop;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.numbers.real.Real;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Adapter for Apache Arrow Integration.
 * <p>
 * Facilitates Zero-Copy data exchange by exporting JScience Matrices
 * into Arrow-compatible ByteBuffers (Float64 contiguous arrays).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class ArrowAdapter {

    /**
     * Converts a Matrix to an Arrow-compatible ByteBuffer (Float64 array).
     * This buffer can be passed to Python/Pandas via IPC.
     *
     * @param matrix Source matrix
     * @return ByteBuffer in Little Endian containing doubles
     */
    public static ByteBuffer toArrowBuffer(Matrix<Real> matrix) {
        int rows = matrix.rows();
        int cols = matrix.cols();
        int sizeBytes = rows * cols * 8;
        
        ByteBuffer buffer = ByteBuffer.allocateDirect(sizeBytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN); // Arrow uses Little Endian
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buffer.putDouble(matrix.get(i, j).doubleValue());
            }
        }
        
        buffer.flip();
        return buffer;
    }
    
    /**
     * Creates a rudimentary Arrow Schema description for the matrix.
     * @return JSON-like string describing the schema (Field: "matrix", Type: Float64)
     */
    public static String getSchemaDescription() {
        return "{ \"fields\": [ { \"name\": \"matrix_data\", \"type\": { \"name\": \"floatingpoint\", \"precision\": \"DOUBLE\" } } ] }";
    }
}
