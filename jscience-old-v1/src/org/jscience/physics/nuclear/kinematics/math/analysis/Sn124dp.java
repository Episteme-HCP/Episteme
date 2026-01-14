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
 * Created on Nov 18, 2003
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
public class Sn124dp {

    Nucleus beam, p, d;
    EnergyLoss rdeloss, reloss;

    Sn124dp() {
        try {
            beam = new Nucleus(50, 124);
            p = new Nucleus(1, 1);
            d = new Nucleus(1, 2);
            rdeloss = new EnergyLoss(new SolidAbsorber(0.0062, Absorber.CM, "Si"));
            reloss = new EnergyLoss(new SolidAbsorber(0.0975, Absorber.CM, "Si"));
        } catch (NuclearException e) {
            e.printStackTrace();
        }
        double ebeam = 570.0;
        int rmin = 86;
        int rmax = 118;
        int lmin = 69;
        int lmax = 102;
        System.out.println("angle\tp_de\tp_e\td_de\td_e");
        for (int angle = lmin; angle <= lmax; angle++) {
            try {
                NuclearCollision r = new NuclearCollision(d, beam, p, ebeam, angle, 0.0);
                double energy = r.getLabEnergyProjectile(0);
                double e_remain = energy;
                double inc_angle = Math.toRadians(Math.abs(90.0 - angle));
                double de = rdeloss.getEnergyLoss(p, e_remain, inc_angle) * .001;
                e_remain -= de;
                double e = de;
                if (e_remain > 0.0) {
                    e += reloss.getEnergyLoss(p, e_remain, inc_angle) * .001;
                }
                System.out.print(angle + "\t" + de + "\t" + e + "\t");
                if (angle < 90) {
                    r = new NuclearCollision(d, beam, d, ebeam, angle, 0.0);
                    energy = r.getLabEnergyProjectile(0);
                    e_remain = energy;
                    de = rdeloss.getEnergyLoss(d, e_remain, inc_angle) * .001;
                    e_remain -= de;
                    e = de;
                    if (e_remain > 0.0) {
                        e += reloss.getEnergyLoss(d, e_remain, inc_angle) * .001;
                    }
                    System.out.println(de + "\t" + e);
                } else {
                    System.out.println();
                }
            } catch (Exception e) {
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        try {
            new Sn124dp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
