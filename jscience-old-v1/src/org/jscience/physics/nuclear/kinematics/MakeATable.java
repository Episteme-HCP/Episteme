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
