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

package org.jscience.mathematics.geometry;

import org.jscience.mathematics.analysis.polynomials.DoublePolynomial;

import org.jscience.util.FatalException;

import java.util.Vector;


/**
 * 2D�̒�?�Ɖ~??��?�̌�_��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:42 $
 */
class IntsLinCnc2D {
    /** DOCUMENT ME! */
    private Line2D line;

    /** DOCUMENT ME! */
    private Conic2D conic;

/**
     * Creates a new IntsLinCnc2D object.
     *
     * @param line  DOCUMENT ME!
     * @param conic DOCUMENT ME!
     */
    IntsLinCnc2D(Line2D line, Conic2D conic) {
        super();

        this.line = line;
        this.conic = conic;
    }

    /**
     * 2�����̌W?������𓾂�
     *
     * @param coef 2�����̌W?�
     *
     * @return 2�����̉�
     *
     * @throws FatalException DOCUMENT ME!
     */
    private double[] getRoot(double[] coef) {
        DoublePolynomial poly;

        try {
            poly = new DoublePolynomial(coef);
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }

        // solve quadratic equation
        double[] root = GeometryPrivateUtils.getRootsIfQuadric(poly);

        return root;
    }

    /**
     * 2�����̉⩂�?d�����?�?�����
     *
     * @param line DOCUMENT ME!
     * @param root 2�����̌W
     *
     * @return ?d����̖���2�����̉�
     */
    private double[] checkDuplicate(Line2D line, double[] root) {
        double dTol2 = line.getToleranceForDistance2();
        double[] realRoot;

        if (root == null) { // for no root

            return null;
        }

        if (root.length == 2) { // for duplicate roots

            double diff = Math.abs(root[1] - root[0]);

            if ((diff * diff * line.dir().norm()) < dTol2) {
                realRoot = new double[1];
                realRoot[0] = root[0];
            } else {
                realRoot = root;
            }
        } else {
            realRoot = root;
        }

        return realRoot;
    }

    /**
     * 2�����̉⩂��_��?�߂�
     *
     * @param line ��?�
     * @param conic �~??��?�
     * @param root 2�����̌W
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_�̔z��
     */
    private IntersectionPoint2D[] getIntersect(Line2D line, Conic2D conic,
        double[] root, boolean doExchange) {
        // get intersections
        Vector intervec = new Vector();

        for (int i = 0; i < root.length; i++) {
            // get parameter
            double eApara = root[i];
            Point2D etmp = line.coordinates(eApara);
            double eBpara;

            try {
                eBpara = conic.pointToParameter(etmp);
            } catch (InvalidArgumentValueException e) {
                continue;
            }

            // make intersection points
            PointOnCurve2D pntA = new PointOnCurve2D(line, eApara,
                    GeometryElement.doCheckDebug);
            PointOnCurve2D pntB = new PointOnCurve2D(conic, eBpara,
                    GeometryElement.doCheckDebug);
            IntersectionPoint2D inter;

            if (!doExchange) {
                inter = new IntersectionPoint2D(pntB, pntA, pntB,
                        GeometryElement.doCheckDebug);
            } else {
                inter = new IntersectionPoint2D(pntB, pntB, pntA,
                        GeometryElement.doCheckDebug);
            }

            if ((inter != null) && pntA.identical(pntB)) { // identical check
                intervec.addElement(inter);
            }
        }

        IntersectionPoint2D[] intersectPoints = new IntersectionPoint2D[intervec.size()];
        intervec.copyInto(intersectPoints);

        return intersectPoints;
    }

    /**
     * ��?�Ɖ~�̌�_��?�߂邽�߂�2�����̌W?��𓾂�
     *
     * @param line ��?�
     * @param circle �~
     *
     * @return 2�����̌W?�
     */
    private double[] getCoef(Line2D line, Circle2D circle) {
        /*
         * NOTE:
         *
         * equation of intersection are
         *
         *        x = Px + u * Vx  =  r * cosT
         *        y = Py + u * Vy  =  r * sinT
         *
         * , so a polynomial of u is
         *
         *        A0 * u**2 + A1 * u + A2 = 0
         *
         *                A0 = Vx**2 + Vy**2
         *                A1 = 2*(Px*Vx +Py*Vy)
         *                A2 = Px**2 + Py**2 - r**2
         */
        double[] ercoef = new double[3];

        Vector2D eApnt = line.pnt().subtract(circle.position().location());

        ercoef[2] = (line.dir().x() * line.dir().x()) +
            (line.dir().y() * line.dir().y());
        ercoef[1] = 2.0 * ((eApnt.x() * line.dir().x()) +
            (eApnt.y() * line.dir().y()));
        ercoef[0] = ((eApnt.x() * eApnt.x()) + (eApnt.y() * eApnt.y())) -
            (circle.radius() * circle.radius());

        return ercoef;
    }

    /**
     * ��?�Ɖ~�̌�_��?�߂�
     *
     * @param line ��?�
     * @param circle �~
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_
     *
     * @see IntersectionPoint2D
     */
    IntersectionPoint2D[] intersection(Line2D line, Circle2D circle,
        boolean doExchange) {
        double[] ercoef = getCoef(line, circle);
        double[] root = getRoot(ercoef); // solving equation

        if (root == null) { // for no root

            return new IntersectionPoint2D[0];
        }

        double[] realRoot = checkDuplicate(line, root); // duplicate check

        return getIntersect(line, circle, realRoot, doExchange);
    }

    /**
     * ��?�Ƒȉ~�̌�_��?�߂邽�߂�2�����̌W?��𓾂�
     *
     * @param line ��?�
     * @param ell �ȉ~
     *
     * @return 2�����̌W?�
     *
     * @throws FatalException DOCUMENT ME!
     */
    private double[] getCoef(Line2D line, Ellipse2D ell) {
        /*
         * NOTE:
         *
         * equation of intersection is
         *
         *        x**2/A**2 + y**2/B**2 = 1   ( A = dBlrd, B = dBsrd )
         *                x = Px + t * Vx
         *                y = Py + t * Vy
         *
         * , so a polynomial of cosT is
         *
         *        A0 * t2 + 2 * A1 * t + A2 = 0
         *                ( A0 = A**2 * Vy**2 + B**2 * Vx**2 )
         *                ( A1 = 2 * ( A**2 * Py * Vy + B**2 * Px * Vx ) )
         *                ( A2 = A**2 * Py**2 + B**2 * Px**2 - A**2 * B**2 )
         */
        double dBlrd; /* longer / shorter radius of B */

        /*
         * NOTE:
         *
         * equation of intersection is
         *
         *        x**2/A**2 + y**2/B**2 = 1   ( A = dBlrd, B = dBsrd )
         *                x = Px + t * Vx
         *                y = Py + t * Vy
         *
         * , so a polynomial of cosT is
         *
         *        A0 * t2 + 2 * A1 * t + A2 = 0
         *                ( A0 = A**2 * Vy**2 + B**2 * Vx**2 )
         *                ( A1 = 2 * ( A**2 * Py * Vy + B**2 * Px * Vx ) )
         *                ( A2 = A**2 * Py**2 + B**2 * Px**2 - A**2 * B**2 )
         */
        double dBsrd; /* longer / shorter radius of B */
        CartesianTransformationOperator2D trans;

        if (ell.semiAxis1() < ell.semiAxis2()) {
            dBlrd = ell.semiAxis2();
            dBsrd = ell.semiAxis1();

            try {
                Axis2Placement2D axis = new Axis2Placement2D(ell.position()
                                                                .location(),
                        ell.position().y());
                trans = new CartesianTransformationOperator2D(axis, 1.0);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
        } else {
            dBlrd = ell.semiAxis1();
            dBsrd = ell.semiAxis2();

            try {
                trans = new CartesianTransformationOperator2D(ell.position(),
                        1.0);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
        }

        // vector from B's center to A
        Vector2D Bc2A = line.pnt().subtract(ell.position().location());

        // inverse rotated point
        Vector2D eAirpnt = trans.toLocal(Bc2A);
        Vector2D eAirvec = trans.toLocal(line.dir());

        // make polynomial
        double eA2 = dBlrd * dBlrd;
        double eB2 = dBsrd * dBsrd;

        // coefficients of polynomial (real)
        double[] ercoef = new double[3];

        ercoef[2] = (eA2 * eAirvec.y() * eAirvec.y()) +
            (eB2 * eAirvec.x() * eAirvec.x());
        ercoef[1] = 2.0 * ((eA2 * eAirpnt.y() * eAirvec.y()) +
            (eB2 * eAirpnt.x() * eAirvec.x()));
        ercoef[0] = ((eA2 * eAirpnt.y() * eAirpnt.y()) +
            (eB2 * eAirpnt.x() * eAirpnt.x())) - (eA2 * eB2);

        return ercoef;
    }

    /**
     * ��?�Ƒȉ~�̌�_��?�߂�
     *
     * @param line ��?�
     * @param ell �ȉ~
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_
     *
     * @see IntersectionPoint2D
     */
    IntersectionPoint2D[] intersection(Line2D line, Ellipse2D ell,
        boolean doExchange) {
        double[] ercoef = getCoef(line, ell);
        double[] root = getRoot(ercoef); // solving equation

        if (root == null) { // for no root

            return new IntersectionPoint2D[0];
        }

        double[] realRoot = checkDuplicate(line, root); // duplicate check

        return getIntersect(line, ell, realRoot, doExchange);
    }

    /**
     * ��?�ƕ�?�̌�_��?�߂邽�߂�2�����̌W?��𓾂�
     *
     * @param line ��?�
     * @param par ��?�
     *
     * @return 2�����̌W?�
     *
     * @throws FatalException DOCUMENT ME!
     */
    private double[] getCoef(Line2D line, Parabola2D par) {
        /*
         * NOTE:
         *
         * equation of intersection is
         *
         *        x = Px + u * Vx  =  a * t**2
         *        y = Py + u * Vy  =  2 * a * t
         *
         * , so a polynomial of u is
         *
         *        Vy**2 * u**2 + 2(Py*Vy - 2*a*Vx) * u + Py**2 - 4*a*Px = 0
         */
        CartesianTransformationOperator2D trans;

        try {
            trans = new CartesianTransformationOperator2D(par.position(), 1.0);
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }

        // vector from B's center to A
        Vector2D Bc2A = line.pnt().subtract(par.position().location());

        // inverse rotated point
        Vector2D eAirpnt = trans.reverseTransform(Bc2A);
        Vector2D eAirvec = trans.reverseTransform(line.dir());

        // coefficients of polynomial (real)
        double[] ercoef = new double[3];

        // make polynomial
        ercoef[2] = eAirvec.y() * eAirvec.y();
        ercoef[1] = 2.0 * ((eAirpnt.y() * eAirvec.y()) -
            (2.0 * par.focalDist() * eAirvec.x()));
        ercoef[0] = (eAirpnt.y() * eAirpnt.y()) -
            (4.0 * par.focalDist() * eAirpnt.x());

        return ercoef;
    }

    /**
     * ��?�ƕ�?�̌�_��?�߂�
     *
     * @param line ��?�
     * @param par ��?�
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_
     *
     * @see IntersectionPoint2D
     */
    IntersectionPoint2D[] intersection(Line2D line, Parabola2D par,
        boolean doExchange) {
        double[] ercoef = getCoef(line, par);
        double[] root = getRoot(ercoef); // solving equation

        if (root == null) { // for no root

            return new IntersectionPoint2D[0];
        }

        double[] realRoot = checkDuplicate(line, root); // duplicate check

        return getIntersect(line, par, realRoot, doExchange);
    }

    /**
     * ��?�ƕ�?�̌�_��?�߂邽�߂�2�����̌W?��𓾂�
     *
     * @param line ��?�
     * @param hyp ��?�
     *
     * @return 2�����̌W?�
     *
     * @throws FatalException DOCUMENT ME!
     */
    private double[] getCoef(Line2D line, Hyperbola2D hyp) {
        /*
         * NOTE:
         *
         * equation of intersection are
         *
         *        x = Px + u * Vx  =  a * coshT
         *        y = Py + u * Vy  =  b * sinhT
         *
         * , so the polynomial of sinhT is
         *
         *        A0 * sinhT**2 + A1 * sinhT + A2 = 0
         *                A0 = (a*Vy)**2 - (b*Vx)**2
         *                A1 = -2*(b*Vx)*(Px*Vy - Py*Vx )
         *                A2 = (a*Vy)**2 - (Px*Vy - Py*Vx )**2
         *
         * , and the polynomial of u is
         *
         *        A0 * u**2 + A1 * u + A2 = 0
         *                A0 = (b*Vx)**2  - (a*Vy)**2
         *                A1 = 2 * (b**2 * Px * Vx - a**2 * Py * Vy)
         *                A2 = (b**2 * Px**2 - a**2 * Py**2) - a**2 * b**2
         */
        CartesianTransformationOperator2D trans;

        try {
            trans = new CartesianTransformationOperator2D(hyp.position(), 1.0);
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }

        // vector from B's center to A
        Vector2D Bc2A = line.pnt().subtract(hyp.position().location());

        // inverse rotated point
        Vector2D eAirpnt = trans.reverseTransform(Bc2A);
        Vector2D eAirvec = trans.reverseTransform(line.dir());

        // make polynomial
        double eA2 = hyp.semiAxis() * hyp.semiAxis();
        double eB2 = hyp.semiImagAxis() * hyp.semiImagAxis();

        // coefficients of polynomial (real)
        double[] ercoef = new double[3];

        ercoef[2] = (eB2 * eAirvec.x() * eAirvec.x()) -
            (eA2 * eAirvec.y() * eAirvec.y());
        ercoef[1] = 2.0 * ((eB2 * eAirpnt.x() * eAirvec.x()) -
            (eA2 * eAirpnt.y() * eAirvec.y()));
        ercoef[0] = (eB2 * eAirpnt.x() * eAirpnt.x()) -
            (eA2 * eAirpnt.y() * eAirpnt.y()) - (eA2 * eB2);

        return ercoef;
    }

    /**
     * ��?�ƕ�?�̌�_��?�߂�
     *
     * @param line ��?�
     * @param hyp ��?�
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_
     *
     * @see IntersectionPoint2D
     */
    IntersectionPoint2D[] intersection(Line2D line, Hyperbola2D hyp,
        boolean doExchange) {
        double[] ercoef = getCoef(line, hyp);
        double[] root = getRoot(ercoef); // solving equation

        if (root == null) { // for no root

            return new IntersectionPoint2D[0];
        }

        double[] realRoot = checkDuplicate(line, root); // duplicate check

        return getIntersect(line, hyp, realRoot, doExchange);
    }
}
