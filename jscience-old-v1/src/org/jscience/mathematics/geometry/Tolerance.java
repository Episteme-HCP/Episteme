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
 * ��?��l�̋��e��?���\���N���X?B
 * <p/>
 * JGCL �ł�?A
 * �􉽉��Z��?i�߂�?ۂ̋��e��?�?��?�ɂ����ĎQ?Ƃ��ׂ��e��̋��e��?��l��
 * ���Z?�? {@link ConditionOfOperation ConditionOfOperation} �Ƃ���?A
 * �܂Ƃ߂ĊǗ?����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:20 $
 * @see ConditionOfOperation
 */

public class Tolerance extends java.lang.Object {

    /**
     * ���e��?��l?B
     */
    private final double value;

    /**
     * �^����ꂽ�l�떗e��?��l�Ƃ���I�u�W�F�N�g��?\�z����?B
     * <p/>
     * value �̒l������?�?��ɂ�?A����?�Βl�떗e��?��l�Ƃ���?B
     * </p>
     * <p/>
     * value �̒l�� 0 ��?�?��ɂ�?AInvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param value ?ݒ肷�鋖�e��?��l
     * @see InvalidArgumentValueException
     */
    public Tolerance(double value) {
        if (value == 0) {
            throw new InvalidArgumentValueException();
        }
        if (value < 0) {
            value = Math.abs(value);
        }

        this.value = value;
    }

    /**
     * ���̋��e��?��̒l��Ԃ�?B
     *
     * @return ���e��?��l
     */
    public double value() {
        return value;
    }
}

