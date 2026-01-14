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

package org.jscience.mathematics.statistics.timeseries;

/**
 * Kalman filter for state estimation in linear dynamic systems.
 *
 * State-space model:
 * x_k = F * x_{k-1} + B * u_k + w_k (state transition)
 * z_k = H * x_k + v_k (measurement)
 *
 * where w_k ~ N(0, Q) and v_k ~ N(0, R)
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class KalmanFilter {

    private final int stateSize;
    private final int measurementSize;

    // State estimate
    private double[] x; // State vector
    private double[][] P; // State covariance

    // System matrices
    private double[][] F; // State transition matrix
    private double[][] H; // Measurement matrix
    private double[][] Q; // Process noise covariance
    private double[][] R; // Measurement noise covariance
    private double[][] B; // Control input matrix (optional)

    /**
     * Creates a Kalman filter.
     * 
     * @param stateSize       Dimension of state vector
     * @param measurementSize Dimension of measurement vector
     */
    public KalmanFilter(int stateSize, int measurementSize) {
        this.stateSize = stateSize;
        this.measurementSize = measurementSize;

        x = new double[stateSize];
        P = identity(stateSize);
        F = identity(stateSize);
        H = new double[measurementSize][stateSize];
        Q = identity(stateSize);
        R = identity(measurementSize);
        B = null;

        // Default H: observe first measurementSize states
        for (int i = 0; i < measurementSize && i < stateSize; i++) {
            H[i][i] = 1.0;
        }
    }

    /**
     * Sets the state transition matrix F.
     */
    public void setTransitionMatrix(double[][] F) {
        this.F = deepCopy(F);
    }

    /**
     * Sets the measurement matrix H.
     */
    public void setMeasurementMatrix(double[][] H) {
        this.H = deepCopy(H);
    }

    /**
     * Sets the process noise covariance Q.
     */
    public void setProcessNoise(double[][] Q) {
        this.Q = deepCopy(Q);
    }

    /**
     * Sets the measurement noise covariance R.
     */
    public void setMeasurementNoise(double[][] R) {
        this.R = deepCopy(R);
    }

    /**
     * Sets the control input matrix B.
     */
    public void setControlMatrix(double[][] B) {
        this.B = deepCopy(B);
    }

    /**
     * Initializes the state estimate.
     */
    public void setState(double[] initialState, double[][] initialCovariance) {
        this.x = initialState.clone();
        this.P = deepCopy(initialCovariance);
    }

    /**
     * Prediction step (time update).
     * x_k|k-1 = F * x_{k-1} + B * u
     * P_k|k-1 = F * P_{k-1} * F^T + Q
     * 
     * @param controlInput Optional control input (can be null)
     */
    public void predict(double[] controlInput) {
        // State prediction
        double[] xPred = matVecMult(F, x);
        if (B != null && controlInput != null) {
            double[] Bu = matVecMult(B, controlInput);
            for (int i = 0; i < stateSize; i++) {
                xPred[i] += Bu[i];
            }
        }
        x = xPred;

        // Covariance prediction: P = F*P*F' + Q
        double[][] FP = matMult(F, P);
        double[][] FPFT = matMultTranspose(FP, F);
        P = matAdd(FPFT, Q);
    }

    /**
     * Update step (measurement update).
     * y = z - H * x (innovation)
     * S = H * P * H^T + R (innovation covariance)
     * K = P * H^T * S^-1 (Kalman gain)
     * x = x + K * y
     * P = (I - K*H) * P
     * 
     * @param measurement Measurement vector z
     */
    public void update(double[] measurement) {
        // Innovation (residual)
        double[] Hx = matVecMult(H, x);
        double[] y = new double[measurementSize];
        for (int i = 0; i < measurementSize; i++) {
            y[i] = measurement[i] - Hx[i];
        }

        // Innovation covariance: S = H*P*H' + R
        double[][] HP = matMult(H, P);
        double[][] HPHT = matMultTranspose(HP, H);
        double[][] S = matAdd(HPHT, R);

        // Kalman gain: K = P*H'*S^-1
        double[][] PHT = matMultTranspose(P, H);
        double[][] SInv = invert(S);
        double[][] K = matMult(PHT, SInv);

        // State update: x = x + K*y
        double[] Ky = matVecMult(K, y);
        for (int i = 0; i < stateSize; i++) {
            x[i] += Ky[i];
        }

        // Covariance update: P = (I - K*H)*P
        double[][] KH = matMult(K, H);
        double[][] I_KH = matSubtract(identity(stateSize), KH);
        P = matMult(I_KH, P);
    }

    /**
     * Returns current state estimate.
     */
    public double[] getState() {
        return x.clone();
    }

    /**
     * Returns current state covariance.
     */
    public double[][] getCovariance() {
        return deepCopy(P);
    }

    // --- Matrix utility methods ---

    private double[][] identity(int n) {
        double[][] I = new double[n][n];
        for (int i = 0; i < n; i++)
            I[i][i] = 1.0;
        return I;
    }

    private double[][] deepCopy(double[][] m) {
        double[][] copy = new double[m.length][];
        for (int i = 0; i < m.length; i++) {
            copy[i] = m[i].clone();
        }
        return copy;
    }

    private double[] matVecMult(double[][] A, double[] v) {
        double[] result = new double[A.length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < v.length; j++) {
                result[i] += A[i][j] * v[j];
            }
        }
        return result;
    }

    private double[][] matMult(double[][] A, double[][] B) {
        int m = A.length, n = B[0].length, p = B.length;
        double[][] C = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < p; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return C;
    }

    private double[][] matMultTranspose(double[][] A, double[][] B) {
        // A * B^T
        int m = A.length, n = B.length, p = B[0].length;
        double[][] C = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < p; k++) {
                    C[i][j] += A[i][k] * B[j][k];
                }
            }
        }
        return C;
    }

    private double[][] matAdd(double[][] A, double[][] B) {
        int m = A.length, n = A[0].length;
        double[][] C = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] + B[i][j];
            }
        }
        return C;
    }

    private double[][] matSubtract(double[][] A, double[][] B) {
        int m = A.length, n = A[0].length;
        double[][] C = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] - B[i][j];
            }
        }
        return C;
    }

    private double[][] invert(double[][] A) {
        // Simple 2x2 or use LU for larger
        int n = A.length;
        if (n == 1) {
            return new double[][] { { 1.0 / A[0][0] } };
        }
        if (n == 2) {
            double det = A[0][0] * A[1][1] - A[0][1] * A[1][0];
            return new double[][] {
                    { A[1][1] / det, -A[0][1] / det },
                    { -A[1][0] / det, A[0][0] / det }
            };
        }
        // For larger matrices, use Gauss-Jordan (simplified)
        double[][] aug = new double[n][2 * n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, aug[i], 0, n);
            aug[i][n + i] = 1.0;
        }
        for (int i = 0; i < n; i++) {
            double pivot = aug[i][i];
            for (int j = 0; j < 2 * n; j++)
                aug[i][j] /= pivot;
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = aug[k][i];
                    for (int j = 0; j < 2 * n; j++) {
                        aug[k][j] -= factor * aug[i][j];
                    }
                }
            }
        }
        double[][] inv = new double[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(aug[i], n, inv[i], 0, n);
        }
        return inv;
    }
}


