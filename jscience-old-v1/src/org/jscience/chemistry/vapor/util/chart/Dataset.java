/*
 * Dataset class for charting tool.
 *
 * Author: Samir Vaidya (mailto: syvaidya@yahoo.com)
 * Copyright (c) Samir Vaidya
 */
package org.jscience.chemistry.vapor.util.chart;

import java.awt.*;
import java.awt.geom.Point2D;


/**
 * Dataset class for charting tool.
 */
public class Dataset {
    /**
     * DOCUMENT ME!
     */
    private Color lineColor = Color.green;

    /**
     * DOCUMENT ME!
     */
    private Point2D.Double[] ptArray = null;

    /**
     * DOCUMENT ME!
     */
    private double minX = +1E10;

    /**
     * DOCUMENT ME!
     */
    private double minY = +1E10;

    /**
     * DOCUMENT ME!
     */
    private double maxX = -1E10;

    /**
     * DOCUMENT ME!
     */
    private double maxY = -1E10;

    /**
     * Creates a new Dataset object.
     *
     * @param numOfPoints DOCUMENT ME!
     */
    public Dataset(int numOfPoints) {
        ptArray = new Point2D.Double[numOfPoints];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumOfPoints() {
        return ptArray.length;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Point2D.Double getValue(int index) {
        return ptArray[index];
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void setValue(int index, double x, double y) {
        if (ptArray[index] == null) {
            ptArray[index] = new Point2D.Double();
        }

        ptArray[index].setLocation(x, y);

        if (x < minX) {
            minX = x;
        }

        if (x > maxX) {
            maxX = x;
        }

        if (y < minY) {
            minY = y;
        }

        if (y > maxY) {
            maxY = y;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getLineColor() {
        return lineColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param lineColor DOCUMENT ME!
     */
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMinX() {
        return minX;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMaxX() {
        return maxX;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMinY() {
        return minY;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMaxY() {
        return maxY;
    }
}
