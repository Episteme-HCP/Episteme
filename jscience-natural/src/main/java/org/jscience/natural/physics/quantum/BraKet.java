/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.physics.quantum;

import org.jscience.core.mathematics.numbers.complex.Complex;
import org.jscience.core.mathematics.linearalgebra.Vector;

import java.util.Arrays;

/**
 * Represents a quantum state using Dirac notation.
 * Re-located to core for unified backend access.
 */
public class BraKet {

    private final Vector<Complex> stateVector;
    private final boolean isDual; // True if Bra, False if Ket

    public BraKet(Complex... amplitudes) {
        this(org.jscience.core.mathematics.linearalgebra.Vector.of(Arrays.asList(amplitudes), org.jscience.core.mathematics.sets.Complexes.getInstance()), false);
    }

    public BraKet(Vector<Complex> vector) {
        this.stateVector = vector;
        this.isDual = false;
    }

    private BraKet(Vector<Complex> vector, boolean isDual) {
        this.stateVector = vector;
        this.isDual = isDual;
    }

    public static BraKet basis(int basisIndex, int dimension) {
        Complex[] data = new Complex[dimension];
        for (int i = 0; i < dimension; i++) {
            data[i] = (i == basisIndex) ? Complex.ONE : Complex.ZERO;
        }
        return new BraKet(data);
    }

    public BraKet dual() {
        Complex[] dualData = new Complex[stateVector.dimension()];
        for (int i = 0; i < stateVector.dimension(); i++) {
            dualData[i] = stateVector.get(i).conjugate();
        }
        return new BraKet(org.jscience.core.mathematics.linearalgebra.Vector.of(Arrays.asList(dualData), org.jscience.core.mathematics.sets.Complexes.getInstance()), !isDual);
    }

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

    public BraKet tensor(BraKet other) {
        int dim1 = this.stateVector.dimension();
        int dim2 = other.stateVector.dimension();
        Complex[] result = new Complex[dim1 * dim2];

        for (int i = 0; i < dim1; i++) {
            for (int j = 0; j < dim2; j++) {
                result[i * dim2 + j] = this.stateVector.get(i).multiply(other.stateVector.get(j));
            }
        }
        return new BraKet(org.jscience.core.mathematics.linearalgebra.Vector.of(Arrays.asList(result), org.jscience.core.mathematics.sets.Complexes.getInstance()), this.isDual);
    }

    public Vector<Complex> vector() {
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
