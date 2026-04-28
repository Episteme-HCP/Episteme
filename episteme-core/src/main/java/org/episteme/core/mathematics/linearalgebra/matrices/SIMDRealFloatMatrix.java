/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra.matrices;

import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorSpecies;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.technical.algorithm.AlgorithmManager;

import org.episteme.core.mathematics.linearalgebra.matrices.storage.HeapRealFloatMatrixStorage;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.MatrixStorage;

/**
 * SIMD-accelerated Matrix implementation using JDK Vector API for Float (Single) precision.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class SIMDRealFloatMatrix extends GenericMatrix<Real> implements AutoCloseable {
    
    public static SIMDRealFloatMatrix from(Matrix<Real> m) {
        if (m instanceof SIMDRealFloatMatrix) return (SIMDRealFloatMatrix) m;
        int rows = m.rows();
        int cols = m.cols();
        float[] data = new float[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i * cols + j] = m.get(i, j).floatValue();
            }
        }
        return new SIMDRealFloatMatrix(rows, cols, data);
    }

    private static final VectorSpecies<Float> SPECIES = FloatVector.SPECIES_PREFERRED;

    private final float[] data;

    public SIMDRealFloatMatrix(int rows, int cols) {
        this(rows, cols, new float[rows * cols]);
    }
    
    public SIMDRealFloatMatrix(int rows, int cols, float[] data) {
        super(new HeapRealFloatMatrixStorage(data, rows, cols),
              getDefaultProvider(),
              Reals.getInstance());
        this.data = data;
    }

    @SuppressWarnings("unchecked")
    private static org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<Real> getDefaultProvider() {
        return (org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<Real>) 
               AlgorithmManager.getProvider(org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider.class);
    }

    @Override
    public void close() {
    }

    @Override
    public int rows() { return storage.rows(); }

    @Override
    public int cols() { return storage.cols(); }

    @Override
    public Real get(int row, int col) {
        return Real.of(data[row * storage.cols() + col]);
    }

    public void set(int row, int col, float val) {
        data[row * storage.cols() + col] = val;
    }
    
    @Override
    public void set(int row, int col, Real val) {
        set(row, col, val.floatValue());
    }

    public Matrix<Real> scale(float scalar) {
        float[] res = new float[data.length];
        int i = 0;
        final VectorSpecies<Float> species = SPECIES;
        for (; i < species.loopBound(data.length); i += species.length()) {
            var v = FloatVector.fromArray(species, this.data, i);
            v.mul(FloatVector.broadcast(species, scalar)).intoArray(res, i);
        }
        for (; i < data.length; i++) res[i] = data[i] * scalar;
        return new SIMDRealFloatMatrix(storage.rows(), storage.cols(), res);
    }

    @Override
    public Matrix<Real> add(Matrix<Real> other) {
        SIMDRealFloatMatrix that = from(other);
        float[] res = new float[data.length];
        int i = 0;
        for (; i < SPECIES.loopBound(data.length); i += SPECIES.length()) {
            var v1 = FloatVector.fromArray(SPECIES, this.data, i);
            var v2 = FloatVector.fromArray(SPECIES, that.data, i);
            v1.add(v2).intoArray(res, i);
        }
        for (; i < data.length; i++) res[i] = this.data[i] + that.data[i];
        return new SIMDRealFloatMatrix(storage.rows(), storage.cols(), res);
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> other) {
        SIMDRealFloatMatrix that = from(other);
        if (storage.cols() != that.storage.rows()) throw new IllegalArgumentException("Dimension mismatch");
        SIMDRealFloatMatrix C = new SIMDRealFloatMatrix(storage.rows(), that.storage.cols());
        for (int i = 0; i < storage.rows(); i++) {
            for (int k = 0; k < storage.cols(); k++) {
                float aik = this.data[i * storage.cols() + k];
                int j = 0;
                for (; j < SPECIES.loopBound(that.storage.cols()); j += SPECIES.length()) {
                    int bIdx = k * that.storage.cols() + j;
                    var bVec = FloatVector.fromArray(SPECIES, that.data, bIdx);
                    int cIdx = i * that.storage.cols() + j;
                    var cVec = FloatVector.fromArray(SPECIES, C.data, cIdx);
                    cVec = bVec.fma(FloatVector.broadcast(SPECIES, aik), cVec); 
                    cVec.intoArray(C.data, cIdx);
                }
                for (; j < that.storage.cols(); j++) C.data[i * that.storage.cols() + j] += aik * that.data[k * that.storage.cols() + j];
            }
        }
        return C;
    }

    @Override
    public Matrix<Real> transpose() {
        float[] tData = new float[storage.rows() * storage.cols()];
        for (int i = 0; i < storage.rows(); i++) {
            for (int j = 0; j < storage.cols(); j++) {
                tData[j * storage.rows() + i] = data[i * storage.cols() + j];
            }
        }
        return new SIMDRealFloatMatrix(storage.cols(), storage.rows(), tData);
    }
    
    @Override 
    public Matrix<Real> subtract(Matrix<Real> other) {
        SIMDRealFloatMatrix that = from(other);
        float[] res = new float[data.length];
        int i = 0;
        for (; i < SPECIES.loopBound(data.length); i += SPECIES.length()) {
            var v1 = FloatVector.fromArray(SPECIES, this.data, i);
            var v2 = FloatVector.fromArray(SPECIES, that.data, i);
            v1.sub(v2).intoArray(res, i);
        }
        for (; i < data.length; i++) res[i] = this.data[i] - that.data[i];
        return new SIMDRealFloatMatrix(storage.rows(), storage.cols(), res);
    }

    @Override public Vector<Real> multiply(Vector<Real> vector) {
        if (storage.cols() != vector.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        float[] bData = new float[vector.dimension()];
        for(int i=0; i<bData.length; i++) bData[i] = vector.get(i).floatValue();
        float[] res = new float[storage.rows()];
        for (int i = 0; i < storage.rows(); i++) {
            int rowOffset = i * storage.cols();
            FloatVector acc = FloatVector.zero(SPECIES);
            int j = 0;
            for (; j < SPECIES.loopBound(storage.cols()); j += SPECIES.length()) {
                var aVec = FloatVector.fromArray(SPECIES, data, rowOffset + j);
                var bVec = FloatVector.fromArray(SPECIES, bData, j);
                acc = acc.add(aVec.mul(bVec)); 
            }
            res[i] = acc.reduceLanes(jdk.incubator.vector.VectorOperators.ADD);
            for (; j < storage.cols(); j++) res[i] += data[rowOffset + j] * bData[j];
        }
        return (Vector<Real>) org.episteme.core.mathematics.linearalgebra.vectors.RealFloatVector.of(res);
    }
    
    @Override public Matrix<Real> negate() { 
        float[] res = new float[data.length];
        for(int i=0; i<data.length; i++) res[i] = -data[i];
        return new SIMDRealFloatMatrix(storage.rows(), storage.cols(), res);
    }
    @Override public Matrix<Real> zero() { return new SIMDRealFloatMatrix(storage.rows(), storage.cols()); }
    @Override public Matrix<Real> one() { 
        SIMDRealFloatMatrix m = new SIMDRealFloatMatrix(storage.rows(), storage.cols());
        for(int i=0; i<Math.min(storage.rows(),storage.cols()); i++) m.set(i,i, 1.0f);
        return m;
    }
    @Override public MatrixStorage<Real> getStorage() { return storage; }
    @Override public Ring<Real> getScalarRing() { return Reals.getInstance(); }
}
