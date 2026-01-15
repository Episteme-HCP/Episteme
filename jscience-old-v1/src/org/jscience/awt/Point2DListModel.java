package org.jscience.awt;

import java.awt.geom.Point2D;

import java.util.List;


/**
 * The Point2DListModel provides a convenient implementation of the
 * Graph2DModel interface based upon a List of Point2D objects.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class Point2DListModel extends AbstractGraphModel
    implements Graph2DModel {
    /** DOCUMENT ME! */
    private List data;

/**
     * Creates a new Point2DListModel object.
     */
    public Point2DListModel() {
    }

    /**
     * Sets the list of points to be plotted.
     *
     * @param points DOCUMENT ME!
     */
    public void setData(List points) {
        data = points;
        fireGraphDataChanged();
    }

    // Graph2DModel interface
    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getXCoord(int i) {
        Point2D p = (Point2D) data.get(i);

        return (float) p.getX();
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getYCoord(int i) {
        Point2D p = (Point2D) data.get(i);

        return (float) p.getY();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int seriesLength() {
        return data.size();
    }

    /**
     * DOCUMENT ME!
     */
    public void firstSeries() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean nextSeries() {
        return false;
    }
}
