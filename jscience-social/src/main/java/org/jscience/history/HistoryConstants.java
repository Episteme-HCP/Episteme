package org.jscience.history;

/**
 * A class representing history useful constants.
 */
public final class HistoryConstants {

    // Defined relative to UNIX EPOCH (1970-01-01) in Seconds (or similar unit?) 
    // Old code used: UNIXTIME = 0. And then used "Julian Year" * "Earth Day".
    // Julian Year = 365.25 days. Earth Day = 86400 seconds?
    
    // Assuming double represents seconds in standard physics context, or days used in Calendar logic.
    // Old code referenced AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY.
    // Let's assume seconds for physics consistency.
    
    public static final double JULIAN_YEAR_SECONDS = 365.25 * 86400.0;
    
    public static final double UNIXTIME = 0.0; 

    public static final double BIGBANG = UNIXTIME - (1.38e10 * JULIAN_YEAR_SECONDS);

    public static final double SOLAR_SYSTEM_AGE = UNIXTIME - (5.0e9 * JULIAN_YEAR_SECONDS);

    public static final double EARTH_AGE = UNIXTIME - (4.6e9 * JULIAN_YEAR_SECONDS);

    private HistoryConstants() {}
}
