package org.jscience.computing.ai.agents;

/**
 * A class representing an hexagonal flat cell.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class HexagonalCell extends Cell {
    /** DOCUMENT ME! */
    private double size;

    /** DOCUMENT ME! */
    private double angle;

/**
     * Creates a new HexagonalCell object.
     *
     * @param environment DOCUMENT ME!
     * @param position    DOCUMENT ME!
     * @param size        DOCUMENT ME!
     * @param angle       DOCUMENT ME!
     */
    public HexagonalCell(DiscreteEnvironment environment, int[] position,
        double size, double angle) {
        super(environment, position);

        if (size > 0) {
            this.size = size;
            this.angle = angle;
        } else {
            throw new IllegalArgumentException(
                "Size must be strictly positive.");
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
    public double getAngle() {
        return angle;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSurface() {
        return (size * size * 3 * Math.sqrt(3)) / 2;
    }
}
