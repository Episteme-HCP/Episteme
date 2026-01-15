// Written by William O'Mullane for the
// Astrophysics Division of ESTEC  - part of the European Space Agency.
package org.jscience.tests.astronomy.milkyway.hipparcos;

import java.util.Iterator;


/**
 * Simple interface for acepting Stars - used by StarLoader Hence to make use
 * fo Star loader you need somwthing which implements StarStore.
 */
public interface StarStore {
    /**
     * DOCUMENT ME!
     *
     * @param star DOCUMENT ME!
     */
    public void addStar(Star star);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAlpha();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDelta();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getTol();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator getStars();
}
