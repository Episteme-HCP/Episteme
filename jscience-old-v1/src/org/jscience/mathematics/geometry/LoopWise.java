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
 * ��?[�v���ǂ�����ł��邩��\����?���ێ?����N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:14 $
 */

public class LoopWise extends Types {
    /**
     * CCW:	�����v��� (?����) �ł��邱�Ƃ���?�
     */
    public static final int CCW = 0;

    /**
     * CW:	���v��� (�E���) �ł��邱�Ƃ���?�
     */
    public static final int CW = 1;

    /**
     * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
     */
    private LoopWise() {
    }

    /**
     * ��?��l�ŗ^����ꂽ����Ƃ͋t�̉������?���Ԃ�?B
     * <p/>
     * �^����ꂽ�l�ɑΉ��������?݂��Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O�𓊂���?B
     * </p>
     *
     * @param value ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l
     * @return �t�̉������?�
     * @see InvalidArgumentValueException
     */
    public static int reverse(int value) {
        switch (value) {
            case CCW:
                return CW;
            case CW:
                return CCW;
            default:
                throw new InvalidArgumentValueException();
        }
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
            case CCW:
                return "CCW";
            case CW:
                return "CW";
            default:
                throw new InvalidArgumentValueException();
        }
    }

}

