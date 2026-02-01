/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.linearalgebra.matrices;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.MatrixStorage;
import org.jscience.core.mathematics.structures.rings.Ring;

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
public class SIMDDoubleMatrix implements Matrix<Real> {

    private static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;

    private final double[] data;
    private final int rows;
    private final int cols;

    public SIMDDoubleMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows * cols];
    }
    
    public SIMDDoubleMatrix(int rows, int cols, double[] data) {
        this.rows = rows;
        this.cols = cols;
        this.data = data;
    }

    @Override
    public int rows() { return rows; }

    @Override
    public int cols() { return cols; }

    @Override
    public Real get(int row, int col) {
        return Real.of(data[row * cols + col]);
    }

    public void set(int row, int col, double val) {
        data[row * cols + col] = val;
    }
    
    // Matrix Interface Stubs needed for set(int,int,Real)
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

    /**
     * Generic internal helper for Unary Operators
     */
    private Matrix<Real> applyOp(jdk.incubator.vector.VectorOperators.Unary op, java.util.function.DoubleUnaryOperator scalarFallback) {
        double[] res = new double[data.length];
        int i = 0;
        final jdk.incubator.vector.VectorSpecies<Double> species = SPECIES;
        for (; i < species.loopBound(data.length); i += species.length()) {
            var v = jdk.incubator.vector.DoubleVector.fromArray(species, this.data, i);
            v.lanewise(op).intoArray(res, i);
        }
        for (; i < data.length; i++) {
            res[i] = scalarFallback.applyAsDouble(data[i]);
        }
        return new SIMDDoubleMatrix(rows, cols, res);
    }

    public Matrix<Real> scale(double scalar) {
        double[] res = new double[data.length];
        int i = 0;
        final jdk.incubator.vector.VectorSpecies<Double> species = SPECIES;
        for (; i < species.loopBound(data.length); i += species.length()) {
            var v = jdk.incubator.vector.DoubleVector.fromArray(species, this.data, i);
            v.mul(jdk.incubator.vector.DoubleVector.broadcast(species, scalar)).intoArray(res, i);
        }
        for (; i < data.length; i++) {
            res[i] = data[i] * scalar;
        }
        return new SIMDDoubleMatrix(rows, cols, res);
    }

    public double doubleSum() {
        int i = 0;
        jdk.incubator.vector.DoubleVector acc = jdk.incubator.vector.DoubleVector.zero(SPECIES);
        for (; i < SPECIES.loopBound(data.length); i += SPECIES.length()) {
            acc = acc.add(jdk.incubator.vector.DoubleVector.fromArray(SPECIES, data, i));
        }
        double total = acc.reduceLanes(jdk.incubator.vector.VectorOperators.ADD);
        for (; i < data.length; i++) total += data[i];
        return total;
    }

    public double doubleMin() {
        int i = 0;
        jdk.incubator.vector.DoubleVector acc = jdk.incubator.vector.DoubleVector.broadcast(SPECIES, java.lang.Double.POSITIVE_INFINITY);
        for (; i < SPECIES.loopBound(data.length); i += SPECIES.length()) {
            acc = acc.lanewise(jdk.incubator.vector.VectorOperators.MIN, jdk.incubator.vector.DoubleVector.fromArray(SPECIES, data, i));
        }
        double min = acc.reduceLanes(jdk.incubator.vector.VectorOperators.MIN);
        for (; i < data.length; i++) min = java.lang.Math.min(min, data[i]);
        return min;
    }

    public double doubleMax() {
        int i = 0;
        jdk.incubator.vector.DoubleVector acc = jdk.incubator.vector.DoubleVector.broadcast(SPECIES, java.lang.Double.NEGATIVE_INFINITY);
        for (; i < SPECIES.loopBound(data.length); i += SPECIES.length()) {
            acc = acc.lanewise(jdk.incubator.vector.VectorOperators.MAX, jdk.incubator.vector.DoubleVector.fromArray(SPECIES, data, i));
        }
        double max = acc.reduceLanes(jdk.incubator.vector.VectorOperators.MAX);
        for (; i < data.length; i++) max = java.lang.Math.max(max, data[i]);
        return max;
    }

    @Override
    public Matrix<Real> add(Matrix<Real> other) {
        if (other instanceof SIMDDoubleMatrix) {
            SIMDDoubleMatrix that = (SIMDDoubleMatrix) other;
            checkDimensions(that);
            
            double[] res = new double[data.length];
            int i = 0;
            // Vector loop
            for (; i < SPECIES.loopBound(data.length); i += SPECIES.length()) {
                var v1 = DoubleVector.fromArray(SPECIES, this.data, i);
                var v2 = DoubleVector.fromArray(SPECIES, that.data, i);
                v1.add(v2).intoArray(res, i);
            }
            // Scalar tail loop
            for (; i < data.length; i++) {
                res[i] = this.data[i] + that.data[i];
            }
            return new SIMDDoubleMatrix(rows, cols, res);
        }
        // Fallback
        throw new UnsupportedOperationException("Mixed type addition not supported yet");
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> other) {
        if (other instanceof SIMDDoubleMatrix) {
            SIMDDoubleMatrix that = (SIMDDoubleMatrix) other;
            if (this.cols != that.rows) throw new IllegalArgumentException("Dimension mismatch");
            
            SIMDDoubleMatrix C = new SIMDDoubleMatrix(this.rows, that.cols);
            
            // Naive loop with vectorization on the inner dot product?
            // Actually, for C[i][j] += A[i][k] * B[k][j], we want to vectorize k.
            // B needs to be accessed column-wise.
            
            // Blocked multiplication is better, but let's do a simple vectorized row-by-row
            // or use "broadcast A[i][k]" * "Vector B[k][...]"
            
            // Simplest efficient SIMD: C[i][j..j+W] += A[i][k] * B[k][j..j+W]
            // We iterate i, k, then vectorize j.
            
            for (int i = 0; i < this.rows; i++) {
                for (int k = 0; k < this.cols; k++) {
                    double aik = this.data[i * this.cols + k];
                    
                    int j = 0;
                    // Vectorize j loop
                    for (; j < SPECIES.loopBound(that.cols); j += SPECIES.length()) {
                        int bIdx = k * that.cols + j;
                        var bVec = DoubleVector.fromArray(SPECIES, that.data, bIdx);
                        
                        int cIdx = i * that.cols + j;
                        // Load C (accumulation)
                        var cVec = DoubleVector.fromArray(SPECIES, C.data, cIdx);
                        
                        // FMA: c = c + a * b
                        cVec = bVec.fma(jdk.incubator.vector.DoubleVector.broadcast(SPECIES, aik), cVec); 
                        // Or: cVec = cVec.add(bVec.mul(DoubleVector.broadcast(SPECIES, aik)));
                        
                        cVec.intoArray(C.data, cIdx);
                    }
                    // Scalar tail for j
                    for (; j < that.cols; j++) {
                        C.data[i * that.cols + j] += aik * that.data[k * that.cols + j];
                    }
                }
            }
            return C;
        }
        throw new UnsupportedOperationException("Mixed type multiplication not supported yet");
    }
    
    private void checkDimensions(Matrix<?> other) {
        if (rows != other.rows() || cols != other.cols()) 
            throw new IllegalArgumentException("Dimensions match");
    }

    // --- Required Boilerplate ---
    
    @Override
    public Matrix<Real> transpose() {
        // Efficient Transpose? 
        // For large matrices, cache-oblivious or block transpose is best.
        // Simple implementation first
        double[] tData = new double[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                tData[j * rows + i] = data[i * cols + j];
            }
        }
        return new SIMDDoubleMatrix(cols, rows, tData);
    }
    
    @Override 
    public Matrix<Real> subtract(Matrix<Real> other) {
        if (other instanceof SIMDDoubleMatrix) {
            SIMDDoubleMatrix that = (SIMDDoubleMatrix) other;
            checkDimensions(that);
            double[] res = new double[data.length];
            int i = 0;
            // Vector loop
            for (; i < SPECIES.loopBound(data.length); i += SPECIES.length()) {
                var v1 = DoubleVector.fromArray(SPECIES, this.data, i);
                var v2 = DoubleVector.fromArray(SPECIES, that.data, i);
                v1.sub(v2).intoArray(res, i);
            }
            // Scalar tail loop
            for (; i < data.length; i++) {
                res[i] = this.data[i] - that.data[i];
            }
            return new SIMDDoubleMatrix(rows, cols, res);
        }
        throw new UnsupportedOperationException("Mixed type subtraction not supported yet");
    }

    @Override public Real trace() { 
        double sum = 0;
        for(int i=0; i<Math.min(rows,cols); i++) sum += data[i*cols + i];
        return Real.of(sum);
    }
    
    @Override public Matrix<Real> getSubMatrix(int rs, int re, int cs, int ce) { 
        // Standard Java pattern: end indices (re, ce) are exclusive
        int r = re - rs;
        int c = ce - cs;
        double[] sub = new double[r*c];
        for(int i=0; i<r; i++) {
            System.arraycopy(data, (rs+i)*cols + cs, sub, i*c, c);
        }
        return new SIMDDoubleMatrix(r, c, sub);
    }

    /**
     * Returns the underlying double array. Use with caution.
     */
    public double[] getInternalData() {
        return data;
    }

    /**
     * Efficiently copies a row into a double array.
     */
    public double[] getRowData(int row) {
        double[] rowData = new double[cols];
        System.arraycopy(data, row * cols, rowData, 0, cols);
        return rowData;
    }

    /**
     * Efficiently sets a row from a double array.
     */
    public void setRowData(int row, double[] rowData) {
        if (rowData.length != cols) throw new IllegalArgumentException("Length mismatch");
        System.arraycopy(rowData, 0, data, row * cols, cols);
    }
    
    @Override public Vector<Real> getRow(int row) { throw new UnsupportedOperationException(); }
    @Override public Vector<Real> getColumn(int col) { throw new UnsupportedOperationException(); }
    @Override public Real determinant() { throw new UnsupportedOperationException(); }
    @Override public Matrix<Real> inverse() { throw new UnsupportedOperationException(); }
    @Override public Vector<Real> multiply(Vector<Real> vector) { throw new UnsupportedOperationException(); }
    @Override public Matrix<Real> negate() { 
        double[] res = new double[data.length];
        for(int i=0; i<data.length; i++) res[i] = -data[i];
        return new SIMDDoubleMatrix(rows, cols, res);
    }
    @Override public Matrix<Real> zero() { return new SIMDDoubleMatrix(rows, cols); }
    @Override public Matrix<Real> one() { 
        SIMDDoubleMatrix m = new SIMDDoubleMatrix(rows, cols);
        for(int i=0; i<Math.min(rows,cols); i++) m.set(i,i, 1.0);
        return m;
    }
    @Override public MatrixStorage<Real> getStorage() { throw new UnsupportedOperationException(); }
    @Override public Ring<Real> getScalarRing() { return Reals.getInstance(); }
    @Override public Matrix<Real> scale(Real scalar, Matrix<Real> element) { throw new UnsupportedOperationException(); }
    
    @Override
    public boolean contains(Matrix<Real> element) {
        return rows == element.rows() && cols == element.cols();
    }
    @Override
    public String description() {
        return "SIMD Matrix (" + rows + "x" + cols + ") using " + SPECIES.toString();
    }
}
