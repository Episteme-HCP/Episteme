package org.jscience.history.temporal;

import java.time.Instant;
import org.jscience.util.Temporal;

/**
 * Interface for a temporal coordinate, representing a position in time.
 * Analogous to {@link org.jscience.earth.coordinates.EarthCoordinate}.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface TemporalCoordinate extends Temporal<TemporalCoordinate>, Comparable<TemporalCoordinate> {

    /**
     * Returns the exact instant this coordinate represents.
     * If the coordinate is fuzzy or an interval, this returns a representative point.
     * 
     * @return the instant
     */
    Instant toInstant();

    /**
     * Returns the precision of this coordinate.
     * 
     * @return the precision level
     */
    Precision getPrecision();

    /**
     * Returns whether this coordinate represents a range or has uncertainty.
     * 
     * @return true if fuzzy or interval-based
     */
    boolean isFuzzy();

    @Override
    default TemporalCoordinate getWhen() {
        return this;
    }
}
