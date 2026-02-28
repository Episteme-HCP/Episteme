/*
 * �Q�����̃x�N�g����\����?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Vector2D.java,v 1.5 2006/03/01 21:16:12 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.mathematics.algebraic.matrices.Double2Vector;

/**
 * �Q�����̃x�N�g����\����?ۃN���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.5 $, $Date: 2006/03/01 21:16:12 $
 * @see Point2D
 */

public abstract class Vector2D extends AbstractVector {

    /**
     * First canonical vector (coordinates : 1, 0, 0). Same as xUnitVector.
     * This is really an {@link LiteralVector2D literalVector2D},
     * hence it can't be changed in any way.
     */
    public static final Vector2D plusI = new LiteralVector2D(1, 0, true);

    /**
     * Opposite of the first canonical vector (coordinates : -1, 0, 0).
     * This is really an {@link LiteralVector2D literalVector2D},
     * hence it can't be changed in any way.
     */
    public static final Vector2D minusI = new LiteralVector2D(-1, 0, true);

    /**
     * Second canonical vector (coordinates : 0, 1, 0).    Same as yUnitVector.
     * This is really an {@link LiteralVector2D literalVector2D},
     * hence it can't be changed in any way.
     */
    public static final Vector2D plusJ = new LiteralVector2D(0, 1, true);

    /**
     * Opposite of the second canonical vector (coordinates : 0, -1, 0).
     * This is really an {@link LiteralVector2D literalVector2D},
     * hence it can't be changed in any way.
     */
    public static final Vector2D minusJ = new LiteralVector2D(0, -1, true);

    /**
     * �Q�����̃[�?�x�N�g��?B
     */
    public static final Vector2D zeroVector;

    /**
     * �Q�����̃O�??[�o���Ȓ���?W�n�� X �����̒P�ʃx�N�g��?B
     */
    public static final Vector2D xUnitVector;

    /**
     * �Q�����̃O�??[�o���Ȓ���?W�n�� Y �����̒P�ʃx�N�g��?B
     */
    public static final Vector2D yUnitVector;

    /**
     * static �ȃt�B?[���h�ɒl��?ݒ肷��?B
     */
    static {
        zeroVector = new LiteralVector2D(0.0, 0.0);
        xUnitVector = new LiteralVector2D(1.0, 0.0, true);
        yUnitVector = new LiteralVector2D(0.0, 1.0, true);
    }

    /**
     * �P�ʃx�N�g��
     *
     * @serial
     */
    private Vector2D unitized;

    /**
     * �I�u�W�F�N�g��?\�z����?B
     * <p/>
     * ?�?����悤�Ƃ���x�N�g����
     * �P�ʃx�N�g���ł��邩�ǂ���������Ȃ�?�?�?A�µ����
     * �P�ʃx�N�g���łȂ����Ƃ���?؂���Ă���?�?��ɂ�?A
     * ���̃R���X�g���N�^��g�p����?B
     * </p>
     */
    protected Vector2D() {
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
    protected Vector2D(boolean confirmedAsUnitized) {
        super();
        unitized = (confirmedAsUnitized) ? this : null;
    }

    /**
     * �Q�����̃[�?�x�N�g����Ԃ�?B
     *
     * @return �Q�����̃[�?�x�N�g��
     */
    public static Vector2D zeroVector() {
        return zeroVector;
    }

    /**
     * �Q�����̃O�??[�o���Ȓ���?W�n�� X �����̒P�ʃx�N�g����Ԃ�?B
     *
     * @return �Q�����̃O�??[�o���Ȓ���?W�n�� X �����̒P�ʃx�N�g��
     */
    public static Vector2D xUnitVector() {
        return xUnitVector;
    }

    /**
     * �Q�����̃O�??[�o���Ȓ���?W�n�� Y �����̒P�ʃx�N�g����Ԃ�?B
     *
     * @return �Q�����̃O�??[�o���Ȓ���?W�n�� Y �����̒P�ʃx�N�g��
     */
    public static Vector2D yUnitVector() {
        return yUnitVector;
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
     * �Q�������ۂ���Ԃ�?B
     * <p/>
     * ?�� true ��Ԃ�?B
     * </p>
     *
     * @return �Q�����Ȃ̂�?A?�� <code>true</code>
     */
    public boolean is2D() {
        return true;
    }

    public Double2Vector getDouble2Vector() {
        return new Double2Vector(x(), y());
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
     * �P�ʉ������x�N�g����Ԃ�?B
     * <p/>
     * ������?���Ȃ��x�N�g���ɑ΂��Ă��̃?�\�b�h��Ă�?�?�?A
     * �����ł̓[�?�x�N�g����Ԃ��悤�ɂȂBĂ���?B
     * ������?A�{���͗�O ZeroLengthException �𓊂���ׂ��ł���?B
     * </p>
     *
     * @return �P�ʉ������x�N�g��
     */
    public Vector2D unitized() {
        if (unitized != null)
            return unitized;

        double leng = length();
        if (!GeometryUtils.isDividable(Math.max(x(), y()), leng)) {
            // throw new ZeroLengthException();
            return (unitized = zeroVector);
        }

        return (unitized = new LiteralVector2D(x() / leng, y() / leng, true));
    }

    /**
     * �e?����̕�?��𔽓]�������x�N�g����Ԃ�?B
     *
     * @return this �𔽓]�����x�N�g��
     */
    public Vector2D reverse() {
        return new LiteralVector2D(-x(), -y());
    }

    /**
     * ��?g��?����ȃx�N�g����?���ɑI��ŕԂ�?B
     *
     * @return this ��?����ȃx�N�g��
     */
    public Vector2D verticalVector() {
        return new LiteralVector2D(-y(), x());
    }

    /**
     * ��?ς�Ԃ�?B
     *
     * @param mate ��?ς��鑊��̃x�N�g��
     * @return ��?�
     */
    public double dotProduct(Vector2D mate) {
        return x() * mate.x() + y() * mate.y();
    }

    /**
     * �O?ς� Z ?�����Ԃ�?B
     *
     * @param mate �O?ς��鑊��̃x�N�g��
     * @return mate �Ƃ̊O?ς� Z ?���
     */
    public double zOfCrossProduct(Vector2D mate) {
        return x() * mate.y() - y() * mate.x();
    }

    /**
     * �x�N�g�����m�̘a��Ԃ�?B
     *
     * @param mate �a���鑊��̃x�N�g��
     * @return �x�N�g���̘a (this + mate)
     */
    public Vector2D add(Vector2D mate) {
        return new LiteralVector2D(x() + mate.x(), y() + mate.y());
    }

    /**
     * �x�N�g�����m��?���Ԃ�?B
     *
     * @param mate ?����鑊��̃x�N�g��
     * @return �x�N�g����?� (this - mate)
     */
    public Vector2D subtract(Vector2D mate) {
        return new LiteralVector2D(x() - mate.x(), y() - mate.y());
    }

    /**
     * �^����ꂽ�X�P?[����?悶���x�N�g����Ԃ�?B
     *
     * @param scale �X�P?[��
     * @return (this * scale)
     */
    public Vector2D multiply(double scale) {
        return new LiteralVector2D(x() * scale, y() * scale);
    }

    /**
     * �^����ꂽ�X�P?[���Ŋ��B��x�N�g����Ԃ�?B
     *
     * @param scale �X�P?[��
     * @return (this / scale)
     */
    public Vector2D divide(double scale) {
        return new LiteralVector2D(x() / scale, y() / scale);
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
     * @see #identicalDirection(Vector2D)
     */
    public boolean identical(Vector2D mate) {
        double dTol2 = getToleranceForDistance2();
        double xv, yv;

        xv = x() - mate.x();
        yv = y() - mate.y();

        return xv * xv + yv * yv < dTol2;
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
     * @see #identical(Vector2D)
     * @see #identicalDirection(Vector2D)
     * @see #parallelDirection(Vector2D)
     */
    private boolean identicalDirection(Vector2D mate, boolean allowReversed) {
        double aTol = getToleranceForAngle();
        double dTol2 = getToleranceForDistance2();
        double dotProd, zOfCrossProd;
        boolean result; // return value

        if (this.norm() < dTol2 || mate.norm() < dTol2) {
            result = true;
        } else {
            dotProd = dotProduct(mate); // cos(theta)*|this|*|mate|
            if (allowReversed)
                dotProd = Math.abs(dotProd);
            zOfCrossProd = zOfCrossProduct(mate); // sin(theta)*|this|*|mate|
            result = Math.abs(Math.atan2(zOfCrossProd, dotProd)) < aTol;
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
     * @see #identical(Vector2D)
     * @see #parallelDirection(Vector2D)
     */
    public boolean identicalDirection(Vector2D mate) {
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
     * @see #identicalDirection(Vector2D)
     */
    public boolean parallelDirection(Vector2D mate) {
        return identicalDirection(mate, true);
    }

    /**
     * �x�N�g���̃m������Ԃ�?B
     *
     * @return �x�N�g���̃m���� (x^2) + (y^2)
     */
    public double norm() {
        double xv = x();
        double yv = y();

        return xv * xv + yv * yv;
    }

    /**
     * �Q�����̓_ (Point2D) �ɕϊ�����?B
     *
     * @return ���_����̈ʒu�x�N�g���Ƃ݂Ȃ����_
     */
    public Point2D toPoint2D() {
        return Point2D.of(x(), y());
    }

    /**
     * double�̔z��ɕϊ�����?B
     *
     * @return ?����l����double�̔z��
     */
    public double[] toDoubleArray() {
        double[] array = {x(), y()};
        return array;
    }

    /**
     * Get a vector orthogonal to the instance.
     *
     * @return a new normalized vector orthogonal to the instance
     * @throws ArithmeticException if the norm of the instance is null
     */
    public Vector2D orthogonal() {

        double threshold = 0.6 * norm();
        if (threshold == 0) {
            throw new ArithmeticException("null norm");
        }

        double inverse = 1 / Math.sqrt(x() * x() + y() * y());
        return new LiteralVector2D(inverse * y(), -inverse * x());

    }

    /*
    * ���̃x�N�g���Ƃ̊p�x (���W�A��) ��Ԃ�?B
    * <p>
    * this ���� mate �ւ�?����̊p�x (0 ?` 2pi)
    * </p>
    *
    * @param mate	����̃x�N�g��
    * @return	����̃x�N�g���Ƃ̊p�x
    */
    public double angleWith(Vector2D mate) {
        Vector2D thisUnitVec;
        Vector2D mateUnitVec;

        try {
            thisUnitVec = this.unitized();
            mateUnitVec = mate.unitized();
        } catch (ZeroLengthException e) {
            return 0.0;
        }

        /*
        * dot product --> radian
        */
        double dotProduct = thisUnitVec.dotProduct(mateUnitVec);
        if (dotProduct > 1.0) dotProduct = 1.0;
        if (dotProduct < (-1.0)) dotProduct = (-1.0);

        double theta = Math.acos(dotProduct);

        if (thisUnitVec.zOfCrossProduct(mateUnitVec) < 0.0)
            theta = (Math.PI * 2) - theta;

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
    protected abstract Vector2D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator2D transformationOperator,
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
    public synchronized Vector2D
    transformBy(boolean reverseTransform,
                CartesianTransformationOperator2D transformationOperator,
                java.util.Hashtable transformedGeometries) {
        if (transformedGeometries == null)
            return this.doTransformBy(reverseTransform,
                    transformationOperator,
                    transformedGeometries);

        Vector2D transformed = (Vector2D) transformedGeometries.get(this);
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
    public synchronized Vector2D
    transformBy(CartesianTransformationOperator2D transformationOperator,
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
    public synchronized Vector2D
    reverseTransformBy(CartesianTransformationOperator2D transformationOperator,
                       java.util.Hashtable transformedGeometries) {
        return this.transformBy(true,
                transformationOperator,
                transformedGeometries);
    }

    /**
     * LiteralVector2D �̃C���X�^���X��?�?�����?B
     *
     * @param x X ?���
     * @param y Y ?���
     * @return LiteralVector2D �̃C���X�^���X
     */
    public static LiteralVector2D of(double x,
                                     double y) {
        return new LiteralVector2D(x, y);
    }

    /**
     * LiteralVector2D �̃C���X�^���X��?�?�����?B
     *
     * @param components X, Y?����̔z�� (�v�f?� 2)
     * @return LiteralVector2D �̃C���X�^���X
     */
    public static LiteralVector2D of(double[] components) {
        return new LiteralVector2D(components);
    }
}
