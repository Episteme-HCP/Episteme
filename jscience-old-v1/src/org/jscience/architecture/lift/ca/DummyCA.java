package org.jscience.architecture.lift.ca;

import org.jscience.architecture.lift.Car;
import org.jscience.architecture.lift.World;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This is a Dummy CA that does nothing.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:52 $
 */
public class DummyCA implements CA {
/**
     * Constructor
     */
    public DummyCA() {
        ;
    }

    /**
     * Does nothing
     *
     * @param C DOCUMENT ME!
     * @param AbsFloor DOCUMENT ME!
     */
    public void issueCommand(Car C, int AbsFloor) {
        ;
    }

    /**
     * Does nothing
     *
     * @param MyW DOCUMENT ME!
     */
    public void setWorld(World MyW) {
        ;
    }

    /**
     * Does nothing (returns {@code false}).
     *
     * @param From DOCUMENT ME!
     * @param To DOCUMENT ME!
     * @param CarIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean goes(int From, int To, int CarIndex) {
        return false;
    }

    /**
     * Does nothing
     */
    public void tick() {
        ;
    }

    /**
     * Does nothing
     */
    public void refresh() {
        ;
    }
}
