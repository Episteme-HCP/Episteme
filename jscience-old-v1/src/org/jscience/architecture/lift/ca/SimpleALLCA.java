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
 * This is SimpleALLCA that controls a {@link Car}
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:53 $
 */
public class SimpleALLCA {
    /** Description of the Field */
    public final static short BOTH_WAY = 0;

    /** Description of the Field */
    public final static short DOWN_WAY = Car.GOING_DOWN;

    /** Description of the Field */
    public final static short UP_WAY = Car.GOING_UP;

    /**
     * DOCUMENT ME!
     */
    protected short Direction = BOTH_WAY;

    /**
     * DOCUMENT ME!
     */
    private short ForcedDir = BOTH_WAY;

    /**
     * DOCUMENT ME!
     */
    private final boolean[] Commands;

    /**
     * DOCUMENT ME!
     */
    private final boolean[] DCs;

    /**
     * DOCUMENT ME!
     */
    private final boolean[] UCs;

    /**
     * DOCUMENT ME!
     */
    private int ParkLevel = -1;

    /**
     * DOCUMENT ME!
     */
    private int ParkTicks = 0;

    /**
     * DOCUMENT ME!
     */
    private int ParkTimeOut = -1;

    /**
     * DOCUMENT ME!
     */
    private int DoorOpenTimeOut = 5;

    /**
     * DOCUMENT ME!
     */
    private int DoorOpenTicks = 0;

    /**
     * DOCUMENT ME!
     */
    private Car C = null;

    /**
     * DOCUMENT ME!
     */
    private int NoF;

/**
     * Constructor
     *
     * @param MyCar             The {@link Car} controlled by this {@code SimpleALLCA}
     * @param MyParkLevel       The level on which {@code MyCar} should park
     * @param MyParkTimeOut     After this many ticks, {@code SimpleALLCA} will park {@code MyCar}
     * @param MyDoorOpenTimeOut After this many ticks, {@code SimpleALLCA} will close {@code MyCar}'s door
     */
    public SimpleALLCA(Car MyCar, int MyParkLevel, int MyParkTimeOut,
        int MyDoorOpenTimeOut) {
        ParkLevel = MyParkLevel;
        ParkTimeOut = MyParkTimeOut;
        DoorOpenTimeOut = MyDoorOpenTimeOut;
        C = MyCar;
        Direction = BOTH_WAY;
        NoF = World.getNoF();
        Commands = new boolean[NoF];
        UCs = new boolean[NoF];
        DCs = new boolean[NoF];

        for (int i = 0; i < Commands.length; i++) {
            Commands[i] = false;
            UCs[i] = false;
            DCs[i] = false;
        }
    }

    /**
     * Decides the direction in which {@code MyCar} should go.
     *
     * @param UpCalls The UP-calls allocated to this {@code SimpleALLCA}
     * @param DownCalls The DOWN-calls allocated to this {@code SimpleALLCA}
     *
     * @return The new {@code Direction}
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public short decideDirection(boolean[] UpCalls, boolean[] DownCalls) {
        if ((Direction != BOTH_WAY) || (C.getState() != C.PARKING)) {
            throw new RuntimeException("Invalid context for decideDirection()!");
        }

        int CL = C.getCrtF();
        int NoF = World.getNoF();

        for (int i = 0; i < NoF; i++) {
            if ((CL + i) < NoF) {
                if (UpCalls[CL + i]) {
                    return (UP_WAY);
                }
            }

            if ((CL - i) >= 0) {
                if (DownCalls[CL - i]) {
                    return (DOWN_WAY);
                }
            }
        }

        for (int i = NoF - 1; i >= 0; i--) {
            if ((CL + i) < NoF) {
                if (DownCalls[CL + i]) {
                    return (DOWN_WAY);
                }
            }

            if ((CL - i) >= 0) {
                if (UpCalls[CL - i]) {
                    return (UP_WAY);
                }
            }
        }

        return (BOTH_WAY);
    }

    /**
     * Returns the current {@code Direction}
     *
     * @return DOCUMENT ME!
     */
    public short getDirection() {
        return (Direction);
    }

    /**
     * Returns the next scheduled floor to stop on
     *
     * @return DOCUMENT ME!
     */
    public int getNextStop() {
        if (Direction == UP_WAY) {
            for (int i = C.getCrtF(); i < NoF; i++) {
                if (Commands[i]) {
                    return (i);
                }
            }
        }

        if (Direction == DOWN_WAY) {
            for (int i = C.getCrtF(); i >= 0; i--) {
                if (Commands[i]) {
                    return (i);
                }
            }
        }

        return -1;
    }

    /**
     * {@link CA}
     *
     * @param From DOCUMENT ME!
     * @param To DOCUMENT ME!
     * @param UpCalls DOCUMENT ME!
     * @param DownCalls DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean goes(int From, int To, boolean[] UpCalls, boolean[] DownCalls) {
        if (C != null) {
            if (C.getCrtF() == From) {
                int CS = C.getState();

                if ((CS == Car.WAITING) || (CS == Car.OPENING)) {
                    if (Direction == BOTH_WAY) {
                        return (true);
                    }

                    if (ForcedDir == DOWN_WAY) {
                        return (To <= From);
                    }

                    if (ForcedDir == UP_WAY) {
                        return (To >= From);
                    }

                    if (Direction == DOWN_WAY) {
                        if (To <= From) {
                            for (int i = From; i >= 0; i--) {
                                if (DownCalls[i] || Commands[i]) {
                                    return (true);
                                }
                            }
                        } else {
                            for (int i = From; i >= 0; i--) {
                                if (DownCalls[i] || Commands[i]) {
                                    return (false);
                                }
                            }

                            return (true);
                        }
                    }

                    if (Direction == UP_WAY) {
                        if (To >= From) {
                            for (int i = From; i < NoF; i++) {
                                if (UpCalls[i] || Commands[i]) {
                                    return (true);
                                }
                            }
                        } else {
                            for (int i = From; i < NoF; i++) {
                                if (UpCalls[i] || Commands[i]) {
                                    return (false);
                                }
                            }

                            return (true);
                        }
                    }
                }
            }
        }

        return (false);
    }

    /**
     * Issues a command to {@code MyCar} to floor {@code AbsFloor}
     *
     * @param AbsFloor DOCUMENT ME!
     */
    public void issueCommand(int AbsFloor) {
        addCommand(AbsFloor);
    }

    /**
     * Returns the next scheduled floor to stop on, depending upon the
     * {@code UpCalls} and {@code DownCalls}
     *
     * @param UpCalls DOCUMENT ME!
     * @param DownCalls DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int nextStop(boolean[] UpCalls, boolean[] DownCalls) {
        if (Direction == UP_WAY) {
            for (int i = C.getCrtF(); i < NoF; i++) {
                if (Commands[i] || ((!C.isFull()) && (UpCalls[i]))) {
                    return (i);
                }
            }

            for (int i = NoF - 1; i >= 0; i--) {
                if (Commands[i] || ((!C.isFull()) && (DownCalls[i]))) {
                    Direction = DOWN_WAY;

                    return (i);
                }
            }

            for (int i = 0; i < C.getCrtF(); i++) {
                if (UpCalls[i]) {
                    return (i);
                }
            }
        }

        if (Direction == DOWN_WAY) {
            for (int i = C.getCrtF(); i >= 0; i--) {
                if (Commands[i] || ((!C.isFull()) && (DownCalls[i]))) {
                    return (i);
                }
            }

            for (int i = 0; i < NoF; i++) {
                if (Commands[i] || ((!C.isFull()) && (UpCalls[i]))) {
                    Direction = UP_WAY;

                    return (i);
                }
            }

            for (int i = NoF - 1; i > C.getCrtF(); i--) {
                if (DownCalls[i]) {
                    return (i);
                }
            }
        }

        return (-1);
    }

    /**
     * This is the {@code tick()} that is called by a {@link CA}.
     *
     * @param UpCalls The up calls that are planned to be served by this car.
     * @param DownCalls The down calls that are planned to be served by this
     *        car.
     */
    public void tick(boolean[] UpCalls, boolean[] DownCalls) {
        int NextStop;

        switch (C.getState()) {
        default:
        case Car.OPENING:
            DoorOpenTicks = 0;

            return;

        case Car.CLOSING:
            ForcedDir = BOTH_WAY;

            return;

        case Car.GOING_UP:

            for (int i = C.getCrtF(); i < C.getDstF(); i++) {
                if ((UpCalls[i]) && (C.mayStopAt(i))) {
                    if (!Commands[i]) {
                        addCommand(i);
                        C.gotoFloor(i);
                    }

                    return;
                }
            }

            break;

        case Car.GOING_DOWN:

            for (int i = C.getCrtF(); i > C.getDstF(); i--) {
                if ((DownCalls[i]) && (C.mayStopAt(i))) {
                    if (!Commands[i]) {
                        addCommand(i);
                        C.gotoFloor(i);
                    }

                    return;
                }
            }

            break;

        case Car.WAITING:
            DoorOpenTicks++;

            if (DoorOpenTicks >= DoorOpenTimeOut) {
                C.closeDoor();
            }

            return;

        case Car.PARKING:

            switch (Direction) {
            case UP_WAY:
            case DOWN_WAY:
                ParkTicks = 0;

                if (getNextStop() == C.getCrtF()) {
                    ForcedDir = Direction;
                    C.openDoor();
                    removeCommand(C.getCrtF());
                } else {
                    NextStop = nextStop(UpCalls, DownCalls);

                    if (NextStop == -1) {
                        Direction = BOTH_WAY;
                    } else {
                        if (NextStop != C.getCrtF()) {
                            addCommand(NextStop);
                            C.gotoFloor(NextStop);
                        } else {
                            ForcedDir = Direction;
                            C.openDoor();
                        }
                    }
                }

                break;

            case BOTH_WAY:
                Direction = decideDirection(UpCalls, DownCalls);
                NextStop = nextStop(UpCalls, DownCalls);

                if (NextStop == -1) {
                    Direction = BOTH_WAY;
                    ParkTicks++;

                    if (ParkTicks == ParkTimeOut) {
                        if (ParkLevel > C.getCrtF()) {
                            Direction = UP_WAY;
                            C.gotoFloor(ParkLevel);
                        }

                        if (ParkLevel < C.getCrtF()) {
                            Direction = DOWN_WAY;
                            C.gotoFloor(ParkLevel);
                        }
                    }
                } else {
                    if (NextStop != C.getCrtF()) {
                        addCommand(NextStop);
                        C.gotoFloor(NextStop);
                    } else {
                        ForcedDir = Direction;
                        C.openDoor();
                    }
                }

                break;

            default:
                break;
            }

            /*
                         *  System.out.print("Direction: {" + DirSs[Dir] + "}");
                         *  System.out.print(generateOut("Commands", Commands));
                         *  System.out.print(generateOut("UpCalls", UpCalls));
                         *  System.out.println(generateOut("DownCalls", DownCalls));
                         */
        }
    }

    /**
     * Adds a command to Floor {@code NewCommand}
     *
     * @param NewCommand DOCUMENT ME!
     */
    private void addCommand(int NewCommand) {
        Commands[NewCommand] = true;
    }

    /**
     * Generates debug output
     *
     * @param Name DOCUMENT ME!
     * @param Values DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String generateOut(String Name, boolean[] Values) {
        StringBuffer RetVal = new StringBuffer((Values.length * 3) +
                Name.length() + 7);
        boolean Was = false;

        RetVal.append("   " + Name + ": {");

        for (int i = 0; i < Values.length; i++) {
            if (Values[i]) {
                RetVal.append(Was ? ", " : "");
                RetVal.append("" + World.toRelFloor(i));
                Was = true;
            }
        }

        RetVal.append("}");

        return (RetVal.toString());
    }

    /**
     * Deletes a command to floor {@code OldCommand}
     *
     * @param OldCommand DOCUMENT ME!
     */
    private void removeCommand(int OldCommand) {
        Commands[OldCommand] = false;
    }
}
