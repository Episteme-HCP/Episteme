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

package org.jscience.physics.nuclear.kinematics.nuclear;

import org.jscience.physics.nuclear.kinematics.math.UncertainNumber;


/**
 * Class for calculating rutherford scattering cross sections in barns.
 *
 * @author <a href="mailto:dale@visser.name">Dale Visser</a>
 */
public class RutherfordScattering {
    /**
     * DOCUMENT ME!
     */
    public static final double FM2_TO_BARNS = 0.01;

    //constants from July 2002 Particle Physics Booklet; Particle Data Group
    /**
     * DOCUMENT ME!
     */
    public static final UncertainNumber ALPHA = new UncertainNumber(7.297352533e-3,
            2.7e-11);

    /**
     * DOCUMENT ME!
     */
    public static final UncertainNumber HBAR_C = new UncertainNumber(197.3269602,
            7.7e-6); //MeV-fm

    /**
     * DOCUMENT ME!
     */
    public static final UncertainNumber E2 = ALPHA.times(HBAR_C); //MeV-fm

    /**
     * DOCUMENT ME!
     */
    private Nucleus beam;

    /**
     * DOCUMENT ME!
     */
    private Nucleus target;

    /**
     * DOCUMENT ME!
     */
    private double ebeam;

    /**
     * DOCUMENT ME!
     */
    private double labangle;

    /**
     * DOCUMENT ME!
     */
    private double xsec;

/**
     * Define a rutherford scattering scenario.
     *
     * @param beam     nuclear species
     * @param target   nuclear species
     * @param ebeam    in MeV
     * @param labangle in degrees
     * @throws KinematicsException for unphysical angles
     */
    public RutherfordScattering(Nucleus beam, Nucleus target, double ebeam,
        double labangle) throws KinematicsException {
        this.beam = beam;
        this.target = target;
        this.ebeam = ebeam;
        this.labangle = labangle;
        calculate();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws KinematicsException DOCUMENT ME!
     */
    private void calculate() throws KinematicsException {
        double mbeam = beam.getMass().value; //MeV
        double mtarget = target.getMass().value;

        try {
            NuclearCollision r = new NuclearCollision(target, beam, beam,
                    ebeam, labangle, 0.0);
            double ebeam_cm = Math.sqrt((mbeam * mbeam) + (mtarget * mtarget) +
                    (2 * mtarget * (ebeam + mbeam))) - (mbeam + mtarget);
            double cmangle = r.getCMAngleProjectile(0);
            xsec = Math.pow((beam.Z * target.Z * E2.value) / (4.0 * ebeam_cm),
                    2.0) * Math.pow(Math.sin(Math.toRadians(0.5 * cmangle)),
                    -4.0);
            xsec *= FM2_TO_BARNS;
        } catch (NuclearException e) {
            System.err.println("Shouldn't be here.");
            e.printStackTrace();
        }
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return RutherfordScattering differential cross-section in barns/sr
     */
    public double getXsection() {
        return xsec;
    }

    /**
     * Change the beam energy.
     *
     * @param ebeam in MeV
     *
     * @throws KinematicsException if stored lab angle become unphysical
     */
    public void setEbeam(double ebeam) throws KinematicsException {
        this.ebeam = ebeam;
        calculate();
    }

    /**
     * Change the lab angle.
     *
     * @param angle in degrees
     *
     * @throws KinematicsException if the angle is unphysical
     */
    public void setLabAngle(double angle) throws KinematicsException {
        this.labangle = angle;
        calculate();
    }
}
