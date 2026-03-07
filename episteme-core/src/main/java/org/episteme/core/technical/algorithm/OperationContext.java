/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.technical.algorithm;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Describes the context of a numerical operation for provider selection.
 * <p>
 * Instead of purely priority-based selection, {@code OperationContext} allows
 * {@link AlgorithmProvider#score(OperationContext)} to make informed decisions
 * based on data characteristics.
 * </p>
 *
 * <pre>
 * OperationContext ctx = new OperationContext.Builder()
 *     .dataSize(1_000_000)
 *     .addHint(OperationContext.Hint.DENSE)
 *     .build();
 *
 * LinearAlgebraProvider best = ProviderSelector.select(
 *     LinearAlgebraProvider.class, ctx);
 * </pre>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public final class OperationContext {

    /**
     * Hints about the data characteristics.
     */
    public enum Hint {
        /** Dense matrix/vector data */
        DENSE,
        /** Sparse matrix/vector data */
        SPARSE,
        /** Data fits in GPU memory */
        GPU_RESIDENT,
        /** Operation is latency-sensitive */
        LOW_LATENCY,
        /** Operation is throughput-sensitive */
        HIGH_THROUGHPUT,
        /** Result must be bit-exact reproducible */
        DETERMINISTIC,
        /** Single-precision is acceptable */
        FLOAT32_OK,
        /** Operation will be repeated many times (amortize setup) */
        BATCH,
        /** Matrix Multiplication */
        MAT_MUL,
        /** Matrix Division / Solve */
        MAT_DIV,
        /** Matrix Inversion */
        MAT_INV,
        /** Matrix Determinant */
        MAT_DET,
        /** Matrix/Vector Solve */
        MAT_SOLVE,
        /** Matrix Transpose */
        MAT_TRANSPOSE,
        /** Matrix Scaling */
        MAT_SCALE,
        /** Matrix Addition */
        MAT_ADD,
        /** Matrix Subtraction */
        MAT_SUBTRACT,
        /** QR Decomposition */
        MAT_QR,
        /** SVD Decomposition */
        MAT_SVD,
        /** Cholesky Decomposition */
        MAT_CHOLESKY,
        /** LU Decomposition */
        MAT_LU,
        /** Eigenvalue Decomposition */
        MAT_EIGEN
    }

    public static final OperationContext DEFAULT = new OperationContext.Builder().build();

    private final long dataSize;
    private final Set<Hint> hints;
    private final int dimensionality;
    private volatile boolean cancelled = false;

    private OperationContext(Builder builder) {
        this.dataSize = builder.dataSize;
        this.hints = Collections.unmodifiableSet(builder.hints);
        this.dimensionality = builder.dimensionality;
    }

    /** Number of elements in the primary operand. */
    public long getDataSize() { return dataSize; }

    /** Unmodifiable set of operation hints. */
    public Set<Hint> getHints() { return hints; }

    /** Whether a specific hint is present. */
    public boolean hasHint(Hint hint) { return hints.contains(hint); }

    /** Dimensionality of the data (e.g., rows for matrix). */
    public int getDimensionality() { return dimensionality; }

    /** Returns true if this operation has been cancelled. */
    public boolean isCancelled() { return cancelled; }

    /** Sets the cancellation state of this operation. */
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

    public static class Builder {
        private long dataSize = 0;
        private final Set<Hint> hints = EnumSet.noneOf(Hint.class);
        private int dimensionality = 0;

        public Builder dataSize(long size) { this.dataSize = size; return this; }
        public Builder addHint(Hint hint) { this.hints.add(hint); return this; }
        public Builder dimensionality(int dim) { this.dimensionality = dim; return this; }

        public OperationContext build() {
            return new OperationContext(this);
        }
    }
}
