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

package org.jscience.architecture.lift;

/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This abstract class is the foundation class of all objects that change as
 * time flows. In other worlds, all time-variant objects must extend this
 * object in the {@code JLESA} system. This applies to {@link Car}s, {@link
 * Passenger}s, {@link org.jscience.architecture.lift.ca.CA}s, etc.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.3 $ $Date: 2007-10-23 18:13:52 $
 */
public abstract class Tickable implements TickableInterface {
    /**
     * DOCUMENT ME!
     */
    private static long ID = 0;

    /**
     * DOCUMENT ME!
     */
    private long MyID = 0;

/**
     * A simple constructor that sets an unique ID for this Tickable. Do not tamper with this one.
     */
    public Tickable() {
        MyID = ID;
        ID++;
    }

    /* Tickable */
    public abstract void Tick();

    /**
     * Returns the Full Canonical Name of object that evenincludes
     * unique ID
     *
     * @return DOCUMENT ME!
     */
    public String getFullName() {
        return (getClass().getName() + " (" + getName() + " " + getVersion() +
        ") #" + getID());
    }

    /* Tickable */
    public long getID() {
        return (MyID);
    }

    /* Tickable */
    public abstract String getName();

    /* Tickable */
    public abstract String getVersion();
}
