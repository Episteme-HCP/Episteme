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

import org.jscience.mathematics.ArrayMathUtils;
import org.jscience.mathematics.MachineEpsilon;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.analysis.PrimitiveMapping;
import org.jscience.mathematics.analysis.polynomials.ComplexPolynomial;
import org.jscience.mathematics.analysis.polynomials.DoublePolynomial;
import org.jscience.util.FatalException;

import java.io.PrintWriter;

/**
 * �Q���� : �ȉ~��\���N���X?B
 * <p/>
 * �ȉ~��?A���̒�?S�̈ʒu�Ƌ�?� X/Y ���̕�����?�?W�n
 * (�z�u?��?A{@link Axis2Placement2D Axis2Placement2D}) position ��
 * ��?� X �����̔��a semiAxis1?A
 * ��?� Y �����̔��a semiAxis2
 * �Œ�`�����?B
 * </p>
 * <p/>
 * t ��p���??[�^�Ƃ���ȉ~ P(t) �̃p���?�g���b�N�\����?A�ȉ��̒ʂ�?B
 * <pre>
 * 	P(t) = position.location()
 * 	     + semiAxis1 * cos(t) * position.x()
 * 	     + semiAxis2 * sin(t) * position.y()
 * </pre>
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.8 $, $Date: 2006/05/20 23:25:41 $
 */

public class Ellipse2D extends Conic2D {

    /**
     * ���a1 (��?�?W�n��X�����̔��a) ?B
     *
     * @serial
     */
    private double semiAxis1;

    /**
     * ���a2 (��?�?W�n��Y�����̔��a) ?B
     *
     * @serial
     */
    private double semiAxis2;

    /**
     * ��̔��a�̒l��?A�����ێ?����t�B?[���h��?ݒ肷��?B
     * <p/>
     * semiAxis1, semiAxis2 �̒l��?��łȂ���΂Ȃ�Ȃ�?B
     * </p>
     * <p/>
     * semiAxis1, semiAxis2 �̂����ꂩ�̒l��
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?����?�����?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     *
     * @param semiAxis1 ���a1
     * @param semiAxis2 ���a2
     * @see InvalidArgumentValueException
     */
    private void setSemiAxis(double semiAxis1, double semiAxis2) {
        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double dTol = condition.getToleranceForDistance();

        if (semiAxis1 < dTol) {
            throw new InvalidArgumentValueException();
        }
        this.semiAxis1 = semiAxis1;

        if (semiAxis2 < dTol) {
            throw new InvalidArgumentValueException();
        }
        this.semiAxis2 = semiAxis2;
    }

    /**
     * ��?�?W�n�Ɗe���ɑ΂��锼�a��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * position �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * semiAxis1, semiAxis2 �̂����ꂩ�̒l��
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?����?�����?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     *
     * @param position  ��?S�Ƌ�?� X/Y ���̕�����?�?W�n
     * @param semiAxis1 ���a1 (��?� X ���ɑ΂��锼�a)
     * @param semiAxis2 ���a2 (��?� Y ���ɑ΂��锼�a)
     * @see InvalidArgumentValueException
     */
    public Ellipse2D(Axis2Placement2D position,
                     double semiAxis1, double semiAxis2) {
        super(position);
        setSemiAxis(semiAxis1, semiAxis2);
    }

    /**
     * ���̑ȉ~�̔��a1 (��?�?W�n�� X �����̔��a) ��Ԃ�?B
     *
     * @return ���a1
     */
    public double semiAxis1() {
        return this.semiAxis1;
    }

    /**
     * {@link #semiAxis1() semiAxis1()} �̕ʖ��?�\�b�h?B
     *
     * @return ���a1
     */
    public double xRadius() {
        return this.semiAxis1;
    }

    /**
     * ���̑ȉ~�̔��a2 (��?�?W�n�� Y �����̔��a) ��Ԃ�?B
     *
     * @return ���a2
     */
    public double semiAxis2() {
        return this.semiAxis2;
    }

    /**
     * {@link #semiAxis2() semiAxis2()} �̕ʖ��?�\�b�h?B
     *
     * @return ���a2
     */
    public double yRadius() {
        return this.semiAxis2;
    }

    /**
     * �^����ꂽ�p���??[�^��Ԃɂ����邱�̋�?�̎��?�ł̒��� (���̂�) ��Ԃ�?B
     * <p/>
     * pint �ŗ^�������Ԃ� [0, 2 * PI] �Ɏ�܂BĂ���K�v�͂Ȃ�?B
     * �܂�?Apint �̑?���l�͕��ł©�܂�Ȃ�?B
     * </p>
     *
     * @param pint ��?�̒�����?�߂�p���??[�^���
     * @return �w�肳�ꂽ�p���??[�^��Ԃɂ������?�̒���
     */
    public double length(ParameterSection pint) {
        final double m2eal_majrd2 = semiAxis1() * semiAxis1();
        final double m2eal_minrd2 = semiAxis2() * semiAxis2();
        double dTol = getToleranceForDistance() / 2.0;

        return GeometryUtils.getDefiniteIntegral
                (new PrimitiveMapping() {
                    /**
                     * Evaluates this function.
                     */
                    public double map(float x) {
                        return map((double) x);
                    }

                    /**
                     * Evaluates this function.
                     */
                    public double map(long x) {
                        return map((double) x);
                    }

                    /**
                     * Evaluates this function.
                     */
                    public double map(int x) {
                        return map((double) x);
                    }

                    public double map(double parameter) {
                        double ecos = Math.cos(parameter);
                        double esin = Math.sin(parameter);

                        return Math.sqrt(m2eal_majrd2 * esin * esin +
                                m2eal_minrd2 * ecos * ecos);
                    }
                },
                        pint, dTol);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ?W�l
     */
    public Point2D coordinates(double param) {
        param = parameterDomain().wrap(param);
        Axis2Placement2D ax = position();
        Vector2D ex = ax.x().multiply(Math.cos(param) * semiAxis1);
        Vector2D ey = ax.y().multiply(Math.sin(param) * semiAxis2);

        return ax.location().add(ex.add(ey));
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ?ڃx�N�g��
     */
    public Vector2D tangentVector(double param) {
        param = parameterDomain().wrap(param);
        Axis2Placement2D ax = position();
        Vector2D ex1 = ax.x().multiply(-Math.sin(param) * semiAxis1);
        Vector2D ey1 = ax.y().multiply(Math.cos(param) * semiAxis2);

        return ex1.add(ey1);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̋ȗ���Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return �ȗ�
     */
    public CurveCurvature2D curvature(double param) {
        param = parameterDomain().wrap(param);
        Axis2Placement2D ax = position();
        double xlen = Math.cos(param) * semiAxis1;
        double ylen = Math.sin(param) * semiAxis2;
        double x1len = -Math.sin(param) * semiAxis1;
        double y1len = Math.cos(param) * semiAxis2;
        double plen = Math.sqrt(x1len * x1len + y1len * y1len);
        double crv = Math.abs(x1len * ylen - y1len * xlen)
                / (plen * plen * plen);
        Vector2D ex1 = ax.x().multiply(x1len);
        Vector2D ey1 = ax.y().multiply(y1len);

        Vector2D tangent = ex1.add(ey1);
        // rotate tangent PI/2
        Vector2D nrmDir =
                new LiteralVector2D(-tangent.y(), tangent.x());
        return new CurveCurvature2D(crv, nrmDir.unitized());
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̓���?���Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ����?�
     */
    public CurveDerivative2D evaluation(double param) {
        param = parameterDomain().wrap(param);
        Axis2Placement2D ax = position();
        Vector2D ex = ax.x().multiply(Math.cos(param) * semiAxis1);
        Vector2D ey = ax.y().multiply(Math.sin(param) * semiAxis2);
        Vector2D ex1 = ax.x().multiply(-Math.sin(param) * semiAxis1);
        Vector2D ey1 = ax.y().multiply(Math.cos(param) * semiAxis2);

        Point2D d0 = ax.location().add(ex.add(ey));
        Vector2D d1 = ex1.add(ey1);
        Vector2D d2 = ex.add(ey).multiply(-1.0);

        return new CurveDerivative2D(d0, d1, d2);
    }

    /**
     * �^����ꂽ�_���炱�̋�?�ւ̓��e�_��?�߂�?B
     * <p/>
     * ���̑ȉ~���~�`?� (�e���ɑ΂��锼�a��������) ��?A
     * �^����ꂽ�_�Ƃ��̑ȉ~�̒�?S�Ƃ̋�����?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?�����
     * ?�����?�?��ɂ�?A
     * �p���??[�^�l 0 �̓_�� suitable �Ƃ���
     * IndefiniteSolutionException �̗�O�𓊂���?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ����l���̑�?�����?���?�߂邱�ƂɋA��������?A��?��I�ɉ⢂Ă���?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_
     * @throws IndefiniteSolutionException �⪕s�� (���e���̓_���~?�̑ȉ~�̒�?S�Ɉ�v����)
     */
    public PointOnCurve2D[] projectFrom(Point2D point)
            throws IndefiniteSolutionException {
        double dTol2 = getToleranceForDistance2();

        /*
        * NOTE:
        *
        * equation of normal line is
        *
        *	A*Px/cosT - B*Py/sinT = A**2 - B**2    ( A=dBlrd, B=dBsrd ),
        *
        * so a polynomial of cosT is
        *
        *   -F**2 * z4 + 2DF * z3 + (F**2-D**2-E**2) * z2 - 2DF * z1 + D**2
        *        ( z = cosT, D = A*Px, E = B*Py, F = A**2-B**2 ).
        */

        double dBlrd, dBsrd;    /* longer / shorter radius of B */
        CartesianTransformationOperator2D trans;

        if (semiAxis1 < semiAxis2) {
            dBlrd = semiAxis2;
            dBsrd = semiAxis1;
            try {
                Axis2Placement2D ax =
                        new Axis2Placement2D(position().location(), position().y());
                trans = new CartesianTransformationOperator2D(ax, 1.0);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
        } else {
            dBlrd = semiAxis1;
            dBsrd = semiAxis2;
            try {
                trans = new CartesianTransformationOperator2D(position(), 1.0);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
        }

        // vector from B's center to A
        Vector2D Bc2A = point.subtract(position().location());

        if ((semiAxis1 - semiAxis2) * (semiAxis1 - semiAxis2) < dTol2 &&
                Bc2A.norm() < dTol2) {
            /*
            * (the ellipse is circle) &
            * (the given point is the center of the circle)
            */

            PointOnCurve2D p;
            try {
                p = new PointOnCurve2D(this, 0, doCheckDebug);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }

            throw new IndefiniteSolutionException(p);
        }

        // inverse rotated point
        Vector2D eAir = trans.reverseTransform(Bc2A);

        // make polynomial
        double eDDD = dBlrd * eAir.x();
        double eEEE = dBsrd * eAir.y();
        double eFFF = dBlrd * dBlrd - dBsrd * dBsrd;

        // coefficients of polynomial (real)
        double[] ercoef = new double[5];

        ercoef[4] = (-eFFF * eFFF);
        ercoef[0] = eDDD * eDDD;
        ercoef[3] = 2.0 * eDDD * eFFF;
        ercoef[1] = (-ercoef[3]);
        ercoef[2] = ercoef[4] + ercoef[0] + eEEE * eEEE;
        ercoef[2] = (-ercoef[2]);

        ComplexPolynomial pol;
        try {
            pol = new ComplexPolynomial(ArrayMathUtils.toComplex(ercoef));
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }

        Complex[] root;

        try {
            root = GeometryPrivateUtils.getRootsByDKA(pol);
        } catch (GeometryPrivateUtils.DKANotConvergeException e) {
            root = e.getValues();
        } catch (GeometryPrivateUtils.ImpossibleEquationException e) {
            throw new FatalException();
        } catch (GeometryPrivateUtils.IndefiniteEquationException e) {
            throw new FatalException();
        }

        PointOnGeometryList projList = new PointOnGeometryList();

        for (int i = 0; i < root.length; i++) {
            PointOnCurve2D proj;

            double eCOS = root[i].real();

            if (eCOS > 1.0)
                eCOS = 1.0;
            if (eCOS < -1.0)
                eCOS = -1.0;

            double Bparam1 = Math.acos(eCOS);
            double Bparam2 = (GeometryUtils.PI2 - Bparam1);
            if (semiAxis1 < semiAxis2) {
                Bparam1 += Math.PI / 2.0;
                Bparam2 += Math.PI / 2.0;
            }

            proj = checkProjection(parameterDomain().wrap(Bparam1),
                    point, dTol2);
            if (proj != null)
                projList.addPoint(proj);

            proj = checkProjection(parameterDomain().wrap(Bparam2),
                    point, dTol2);
            if (proj != null)
                projList.addPoint(proj);
        }
        return projList.toPointOnCurve2DArray();
    }

    /**
     * �^����ꂽ�p���??[�^��Ԃɂ�����?A
     * ��Ԃ̗��[�싂Ԍ�����?łף�ꂽ�_�̃p���??[�^�l��?�߂�?B
     * <p/>
     * ���̃?�\�b�h��
     * {@link Conic2D#toPolyline(ParameterSection,ToleranceForDistance)
     * Conic2D.toPolyline(ParameterSection, ToleranceForDistance)}
     * �̓Ք�ŌĂ�?o�����?B
     * </p>
     *
     * @param left  ?��[ (��ԉ���) �̃p���??[�^�l
     * @param right �E�[ (���?��) �̃p���??[�^�l
     * @return ?łף�ꂽ�_�̃p���??[�^�l
     */
    double getPeak(double left, double right) {
        double peak;

        peak = Math.atan2(Math.cos(left) - Math.cos(right),
                Math.sin(right) - Math.sin(left));

        while (peak < left)
            peak += Math.PI;
        while (peak > right)
            peak -= Math.PI;

        return peak;
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?Č�����L�?�x�W�G��?�̗��Ԃ�?B
     * <p/>
     * pint �̑?���l��?�Βl�� (2 * ��) ��?��?�?��ɂ�?A
     * ����� (2 * ��) �ƌ��Ȃ���?��?����?B
     * </p>
     *
     * @param pint ?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�?�x�W�G��?�̔z��
     */
    public PureBezierCurve2D[] toPolyBezierCurves(ParameterSection pint) {
        double increase = pint.increase();
        int nCurves;
        double startP;
        double increaseP;
        int i;

        if (Math.abs(increase) > GeometryUtils.PI2) {
            nCurves = 3;
            increaseP = (increase > 0.0) ? (GeometryUtils.PI2 / 3) : (-GeometryUtils.PI2 / 3);
        } else if (Math.abs(increase) > (4 * GeometryUtils.PI2 / 5)) {
            nCurves = 3;
            increaseP = increase / 3;
        } else if (Math.abs(increase) > (4 * Math.PI / 5)) {
            nCurves = 2;
            increaseP = increase / 2;
        } else {
            nCurves = 1;
            increaseP = increase;
        }

        CartesianTransformationOperator2D localTransformationOperator =
                this.position().toCartesianTransformationOperator(1.0);

        PureBezierCurve2D[] bzcs = new PureBezierCurve2D[nCurves];

        for (i = 0, startP = pint.start();
             i < nCurves;
             i++, startP += increaseP) {

            ParameterSection pintl = new ParameterSection(startP, increaseP);
            Point2D[] controlPoints = this.getControlPointsOfBezierCurve(pintl);
            double[] weights = {1.0, 1.0, 1.0};

            /*
            * the middle weight will be greater than 0.0 & less than 1.0
            */

            /*
            * Given:
            *	Rx, Ry	: ellipse's {x, y}_radius
            *	Tx, Ty	: unit vector of shoulder point
            *		  (rotated into ellipse's local coordinates system)
            *
            * Find:
            *	theta	: the parameter of shoulder point
            *
            *	tan(theta) = - (Ry * Tx) / (Rx * Ty)
            */
            Vector2D mvec = controlPoints[2].subtract(controlPoints[0]);
            Vector2D tmvec = localTransformationOperator.toLocal(mvec).unitized();

            double shoulderParam;
            double tmpBuf;
            if (Math.abs(tmpBuf = (this.xRadius() * tmvec.y())) > MachineEpsilon.DOUBLE) {
                shoulderParam = Math.atan(-(this.yRadius() * tmvec.x()) / tmpBuf);
            } else {
                shoulderParam = Math.PI / 2.0;
            }

            while (shoulderParam < pintl.lower())
                shoulderParam += Math.PI;
            while (shoulderParam > pintl.upper())
                shoulderParam -= Math.PI;

            /*
            *	v = (sp - m) / (cp1 - m)
            *	w1 = v / (1 - v)
            *
            * where
            *	sp	: shoulder point
            *	cp1	: middle control point
            *	m	: middle point between end points
            *
            *	w1	: middle weight
            */
            Point2D shoulderPoint = this.coordinates(shoulderParam);
            Point2D middlePoint = controlPoints[0].midPoint(controlPoints[2]);
            double vvv = Math.sqrt(shoulderPoint.subtract(middlePoint).norm() /
                    controlPoints[1].subtract(middlePoint).norm());
            // if (Math.abs(increaseP) > Math.PI) vvv = (- vvv);

            weights[1] = vvv / (1.0 - vvv);

            bzcs[i] = new PureBezierCurve2D(controlPoints, weights);
        }

        return bzcs;
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?Č�����L�?�a�X�v���C����?��Ԃ�?B
     * <p/>
     * pint �̑?���l��?�Βl�� (2 * ��) ��?��?�?��ɂ�?A
     * ����� (2 * ��) �ƌ��Ȃ���?��?��?A
     * �����`���̋�?��Ԃ�?B
     * </p>
     *
     * @param pint ?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�?�a�X�v���C����?�
     */
    public BsplineCurve2D toBsplineCurve(ParameterSection pint) {
        PureBezierCurve2D[] bzcs = this.toPolyBezierCurves(pint);
        boolean closed =
                (Math.abs(pint.increase()) >= GeometryUtils.PI2) ? true : false;

        return Conic2D.convertPolyBezierCurvesToOneBsplineCurve(bzcs, closed);
    }

    /**
     * ���̋�?�Ƒ��̋�?�Ƃ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * �����ȉ~�̂Ƃ���?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̉���?A
     * ��ȉ~�̒�?S�Ԃ̋����������̋��e��?����?�����?A
     * ��ȉ~�̋�?� X ���̂Ȃ��p�x���p�x�̋��e��?����?�����?A
     * ��ȉ~�̋�?� X/Y �����ꂼ��ɂ��Ă̔��a��?��������̋��e��?��ȓ�ł���?�?��ɂ�?A
     * ��ȉ~�̓I?[�o?[���b�v���Ă����̂Ƃ���?A
     * IndefiniteSolutionException �̗�O��?�������?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException mate ��ȉ~��?A��ȉ~�̓I?[�o?[���b�v���Ă���?A�⪕s��ł���
     */
    public IntersectionPoint2D[] intersect(ParametricCurve2D mate)
            throws IndefiniteSolutionException {
        return mate.intersect(this, true);
    }

    /**
     * ���̑ȉ~�� (��?����\�����ꂽ) ���R��?�̌�_��\����?�����?�?�����?B
     *
     * @param poly �x�W�G��?�邢�͂a�X�v���C����?�̂���Z�O�?���g�̑�?����\���̔z��
     * @return ���̑ȉ~�� poly �̌�_��\����?�����?���
     */
    DoublePolynomial makePoly(DoublePolynomial[] poly) {
        DoublePolynomial xPoly = (DoublePolynomial) poly[0].multiply(poly[0]);
        DoublePolynomial yPoly = (DoublePolynomial) poly[1].multiply(poly[1]);
        double dAlrd2 = xRadius() * xRadius();
        double dAsrd2 = yRadius() * yRadius();
        boolean isPoly = poly.length < 3;
        int degree = xPoly.degree();
        double[] coef = new double[degree + 1];

        if (isPoly) {
            for (int j = 0; j <= degree; j++)
                coef[j] = (xPoly.getCoefficientAsDouble(j) / dAlrd2) +
                        (yPoly.getCoefficientAsDouble(j) / dAsrd2);
            coef[0] -= 1.0;
        } else {
            DoublePolynomial wPoly = (DoublePolynomial) poly[2].multiply(poly[2]);
            for (int j = 0; j <= degree; j++)
                coef[j] = (dAsrd2 * xPoly.getCoefficientAsDouble(j)) + (dAlrd2 * yPoly.getCoefficientAsDouble(j))
                        - (dAlrd2 * dAsrd2 * wPoly.getCoefficientAsDouble(j));
        }
        return new DoublePolynomial(coef);
    }

    /**
     * �^����ꂽ�_�����̋�?�?�ɂ��邩�ۂ���`�F�b�N����?B
     *
     * @param point ?��?��?ۂƂȂ�_
     * @return �^����ꂽ�_�����̋�?�?�ɂ���� true?A�����łȂ���� false
     */
    boolean checkSolution(Point2D point) {
        double param = getParameter(point);
        double px = xRadius() * Math.cos(param);
        double py = yRadius() * Math.sin(param);

        return point.identical(new CartesianPoint2D(px, py));
    }

    /**
     * �^����ꂽ�_�����̋�?�?�ɂ����̂Ƃ���?A
     * ���̓_�̋�?�?�ł̃p���??[�^�l��?�߂�?B
     *
     * @param point ?��?��?ۂƂȂ�_
     * @return �p���??[�^�l
     */
    double getParameter(Point2D point) {
        double cos = point.x() / xRadius();
        if (cos > 1.0) cos = 1.0;
        if (cos < -1.0) cos = -1.0;
        double acos = Math.acos(cos);
        if (point.y() < 0.0) acos = 2 * Math.PI - acos;

        return acos;
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsLinCnc2D#intersection(Line2D,Ellipse2D,boolean)
     * IntsLinCnc2D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Line2D mate, boolean doExchange) {
        IntsLinCnc2D doObj = new IntsLinCnc2D(mate, this);
        return doObj.intersection(mate, this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�~) �Ƃ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �~�̃N���X��?u�~ vs. �ȉ~?v�̌�_���Z�?�\�b�h
     * {@link Circle2D#intersect(Ellipse2D,boolean)
     * Circle2D.intersect(Ellipse2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Circle2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�ȉ~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̉���?A
     * ��ȉ~�̒�?S�Ԃ̋����������̋��e��?����?�����?A
     * ��ȉ~�̋�?� X ���̂Ȃ��p�x���p�x�̋��e��?����?�����?A
     * ��ȉ~�̋�?� X/Y �����ꂼ��ɂ��Ă̔��a��?��������̋��e��?��ȓ�ł���?�?��ɂ�?A
     * ��ȉ~�̓I?[�o?[���b�v���Ă����̂Ƃ���?A
     * IndefiniteSolutionException �̗�O��?�������?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsEllCnc2D#intersection(Ellipse2D,Ellipse2D,boolean)
     * IntsEllCnc2D.intersection}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�ȉ~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException ��ȉ~�̓I?[�o?[���b�v���Ă���?A�⪕s��ł���
     */
    IntersectionPoint2D[] intersect(Ellipse2D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        double d_tol = getToleranceForDistance();
        IntersectionPoint2D one_sol;
        double mateParam;
        if (this.position().location().identical(mate.position().location())) {
            if (this.position().x().parallelDirection(mate.position().x())) {
                if (Math.abs(this.xRadius() - mate.xRadius()) <= d_tol &&
                        Math.abs(this.yRadius() - mate.yRadius()) <= d_tol) {
                    if (this.position().x().dotProduct(mate.position().x()) > 0.0) {
                        mateParam = 0.0;
                    } else {
                        mateParam = Math.PI;
                    }
                    if (!doExchange)
                        one_sol = new IntersectionPoint2D(this, 0.0, mate, mateParam, doCheckDebug);
                    else
                        one_sol = new IntersectionPoint2D(mate, mateParam, this, 0.0, doCheckDebug);
                    throw new IndefiniteSolutionException(one_sol);
                }
            }
            if (this.position().x().parallelDirection(mate.position().y())) {
                if (Math.abs(this.xRadius() - mate.yRadius()) <= d_tol &&
                        Math.abs(this.yRadius() - mate.xRadius()) <= d_tol) {
                    if (this.position().x().dotProduct(mate.position().y()) > 0.0) {
                        mateParam = Math.PI * 0.5;
                    } else {
                        mateParam = Math.PI * 1.5;
                    }
                    if (!doExchange)
                        one_sol = new IntersectionPoint2D(this, 0.0, mate, mateParam, doCheckDebug);
                    else
                        one_sol = new IntersectionPoint2D(mate, mateParam, this, 0.0, doCheckDebug);
                    throw new IndefiniteSolutionException(one_sol);
                }
            }
        }

        IntsEllCnc2D doObj = new IntsEllCnc2D();
        return doObj.intersection(this, mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsEllCnc2D#intersection(Ellipse2D,Parabola2D,boolean)
     * IntsEllCnc2D.intersection}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Parabola2D mate, boolean doExchange) {
        IntsEllCnc2D doObj = new IntsEllCnc2D();
        return doObj.intersection(this, mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�o��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsEllCnc2D#intersection(Ellipse2D,Hyperbola2D,boolean)
     * IntsEllCnc2D.intersection}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�o��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Hyperbola2D mate, boolean doExchange) {
        IntsEllCnc2D doObj = new IntsEllCnc2D();
        return doObj.intersection(this, mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�|�����C��) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �|�����C���̃N���X��?u�|�����C�� vs. �ȉ~?v�̌�_���Z�?�\�b�h
     * {@link Polyline2D#intersect(Ellipse2D,boolean)
     * Polyline2D.intersect(Ellipse2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�|�����C��)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Polyline2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�g������?�) �Ƃ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �g������?�̃N���X��?u�g������?� vs. �ȉ~?v�̌�_���Z�?�\�b�h
     * {@link TrimmedCurve2D#intersect(Ellipse2D,boolean)
     * TrimmedCurve2D.intersect(Ellipse2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�g������?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(TrimmedCurve2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�Z�O�?���g) �Ƃ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�Z�O�?���g�̃N���X��?u��?���?�Z�O�?���g vs. �ȉ~?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurveSegment2D#intersect(Ellipse2D,boolean)
     * CompositeCurveSegment2D.intersect(Ellipse2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�Z�O�?���g)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(CompositeCurveSegment2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�) �Ƃ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�̃N���X��?u��?���?� vs. �ȉ~?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurve2D#intersect(Ellipse2D,boolean)
     * CompositeCurve2D.intersect(Ellipse2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(CompositeCurve2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�I�t�Z�b�g������?��?A
     * �^����ꂽ��?��ŋߎ����� Bspline ��?��?�߂�?B
     *
     * @param pint  �I�t�Z�b�g����p���??[�^���
     * @param magni �I�t�Z�b�g��
     * @param side  �I�t�Z�b�g�̌� (WhichSide.LEFT/RIGHT)
     * @param tol   �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ̃I�t�Z�b�g��?��ߎ����� Bspline ��?�
     * @see WhichSide
     */
    public BsplineCurve2D
    offsetByBsplineCurve(ParameterSection pint,
                         double magni,
                         int side,
                         ToleranceForDistance tol) {
        Ofst2D doObj = new Ofst2D(this, pint, magni, side, tol);
        return doObj.offset();
    }

    /**
     * ���̋�?�Ƒ��̋�?�Ƃ̋���?�?��?�߂�?B
     * <p/>
     * ����?�?�?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * �����_�ł͎�����Ă��Ȃ�����?A
     * UnsupportedOperationException	�̗�O��?�����?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ����?�?�̔z��
     * @throws UnsupportedOperationException ���܂̂Ƃ���?A������Ȃ��@�\�ł���
     */
    public CommonTangent2D[] commonTangent(ParametricCurve2D mate) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?�Ƃ̋��ʖ@?��?�߂�?B
     * <p/>
     * ���ʖ@?�?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * �����_�ł͎�����Ă��Ȃ�����?A
     * UnsupportedOperationException	�̗�O��?�����?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ���ʖ@?�̔z��
     * @throws UnsupportedOperationException ���܂̂Ƃ���?A������Ȃ��@�\�ł���
     */
    public CommonNormal2D[] commonNormal(ParametricCurve2D mate) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�̃p���??[�^��`���Ԃ�?B
     * <p/>
     * �L�Ŏ��I�ȃp���??[�^��`���Ԃ�?B
     * �Ȃ�?A�v���C�}���ȗL���Ԃ� [0, (2 * ��)] �ł���?B
     * </p>
     *
     * @return �L�Ŏ��I�ȃp���??[�^��`��
     */
    ParameterDomain getParameterDomain() {
        try {
            return new ParameterDomain(true, 0, 2 * Math.PI);
        } catch (InvalidArgumentValueException e) {
            // should never be occurred
            throw new FatalException();
        }
    }

    /**
     * ���̋�?�􉽓I�ɕ��Ă��邩�ۂ���Ԃ�?B
     * <p/>
     * �ȉ~�Ȃ̂�?A?�� true ��Ԃ�?B
     * </p>
     *
     * @return �ȉ~�Ȃ̂�?A?�� <code>false</code>
     */
    boolean getClosedFlag() {
        return true;
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricCurve2D#ELLIPSE_2D ParametricCurve2D.ELLIPSE_2D}
     */
    int type() {
        return ELLIPSE_2D;
    }

    /**
     * ���̋�?��?A�^����ꂽ�􉽓I�ϊ����Z�q�ŕϊ�����?B
     * <p/>
     * transformedGeometries ��?A
     * �ϊ��O�̊􉽗v�f��L?[�Ƃ�?A
     * �ϊ���̊􉽗v�f��l�Ƃ���n�b�V���e?[�u���ł���?B
     * </p>
     * <p/>
     * this �� transformedGeometries ��ɃL?[�Ƃ��đ�?݂��Ȃ�?�?��ɂ�?A
     * this �� transformationOperator �ŕϊ�������̂�Ԃ�?B
     * ����?ۂɃ?�\�b�h�Ք�ł� this ��L?[?A
     * �ϊ����ʂ�l�Ƃ��� transformedGeometries �ɒǉB���?B
     * </p>
     * <p/>
     * this �� transformedGeometries ��Ɋ�ɃL?[�Ƃ��đ�?݂���?�?��ɂ�?A
     * ��?ۂ̕ϊ���?s�Ȃ킸?A���̃L?[�ɑΉ�����l��Ԃ�?B
     * ����?��?��?ċA�I��?s�Ȃ���?B
     * </p>
     * <p/>
     * transformedGeometries �� null �ł�?\��Ȃ�?B
     * transformedGeometries �� null ��?�?��ɂ�?A
     * ?�� this �� transformationOperator �ŕϊ�������̂�Ԃ�?B
     * </p>
     *
     * @param reverseTransform       �t�ϊ�����̂ł���� true?A�����łȂ���� false
     * @param transformationOperator �􉽓I�ϊ����Z�q
     * @param transformedGeometries  ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �ϊ���̊􉽗v�f
     */
    protected synchronized ParametricCurve2D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator2D transformationOperator,
                  java.util.Hashtable transformedGeometries) {
        Axis2Placement2D tPosition =
                this.position().transformBy(reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        double tSemiAxis1;
        double tSemiAxis2;
        if (reverseTransform != true) {
            tSemiAxis1 = transformationOperator.transform(this.semiAxis1());
            tSemiAxis2 = transformationOperator.transform(this.semiAxis2());
        } else {
            tSemiAxis1 = transformationOperator.reverseTransform(this.semiAxis1());
            tSemiAxis2 = transformationOperator.reverseTransform(this.semiAxis2());
        }
        return new Ellipse2D(tPosition, tSemiAxis1, tSemiAxis2);
    }

    /**
     * ?o�̓X�g��?[���Ɍ`?�?���?o�͂���?B
     *
     * @param writer PrintWriter
     * @param indent �C���f���g��?[��
     * @see GeometryElement
     */
    protected void output(PrintWriter writer, int indent) {
        String indent_tab = makeIndent(indent);

        writer.println(indent_tab + getClassName());
        writer.println(indent_tab + "\tposition");
        position().output(writer, indent + 2);
        writer.println(indent_tab + "\tsemiAxis1 " + semiAxis1);
        writer.println(indent_tab + "\tsemiAxis2 " + semiAxis2);
        writer.println(indent_tab + "End");
    }
}
