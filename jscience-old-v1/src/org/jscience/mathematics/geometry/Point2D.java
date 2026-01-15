/*
 * �Q�����̓_��\����?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Point2D.java,v 1.2 2006/03/01 21:16:07 virtualcall Exp $
 *
 */

package org.jscience.mathematics.geometry;

import org.jscience.util.FatalException;

/**
 * �Q�����̓_��\����?ۃN���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:16:07 $
 * @see Vector2D
 */

public abstract class Point2D extends AbstractPoint {
    /**
     * �Q�����̌��_ (0, 0)?B
     */
    public static final Point2D origin;

    /**
     * static �ȃt�B?[���h�ɒl��?ݒ肷��?B
     */
    static {
        origin = new CartesianPoint2D(0.0, 0.0);
    }

    /**
     * �I�u�W�F�N�g��?\�z����?B
     */
    protected Point2D() {
        super();
    }

    /**
     * ������Ԃ�?B
     * <p/>
     * ?�� 2 ��Ԃ�?B
     * </p>
     *
     * @return �Q�����Ȃ̂�?A?�� 2
     */
    public int dimension() {
        return 2;
    }

    /**
     * �Q�������ۂ���Ԃ�
     * <p/>
     * <p/>
     * ?�� true ��Ԃ�?B
     * </p>
     *
     * @return �Q�����Ȃ̂�?A?�� <code>true</code>
     */
    public boolean is2D() {
        return true;
    }

    /**
     * �_�� X ?W�l��Ԃ���?ۃ?�\�b�h?B
     *
     * @return �_�� X ?W�l
     */
    public abstract double x();

    /**
     * �_�� Y ?W�l��Ԃ���?ۃ?�\�b�h?B
     *
     * @return �_�� Y ?W�l
     */
    public abstract double y();

    /**
     * ���̓_�ɗ^����ꂽ�x�N�g���𑫂����_��Ԃ�?B
     *
     * @param vector �_�ɑ����x�N�g��
     * @return �^����ꂽ�x�N�g���𑫂����_ (this + vector)
     */
    public Point2D add(Vector2D vector) {
        return new CartesianPoint2D(x() + vector.x(),
                y() + vector.y());
    }

    /**
     * ���̓_����^����ꂽ�x�N�g����򢂽�_��Ԃ�?B
     *
     * @param vector �_�����x�N�g��
     * @return �^����ꂽ�x�N�g����򢂽�_ (this - vector)
     */
    public Point2D subtract(Vector2D vector) {
        return new CartesianPoint2D(x() - vector.x(),
                y() - vector.y());
    }

    /**
     * ���̓_�Ɨ^����ꂽ�_�Ƃ�?���Ԃ�?B
     *
     * @param mate ?����鑊��̓_
     * @return ��_��?� (this - mate)
     */
    public Vector2D subtract(Point2D mate) {
        return new LiteralVector2D(x() - mate.x(),
                y() - mate.y());
    }

    /**
     * ���̓_�ɗ^����ꂽ�X�P?[����?悶���_��Ԃ�?B
     *
     * @param scale �X�P?[��
     * @return (this * scale)
     */
    public Point2D multiply(double scale) {
        return new CartesianPoint2D(x() * scale,
                y() * scale);
    }

    /**
     * ���̓_��^����ꂽ�X�P?[���Ŋ��B��_��Ԃ�?B
     *
     * @param scale �X�P?[��
     * @return (this / scale)
     */
    public Point2D divide(double scale) {
        return new CartesianPoint2D(x() / scale,
                y() / scale);
    }

    /**
     * ���̓_�Ɨ^����ꂽ�_��?�^��Ԃ������ʂ�Ԃ�?B
     *
     * @param mate          ?�`��Ԃ̑���ƂȂ�_
     * @param weightForThis ��?g�ɑ΂���?d�� (����ɑ΂���?d�݂� 1 - weightForThis)
     * @return ?�`��Ԃ������ʂ̓_ (weightForThis * this + (1 - weightForThis) * mate)
     */
    public Point2D linearInterpolate(Point2D mate,
                                     double weightForThis) {
        double weightForMate = 1.0 - weightForThis;
        return new CartesianPoint2D(this.x() * weightForThis + mate.x() * weightForMate,
                this.y() * weightForThis + mate.y() * weightForMate);
    }

    /**
     * ���̓_�Ɨ^����ꂽ�_�̒��_��Ԃ�?B
     *
     * @param mate ���_��?�߂鑊��ƂȂ�_
     * @return ���_ (0.5 * this + 0.5 * mate)
     */
    public Point2D midPoint(Point2D mate) {
        return linearInterpolate(mate, 0.5);
    }

    /**
     * �Q�_�̓���?��𔻒肷��?B
     *
     * @param mate ����̑�?ۂƂȂ�_
     * @return this �� mate ��?u�����̋��e��?�?v�ȓ��
     *         ����̓_�ł���Ƃ݂Ȃ���� true?A����Ȃ��� false
     * @see ConditionOfOperation
     */
    public boolean identical(Point2D mate) {
        return distance2(mate) < getToleranceForDistance2();
    }

    /**
     * ���̓_��Q�����̃x�N�g�� (Vector2D) �ɕϊ�����?B
     *
     * @return ���_����̃x�N�g���Ƃ݂Ȃ����x�N�g��
     */
    public Vector2D toVector2D() {
        return new LiteralVector2D(x(), y());
    }

    /**
     * ���̓_����^����ꂽ��?�ւ̓��e�_��?�߂�?B
     *
     * @param mate ���e��?ۂ̋�?�
     * @return �w�肳�ꂽ��?�ւ̓��e�_ (��?݂��Ȃ��Ƃ��͒���0�̔z���Ԃ�)
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public PointOnCurve2D[] project(ParametricCurve2D mate)
            throws IndefiniteSolutionException {
        return mate.projectFrom(this);
    }

    /**
     * ���̓_���^����ꂽ��?��?��?�BĂ��邩�ǂ�����Ԃ�?B
     *
     * @param mate �����?ۂ̋�?�
     * @return ��?��?��?�BĂ���� true?A�����łȂ���� false
     */
    public boolean isOn(ParametricCurve2D mate) {
        PointOnCurve2D prjp = mate.nearestProjectFrom(this);

        if (prjp != null && identical(prjp))
            return true;

        if (false) {    // �K�v��??
            if (mate.isFinite() && mate.isNonPeriodic()) {    // �L�Ŕ���I
                // mate must be a bounded curve
                BoundedCurve2D bnd = (BoundedCurve2D) mate;

                if (identical(bnd.startPoint()))
                    return true;
                if (identical(bnd.endPoint()))
                    return true;
            }
        }

        return false;
    }

    /**
     * ���̓_��?u���Ȋ�?��Ȃ���?�?v�̓Ѥ�ɂ��邩�ǂ�����Ԃ�?B
     * <p/>
     * �_����?��?�BĂ���?�?��ɂ�?u�Ѥ�ɂ���?v��̂Ƃ���?B
     * </p>
     * <p/>
     * �Ȃ�?Amate �����Ȋ�?��Ȃ����Ƃ�?A�Ă�?o�����ŕ�?؂���K�v������?B
     * </p>
     *
     * @param mate �����?ۂ̕�?�
     * @return ��?�̓Ѥ�ɂ���� true?A�����łȂ���� false
     * @throws OpenCurveException mate �͊J������?�ł���
     */
    public boolean isIn(ParametricCurve2D mate) throws OpenCurveException {
        if (mate.isClosed() != true)
            throw new OpenCurveException("mate is open");

        if (this.isOn(mate) == true) {
            return true;
        }

        return this.pointIsInsideOf(mate);
    }

    /**
     * ���̓_��?u���Ȋ�?��Ȃ���?�?v�̓Ѥ�ɂ��邩�ǂ�����Ԃ�?B
     * <p/>
     * �_����?��?�BĂ���?�?��ɂ�?u�O���ɂ���?v��̂Ƃ���?B
     * </p>
     * <p/>
     * �Ȃ�?Amate �����Ȋ�?��Ȃ����Ƃ�?A�Ă�?o�����ŕ�?؂���K�v������?B
     * </p>
     *
     * @param mate �����?ۂ̕�?�
     * @return ��?�̓Ѥ�ɂ���� true?A�����łȂ���� false
     * @throws OpenCurveException mate �͊J������?�ł���
     */
    public boolean isInsideOf(ParametricCurve2D mate) throws OpenCurveException {
        if (mate.isClosed() != true)
            throw new OpenCurveException("mate is open");

        if (this.isOn(mate) == true) {
            return false;
        }

        return this.pointIsInsideOf(mate);
    }

    /**
     * ���̓_��?u���Ȋ�?��Ȃ���?�?v�̓Ѥ�ɂ��邩�ǂ�����Ԃ�?B
     * <p/>
     * �Ȃ�?Amate �����Ȋ�?��Ȃ����Ƃ�?A�Ă�?o�����ŕ�?؂���K�v������?B
     * </p>
     *
     * @param mate �����?ۂ̕�?�
     * @return ��?�̓Ѥ�ɂ���� true?A�����łȂ���� false
     * @throws OpenCurveException mate �͊J������?�ł���
     */
    private boolean pointIsInsideOf(ParametricCurve2D mate) throws OpenCurveException {
        int inside = 0;
        int outside = 0;

        for (int i = 0; i < 3; i++) {
            Line2D lin =
                    new Line2D(this, new LiteralVector2D(1.0, (i * 0.1)));

            try {
                IntersectionPoint2D[] ints = lin.intersect(mate);
                int nPositiveInts = 0;
                for (int j = 0; j < ints.length; j++) {
                    if (ints[j].pointOnCurve1().parameter() > 0.0)
                        nPositiveInts++;
                }
                if ((nPositiveInts % 2) == 1) {
                    inside++;
                } else {
                    outside++;
                }
            } catch (IndefiniteSolutionException e) {
            }
        }

        return (inside > outside) ? true : false;
    }

    /**
     * ���̓_�Ɨ^����ꂽ�_�Ƃ̊Ԃ̋�����Ԃ�?B
     *
     * @param mate ������?�߂�_
     * @return this - mate �Ԃ̋���
     */
    public double distance(Point2D mate) {
        return Math.sqrt(distance2(mate));
    }

    /**
     * ���̓_�Ɨ^����ꂽ�_�Ƃ̊Ԃ̋����̎�?��Ԃ�?B
     *
     * @param mate �����̎�?��?�߂�_
     * @return this - mate �Ԃ̋����̎�?�
     */
    public double distance2(Point2D mate) {
        double dx, dy;

        dx = x() - mate.x();
        dy = y() - mate.y();

        return (dx * dx + dy * dy);
    }

    /**
     * �^����ꂽ�_��̒���?A���̓_����?łɓ���_��Ԃ�?B
     *
     * @param pnts �_��
     * @return ?łɓ���_
     */
    public Point2D longestPoint(Point2D[] pnts) {
        return longestPoint(pnts, 0, pnts.length - 1);
    }

    /**
     * �^����ꂽ�_��̎w��͈̔͂̒���?A���̓_����?łɓ���_��Ԃ�?B
     *
     * @param pnts  �_��
     * @param start �J�n�_�̃C���f�b�N�X (0 ~ pnts.length - 1)
     * @param end   ?I���_�̃C���f�b�N�X (0 ~ pnts.length - 1, start <= end)
     * @return ?łɓ���_
     */
    public Point2D longestPoint(Point2D[] pnts,
                                int start, int end) {
        double max_dist, dist;
        int index;

        if ((start < 0) || (end >= pnts.length)) {
            throw new InvalidArgumentValueException();
        }

        max_dist = distance2(pnts[start]);
        index = start;

        for (int i = start + 1; i <= end; i++) {
            if ((dist = distance2(pnts[i])) > max_dist) {
                max_dist = dist;
                index = i;
            }
        }

        return pnts[index];
    }

    /**
     * �_�Q��?�����?�̕��x�N�g����Ԃ�?B
     * <p/>
     * (start == end) ��?�?��ɂ�?A�[�?�x�N�g����Ԃ�?B
     * </p>
     *
     * @param points �_�Q
     * @param start  �J�n�_�̃C���f�b�N�X (0 ~ points.length - 1)
     * @param end    ?I���_�̃C���f�b�N�X (0 ~ points.length - 1, start <= end)
     * @return ��?�̒P�ʕ��x�N�g��
     */
    static Vector2D collinear(Point2D[] points,
                              int start, int end) {
        if ((start < 0) || (points.length <= end)) {
            throw new InvalidArgumentValueException();
        }

        if ((end - start) <= 1) {
            return Vector2D.zeroVector;
        }

        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double dTol = condition.getToleranceForDistance();
        double dTol2 = dTol * dTol;

        Point2D basisPoint = points[start];
        Point2D longestPoint =
                basisPoint.longestPoint(points, start + 1, end);
        Vector2D uax = longestPoint.subtract(basisPoint);

        if (uax.norm() < dTol2) {
            return Vector2D.zeroVector;
        }
        uax = uax.unitized();

        Vector2D evec;
        double ecrs;

        for (int i = start + 1; i <= end; i++) {
            evec = points[i].subtract(points[start]);
            ecrs = evec.zOfCrossProduct(uax);
            if ((ecrs * ecrs) > dTol2) {
                return null;
            }
        }

        return uax;
    }

    /**
     * �O�_����̓������_��?�߂�?B
     * <p/>
     * pnt2, pnt3 �̂����ꂩ�� null ��?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     * <p/>
     * �܂�?A�O�_����?�?�Ԃɂ���?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     *
     * @param pnt2 �_2
     * @param pnt3 �_3
     * @return this, pnt2, pnt3 ����̓������_
     */
    public Point2D center(Point2D pnt2, Point2D pnt3) {
        // (pnt1, pnt2) �̒��_��ʂ�?Aline(pnt1, pnt2) ��?����Ȓ�?��?A
        // (pnt2, pnt3) �̒��_��ʂ�?Aline(pnt2, pnt3) ��?����Ȓ�?�̌�_��?A
        // 3 �_����̓������_

        if ((pnt2 == null) || (pnt3 == null))
            throw new InvalidArgumentValueException("There is a null argument.");

        if (identical(pnt2) || identical(pnt3) || pnt2.identical(pnt3))
            throw new InvalidArgumentValueException("Points are collinear.");

        Vector2D vec2 = subtract(pnt2);
        Vector2D vec3 = subtract(pnt3);

        if (vec2.identicalDirection(vec3))
            throw new InvalidArgumentValueException("Points are collinear.");

        Line2D line2 = new Line2D(linearInterpolate(pnt2, 0.5),
                vec2.verticalVector());

        Line2D line3 = new Line2D(linearInterpolate(pnt3, 0.5),
                vec3.verticalVector());

        try {
            IntersectionPoint2D isec = line2.intersect1Line(line3);
            return isec.coordinates();
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();
        }
    }

    /**
     * �O�_����̓������_��?�߂�?B
     * <p/>
     * pnt1, pnt2, pnt3 �̂����ꂩ�� null ��?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     * <p/>
     * �܂�?A�O�_����?�?�Ԃɂ���?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     *
     * @param pnt1 �_1
     * @param pnt2 �_2
     * @param pnt3 �_3
     * @return �O�_����̓������_
     */
    public static Point2D center(Point2D pnt1,
                                 Point2D pnt2,
                                 Point2D pnt3) {
        if (pnt1 == null)
            throw new InvalidArgumentValueException("There is a null argument.");
        return pnt1.center(pnt2, pnt3);
    }

    /**
     * ���̓_ (x, y) �� z = 0 �Ƃ���?A3D ������?B
     *
     * @return 3D �������_ (x, y, 0)
     */
    Point3D to3D() {
        return new CartesianPoint3D(x(), y(), 0.0);
    }

    /**
     * ���̓_ (x, y) �� 3D ������?B
     *
     * @param z Z �̒l
     * @return 3D �������_ (x, y, z)
     */
    Point3D to3D(double z) {
        return new CartesianPoint3D(x(), y(), z);
    }

    /**
     * ���̓_�� CartesianPoint2D �̃I�u�W�F�N�g�ɕϊ�����?B
     *
     * @return �ϊ������_
     */
    CartesianPoint2D literal() {
        return new CartesianPoint2D(x(), y());
    }

    /**
     * ���̓_��?A�^����ꂽ�􉽓I�ϊ����Z�q�ŕϊ�����?B
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
    protected abstract Point2D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator2D transformationOperator,
                  java.util.Hashtable transformedGeometries);

    /**
     * ���̓_��?A�^����ꂽ�􉽓I�ϊ����Z�q�ŕϊ�����?B
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
    public synchronized Point2D
    transformBy(boolean reverseTransform,
                CartesianTransformationOperator2D transformationOperator,
                java.util.Hashtable transformedGeometries) {
        if (transformedGeometries == null)
            return this.doTransformBy(reverseTransform,
                    transformationOperator,
                    transformedGeometries);

        Point2D transformed = (Point2D) transformedGeometries.get(this);
        if (transformed == null) {
            transformed = this.doTransformBy(reverseTransform,
                    transformationOperator,
                    transformedGeometries);
            transformedGeometries.put(this, transformed);
        }
        return transformed;
    }

    /**
     * ���̓_��?A�^����ꂽ�􉽓I�ϊ����Z�q�ŕϊ�����?B
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
     * @param transformationOperator �􉽓I�ϊ����Z�q
     * @param transformedGeometries  ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �ϊ���̊􉽗v�f
     */
    public synchronized Point2D
    transformBy(CartesianTransformationOperator2D transformationOperator,
                java.util.Hashtable transformedGeometries) {
        return this.transformBy(false,
                transformationOperator,
                transformedGeometries);
    }

    /**
     * ���̓_��?A�^����ꂽ�􉽓I�ϊ����Z�q�ŋt�ϊ�����?B
     * <p/>
     * transformedGeometries ��?A
     * �ϊ��O�̊􉽗v�f��L?[�Ƃ�?A
     * �ϊ���̊􉽗v�f��l�Ƃ���n�b�V���e?[�u���ł���?B
     * </p>
     * <p/>
     * this �� transformedGeometries ��ɃL?[�Ƃ��đ�?݂��Ȃ�?�?��ɂ�?A
     * this �� transformationOperator �ŋt�ϊ�������̂�Ԃ�?B
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
     * ?�� this �� transformationOperator �ŋt�ϊ�������̂�Ԃ�?B
     * </p>
     *
     * @param transformationOperator �􉽓I�ϊ����Z�q
     * @param transformedGeometries  ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �t�ϊ���̊􉽗v�f
     */
    public synchronized Point2D
    reverseTransformBy(CartesianTransformationOperator2D transformationOperator,
                       java.util.Hashtable transformedGeometries) {
        return this.transformBy(true,
                transformationOperator,
                transformedGeometries);
    }

    /**
     * �_���?A�^����ꂽ�􉽓I�ϊ����Z�q�ŕϊ�����?B
     * <p/>
     * transformedGeometries ��?A
     * �ϊ��O�̊􉽗v�f��L?[�Ƃ�?A
     * �ϊ���̊􉽗v�f��l�Ƃ���n�b�V���e?[�u���ł���?B
     * </p>
     * <p/>
     * ��?ۂƂȂ�_�� transformedGeometries ��ɃL?[�Ƃ��đ�?݂��Ȃ�?�?��ɂ�?A
     * ����� transformationOperator �ŕϊ�������̂�Ԃ�?B
     * ��?ۂƂȂ邪 transformedGeometries ��Ɋ�ɃL?[�Ƃ��đ�?݂���?�?��ɂ�?A
     * ��?ۂ̕ϊ���?s�Ȃ킸?A���̃L?[�ɑΉ�����l��Ԃ�?B
     * ����?��?��?ċA�I��?s�Ȃ���?B
     * </p>
     * <p/>
     * transformedGeometries �� null �ł�?\��Ȃ�?B
     * transformedGeometries �� null ��?�?��ɂ�?A
     * ?�ɑ�?ۂƂȂ�_�� transformationOperator �ŕϊ�������̂�Ԃ�?B
     * </p>
     *
     * @param points                 �_��
     * @param reverseTransform       �t�ϊ�����̂ł���� true?A�����łȂ���� false
     * @param transformationOperator �􉽓I�ϊ����Z�q
     * @param transformedGeometries  ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     */
    public static Point2D[]
    transform(Point2D[] points,
              boolean reverseTransform,
              CartesianTransformationOperator2D transformationOperator,
              java.util.Hashtable transformedGeometries) {
        Point2D[] tPoints = new Point2D[points.length];
        for (int i = 0; i < points.length; i++)
            tPoints[i] = points[i].transformBy(reverseTransform,
                    transformationOperator,
                    transformedGeometries);
        return tPoints;
    }

    /**
     * �_���?A�^����ꂽ�􉽓I�ϊ����Z�q�ŕϊ�����?B
     * <p/>
     * transformedGeometries ��?A
     * �ϊ��O�̊􉽗v�f��L?[�Ƃ�?A
     * �ϊ���̊􉽗v�f��l�Ƃ���n�b�V���e?[�u���ł���?B
     * </p>
     * <p/>
     * ��?ۂƂȂ�_�� transformedGeometries ��ɃL?[�Ƃ��đ�?݂��Ȃ�?�?��ɂ�?A
     * ����� transformationOperator �ŕϊ�������̂�Ԃ�?B
     * ����?ۂɃ?�\�b�h�Ք�ł͑�?ۂƂȂ�_��L?[?A
     * ���̕ϊ����ʂ�l�Ƃ��� transformedGeometries �ɒǉB���?B
     * </p>
     * <p/>
     * ��?ۂƂȂ邪 transformedGeometries ��Ɋ�ɃL?[�Ƃ��đ�?݂���?�?��ɂ�?A
     * ��?ۂ̕ϊ���?s�Ȃ킸?A���̃L?[�ɑΉ�����l��Ԃ�?B
     * ����?��?��?ċA�I��?s�Ȃ���?B
     * </p>
     * <p/>
     * transformedGeometries �� null �ł�?\��Ȃ�?B
     * transformedGeometries �� null ��?�?��ɂ�?A
     * ?�ɑ�?ۂƂȂ�_�� transformationOperator �ŕϊ�������̂�Ԃ�?B
     * </p>
     *
     * @param points                 �_��
     * @param transformationOperator �􉽓I�ϊ����Z�q
     * @param transformedGeometries  ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     */
    public static Point2D[]
    transform(Point2D[] points,
              CartesianTransformationOperator2D transformationOperator,
              java.util.Hashtable transformedGeometries) {
        return transform(points, false, transformationOperator, transformedGeometries);
    }

    /**
     * �_���?A�^����ꂽ�􉽓I�ϊ����Z�q�ŋt�ϊ�����?B
     * <p/>
     * transformedGeometries ��?A
     * �ϊ��O�̊􉽗v�f��L?[�Ƃ�?A
     * �ϊ���̊􉽗v�f��l�Ƃ���n�b�V���e?[�u���ł���?B
     * </p>
     * <p/>
     * ��?ۂƂȂ�_�� transformedGeometries ��ɃL?[�Ƃ��đ�?݂��Ȃ�?�?��ɂ�?A
     * ����� transformationOperator �ŋt�ϊ�������̂�Ԃ�?B
     * ����?ۂɃ?�\�b�h�Ք�ł͑�?ۂƂȂ�_��L?[?A
     * ���̕ϊ����ʂ�l�Ƃ��� transformedGeometries �ɒǉB���?B
     * </p>
     * <p/>
     * ��?ۂƂȂ邪 transformedGeometries ��Ɋ�ɃL?[�Ƃ��đ�?݂���?�?��ɂ�?A
     * ��?ۂ̕ϊ���?s�Ȃ킸?A���̃L?[�ɑΉ�����l��Ԃ�?B
     * ����?��?��?ċA�I��?s�Ȃ���?B
     * </p>
     * <p/>
     * transformedGeometries �� null �ł�?\��Ȃ�?B
     * transformedGeometries �� null ��?�?��ɂ�?A
     * ?�ɑ�?ۂƂȂ�_�� transformationOperator �ŋt�ϊ�������̂�Ԃ�?B
     * </p>
     *
     * @param points                 �_��
     * @param transformationOperator �􉽓I�ϊ����Z�q
     * @param transformedGeometries  ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     */
    public static Point2D[]
    reverseTransform(Point2D[] points,
                     CartesianTransformationOperator2D transformationOperator,
                     java.util.Hashtable transformedGeometries) {
        return transform(points, true, transformationOperator, transformedGeometries);
    }

    /**
     * CartesianPoint2D �̃C���X�^���X��?�?�����?B
     *
     * @param x X ?���
     * @param y Y ?���
     * @return CartesianPoint2D �̃C���X�^���X
     */
    public static CartesianPoint2D of(double x,
                                      double y) {
        return new CartesianPoint2D(x, y);
    }

    /**
     * CartesianPoint2D �̃C���X�^���X��?�?�����?B
     *
     * @param components X, Y?����̔z�� (�v�f?� 2)
     * @return CartesianPoint2D �̃C���X�^���X
     */
    public static CartesianPoint2D of(double[] components) {
        return new CartesianPoint2D(components);
    }
}
