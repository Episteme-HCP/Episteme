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
 * �g���~���O�ʒu�̊�?���\����?���ێ?����N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:16:12 $
 */

public class TrimmingPreference extends Types {
    /**
     * ��Ԃł̓_���?��Ƃ��邱�Ƃ���?�?B
     */
    public final static int POINT = 0;

    /**
     * �p���??[�^�l���?��Ƃ��邱�Ƃ���?�?B
     */
    public final static int PARAMETER = 1;

    /**
     * �BɊ�?��̂Ȃ����Ƃ���?�?B
     */
    public final static int UNSPECIFIED = 2;

    /**
     * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
     */
    private TrimmingPreference() {
    }

    /**
     * ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l��t�B?[���h���ɕϊ�����?B
     * <p/>
     * �^����ꂽ�l�ɑΉ�����t�B?[���h����?݂��Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O�𓊂���?B
     * </p>
     *
     * @param masterRepresentation ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l
     * @return �Ή�����t�B?[���h��
     * @see InvalidArgumentValueException
     */
    public static String toString(int masterRepresentation) {
        switch (masterRepresentation) {
            case POINT:
                return "POINT";
            case PARAMETER:
                return "PARAMETER";
            case UNSPECIFIED:
                return "UNSPECIFIED";
            default:
                throw new InvalidArgumentValueException();
        }
    }
}


