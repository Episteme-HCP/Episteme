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
