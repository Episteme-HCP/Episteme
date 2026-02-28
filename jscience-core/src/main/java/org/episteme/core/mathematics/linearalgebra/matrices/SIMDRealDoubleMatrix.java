/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra.matrices;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.mathematics.linearalgebra.providers.CPUDenseLinearAlgebraProvider;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.HeapRealDoubleMatrixStorage;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.MatrixStorage;
import org.episteme.core.mathematics.linearalgebra.vectors.GenericVector;

/**
 * SIMD-accelerated Matrix implementation using JDK Vector API.
 * <p>
 * This matrix stores data in a flat double array and uses CPU vector instructions (AVX-512, NEON)
 * for element-wise operations and matrix multiplication.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class SIMDRealDoubleMatrix extends GenericMatrix<Real> implements AutoCloseable {

    private static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;

    private final double[] data;

    public SIMDRealDoubleMatrix(int rows, int cols) {
        this(rows, cols, new double[rows * cols]);
    }
    
    public SIMDRealDoubleMatrix(int rows, int cols, double[] data) {
        super(new HeapRealDoubleMatrixStorage(data, rows, cols),
              new CPUDenseLinearAlgebraProvider<>((org.episteme.core.mathematics.structures.rings.Field<Real>) Reals.getInstance()),
              Reals.getInstance());
        this.data = data;
    }

    @Override
    public void close() {
        // No-op for now unless we use off-heap memory later
    }

    @Override
    public int rows() { return storage.rows(); }

    @Override
    public int cols() { return storage.cols(); }

    @Override
    public Real get(int row, int col) {
        return Real.of(data[row * storage.cols() + col]);
    }

    public void set(int row, int col, double val) {
        data[row * storage.cols() + col] = val;
    }
    
    @Override
    public void set(int row, int col, Real val) {
        set(row, col, val.doubleValue());
    }

    // --- Advanced Vectorized Math Operations ---

    public Matrix<Real> abs() {
        return applyOp(VectorOperators.ABS, (d) -> Math.abs(d));
    }

    public Matrix<Real> sqrt() {
        return applyOp(VectorOperators.SQRT, (d) -> Math.sqrt(d));
    }

    public Matrix<Real> log() {
        return applyOp(VectorOperators.LOG, (d) -> Math.log(d));
    }

    public Matrix<Real> exp() {
        return applyOp(VectorOperators.EXP, (d) -> Math.exp(d));
    }

    public Matrix<Real> sin() {
        return applyOp(VectorOperators.SIN, (d) -> Math.sin(d));
    }

    public Matrix<Real> cos() {
        return applyOp(VectorOperators.COS, (d) -> Math.cos(d));
    }

    private Matrix<Real> applyOp(jdk.incubator.vector.VectorOperators.Unary op, java.util.function.DoubleUnaryOperator scalarFallback) {
        double[] res = new double[data.length];
        int i = 0;
        final VectorSpecies<Double> species = SPECIES;
        for (; i < species.loopBound(data.length); i += species.length()) {
            var v = DoubleVector.fromArray(species, this.data, i);
            v.lanewise(op).intoArray(res, i);
        }
        for (; i < data.length; i++) {
            res[i] = scalarFallback.applyAsDouble(data[i]);
        }
        return new SIMDRealDoubleMatrix(storage.rows(), storage.cols(), res);
    }

    public Matrix<Real> scale(double scalar) {
        double[] res = new double[data.length];
        int i = 0;
        final VectorSpecies<Double> species = SPECIES;
        for (; i < species.loopBound(data.length); i += species.length()) {
            var v = DoubleVector.fromArray(species, this.data, i);
            v.mul(DoubleVector.broadcast(species, scalar)).intoArray(res, i);
        }
        for (; i < data.length; i++) {
            res[i] = data[i] * scalar;
        }
        return new SIMDRealDoubleMatrix(storage.rows(), storage.cols(), res);
    }

    public double doubleSum() {
        int i = 0;
        DoubleVector acc = DoubleVector.zero(SPECIES);
        for (; i < SPECIES.loopBound(data.length); i += SPECIES.length()) {
            acc = acc.add(DoubleVector.fromArray(SPECIES, data, i));
        }
        double total = acc.reduceLanes(jdk.incubator.vector.VectorOperators.ADD);
        for (; i < data.length; i++) total += data[i];
        return total;
    }

    public double doubleMin() {
        int i = 0;
        DoubleVector acc = DoubleVector.broadcast(SPECIES, java.lang.Double.POSITIVE_INFINITY);
        for (; i < SPECIES.loopBound(data.length); i += SPECIES.length()) {
            acc = acc.lanewise(jdk.incubator.vector.VectorOperators.MIN, DoubleVector.fromArray(SPECIES, data, i));
        }
        double min = acc.reduceLanes(jdk.incubator.vector.VectorOperators.MIN);
        for (; i < data.length; i++) min = java.lang.Math.min(min, data[i]);
        return min;
    }

    public double doubleMax() {
        int i = 0;
        DoubleVector acc = DoubleVector.broadcast(SPECIES, java.lang.Double.NEGATIVE_INFINITY);
        for (; i < SPECIES.loopBound(data.length); i += SPECIES.length()) {
            acc = acc.lanewise(jdk.incubator.vector.VectorOperators.MAX, DoubleVector.fromArray(SPECIES, data, i));
        }
        double max = acc.reduceLanes(jdk.incubator.vector.VectorOperators.MAX);
        for (; i < data.length; i++) max = java.lang.Math.max(max, data[i]);
        return max;
    }

    @Override
    public Matrix<Real> add(Matrix<Real> other) {
        if (other instanceof SIMDRealDoubleMatrix) {
            SIMDRealDoubleMatrix that = (SIMDRealDoubleMatrix) other;
            checkDimensions(that);
            
            double[] res = new double[data.length];
            int i = 0;
            for (; i < SPECIES.loopBound(data.length); i += SPECIES.length()) {
                var v1 = DoubleVector.fromArray(SPECIES, this.data, i);
                var v2 = DoubleVector.fromArray(SPECIES, that.data, i);
                v1.add(v2).intoArray(res, i);
            }
            for (; i < data.length; i++) {
                res[i] = this.data[i] + that.data[i];
            }
            return new SIMDRealDoubleMatrix(storage.rows(), storage.cols(), res);
        }
        throw new UnsupportedOperationException("Mixed type addition not supported yet");
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> other) {
        if (other instanceof SIMDRealDoubleMatrix) {
            SIMDRealDoubleMatrix that = (SIMDRealDoubleMatrix) other;
            if (storage.cols() != that.storage.rows()) throw new IllegalArgumentException("Dimension mismatch");
            
            SIMDRealDoubleMatrix C = new SIMDRealDoubleMatrix(storage.rows(), that.storage.cols());
            
            for (int i = 0; i < storage.rows(); i++) {
                for (int k = 0; k < storage.cols(); k++) {
                    double aik = this.data[i * storage.cols() + k];
                    
                    int j = 0;
                    for (; j < SPECIES.loopBound(that.storage.cols()); j += SPECIES.length()) {
                        int bIdx = k * that.storage.cols() + j;
                        var bVec = DoubleVector.fromArray(SPECIES, that.data, bIdx);
                        
                        int cIdx = i * that.storage.cols() + j;
                        var cVec = DoubleVector.fromArray(SPECIES, C.data, cIdx);
                        cVec = bVec.fma(DoubleVector.broadcast(SPECIES, aik), cVec); 
                        cVec.intoArray(C.data, cIdx);
                    }
                    for (; j < that.storage.cols(); j++) {
                        C.data[i * that.storage.cols() + j] += aik * that.data[k * that.storage.cols() + j];
                    }
                }
            }
            return C;
        }
        throw new UnsupportedOperationException("Mixed type multiplication not supported yet");
    }
    
    private void checkDimensions(Matrix<?> other) {
        if (storage.rows() != other.rows() || storage.cols() != other.cols()) 
            throw new IllegalArgumentException("Dimensions match");
    }

    @Override
    public Matrix<Real> transpose() {
        double[] tData = new double[storage.rows() * storage.cols()];
        for (int i = 0; i < storage.rows(); i++) {
            for (int j = 0; j < storage.cols(); j++) {
                tData[j * storage.rows() + i] = data[i * storage.cols() + j];
            }
        }
        return new SIMDRealDoubleMatrix(storage.cols(), storage.rows(), tData);
    }
    
    @Override 
    public Matrix<Real> subtract(Matrix<Real> other) {
        if (other instanceof SIMDRealDoubleMatrix) {
            SIMDRealDoubleMatrix that = (SIMDRealDoubleMatrix) other;
            checkDimensions(that);
            double[] res = new double[data.length];
            int i = 0;
            for (; i < SPECIES.loopBound(data.length); i += SPECIES.length()) {
                DoubleVector v1 = DoubleVector.fromArray(SPECIES, this.data, i);
                DoubleVector v2 = DoubleVector.fromArray(SPECIES, that.data, i);
                v1.sub(v2).intoArray(res, i);
            }
            for (; i < data.length; i++) {
                res[i] = this.data[i] - that.data[i];
            }
            return new SIMDRealDoubleMatrix(storage.rows(), storage.cols(), res);
        }
        throw new UnsupportedOperationException("Mixed type subtraction not supported yet");
    }

    @Override public Real trace() { 
        double sum = 0;
        for(int i=0; i<Math.min(storage.rows(),storage.cols()); i++) sum += data[i*storage.cols() + i];
        return Real.of(sum);
    }
    
    @Override public Matrix<Real> getSubMatrix(int rs, int re, int cs, int ce) { 
        int r = re - rs;
        int c = ce - cs;
        double[] sub = new double[r*c];
        for(int i=0; i<r; i++) {
            System.arraycopy(data, (rs+i)*storage.cols() + cs, sub, i*c, c);
        }
        return new SIMDRealDoubleMatrix(r, c, sub);
    }

    public double[] getInternalData() {
        return data;
    }

    public double[] getRowData(int row) {
        double[] rowData = new double[storage.cols()];
        System.arraycopy(data, row * storage.cols(), rowData, 0, storage.cols());
        return rowData;
    }

    public void setRowData(int row, double[] rowData) {
        if (rowData.length != storage.cols()) throw new IllegalArgumentException("Length mismatch");
        System.arraycopy(rowData, 0, data, row * storage.cols(), storage.cols());
    }
    
    @Override public Vector<Real> getRow(int row) { throw new UnsupportedOperationException(); }
    @Override public Vector<Real> getColumn(int col) { throw new UnsupportedOperationException(); }
    @Override public Real determinant() { throw new UnsupportedOperationException(); }
    @Override public Matrix<Real> inverse() { throw new UnsupportedOperationException(); }
    @Override 
    public Vector<Real> multiply(Vector<Real> vector) {
        if (storage.cols() != vector.dimension()) {
            throw new IllegalArgumentException("Dimension mismatch");
        }
        
        double[] bData = new double[vector.dimension()];
        for(int i=0; i<bData.length; i++) bData[i] = vector.get(i).doubleValue();
        
        double[] res = new double[storage.rows()];
        
        for (int i = 0; i < storage.rows(); i++) {
            int rowOffset = i * storage.cols();
            
            DoubleVector acc = DoubleVector.zero(SPECIES);
            int j = 0;
            for (; j < SPECIES.loopBound(storage.cols()); j += SPECIES.length()) {
                var aVec = DoubleVector.fromArray(SPECIES, data, rowOffset + j);
                var bVec = DoubleVector.fromArray(SPECIES, bData, j);
                acc = acc.add(aVec.mul(bVec)); 
            }
            res[i] = acc.reduceLanes(jdk.incubator.vector.VectorOperators.ADD);
            
            for (; j < storage.cols(); j++) {
                res[i] += data[rowOffset + j] * bData[j];
            }
        }
        
        return new GenericVector<>(
             new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(
                 java.util.stream.DoubleStream.of(res).mapToObj(Real::of).toArray(Real[]::new)),
             new CPUDenseLinearAlgebraProvider<>((org.episteme.core.mathematics.structures.rings.Field<Real>) Reals.getInstance()),
             Reals.getInstance());
    }
    @Override public Matrix<Real> negate() { 
        double[] res = new double[data.length];
        for(int i=0; i<data.length; i++) res[i] = -data[i];
        return new SIMDRealDoubleMatrix(storage.rows(), storage.cols(), res);
    }
    @Override public Matrix<Real> zero() { return new SIMDRealDoubleMatrix(storage.rows(), storage.cols()); }
    @Override public Matrix<Real> one() { 
        SIMDRealDoubleMatrix m = new SIMDRealDoubleMatrix(storage.rows(), storage.cols());
        for(int i=0; i<Math.min(storage.rows(),storage.cols()); i++) m.set(i,i, 1.0);
        return m;
    }
    @Override public MatrixStorage<Real> getStorage() { throw new UnsupportedOperationException(); }
    @Override public Ring<Real> getScalarRing() { return Reals.getInstance(); }
    @Override public Matrix<Real> scale(Real scalar, Matrix<Real> element) { throw new UnsupportedOperationException(); }
    
    @Override
    public boolean contains(Matrix<Real> element) {
        return storage.rows() == element.rows() && storage.cols() == element.cols();
    }
    @Override
    public String description() {
        return "SIMD Matrix (" + storage.rows() + "x" + storage.cols() + ") using " + SPECIES.toString();
    }
}
