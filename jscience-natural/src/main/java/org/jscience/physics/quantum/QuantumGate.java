package org.jscience.physics.quantum;

import org.jscience.mathematics.numbers.complex.Complex;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a unitary quantum gate.
 * Supports both Matrix-based simulation and Symbolic (OpenQASM) representation.
 */
public class QuantumGate {

    private final QuantumGateType type;
    private final int[] targetQubits;
    private final DenseMatrix<Complex> matrix;
    private final int qubits;

    /**
     * Constructor for Symbolic Gate (e.g., for Qiskit/QASM generation).
     * Initializes the matrix for standard gates if possible.
     * @param type The type of the gate.
     * @param targetQubits The target qubits.
     */
    public QuantumGate(QuantumGateType type, int... targetQubits) {
        this.type = type;
        this.targetQubits = targetQubits;
        this.qubits = targetQubits.length;
        this.matrix = createMatrixForType(type);
    }

    /**
     * Constructor for Raw Matrix Gate (Custom).
     * @param matrix The unitary matrix.
     */
    public QuantumGate(DenseMatrix<Complex> matrix) {
        this.type = null;
        this.targetQubits = new int[0];
        this.matrix = matrix;
        this.qubits = (int) (Math.log(matrix.rows()) / Math.log(2));
    }

    public QuantumGateType getType() {
        return type;
    }

    public int[] getTargetQubits() {
        return targetQubits;
    }

    public int getQubits() {
        return qubits;
    }

    public DenseMatrix<Complex> getMatrix() {
        return matrix;
    }

    @Override
    public String toString() {
        if (type != null) {
            return type + " " + Arrays.toString(targetQubits);
        }
        return "CustomGate(" + qubits + " qubits)";
    }
    
    // --- Helper to create matrices for standard types ---
    
    private static DenseMatrix<Complex> createMatrixForType(QuantumGateType t) {
        if (t == null) return null;
        switch (t) {
            case H: return hadamardMatrix();
            case X: return pauliXMatrix();
            case Y: return pauliYMatrix();
            case Z: return pauliZMatrix();
             // CNOT involves 2 qubits, matrix is 4x4
            case CX: return cnotMatrix();
            default: return null;
        }
    }

    // --- Static Factories returning QuantumGate (wrapping the matrix) ---
    // Kept for backward compatibility but mapped to new structure where possible.

    public static QuantumGate hadamard() {
        return new QuantumGate(QuantumGateType.H, 0); // Default to q0 target if unused? Or just return wrapper?
        // Existing factories returned a gate with just a matrix. 
        // We can use the Matrix constructor, OR the Type constructor with NO targets? 
        // If targets are empty, it's just an operator.
        // Let's use Symbolic constructor with empty targets if generic.
    }

    // --- Matrix Generation Methods ---

    private static DenseMatrix<Complex> hadamardMatrix() {
        Complex scalar = Complex.of(1.0 / Math.sqrt(2));
        Complex[][] data = {
                { scalar, scalar },
                { scalar, scalar.negate() }
        };
        return createMatrix(data);
    }

    private static DenseMatrix<Complex> pauliXMatrix() {
        Complex[][] data = {
                { Complex.ZERO, Complex.ONE },
                { Complex.ONE, Complex.ZERO }
        };
        return createMatrix(data);
    }

    private static DenseMatrix<Complex> pauliYMatrix() {
        Complex i = Complex.I;
        Complex neg_i = i.negate();
        Complex[][] data = {
                { Complex.ZERO, neg_i },
                { i, Complex.ZERO }
        };
        return createMatrix(data);
    }

    private static DenseMatrix<Complex> pauliZMatrix() {
        Complex[][] data = {
                { Complex.ONE, Complex.ZERO },
                { Complex.ZERO, Complex.ONE.negate() }
        };
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
         for (Complex[] row : data) {
             rowsList.add(Arrays.asList(row));
         }
         return new DenseMatrix<>(rowsList, Complex.ZERO);
    }
    
    /**
     * Applies this gate to a quantum state (Ket).
     */
    public BraKet apply(BraKet ket) {
        if (ket.isBra()) {
            throw new IllegalArgumentException("Gates cannot be applied directly to Bra vectors.");
        }
        if (matrix == null) {
             throw new UnsupportedOperationException("This gate does not have a matrix representation.");
        }

        // Manual Matrix-Vector multiplication
        Complex[] inputObj = new Complex[ket.vector().dimension()];
        for (int i = 0; i < inputObj.length; i++)
            inputObj[i] = ket.vector().get(i);

        Complex[] outputObj = new Complex[inputObj.length];
        int n = matrix.rows();
        // Check dimensions
        if (n != inputObj.length) {
             // If gate is smaller than system (e.g., 1-qubit gate on 3-qubit system),
             // we technically need Tensor Product expansion.
             // For MVP, if dimensions mismatch, we might throw or assume full system gate.
             // For valid simulation, the gate MUST be same size as system.
             // (Expansion logic usually happens in Context/Circuit before calling apply).
             throw new IllegalArgumentException("Gate dimension " + n + " != State dimension " + inputObj.length);
        }
        
        for (int i = 0; i < n; i++) {
            Complex sum = Complex.ZERO;
            for (int j = 0; j < n; j++) {
                sum = sum.add(matrix.get(i, j).multiply(inputObj[j]));
            }
            outputObj[i] = sum;
        }

        return new BraKet(outputObj);
    }
}
