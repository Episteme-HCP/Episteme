/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.linearalgebra.matrices;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.jscience.core.mathematics.sets.Reals;

/**
 * Memory-Mapped Matrix implementation for Out-of-Core processing.
 * <p>
 * Allows handling matrices larger than available RAM by mapping file segments into virtual memory.
 * Supported max size per segment is 2GB (Integer.MAX_VALUE bytes). This implementation uses
 * a single buffer for simplicity, limiting it to ~256 million doubles.
 * A production version would use multiple mapped regions.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class MMapMatrix extends Matrix<Real> {

    private final MappedByteBuffer buffer;
    private final int rows;
    private final int cols;
    private final Path filePath;

    public MMapMatrix(Path path, int rows, int cols) throws IOException {
        super(Reals.getInstance());
        this.rows = rows;
        this.cols = cols;
        this.filePath = path;

        long sizeBytes = (long) rows * cols * 8; // 8 bytes per double
        if (sizeBytes > Integer.MAX_VALUE) {
            throw new UnsupportedOperationException("Matrix too large for single map (limited to 2GB in this version).");
        }

        try (RandomAccessFile raf = new RandomAccessFile(path.toFile(), "rw")) {
            raf.setLength(sizeBytes);
            this.buffer = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, sizeBytes);
        }
    }

    @Override
    public int getNumRows() { // Legacy naming
        return rows;
    }

    @Override
    public int getNumCols() { // Legacy naming
        return cols;
    }
    
    @Override
    public int rows() {
        return rows;
    }

    @Override
    public int cols() {
        return cols;
    }

    @Override
    public Real get(int i, int j) {
        checkBounds(i, j);
        int offset = (i * cols + j) * 8;
        double val = buffer.getDouble(offset);
        return Real.of(val);
    }

    @Override
    public void set(int i, int j, Real value) {
        checkBounds(i, j);
        int offset = (i * cols + j) * 8;
        buffer.putDouble(offset, value.doubleValue());
    }

    private void checkBounds(int i, int j) {
        if (i < 0 || i >= rows || j < 0 || j >= cols) {
            throw new IndexOutOfBoundsException("Index: " + i + "," + j);
        }
    }

    @Override
    public MMapMatrix copy() {
        throw new UnsupportedOperationException("Deep copy of MMapMatrix not implemented (IO heavy).");
    }
    
    /**
     * Forces changes to be written to disk.
     */
    public void flush() {
        buffer.force();
    }
}
