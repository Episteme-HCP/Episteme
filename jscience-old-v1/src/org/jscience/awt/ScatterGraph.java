package org.jscience.awt;

/**
 * A scatter graph AWT component.
 *
 * @author Mark Hale
 * @version 1.3
 */
public class ScatterGraph extends Graph2D {
/**
     * Constructs a scatter graph.
     *
     * @param gm DOCUMENT ME!
     */
    public ScatterGraph(Graph2DModel gm) {
        super(gm);
        dataMarker = new Graph2D.DataMarker.Square(3);
    }
}
