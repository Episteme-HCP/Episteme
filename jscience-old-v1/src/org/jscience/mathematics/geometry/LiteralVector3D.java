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
 * �R����?F�e?�������?��Œ�`���ꂽ�x�N�g����\���N���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:14 $
 * @see HomogeneousVector3D
 */

public class LiteralVector3D extends Vector3D {

    /**
     * X ?���?B
     *
     * @serial
     */
    private final double x;

    /**
     * Y ?���?B
     *
     * @serial
     */
    private final double y;

    /**
     * Z ?���?B
     *
     * @serial
     */
    private final double z;

    /**
     * (x, y, z) �Œ�`�����I�u�W�F�N�g��?\�z����?B
     *
     * @param x X ?���
     * @param y Y ?���
     * @param z Z ?���
     */
    public LiteralVector3D(double x, double y, double z) {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * (c[0], c[1], c[2]) �Œ�`�����I�u�W�F�N�g��?\�z����?B
     *
     * @param c ?����̔z��
     */
    public LiteralVector3D(double[] c) {
        super();
        this.x = c[0];
        this.y = c[1];
        this.z = c[2];
    }

    /**
     * Simple constructor.
     * Build a vector from its azimuthal coordinates
     *
     * @param alpha azimuth around Z
     *              (0 is +X, PI/2 is +Y, PI is -X and 3PI/2 is -Y)
     * @param delta elevation above (XY) plane, from -PI to +PI
     */
    public LiteralVector3D(double alpha, double delta) {
        double cosDelta = Math.cos(delta);
        this.x = Math.cos(alpha) * cosDelta;
        this.y = Math.sin(alpha) * cosDelta;
        this.z = Math.sin(delta);
    }

    /**
     * Multiplicative constructor
     * Build a vector from another one and a scale factor.
     * The vector built will be a * u
     *
     * @param a scale factor
     * @param u base (unscaled) vector
     */
    public LiteralVector3D(double a, LiteralVector3D u) {
        this.x = a * u.x;
        this.y = a * u.y;
        this.z = a * u.z;
    }

    /**
     * Linear constructor
     * Build a vector from two other ones and corresponding scale factors.
     * The vector built will be a * u +  b * v
     *
     * @param a first scale factor
     * @param u first base (unscaled) vector
     * @param b second scale factor
     * @param v second base (unscaled) vector
     */
    public LiteralVector3D(double a, LiteralVector3D u, double b, LiteralVector3D v) {
        this.x = a * u.x + b * v.x;
        this.y = a * u.y + b * v.y;
        this.z = a * u.z + b * v.z;
    }

    /**
     * (x, y, z) �Œ�`�����I�u�W�F�N�g��?\�z����?B
     *
     * @param x                   X ?���
     * @param y                   Y ?���
     * @param confirmedAsUnitized ?�?����悤�Ƃ���x�N�g����
     *                            �P�ʃx�N�g���ł��邱�Ƃ������BĂ���Ȃ�� <code>true</code>?A
     *                            ����Ȃ��� <code>false</code>
     */
    LiteralVector3D(double x, double y, double z,
                    boolean confirmedAsUnitized) {
        super(confirmedAsUnitized);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * (c[0], c[1], c[2]) �Œ�`�����I�u�W�F�N�g��?\�z����?B
     *
     * @param c                   ?����̔z��
     * @param confirmedAsUnitized ?�?����悤�Ƃ���x�N�g����
     *                            �P�ʃx�N�g���ł��邱�Ƃ������BĂ���Ȃ�� <code>true</code>?A
     *                            ����Ȃ��� <code>false</code>
     */
    LiteralVector3D(double[] c, boolean confirmedAsUnitized) {
        super(confirmedAsUnitized);
        this.x = c[0];
        this.y = c[1];
        this.z = c[2];
    }

    /**
     * X ?�����Ԃ�?B
     *
     * @return �x�N�g���� X ?���
     */
    public double x() {
        return this.x;
    }

    /**
     * Y ?�����Ԃ�?B
     *
     * @return �x�N�g���� Y ?���
     */
    public double y() {
        return this.y;
    }

    /**
     * Z ?�����Ԃ�?B
     *
     * @return �x�N�g���� Z ?���
     */
    public double z() {
        return this.z;
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
    protected synchronized Vector3D
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

        writer.println(indent_tab
                + getClassName() + " "
                + x() + " "
                + y() + " "
                + z() + " End");
    }
}

