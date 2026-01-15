package org.jscience.awt;

import java.awt.*;


/**
 * This interface allows the user to define a custom color scheme for plots
 * such as ContourPlot.
 *
 * @author Daniel Lemire
 */
public interface ColorScheme {
    /**
     * By convention, this should return a color for f between 0 and 1.
     * Exceptions should be throw for out of bounds values.
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Color getColor(float f);
}
