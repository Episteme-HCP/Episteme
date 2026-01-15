package org.jscience.history;

import org.jscience.astronomy.AstronomyConstants;


/**
 * A class representing history useful constants.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class HistoryConstants extends Object {
    /** DOCUMENT ME! */
    public static final double UNIXTIME = 0; //our reference

    /** DOCUMENT ME! */
    public static final double BIGBANG = UNIXTIME -
        (1.38e10 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY);

    /** DOCUMENT ME! */
    public static final double SOLAR_SYSTEM_AGE = UNIXTIME -
        (5.0e9 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY);

    /** DOCUMENT ME! */
    public static final double EARTH_AGE = UNIXTIME -
        (4.6e9 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY);
}
