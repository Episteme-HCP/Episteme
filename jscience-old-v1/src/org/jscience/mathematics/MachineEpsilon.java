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

package org.jscience.mathematics;

/**
 * �v�Z�@ (Java Virtual Machine) �̕���?�?��_���Z�̊ۂߌ�?���ێ?����N���X?B
 * <p/>
 * ((1 + a) &gt; 1) ���U�ƂȂ�?�?���?��l a ��\��?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2007-10-21 17:38:57 $
 */
public class MachineEpsilon extends java.lang.Object {
    /**
     * �P?��x�̊ۂߌ�?�?B
     */
    public static final float SINGLE = determineFloatValue();

    /**
     * �{?��x�̊ۂߌ�?�?B
     */
    public static final double DOUBLE = determineDoubleValue();

    /**
     * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
     */
    private MachineEpsilon() {
    }

    /**
     * �P?��x�̊ۂߌ�?���v�Z����?B
     */
    private static float determineFloatValue() {
        float half = (float) 0.5;
        float one = (float) 1.0;
        float two = (float) 2.0;
        float meps = one;

        for (; ; meps *= half) {
            float work = one + meps;
            if (work <= one)
                break;
        }

        return (meps * two);
    }

    /**
     * �{?��x�̊ۂߌ�?���v�Z����?B
     */
    private static double determineDoubleValue() {
        double half = 0.5;
        double one = 1.0;
        double two = 2.0;
        double meps = one;

        for (; ; meps *= half) {
            double work = one + meps;
            if (work <= one)
                break;
        }

        return (meps * two);
    }

    /* Debug
    public static void main(String argv[]) {
        System.out.println(SINGLE);
    System.out.println(DOUBLE);
    }
    */
}

/* end of file */
