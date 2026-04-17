/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.nativ.mathematics.linearalgebra.matrices;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealFloat;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.mathematics.structures.rings.Field;
import org.episteme.nativ.mathematics.linearalgebra.backends.NativeSegmentProxy;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.HeapRealFloatMatrixStorage;
import org.episteme.core.mathematics.linearalgebra.providers.CPUDenseLinearAlgebraProvider;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.FloatBuffer;

/**
 * High-performance Real Float Matrix backed by a persistent native MemorySegment.
 * Implements {@link NativeSegmentProxy} for zero-copy transfer to FFM-based BLAS backends.
 */
public class NativeRealFloatMatrix extends GenericMatrix<Real> implements NativeSegmentProxy {
    
    private final Arena arena;
    private final MemorySegment segment;

    @SuppressWarnings("unchecked")
    public NativeRealFloatMatrix(int rows, int cols, Arena arena) {
        super(new HeapRealFloatMatrixStorage(rows, cols),
              new CPUDenseLinearAlgebraProvider<Real>((Field<Real>)(Object)Reals.getInstance()),
              Reals.getInstance());
        this.arena = arena;
        this.segment = arena.allocate(ValueLayout.JAVA_FLOAT, (long) rows * cols);
    }

    public static NativeRealFloatMatrix copyOf(Matrix<Real> other, Arena arena) {
        NativeRealFloatMatrix m = new NativeRealFloatMatrix(other.rows(), other.cols(), arena);
        // Fallback to generic set loop which handles both native and heap storage
        for (int i = 0; i < m.rows(); i++) {
            for (int j = 0; j < m.cols(); j++) {
                m.set(i, j, other.get(i, j));
            }
        }
        return m;
    }

    @Override
    public Real get(int row, int col) {
        float val = segment.getAtIndex(ValueLayout.JAVA_FLOAT, (long) row * cols() + col);
        return RealFloat.of(val);
    }

    @Override
    public void set(int row, int col, Real value) {
        segment.setAtIndex(ValueLayout.JAVA_FLOAT, (long) row * cols() + col, value.floatValue());
        // Also update the heap storage for consistency
        if (storage instanceof HeapRealFloatMatrixStorage heap) {
            heap.setFloat(row, col, value.floatValue());
        } else {
            storage.set(row, col, value);
        }
    }

    @Override public MemorySegment segment() { return segment; }
    @Override public Arena arena() { return arena; }

    /**
     * Provides a view of the native memory as a FloatBuffer for legacy interop if needed.
     */
    public FloatBuffer asBuffer() {
        return segment.asByteBuffer().asFloatBuffer();
    }

    public Matrix<Real> copy() {
        NativeRealFloatMatrix clone = new NativeRealFloatMatrix(rows(), cols(), arena);
        MemorySegment.copy(segment, 0, clone.segment, 0, segment.byteSize());
        return clone;
    }

    @Override
    public String description() {
        return "NativeRealFloatMatrix (" + rows() + "x" + cols() + ") [Zero-Copy Proxy]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Matrix)) return false;
        Matrix<?> that = (Matrix<?>) o;
        if (this.rows() != that.rows() || this.cols() != that.cols()) return false;
        for (int i = 0; i < rows(); i++) {
            for (int j = 0; j < cols(); j++) {
                if (!this.get(i, j).equals(that.get(i, j))) return false;
            }
        }
        return true;
    }
}
