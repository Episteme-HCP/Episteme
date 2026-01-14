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

package org.jscience.astronomy;

import javax.media.j3d.Group;


/**
 * The Nebula class provides support for the category of AstralBody of the
 * same name.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//describes complete information about a nebula
//this is not a solid object but a gaz cloud, nevertheless, it is treated as if a solid object as the whole gaz cloud by itself may have a huge mass
public abstract class Nebula extends AstralBody {
/**
     * Creates a new Nebula object.
     *
     * @param name DOCUMENT ME!
     */
    public Nebula(String name) {
        super(name);
    }

/**
     * Creates a new Nebula object.
     *
     * @param name DOCUMENT ME!
     * @param mass DOCUMENT ME!
     */
    public Nebula(String name, double mass) {
        super(name, mass);
    }

/**
     * Creates a new Nebula object.
     *
     * @param name  DOCUMENT ME!
     * @param shape DOCUMENT ME!
     * @param mass  DOCUMENT ME!
     */
    public Nebula(String name, Group shape, double mass) {
        super(name, shape, mass);
    }

    /**
     * DOCUMENT ME!
     */
    protected void updateShape() {
        super.updateShape();
    }
}
