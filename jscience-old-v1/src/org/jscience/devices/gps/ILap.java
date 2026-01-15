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
public interface ILap {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long getStartTime();

    /**
     * total time
     *
     * @return DOCUMENT ME!
     */
    public long getTotalTime();

    /**
     * total_distance
     *
     * @return DOCUMENT ME!
     */
    public float getTotalDistance();

    /**
     * begin point
     *
     * @return DOCUMENT ME!
     */
    public Position getStartPosition();

    /**
     * end point
     *
     * @return DOCUMENT ME!
     */
    public Position getEndPosition();

    /**
     * calories
     *
     * @return DOCUMENT ME!
     */
    public int getCalories();

    /**
     * track index
     *
     * @return DOCUMENT ME!
     */
    public short getTrackIndex();
}
