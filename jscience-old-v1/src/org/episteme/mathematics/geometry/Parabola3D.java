/*
 * �R���� : ��?��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Parabola3D.java,v 1.5 2006/05/20 23:25:51 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.mathematics.analysis.polynomials.DoublePolynomial;
import org.episteme.util.FatalException;

import java.io.PrintWriter;

/**
 * �R���� : ��?��\���N���X?B
 * <p/>
 * ��?��?A���̒��_�̈ʒu�Ƌ�?� X/Y ���̕�����?�?W�n
 * (�z�u?��?A{@link Axis2Placement3D Axis2Placement3D}) position ��
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
 * @version $Revision: 1.5 $, $Date: 2006/05/20 23:25:51 $
 */

public class Parabola3D extends Conic3D {

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
     * @param position  ��?S�Ƌ�?� X/Y/Z ���̕�����?�?W�n
     * @param focalDist ���_����?œ_�܂ł̋���
     * @see InvalidArgumentValueException
     */
    public Parabola3D(Axis2Placement3D position, double focalDist) {
        super(position);
        setFocalDist(focalDist);
    }

    /**
     * ���̕�?��?u���_?|?œ_�ԋ���?v��p����?A�^����ꂽ�ʒu�ƌX���łQ�����̕�?��?�?�����?B
     * <p/>
     * position �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param position �Q�����̕�?�̈ʒu�ƌX������?�?W�n
     * @return �Q�����̕�?�
     * @see InvalidArgumentValueException
     */
    Conic2D toLocal2D(Axis2Placement2D position) {
        return new Parabola2D(position, focalDist());
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
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ?W�l
     */
    public Point3D coordinates(double param) {
        Axis2Placement3D ax = position();
        Vector3D x = ax.x().multiply(param * param * focalDist);
        Vector3D y = ax.y().multiply(2 * param * focalDist);

        return ax.location().add(x.add(y));
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ?ڃx�N�g��
     */
    public Vector3D tangentVector(double param) {
        Axis2Placement3D ax = position();
        Vector3D x1 = ax.x().multiply(2 * param * focalDist);
        Vector3D y1 = ax.y().multiply(2 * focalDist);

        return x1.add(y1);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̋ȗ���Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return �ȗ�
     */
    public CurveCurvature3D curvature(double param) {
        Axis2Placement3D ax = position();
        double x1len = 2 * param * focalDist;
        double y1len = 2 * focalDist;
        double x2len = 2 * focalDist;
        double tlen = Math.sqrt(x1len * x1len + y1len * y1len);
        double crv = Math.abs(y1len * x2len) / (tlen * tlen * tlen);
        Vector3D ex1 = ax.x().multiply(x1len);
        Vector3D ey1 = ax.y().multiply(y1len);

        Vector3D tangent = ex1.add(ey1);
        // rotate tangent PI/2 around Z axis
        Vector3D nrmDir = tangent.crossProduct(ax.z());
        return new CurveCurvature3D(crv, nrmDir.unitized());
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̓���?���Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ����?�
     */
    public CurveDerivative3D evaluation(double param) {
        Axis2Placement3D ax = position();
        Vector3D ex = ax.x().multiply(param * param * focalDist);
        Vector3D ey = ax.y().multiply(2 * param * focalDist);
        Vector3D ex1 = ax.x().multiply(2 * param * focalDist);
        Vector3D ey1 = ax.y().multiply(2 * focalDist);
        Vector3D ex2 = ax.x().multiply(2 * focalDist);

        Point3D d0 = ax.location().add(ex.add(ey));
        Vector3D d1 = ex1.add(ey1);
        Vector3D zero = Vector3D.zeroVector;

        return new CurveDerivative3D(d0, d1, ex2, zero);
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
    public PureBezierCurve3D[] toPolyBezierCurves(ParameterSection pint) {
        Parabola2D this2D =
                (Parabola2D) this.toLocal2D(Axis2Placement2D.origin);
        PureBezierCurve2D[] bzcs2D = this2D.toPolyBezierCurves(pint);
        return this.transformPolyBezierCurvesInLocal2DToGrobal3D(bzcs2D);
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?Č�����L�?�a�X�v���C����?��Ԃ�?B
     *
     * @param pint ?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�?�a�X�v���C����?�
     */
    public BsplineCurve3D toBsplineCurve(ParameterSection pint) {
        PureBezierCurve3D[] bzcs = this.toPolyBezierCurves(pint);
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
     * ���?�ꕽ��?�ɂ���?A
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
    public IntersectionPoint3D[] intersect(ParametricCurve3D mate)
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
        boolean isPoly = poly.length < 4;
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
            DoublePolynomial xwPoly = (DoublePolynomial) poly[0].multiply(poly[3]);
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
    boolean checkSolution(Point3D point) {
        double param = getParameter(point);
        double px = focalDist() * param * param;
        double py = 2.0 * focalDist() * param;

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
        return point.y() / (2.0 * focalDist());
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
     */
    IntersectionPoint3D[] intersect(Ellipse3D mate, boolean doExchange) {
        try {
            return intersectCnc(mate, doExchange);
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();    // Never be occured
        }
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
     * <p/>
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̉���?A
     * ���?�ꕽ��?�ɂ���?A
     * ���?�̒��_�Ԃ̋����������̋��e��?����?�����?A
     * ���?�̋�?� X ���̂Ȃ��p�x���p�x�̋��e��?����?�����?A
     * ���?�̒��_?|?œ_�ԋ�����?��������̋��e��?��ȓ�ł���?�?��ɂ�?A
     * ���?�̓I?[�o?[���b�v���Ă����̂Ƃ���?A
     * IndefiniteSolutionException �̗�O��?�������?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException ���?�̓I?[�o?[���b�v���Ă���?A�⪕s��ł���
     */
    IntersectionPoint3D[] intersect(Parabola3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return intersectCnc(mate, doExchange);
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
     * �|�����C���̃N���X��?u�|�����C�� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link Polyline3D#intersect(Parabola3D,boolean)
     * Polyline3D.intersect(Parabola3D, boolean)}
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
     * �g������?�̃N���X��?u�g������?� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link TrimmedCurve3D#intersect(Parabola3D,boolean)
     * TrimmedCurve3D.intersect(Parabola3D, boolean)}
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
     * ��?���?�Z�O�?���g�̃N���X��?u��?���?�Z�O�?���g vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurveSegment3D#intersect(Parabola3D,boolean)
     * CompositeCurveSegment3D.intersect(Parabola3D, boolean)}
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
     * ��?���?�N���X��?u��?���?� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurve3D#intersect(Parabola3D,boolean)
     * CompositeCurve3D.intersect(Parabola3D, boolean)}
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
        return new Parabola3D(position().parallelTranslate(moveVec), focalDist);
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
     * @return {@link ParametricCurve3D#PARABOLA_3D ParametricCurve3D.PARABOLA_3D}
     */
    int type() {
        return PARABOLA_3D;
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
        return new Parabola3D(rpos, focalDist());
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

        double start = 0.0, increase = 1.0;
        int itry = 0, limit = 100;
        Point3D point;
        Vector3D vector;

        /*
        * Get a point which is not on the line, then verify that
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
            return false; /* no interference */

        if (bi.box.max().x() < -dTol)
            return false; /* no interference */

        boolean all_in = true;
        boolean all_out = true;

        Point2D point = null;
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    point = new CartesianPoint2D(bi.box.min().x(), bi.box.min().y());
                    break;
                case 1:
                    point = new CartesianPoint2D(bi.box.max().x(), bi.box.min().y());
                    break;
                case 2:
                    point = new CartesianPoint2D(bi.box.max().x(), bi.box.max().y());
                    break;
                case 3:
                    point = new CartesianPoint2D(bi.box.min().x(), bi.box.max().y());
                    break;
            }
            double epara = point.y() / (2.0 * this.focalDist());
            double ex = this.focalDist() * epara * epara;
            ex = point.x() - ex;

            if (ex < -dTol)
                all_in = false;
            else if (ex > dTol)
                all_out = false;
            else {
                all_out = false;
                all_in = false;
            }
        }

        if (all_in == true)
            return false; /* no interfere */

        else if (all_out == true) {
            return ((bi.box.min().y() * bi.box.max().y()) > 0.0)
                    ? false /* no interfere */
                    : true; /* interfere */
        } else
            return true;
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
        Parabola3D parabola = new Parabola3D(position,
                this.focalDist());
        try {
            return parabola.intersect(plane);
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
        double y = this.focalDist() * parameter;
        double x = y * parameter;
        y = 2.0 * y;
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
        double y = 2.0 * this.focalDist();
        double x = y * parameter;
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
        double tFocalDist;
        if (reverseTransform != true)
            tFocalDist = transformationOperator.transform(this.focalDist());
        else
            tFocalDist = transformationOperator.reverseTransform(this.focalDist());
        return new Parabola3D(tPosition, tFocalDist);
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
