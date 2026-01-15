package org.jscience.architecture.lift;

/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This is a sample (simple and effective) {@link KinematicModel}. It is
 * simple: The time needed to reach Floor {@code I} from Floor {@code J}
 * ({@code K} is the difference of {@code I} and {@code J}) is {@code
 * BaseTime+KFloorFligthTime}.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:52 $
 *
 * @see LoadSpeed
 */
public class SimpleKinematicModel implements KinematicModel {
    /**
     * DOCUMENT ME!
     */
    private int BT;

    /**
     * DOCUMENT ME!
     */
    private int[][] MoveTimes = null;

    /**
     * DOCUMENT ME!
     */
    private int DCT;

    /**
     * DOCUMENT ME!
     */
    private int DOT;

/**
     * Constructor
     *
     * @param BaseTime        See the class description
     * @param FloorFligthTime See the class description
     * @param DoorOpenTime    Time needed to open the door
     * @param DoorCloseTime   Time needed to close the door
     */
    public SimpleKinematicModel(int BaseTime, int FloorFligthTime,
        int DoorOpenTime, int DoorCloseTime) {
        int NoF = World.getNoF();
        int D = 0;

        DOT = DoorOpenTime;
        DCT = DoorCloseTime;

        BT = BaseTime;
        MoveTimes = new int[NoF][];

        for (int i = 0; i < NoF; i++) {
            MoveTimes[i] = new int[NoF];

            for (int j = 0; j < NoF; j++) {
                D = Math.abs(i - j);
                MoveTimes[i][j] = ((D == 0) ? 0 : ((D * FloorFligthTime) +
                    BaseTime));
            }
        }
    }

    /* KinematicModel */
    public void dstFChanged(Car C) {
        ;
    }

    /* KinematicModel */
    public int getDoorCloseTime(int AbsFloor, Car C) {
        return (DCT);
    }

    /* KinematicModel */
    public int getDoorOpenTime(int AbsFloor, Car C) {
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

            return (((double) Part) / ((double) Whole));
        } else {
            switch (C.getState()) {
            case Car.CLOSING:
                return (((double) C.getProgress()) / ((double) DCT));

            case Car.OPENING:
                return (1 - (((double) C.getProgress()) / ((double) DOT)));

            default:
                throw new RuntimeException("Not implemented!");
            }
        }
    }

    /* KinematicModel */
    public boolean mayStopAt(Car C, int AbsFloor) {
        return (C.getProgress() <= (MoveTimes[C.getSrcF()][AbsFloor] - BT));
    }

    /* KinematicModel */
    public void tick(SimulatedCar C) {
        if (C.getProgress() == MoveTimes[C.getSrcF()][C.getDstF()]) {
            C.arrived();
        } else {
            int NextFloor = (C.getSrcF() < C.getDstF()) ? (C.getCrtF() + 1)
                                                        : (C.getCrtF() - 1);

            if (C.getProgress() >= MoveTimes[C.getSrcF()][NextFloor]) {
                C.CrtFReached(NextFloor);
            }
        }
    }
}
