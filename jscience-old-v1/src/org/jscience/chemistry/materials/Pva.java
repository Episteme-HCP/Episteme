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

package org.jscience.chemistry.materials;

import org.jscience.chemistry.ChemistryConstants;


//The author agreed that we reuse his code under GPL, the above license is the original license
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class Pva {
    // METHODS
    /**
     * DOCUMENT ME!
     *
     * @param concn DOCUMENT ME!
     * @param molwt DOCUMENT ME!
     * @param temp DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double viscosity(double concn, double molwt, double temp) {
        double intVisc30;
        double intVisc20;
        double intViscT;
        double spViscT;
        double waterViscT;
        double viscosity;
        double concndlpg;

        intVisc30 = 4.53e-4 * Math.pow(molwt, 0.64);
        intVisc20 = intVisc30 * 1.07;
        intViscT = intVisc20 * Math.pow(1.07, -(temp - 20) / 10);
        concndlpg = concn / 10.0;
        spViscT = concndlpg * (intViscT +
            (0.201 * concndlpg * Math.pow(intViscT, 2.28)));
        waterViscT = Water.viscosity(temp);
        viscosity = (spViscT + 1.0) * waterViscT;

        return viscosity;
    }

    // Returns the density (kg/m^3) of aqueous pva solutions, at 30 C, as a function of
    /**
     * DOCUMENT ME!
     *
     * @param concn DOCUMENT ME!
     * @param molwt DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double density(double concn, double molwt) {
        double density;

        concn = concn / 10.0;
        density = 1000 * (0.99565 + ((0.00248 - (1.09 / molwt)) * concn) +
            ((0.000064 - (0.39 / molwt)) * concn * concn));

        return density;
    }

    // Returns the specific volume (kg/m^3) of pva in aqueous solution
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double specificVolume() {
        return 0.000765;
    }

    // returns the diffusion coefficient (m^2 s^-1) of pva in aqueous solution
    /**
     * DOCUMENT ME!
     *
     * @param concn DOCUMENT ME!
     * @param molwt DOCUMENT ME!
     * @param temp DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double diffCoeff(double concn, double molwt, double temp) {
        double diffcoef;
        double f;
        double viscosity;
        double specvol;
        double vol;
        double radius;
        double tempa;

        tempa = temp + 273.15;

        viscosity = Pva.viscosity(concn, molwt, temp);
        specvol = Pva.specificVolume();
        vol = (molwt * specvol) / (ChemistryConstants.AVOGADRO * 1000);
        radius = Math.pow((3.0 * vol) / (4.0 * Math.PI), 1.0 / 3.0);

        f = 6.0 * Math.PI * viscosity * radius;
        diffcoef = (ChemistryConstants.BOLTZMANN * tempa) / f;

        return diffcoef;
    }

    // Returns the refractive index of pva solutions as a function of g/l pva concentration
    /**
     * DOCUMENT ME!
     *
     * @param concn DOCUMENT ME!
     * @param wavl DOCUMENT ME!
     * @param temp DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double refractIndex(double concn, double wavl, double temp) {
        double refind;
        double rfwater;
        double rfincr;
        double a1 = -0.998419;
        double b1 = -1.87778e-17;

        rfwater = Water.refractIndex(wavl, temp);
        rfincr = 1.0 + (a1 * (1.0 + (b1 / Math.pow(wavl, 2))));
        refind = rfwater + ((rfincr * concn) / 10.0);

        return refind;
    }
}
