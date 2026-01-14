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
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.analysis.PrimitiveMapping;
import org.jscience.mathematics.analysis.polynomials.ComplexPolynomial;
import org.jscience.mathematics.analysis.polynomials.DoublePolynomial;
import org.jscience.util.FatalException;

import java.io.PrintWriter;

/**
 * �Q���� : ��?��\���N���X?B
 * <p/>
 * ��?��?A���̒��_�̈ʒu�Ƌ�?� X/Y ���̕�����?�?W�n
 * (�z�u?��?A{@link Axis2Placement2D Axis2Placement2D}) position ��
 * ���_����?œ_�܂ł̋��� focalDist
 * �Œ�`�����?B
 * </p>
 * <p/>
 * t ��p���??[�^�Ƃ����?� P(t) �̃p���?�g���b�N�\����?A�ȉ��̒ʂ�?B
 * <pre>
 * 	P(t) = position.location() + focalDist * (t * t * position.x() + 2 * t * position.y())
 * </pre>
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.8 $, $Date: 2006/05/20 23:25:50 $
 */

public class Parabola2D extends Conic2D {

    /**
     * ���_����?œ_�܂ł̋���?B
     *
     * @serial
     */
    private double focalDist;

    /**
     * ���_����?œ_�܂ł̋�����?A�����ێ?����t�B?[���h��?ݒ肷��?B
     * <p/>
     * focalDist �̒l��?��łȂ���΂Ȃ�Ȃ�?B
     * </p>
     * <p/>
     * focalDist �̒l��
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?����?�����?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     *
     * @param focalDist ���_����?œ_�܂ł̋���
     * @see InvalidArgumentValueException
     */
    private void setFocalDist(double focalDist) {
        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double dTol = condition.getToleranceForDistance();

        if (focalDist < dTol) {
            throw new InvalidArgumentValueException();
        }
        this.focalDist = focalDist;
    }

    /**
     * ��?�?W�n�ƒ��_����?œ_�܂ł̋�����^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * position �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * focalDist �̒l��
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?����?�����?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     *
     * @param position  ��?S�Ƌ�?� X/Y ���̕�����?�?W�n
     * @param focalDist ���_����?œ_�܂ł̋���
     * @see InvalidArgumentValueException
     */
    public Parabola2D(Axis2Placement2D position, double focalDist) {
        super(position);
        setFocalDist(focalDist);
    }

    /**
     * ���̕�?�̒��_����?œ_�܂ł̋�����Ԃ�?B
     *
     * @return ���_����?œ_�܂ł̋���
     */
    public double focalDist() {
        return this.focalDist;
    }

    /**
     * �^����ꂽ�p���??[�^��Ԃɂ����邱�̋�?�̎��?�ł̒��� (���̂�) ��Ԃ�?B
     * <p/>
     * pint �̑?���l�͕��ł©�܂�Ȃ�?B
     * </p>
     *
     * @param pint ��?�̒�����?�߂�p���??[�^���
     * @return �w�肳�ꂽ�p���??[�^��Ԃɂ������?�̒���
     */
    public double length(ParameterSection pint) {
        final double m2pbl_2fcldst = 2.0 * focalDist();
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
                        return m2pbl_2fcldst * Math.sqrt(parameter * parameter
                                + 1.0);
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
        Axis2Placement2D ax = position();
        Vector2D x = ax.x().multiply(param * param * focalDist);
        Vector2D y = ax.y().multiply(2 * param * focalDist);

        return ax.location().add(x.add(y));
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ?ڃx�N�g��
     */
    public Vector2D tangentVector(double param) {
        Axis2Placement2D ax = position();
        Vector2D x1 = ax.x().multiply(2 * param * focalDist);
        Vector2D y1 = ax.y().multiply(2 * focalDist);

        return x1.add(y1);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̋ȗ���Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return �ȗ�
     */
    public CurveCurvature2D curvature(double param) {
        Axis2Placement2D ax = position();
        double x1len = 2 * param * focalDist;
        double y1len = 2 * focalDist;
        double x2len = 2 * focalDist;
        double tlen = Math.sqrt(x1len * x1len + y1len * y1len);
        double crv = Math.abs(y1len * x2len) / (tlen * tlen * tlen);
        Vector2D ex1 = ax.x().multiply(x1len);
        Vector2D ey1 = ax.y().multiply(y1len);

        Vector2D tangent = ex1.add(ey1);
        // rotate tangent PI/2
        Vector2D nrmDir =
                new LiteralVector2D(tangent.y(), -tangent.x());
        return new CurveCurvature2D(crv, nrmDir.unitized());
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̓���?���Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ����?�
     */
    public CurveDerivative2D evaluation(double param) {
        Axis2Placement2D ax = position();
        Vector2D ex = ax.x().multiply(param * param * focalDist);
        Vector2D ey = ax.y().multiply(2 * param * focalDist);
        Vector2D ex1 = ax.x().multiply(2 * param * focalDist);
        Vector2D ey1 = ax.y().multiply(2 * focalDist);
        Vector2D ex2 = ax.x().multiply(2 * focalDist);

        Point2D d0 = ax.location().add(ex.add(ey));
        Vector2D d1 = ex1.add(ey1);

        return new CurveDerivative2D(d0, d1, ex2);
    }

    /**
     * �^����ꂽ�_���炱�̋�?�ւ̓��e�_��?�߂�?B
     * <p/>
     * [�Ք?��?]
     * <br>
     * ����O���̑�?�����?���?�߂邱�ƂɋA��������?A��?��I�ɉ⢂Ă���?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_
     */
    public PointOnCurve2D[] projectFrom(Point2D point) {
        double dTol2 = getToleranceForDistance2();

        /*
        * NOTE:
        * equation of normal line
        *
        *	A * T3 + 0 * T2 + ( 2 * A - Px ) * T - Py = 0
        *		( A = dB->focal_dist, Px,Py = dApnt, T = parameter )
        *
        * distinction
        *	A3 ** 2 / 4 + A2 ** 3 / 27   <  0  --> 3 points
        *                                   =  0  --> 1 point
        *                                   >  0  --> 1 point
        */

        CartesianTransformationOperator2D trans;

        try {
            trans = new CartesianTransformationOperator2D(position(), 1.0);
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }
        // vector from B's center to A
        Vector2D Bc2A = point.subtract(position().location());

        // inverse rotated point
        Vector2D eAir = trans.reverseTransform(Bc2A);

        Complex[] root;

        // coefficients of polynomial (real)
        double[] ercoef = new double[4];

        // make polynomial
        ercoef[3] = focalDist;
        ercoef[2] = 0.0;
        ercoef[1] = 2.0 * focalDist - eAir.x();
        ercoef[0] = (-eAir.y());

        // distinction

        if (ercoef[1] * ercoef[1] < dTol2 && ercoef[0] * ercoef[0] < dTol2) {
            root = new Complex[1];
            root[0] = new Complex(0.0);
        } else {

            ComplexPolynomial pol;
            try {
                pol = new ComplexPolynomial(ArrayMathUtils.toComplex(ercoef));
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }

            try {
                root = GeometryPrivateUtils.getRootsByDKA(pol);
            } catch (GeometryPrivateUtils.DKANotConvergeException e) {
                root = e.getValues();
            } catch (GeometryPrivateUtils.ImpossibleEquationException e) {
                throw new FatalException();
            } catch (GeometryPrivateUtils.IndefiniteEquationException e) {
                throw new FatalException();
            }
        }

        PointOnGeometryList projList = new PointOnGeometryList();

        for (int i = 0; i < root.length; i++) {
            PointOnCurve2D proj;

            double epara = root[i].real();

            proj = checkProjection(epara, point, dTol2);
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
        return ((left + right) / 2.0);
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?Č�����L�?�x�W�G��?�̗��Ԃ�?B
     * <p/>
     * ��?��?�?�?A������L�?�x�W�G��?��̗v�f?���?�� 1 �ł���
     * </p>
     *
     * @param pint ?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�?�x�W�G��?�̔z��
     */
    public PureBezierCurve2D[] toPolyBezierCurves(ParameterSection pint) {
        double[] weights = {1.0, 1.0, 1.0};
        PureBezierCurve2D[] bzcs = {
                new PureBezierCurve2D(this.getControlPointsOfBezierCurve(pint),
                        weights)};
        return bzcs;
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?Č�����L�?�a�X�v���C����?��Ԃ�?B
     *
     * @param pint ?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�?�a�X�v���C����?�
     */
    public BsplineCurve2D toBsplineCurve(ParameterSection pint) {
        PureBezierCurve2D[] bzcs = this.toPolyBezierCurves(pint);
        return bzcs[0].toBsplineCurve();
    }

    /**
     * ���̋�?�Ƒ��̋�?�Ƃ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ������?�̂Ƃ���?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̉���?A
     * ���?�̒��_�Ԃ̋����������̋��e��?����?�����?A
     * ���?�̋�?� X ���̂Ȃ��p�x���p�x�̋��e��?����?�����?A
     * ���?�̒��_?|?œ_�ԋ�����?��������̋��e��?��ȓ�ł���?�?��ɂ�?A
     * ���?�̓I?[�o?[���b�v���Ă����̂Ƃ���?A
     * IndefiniteSolutionException �̗�O��?�������?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException mate ���?��?A���?�̓I?[�o?[���b�v���Ă���?A�⪕s��ł���
     */
    public IntersectionPoint2D[] intersect(ParametricCurve2D mate)
            throws IndefiniteSolutionException {
        return mate.intersect(this, true);
    }

    /**
     * ���̕�?�� (��?����\�����ꂽ) ���R��?�̌�_��\����?�����?�?�����?B
     *
     * @param poly �x�W�G��?�邢�͂a�X�v���C����?�̂���Z�O�?���g�̑�?����\���̔z��
     * @return ���̕�?�� poly �̌�_��\����?�����?���
     */
    DoublePolynomial makePoly(DoublePolynomial[] poly) {
        DoublePolynomial yPoly = (DoublePolynomial) poly[1].multiply(poly[1]);
        double dA4fd = 4.0 * focalDist();
        boolean isPoly = poly.length < 3;
        int degree = yPoly.degree();
        double[] coef = new double[degree + 1];

        if (isPoly) {
            int deg = poly[1].degree();
            for (int j = 0; j <= degree; j++)
                if (j > (degree - deg))
                    coef[j] = yPoly.getCoefficientAsDouble(j);
                else
                    coef[j] = yPoly.getCoefficientAsDouble(j) -
                            (dA4fd * poly[0].getCoefficientAsDouble(j));
        } else {
            DoublePolynomial xwPoly = (DoublePolynomial) poly[0].multiply(poly[2]);
            for (int j = 0; j <= degree; j++)
                coef[j] = yPoly.getCoefficientAsDouble(j) - (dA4fd * xwPoly.getCoefficientAsDouble(j));
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
        double px = focalDist() * param * param;
        double py = 2.0 * focalDist() * param;

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
        return point.y() / (2.0 * focalDist());
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
     * {@link IntsLinCnc2D#intersection(Line2D,Parabola2D,boolean)
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
     * ���̋�?�Ƒ��̋�?� (�~) �̌�_��?�߂�
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �~�̃N���X��?u�~ vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link Circle2D#intersect(Parabola2D,boolean)
     * Circle2D.intersect(Parabola2D, boolean)}
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
     * ���̋�?�Ƒ��̋�?� (�ȉ~) �̌�_��?�߂�
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �ȉ~�̃N���X��?u�ȉ~ vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link Ellipse2D#intersect(Parabola2D,boolean)
     * Ellipse2D.intersect(Parabola2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�ȉ~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Ellipse2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̉���?A
     * ���?�̒��_�Ԃ̋����������̋��e��?����?�����?A
     * ���?�̋�?� X ���̂Ȃ��p�x���p�x�̋��e��?����?�����?A
     * ���?�̒��_?|?œ_�ԋ�����?��������̋��e��?��ȓ�ł���?�?��ɂ�?A
     * ���?�̓I?[�o?[���b�v���Ă����̂Ƃ���?A
     * IndefiniteSolutionException �̗�O��?�������?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsParCnc2D#intersection(Parabola2D,Parabola2D,boolean)
     * IntsParCnc2D.intersection}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException ���?�̓I?[�o?[���b�v���Ă���?A�⪕s��ł���
     */
    IntersectionPoint2D[] intersect(Parabola2D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        if ((this.position().location().identical(mate.position().location())) &&
                (this.position().x().identicalDirection(mate.position().x()))) {
            double d_tol = getToleranceForDistance();
            if (Math.abs(this.focalDist() - mate.focalDist()) <= d_tol) {
                IntersectionPoint2D one_sol;
                if (!doExchange)
                    one_sol = new IntersectionPoint2D(this, 0.0, mate, 0.0, doCheckDebug);
                else
                    one_sol = new IntersectionPoint2D(mate, 0.0, this, 0.0, doCheckDebug);
                throw new IndefiniteSolutionException(one_sol);
            }
        }
        IntsParCnc2D doObj = new IntsParCnc2D();
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
     * {@link IntsParCnc2D#intersection(Parabola2D,Hyperbola2D,boolean)
     * IntsParCnc2D.intersection}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�o��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Hyperbola2D mate, boolean doExchange) {
        IntsParCnc2D doObj = new IntsParCnc2D();
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
     * �|�����C���̃N���X��?u�|�����C�� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link Polyline2D#intersect(Parabola2D,boolean)
     * Polyline2D.intersect(Parabola2D, boolean)}
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
     * �g������?�̃N���X��?u�g������?� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link TrimmedCurve2D#intersect(Parabola2D,boolean)
     * TrimmedCurve2D.intersect(Parabola2D, boolean)}
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
     * ��?���?�Z�O�?���g�̃N���X��?u��?���?�Z�O�?���g vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurveSegment2D#intersect(Parabola2D,boolean)
     * CompositeCurveSegment2D.intersect(Parabola2D, boolean)}
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
     * ��?���?�̃N���X��?u��?���?� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurve2D#intersect(Parabola2D,boolean)
     * CompositeCurve2D.intersect(Parabola2D, boolean)}
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
     * ���Ŕ���I�ȃp���??[�^��`���Ԃ�?B
     * </p>
     *
     * @return ���Ŕ���I�ȃp���??[�^��`��
     */
    ParameterDomain getParameterDomain() {
        return new ParameterDomain();
    }

    /**
     * ���̋�?�􉽓I�ɕ��Ă��邩�ۂ���Ԃ�?B
     * <p/>
     * ��?�Ȃ̂�?A?�� false ��Ԃ�?B
     * </p>
     *
     * @return ��?�͕��邱�Ƃ͂Ȃ��̂�?A?�� <code>false</code>
     */
    boolean getClosedFlag() {
        return false;
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricCurve2D#PARABOLA_2D ParametricCurve2D.PARABOLA_2D}
     */
    int type() {
        return PARABOLA_2D;
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
        double tFocalDist;
        if (reverseTransform != true)
            tFocalDist = transformationOperator.transform(this.focalDist());
        else
            tFocalDist = transformationOperator.reverseTransform(this.focalDist());
        return new Parabola2D(tPosition, tFocalDist);
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
        writer.println(indent_tab + "\tfocalDist " + focalDist);
        writer.println(indent_tab + "End");
    }
}
