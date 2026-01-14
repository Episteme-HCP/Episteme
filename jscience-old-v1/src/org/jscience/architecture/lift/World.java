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

import org.jscience.architecture.lift.ca.CA;
import org.jscience.architecture.lift.util.EvenSimplerFormatter;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This foundation class is the {@code World} that contains almost everything
 * else. GUIs are not part of the {@code World} so that {@code JLESA} can work
 * in a command-line.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:52 $
 */
public class World extends Tickable {
    /** Maximum number of Ticks before stopping. */
    public static int MaximumNumberOfTicks = 0;

    /**
     * DOCUMENT ME!
     */
    private static Car[] Cars = new Car[0];

    /**
     * DOCUMENT ME!
     */
    private static boolean[] CarsBlocked = new boolean[0];

    /**
     * DOCUMENT ME!
     */
    private static int ConfigID = -1;

    /**
     * DOCUMENT ME!
     */
    private static InOutput[] Inputs = new InOutput[0];

    /**
     * DOCUMENT ME!
     */
    private static int MaxFloor = 0;

    /**
     * DOCUMENT ME!
     */
    private static int MinFloor = 0;

    /**
     * DOCUMENT ME!
     */
    private static CA MyCA = null;

    /**
     * DOCUMENT ME!
     */
    private final static Logger MyLogger;

    /**
     * DOCUMENT ME!
     */
    private static int NoF = 0;

    /**
     * DOCUMENT ME!
     */
    private static InOutput[][] Outputs = new InOutput[0][0];

    /**
     * DOCUMENT ME!
     */
    private static PassengerGenerator[] PGs = new PassengerGenerator[0];

    /**
     * DOCUMENT ME!
     */
    private static PassengerProcessor[] PPs = new PassengerProcessor[0];

    /**
     * DOCUMENT ME!
     */
    private static ArrayList[] PassengerLists = new ArrayList[0];

    /**
     * DOCUMENT ME!
     */
    private static ArrayList Passengers = new ArrayList();

    /**
     * DOCUMENT ME!
     */
    private static int TotalTicks = 0;

    /**
     * DOCUMENT ME!
     */
    private static int WorldCount = 0;

    /**
     * DOCUMENT ME!
     */
    private static boolean PreparedToDie = false;

    static {
        MyLogger = Logger.getAnonymousLogger();
        MyLogger.setUseParentHandlers(false);

        try {
            Handler MyFileHandler = new java.util.logging.FileHandler(
                    "data/jlesa/log.log");
            MyFileHandler.setFormatter(new EvenSimplerFormatter());
            MyLogger.addHandler(MyFileHandler);
            MyLogger.setLevel(Level.INFO);
        } catch (java.io.IOException IOE) {
            System.out.println(IOE.toString());
        }
    }

/**
     * Constructor a new {@code World}, with {@code (NewMinFloor,MaxFloorm-1)}
     */
    public World(int MinFloor, int MaxFloor) {
        this(MinFloor, MaxFloor, -1);
    }

/**
     * Constructor a new {@code World}.
     *
     * @param MinFloor     Lowest floor
     * @param MaxFloor     Highest floor
     * @param ConfigFileID The ID of the translation/config file to use
     */
    public World(int MinFloor, int MaxFloor, int ConfigFileID) {
        if (MinFloor >= MaxFloor) {
            throw new RuntimeException("ERROR:World:getMinF()>=getMaxF()");
        }

        if (WorldCount > 0) {
            throw new RuntimeException("ERROR:World:" + WorldCount +
                " piece(s) already extisting");
        }

        WorldCount++;
        this.MinFloor = MinFloor;
        this.MaxFloor = MaxFloor;
        NoF = MaxFloor - MinFloor + 1;
        Inputs = new InOutput[NoF];
        PassengerLists = new ArrayList[NoF];

        for (int i = 0; i < NoF; i++) {
            Inputs[i] = new InOutput(i, NoF);
            PassengerLists[i] = new ArrayList();
        }

        if (ConfigFileID != -1) {
            ConfigID = ConfigFileID;
        }
    }

    /**
     * Returns {@code true} if the {@code C}{@link Car} is blocked (due
     * to {@link Passenger}s).
     *
     * @param C DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public static boolean isBlocked(Car C) {
        for (int i = 0; i < Cars.length; i++) {
            if (Cars[i] == C) {
                return (CarsBlocked[i]);
            }
        }

        throw new RuntimeException("Invalid argument! (" + C.getFullName() +
            ")");
    }

    /**
     * Returns the current {@link CA}.
     *
     * @return DOCUMENT ME!
     */
    public static CA getCA() {
        return (MyCA);
    }

    /**
     * Returns the {@code Index}th {@link Car}.
     *
     * @param Index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Car getCar(int Index) {
        return (Cars[Index]);
    }

    /**
     * Returns all {@link Car}s in an Array.
     *
     * @return DOCUMENT ME!
     */
    public static Car[] getCars() {
        return (Cars);
    }

    /**
     * Returns the {@code Index} of the {@code C}{@link Car}.
     *
     * @param C DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getIndexOfCar(Car C) {
        for (int i = 0; i < Cars.length; i++) {
            if (C == Cars[i]) {
                return (i);
            }
        }

        return (-1);
    }

    /**
     * Returns the {@code Index}th {@code Input} ( which is really a
     * {@link InOutput}).
     *
     * @param Index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static InOutput getInput(int Index) {
        return (Inputs[Index]);
    }

    /**
     * Returns all the {@code Input}s ( which are really {@link
     * InOutput}s).
     *
     * @return DOCUMENT ME!
     */
    public static InOutput[] getInputs() {
        return (Inputs);
    }

    /**
     * Returns the {@link java.util.logging.Logger} of the {@code
     * World}
     *
     * @return DOCUMENT ME!
     */
    public static Logger getLogger() {
        return (MyLogger);
    }

    /**
     * Returns the highest floor
     *
     * @return DOCUMENT ME!
     */
    public static int getMaxF() {
        return (MaxFloor);
    }

    /**
     * Returns the lowest floor
     *
     * @return DOCUMENT ME!
     */
    public static int getMinF() {
        return (MinFloor);
    }

    /* Tickable */
    public String getName() {
        return ("World");
    }

    /**
     * Returns the number of {@link Car}s.
     *
     * @return DOCUMENT ME!
     */
    public static int getNoC() {
        return (Cars.length);
    }

    /**
     * Returns the number of floors.
     *
     * @return DOCUMENT ME!
     */
    public static int getNoF() {
        return (NoF);
    }

    /**
     * Returns the number of {@link Passenger}s on floor {@code Floor}.
     *
     * @param Floor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getNumberOfPassengers(int Floor) {
        return (PassengerLists[Floor].size());
    }

    /**
     * Returns all the {@code Output}s (which are really {@link
     * InOutput}s) on the {@code AbsFloor}th floor
     *
     * @param AbsFloor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static InOutput[] getOutputs(int AbsFloor) {
        return (Outputs[AbsFloor]);
    }

    /**
     * Returns the destination floors of the {@link Passenger}s in
     * {@link Car}{@code C}
     *
     * @param C DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int[] getPassengerDstFloorsInCar(Car C) {
        int[] RetVal = new int[C.getCapacity()];
        int Index = 0;

        for (int i = 0; i < RetVal.length; i++) {
            RetVal[i] = 0;
        }

        for (int i = 0; i < Passengers.size(); i++) {
            Passenger CP = (Passenger) Passengers.get(i);

            if (CP.TravelCar == C) {
                RetVal[Index] = toRelFloor(CP.getDstF());
                Index++;
            }
        }

        return (RetVal);
    }

    /**
     * Returns the {@link Passenger}s on floor {@code Floor}.
     *
     * @param Floor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ArrayList getPassengers(int Floor) {
        return (PassengerLists[Floor]);
    }

    /**
     * Returns the {@link Passenger}s getting out on floor {@code
     * Floor}.
     *
     * @param AbsFloor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ArrayList getPassengersGettingOutAtFloor(int AbsFloor) {
        ArrayList RetVal = new ArrayList();
        ArrayList PiC = null;

        for (int i = 0; i < Cars.length; i++) {
            if ((Cars[i].getState() == Car.WAITING) &&
                    (Cars[i].getCrtF() == AbsFloor)) {
                PiC = getPassengersInCar(Cars[i]);

                for (int k = 0; k < PiC.size(); k++) {
                    Passenger CurPas = (Passenger) PiC.get(k);

                    if (CurPas.getState() == Passenger.GETTING_OUT) {
                        RetVal.add(CurPas);
                    }
                }
            }
        }

        return (RetVal);
    }

    /**
     * Returns the {@link Passenger}s in {@link Car}{@code C}.
     *
     * @param C DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ArrayList getPassengersInCar(Car C) {
        ArrayList RetVal = new ArrayList();

        for (int i = 0; i < Passengers.size(); i++) {
            Passenger CP = (Passenger) Passengers.get(i);

            if (CP.TravelCar == C) {
                RetVal.add(CP);
            }
        }

        return (RetVal);
    }

    /**
     * Returns the total number of ticks so long.
     *
     * @return DOCUMENT ME!
     */
    public static int getTotalTicks() {
        return (TotalTicks);
    }

    /* Tickable */
    public String getVersion() {
        return ("0.1.0");
    }

    /* Tickable */
    public void Tick() {
        Tick(1);
    }

    /**
     * Equivalent of {@code TN} pieces of {@code Tick()}
     *
     * @param TN DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean Tick(int TN) {
        long ST = System.currentTimeMillis();

        for (int j = 0; j < TN; j++) {
            if ((MaximumNumberOfTicks != 0) &&
                    (getTotalTicks() > MaximumNumberOfTicks)) {
                if (!PreparedToDie) {
                    System.out.print("Notifying PassengerProcessors...");

                    for (int i = 0; i < PPs.length; i++) {
                        PPs[i].prepareToDie();
                    }

                    System.out.println("Done!");
                    PreparedToDie = true;
                } else {
                    System.err.println("No more ticks are allowed!");
                }

                return false;
            }

            for (int i = 0; i < PPs.length; i++) {
                PPs[i].Tick();
            }

            for (int c = 0; c < Cars.length; c++) {
                for (int f = 0; f < NoF; f++) {
                    for (int tf = 0; tf < NoF; tf++) {
                        Outputs[f][c].setSignal(tf, MyCA.goes(f, tf, c));
                    }
                }
            }

            for (int i = 0; i < Cars.length; i++) {
                Cars[i].Tick();
            }

            for (int i = 0; i < Passengers.size(); i++) {
                ((Passenger) Passengers.get(i)).Tick();
            }

            for (int i = 0; i < Cars.length; i++) {
                Car CC = Cars[i];

                if (CC.getState() == Car.WAITING) {
                    doInsOuts(CC);
                    deleteCorrespongingCalls(CC);
                }

                if (CC.getState() == Car.OPENING) {
                    deleteCorrespongingCalls(CC);
                }
            }

            for (int i = 0; i < PGs.length; i++) {
                PGs[i].Tick();

                ArrayList AL = PGs[i].getGeneratedPassengers();

                while (AL.size() > 0) {
                    Passenger CP = (Passenger) AL.get(0);
                    add(CP);
                    World.getLogger()
                         .log(Level.INFO, "Passenger created",
                        new Object[] {
                            new Integer(World.getTotalTicks()),
                            new Long(CP.getID()), new Integer(CP.getSrcF()),
                            new Integer(CP.getDstF())
                        });

                    for (int k = 0; k < PPs.length; k++) {
                        PPs[k].created(CP);
                    }

                    AL.remove(0);
                }
            }

            letPassengersToSignal();
            MyCA.tick();
            TotalTicks++;
        }

        long FT = System.currentTimeMillis();
        double DiS = FT - ST;

        if (DiS > 250) {
            DiS /= 1000;
            System.out.println("" + TN + " ticks made in " + DiS +
                " seconds. (" + (int) ((double) TN / DiS) + " TpS)");
        }

        return true;
    }

    /**
     * Adds the {@code TP}{@link Passenger} to the {@code World}.
     *
     * @param TP DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public static void add(Passenger TP) {
        if ((TP.getCrtF() < toAbsFloor(MinFloor)) ||
                (TP.getCrtF() > toAbsFloor(MaxFloor))) {
            throw new RuntimeException("ERROR:World:Passenger #" + TP.getID() +
                ".getCrtFlor()==" + toRelFloor(TP.getCrtF()) +
                " not in [MinFloor==" + MinFloor + ", MaxFloor==" + MaxFloor +
                "]");
        }

        ArrayList PAL = PassengerLists[TP.getCrtF()];

        for (int i = 0; i < PAL.size(); i++) {
            if ((Passenger) PAL.get(i) == TP) {
                throw new RuntimeException("ERROR:World:Passenger #" +
                    TP.getID() + " is already registered.");
            }
        }

        PAL.add(TP);
        Passengers.add(TP);

        //      Inputs[TP.getSrcF()].setSignal(TP.getDstF(), true);
    }

    /**
     * Adds the {@code TC}{@link Car} to the {@code World}.
     *
     * @param TC DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public void add(Car TC) {
        if ((TC.getCrtF() < MinFloor) || (TC.getCrtF() > MaxFloor)) {
            throw new RuntimeException("ERROR:World:Car #" + TC.getID() +
                ".getCrtFlor()==" + TC.getCrtF() + " not in [MinFloor==" +
                MinFloor + ", MaxFloor==" + MaxFloor + "]");
        }

        int CL = Cars.length;

        for (int i = 0; i < CL; i++) {
            if (Cars[i] == TC) {
                throw new RuntimeException("ERROR:World:Car #" + TC.getID() +
                    " is already registered.");
            }
        }

        Car[] NCars = new Car[CL + 1];
        boolean[] NCarsBlocked = new boolean[CL + 1];
        System.arraycopy(Cars, 0, NCars, 0, CL);
        System.arraycopy(CarsBlocked, 0, NCarsBlocked, 0, CL);
        NCars[CL] = TC;
        NCarsBlocked[CL] = false;
        Cars = NCars;
        CarsBlocked = NCarsBlocked;
        Outputs = new InOutput[NoF][CL + 1];

        for (int i = 0; i < Outputs.length; i++) {
            for (int j = 0; j < Outputs[i].length; j++) {
                Outputs[i][j] = new InOutput(i, Outputs.length);
            }
        }
    }

    /**
     * Adds the {@code PG}{@link PassengerGenerator} to the {@code
     * World}.
     *
     * @param PG DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public void add(PassengerGenerator PG) {
        if (PGs == null) {
            PGs = new PassengerGenerator[1];
            PGs[0] = PG;
        }

        int PGsL = PGs.length;
        PassengerGenerator[] NPGs = new PassengerGenerator[PGsL + 1];

        for (int i = 0; i < PGsL; i++) {
            if (PGs[i] == PG) {
                throw new RuntimeException(
                    "ERROR:World:Object is already added to array.");
            }
        }

        System.arraycopy(PGs, 0, NPGs, 0, PGsL);
        NPGs[PGsL] = PG;
        PGs = NPGs;
    }

    /**
     * Adds the {@code PP}{@link PassengerProcessor} to the {@code
     * World}.
     *
     * @param PP DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public void add(PassengerProcessor PP) {
        if (PPs == null) {
            PPs = new PassengerProcessor[1];
            PPs[0] = PP;
        }

        int PPsL = PPs.length;
        PassengerProcessor[] NPPs = new PassengerProcessor[PPsL + 1];

        for (int i = 0; i < PPsL; i++) {
            if (PPs[i] == PP) {
                throw new RuntimeException(
                    "ERROR:World:Object is already added to array.");
            }
        }

        System.arraycopy(PPs, 0, NPPs, 0, PPsL);
        NPPs[PPsL] = PP;
        PPs = NPPs;
    }

    /**
     * Deletes all calls that are available on {@code C}{@link Car}'s
     * indicator. This usually happens when a {@link Car} arrives to a floor.
     *
     * @param C DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public static void deleteCorrespongingCalls(Car C) {
        if (!((C.getState() == Car.WAITING) || (C.getState() == Car.OPENING))) {
            throw new RuntimeException("ERROR: deleteCorrespongingCalls() 001");
        }

        int cf = C.getCrtF();
        int cn = getIndexOfCar(C);

        for (int tf = 0; tf < getNoF(); tf++) {
            if (Outputs[cf][cn].getSignal(tf)) {
                Inputs[cf].setSignal(tf, false);
            }
        }
    }

    /**
     * Lets the {@link Passenger}s signal.
     */
    public static void letPassengersToSignal() {
        for (int f = 0; f < getNoF(); f++) {
            ArrayList PAL = getPassengers(f);

            for (int pc = 0; pc < PAL.size(); pc++) {
                Passenger CP = (Passenger) PAL.get(pc);

                if (CP.getState() == Passenger.WAITING) {
                    CP.mayPushButtons(Inputs[f]);
                }
            }
        }
    }

    /**
     * Description of the Method
     *
     * @param TP Description of the Parameter
     *
     * @throws RuntimeException DOCUMENT ME!
     */

    /*
     *  public static void refreshSignalOnFloor(int AbsFloor) {
     *  for (int i = 0; i < NoF; i++) {
     *  Inputs[AbsFloor].setSignal(i, false);
     *  }
     *  ArrayList POTF  = PassengerLists[AbsFloor];
     *  for (int i = 0; i < POTF.size(); i++) {
     *  Passenger CP  = (Passenger) POTF.get(i);
     *  Inputs[AbsFloor].setSignal(CP.getDstF(), true);
     *  }
     *  }
     */
    /**
     * Removes the finished {@code TP}{@link Passenger} from the {@code
     * World} after processing it through all added {@link
     * PassengerProcessor}s.
     *
     * @param TP DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public static void remove(Passenger TP) {
        if (TP.getState() != TP.FINISHED) {
            throw new RuntimeException("Passenger #" + TP.getID() +
                " is not in state \"FINISHED\", it cannot be removed!");
        }

        for (int i = 0; i < PassengerLists.length; i++) {
            for (int j = 0; j < PassengerLists[i].size(); j++) {
                if (TP == (Passenger) PassengerLists[i].get(j)) {
                    throw new RuntimeException("Passenger #" + TP.getID() +
                        " is standing on Floor #" + i +
                        " and is in state \"FINISHED\", it cannot be removed!");
                }
            }
        }

        for (int j = 0; j < Passengers.size(); j++) {
            if (TP == (Passenger) Passengers.get(j)) {
                for (int k = 0; k < PPs.length; k++) {
                    PPs[k].process(TP);
                }

                Passengers.remove(j);
                j--;
            }
        }
    }

    /**
     * Converts {@code RelFloor} to {@code AbsFloor}
     *
     * @param RelFloor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int toAbsFloor(int RelFloor) {
        return (RelFloor - MinFloor);
    }

    /**
     * Converts {@code AbsFloor} to {@code RelFloor}
     *
     * @param AbsFloor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int toRelFloor(int AbsFloor) {
        return (AbsFloor + MinFloor);
    }

    /**
     * Sets the {@code World}'s {@link CA} to {@code NewCA}.
     *
     * @param NewCA DOCUMENT ME!
     */
    protected void setCA(CA NewCA) {
        MyCA = NewCA;
    }

    /**
     * Initiates get-ins and get-outs of {@link Passenger}s to/from
     * {@link Car}s.
     *
     * @param CC DOCUMENT ME!
     */
    private void doInsOuts(Car CC) {
        ArrayList CPL;
        Passenger CP;
        int CI = getIndexOfCar(CC);
        CPL = getPassengersInCar(CC);

        for (int p = 0; p < CPL.size(); p++) {
            CP = (Passenger) CPL.get(p);

            if (CP.getState() == CP.GETTING_OUT) {
                if (CP.Progress == CP.GetOutTime) {
                    CarsBlocked[CI] = false;
                    World.getLogger()
                         .log(Level.INFO, "Passenger finished",
                        new Object[] {
                            new Integer(World.getTotalTicks()),
                            new Long(CP.getID()),
                            new Integer(CP.getTicksWaited()),
                            new Integer(CP.getTicksTravelled())
                        });
                    CP.finishGetOut();
                    CC.decreaseNumberOfPassangers();
                    remove(CP);
                }
            }
        }

        CPL = getPassengers(CC.getCrtF());

        for (int p = 0; p < CPL.size(); p++) {
            CP = (Passenger) CPL.get(p);

            if ((CP.getState() == CP.GETTING_IN) &&
                    (CP.Progress == CP.GetInTime) && (CP.TravelCar == CC)) {
                CarsBlocked[CI] = false;
                World.getLogger()
                     .log(Level.INFO, "Passenger finished getting in",
                    new Object[] {
                        new Integer(World.getTotalTicks()), new Long(CP.getID()),
                        new Long(CC.getID())
                    });
                CP.finishGetIn();
                CC.increaseNumberOfPassangers();
                MyCA.issueCommand(CC, CP.getDstF());
                CPL.remove(CP);
                p--;
            }
        }

        CPL = getPassengersInCar(CC);

        for (int p = 0; p < CPL.size(); p++) {
            CP = (Passenger) CPL.get(p);

            if ((CP.getDstF() == CC.getCrtF()) &&
                    (CP.getState() == CP.TRAVELLING) && (!CarsBlocked[CI])) {
                CarsBlocked[CI] = true;
                World.getLogger()
                     .log(Level.INFO, "Passenger started getting out",
                    new Object[] {
                        new Integer(World.getTotalTicks()), new Long(CP.getID()),
                        new Long(CC.getID())
                    });
                CP.startGetOut();
            }
        }

        CPL = getPassengers(CC.getCrtF());

        for (int p = 0; p < CPL.size(); p++) {
            CP = (Passenger) CPL.get(p);

            if ((CP.getState() == CP.WAITING) && (!CC.isFull()) &&
                    (!CarsBlocked[CI])) {
                InOutput CIO = (getOutputs(CC.getCrtF()))[CI];

                if (CP.decideGetIn(CIO)) {
                    //            if (CIO.getSignal(CP.getDstF())) {
                    CarsBlocked[CI] = true;
                    World.getLogger()
                         .log(Level.INFO, "Passenger started getting in",
                        new Object[] {
                            new Integer(World.getTotalTicks()),
                            new Long(CP.getID()), new Long(CC.getID())
                        });
                    CP.startGetIn(CC);
                }
            }
        }
    }
}
