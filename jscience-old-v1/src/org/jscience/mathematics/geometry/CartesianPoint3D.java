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
 * �R����?F�e?W�l����?��Œ�`���ꂽ�_��\���N���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:15:54 $
 * @see HomogeneousPoint3D
 */

public class CartesianPoint3D extends Point3D {
    /**
     * X ?W�l?B
     *
     * @serial
     */
    private final double x;

    /**
     * Y ?W�l?B
     *
     * @serial
     */
    private final double y;

    /**
     * Z ?W�l?B
     *
     * @serial
     */
    private final double z;

    /**
     * (x, y, z) �Œ�`�����I�u�W�F�N�g��?\�z����?B
     *
     * @param x X ?W�l
     * @param y Y ?W�l
     * @param z Z ?W�l
     */
    public CartesianPoint3D(double x, double y, double z) {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * (components[0], components[1], components[2]) �Œ�`�����
     * �I�u�W�F�N�g��?\�z����?B
     *
     * @param components ?W�l�̔z��
     */
    public CartesianPoint3D(double[] components) {
        super();
        this.x = components[0];
        this.y = components[1];
        this.z = components[2];
    }

    /**
     * X ?W�l��Ԃ�?B
     *
     * @return X ?W�l
     */
    public double x() {
        return x;
    }

    /**
     * Y ?W�l��Ԃ�?B
     *
     * @return Y ?W�l
     */
    public double y() {
        return y;
    }

    /**
     * Z ?W�l��Ԃ�?B
     *
     * @return Z ?W�l
     */
    public double z() {
        return z;
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
    protected synchronized Point3D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator3D transformationOperator,
                  java.util.Hashtable transformedGeometries) {
        if (reverseTransform == false)
            return transformationOperator.transform(this);
        else
            return transformationOperator.reverseTransform(this);
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

        writer.println(indent_tab +
                getClassName() +
                " " + x() +
                " " + y() +
                " " + z() +
                " End");
    }
}
