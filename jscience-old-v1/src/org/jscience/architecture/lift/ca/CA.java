package org.jscience.architecture.lift.ca;

import org.jscience.architecture.lift.Car;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This interface defines the methods that must be implemented by all Control
 * Algorithms (also called as High-Level Controls, Call Dispatcher Algorithms,
 * Call Allocation Algorithms, Call Scheduler Algorithms, etc).
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:52 $
 */
public interface CA {
    /**
     * Return {@code true} if the {@code CarIndex}th car goes from
     * Floor {@code From} to Floor {@code To}. This is used to notify the
     * passengers so they can decide if they want to get into the car or not.
     *
     * @param From The passenger's current floor
     * @param To The passenger's destination floor
     * @param CarIndex The index of the Car
     *
     * @return {@code true} if the Car is suitable for the passenger, false
     *         otherwise
     */
    public boolean goes(int From, int To, int CarIndex);

    /**
     * Issues (registers) a command for the {@code AbsFloor}th Floor to
     * the {@code C} Car. This happens when a Passenger pushes the
     * corresponding Floor button.
     *
     * @param C DOCUMENT ME!
     * @param AbsFloor DOCUMENT ME!
     */
    public void issueCommand(Car C, int AbsFloor);

    /**
     * {@link org.jscience.architecture.lift.Tickable}
     */
    public void tick();
}
