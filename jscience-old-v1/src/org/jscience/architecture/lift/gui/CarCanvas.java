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

package org.jscience.architecture.lift.gui;

import java.awt.*;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.3 $ $Date: 2007-10-23 18:13:53 $
 */
public interface CarCanvas {
    /**
     * DOCUMENT ME!
     */
    final static int GOING_UP = 0;

    /**
     * DOCUMENT ME!
     */
    final static int GOING_DOWN = 1;

    /**
     * DOCUMENT ME!
     */
    final static int ARRIVING_UP = 2;

    /**
     * DOCUMENT ME!
     */
    final static int ARRIVING_DOWN = 3;

    /**
     * DOCUMENT ME!
     */
    final static int PARKING = 4;

    /**
     * DOCUMENT ME!
     */
    final static int WAITING = 5;

    /**
     * DOCUMENT ME!
     */
    final static int CLOSE_OPEN = 6;

    /**
     * DOCUMENT ME!
     *
     * @param DstFloors DOCUMENT ME!
     */
    abstract public void setDstFloors(int[] DstFloors);

    /**
     * DOCUMENT ME!
     *
     * @param MaxNumber DOCUMENT ME!
     */
    abstract public void setMaxNumber(int MaxNumber);

    /**
     * DOCUMENT ME!
     *
     * @param ActNumber DOCUMENT ME!
     */
    abstract public void setActNumber(int ActNumber);

    /**
     * DOCUMENT ME!
     *
     * @param NewState DOCUMENT ME!
     */
    public abstract void setState(int NewState);

    /**
     * DOCUMENT ME!
     *
     * @param NewProgress DOCUMENT ME!
     */
    public abstract void setProgress(double NewProgress);

    /**
     * DOCUMENT ME!
     *
     * @param NewCarPresent DOCUMENT ME!
     */
    public abstract void setCarPresent(boolean NewCarPresent);

    /**
     * DOCUMENT ME!
     *
     * @param G DOCUMENT ME!
     */
    public abstract void paint(Graphics G);
}
