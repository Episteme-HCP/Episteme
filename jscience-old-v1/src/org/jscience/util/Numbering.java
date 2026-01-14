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

package org.jscience.util;

/**
 * A class representing a number like the one found in law articles, or in
 * software version numbers
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Numbering extends Object implements Comparable {
    /** DOCUMENT ME! */
    private int[] value; //for example 1.0.13.4

    //array must be of length one or more and have only positive values
/**
     * Creates a new Numbering object.
     *
     * @param value DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Numbering(int[] value) {
        int i;
        boolean valid;

        if ((value != null) && (value.length > 0)) {
            i = 0;
            valid = true;

            while ((i < value.length) && valid) {
                valid = value[i] >= 0;
                i++;
            }

            if (valid) {
                this.value = value;
            } else {
                throw new IllegalArgumentException(
                    "A numbering must contain only positive elements in the array of constructor.");
            }
        } else {
            throw new IllegalArgumentException(
                "A numbering must have at least one element in the array of constructor.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getValue() {
        return value;
    }

    //returns the String corresponding to this value
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String valueOf() {
        String result;

        result = new String();

        for (int i = 0; i < value.length; i++) {
            result.concat(value[i] + ".");
        }

        return result.substring(0, result.length());
    }

    //will remove the trailing value (if more than 1 value)
    /**
     * DOCUMENT ME!
     */
    public void removeMinor() {
        int[] copy;

        if (value.length > 1) {
            copy = new int[value.length - 1];
            System.arraycopy(value, 0, copy, 0, value.length - 1);
            value = copy;
        }
    }

    //adds a trailing 0
    /**
     * DOCUMENT ME!
     */
    public void addMinor() {
        int[] copy;

        copy = new int[value.length + 1];
        System.arraycopy(value, 0, copy, 0, value.length);
        copy[value.length] = 0;
        value = copy;
    }

    //for example for 1.2.3.4 will return 1.2.3.5
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Numbering getNext() {
        int[] copy;

        copy = new int[value.length];
        System.arraycopy(value, 0, copy, 0, value.length);
        copy[value.length - 1]++;

        return new Numbering(copy);
    }

    //for example 1.2.3 equals 1.2.3.0
    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Numbering value) {
        int i;
        boolean result;

        i = 0;
        result = true;

        if (this.value.length < value.getValue().length) {
            while ((i < this.value.length) && result) {
                result = (this.value[i] == value.getValue()[i]);
                i++;
            }

            if (result) {
                while ((i < value.getValue().length) && result) {
                    result = (value.getValue()[i] == 0);
                    i++;
                }
            }
        } else {
            while ((i < value.getValue().length) && result) {
                result = (this.value[i] == value.getValue()[i]);
                i++;
            }

            if (result) {
                while ((i < this.value.length) && result) {
                    result = (this.value[i] == 0);
                    i++;
                }
            }
        }

        return result;
    }

    //for example 1.2.3 < 1.3.0
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ClassCastException DOCUMENT ME!
     */
    public int compareTo(Object o) {
        int i;
        int result;

        if (o instanceof Numbering) {
            i = 0;
            result = 0;

            if (this.value.length < value.length) {
                while ((i < this.value.length) && (result == 0)) {
                    result = sign(value[i] - this.value[i]);
                    i++;
                }

                if (result == 0) {
                    while ((i < value.length) && (result == 0)) {
                        result = sign(value[i]);
                        i++;
                    }
                }
            } else {
                while ((i < value.length) && (result == 0)) {
                    result = sign(value[i] - this.value[i]);
                    i++;
                }

                if (result == 0) {
                    while ((i < this.value.length) && (result == 0)) {
                        result = -sign(this.value[i]);
                        i++;
                    }
                }
            }

            return result;
        } else {
            throw new ClassCastException(
                "Can't compare a Numbering to a non-Numbering object.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int sign(int a) {
        if (a > 0) {
            return 1;
        } else {
            if (a < 0) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
