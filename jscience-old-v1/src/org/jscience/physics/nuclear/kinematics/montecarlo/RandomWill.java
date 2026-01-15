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

/*
 * Random.java
 *
 * Created on May 21, 2001, 4:35 PM
 */
package org.jscience.physics.nuclear.kinematics.montecarlo;

/**
 * 
DOCUMENT ME!
 *
 * @author org.jscience.physics.nuclear.kinematics
 */
public class RandomWill extends Object {
    /**
     * DOCUMENT ME!
     */
    double[] u = new double[97];

    /**
     * DOCUMENT ME!
     */
    double c;

    /**
     * DOCUMENT ME!
     */
    double cd;

    /**
     * DOCUMENT ME!
     */
    double cm;

    /**
     * DOCUMENT ME!
     */
    double uni;

    /**
     * DOCUMENT ME!
     */
    int i97;

    /**
     * DOCUMENT ME!
     */
    int j97;

    /**
     * DOCUMENT ME!
     */
    boolean initialised;

/**
     * Creates new Random number generator.  The seed value Will has always
     * used is 6789754.
     *
     * @param ijkl seed value from 0 to 900000000
     */
    public RandomWill(int ijkl) throws Exception {
        int i;
        int j;
        int k;
        int l;
        int ij;
        int kl;
        int ii;
        int jj;
        int m;
        double s;
        double t;

        initialised = false;

        if ((ijkl < 0) || (ijkl > 900000000)) {
            throw new Exception("The random number seed must have a value " +
                "between 0 and 900 000 000.");
        }

        ij = ijkl / 30082;
        kl = ijkl - (30082 * ij);

        i = mod(ij / 177, 177) + 2;
        j = mod(ij, 177) + 2;
        k = mod(kl / 169, 178) + 1;
        l = mod(kl, 169);

        for (ii = 0; ii < 97; ii++) {
            s = 0.0;
            t = 0.5;

            for (jj = 0; jj < 24; jj++) {
                m = mod(mod(i * j, 179) * k, 179);
                i = j;
                j = k;
                k = m;
                l = mod((53 * l) + 1, 169);

                if (mod(l * m, 64) >= 32) {
                    s = s + t;
                }

                t = 0.5 * t;
            }

            u[ii] = s;
        }

        c = 362436.0 / 16777216.0;
        cd = 7654321.0 / 16777216.0;
        cm = 16777213.0 / 16777216.0;
        i97 = 96;
        j97 = 32;
        initialised = true;
        System.out.println("Initialized " + getClass() + " with seed " + ijkl);
    }

    /**
     * Creates a new RandomWill object.
     *
     * @throws Exception DOCUMENT ME!
     */
    public RandomWill() throws Exception {
        this(6789754);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double next() {
        /*if (!initialised) {
        throw new Exception(getClass().getName()+".next(): not initialized");
        }*/
        uni = u[i97] - u[j97];

        if (uni < 0.0) {
            uni = uni + 1.0;
        }

        u[i97] = uni;
        i97 = i97 - 1;

        if (i97 == -1) {
            i97 = 96;
        }

        j97 = j97 - 1;

        if (j97 == -1) {
            j97 = 96;
        }

        c = c - cd;

        if (c < 0.0) {
            c = c + cm;
        }

        uni = uni - c;

        if (uni < 0.0) {
            uni = uni + 1.0;
        }

        return uni;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static private int mod(int i, int j) {
        return i % j;
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     */
    public static void main(String[] a) {
        try {
            RandomWill rw = new RandomWill();

            for (int i = 0; i < 10; i++) {
                System.out.println(rw.next());
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
