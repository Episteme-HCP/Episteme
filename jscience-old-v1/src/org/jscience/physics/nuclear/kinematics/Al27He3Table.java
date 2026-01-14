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
public class Al27He3Table {
    /**
     * quick piece of code to make an ASCII table of some reaction
     * kinematics
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        try {
            Nucleus beam = new Nucleus(2, 3);
            Nucleus target = new Nucleus(13, 27);
            Nucleus[] projectiles = { new Nucleus(1, 2), new Nucleus(1, 3) };
            double[] ExResid = { 0, 0.9574 }; //MeV
                                              //double ExResid = 0; //MeV

            double EbeamMin = 30; //MeV
            double EbeamMax = 30; //MeV
            double EbeamStep = 1; //MeV
            double thetaLabMin = 5; //degrees
            double thetaLabMax = 20;
            double thetaLabStep = 1;
            File out = new File(
                    "c:\\Documents and Settings\\Administrator\\My Documents\\Work Related\\24Mg(3He,t) planning\\table.dat");
            FileWriter fw = new FileWriter(out);
            fw.write("Ebeam\tLabTheta");

            for (int i = projectiles.length - 1; i >= 0; i--) {
                fw.write("\tBrho[" + projectiles[i] + "]");
            }

            fw.write('\n');

            for (double Ebeam = EbeamMin; Ebeam <= EbeamMax;
                    Ebeam += EbeamStep) {
                for (double thetaLab = thetaLabMin; thetaLab <= thetaLabMax;
                        thetaLab += thetaLabStep) {
                    fw.write(Ebeam + "\t" + thetaLab);

                    /*+ "\t" + (rxn.getQBrho(0)/projectile.Z)
                    + "\n");*/
                    for (int i = projectiles.length - 1; i >= 0; i--) {
                        NuclearCollision rxn = new NuclearCollision(target,
                                beam, projectiles[i], Ebeam, thetaLab,
                                ExResid[i]);
                        System.out.println(rxn + ":" +
                            rxn.getAngleDegeneracy());
                        fw.write("\t" + (rxn.getQBrho(0) / projectiles[i].Z));
                    }

                    fw.write('\n');
                }
            }

            fw.close();
        } catch (Exception e) {
            System.err.println(e);
        }

        System.out.println("done.");
    }
}
