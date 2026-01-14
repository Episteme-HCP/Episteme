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

import java.util.logging.Level;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * SimulatedCar is one of the most common forms of {@link Car}s, at least, in
 * {@code JLESA}. Through the {@link Car} interface, it is not impossible to
 * visualize real cars, for example.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:52 $
 */
public class SimulatedCar extends Tickable implements Car {
    /**
     * DOCUMENT ME!
     */
    private int NoPs = 0;

    /**
     * DOCUMENT ME!
     */
    private int State = -1;

    /**
     * DOCUMENT ME!
     */
    private int Capacity;

    /**
     * DOCUMENT ME!
     */
    private final KinematicModel KM;

    /**
     * DOCUMENT ME!
     */
    private int CrtF = 0;

    /**
     * DOCUMENT ME!
     */
    private int SrcF = -1;

    /**
     * DOCUMENT ME!
     */
    private int DstF = -1;

    /**
     * DOCUMENT ME!
     */
    private int Progress = 0;

    /* Car*/
    public SimulatedCar(int CurrentFloor, KinematicModel KinMod) {
        CrtF = CurrentFloor;
        State = PARKING;
        KM = KinMod;
    }

    /* Car*/
    public void decreaseNumberOfPassangers() {
        NoPs--;
    }

    /* Car*/
    public void increaseNumberOfPassangers() {
        NoPs++;
    }

    /* Car*/
    public void setCapacity(int NewCapacity) {
        Capacity = NewCapacity;
    }

    /* Car*/
    public void Tick() {
        if (KM == null) {
            throw new RuntimeException("Car #" + getID() +
                " is uninitialized!");
        }

        switch (State) {
        case WAITING:
        case PARKING:
            break;

        case OPENING:
            Progress++;

            if (Progress == KM.getDoorOpenTime(CrtF, this)) {
                World.getLogger()
                     .log(Level.INFO, "Car finished openening doors",
                    new Object[] {
                        new Integer(World.getTotalTicks()), new Long(getID())
                    });
                State = WAITING;
                Progress = 0;
            }

            break;

        case CLOSING:
            Progress++;

            if (Progress == KM.getDoorCloseTime(CrtF, this)) {
                World.getLogger()
                     .log(Level.INFO, "Car finished closing doors",
                    new Object[] {
                        new Integer(World.getTotalTicks()), new Long(getID())
                    });
                State = PARKING;
                Progress = 0;
            }

            break;

        case GOING_UP:
        case GOING_DOWN:
            Progress++;
            KM.tick(this);

            break;

        default:
            throw new RuntimeException("Not Implemented!");
        }
    }

    /* Car*/
    public boolean closeDoor() {
        if ((State != WAITING) || (World.isBlocked(this))) {
            return (false);
        } else {
            Progress = 0;
            State = CLOSING;
            World.getLogger()
                 .log(Level.INFO, "Car started closing doors",
                new Object[] {
                    new Integer(World.getTotalTicks()), new Long(getID())
                });

            return (true);
        }
    }

    /* Car*/
    public int getCapacity() {
        return (Capacity);
    }

    /* Car*/
    public int getCrtF() {
        return (CrtF);
    }

    /* Car*/
    public int getDstF() {
        return (DstF);
    }

    /* Car*/
    public KinematicModel getKinematicModel() {
        return (KM);
    }

    /* Car*/
    public int getNoPs() {
        return (NoPs);
    }

    /* Car*/
    public int getPossibleNextFloor() {
        switch (getState()) {
        case PARKING:
        case CLOSING:
        case OPENING:
        case WAITING:
            return (CrtF);

        case GOING_UP:

            for (int i = CrtF; i <= DstF; i++) {
                if (mayStopAt(i)) {
                    return (i);
                }
            }

        case GOING_DOWN:

            for (int i = CrtF; i >= DstF; i--) {
                if (mayStopAt(i)) {
                    return (i);
                }
            }

        default:
            throw new RuntimeException("Not implemented! #726");
        }
    }

    /* Car*/
    public int getProgress() {
        return (Progress);
    }

    /* Car*/
    public int getSrcF() {
        return (SrcF);
    }

    /* Car*/
    public int getState() {
        return (State);
    }

    /* Tickable */
    public String getName() {
        return ("SimulatedCar");
    }

    /* Tickable */
    public String getVersion() {
        return ("0.2.1");
    }

    /* Car*/
    public boolean gotoFloor(int TargetFloor) {
        if (State != PARKING) {
            if ((isMoving()) && (mayStopAt(TargetFloor))) {
                DstF = TargetFloor;
                World.getLogger()
                     .log(Level.INFO, "Car changed destination",
                    new Object[] {
                        new Integer(World.getTotalTicks()), new Long(getID()),
                        new Integer(getDstF()), new Integer(TargetFloor)
                    });
                KM.dstFChanged(this);

                return (true);
            }

            return (false);
        } else {
            if (CrtF == TargetFloor) {
                throw new RuntimeException("Error:Car #" + getID() +
                    ": Cannot go from AbsFloor #" + CrtF + " to AbsFloor #" +
                    CrtF);
            }

            Progress = 0;
            SrcF = CrtF;
            DstF = TargetFloor;

            if (DstF > SrcF) {
                State = GOING_UP;
            } else {
                State = GOING_DOWN;
            }

            World.getLogger()
                 .log(Level.INFO, "Car started",
                new Object[] {
                    new Integer(World.getTotalTicks()), new Long(getID()),
                    new Integer(getSrcF()), new Integer(getDstF())
                });

            return (true);
        }
    }

    /* Car*/
    public boolean isEmpty() {
        return (NoPs == 0);
    }

    /* Car*/
    public boolean isFull() {
        return (NoPs >= Capacity);
    }

    /* Car*/
    public boolean isMoving() {
        return ((State == GOING_UP) || (State == GOING_DOWN));
    }

    /* Car*/
    public boolean mayStopAt(int AbsFloor) {
        if (!isMoving()) {
            throw new RuntimeException("Not moving!!! #324");
        }

        if ((State == GOING_DOWN) && ((AbsFloor < DstF) || (AbsFloor >= SrcF))) {
            return false;
        }

        if ((State == GOING_UP) && ((AbsFloor > DstF) || (AbsFloor <= SrcF))) {
            return false;
        }

        if (DstF == AbsFloor) {
            return true;
        }

        return (KM.mayStopAt(this, AbsFloor));
    }

    /* Car*/
    public boolean openDoor() {
        if (State != PARKING) {
            return (false);
        } else {
            Progress = 0;
            State = OPENING;
            World.getLogger()
                 .log(Level.INFO, "Car started opening doors",
                new Object[] {
                    new Integer(World.getTotalTicks()), new Long(getID())
                });

            return (true);
        }
    }

    /* Object */
    public String toString() {
        return ("Car #" + getID());
    }

    /**
     * Called from the {@link KinematicModel} of the {@link Car}, this
     * method notifies the {@code SimulatedCar} that it has reached a new
     * floor.
     *
     * @param AbsFloor DOCUMENT ME!
     */
    protected void CrtFReached(int AbsFloor) {
        CrtF = AbsFloor;
        World.getLogger()
             .log(Level.INFO, "Car reached level while moving",
            new Object[] {
                new Integer(World.getTotalTicks()), new Long(getID()),
                new Integer(CrtF)
            });
    }

    /**
     * Called from the {@link KinematicModel} of the {@link Car}, this
     * method notifies the {@code SimulatedCar} that it has arrived to it's
     * target floor.
     */
    protected void arrived() {
        CrtF = DstF;
        SrcF = -1;
        DstF = -1;
        State = Car.PARKING;
        Progress = 0;
        World.getLogger()
             .log(Level.INFO, "Car arrived",
            new Object[] {
                new Integer(World.getTotalTicks()), new Long(getID()),
                new Integer(CrtF)
            });
    }
}
