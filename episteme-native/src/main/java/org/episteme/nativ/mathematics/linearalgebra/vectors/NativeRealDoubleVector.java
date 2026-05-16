/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.nativ.mathematics.linearalgebra.vectors;

import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.vectors.GenericVector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.nativ.mathematics.linearalgebra.vectors.storage.NativeRealDoubleVectorStorage;
import org.episteme.nativ.technical.backend.nativ.NativeSegmentProxy;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

/**
 * High-performance Real Double Vector backed by a persistent native MemorySegment.
 * Implements {@link NativeSegmentProxy} for zero-copy transfer to FFM-based BLAS backends.
 */
public class NativeRealDoubleVector extends GenericVector<Real> implements NativeSegmentProxy {
    
    private final NativeRealDoubleVectorStorage nativeStorage;

    public NativeRealDoubleVector(int dimension, Arena arena, LinearAlgebraProvider<Real> provider) {
        this(new NativeRealDoubleVectorStorage(dimension, arena), provider);
    }

    public NativeRealDoubleVector(NativeRealDoubleVectorStorage storage, LinearAlgebraProvider<Real> provider) {
        super(storage, provider, Reals.getInstance());
        this.nativeStorage = storage;
    }

    public static NativeRealDoubleVector copyOf(Vector<Real> other, Arena arena, LinearAlgebraProvider<Real> provider) {
        NativeRealDoubleVector v = new NativeRealDoubleVector(other.dimension(), arena, provider);
        for (int i = 0; i < v.dimension(); i++) {
            v.set(i, other.get(i));
        }
        return v;
    }

    @Override public MemorySegment segment() { return nativeStorage.segment(); }
    @Override public Arena arena() { return nativeStorage.arena(); }

    public String description() {
        return "NativeRealDoubleVector (" + dimension() + ") [Zero-Copy Proxy]";
    }

    public NativeRealDoubleVector copy() {
        return new NativeRealDoubleVector(nativeStorage.copy() instanceof NativeRealDoubleVectorStorage ns ? ns : nativeStorage, getProvider());
    }
}


