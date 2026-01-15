package org.jscience.architecture.lift;

/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This should be the father of all entities that process {@link Passenger}s.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.3 $ $Date: 2007-10-23 18:13:52 $
 *
 * @see Passenger
 * @see PassengerGenerator
 */
public abstract class PassengerProcessor extends Tickable {
/**
     * Constructor
     */
    public PassengerProcessor() {
        super();
    }

    /**
     * This method is called by {@link World} after each new {@link
     * Passenger} generated.
     *
     * @param P DOCUMENT ME!
     */
    public abstract void created(Passenger P);

    /* Tickable */
    public abstract String getName();

    /* Tickable */
    public abstract String getVersion();

    /**
     * This method is called by {@link World} before each {@link
     * Passenger} terminated.
     *
     * @param P DOCUMENT ME!
     */
    public abstract void process(Passenger P);

    /**
     * This method is called by {@link World} before this {@code
     * Passengerprocessor} is terminated. "As a reaction, the instance of this
     * object class should move and/or duplicate all non-temporary information
     * to persistent databases". In other worlds, it should save everything
     * important to disk.
     */
    public abstract void prepareToDie();
}
