/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * 2D Micromagnetic simulation supporting skyrmions and domain patterns.
 * <p>
 * Extends 1D model to a 2D grid with:
 * <ul>
 *   <li>Exchange interaction</li>
 *   <li>Dzyaloshinskii-Moriya Interaction (DMI)</li>
 *   <li>Perpendicular Magnetic Anisotropy (PMA)</li>
 *   <li>External field</li>
 *   <li>Current-driven dynamics</li>
 * </ul>
 * </p>
 * 
 * <h3>References</h3>
 * <ul>
 * <li><b>Fert, A. et al.</b> (2017). "Magnetic skyrmions: advances in physics and potential applications". 
 *     <i>Nature Reviews Materials</i>, 2, 17031. 
 *     <a href="https://doi.org/10.1038/natrevmats.2017.31">DOI: 10.1038/natrevmats.2017.31</a></li>
 * </ul>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class Micromagnetics2D {

    private final int nx, ny;
    private final Real cellSize;
    private final Real[][][] magnetization; // [x][y][xyz components]
    private final SpintronicMaterial material;
    private final Real exchangeStiffness;  // A (J/m)
    private final Real dmiConstant;        // D (J/m²)
    private final Real anisotropyConstant; // K_u (J/m³)

    private static final Real MU0 = Real.of(1.2566370614e-6);

    public Micromagnetics2D(int nx, int ny, Real cellSize, SpintronicMaterial material,
                            Real A, Real D, Real Ku) {
        this.nx = nx;
        this.ny = ny;
        this.cellSize = cellSize;
        this.material = material;
        this.exchangeStiffness = A;
        this.dmiConstant = D;
        this.anisotropyConstant = Ku;
        this.magnetization = new Real[nx][ny][3];

        // Initialize: uniform +z (up)
        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
                magnetization[i][j] = new Real[]{Real.ZERO, Real.ZERO, Real.ONE};
            }
        }
    }

    /**
     * Nucleates a Néel skyrmion at position (cx, cy) with given radius.
     */
    public void nucleateSkyrmion(int cx, int cy, int radius) {
        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
                double dx = i - cx;
                double dy = j - cy;
                double r = Math.sqrt(dx * dx + dy * dy);

                if (r < radius) {
                    // Inside skyrmion: pointing down
                    magnetization[i][j] = new Real[]{Real.ZERO, Real.ZERO, Real.ONE.negate()};
                } else if (r < radius * 1.5) {
                    // Transition region: Néel wall (radial)
                    double theta = Math.PI * (r - radius) / (radius * 0.5);
                    double phi = Math.atan2(dy, dx);
                    Real mx = Real.of(Math.sin(theta) * Math.cos(phi));
                    Real my = Real.of(Math.sin(theta) * Math.sin(phi));
                    Real mz = Real.of(Math.cos(theta));
                    magnetization[i][j] = new Real[]{mx, my, mz};
                }
                // Outside: stays +z
            }
        }
    }

    /**
     * Calculates effective field at cell (i, j).
     */
    public Real[] calculateEffectiveField(int i, int j, Real hExtZ) {
        Real ms = material.getSaturationMagnetization();
        Real[] m = magnetization[i][j];

        // 1. Exchange: H_ex = (2A / μ₀Ms) * ∇²m
        Real[] laplacian = calculateLaplacian(i, j);
        Real exFactor = exchangeStiffness.multiply(Real.TWO).divide(MU0.multiply(ms));
        Real[] hExchange = scale(laplacian, exFactor);

        // 2. DMI: H_DMI = (2D / μ₀Ms) * (∇ × m)_z (interfacial DMI)
        Real[] curl = calculateCurl(i, j);
        Real dmiFactor = dmiConstant.multiply(Real.TWO).divide(MU0.multiply(ms));
        Real[] hDMI = scale(curl, dmiFactor);

        // 3. Anisotropy: H_K = (2Ku / μ₀Ms) * m_z * ẑ (PMA)
        Real anisFactor = anisotropyConstant.multiply(Real.TWO).divide(MU0.multiply(ms));
        Real[] hAnisotropy = {Real.ZERO, Real.ZERO, m[2].multiply(anisFactor)};

        // 4. External field (z-direction)
        Real[] hExt = {Real.ZERO, Real.ZERO, hExtZ};

        // Total
        return new Real[] {
            hExchange[0].add(hDMI[0]).add(hAnisotropy[0]).add(hExt[0]),
            hExchange[1].add(hDMI[1]).add(hAnisotropy[1]).add(hExt[1]),
            hExchange[2].add(hDMI[2]).add(hAnisotropy[2]).add(hExt[2])
        };
    }

    private Real[] calculateLaplacian(int i, int j) {
        Real[] lap = {Real.ZERO, Real.ZERO, Real.ZERO};
        Real dx2 = cellSize.pow(2);

        for (int d = 0; d < 3; d++) {
            Real center = magnetization[i][j][d].multiply(Real.of(-4));
            Real sum = center;
            if (i > 0) sum = sum.add(magnetization[i-1][j][d]);
            else sum = sum.add(magnetization[i][j][d]); // Neumann BC
            if (i < nx-1) sum = sum.add(magnetization[i+1][j][d]);
            else sum = sum.add(magnetization[i][j][d]);
            if (j > 0) sum = sum.add(magnetization[i][j-1][d]);
            else sum = sum.add(magnetization[i][j][d]);
            if (j < ny-1) sum = sum.add(magnetization[i][j+1][d]);
            else sum = sum.add(magnetization[i][j][d]);
            lap[d] = sum.divide(dx2);
        }
        return lap;
    }

    private Real[] calculateCurl(int i, int j) {
        // Simplified interfacial DMI: only contributes to z-component
        Real dx = cellSize;
        
        Real dmx_dy = (j < ny-1 ? magnetization[i][j+1][0] : magnetization[i][j][0])
                .subtract(j > 0 ? magnetization[i][j-1][0] : magnetization[i][j][0])
                .divide(dx.multiply(Real.TWO));
        Real dmy_dx = (i < nx-1 ? magnetization[i+1][j][1] : magnetization[i][j][1])
                .subtract(i > 0 ? magnetization[i-1][j][1] : magnetization[i][j][1])
                .divide(dx.multiply(Real.TWO));

        // Curl_z = ∂m_y/∂x - ∂m_x/∂y
        return new Real[]{Real.ZERO, Real.ZERO, dmy_dx.subtract(dmx_dy)};
    }

    private Real[] scale(Real[] v, Real s) {
        return new Real[]{v[0].multiply(s), v[1].multiply(s), v[2].multiply(s)};
    }

    /**
     * Performs one LLG time step for all cells.
     */
    public void step(Real dt, Real alpha, Real gamma, Real hExtZ) {
        Real[][][] newM = new Real[nx][ny][3];

        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
                Real[] hEff = calculateEffectiveField(i, j, hExtZ);
                
                FerromagneticLayer tempLayer = new FerromagneticLayer(material, cellSize, false);
                tempLayer.setMagnetization(magnetization[i][j][0], magnetization[i][j][1], magnetization[i][j][2]);
                
                Real[] updated = SpinTransport.stepLLGHeun(tempLayer, hEff, dt, alpha, gamma);
                newM[i][j] = updated;
            }
        }

        // Copy back
        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
                magnetization[i][j] = newM[i][j];
            }
        }
    }

    /**
     * Calculates the topological charge (skyrmion number).
     * Q = (1/4π) ∫∫ m · (∂m/∂x × ∂m/∂y) dxdy
     */
    public double calculateSkyrmionNumber() {
        double Q = 0;
        Real dx = cellSize;

        for (int i = 1; i < nx - 1; i++) {
            for (int j = 1; j < ny - 1; j++) {
                Real[] m = magnetization[i][j];
                Real[] dmdx = {
                    magnetization[i+1][j][0].subtract(magnetization[i-1][j][0]).divide(dx.multiply(Real.TWO)),
                    magnetization[i+1][j][1].subtract(magnetization[i-1][j][1]).divide(dx.multiply(Real.TWO)),
                    magnetization[i+1][j][2].subtract(magnetization[i-1][j][2]).divide(dx.multiply(Real.TWO))
                };
                Real[] dmdy = {
                    magnetization[i][j+1][0].subtract(magnetization[i][j-1][0]).divide(dx.multiply(Real.TWO)),
                    magnetization[i][j+1][1].subtract(magnetization[i][j-1][1]).divide(dx.multiply(Real.TWO)),
                    magnetization[i][j+1][2].subtract(magnetization[i][j-1][2]).divide(dx.multiply(Real.TWO))
                };

                // m · (dm/dx × dm/dy)
                Real[] cross = crossProduct(dmdx, dmdy);
                Real dot = m[0].multiply(cross[0]).add(m[1].multiply(cross[1])).add(m[2].multiply(cross[2]));
                Q += dot.doubleValue() * dx.doubleValue() * dx.doubleValue();
            }
        }
        return Q / (4 * Math.PI);
    }

    private static Real[] crossProduct(Real[] a, Real[] b) {
        return new Real[] {
            a[1].multiply(b[2]).subtract(a[2].multiply(b[1])),
            a[2].multiply(b[0]).subtract(a[0].multiply(b[2])),
            a[0].multiply(b[1]).subtract(a[1].multiply(b[0]))
        };
    }

    // Getters
    public int getNx() { return nx; }
    public int getNy() { return ny; }
    public Real getCellSize() { return cellSize; }
    public Real[] getMagnetization(int i, int j) { return magnetization[i][j]; }
}
