/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.episteme.core.mathematics.linearalgebra.vectors;

import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.vectors.storage.*;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;

/**
 * An optimized Vector implementation for Single-Precision Real numbers.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class RealFloatVector extends DenseVector<Real> implements AutoCloseable {

    public static RealFloatVector of(float[] elements) {
        return new RealFloatVector(new HeapRealFloatVectorStorage(elements));
    }

    public static RealFloatVector of(RealFloatVectorStorage storage) {
        return new RealFloatVector(storage);
    }

    protected RealFloatVector(RealFloatVectorStorage storage) {
        super(storage, Reals.getInstance());
    }

    public RealFloatVectorStorage getRealStorage() {
        return (RealFloatVectorStorage) storage;
    }

    public java.nio.FloatBuffer getBuffer() {
        return getRealStorage().getBuffer();
    }

    @Override
    public void close() {
        getRealStorage().close();
    }

    public float[] toFloatArray() {
        return getRealStorage().toFloatArray();
    }

    @Override
    public Vector<Real> add(Vector<Real> that) {
        if (that instanceof RealFloatVector) {
            RealFloatVector other = (RealFloatVector) that;
            if (this.dimension() != other.dimension())
                throw new IllegalArgumentException("Dimension mismatch");

            float[] v1 = this.toFloatArray();
            float[] v2 = other.toFloatArray();
            float[] res = new float[dimension()];
            for (int i = 0; i < dimension(); i++) {
                res[i] = v1[i] + v2[i];
            }
            return new RealFloatVector(new HeapRealFloatVectorStorage(res));
        }
        return super.add(that);
    }

    @Override
    public Vector<Real> multiply(Real scalar) {
        float s = scalar.floatValue();
        float[] v = toFloatArray();
        float[] res = new float[dimension()];
        for (int i = 0; i < dimension(); i++) {
            res[i] = v[i] * s;
        }
        return new RealFloatVector(new HeapRealFloatVectorStorage(res));
    }

    @Override
    public String toString() {
        return "RealFloatVector[" + dimension() + "]";
    }
}
