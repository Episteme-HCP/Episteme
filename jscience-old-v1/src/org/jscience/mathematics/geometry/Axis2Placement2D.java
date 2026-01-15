/*
 * �Q���� : ���_��?W�Ƃw���̕��Œ�`���ꂽ��?�?W�n (�z�u?��) ��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Axis2Placement2D.java,v 1.3 2006/03/01 21:15:52 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

import java.io.PrintWriter;

/**
 * �Q���� : ���_��?W�Ƃw���̕��Œ�`���ꂽ��?�?W�n (�z�u?��) ��\���N���X?B
 * <p/>
 * ���̃N���X��?A
 * �~??��?�̈ʒu��X��������?A
 * ?W�ϊ��̕ϊ�?s��̎w���Ȃǂɗ��p����܂�?B
 * </p>
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * ��?�?W�n�̌��_�ƂȂ�_ location
 * ��?A
 * ��?�?W�n�̂w���̕����x�N�g�� refDirection
 * ��ێ?���܂�?B
 * ���̋�?�?W�n��?A�E��n�̒���?W�n��\���̂�?A
 * �x���̕���?A�w���̕���莩���I�Ɍ��肳��܂�?B
 * �w���̕���?����� 90 �x��]���������x���̕��ƂȂ�܂�?B
 * �w�x���̊e���x�N�g����?A?�ɂ��̑傫���� 1 �ɒP�ʉ����Ĉ����܂�?B
 * �Ȃ�?A�����̃x�N�g����
 * {@link GeometrySchemaFunction#build2Axes(Vector2D)
 * GeometrySchemaFunction.build2Axes}(refDirection)
 * �ɂ�Bċ?�߂Ă��܂�?B
 * </p>
 * <p/>
 * location �ɗ^����_�ɂ͓B�?��͂Ȃ�?A
 * {@link Point2D Point2D} �N���X�̔C�ӂ̓_��^���邱�Ƃ��ł��܂���?A
 * null ��w�肷�邱�Ƃ͂ł��܂���?B
 * refDirection �ɗ^����x�N�g�� {@link Vector2D Vector2D}
 * �͓BɒP�ʉ�����Ă���K�v�͂���܂���?A
 * �[�?�x�N�g����^���邱�Ƃ͂ł��܂���?B
 * �Ȃ�?ArefDirection �ɗ^����x�N�g����
 * �w�肵�Ȃ� (null ��w�肷��) ���Ƃ�ł��܂�?B
 * ����?�?��ɂ�?A�w���̕��� (1, 0) �ɓ�������̂Ɖ�߂���܂�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:15:52 $
 * @see Axis2Placement3D
 */

public class Axis2Placement2D extends Placement2D {
    /**
     * ��?W�n?B
     * <p/>
     * (0, 0) �촓_?A(1, 0) �� X ���̕��Ƃ���?W�n?B
     * </p>
     */
    public static final Axis2Placement2D origin;

    /**
     * static �ȃt�B?[���h�ɒl��?ݒ肷��?B
     */
    static {
        origin = new Axis2Placement2D(Point2D.origin,
                Vector2D.xUnitVector);
    }

    /**
     * �w���̕���\���x�N�g��?B
     *
     * @serial
     */
    private final Vector2D refDirection;

    /**
     * �w/�x����\���P�ʃx�N�g��?B
     * <p/>
     * �K�v�ɉ����ăL���b�V�������?B
     * </p>
     *
     * @serial
     */
    private Vector2D[] axes;

    /**
     * ��?�?W�n�̌��_�ƂȂ�_��
     * �w���̕����x�N�g����^����?A
     * �I�u�W�F�N�g��?\�z����?B
     * <p/>
     * location �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * refDirection �� null �ł�?\��Ȃ�?B
     * refDirection �� null ��?�?��ɂ�?A
     * �w���̕��� (1, 0) �ɓ�������̂Ɖ�߂����?B
     * </p>
     * <p/>
     * refDirection �̑傫����?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?��ȉ���?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param location     ���_�ƂȂ�_
     * @param refDirection �w���̕����x�N�g��
     * @see ConditionOfOperation
     * @see InvalidArgumentValueException
     */
    public Axis2Placement2D(Point2D location,
                            Vector2D refDirection) {
        super(location);
        this.refDirection = refDirection;
        checkRefDirection();
    }

    /**
     * �w���̕����x�N�g���̑Ó�?���`�F�b�N����?B
     * <p/>
     * refDirection �� null �ł�?\��Ȃ�?B
     * refDirection �� null ��?�?��ɂ�?A
     * �w���̕��� (1, 0) �ɓ�������̂Ɖ�߂����?B
     * </p>
     * <p/>
     * refDirection �̑傫����?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?��ȉ���?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @see ConditionOfOperation
     * @see InvalidArgumentValueException
     */
    private void checkRefDirection() {
        if (refDirection != null) {
            ConditionOfOperation condition =
                    ConditionOfOperation.getCondition();
            double tol_d = condition.getToleranceForDistance();
            if (refDirection.norm() <= tol_d * tol_d) {
                throw new InvalidArgumentValueException();
            }
        }
    }

    /**
     * ���̋�?�?W�n�̂w���̕����x�N�g����Ԃ�?B
     * <p/>
     * �I�u�W�F�N�g��?\�z���ɗ^����ꂽ refDirection ��Ԃ�?B
     * ��B�?Anull ���Ԃ邱�Ƃ �肤��?B
     * </p>
     *
     * @return �w���̕����x�N�g��
     */
    public Vector2D refDirection() {
        return refDirection;
    }

    /**
     * ���̋�?�?W�n�̂w���̕��� (���I��) �x�N�g����Ԃ�?B
     * <p/>
     * refDirection �� null �łȂ����?ArefDirection ��Ԃ�?B
     * refDirection �� null �Ȃ��?A(1, 0) �̃x�N�g����Ԃ�?B
     * </p>
     *
     * @return �w���̕��� (���I��) �x�N�g��
     */
    public Vector2D effectiveRefDirection() {
        return (refDirection != null)
                ? refDirection : GeometrySchemaFunction.defaultRefDirection2D;
    }

    /**
     * ���̋�?�?W�n�̂w����\���P�ʃx�N�g����Ԃ�?B
     *
     * @return �w����\���P�ʃx�N�g��
     */
    public Vector2D x() {
        if (axes == null)
            axes();
        return axes[0];
    }

    /**
     * ���̋�?�?W�n�̂x����\���P�ʃx�N�g����Ԃ�?B
     *
     * @return �x����\���P�ʃx�N�g��
     */
    public Vector2D y() {
        if (axes == null)
            axes();
        return axes[1];
    }

    /**
     * ���̋�?�?W�n�̂w/�x����\���P�ʃx�N�g����Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ�z���?�?��̗v�f���w��?A
     * ��Ԗڂ̗v�f���x����\��?B
     * </p>
     *
     * @return �w/�x����\���P�ʃx�N�g���̔z��
     * @see GeometrySchemaFunction#build2Axes(Vector2D)
     */
    public Vector2D[] axes() {
        if (axes == null) {
            axes = GeometrySchemaFunction.build2Axes(refDirection);
        }
        return (Vector2D[]) axes.clone();
    }

    /**
     * ���̋�?�?W�n������I��?W�n�ւ�?W�ϊ����Z�q��Ԃ�?B
     *
     * @param scale �X�P?[�����O�̗ʂ�K�肷��l
     * @return �X�P?[�����O��܂�?W�ϊ����Z�q
     */
    public CartesianTransformationOperator2D
    toCartesianTransformationOperator(double scale) {
        return new CartesianTransformationOperator2D(this, scale);
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
    protected synchronized Axis2Placement2D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator2D transformationOperator,
                  java.util.Hashtable transformedGeometries) {
        Point2D tLocation =
                this.location().transformBy(reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        Vector2D tRefDirection =
                this.effectiveRefDirection().transformBy(reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        return new Axis2Placement2D(tLocation, tRefDirection);
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
    public synchronized Axis2Placement2D
    transformBy(boolean reverseTransform,
                CartesianTransformationOperator2D transformationOperator,
                java.util.Hashtable transformedGeometries) {
        if (transformedGeometries == null)
            return this.doTransformBy(reverseTransform,
                    transformationOperator,
                    transformedGeometries);

        Axis2Placement2D transformed = (Axis2Placement2D) transformedGeometries.get(this);
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
    public synchronized Axis2Placement2D
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
    public synchronized Axis2Placement2D
    reverseTransformBy(CartesianTransformationOperator2D transformationOperator,
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
        if (refDirection != null) {
            writer.println(indent_tab + "\trefDirection");
            refDirection.output(writer, indent + 2);
        }
        writer.println(indent_tab + "End");
    }
}
