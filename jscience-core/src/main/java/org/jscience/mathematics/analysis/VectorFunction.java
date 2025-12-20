/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.analysis;

import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;

/**
 * Represents a function from a vector space to another (F^n -> F^m).
 * <p>
 * Supports Jacobian matrix calculation for differentiation.
 * </p>
 * 
 * @param <F> the field of the vector elements
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface VectorFunction<F extends Field<F>> extends DifferentiableFunction<Vector<F>, Vector<F>> {

    /**
     * Returns the dimension of the output vector.
     * 
     * @return m where f: F^n -> F^m
     */
    int outputDimension();

    /**
     * Calculates the Jacobian matrix at the given point.
     * J_ij = d(f_i)/d(x_j)
     * 
     * Default implementation uses central difference approximation if F is Real.
     * 
     * @param point the point at which to evaluate the Jacobian
     * @return the Jacobian matrix (m x n)
     */
    default Matrix<F> jacobian(Vector<F> point) {
        F val = point.get(0);
        if (!(val instanceof org.jscience.mathematics.numbers.real.Real)) {
            throw new UnsupportedOperationException("Automatic Jacobian only supported for Real fields");
        }

        int n = point.dimension();
        Vector<F> f0 = evaluate(point);
        int m = f0.dimension();

        // Use central difference: f(x+h) - f(x-h) / 2h
        double hVal = 1e-8;
        @SuppressWarnings("unchecked")
        F h = (F) org.jscience.mathematics.numbers.real.Real.of(hVal);
        @SuppressWarnings("unchecked")
        F twoH = (F) org.jscience.mathematics.numbers.real.Real.of(2 * hVal);

        // Get Field from Vector's scalar ring
        @SuppressWarnings("unchecked")
        Field<F> field = (Field<F>) point.getScalarRing();

        java.util.List<java.util.List<F>> columns = new java.util.ArrayList<>();

        for (int j = 0; j < n; j++) {
            // Create x+h and x-h
            java.util.List<F> plusCoords = new java.util.ArrayList<>();
            java.util.List<F> minusCoords = new java.util.ArrayList<>();
            for (int k = 0; k < n; k++) {
                F coord = point.get(k);
                if (k == j) {
                    // Use Field operations: field.add(a, b), field.subtract(a, b)
                    plusCoords.add(field.add(coord, h));
                    minusCoords.add(field.subtract(coord, h));
                } else {
                    plusCoords.add(coord);
                    minusCoords.add(coord);
                }
            }

            Vector<F> pPlus = org.jscience.mathematics.linearalgebra.vectors.VectorFactory.create(plusCoords, field);
            Vector<F> pMinus = org.jscience.mathematics.linearalgebra.vectors.VectorFactory.create(minusCoords, field);

            Vector<F> fPlus = evaluate(pPlus);
            Vector<F> fMinus = evaluate(pMinus);

            // Column j = (fPlus - fMinus) / 2h
            F invTwoH = field.inverse(twoH);
            Vector<F> diff = fPlus.subtract(fMinus).multiply(invTwoH);

            // Add to columns list
            java.util.List<F> colData = new java.util.ArrayList<>();
            for (int i = 0; i < m; i++) {
                colData.add(diff.get(i));
            }
            columns.add(colData);
        }

        // Transpose to rows for MatrixFactory (create takes row-major List<List>)
        java.util.List<java.util.List<F>> rows = new java.util.ArrayList<>();
        for (int i = 0; i < m; i++) {
            java.util.List<F> rowData = new java.util.ArrayList<>();
            for (int j = 0; j < n; j++) {
                rowData.add(columns.get(j).get(i));
            }
            rows.add(rowData);
        }

        @SuppressWarnings("unchecked")
        Field<F> resultField = (Field<F>) point.getScalarRing();
        return org.jscience.mathematics.linearalgebra.matrices.MatrixFactory.create(rows, resultField);
    }

    /**
     * Returns the derivative of this function, which is the linear map
     * represented by the Jacobian.
     */
    @Override
    default Function<Vector<F>, Vector<F>> differentiate() {
        return new Function<Vector<F>, Vector<F>>() {
            @Override
            public Vector<F> evaluate(Vector<F> x) {
                // The derivative at x is a linear map L(h) = J(x) * h
                // But differentiate() usually returns f'(x).
                // In 1D, f'(x) is a number. In nD, it's a linear map (Matrix).
                // So this return type Function<Vector, Vector> is slightly ambiguous.
                // It usually means the function x -> f'(x), where f'(x) is the linear map.
                // But here we return a Function that evaluates the derivative?
                // No, differentiate() returns the derivative function.
                // For R->R, f' maps R->R.
                // For R^n->R^m, f' maps R^n -> L(R^n, R^m) (Matrix).
                // So the return type of differentiate() should ideally be Function<Vector<F>,
                // Matrix<F>>.
                // But DifferentiableFunction defines it as Function<D, C>.
                // This implies DifferentiableFunction assumes D=C or similar simple case?
                // Or maybe DifferentiableFunction<D, C> should return Function<D, LinearMap<D,
                // C>>?

                // Given the constraints of DifferentiableFunction<D, C> returning Function<D,
                // C>,
                // this interface might be too restrictive for Vector calculus if we strictly
                // follow types.
                // For now, we'll throw or return a placeholder.
                // The Jacobian method is the primary way to get derivative info here.
                throw new UnsupportedOperationException("Use jacobian(point) for vector functions");
            }
        };
    }
}
