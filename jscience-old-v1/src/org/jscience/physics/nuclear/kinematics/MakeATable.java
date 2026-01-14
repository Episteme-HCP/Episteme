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

package org.jscience.physics.nuclear.kinematics;

import org.jscience.physics.nuclear.kinematics.nuclear.NuclearCollision;
import org.jscience.physics.nuclear.kinematics.nuclear.Nucleus;

import java.io.File;
import java.io.FileWriter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class MakeATable {
    /**
     * quick piece of code to make an ASCII table of some reaction
     * kinematics
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        try {
            Nucleus beam = new Nucleus(1, 1);
            Nucleus target = new Nucleus(6, 12);
            Nucleus projectile = new Nucleus(1, 3);
            double ExResid = 3.3536; //MeV
            double EbeamMin = 36; //MeV
            double EbeamMax = 36; //MeV
            double EbeamStep = 1; //MeV
            double thetaLabMin = 0; //degrees
            double thetaLabMax = 70;
            double thetaLabStep = 2;
            File out = new File("d:\\simulations\\C1033536.dat");
            FileWriter fw = new FileWriter(out);
            fw.write("Ebeam\tLabTheta\tBrho\n");

            for (double Ebeam = EbeamMin; Ebeam <= EbeamMax;
                    Ebeam += EbeamStep) {
                for (double thetaLab = thetaLabMin; thetaLab <= thetaLabMax;
                        thetaLab += thetaLabStep) {
                    NuclearCollision rxn = new NuclearCollision(target, beam,
                            projectile, Ebeam, thetaLab, ExResid);
                    fw.write(Ebeam + "\t" + thetaLab + "\t" +
                        (rxn.getQBrho(0) / projectile.Z) + "\n");
                }
            }

            fw.close();
        } catch (Exception e) {
            System.err.println(e);
        }

        System.out.println("done.");
    }
}
