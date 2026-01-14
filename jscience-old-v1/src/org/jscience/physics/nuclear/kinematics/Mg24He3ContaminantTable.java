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
public class Mg24He3ContaminantTable {
    /**
     * quick piece of code to make an ASCII table of some reaction
     * kinematics
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        try {
            Nucleus beam = new Nucleus(2, 3);
            Nucleus[] target = { new Nucleus(12, 24), new Nucleus(8, 16) };
            Nucleus projectile = new Nucleus(1, 3);
            double[][] ExResid = {
                    { 2.349, 2.534, 2.810, 2.900 },
                    { 0, 0.193, 0.424, 0.721 }
                }; //MeV
            double Ebeam = 30;
            double thetaLabMin = 0; //degrees
            double thetaLabMax = 90;
            double thetaLabStep = 2;
            double B = 11;
            File out = new File(
                    "c:\\Documents and Settings\\Administrator\\My Documents\\Work Related\\24Mg(3He,t) planning\\table.dat");
            FileWriter fw = new FileWriter(out);
            fw.write("Ebeam\tLabTheta");

            for (int i = target.length - 1; i >= 0; i--) {
                for (int j = ExResid[i].length - 1; j >= 0; j--) {
                    fw.write("\trhoDiff[" + target[i] + ExResid[i][j] + "]");
                }
            }

            fw.write('\n');

            for (double thetaLab = thetaLabMin; thetaLab <= thetaLabMax;
                    thetaLab += thetaLabStep) {
                NuclearCollision rref = new NuclearCollision(new Nucleus(8, 16),
                        beam, projectile, Ebeam, thetaLab, 0.0);
                double rhoref = rref.getQBrho(0) / projectile.Z / B;
                fw.write(Ebeam + "\t" + thetaLab);

                for (int i = target.length - 1; i >= 0; i--) { //loop through the targets

                    for (int j = ExResid[i].length - 1; j >= 0; j--) { //loop for excitations for particular target

                        NuclearCollision rxn = new NuclearCollision(target[i],
                                beam, projectile, Ebeam, thetaLab, ExResid[i][j]);
                        System.out.println(rxn + " degeneracy:" +
                            rxn.getAngleDegeneracy());
                        fw.write("\t" +
                            ((rxn.getQBrho(0) / projectile.Z / B) - rhoref));
                    }
                }

                fw.write('\n');
            }

            fw.close();
        } catch (Exception e) {
            System.err.println(e);
        }

        System.out.println("done.");
    }
}
