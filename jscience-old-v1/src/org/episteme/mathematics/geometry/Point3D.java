/*
 * �R�����̓_��\����?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Point3D.java,v 1.2 2006/03/01 21:16:07 virtualcall Exp $
 *
 */

package org.episteme.mathematics.geometry;

/**
 * �R�����̓_��\����?ۃN���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:16:07 $
 * @see Vector3D
 */

public abstract class Point3D extends AbstractPoint {
    /**
     * �R�����̌��_ (0, 0, 0)?B
     */
    public static final Point3D origin;

    /**
     * static �ȃt�B?[���h�ɒl��?ݒ肷��?B
     */
    static {
        origin = new CartesianPoint3D(0.0, 0.0, 0.0);
    }

    /**
     * �I�u�W�F�N�g��?\�z����?B
     *
     * @see ConditionOfOperation
     */
    protected Point3D() {
        super();
    }

    /**
     * ������Ԃ�?B
     * <p/>
     * ?�� 3 ��Ԃ�?B
     * </p>
     *
     * @return �R�����Ȃ̂�?A?�� 3
     */
    public int dimension() {
        return 3;
    }

    /**
     * �R�������ۂ���Ԃ�
     * <p/>
     * <p/>
     * ?�� true ��Ԃ�?B
     * </p>
     *
     * @return �R�����Ȃ̂�?A?�� <code>true</code>
     */
    public boolean is3D() {
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
     * �_�� Z ?W�l��Ԃ���?ۃ?�\�b�h?B
     *
     * @return �_�� Z ?W�l
     */
    public abstract double z();

    /**
     * ���̓_�ɗ^����ꂽ�x�N�g���𑫂����_��Ԃ�?B
     *
     * @param vector �_�ɑ����x�N�g��
     * @return �^����ꂽ�x�N�g���𑫂����_ (this + vector)
     */
    public Point3D add(Vector3D vector) {
        return new CartesianPoint3D(x() + vector.x(),
                y() + vector.y(),
                z() + vector.z());
    }

    /**
     * ���̓_����^����ꂽ�x�N�g����򢂽�_��Ԃ�?B
     *
     * @param vector �_�����x�N�g��
     * @return �^����ꂽ�x�N�g����򢂽�_ (this - vector)
     */
    public Point3D subtract(Vector3D vector) {
        return new CartesianPoint3D(x() - vector.x(),
                y() - vector.y(),
                z() - vector.z());
    }

    /**
     * ���̓_�Ɨ^����ꂽ�_�Ƃ�?���Ԃ�?B
     *
     * @param mate ?����鑊��̓_
     * @return ��_��?� (this - mate)
     */
    public Vector3D subtract(Point3D mate) {
        return new LiteralVector3D(x() - mate.x(),
                y() - mate.y(),
                z() - mate.z());
    }

    /**
     * ���̓_�ɗ^����ꂽ�X�P?[����?悶���_��Ԃ�?B
     *
     * @param scale �X�P?[��
     * @return (this * scale)
     */
    public Point3D multiply(double scale) {
        return new CartesianPoint3D(x() * scale,
                y() * scale,
                z() * scale);
    }

    /**
     * ���̓_��^����ꂽ�X�P?[���Ŋ��B��_��Ԃ�?B
     *
     * @param scale �X�P?[��
     * @return (this / scale)
     */
    public Point3D divide(double scale) {
        return new CartesianPoint3D(x() / scale,
                y() / scale,
                z() / scale);
    }

    /**
     * ���̓_�Ɨ^����ꂽ�_��?�^��Ԃ������ʂ�Ԃ�?B
     *
     * @param mate          ?�`��Ԃ̑���ƂȂ�_
     * @param weightForThis ��?g�ɑ΂���?d�� (����ɑ΂���?d�݂� 1 - weightForThis)
     * @return ?�`��Ԃ������ʂ̓_ (weightForThis * this + (1 - weightForThis) * mate)
     */
    public Point3D linearInterpolate(Point3D mate,
                                     double weightForThis) {
        double weightForMate = 1.0 - weightForThis;
        return new CartesianPoint3D(this.x() * weightForThis + mate.x() * weightForMate,
                this.y() * weightForThis + mate.y() * weightForMate,
                this.z() * weightForThis + mate.z() * weightForMate);
    }

    /**
     * ���̓_�Ɨ^����ꂽ�_�̒��_��Ԃ�?B
     *
     * @param mate ���_��?�߂鑊��ƂȂ�_
     * @return ���_ (0.5 * this + 0.5 * mate)
     */
    public Point3D midPoint(Point3D mate) {
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
    public boolean identical(Point3D mate) {
        return distance2(mate) < getToleranceForDistance2();
    }

    /**
     * ���̓_��R�����̃x�N�g�� (Vector3D) �ɕϊ�����?B
     *
     * @return ���_����̃x�N�g���Ƃ݂Ȃ����x�N�g��
     */
    public Vector3D toVector3D() {
        return new LiteralVector3D(x(), y(), z());
    }

    /**
     * �_�̔z���x�N�g�� (Vector3D) �̔z��ɕϊ�����?B
     *
     * @return ���_����̃x�N�g���Ƃ݂Ȃ����x�N�g���̔z��
     */
    public static Vector3D[] toVector3D(Point3D[] pnts) {
        Vector3D[] vecs = new Vector3D[pnts.length];
        for (int i = 0; i < pnts.length; i++)
            vecs[i] = pnts[i].toVector3D();
        return vecs;
    }

    /**
     * ���̓_����^����ꂽ��?�ւ̓��e�_��?�߂�?B
     *
     * @param mate ���e��?ۂ̋�?�
     * @return �w�肳�ꂽ��?�ւ̓��e�_ (��?݂��Ȃ��Ƃ��͒���0�̔z���Ԃ�)
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public PointOnCurve3D[] project(ParametricCurve3D mate)
            throws IndefiniteSolutionException {
        return mate.projectFrom(this);
    }

    /**
     * ���̓_���^����ꂽ��?��?��?�BĂ��邩�ǂ�����Ԃ�?B
     *
     * @param mate �����?ۂ̋�?�
     * @return ��?��?��?�BĂ���� true?A�����łȂ���� false
     */
    public boolean isOn(ParametricCurve3D mate) {
        PointOnCurve3D prjp = mate.nearestProjectFrom(this);

        if (prjp != null && identical(prjp))
            return true;

        if (false) {    // �K�v��??
            if (mate.isFinite() && mate.isNonPeriodic()) {    // �L�Ŕ���I
                // mate must be a bounded curve
                BoundedCurve3D bnd = (BoundedCurve3D) mate;

                if (identical(bnd.startPoint()))
                    return true;
                if (identical(bnd.endPoint()))
                    return true;
            }
        }

        return false;
    }

    /**
     * ���̓_���^����ꂽ�Ȗʂ�?��?�BĂ��邩�ǂ�����Ԃ�?B
     *
     * @param mate �����?ۂ̋Ȗ�
     * @return �Ȗʂ�?��?�BĂ���� true?A�����łȂ���� false
     */
    public boolean isOn(ParametricSurface3D mate) {
        PointOnSurface3D prjp = mate.nearestProjectFrom(this);

        if (prjp != null && identical(prjp))
            return true;

        return false;
    }

    /**
     * ���̓_�Ɨ^����ꂽ�_�Ƃ̊Ԃ̋�����Ԃ�?B
     *
     * @param mate ������?�߂�_
     * @return this - mate �Ԃ̋���
     */
    public double distance(Point3D mate) {
        return Math.sqrt(distance2(mate));
    }

    /**
     * ���̓_�Ɨ^����ꂽ�_�Ƃ̊Ԃ̋����̎�?��Ԃ�?B
     *
     * @param mate �����̎�?��?�߂�_
     * @return this - mate �Ԃ̋����̎�?�
     */
    public double distance2(Point3D mate) {
        double dx, dy, dz;

        dx = x() - mate.x();
        dy = y() - mate.y();
        dz = z() - mate.z();
        return (dx * dx + dy * dy + dz * dz);
    }

    /**
     * �^����ꂽ�_��̒���?A���̓_����?łɓ���_��Ԃ�?B
     *
     * @param pnts �_��
     * @return ?łɓ���_
     */
    public Point3D longestPoint(Point3D[] pnts) {
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
    public Point3D longestPoint(Point3D[] pnts,
                                int start, int end) {

        double max_dist, dist;
        int index;

        if ((start < 0) || (end >= pnts.length)) {
            throw new InvalidArgumentValueException();
        }

        max_dist = distance2(pnts[start]);
        index = 0;

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
    static Vector3D collinear(Point3D[] points,
                              int start, int end) {
        if ((start < 0) || (points.length <= end)) {
            throw new InvalidArgumentValueException();
        }

        if (end - start <= 1) {
            return Vector3D.zeroVector;
        }

        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double dTol = condition.getToleranceForDistance();
        double dTol2 = dTol * dTol;

        Point3D basisPoint = points[start];
        Point3D longestPoint =
                basisPoint.longestPoint(points, start + 1, end);
        Vector3D uax = longestPoint.subtract(basisPoint);

        if (uax.norm() < dTol2) {
            return Vector3D.zeroVector;
        }
        uax = uax.unitized();

        Vector3D evec;
        double ecrs;

        for (int i = start + 1; i <= end; i++) {
            evec = points[i].subtract(points[start]);
            ecrs = evec.crossProduct(uax).norm();
            if (ecrs > dTol2) {
                return null;
            }
        }
        return uax;
    }

    /**
     * ���̓_��?A�^����ꂽ��?�?W�n�� Z ���ɑ΂���?A�^����ꂽ�p�x������]�������_��Ԃ�?B
     * <p/>
     * �Ă�?o������?A(rCos * rCos + rSin * rSin) �̒l�� 1 �ł��邱�Ƃ�
     * ��?؂��Ȃ���΂Ȃ�Ȃ�?B
     * </p>
     *
     * @param trns ��?�?W�n��\��?W�ϊ����Z�q
     * @param rCos cos(��]�p�x)
     * @param rSin sin(��]�p�x)
     * @return ��]��̓_
     * @see Vector3D#rotateZ(CartesianTransformationOperator3D,double,double)
     * @see Axis2Placement3D#rotateZ(CartesianTransformationOperator3D,double,double)
     */
    Point3D rotateZ(CartesianTransformationOperator3D trns,
                    double rCos, double rSin) {
        Point3D lpnt, rpnt;
        double x, y, z;

        lpnt = trns.toLocal(this);
        x = (rCos * lpnt.x()) - (rSin * lpnt.y());
        y = (rSin * lpnt.x()) + (rCos * lpnt.y());
        z = lpnt.z();
        rpnt = new CartesianPoint3D(x, y, z);
        return trns.toEnclosed(rpnt);
    }

    /**
     * ���̓_ (x, y, z) �� XY ���ʂɎˉe���� 2D ������?B
     *
     * @return 2D �������_ (x, y)
     */
    Point2D to2D() {
        return new CartesianPoint2D(x(), y());
    }

    /**
     * ���̓_���?�?W�n�� XY ���ʂɎˉe���� 2D ������?B
     *
     * @return 2D �������_ (���̓_��?W�l�͋�?�?W�n��ł� XY �l)
     */
    Point2D to2D(CartesianTransformationOperator3D transform) {
        return transform.toLocal(this).to2D();
    }

    /**
     * ���̓_�� CartesianPoint3D �̃I�u�W�F�N�g�ɕϊ�����?B
     *
     * @return �ϊ������_
     */
    CartesianPoint3D literal() {
        return new CartesianPoint3D(x(), y(), z());
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
    protected abstract Point3D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator3D transformationOperator,
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
    public synchronized Point3D
    transformBy(boolean reverseTransform,
                CartesianTransformationOperator3D transformationOperator,
                java.util.Hashtable transformedGeometries) {
        if (transformedGeometries == null)
            return this.doTransformBy(reverseTransform,
                    transformationOperator,
                    transformedGeometries);

        Point3D transformed = (Point3D) transformedGeometries.get(this);
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
    public synchronized Point3D
    transformBy(CartesianTransformationOperator3D transformationOperator,
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
    public synchronized Point3D
    reverseTransformBy(CartesianTransformationOperator3D transformationOperator,
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
     * ����?ۂɃ?�\�b�h�Ք�ł͑�?ۂƂȂ��L?[?A
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
     * @param reverseTransform       �t�ϊ�����̂ł���� true?A�����łȂ���� false
     * @param transformationOperator �􉽓I�ϊ����Z�q
     * @param transformedGeometries  ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     */
    public static Point3D[]
    transform(Point3D[] points,
              boolean reverseTransform,
              CartesianTransformationOperator3D transformationOperator,
              java.util.Hashtable transformedGeometries) {
        Point3D[] tPoints = new Point3D[points.length];
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
    public static Point3D[]
    transform(Point3D[] points,
              CartesianTransformationOperator3D transformationOperator,
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
    public static Point3D[]
    reverseTransform(Point3D[] points,
                     CartesianTransformationOperator3D transformationOperator,
                     java.util.Hashtable transformedGeometries) {
        return transform(points, true, transformationOperator, transformedGeometries);
    }

    /**
     * CartesianPoint3D �̃C���X�^���X��?�?�����?B
     *
     * @param x X ?���
     * @param y Y ?���
     * @param z Z ?���
     * @return CartesianPoint3D �̃C���X�^���X
     */
    public static CartesianPoint3D of(double x,
                                      double y,
                                      double z) {
        return new CartesianPoint3D(x, y, z);
    }

    /**
     * CartesianPoint3D �̃C���X�^���X��?�?�����?B
     *
     * @param components X, Y?����̔z�� (�v�f?� 3)
     * @return CartesianPoint3D �̃C���X�^���X
     */
    public static CartesianPoint3D of(double[] components) {
        return new CartesianPoint3D(components);
    }
}

