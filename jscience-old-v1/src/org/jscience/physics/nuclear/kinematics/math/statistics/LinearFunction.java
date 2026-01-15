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
package org.jscience.physics.nuclear.kinematics.math.statistics;

import org.jscience.physics.nuclear.kinematics.math.UncertainNumber;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class LinearFunction implements UncertainFunction {
    /** y=a(x-delx)+b */
    UncertainNumber a;

    /** y=a(x-delx)+b */
    UncertainNumber b;

    /**
     * delx is an offset introduced in x which diagonalizes the
     * covariance matrix of a and b.  It has no error of its own, by
     * assumption.
     */
    double delx;

    /**
     * Creates a new LinearFunction object.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param delx DOCUMENT ME!
     */
    public LinearFunction(UncertainNumber a, UncertainNumber b, double delx) {
        this.a = a;
        this.b = b;
        this.delx = delx;
    }

    /**
     * Creates a new LinearFunction object.
     *
     * @param a DOCUMENT ME!
     * @param siga DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param sigb DOCUMENT ME!
     * @param delx DOCUMENT ME!
     */
    public LinearFunction(double a, double siga, double b, double sigb,
        double delx) {
        this(new UncertainNumber(a, siga), new UncertainNumber(b, sigb), delx);
    }

    /**
     * Creates a new LinearFunction object.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param delx DOCUMENT ME!
     */
    public LinearFunction(double a, double b, double delx) {
        this(a, 0.0, b, 0.0, delx);
    }

    /**
     * Given an array of uncertain numbers, return back a function
     * value.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws StatisticsException DOCUMENT ME!
     */
    public UncertainNumber evaluate(UncertainNumber[] x)
        throws StatisticsException {
        UncertainNumber xp;

        if (x.length > 1) {
            throw new StatisticsException("LinearFit: too many" +
                " arguments: " + x.length);
        }

        //Gives the appropriate number to use in the function.
        xp = new UncertainNumber(x[0].value - delx, x[0].error);

        return a.plus(b.times(xp));
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double valueAt(double x) {
        return a.value + (b.value * x);
    }
}
