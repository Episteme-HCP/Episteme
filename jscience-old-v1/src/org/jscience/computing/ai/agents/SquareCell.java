package org.jscience.computing.ai.agents;

/**
 * A class representing a square flat cell.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class SquareCell extends Cell {
    /** DOCUMENT ME! */
    private double size;

/**
     * Creates a new SquareCell object.
     *
     * @param environment DOCUMENT ME!
     * @param position    DOCUMENT ME!
     * @param size        DOCUMENT ME!
     */
    public SquareCell(DiscreteEnvironment environment, int[] position,
        double size) {
        super(environment, position);

        if (size > 0) {
            this.size = size;
        } else {
            throw new IllegalArgumentException("Size must be strctly positive.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSize() {
        return size;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSurface() {
        return size * size;
    }
}
