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
 * A class representing a cubic environment.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class CubicBoundedEnvironment extends DiscreteEnvironment {
    /**
     * DOCUMENT ME!
     */
    private Cell[][][] cells;

    /**
     * Creates a new CubicBoundedEnvironment object.
     *
     * @param xDimension DOCUMENT ME!
     * @param yDimension DOCUMENT ME!
     * @param zDimension DOCUMENT ME!
     */
    public CubicBoundedEnvironment(int xDimension, int yDimension,
        int zDimension) {
        if ((xDimension > 0) && (yDimension > 0)) {
            cells = new Cell[xDimension][yDimension][zDimension];

            int[] position;

            for (int i = 0; i < xDimension; i++) {
                for (int j = 0; j < yDimension; j++) {
                    for (int k = 0; k < zDimension; k++) {
                        position = new int[3];
                        position[0] = i;
                        position[1] = j;
                        position[2] = k;
                        cells[i][j][k] = new Cell(this, position);
                    }
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
    public double getZDimension() {
        return cells[0][0].length;
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
                for (int k = 0; k < cells[0][0].length; k++) {
                    result.addAll(cells[i][j][k].getContents());
                }
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
        if (position.length == 3) {
            if ((position[0] >= 0) && (position[0] < cells.length) &&
                    (position[1] >= 0) && (position[1] < cells[0].length) &&
                    (position[2] >= 0) && (position[2] < cells[0][0].length)) {
                return cells[position[0]][position[1]][position[2]].getContents();
            } else {
                throw new IllegalDimensionException(
                    "The position contents must match environment size.");
            }
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
        if (position.length == 3) {
            if ((position[0] >= 0) && (position[0] < cells.length) &&
                    (position[1] >= 0) && (position[1] < cells[0].length) &&
                    (position[2] >= 0) && (position[2] < cells[0][0].length)) {
                cells[position[0]][position[1]][position[2]].setContents(contents);
            } else {
                throw new IllegalDimensionException(
                    "The position contents must match environment size.");
            }
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
            if ((position[0] >= 0) && (position[0] < cells.length) &&
                    (position[1] >= 0) && (position[1] < cells.length)) {
                Set result;
                result = Collections.EMPTY_SET;

                if (method == DiscreteEnvironment.MOORE_NEIGHBORHOOD) {
                    //todo we can provide a better optimization here
                    if (((position[2] + 1) >= 0) &&
                            ((position[2] + 1) < cells.length)) {
                        result.add(cells[position[0]][position[1]][position[2] +
                            1]);

                        if (((position[0] + 1) >= 0) &&
                                ((position[0] + 1) < cells.length)) {
                            result.add(cells[position[0] + 1][position[1]][position[2] +
                                1]);

                            if (((position[1] - 1) >= 0) &&
                                    ((position[1] - 1) < cells[0].length)) {
                                result.add(cells[position[0] + 1][position[1] -
                                    1][position[2] + 1]);
                            }
                        }

                        if (((position[1] + 1) >= 0) &&
                                ((position[1] + 1) < cells.length)) {
                            result.add(cells[position[0]][position[1] + 1][position[2] +
                                1]);

                            if (((position[0] - 1) >= 0) &&
                                    ((position[0] - 1) < cells[0].length)) {
                                result.add(cells[position[0] - 1][position[1] +
                                    1][position[2] + 1]);
                            }
                        }

                        if (((position[0] - 1) >= 0) &&
                                ((position[0] - 1) < cells.length)) {
                            result.add(cells[position[0] - 1][position[1]][position[2] +
                                1]);

                            if (((position[1] - 1) >= 0) &&
                                    ((position[1] - 1) < cells[0].length)) {
                                result.add(cells[position[0] - 1][position[1] -
                                    1][position[2] + 1]);
                            }
                        }

                        if (((position[1] - 1) >= 0) &&
                                ((position[1] - 1) < cells.length)) {
                            result.add(cells[position[0]][position[1] - 1][position[2] +
                                1]);

                            if (((position[0] - 1) >= 0) &&
                                    ((position[0] - 1) < cells[0].length)) {
                                result.add(cells[position[0] - 1][position[1] -
                                    1][position[2] + 1]);
                            }
                        }
                    }

                    if ((position[2] >= 0) && (position[2] < cells.length)) {
                        if (((position[0] + 1) >= 0) &&
                                ((position[0] + 1) < cells.length)) {
                            result.add(cells[position[0] + 1][position[1]][position[2]]);

                            if (((position[1] - 1) >= 0) &&
                                    ((position[1] - 1) < cells[0].length)) {
                                result.add(cells[position[0] + 1][position[1] -
                                    1][position[2]]);
                            }
                        }

                        if (((position[1] + 1) >= 0) &&
                                ((position[1] + 1) < cells.length)) {
                            result.add(cells[position[0]][position[1] + 1][position[2]]);

                            if (((position[0] - 1) >= 0) &&
                                    ((position[0] - 1) < cells[0].length)) {
                                result.add(cells[position[0] - 1][position[1] +
                                    1][position[2]]);
                            }
                        }

                        if (((position[0] - 1) >= 0) &&
                                ((position[0] - 1) < cells.length)) {
                            result.add(cells[position[0] - 1][position[1]][position[2] +
                                1]);

                            if (((position[1] - 1) >= 0) &&
                                    ((position[1] - 1) < cells[0].length)) {
                                result.add(cells[position[0] - 1][position[1] -
                                    1][position[2]]);
                            }
                        }

                        if (((position[1] - 1) >= 0) &&
                                ((position[1] - 1) < cells.length)) {
                            result.add(cells[position[0]][position[1] - 1][position[2] +
                                1]);

                            if (((position[0] - 1) >= 0) &&
                                    ((position[0] - 1) < cells[0].length)) {
                                result.add(cells[position[0] - 1][position[1] -
                                    1][position[2]]);
                            }
                        }
                    }

                    if (((position[2] + 1) >= 0) &&
                            ((position[2] - 1) < cells.length)) {
                        result.add(cells[position[0]][position[1]][position[2] -
                            1]);

                        if (((position[0] + 1) >= 0) &&
                                ((position[0] + 1) < cells.length)) {
                            result.add(cells[position[0] + 1][position[1]][position[2] -
                                1]);

                            if (((position[1] - 1) >= 0) &&
                                    ((position[1] - 1) < cells[0].length)) {
                                result.add(cells[position[0] + 1][position[1] -
                                    1][position[2] - 1]);
                            }
                        }

                        if (((position[1] + 1) >= 0) &&
                                ((position[1] + 1) < cells.length)) {
                            result.add(cells[position[0]][position[1] + 1][position[2] -
                                1]);

                            if (((position[0] - 1) >= 0) &&
                                    ((position[0] - 1) < cells[0].length)) {
                                result.add(cells[position[0] - 1][position[1] +
                                    1][position[2] - 1]);
                            }
                        }

                        if (((position[0] - 1) >= 0) &&
                                ((position[0] - 1) < cells.length)) {
                            result.add(cells[position[0] - 1][position[1]][position[2] -
                                1]);

                            if (((position[1] - 1) >= 0) &&
                                    ((position[1] - 1) < cells[0].length)) {
                                result.add(cells[position[0] - 1][position[1] -
                                    1][position[2] - 1]);
                            }
                        }

                        if (((position[1] - 1) >= 0) &&
                                ((position[1] - 1) < cells.length)) {
                            result.add(cells[position[0]][position[1] - 1][position[2] -
                                1]);

                            if (((position[0] - 1) >= 0) &&
                                    ((position[0] - 1) < cells[0].length)) {
                                result.add(cells[position[0] - 1][position[1] -
                                    1][position[2] - 1]);
                            }
                        }
                    }

                    return result;
                } else {
                    if (method == DiscreteEnvironment.VON_NEUMANN_NEIGHBORHOOD) {
                        if (((position[0] + 1) >= 0) &&
                                ((position[0] + 1) < cells.length) &&
                                (position[1] >= 0) &&
                                (position[1] < cells[0].length) &&
                                (position[2] >= 0) &&
                                (position[2] < cells[0][0].length)) {
                            result.add(cells[position[0] + 1][position[1]][position[2]]);
                        }

                        if (((position[0] - 1) >= 0) &&
                                ((position[0] - 1) < cells.length) &&
                                (position[1] >= 0) &&
                                (position[1] < cells[0].length) &&
                                (position[2] >= 0) &&
                                (position[2] < cells[0][0].length)) {
                            result.add(cells[position[0] - 1][position[1]][position[2]]);
                        }

                        if ((position[0] >= 0) && (position[0] < cells.length) &&
                                ((position[1] + 1) >= 0) &&
                                ((position[1] + 1) < cells[0].length) &&
                                (position[2] >= 0) &&
                                (position[2] < cells[0][0].length)) {
                            result.add(cells[position[0]][position[1] + 1][position[2]]);
                        }

                        if ((position[0] >= 0) && (position[0] < cells.length) &&
                                ((position[1] - 1) >= 0) &&
                                ((position[1] - 1) < cells[0].length) &&
                                (position[2] >= 0) &&
                                (position[2] < cells[0][0].length)) {
                            result.add(cells[position[0]][position[1] - 1][position[2]]);
                        }

                        if ((position[0] >= 0) && (position[0] < cells.length) &&
                                (position[1] >= 0) &&
                                (position[1] < cells[0].length) &&
                                ((position[2] + 1) >= 0) &&
                                ((position[2] + 1) < cells[0][0].length)) {
                            result.add(cells[position[0]][position[1]][position[2] +
                                1]);
                        }

                        if ((position[0] >= 0) && (position[0] < cells.length) &&
                                (position[1] >= 0) &&
                                (position[1] < cells[0].length) &&
                                ((position[2] - 1) >= 0) &&
                                ((position[2] - 1) < cells[0][0].length)) {
                            result.add(cells[position[0]][position[1]][position[2] -
                                1]);
                        }

                        return result;
                    } else {
                        throw new IllegalArgumentException(
                            "Only Moore and Von Neumann neighborhood are supported.");
                    }
                }
            } else {
                throw new IllegalDimensionException(
                    "The position contents must match environment size.");
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
        return 3;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSurface() {
        return cells.length * cells[0].length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getVolume() {
        return cells.length * cells[0].length * cells[0][0].length;
    }
}
