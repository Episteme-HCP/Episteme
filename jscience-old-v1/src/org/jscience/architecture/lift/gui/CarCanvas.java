package org.jscience.architecture.lift.gui;

import java.awt.*;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.3 $ $Date: 2007-10-23 18:13:53 $
 */
public interface CarCanvas {
    /**
     * DOCUMENT ME!
     */
    final static int GOING_UP = 0;

    /**
     * DOCUMENT ME!
     */
    final static int GOING_DOWN = 1;

    /**
     * DOCUMENT ME!
     */
    final static int ARRIVING_UP = 2;

    /**
     * DOCUMENT ME!
     */
    final static int ARRIVING_DOWN = 3;

    /**
     * DOCUMENT ME!
     */
    final static int PARKING = 4;

    /**
     * DOCUMENT ME!
     */
    final static int WAITING = 5;

    /**
     * DOCUMENT ME!
     */
    final static int CLOSE_OPEN = 6;

    /**
     * DOCUMENT ME!
     *
     * @param DstFloors DOCUMENT ME!
     */
    abstract public void setDstFloors(int[] DstFloors);

    /**
     * DOCUMENT ME!
     *
     * @param MaxNumber DOCUMENT ME!
     */
    abstract public void setMaxNumber(int MaxNumber);

    /**
     * DOCUMENT ME!
     *
     * @param ActNumber DOCUMENT ME!
     */
    abstract public void setActNumber(int ActNumber);

    /**
     * DOCUMENT ME!
     *
     * @param NewState DOCUMENT ME!
     */
    public abstract void setState(int NewState);

    /**
     * DOCUMENT ME!
     *
     * @param NewProgress DOCUMENT ME!
     */
    public abstract void setProgress(double NewProgress);

    /**
     * DOCUMENT ME!
     *
     * @param NewCarPresent DOCUMENT ME!
     */
    public abstract void setCarPresent(boolean NewCarPresent);

    /**
     * DOCUMENT ME!
     *
     * @param G DOCUMENT ME!
     */
    public abstract void paint(Graphics G);
}
