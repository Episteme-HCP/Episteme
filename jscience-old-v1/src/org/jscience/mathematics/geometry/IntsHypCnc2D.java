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

import org.jscience.mathematics.algebraic.numbers.Complex;

import java.util.Vector;


/**
 * 2D�̑o��?�Ɖ~??��?�m�̌�_��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:42 $
 */
class IntsHypCnc2D extends IntsCncCnc2D {
/**
     * Creates a new IntsHypCnc2D object.
     */
    IntsHypCnc2D() {
        super();
    }

    /**
     * �o��?�m�̌�_��?�߂邽�߂̕��⽂Ă�
     *
     * @param hypA �o��?� : A
     * @param hypB �o��?� : B
     * @param trans DOCUMENT ME!
     *
     * @return ���̌W?�
     */
    double[] getCoefficent(Hyperbola2D hypA, Hyperbola2D hypB,
        CartesianTransformationOperator2D trans) {
        /*
         * NOTE:
         *
         * AbstractPoint of inverse transformed of B is
         *
         *        x = cosP * E*coshL - sinP * F*sinhL + Cx    ---> (1)
         *        y = sinP * E*coshL + cosP * F*sinhL + Cy
         *        /  E = distance from center to vertex      \
         *        |  F = b of asymptotic slope b/dB->x_radius|
         *        |  P = ( slope of B ) - ( slope of A )     |
         *        \  C = ( center of B ) - ( center of A )
         *
         * AbstractPoint of A is
         *
         *        x = A * coshT                               ---> (2)
         *        y = B * sinhT
         *
         * (1) & (2) -->
         *
         *          ( (H+I)**2 - J**2 )                       * sinhL**4
         *        + 2 * ( (H+I)*K - J*L )                     * sinhL**3
         *        + ( 2 * (H+I)*(M+I) + K**2 - J**2 - L**2 )  * sinhL**2
         *        + 2 * ( (M+I)*K - J*L )                     * sinhL
         *        + ( (M+I)**2 - L**2 )
         *        = 0
         *
         *        / H = F**2 * ((B*sinP)**2 - (A*cosP)**2)            \
         *        | I = E**2 * ((B*cosP)**2 - (A*sinP)**2)            |
         *        | J = -4 * E * F * cosP * sinP * ( A**2 + B**2 )    |
         *        | K = -2 * F * ( B**2*Cx*sinP + A**2*Cy*cosP )      |
         *        | L = 2 * E * ( B**2*Cx*cosP - A**2*Cy*sinP )       |
         *        \ M = B**2 * Cx**2 - A**2 * Cy**2 - A**2 * B**2
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

        // vector from A's center to B's center
        Ac2Bc = hypB.position().location().subtract(hypA.position().location());

        // inverse rotated point
        eiBc = trans.toLocal(Ac2Bc).toPoint2D();

        // make slope of A & B
        dAslp = Math.atan2(hypA.position().x().y(), hypA.position().x().x());
        dBslp = Math.atan2(hypB.position().x().y(), hypB.position().x().x());
        eslope = dBslp - dAslp;
        erc = Math.cos(eslope);
        ers = Math.sin(eslope);

        double a = hypA.xRadius();
        double b = hypA.yRadius();
        double e = hypB.xRadius();
        double f = hypB.yRadius();
        double cx = eiBc.x();
        double cy = eiBc.y();
        double erc2 = erc * erc;
        double ers2 = ers * ers;
        double eA2 = a * a;
        double eB2 = b * b;

        // make coefficients of polynomial (real)
        double[] eprep = new double[6];
        double[] ercoef = new double[5];

        // make polynomial
        eprep[0] = f * f * ((eB2 * ers2) - (eA2 * erc2)); // H
        eprep[1] = e * e * ((eB2 * erc2) - (eA2 * ers2)); // I
        eprep[2] = (-2.0) * e * f * erc * ers * (eA2 + eB2); // J
        eprep[3] = (-2.0) * f * ((eB2 * cx * ers) + (eA2 * cy * erc)); // K
        eprep[4] = 2.0 * e * ((eB2 * cx * erc) - (eA2 * cy * ers)); // L
        eprep[5] = (eB2 * cx * cx) - (eA2 * cy * cy) - (eA2 * eB2); // M

        ercoef[4] = eprep[0] + eprep[1]; // H+I
        ercoef[0] = eprep[5] + eprep[1]; // M+I
        ercoef[1] = eprep[2] * eprep[4]; // J*L
        ercoef[3] = 2.0 * ((ercoef[4] * eprep[3]) - ercoef[1]); // 2*((H+I)*K-J*L)
        ercoef[1] = 2.0 * ((ercoef[0] * eprep[3]) - ercoef[1]); // 2*((M+I)*K-J*L)
        ercoef[2] = ((2.0 * ercoef[4] * ercoef[0]) + (eprep[3] * eprep[3])) -
            (eprep[2] * eprep[2]) - (eprep[4] * eprep[4]); // 2*(H+I)*(M+I)+K**2-J**2-L**2
        ercoef[4] = (ercoef[4] * ercoef[4]) - (eprep[2] * eprep[2]); // (H+I)**2-J**2
        ercoef[0] = (ercoef[0] * ercoef[0]) - (eprep[4] * eprep[4]); // (M+I)**2-L**2

        return ercoef;
    }

    /**
     * ���̉⩂�o��?�m�̌�_��?�߂�
     *
     * @param hypA �o��?� : A
     * @param hypB �o��?� : B
     * @param ecroot ���̉�
     * @param trans DOCUMENT ME!
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_
     */
    private IntersectionPoint2D[] getIntersection(Hyperbola2D hypA,
        Hyperbola2D hypB, Complex[] ecroot,
        CartesianTransformationOperator2D trans, boolean doExchange) {
        double dTol = hypA.getToleranceForDistance();
        Vector intervec = new Vector();

        for (int i = 0; i < ecroot.length; i++) {
            double sinhB = ecroot[i].real();
            double coshB = Math.sqrt(1.0 + (sinhB * sinhB));
            double bParam = Math.log(sinhB + coshB);

            Point2D dDpnt = trans.toLocal(hypB.coordinates(bParam));
            double sinhA = dDpnt.y() / hypA.yRadius();
            double coshA = Math.sqrt(1.0 + (sinhA * sinhA));
            double aParam = Math.log(sinhA + coshA);

            // make intersection points
            PointOnCurve2D aPnt = new PointOnCurve2D(hypA, aParam,
                    GeometryElement.doCheckDebug);
            PointOnCurve2D bPnt = new PointOnCurve2D(hypB, bParam,
                    GeometryElement.doCheckDebug);

            /* DEBUG
            System.out.println(" aPnt =" + "(" + aPnt.x() +", " + aPnt.y() + ")");
            System.out.println(" bPnt =" + "(" + bPnt.x() +", " + bPnt.y() + ")");
            */

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
     * �o��?�m�̌�_��?�߂�
     *
     * @param hypA �o��?� : A
     * @param hypB �o��?� : B
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_
     */
    IntersectionPoint2D[] intersection(Hyperbola2D hypA, Hyperbola2D hypB,
        boolean doExchange) {
        CartesianTransformationOperator2D trans = new CartesianTransformationOperator2D(hypA.position(),
                1.0);

        // get root of polynomial
        double[] coefficent = getCoefficent(hypA, hypB, trans);
        Complex[] root = getRoot(coefficent);

        if (root == null) {
            return new IntersectionPoint2D[0];
        }

        return getIntersection(hypA, hypB, root, trans, doExchange);
    }
}
