/*
 * 2D�̑ȉ~�Ɖ~??��?�̌�_��?�߂�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: IntsEllCnc2D.java,v 1.3 2007-10-23 18:19:41 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

import org.jscience.mathematics.algebraic.numbers.Complex;

import java.util.Vector;


/**
 * 2D�̑ȉ~�Ɖ~??��?�̌�_��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:41 $
 */
class IntsEllCnc2D extends IntsCncCnc2D {
    /** DOCUMENT ME! */
    static final double SAFETY_ATOL = 0.001;

    /** DOCUMENT ME! */
    static final double SAFETY_EPS = 0.1;

/**
     * Creates a new IntsEllCnc2D object.
     */
    IntsEllCnc2D() {
        super();
    }

    /**
     * �ȉ~���m�̌�_��?�߂邽�߂̕��⽂Ă�
     *
     * @param ellA �ȉ~ : A
     * @param ellB �ȉ~ : B
     * @param dAlrd A's longer radius
     * @param dAsrd A's shorter radius
     * @param dBlrd B's longer radius
     * @param dBsrd B's shorter radius
     * @param eiBc transformed vertex of B
     * @param erc sin(B's slope - A's slope)
     * @param ers cos(B's slope - A's slope)
     * @param imethod DOCUMENT ME!
     *
     * @return ���̌W?�
     */
    double[] getCoefficent(Ellipse2D ellA, Ellipse2D ellB, double dAlrd,
        double dAsrd, double dBlrd, double dBsrd, Point2D eiBc, double erc,
        double ers, double imethod) {
        /*
         * NOTE:
         *
         * AbstractPoint of inverse transformed of B (Ellipse ellB) is
         *
         *        x = cosP * Ab*cosTb - sinP * Bb*sinTb + Cx    ---> (1)
         *        y = sinP * Ab*cosTb + cosP * Bb*sinTb + Cy
         *
         *        /  Ab = major radius of B                 \
         *        |  Bb = minor radius of B                 |
         *        |  P = ( slope of B ) - ( slope of A )    |
         *        \  C = ( center of B ) - ( center of A )
         *
         * AbstractPoint of A (Ellipse ellA) is
         *
         *        x = Aa * cosTa                                ---> (2)
         *        y = Ba * sinTa
         *
         * (1) & (2) -->
         *
         *          ( (H-I)**2 + J**2 )                         * cosTb**4
         *        + 2 * ( (H-I)*K + J*L )                       * cosTb**3
         *        + ( 2 * (H-I)*(M+I-N) + K**2 + L**2 - J**2 )  * cosTb**2
         *        + 2 * ( (M+I-N)*K - J*L )                     * cosTb
         *        + ( (M+I-N)**2 - L**2 )
         *        = 0
         *
         *        / H = ( cosP*Ab*Ba )**2 + ( sinP*Ab*Aa )**2         \
         *        | I = ( sinP*Bb*Ba )**2 + ( cosP*Bb*Aa )**2         |
         *        | J = 2 * cosP * sinP * Ab * Bb * ( Aa**2 - Ba**2 ) |
         *        | K = ( 2*cosP*Ab*Cx*Ba**2 + 2*sinP*Ab*Cy*Aa**2 )   |
         *        | L = ( 2*cosP*Bb*Cy*Aa**2 - 2*sinP*Bb*Cx*Ba**2 )   |
         *        | M = Cx**2 + Cy**2                                 |
         *        \ N = Aa**2 * Ba**2
         *
         * This is a 4th order polynomial for cosTb.
         * We'll get roots of this, then intersection
         */

        // make polynomial
        double[] eprep = new double[7];
        double[] ercoef = new double[5];

        double aA = dAlrd;
        double bA = dAsrd;
        double aB = dBlrd;
        double bB = dBsrd;
        double cx = eiBc.x();
        double cy = eiBc.y();

        eprep[0] = erc * aB;
        eprep[1] = ers * bB;
        eprep[2] = 2.0 * eprep[0] * eprep[1] * ((aA * aA) - (bA * bA));
        eprep[0] = eprep[0] * bA;
        eprep[1] = eprep[1] * bA;
        eprep[5] = ers * aB * aA;
        eprep[6] = erc * bB * aA;
        eprep[3] = 2.0 * ((eprep[0] * cx * bA) + (eprep[5] * cy * aA));
        eprep[4] = 2.0 * ((eprep[6] * cy * aA) - (eprep[1] * cx * bA));
        eprep[0] = (eprep[0] * eprep[0]) + (eprep[5] * eprep[5]);
        eprep[1] = (eprep[1] * eprep[1]) + (eprep[6] * eprep[6]);
        eprep[5] = (bA * bA * cx * cx) + (aA * aA * cy * cy);
        eprep[6] = aA * aA * bA * bA;

        if (imethod == 1) {
            double ework;
            ework = eprep[0];
            eprep[0] = eprep[1];
            eprep[1] = ework;
            ework = eprep[3];
            eprep[3] = eprep[4];
            eprep[4] = ework;
        }

        ercoef[4] = eprep[0] - eprep[1];
        ercoef[0] = (eprep[5] + eprep[1]) - eprep[6];
        ercoef[1] = eprep[2] * eprep[4];
        ercoef[3] = 2.0 * ((ercoef[4] * eprep[3]) + ercoef[1]);
        ercoef[1] = 2.0 * ((ercoef[0] * eprep[3]) - ercoef[1]);
        ercoef[2] = ((2.0 * ercoef[4] * ercoef[0]) + (eprep[3] * eprep[3]) +
            (eprep[4] * eprep[4])) - (eprep[2] * eprep[2]);
        ercoef[4] = (ercoef[4] * ercoef[4]) + (eprep[2] * eprep[2]);
        ercoef[0] = (ercoef[0] * ercoef[0]) - (eprep[4] * eprep[4]);

        return ercoef;
    }

    /**
     * DOCUMENT ME!
     *
     * @param T DOCUMENT ME!
     * @param Ta DOCUMENT ME!
     * @param n_ints DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean choose_far_solution_1st(double T, double[] Ta, int n_ints) {
        double ediff;

        for (int i = 0; i < n_ints; i++) {
            ediff = Math.abs(T - Ta[i]);

            if ((ediff < SAFETY_ATOL) ||
                    ((GeometryUtils.PI2 - ediff) < SAFETY_ATOL)) {
                return false;
            }
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pB DOCUMENT ME!
     * @param ellA DOCUMENT ME!
     * @param dAlrd DOCUMENT ME!
     * @param dAsrd DOCUMENT ME!
     * @param ellB DOCUMENT ME!
     * @param dBic DOCUMENT ME!
     * @param dBlrd DOCUMENT ME!
     * @param dBsrd DOCUMENT ME!
     * @param erc DOCUMENT ME!
     * @param ers DOCUMENT ME!
     * @param eTb DOCUMENT ME!
     * @param n_ints DOCUMENT ME!
     * @param intervec DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean check_solution(double pB, Ellipse2D ellA, double dAlrd,
        double dAsrd, Ellipse2D ellB, Point2D dBic, double dBlrd, double dBsrd,
        double erc, double ers, double[] eTb, int n_ints, Vector intervec) {
        double pPntx = dBlrd * Math.cos(pB);
        double pPnty = dBsrd * Math.sin(pB);
        double bPntx = (erc * pPntx) - (ers * pPnty) + dBic.x();
        double bPnty = (ers * pPntx) + (erc * pPnty) + dBic.y();

        double cosA = bPntx / dAlrd;

        if (cosA > 1.0) {
            cosA = 1.0;
        }

        if (cosA < (-1.0)) {
            cosA = (-1.0);
        }

        double pA = Math.acos(cosA);

        int i;
        Point2D lbPnt = new CartesianPoint2D(bPntx, bPnty);

        for (i = 0; i < 2; i++) {
            double aPntx = dAlrd * Math.cos(pA);
            double aPnty = dAsrd * Math.sin(pA);
            Point2D aPnt = new CartesianPoint2D(aPntx, aPnty);

            if (aPnt.identical(lbPnt)) {
                break;
            }

            pA = GeometryUtils.PI2 - pA;
        }

        if (i == 2) {
            return false;
        }

        double Aparam = pA;

        if (ellA.xRadius() < ellA.yRadius()) {
            Aparam += (Math.PI / 2.0);
        }

        if ((Aparam < 0.0) || (Aparam > (2.0 * Math.PI))) {
            Aparam = GeometryUtils.normalizeAngle(Aparam);
        }

        double Bparam = pB;

        if (ellB.xRadius() < ellB.yRadius()) {
            Bparam += (Math.PI / 2.0);
        }

        if ((Bparam < 0.0) || (Bparam > (2.0 * Math.PI))) {
            Bparam = GeometryUtils.normalizeAngle(Bparam);
        }

        // make intersection points
        PointOnCurve2D aPnt = new PointOnCurve2D(ellA, Aparam,
                GeometryElement.doCheckDebug);
        PointOnCurve2D bPnt = new PointOnCurve2D(ellB, Bparam,
                GeometryElement.doCheckDebug);

        IntersectionPoint2D inter = new IntersectionPoint2D(aPnt, bPnt,
                GeometryElement.doCheckDebug);

        // duplicate check
        if (checkUnique(inter, intervec)) {
            intervec.addElement(inter);

            return true;
        }

        return false;
    }

    /**
     * ���̉⩂�ȉ~���m�ƌ�_��?�߂�
     *
     * @param ellA �ȉ~ : A
     * @param ellB �ȉ~ : B
     * @param root ���̉�
     * @param eiBc transformed vertex of B
     * @param dAlrd A's longer radius
     * @param dAsrd A's shorter radius
     * @param dBlrd B's longer radius
     * @param dBsrd B's shorter radius
     * @param ers cos(B's slope - A's slope)
     * @param erc sin(B's slope - A's slope)
     * @param imethod
     * @param eanthr DOCUMENT ME!
     * @param doExchange
     *
     * @return ��_
     */
    private IntersectionPoint2D[] getIntersection(Ellipse2D ellA,
        Ellipse2D ellB, Complex[] root, Point2D eiBc, double dAlrd,
        double dAsrd, double dBlrd, double dBsrd, double ers, double erc,
        int imethod, double eanthr, boolean doExchange) {
        int n_real_roots = 0;
        double[] eTb = new double[root.length];

        for (int i = 0; i < root.length; i++) {
            eTb[i] = root[i].real();

            if (Math.abs(eTb[i]) > (1.0 + SAFETY_EPS)) {
                continue;
            }

            if (eTb[i] > 1.0) {
                eTb[i] = 1.0;
            }

            if (eTb[i] < -1.0) {
                eTb[i] = -1.0;
            }

            if (imethod == 0) {
                eTb[n_real_roots] = Math.acos(eTb[i]);
            } else {
                eTb[n_real_roots] = Math.asin(eTb[i]);
            }

            n_real_roots++;
        }

        Vector intervec = new Vector();

        int n_ints = 0;
        int itryTb;

        for (int i = 0; i < n_real_roots; i++) {
            for (itryTb = 0; itryTb < 2; itryTb++) {
                if (choose_far_solution_1st(eTb[i], eTb, n_ints)) {
                    break;
                }

                eTb[i] = eanthr - eTb[i];
            }

            for (; itryTb < 2; itryTb++) {
                if (check_solution(eTb[i], ellA, dAlrd, dAsrd, ellB, eiBc,
                            dBlrd, dBsrd, erc, ers, eTb, n_ints, intervec)) {
                    eTb[n_ints] = eTb[i];
                    n_ints++;

                    break;
                }

                eTb[i] = eanthr - eTb[i];
            }
        }

        IntersectionPoint2D[] intersectPoints = vectorToArray(intervec,
                doExchange);

        return intersectPoints;
    }

    /**
     * �ȉ~���m�̌�_��?�߂�
     *
     * @param ellA �ȉ~ : A
     * @param ellB �ȉ~ : B
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_
     */
    IntersectionPoint2D[] intersection(Ellipse2D ellA, Ellipse2D ellB,
        boolean doExchange) {
        double dAlrd; /* A's longer radius, shorter radius, slope */
        double dAsrd; /* A's longer radius, shorter radius, slope */
        double dAslp; /* A's longer radius, shorter radius, slope */
        double dBlrd; /* B's longer radius, shorter radius, slope */
        double dBsrd; /* B's longer radius, shorter radius, slope */
        double dBslp; /* B's longer radius, shorter radius, slope */
        Vector2D ac2bc; /* vector from A's center to B's vertex */
        Point2D eiBc; /* transformed vertex of B */
        CartesianTransformationOperator2D transA;
        CartesianTransformationOperator2D transB;
        double dTol = ellA.getToleranceForDistance();

        // make longer radius, shorter radius, slope of A & B
        dAlrd = Math.max(ellA.semiAxis1(), ellA.semiAxis2());
        dAsrd = Math.min(ellA.semiAxis1(), ellA.semiAxis2());
        dAslp = Math.atan2(ellA.position().x().y(), ellA.position().x().x());

        if (ellA.xRadius() < ellA.yRadius()) {
            dAslp += (Math.PI / 2.0);
        }

        dBlrd = Math.max(ellB.semiAxis1(), ellB.semiAxis2());
        dBsrd = Math.min(ellB.semiAxis1(), ellB.semiAxis2());
        dBslp = Math.atan2(ellB.position().x().y(), ellB.position().x().x());

        if (ellB.xRadius() < ellB.yRadius()) {
            dBslp += (Math.PI / 2.0);
        }

        // vector from A's center to B's center
        ac2bc = ellB.position().location().subtract(ellA.position().location());

        // rough check
        double length_ac2bc = ac2bc.length();

        if (((Math.abs(dBsrd - (length_ac2bc + dAlrd))) < dTol) ||
                ((Math.abs(dAsrd - (length_ac2bc - dBlrd))) < dTol) ||
                ((Math.abs(length_ac2bc - (dBsrd - dBlrd))) < dTol)) {
            return new IntersectionPoint2D[0];
        }

        // inverse rotated point
        double erotcos = Math.cos(dAslp);
        double erotsin = Math.sin(dAslp);
        double rotateX = (erotcos * ac2bc.x()) + (erotsin * ac2bc.y());
        double rotateY = ((-erotsin) * ac2bc.x()) + (erotcos * ac2bc.y());
        eiBc = new CartesianPoint2D(rotateX, rotateY);

        double eslope = dBslp - dAslp;
        double erc = Math.cos(eslope);
        double ers = Math.sin(eslope);

        int imethod;
        double eanthr;

        if (Math.abs(eiBc.x()) < Math.abs(eiBc.y())) {
            imethod = 0;
            eanthr = 2.0 * Math.PI;
        } else {
            imethod = 1;
            eanthr = Math.PI;
        }

        // get root of polynomial
        double[] coefficent = getCoefficent(ellA, ellB, dAlrd, dAsrd, dBlrd,
                dBsrd, eiBc, erc, ers, imethod);

        Complex[] root = getRoot(coefficent);

        if (root == null) {
            return new IntersectionPoint2D[0];
        }

        return getIntersection(ellA, ellB, root, eiBc, dAlrd, dAsrd, dBlrd,
            dBsrd, ers, erc, imethod, eanthr, doExchange);
    }

    /**
     * �ȉ~�ƕ�?�̌�_��?�߂邽�߂̕��⽂Ă�
     *
     * @param ell �ȉ~   : A
     * @param par ��?� : B
     * @param eiBc transformed vertex of B
     * @param dAlrd A's longer radius
     * @param dAsrd A's shorter radius
     * @param ers cos(B's slope - A's slope)
     * @param erc sin(B's slope - A's slope)
     *
     * @return ���̌W?�
     */
    double[] getCoefficent(Ellipse2D ell, Parabola2D par, Point2D eiBc,
        double dAlrd, double dAsrd, double ers, double erc) {
        /*
         * NOTE:
         *
         * AbstractPoint of inverse transformed of B (Ellipse) is
         *
         *        x = cosP * F * t**2 - sinP * 2*F * t + Vx    ---> (1)
         *        y = sinP * F * t**2 + cosP * 2*F * t + Vy
         *        /  F = dB->focal_dist                     \
         *        |  P = ( slope of B ) - ( slope of A )    |
         *        \  V = ( vertex of B ) - ( center of A )
         *
         * AbstractPoint of A (Parabola) is
         *
         *        x = Aa * cosT                                ---> (2)
         *        y = Ba * sinT
         *
         * (1) & (2) -->
         *
         *          ( A**2 * (F*sinP)**2 + B**2 * (F*cosP)**2 )            * t**4
         *        + 4 * (A**2-B**2) * (F*sinP) * (F*cosP)                  * t**3
         *        + 2 * ( A**2 * ( Vy * (F*sinP) + 2 * (F*cosP)**2 )
         *                + B**2 * ( Vx * (F*cosP) + 2 * (F*sinP)**2 ) )   * t**2
         *        + 4 * ( A**2 * Vy * (F*cosP) - B**2 * Vx * (F*sinP) )    * t
         *        + A**2 * Vy**2 + B**2 * Vx**2 - A**2 * B**2
         *        = 0
         *
         * This is a 4th order polynomial for t.
         * We'll get roots of this, then intersection
         */

        // make coefficients of polynomial (real)
        double[] ercoef = new double[5];

        // make polynomial
        double a = dAlrd;
        double b = dAsrd;
        double a2 = a * a;
        double b2 = b * b;
        double vx = eiBc.x();
        double vy = eiBc.y();
        double efcos = par.focalDist() * erc;
        double efsin = par.focalDist() * ers;

        ercoef[4] = ((a * efsin) * (a * efsin)) + ((b * efcos) * (b * efcos));
        ercoef[3] = 4.0 * (a2 - b2) * efsin * efcos;
        ercoef[2] = 2.0 * ((a2 * ((vy * efsin) + (2.0 * efcos * efcos))) +
            (b2 * ((vx * efcos) + (2.0 * efsin * efsin))));
        ercoef[1] = 4.0 * ((a2 * vy * efcos) - (b2 * vx * efsin));
        ercoef[0] = ((a2 * vy * vy) + (b2 * vx * vx)) - (a2 * b2);

        return ercoef;
    }

    /**
     * ���̉⩂�ȉ~�ƕ�?�̌�_��?�߂�
     *
     * @param ell �ȉ~   : A
     * @param par ��?� : B
     * @param root ���̉�
     * @param eiBc transformed vertex of B
     * @param dAlrd A's longer radius
     * @param dAsrd A's shorter radius
     * @param ers cos(B's slope - A's slope)
     * @param erc sin(B's slope - A's slope)
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_
     */
    private IntersectionPoint2D[] getIntersection(Ellipse2D ell,
        Parabola2D par, Complex[] root, Point2D eiBc, double dAlrd,
        double dAsrd, double ers, double erc, boolean doExchange) {
        double dTol = ell.getToleranceForDistance();
        Vector intervec = new Vector();

        Point2D[] dDpnts = new Point2D[root.length];
        int n_ints = 0;

nextPoint: 
        for (int i = 0; i < root.length; i++) {
            double eTb = root[i].real();

            double dDpnt_x = par.focalDist() * eTb * eTb;
            double dDpnt_y = 2.0 * par.focalDist() * eTb;

            Point2D dDpnt = new CartesianPoint2D(dDpnt_x, dDpnt_y);

            for (int j = 0; j < n_ints; j++) {
                if (dDpnt.identical(dDpnts[j])) {
                    continue nextPoint;
                }
            }

            dDpnts[n_ints] = dDpnt;
            n_ints++;

            //Point2D dDpnt = par.coordinates(eTb);
            double pPntx = (erc * dDpnt.x()) - (ers * dDpnt.y()) + eiBc.x();
            double pPnty = (ers * dDpnt.x()) + (erc * dDpnt.y()) + eiBc.y();
            double cosA = pPntx / dAlrd;

            if (cosA > 1.0) {
                cosA = 1.0;
            }

            if (cosA < (-1.0)) {
                cosA = (-1.0);
            }

            double eTa = Math.acos(cosA);
            double yA = dAsrd * Math.sqrt(1.0 - (cosA * cosA));

            if (Math.abs(yA - pPnty) > dTol) {
                eTa = (2.0 * Math.PI) - eTa;
            }

            double Aparam = eTa;

            if (ell.xRadius() < ell.yRadius()) {
                Aparam += (Math.PI / 2.0);
            }

            if ((Aparam < 0.0) || (Aparam > (2.0 * Math.PI))) {
                Aparam = GeometryUtils.normalizeAngle(Aparam);
            }

            double Bparam = eTb;

            // make intersection points
            PointOnCurve2D aPnt = new PointOnCurve2D(ell, Aparam,
                    GeometryElement.doCheckDebug);
            PointOnCurve2D bPnt = new PointOnCurve2D(par, Bparam,
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
     * �ȉ~�ƕ�?�̌�_��?�߂�
     *
     * @param ell �ȉ~   : A
     * @param par ��?� : B
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_
     */
    IntersectionPoint2D[] intersection(Ellipse2D ell, Parabola2D par,
        boolean doExchange) {
        double dAlrd; /* longer / shorter radius of A */
        double dAsrd; /* longer / shorter radius of A */
        double dAslp; /* A's slope angle */
        double dBslp; /* B's slope angle */
        double eslope; /* difference of slope angles */
        double erc; /* difference of slope angles */
        double ers; /* difference of slope angles */
        Vector2D ac2bc; /* vector from A's center to B's vertex */
        Point2D eiBc; /* transformed vertex of B */
        double dTol = ell.getToleranceForDistance();

        // make longer / shorter radius of A & B
        dAlrd = Math.max(ell.semiAxis1(), ell.semiAxis2());
        dAsrd = Math.min(ell.semiAxis1(), ell.semiAxis2());

        // make slope of A & B
        dAslp = Math.atan2(ell.position().x().y(), ell.position().x().x());

        if (ell.xRadius() < ell.yRadius()) {
            dAslp += (Math.PI / 2.0);
        }

        dBslp = Math.atan2(par.position().x().y(), par.position().x().x());

        // vector from A's center to B's center
        ac2bc = par.position().location().subtract(ell.position().location());

        // inverse rotated point
        double erotcos = Math.cos(dAslp);
        double erotsin = Math.sin(dAslp);
        double rotateX = (erotcos * ac2bc.x()) + (erotsin * ac2bc.y());
        double rotateY = ((-erotsin) * ac2bc.x()) + (erotcos * ac2bc.y());
        eiBc = new CartesianPoint2D(rotateX, rotateY);

        eslope = dBslp - dAslp;
        erc = Math.cos(eslope);
        ers = Math.sin(eslope);

        // get root of polynomial
        double[] coefficent = getCoefficent(ell, par, eiBc, dAlrd, dAsrd, ers,
                erc);
        Complex[] root = getRoot(coefficent);

        if (root == null) {
            return new IntersectionPoint2D[0];
        }

        return getIntersection(ell, par, root, eiBc, dAlrd, dAsrd, ers, erc,
            doExchange);
    }

    /**
     * �ȉ~�Ƒo��?�̌�_��?�߂邽�߂̕��⽂Ă�
     *
     * @param ell �ȉ~   : A
     * @param hyp �o��?� : B
     * @param eiBc transformed vertex of B
     * @param dAlrd A's longer radius
     * @param dAsrd A's shorter radius
     * @param ers cos(B's slope - A's slope)
     * @param erc sin(B's slope - A's slope)
     *
     * @return ���̌W?�
     */
    double[] getCoefficent(Ellipse2D ell, Hyperbola2D hyp, Point2D eiBc,
        double dAlrd, double dAsrd, double ers, double erc) {
        /*
         * NOTE:
         *
         * AbstractPoint of inverse transformed of B (Ellipse) is
         *
         *        x = cosP * E*coshL - sinP * F*sinhL + Cx    ---> (1)
         *        y = sinP * E*coshL + cosP * F*sinhL + Cy
         *        /  E = distance from center to vertex      \
         *        |  F = b of asymptotic slope b/dB->x_radius|
         *        |  P = ( slope of B ) - ( slope of A )     |
         *        \  C = ( center of B ) - ( center of A )
         *
         * AbstractPoint of A (Hyperbola) is
         *
         *        x = A * cosT                                ---> (2)
         *        y = B * sinT
         *
         * (1) & (2) -->
         *
         *          ( (I+H)**2 - J**2 )                       * sinhL**4
         *        + 2 * ( (I+H)*L - J*K )                     * sinhL**3
         *        + ( 2 * (I+H)*(M+H) + L**2 - J**2 - K**2 )  * sinhL**2
         *        + 2 * ( (M+H)*L - J*K )                     * sinhL
         *        + ( (M+H)**2 - J**2 )
         *        = 0
         *
         *        / H = E**2 * ((B*cosP)**2 + (A*sinP)**2)            \
         *        | I = F**2 * ((B*sinP)**2 + (A*cosP)**2)            |
         *        | J = 2 * E * F * cosP * sinP * ( A**2 - B**2 )     |
         *        | K = 2 * E * ( B**2*Cx*cosP + A**2*Cy*sinP )       |
         *        | L = 2 * F * ( A**2*Cy*cosP - B**2*Cx*sinP )       |
         *        \ M = B**2 * Cx**2 + A**2 * Cy**2 - A**2 * B**2
         *
         * This is a 4th order polynomial for sinhL.
         * We'll get roots of this, then intersection
         */

        // make coefficients of polynomial (real)
        double[] eprep = new double[6];
        double[] ercoef = new double[5];

        // make polynomial
        double a = dAlrd;
        double b = dAsrd;
        double a2 = a * a;
        double b2 = b * b;
        double e = hyp.xRadius();
        double f = hyp.yRadius();
        double cx = eiBc.x();
        double cy = eiBc.y();
        double erc2 = erc * erc;
        double ers2 = ers * ers;

        eprep[0] = e * e * ((b2 * erc2) + (a2 * ers2));
        eprep[1] = f * f * ((b2 * ers2) + (a2 * erc2));
        eprep[2] = 2.0 * e * f * erc * ers * (a2 - b2);
        eprep[3] = 2.0 * e * ((b2 * cx * erc) + (a2 * cy * ers));
        eprep[4] = 2.0 * f * ((a2 * cy * erc) - (b2 * cx * ers));
        eprep[5] = ((b2 * cx * cx) + (a2 * cy * cy)) - (a2 * b2);

        ercoef[4] = eprep[1] + eprep[0];
        ercoef[0] = eprep[5] + eprep[0];
        ercoef[1] = eprep[2] * eprep[3];
        ercoef[3] = 2.0 * ((ercoef[4] * eprep[4]) - ercoef[1]);
        ercoef[1] = 2.0 * ((ercoef[0] * eprep[4]) - ercoef[1]);
        ercoef[2] = ((2.0 * ercoef[4] * ercoef[0]) + (eprep[4] * eprep[4])) -
            (eprep[2] * eprep[2]) - (eprep[3] * eprep[3]);
        ercoef[4] = (ercoef[4] * ercoef[4]) - (eprep[2] * eprep[2]);
        ercoef[0] = (ercoef[0] * ercoef[0]) - (eprep[3] * eprep[3]);

        return ercoef;
    }

    /**
     * ���̉⩂�ȉ~�Ƒo��?�̌�_��?�߂�
     *
     * @param ell �ȉ~   : A
     * @param hyp �o��?� : B
     * @param root ���̉�
     * @param eiBc transformed vertex of B
     * @param dAlrd A's longer radius
     * @param dAsrd A's shorter radius
     * @param ers cos(B's slope - A's slope)
     * @param erc sin(B's slope - A's slope)
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_
     */
    private IntersectionPoint2D[] getIntersection(Ellipse2D ell,
        Hyperbola2D hyp, Complex[] root, Point2D eiBc, double dAlrd,
        double dAsrd, double ers, double erc, boolean doExchange) {
        double dTol = ell.getToleranceForDistance();
        Vector intervec = new Vector();
        int j;

        for (int i = 0; i < root.length; i++) {
            double sinhB = root[i].real();
            double coshB = Math.sqrt(1.0 + (sinhB * sinhB));

            double pPntx = hyp.xRadius() * coshB;
            double pPnty = hyp.yRadius() * sinhB;
            double bPntx = (erc * pPntx) - (ers * pPnty) + eiBc.x();
            double bPnty = (ers * pPntx) + (erc * pPnty) + eiBc.y();

            double cosA = bPntx / dAlrd;

            if (cosA > 1.0) {
                cosA = 1.0;
            }

            if (cosA < (-1.0)) {
                cosA = (-1.0);
            }

            double pA = Math.acos(cosA);

            Point2D laPnt;
            Point2D lbPnt = new CartesianPoint2D(bPntx, bPnty);

            for (j = 0; j < 2; j++) {
                laPnt = new CartesianPoint2D(dAlrd * Math.cos(pA),
                        dAsrd * Math.sin(pA));

                if (laPnt.identical(lbPnt)) {
                    break;
                }

                pA = GeometryUtils.PI2 - pA;
            }

            if (j == 2) {
                continue;
            }

            //if (Math.abs(pA - pPnty) > dTol) {
            //pA = 2.0 * Math.PI - pA;
            //}
            double Aparam = pA;

            if (ell.xRadius() < ell.yRadius()) {
                Aparam += (Math.PI / 2.0);
            }

            if ((Aparam < 0.0) || (Aparam > (2.0 * Math.PI))) {
                Aparam = GeometryUtils.normalizeAngle(Aparam);
            }

            double Bparam = Math.log(sinhB + coshB);

            // make intersection points
            PointOnCurve2D aPnt = new PointOnCurve2D(ell, Aparam,
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
     * �ȉ~�Ƒo��?�̌�_��?�߂�
     *
     * @param ell �ȉ~   : A
     * @param hyp �o��?� : B
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_
     */
    IntersectionPoint2D[] intersection(Ellipse2D ell, Hyperbola2D hyp,
        boolean doExchange) {
        double dAlrd; /* longer / shorter radius of A */
        double dAsrd; /* longer / shorter radius of A */
        double dAslp; /* A's slope angle */
        double dBslp; /* B's slope angle */
        double eslope; /* difference of slope angles */
        double erc; /* difference of slope angles */
        double ers; /* difference of slope angles */
        Vector2D ac2bc; /* vector from A's center to B's vertex */
        Point2D eiBc; /* transformed vertex of B */
        double dTol = ell.getToleranceForDistance();

        // make longer / shorter radius of A & B
        dAlrd = Math.max(ell.semiAxis1(), ell.semiAxis2());
        dAsrd = Math.min(ell.semiAxis1(), ell.semiAxis2());

        // make slope of A & B
        dAslp = Math.atan2(ell.position().x().y(), ell.position().x().x());

        if (ell.xRadius() < ell.yRadius()) {
            dAslp += (Math.PI / 2.0);
        }

        dBslp = Math.atan2(hyp.position().x().y(), hyp.position().x().x());

        // vector from A's center to B's center
        ac2bc = hyp.position().location().subtract(ell.position().location());

        // inverse rotated point
        double erotcos = Math.cos(dAslp);
        double erotsin = Math.sin(dAslp);
        double rotateX = (erotcos * ac2bc.x()) + (erotsin * ac2bc.y());
        double rotateY = ((-erotsin) * ac2bc.x()) + (erotcos * ac2bc.y());
        eiBc = new CartesianPoint2D(rotateX, rotateY);

        eslope = dBslp - dAslp;
        erc = Math.cos(eslope);
        ers = Math.sin(eslope);

        // get root of polynomial
        double[] coefficent = getCoefficent(ell, hyp, eiBc, dAlrd, dAsrd, ers,
                erc);
        Complex[] root = getRoot(coefficent);

        if (root == null) {
            return new IntersectionPoint2D[0];
        }

        return getIntersection(ell, hyp, root, eiBc, dAlrd, dAsrd, ers, erc,
            doExchange);
    }
}
