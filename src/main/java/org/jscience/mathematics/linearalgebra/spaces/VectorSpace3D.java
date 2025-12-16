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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.mathematics.linearalgebra.spaces;

import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.numbers.real.Real;

import org.jscience.mathematics.structures.spaces.VectorSpace;
import java.util.Arrays;

/**
 * 3D Vector Space.
 * <p>
 * Optimized for 3D geometry and physics.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VectorSpace3D implements VectorSpace<Vector<Real>, Real> {

    private static final VectorSpace3D INSTANCE = new VectorSpace3D();

    public static VectorSpace3D getInstance() {
        return INSTANCE;
    }

    private VectorSpace3D() {
    }

    @Override
    public Vector<Real> operate(Vector<Real> left, Vector<Real> right) {
        return left.add(right);
    }

    @Override
    public Vector<Real> add(Vector<Real> a, Vector<Real> b) {
        return a.add(b);
    }

    @Override
    public Vector<Real> zero() {
        return new DenseVector<>(Arrays.asList(Real.ZERO, Real.ZERO, Real.ZERO), Real.ZERO);
    }

    @Override
    public Vector<Real> negate(Vector<Real> element) {
        return element.negate();
    }

    @Override
    public Vector<Real> inverse(Vector<Real> element) {
        return negate(element);
    }

    @Override
    public Vector<Real> scale(Real scalar, Vector<Real> vector) {
        return vector.multiply(scalar);
    }

    @Override
    public boolean isCommutative() {
        return true;
    }

    @Override
    public String description() {
        return "R^3 (3D Euclidean Space)";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Vector<Real> element) {
        return element != null && element.dimension() == 3;
    }

    @Override
    public org.jscience.mathematics.structures.rings.Field<Real> getScalarField() {
        return Real.ZERO;
    }

    @Override
    public org.jscience.mathematics.structures.rings.Ring<Real> getScalarRing() {
        return Real.ZERO;
    }

    @Override
    public int dimension() {
        return 3;
    }
}
