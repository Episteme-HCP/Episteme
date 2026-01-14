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

package org.jscience.medicine.volumetric;

import javax.vecmath.Point3d;
import javax.vecmath.Point3f;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class CoordAttr extends Attr {
    /** DOCUMENT ME! */
    Point3d value = new Point3d();

    /** DOCUMENT ME! */
    Point3d initValue = new Point3d();

/**
     * Creates a new CoordAttr object.
     *
     * @param label     DOCUMENT ME!
     * @param initValue DOCUMENT ME!
     */
    CoordAttr(String label, Point3d initValue) {
        super(label);
        this.initValue.set(initValue);
        this.value.set(initValue);
    }

    /**
     * DOCUMENT ME!
     *
     * @param stringValue DOCUMENT ME!
     */
    public void set(String stringValue) {
        int index;
        String doubleString;
        index = stringValue.indexOf('(');
        stringValue = stringValue.substring(index + 1);

        index = stringValue.indexOf(',');
        doubleString = stringValue.substring(0, index);

        double x = Double.valueOf(doubleString).doubleValue();

        stringValue = stringValue.substring(index + 1);
        index = stringValue.indexOf(',');
        doubleString = stringValue.substring(0, index);

        double y = Double.valueOf(doubleString).doubleValue();

        stringValue = stringValue.substring(index + 1);
        index = stringValue.indexOf(')');
        doubleString = stringValue.substring(0, index);

        double z = Double.valueOf(doubleString).doubleValue();

        set(new Point3d(x, y, z));
    }

    /**
     * DOCUMENT ME!
     *
     * @param newValue DOCUMENT ME!
     */
    public void set(Point3d newValue) {
        value.set(newValue);
    }

    /**
     * DOCUMENT ME!
     *
     * @param newValue DOCUMENT ME!
     */
    public void set(Point3f newValue) {
        value.set(newValue);
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        value.set(initValue);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return name + " (" + numFormat.format(value.x) + ", " +
        numFormat.format(value.y) + ", " + numFormat.format(value.z) + ")";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Point3d getValue() {
        return new Point3d(value);
    }
}
