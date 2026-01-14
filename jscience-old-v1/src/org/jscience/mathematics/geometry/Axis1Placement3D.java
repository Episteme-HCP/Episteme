/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.mathematics.geometry;

import java.io.PrintWriter;

/**
 * �R���� : ���_��?W�Ƃy���̕��݂̂��`�����?�?W�n (�z�u?��) ��\���N���X?B
 * <p/>
 * ���̃N���X��?A
 * {@link SurfaceOfRevolution3D ��]��} �̒�?S�������ƂȂǂɗ��p����܂�?B
 * </p>
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * ��?�?W�n�̌��_�ƂȂ�_ location
 * ��?A
 * ��?�?W�n�̂y���̕����x�N�g�� axis
 * ��ێ?���܂�?B
 * ���̋�?�?W�n��?A(�Öق�) �E��n�̒���?W�n��\���܂���?A
 * ��?g�ł͂w������тx���̕��͌��肵�܂���?B
 * �w���̕��Ƃx���̕��̌����?A���̋�?�?W�n�𗘗p���鑤�ɔC����܂�?B
 * </p>
 * <p/>
 * �y�̊��x�N�g����?A?�ɂ��̑傫���� 1 �ɒP�ʉ����Ĉ����܂�?B
 * </p>
 * <p/>
 * location �ɗ^����_�ɂ͓B�?��͂Ȃ�?A
 * {@link Point3D Point3D} �N���X�̔C�ӂ̓_��^���邱�Ƃ��ł��܂���?A
 * null ��w�肷�邱�Ƃ͂ł��܂���?B
 * axis �ɗ^����x�N�g�� {@link Vector3D Vector3D}
 * �͓BɒP�ʉ�����Ă���K�v�͂���܂���?A
 * �[�?�x�N�g����^���邱�Ƃ͂ł��܂���?B
 * �Ȃ�?Aaxis �ɗ^����x�N�g����
 * �w�肵�Ȃ� (null ��w�肷��) ���Ƃ�ł��܂�?B
 * axis �� null �Ƃ����?A�y���̕��� (0, 0, 1) �ɓ�������̂Ɖ�߂���܂�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:15:52 $
 * @see Axis2Placement3D
 */

public class Axis1Placement3D extends Placement3D {
    /**
     * �y���̕���\���x�N�g��?B
     *
     * @serial
     */
    private Vector3D axis;

    /**
     * �y����\���P�ʃx�N�g��?B
     * <p/>
     * �K�v�ɉ����ăL���b�V�������?B
     * </p>
     *
     * @serial
     */
    private Vector3D z;

    /**
     * ��?�?W�n�̌��_�ƂȂ�_��
     * �y���̕����x�N�g����^����?A
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
     * axis �̑傫����?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?��ȉ���?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param location ��?�?W�n�̌��_
     * @param axis     �y���̕����x�N�g��
     */
    public Axis1Placement3D(Point3D location, Vector3D axis) {
        super(location);
        this.axis = axis;
        checkAxis();
    }

    /**
     * �y���̕����x�N�g���̑Ó�?���`�F�b�N����?B
     * <p/>
     * axis �� null �ł�?\��Ȃ�?B
     * axis �� null ��?�?��ɂ�?A
     * �y���̕��� (0, 0, 1) �ɓ�������̂Ɖ�߂����?B
     * </p>
     * <p/>
     * axis �̑傫����?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?��ȉ���?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @see ConditionOfOperation
     * @see InvalidArgumentValueException
     */
    private void checkAxis() {
        if (axis != null) {
            ConditionOfOperation condition =
                    ConditionOfOperation.getCondition();
            double tol_d = condition.getToleranceForDistance();

            if (axis.norm() <= tol_d * tol_d) {
                throw new InvalidArgumentValueException();
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
     * ���̋�?�?W�n�̂y����\���P�ʃx�N�g����Ԃ�?B
     *
     * @return �y����\���P�ʃx�N�g��
     */
    public Vector3D z() {
        if (z == null) {
            z = (axis != null) ? axis.unitized() : GeometrySchemaFunction.defaultAxis3D;
        }
        return z;
    }

    /**
     * ���̋�?�?W�n�̌��_��ʂ�?A�y���̕���?L�т钼?��Ԃ�?B
     *
     * @return ���_��ʂ�?A�y���̕���?L�т钼?�
     */
    public Line3D toLine() {
        return new Line3D(location(), z());
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
    protected synchronized Axis1Placement3D
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
        return new Axis1Placement3D(tLocation, tAxis);
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
    public synchronized Axis1Placement3D
    transformBy(boolean reverseTransform,
                CartesianTransformationOperator3D transformationOperator,
                java.util.Hashtable transformedGeometries) {
        if (transformedGeometries == null)
            return this.doTransformBy(reverseTransform,
                    transformationOperator,
                    transformedGeometries);

        Axis1Placement3D transformed = (Axis1Placement3D) transformedGeometries.get(this);
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
    public synchronized Axis1Placement3D
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
    public synchronized Axis1Placement3D
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
        writer.println(indent_tab + "End");
    }
}

