/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.physics.quantum;

import org.jscience.core.mathematics.numbers.complex.Complex;
import org.jscience.core.mathematics.linearalgebra.matrices.DenseMatrix;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a unitary quantum gate.
 * Re-located to core for unified backend access.
 */
public class QuantumGate {

    private final QuantumGateType type;
    private final int[] targetQubits;
    private final DenseMatrix<Complex> matrix;
    private final int qubits;
    private double parameter = 0.0;

    public QuantumGate(QuantumGateType type, int... targetQubits) {
        this.type = type;
        this.targetQubits = targetQubits;
        this.qubits = targetQubits.length;
        this.matrix = createMatrixForType(type);
    }

    public QuantumGate(DenseMatrix<Complex> matrix) {
        this.type = null;
        this.targetQubits = new int[0];
        this.matrix = matrix;
        this.qubits = (int) (Math.log(matrix.rows()) / Math.log(2));
    }

    public static QuantumGate hadamard() { return new QuantumGate(hadamardMatrix()); }
    public static QuantumGate pauliX() { return new QuantumGate(pauliXMatrix()); }
    public static QuantumGate pauliY() { return new QuantumGate(pauliYMatrix()); }
    public static QuantumGate pauliZ() { return new QuantumGate(pauliZMatrix()); }
    public static QuantumGate cnot() { return new QuantumGate(cnotMatrix()); }

    public QuantumGateType getType() { return type; }
    public int[] getTargetQubits() { return targetQubits; }
    public int getQubits() { return qubits; }
    public DenseMatrix<Complex> getMatrix() { return matrix; }

    public void setParameter(double p) { this.parameter = p; }
    public double getParameter() { return parameter; }

    @Override
    public String toString() {
        if (type != null) return type + " " + Arrays.toString(targetQubits);
        return "CustomGate(" + qubits + " qubits)";
    }
    
    private static DenseMatrix<Complex> createMatrixForType(QuantumGateType t) {
        if (t == null) return null;
        switch (t) {
            case H: return hadamardMatrix();
            case X: return pauliXMatrix();
            case Y: return pauliYMatrix();
            case Z: return pauliZMatrix();
            case CX: return cnotMatrix();
            default: return null;
        }
    }

    private static DenseMatrix<Complex> hadamardMatrix() {
        Complex scalar = Complex.of(1.0 / Math.sqrt(2));
        Complex[][] data = {{ scalar, scalar }, { scalar, scalar.negate() }};
        return createMatrix(data);
    }

    private static DenseMatrix<Complex> pauliXMatrix() {
        Complex[][] data = {{ Complex.ZERO, Complex.ONE }, { Complex.ONE, Complex.ZERO }};
        return createMatrix(data);
    }

    private static DenseMatrix<Complex> pauliYMatrix() {
        Complex i = Complex.I;
        Complex[][] data = {{ Complex.ZERO, i.negate() }, { i, Complex.ZERO }};
        return createMatrix(data);
    }

    private static DenseMatrix<Complex> pauliZMatrix() {
        Complex[][] data = {{ Complex.ONE, Complex.ZERO }, { Complex.ZERO, Complex.ONE.negate() }};
        return createMatrix(data);
    }
    
    private static DenseMatrix<Complex> cnotMatrix() {
        Complex one = Complex.ONE;
        Complex zero = Complex.ZERO;
        Complex[][] data = {
                { one, zero, zero, zero },
                { zero, one, zero, zero },
                { zero, zero, zero, one },
                { zero, zero, one, zero }
        };
        return createMatrix(data);
    }

    private static DenseMatrix<Complex> createMatrix(Complex[][] data) {
         List<List<Complex>> rowsList = new ArrayList<>();
         for (Complex[] row : data) rowsList.add(Arrays.asList(row));
         return new DenseMatrix<>(rowsList, Complex.ZERO);
    }
    
    public BraKet apply(BraKet ket) {
        if (ket.isBra()) throw new IllegalArgumentException("Gates cannot be applied directly to Bra vectors.");
        if (matrix == null) throw new UnsupportedOperationException("This gate does not have a matrix representation.");

        int n = matrix.rows();
        if (n != ket.vector().dimension()) throw new IllegalArgumentException("Dimension mismatch");
        
        Complex[] output = new Complex[n];
        for (int i = 0; i < n; i++) {
            Complex sum = Complex.ZERO;
            for (int j = 0; j < n; j++) {
                sum = sum.add(matrix.get(i, j).multiply(ket.vector().get(j)));
            }
            output[i] = sum;
        }
        return new BraKet(output);
    }
}
