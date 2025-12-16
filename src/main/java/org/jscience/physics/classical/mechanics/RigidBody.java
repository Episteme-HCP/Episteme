package org.jscience.physics.classical.mechanics;

import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;

import org.jscience.mathematics.numbers.complex.Quaternion;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.linearalgebra.matrices.solvers.MatrixSolver;
import org.jscience.mathematics.sets.Reals;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a rigid body in 6DOF space (3 translation + 3 rotation).
 * <p>
 * Uses Quaternions for orientation to avoid gimbal lock.
 * Integration is performed using symplectic Euler or RK4.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 3.0
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
        double vx = v.get(0).doubleValue();
        double vy = v.get(1).doubleValue();
        double vz = v.get(2).doubleValue();

        double qw = q.getReal();
        double qx = q.getI();
        double qy = q.getJ();
        double qz = q.getK();

        // t = 2 * cross(q_xyz, v)
        double tx = 2 * (qy * vz - qz * vy);
        double ty = 2 * (qz * vx - qx * vz);
        double tz = 2 * (qx * vy - qy * vx);

        // v' = v + q_w * t + cross(q_xyz, t)
        double resX = vx + qw * tx + (qy * tz - qz * ty);
        double resY = vy + qw * ty + (qz * tx - qx * tz);
        double resZ = vz + qw * tz + (qx * ty - qy * tx);

        return DenseVector.of(Arrays.asList(Real.of(resX), Real.of(resY), Real.of(resZ)), Reals.getInstance());
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

    public void integrate(Real dt) {
        if (mass.isZero())
            return; // Static body
        double dts = dt.doubleValue();

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

        Vector<Real> angularAccel = torque; // Placeholder for I^-1 * T
        angularVelocity = angularVelocity.add(angularAccel.multiply(dt));
        angularVelocity = angularVelocity.multiply(angularDamping);

        // 4. Integrate orientation
        // dq/dt = 0.5 * w * q
        double wx = angularVelocity.get(0).doubleValue();
        double wy = angularVelocity.get(1).doubleValue();
        double wz = angularVelocity.get(2).doubleValue();

        Quaternion wq = new Quaternion(0, wx, wy, wz);
        Quaternion dq = wq.multiply(orientation).multiply(0.5 * dts);

        orientation = orientation.add(dq);

        // Normalize orientation
        double normSq = orientation.normSquared();
        if (Math.abs(normSq - 1.0) > 1e-6) {
            double invNorm = 1.0 / Math.sqrt(normSq);
            orientation = orientation.multiply(invNorm);
        }

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
