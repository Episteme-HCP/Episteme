/*
 * Created on Oct 11, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jscience.devices.gps;

/**
 * @author tkuebler
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public interface ITrackpoint {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Position getPosition();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long getTime();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getAltitude();

    // header methods
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isHeader();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getIndex();
}
