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
 * �􉽗v�f�̂ǂ��瑤����\����?���ێ?����N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:21 $
 */

public class WhichSide extends Types {
    /**
     * ��������?�
     */
    public static final int BOTH = -1;

    /**
     * �v�f?����?�
     */
    public static final int ON = 0;

    /**
     * �E������?�
     */
    public static final int RIGHT = 1;

    /**
     * ?�������?�
     */
    public static final int LEFT = 2;

    /**
     * �O������?�
     */
    public static final int FRONT = 3;

    /**
     * ��둤����?�
     */
    public static final int BACK = 4;

    /**
     * �Ѥ����?�
     */
    public static final int IN = 5;

    /**
     * �O������?�
     */
    public static final int OUT = 6;

    /**
     * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
     */
    private WhichSide() {
    }

    /**
     * ��?��l�ŗ^����ꂽ?u��?v�Ƃ͋t�̑�����?���Ԃ�?B
     * <p/>
     * �^����ꂽ�l�ɑΉ�����?u��?v?A�µ���͂��̋t�̑�����?݂��Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O�𓊂���?B
     * </p>
     * <p/>
     * BOTH ����� ON �ɂ͋t�̑�����?݂��Ȃ�?B
     * </p>
     *
     * @param value ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l
     * @return �t�̑�����?�
     * @see InvalidArgumentValueException
     */
    public static int reverse(int value) {
        switch (value) {
            case RIGHT:
                return LEFT;
            case LEFT:
                return RIGHT;
            case FRONT:
                return BACK;
            case BACK:
                return FRONT;
            case IN:
                return OUT;
            case OUT:
                return IN;
        }
        throw new InvalidArgumentValueException();
    }

    /**
     * ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l��t�B?[���h���ɕϊ�����?B
     * <p/>
     * �^����ꂽ�l�ɑΉ�����t�B?[���h����?݂��Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O�𓊂���?B
     * </p>
     *
     * @param value ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l
     * @return �Ή�����t�B?[���h��
     * @see InvalidArgumentValueException
     */
    public static String toString(int value) {
        switch (value) {
            case BOTH:
                return "BOTH";
            case ON:
                return "ON";
            case RIGHT:
                return "RIGHT";
            case LEFT:
                return "LEFT";
            case FRONT:
                return "FRONT";
            case BACK:
                return "BACK";
            case IN:
                return "IN";
            case OUT:
                return "OUT";
            default:
                throw new InvalidArgumentValueException();
        }
    }
}

