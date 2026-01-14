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

/*
 * Created on Nov 14, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.jscience.physics.nuclear.kinematics.math.analysis;

import org.jscience.physics.nuclear.kinematics.nuclear.*;

/**
 * @author Administrator
 *         <p/>
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class OrnlAstroIonCounter {

    Nucleus species;
    EnergyLoss window;
    EnergyLoss de12, de3;
    double pressure = 3;//gas pressure in torr
    double emin = 0;
    double emax = 18;
    double dele = 0.5;

    double[] energy, wloss, de1loss, de2loss, de3loss;

    public OrnlAstroIonCounter() throws NuclearException {
        species = new Nucleus(12, 24);
        window = new EnergyLoss(SolidAbsorber.Mylar(200, Absorber.MICROGRAM_CM2));
        de12 = new EnergyLoss(GasAbsorber.CF4(5, pressure));
        de3 = new EnergyLoss(GasAbsorber.CF4(20, pressure));
        int count = 0;
        for (double e = emin; e <= emax; e += dele) {
            count++;
        }
        energy = new double[count];
        wloss = new double[count];
        de1loss = new double[count];
        de2loss = new double[count];
        de3loss = new double[count];
        double e = emin;
        for (int i = 0; i < count; i++) {
            double eleft = e;
            energy[i] = e;
            if (eleft > 0.0) {
                wloss[i] = .001 * window.getEnergyLoss(species, eleft);
                eleft -= wloss[i];
            } else {
                wloss[i] = 0.0;
            }
            if (eleft > 0.0) {
                de1loss[i] = .001 * de12.getEnergyLoss(species, eleft);
                eleft -= de1loss[i];
            } else {
                de1loss[i] = 0.0;
            }
            if (eleft > 0.0) {
                de2loss[i] = .001 * de12.getEnergyLoss(species, eleft);
                eleft -= de2loss[i];
            } else {
                de2loss[i] = 0.0;
            }
            if (eleft > 0.0) {
                de3loss[i] = .001 * de3.getEnergyLoss(species, eleft);
                eleft -= de3loss[i];
            } else {
                de3loss[i] = 0.0;
            }
            e += dele;
        }
        System.out.println("E\tDE1+DE2\tDE1+DE2+DE3");
        for (int i = 0; i < count; i++) {
            double sum1 = de1loss[i] + de2loss[i];
            double sum2 = sum1 + de3loss[i];
            System.out.println(energy[i] + "\t" + sum1 + "\t" + sum2);
        }
    }

    public static void main(String[] args) {
        try {
            new OrnlAstroIonCounter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
