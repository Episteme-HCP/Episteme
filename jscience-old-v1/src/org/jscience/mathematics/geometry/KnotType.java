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
 * �a�X�v���C���̃m�b�g��̎�ʂ�\����?���ێ?����N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:14 $
 */

public class KnotType extends Types {
    /**
     * �S��ɓn�Bă��j�t�H?[���ȃm�b�g��ł��邱�Ƃ���?�?B
     */
    public static final int UNIFORM_KNOTS = 0;

    /**
     * �BɓR���w�肳��Ȃ���ʂ̃m�b�g��ł��邱�Ƃ���?�?B
     */
    public static final int UNSPECIFIED = 1;

    /**
     * ���[�� (��?�+1) �̑�?d�x��?�B����j�t�H?[���ȃm�b�g��ł��邱�Ƃ���?�?B
     * <p/>
     * �����_�� JGCL �ł�?A���̎�̃m�b�g���?�a�X�v���C���ɂ͖��Ή�
     * </p>
     */
    public static final int QUASI_UNIFORM_KNOTS = 2;

    /**
     * �敪�x�W�G��?�ɑΉ������m�b�g��ł��邱�Ƃ���?�
     * <p/>
     * �����_�� JGCL �ł�?A���̎�̃m�b�g���?�a�X�v���C���ɂ͖��Ή�
     * </p>
     */
    public static final int PIECEWISE_BEZIER_KNOTS = 3;

    /**
     * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
     */
    private KnotType() {
    }

    /**
     * ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l��t�B?[���h���ɕϊ�����?B
     * <p/>
     * �^����ꂽ�l�ɑΉ�����t�B?[���h����?݂��Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O�𓊂���?B
     * </p>
     *
     * @param knotSpec ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l
     * @return �Ή�����t�B?[���h��
     * @see InvalidArgumentValueException
     */
    public static String toString(int knotSpec) {
        switch (knotSpec) {
            case UNIFORM_KNOTS:
                return "UNIFORM_KNOTS";
            case UNSPECIFIED:
                return "UNSPECIFIED";
            case QUASI_UNIFORM_KNOTS:
                return "QUASI_UNIFORM_KNOTS";
            case PIECEWISE_BEZIER_KNOTS:
                return "PIECEWISE_BEZIER_KNOTS";
            default:
                throw new InvalidArgumentValueException();
        }
    }
}

