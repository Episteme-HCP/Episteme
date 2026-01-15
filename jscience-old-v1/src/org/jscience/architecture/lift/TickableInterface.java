package org.jscience.architecture.lift;

/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This interface defines the methods of {@link Tickable}.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:52 $
 */
public interface TickableInterface {
    /**
     * This function is called in each step of the simulator. In other
     * words, this method is called whenever a discrete simulation step (0.1
     * second) happens. All objects that are time-variant is notified by this
     * method that some time has elapsed since.
     */
    public void Tick();

    /**
     * Returns the Full Canonical Name of the object.
     *
     * @return DOCUMENT ME!
     */
    public String getFullName();

    /**
     * Returns the unique ID.
     *
     * @return DOCUMENT ME!
     */
    public long getID();

    /**
     * Returns the Name of the object.
     *
     * @return DOCUMENT ME!
     */
    public String getName();

    /**
     * Returns the Version of the object
     *
     * @return DOCUMENT ME!
     */
    public String getVersion();
}
