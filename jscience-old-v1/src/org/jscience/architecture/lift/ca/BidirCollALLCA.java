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
import org.jscience.architecture.lift.World;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This is a classic Bidirectional Collecting Autonomous Low Level Control
 * Algorithm (ALLCA) that can be used in more sophisticated Call Allocators
 * ({@link CA}).
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:52 $
 *
 * @see SimpleALLCA
 */
public class BidirCollALLCA {
    /** Collecting both up and down calls. */
    public final static short BOTH_WAY = 0;

    /** Collecting down calls only. */
    public final static short DOWN_WAY = Car.GOING_DOWN;

    /** Collecting up calls only. */
    public final static short UP_WAY = Car.GOING_UP;

    /**
     * DOCUMENT ME!
     */
    protected short Direction = BOTH_WAY;

    /**
     * DOCUMENT ME!
     */
    private final int NoF;

    /**
     * DOCUMENT ME!
     */
    private final Car C;

    /**
     * DOCUMENT ME!
     */
    private final boolean[] Cs;

    /**
     * DOCUMENT ME!
     */
    private boolean[] DCs;

    /**
     * DOCUMENT ME!
     */
    private boolean[] UCs;

    /**
     * DOCUMENT ME!
     */
    private boolean EnableCallCancellation = false;

    /**
     * DOCUMENT ME!
     */
    private boolean EnableNewCalls = false;

/**
     * Constructor
     *
     * @param MyCar The Car used by this class
     */
    public BidirCollALLCA(Car MyCar) {
        C = MyCar;
        Direction = BOTH_WAY;
        NoF = World.getNoF();
        Cs = new boolean[NoF];

        for (int i = 0; i < Cs.length; i++) {
            Cs[i] = false;
        }
    }

    /**
     * Sets the EnableNewCalls attribute
     *
     * @param Value DOCUMENT ME!
     */
    public void setEnableNewCalls(boolean Value) {
        if (Value) {
            System.err.println("Warning! Highly experimental!");
        }

        EnableNewCalls = Value;
    }

    /**
     * Gets the EnableNewCalls attribute
     *
     * @return DOCUMENT ME!
     */
    public boolean getEnableNewCalls() {
        return (EnableNewCalls);
    }

    /**
     * Sets the EnableCallCancellation attribute
     *
     * @param Value DOCUMENT ME!
     */
    public void setEnableCallCancellation(boolean Value) {
        if (Value) {
            System.err.println("Warning! Highly experimental!");
        }

        EnableCallCancellation = Value;
    }

    /**
     * Gets the EnableCallCancellation attribute
     *
     * @return DOCUMENT ME!
     */
    public boolean getEnableCallCancellation() {
        return (EnableCallCancellation);
    }

    /**
     * Gets the direction of the calls the ALLCA is currently
     * collecting
     *
     * @return DOCUMENT ME!
     */
    public short getDirection() {
        return (Direction);
    }

    /**
     * Returns {@code true} if and only if the Car is allowed to travel
     * from floor {@code From} to floor {@code To}. For example, if the Car
     * has passengers inside and the passengers are going up, {@code
     * goes(From,To)}  should return {@code false} if {@code From>To}  so that
     * no new passenger tries to get in.
     *
     * @param From DOCUMENT ME!
     * @param To DOCUMENT ME!
     *
     * @return {@code true}  if and only if the Car is allowed to travel from
     *         {@code From} to {@code To}
     */
    public boolean goes(int From, int To) {
        if (C != null) {
            if (C.getCrtF() == From) {
                int CS = C.getState();

                if ((CS == Car.WAITING) || (CS == Car.OPENING)) {
                    if (Direction == BOTH_WAY) {
                        return (true);
                    }

                    if (getNext() != -1) {
                        if ((Direction == DOWN_WAY) && (To <= From)) {
                            return (true);
                        }

                        if ((Direction == UP_WAY) && (To >= From)) {
                            return (true);
                        }
                    }
                }
            }
        }

        return (false);
    }

    /**
     * Issues a new command. This reflects that a passenger pushed a
     * button.
     *
     * @param AbsFloor A destination as an Absolut Floor.
     */
    public void issueCommand(int AbsFloor) {
        Cs[AbsFloor] = true;
    }

    /**
     * This is the {@code tick()} that is called by a {@link CA}.
     *
     * @param UpCalls The up calls that are planned to be served by this car.
     * @param DownCalls The down calls that are planned to be served by this
     *        car.
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public void tick(boolean[] UpCalls, boolean[] DownCalls) {
        UCs = UpCalls;
        DCs = DownCalls;

        int CF = C.getCrtF();
        int NF;

        switch (C.getState()) {
        default:
            throw new RuntimeException("Unimplemented!");

        case Car.WAITING:
            C.closeDoor();

            if ((Direction == UP_WAY) || (Direction == DOWN_WAY)) {
                NF = getNext();

                if (NF == -1) {
                    reverseDir();
                    NF = getNext();

                    if ((NF == -1) || (NF != CF)) {
                        Direction = BOTH_WAY;
                        C.closeDoor();
                    }
                }
            }

            break;

        case Car.OPENING:
        case Car.CLOSING:
            return;

        case Car.GOING_UP:
        case Car.GOING_DOWN:
            NF = getNext();

            if (NF == -1) {
                if (EnableCallCancellation) {
                    C.gotoFloor(C.getPossibleNextFloor());
                }
            } else {
                if ((EnableNewCalls) && (NF != C.getDstF()) && C.mayStopAt(NF)) {
                    C.gotoFloor(NF);
                }
            }

            break;

        case Car.PARKING:

            /*
                *  If we park on a floor:
                *  if we have a command then we must open the door.
                *  If we have a direction, we must seek for any calls in that direction
                *  until the end of the building, and opposing calls from the end of the
                *  building to the next level.
                *  If there is no call, we should check the opposite direction. If we find
                *  any, we should reverse our direction. If there is still no call, we must
                *  reverse our direction again.
                *  If we don't have a call, we must do the same, except we must set our
                *  direction to undefind if we cannot find any calls.
                */
            if (Cs[CF]) {
                Cs[CF] = false;
                C.openDoor();
                NF = getNext();

                if (NF == -1) {
                    reverseDir();
                    NF = getNext();

                    if (NF == -1) {
                        reverseDir();
                    }
                }

                break;
            } else if ((Direction == UP_WAY) || (Direction == DOWN_WAY)) {
                NF = getNext();

                if (NF == -1) {
                    reverseDir();
                    NF = getNext();

                    if (NF == -1) {
                        Direction = BOTH_WAY;
                    } else if (NF == CF) {
                        C.openDoor();
                    } else {
                        C.gotoFloor(NF);
                    }
                } else if (NF == CF) {
                    C.openDoor();
                } else {
                    C.gotoFloor(NF);
                }

                break;
            } else {
                NF = getNext();

                if ((NF != -1) && (NF != CF)) {
                    C.gotoFloor(NF);
                }
            }

            break;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private short getNewDir() {
        int CF = C.getCrtF();
        int UN;
        int DN;

        if (Direction == BOTH_WAY) {
            Direction = UP_WAY;
            UN = getNext();
            Direction = DOWN_WAY;
            DN = getNext();

            int UD = ((UN == -1) ? (World.getNoF() + 2) : Math.abs(UN - CF));
            int DD = ((DN == -1) ? (World.getNoF() + 2) : Math.abs(DN - CF));

            if ((UD == DD) && (DD == (World.getNoF() + 2))) {
                return (BOTH_WAY);
            } else if (UD < DD) {
                return (UP_WAY);
            } else {
                return (DOWN_WAY);
            }
        } else {
            throw new RuntimeException("Illegal state!");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int getNext() {
        int CF = C.getCrtF();

        switch (Direction) {
        case UP_WAY:

            for (int i = CF; i < NoF; i++) {
                if ((Cs[i]) || ((UCs[i]) && (!C.isFull()))) {
                    return (i);
                }
            }

            for (int i = NoF - 1; i > CF; i--) {
                if ((DCs[i]) && (!C.isFull())) {
                    return (i);
                }
            }

            return (-1);

        case DOWN_WAY:

            for (int i = CF; i >= 0; i--) {
                if ((Cs[i]) || ((DCs[i]) && (!C.isFull()))) {
                    return (i);
                }
            }

            for (int i = 0; i < CF; i++) {
                if ((UCs[i]) && (!C.isFull())) {
                    return (i);
                }
            }

            return (-1);

        case BOTH_WAY:
            Direction = getNewDir();

            if (Direction == BOTH_WAY) {
                return (-1);
            } else {
                return (getNext());
            }

        default:
            throw new RuntimeException("Illegal State!");
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void reverseDir() {
        if (Direction == UP_WAY) {
            Direction = DOWN_WAY;
        } else if (Direction == DOWN_WAY) {
            Direction = UP_WAY;
        } else {
            throw new RuntimeException("Cannot reverse!");
        }
    }
}
