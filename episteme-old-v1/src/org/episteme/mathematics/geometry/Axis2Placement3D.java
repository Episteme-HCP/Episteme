/*
 * �R���� : ���_��?W�Ƃy������тw����w�肷����Œ�`���ꂽ��?�?W�n (�z�u?��) ��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Axis2Placement3D.java,v 1.3 2006/03/01 21:15:52 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import java.io.PrintWriter;

/**
 * �R���� : ���_��?W�Ƃy������тw����w�肷����Œ�`���ꂽ��?�?W�n (�z�u?��) ��\���N���X?B
 * <p/>
 * ���̃N���X��?A
 * �~??��?��񎟋Ȗʂ̈ʒu��X��������?A
 * ?W�ϊ��̕ϊ�?s��̎w���Ȃǂɗ��p����܂�?B
 * </p>
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * ��?�?W�n�̌��_�ƂȂ�_ location
 * ��?A
 * ��?�?W�n�̂y���̕����x�N�g�� axis
 * �����
 * axis ��@?���Ƃ��镽�ʂ�?����e����邱�Ƃ�
 * �w���̕��숒肷��x�N�g�� refDirection
 * ��ێ?���܂�?B
 * ���̋�?�?W�n��?A�E��n�̒���?W�n��\���̂�?A
 * �x���̕���?A�y��?^�w���̕���莩���I�Ɍ��肳��܂�?B
 * �y���Ƃw���̊O?ϕ��x���̕��ɂȂ�܂�?B
 * �w�x�y�̊e���x�N�g����?A?�ɂ��̑傫���� 1 �ɒP�ʉ����Ĉ����܂�?B
 * �Ȃ�?A�����̃x�N�g����
 * {@link GeometrySchemaFunction#buildAxes(Vector3D,Vector3D)
 * GeometrySchemaFunction.buildAxes}(axis, refDirection)
 * �ɂ�Bċ?�߂Ă��܂�?B
 * </p>
 * <p/>
 * location �ɗ^����_�ɂ͓B�?��͂Ȃ�?A
 * {@link Point3D Point3D} �N���X�̔C�ӂ̓_��^���邱�Ƃ��ł��܂���?A
 * null ��w�肷�邱�Ƃ͂ł��܂���?B
 * axis ����� refDirection �ɗ^����x�N�g�� {@link Vector3D Vector3D}
 * �͓BɒP�ʉ�����Ă���K�v�͂���܂���?A
 * �[�?�x�N�g����^���邱�Ƃ͂ł��܂���?B
 * �Ȃ�?Aaxis ����� refDirection �ɗ^����x�N�g����
 * �w�肵�Ȃ� (null ��w�肷��) ���Ƃ�ł��܂�?B
 * axis �� null �Ƃ����?A�y���̕��� (0, 0, 1) �ɓ�������̂Ɖ�߂���܂�?B
 * refDirection �� null �Ƃ����?A
 * refDirection �̕��� (1, 0, 0) �ɓ�������̂Ɖ�߂���܂�?B
 * </p>
 * <p/>
 * �܂�?Aaxis �� refDirection ��?A�t���܂߂�?A
 * ����̕���w���Ă͂����܂���?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:15:52 $
 * @see Axis1Placement3D
 * @see Axis2Placement2D
 */

public class Axis2Placement3D extends Placement3D {
    /**
     * ��?W�n?B
     * <p/>
     * (0, 0, 0) �촓_?A
     * (0, 0, 1) �� Z ���̕��?A
     * (1, 0, 0) �� X ���̕��Ƃ���?W�n?B
     * </p>
     */
    public static final Axis2Placement3D origin;

    /**
     * static �ȃt�B?[���h�ɒl��?ݒ肷��?B
     */
    static {
        origin = new Axis2Placement3D(Point3D.origin,
                Vector3D.zUnitVector,
                Vector3D.xUnitVector);
    }

    /**
     * �y���̕���\���x�N�g��?B
     *
     * @serial
     */
    private final Vector3D axis;

    /**
     * �w���̕��숒肷��x�N�g��?B
     *
     * @serial
     */
    private final Vector3D refDirection;

    /**
     * �w/�x/�y����\���P�ʃx�N�g��?B
     * <p/>
     * �K�v�ɉ����ăL���b�V�������?B
     * </p>
     *
     * @serial
     */
    private Vector3D[] axes;

    /**
     * ��?�?W�n�̌��_�ƂȂ�_��
     * �y���̕����x�N�g�������
     * �w���̕��숒肷��x�N�g����^����?A
     * �I�u�W�F�N�g��?\�z����?B
     * <p/>
     * location �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * axis �� null �ł�?\��Ȃ�?B
     * axis �� null ��?�?��ɂ�?A
     * �y���̕��� (0, 0, 1) �ɓ�������̂Ɖ�߂����?B
     * </p>
     * <p/>
     * refDirection �� null �ł�?\��Ȃ�?B
     * refDirection �� null ��?�?��ɂ�?A
     * refDirection �̕��� (1, 0, 0) �ɓ�������̂Ɖ�߂����?B
     * </p>
     * <p/>
     * axis ���邢�� refDirection �̑傫����?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?��ȉ���?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * �����?Aaxis �� refDirection �̊O?ς̑傫����
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?��ȉ���?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param location     ��?�?W�n�̌��_
     * @param axis         �y���̕����x�N�g��
     * @param refDirection �w���̕��숒肷��x�N�g��
     * @see ConditionOfOperation
     * @see InvalidArgumentValueException
     */
    public Axis2Placement3D(Point3D location,
                            Vector3D axis,
                            Vector3D refDirection) {
        super(location);
        this.axis = axis;
        this.refDirection = refDirection;
        checkFields();
    }

    /**
     * �y���̕����x�N�g��?A�����
     * �w���̕��숒肷��x�N�g���̑Ó�?���`�F�b�N����?B
     * <p/>
     * axis �� null �ł�?\��Ȃ�?B
     * axis �� null ��?�?��ɂ�?A
     * �y���̕��� (0, 0, 1) �ɓ�������̂Ɖ�߂����?B
     * </p>
     * <p/>
     * refDirection �� null �ł�?\��Ȃ�?B
     * refDirection �� null ��?�?��ɂ�?A
     * refDirection �̕��� (1, 0, 0) �ɓ�������̂Ɖ�߂����?B
     * </p>
     * <p/>
     * axis ���邢�� refDirection �̑傫����?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?��ȉ���?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * �����?Aaxis �� refDirection �̊O?ς̑傫����
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?��ȉ���?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @see ConditionOfOperation
     * @see InvalidArgumentValueException
     */
    private void checkFields() {
        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double tol_2d = condition.getToleranceForDistance();

        tol_2d *= tol_2d;

        if (axis != null) {
            if (axis.norm() <= tol_2d) {
                throw new InvalidArgumentValueException();
            }
        }

        if (refDirection != null) {
            if (refDirection.norm() <= tol_2d) {
                throw new InvalidArgumentValueException();
            }
        }

        if (axis != null || refDirection != null) {
            Vector3D a, b;
            if (axis == null) {
                a = Vector3D.zUnitVector;
            } else {
                a = axis;
            }
            if (refDirection == null) {
                b = Vector3D.xUnitVector;
            } else {
                b = refDirection;
            }
            if (true) {
                /* see p30 of ISO10303-42 */
                Vector3D c;
                c = a.crossProduct(b);
                if (c.norm() <= tol_2d) {
                    throw new InvalidArgumentValueException();
                }
            } else {
                if (a.identicalDirection(b)) {
                    throw new InvalidArgumentValueException();
                }
            }
        }
    }

    /**
     * ���̋�?�?W�n�̂y���̕���\���x�N�g����Ԃ�?B
     * <p/>
     * �I�u�W�F�N�g��?\�z���ɗ^����ꂽ axis ��Ԃ�?B
     * ��B�?Anull ���Ԃ邱�Ƃ �肤��?B
     * </p>
     *
     * @return �y���̕���\���x�N�g��
     */
    public Vector3D axis() {
        return axis;
    }

    /**
     * ���̋�?�?W�n�̂y���̕���\�� (���I��) �x�N�g����Ԃ�?B
     * <p/>
     * axis �� null �łȂ����?Aaxis ��Ԃ�?B
     * axis �� null �Ȃ��?A(0, 0, 1) �̃x�N�g����Ԃ�?B
     * </p>
     *
     * @return �y���̕���\�� (���I��) �x�N�g��
     */
    public Vector3D effectiveAxis() {
        return (axis != null)
                ? axis : GeometrySchemaFunction.defaultAxis3D;
    }

    /**
     * ���̋�?�?W�n�̂w���̕��숒肷��x�N�g����Ԃ�?B
     * <p/>
     * �I�u�W�F�N�g��?\�z���ɗ^����ꂽ refDirection ��Ԃ�?B
     * ��B�?Anull ���Ԃ邱�Ƃ �肤��?B
     * </p>
     *
     * @return �w���̕��숒肷��x�N�g��
     */
    public Vector3D refDirection() {
        return refDirection;
    }

    /**
     * ���̋�?�?W�n�̂w���̕��숒肷�� (���I��) �x�N�g����Ԃ�?B
     * <p/>
     * refDirection �� null �łȂ����?ArefDirection ��Ԃ�?B
     * refDirection �� null �Ȃ��?A(1, 0, 0) �̃x�N�g����Ԃ�?B
     * </p>
     *
     * @return �w���̕��숒肷�� (���I��) �x�N�g��
     */
    public Vector3D effectiveRefDirection() {
        return (refDirection != null)
                ? refDirection : GeometrySchemaFunction.defaultRefDirection3D;
    }

    /**
     * ���̋�?�?W�n�̂w����\���P�ʃx�N�g����Ԃ�?B
     *
     * @return �w����\���P�ʃx�N�g��
     */
    public Vector3D x() {
        if (axes == null)
            axes();
        return axes[0];
    }

    /**
     * ���̋�?�?W�n�̂x����\���P�ʃx�N�g����Ԃ�?B
     *
     * @return �x����\���P�ʃx�N�g��
     */
    public Vector3D y() {
        if (axes == null)
            axes();
        return axes[1];
    }

    /**
     * ���̋�?�?W�n�̂y����\���P�ʃx�N�g����Ԃ�?B
     *
     * @return �y����\���P�ʃx�N�g��
     */
    public Vector3D z() {
        if (axes == null)
            axes();
        return axes[2];
    }

    /**
     * ���̋�?�?W�n�̂w/�x/�y����\���P�ʃx�N�g����Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ�z���?�?��̗v�f���w��?A
     * ��Ԗڂ̗v�f���x��?A
     * �O�Ԗڂ̗v�f���y����\��?B
     * </p>
     *
     * @return �w/�x/�y����\���P�ʃx�N�g���̔z��
     * @see GeometrySchemaFunction#buildAxes(Vector3D,Vector3D)
     */
    public Vector3D[] axes() {
        if (axes == null) {
            axes = GeometrySchemaFunction.buildAxes(axis, refDirection);
        }
        return (Vector3D[]) axes.clone();
    }

    /**
     * ���̋�?�?W�n�̌��_�ƂȂ�_��?A�^����ꂽ�x�N�g����?]�B�
     * ��?s�ړ���������?�?W�n��Ԃ�?B
     *
     * @param moveVec ��?s�ړ�������Ɨʂ�w�肷��x�N�g��
     * @return ��?s�ړ���̋�?�?W�n
     */
    public Axis2Placement3D parallelTranslate(Vector3D moveVec) {
        return new Axis2Placement3D(location().add(moveVec), axis, refDirection);
    }

    /**
     * ���̋�?�?W�n��?A
     * �^����ꂽ��?�?W�n�̂y���̎���?A
     * �^����ꂽ�p�x������]��������?�?W�n��Ԃ�?B
     * <p/>
     * �Ă�?o������?A(rCos * rCos + rSin * rSin) �̒l�� 1 �ł��邱�Ƃ�
     * ��?؂��Ȃ���΂Ȃ�Ȃ�?B
     * </p>
     *
     * @param trns ��]�̎���^�����?�?W�n��?W�ϊ����Z�q
     * @param rCos cos(��]�p�x)
     * @param rSin sin(��]�p�x)
     * @return ��]��̋�?�?W�n
     * @see Point3D#rotateZ(CartesianTransformationOperator3D,double,double)
     * @see Vector3D#rotateZ(CartesianTransformationOperator3D,double,double)
     */
    Axis2Placement3D rotateZ(CartesianTransformationOperator3D trns,
                             double rCos, double rSin) {
        Point3D rloc = location().rotateZ(trns, rCos, rSin);
        Vector3D raxis = z().rotateZ(trns, rCos, rSin);
        Vector3D rref = x().rotateZ(trns, rCos, rSin);
        return new Axis2Placement3D(rloc, raxis, rref);
    }

    /**
     * ���̋�?�?W�n������I��?W�n�ւ�?W�ϊ����Z�q��Ԃ�?B
     *
     * @param scale �X�P?[�����O�̗ʂ�K�肷��l
     * @return �X�P?[�����O��܂�?W�ϊ����Z�q
     * @see #toCartesianTransformationOperator()
     */
    public CartesianTransformationOperator3D
    toCartesianTransformationOperator(double scale) {
        return new CartesianTransformationOperator3D(this, scale);
    }

    /**
     * ���̋�?�?W�n������I��?W�n�ւ�?W�ϊ����Z�q (?k�ڔ� 1 : 1) ��Ԃ�?B
     *
     * @return �X�P?[�����O��܂܂Ȃ�?W�ϊ����Z�q
     * @see #toCartesianTransformationOperator(double)
     */
    public CartesianTransformationOperator3D
    toCartesianTransformationOperator() {
        return new CartesianTransformationOperator3D(this);
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
    protected synchronized Axis2Placement3D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator3D transformationOperator,
                  java.util.Hashtable transformedGeometries) {
        Point3D tLocation =
                this.location().transformBy(reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        Vector3D tAxis =
                this.effectiveAxis().transformBy(reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        Vector3D tRefDirection =
                this.effectiveRefDirection().transformBy(reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        return new Axis2Placement3D(tLocation, tAxis, tRefDirection);
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
    public synchronized Axis2Placement3D
    transformBy(boolean reverseTransform,
                CartesianTransformationOperator3D transformationOperator,
                java.util.Hashtable transformedGeometries) {
        if (transformedGeometries == null)
            return this.doTransformBy(reverseTransform,
                    transformationOperator,
                    transformedGeometries);

        Axis2Placement3D transformed = (Axis2Placement3D) transformedGeometries.get(this);
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
    public synchronized Axis2Placement3D
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
    public synchronized Axis2Placement3D
    reverseTransformBy(CartesianTransformationOperator3D transformationOperator,
                       java.util.Hashtable transformedGeometries) {
        return this.transformBy(true,
                transformationOperator,
                transformedGeometries);
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
        writer.println(indent_tab + "\tlocation");
        location().output(writer, indent + 2);
        if (axis != null) {
            writer.println(indent_tab + "\taxis");
            axis.output(writer, indent + 2);
        }
        if (refDirection != null) {
            writer.println(indent_tab + "\trefDirection");
            refDirection.output(writer, indent + 2);
        }
        writer.println(indent_tab + "End");
    }
}

