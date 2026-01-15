package org.jscience.computing.ai.agents;

/**
 * A class representing a cubic cell.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class CubicCell extends Cell {
    /** DOCUMENT ME! */
    private double size;

/**
     * Creates a new CubicCell object.
     *
     * @param environment DOCUMENT ME!
     * @param position    DOCUMENT ME!
     * @param size        DOCUMENT ME!
     */
    public CubicCell(DiscreteEnvironment environment, int[] position,
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
    public double getVolume() {
        return size * size * size;
    }
}
