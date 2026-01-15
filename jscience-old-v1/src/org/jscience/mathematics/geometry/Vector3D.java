/*
 * �R�����̃x�N�g����\����?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Vector3D.java,v 1.7 2006/05/20 23:25:56 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

import org.jscience.mathematics.MathUtils;
import org.jscience.mathematics.algebraic.matrices.Double3Vector;

/**
 * �R�����̃x�N�g����\����?ۃN���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.7 $, $Date: 2006/05/20 23:25:56 $
 * @see Point3D
 */

public abstract class Vector3D extends AbstractVector {

    /**
     * First canonical vector (coordinates : 1, 0, 0). Same as xUnitVector.
     * This is really an {@link LiteralVector3D literalVector3D},
     * hence it can't be changed in any way.
     */
    public static final Vector3D plusI = new LiteralVector3D(1, 0, 0, true);

    /**
     * Opposite of the first canonical vector (coordinates : -1, 0, 0).
     * This is really an {@link LiteralVector3D literalVector3D},
     * hence it can't be changed in any way.
     */
    public static final Vector3D minusI = new LiteralVector3D(-1, 0, 0, true);

    /**
     * Second canonical vector (coordinates : 0, 1, 0).    Same as yUnitVector.
     * This is really an {@link LiteralVector3D literalVector3D},
     * hence it can't be changed in any way.
     */
    public static final Vector3D plusJ = new LiteralVector3D(0, 1, 0, true);

    /**
     * Opposite of the second canonical vector (coordinates : 0, -1, 0).
     * This is really an {@link LiteralVector3D literalVector3D},
     * hence it can't be changed in any way.
     */
    public static final Vector3D minusJ = new LiteralVector3D(0, -1, 0, true);

    /**
     * Third canonical vector (coordinates : 0, 0, 1).     Same as zUnitVector.
     * This is really an {@link LiteralVector3D literalVector3D},
     * hence it can't be changed in any way.
     */
    public static final Vector3D plusK = new LiteralVector3D(0, 0, 1, true);

    /**
     * Opposite of the third canonical vector (coordinates : 0, 0, -1).
     * This is really an {@link LiteralVector3D literalVector3D},
     * hence it can't be changed in any way.
     */
    public static final Vector3D minusK = new LiteralVector3D(0, 0, -1, true);

    /**
     * �R�����̃[�?�x�N�g��?B
     */
    public static final Vector3D zeroVector;

    /**
     * �R�����̃O�??[�o���Ȓ���?W�n�� X �����̒P�ʃx�N�g��?B
     */
    public static final Vector3D xUnitVector;

    /**
     * �R�����̃O�??[�o���Ȓ���?W�n�� Y �����̒P�ʃx�N�g��?B
     */
    public static final Vector3D yUnitVector;

    /**
     * �R�����̃O�??[�o���Ȓ���?W�n�� Z �����̒P�ʃx�N�g��?B
     */
    public static final Vector3D zUnitVector;

    /**
     * static �ȃt�B?[���h�ɒl��?ݒ肷��?B
     */
    static {
        zeroVector = new LiteralVector3D(0.0, 0.0, 0.0);
        xUnitVector = new LiteralVector3D(1.0, 0.0, 0.0, true);
        yUnitVector = new LiteralVector3D(0.0, 1.0, 0.0, true);
        zUnitVector = new LiteralVector3D(0.0, 0.0, 1.0, true);
    }

    /**
     * �P�ʃx�N�g��
     *
     * @serial
     */
    private Vector3D unitized;

    /**
     * �I�u�W�F�N�g��?\�z����?B
     * <p/>
     * ?�?����悤�Ƃ���x�N�g����
     * �P�ʃx�N�g���ł��邩�ǂ���������Ȃ�?�?�?A�µ����
     * �P�ʃx�N�g���łȂ����Ƃ���?؂���Ă���?�?��ɂ�?A
     * ���̃R���X�g���N�^��g�p����?B
     * </p>
     */
    protected Vector3D() {
        super();

        unitized = null;
    }

    /**
     * �I�u�W�F�N�g��?\�z����?B
     * <p/>
     * ?�?����悤�Ƃ���x�N�g����
     * �P�ʃx�N�g���ł��邩�ǂ���������?�?��ɂ�?A
     * ���̃R���X�g���N�^��g�p����?B
     * </p>
     *
     * @param confirmedAsUnitized ?�?����悤�Ƃ���x�N�g����
     *                            �P�ʃx�N�g���ł���Ȃ�� <code>true</code>?A
     *                            ����Ȃ��� <code>false</code>
     */
    protected Vector3D(boolean confirmedAsUnitized) {
        super();
        unitized = (confirmedAsUnitized) ? this : null;
    }

    /**
     * �R�����̃[�?�x�N�g����Ԃ�?B
     *
     * @return �R�����̃[�?�x�N�g��
     */
    public static Vector3D zeroVector() {
        return zeroVector;
    }

    /**
     * �R�����̃O�??[�o���Ȓ���?W�n�� X �����̒P�ʃx�N�g����Ԃ�?B
     *
     * @return �R�����̃O�??[�o���Ȓ���?W�n�� X �����̒P�ʃx�N�g��
     */
    public static Vector3D xUnitVector() {
        return xUnitVector;
    }

    /**
     * �R�����̃O�??[�o���Ȓ���?W�n�� Y �����̒P�ʃx�N�g����Ԃ�?B
     *
     * @return �R�����̃O�??[�o���Ȓ���?W�n�� Y �����̒P�ʃx�N�g��
     */
    public static Vector3D yUnitVector() {
        return yUnitVector;
    }

    /**
     * �R�����̃O�??[�o���Ȓ���?W�n�� Z �����̒P�ʃx�N�g����Ԃ�?B
     *
     * @return �R�����̃O�??[�o���Ȓ���?W�n�� Z �����̒P�ʃx�N�g��
     */
    public static Vector3D zUnitVector() {
        return zUnitVector;
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
     * �R�������ۂ���Ԃ�?B
     * <p/>
     * ?�� true ��Ԃ�?B
     * </p>
     *
     * @return �R�����Ȃ̂�?A?�� <code>true</code>
     */
    public boolean is3D() {
        return true;
    }

    public Double3Vector getDouble3Vector() {
        return new Double3Vector(x(), y(), z());
    }

    /**
     * �x�N�g���� X ?�����Ԃ���?ۃ?�\�b�h?B
     *
     * @return �x�N�g���� X ?���
     */
    public abstract double x();

    /**
     * �x�N�g���� Y ?�����Ԃ���?ۃ?�\�b�h?B
     *
     * @return �x�N�g���� Y ?���
     */
    public abstract double y();

    /**
     * �x�N�g���� Z ?�����Ԃ���?ۃ?�\�b�h?B
     *
     * @return �x�N�g���� Z ?���
     */
    public abstract double z();

    /**
     * �P�ʉ������x�N�g����Ԃ�?B
     * <p/>
     * ������?���Ȃ��x�N�g���ɑ΂��Ă��̃?�\�b�h��Ă�?�?�?A
     * �����ł̓[�?�x�N�g����Ԃ��悤�ɂȂBĂ���?B
     * ������?A�{���͗�O ZeroLengthException �𓊂���ׂ��ł���?B
     * </p>
     *
     * @return �P�ʉ������x�N�g��
     */
    public Vector3D unitized() {
        if (unitized != null)
            return unitized;

        double leng = length();
        if (!GeometryUtils.isDividable(MathUtils.maxOf3(x(), y(), z()), leng)) {
            // throw new ZeroLengthException();
            return (unitized = zeroVector);
        }

        return (unitized = new LiteralVector3D(x() / leng, y() / leng, z() / leng, true));
    }

    /**
     * �e?����̕�?��𔽓]�������x�N�g����Ԃ�?B
     *
     * @return this �𔽓]�����x�N�g��
     */
    public Vector3D reverse() {
        return new LiteralVector3D(-x(), -y(), -z());
    }

    /**
     * ������?����ȃx�N�g����?���ɑI��ŕԂ�?B
     *
     * @return this ��?����ȃx�N�g��
     */
    public Vector3D verticalVector() {
        return crossProduct((parallelDirection(yUnitVector)) ? zUnitVector : yUnitVector);
    }

    /**
     * ��?ς�Ԃ�?B
     *
     * @param mate ��?ς��鑊��̃x�N�g��
     * @return ��?�
     */
    public double dotProduct(Vector3D mate) {
        return x() * mate.x() + y() * mate.y() + z() * mate.z();
    }

    /**
     * �O?ς�Ԃ�?B
     *
     * @param mate �O?ς��鑊��̃x�N�g��
     * @return mate �Ƃ̊O?�
     */
    public Vector3D crossProduct(Vector3D mate) {
        return new LiteralVector3D(y() * mate.z() - z() * mate.y(),
                z() * mate.x() - x() * mate.z(),
                x() * mate.y() - y() * mate.x());
    }

    /**
     * �x�N�g�����m�̘a��Ԃ�?B
     *
     * @param mate �a���鑊��̃x�N�g��
     * @return �x�N�g���̘a (this + mate)
     */
    public Vector3D add(Vector3D mate) {
        return new LiteralVector3D(x() + mate.x(),
                y() + mate.y(),
                z() + mate.z());
    }

    /**
     * �x�N�g�����m��?���Ԃ�?B
     *
     * @param mate ?����鑊��̃x�N�g��
     * @return �x�N�g����?� (this - mate)
     */
    public Vector3D subtract(Vector3D mate) {
        return new LiteralVector3D(x() - mate.x(),
                y() - mate.y(),
                z() - mate.z());
    }

    /**
     * �^����ꂽ�X�P?[����?悶���x�N�g����Ԃ�?B
     *
     * @param scale �X�P?[��
     * @return (this * scale)
     */
    public Vector3D multiply(double scale) {
        return new LiteralVector3D(x() * scale,
                y() * scale,
                z() * scale);
    }

    /**
     * �^����ꂽ�X�P?[���Ŋ��B��x�N�g����Ԃ�?B
     *
     * @param scale �X�P?[��
     * @return (this / scale)
     */
    public Vector3D divide(double scale) {
        return new LiteralVector3D(x() / scale,
                y() / scale,
                z() / scale);
    }

    /**
     * ��x�N�g���̓���?��𔻒肷��?B
     * <p/>
     * ��̃x�N�g����?��̑傫����?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?��?u�����̋��e��?�?v���?��������?A
     * ��̃x�N�g���͓���ł����̂Ɣ��f����?B
     * </p>
     *
     * @param mate ����̑�?ۂƂȂ�x�N�g��
     * @return ��̃x�N�g��������̃x�N�g���ł���Ƃ݂Ȃ���� true?A����Ȃ��� false
     * @see ConditionOfOperation
     * @see #identicalDirection(Vector3D)
     */
    public boolean identical(Vector3D mate) {
        double dTol2 = getToleranceForDistance2();
        double xv, yv, zv;

        xv = x() - mate.x();
        yv = y() - mate.y();
        zv = z() - mate.z();

        return xv * xv + yv * yv + zv * zv < dTol2;
    }

    /**
     * ��x�N�g���̓�����?��𔻒肷��?B
     * <p/>
     * ��̃x�N�g���̂Ȃ��p�x��?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?��?u�p�x�̋��e��?�?v���?��������?A
     * ��̃x�N�g���͓�����ł����̂Ɣ��f����?B
     * </p>
     *
     * @param mate          ����̑�?ۂƂȂ�x�N�g��
     * @param allowReversed ���]���Ă���?�Ԃ𓯈�ƌ��Ȃ��Ȃ�� true
     * @return ��̃x�N�g����������̃x�N�g���Ƃ݂Ȃ���� true?A����Ȃ��� false
     * @see ConditionOfOperation
     * @see #identical(Vector3D)
     * @see #identicalDirection(Vector3D)
     * @see #parallelDirection(Vector3D)
     */
    private boolean identicalDirection(Vector3D mate, boolean allowReversed) {
        double aTol = getToleranceForAngle();
        double dTol2 = getToleranceForDistance2();
        double dotProd, crossProd;
        boolean result; // return value

        if (this.norm() < dTol2 || mate.norm() < dTol2) {
            result = true;
        } else {
            dotProd = dotProduct(mate); // cos(theta)*|this|*|mate|
            if (allowReversed)
                dotProd = Math.abs(dotProd);
            crossProd = crossProduct(mate).length(); // sin(theta)*|this|*|mate|
            result = Math.abs(Math.atan2(crossProd, dotProd)) < aTol;
        }

        return result;
    }

    /**
     * ��x�N�g���̓�����?��𔻒肷��?B
     * <p/>
     * ��̃x�N�g���̂Ȃ��p�x��?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?��?u�p�x�̋��e��?�?v���?��������?A
     * ��̃x�N�g���͓�����ł����̂Ɣ��f����?B
     * </p>
     * <p/>
     * �Ȃ�?A���]?�Ԃ͓���Ƃ݂Ȃ��Ȃ�?B
     * </p>
     *
     * @param mate ����̑�?ۂƂȂ�x�N�g��
     * @return ��̃x�N�g����������̃x�N�g���Ƃ݂Ȃ���� true?A����Ȃ��� false
     * @see ConditionOfOperation
     * @see #identical(Vector3D)
     * @see #parallelDirection(Vector3D)
     */
    public boolean identicalDirection(Vector3D mate) {
        return identicalDirection(mate, false);
    }

    /**
     * ��x�N�g���̓�����?��𔻒肷��?B
     * <p/>
     * ��̃x�N�g���̂Ȃ��p�x��?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?��?u�p�x�̋��e��?�?v���?��������?A
     * ��̃x�N�g���͓�����ł����̂Ɣ��f����?B
     * </p>
     * <p/>
     * �Ȃ�?A���]?�Ԃӯ��Ƃ݂Ȃ�?B
     * </p>
     *
     * @param mate ����̑�?ۂƂȂ�x�N�g��
     * @return ��̃x�N�g����������̃x�N�g���Ƃ݂Ȃ���� true?A����Ȃ��� false
     * @see ConditionOfOperation
     * @see #identicalDirection(Vector3D)
     */
    public boolean parallelDirection(Vector3D mate) {
        return identicalDirection(mate, true);
    }

    /**
     * �x�N�g���̃m������Ԃ�?B
     *
     * @return �x�N�g���̃m���� (x^2) + (y^2) + (z^2)
     */
    public double norm() {
        double xv = x();
        double yv = y();
        double zv = z();

        return xv * xv + yv * yv + zv * zv;
    }

    /**
     * �^����ꂽ��?�?W��Z���̎���?A�^����ꂽ�p�x������]�������x�N�g����Ԃ�?B
     * <p/>
     * �Ă�?o������?A(rCos * rCos + rSin * rSin) �̒l�� 1 �ł��邱�Ƃ�
     * ��?؂��Ȃ���΂Ȃ�Ȃ�?B
     * </p>
     *
     * @param trns ��?�?W���瓾��ꂽ?W�ϊ�
     * @param rCos cos(��]�p�x)
     * @param rSin sin(��]�p�x)
     * @return ��]��̃x�N�g��
     * @see Point3D#rotateZ(CartesianTransformationOperator3D,double,double)
     * @see Axis2Placement3D#rotateZ(CartesianTransformationOperator3D,double,double)
     */
    Vector3D rotateZ(CartesianTransformationOperator3D trns,
                     double rCos, double rSin) {
        Vector3D lvec, rvec;
        double x, y, z;

        lvec = trns.toLocal(this);
        x = (rCos * lvec.x()) - (rSin * lvec.y());
        y = (rSin * lvec.x()) + (rCos * lvec.y());
        z = lvec.z();
        rvec = new LiteralVector3D(x, y, z);
        return trns.toEnclosed(rvec);
    }

    /**
     * �x�N�g���𕽖ʂɓ��e����?B
     *
     * @param dNorm ���ʂ̖@?�x�N�g��
     * @return ����?�ɓ��e���ꂽ�x�N�g��
     */
    public Vector3D project(Vector3D dNorm) {
        Vector3D uNorm;
        double d;

        /*
        * uNorm <- unit(dNorm)
        * dDvec <- dSvec - dot(uNorm, dSvec) * uNorm
        */
        uNorm = dNorm.unitized();
        d = uNorm.dotProduct(this);
        return this.subtract(uNorm.multiply(d));
    }

    /**
     * �x�N�g���� XY ���ʂɎˉe���� 2D ������?B
     *
     * @return 2D �������x�N�g��
     * @see Vector2D
     */
    Vector2D to2D() {
        return new LiteralVector2D(x(), y());
    }

    /**
     * �x�N�g�����?�?W�n�� XY ���ʂɎˉe���� 2D ������?B
     *
     * @return 2D �������x�N�g��
     * @see CartesianTransformationOperator3D
     * @see Vector2D
     */
    Vector2D to2D(CartesianTransformationOperator3D transform) {
        return transform.toLocal(this).to2D();
    }

    /**
     * �R�����̓_ (Point3D) �ɕϊ�����?B
     *
     * @return ���_����̈ʒu�x�N�g���Ƃ݂Ȃ����_
     */
    public Point3D toPoint3D() {
        return new CartesianPoint3D(x(), y(), z());
    }

    /**
     * �x�N�g���̔z���R�����̓_ (Point3D) �̔z��ɕϊ�����?B
     *
     * @return ���_����̈ʒu�x�N�g���Ƃ݂Ȃ����_�̔z��
     */
    public static Point3D[] toPoint3D(Vector3D[] vecs) {
        Point3D[] pnts = new Point3D[vecs.length];
        for (int i = 0; i < vecs.length; i++)
            pnts[i] = vecs[i].toPoint3D();
        return pnts;
    }

    /**
     * double�̔z��ɕϊ�����?B
     *
     * @return ?W�l����double�̔z��
     */
    public double[] toDoubleArray() {
        double[] array = {x(), y(), z()};
        return array;
    }

    /**
     * Get a vector orthogonal to the instance.
     * <p>There are an infinite number of normalized vectors orthogonal
     * to the instance. This method picks up one of them almost
     * arbitrarily. It is useful when one needs to compute a reference
     * frame with one of the axes in a predefined direction. The
     * following example shows hos to build a frame having the k axis
     * aligned with the known vector u :
     * <pre><code>
     *   LiteralVector3D k = u;
     *   k.unitized();
     *   Vector3D i = k.orthogonal();
     *   Vector3D j = k.crossProduct(i);
     * </code></pre></p>
     *
     * @return a new normalized vector orthogonal to the instance
     * @throws ArithmeticException if the norm of the instance is null
     */
    public Vector3D orthogonal() {

        double threshold = 0.6 * norm();
        if (threshold == 0) {
            throw new ArithmeticException("null norm");
        }

        if ((x() >= -threshold) && (x() <= threshold)) {
            double inverse = 1 / Math.sqrt(y() * y() + z() * z());
            return new LiteralVector3D(0, inverse * z(), -inverse * y());
        } else if ((y() >= -threshold) && (y() <= threshold)) {
            double inverse = 1 / Math.sqrt(x() * x() + z() * z());
            return new LiteralVector3D(-inverse * z(), 0, inverse * x());
        } else {
            double inverse = 1 / Math.sqrt(x() * x() + y() * y());
            return new LiteralVector3D(inverse * y(), -inverse * x(), 0);
        }

    }

    /**
     * ���̃x�N�g���Ƃ̊p�x (���W�A��) ��?�߂�?B
     * <p/>
     * this ���� mate �ւ�?����̊p�x (0 ?` 2pi)
     * <br>
     * �����ł�?����Ƃ�?Anorm ����BĂ�����猩���Ƃ���?����
     * </p>
     *
     * @param mate ����̃x�N�g��
     * @param norm ?u?����?v�숂߂��?��ƂȂ�x�N�g��
     * @return ����̃x�N�g���Ƃ̊p�x
     */
    public double angleWith(Vector3D mate, Vector3D norm) {
        Vector3D thisUnitVec;
        Vector3D mateUnitVec;

        try {
            thisUnitVec = this.unitized();
            mateUnitVec = mate.unitized();
        } catch (ZeroLengthException e) {
            return 0.0;
        }

        double dot = thisUnitVec.dotProduct(mateUnitVec);
        if (dot > 1.0) dot = 1.0;
        if (dot < -1.0) dot = -1.0;
        double theta = Math.acos(dot);

        Vector3D crsVec = thisUnitVec.crossProduct(mateUnitVec);
        Vector3D crsUnitVec;

        try {
            crsUnitVec = crsVec.unitized();
        } catch (ZeroLengthException e) {
            return (dot < 0.0) ? Math.PI : 0.0;
        }

        if (crsUnitVec.dotProduct(norm) < 0.0)
            theta = Math.PI * 2 - theta;

        return theta;
    }

    /**
     * ���̃x�N�g����?A�^����ꂽ�􉽓I�ϊ����Z�q�ŕϊ�����?B
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
    protected abstract Vector3D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator3D transformationOperator,
                  java.util.Hashtable transformedGeometries);

    /**
     * ���̃x�N�g����?A�^����ꂽ�􉽓I�ϊ����Z�q�ŕϊ�����?B
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
    public synchronized Vector3D
    transformBy(boolean reverseTransform,
                CartesianTransformationOperator3D transformationOperator,
                java.util.Hashtable transformedGeometries) {
        if (transformedGeometries == null)
            return this.doTransformBy(reverseTransform,
                    transformationOperator,
                    transformedGeometries);

        Vector3D transformed = (Vector3D) transformedGeometries.get(this);
        if (transformed == null) {
            transformed = this.doTransformBy(reverseTransform,
                    transformationOperator,
                    transformedGeometries);
            transformedGeometries.put(this, transformed);
        }
        return transformed;
    }

    /**
     * ���̃x�N�g����?A�^����ꂽ�􉽓I�ϊ����Z�q�ŕϊ�����?B
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
    public synchronized Vector3D
    transformBy(CartesianTransformationOperator3D transformationOperator,
                java.util.Hashtable transformedGeometries) {
        return this.transformBy(false,
                transformationOperator,
                transformedGeometries);
    }

    /**
     * ���̃x�N�g����?A�^����ꂽ�􉽓I�ϊ����Z�q�ŋt�ϊ�����?B
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
    public synchronized Vector3D
    reverseTransformBy(CartesianTransformationOperator3D transformationOperator,
                       java.util.Hashtable transformedGeometries) {
        return this.transformBy(true,
                transformationOperator,
                transformedGeometries);
    }

    /**
     * LiteralVector3D �̃C���X�^���X��?�?�����?B
     *
     * @param x X ?���
     * @param y Y ?���
     * @param z Z ?���
     * @return LiteralVector3D �̃C���X�^���X
     */
    public static LiteralVector3D of(double x,
                                     double y,
                                     double z) {
        return new LiteralVector3D(x, y, z);
    }

    /**
     * LiteralVector3D �̃C���X�^���X��?�?�����?B
     *
     * @param components X, Y?����̔z�� (�v�f?� 3)
     * @return LiteralVector3D �̃C���X�^���X
     */
    public static LiteralVector3D of(double[] components) {
        return new LiteralVector3D(components);
    }
}
