/***************************************************************
 * Nuclear Simulation Java Class Libraries
 * Copyright (C) 2003 Yale University
 *
 * Original Developer
 *     Dale Visser (dale@visser.name)
 *
 * OSI Certified Open Source Software
 *
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the University of Illinois/NCSA 
 * Open Source License.
 *
 * This program is distributed in the hope that it will be 
 * useful, but without any warranty; without even the implied 
 * warranty of merchantability or fitness for a particular 
 * purpose. See the University of Illinois/NCSA Open Source 
 * License for more details.
 *
 * You should have received a copy of the University of 
 * Illinois/NCSA Open Source License along with this program; if 
 * not, see http://www.opensource.org/
 **************************************************************/
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
public class Mg24He3Table {
    /**
     * quick piece of code to make an ASCII table of some reaction
     * kinematics
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        try {
            Nucleus beam = new Nucleus(2, 3);
            Nucleus target = new Nucleus(12, 24);
            Nucleus[] projectiles = {
                    new Nucleus(1, 1), new Nucleus(1, 2), new Nucleus(1, 3),
                    new Nucleus(2, 3), new Nucleus(2, 4)
                };
            double ExResid = 0; //MeV
            double EbeamMin = 30; //MeV
            double EbeamMax = 30; //MeV
            double EbeamStep = 1; //MeV
            double thetaLabMin = 0; //degrees
            double thetaLabMax = 90;
            double thetaLabStep = 2;
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
                                beam, projectiles[i], Ebeam, thetaLab, ExResid);
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
