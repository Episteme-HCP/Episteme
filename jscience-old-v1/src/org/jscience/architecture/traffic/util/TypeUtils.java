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

package org.jscience.architecture.traffic.util;

/**
 * These functions can be used combine and extract primitive types to and
 * from a type-integer.
 *
 * @author Jilles V
 * @version 1.0
 */
public class TypeUtils {
    /**
     * Extracts all the primitive types (2^n) from a given type-integer
     *
     * @param type The type-integer to extract.
     *
     * @return DOCUMENT ME!
     */
    public static int[] getTypes(int type) {
        //System.out.println("To Type: "+type);
        if (type == 0) {
            return new int[0];
        }

        int log = (int) Math.floor(Math.log(type) / Math.log(2));

        //System.out.println("Log: "+log);
        int checktype = (int) Math.pow(2, log);
        int[] types = new int[log + 1];
        int c = 0;

        for (; type > 0; checktype /= 2) {
            //System.out.println("Checktype = "+checktype);
            if (checktype > type) {
                continue;
            }

            types[c] = checktype;
            type -= checktype;
            c++;
        }

        return (int[]) ArrayUtils.cropArray(types, c);
    }
}
