package org.jscience.mathematics.linearalgebra.vectors;

import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.storage.*;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.sets.Reals;

/**
 * An optimized Vector implementation for Real numbers.
 * Uses primitive double arrays/buffers for storage to avoid boxing overhead and
 * enable SIMD (future).
 */
public class RealDoubleVector extends DenseVector<Real> {

    // Factory methods

    public static RealDoubleVector of(double[] elements) {
        return new RealDoubleVector(new HeapRealDoubleVectorStorage(elements));
    }

    public static RealDoubleVector direct(int dimension) {
        return new RealDoubleVector(new DirectRealDoubleVectorStorage(dimension));
    }

    // Constructors

    protected RealDoubleVector(RealDoubleVectorStorage storage) {
        super(storage, Reals.getInstance());
    }

    public RealDoubleVectorStorage getRealStorage() {
        return (RealDoubleVectorStorage) storage;
    }

    // Optimized Operations

    public double[] toDoubleArray() {
        return getRealStorage().toDoubleArray();
    }

    @Override
    public Vector<Real> add(Vector<Real> that) {
        if (that instanceof RealDoubleVector) {
            RealDoubleVector other = (RealDoubleVector) that;
            if (this.dimension() != other.dimension())
                throw new IllegalArgumentException("Dimension mismatch");

            double[] v1 = this.toDoubleArray();
            double[] v2 = other.toDoubleArray();
            double[] res = new double[dimension()];
            for (int i = 0; i < dimension(); i++) {
                res[i] = v1[i] + v2[i];
            }
            return new RealDoubleVector(new HeapRealDoubleVectorStorage(res));
        }
        return super.add(that);
    }

    @Override
    public Vector<Real> multiply(Real scalar) {
        double s = scalar.doubleValue();
        double[] v = toDoubleArray();
        double[] res = new double[dimension()];
        for (int i = 0; i < dimension(); i++) {
            res[i] = v[i] * s;
        }
        return new RealDoubleVector(new HeapRealDoubleVectorStorage(res));
    }

    @Override
    public String toString() {
        return "RealDoubleVector[" + dimension() + "]";
    }
}
