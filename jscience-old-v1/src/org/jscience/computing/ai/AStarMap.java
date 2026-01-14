/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.computing.ai;

import org.jscience.computing.ai.util.Gradient;
import org.jscience.computing.ai.util.StandardMap2D;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import java.util.ListIterator;


/**
 * <code>AStarMap</code> is simply derived from <code>StandardMap2D</code>
 * to provide further functionality to visualize the A algorithm. It displays
 * the map, start/goal positions, the best route as well as open and closed
 * lists.
 *
 * @author James Matthews
 */
public class AStarMap extends StandardMap2D {
    /** Start x-position. */
    protected int sx = -1;

    /** Start y-position. */
    protected int sy = -1;

    /** Goal x-position. */
    protected int gx = -1;

    /** Goal y-position. */
    protected int gy = -1;

    /** The open list gradient (red). */
    protected Gradient openGradient = new Gradient();

    /** The closed list gradient (blue). */
    protected Gradient closedGradient = new Gradient();

    /** The pathfinder (used for drawing the routes). */
    protected AStarPathfinder aStar;

/**
     * Creates a new instance of AStarMap
     */
    public AStarMap() {
        this(0, 0);
    }

/**
     * Creates a new instance with dimensional information.
     *
     * @param width  the width of the map.
     * @param height the height of the map.
     */
    public AStarMap(int width, int height) {
        super(width, height);

        // Open gradient
        openGradient.addPoint(new java.awt.Color(95, 0, 0));
        openGradient.addPoint(new java.awt.Color(250, 215, 215));
        openGradient.createGradient();

        // Closed gradient
        closedGradient.addPoint(new java.awt.Color(25, 65, 120));
        closedGradient.addPoint(new java.awt.Color(185, 215, 255));
        closedGradient.createGradient();
    }

/**
     * Creates a new instance from a BufferedImage.
     *
     * @param img the image to be used as the map values.
     */
    public AStarMap(BufferedImage img) {
        super(img.getWidth(), img.getHeight());

        BufferedImage sourceImage = img;
        Raster sourceRaster = img.getRaster();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                setMapAt(i, j, sourceRaster.getSample(i, j, 0));
            }
        }

        // Open gradient
        openGradient.addPoint(new java.awt.Color(95, 0, 0));
        openGradient.addPoint(new java.awt.Color(250, 215, 215));
        openGradient.createGradient();

        // Closed gradient
        closedGradient.addPoint(new java.awt.Color(25, 65, 120));
        closedGradient.addPoint(new java.awt.Color(185, 215, 255));
        closedGradient.createGradient();
    }

    /**
     * Set the pathfinder to display.
     *
     * @param as the pathfinder to display.
     */
    public void setPathfinder(AStarPathfinder as) {
        aStar = as;
    }

    /**
     * Return the pathfinder being used.
     *
     * @return the pathfinder.
     */
    public AStarPathfinder getPathfinder() {
        return aStar;
    }

    /**
     * Render the map.
     *
     * @param g the graphics context.
     * @param ww the content width.
     * @param hh the context height.
     *
     * @see org.jscience.computing.ai.util.StandardMap2D#render(java.awt.Graphics,
     *      int,int)
     */
    public void render(java.awt.Graphics g, int ww, int hh) {
        // Render the map
        super.render(g, ww, hh);

        // Render the start and goal points
        int cx = width * renderSize;
        int cy = height * renderSize;

        // sx/sy are the starting points for the CA world
        // centred within the the graphics context.
        int ssx = (int) ((double) (ww - cx) / 2.0);
        int ssy = (int) ((double) (hh - cy) / 2.0);

        // Firstly, render the open/closed lists
        int i;

        // Firstly, render the open/closed lists
        int j;
        AStarPathfinder.AStarNode node;
        ListIterator open = aStar.getOpen().listIterator();
        ListIterator closed = aStar.getClosed().listIterator();

        while (open.hasNext()) {
            node = (AStarPathfinder.AStarNode) open.next();
            i = node.getX();
            j = node.getY();
            g.setColor(openGradient.getColour(mapValues[i][j]));
            g.fillRect((i * renderSize) + ssx, (j * renderSize) + ssy,
                renderSize, renderSize);
        }

        while (closed.hasNext()) {
            node = (AStarPathfinder.AStarNode) closed.next();
            i = node.getX();
            j = node.getY();
            g.setColor(closedGradient.getColour(mapValues[i][j]));
            g.fillRect((i * renderSize) + ssx, (j * renderSize) + ssy,
                renderSize, renderSize);
        }

        AStarPathfinder.AStarNode start = aStar.getStart();
        AStarPathfinder.AStarNode goal = aStar.getGoal();

        sx = start.getX();
        sy = start.getY();
        gx = goal.getX();
        gy = goal.getY();

        g.setColor(java.awt.Color.GREEN);
        g.fillOval((ssx + (sx * renderSize)) - 2,
            (ssy + (sy * renderSize)) - 2, 5, 5);
        g.setColor(java.awt.Color.RED);
        g.fillOval((ssx + (gx * renderSize)) - 2,
            (ssy + (gy * renderSize)) - 2, 5, 5);
        g.setColor(java.awt.Color.BLACK);
        g.drawRect(ssx, ssy, width * renderSize, height * renderSize);

        if (aStar.getBestNode() != null) {
            g.setColor(new java.awt.Color(0, 0, 128));

            AStarPathfinder.AStarNode first = aStar.getBestNode();
            AStarPathfinder.AStarNode next = first.getParent();
            AStarPathfinder.AStarNode temp;

            while (next != null) {
                g.drawLine(ssx + (first.getX() * renderSize),
                    ssy + (first.getY() * renderSize),
                    ssx + (next.getX() * renderSize),
                    ssy + (next.getY() * renderSize));

                first = next;
                next = next.getParent();
            }
        }
    }
}
