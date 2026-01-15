/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util.logging;

import org.jscience.util.Filter;


/**
 * filters LogEntry objects by their level using the natural order of Level
 * objects
 *
 * @author Holger Antelmann
 *
 * @see Level
 */
public class LevelFilter implements Filter<LogEntry> {
    /** accepts LogEntry objects that exclude the levels FINE, FINER, FINEST. */
    public static final Filter<LogEntry> NO_FINE_LEVEL_FILTER = new Filter<LogEntry>() {
            public boolean accept(LogEntry entry) {
                //if (entry.getLevel() == null) return true;
                if (entry.getLevel().equals(Level.FINE)) {
                    return false;
                }

                if (entry.getLevel().equals(Level.FINER)) {
                    return false;
                }

                if (entry.getLevel().equals(Level.FINEST)) {
                    return false;
                }

                return true;
            }
        };

    /** accepts LogEntry objects that compare to more than the FINE level. */
    public static final Filter<LogEntry> MORE_THAN_FINE_LEVEL_FILTER = new Filter<LogEntry>() {
            public boolean accept(LogEntry entry) {
                return (entry.getLevel().compareTo(Level.FINE) > 0);
            }
        };

    /** DOCUMENT ME! */
    Level minLevel;

/**
     * Creates a new LevelFilter object.
     *
     * @param minLevel DOCUMENT ME!
     */
    public LevelFilter(Level minLevel) {
        if (minLevel == null) {
            throw new NullPointerException("level must not be null");
        }

        this.minLevel = minLevel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Level getMinLevel() {
        return minLevel;
    }

    /**
     * returns true only if the Level of the entry is equal or comes
     * after the configured Level of this filter.
     *
     * @param entry DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean accept(LogEntry entry) {
        return (minLevel.compareTo(entry.getLevel()) <= 0);
    }
}
