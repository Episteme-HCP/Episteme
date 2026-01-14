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

package org.jscience.astronomy.catalogs.hipparcos;

import java.util.Enumeration;
import java.util.Iterator;

import javax.media.j3d.TransformGroup;


//this package is rebundled from Hipparcos Java package from
//William O'Mullane
//http://astro.estec.esa.nl/Hipparcos/hipparcos_java.html
//mailto:hipparcos@astro.estec.esa.nl
/**
 * Iterate over the children of a transform group and return only stars
 */
class GroupIterator implements Iterator {
    /** DOCUMENT ME! */
    protected Object theNext;

    /** DOCUMENT ME! */
    protected Enumeration factory;

/**
     * Creates a new GroupIterator object.
     *
     * @param f DOCUMENT ME!
     */
    public GroupIterator(Enumeration f) {
        factory = f;
        preNext();
    }

    /**
     * DOCUMENT ME!
     */
    public void remove() {
    }

    /**
     * Should be straight forward but actually not that simple. Dont no
     * if there is a next until we get it !
     *
     * @return DOCUMENT ME!
     */
    public boolean hasNext() {
        return (theNext != null);
    }

    /**
     * Return theNext and getthe next object of possible. Next is
     * always running one object ahead so it can respond to hasNext().
     *
     * @return DOCUMENT ME!
     */
    public Object next() {
        if (theNext == null) {
            return null;
        }

        HipparcosStar ret = (HipparcosStar) theNext;
        preNext();

        return ret.getStar();
    }

    /**
     * Find the next object but just hold on to it.
     */
    protected void preNext() {
        try {
            boolean keepgoing = true;

            while (keepgoing) {
                theNext = factory.nextElement();

                if (theNext instanceof TransformGroup) {
                    theNext = ((TransformGroup) theNext).getChild(0);
                    keepgoing = !(theNext instanceof HipparcosStar);
                }
            }
        } catch (Exception e) {
            theNext = null;
        }
    }
}
