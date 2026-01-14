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
 * �R���� : ��?�Ԃ̃p���??[�^�ϊ�?��?��\����?ۃN���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * �����?� P �ɑ΂���p���??[�^�l s ��?A
 * P(s) = Q(t) �𖞂����悤��
 * �ʂ̋�?� Q �ɑ΂���p���??[�^�l t �ɕϊ�����?��?��?s�Ȃ��?�\�b�h����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:16 $
 */
abstract class ParameterConversion3D {
    /**
     * P �ɑ΂���p���??[�^�l�� Q �ɑ΂���p���??[�^�l�ɕϊ����钊?ۃ?�\�b�h?B
     *
     * @param param P �ɑ΂���p���??[�^�l
     * @return Q �ɑ΂���p���??[�^�l
     */
    abstract double convParameter(double param);

    /**
     * P �ɑ΂���p���??[�^�l��ϊ������?ۂł��� Q ��Ԃ���?ۃ?�\�b�h?B
     *
     * @param param P �ɑ΂���p���??[�^�l
     * @return ��?� Q
     */
    abstract ParametricCurve3D convCurve(double param);

    /**
     * P �ɑ΂���p���??[�^��Ԃ� Q �ɑ΂���p���??[�^��Ԃɕϊ�����?B
     *
     * @param sec P �ɑ΂���p���??[�^���
     * @return Q �ɑ΂���p���??[�^���
     * @see #convParameter(double)
     * @see #convCurve(double)
     */
    ParameterSection convParameter(ParameterSection sec) {
        ParametricCurve3D scurve = convCurve(sec.start());
        ParametricCurve3D ecurve = convCurve(sec.end());

        if (scurve != ecurve)
            return null;

        double sparam = convParameter(sec.start());
        double eparam = convParameter(sec.end());
        return new ParameterSection(sparam, eparam - sparam);
    }

    /**
     * P �ɑ΂���p���??[�^�l�ɑΉ����� Q ��?�?�_ Q(t) ��Ԃ�?B
     *
     * @param param P �ɑ΂���p���??[�^�l
     * @return �Ή����� Q ��?�?�_ Q(t)
     * @see #convParameter(double)
     * @see #convCurve(double)
     */
    PointOnCurve3D convToPoint(double param) {
        double tparam = convParameter(param);
        ParametricCurve3D curve = convCurve(param);

        return new PointOnCurve3D(curve, tparam, GeometryElement.doCheckDebug);
    }

    /**
     * P �ɑ΂���p���??[�^��ԂɑΉ����� Q �̃g������?��Ԃ�?B
     *
     * @param sec P �ɑ΂���p���??[�^���
     * @return �Ή����� Q �̃g������?�
     * @see #convParameter(double)
     * @see #convCurve(double)
     */
    TrimmedCurve3D convToTrimmedCurve(ParameterSection sec) {
        ParametricCurve3D scurve = convCurve(sec.start());
        ParametricCurve3D ecurve = convCurve(sec.end());

        if (scurve != ecurve)
            return null;

        double sparam = convParameter(sec.start());
        double eparam = convParameter(sec.end());
        return new TrimmedCurve3D(ecurve,
                new ParameterSection(sparam, eparam - sparam));
    }
}
