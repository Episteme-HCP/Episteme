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

package org.jscience.computing.ai.util;

/**
 * Utility class for conversions
 *
 * @author Levent Bayindir
 * @version 0.1
 */
public class Converter {
    /**
     * Returns string representation of the given bit.
     *
     * @param bit boolean value of the given bit
     *
     * @return if bit is true then 1 is returned, else 0 is returned
     */
    public static String booleanToString(boolean bit) {
        if (bit) {
            return "1";
        } else {
            return "0";
        }
    }

    /**
     * Returns string representation of the given array of bits. Each
     * bit is converted using booleanToString(...) method and concatenated.
     *
     * @param booleanArray boolean array of the given bits
     *
     * @return string representation of given array of bits
     */
    public static String booleanArrayToString(boolean[] booleanArray) {
        String tmpString = new String();

        for (int i = 0; i < booleanArray.length; i++) {
            tmpString = tmpString + booleanToString(booleanArray[i]);
        }

        return tmpString;
    }

    /**
     * Returns int value of the given array of bits.
     * booleanArrayToString(...) method is called for the given array of bits,
     * then the returned value is converted to int value
     *
     * @param booleanArray boolean array of the given bits
     *
     * @return int value of the given array of bits
     */
    public static int booleanArrayToInt(boolean[] booleanArray) {
        return binaryString2Int(booleanArrayToString(booleanArray));
    }

    /**
     * Returns integer value of given bits
     *
     * @param binary string representation of given bits
     *
     * @return integer value of given string
     */
    public static int binaryString2Int(String binary) {
        return Integer.valueOf(binary, 2).intValue();
    }
}
