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

package org.episteme.natural.chemistry.computational.quantum;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.natural.chemistry.Molecule;

/**
 * An abstract class representing the Self Consistent Field (SCF) method
 * such as Hartree-Fock.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class SCFMethod {
    
    protected static final int DEFAULT_MAX_ITERATION = 50;
    protected static final double DEFAULT_ENERGY_TOLERANCE = 1.0e-6;

    protected int maxIteration = DEFAULT_MAX_ITERATION;
    protected double energyTolerance = DEFAULT_ENERGY_TOLERANCE;
    protected int scfIteration;
    protected double totalEnergy;

    protected Molecule molecule;
    protected OneElectronIntegrals oneEI;
    protected TwoElectronIntegrals twoEI;
    
    // Matrices (using generic Matrix interface from core)
    protected Matrix<org.episteme.core.mathematics.numbers.real.Real> densityMatrix;
    protected Matrix<org.episteme.core.mathematics.numbers.real.Real> fockMatrix;
    protected Matrix<org.episteme.core.mathematics.numbers.real.Real> overlapMatrix;

    public SCFMethod(Molecule molecule) {
        this.molecule = molecule;
        this.scfIteration = 0;
    }

    /**
     * Set the integrals for the calculation.
     */
    public void setIntegrals(OneElectronIntegrals one, TwoElectronIntegrals two) {
        this.oneEI = one;
        this.twoEI = two;
    }

    /**
     * Performs the SCF calculation.
     */
    public abstract void scf();

    /**
     * Compute nuclear repulsion energy.
     */
    public abstract double calculateNuclearEnergy();

    public int getMaxIteration() {
        return maxIteration;
    }

    public void setMaxIteration(int maxIteration) {
        this.maxIteration = maxIteration;
    }

    public double getEnergyTolerance() {
        return energyTolerance;
    }

    public void setEnergyTolerance(double energyTolerance) {
        this.energyTolerance = energyTolerance;
    }

    public int getScfIteration() {
        return scfIteration;
    }

    public double getTotalEnergy() {
        return totalEnergy;
    }
    
    public Matrix<org.episteme.core.mathematics.numbers.real.Real> getDensityMatrix() {
        return densityMatrix;
    }
    
    public Matrix<org.episteme.core.mathematics.numbers.real.Real> getFockMatrix() {
        return fockMatrix;
    }
}
