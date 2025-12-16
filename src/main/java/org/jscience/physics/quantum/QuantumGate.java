package org.jscience.physics.quantum;

import org.jscience.mathematics.numbers.complex.Complex;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a unitary quantum gate.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 3.3
 */
public class QuantumGate {

    private final DenseMatrix<Complex> matrix;
    private final int qubits;

    public QuantumGate(DenseMatrix<Complex> matrix) {
        if (matrix.rows() != matrix.cols()) {
            throw new IllegalArgumentException("Gate matrix must be square");
        }
        this.matrix = matrix;
        // Check if dimension is power of 2
        this.qubits = (int) (Math.log(matrix.rows()) / Math.log(2));
    }

    public int getQubits() {
        return qubits;
    }

    /**
     * Applies this gate to a quantum state (Ket).
     * $|\psi'\rangle = U |\psi\rangle$
     */
    public BraKet apply(BraKet ket) {
        if (ket.isBra()) {
            // If applying to Bra: <psi| U^\dagger
            // Simple implementation assumes Ket for now as primary state carrier.
            throw new UnsupportedOperationException(
                    "Applying gate to Bra not yet implemented (requires conjugate transpose check)");
        }

        // Matrix-Vector multiplication
        // DenseMatrix has multiply(Vector)
        // We need to extract vector from BraKet

        // Note: DenseMatrix.multiply(Vector) returns Vector.
        // We need to cast or convert.
        // Actually DenseMatrix.multiply returns Vector<E>

        // DenseVector<Complex> result = matrix.multiply(ket.vector()); // Type issue
        // potentially if interfaces mismatch
        // Using explicit loop or relying on library if clean.

        // Let's assume DenseMatrix can multiply DenseVector
        // If not, we do it manually for MVP clarity.

        Complex[] inputObj = new Complex[ket.vector().dimension()];
        for (int i = 0; i < inputObj.length; i++)
            inputObj[i] = ket.vector().get(i);

        Complex[] outputObj = new Complex[inputObj.length];

        int n = matrix.rows();
        for (int i = 0; i < n; i++) {
            Complex sum = Complex.ZERO;
            for (int j = 0; j < n; j++) {
                sum = sum.add(matrix.get(i, j).multiply(inputObj[j]));
            }
            outputObj[i] = sum;
        }

        return new BraKet(outputObj);
    }

    // --- Standard Gates ---

    public static QuantumGate hadamard() {
        // H = 1/sqrt(2) * [[1, 1], [1, -1]]
        Complex scalar = Complex.of(1.0 / Math.sqrt(2));
        Complex[][] data = {
                { scalar, scalar },
                { scalar, scalar.negate() }
        };
        return createGate(data);
    }

    public static QuantumGate pauliX() {
        // X = [[0, 1], [1, 0]]
        Complex[][] data = {
                { Complex.ZERO, Complex.ONE },
                { Complex.ONE, Complex.ZERO }
        };
        return createGate(data);
    }

    public static QuantumGate pauliY() {
        // Y = [[0, -i], [i, 0]]
        Complex i = Complex.I;
        Complex neg_i = i.negate();
        Complex[][] data = {
                { Complex.ZERO, neg_i },
                { i, Complex.ZERO }
        };
        return createGate(data);
    }

    public static QuantumGate pauliZ() {
        // Z = [[1, 0], [0, -1]]
        Complex[][] data = {
                { Complex.ONE, Complex.ZERO },
                { Complex.ZERO, Complex.ONE.negate() }
        };
        return createGate(data);
    }

    public static QuantumGate cnot() {
        // CNOT (control 0, target 1)
        // [[1, 0, 0, 0], [0, 1, 0, 0], [0, 0, 0, 1], [0, 0, 1, 0]]
        Complex one = Complex.ONE;
        Complex zero = Complex.ZERO;
        Complex[][] data = {
                { one, zero, zero, zero },
                { zero, one, zero, zero },
                { zero, zero, zero, one },
                { zero, zero, one, zero }
        };
        return createGate(data);
    }

    private static QuantumGate createGate(Complex[][] data) {

        List<List<Complex>> rowsList = new ArrayList<>();
        for (Complex[] row : data) {
            rowsList.add(Arrays.asList(row));
        }
        // Assuming Complex implements Field<Complex>
        // We need an instance of Field<Complex> (e.g., Complex.ZERO typically acts as
        // factory/field?)
        // DenseMatrix.of takes rows and Field<E> field
        return new QuantumGate(new DenseMatrix<>(rowsList, Complex.ZERO));
    }

    public DenseMatrix<Complex> getMatrix() {
        return matrix;
    }
}
