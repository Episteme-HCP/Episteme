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
//author: Doug Gehringer
//email:Doug.Gehringer@sun.com
import java.text.NumberFormat;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public abstract class Attr {
    /** DOCUMENT ME! */
    static protected NumberFormat numFormat;

    static {
        numFormat = NumberFormat.getInstance();
        numFormat.setMaximumFractionDigits(5);
    }

    /** DOCUMENT ME! */
    String label;

    /** DOCUMENT ME! */
    String name;

    /** DOCUMENT ME! */
    AttrComponent component = null;

/**
     * Creates a new Attr object.
     *
     * @param label DOCUMENT ME!
     */
    Attr(String label) {
        this.label = label;
        this.name = toName(label);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLabel() {
        return label;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param component DOCUMENT ME!
     */
    void setComponent(AttrComponent component) {
        this.component = component;
    }

    /**
     * DOCUMENT ME!
     */
    void updateComponent() {
        if (component != null) {
            component.update();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param label DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static String toName(String label) {
        return label.replace(' ', '_');
    }

    /**
     * DOCUMENT ME!
     */
    abstract void reset();

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    abstract void set(String value);
}
