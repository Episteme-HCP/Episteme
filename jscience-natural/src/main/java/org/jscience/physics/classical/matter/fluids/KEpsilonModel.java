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

package org.jscience.physics.classical.matter.fluids;

/**
 * k-epsilon turbulence model for RANS simulations.
 *
 * Standard k-ÃŽÂµ model equations:
 * Dk/Dt = P_k - ÃŽÂµ + Ã¢Ë†â€¡Ã‚Â·((ÃŽÂ½ + ÃŽÂ½_t/ÃÆ’_k)Ã¢Ë†â€¡k)
 * DÃŽÂµ/Dt = C_ÃŽÂµ1 * ÃŽÂµ/k * P_k - C_ÃŽÂµ2 * ÃŽÂµÃ‚Â²/k + Ã¢Ë†â€¡Ã‚Â·((ÃŽÂ½ + ÃŽÂ½_t/ÃÆ’_ÃŽÂµ)Ã¢Ë†â€¡ÃŽÂµ)
 *
 * where ÃŽÂ½_t = C_ÃŽÂ¼ * kÃ‚Â²/ÃŽÂµ (turbulent viscosity)
 * * <p>
 * <b>Reference:</b><br>
 * Zeigler, B. P., Praehofer, H., & Kim, T. G. (2000). <i>Theory of Modeling and Simulation</i>. Academic Press.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class KEpsilonModel {

    // Standard k-ÃŽÂµ model constants
    public static final double C_MU = 0.09;
    public static final double C_EPS1 = 1.44;
    public static final double C_EPS2 = 1.92;
    public static final double SIGMA_K = 1.0;
    public static final double SIGMA_EPS = 1.3;

    private final int width;
    private final int height;
    private final double nu; // Molecular viscosity

    // Turbulence fields
    private double[][] k; // Turbulent kinetic energy
    private double[][] epsilon; // Dissipation rate
    private double[][] nuT; // Turbulent viscosity

    // Velocity gradients (should be provided by main solver)
    private double[][] dudx, dudy, dvdx, dvdy;

    public KEpsilonModel(int width, int height, double molecularViscosity) {
        this.width = width;
        this.height = height;
        this.nu = molecularViscosity;

        k = new double[width][height];
        epsilon = new double[width][height];
        nuT = new double[width][height];

        dudx = new double[width][height];
        dudy = new double[width][height];
        dvdx = new double[width][height];
        dvdy = new double[width][height];

        // Initialize with small values to avoid division by zero
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                k[x][y] = 1e-6;
                epsilon[x][y] = 1e-8;
            }
        }
    }

    /**
     * Sets inlet turbulence conditions.
     * 
     * @param inletK       Inlet turbulent kinetic energy
     * @param inletEpsilon Inlet dissipation rate
     */
    public void setInletConditions(double inletK, double inletEpsilon) {
        for (int y = 0; y < height; y++) {
            k[0][y] = inletK;
            epsilon[0][y] = inletEpsilon;
        }
    }

    /**
     * Estimates inlet k and ÃŽÂµ from turbulence intensity and length scale.
     * k = 1.5 * (U * I)Ã‚Â²
     * ÃŽÂµ = C_ÃŽÂ¼^(3/4) * k^(3/2) / L
     */
    public static double[] estimateInletConditions(double velocity, double turbulenceIntensity,
            double lengthScale) {
        double kIn = 1.5 * Math.pow(velocity * turbulenceIntensity, 2);
        double epsIn = Math.pow(C_MU, 0.75) * Math.pow(kIn, 1.5) / lengthScale;
        return new double[] { kIn, epsIn };
    }

    /**
     * Updates velocity gradients (call before each step).
     */
    public void setVelocityGradients(double[][] dudx, double[][] dudy,
            double[][] dvdx, double[][] dvdy) {
        this.dudx = dudx;
        this.dudy = dudy;
        this.dvdx = dvdx;
        this.dvdy = dvdy;
    }

    /**
     * Performs one time step of k-ÃŽÂµ model.
     * 
     * @param dt Time step
     */
    public void step(double dt) {
        // Compute turbulent viscosity
        computeTurbulentViscosity();

        // Compute production term
        double[][] Pk = computeProduction();

        // Update k and ÃŽÂµ (explicit Euler, simplified)
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                double localK = k[x][y];
                double localEps = epsilon[x][y];

                // Ensure numerical stability
                if (localK < 1e-10)
                    localK = 1e-10;
                if (localEps < 1e-12)
                    localEps = 1e-12;

                // k equation: dk/dt = P_k - ÃŽÂµ
                double dkdt = Pk[x][y] - localEps;

                // ÃŽÂµ equation: dÃŽÂµ/dt = C_ÃŽÂµ1 * ÃŽÂµ/k * P_k - C_ÃŽÂµ2 * ÃŽÂµÃ‚Â²/k
                double depsdt = C_EPS1 * localEps / localK * Pk[x][y]
                        - C_EPS2 * localEps * localEps / localK;

                // Update
                k[x][y] = Math.max(1e-10, localK + dt * dkdt);
                epsilon[x][y] = Math.max(1e-12, localEps + dt * depsdt);
            }
        }

        // Recompute turbulent viscosity
        computeTurbulentViscosity();
    }

    private void computeTurbulentViscosity() {
        // ÃŽÂ½_t = C_ÃŽÂ¼ * kÃ‚Â²/ÃŽÂµ
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double localK = Math.max(k[x][y], 1e-10);
                double localEps = Math.max(epsilon[x][y], 1e-12);
                nuT[x][y] = C_MU * localK * localK / localEps;
            }
        }
    }

    private double[][] computeProduction() {
        // P_k = ÃŽÂ½_t * SÃ‚Â² where SÃ‚Â² = 2*S_ij*S_ij
        double[][] Pk = new double[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Strain rate tensor components
                double s11 = dudx[x][y];
                double s22 = dvdy[x][y];
                double s12 = 0.5 * (dudy[x][y] + dvdx[x][y]);

                // SÃ‚Â² = 2*(s11Ã‚Â² + s22Ã‚Â² + 2*s12Ã‚Â²)
                double s2 = 2.0 * (s11 * s11 + s22 * s22 + 2 * s12 * s12);

                Pk[x][y] = nuT[x][y] * s2;
            }
        }

        return Pk;
    }

    /**
     * Returns effective viscosity (molecular + turbulent).
     */
    public double[][] getEffectiveViscosity() {
        double[][] nuEff = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                nuEff[x][y] = nu + nuT[x][y];
            }
        }
        return nuEff;
    }

    // Accessors
    public double[][] getK() {
        return k;
    }

    public double[][] getEpsilon() {
        return epsilon;
    }

    public double[][] getTurbulentViscosity() {
        return nuT;
    }
}


