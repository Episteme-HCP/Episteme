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

package org.jscience.physics.classical.waves.electromagnetism.field;

import org.jscience.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.mathematics.linearalgebra.tensors.DenseTensor;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.geometry.Vector3D;

/**
 * Represents the electromagnetic field tensor $F_{\mu\nu}$ in relativistic
 * physics.
 * <p>
 * This rank-2 antisymmetric tensor encapsulates both the electric field
 * $\mathbf{E}$
 * and the magnetic field $\mathbf{B}$ into a single geometric object.
 * </p>
 * <p>
 * Components (in Cartesian coordinates with $c=1$):
 * $$
 * F_{\mu\nu} = \begin{pmatrix}
 * 0 & -E_x & -E_y & -E_z \\
 * E_x & 0 & B_z & -B_y \\
 * E_y & -B_z & 0 & B_x \\
 * E_z & B_y & -B_x & 0
 * \end{pmatrix}
 * $$
 * Note: Signs might vary depending on metric signature $(-, +, +, +)$ vs $(+,
 * -, -, -)$.
 * Here we assume $(-, +, +, +)$ signature standard in GR, but $F_{\mu\nu}$
 * definition
 * is often consistent.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ElectromagneticTensor {

    private final Tensor<Real> tensor;

    /**
     * Creates an EM tensor from explicit components.
     * 
     * @param tensor rank-2 tensor of shape [4, 4]
     */
    public ElectromagneticTensor(Tensor<Real> tensor) {
        if (tensor.rank() != 2 || tensor.shape()[0] != 4 || tensor.shape()[1] != 4) {
            throw new IllegalArgumentException("Electromagnetic tensor must be 4x4");
        }
        this.tensor = tensor;
    }

    /**
     * Creates an EM tensor from Electric and Magnetic fields vectors.
     * <p>
     * Assumes a local inertial frame where these vectors are measured.
     * </p>
     * 
     * @param E Electric field vector
     * @param B Magnetic field vector
     * @return ElectromagneticTensor instance
     */
    public static ElectromagneticTensor fromVectors(Vector3D E, Vector3D B) {
        Real zero = Real.ZERO;
        Real Ex = E.x();
        Real Ey = E.y();
        Real Ez = E.z();
        Real Bx = B.x();
        Real By = B.y();
        Real Bz = B.z();

        // F_0i = -E_i/c (using c=1 -> -E_i)
        // F_ij = epsilon_ijk B_k

        Real[] data = new Real[] {
                zero, Ex.negate(), Ey.negate(), Ez.negate(),
                Ex, zero, Bz, By.negate(),
                Ey, Bz.negate(), zero, Bx,
                Ez, By, Bx.negate(), zero
        };

        return new ElectromagneticTensor(new DenseTensor<>(data, 4, 4));
    }

    /**
     * Returns the raw tensor object.
     * 
     * @return backing Tensor<Real>
     */
    public Tensor<Real> getTensor() {
        return tensor;
    }

    /**
     * Extracts the electric field 4-vector seen by an observer with 4-velocity
     * $u^\mu$.
     * <p>
     * $E^\mu = F^{\mu\nu} u_\nu$
     * </p>
     * 
     * @param uObserver 4-velocity of the observer
     * @return Electric field 4-vector
     */
    public DenseTensor<Real> extractElectricField(Tensor<Real> uObserver) {
        // u is rank 1, tensor is rank 2.
        // explicit contraction
        Real[] Edata = new Real[4];
        Real zero = Real.ZERO;

        for (int mu = 0; mu < 4; mu++) {
            Edata[mu] = zero;
            for (int nu = 0; nu < 4; nu++) {
                // F^mu_nu * u_nu
                // Assuming metric signature -+++ implies F_mu_nu indices are covariant?
                // The provided definition in class doc is F_mu_nu.
                // To get E^mu, we need F^mu^nu usually?
                // Or simply contraction $E_\mu = F_{\mu\nu} u^\nu$?
                // Standard definition: $E_\mu = F_{\mu\nu} u^\nu$.
                // Let's implement that.

                Real F_munu = tensor.get(mu, nu);
                Real u_nu = uObserver.get(nu);
                Edata[mu] = Edata[mu].add(F_munu.multiply(u_nu));
            }
        }
        return new DenseTensor<>(Edata, 4);
    }
}


