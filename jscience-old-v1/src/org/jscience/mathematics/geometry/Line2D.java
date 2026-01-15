/*
 * �Q���� : ��?��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Line2D.java,v 1.3 2006/03/01 21:16:03 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

import java.io.PrintWriter;

/**
 * �Q���� : ��?��\���N���X?B
 * <p/>
 * ��?��?A?�?�̂����_ pnt �ƕ��x�N�g�� dir �Œ�`�����?B
 * </p>
 * <p/>
 * t ��p���??[�^�Ƃ��钼?� P(t) �̃p���?�g���b�N�\����?A�ȉ��̒ʂ�?B
 * <pre>
 * 	P(t) = pnt + t * dir
 * </pre>
 * </p>
 * <p/>
 * ��?��?u��?�?v�ł�?B
 * </p>
 * <p/>
 * ���̒�?�͖��Ȓ�?��\��?B
 * �L�Ȓ�?��\������?�?��ɂ�
 * {@link BoundedLine2D BoundedLine2D}
 * ��
 * {@link TrimmedCurve2D TrimmedCurve2D}
 * �����p�ł���?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:16:03 $
 */

public class Line2D extends ParametricCurve2D {
    /**
     * ?�?�̂����_?B
     * <p/>
     * �p���??[�^�l 0 �ɑΉ�����_?B
     * </p>
     *
     * @serial
     */
    private final Point2D pnt;

    /**
     * ���x�N�g��?B
     * <p/>
     * �p���??[�^�l 1 �ɑΉ�����_�� pnt + dir �ɂȂ�?B
     * </p>
     *
     * @serial
     */
    private final Vector2D dir;

    /**
     * �t�B?[���h��?ݒ肷��l�⻂̂܂ܗ^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * pnt ��p���??[�^�l 0 �̓_�Ƃ�?A
     * dir ����x�N�g���Ƃ��钼?��?�?�����?B
     * </p>
     * <p/>
     * dir �̑傫����?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?�����?�����?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param pnt ?�?�̂����_ (�p���??[�^�l 0 �ɑΉ�����)
     * @param dir ���x�N�g��
     * @see ConditionOfOperation
     * @see InvalidArgumentValueException
     */
    public Line2D(Point2D pnt, Vector2D dir) {
        super();

        if (dir.norm() < getToleranceForDistance2())
            throw new InvalidArgumentValueException();
        this.pnt = pnt;
        this.dir = dir;
    }

    /**
     * �ʉ߂����_��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * pnt1 ��p���??[�^�l 0 �̓_�Ƃ�?A
     * pnt2 ��p���??[�^�l 1 �̓_�Ƃ���
     * ��?��?�?�����?B
     * </p>
     * <p/>
     * pnt �� dir ��?A�ȉ��̂悤��?ݒ肳���?B
     * <pre>
     * 		pnt = pnt1
     * 		dir = pnt2 - pnt1
     * </pre>
     * </p>
     * <p/>
     * pnt1 �� pnt2 ��?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?��̉���?A
     * ����̓_�ƌ��Ȃ���?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param pnt1 ?�?�̂����_ (�p���??[�^�l 0 �ɑΉ�����)
     * @param pnt2 ?�?�̂����_ (�p���??[�^�l 1 �ɑΉ�����)
     * @see ConditionOfOperation
     * @see InvalidArgumentValueException
     * @see Point2D#identical(Point2D)
     */
    public Line2D(Point2D pnt1, Point2D pnt2) {
        super();

        if (pnt1.identical(pnt2))
            throw new InvalidArgumentValueException();
        this.pnt = pnt1;
        this.dir = pnt2.subtract(pnt1);
    }

    /**
     * ���̒�?���`���Ă���?�?�̂����_ (�p���??[�^�l 0 �ɑΉ�����_) ��Ԃ�?B
     *
     * @return ?�?�̂����_ (�p���??[�^�l 0 �ɑΉ�����_)
     */
    public Point2D pnt() {
        return this.pnt;
    }

    /**
     * ���̒�?���`���Ă�����x�N�g����Ԃ�?B
     * <p/>
     * ���̃x�N�g����?A���̒�?��?ڃx�N�g���ɓ�����?B
     * </p>
     *
     * @return ���x�N�g��
     */
    public Vector2D dir() {
        return this.dir;
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^��Ԃɂ�������?�ł̒��� (���̂�) ��Ԃ�?B
     *
     * @param pint ������?�߂�p���??[�^���
     * @return �w�肳�ꂽ�p���??[�^��Ԃɂ������?�̒���
     */
    public double length(ParameterSection pint) {
        return dir.length() * Math.abs(pint.increase());
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ?W�l
     */
    public Point2D coordinates(double param) {
        return pnt.add(dir.multiply(param));
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ�?B
     * <p/>
     * ��?��?ڃx�N�g����?A?�� dir �ɓ�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return ?ڃx�N�g��
     */
    public Vector2D tangentVector(double param) {
        return dir;
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̋ȗ���Ԃ�?B
     * <p/>
     * ��?�̋ȗ���?A?�� 0 �ł���?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return �ȗ�
     */
    public CurveCurvature2D curvature(double param) {
        return new CurveCurvature2D(0.0, Vector2D.zeroVector);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̓���?���Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ����?�
     */
    public CurveDerivative2D evaluation(double param) {
        return new CurveDerivative2D(coordinates(param),
                dir,
                Vector2D.zeroVector);
    }

    /**
     * ���̋�?�̓Hٓ_��Ԃ�?B
     * <p/>
     * ��?�ɂ͓Hٓ_�͑�?݂��Ȃ��̂�?A?�ɗv�f?� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return �Hٓ_�̔z��
     */
    public PointOnCurve2D[] singular() {
        return new PointOnCurve2D[0];
    }

    /**
     * ���̋�?�̕ϋȓ_��Ԃ�?B
     * <p/>
     * ��?�ɂ͕ϋȓ_�͑�?݂��Ȃ��̂�?A?�ɗv�f?� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return �ϋȓ_�̔z��
     */
    public PointOnCurve2D[] inflexion() {
        return new PointOnCurve2D[0];
    }

    /**
     * �^����ꂽ�_���炱�̋�?�ւ̓��e�_��?�߂�?B
     * <p/>
     * ���钼?�ւ̔C�ӂ̓_����̓��e�_��?��͕K�� 1 �ɂȂ�?B
     * </p>
     * <p/>
     * ���̃?�\�b�h��?A
     * {@link ParametricCurve2D ParametricCurve2D} �N���X��
     * ��?ۃ?�\�b�h�Ƃ���?錾����Ă����̂ł��邪?A
     * ���̃N���X�ɂ͓��e�_��?�߂�?�\�b�h�Ƃ���?A����
     * {@link #project1From(Point2D) project1From(Point2D)}
     * ������?B
     * project1From(Point2D) ��?A
     * ?u���e�_�̔z��?v�ł͂Ȃ�?A
     * �����������?u���e�_?v�⻂̂܂ܕԂ�?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_�̔z��
     * @see #project1From(Point2D)
     */
    public PointOnCurve2D[] projectFrom(Point2D point) {
        PointOnCurve2D[] proj = new PointOnCurve2D[1];
        proj[0] = project1From(point);
        return proj;
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?A�^����ꂽ��?��Œ�?�ߎ�����|�����C����Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����|�����C����?\?�����_��
     * ���̋�?��x?[�X�Ƃ��� PointOnCurve2D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     * <p/>
     * �Ȃ�?A
     * ���ʂƂ��ĕԂ����|�����C����?A
     * ���̒�?�̎w�肳�ꂽ��Ԃ�?u�ߎ�?v�ł͂Ȃ�?A������?u?Č�?v�ł���?B
     * ���̃?�\�b�h�̓Ք�ł� tol �̒l�͎Q?Ƃ��Ȃ�?B
     * </p>
     *
     * @param pint ��?�ߎ�����p���??[�^���
     * @param tol  �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ�?�ߎ�����|�����C��
     * @see PointOnCurve2D
     */
    public Polyline2D toPolyline(ParameterSection pint,
                                 ToleranceForDistance tol) {
        Point2D[] points = new Point2D[2];

        points[0] = new PointOnCurve2D(this, pint.start(), doCheckDebug);
        points[1] = new PointOnCurve2D(this, pint.end(), doCheckDebug);

        if (points[0].identical(points[1]))
            throw new ZeroLengthException();

        return new Polyline2D(points);
    }

    /**
     * ���̋�?�̎w��̋�Ԃ쵖���?Č�����L�? Bspline ��?��Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����L�? Bspline ��?��
     * �P����?���_?��� 2?A���[��?d�̃��j�t�H?[���ȃm�b�g���?��?B
     * </p>
     *
     * @param pint �L�? Bspline ��?��?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�? Bspline ��?�
     */
    public BsplineCurve2D toBsplineCurve(ParameterSection pint) {
        Point2D[] controlPoints = {this.coordinates(pint.start()),
                this.coordinates(pint.end())};
        double[] weights = {1.0, 1.0};

        return new BsplineCurve2D(BsplineKnot.quasiUniformKnotsOfLinearOneSegment,
                controlPoints, weights);
    }

    /**
     * ���̋�?�Ƒ��̋�?�̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ����Ҽ?��?A��?�I?[�o?[���b�v���Ă���?�?��ɂ�?A
     * �⪕s��ł���Ƃ��� IndefiniteSolutionException ��?�����?B
     * ��?�I?[�o?[���b�v���Ă���Ɣ��f����?�?��
     * {@link #intersect1Line(Line2D) intersect1Line(Line2D)}
     * �Ɠ��l�ł���?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException mate �Ҽ?��?A��?�̓I?[�o?[���b�v���Ă���?A�⪕s��ł���
     */
    public IntersectionPoint2D[] intersect(ParametricCurve2D mate)
            throws IndefiniteSolutionException {
        return mate.intersect(this, true);
    }

    /**
     * ���̒�?�Ƒ��̒�?�Ƃ� (�������) ��_��?�߂�?B
     * <p/>
     * ��?�?s�ł���?�?��ɂ� null ��Ԃ�?B
     * ��?�̕��x�N�g���̂Ȃ��p�x�� (�µ���̓� - ��) ��
     * ��?�?ݒ肳��Ă��鉉�Z?�?��?u�p�x�̋��e��?��ȓ�?v�ł����?A
     * ��?�͕�?s�ł����̂Ɣ��f����?B
     * </p>
     * <p/>
     * ������?A��?�?s�ł���?�?���?A
     * �����ꂩ�� pnt ���瑊��ւ̋�����
     * ��?�?ݒ肳��Ă��鉉�Z?�?��?u�����̋��e��?��ȓ�?v�ł����?A
     * ��?�̓I?[�o?[���b�v���Ă����̂Ƃ���?A
     * IndefiniteSolutionException �̗�O��?�������?B
     * </p>
     *
     * @param mate ���̒�?�
     * @return ��_
     * @throws IndefiniteSolutionException ��?�̓I?[�o?[���b�v���Ă���?A�⪕s��ł���
     * @see ConditionOfOperation
     * @see Vector2D#parallelDirection(Vector2D)
     */
    public IntersectionPoint2D intersect1Line(Line2D mate)
            throws IndefiniteSolutionException {
        Vector2D p = mate.pnt().subtract(pnt());
        double pa = p.zOfCrossProduct(dir());
        double pb = p.zOfCrossProduct(mate.dir());
        double dTol = getToleranceForDistance();

        if (dir().parallelDirection(mate.dir())) {
            if (Math.abs(pa / dir().length()) < dTol ||
                    Math.abs(pb / mate.dir().length()) < dTol) {
                // same line
                PointOnCurve2D pnt1 = new PointOnCurve2D(this, 0, doCheckDebug);
                PointOnCurve2D pnt2 = mate.project1From(pnt1);
                IntersectionPoint2D ip =
                        new IntersectionPoint2D(pnt1, pnt2, doCheckDebug);
                throw new IndefiniteSolutionException(ip);
            } else
                return null;    // parallel
        }

        double o = dir().zOfCrossProduct(mate.dir());
        return new IntersectionPoint2D(this, pb / o, mate, pa / o, doCheckDebug);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * �⪕s��ł���Ƃ��� IndefiniteSolutionException ��?�������?�?��
     * {@link #intersect1Line(Line2D) intersect1Line(Line2D)}
     * �Ɠ��l�ł���?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link #intersect1Line(Line2D) intersect1Line(Line2D)}
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint2D[] intersect(Line2D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        IntersectionPoint2D ints;
        if ((ints = intersect1Line(mate)) == null) {
            return new IntersectionPoint2D[0];
        }
        if (doExchange)
            ints = ints.exchange();
        IntersectionPoint2D[] sol = new IntersectionPoint2D[1];
        sol[0] = ints;

        return sol;
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �~�̃N���X��?u�~ vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link Circle2D#intersect(Line2D,boolean)
     * Circle2D.intersect(Line2D, boolean)}
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
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �ȉ~�̃N���X��?u�ȉ~ vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link Ellipse2D#intersect(Line2D,boolean)
     * Ellipse2D.intersect(Line2D, boolean)}
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
     * ���̋�?�Ƒ��̋�?� (�o��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �o��?�̃N���X��?u�o��?� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link Hyperbola2D#intersect(Line2D,boolean)
     * Hyperbola2D.intersect(Line2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�o��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Hyperbola2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
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
     * ��?�̃N���X��?u��?� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link Parabola2D#intersect(Line2D,boolean)
     * Parabola2D.intersect(Line2D, boolean)}
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
     * ���̋�?�Ƒ��̋�?� (�|�����C��) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �|�����C���̃N���X��?u�|�����C�� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link Polyline2D#intersect(Line2D,boolean)
     * Polyline2D.intersect(Line2D, boolean)}
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
     * ���̋�?�Ƒ��̋�?� (�x�W�G��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �x�W�G��?�̃N���X��?u�x�W�G��?� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link PureBezierCurve2D#intersect(Line2D,boolean)
     * PureBezierCurve2D.intersect(Line2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(PureBezierCurve2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�a�X�v���C����?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �a�X�v���C����?�̃N���X��?u�a�X�v���C����?� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link BsplineCurve2D#intersect(Line2D,boolean)
     * BsplineCurve2D.intersect(Line2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(BsplineCurve2D mate, boolean doExchange) {
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
     * {@link TrimmedCurve2D#intersect(Line2D,boolean)
     * TrimmedCurve2D.intersect(Line2D, boolean)}
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
     * ���̋�?�Ƒ��̋�?� (��?���?�Z�O�?���g) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�Z�O�?���g�̃N���X��?u��?���?�Z�O�?���g vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurveSegment2D#intersect(Line2D,boolean)
     * CompositeCurveSegment2D.intersect(Line2D, boolean)}
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
     * ���̋�?�Ƒ��̋�?� (��?���?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�̃N���X��?u��?���?� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurve2D#intersect(Line2D,boolean)
     * CompositeCurve2D.intersect(Line2D, boolean)}
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
     * <p/>
     * �Ȃ�?A
     * ���ʂƂ��ĕԂ���� Bspline ��?��?A
     * ���̒�?�̎w�肳�ꂽ��Ԃ̃I�t�Z�b�g��?u�ߎ�?v�ł͂Ȃ�?A
     * ������?u?Č�?v�ł���?B
     * ���̃?�\�b�h�̓Ք�ł� tol �̒l�͎Q?Ƃ��Ȃ�?B
     * </p>
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
        int uicp = 4;       // ?���_
        int uik = 2;        // �m�b�g�z��̗v�f?�

        // parameter section
        double start_param = pint.start();
        double end_param = start_param + pint.increase();
        double param_interval = pint.increase() / (uicp - 1);

        double[] knots = new double[2];
        int[] knot_multi = new int[2];

        knots[0] = 0.0;
        knot_multi[0] = 4;
        knots[1] = Math.abs(pint.increase());
        knot_multi[1] = 4;

        Vector2D offset_vector;

        // offset vector
        if (side == WhichSide.LEFT)
            offset_vector =
                    new LiteralVector2D(-1 * dir().y(), dir().x());
        else if (side == WhichSide.RIGHT)
            offset_vector =
                    new LiteralVector2D(dir().y(), -1 * dir().x());
        else
            throw new InvalidArgumentValueException();

        offset_vector = offset_vector.unitized();
        offset_vector = offset_vector.multiply(magni);

        if (pint.increase() < 0.0)
            offset_vector = offset_vector.reverse();

        // offset
        int i;
        double crnt_param;
        Point2D[] crnt_pnt = new Point2D[uicp];

        crnt_pnt[0] = coordinates(start_param);
        crnt_pnt[0] = crnt_pnt[0].add(offset_vector);

        for (i = 1, crnt_param = (start_param + param_interval);
             i < (uicp - 1);
             i++, crnt_param += param_interval) {
            crnt_pnt[i] = coordinates(crnt_param);
            crnt_pnt[i] = crnt_pnt[i].add(offset_vector);
        }

        crnt_pnt[i] = coordinates(end_param);
        crnt_pnt[i] = crnt_pnt[i].add(offset_vector);

        BsplineCurve2D bsc =
                new BsplineCurve2D(3, knot_multi, knots, crnt_pnt);

        return bsc;
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�I�t�Z�b�g������?��?A
     * �^����ꂽ��?��ŋߎ�����L��?��?�߂�?B
     * <p/>
     * ���ʂƂ��ĕԂ����L��?��?A
     * {@link BoundedLine2D BoundedLine2D}
     * �̃C���X�^���X�ł���?B
     * </p>
     * <p/>
     * �Ȃ�?A
     * ���ʂƂ��ĕԂ����L��?��?A
     * ���̒�?�̎w�肳�ꂽ��Ԃ̃I�t�Z�b�g��?u�ߎ�?v�ł͂Ȃ�?A
     * ������?u?Č�?v�ł���?B
     * ���̃?�\�b�h�̓Ք�ł� tol �̒l�͎Q?Ƃ��Ȃ�?B
     * </p>
     *
     * @param pint  �I�t�Z�b�g����p���??[�^���
     * @param magni �I�t�Z�b�g��
     * @param side  �I�t�Z�b�g�̌� (WhichSide.LEFT/RIGHT)
     * @param tol   �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ̃I�t�Z�b�g��?��ߎ�����L��?�
     * @see WhichSide
     */
    public BoundedCurve2D
    offsetByBoundedCurve(ParameterSection pint,
                         double magni,
                         int side,
                         ToleranceForDistance tol) {
        Vector2D lineDirection = this.tangentVector(0.0);
        Vector2D offsetVector;
        if (side == WhichSide.RIGHT)
            offsetVector = Vector2D.of(lineDirection.y(), -lineDirection.x());
        else
            offsetVector = Vector2D.of(-lineDirection.y(), lineDirection.x());
        offsetVector = offsetVector.unitized().multiply(magni);
        if (pint.increase() < 0.0)
            offsetVector = offsetVector.reverse();

        return new BoundedLine2D(this.coordinates(pint.start()).add(offsetVector),
                this.coordinates(pint.end()).add(offsetVector));
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
     * �^����ꂽ�_���炱�̒�?�ւ� (�������?݂���) ���e�_��?�߂�?B
     * <p/>
     * �Ք?��?�͈ȉ��̒ʂ�?B
     * <br>
     * (point - this.pnt) �� (this.dir �̒P�ʃx�N�g��) �̓�?ς̒l��
     * ���̒�?�ɑ΂��铊�e�_�̃p���??[�^�l�Ƃ�?A
     * ���̃p���??[�^�l��?�� PointOnCurve2D �̃C���X�^���X��Ԃ�?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_
     * @see #projectFrom(Point2D)
     */
    public PointOnCurve2D project1From(Point2D point) {
        /*
        * ALGORITHM
        *
        *                            point
        *                              x
        *                             /|
        *                            / |
        *                           /  |
        *                          /   |
        *                   theta /    |
        *                        /\    |   Line.pnt + ( k * Line.dir )
        *                 ------x------x-------->     (k: real)
        *                  Line.pnt    prj
        *
        *     evpp = vector of ( Line.pnt --> point )
        *     euvec = unit direction vector of Line
        *
        *     prj = Line.pnt + ( |evpp|*cos( theta ) * euvec )
        *         = Line.pnt + ( (evpp, euvec) * euvec )
        *         = Line.pnt + ( (dir / sqrt(|Line.dir|), euvec) ) *
        *                        ( Line.dir / sqrt(|Line.dir| )
        *         = Line.pnt + ( (dir, euvec) / |Line.dir| ) * Line.dir
        *                      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ parameter
        */

        // ��?�̎n�_���瓊�e���̓_�܂ł̃x�N�g����?�߂�
        Vector2D evpp = point.subtract(pnt);

        // �x�N�g���̓�?ς��瓊�e�_�̃p���??[�^��?�߂�
        double edot = dir.dotProduct(evpp);
        double param = edot / dir.norm();
        // ���e�_��?�߂�
        return new PointOnCurve2D(this, param, doCheckDebug);
    }

    /**
     * �^����ꂽ�_�����̒�?�̂ǂ��瑤�ɂ��邩��Ԃ�?B
     * <p/>
     * {@link WhichSide#ON        WhichSide.ON},
     * {@link WhichSide#RIGHT    WhichSide.RIGHT},
     * {@link WhichSide#LEFT    WhichSide.LEFT}
     * �̂����ꂩ�̒l��Ԃ�?B
     * </p>
     * <p/>
     * point ���炱�̒�?�ւ̋�����?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?��ȉ��ł���?�?��ɂ�
     * point �͂��̒�?�?��?�BĂ����̂Ƃ��� WhichSide.ON ��Ԃ�?B
     * </p>
     * <p/>
     * ���̃?�\�b�h��?A���̋�?�ƂƂ�ɂ܂Ƃ߂���ׂ��ł���?B
     * </p>
     *
     * @return �ǂ��瑤�ł��邩��?�?�
     * @see WhichSide
     */
    int pointIsWhichSide(Point2D point) {
        Vector2D eBAvec;    /* vector from line's start point to point */
        Vector2D uDir;    /* unitized vector of line */
        double ework;
        double d_tol = getToleranceForDistance();

        /*
        * vector from line's start point to point.
        * unitized vector of line.
        */
        eBAvec = point.subtract(pnt);
        uDir = dir.unitized();

        /*
        * 3rd compornent of cross product
        */
        ework = eBAvec.zOfCrossProduct(uDir);

        if (ework > d_tol)
            return (WhichSide.RIGHT);
        else if (ework < (-d_tol))
            return (WhichSide.LEFT);
        else
            return (WhichSide.ON);
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
     * @return {@link ParametricCurve2D#LINE_2D ParametricCurve2D.LINE_2D}
     */
    int type() {
        return LINE_2D;
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
        Point2D tPnt = this.pnt().transformBy(reverseTransform,
                transformationOperator, transformedGeometries);
        Vector2D tDir = this.dir().transformBy(reverseTransform,
                transformationOperator, transformedGeometries);
        return new Line2D(tPnt, tDir);
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
        writer.println(indent_tab + "\tpnt");
        pnt.output(writer, indent + 2);
        writer.println(indent_tab + "\tdir");
        dir.output(writer, indent + 2);
        writer.println(indent_tab + "End");
    }
}
