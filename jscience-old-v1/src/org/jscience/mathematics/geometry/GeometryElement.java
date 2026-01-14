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

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * GeometryElement ��?A�􉽗v�f�̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X�ł�?B
 * ���ׂĂ̊􉽗v�f�N���X��?A���̃X?[�p?[�N���X�Ƃ��� GeometryElement ��?���܂�?B
 * <p/>
 * GeometryElement ��?A
 * �􉽗v�f�̎�� (��?�Ȃ̂��ȖʂȂ̂�?A�Ƃ��B�����) ��
 * ���� (�Q�����Ȃ̂��R�����Ȃ̂�?A�Ƃ��B�����) ��
 * �₢?��킹��?�\�b�h��?�BĂ��܂�?B
 * GeometryElement �̃T�u�N���X��?A
 * �����̖₢?��킹�?�\�b�h��?g�̎�?��?��v����悤��
 * �I?[�o?[���C�h���Ȃ���΂Ȃ�܂���?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:15:58 $
 */
public abstract class GeometryElement extends java.lang.Object
        implements java.io.Serializable {
    /**
     * doCheck �`�F�b�N��?��t���R���X�g���N�^�Ă�?o������
     * doCheck �̃}�X�^?[�t���O?B
     * <p/>
     * ��?�� false �ɂ���?�������?}��?B
     * debug ���ɂ� true �ɂ���?B
     * </p>
     */
    static final boolean doCheckDebug = false;

    /**
     * �I�u�W�F�N�g��?\�z����?B
     */
    protected GeometryElement() {
        super();
    }

    /**
     * ���̊􉽗v�f�̎�����Ԃ���?ۃ?�\�b�h?B
     *
     * @return �􉽗v�f�̎���?�
     */

    public abstract int dimension();

    /**
     * ���̊􉽗v�f���P�������ۂ���Ԃ�?B
     *
     * @return �P�����Ȃ� <code>true</code>, ����Ȃ��� <code>false<code>
     */
    public boolean is1D() {
        return dimension() == 1;
    }

    /**
     * ���̊􉽗v�f���Q�������ۂ���Ԃ�?B
     *
     * @return �Q�����Ȃ� <code>true</code>, ����Ȃ��� <code>false<code>
     */
    public boolean is2D() {
        return dimension() == 2;
    }

    /**
     * ���̊􉽗v�f���R�������ۂ���Ԃ�?B
     *
     * @return �R�����Ȃ� <code>true</code>, ����Ȃ��� <code>false<code>
     */
    public boolean is3D() {
        return dimension() == 3;
    }

    /**
     * ���̊􉽗v�f���x�N�g�����ۂ���Ԃ�?B
     *
     * @return �x�N�g���Ȃ� <code>true</code>, ����Ȃ��� <code>false<code>
     * @see Vector
     */
    public boolean isVector() {
        // return this instanceof Vector;
        return false;
    }

    /**
     * ���̊􉽗v�f���_���ۂ���Ԃ�?B
     *
     * @return �_�Ȃ� <code>true</code>, ����Ȃ��� <code>false<code>
     * @see AbstractPoint
     */
    public boolean isPoint() {
        // return this instanceof AbstractPoint;
        return false;
    }

    /**
     * ���̊􉽗v�f���z�u?�� (��?�?W�n) ���ۂ���Ԃ�?B
     *
     * @return �z�u?�� (��?�?W�n) �Ȃ� <code>true</code>, ����Ȃ��� <code>false<code>
     * @see AbstractPlacement
     */
    public boolean isPlacement() {
        // return this instanceof AbstractPlacement;
        return false;
    }

    /**
     * ���̊􉽗v�f���􉽓I�ȕϊ���?s�Ȃ����Z�q���ۂ���Ԃ�?B
     *
     * @return �􉽓I�ȕϊ���?s�Ȃ����Z�q�Ȃ� <code>true</code>,
     *         ����Ȃ��� <code>false<code>
     * @see AbstractCartesianTransformationOperator
     */
    public boolean isTransformationOperator() {
        // return this instanceof AbstractCartesianTransformationOperator;
        return false;
    }

    /**
     * ���̊􉽗v�f����?�ۂ���Ԃ�?B
     *
     * @return ��?�Ȃ� <code>true</code>, ����Ȃ��� <code>false<code>
     * @see AbstractParametricCurve
     */
    public boolean isCurve() {
        // return this instanceof AbstractParametricCurve;
        return false;
    }

    /**
     * ���̊􉽗v�f���Ȗʂ��ۂ���Ԃ�?B
     *
     * @return �ȖʂȂ� <code>true</code>, ����Ȃ��� <code>false<code>
     * @see AbstractParametricSurface
     * @see AbstractNonParametricSurface
     */
    public boolean isSurface() {
        // return this instanceof AbstractParametricSurface
        //    || this instanceof AbstractNonParametricSurface;
        return false;
    }

    /**
     * ���̊􉽗v�f���p���?�g���b�N���ۂ���Ԃ�?B
     *
     * @return �p���?�g���b�N�Ȃ� <code>true</code>,
     *         ����Ȃ��� <code>false<code>
     * @see AbstractParametricCurve
     * @see AbstractParametricSurface
     */
    public boolean isParametric() {
        // return this instanceof AbstractParametricCurve
        //    || this instanceof AbstractParametricSurface;
        return false;
    }

    /**
     * ���̊􉽗v�f�����R�`?󂩔ۂ���Ԃ�?B
     *
     * @return ���R�`?�Ȃ� <code>true</code>,
     *         ����Ȃ��� <code>false<code>
     */
    public boolean isFreeform() {
        return false;
    }

    /**
     * ��?݂̃X���b�h��?ݒ肳��Ă��鋗���̋��e��?���Ԃ�?B
     *
     * @return ��?݂̃X���b�h��?ݒ肳��Ă��鋗���̋��e��?�
     * @see ConditionOfOperation
     */
    public double getToleranceForDistance() {
        ConditionOfOperation cond =
                ConditionOfOperation.getCondition();
        return cond.getToleranceForDistance();
    }

    /**
     * ��?݂̃X���b�h��?ݒ肳��Ă��鋗���̋��e��?���Ԃ�?B
     *
     * @return ��?݂̃X���b�h��?ݒ肳��Ă��鋗���̋��e��?�
     * @see ConditionOfOperation
     */
    public ToleranceForDistance getToleranceForDistanceAsObject() {
        ConditionOfOperation cond =
                ConditionOfOperation.getCondition();
        return cond.getToleranceForDistanceAsObject();
    }

    /**
     * ��?݂̃X���b�h��?ݒ肳��Ă��鋗���̋��e��?��̎�?��Ԃ�?B
     *
     * @return ��?݂̃X���b�h��?ݒ肳��Ă��鋗���̋��e��?��̎�?�
     * @see ConditionOfOperation
     */
    public double getToleranceForDistance2() {
        ConditionOfOperation cond =
                ConditionOfOperation.getCondition();
        return cond.getToleranceForDistance2();
    }

    /**
     * ��?݂̃X���b�h��?ݒ肳��Ă���p�x�̋��e��?���Ԃ�?B
     *
     * @return ��?݂̃X���b�h��?ݒ肳��Ă���p�x�̋��e��?�
     * @see ConditionOfOperation
     */
    public double getToleranceForAngle() {
        ConditionOfOperation cond =
                ConditionOfOperation.getCondition();
        return cond.getToleranceForAngle();
    }

    /**
     * ��?݂̃X���b�h��?ݒ肳��Ă���p�x�̋��e��?���Ԃ�?B
     *
     * @return ��?݂̃X���b�h��?ݒ肳��Ă���p�x�̋��e��?�
     * @see ConditionOfOperation
     */
    public ToleranceForAngle getToleranceForAngleAsObject() {
        ConditionOfOperation cond =
                ConditionOfOperation.getCondition();
        return cond.getToleranceForAngleAsObject();
    }

    /**
     * ��?݂̃X���b�h��?ݒ肳��Ă���p���??[�^�l�̋��e��?���Ԃ�?B
     *
     * @return ��?݂̃X���b�h��?ݒ肳��Ă���p���??[�^�l�̋��e��?�
     * @see ConditionOfOperation
     */
    public double getToleranceForParameter() {
        ConditionOfOperation cond =
                ConditionOfOperation.getCondition();
        return cond.getToleranceForParameter();
    }

    /**
     * ��?݂̃X���b�h��?ݒ肳��Ă���p���??[�^�l�̋��e��?���Ԃ�?B
     *
     * @return ��?݂̃X���b�h��?ݒ肳��Ă���p���??[�^�l�̋��e��?�
     * @see ConditionOfOperation
     */
    public ToleranceForParameter getToleranceForParameterAsObject() {
        ConditionOfOperation cond =
                ConditionOfOperation.getCondition();
        return cond.getToleranceForParameterAsObject();
    }

    /**
     * ��?݂̃X���b�h��?ݒ肳��Ă����?��l�̋��e��?���Ԃ�?B
     *
     * @return ��?݂̃X���b�h��?ݒ肳��Ă����?��l�̋��e��?�
     * @see ConditionOfOperation
     */
    public double getToleranceForRealNumber() {
        ConditionOfOperation cond =
                ConditionOfOperation.getCondition();
        return cond.getToleranceForRealNumber();
    }

    /**
     * ��?݂̃X���b�h��?ݒ肳��Ă����?��l�̋��e��?���Ԃ�?B
     *
     * @return ��?݂̃X���b�h��?ݒ肳��Ă����?��l�̋��e��?�
     * @see ConditionOfOperation
     */
    public Tolerance getToleranceForRealNumberAsObject() {
        ConditionOfOperation cond =
                ConditionOfOperation.getCondition();
        return cond.getToleranceForRealNumberAsObject();
    }

    /**
     * ?o�̓X�g��?[���ɂ��̊􉽗v�f�̌`?�?���?o�͂���?B
     *
     * @param out ?o�̓X�g��?[��
     */
    public void output(OutputStream out) {
        PrintWriter writer = new PrintWriter(out, true);
        output(writer, 0);
    }

    /**
     * ?o�̓X�g��?[���ɂ��̊􉽗v�f�̌`?�?���?o�͂���?B
     * <p/>
     * ��?ۃ?�\�b�h�Ȃ̂�?A�e�T�u�N���X�ŃI?[�o���C�h����
     * </p>
     *
     * @param writer PrintWriter
     * @param indent �C���f���g��?[��
     */
    protected abstract void output(PrintWriter writer, int indent);

    /**
     * �C���f���g��?[����?�����Tab�������Ԃ�?B
     *
     * @param indent �C���f���g��?[��
     * @return Tab������
     */
    protected String makeIndent(int indent) {
        StringBuffer buffer;
        String indent_tab;

        buffer = new StringBuffer();
        // make string of tabs
        for (int i = 0; i < indent; i++) {
            buffer.append("\t");
        }
        return new String(buffer);
    }

    /**
     * ���̊􉽗v�f��?u�p�b�P?[�W����?������N���X��?v��Ԃ�?B
     *
     * @return �p�b�P?[�W����?������N���X��
     */
    protected String getClassName() {
        StringTokenizer tokens =
                new StringTokenizer(getClass().getName(), ".");
        // get last of tokens
        while (tokens.countTokens() != 1) {
            tokens.nextToken();
        }
        return tokens.nextToken();
    }
}
