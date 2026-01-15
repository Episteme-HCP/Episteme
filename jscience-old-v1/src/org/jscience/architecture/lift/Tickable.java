package org.jscience.architecture.lift;

/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This abstract class is the foundation class of all objects that change as
 * time flows. In other worlds, all time-variant objects must extend this
 * object in the {@code JLESA} system. This applies to {@link Car}s, {@link
 * Passenger}s, {@link org.jscience.architecture.lift.ca.CA}s, etc.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.3 $ $Date: 2007-10-23 18:13:52 $
 */
public abstract class Tickable implements TickableInterface {
    /**
     * DOCUMENT ME!
     */
    private static long ID = 0;

    /**
     * DOCUMENT ME!
     */
    private long MyID = 0;

/**
     * A simple constructor that sets an unique ID for this Tickable. Do not tamper with this one.
     */
    public Tickable() {
        MyID = ID;
        ID++;
    }

    /* Tickable */
    public abstract void Tick();

    /**
     * Returns the Full Canonical Name of object that evenincludes
     * unique ID
     *
     * @return DOCUMENT ME!
     */
    public String getFullName() {
        return (getClass().getName() + " (" + getName() + " " + getVersion() +
        ") #" + getID());
    }

    /* Tickable */
    public long getID() {
        return (MyID);
    }

    /* Tickable */
    public abstract String getName();

    /* Tickable */
    public abstract String getVersion();
}
