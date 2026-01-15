package org.jscience.mathematics.analysis.chaos;

/**
 * The CantorDust class provides an object that encapsulates the Cantor
 * dust fractal.
 *
 * @author Mark Hale
 * @version 0.1
 */
public abstract class CantorDust extends Object {
/**
     * Creates a new CantorDust object.
     */
    public CantorDust() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double hausdorffDimension() {
        return Math.log(2.0) / Math.log(3.0);
    }

    /**
     * The recursive algorithm for Cantor dust.
     *
     * @param start the start of the line.
     * @param end the end of the line.
     * @param n the number of recursions.
     */
    public void recurse(double start, double end, int n) {
        if (n == 0) {
            return;
        }

        final double l_3 = (end - start) / 3.0;
        eraseLine(start + l_3, end - l_3);
        recurse(start, start + l_3, n - 1);
        recurse(end - l_3, end, n - 1);
    }

    /**
     * Erases a line segment. This need not be an actual graphical
     * operation, but some corresponding abstract operation.
     *
     * @param start DOCUMENT ME!
     * @param end DOCUMENT ME!
     */
    protected abstract void eraseLine(double start, double end);
}
