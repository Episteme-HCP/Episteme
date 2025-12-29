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

package org.jscience.physics.relativity;

import org.jscience.mathematics.geometry.Vector3D;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.mathematics.linearalgebra.tensors.DenseTensor;

/**
 * Represents a 4-vector in spacetime (ct, x, y, z).
 * <p>
 * In relativity, events are described by 4 coordinates.
 * The 0-th component is time (scaled by c), and 1-3 are spatial.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Vector4D {

    private final Tensor<Real> tensor;

    public Vector4D(Real ct, Real x, Real y, Real z) {
        Real[] data = new Real[] { ct, x, y, z };
        this.tensor = new DenseTensor<>(data, 4);
    }

    public Vector4D(Real ct, Vector3D spatial) {
        this(ct, spatial.x(), spatial.y(), spatial.z());
    }

    public Real ct() {
        return tensor.get(0);
    }

    public Real x() {
        return tensor.get(1);
    }

    public Real y() {
        return tensor.get(2);
    }

    public Real z() {
        return tensor.get(3);
    }

    public Tensor<Real> asTensor() {
        return tensor;
    }

    public Vector3D spatial() {
        return new Vector3D(x(), y(), z());
    }

    // Dot product with metric signature (+, -, -, -) or (-, +, +, +)
    // defined by the metric tensor.
    // This class just holds components.
}
