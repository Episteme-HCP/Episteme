/*
 * �Q���� : �o��?��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Hyperbola2D.java,v 1.9 2006/05/20 23:25:45 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

import org.jscience.mathematics.ArrayMathUtils;
import org.jscience.mathematics.MathUtils;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.analysis.PrimitiveMapping;
import org.jscience.mathematics.analysis.polynomials.ComplexPolynomial;
import org.jscience.mathematics.analysis.polynomials.DoublePolynomial;
import org.jscience.util.FatalException;

import java.io.PrintWriter;

/**
 * �Q���� : �o��?��\���N���X?B
 * <p/>
 * �o��?��?A���̒�?S�̈ʒu�Ƌ�?� X/Y ���̕�����?�?W�n
 * (�z�u?��?A{@link Axis2Placement2D Axis2Placement2D}) position ��
 * ��?S���璸�_�܂ł̋��� semiAxis?A
 * semiAxis ��?��킹�đQ��?�̌X����w�肷�鋗�� semiImagAxis
 * �Œ�`�����?B
 * </p>
 * <p/>
 * t ��p���??[�^�Ƃ���o��?� P(t) �̃p���?�g���b�N�\����?A�ȉ��̒ʂ�?B
 * <pre>
 * 	P(t) = position.location()
 * 	     + semiAxis * cosh(t) * position.x()
 * 	     + semiImagAxis * sinh(t) * position.y()
 * </pre>
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.9 $, $Date: 2006/05/20 23:25:45 $
 */

public class Hyperbola2D extends Conic2D {

    /**
     * ��?S���璸�_�܂ł̋���?B
     *
     * @serial
     */
    private double semiAxis;

    /**
     * semiAxis ��?��킹�đQ��?�̌X����w�肷�鋗��?B
     *
     * @serial
     */
    private double semiImagAxis;

    /**
     * ��?S���璸�_�܂ł̋�����?A�����?��킹�đQ��?�̌X����w�肷�鋗����?A
     * ������ێ?����t�B?[���h��?ݒ肷��?B
     * <p/>
     * semiAxis, semiImagAxis �̒l��?��łȂ���΂Ȃ�Ȃ�?B
     * </p>
     * <p/>
     * semiAxis, semiImagAxis �̂����ꂩ�̒l��
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?����?�����?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     *
     * @param semiAxis     ��?S���璸�_�܂ł̋���
     * @param semiImagAxis semiAxis ��?��킹�đQ��?�̌X����w�肷�鋗��
     * @see InvalidArgumentValueException
     */
    private void setSemiAxis(double semiAxis, double semiImagAxis) {
        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double dTol = condition.getToleranceForDistance();

        if (semiAxis < dTol) {
            throw new InvalidArgumentValueException();
        }
        this.semiAxis = semiAxis;

        if (semiImagAxis < dTol) {
            throw new InvalidArgumentValueException();
        }
        this.semiImagAxis = semiImagAxis;
    }

    /**
     * ��?�?W�n�ƒ�?S���璸�_�܂ł̋�������ёQ��?�̌X����w�肷�鋗��
     * ��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * position �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * semiAxis, semiImagAxis �̂����ꂩ�̒l��
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?����?�����?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     *
     * @param position     ��?S�Ƌ�?� X/Y ���̕�����?�?W�n
     * @param semiAxis     ��?S���璸�_�܂ł̋���
     * @param semiImagAxis semiAxis ��?��킹�đQ��?�̌X����w�肷�鋗��
     * @see InvalidArgumentValueException
     */
    public Hyperbola2D(Axis2Placement2D position,
                       double semiAxis, double semiImagAxis) {
        super(position);
        setSemiAxis(semiAxis, semiImagAxis);
    }

    /**
     * ���̑o��?�̒�?S���璸�_�܂ł̋��� (semiAxis) ��Ԃ�?B
     *
     * @return ��?S���璸�_�܂ł̋���
     */
    public double semiAxis() {
        return this.semiAxis;
    }

    /**
     * {@link #semiAxis() semiAxis()} �̕ʖ��?�\�b�h?B
     *
     * @return ��?S���璸�_�܂ł̋���
     */
    public double xRadius() {
        return this.semiAxis;
    }

    /**
     * ���̑o��?�� semiImagAxis �̒l��Ԃ�?B
     *
     * @return semiImagAxis �̒l
     */
    public double semiImagAxis() {
        return this.semiImagAxis;
    }

    /**
     * {@link #semiImagAxis() semiImagAxis()} �̕ʖ��?�\�b�h?B
     *
     * @return semiImagAxis �̒l
     */
    public double yRadius() {
        return this.semiImagAxis;
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
        final double m2hbl_majrd2 = semiAxis() * semiAxis();
        final double m2hbl_minrd2 = semiImagAxis() * semiImagAxis();
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
                        double ecosh = Math.cosh(parameter);
                        double esinh = Math.sinh(parameter);

                        return Math.sqrt(m2hbl_majrd2 * esinh * esinh +
                                m2hbl_minrd2 * ecosh * ecosh);
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
        Vector2D x = ax.x().multiply(Math.cosh(param) * semiAxis);
        Vector2D y = ax.y().multiply(Math.sinh(param) * semiImagAxis);

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
        Vector2D x1 = ax.x().multiply(Math.sinh(param) * semiAxis);
        Vector2D y1 = ax.y().multiply(Math.cosh(param) * semiImagAxis);

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
        double x1len = Math.sinh(param) * semiAxis;
        double y1len = Math.cosh(param) * semiImagAxis;
        double x2len = Math.cosh(param) * semiAxis;
        double y2len = Math.sinh(param) * semiImagAxis;
        double tlen = Math.sqrt(x1len * x1len + y1len * y1len);
        double crv = Math.abs(x1len * y2len - y1len * x2len)
                / (tlen * tlen * tlen);
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
        Vector2D ex = ax.x().multiply(Math.cosh(param) * semiAxis);
        Vector2D ey = ax.y().multiply(Math.sinh(param) * semiImagAxis);
        Vector2D ex1 = ax.x().multiply(Math.sinh(param) * semiAxis);
        Vector2D ey1 = ax.y().multiply(Math.cosh(param) * semiImagAxis);

        Vector2D d1 = ex1.add(ey1);
        Vector2D d2 = ex.add(ey); // ex2 == ex, ey2 == ey
        Point2D d0 = ax.location().add(d2);

        return new CurveDerivative2D(d0, d1, d2);
    }

    /**
     * �^����ꂽ�_���炱�̋�?�ւ̓��e�_��?�߂�?B
     * <p/>
     * [�Ք?��?]
     * <br>
     * ����l���̑�?�����?���?�߂邱�ƂɋA��������?A��?��I�ɉ⢂Ă���?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_
     */
    public PointOnCurve2D[] projectFrom(Point2D point) {
        double dTol2 = getToleranceForDistance2();

        /*
        * NOTE:
        *
        * equation of normal line
        *
        * (A**2 * Px)/(A * coshT) + (B**2 * Py)/(B * sinhT) = A**2 + B**2
        *		(A = dB->x_radius, B = dB->y_radius),
        *
        * so a polynomial of sinhT is
        *
        * F**2 * Z4  -2EF * Z3  +(F**2+E**2-D**2) * Z2  -2EF * Z1  +E**2 = 0
        *		(Z = sinhT, D = A*Px, E = B*Py, F = A**2 + B**2).
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

        // critical check
        if ((eAir.x() - semiAxis) * (eAir.x() - semiAxis) < dTol2 &&
                (eAir.y() * eAir.y()) < dTol2) {

            root = new Complex[1];
            root[0] = new Complex(0.0);
        } else {

            // make polynomial
            double eDDD = semiAxis * eAir.x();
            double eEEE = semiImagAxis * eAir.y();
            double eFFF = semiAxis * semiAxis + semiImagAxis * semiImagAxis;

            // coefficients of polynomial (real)
            double[] ercoef = new double[5];

            ercoef[4] = eFFF * eFFF;
            ercoef[0] = eEEE * eEEE;
            ercoef[3] = (-2.0 * eEEE * eFFF);
            ercoef[1] = ercoef[3];
            ercoef[2] = ercoef[4] + ercoef[0] - eDDD * eDDD;

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

            double eSINH = root[i].real();

            proj = checkProjection(MathUtils.asinh(eSINH), point, dTol2);
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
        return MathUtils.atanh((Math.cosh(right) - Math.cosh(left)) /
                (Math.sinh(right) - Math.sinh(left)));
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?Č�����L�?�x�W�G��?�̗��Ԃ�?B
     * <p/>
     * �o��?��?�?�?A������L�?�x�W�G��?��̗v�f?���?�� 1 �ł���
     * </p>
     *
     * @param pint ?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�?�x�W�G��?�̔z��
     */
    public PureBezierCurve2D[] toPolyBezierCurves(ParameterSection pint) {
        Point2D[] controlPoints = this.getControlPointsOfBezierCurve(pint);
        double[] weights = {1.0, 1.0, 1.0};

        /*
        * the middle weight will be greater than 1.0
        */

        /*
        * Given:
        *	Rx, Ry	: hyperbola's {x, y}_radius
        *	Tx, Ty	: unit vector of shoulder point
        *		  (rotated into hyperbola's local coordinates system)
        *
        * Find:
        *	theta	: the parameter of shoulder point
        *
        *	tanh(theta) = (Ry * Tx) / (Rx * Ty)
        */
        Vector2D mvec = controlPoints[2].subtract(controlPoints[0]);
        CartesianTransformationOperator2D localTransformationOperator =
                this.position().toCartesianTransformationOperator(1.0);
        Vector2D tmvec = localTransformationOperator.toLocal(mvec).unitized();

        double shoulderParam = MathUtils.atanh((this.yRadius() * tmvec.x()) /
                (this.xRadius() * tmvec.y()));

        /*
        *	v = (sp - m) / (p1 - m)
        *	w1 = v / (1 - v)
        *
        * where
        *	sp	: shoulder point
        *	p1	: middle control point
        *	m	: middle point between end points
        *
        *	w1	: middle weight
        */
        Point2D shoulderPoint = this.coordinates(shoulderParam);
        Point2D middlePoint = controlPoints[0].midPoint(controlPoints[2]);
        double vvv = Math.sqrt(shoulderPoint.subtract(middlePoint).norm() /
                controlPoints[1].subtract(middlePoint).norm());

        weights[1] = vvv / (1.0 - vvv);

        PureBezierCurve2D[] bzcs = {
                new PureBezierCurve2D(controlPoints, weights)};

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
     * �����o��?�̂Ƃ���?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̉���?A
     * ��o��?�̒�?S�Ԃ̋����������̋��e��?����?�����?A
     * ��o��?�̋�?� X ���̂Ȃ��p�x���p�x�̋��e��?����?�����?A
     * ��o��?�� semiAxis, semiImagAxis ���ꂼ���?��������̋��e��?��ȓ�ł���?�?��ɂ�?A
     * ��o��?�̓I?[�o?[���b�v���Ă����̂Ƃ���?A
     * IndefiniteSolutionException �̗�O��?�������?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException mate ��o��?��?A��o��?�̓I?[�o?[���b�v���Ă���?A�⪕s��ł���
     */
    public IntersectionPoint2D[] intersect(ParametricCurve2D mate)
            throws IndefiniteSolutionException {
        return mate.intersect(this, true);
    }

    /**
     * ���̑o��?�� (��?����\�����ꂽ) ���R��?�̌�_��\����?�����?�?�����?B
     *
     * @param poly �x�W�G��?�邢�͂a�X�v���C����?�̂���Z�O�?���g�̑�?����\���̔z��
     * @return ���̑o��?�� poly �̌�_��\����?�����?���
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
                coef[j] = (xPoly.getCoefficientAsDouble(j) / dAlrd2) -
                        (yPoly.getCoefficientAsDouble(j) / dAsrd2);
            coef[0] -= 1.0;
        } else {
            DoublePolynomial wPoly = (DoublePolynomial) poly[2].multiply(poly[2]);
            for (int j = 0; j <= degree; j++)
                coef[j] = (dAsrd2 * xPoly.getCoefficientAsDouble(j)) - (dAlrd2 * yPoly.getCoefficientAsDouble(j))
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
        double px = xRadius() * Math.cosh(param);
        double py = yRadius() * Math.sinh(param);

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
        double sinh = point.y() / yRadius();

        return MathUtils.asinh(sinh);
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
     * {@link IntsLinCnc2D#intersection(Line2D,Hyperbola2D,boolean)
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
     * �~�̃N���X��?u�~ vs. �o��?�?v�̌�_���Z�?�\�b�h
     * {@link Circle2D#intersect(Hyperbola2D,boolean)
     * Circle2D.intersect(Hyperbola2D, boolean)}
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
     * �ȉ~�̃N���X��?u�ȉ~ vs. �o��?�?v�̌�_���Z�?�\�b�h
     * {@link Ellipse2D#intersect(Hyperbola2D,boolean)
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
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?�̃N���X��?u��?� vs. �o��?�?v�̌�_���Z�?�\�b�h
     * {@link Parabola2D#intersect(Hyperbola2D,boolean)
     * Parabola2D.intersect(Hyperbola2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Parabola2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�o��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̉���?A
     * ��o��?�̒�?S�Ԃ̋����������̋��e��?����?�����?A
     * ��o��?�̋�?� X ���̂Ȃ��p�x���p�x�̋��e��?����?�����?A
     * ��o��?�� semiAxis, semiImagAxis ���ꂼ���?��������̋��e��?��ȓ�ł���?�?��ɂ�?A
     * ��o��?�̓I?[�o?[���b�v���Ă����̂Ƃ���?A
     * IndefiniteSolutionException �̗�O��?�������?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsHypCnc2D#intersection(Hyperbola2D,Hyperbola2D,boolean)
     * IntsHypCnc2D.intersection}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�o��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException ��o��?�̓I?[�o?[���b�v���Ă���?A�⪕s��ł���
     */
    IntersectionPoint2D[] intersect(Hyperbola2D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        if ((this.position().location().identical(mate.position().location())) &&
                (this.position().x().identicalDirection(mate.position().x()))) {
            double d_tol = getToleranceForDistance();
            if (Math.abs(this.xRadius() - mate.xRadius()) <= d_tol &&
                    Math.abs(this.yRadius() - mate.yRadius()) <= d_tol) {
                IntersectionPoint2D one_sol;
                if (!doExchange)
                    one_sol = new IntersectionPoint2D(this, 0.0, mate, 0.0, doCheckDebug);
                else
                    one_sol = new IntersectionPoint2D(mate, 0.0, this, 0.0, doCheckDebug);
                throw new IndefiniteSolutionException(one_sol);
            }
        }
        IntsHypCnc2D doObj = new IntsHypCnc2D();
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
     * �|�����C���̃N���X��?u�|�����C�� vs. �o��?�?v�̌�_���Z�?�\�b�h
     * {@link Polyline2D#intersect(Hyperbola2D,boolean)
     * Polyline2D.intersect(Hyperbola2D, boolean)}
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
     * �g������?�̃N���X��?u�g������?� vs. �o��?�?v�̌�_���Z�?�\�b�h
     * {@link TrimmedCurve2D#intersect(Hyperbola2D,boolean)
     * TrimmedCurve2D.intersect(Hyperbola2D, boolean)}
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
     * ��?���?�Z�O�?���g�̃N���X��?u��?���?�Z�O�?���g vs. �o��?�?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurveSegment2D#intersect(Hyperbola2D,boolean)
     * CompositeCurveSegment2D.intersect(Hyperbola2D, boolean)}
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
     * ��?���?�̃N���X��?u��?���?� vs. �o��?�?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurve2D#intersect(Hyperbola2D,boolean)
     * CompositeCurve2D.intersect(Hyperbola2D, boolean)}
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
     * �o��?�Ȃ̂�?A?�� false ��Ԃ�?B
     * </p>
     *
     * @return �o��?�͕��邱�Ƃ͂Ȃ��̂�?A?�� <code>false</code>
     */
    boolean getClosedFlag() {
        return false;
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricCurve2D#HYPERBOLA_2D ParametricCurve2D.HYPERBOLA_2D}
     */
    int type() {
        return HYPERBOLA_2D;
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
        double tSemiAxis;
        double tSemiImagAxis;
        if (reverseTransform != true) {
            tSemiAxis = transformationOperator.transform(this.semiAxis());
            tSemiImagAxis = transformationOperator.transform(this.semiImagAxis());
        } else {
            tSemiAxis = transformationOperator.reverseTransform(this.semiAxis());
            tSemiImagAxis = transformationOperator.reverseTransform(this.semiImagAxis());
        }
        return new Hyperbola2D(tPosition, tSemiAxis, tSemiImagAxis);
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
        writer.println(indent_tab + "\tsemiAxis " + semiAxis);
        writer.println(indent_tab + "\tsemiImagAxis " + semiImagAxis);
        writer.println(indent_tab + "End");
    }
}
