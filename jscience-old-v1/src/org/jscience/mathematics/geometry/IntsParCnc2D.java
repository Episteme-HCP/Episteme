/*
 * 2D�̕�?�Ɖ~??��?�̌�_��?�߂�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: IntsParCnc2D.java,v 1.3 2007-10-23 18:19:42 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

import org.jscience.mathematics.algebraic.numbers.Complex;

import java.util.Vector;


/**
 * 2D�̕�?�Ɖ~??��?�̌�_��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:42 $
 */
class IntsParCnc2D extends IntsCncCnc2D {
/**
     * Creates a new IntsParCnc2D object.
     */
    IntsParCnc2D() {
        super();
    }

    /**
     * ��?�Ƒo��?�̌�_��?�߂邽�߂̕��⽂Ă�
     *
     * @param par ��?� : A
     * @param hyp �o��?� : B
     * @param trans DOCUMENT ME!
     *
     * @return ���̌W?�
     */
    double[] getCoefficent(Parabola2D par, Hyperbola2D hyp,
        CartesianTransformationOperator2D trans) {
        /*
         * NOTE:
         *
         * AbstractPoint of inverse transformed of B (Hyperbola) is
         *
         *        x = cosP * E*coshL - sinP * F*sinhL + Cx    ---> (1)
         *        y = sinP * E*coshL + cosP * F*sinhL + Cy
         *        /  E = distance from center to vertex      \
         *        |  F = b of asymptotic slope b/dB->x_radius|
         *        |  P = ( slope of B ) - ( slope of A )     |
         *        \  C = ( center of B ) - ( vertex of A )
         *
         * AbstractPoint of A (Parabola) is
         *
         *        x = A * s**2                                ---> (2)
         *        y = 2 * A * s
         *        (  A = dA->focal_dist )
         *
         * (1) & (2) -->
         *
         *          ( (I+H)**2 - J**2 )                       * sinhL**4
         *        + 2 * ( (I+H)*L - J*K )                     * sinhL**3
         *        + ( 2 * (I+H)*(M+H) + L**2 - J**2 - K**2 )  * sinhL**2
         *        + 2 * ( (M+H)*L - J*K )                     * sinhL
         *        + ( (M+H)**2 - K**2 )
         *        = 0
         *
         *        / H = E**2 * cosP**2                           \
         *        | I = F**2 * sinP**2                           |
         *        | J = 2 * E * F * cosP * sinP                  |
         *        | K = 2 * E * ( Cy*sinP - 2*A*cosP )           |
         *        | L = 2 * F * ( Cy*cosP + 2*A*sinP )           |
         *        \ M = Cy**2 - 4*A*Cx**2
         *
         * This is a 4th order polynomial for sinhL.
         * We'll get roots of this, then intersection
         */
        double dAslp; /* A's slope angle */
        double dBslp; /* B's slope angle */
        double eslope; /* difference of slope angles */
        double erc; /* difference of slope angles */
        double ers; /* difference of slope angles */
        Vector2D Ac2Bc; /* vector from A's center to B's vertex */
        Point2D eiBc; /* transformed vertex of B */
        double dTol = par.getToleranceForDistance();

        // vector from B's center to A's center
        Ac2Bc = hyp.position().location().subtract(par.position().location());

        // inverse rotated point
        eiBc = trans.toLocal(Ac2Bc).toPoint2D();

        // make slope of A & B
        dAslp = Math.atan2(par.position().x().y(), par.position().x().x());
        dBslp = Math.atan2(hyp.position().x().y(), hyp.position().x().x());
        eslope = dBslp - dAslp;
        erc = Math.cos(eslope);
        ers = Math.sin(eslope);

        // make Coefficents of polynomial (real)
        double[] eprep = new double[6];
        double[] ercoef = new double[5];

        double a = par.focalDist();
        double e = hyp.xRadius();
        double f = hyp.yRadius();
        double cx = eiBc.x();
        double cy = eiBc.y();

        // make polynomial
        eprep[0] = (e * ers) * (e * ers); // H
        eprep[1] = (f * erc) * (f * erc); // I
        eprep[2] = 2.0 * e * f * erc * ers; // J
        eprep[3] = 2.0 * e * ((cy * ers) - (2.0 * a * erc)); // K
        eprep[4] = 2.0 * f * ((cy * erc) + (2.0 * a * ers)); // L
        eprep[5] = (cy * cy) - (4.0 * a * cx); // M

        ercoef[4] = eprep[1] + eprep[0]; // I+H
        ercoef[0] = eprep[5] + eprep[0]; // M+H
        ercoef[1] = eprep[2] * eprep[3]; // J+K
        ercoef[3] = 2.0 * ((ercoef[4] * eprep[4]) - ercoef[1]); // 2*((I+H)*L-(J+K))
        ercoef[1] = 2.0 * ((ercoef[0] * eprep[4]) - ercoef[1]); // 2*((M+H)*L-(J+K))
        ercoef[2] = ((2.0 * ercoef[4] * ercoef[0]) + (eprep[4] * eprep[4])) -
            (eprep[2] * eprep[2]) - (eprep[3] * eprep[3]); // 2*(I+H)*(M+H)+L**2-J**2-K**2
        ercoef[4] = (ercoef[4] * ercoef[4]) - (eprep[2] * eprep[2]); // (I+H)**2 - J**2
        ercoef[0] = (ercoef[0] * ercoef[0]) - (eprep[3] * eprep[3]); // (M+H)**2 - K**2

        return ercoef;
    }

    /**
     * ���̉⩂��?�Ƒo��?�̌�_��?�߂�
     *
     * @param par ��?� : A
     * @param hyp �o��?� : B
     * @param root ���̉�
     * @param trans DOCUMENT ME!
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_
     */
    private IntersectionPoint2D[] getIntersection(Parabola2D par,
        Hyperbola2D hyp, Complex[] root,
        CartesianTransformationOperator2D trans, boolean doExchange) {
        double dTol = par.getToleranceForDistance();
        Vector intervec = new Vector();

        for (int i = 0; i < root.length; i++) {
            double sinhB = root[i].real();
            double coshB = Math.sqrt(1.0 + (sinhB * sinhB));
            double Bparam = Math.log(sinhB + coshB);
            Point2D dDpnt = trans.toLocal(hyp.coordinates(Bparam));

            double Aparam = dDpnt.y() / (2.0 * par.focalDist());

            // make intersection points
            PointOnCurve2D aPnt = new PointOnCurve2D(par, Aparam,
                    GeometryElement.doCheckDebug);
            PointOnCurve2D bPnt = new PointOnCurve2D(hyp, Bparam,
                    GeometryElement.doCheckDebug);

            // identical check
            if (aPnt.identical(bPnt)) {
                IntersectionPoint2D inter = new IntersectionPoint2D(aPnt, bPnt,
                        GeometryElement.doCheckDebug);

                // duplicate check
                if ((inter != null) && checkUnique(inter, intervec)) {
                    intervec.addElement(inter);
                }
            }
        }

        IntersectionPoint2D[] intersectPoints = vectorToArray(intervec,
                doExchange);

        return intersectPoints;
    }

    /**
     * ��?�Ƒo��?�̌�_��?�߂�
     *
     * @param par ��?� : A
     * @param hyp �o��?� : B
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_
     */
    IntersectionPoint2D[] intersection(Parabola2D par, Hyperbola2D hyp,
        boolean doExchange) {
        CartesianTransformationOperator2D trans = new CartesianTransformationOperator2D(par.position(),
                1.0);

        // get coefficent
        double[] coefficent = getCoefficent(par, hyp, trans);

        // get root of above polynomial
        Complex[] root = getRoot(coefficent);

        if (root == null) {
            return new IntersectionPoint2D[0];
        }

        return getIntersection(par, hyp, root, trans, doExchange);
    }

    /**
     * ��?�m�̌�_��?�߂邽�߂̕��⽂Ă�
     *
     * @param parA ��?�
     * @param parB ��?�
     * @param trans DOCUMENT ME!
     *
     * @return ���̌W?�
     */
    double[] getCoefficent(Parabola2D parA, Parabola2D parB,
        CartesianTransformationOperator2D trans) {
        /*
         * NOTE:
         *
         * AbstractPoint of inverse transformed of B (Parabola parB)is
         *
         *        x = cosP * B * t**2 - sinP * 2*B * t + Vx    ---> (1)
         *        y = sinP * B * t**2 + cosP * 2*B * t + Vy
         *
         *        /  B = dB->focal_dist                     \
         *        |  P = ( slope of B ) - ( slope of A )    |
         *        \  V = ( vertex of B ) - ( vertex of A )
         *
         * AbstractPoint of A (Parabola parA) is
         *
         *        x = A * s**2                                 ---> (2)
         *        y = 2 * A * s
         *        (A = dA->focal_dist)
         *
         * (1) & (2) -->
         *
         *          ( B * sinP )**2                              * t**4
         *        + 4 * B**2 * sinP * cosP                       * t**3
         *        + 2 * B * ( Vy*sinP + 2*B*cosP**2 - 2*A*cosP ) * t**2
         *        + 4 * B * ( Vy*cosP + 2*A*sinP )               * t
         *        + Vy**2 -  4*A*Vx
         *        = 0
         *
         * This is a 4th order polynomial for t.
         * We'll get roots of this, then intersection
         */
        double dAslp; /* A's slope angle */
        double dBslp; /* B's slope angle */
        double eslope; /* difference of slope angles */
        double erc; /* difference of slope angles */
        double ers; /* difference of slope angles */
        Vector2D Ac2Bc; /* vector from A's center to B's vertex */
        Point2D eiBc; /* transformed vertex of B */
        double dTol = parA.getToleranceForDistance();

        // vector from B's center to A's center
        Ac2Bc = parB.position().location().subtract(parA.position().location());

        // inverse rotated point
        eiBc = trans.toLocal(Ac2Bc).toPoint2D();

        // make slope of A & B
        dAslp = Math.atan2(parA.position().x().y(), parA.position().x().x());
        dBslp = Math.atan2(parB.position().x().y(), parB.position().x().x());
        eslope = dBslp - dAslp;
        erc = Math.cos(eslope);
        ers = Math.sin(eslope);

        // make Coefficents of polynomial (real)
        double[] ercoef = new double[5];

        double a = parA.focalDist();
        double b = parB.focalDist();
        double vx = eiBc.x();
        double vy = eiBc.y();

        // make polynomial
        ercoef[4] = (b * ers) * (b * ers);
        ercoef[3] = 4.0 * b * b * ers * erc;
        ercoef[2] = 2.0 * b * (((vy * ers) + (2.0 * b * erc * erc)) -
            (2.0 * a * erc));
        ercoef[1] = 4.0 * b * ((vy * erc) + (2.0 * a * ers));
        ercoef[0] = (vy * vy) - (4.0 * a * vx);

        return ercoef;
    }

    /**
     * ���̉⩂��?�m�̌�_��?�߂�
     *
     * @param parA ��?� : A
     * @param parB ��?� : B
     * @param root ���̉�
     * @param trans DOCUMENT ME!
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_
     */
    private IntersectionPoint2D[] getIntersection(Parabola2D parA,
        Parabola2D parB, Complex[] root,
        CartesianTransformationOperator2D trans, boolean doExchange) {
        double dTol = parA.getToleranceForDistance();
        Vector intervec = new Vector();

        for (int i = 0; i < root.length; i++) {
            double Bparam = root[i].real();

            Point2D dDpnt = trans.toLocal(parB.coordinates(Bparam));
            double Aparam = dDpnt.y() / (2.0 * parA.focalDist());

            // make intersection points
            PointOnCurve2D aPnt = new PointOnCurve2D(parA, Aparam,
                    GeometryElement.doCheckDebug);
            PointOnCurve2D bPnt = new PointOnCurve2D(parB, Bparam,
                    GeometryElement.doCheckDebug);

            // identical check
            if (aPnt.identical(bPnt)) {
                IntersectionPoint2D inter = new IntersectionPoint2D(aPnt, bPnt,
                        GeometryElement.doCheckDebug);

                // duplicate check
                if ((inter != null) && checkUnique(inter, intervec)) {
                    intervec.addElement(inter);
                }
            }
        }

        IntersectionPoint2D[] intersectPoints = vectorToArray(intervec,
                doExchange);

        return intersectPoints;
    }

    /**
     * ��?�m�̌�_��?�߂�
     *
     * @param parA ��?� A
     * @param parB ��?� B
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_
     */
    IntersectionPoint2D[] intersection(Parabola2D parA, Parabola2D parB,
        boolean doExchange) {
        CartesianTransformationOperator2D trans = new CartesianTransformationOperator2D(parA.position(),
                1.0);

        // get root of polynomial
        double[] coefficent = getCoefficent(parA, parB, trans);
        Complex[] root = getRoot(coefficent);

        if (root == null) {
            return new IntersectionPoint2D[0];
        }

        return getIntersection(parA, parB, root, trans, doExchange);
    }
}
