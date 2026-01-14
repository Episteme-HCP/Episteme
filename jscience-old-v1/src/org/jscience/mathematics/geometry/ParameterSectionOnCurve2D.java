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
 * �Q���� : ����p���?�g���b�N��?�?�̂���p���??[�^��Ԃ�\���N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * �����?��
 * ����?�̃p���??[�^���
 * ��ێ?����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:16 $
 */

public class ParameterSectionOnCurve2D implements ParameterRangeOnCurve2D {
    /**
     * ��?�Ƃ��̃p���??[�^��Ԃ�\���g������?�?B
     */
    private TrimmedCurve2D trc;

    /**
     * ��?��p���??[�^��Ԃ�^�����ɃI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^���Ă�?o����邱�Ƃ͂Ȃ�?B
     * </p>
     */
    private ParameterSectionOnCurve2D() {
        trc = null;
    }

    /**
     * �����?�Ƃ���?�̃p���??[�^��Ԃ��g������?��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * doCheck �̒l�͎Q?Ƃ���Ȃ�?B
     * </p>
     *
     * @param trc     �����?�?�̂����Ԃ�\���g������?�
     * @param doCheck ��?���`�F�b�N���邩�ǂ���
     */
    ParameterSectionOnCurve2D(TrimmedCurve2D trc,
                              boolean doCheck) {
        super();
        this.trc = trc;
    }

    /**
     * ��?�Ƃ���?�̃p���??[�^��Ԃ�^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * doCheck �̒l�͎Q?Ƃ���Ȃ�?B
     * </p>
     *
     * @param curve   ��?�
     * @param section ��?�?�̃p���??[�^���
     * @param doCheck ��?���`�F�b�N���邩�ǂ���
     */
    ParameterSectionOnCurve2D(ParametricCurve2D curve,
                              ParameterSection section,
                              boolean doCheck) {
        super();
        this.trc = new TrimmedCurve2D(curve, section);
    }

    /**
     * ��?�Ƃ���?�̃p���??[�^��Ԃ�^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * doCheck �̒l�͎Q?Ƃ���Ȃ�?B
     * </p>
     *
     * @param curve   ��?�
     * @param start   ��?�?�̃p���??[�^��Ԃ̊J�n�l
     * @param inc     ��?�?�̃p���??[�^��Ԃ̑?���l
     * @param doCheck ��?���`�F�b�N���邩�ǂ���
     */
    public ParameterSectionOnCurve2D(ParametricCurve2D curve,
                                     double start,
                                     double inc,
                                     boolean doCheck) {
        super();
        this.trc = new TrimmedCurve2D(curve, new ParameterSection(start, inc));
    }

    /**
     * ���̋�Ԃ̑�?ۂƂȂBĂ����?��Ԃ�?B
     *
     * @return ��?�
     */
    public ParametricCurve2D curve() {
        return trc.basisCurve();
    }

    /**
     * ���̃p���??[�^��Ԃ̊J�n�l��Ԃ�?B
     *
     * @return �p���??[�^��Ԃ̊J�n�l
     */
    public double start() {
        return trc.tParam1();
    }

    /**
     * ���̃p���??[�^��Ԃ�?I���l��Ԃ�?B
     *
     * @return �p���??[�^��Ԃ�?I���l
     */
    public double end() {
        return trc.tParam2();
    }

    /**
     * ���̃p���??[�^��Ԃ̑?���l��Ԃ�?B
     *
     * @return �p���??[�^��Ԃ̑?���l
     */
    public double increase() {
        return end() - start();
    }

    /**
     * ���̃p���??[�^��Ԃ�?u�_?v���ۂ���Ԃ�?B
     *
     * @return ?�� false
     */
    public boolean isPoint() {
        return false;
    }

    /**
     * ���̃p���??[�^��Ԃ�?u���?v���ۂ���Ԃ�?B
     *
     * @return ?�� true
     */
    public boolean isSection() {
        return true;
    }
}

// end of file
