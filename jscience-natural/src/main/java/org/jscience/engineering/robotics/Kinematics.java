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
package org.jscience.engineering.robotics;

/**
 * Robotics kinematics calculations.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Kinematics {

    /**
     * 2D forward kinematics for 2-link planar arm.
     * 
     * @param L1     Length of link 1
     * @param L2     Length of link 2
     * @param theta1 Angle of joint 1 (radians)
     * @param theta2 Angle of joint 2 (radians, relative to link 1)
     * @return [x, y] position of end effector
     */
    public static double[] forwardKinematics2Link(double L1, double L2,
            double theta1, double theta2) {
        double x = L1 * Math.cos(theta1) + L2 * Math.cos(theta1 + theta2);
        double y = L1 * Math.sin(theta1) + L2 * Math.sin(theta1 + theta2);
        return new double[] { x, y };
    }

    /**
     * 2D inverse kinematics for 2-link planar arm.
     * 
     * @param L1 Length of link 1
     * @param L2 Length of link 2
     * @param x  Target x position
     * @param y  Target y position
     * @return [theta1, theta2] joint angles (radians), or null if unreachable
     */
    public static double[] inverseKinematics2Link(double L1, double L2, double x, double y) {
        double d = Math.sqrt(x * x + y * y);

        // Check reachability
        if (d > L1 + L2 || d < Math.abs(L1 - L2)) {
            return null;
        }

        // Law of cosines for theta2
        double cosTheta2 = (x * x + y * y - L1 * L1 - L2 * L2) / (2 * L1 * L2);
        double theta2 = Math.acos(cosTheta2); // Elbow up solution

        // Theta1 from geometry
        double k1 = L1 + L2 * Math.cos(theta2);
        double k2 = L2 * Math.sin(theta2);
        double theta1 = Math.atan2(y, x) - Math.atan2(k2, k1);

        return new double[] { theta1, theta2 };
    }

    /**
     * Denavit-Hartenberg transformation matrix.
     * T = Rz(θ) * Tz(d) * Tx(a) * Rx(α)
     * 
     * @param theta Joint angle (radians)
     * @param d     Link offset
     * @param a     Link length
     * @param alpha Link twist (radians)
     * @return 4x4 transformation matrix (row-major)
     */
    public static double[][] dhMatrix(double theta, double d, double a, double alpha) {
        double ct = Math.cos(theta);
        double st = Math.sin(theta);
        double ca = Math.cos(alpha);
        double sa = Math.sin(alpha);

        return new double[][] {
                { ct, -st * ca, st * sa, a * ct },
                { st, ct * ca, -ct * sa, a * st },
                { 0, sa, ca, d },
                { 0, 0, 0, 1 }
        };
    }

    /**
     * Multiplies two 4x4 matrices.
     */
    public static double[][] multiplyMatrices(double[][] A, double[][] B) {
        double[][] C = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return C;
    }

    /**
     * Extracts position from transformation matrix.
     */
    public static double[] getPosition(double[][] T) {
        return new double[] { T[0][3], T[1][3], T[2][3] };
    }

    /**
     * Jacobian for 2-link planar arm (velocity kinematics).
     * J = [dx/dθ1, dx/dθ2; dy/dθ1, dy/dθ2]
     */
    public static double[][] jacobian2Link(double L1, double L2, double theta1, double theta2) {
        double s1 = Math.sin(theta1);
        double c1 = Math.cos(theta1);
        double s12 = Math.sin(theta1 + theta2);
        double c12 = Math.cos(theta1 + theta2);

        return new double[][] {
                { -L1 * s1 - L2 * s12, -L2 * s12 },
                { L1 * c1 + L2 * c12, L2 * c12 }
        };
    }

    /**
     * Trapezoidal velocity profile for trajectory planning.
     * Returns position at time t.
     */
    public static double trapezoidalProfile(double t, double totalTime,
            double accelTime, double startPos, double endPos) {
        double distance = endPos - startPos;
        double vMax = distance / (totalTime - accelTime);

        if (t <= accelTime) {
            // Acceleration phase
            double a = vMax / accelTime;
            return startPos + 0.5 * a * t * t;
        } else if (t <= totalTime - accelTime) {
            // Constant velocity phase
            return startPos + 0.5 * vMax * accelTime + vMax * (t - accelTime);
        } else if (t <= totalTime) {
            // Deceleration phase
            double tDecel = t - (totalTime - accelTime);
            double posAtDecelStart = startPos + distance - 0.5 * vMax * accelTime;
            double a = vMax / accelTime;
            return posAtDecelStart + vMax * tDecel - 0.5 * a * tDecel * tDecel;
        }
        return endPos;
    }

    /**
     * Cubic polynomial trajectory.
     * θ(t) = a0 + a1*t + a2*t² + a3*t³
     * With zero velocity at start and end.
     */
    public static double cubicTrajectory(double t, double totalTime,
            double startPos, double endPos) {
        double T = totalTime;
        double s = t / T; // Normalized time [0, 1]
        double h = endPos - startPos;

        // Coefficients for zero velocity at endpoints
        double a0 = startPos;
        double a1 = 0;
        double a2 = 3 * h;
        double a3 = -2 * h;

        return a0 + a1 * s + a2 * s * s + a3 * s * s * s;
    }
}
