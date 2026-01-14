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
 * This interface describes a Kinematic Model. A Kinematic Model is a
 * mathematical model used to kinematically (mechanically) model an elevator
 * car and it's movement in the shaft.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.3 $ $Date: 2007-10-21 21:07:00 $
 */

public interface KinematicModel {
    /**
     * {@link Car}s inherit {@code void Tick()} from the {@link Tickable} interface and
     * call this function each time their {@code Tick()} method is called. Before calling,
     * the {@link Car} increasess it's {@code Progress} by one. This happens in every
     * {@code Tick()}, namely in every 0.1 second, so this method is called for each {@link Car}
     * in every 0.1 second of simulation time.<br>
     * <b>It must call {@code C.arrived()} if the C {@link Car} arrives at it's planned
     * destination Floor.</b><br>
     * <b>It must call {@code C.CrtFReached()} if the C {@link Car} is reached the next floor
     * while moving</b>
     *
     * @param C The {@link Car} which called this method.
     */
    public void tick(SimulatedCar C);


    /**
     * This method is used to determinate if the {@link Car} C may stop on floor
     * {@code AbsFloor} before its next planned stop without reversing its direction.
     * This occures when a moving {@link Car} needs to determine if it may accept a new
     * call between its current floor and planned destination floor.
     */
    public boolean mayStopAt(Car C, int AbsFloor);

    /**
     * Notifies the Kinematic Model that the destination floor of the moving {@link Car}
     * has changed. This usually happens when a {@link Car} catches a call that appears
     * after it's start.
     *
     * @param C The Car which changed its destination floor
     */
    public void dstFChanged(Car C);


    /**
     * Gets the {@code Progress} of a {@link Car} in the [0, 1] interval. This is only
     * applicable if the doors are closing or opening or if the {@link Car} is moving from
     * one floor to another. This should be 0.25 if the door is only 25 percent open, 0.66
     * if the car already moved 66% of the distance between two adjacent floors, and so on.
     *
     * @param C The target {@link Car}
     * @return The {@code 0<=Progress<=1} value
     */
    public double getProgress(Car C);

    /**
     * Returns the time (in Ticks) needed to open the door of the {@link Car} C on
     * the {@code AbsFloor}th Floor.
     */
    public int getDoorOpenTime(int AbsFloor, Car C);

    /**
     * Returns the time (in Ticks) needed to close the door of the {@link Car} C on
     * the {@code AbsFloor}th Floor.
     */
    public int getDoorCloseTime(int AbsFloor, Car C);
}

