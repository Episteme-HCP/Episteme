package org.jscience.architecture.lift;

/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This is a fairly simple implementation of a load-dependend {@link
 * KinematicModel}. The time needed to reach {@code Floor #I} from {@code
 * Floor #J} is (the difference of {@code I} and {@code J} is {@code K}) is
 * {@code BaseTime+KFloorFligthTime}, where {@code BaseTime} and {@code
 * FloorFlightTime} may depend on the number of {@link Passenger}s in the
 * {@link Car}.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:51 $
 *
 * @see LoadSpeed
 */
public class LoadDependentKinematicModel implements KinematicModel {
    /**
     * DOCUMENT ME!
     */
    private int BT;

    /**
     * DOCUMENT ME!
     */
    private LoadSpeed[] Config;

    /**
     * DOCUMENT ME!
     */
    private int DCT;

    /**
     * DOCUMENT ME!
     */
    private int DOT;

    /**
     * DOCUMENT ME!
     */
    private int[] LoadTables;

    /**
     * DOCUMENT ME!
     */
    private int[][] MoveTimes = null;

    /**
     * DOCUMENT ME!
     */
    private int prevNoP = 0;

/**
     * Constructor
     *
     * @param Config Configuration tables in the form of a {@link LoadSpeed} array.
     */
    public LoadDependentKinematicModel(LoadSpeed[] Config) {
        int MinLoad = Integer.MAX_VALUE;
        int MaxLoad = Integer.MIN_VALUE;

        for (int i = 0; i < Config.length; i++) {
            MinLoad = (Config[i].FromLoad < MinLoad) ? Config[i].FromLoad
                                                     : MinLoad;
            MaxLoad = (Config[i].ToLoad > MaxLoad) ? Config[i].ToLoad : MaxLoad;
        }

        if ((MaxLoad < MinLoad) || (MinLoad != 0)) {
            throw new RuntimeException(
                "Illegal parameter exception in LoadDependentKinematicModel!");
        }

        LoadTables = new int[MaxLoad + 1];

        for (int i = 0; i <= MaxLoad; i++) {
            LoadTables[i] = -1;

            for (int j = 0; j < Config.length; j++) {
                if ((Config[j].FromLoad <= i) && (Config[j].ToLoad >= i)) {
                    if (LoadTables[i] != -1) {
                        throw new RuntimeException(
                            "Illegal parameter exception in LoadDependentKinematicModel!");
                    } else {
                        LoadTables[i] = j;
                    }
                }
            }

            if (LoadTables[i] == -1) {
                throw new RuntimeException(
                    "Illegal parameter exception in LoadDependentKinematicModel!");
            }
        }

        this.Config = Config;
        reconfigure(0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLoadTablesLength() {
        return (LoadTables.length);
    }

    /* KinematicModel */
    public int getDoorCloseTime(int AbsFloor, Car C) {
        if (C.getNoPs() != prevNoP) {
            reconfigure(C.getNoPs());
        }

        return (DCT);
    }

    /* KinematicModel */
    public int getDoorOpenTime(int AbsFloor, Car C) {
        if (C.getNoPs() != prevNoP) {
            reconfigure(C.getNoPs());
        }

        return (DOT);
    }

    /* KinematicModel */
    public double getProgress(Car C) {
        if (C.isMoving()) {
            int NextFloor = (C.getSrcF() < C.getDstF()) ? (C.getCrtF() + 1)
                                                        : (C.getCrtF() - 1);
            int Whole = MoveTimes[C.getSrcF()][NextFloor] -
                MoveTimes[C.getSrcF()][C.getCrtF()];
            int Part = C.getProgress() - MoveTimes[C.getSrcF()][C.getCrtF()];

            return ((double) Part / (double) Whole);
        } else {
            switch (C.getState()) {
            case Car.CLOSING:
                return ((double) C.getProgress() / (double) DCT);

            case Car.OPENING:
                return (1 - ((double) C.getProgress() / (double) DOT));

            default:
                throw new RuntimeException("Not implemented!");
            }
        }
    }

    /* KinematicModel */
    public void dstFChanged(Car C) {
        ;
    }

    /* KinematicModel */
    public boolean mayStopAt(Car C, int AbsFloor) {
        return (C.getProgress() <= (MoveTimes[C.getSrcF()][AbsFloor] - BT));
    }

    /* KinematicModel */
    public void tick(SimulatedCar C) {
        if (C.getNoPs() != prevNoP) {
            reconfigure(C.getNoPs());
        }

        if (C.getProgress() >= MoveTimes[C.getSrcF()][C.getDstF()]) {
            if (C.getProgress() > MoveTimes[C.getSrcF()][C.getDstF()]) {
                System.out.println("WARNING: " + C.getProgress() + " " +
                    MoveTimes[C.getSrcF()][C.getDstF()] + " " + C.getNoPs() +
                    " " + C.getSrcF() + " " + C.getDstF());
            }

            C.arrived();
        } else {
            int NextFloor = (C.getSrcF() < C.getDstF()) ? (C.getCrtF() + 1)
                                                        : (C.getCrtF() - 1);

            if (C.getProgress() >= MoveTimes[C.getSrcF()][NextFloor]) {
                C.CrtFReached(NextFloor);
            }
        }
    }

    /**
     * Used to reload {@link SpeedLoad} tables whenever the number of
     * {@link Passenger}s changed in the {@link Car}.
     *
     * @param NoP DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    private void reconfigure(int NoP) {
        if (NoP > LoadTables.length) {
            throw new RuntimeException("Impossible");
        }

        /*
          System.err.print(NoP+" "+LoadTable.length+":\t");
          for (int i=0; i<LoadTable.length;i++) {
              System.err.print(LoadTable[i]+"  ");
          }
          System.err.println();
          */
        int NoF = World.getNoF();
        MoveTimes = new int[NoF][NoF];

        int D = 0;
        DCT = Config[LoadTables[NoP]].DoorCloseTime;
        DOT = Config[LoadTables[NoP]].DoorOpenTime;

        int BaseTime = Config[LoadTables[NoP]].BaseTime;
        int FloorFligthTime = Config[LoadTables[NoP]].FloorFligthTime;

        for (int i = 0; i < NoF; i++) {
            for (int j = 0; j < NoF; j++) {
                D = Math.abs(i - j);
                MoveTimes[i][j] = ((D == 0) ? 0 : ((D * FloorFligthTime) +
                    BaseTime));
            }
        }

        prevNoP = NoP;
    }
}
