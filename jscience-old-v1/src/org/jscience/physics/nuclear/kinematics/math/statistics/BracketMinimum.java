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


//#include <math.h>
//#define NRANSI
//#include "nrutil.h"
//#define SHFT(a,b,c,d) (a)=(b);(b)=(c);(c)=(d);
/**
 * Based on mnbrak.c in "Numerical Recipes" Given 2 initial points axi and
 * bxi, this routine searches in the downhill direction (defined by the
 * function as evaluated at the initial points) and returns new points ax,bx
 * cx that bracket a minimum of the function. Also returned are the function
 * values at the 3 points, fa, fb, and fc.
 */
public class BracketMinimum {
    /**
     * DOCUMENT ME!
     */
    static final double GOLD = 1.618034;

    /**
     * DOCUMENT ME!
     */
    static final double GLIMIT = 100.0;

    /**
     * DOCUMENT ME!
     */
    static final double TINY = 1.0e-20;

    /**
     * DOCUMENT ME!
     */
    public double ax; //points that bracket minimum of function;

    /**
     * DOCUMENT ME!
     */
    public double bx; //points that bracket minimum of function;

    /**
     * DOCUMENT ME!
     */
    public double cx; //points that bracket minimum of function;

    /**
     * DOCUMENT ME!
     */
    public double fa; //function evaluated at above points

    /**
     * DOCUMENT ME!
     */
    public double fb; //function evaluated at above points

    /**
     * DOCUMENT ME!
     */
    public double fc; //function evaluated at above points

    /**
     * DOCUMENT ME!
     */
    Function func;

    /**
     * Creates a new BracketMinimum object.
     *
     * @param f DOCUMENT ME!
     */
    public BracketMinimum(Function f) {
        func = f;
    }

    /**
     * 
     *
     * @param axi DOCUMENT ME!
     * @param bxi DOCUMENT ME!
     */
    public void bracket(double axi, double bxi) {
        double ulim;
        double u;
        double r;
        double q;
        double fu;
        double dum;

        ax = axi;
        bx = bxi;
        fa = func.valueAt(ax);
        fb = func.valueAt(bx);

        if (fb > fa) {
            //SHFT(dum,*ax,*bx,dum)
            dum = ax;
            ax = bx;
            bx = dum;
            //SHFT(dum,*fb,*fa,dum)
            dum = fb;
            fb = fa;
            fa = dum;
        }

        cx = (bx) + (GOLD * (bx - ax));
        fc = func.valueAt(cx);

        while (fb > fc) {
            r = (bx - ax) * (fb - fc);
            q = (bx - cx) * (fb - fa);
            u = (bx) -
                ((((bx - cx) * q) - ((bx - ax) * r)) / (2.0 * sign(Math.max(
                        Math.abs(q - r), TINY), q - r)));
            ulim = (bx) + (GLIMIT * (cx - bx));

            if (((bx - u) * (u - cx)) > 0.0) {
                fu = func.valueAt(u);

                if (fu < fc) {
                    ax = (bx);
                    bx = u;
                    fa = (fb);
                    fb = fu;

                    return;
                } else if (fu > fb) {
                    cx = u;
                    fc = fu;

                    return;
                }

                u = (cx) + (GOLD * (cx - bx));
                fu = func.valueAt(u);
            } else if (((cx - u) * (u - ulim)) > 0.0) {
                fu = func.valueAt(u);

                if (fu < fc) {
                    //SHFT(*bx,*cx,u,*cx+GOLD*(*cx-*bx))
                    bx = cx;
                    cx = u;
                    u = cx + (GOLD * (cx - bx));
                    //SHFT(*fb,*fc,fu,(*func)(u))
                    fb = fc;
                    fc = fu;
                    fu = func.valueAt(u);
                }
            } else if (((u - ulim) * (ulim - cx)) >= 0.0) {
                u = ulim;
                fu = func.valueAt(u);
            } else {
                u = (cx) + (GOLD * (cx - bx));
                fu = func.valueAt(u);
            }

            //SHFT(*ax,*bx,*cx,u)
            ax = bx;
            bx = cx;
            cx = u;
            //SHFT(*fa,*fb,*fc,fu)
            fa = fb;
            fb = fc;
            fc = fu;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double sign(double a, double b) {
        return ((b >= 0.0) ? Math.abs(a) : (-Math.abs(a)));
    }
}
