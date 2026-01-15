package org.jscience.architecture.lift.ca;

import org.jscience.architecture.lift.Car;
import org.jscience.architecture.lift.InOutput;
import org.jscience.architecture.lift.World;
import org.jscience.architecture.lift.gui.DynZoneCAGUI;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This is nice Dynamic Zoning {@link CA} that uses a {@link SimpleALLCA} for
 * each {@link Car} and has a pretty informative GUI.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:53 $
 */
public class DynZoneCA implements CA {
    /**
     * DOCUMENT ME!
     */
    private final boolean[] DownCalls;

    /**
     * DOCUMENT ME!
     */
    private final boolean[] UpCalls;

    /**
     * {@code DownMask[i]==n} if and only if the down calls on floor
     * {@code i} are allocated to {@link SimpleALLCA}{@code [n]}
     */
    public int[] DownMasks;

    /**
     * {@code DownMask[i]==n} if and only if the up calls on floor
     * {@code i} are allocated to {@link SimpleALLCA}{@code [n]}
     */
    public int[] UpMasks;

    /**
     * DOCUMENT ME!
     */
    private int NoC = -1;

    /**
     * DOCUMENT ME!
     */
    private int NoF;

    /**
     * DOCUMENT ME!
     */
    private boolean GUI = false;

    /**
     * DOCUMENT ME!
     */
    private DynZoneCAGUI G;

    /**
     * DOCUMENT ME!
     */
    private SimpleALLCA[] ALLCAs = null;

    /**
     * DOCUMENT ME!
     */
    private Car[] Cars = null;

/**
     * Constructor
     *
     * @param hasGUI {@code true} if a GUI is required
     */
    public DynZoneCA(boolean hasGUI) {
        Cars = World.getCars();
        NoC = Cars.length;
        NoF = World.getNoF();
        UpCalls = new boolean[NoF];
        DownCalls = new boolean[NoF];
        UpMasks = new int[NoF];
        DownMasks = new int[NoF];
        ALLCAs = new SimpleALLCA[NoC];

        for (int i = 0; i < NoC; i++) {
            ALLCAs[i] = new SimpleALLCA(Cars[i], 0, 20000, 5);
        }

        GUI = hasGUI;

        if (GUI) {
            G = new DynZoneCAGUI(this);
        }
    }

    /**
     * Forwards the request to the corresponding {@link SimpleALLCA}
     *
     * @param From DOCUMENT ME!
     * @param To DOCUMENT ME!
     * @param CarIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @see CA
     * @see SimpleALLCA
     */
    public boolean goes(int From, int To, int CarIndex) {
        return (ALLCAs[CarIndex].goes(From, To, UpCalls, DownCalls));
    }

    /**
     * Forwards the request to the corresponding {@link SimpleALLCA}
     *
     * @param CC DOCUMENT ME!
     * @param AbsFloor DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     *
     * @see CA
     * @see SimpleALLCA
     */
    public void issueCommand(Car CC, int AbsFloor) {
        for (int i = 0; i < NoC; i++) {
            if (Cars[i].equals(CC)) {
                ALLCAs[i].issueCommand(AbsFloor);

                return;
            }
        }

        throw new RuntimeException("There is no such Car! Error #442");
    }

    /**
     * {@link org.jscience.architecture.lift.Tickable}
     */
    public void tick() {
        for (int i = 0; i < NoF; i++) {
            InOutput IOP = World.getInput(i);

            UpCalls[i] = IOP.getUp();
            DownCalls[i] = IOP.getDown();
        }

        int MovingIndex = -1;

        for (int i = 0; i < NoC; i++) {
            if (ALLCAs[i].getDirection() != SimpleALLCA.BOTH_WAY) {
                MovingIndex = i;

                break;
            }
        }

        if (MovingIndex == -1) {
            ALLCAs[0].Direction = SimpleALLCA.UP_WAY;
            MovingIndex = 0;
        }

        int LastCar = MovingIndex;

        for (int i = 0; i < (2 * NoF); i++) {
            int CF = unCycle(i, MovingIndex);
            int CD = unCycleDir(i, MovingIndex);

            for (int j = 0; j < NoC; j++) {
                if (Cars[j].getPossibleNextFloor() == CF) {
                    if ((getDirection(j) == -1) || (getDirection(j) == CD)) {
                        LastCar = j;
                    }
                }
            }

            if ((CF >= 0) && (CF < NoF)) {
                for (int j = 0; j < NoC; j++) {
                    if (CD == SimpleALLCA.UP_WAY) {
                        UpMasks[CF] = LastCar;
                    } else {
                        DownMasks[CF] = LastCar;
                    }
                }
            }
        }

        UpMasks[NoF - 1] = -1;
        DownMasks[0] = -1;

        boolean[] MaskedUpCalls = new boolean[NoF];
        boolean[] MaskedDownCalls = new boolean[NoF];

        for (int i = 0; i < NoC; i++) {
            for (int j = 0; j < NoF; j++) {
                MaskedUpCalls[j] = ((UpCalls[j]) && (UpMasks[j] == i));
                MaskedDownCalls[j] = ((DownCalls[j]) && (DownMasks[j] == i));
            }

            ALLCAs[i].tick(MaskedUpCalls, MaskedDownCalls);
        }

        if (GUI) {
            G.refresh();
        }
    }

    /**
     * Returns the {@code Direction} of the {@code CarIndex}th {@link
     * org.jscience.architecture.lift.Car}
     *
     * @param CarIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDirection(int CarIndex) {
        if (Cars[CarIndex].isMoving()) {
            return ((Cars[CarIndex].getState() == Car.GOING_UP) ? Car.GOING_UP
                                                                : Car.GOING_DOWN);
        } else {
            if (ALLCAs[CarIndex].getDirection() != ALLCAs[CarIndex].BOTH_WAY) {
                return ((ALLCAs[CarIndex].getDirection() == ALLCAs[CarIndex].UP_WAY)
                ? Car.GOING_UP : Car.GOING_DOWN);
            }
        }

        return (-1);
    }

    /**
     * Uncycles (creates a linear distance from a cyclic distance) the
     * distance of {@code AbsFloor} from the {@code CarIndex}th {@link Car}.
     *
     * @param AbsFloor DOCUMENT ME!
     * @param CarIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    private int unCycle(int AbsFloor, int CarIndex) {
        if (ALLCAs[CarIndex].getDirection() == ALLCAs[CarIndex].BOTH_WAY) {
            throw new RuntimeException("Not moving! # 1251");
        }

        int CF = Cars[CarIndex].getCrtF();

        if (ALLCAs[CarIndex].getDirection() == ALLCAs[CarIndex].UP_WAY) {
            AbsFloor += CF;

            if (AbsFloor < (NoF - 1)) {
                return (AbsFloor);
            }

            AbsFloor = (2 * NoF) - 2 - AbsFloor;

            return ((AbsFloor > 0) ? AbsFloor : (-AbsFloor));
        } else if (ALLCAs[CarIndex].getDirection() == ALLCAs[CarIndex].DOWN_WAY) {
            AbsFloor = CF - AbsFloor;

            if (AbsFloor > 0) {
                return (AbsFloor);
            } else {
                AbsFloor = -AbsFloor;

                if (AbsFloor < (NoF - 1)) {
                    return (AbsFloor);
                } else {
                    return ((2 * NoF) - 2 - AbsFloor);
                }
            }
        } else {
            throw new RuntimeException("Unimplemented! # 1252");
        }
    }

    /**
     * Uncycles (creates a linear direction from a cyclic direction)
     * the direction  of {@code AbsFloor} from the {@code CarIndex}th {@link
     * Car}.
     *
     * @param AbsFloor DOCUMENT ME!
     * @param CarIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    private int unCycleDir(int AbsFloor, int CarIndex) {
        if (ALLCAs[CarIndex].getDirection() == ALLCAs[CarIndex].BOTH_WAY) {
            throw new RuntimeException("Not moving! # 1253");
        }

        int CF = Cars[CarIndex].getCrtF();

        if (ALLCAs[CarIndex].getDirection() == ALLCAs[CarIndex].UP_WAY) {
            AbsFloor += CF;

            if (AbsFloor < (NoF - 1)) {
                return (ALLCAs[CarIndex].UP_WAY);
            }

            AbsFloor = (2 * NoF) - 2 - AbsFloor;

            return ((AbsFloor > 0) ? ALLCAs[CarIndex].DOWN_WAY
                                   : ALLCAs[CarIndex].UP_WAY);
        } else if (ALLCAs[CarIndex].getDirection() == ALLCAs[CarIndex].DOWN_WAY) {
            AbsFloor = CF - AbsFloor;

            if (AbsFloor > 0) {
                return (ALLCAs[CarIndex].DOWN_WAY);
            } else {
                AbsFloor = -AbsFloor;

                if (AbsFloor < (NoF - 1)) {
                    return (ALLCAs[CarIndex].UP_WAY);
                } else {
                    return (ALLCAs[CarIndex].DOWN_WAY);
                }
            }
        } else {
            throw new RuntimeException("Unimplemented! # 1252b");
        }
    }
}
