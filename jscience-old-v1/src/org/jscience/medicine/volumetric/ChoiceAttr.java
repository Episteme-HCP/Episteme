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


//repackaged after the code available at http://www.j3d.org/tutorials/quick_fix/volume.html
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class ChoiceAttr extends Attr {
    /** DOCUMENT ME! */
    int value;

    /** DOCUMENT ME! */
    int initValue;

    /** DOCUMENT ME! */
    String[] valueLabels;

    /** DOCUMENT ME! */
    String[] valueNames;

/**
     * Creates a new ChoiceAttr object.
     *
     * @param label           DOCUMENT ME!
     * @param initValueLabels DOCUMENT ME!
     * @param initValue       DOCUMENT ME!
     */
    ChoiceAttr(String label, String[] initValueLabels, int initValue) {
        super(label);
        this.initValue = value = initValue;
        valueLabels = initValueLabels;
        valueNames = new String[valueLabels.length];

        for (int i = 0; i < valueLabels.length; i++) {
            valueNames[i] = toName(valueLabels[i]);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return name + " " + valueNames[value];
    }

    /**
     * DOCUMENT ME!
     *
     * @param stringValue DOCUMENT ME!
     */
    public void set(String stringValue) {
        // TODO: worry about getting the next word?
        for (int i = 0; i < valueNames.length; i++) {
            if (stringValue.equals(valueNames[i])) {
                value = i;
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    void reset() {
        value = initValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getValue() {
        return value;
    }
}
