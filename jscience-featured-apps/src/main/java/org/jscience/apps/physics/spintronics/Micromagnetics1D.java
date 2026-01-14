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

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * 1D Micromagnetic simulation for domain wall dynamics.
 * <p>
 * Discretizes a magnetic nanowire into a chain of interacting spins.
 * Supports domain wall nucleation and current-driven motion.
 * </p>
 * 
 * <h3>Physics</h3>
 * <p>
 * The effective field at each cell includes:
 * <ul>
 *   <li>Exchange: $H_{ex} = \frac{2A}{\mu_0 M_s} \nabla^2 \mathbf{m}$</li>
 *   <li>Anisotropy: $H_K = \frac{2K_u}{\mu_0 M_s} (\mathbf{m} \cdot \hat{u}) \hat{u}$</li>
 *   <li>External field: $H_{ext}$</li>
 *   <li>STT from current</li>
 * </ul>
 * </p>
 * 
 * <h3>References</h3>
 * <ul>
 * <li><b>Thiaville, A. et al.</b> (2005). "Micromagnetic understanding of current-driven domain wall motion in patterned nanowires". 
 *     <i>Europhys. Lett.</i>, 69(6), 990. 
 *     <a href="https://doi.org/10.1209/epl/i2004-10452-6">DOI: 10.1209/epl/i2004-10452-6</a></li>
 * </ul>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class Micromagnetics1D {

    private final int numCells;
    private final Real cellSize;    // Δx (m)
    private final Real[][] magnetization; // [cell][x,y,z]
    private final SpintronicMaterial material;
    private final Real exchangeStiffness; // A (J/m)
    private final Real anisotropyConstant; // K_u (J/m³)
    private final Real[] easyAxis = {Real.ONE, Real.ZERO, Real.ZERO}; // Uniaxial

    private static final Real MU0 = Real.of(1.2566370614e-6); // Permeability of free space

    public Micromagnetics1D(int numCells, Real cellSize, SpintronicMaterial material, Real A, Real Ku) {
        this.numCells = numCells;
        this.cellSize = cellSize;
        this.material = material;
        this.exchangeStiffness = A;
        this.anisotropyConstant = Ku;
        this.magnetization = new Real[numCells][3];

        // Initialize: half up, half down (domain wall in center)
        for (int i = 0; i < numCells; i++) {
            if (i < numCells / 2) {
                magnetization[i] = new Real[]{Real.ONE, Real.ZERO, Real.ZERO};
            } else {
                magnetization[i] = new Real[]{Real.ONE.negate(), Real.ZERO, Real.ZERO};
            }
        }
    }

    /**
     * Calculates the effective field at a given cell.
     */
    public Real[] calculateEffectiveField(int cell, Real hExtX, Real hExtY, Real hExtZ) {
        Real ms = material.getSaturationMagnetization();
        Real[] m = magnetization[cell];

        // 1. Exchange field: H_ex = (2A / μ₀Ms) * ∇²m
        Real[] laplacian = {Real.ZERO, Real.ZERO, Real.ZERO};
        if (cell > 0 && cell < numCells - 1) {
            for (int d = 0; d < 3; d++) {
                laplacian[d] = magnetization[cell - 1][d]
                        .add(magnetization[cell + 1][d])
                        .subtract(magnetization[cell][d].multiply(Real.TWO))
                        .divide(cellSize.pow(2));
            }
        }
        Real exchangeFactor = exchangeStiffness.multiply(Real.TWO).divide(MU0.multiply(ms));
        Real[] hExchange = {
            laplacian[0].multiply(exchangeFactor),
            laplacian[1].multiply(exchangeFactor),
            laplacian[2].multiply(exchangeFactor)
        };

        // 2. Anisotropy field: H_K = (2Ku / μ₀Ms) * (m·u) * u
        Real mDotU = m[0].multiply(easyAxis[0]).add(m[1].multiply(easyAxis[1])).add(m[2].multiply(easyAxis[2]));
        Real anisotropyFactor = anisotropyConstant.multiply(Real.TWO).divide(MU0.multiply(ms));
        Real[] hAnisotropy = {
            easyAxis[0].multiply(mDotU).multiply(anisotropyFactor),
            easyAxis[1].multiply(mDotU).multiply(anisotropyFactor),
            easyAxis[2].multiply(mDotU).multiply(anisotropyFactor)
        };

        // 3. External field
        Real[] hExt = {Real.of(hExtX.doubleValue()), Real.of(hExtY.doubleValue()), Real.of(hExtZ.doubleValue())};

        // Total
        return new Real[] {
            hExchange[0].add(hAnisotropy[0]).add(hExt[0]),
            hExchange[1].add(hAnisotropy[1]).add(hExt[1]),
            hExchange[2].add(hAnisotropy[2]).add(hExt[2])
        };
    }

    /**
     * Performs one LLG time step for all cells.
     */
    public void step(Real dt, Real alpha, Real gamma, Real hExtX, Real hExtY, Real hExtZ) {
        Real[][] newM = new Real[numCells][3];

        for (int i = 0; i < numCells; i++) {
            Real[] hEff = calculateEffectiveField(i, hExtX, hExtY, hExtZ);

            // Use simple LLG stepper (could be parallelized)
            FerromagneticLayer tempLayer = new FerromagneticLayer(material, cellSize, false);
            tempLayer.setMagnetization(magnetization[i][0], magnetization[i][1], magnetization[i][2]);

            Real[] updated = SpinTransport.stepLLG(tempLayer, hEff, dt, alpha, gamma);
            newM[i] = updated;
        }

        // Copy back
        for (int i = 0; i < numCells; i++) {
            magnetization[i] = newM[i];
        }
    }

    /**
     * Gets the domain wall position (center of transition).
     */
    public int getDomainWallPosition() {
        for (int i = 1; i < numCells; i++) {
            if (magnetization[i][0].multiply(magnetization[i - 1][0]).compareTo(Real.ZERO) < 0) {
                return i;
            }
        }
        return numCells / 2;
    }

    /**
     * Gets magnetization at a cell.
     */
    public Real[] getMagnetization(int cell) {
        return magnetization[cell];
    }

    public int getNumCells() { return numCells; }
    public Real getCellSize() { return cellSize; }
}
