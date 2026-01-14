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

/**
 * �Q���� : ��Ȑ�Ԃ̊���\���C���^�[�t�F�C�X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:09 $
 */

public interface CurveCurveInterference2D extends Interference2D {
    /**
     * ���̊����I�[�o�[���b�v�ł��邩�ۂ���Ԃ��B
     *
     * @return �I�[�o�[���b�v�ł���� true�A�����łȂ���� false
     */
    public boolean isOverlapCurve();

    /**
     * ���̊���I�[�o�[���b�v�ɕϊ�����B
     * <p/>
     * �I�[�o�[���b�v�ɕϊ��ł��Ȃ��ꍇ�� null ��Ԃ��B
     * </p>
     *
     * @return �I�[�o�[���b�v
     */
    public OverlapCurve2D toOverlapCurve();

    /**
     * ���̊��̈��̋Ȑ� (�Ȑ�1) ��ł̈ʒu��A
     * �^����ꂽ�ϊ������ɂ�Bĕϊ�������̂ɒu������������Ԃ��B
     *
     * @param sec  �Ȑ�1 �̃p�����[�^���
     * @param conv �Ȑ�1 �̃p�����[�^�l��ϊ�����I�u�W�F�N�g
     * @return �Ȑ�1 ��̈ʒu��^����ꂽ�ϊ������ɂ�Bĕϊ�������̂ɒu������������
     */
    public CurveCurveInterference2D trim1(ParameterSection sec,
                                          ParameterConversion2D conv);

    /**
     * ���̊��̑���̋Ȑ� (�Ȑ�2) ��ł̈ʒu��A
     * �^����ꂽ�ϊ������ɂ�Bĕϊ�������̂ɒu������������Ԃ��B
     *
     * @param sec  �Ȑ�2 �̃p�����[�^���
     * @param conv �Ȑ�2 �̃p�����[�^�l��ϊ�����I�u�W�F�N�g
     * @return �Ȑ�2 ��̈ʒu��^����ꂽ�ϊ������ɂ�Bĕϊ�������̂ɒu������������
     */
    public CurveCurveInterference2D trim2(ParameterSection sec,
                                          ParameterConversion2D conv);

    /**
     * ���̊��̈��̋Ȑ� (�Ȑ�1) ��^����ꂽ�Ȑ�ɒu������������Ԃ��B
     * <p/>
     * �p�����[�^�l�Ȃǂ͂��̂܂܁B
     * </p>
     *
     * @param newCurve �Ȑ�1 �ɐݒ肷��Ȑ�
     * @return �Ȑ�1��u������������
     */
    public CurveCurveInterference2D changeCurve1(ParametricCurve2D newCurve);

    /**
     * ���̊��̑���̋Ȑ� (�Ȑ�2) ��^����ꂽ�Ȑ�ɒu������������Ԃ��B
     * <p/>
     * �p�����[�^�l�Ȃǂ͂��̂܂܁B
     * </p>
     *
     * @param newCurve �Ȑ�2 �ɐݒ肷��Ȑ�
     * @return �Ȑ�2 ��u������������
     */
    public CurveCurveInterference2D changeCurve2(ParametricCurve2D newCurve);
}
