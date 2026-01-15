package org.jscience.architecture.lift;

/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This is a very simple encapsulation class. May be used in {@link
 * KinematicModel}s.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:52 $
 *
 * @see LoadDependentKinematicModel
 */
public class LoadSpeed {
    /**
     * The base time of interfloor travel, used in {@link
     * LoadDependentKinematicModel}.
     */
    public int BaseTime;

    /**
     * The floor fight time of interfloor travel, used in {@link
     * LoadDependentKinematicModel}.
     */
    public int FloorFligthTime;

    /**
     * The time needed to open the door, used in {@link
     * LoadDependentKinematicModel}.
     */
    public int DoorOpenTime;

    /**
     * The time needed to close the door, used in {@link
     * LoadDependentKinematicModel}.
     */
    public int DoorCloseTime;

    /**
     * The minimum load (in {@link Passenger}s) when this {@code
     * LoadSpeed} rule is true.
     */
    public int FromLoad;

    /**
     * The maximum load (in {@link Passenger}s) when this {@code
     * LoadSpeed} rule is enabled.
     */
    public int ToLoad;

/**
     * Constructor.
     */
    public LoadSpeed(int BaseTime, int FloorFligthTime, int DoorOpenTime,
        int DoorCloseTime, int FromLoad, int ToLoad) {
        this.BaseTime = BaseTime;
        this.FloorFligthTime = FloorFligthTime;
        this.DoorOpenTime = DoorOpenTime;
        this.DoorCloseTime = DoorCloseTime;
        this.FromLoad = FromLoad;
        this.ToLoad = ToLoad;
    }
}
