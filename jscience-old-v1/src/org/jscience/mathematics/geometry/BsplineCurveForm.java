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
 * �a�X�v���C����?�̌`?�̓R���\����?���ێ?����N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:07 $
 */

public class BsplineCurveForm extends Types {
    /**
     * �|�����C����\���P���̂a�X�v���C����?�ł��邱�Ƃ���?�
     */
    public static final int POLYLINE_FORM = 0;

    /**
     * �~�̈ꕔ/�S�̂�\���a�X�v���C����?�ł��邱�Ƃ���?�
     */
    public static final int CIRCULAR_ARC = 1;

    /**
     * �ȉ~�̈ꕔ/�S�̂�\���a�X�v���C����?�ł��邱�Ƃ���?�
     */
    public static final int ELLIPTIC_ARC = 2;

    /**
     * ��?�̈ꕔ��\���a�X�v���C����?�ł��邱�Ƃ���?�
     */
    public static final int PARABOLIC_ARC = 3;

    /**
     * �o��?�̈ꕔ��\���a�X�v���C����?�ł��邱�Ƃ���?�
     */
    public static final int HYPERBOLIC_ARC = 4;

    /**
     * �`?�̓R���BɎw�肳��Ȃ��a�X�v���C����?�ł��邱�Ƃ���?�
     */
    public static final int UNSPECIFIED = 5;

    /**
     * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
     */
    private BsplineCurveForm() {
    }

    /**
     * ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l��t�B?[���h���ɕϊ�����?B
     * <p/>
     * �^����ꂽ�l�ɑΉ�����t�B?[���h����?݂��Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O�𓊂���?B
     * </p>
     *
     * @param curveForm ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l
     * @return �Ή�����t�B?[���h��
     * @see InvalidArgumentValueException
     */
    public static String toString(int curveForm) {
        switch (curveForm) {
            case POLYLINE_FORM:
                return "POLYLINE_FORM";
            case CIRCULAR_ARC:
                return "CIRCULAR_ARC";
            case ELLIPTIC_ARC:
                return "ELLIPTIC_ARC";
            case PARABOLIC_ARC:
                return "PARABOLIC_ARC";
            case HYPERBOLIC_ARC:
                return "HYPERBOLIC_ARC";
            case UNSPECIFIED:
                return "UNSPECIFIED";
            default:
                throw new InvalidArgumentValueException();
        }
    }
}

