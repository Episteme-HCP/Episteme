/*
 * 2D�̉~??��?�m�̌�_��?�߂�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: IntsCncCnc2D.java,v 1.3 2007-10-21 21:08:14 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

import org.jscience.mathematics.ArrayMathUtils;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.analysis.polynomials.ComplexPolynomial;
import org.jscience.util.FatalException;

import java.util.Enumeration;
import java.util.Vector;

/**
 * 2D�̉~??��?�m�̌�_��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:14 $
 */

abstract class IntsCncCnc2D {

    /**
     * ���̌W?������𓾂�
     *
     * @param eccoef ���̌W?�
     * @return ���̉�
     */
    protected Complex[] getRoot(double[] eccoef) {
        ComplexPolynomial poly;
        Complex[] root;

        // get a complex polynomial
        try {
            poly = new ComplexPolynomial(ArrayMathUtils.toComplex(eccoef));
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }

        // solve equation
        if (poly.degree() < 3) {
            // quadratic case
            root = GeometryPrivateUtils.getRootsIfQuadric(poly);
        } else {
            // other cases
            try {
                root = GeometryPrivateUtils.getRootsByDKA(poly);
            } catch (GeometryPrivateUtils.DKANotConvergeException e) {
                root = e.getValues();
            } catch (GeometryPrivateUtils.ImpossibleEquationException e) {
                throw new FatalException();
            } catch (GeometryPrivateUtils.IndefiniteEquationException e) {
                throw new FatalException();
            }
        }

        return root;
    }

    /**
     * ��_��i�[����Vector��z��ɕϊ�����
     *
     * @param doExchange ��_��pointOnCurve1,2��귂��邩�ǂ���
     * @param intervec   ��_��i�[����Vector
     * @return �l�߂����̌W?�
     */
    protected IntersectionPoint2D[] vectorToArray(Vector intervec,
                                                  boolean doExchange) {
        IntersectionPoint2D[] intersectPoints = new
                IntersectionPoint2D[intervec.size()];
        intervec.copyInto(intersectPoints);
        if (doExchange) {
            for (int i = 0; i < intersectPoints.length; i++)
                intersectPoints[i] = intersectPoints[i].exchange();
        }
        return intersectPoints;
    }

    /**
     * �?�߂���_��?d�����Ă��Ȃ����ǂ����m�F����
     *
     * @param inter    ��_
     * @param intervec ��_�̃x�N�g��
     * @return ?d�����Ȃ���� <code>true</code>,
     *         ����Ȃ��� <code>false<code>
     * @see IntersectionPoint2D
     * @see PointOnCurve2D
     * @see Vector
     */
    protected boolean checkUnique(IntersectionPoint2D inter,
                                  Vector intervec) {
        Enumeration en = intervec.elements();
        double paramA = inter.pointOnCurve1().parameter();
        double paramB = inter.pointOnCurve2().parameter();
        ParametricCurve2D curveA = inter.pointOnCurve1().basisCurve();
        ParametricCurve2D curveB = inter.pointOnCurve2().basisCurve();

        while (en.hasMoreElements()) {
            IntersectionPoint2D obj =
                    (IntersectionPoint2D) en.nextElement();

            PointOnCurve2D pointA = obj.pointOnCurve1();
            if (curveA.identicalParameter(paramA, pointA.parameter()))
                return false;

            PointOnCurve2D pointB = obj.pointOnCurve2();
            if (curveB.identicalParameter(paramB, pointB.parameter()))
                return false;
        }
        return true;
    }
}
