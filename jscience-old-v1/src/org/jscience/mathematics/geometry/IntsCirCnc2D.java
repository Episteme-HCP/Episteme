/*
 * 2D�̉~�Ɖ~??��?�̌�_��?�߂�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: IntsCirCnc2D.java,v 1.5 2006/03/28 23:49:39 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

import org.jscience.mathematics.algebraic.numbers.Complex;

import java.util.Vector;


/**
 * 2D�̉~�Ɖ~??��?�̌�_��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.5 $, $Date: 2006/03/28 23:49:39 $
 */
class IntsCirCnc2D extends IntsCncCnc2D {
/**
     * Creates a new IntsCirCnc2D object.
     */
    IntsCirCnc2D() {
        super();
    }

    /**
     * �~�Ƒȉ~�̌�_��?�߂邽�߂̕��⽂Ă�
     *
     * @param dA �~   : A
     * @param dB �ȉ~ : B
     * @param eAic �ȉ~B��?W�n?�̉~A�̒�?S
     * @param dBlrd �ȉ~�̒����a
     * @param dBsrd �ȉ~�̒Z���a
     *
     * @return ���̌W?�
     */
    private double[] getCoefficent(Circle2D dA, Ellipse2D dB, Point2D eAic,
        double dBlrd, double dBsrd) {
        /*
         * point of inverse transformed A (Circle) is
         *
         *        x = R * cosT + Cx               ---> (1)
         *        y = R * sinT + Cy
         *
         * point of B (Ellispe) is
         *
         *        x**2 / A**2 + y**2 / B**2 = 1   ---> (2)
         *
         * (1) & (2) -->
         *
         *          ( B**2 * ( Cx - R )**2 + A**2 * ( Cy**2 - B**2 ) ) * t**4
         *        + ( 4 * A**2 * Cy * R )                              * t**3
         *        + ( 2* B**2 *(Cx**2-R**2) + 2* A**2 *(Cy**2+2*R**2-B**2)) * t**2
         *        + ( 4 * A**2 * Cy * R ) * t
         *        + ( B**2 * ( Cx + R )**2 + A**2 * ( Cy**2 - B**2 ) )
         *        = 0                            ( t = tan(T/2) )
         *
         * This is a 4th order polynomial for t.
         * We'll get roots of this, then T
         */
        double[] ercoef = new double[5];
        double r = dA.radius();
        double a = dBlrd;
        double b = dBsrd;
        double cx = eAic.x();
        double cy = eAic.y();

        ercoef[4] = (cy * cy) - (b * b);
        ercoef[2] = ercoef[4] + (2.0 * r * r);
        ercoef[4] = a * a * ercoef[4];
        ercoef[0] = ercoef[4];
        ercoef[4] += (b * b * (cx - r) * (cx - r));
        ercoef[0] += (b * b * (cx + r) * (cx + r));
        ercoef[2] = 2.0 * ((a * a * ercoef[2]) +
            (b * b * ((cx * cx) - (r * r))));
        ercoef[3] = 4.0 * a * a * r * cy;
        ercoef[1] = ercoef[3];

        return ercoef;
    }

    /**
     * ���̉⩂�~�Ƒȉ~�ƌ�_��?�߂�
     *
     * @param dA �~   : A
     * @param dB �ȉ~ : B
     * @param erroot ���̉�
     * @param eAic �ȉ~B��?W�n?�ł̉~A�̒�?S
     * @param dBlrd �ȉ~B�̒����a
     * @param dBsrd �ȉ~B�̒Z���a
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_
     */
    private IntersectionPoint2D[] getIntersection(Circle2D dA, Ellipse2D dB,
        Complex[] erroot, Point2D eAic, double dBlrd, double dBsrd,
        boolean doExchange) {
        // make slope angle of A & B
        double dAslp = Math.atan2(dA.position().x().y(), dA.position().x().x());
        double dBslp = Math.atan2(dB.position().x().y(), dB.position().x().x());

        if (dB.xRadius() < dB.yRadius()) {
            dBslp += (Math.PI / 2.0);
        }

        // get intersection points
        Vector intervec = new Vector();

        for (int i = 0; i < erroot.length; i++) {
            // get parameter ( t = tan(T/2) )
            double eTHETA = 2.0 * Math.atan(erroot[i].real());

            double aParam = (eTHETA + dBslp) - dAslp;

            if ((aParam < 0.0) || (aParam > GeometryUtils.PI2)) {
                aParam = GeometryUtils.normalizeAngle(aParam);
            }

            double tPntx = (eAic.x() + (dA.radius() * Math.cos(eTHETA))) / dBlrd;
            double tPnty = (eAic.y() + (dA.radius() * Math.sin(eTHETA))) / dBsrd;

            if (tPntx > 1.0) {
                tPntx = 1.0;
            }

            if (tPntx < (-1.0)) {
                tPntx = (-1.0);
            }

            double bParam = Math.acos(tPntx);

            if (tPnty < 0.0) {
                bParam = (2.0 * Math.PI) - bParam;
            }

            if (dB.xRadius() < dB.yRadius()) {
                bParam += (Math.PI / 2.0);
            }

            if ((bParam < 0.0) || (bParam > (2.0 * Math.PI))) {
                bParam = GeometryUtils.normalizeAngle(bParam);
            }

            // make intersection points
            PointOnCurve2D aPnt = new PointOnCurve2D(dA, aParam,
                    GeometryElement.doCheckDebug);
            PointOnCurve2D bPnt = new PointOnCurve2D(dB, bParam,
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
     * �~�Ƒȉ~�ƌ�_��?�߂�
     *
     * @param dA �~   : A
     * @param dB �ȉ~ : B
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_
     */
    IntersectionPoint2D[] intersection(Circle2D dA, Ellipse2D dB,
        boolean doExchange) {
        double dTol = dA.getToleranceForDistance();
        double dBlrd; /* longer / shorter radius of B */
        double dBsrd; /* longer / shorter radius of B */
        CartesianTransformationOperator2D trans;

        // make longer / shorter radius of B
        if (dB.xRadius() < dB.yRadius()) {
            dBlrd = dB.yRadius();
            dBsrd = dB.xRadius();

            Axis2Placement2D axis = new Axis2Placement2D(dB.position().location(),
                    dB.position().y());
            trans = new CartesianTransformationOperator2D(axis, 1.0);
        } else {
            dBlrd = dB.xRadius();
            dBsrd = dB.yRadius();
            trans = new CartesianTransformationOperator2D(dB.position(), 1.0);
        }

        // vector from B's center to A's center
        Vector2D Bc2Ac = dA.position().location()
                           .subtract(dB.position().location());

        // rough check
        double lengBc2Ac = Bc2Ac.length();

        if (((lengBc2Ac - (dA.radius() + dBlrd)) > dTol) ||
                (((dA.radius() - dBlrd) - lengBc2Ac) > dTol) ||
                (((dBsrd - dA.radius()) - lengBc2Ac) > dTol)) {
            return new IntersectionPoint2D[0];
        }

        // rotated center of A
        Point2D eAic = trans.toLocal(Bc2Ac).toPoint2D();

        // get root of polynomial
        double[] coefficent = getCoefficent(dA, dB, eAic, dBlrd, dBsrd);
        Complex[] erroot = getRoot(coefficent);

        if (erroot == null) {
            return new IntersectionPoint2D[0];
        }

        return getIntersection(dA, dB, erroot, eAic, dBlrd, dBsrd, doExchange);
    }

    /**
     * �~�Ƒo��?�ƌ�_��?�߂邽�߂̕��⽂Ă�
     *
     * @param circle �~     : A
     * @param hyp �o��?� : B
     * @param eiAc DOCUMENT ME!
     *
     * @return ���̌W?�
     */
    private double[] getCoefficent(Circle2D circle, Hyperbola2D hyp,
        Point2D eiAc) {
        /*
         * NOTE:
         *
         * point of inverse transformed A (Circle) is
         *
         *        x = R * cosP + Cx               ---> (1)
         *        y = R * sinP + Cy
         *
         * point of B (Hyperbola) is
         *
         *        x = A * coshT                   ---> (2)
         *        y = B * sinhT
         *
         * (1) & (2) -->
         *
         *          ( A**2 + B**2 )**2                                * sinhT**4
         *        - 4 * B * Cy * ( A**2 + B**2 )                        * sinhT**3
         *        + 2*((A**2+B**2)*(A**2-R**2+Cx**2+Cy**2)
         *                + 2*B**2*Cy**2 - 2*A**2*Cx**2 )                * sinhT**2
         *        - 4 * B * Cy * ( A**2 - R**2 + Cx**2 + Cy**2 )        * sinhT
         *        + ( A**2 - R**2 + Cx**2 + Cy**2 )**2 - 4*A**2*Cx**2
         *        = 0
         *
         * This is a 4th order polynomial for sinhT.
         * We'll get roots of this, then intersection
         */
        double[] ercoef = new double[5];
        double a = hyp.xRadius();
        double b = hyp.yRadius();
        double r = circle.radius();
        double cx = eiAc.x();
        double cy = eiAc.y();

        ercoef[4] = (a * a) + (b * b);
        ercoef[0] = (a * a) - (r * r) + (cx * cx) + (cy * cy);
        ercoef[3] = (-4.0) * b * cy * ercoef[4];
        ercoef[1] = (-4.0) * b * cy * ercoef[0];

        double workX = (-2.0) * a * a * cx * cx;
        double workY = 2.0 * b * b * cy * cy;

        ercoef[2] = 2.0 * ((ercoef[0] * ercoef[4]) + workY + workX);
        ercoef[4] = ercoef[4] * ercoef[4];
        ercoef[0] = (ercoef[0] * ercoef[0]) + (2.0 * workX);

        return ercoef;
    }

    /**
     * ���̉⩂�~�Ƒo��?�ƌ�_��?�߂�
     *
     * @param circle �~     : A
     * @param hyp �o��?� : B
     * @param erroot ���
     * @param eiAc �o��?��?W�n���猩���~�̒�?S
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_
     */
    private IntersectionPoint2D[] getIntersection(Circle2D circle,
        Hyperbola2D hyp, Complex[] erroot, Point2D eiAc, boolean doExchange) {
        double dTol = circle.getToleranceForDistance();

        // make slope angle of A & B
        double dAslp = Math.atan2(circle.position().x().y(),
                circle.position().x().x());
        double dBslp = Math.atan2(hyp.position().x().y(), hyp.position().x().x());

        // get intersection points
        Vector intervec = new Vector();

        for (int i = 0; i < erroot.length; i++) {
            // get parameter
            double sinhB = erroot[i].real();
            double coshB = Math.sqrt(1.0 + (sinhB * sinhB));

            double Ppntx = hyp.xRadius() * coshB;
            double Ppnty = hyp.yRadius() * sinhB;

            if (Math.abs(Ppnty - eiAc.y()) > (circle.radius() + dTol)) {
                continue;
            }

            double sinA = (Ppnty - eiAc.y()) / circle.radius();

            if (sinA > 1.0) {
                sinA = 1.0;
            }

            if (sinA < (-1.0)) {
                sinA = (-1.0);
            }

            double cosA = Math.sqrt(1.0 - (sinA * sinA));
            double xA = eiAc.x() + (circle.radius() * cosA);

            if (Math.abs(Ppntx - xA) > dTol) {
                cosA = (-cosA);
                xA = eiAc.x() + (circle.radius() * cosA);

                if (Math.abs(Ppntx - xA) > dTol) {
                    continue;
                }
            }

            double T = Math.acos(cosA);

            if (sinA < 0.0) {
                T = (2.0 * Math.PI) - T;
            }

            double aParam = (T + dBslp) - dAslp;

            if ((aParam < 0.0) || (aParam > (2.0 * Math.PI))) {
                aParam = GeometryUtils.normalizeAngle(aParam);
            }

            double bParam = Math.log(sinhB + coshB);

            // make intersection points
            PointOnCurve2D aPnt = new PointOnCurve2D(circle, aParam,
                    GeometryElement.doCheckDebug);
            PointOnCurve2D bPnt = new PointOnCurve2D(hyp, bParam,
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
     * �~�Ƒo��?�ƌ�_��?�߂�
     *
     * @param circle �~     : A
     * @param hyp �o��?� : B
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_
     */
    IntersectionPoint2D[] intersection(Circle2D circle, Hyperbola2D hyp,
        boolean doExchange) {
        // vector from B's center to A's center
        Vector2D Bc2Ac = circle.position().location()
                               .subtract(hyp.position().location());

        // rotated center of A
        CartesianTransformationOperator2D trans = new CartesianTransformationOperator2D(hyp.position(),
                1.0);
        Point2D eiAc = trans.toLocal(Bc2Ac).toPoint2D();

        // get root of polynomial
        double[] coefficent = getCoefficent(circle, hyp, eiAc);
        Complex[] erroot = getRoot(coefficent);

        if (erroot == null) {
            return new IntersectionPoint2D[0];
        }

        return getIntersection(circle, hyp, erroot, eiAc, doExchange);
    }

    /**
     * �~�ƕ�?�̌�_��?�߂邽�߂̕��⽂Ă�
     *
     * @param circle �~     : A
     * @param par ��?� : B
     * @param eiAc ��?��?W�n���猩���~�̒�?S
     *
     * @return ���̌W?�
     */
    double[] getCoefficent(Circle2D circle, Parabola2D par, Point2D eiAc) {
        /*
         * NOTE:
         *
         * point of inverse transformed A (Circle) is
         *
         *        x = R * cosT + Cx            ---> (1)
         *        y = R * sinT + Cy
         *
         * point of B (Palabola) is
         *
         *        x = A * t**2                    ---> (2)
         *        y = 2 * A * t
         *
         * (1) & (2) -->
         *
         *          A**2                   * t**4
         *        + 0                      * t**3
         *        + 2* A * ( 2 * A - Cx )  * t**2
         *        - 4 * A * Cy             * t
         *        + Cx**2 + Cy**2 - R**2
         *        = 0
         *
         * This is a 4th order polynomial for t.
         * We'll get roots of this, then intersection
         */
        double[] ercoef = new double[5];
        double a = par.focalDist();
        double cx = eiAc.x();
        double cy = eiAc.y();
        double r = circle.radius();

        ercoef[4] = a * a;
        ercoef[3] = 0.0;
        ercoef[2] = 2.0 * a * ((2.0 * a) - cx);
        ercoef[1] = (-4.0 * a * cy);
        ercoef[0] = ((cx * cx) + (cy * cy)) - (r * r);

        return ercoef;
    }

    /**
     * ���̉⩂�~�ƕ�?�ƌ�_��?�߂�
     *
     * @param circle �~     : A
     * @param par ��?� : B
     * @param erroot ���
     * @param eiAc ��?��?W�n���猩���~�̒�?S
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_
     */
    private IntersectionPoint2D[] getIntersection(Circle2D circle,
        Parabola2D par, Complex[] erroot, Point2D eiAc, boolean doExchange) {
        double dTol = circle.getToleranceForDistance();

        // make slope angle of A & B
        double dAslp = Math.atan2(circle.position().x().y(),
                circle.position().x().x());
        double dBslp = Math.atan2(par.position().x().y(), par.position().x().x());

        // get intersection points
        Vector intervec = new Vector();

        for (int i = 0; i < erroot.length; i++) {
            // get parameter
            double bParam = erroot[i].real();

            double Ppntx = par.focalDist() * bParam * bParam;
            double Ppnty = 2.0 * par.focalDist() * bParam;

            Ppntx = (Ppntx - eiAc.x()) / circle.radius();
            Ppnty = (Ppnty - eiAc.y()) / circle.radius();

            if (Ppntx > 1.0) {
                Ppntx = 1.0;
            }

            if (Ppntx < (-1.0)) {
                Ppntx = (-1.0);
            }

            double aParam = Math.acos(Ppntx);

            if (Ppnty < 0.0) {
                aParam = (2.0 * Math.PI) - aParam;
            }

            aParam = (aParam + dBslp) - dAslp;

            if ((aParam < 0.0) || (aParam > (2.0 * Math.PI))) {
                aParam = GeometryUtils.normalizeAngle(aParam);
            }

            // make intersection points
            PointOnCurve2D aPnt = new PointOnCurve2D(circle, aParam,
                    GeometryElement.doCheckDebug);
            PointOnCurve2D bPnt = new PointOnCurve2D(par, bParam,
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
     * �~�ƕ�?�̌�_��?�߂�
     *
     * @param circle �~     : A
     * @param par ��?� : B
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_
     */
    IntersectionPoint2D[] intersection(Circle2D circle, Parabola2D par,
        boolean doExchange) {
        double dTol = circle.getToleranceForDistance();

        // vector from B's center to A's center
        Vector2D Bc2Ac = circle.position().location()
                               .subtract(par.position().location());

        // rotated center of A
        CartesianTransformationOperator2D trans = new CartesianTransformationOperator2D(par.position(),
                1.0);
        Point2D eiAc = trans.toLocal(Bc2Ac).toPoint2D();

        // rough check
        if ((eiAc.x() + circle.radius()) < (-dTol)) {
            return new IntersectionPoint2D[0];
        }

        // get root of polynomial
        double[] coefficent = getCoefficent(circle, par, eiAc);
        Complex[] erroot = getRoot(coefficent);

        if (erroot == null) {
            return new IntersectionPoint2D[0];
        }

        return getIntersection(circle, par, erroot, eiAc, doExchange);
    }
}
