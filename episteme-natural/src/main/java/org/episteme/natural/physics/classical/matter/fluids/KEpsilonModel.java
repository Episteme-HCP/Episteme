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

package org.episteme.natural.physics.classical.matter.fluids;

/**
 * k-epsilon turbulence model for RANS simulations.
 *
 * Standard k-ÃƒÅ½Ã‚Âµ model equations:
 * Dk/Dt = P_k - ÃƒÅ½Ã‚Âµ + ÃƒÂ¢Ã‹â€ Ã¢â‚¬Â¡Ãƒâ€šÃ‚Â·((ÃƒÅ½Ã‚Â½ + ÃƒÅ½Ã‚Â½_t/ÃƒÂÃ†â€™_k)ÃƒÂ¢Ã‹â€ Ã¢â‚¬Â¡k)
 * DÃƒÅ½Ã‚Âµ/Dt = C_ÃƒÅ½Ã‚Âµ1 * ÃƒÅ½Ã‚Âµ/k * P_k - C_ÃƒÅ½Ã‚Âµ2 * ÃƒÅ½Ã‚ÂµÃƒâ€šÃ‚Â²/k + ÃƒÂ¢Ã‹â€ Ã¢â‚¬Â¡Ãƒâ€šÃ‚Â·((ÃƒÅ½Ã‚Â½ + ÃƒÅ½Ã‚Â½_t/ÃƒÂÃ†â€™_ÃƒÅ½Ã‚Âµ)ÃƒÂ¢Ã‹â€ Ã¢â‚¬Â¡ÃƒÅ½Ã‚Âµ)
 *
 * where ÃƒÅ½Ã‚Â½_t = C_ÃƒÅ½Ã‚Â¼ * kÃƒâ€šÃ‚Â²/ÃƒÅ½Ã‚Âµ (turbulent viscosity)
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

    // Standard k-ÃƒÅ½Ã‚Âµ model constants
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
     * Estimates inlet k and ÃƒÅ½Ã‚Âµ from turbulence intensity and length scale.
     * k = 1.5 * (U * I)Ãƒâ€šÃ‚Â²
     * ÃƒÅ½Ã‚Âµ = C_ÃƒÅ½Ã‚Â¼^(3/4) * k^(3/2) / L
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
     * Performs one time step of k-ÃƒÅ½Ã‚Âµ model.
     * 
     * @param dt Time step
     */
    public void step(double dt) {
        // Compute turbulent viscosity
        computeTurbulentViscosity();

        // Compute production term
        double[][] Pk = computeProduction();

        // Update k and ÃƒÅ½Ã‚Âµ (explicit Euler, simplified)
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                double localK = k[x][y];
                double localEps = epsilon[x][y];

                // Ensure numerical stability
                if (localK < 1e-10)
                    localK = 1e-10;
                if (localEps < 1e-12)
                    localEps = 1e-12;

                // k equation: dk/dt = P_k - ÃƒÅ½Ã‚Âµ
                double dkdt = Pk[x][y] - localEps;

                // ÃƒÅ½Ã‚Âµ equation: dÃƒÅ½Ã‚Âµ/dt = C_ÃƒÅ½Ã‚Âµ1 * ÃƒÅ½Ã‚Âµ/k * P_k - C_ÃƒÅ½Ã‚Âµ2 * ÃƒÅ½Ã‚ÂµÃƒâ€šÃ‚Â²/k
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
        // ÃƒÅ½Ã‚Â½_t = C_ÃƒÅ½Ã‚Â¼ * kÃƒâ€šÃ‚Â²/ÃƒÅ½Ã‚Âµ
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double localK = Math.max(k[x][y], 1e-10);
                double localEps = Math.max(epsilon[x][y], 1e-12);
                nuT[x][y] = C_MU * localK * localK / localEps;
            }
        }
    }

    private double[][] computeProduction() {
        // P_k = ÃƒÅ½Ã‚Â½_t * SÃƒâ€šÃ‚Â² where SÃƒâ€šÃ‚Â² = 2*S_ij*S_ij
        double[][] Pk = new double[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Strain rate tensor components
                double s11 = dudx[x][y];
                double s22 = dvdy[x][y];
                double s12 = 0.5 * (dudy[x][y] + dvdx[x][y]);

                // SÃƒâ€šÃ‚Â² = 2*(s11Ãƒâ€šÃ‚Â² + s22Ãƒâ€šÃ‚Â² + 2*s12Ãƒâ€šÃ‚Â²)
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



