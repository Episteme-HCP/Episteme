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

package org.jscience.biology.lsystems.growing;

/**
 * This class holds methods for easier conversion of types.
 *
 * @author <a href="http://www.gressly.ch/rene/" target="_top">Rene Gressly</a>
 */
public class Converter {
    /**
     * Extracts the value from the string and converts it to an integer
     *
     * @param strNumber The the string value.
     *
     * @return The integer value from the string. zero if an error occured.
     */
    public static int readInt(String strNumber) {
        try {
            return (Integer.valueOf(strNumber).intValue());
        } catch (Exception e) {
            //Log.log("ERROR: Could not convert string '" + strNumber +"' to int. Returning zero");
            return 0;
        }
    }

    /**
     * Extracts the float value from a string.
     *
     * @param strNumber The string with the float value.
     *
     * @return The float value. zero of an error occured.
     */
    public static float readFloat(String strNumber) {
        try {
            double dValue = Float.valueOf(strNumber).floatValue();

            return (float) dValue;
        } catch (Exception e) {
            //Log.log("ERROR: Could not convert string '" + strNumber + "' to float. Returning zero");
            return 0f;
        }
    }

    /**
     * Gets the string behind a separator. This is the string from the
     * separator to the end of the line.
     *
     * @param strMain The string containing the separator.
     * @param strSub DOCUMENT ME!
     *
     * @return The string behind the separator.
     *
     * @throws Exception An exception in thrown if no separator can be found.
     */
    public static String getStringBehind(String strMain, String strSub)
        throws Exception {
        return strMain.substring(strMain.indexOf(strSub) + strSub.length())
                      .trim();
    }
}
