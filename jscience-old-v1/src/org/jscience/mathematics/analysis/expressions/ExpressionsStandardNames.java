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

package org.jscience.mathematics.analysis.expressions;

/**
 * Abstract class used for supplying some standard names.
 *
 * @author Martin Egholm Nielsen
 * @version 1.0
 */
public abstract class ExpressionsStandardNames {
    /** String object holding the standard base name for a state variable. */
    public static final String STATE_BASE_NAME = "x";

    /** String object holding the standard base name for a parameter. */
    public static final String PARAMETER_BASE_NAME = "p";

    /** String object holding the standard base name for a auxiliary. */
    public static final String AUXILIARY_BASE_NAME = "aux";

    /** String object holding the standard name for the time. */
    public static final String TIME_CONTINUOUS_NAME = "t";

    /** String object holding the standard name for the "time". */
    public static final String DISCRETE_NAME = "t";

    /** String holding the abbreviation for positive infinity. */
    public static final String POSITIVE_INFINITY = "inf";

    /** String holding the abbreviation for negative infinity. */
    public static final String NEGATIVE_INFINITY = "-inf";

    /** String object holding the standard title for a settings frame. */
    public static final String SETTINGS_TITLE = "Settings menu for ";

    /**
     * Return a String object holding the standard name of the
     * <i>i</i>th state variable.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String nameOfState(int i) {
        return STATE_BASE_NAME + i;
    } // nameOfState

    /**
     * Return a String object holding the standard name of the
     * <i>i</i>th parameter.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String nameOfParameter(int i) {
        return PARAMETER_BASE_NAME + i;
    } // nameOfParameter

    /**
     * Return a String object holding the standard name of the
     * <i>i</i>th auxiliary.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String nameOfAuxiliary(int i) {
        return AUXILIARY_BASE_NAME + i;
    } // nameOfAuxiliary

    //this part of Janet was never merged in.
/**
     * Returns the appropriate String object representing the time for a given
     * {@link org.jscience.mathematics.analysis.Map} object. If the
     * <code>map</code> is {@link
     * org.jscience.mathematics.analysis.TimeDiscrete} the {@link
     * #DISCRETE_NAME} is returned, otherwise {@link #TIME_CONTINUOUS_NAME} is
     * returned.
     */

    //public static String nameOfTime(Map map) {
    //    if (map instanceof TimeDiscrete) {
    //        return DISCRETE_NAME;
    //    } else {
    //        return TIME_CONTINUOUS_NAME;
    //    }
    //} // nameOfTime
} // StandardNames
