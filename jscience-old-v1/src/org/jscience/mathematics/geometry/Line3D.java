/*
 * �R���� : ��?��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Line3D.java,v 1.3 2006/03/01 21:16:03 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

import org.jscience.util.FatalException;

import java.io.PrintWriter;

/**
 * �R���� : ��?��\���N���X?B
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
 * {@link BoundedLine3D BoundedLine3D}
 * ��
 * {@link TrimmedCurve3D TrimmedCurve3D}
 * �����p�ł���?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:16:03 $
 */

public class Line3D extends ParametricCurve3D {
    /**
     * ?�?�̂����_?B
     * <p/>
     * �p���??[�^�l 0 �ɑΉ�����_?B
     * </p>
     *
     * @serial
     */
    private final Point3D pnt;

    /**
     * ���x�N�g��?B
     * <p/>
     * �p���??[�^�l 1 �ɑΉ�����_�� pnt + dir �ɂȂ�?B
     * </p>
     *
     * @serial
     */
    private final Vector3D dir;

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
    public Line3D(Point3D pnt, Vector3D dir) {
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
     * @see Point3D#identical(Point3D)
     */
    public Line3D(Point3D pnt1, Point3D pnt2) {
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
    public Point3D pnt() {
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
    public Vector3D dir() {
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
    public Point3D coordinates(double param) {
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
    public Vector3D tangentVector(double param) {
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
    public CurveCurvature3D curvature(double param) {
        return new CurveCurvature3D(0.0, Vector3D.zeroVector);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̓���?���Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ����?�
     */
    public CurveDerivative3D evaluation(double param) {
        return new CurveDerivative3D(coordinates(param),
                dir,
                Vector3D.zeroVector,
                Vector3D.zeroVector);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̃��C����Ԃ�?B
     * <p/>
     * ��?�̃��C����?A?�� 0 �ł���?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return ���C��
     */
    public double torsion(double param) {
        return 0.0;
    }

    /**
     * ���̋�?�̓Hٓ_��Ԃ�?B
     * <p/>
     * ��?�ɂ͓Hٓ_�͑�?݂��Ȃ��̂�?A?�ɗv�f?� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return �Hٓ_�̔z��
     */
    public PointOnCurve3D[] singular() {
        return new PointOnCurve3D[0];
    }

    /**
     * ���̋�?�̕ϋȓ_��Ԃ�?B
     * <p/>
     * ��?�ɂ͕ϋȓ_�͑�?݂��Ȃ��̂�?A?�ɗv�f?� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return �ϋȓ_�̔z��
     */
    public PointOnCurve3D[] inflexion() {
        return new PointOnCurve3D[0];
    }

    /**
     * �^����ꂽ�_���炱�̋�?�ւ̓��e�_��?�߂�?B
     * <p/>
     * ���钼?�ւ̔C�ӂ̓_����̓��e�_��?��͕K�� 1 �ɂȂ�?B
     * </p>
     * <p/>
     * ���̃?�\�b�h��?A
     * {@link ParametricCurve3D ParametricCurve3D} �N���X��
     * ��?ۃ?�\�b�h�Ƃ���?錾����Ă����̂ł��邪?A
     * ���̃N���X�ɂ͓��e�_��?�߂�?�\�b�h�Ƃ���?A����
     * {@link #project1From(Point3D) project1From(Point3D)}
     * ������?B
     * project1From(Point3D) ��?A
     * ?u���e�_�̔z��?v�ł͂Ȃ�?A
     * �����������?u���e�_?v�⻂̂܂ܕԂ�?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_�̔z��
     * @see #project1From(Point3D)
     */
    public PointOnCurve3D[] projectFrom(Point3D point) {
        PointOnCurve3D[] proj = new PointOnCurve3D[1];
        proj[0] = project1From(point);
        return proj;
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?A�^����ꂽ��?��Œ�?�ߎ�����|�����C����Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����|�����C����?\?�����_��
     * ���̋�?��x?[�X�Ƃ��� PointOnCurve3D ��
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
     * @see PointOnCurve3D
     */
    public Polyline3D toPolyline(ParameterSection pint,
                                 ToleranceForDistance tol) {
        Point3D[] points = new Point3D[2];

        points[0] = new PointOnCurve3D(this, pint.start(), doCheckDebug);
        points[1] = new PointOnCurve3D(this, pint.end(), doCheckDebug);

        if (points[0].identical(points[1]))
            throw new ZeroLengthException();

        return new Polyline3D(points);
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
    public BsplineCurve3D toBsplineCurve(ParameterSection pint) {
        Point3D[] controlPoints = {this.coordinates(pint.start()),
                this.coordinates(pint.end())};
        double[] weights = {1.0, 1.0};

        return new BsplineCurve3D(BsplineKnot.quasiUniformKnotsOfLinearOneSegment,
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
     * {@link #intersect1Line(Line3D) intersect1Line(Line3D)}
     * �Ɠ��l�ł���?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException mate �Ҽ?��?A��?�̓I?[�o?[���b�v���Ă���?A�⪕s��ł���
     */
    public IntersectionPoint3D[] intersect(ParametricCurve3D mate)
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
     * @see Vector3D#parallelDirection(Vector3D)
     */
    public IntersectionPoint3D intersect1Line(Line3D mate)
            throws IndefiniteSolutionException {
        Vector3D p = mate.pnt().subtract(pnt());
        Vector3D pa = p.crossProduct(dir());
        Vector3D pb = p.crossProduct(mate.dir());
        double dTol = getToleranceForDistance();

        if (dir().parallelDirection(mate.dir())) {
            if (pa.length() / dir().length() < dTol ||
                    pb.length() / mate.dir().length() < dTol) {
                // same line
                PointOnCurve3D pnt1 = new PointOnCurve3D(this, 0, doCheckDebug);
                PointOnCurve3D pnt2 = mate.project1From(pnt1);
                IntersectionPoint3D ip =
                        new IntersectionPoint3D(pnt1, pnt2, doCheckDebug);
                throw new IndefiniteSolutionException(ip);
            } else
                return null;    // parallel
        }

        Vector3D o = dir().crossProduct(mate.dir());

        if (!o.parallelDirection(pa) || !o.parallelDirection(pb))
            return null;    // twisted

        double paramT = pb.length() / o.length();
        if (pb.dotProduct(o) < 0.0)
            paramT = -paramT;
        double paramM = pa.length() / o.length();
        if (pa.dotProduct(o) < 0.0)
            paramM = -paramM;
        return new IntersectionPoint3D(this, paramT,
                mate, paramM, doCheckDebug);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * �⪕s��ł���Ƃ��� IndefiniteSolutionException ��?�������?�?��
     * {@link #intersect1Line(Line3D) intersect1Line(Line3D)}
     * �Ɠ��l�ł���?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link #intersect1Line(Line3D) intersect1Line(Line3D)}
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersect(Line3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        IntersectionPoint3D ints;
        if ((ints = intersect1Line(mate)) == null) {
            return new IntersectionPoint3D[0];
        }
        if (doExchange)
            ints = ints.exchange();
        IntersectionPoint3D[] sol = new IntersectionPoint3D[1];
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
     * �~??��?�̃N���X��?u�~??��?� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link Conic3D#intersect(Line3D,boolean)
     * Conic3D.intersect(Line3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Circle3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�ȉ~ �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �~??��?�̃N���X��?u�~??��?� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link Conic3D#intersect(Line3D,boolean)
     * Conic3D.intersect(Line3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�ȉ~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Ellipse3D mate, boolean doExchange) {
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
     * �~??��?�̃N���X��?u�~??��?� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link Conic3D#intersect(Line3D,boolean)
     * Conic3D.intersect(Line3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�o��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Hyperbola3D mate, boolean doExchange) {
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
     * �~??��?�̃N���X��?u�~??��?� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link Conic3D#intersect(Line3D,boolean)
     * Conic3D.intersect(Line3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Parabola3D mate, boolean doExchange) {
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
     * {@link Polyline3D#intersect(Line3D,boolean)
     * Polyline3D.intersect(Line3D, boolean)}
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
     * ���̋�?�Ƒ��̋�?� (�x�W�G��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �x�W�G��?�̃N���X��?u�x�W�G��?� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link PureBezierCurve3D#intersect(Line3D,boolean)
     * PureBezierCurve3D.intersect(Line3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(PureBezierCurve3D mate, boolean doExchange) {
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
     * {@link BsplineCurve3D#intersect(Line3D,boolean)
     * BsplineCurve3D.intersect(Line3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(BsplineCurve3D mate, boolean doExchange) {
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
     * {@link TrimmedCurve3D#intersect(Line3D,boolean)
     * TrimmedCurve3D.intersect(Line3D, boolean)}
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
     * {@link CompositeCurveSegment3D#intersect(Line3D,boolean)
     * CompositeCurveSegment3D.intersect(Line3D, boolean)}
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
     * {@link CompositeCurve3D#intersect(Line3D,boolean)
     * CompositeCurve3D.intersect(Line3D, boolean)}
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
     * ���̋�?�Ƒ��̋Ȗʂ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋Ȗ�
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public IntersectionPoint3D[] intersect(ParametricSurface3D mate)
            throws IndefiniteSolutionException {
        return mate.intersect(this, true);
    }

    /**
     * ���̋�?�Ƒ��̋Ȗ� (��?͋Ȗ�) �̌�_��?�߂�?B
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?͋ȖʃN���X��?u��?͋Ȗ� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link ElementarySurface3D#intersect(Line3D,boolean)
     * ElementarySurface3D.intersect(Line3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (��?͋Ȗ�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersect(ElementarySurface3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋Ȗ� (����) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋Ȗ� (����)
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException this �� mate ��?�BĂ���?A�⪕s��ł���
     */
    public IntersectionPoint3D[] intersect(Plane3D mate)
            throws IndefiniteSolutionException {
        IntersectionPoint3D intersectionPoint = mate.intersect1(this);
        IntersectionPoint3D[] ints = {intersectionPoint.exchange()};
        return ints;
    }

    /**
     * ���̋�?�Ƒ��̋Ȗ� (����) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * {@link Plane3D#intersect1(Line3D) Plane3D.intersect1(Line3D)}
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException this �� mate ��?�BĂ���?A�⪕s��ł���
     */
    IntersectionPoint3D[] intersect(Plane3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        IntersectionPoint3D intersectionPoint = mate.intersect1(this);
        if (!doExchange) {
            intersectionPoint = intersectionPoint.exchange();
        }
        IntersectionPoint3D[] ints = {intersectionPoint};
        return ints;
    }

    /**
     * ���̋�?�Ƒ��̋Ȗ� (����) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ���ʂ̃N���X��?u���� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link SphericalSurface3D#intersect(Line3D,boolean)
     * SphericalSurface3D.intersect(Line3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(SphericalSurface3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋Ȗ� (�~����) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �~���ʂ̃N���X��?u�~���� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link CylindricalSurface3D#intersect(Line3D,boolean)
     * CylindricalSurface3D.intersect(Line3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�~����)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException this �� mate ��?�BĂ���?A�⪕s��ł���
     */
    IntersectionPoint3D[] intersect(CylindricalSurface3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋Ȗ� (�~??��) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �~??�ʂ̃N���X��?u�~??�� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link ConicalSurface3D#intersect(Line3D,boolean)
     * ConicalSurface3D.intersect(Line3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�~??��)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException this �� mate ��?�BĂ���?A�⪕s��ł���
     */
    IntersectionPoint3D[] intersect(ConicalSurface3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋Ȗ� (�x�W�G�Ȗ�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �x�W�G�Ȗʂ̃N���X��?u�x�W�G�Ȗ� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link PureBezierSurface3D#intersect(Line3D,boolean)
     * PureBezierSurface3D.intersect(Line3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�x�W�G�Ȗ�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(PureBezierSurface3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋Ȗ� (�a�X�v���C���Ȗ�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �a�X�v���C���Ȗʂ̃N���X��?u�a�X�v���C���Ȗ� vs. ��?�?v�̌�_���Z�?�\�b�h
     * {@link BsplineSurface3D#intersect(Line3D,boolean)
     * BsplineSurface3D.intersect(Line3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�a�X�v���C���Ȗ�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(BsplineSurface3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * �^����ꂽ�_���炱�̒�?�ւ� (�������?݂���) ���e�_��?�߂�?B
     * <p/>
     * �Ք?��?�͈ȉ��̒ʂ�?B
     * <br>
     * (point - this.pnt) �� (this.dir �̒P�ʃx�N�g��) �̓�?ς̒l��
     * ���̒�?�ɑ΂��铊�e�_�̃p���??[�^�l�Ƃ�?A
     * ���̃p���??[�^�l��?�� PointOnCurve3D �̃C���X�^���X��Ԃ�?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_
     * @see #projectFrom(Point3D)
     */
    public PointOnCurve3D project1From(Point3D point) {
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
        *                        /\    |   Line.pnt + ( param * Line.dir )
        *                 ------x------x-------->     (param: real)
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
        Vector3D evpp = point.subtract(pnt);

        // �x�N�g���̓�?ς��瓊�e�_�̃p���??[�^��?�߂�
        double edot = dir.dotProduct(evpp);
        double param = edot / dir.norm();

        // ���e�_��?�߂�
        return new PointOnCurve3D(this, param, doCheckDebug);
    }

    /**
     * ���̋�?��?A�^����ꂽ�x�N�g����?]�Bĕ�?s�ړ�������?��Ԃ�?B
     *
     * @param moveVec ��?s�ړ��̕��Ɨʂ�\���x�N�g��
     * @return ��?s�ړ���̋�?�
     */
    public ParametricCurve3D parallelTranslate(Vector3D moveVec) {
        return new Line3D(pnt().add(moveVec), dir());
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
     * @return {@link ParametricCurve3D#LINE_3D ParametricCurve3D.LINE_3D}
     */
    int type() {
        return LINE_3D;
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
        Point3D rpnt = pnt().rotateZ(trns, rCos, rSin);
        Vector3D rdir = dir().rotateZ(trns, rCos, rSin);
        return new Line3D(rpnt, rdir);
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
     * ���̒�?��?A�^����ꂽ��?�?W�n�� XY ���ʂɓ��e���ē񎟌���������?��Ԃ�?B
     *
     * @param transform ��?�?W�n���瓾��ꂽ?W�ϊ����Z�q
     * @return �񎟌���������?�
     */
    Line2D toLocal2D(CartesianTransformationOperator3D transform) {
        return new Line2D(pnt().to2D(transform),
                dir().to2D(transform));
    }

    /**
     * ���̒�?��?A�^����ꂽ��?�̋��ʖ@?��?�߂�?B
     * <p/>
     * ���ʂƂ��ē�����z��̗v�f?��� 2 �ł���?B
     * ?�?��̗v�f�ɂ��̒�?�?�̓_?A
     * ��Ԗڂ̗v�f�ɗ^����ꂽ��?�?�̓_�����?B
     * </p>
     * <p/>
     * ��?�̕��x�N�g���̂Ȃ��p�x�� (�µ���̓� - ��) ��
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̊p�x�̋��e��?����?��������?A
     * ��?�͕�?s�ł����̂Ɣ��f��?A
     * IndefiniteSolutionException �̗�O��Ԃ�?B
     * </p>
     *
     * @param mate ��?�
     * @return ���ʖ@?�̒[�_
     * @throws IndefiniteSolutionException �⪕s��ł��� (��?�?s�ł���)
     */
    PointOnCurve3D[] commonNormal(Line3D mate) throws IndefiniteSolutionException {
        Vector3D thisUnitVec = this.dir().unitized();
        Vector3D mateUnitVec = mate.dir().unitized();
        double aTol = getToleranceForAngle();

        if (Math.abs(thisUnitVec.dotProduct(mateUnitVec)) > Math.cos(aTol)) {
            throw new IndefiniteSolutionException(this);
        }
        Vector3D crossVec = thisUnitVec.crossProduct(mateUnitVec);
        Axis2Placement3D position;

        Vector3D aNormVec = thisUnitVec.crossProduct(crossVec);
        position = new Axis2Placement3D(this.pnt(),
                aNormVec, aNormVec.verticalVector());
        Plane3D planeA = new Plane3D(position);
        IntersectionPoint3D[] planeAlineB;
        try {
            planeAlineB = planeA.intersect(mate);
        } catch (IndefiniteSolutionException e) {
            throw e;
        }

        Vector3D bNormVec = mateUnitVec.crossProduct(crossVec);
        position = new Axis2Placement3D(mate.pnt(),
                bNormVec, bNormVec.verticalVector());
        Plane3D planeB = new Plane3D(position);
        IntersectionPoint3D[] planeBlineA;
        try {
            planeBlineA = planeB.intersect(this);
        } catch (IndefiniteSolutionException e) {
            throw e;
        }

        PointOnCurve3D[] point = new PointOnCurve3D[2];
        point[0] = planeAlineB[0].pointOnCurve2();
        point[1] = planeBlineA[0].pointOnCurve2();

        return point;
    }

    /**
     * ���̒�?��?A�^����ꂽ��?�̂Ȃ��p�x��?�߂�?B
     *
     * @param mate ��?�
     * @return ��?�̂Ȃ��p�x
     */
    double angleWith(Line3D mate) {
        Vector3D thisVec = this.dir().unitized();
        Vector3D mateVec = mate.dir().unitized();
        double cosAB = thisVec.dotProduct(mateVec);

        if (cosAB > 1.0)
            cosAB = 1.0;
        if (cosAB < -1.0)
            cosAB = -1.0;

        return Math.acos(cosAB);
    }

    /**
     * �^����ꂽ�_���炱�̒�?�ւ̋�����?�߂�?B
     *
     * @param mate �_
     * @return �_���炱�̒�?�ւ̋���
     */
    double distanceFrom(Point3D point) {
        Vector3D subVec = point.toVector3D().subtract(this.pnt().toVector3D());
        Vector3D unitVec = this.dir().unitized();
        Vector3D crossVec = unitVec.crossProduct(subVec);

        return Math.sqrt(crossVec.norm());
    }

    /**
     * ���̒�?��?A�^����ꂽ��?�Ƃ̋�����?�߂�?B
     * <p/>
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̉���?A
     * ��?�̕��x�N�g���̂Ȃ��p�x���� (�µ���̓� - ��) ��
     * �p�x�̋��e��?����?�����?A
     * ���̒�?�?�̂����_����^����ꂽ��?�ւ̋�����
     * �����̋��e��?����?�����?�?��ɂ�?A
     * ���̋�����Ԃ��̂ł͂Ȃ�?A
     * IndefiniteSolutionException �̗�O��?�����?B
     * </p>
     *
     * @param mate ��?�
     * @return ��?�Ԃ̋���
     * @throws IndefiniteSolutionException ��?�̋����������̋��e��?����?�����
     * @see #distanceFrom(Point3D)
     */
    double distanceFrom(Line3D mate) throws IndefiniteSolutionException {
        Vector3D thisVec = this.dir().unitized();
        Vector3D mateVec = mate.dir().unitized();
        Vector3D crossVec = thisVec.crossProduct(mateVec);

        double aTol = getToleranceForAngle();
        double aTol2 = aTol * aTol;
        double dTol = getToleranceForDistance();
        double dist;

        if (crossVec.norm() < aTol2) {
            // Parallel
            dist = mate.distanceFrom(this.pnt());
            if (Math.abs(dist) < dTol)
                // overlap
                throw new IndefiniteSolutionException(this);
            return dist;
        }

        Vector3D normVec = this.dir().crossProduct(mate.dir());
        Vector3D axis = normVec.crossProduct(this.dir());
        Axis2Placement3D position =
                new Axis2Placement3D(this.pnt(), axis, this.dir());
        Plane3D plane = new Plane3D(position);
        IntersectionPoint3D[] point = mate.intersect(plane);
        if (point.length != 1) throw new FatalException();

        return this.distanceFrom(point[0]);
    }

    /**
     * �������ꂽ�x�W�G�ȖʂƂ̊�?𒲂ׂ�
     *
     * @param bi �x�W�G�Ȗ�?��
     * @return ��?����邩�ǂ���
     */
    boolean checkInterfere(IntsCncBzs3D.BezierSurfaceInfo bi) {
        double dTol = getToleranceForDistance();
        if (!(bi.box.min().y() < dTol))
            return false;
        if (!(bi.box.min().z() < dTol))
            return false;
        if (!(bi.box.max().y() > -dTol))
            return false;
        if (!(bi.box.max().z() > -dTol))
            return false;

        return true;
    }

    /**
     * nlFunc
     */
    Point3D nlFunc(double parameter) {
        double ework = Math.sqrt(this.dir().norm());
        Line3D line =
                new Line3D(Point3D.origin,
                        new LiteralVector3D(ework, 0, 0));

        double x = line.dir().x() * parameter;
        double y = 0.0;
        double z = 0.0;

        return new CartesianPoint3D(x, y, z);
    }

    /**
     * dnlFunc
     */
    Vector3D dnlFunc(double parameter) {
        double ework = Math.sqrt(this.dir().norm());

        return new LiteralVector3D(ework, 0, 0);
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
        Point3D tPnt = this.pnt().transformBy(reverseTransform,
                transformationOperator, transformedGeometries);
        Vector3D tDir = this.dir().transformBy(reverseTransform,
                transformationOperator, transformedGeometries);
        return new Line3D(tPnt, tDir);
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
