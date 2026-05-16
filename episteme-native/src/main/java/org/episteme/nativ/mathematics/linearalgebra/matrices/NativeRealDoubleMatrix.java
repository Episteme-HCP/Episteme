/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.nativ.mathematics.linearalgebra.matrices;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.nativ.mathematics.linearalgebra.matrices.storage.NativeRealDoubleMatrixStorage;
import org.episteme.nativ.technical.backend.nativ.NativeSegmentProxy;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

/**
 * High-performance Real Double Matrix backed by a persistent native MemorySegment.
 * Implements {@link NativeSegmentProxy} for zero-copy transfer to FFM-based BLAS backends.
 */
public class NativeRealDoubleMatrix extends GenericMatrix<Real> implements NativeSegmentProxy {
    
    private final NativeRealDoubleMatrixStorage nativeStorage;

    public NativeRealDoubleMatrix(int rows, int cols, Arena arena, LinearAlgebraProvider<Real> provider) {
        this(new NativeRealDoubleMatrixStorage(rows, cols, arena), provider);
    }

    public NativeRealDoubleMatrix(NativeRealDoubleMatrixStorage storage, LinearAlgebraProvider<Real> provider) {
        super(storage, provider, Reals.getInstance());
        this.nativeStorage = storage;
    }

    public static NativeRealDoubleMatrix copyOf(Matrix<Real> other, Arena arena, LinearAlgebraProvider<Real> provider) {
        NativeRealDoubleMatrix m = new NativeRealDoubleMatrix(other.rows(), other.cols(), arena, provider);
        for (int i = 0; i < m.rows(); i++) {
            for (int j = 0; j < m.cols(); j++) {
                m.set(i, j, other.get(i, j));
            }
        }
        return m;
    }

    @Override public MemorySegment segment() { return nativeStorage.segment(); }
    @Override public Arena arena() { return nativeStorage.arena(); }

    public String description() {
        return "NativeRealDoubleMatrix (" + rows() + "x" + cols() + ") [Zero-Copy Proxy]";
    }

    public NativeRealDoubleMatrix copy() {
        return new NativeRealDoubleMatrix(nativeStorage.clone(), getProvider());
    }
}


