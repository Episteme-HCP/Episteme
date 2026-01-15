package org.jscience.swing;

import org.jscience.awt.Graph2D;
import org.jscience.awt.Graph2DModel;


/**
 * A scatter graph Swing component.
 *
 * @author Mark Hale
 * @version 1.3
 */
public class JScatterGraph extends JGraph2D {
/**
     * Constructs a scatter graph.
     *
     * @param gm DOCUMENT ME!
     */
    public JScatterGraph(Graph2DModel gm) {
        super(gm);
        dataMarker = new Graph2D.DataMarker.Square(3);
    }
}
