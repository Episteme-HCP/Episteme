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

package org.jscience.chemistry.gui.extended.geometry;

/**
 * A class that provides mathematical definition of geometric angle
 *
 * @author Zhidong Xie (zxie@tripos.com)
 *         Original Version
 * @date 8/14/97
 */
public class Angle {
    /**
     * value of the angle
     */
    protected double value = 0.0;

    /**
     * error/warning message, or other comment
     */
    protected String comment = null;

    /**
     * default constructor: value is 0.0;
     */
    public Angle() {
    }

    /**
     * full constructor
     *
     * @param angle
     * @param inDegree boolean flag indicating value in degree unit
     */
    public Angle(double value, boolean inDegree) {
        this.value = (inDegree) ? (value / 180.0 * Math.PI) : value;
    }

    /**
     * constructor
     *
     * @param angle value in rad unit
     */
    public Angle(double value) {
        this(value, false);
    }

    /**
     * copy constructor
     *
     * @param a the angle to be copied
     */
    public Angle(Angle angle) {
        value = angle.value;
    }

    /**
     * Return angle value in rad unit
     */
    public double radValue() {
        return value;
    }

    /**
     * Return angle value in degree unit
     */
    public double degreeValue() {
        return ((180.0 * value) / Math.PI);
    }

    /**
     * Return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Return string representation of angle
     */
    public String toString() {
        return Float.toString((float) degreeValue());
    }

    /**
     * Set value of the angle
     *
     * @param value    angle's value to be set
     * @param inDegree true if value is in degree unit, false if in rad unit
     */
    public void setValue(double value, boolean inDegree) {
        this.value = (inDegree) ? (value / 180.0 * Math.PI) : value;
    }

    /**
     * Set value of the angle
     *
     * @param value angle's value to be set, in rad unit
     */
    public void setValue(double value) {
        this.setValue(value, false);
    }

    /**
     * Set comment
     *
     * @param comment
     * @see getComment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
} // end of Angle class
