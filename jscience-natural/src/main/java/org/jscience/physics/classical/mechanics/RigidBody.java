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
package org.jscience.physics.classical.mechanics;

import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;

import org.jscience.mathematics.numbers.complex.Quaternion;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.linearalgebra.matrices.solvers.MatrixSolver;
import org.jscience.mathematics.sets.Reals;
import org.jscience.mathematics.geometry.collision.CollisionShape;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a rigid body in 6DOF space (3 translation + 3 rotation).
 * <p>
 * Uses Quaternions for orientation to avoid gimbal lock.
 * Integration is performed using symplectic Euler or RK4.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class RigidBody {

    // linear state
    private Vector<Real> position;
    private Vector<Real> velocity;
    private Vector<Real> force;
    private Real mass;
    private Real inverseMass;

    // angular state
    private Quaternion orientation;
    private Vector<Real> angularVelocity;
    private Vector<Real> torque;
    private DenseMatrix<Real> inertiaTensor;
    private DenseMatrix<Real> inverseInertiaTensor;

    // Damping
    private Real linearDamping = Real.of(0.99);
    private Real angularDamping = Real.of(0.99);

    private CollisionShape collisionShape;

    public RigidBody(Vector<Real> position, Real mass, DenseMatrix<Real> inertiaTensor, CollisionShape shape) {
        this.position = position;
        this.mass = mass;
        this.inverseMass = mass.isZero() ? Real.ZERO : Real.ONE.divide(mass);
        this.collisionShape = shape;

        this.inertiaTensor = inertiaTensor;
        // Invert inertia tensor (assuming diagonal/symmetric positive definite for now)
        // Ideally use MatrixSolver.
        this.inverseInertiaTensor = invertInertia(inertiaTensor); // Placeholder

        this.orientation = Quaternion.ONE; // Identity rotation
        this.velocity = createZeroVector();
        this.angularVelocity = createZeroVector();
        this.force = createZeroVector();
        this.torque = createZeroVector();
    }

    private Vector<Real> createZeroVector() {
        return DenseVector.of(Collections.nCopies(3, Real.ZERO), Reals.getInstance());
    }

    public CollisionShape getCollisionShape() {
        return collisionShape;
    }

    public void setCollisionShape(CollisionShape shape) {
        this.collisionShape = shape;
    }

    // Helper to rotate a vector by a quaternion: v' = q * v * q^-1
    public static Vector<Real> rotate(Vector<Real> v, Quaternion q) {
        // q * v
        // v as pure quaternion (0, x, y, z)
        Real vx = v.get(0);
        Real vy = v.get(1);
        Real vz = v.get(2);

        Real qw = q.getReal();
        Real qx = q.getI();
        Real qy = q.getJ();
        Real qz = q.getK();

        // t = 2 * cross(q_xyz, v)
        // t_x = 2 * (qy*vz - qz*vy)
        Real two = Real.TWO;
        Real tx = two.multiply(qy.multiply(vz).subtract(qz.multiply(vy)));
        Real ty = two.multiply(qz.multiply(vx).subtract(qx.multiply(vz)));
        Real tz = two.multiply(qx.multiply(vy).subtract(qy.multiply(vx)));

        // v' = v + q_w * t + cross(q_xyz, t)
        // cross(q_xyz, t)_x = qy*tz - qz*ty
        Real crossX = qy.multiply(tz).subtract(qz.multiply(ty));
        Real crossY = qz.multiply(tx).subtract(qx.multiply(tz));
        Real crossZ = qx.multiply(ty).subtract(qy.multiply(tx));

        Real resX = vx.add(qw.multiply(tx)).add(crossX);
        Real resY = vy.add(qw.multiply(ty)).add(crossY);
        Real resZ = vz.add(qw.multiply(tz)).add(crossZ);

        return DenseVector.of(Arrays.asList(resX, resY, resZ), Reals.getInstance());
    }

    // Placeholder helper
    private DenseMatrix<Real> invertInertia(DenseMatrix<Real> I) {
        int n = I.rows();

        // Assuming square

        // Solve I * X = Identity to find Inverse
        Real[][] inverseData = new Real[n][n];

        for (int j = 0; j < n; j++) {
            // Create column vector e_j (Identity column)
            Real[] b = new Real[n];
            for (int i = 0; i < n; i++)
                b[i] = (i == j) ? Real.ZERO.add(Real.ONE) : Real.ZERO;

            // Solve I * x = b
            Real[] x = MatrixSolver.solve(I, b);

            // x is the j-th column of Inverse
            for (int i = 0; i < n; i++)
                inverseData[i][j] = x[i];
        }

        List<java.util.List<Real>> rows = new java.util.ArrayList<>();
        for (int i = 0; i < n; i++) {
            rows.add(Arrays.asList(inverseData[i]));
        }

        return new DenseMatrix<>(rows, Reals.getInstance());
    }

    // Helper for matrix-vector multiplication: M * v
    private Vector<Real> multiplyMatrixVector(DenseMatrix<Real> mat, Vector<Real> vec) {
        int n = mat.rows();
        Real[] result = new Real[n];
        for (int i = 0; i < n; i++) {
            result[i] = Real.ZERO;
            for (int j = 0; j < vec.dimension(); j++) {
                result[i] = result[i].add(mat.get(i, j).multiply(vec.get(j)));
            }
        }
        return DenseVector.of(Arrays.asList(result), Reals.getInstance());
    }

    public void integrate(Real dt) {
        if (mass.isZero())
            return; // Static body
        // Real dts = dt;

        // 1. Integrate linear velocity
        // v += (F / m) * dt
        Vector<Real> acceleration = force.multiply(inverseMass);
        velocity = velocity.add(acceleration.multiply(dt));
        velocity = velocity.multiply(linearDamping);

        // 2. Integrate position
        // x += v * dt
        position = position.add(velocity.multiply(dt));

        // 3. Integrate angular velocity
        // w += I_world^-1 * (T - w x (I_world * w)) * dt (Euler equations)

        // Simplified: w += T * dt (Assuming unit inertia for now to avoid crashing with
        // matrix operations not yet setup)
        // Real logic: angularVelocity += inverseInertiaWorld * torque * dt

        // Angular acceleration: α = I^-1 * T (simplified Euler equation)
        Vector<Real> angularAccel = multiplyMatrixVector(inverseInertiaTensor, torque);
        angularVelocity = angularVelocity.add(angularAccel.multiply(dt));
        angularVelocity = angularVelocity.multiply(angularDamping);

        // 4. Integrate orientation
        // dq/dt = 0.5 * w * q
        Real wx = angularVelocity.get(0);
        Real wy = angularVelocity.get(1);
        Real wz = angularVelocity.get(2);

        Quaternion wq = new Quaternion(Real.ZERO, wx, wy, wz);
        Quaternion dq = wq.multiply(orientation).multiply(dt.multiply(Real.of(0.5)));

        orientation = orientation.add(dq);

        // Normalize orientation
        Real normSq = orientation.normSquared();
        // if (Math.abs(normSq - 1.0) > 1e-6)
        // Real.ONE.subtract(normSq).abs().compareTo ...
        // Simplify: always normalize to avoid drift
        orientation = orientation.multiply(normSq.sqrt().inverse());

        // Clear accumulators
        force = createZeroVector();
        torque = createZeroVector();
    }

    public void applyForce(Vector<Real> f, Vector<Real> p) {
        force = force.add(f);
        // Torque += (p - position) x F
        Vector<Real> r = p.subtract(position);
        torque = torque.add(Kinematics.crossProduct(r, f));
    }

    public void applyTorque(Vector<Real> t) {
        torque = torque.add(t);
    }

    public Vector<Real> getPosition() {
        return position;
    }

    public Quaternion getOrientation() {
        return orientation;
    }

    public Vector<Real> getVelocity() {
        return velocity;
    }

    public Vector<Real> getAngularVelocity() {
        return angularVelocity;
    }

    public DenseMatrix<Real> getInertiaTensor() {
        return inertiaTensor;
    }

    public DenseMatrix<Real> getInverseInertiaTensor() {
        return inverseInertiaTensor;
    }
}
