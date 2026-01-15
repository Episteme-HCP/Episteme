package org.jscience.computing.ai.agents;

import org.jscience.util.IllegalDimensionException;

import java.util.Collections;
import java.util.Set;


/**
 * A class representing a cubic environment whose bounds are curved onto
 * each other.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class CubicTorusEnvironment extends DiscreteEnvironment {
    /**
     * DOCUMENT ME!
     */
    private Cell[][][] cells;

    /**
     * Creates a new CubicTorusEnvironment object.
     *
     * @param xDimension DOCUMENT ME!
     * @param yDimension DOCUMENT ME!
     * @param zDimension DOCUMENT ME!
     */
    public CubicTorusEnvironment(int xDimension, int yDimension, int zDimension) {
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
            return cells[getTorusValue(position[0], cells.length)][getTorusValue(position[1],
                cells[0].length)][getTorusValue(position[2], cells[0][0].length)].getContents();
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
            cells[getTorusValue(position[0], cells.length)][getTorusValue(position[1],
                cells[0].length)][getTorusValue(position[2], cells[0][0].length)].setContents(contents);
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
                        1, cells[0].length)][getTorusValue(position[2] + 1,
                        cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0] + 1, cells.length)][getTorusValue(
                        position[1], cells[0].length)][getTorusValue(position[2] +
                        1, cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0] + 1, cells.length)][getTorusValue(position[1] -
                        1, cells[0].length)][getTorusValue(position[2] + 1,
                        cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0], cells.length)][getTorusValue(position[1] +
                        1, cells[0].length)][getTorusValue(position[2] + 1,
                        cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0], cells.length)][getTorusValue(
                        position[1], cells[0].length)][getTorusValue(position[2] +
                        1, cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0], cells.length)][getTorusValue(position[1] -
                        1, cells[0].length)][getTorusValue(position[2] + 1,
                        cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0] - 1, cells.length)][getTorusValue(position[1] +
                        1, cells[0].length)][getTorusValue(position[2] + 1,
                        cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0] - 1, cells.length)][getTorusValue(
                        position[1], cells[0].length)][getTorusValue(position[2] +
                        1, cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0] - 1, cells.length)][getTorusValue(position[1] -
                        1, cells[0].length)][getTorusValue(position[2] + 1,
                        cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0] + 1, cells.length)][getTorusValue(position[1] +
                        1, cells[0].length)][getTorusValue(position[2],
                        cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0] + 1, cells.length)][getTorusValue(
                        position[1], cells[0].length)][getTorusValue(
                        position[2], cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0] + 1, cells.length)][getTorusValue(position[1] -
                        1, cells[0].length)][getTorusValue(position[2],
                        cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0], cells.length)][getTorusValue(position[1] +
                        1, cells[0].length)][getTorusValue(position[2],
                        cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0], cells.length)][getTorusValue(position[1] -
                        1, cells[0].length)][getTorusValue(position[2],
                        cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0] - 1, cells.length)][getTorusValue(position[1] +
                        1, cells[0].length)][getTorusValue(position[2],
                        cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0] - 1, cells.length)][getTorusValue(
                        position[1], cells[0].length)][getTorusValue(
                        position[2], cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0] - 1, cells.length)][getTorusValue(position[1] -
                        1, cells[0].length)][getTorusValue(position[2],
                        cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0] + 1, cells.length)][getTorusValue(position[1] +
                        1, cells[0].length)][getTorusValue(position[2] - 1,
                        cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0] + 1, cells.length)][getTorusValue(
                        position[1], cells[0].length)][getTorusValue(position[2] -
                        1, cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0] + 1, cells.length)][getTorusValue(position[1] -
                        1, cells[0].length)][getTorusValue(position[2] - 1,
                        cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0], cells.length)][getTorusValue(position[1] +
                        1, cells[0].length)][getTorusValue(position[2] - 1,
                        cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0], cells.length)][getTorusValue(
                        position[1], cells[0].length)][getTorusValue(position[2] -
                        1, cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0], cells.length)][getTorusValue(position[1] -
                        1, cells[0].length)][getTorusValue(position[2] - 1,
                        cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0] - 1, cells.length)][getTorusValue(position[1] +
                        1, cells[0].length)][getTorusValue(position[2] - 1,
                        cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0] - 1, cells.length)][getTorusValue(
                        position[1], cells[0].length)][getTorusValue(position[2] -
                        1, cells[0][0].length)]);
                result.add(cells[getTorusValue(position[0] - 1, cells.length)][getTorusValue(position[1] -
                        1, cells[0].length)][getTorusValue(position[2] - 1,
                        cells[0][0].length)]);

                return result;
            } else {
                if (method == DiscreteEnvironment.VON_NEUMANN_NEIGHBORHOOD) {
                    result.add(cells[getTorusValue(position[0], cells.length)][getTorusValue(position[1] +
                            1, cells[0].length)][getTorusValue(position[2],
                            cells[0][0].length)]);
                    result.add(cells[getTorusValue(position[0] + 1, cells.length)][getTorusValue(
                            position[1], cells[0].length)][getTorusValue(
                            position[2], cells[0][0].length)]);
                    result.add(cells[getTorusValue(position[0] - 1, cells.length)][getTorusValue(
                            position[1], cells[0].length)][getTorusValue(
                            position[2], cells[0][0].length)]);
                    result.add(cells[getTorusValue(position[0], cells.length)][getTorusValue(position[1] -
                            1, cells[0].length)][getTorusValue(position[2],
                            cells[0][0].length)]);
                    result.add(cells[getTorusValue(position[0], cells.length)][getTorusValue(
                            position[1], cells[0].length)][getTorusValue(position[2] +
                            1, cells[0][0].length)]);
                    result.add(cells[getTorusValue(position[0], cells.length)][getTorusValue(
                            position[1], cells[0].length)][getTorusValue(position[2] -
                            1, cells[0][0].length)]);

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
        return 3;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSurface() {
        return Double.POSITIVE_INFINITY; //cells.length * cells[0].length
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getVolume() {
        return Double.POSITIVE_INFINITY; //cells.length * cells[0].length * cells[0][0].length
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
