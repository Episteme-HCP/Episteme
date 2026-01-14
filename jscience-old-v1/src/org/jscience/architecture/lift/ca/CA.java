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

package org.jscience.architecture.lift.ca;

import org.jscience.architecture.lift.Car;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This interface defines the methods that must be implemented by all Control
 * Algorithms (also called as High-Level Controls, Call Dispatcher Algorithms,
 * Call Allocation Algorithms, Call Scheduler Algorithms, etc).
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:52 $
 */
public interface CA {
    /**
     * Return {@code true} if the {@code CarIndex}th car goes from
     * Floor {@code From} to Floor {@code To}. This is used to notify the
     * passengers so they can decide if they want to get into the car or not.
     *
     * @param From The passenger's current floor
     * @param To The passenger's destination floor
     * @param CarIndex The index of the Car
     *
     * @return {@code true} if the Car is suitable for the passenger, false
     *         otherwise
     */
    public boolean goes(int From, int To, int CarIndex);

    /**
     * Issues (registers) a command for the {@code AbsFloor}th Floor to
     * the {@code C} Car. This happens when a Passenger pushes the
     * corresponding Floor button.
     *
     * @param C DOCUMENT ME!
     * @param AbsFloor DOCUMENT ME!
     */
    public void issueCommand(Car C, int AbsFloor);

    /**
     * {@link org.jscience.architecture.lift.Tickable}
     */
    public void tick();
}
