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
 * This should be the father of all entities that process {@link Passenger}s.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.3 $ $Date: 2007-10-23 18:13:52 $
 *
 * @see Passenger
 * @see PassengerGenerator
 */
public abstract class PassengerProcessor extends Tickable {
/**
     * Constructor
     */
    public PassengerProcessor() {
        super();
    }

    /**
     * This method is called by {@link World} after each new {@link
     * Passenger} generated.
     *
     * @param P DOCUMENT ME!
     */
    public abstract void created(Passenger P);

    /* Tickable */
    public abstract String getName();

    /* Tickable */
    public abstract String getVersion();

    /**
     * This method is called by {@link World} before each {@link
     * Passenger} terminated.
     *
     * @param P DOCUMENT ME!
     */
    public abstract void process(Passenger P);

    /**
     * This method is called by {@link World} before this {@code
     * Passengerprocessor} is terminated. "As a reaction, the instance of this
     * object class should move and/or duplicate all non-temporary information
     * to persistent databases". In other worlds, it should save everything
     * important to disk.
     */
    public abstract void prepareToDie();
}
