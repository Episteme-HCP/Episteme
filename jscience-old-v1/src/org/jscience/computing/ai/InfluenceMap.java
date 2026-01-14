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

/**
 * A class encapsulating basic influence mapping functionality.
 *
 * @author James Matthews
 */
public class InfluenceMap {
    /** Select the first maximum value. */
    public static final int FIRST = 0;

    /** Select the last maximum value. */
    public static final int LAST = 1;

    /** Select a random maximum value. */
    public static final int RANDOM = 2;

    /** The influence map itself. */
    protected int[][] influenceMap;

    /** The random threshold. */
    protected double threshold = 0.25;

/**
     * Creates a new instance of InfluenceMap
     */
    public InfluenceMap() {
    }

/**
     * Create a new instance of InfluenceMap with size information.
     *
     * @param width  the width of the influence map.
     * @param height the height of the influence map.
     */
    public InfluenceMap(int width, int height) {
        influenceMap = new int[width][height];
    }

    /**
     * Return the width of the influence map.
     *
     * @return the width.
     */
    public int getWidth() {
        return influenceMap.length;
    }

    /**
     * Return the height of the influence map.
     *
     * @return the height.
     */
    public int getHeight() {
        return influenceMap[0].length;
    }

    /**
     * Resets the influence map to zero (calls <code>resetTo(0)</code>)
     */
    public void reset() {
        resetTo(0);
    }

    /**
     * Resets the influence map to the specified value.
     *
     * @param value the value to set all positions of the influence map.
     */
    public void resetTo(int value) {
        for (int i = 0; i < influenceMap.length; i++) {
            for (int j = 0; j < influenceMap[i].length; j++) {
                influenceMap[i][j] = value;
            }
        }
    }

    /**
     * Set a point on the influence map to a specified value.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     * @param value the value to set.
     */
    public void setAt(int x, int y, int value) {
        influenceMap[x][y] = value;
    }

    /**
     * Return the value of the influence map at a given coordinate.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     *
     * @return the value of the influence map.
     */
    public int getAt(int x, int y) {
        return influenceMap[x][y];
    }

    /**
     * Increment the influence map by a given increment. This method
     * can also be used to decrement the influence map.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     * @param increment the increment to be added (can be negative).
     */
    public void incrementAt(int x, int y, int increment) {
        influenceMap[x][y] += increment;
    }

    /**
     * Returns the maximum value. Equal values are treated differently
     * according to the type specified: FIRST will return the first maximum
     * value, LAST will return the last, and RANDOM will return a randomly
     * selected maximum position.
     *
     * @param type the selection type.
     *
     * @return the maximum point. This is returned as an <code>int[3]</code>
     *         (x-position, y-position, maximum value).
     */
    public int[] getMaximum(int type) {
        int mx = -1;
        int my = -1;
        int max = Integer.MIN_VALUE;
        int map;
        java.util.Random random = new java.util.Random();

        for (int i = 0; i < influenceMap.length; i++) {
            for (int j = 0; j < influenceMap[i].length; j++) {
                map = influenceMap[i][j];

                if (map >= max) {
                    if ((type == FIRST) && (map == max)) {
                        continue;
                    }

                    if ((type == RANDOM) && (map == max) &&
                            (random.nextGaussian() > threshold)) {
                        continue;
                    }

                    mx = i;
                    my = j;
                    max = influenceMap[i][j];
                }
            }
        }

        return new int[] { mx, my, max };
    }
}
