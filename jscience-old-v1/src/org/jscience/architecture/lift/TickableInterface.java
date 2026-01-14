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
 * This interface defines the methods of {@link Tickable}.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:52 $
 */
public interface TickableInterface {
    /**
     * This function is called in each step of the simulator. In other
     * words, this method is called whenever a discrete simulation step (0.1
     * second) happens. All objects that are time-variant is notified by this
     * method that some time has elapsed since.
     */
    public void Tick();

    /**
     * Returns the Full Canonical Name of the object.
     *
     * @return DOCUMENT ME!
     */
    public String getFullName();

    /**
     * Returns the unique ID.
     *
     * @return DOCUMENT ME!
     */
    public long getID();

    /**
     * Returns the Name of the object.
     *
     * @return DOCUMENT ME!
     */
    public String getName();

    /**
     * Returns the Version of the object
     *
     * @return DOCUMENT ME!
     */
    public String getVersion();
}
