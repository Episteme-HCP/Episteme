package org.jscience.geography;

import org.jscience.geography.coordinates.Coord;

import org.jscience.mathematics.algebraic.numbers.Double;
import org.jscience.mathematics.analysis.Interval;
import org.jscience.mathematics.analysis.IntervalsList;


/**
 * A class used to define a boundary that changes according to time frame.
 * This is to be used for things that move.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//ok may be this class should extend Object as it is basically of pairs Interval/Boundary
//yet this is done this way as to enable seamless support for places that expect boundary
public class TimedBoundary extends Boundary {
    /** DOCUMENT ME! */
    private IntervalsList dates;

    /** DOCUMENT ME! */
    private Coord[][] coords;

    /** DOCUMENT ME! */
    private boolean[][] boundariesIncluded;

/**
     * Creates a new TimedBoundary object.
     *
     * @param coords DOCUMENT ME!
     */
    public TimedBoundary(Coord[] coords) {
        super(coords);

        if ((coords != null) && (coords.length > 0)) {
            this.coords = new Coord[1][];
            this.dates = new IntervalsList(new Interval(
                        new Double(Double.NEGATIVE_INFINITY),
                        new Double(Double.POSITIVE_INFINITY)));
            System.arraycopy(coords, 0, this.coords[1], 0, coords.length);
            this.boundariesIncluded = new boolean[1][];

            for (int i = 0; i < boundariesIncluded.length; i++) {
                this.boundariesIncluded[0][i] = true;
            }
        } else {
            throw new IllegalArgumentException(
                "The TimedBoundary constructor can't have null or empty arguments.");
        }
    }

/**
     * Creates a new TimedBoundary object.
     *
     * @param coords             DOCUMENT ME!
     * @param boundariesIncluded DOCUMENT ME!
     */
    public TimedBoundary(Coord[] coords, boolean[] boundariesIncluded) {
        super(coords, boundariesIncluded);

        if ((coords != null) && (coords.length > 0) &&
                (boundariesIncluded.length > 0) &&
                (boundariesIncluded.length == coords.length)) {
            this.coords = new Coord[1][];
            this.dates = new IntervalsList(new Interval(
                        new Double(Double.NEGATIVE_INFINITY),
                        new Double(Double.POSITIVE_INFINITY)));
            System.arraycopy(coords, 0, this.coords[0], 0, coords.length);
            this.boundariesIncluded = new boolean[1][];
            System.arraycopy(boundariesIncluded, 0, this.boundariesIncluded[0],
                0, boundariesIncluded.length);
        } else {
            throw new IllegalArgumentException(
                "The TimedBoundary constructor can't have null or empty arguments.");
        }
    }

    //may be we should also check that every element from coord[] is of length>0
    /**
     * Creates a new TimedBoundary object.
     *
     * @param dates DOCUMENT ME!
     * @param coords DOCUMENT ME!
     */
    public TimedBoundary(IntervalsList dates, Coord[][] coords) {
        super(coords[0]);

        boolean[] boundariesIncluded1;

        if ((dates != null) && (dates.getSize() > 0) && (coords != null) &&
                (coords.length > 0) && (coords[0].length > 0) &&
                (coords.length == dates.getSize())) {
            this.coords = coords;
            boundariesIncluded = new boolean[coords.length][];

            for (int i = 0; i < coords.length; i++) {
                boundariesIncluded1 = new boolean[coords[i].length];

                for (int j = 0; j < boundariesIncluded1.length; j++) {
                    boundariesIncluded1[j] = true;
                }

                boundariesIncluded[i] = boundariesIncluded1;
            }
        } else {
            throw new IllegalArgumentException(
                "The TimedBoundary constructor can't have null or empty arguments.");
        }
    }

    //may be we should also check that every element from coord[] and from boundariesIncluded[] is of length>0
    /**
     * Creates a new TimedBoundary object.
     *
     * @param dates DOCUMENT ME!
     * @param coords DOCUMENT ME!
     * @param boundariesIncluded DOCUMENT ME!
     */
    public TimedBoundary(IntervalsList dates, Coord[][] coords,
        boolean[][] boundariesIncluded) {
        super(coords[0], boundariesIncluded[0]);

        boolean valid;
        int i;

        if ((dates != null) && (dates.getSize() > 0) && (coords != null) &&
                (coords.length > 0) && (coords[0].length > 0) &&
                (coords.length == dates.getSize()) &&
                (boundariesIncluded.length > 0) &&
                (boundariesIncluded[0].length > 0) &&
                (boundariesIncluded.length == dates.getSize())) {
            this.coords = coords;
            valid = true;
            i = 0;

            while ((i < coords.length) && (valid)) {
                valid = (boundariesIncluded[i].length == coords[i].length);
            }

            if (valid) {
                this.boundariesIncluded = boundariesIncluded;
            } else {
                throw new IllegalArgumentException(
                    "The length of each boundariesIncluded element must match the corresponding coords element.");
            }
        } else {
            throw new IllegalArgumentException(
                "The TimedBoundary constructor can't have null or empty arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Boundary getDefaultBoundary() {
        return new Boundary(coords[0], boundariesIncluded[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IndexOutOfBoundsException DOCUMENT ME!
     */
    public Boundary getBoundary(int i) {
        if ((i >= 0) && (i < dates.getSize())) {
            return new Boundary(coords[i], boundariesIncluded[i]);
        } else {
            throw new IndexOutOfBoundsException(
                "You can't get the Boundary for this interval.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IntervalsList getDates() {
        return dates;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Coord[][] getDatedCoords() {
        return coords;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean[][] getDatedBoundariesInclusion() {
        return boundariesIncluded;
    }

    // we have to take care of overridding every method
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getPosition() {
        return getDefaultBoundary().getPosition();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Coord[] getCoords() {
        return getDefaultBoundary().getCoords();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean[] getBoundariesInclusion() {
        return getDefaultBoundary().getBoundariesInclusion();
    }

    //explicitely unsupported
    /**
     * DOCUMENT ME!
     *
     * @param boundary DOCUMENT ME!
     */
    public void union(Boundary boundary) {
    }

    //explicitely unsupported
    /**
     * DOCUMENT ME!
     *
     * @param boundary DOCUMENT ME!
     */
    public void intersection(Boundary boundary) {
    }
}
