package org.jscience.computing.ai.agents;

/**
 * A class representing a triangular flat cell.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class TriangularCell extends Cell {
    /** DOCUMENT ME! */
    private double side1Length;

    /** DOCUMENT ME! */
    private double side2Length;

    /** DOCUMENT ME! */
    private double side3Length;

/**
     * Creates a new TriangularCell object.
     *
     * @param environment DOCUMENT ME!
     * @param position    DOCUMENT ME!
     * @param side1Length DOCUMENT ME!
     * @param side2Length DOCUMENT ME!
     * @param side3Length DOCUMENT ME!
     */
    public TriangularCell(DiscreteEnvironment environment, int[] position,
        double side1Length, double side2Length, double side3Length) {
        super(environment, position);

        if ((side1Length > 0) && (side2Length > 0)) {
            this.side1Length = side1Length;
            this.side2Length = side2Length;
            this.side3Length = side3Length;
        } else {
            throw new IllegalArgumentException(
                "Side lengths must be strictly positive.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSide1Length() {
        return side1Length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSide2Length() {
        return side2Length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSide3Length() {
        return side3Length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSurface() {
        //using heron formula, also look at http://en.wikipedia.org/wiki/Triangle
        double s = (side1Length + side2Length + side3Length) / 2;

        return Math.sqrt(s * (s - side1Length) * (s - side2Length) * (s -
            side3Length));
    }
}
