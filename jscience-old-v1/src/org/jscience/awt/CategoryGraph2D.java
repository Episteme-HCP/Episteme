package org.jscience.awt;

import java.awt.*;


/**
 * The CategoryGraph2D superclass provides an abstract encapsulation of 2D
 * category graphs.
 *
 * @author Mark Hale
 * @version 1.2
 */
public abstract class CategoryGraph2D extends DoubleBufferedCanvas
    implements GraphDataListener {
    /** Data model. */
    protected CategoryGraph2DModel model;

    /** Origin. */
    protected Point origin = new Point();

    /** Padding. */
    protected final int scalePad = 5;

    /** DOCUMENT ME! */
    protected final int axisPad = 25;

    /** DOCUMENT ME! */
    protected int leftAxisPad;

/**
     * Constructs a 2D category graph.
     *
     * @param cgm DOCUMENT ME!
     */
    public CategoryGraph2D(CategoryGraph2DModel cgm) {
        model = cgm;
        model.addGraphDataListener(this);
    }

    /**
     * Sets the data plotted by this graph to the specified data.
     *
     * @param cgm DOCUMENT ME!
     */
    public final void setModel(CategoryGraph2DModel cgm) {
        model.removeGraphDataListener(this);
        model = cgm;
        model.addGraphDataListener(this);
        dataChanged(new GraphDataEvent(model));
    }

    /**
     * Returns the model used by this graph.
     *
     * @return DOCUMENT ME!
     */
    public final CategoryGraph2DModel getModel() {
        return model;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public abstract void dataChanged(GraphDataEvent e);

    /**
     * Returns the preferred size of this component.
     *
     * @return DOCUMENT ME!
     */
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    /**
     * Returns the minimum size of this component.
     *
     * @return DOCUMENT ME!
     */
    public Dimension getMinimumSize() {
        return new Dimension(200, 200);
    }
}
