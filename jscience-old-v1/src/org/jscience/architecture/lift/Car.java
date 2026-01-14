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
 * This is the fundamental interface of all {@code Car}s.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:51 $
 */
public interface Car extends TickableInterface {
    /** The car is parking with closed doors */
    public final static int PARKING = 0;

    /** The car is closing its doors */
    public final static int CLOSING = 1;

    /** The car is opening its doors */
    public final static int OPENING = 2;

    /** The car is waiting with open doors */
    public final static int WAITING = 3;

    /** The car is waiting with going up */
    public final static int GOING_UP = 4;

    /** The car is waiting with going down */
    public final static int GOING_DOWN = 5;

    /**
     * This method is called whenever a passenger leaves the {@code
     * Car}.
     */
    public void decreaseNumberOfPassangers();

    /**
     * This method is called whenever a passenger enters the {@code
     * Car}.
     */
    public void increaseNumberOfPassangers();

    /**
     * Tries to open the {@code Car}'s door.
     *
     * @return {@code true} if and only if succesful.
     */
    public boolean closeDoor();

    /**
     * Returns the capacity of the {@code Car}, measured in persons
     *
     * @return DOCUMENT ME!
     */
    public int getCapacity();

    /**
     * Sets the capacity of the {@code Car}, measured in persons
     *
     * @param NewCapacity DOCUMENT ME!
     */
    public void setCapacity(int NewCapacity);

    /**
     * Returns the current floor of the {@code Car}
     *
     * @return DOCUMENT ME!
     */
    public int getCrtF();

    /**
     * Returns the destination floor of the {@code Car} if it is
     * moving.
     *
     * @return DOCUMENT ME!
     */
    public int getDstF();

    /**
     * Returns the {@link KinematicModel} of the {@code Car}
     *
     * @return DOCUMENT ME!
     */
    public KinematicModel getKinematicModel();

    /**
     * Returns the number of {@link Passenger}s currently travelling in
     * the {@code Car}
     *
     * @return DOCUMENT ME!
     */
    public int getNoPs();

    /**
     * Returns the destination floor of the {@code Car}. This is the
     * next floor where the car could stop, according to its {@link
     * KinematicModel}.
     *
     * @return DOCUMENT ME!
     */
    public int getPossibleNextFloor();

    /**
     * Returns the progress of the current action (closing door, going
     * to somewhere, etc) of the {@code Car} from its {@link KinematicModel}.
     *
     * @return DOCUMENT ME!
     */
    public int getProgress();

    /**
     * Returns the source floor of the {@code Car} if it is moving.
     *
     * @return DOCUMENT ME!
     */
    public int getSrcF();

    /**
     * Returns the current state of the {@code Car}.
     *
     * @return DOCUMENT ME!
     */
    public int getState();

    /**
     * Tries to send the {@code Car} to floor {@code TargetFloor}.
     *
     * @param TargetFloor DOCUMENT ME!
     *
     * @return {@code true} if and only if succesful.
     */
    public boolean gotoFloor(int TargetFloor);

    /**
     * Returns true if and only if the {@code Car} is empty.
     *
     * @return DOCUMENT ME!
     */
    public boolean isEmpty();

    /**
     * Returns true if and only if the {@code Car} is full.
     *
     * @return DOCUMENT ME!
     */
    public boolean isFull();

    /**
     * Returns true if and only if the {@code Car} is moving.
     *
     * @return DOCUMENT ME!
     */
    public boolean isMoving();

    /**
     * Returns true if and only if the {@code Car} may stop at {@code
     * AbsFloor} before it's planned next stop from the {@code Car}'s {@link
     * KinematicModel}.
     *
     * @param AbsFloor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean mayStopAt(int AbsFloor);

    /**
     * Tries to open the {@code Car}'s door.
     *
     * @return {@code true} if and only if succesful.
     */
    public boolean openDoor();

    /**
     * {@link java.lang.Object}
     *
     * @return DOCUMENT ME!
     */
    public String toString();
}
