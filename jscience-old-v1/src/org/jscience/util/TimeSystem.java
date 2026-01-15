/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util;


//import org.jscience.*;
/**
 * TimeSystem allows to use a time measurement that is independent from the
 * system time.
 *
 * @author Holger Antelmann
 */
public interface TimeSystem {
    /**
     * provides a TimeSystem implementation based on the system time.
     *
     * @see java.lang.System#currentTimeMillis()
     */
    public static final TimeSystem systemTime = new TimeSystem() {
            public long currentTimeMillis() {
                return System.currentTimeMillis();
            }
        };

    /**
     * returns the current time in milliseconds as measured by the
     * underlying time system which may be different from the system time.
     *
     * @return the difference, measured in milliseconds, between the current
     *         time and midnight, January 1, 1970 UTC.
     */
    long currentTimeMillis();
}
