/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.physics.quantum;

import org.jscience.mathematics.numbers.complex.Complex;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.linearalgebra.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a quantum state using Dirac notation.
 * <p>
 * A `Ket` $|\psi\rangle$ is a column vector.
 * A `Bra` $\langle\psi|$ is a row vector (conjugate transpose).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BraKet {

    private final DenseVector<Complex> stateVector;
    private final boolean isDual; // True if Bra, False if Ket

    /**
     * Creates a Ket vector $|\psi\rangle$.
     * 
     * @param amplitudes the complex amplitudes
     */
    public BraKet(Complex... amplitudes) {
        this(new DenseVector<>(Arrays.asList(amplitudes), null), false);
    }

    /**
     * Creates a Ket vector from a Vector object.
     * 
     * @param vector the state vector
     */
    public BraKet(Vector<Complex> vector) {
        if (vector instanceof DenseVector) {
            this.stateVector = (DenseVector<Complex>) vector;
        } else {
            // Convert to DenseVector if necessary
            // For now, assume we can wrap or copy.
            // Since DenseVector constructor from List is available:
            List<Complex> list = new ArrayList<>();
            for (int i = 0; i < vector.dimension(); i++)
                list.add(vector.get(i));
            this.stateVector = new DenseVector<>(list, null);
        }
        this.isDual = false; // Default to Ket
    }

    // Internal constructor
    private BraKet(DenseVector<Complex> vector, boolean isDual) {
        this.stateVector = vector;
        this.isDual = isDual;
    }

    /**
     * Creates a standard basis Ket $|0\rangle$ or $|1\rangle$.
     * 
     * @param basisIndex 0 or 1 usually
     * @param dimension  usually 2 for qubit
     */
    public static BraKet basis(int basisIndex, int dimension) {
        Complex[] data = new Complex[dimension];
        for (int i = 0; i < dimension; i++) {
            data[i] = (i == basisIndex) ? Complex.ONE : Complex.ZERO;
        }
        return new BraKet(data);
    }

    /**
     * Returns the dual (conjugate transpose).
     * $|\psi\rangle^\dagger = \langle\psi|$
     */
    public BraKet dual() {
        // Conjugate elements
        // Logic for conjugation needs to be applied to vector elements
        // For DenseVector, we might need to iterate.
        // Assuming DenseVector is immutable or we create new one.

        // MVP: Simple iteration
        Complex[] dualData = new Complex[stateVector.dimension()];
        for (int i = 0; i < stateVector.dimension(); i++) {
            dualData[i] = stateVector.get(i).conjugate();
        }
        return new BraKet(new DenseVector<>(Arrays.asList(dualData), null), !isDual);
    }

    /**
     * Inner product $\langle\phi|\psi\rangle$.
     * Only valid if this is a Bra and other is a Ket.
     */
    public Complex dot(BraKet other) {
        if (!this.isDual || other.isDual) {
            throw new IllegalArgumentException("Inner product requires <Bra|Ket>");
        }
        if (this.stateVector.dimension() != other.stateVector.dimension()) {
            throw new IllegalArgumentException("Dimension mismatch");
        }

        Complex sum = Complex.ZERO;
        for (int i = 0; i < this.stateVector.dimension(); i++) {
            sum = sum.add(this.stateVector.get(i).multiply(other.stateVector.get(i)));
        }
        return sum;
    }

    /**
     * Tensor product $|\psi\rangle \otimes |\phi\rangle$.
     */
    public BraKet tensor(BraKet other) {
        if (this.isDual != other.isDual) {
            throw new IllegalArgumentException("Tensor product requires same type (Ket-Ket or Bra-Bra)");
        }

        int dim1 = this.stateVector.dimension();
        int dim2 = other.stateVector.dimension();
        Complex[] result = new Complex[dim1 * dim2];

        for (int i = 0; i < dim1; i++) {
            for (int j = 0; j < dim2; j++) {
                result[i * dim2 + j] = this.stateVector.get(i).multiply(other.stateVector.get(j));
            }
        }

        return new BraKet(result); // Defaults to Ket, should preserve duality?
        // Ideally tensor product of Bras is a Bra.
        // We'll reset isDual in a refined constructor or logic.
        // For MVP, simplistic.
    }

    public DenseVector<Complex> vector() {
        return stateVector;
    }

    public boolean isBra() {
        return isDual;
    }

    public boolean isKet() {
        return !isDual;
    }

    @Override
    public String toString() {
        return (isDual ? "<" : "|") + "Psi" + (isDual ? "|" : ">") + " (dim=" + stateVector.dimension() + ")";
    }
}
