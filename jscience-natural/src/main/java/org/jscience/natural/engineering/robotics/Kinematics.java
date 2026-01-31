/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.natural.engineering.robotics;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.quantity.Angle;
import org.jscience.core.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.core.measure.quantity.Length;

import java.util.ArrayList;
import java.util.List;

/**
 * Robotics kinematics calculations.
 * Modernized to use high-precision Real matrices and typed Quantities.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Kinematics {

    private Kinematics() {}

    /**
     * 2D forward kinematics for 2-link planar arm.
     * Returns the end-effector position (x, y).
     */
    public static Vector<Real> forwardKinematics2Link(
            Quantity<Length> L1, Quantity<Length> L2,
            Quantity<Angle> theta1, Quantity<Angle> theta2) {

        Real l1 = L1.to(Units.METER).getValue();
        Real l2 = L2.to(Units.METER).getValue();
        Real t1 = theta1.to(Units.RADIAN).getValue();
        Real t2 = theta2.to(Units.RADIAN).getValue();

        // Using Real trig functions
        Real x = l1.multiply(t1.cos()).add(l2.multiply(t1.add(t2).cos()));
        Real y = l1.multiply(t1.sin()).add(l2.multiply(t1.add(t2).sin()));

        return DenseVector.of(List.of(x, y), Reals.getInstance());
    }

    /**
     * Denavit-Hartenberg transformation matrix.
     * T = Rz(Î¸) * Tz(d) * Tx(a) * Rx(Î±)
     */
    public static Matrix<Real> dhMatrix(Quantity<Angle> theta, Quantity<Length> d, 
                                       Quantity<Length> a, Quantity<Angle> alpha) {
        Real t = theta.to(Units.RADIAN).getValue();
        Real l_d = d.to(Units.METER).getValue();
        Real l_a = a.to(Units.METER).getValue();
        Real al = alpha.to(Units.RADIAN).getValue();

        Real ct = t.cos();
        Real st = t.sin();
        Real ca = al.cos();
        Real sa = al.sin();

        List<List<Real>> rows = new ArrayList<>();
        rows.add(List.of(ct, st.negate().multiply(ca), st.multiply(sa), l_a.multiply(ct)));
        rows.add(List.of(st, ct.multiply(ca), ct.negate().multiply(sa), l_a.multiply(st)));
        rows.add(List.of(Real.ZERO, sa, ca, l_d));
        rows.add(List.of(Real.ZERO, Real.ZERO, Real.ZERO, Real.ONE));

        return DenseMatrix.of(rows, Reals.getInstance());
    }

    /**
     * Extracts position vector from a 4x4 transformation matrix.
     */
    public static Vector<Real> getPosition(Matrix<Real> T) {
        return DenseVector.of(List.of(T.get(0, 3), T.get(1, 3), T.get(2, 3)), Reals.getInstance());
    }

    /**
     * Jacobian for 2-link planar arm (velocity kinematics).
     */
    public static Matrix<Real> jacobian2Link(Quantity<Length> L1, Quantity<Length> L2, 
                                            Quantity<Angle> theta1, Quantity<Angle> theta2) {
        Real l1 = L1.to(Units.METER).getValue();
        Real l2 = L2.to(Units.METER).getValue();
        Real t1 = theta1.to(Units.RADIAN).getValue();
        Real t2 = theta2.to(Units.RADIAN).getValue();
        Real t12 = t1.add(t2);

        Real s1 = t1.sin();
        Real c1 = t1.cos();
        Real s12 = t12.sin();
        Real c12 = t12.cos();

        List<List<Real>> rows = new ArrayList<>();
        rows.add(List.of(l1.multiply(s1).negate().subtract(l2.multiply(s12)), l2.multiply(s12).negate()));
        rows.add(List.of(l1.multiply(c1).add(l2.multiply(c12)), l2.multiply(c12)));

        return DenseMatrix.of(rows, Reals.getInstance());
    }
}

