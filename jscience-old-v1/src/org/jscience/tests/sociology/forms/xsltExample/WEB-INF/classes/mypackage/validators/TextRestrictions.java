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

package mypackage.validators;

import java.text.StringCharacterIterator;


/**
 * This class is a utility class for jspBeanExample and xsltExample
 * applications but it might be usefull to you too. The validation logic
 * implemented by this class does not need server side communication. This
 * means you can do the same job with some JavaScript programming on client
 * side.
 */
public class TextRestrictions {
    /**
     * DOCUMENT ME!
     */
    private boolean noSpace;

    /**
     * DOCUMENT ME!
     */
    private boolean numbersOnly;

    /**
     * DOCUMENT ME!
     */
    private int charAtLeast = 0;

    /**
     * DOCUMENT ME!
     */
    private int charMax = 200;

    /**
     * DOCUMENT ME!
     */
    private char[] prohibitedChars;

    /**
     * DOCUMENT ME!
     *
     * @param numbersOnly DOCUMENT ME!
     */
    public void setNumbersOnly(boolean numbersOnly) {
        this.numbersOnly = numbersOnly;
    }

    /**
     * DOCUMENT ME!
     *
     * @param noSpace DOCUMENT ME!
     */
    public void setNoSpace(boolean noSpace) {
        this.noSpace = noSpace;
    }

    /**
     * DOCUMENT ME!
     *
     * @param charAtLeast DOCUMENT ME!
     */
    public void setCharAtLeast(int charAtLeast) {
        this.charAtLeast = charAtLeast;
    }

    /**
     * DOCUMENT ME!
     *
     * @param charMax DOCUMENT ME!
     */
    public void setCharMax(int charMax) {
        this.charMax = charMax;
    }

    /**
     * DOCUMENT ME!
     *
     * @param prohibitedChars DOCUMENT ME!
     */
    public void setProhibitedChars(char[] prohibitedChars) {
        this.prohibitedChars = prohibitedChars;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getErrorMessage(String value) {
        if (numbersOnly) {
            StringCharacterIterator iterat = new StringCharacterIterator(value);

            for (char c = iterat.first(); c != StringCharacterIterator.DONE;
                    c = iterat.next()) {
                if (c == ' ') {
                    continue;
                }

                if (!Character.isDigit(c)) {
                    return " only numbers";
                }
            }
        }

        if (noSpace) {
            StringCharacterIterator iterat = new StringCharacterIterator(value);

            for (char c = iterat.first(); c != StringCharacterIterator.DONE;
                    c = iterat.next()) {
                if (c == ' ') {
                    return " space prohibited";
                }
            }
        }

        if (value.length() < charAtLeast) {
            return " " + charAtLeast + " characters at least";
        }

        if (value.length() > charMax) {
            return " " + charMax + " characters at most";
        }

        if ((prohibitedChars != null) && (prohibitedChars.length > 0)) {
            StringCharacterIterator iterat = new StringCharacterIterator(value);

            for (char c = iterat.first(); c != StringCharacterIterator.DONE;
                    c = iterat.next()) {
                for (int k = 0; k < prohibitedChars.length; k++) {
                    if (c == prohibitedChars[k]) {
                        return " (" + prohibitedChars[k] + ") prohibited";
                    }
                }
            }
        }

        return "";
    }
}
