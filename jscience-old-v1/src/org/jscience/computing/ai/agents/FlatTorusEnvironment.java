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

package org.jscience.computing.ai.agents;

import org.jscience.util.IllegalDimensionException;

import java.util.Collections;
import java.util.Set;


/**
 * A class representing a flat environment whose bounds are curved onto
 * each other.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class FlatTorusEnvironment extends DiscreteEnvironment {
    /**
     * DOCUMENT ME!
     */
    private Cell[][] cells;

    /**
     * Creates a new FlatTorusEnvironment object.
     *
     * @param xDimension DOCUMENT ME!
     * @param yDimension DOCUMENT ME!
     */
    public FlatTorusEnvironment(int xDimension, int yDimension) {
        if ((xDimension > 0) && (yDimension > 0)) {
            cells = new Cell[xDimension][yDimension];

            int[] position;

            for (int i = 0; i < xDimension; i++) {
                for (int j = 0; j < yDimension; j++) {
                    position = new int[2];
                    position[0] = i;
                    position[1] = j;
                    cells[i][j] = new Cell(this, position);
                }
            }
        } else {
            throw new IllegalArgumentException(
                "The sizes of the environment must be strictly positive.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getXDimension() {
        return cells.length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getYDimension() {
        return cells[0].length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getContents() {
        Set result;
        result = Collections.EMPTY_SET;

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                result.addAll(cells[i][j].getContents());
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCurvedOnItself() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param position DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public Set getContentsAt(int[] position) {
        if (position.length == 2) {
            return cells[getTorusValue(position[0], cells.length)][getTorusValue(position[1],
                cells[0].length)].getContents();
        } else {
            throw new IllegalDimensionException(
                "The position argument must be of same length as environment dimension.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param position DOCUMENT ME!
     * @param contents DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public void setContentsAt(int[] position, Set contents) {
        if (position.length == 2) {
            cells[getTorusValue(position[0], cells.length)][getTorusValue(position[1],
                cells[0].length)].setContents(contents);
        } else {
            throw new IllegalDimensionException(
                "The position argument must be of same length as environment dimension.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param position DOCUMENT ME!
     * @param method DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public Set getNeighbors(int[] position, int method) {
        if (position.length == 2) {
            Set result;
            result = Collections.EMPTY_SET;

            if (method == DiscreteEnvironment.MOORE_NEIGHBORHOOD) {
                result.add(cells[getTorusValue(position[0] + 1, cells.length)][getTorusValue(position[1] +
                        1, cells[0].length)]);
                result.add(cells[getTorusValue(position[0] + 1, cells.length)][getTorusValue(
                        position[1], cells[0].length)]);
                result.add(cells[getTorusValue(position[0] + 1, cells.length)][getTorusValue(position[1] -
                        1, cells[0].length)]);
                result.add(cells[getTorusValue(position[0], cells.length)][getTorusValue(position[1] +
                        1, cells[0].length)]);
                result.add(cells[getTorusValue(position[0], cells.length)][getTorusValue(position[1] -
                        1, cells[0].length)]);
                result.add(cells[getTorusValue(position[0] - 1, cells.length)][getTorusValue(position[1] +
                        1, cells[0].length)]);
                result.add(cells[getTorusValue(position[0] - 1, cells.length)][getTorusValue(
                        position[1], cells[0].length)]);
                result.add(cells[getTorusValue(position[0] - 1, cells.length)][getTorusValue(position[1] -
                        1, cells[0].length)]);

                return result;
            } else {
                if (method == DiscreteEnvironment.VON_NEUMANN_NEIGHBORHOOD) {
                    result.add(cells[getTorusValue(position[0], cells.length)][getTorusValue(position[1] +
                            1, cells[0].length)]);
                    result.add(cells[getTorusValue(position[0] + 1, cells.length)][getTorusValue(
                            position[1], cells[0].length)]);
                    result.add(cells[getTorusValue(position[0] - 1, cells.length)][getTorusValue(
                            position[1], cells[0].length)]);
                    result.add(cells[getTorusValue(position[0], cells.length)][getTorusValue(position[1] -
                            1, cells[0].length)]);

                    return result;
                } else {
                    throw new IllegalArgumentException(
                        "Only Moore and Von Neumann neighborhood are supported.");
                }
            }
        } else {
            throw new IllegalDimensionException(
                "The position argument must be of same length as environment dimension.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDimension() {
        return 2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSurface() {
        return Double.POSITIVE_INFINITY; //cells.length * cells[0].length
    }

    //bound is positive
    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     * @param bound DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int getTorusValue(int value, int bound) {
        if (value < 0) {
            if ((value % bound) == 0) {
                return 0;
            } else {
                return bound + (value % bound);
            }
        } else {
            return value % bound;
        }
    }
}
