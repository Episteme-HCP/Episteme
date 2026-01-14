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

package org.jscience.computing.ai.util;

import org.jscience.computing.ai.Navigable;
import org.jscience.computing.ai.Pathfinder;
import org.jscience.util.Visualizable;

/**
 * Implements a standard 2D map. For efficiency purposes, distance is implemented
 * as Manhattan distance. Map values are specified between 0 and 255 (rendered as
 * black to white). A position on the map is valid if the map value is less than 32.
 * <p/>
 * Cost is determined as (255 - map_value) + 1, although a small additional bias
 * is added if the movement is diagonal.
 *
 * @author James Matthews
 */
public class StandardMap2D implements Navigable, Visualizable {
    /**
     * The render size of the map.
     * 4
     */
    protected int renderSize = 4;

    /**
     * The width of the map.
     */
    protected int width;

    /**
     * The height of the map.
     */
    protected int height;

    /**
     * The value of the map.
     */
    protected int[][] mapValues;

    /**
     * Creates a new instance of Map2D
     */
    public StandardMap2D() {
        this(0, 0);
    }

    /**
     * Create a new instance of StandardMap2D with dimensional information.
     *
     * @param width  the width of the map.
     * @param height the height of the map.
     */
    public StandardMap2D(int width, int height) {
        this.width = width;
        this.height = height;

        mapValues = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                mapValues[i][j] = 255;
            }
        }
    }

    /**
     * Return the width of the map.
     *
     * @return the width of the map.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Return the height of the map.
     *
     * @return the height of the map.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Return the render size of the map.
     *
     * @return the render size.
     */
    public int getRenderSize() {
        return renderSize;
    }

    /**
     * Set the render size of the map.
     *
     * @param rs the new render size.
     */
    public void setRenderSize(int rs) {
        renderSize = rs;
    }

    /**
     * Set the map at the given location with the given value. Note that while the
     * positional information is checked, no limits are based on the map value.
     *
     * @param x     the x-position.
     * @param y     the y-position.
     * @param value the new map value.
     */
    public void setMapAt(int x, int y, int value) {
        if ((x < 0) || (x >= width) || (y < 0) || (y >= height)) {
            throw new IllegalArgumentException("invalid position!");
        }

        mapValues[x][y] = value;
    }

    /**
     * Create the node ID from positional information. This is calculated as:
     * <code>node.getX() * width + node.getY()</code>
     *
     * @param node the node to calculate.
     * @return the node ID.
     */
    public int createNodeID(Pathfinder.Node node) {
        return (node.getX() * width) + node.getY();
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param node   DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public double getCost(Pathfinder.Node parent, Pathfinder.Node node) {
        double cost = (256 - mapValues[node.getX()][node.getY()]);

        // Add small additional cost diagonals
        if (((parent.getX() - node.getX()) != 0) &&
                ((parent.getY() - node.getY()) != 0)) {
            cost += 0.414;
        }

        return cost;
    }

    /**
     * DOCUMENT ME!
     *
     * @param goal DOCUMENT ME!
     * @param node DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public double getDistance(Pathfinder.Node goal, Pathfinder.Node node) {
        return Math.abs(goal.getX() - node.getX()) +
                Math.abs(goal.getY() - node.getY());
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean isValid(int x, int y) {
        // Check validity.
        if ((x < 0) || (y < 0) || (x >= width) || (y >= height)) {
            return false;
        }

        if (mapValues[x][y] < 32) {
            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean isValid(Pathfinder.Node node) {
        return isValid(node.getX(), node.getY());
    }

    /**
     * Render the map.
     *
     * @param g  the graphics context.
     * @param ww the width of the context.
     * @param hh the height of the context.
     */
    public void render(java.awt.Graphics g, int ww, int hh) {
        int cx = width * renderSize;
        int cy = height * renderSize;

        // sx/sy are the starting points for the CA world
        // centred within the the graphics context.
        int sx = (int) ((double) (ww - cx) / 2.0);
        int sy = (int) ((double) (hh - cy) / 2.0);

        g.setColor(java.awt.Color.lightGray);
        g.fillRect(0, 0, ww, hh);
        g.setColor(java.awt.Color.WHITE);
        g.fillRect(sx, sy, width * renderSize, height * renderSize);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (mapValues[i][j] == 255) {
                    continue;
                }

                g.setColor(new java.awt.Color(mapValues[i][j], mapValues[i][j],
                        mapValues[i][j]));
                g.fillRect((i * renderSize) + sx, (j * renderSize) + sy,
                        renderSize, renderSize);
            }
        }
    }

    /**
     * Write an image of the map.
     *
     * @param s      the filename.
     * @param width  the width of the image.
     * @param height the height of the image.
     */
    public void writeImage(String s, int width, int height) {
        try {
            ImageHelper.writeVisualizedImage(s, width, height, this);
        } catch (java.io.IOException e) {
            System.err.println(e);
        }
    }
}
