package org.jscience.awt;

import org.jscience.mathematics.algebraic.numbers.Complex;


/**
 * The ArgandDiagramModel provides a convenient implementation of the
 * Graph2DModel interface for creating Argand diagrams using the LineGraph
 * component.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class ArgandDiagramModel extends AbstractGraphModel
    implements Graph2DModel {
    /** DOCUMENT ME! */
    private Complex[] data = new Complex[0];

/**
     * Creates a new ArgandDiagramModel object.
     */
    public ArgandDiagramModel() {
    }

    /**
     * Sets the list of complex numbers to be plotted.
     *
     * @param z DOCUMENT ME!
     */
    public void setData(Complex[] z) {
        if (data.length != z.length) {
            data = new Complex[z.length];
        }

        System.arraycopy(z, 0, data, 0, z.length);
        fireGraphDataChanged();
    }

    // Graph2DModel interface
    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getXCoord(int i) {
        return (float) data[i].real();
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getYCoord(int i) {
        return (float) data[i].imag();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int seriesLength() {
        return data.length;
    }

    /**
     * DOCUMENT ME!
     */
    public void firstSeries() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean nextSeries() {
        return false;
    }
}
