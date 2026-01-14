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

import java.util.Random;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This is the fundamental class that simulated {@code Passenger}s.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:52 $
 *
 * @see PassengerGenerator
 * @see PassengerProcessor
 */
public class Passenger extends Tickable {
    /** The {@code Passenger} is waiting. */
    public final static int WAITING = 0;

    /** The {@code Passenger} is getting into a {@link Car}. */
    public final static int GETTING_IN = 1;

    /** The {@code Passenger} is travelling in a {@link Car}. */
    public final static int TRAVELLING = 2;

    /** The {@code Passenger} is getting out from a {@link Car}. */
    public final static int GETTING_OUT = 3;

    /**
     * The {@code Passenger} is finished it's travel, it is ready to
     * terminate in the {@link PassengerProcessor}s.
     */
    public final static int FINISHED = 4;

    /**
     * The (in this case, absolutely not) random seed used by the
     * {@link java.util.Random} entity used to generate pseudorandom numbers
     * for the {@code Passenger} class.
     */
    public static final Random PassengerRandom = new Random(23235634);

    /**
     * DOCUMENT ME!
     */
    private int PushesBoth;

    /**
     * DOCUMENT ME!
     */
    private int Treshold = 20;

    /** The current progress of the {@code Passenger}'s current action. */
    protected int Progress = 0;

    /** The {@link Car} in which the {@code Passenger} is travelling in. */
    protected Car TravelCar = null;

    /** The time needed by the {@code Passenger} to get in into the {@link Car} */
    protected int GetInTime = 20;

    /** The time needed by the {@code Passenger} to get out of the {@link Car} */
    protected int GetOutTime = 20;

    /**
     * DOCUMENT ME!
     */
    private final int CreationTime;

    /**
     * DOCUMENT ME!
     */
    private int Ticks_Waited = 0;

    /**
     * DOCUMENT ME!
     */
    private int Ticks_Travelled = 0;

    /**
     * DOCUMENT ME!
     */
    private int State = 0;

    /**
     * DOCUMENT ME!
     */
    private int SrcF = 0;

    /**
     * DOCUMENT ME!
     */
    private int CrtF = 0;

    /**
     * DOCUMENT ME!
     */
    private int DstF = 0;

    /**
     * DOCUMENT ME!
     */
    private int AngerLevel;

    /**
     * DOCUMENT ME!
     */
    private String Desc;

/**
     * Constructor. Calls {@code Passenger(FromFloor,ToFloor,true).}
     *
     * @param FromFloor The {@code Passenger}'s source floor
     * @param ToFloor   The {@code Passenger}'s destination floor
     */
    public Passenger(int FromFloor, int ToFloor) {
        this(FromFloor, ToFloor, true);
    }

/**
     * Constructor. The tricky one.
     *
     * @param FromFloor The {@code Passenger}'s source floor
     * @param ToFloor   The {@code Passenger}'s destination floor
     * @param AbsFloor  This must be set to {@code true} if the above parameters are given in
     *                  "absolute floors" and not in "building floors". For example, if the building has {@code [-2,5]}
     *                  floors, the lowest floor is {@code 0} in absolute floors and {@code -2} in building floors.
     */
    public Passenger(int FromFloor, int ToFloor, boolean AbsFloor) {
        if (FromFloor == ToFloor) {
            throw new RuntimeException("ERROR:Passanger:" + getID() +
                ":FromFloor()=ToFloor()");
        }

        SrcF = AbsFloor ? FromFloor : World.toAbsFloor(FromFloor);
        CrtF = SrcF;
        DstF = AbsFloor ? ToFloor : World.toAbsFloor(ToFloor);
        //		Desc = "{" + SrcF + "->" + DstF + "}";
        Desc = "" + DstF;
        CreationTime = World.getTotalTicks();
        PushesBoth = (PassengerRandom.nextFloat() > 0.75) ? 3 : 0;
    }

    /* Tickable */
    public void Tick() {
        Progress++;

        if (State == WAITING) {
            if (Progress < Treshold) {
                AngerLevel = 0;
            } else if (Progress < (2 * Treshold)) {
                AngerLevel = 1;
            } else {
                AngerLevel = 2;
            }
        }
    }

    /**
     * Returns the progress of the {@code Passenger} in a {@code
     * [0,12]} scale instead of the usual {@code [0,1]} scale.
     *
     * @return DOCUMENT ME!
     */
    public int get12Progress() {
        if (State == WAITING) {
            int P12 = (int) ((Progress * 12.0) / (double) Treshold);

            if (Progress >= Treshold) {
                P12 = Math.min(P12 - 12, 12);
            }

            return (P12);
        }

        if (State == GETTING_IN) {
            return ((int) ((Progress * 12.0) / (double) GetInTime));
        }

        if (State == GETTING_OUT) {
            return ((int) ((Progress * 12.0) / (double) GetOutTime));
        }

        return (0);
    }

    /**
     * Returns the {@code Anger Level}.
     *
     * @return DOCUMENT ME!
     */
    public int getAngerLevel() {
        return (AngerLevel);
    }

    /**
     * Returns the {@code Creation Time}.
     *
     * @return DOCUMENT ME!
     */
    public int getCreationTime() {
        return (CreationTime);
    }

    /**
     * Returns the {@code Current Floor}.
     *
     * @return DOCUMENT ME!
     */
    public int getCrtF() {
        return (CrtF);
    }

    /**
     * Returns the {@code Destination Floor}.
     *
     * @return DOCUMENT ME!
     */
    public int getDstF() {
        return (DstF);
    }

    /**
     * Returns the {@code Source Floor}.
     *
     * @return DOCUMENT ME!
     */
    public int getSrcF() {
        return (SrcF);
    }

    /* Tickable */
    public String getName() {
        return ("Simple Passenger");
    }

    /**
     * Returns the {@code State}.
     *
     * @return DOCUMENT ME!
     */
    public int getState() {
        return (State);
    }

    /**
     * Returns the number of {@code Ticks} travelled.
     *
     * @return DOCUMENT ME!
     */
    public int getTicksTravelled() {
        return (Ticks_Travelled);
    }

    /**
     * Returns the number of {@code Ticks} waited.
     *
     * @return DOCUMENT ME!
     */
    public int getTicksWaited() {
        return (Ticks_Waited);
    }

    /* Tickable */
    public String getVersion() {
        return ("0.0.1");
    }

    /**
     * Called by the GUI to increase the {@code Destination Floor}.
     * Pretty neat. Ruins some calls sometimes. Used only when demonstrating
     * the GUI. TODO: replace it later with a "New Passenger Wizard"
     */
    public void increaseDstF() {
        if (State == WAITING) {
            DstF += (((SrcF - 1) == DstF) ? 2 : 1);

            if (DstF >= World.getNoF()) {
                DstF -= World.getNoF();
            }
        }
    }

    /**
     * Called by the {@link World} every time the {@code Passenger} may
     * push one or more of the given {@link InOutput} IO.
     *
     * @param IO DOCUMENT ME!
     */
    public void mayPushButtons(InOutput IO) {
        IO.setSignal(DstF, true);

        if (PushesBoth > 0) {
            IO.setSignal(0, true);
            IO.setSignal(World.getNoF() - 1, true);
            PushesBoth--;
        }
    }

    /**
     * Called by the {@link World} every time the {@code Passenger} may
     * decide to get into a the given {@link Car} C with the given indicator
     * {@link InOutput} IO.
     *
     * @param CarIO DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean decideGetIn(InOutput CarIO) {
        //		return true;
        return ((CarIO.getSignal(getDstF())) &&
        (PassengerRandom.nextLong() > 0.75));
    }

    /**
     * Called by the {@link World}, this function switches the {@code
     * Passenger} from {@code GETTING_IN} to {@code TRAVELLING}
     */
    public void finishGetIn() {
        State = TRAVELLING;
        Progress = 0;
    }

    /**
     * Called by the {@link World}, this function switches the {@code
     * Passenger} from {@code GETTING_OUT} to {@code FINISHED}
     */
    public void finishGetOut() {
        State = FINISHED;
        Progress = 0;
        TravelCar = null;
    }

    /**
     * Called by the {@link World}, this function switches the {@code
     * Passenger} from {@code WAITING} to {@code GETTING_IN}
     *
     * @param C DOCUMENT ME!
     */
    public void startGetIn(Car C) {
        if (AngerLevel > 0) {
            AngerLevel--;
        }

        State = GETTING_IN;
        Ticks_Waited = Progress;
        Progress = 0;
        TravelCar = C;
    }

    /**
     * Called by the {@link World}, this function switches the {@code
     * Passenger} from {@code TRAVELLING} to {@code GETTING_OUT}
     */
    public void startGetOut() {
        State = GETTING_OUT;
        Ticks_Travelled = Progress;
        Progress = 0;
    }

    /**
     * {@link java.lang.Object}
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return (Desc);
    }
}
