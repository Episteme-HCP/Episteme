package org.jscience.computing.kmeans.gui;

import org.jscience.computing.kmeans.Cluster;
import org.jscience.computing.kmeans.ClusterSet;
import org.jscience.computing.kmeans.Coordinate;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

/**
 * Displays two dimensional clusters on a graph.
 *
 * @author Harlan Crystal <hpc4@cornell.edu>
 * @version $Id: Graph.java,v 1.2 2007-10-21 21:08:01 virtualcall Exp $
 * @date April 17, 2003
 */
public class Graph extends JPanel {
    /**
     * The number of colors used to color clusters
     */
    private static final int NUMCOLORS = 12;

    /**
     * Array of colors for each cluster
     */
    private Color[] clustercolors;

    /**
     * The clusters we're visualizing
     */
    private ClusterSet clusters;

    /**
     * Controls whether we show mean points or not
     */
    private boolean showMeans;

    /**
     * Controls whether we show the axis
     */
    private boolean showAxis;

    /**
     * Constructor.
     * Doesn't draw the dataset, since not provided one.
     */
    public Graph() {
        super();
        clusters = null;
        showMeans = true;
        showAxis = true;
        initColors();
        setMinimumSize(new Dimension(20, 20));
    }

    /**
     * Sets the value for displaying the axis.
     *
     * @param value Set to true to show the axis, false to not show axis.
     */
    public void setShowAxis(boolean value) {
        this.showAxis = value;
        repaint();
    }

    /**
     * Sets the value for displaying mean points.
     *
     * @param value Set to true to show mean points, false to not show means.
     */
    public void setShowMeans(boolean value) {
        this.showMeans = value;
        repaint();
    }

    /**
     * Helper function for initializing the colors.
     */
    private void initColors() {
        clustercolors = new Color[NUMCOLORS]; // number of name-able colors;
        clustercolors[0] = Color.DARK_GRAY;
        clustercolors[1] = Color.BLUE;
        clustercolors[2] = Color.RED;
        clustercolors[3] = Color.YELLOW;
        clustercolors[4] = Color.GRAY;
        clustercolors[5] = Color.GREEN;
        clustercolors[6] = Color.LIGHT_GRAY;
        clustercolors[7] = Color.MAGENTA;
        clustercolors[8] = Color.ORANGE;
        clustercolors[9] = Color.PINK;
        clustercolors[10] = Color.CYAN;
        clustercolors[11] = Color.BLACK;
    }

    private int transposeY(double y) {
        double middle = getHeight() / 2.0; // move to middle
        double scale = getHeight() / 20.0;
        return (int) (middle - (scale * y));
    }

    private int transposeX(double x) {
        double middle = getWidth() / 2.0;
        double scale = getWidth() / 20.0;
        return (int) (middle + (scale * x));
    }

    /**
     * The new painting for this object.
     */
    protected void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // draw the axis
        if (showAxis) {
            g.setColor(Color.BLACK);
            int x0 = transposeX(0);
            int y0 = transposeY(0);
            g.drawLine(x0, transposeY(-10), x0, transposeY(10));
            g.drawLine(transposeX(-10), y0, transposeX(10), y0);
        }

        if (clusters == null)
            return;

        int i = 0;
        for (Iterator cit = clusters.iterator(); cit.hasNext();) {
            g.setColor(clustercolors[i % NUMCOLORS]);
            Cluster clust = (Cluster) cit.next();

            for (Iterator it = clust.iterator(); it.hasNext();) {
                Coordinate data = (Coordinate) it.next();
                int x = transposeX(data.get(0)) - 2;
                int y = transposeY(data.get(1)) - 2;
                g.fillOval(x, y, 4, 4);
            }

            // draw the mean points
            if (showMeans) {
                g.setColor(Color.GREEN);
                Coordinate mean = clust.average();
                int x = transposeX(mean.get(0)) - 3;
                int y = transposeY(mean.get(1)) - 3;
                g.fillOval(x, y, 6, 6);
            }

            i++;
        }

    }

    /**
     * Changes the clusters we're drawing.
     * Updates the visualization to reflect change.
     *
     * @param clusters The clusters to visualize.
     */
    public void set(ClusterSet clusters) {
        this.clusters = clusters;
        repaint();
    }
}
