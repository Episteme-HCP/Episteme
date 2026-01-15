package org.jscience.earth;

/**
 * A class representing some useful methods for earth sciences
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class EarthSciencesUtils extends Object {
    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final double getHubbert(double d) {
        return 1 / (2 + (2 * Math.cosh(d)));
    }
}
