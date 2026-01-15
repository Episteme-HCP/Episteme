import org.jscience.awt.DefaultGraph2DModel;
import org.jscience.awt.ScatterGraph;

import org.jscience.mathematics.chaos.*;

import java.applet.Applet;

import java.awt.*;


/**
 * Plot of the cat map.
 *
 * @author Mark Hale
 * @version 1.1
 */
public final class CatPlot extends Applet {
    /**
     * DOCUMENT ME!
     */
    private CatMap cm;

    /**
     * DOCUMENT ME!
     */
    private ScatterGraph graph;

    /**
     * DOCUMENT ME!
     */
    private final int N = 10000;

    /**
     * DOCUMENT ME!
     */
    public void init() {
        cm = new CatMap();

        float[] xData = new float[N];
        float[] yData = new float[N];
        double[] x = { 0.1, 0.1 };

        for (int i = 0; i < N; i++) {
            xData[i] = (float) x[0];
            yData[i] = (float) x[1];
            x = cm.map(x);
        }

        DefaultGraph2DModel model = new DefaultGraph2DModel();
        model.setXAxis(xData);
        model.addSeries(yData);
        graph = new ScatterGraph(model);
        graph.setNumbering(false);
        setLayout(new BorderLayout());
        add(graph, "Center");
    }
}
