/*
 * �R���� : �ȉ~��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Ellipse3D.java,v 1.6 2006/05/20 23:25:42 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.mathematics.analysis.polynomials.DoublePolynomial;
import org.episteme.util.FatalException;

import java.io.PrintWriter;

/**
 * �R���� : �ȉ~��\���N���X?B
 * <p/>
 * �ȉ~��?A���̒�?S�̈ʒu�Ƌ�?� X/Y ���̕�����?�?W�n
 * (�z�u?��?A{@link Axis2Placement3D Axis2Placement3D}) position ��
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
 * @version $Revision: 1.6 $, $Date: 2006/05/20 23:25:42 $
 */

public class Ellipse3D extends Conic3D {
    /**
     * ���a1 (��?�?W��X�����̔��a) ?B
     *
     * @serial
     */
    private double semiAxis1;

    /**
     * ���a2 (��?�?W��Y�����̔��a) ?B
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
     * @param position  ��?S�Ƌ�?� X/Y/Z ���̕�����?�?W�n
     * @param semiAxis1 ���a1 (��?� X ���ɑ΂��锼�a)
     * @param semiAxis2 ���a2 (��?� Y ���ɑ΂��锼�a)
     * @see InvalidArgumentValueException
     */
    public Ellipse3D(Axis2Placement3D position,
                     double semiAxis1, double semiAxis2) {
        super(position);
        setSemiAxis(semiAxis1, semiAxis2);
    }

    /**
     * ���̑ȉ~�̊e���̔��a��p����?A�^����ꂽ�ʒu�ƌX���łQ�����̑ȉ~��?�?�����?B
     * <p/>
     * position �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param position �Q�����̑ȉ~�̈ʒu�ƌX������?�?W�n
     * @return �Q�����̑ȉ~
     * @see InvalidArgumentValueException
     */
    Conic2D toLocal2D(Axis2Placement2D position) {
        return new Ellipse2D(position, semiAxis1(), semiAxis2());
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
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ?W�l
     */
    public Point3D coordinates(double param) {
        param = parameterDomain().wrap(param);
        Axis2Placement3D ax = position();
        Vector3D ex = ax.x().multiply(Math.cos(param) * semiAxis1);
        Vector3D ey = ax.y().multiply(Math.sin(param) * semiAxis2);

        return ax.location().add(ex.add(ey));
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ?ڃx�N�g��
     */
    public Vector3D tangentVector(double param) {
        param = parameterDomain().wrap(param);
        Axis2Placement3D ax = position();
        Vector3D ex1 = ax.x().multiply(-Math.sin(param) * semiAxis1);
        Vector3D ey1 = ax.y().multiply(Math.cos(param) * semiAxis2);

        return ex1.add(ey1);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̋ȗ���Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return �ȗ�
     */
    public CurveCurvature3D curvature(double param) {
        param = parameterDomain().wrap(param);
        Axis2Placement3D ax = position();
        double xlen = Math.cos(param) * semiAxis1;
        double ylen = Math.sin(param) * semiAxis2;
        double x1len = -Math.sin(param) * semiAxis1;
        double y1len = Math.cos(param) * semiAxis2;
        double plen = Math.sqrt(x1len * x1len + y1len * y1len);
        double crv = Math.abs(x1len * ylen - y1len * xlen)
                / (plen * plen * plen);
        Vector3D ex1 = ax.x().multiply(x1len);
        Vector3D ey1 = ax.y().multiply(y1len);

        Vector3D tangent = ex1.add(ey1);
        // rotate tangent PI/2 around Z axis
        Vector3D nrmDir = ax.z().crossProduct(tangent);
        return new CurveCurvature3D(crv, nrmDir.unitized());
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̓���?���Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ����?�
     */
    public CurveDerivative3D evaluation(double param) {
        param = parameterDomain().wrap(param);
        Axis2Placement3D ax = position();
        Vector3D ex = ax.x().multiply(Math.cos(param) * semiAxis1);
        Vector3D ey = ax.y().multiply(Math.sin(param) * semiAxis2);
        Vector3D ex1 = ax.x().multiply(-Math.sin(param) * semiAxis1);
        Vector3D ey1 = ax.y().multiply(Math.cos(param) * semiAxis2);

        Point3D d0 = ax.location().add(ex.add(ey));
        Vector3D d1 = ex1.add(ey1);
        Vector3D d2 = ex.add(ey).multiply(-1.0);
        Vector3D d3 = ex1.add(ey1).multiply(-1.0);

        return new CurveDerivative3D(d0, d1, d2, d3);
    }

    /**
     * �^����ꂽ�p���??[�^��Ԃɂ�����?A
     * ��Ԃ̗��[�싂Ԍ�����?łף�ꂽ�_�̃p���??[�^�l��?�߂�?B
     * <p/>
     * ���̃?�\�b�h��
     * {@link Conic3D#toPolyline(ParameterSection,ToleranceForDistance)
     * Conic3D.toPolyline(ParameterSection, ToleranceForDistance)}
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
    public PureBezierCurve3D[] toPolyBezierCurves(ParameterSection pint) {
        Ellipse2D this2D =
                (Ellipse2D) this.toLocal2D(Axis2Placement2D.origin);
        PureBezierCurve2D[] bzcs2D = this2D.toPolyBezierCurves(pint);
        return this.transformPolyBezierCurvesInLocal2DToGrobal3D(bzcs2D);
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
    public BsplineCurve3D toBsplineCurve(ParameterSection pint) {
        PureBezierCurve3D[] bzcs = this.toPolyBezierCurves(pint);
        boolean closed =
                (Math.abs(pint.increase()) >= GeometryUtils.PI2) ? true : false;

        return Conic3D.convertPolyBezierCurvesToOneBsplineCurve(bzcs, closed);
    }

    /**
     * ���̋�?�Ƒ��̋�?�Ƃ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * �����ȉ~�̂Ƃ���?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̉���?A
     * ��ȉ~�����ꕽ��?��?�BĂ���?A
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
    public IntersectionPoint3D[] intersect(ParametricCurve3D mate)
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
        boolean isPoly = poly.length < 4;
        int degree = xPoly.degree();
        double[] coef = new double[degree + 1];

        if (isPoly) {
            for (int j = 0; j <= degree; j++)
                coef[j] = (xPoly.getCoefficientAsDouble(j) / dAlrd2) +
                        (yPoly.getCoefficientAsDouble(j) / dAsrd2);
            coef[0] -= 1.0;
        } else {
            DoublePolynomial wPoly = (DoublePolynomial) poly[3].multiply(poly[3]);
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
    boolean checkSolution(Point3D point) {
        double param = getParameter(point);
        double px = xRadius() * Math.cos(param);
        double py = yRadius() * Math.sin(param);

        return point.identical(new CartesianPoint3D(px, py, 0.0));
        //return Math.abs(point.z()) < getToleranceForDistance();
    }

    /**
     * �^����ꂽ�_�����̋�?�?�ɂ����̂Ƃ���?A
     * ���̓_�̋�?�?�ł̃p���??[�^�l��?�߂�?B
     *
     * @param point ?��?��?ۂƂȂ�_
     * @return �p���??[�^�l
     */
    double getParameter(Point3D point) {
        double cos = point.x() / xRadius();
        if (cos > 1.0) cos = 1.0;
        if (cos < -1.0) cos = -1.0;
        double acos = Math.acos(cos);
        if (point.y() < 0.0) acos = 2 * Math.PI - acos;

        return acos;
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link Conic3D#intersectCnc(Conic3D,boolean)
     * Conic3D.intersectCnc(Conic3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Circle3D mate, boolean doExchange) {
        try {
            return intersectCnc(mate, doExchange);
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();    // Never be occured
        }
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�ȉ~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̉���?A
     * ��ȉ~�����ꕽ��?��?�BĂ���?A
     * ��ȉ~�̒�?S�Ԃ̋����������̋��e��?����?�����?A
     * ��ȉ~�̋�?� X ���̂Ȃ��p�x���p�x�̋��e��?����?�����?A
     * ��ȉ~�̋�?� X/Y �����ꂼ��ɂ��Ă̔��a��?��������̋��e��?��ȓ�ł���?�?��ɂ�?A
     * ��ȉ~�̓I?[�o?[���b�v���Ă����̂Ƃ���?A
     * IndefiniteSolutionException �̗�O��?�������?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link Conic3D#intersectCnc(Conic3D,boolean)
     * Conic3D.intersectCnc(Conic3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�ȉ~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException ��ȉ~�̓I?[�o?[���b�v���Ă���?A�⪕s��ł���
     */
    IntersectionPoint3D[] intersect(Ellipse3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return intersectCnc(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link Conic3D#intersectCnc(Conic3D,boolean)
     * Conic3D.intersectCnc(Conic3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Parabola3D mate, boolean doExchange) {
        try {
            return intersectCnc(mate, doExchange);
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();    // Never be occured
        }
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�o��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link Conic3D#intersectCnc(Conic3D,boolean)
     * Conic3D.intersectCnc(Conic3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�o��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Hyperbola3D mate, boolean doExchange) {
        try {
            return intersectCnc(mate, doExchange);
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();    // Never be occured
        }
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
     * {@link Polyline3D#intersect(Ellipse3D,boolean)
     * Polyline3D.intersect(Ellipse3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�|�����C��)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Polyline3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�g������?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �g������?�̃N���X��?u�g������?� vs. �ȉ~?v�̌�_���Z�?�\�b�h
     * {@link TrimmedCurve3D#intersect(Ellipse3D,boolean)
     * TrimmedCurve3D.intersect(Ellipse3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�g������?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(TrimmedCurve3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�Z�O�?���g) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�Z�O�?���g�̃N���X��?u��?���?�Z�O�?���g vs. �ȉ~?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurveSegment3D#intersect(Ellipse3D,boolean)
     * CompositeCurveSegment3D.intersect(Ellipse3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�Z�O�?���g)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(CompositeCurveSegment3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�N���X��?u��?���?� vs. �ȉ~?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurve3D#intersect(Ellipse3D,boolean)
     * CompositeCurve3D.intersect(Ellipse3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(CompositeCurve3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?��?A�^����ꂽ�x�N�g����?]�Bĕ�?s�ړ�������?��Ԃ�?B
     *
     * @param moveVec ��?s�ړ��̕��Ɨʂ�\���x�N�g��
     * @return ��?s�ړ���̋�?�
     */
    public ParametricCurve3D parallelTranslate(Vector3D moveVec) {
        return new Ellipse3D(position().parallelTranslate(moveVec), semiAxis1, semiAxis2);
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
     * @return {@link ParametricCurve3D#ELLIPSE_3D ParametricCurve3D.ELLIPSE_3D}
     */
    int type() {
        return ELLIPSE_3D;
    }

    /**
     * ���̋�?��?A�^����ꂽ��?�?W�n�� Z ���̎���?A
     * �^����ꂽ�p�x������]��������?��Ԃ�?B
     *
     * @param trns ��?�?W�n���瓾��ꂽ?W�ϊ����Z�q
     * @param rCos cos(��]�p�x)
     * @param rSin sin(��]�p�x)
     * @return ��]��̋�?�
     */
    ParametricCurve3D rotateZ(CartesianTransformationOperator3D trns,
                              double rCos, double rSin) {
        Axis2Placement3D rpos = position().rotateZ(trns, rCos, rSin);
        return new Ellipse3D(rpos, semiAxis1(), semiAxis2());
    }

    /**
     * ���̋�?�?�̓_��?A�^����ꂽ��?�?�ɂȂ��_���Ԃ�?B
     *
     * @param line ��?�
     * @return �^����ꂽ��?�?�ɂȂ��_
     */
    Point3D getPointNotOnLine(Line3D line) {
        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double dTol2 = condition.getToleranceForDistance2();

        double start = 0.0, increase = Math.PI / 2.0;
        int itry = 0, limit = 3;
        Point3D point;
        Vector3D vector;

        /*
        * Get a point which is not on the line and verify that
        * the distance between a point and the line is greater
        * than the tolerance.
        */
        do {
            if (itry > limit) {
                throw new FatalException();    // should never be occurred
            }
            point = this.coordinates(start + (increase * itry));
            vector = point.subtract(line.project1From(point));
            itry++;
        } while (point.isOn(line) || vector.norm() < dTol2);

        return point;
    }

    /**
     * ���̋�?��?A�������ꂽ�x�W�G�ȖʂƂ̊�?𒲂ׂ�?B
     * <p/>
     * ���̃?�\�b�h�� {@link IntsCncBzs3D IntsCncBzs3D} �̓Ք�Ŏg����?B
     * </p>
     *
     * @param bi �������ꂽ�x�W�G�Ȗʂ�?��
     * @return ��?��Ă���� true?A�����łȂ���� false
     */
    boolean checkInterfere(IntsCncBzs3D.BezierSurfaceInfo bi) {
        double dTol = getToleranceForDistance();
        if (!((bi.box.min().z() < -dTol) && (bi.box.max().z() > dTol)))
            return false;

        double ratio = this.yRadius() / this.xRadius();
        boolean all_in = true;
        boolean all_out = true;

        Point2D[] point = new Point2D[4];
        point[0] = new CartesianPoint2D(bi.box.min().x(), bi.box.min().y() / ratio);
        point[1] = new CartesianPoint2D(bi.box.max().x(), bi.box.min().y() / ratio);
        point[2] = new CartesianPoint2D(bi.box.max().x(), bi.box.max().y() / ratio);
        point[3] = new CartesianPoint2D(bi.box.min().x(), bi.box.max().y() / ratio);

        for (int i = 0; i < 4; i++) {
            double dist = point[i].toVector2D().length();
            if (dist < this.xRadius() - dTol)
                all_out = false;
            else if (dist > this.xRadius() + dTol)
                all_in = false;
            else {
                all_out = false;
                all_in = false;
            }
        }

        if (all_in == true)
            return false; /* no interfere */

        else if (all_out == true) {
            if ((bi.box.min().x() > (this.xRadius() + dTol))
                    || (bi.box.min().y() > (this.yRadius() + dTol))
                    || (bi.box.max().x() < (-(this.xRadius() + dTol)))
                    || (bi.box.max().y() < (-(this.yRadius() + dTol))))
                return false;  /* no interfere */
            if (((point[0].x() > 0.0) && (point[0].y() > 0.0))
                    || ((point[1].x() < 0.0) && (point[1].y() > 0.0))
                    || ((point[2].x() < 0.0) && (point[2].y() < 0.0))
                    || ((point[3].x() > 0.0) && (point[3].y() < 0.0)))
                return false;  /* no interfere */

            return true; /* interfere */
        } else
            return true; /* interfere */
    }

    /**
     * ���̉~??��?�Ɨ^����ꂽ���ʂ̌�_��?�߂�?B
     * <p/>
     * ���̃?�\�b�h�� {@link IntsCncBzs3D IntsCncBzs3D} �̓Ք�Ŏg����?B
     * </p>
     *
     * @param plane ����
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersectConicPlane(Plane3D plane) {
        Axis2Placement3D position =
                new Axis2Placement3D(Point3D.origin, null, null);
        Ellipse3D ellipse = new Ellipse3D(position,
                this.xRadius(),
                this.yRadius());
        try {
            return ellipse.intersect(plane);
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();
        }
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł� (���̉~??��?�̋�?�?W�n��ł�) ?W�l
     * ��Ԃ�?B
     *
     * @param parameter �p���??[�^�l
     * @return ���̉~??��?�̋�?�?W�n��ł�?W�l
     */
    Point3D nlFunc(double parameter) {
        double x = this.xRadius() * Math.cos(parameter);
        double y = this.yRadius() * Math.sin(parameter);
        double z = 0.0;

        return new CartesianPoint3D(x, y, z);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł� (���̉~??��?�̋�?�?W�n��ł�) ?ڃx�N�g��
     * ��Ԃ�?B
     *
     * @param parameter �p���??[�^�l
     * @return ���̉~??��?�̋�?�?W�n��ł�?ڃx�N�g��
     */
    Vector3D dnlFunc(double parameter) {
        double x = -this.xRadius() * Math.sin(parameter);
        double y = this.yRadius() * Math.cos(parameter);
        double z = 0.0;

        return new LiteralVector3D(x, y, z);
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
    protected synchronized ParametricCurve3D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator3D transformationOperator,
                  java.util.Hashtable transformedGeometries) {
        Axis2Placement3D tPosition =
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
        return new Ellipse3D(tPosition, tSemiAxis1, tSemiAxis2);
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
