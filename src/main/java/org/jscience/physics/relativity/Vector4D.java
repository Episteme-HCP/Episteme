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
 * @author Gemini AI
 * @since 3.2
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
