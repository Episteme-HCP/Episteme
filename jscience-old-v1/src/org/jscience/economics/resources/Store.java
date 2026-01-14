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

package org.jscience.economics.resources;

import java.util.Set;


/**
 * A class representing a container.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//describes the fact that some things may be contained within this thing
//for example a fridge stores food, a vehicle stores passengers
//buildings (houses, cages) also store creatures
//this class is useful to provide some knowledge about the things that are in, how they get in and out
//stored things, once in, of course move with the thing they are into
public interface Store {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getContents();

    /**
     * DOCUMENT ME!
     */
    public void getIn();

    /**
     * DOCUMENT ME!
     */
    public void getOut();
}
