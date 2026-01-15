package org.jscience.awt;

import java.awt.*;


/**
 * A line graph AWT component.
 *
 * @author Mark Hale
 * @version 1.3
 */
public class LineGraph extends Graph2D {
/**
     * Constructs a line graph.
     *
     * @param gm DOCUMENT ME!
     */
    public LineGraph(Graph2DModel gm) {
        super(gm);
    }

    /**
     * Paints the graph.
     *
     * @param g DOCUMENT ME!
     */
    protected void offscreenPaint(Graphics g) {
        // lines
        Point p1;

        // lines
        Point p2;
        int i;
        model.firstSeries();

        if (model.seriesLength() > 0) {
            g.setColor(seriesColor[0]);
            p1 = dataToScreen(model.getXCoord(0), model.getYCoord(0));

            for (i = 1; i < model.seriesLength(); i++) {
                p2 = dataToScreen(model.getXCoord(i), model.getYCoord(i));
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
                p1 = p2;
            }
        }

        for (int n = 1; model.nextSeries(); n++) {
            if (model.seriesLength() > 0) {
                g.setColor(seriesColor[n]);
                p1 = dataToScreen(model.getXCoord(0), model.getYCoord(0));

                for (i = 1; i < model.seriesLength(); i++) {
                    p2 = dataToScreen(model.getXCoord(i), model.getYCoord(i));
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                    p1 = p2;
                }
            }
        }

        super.offscreenPaint(g);
    }
}
