import org.jscience.awt.DefaultGraph2DModel;
import org.jscience.awt.ScatterGraph;

import org.jscience.mathematics.MathConstants;
import org.jscience.mathematics.chaos.*;

import java.applet.Applet;

import java.awt.*;


/**
 * Plot of the Standard map.
 *
 * @author Mark Hale
 * @version 1.1
 */
public final class StandardPlot extends Applet {
    /**
     * DOCUMENT ME!
     */
    private StandardMap cm;

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
        cm = new StandardMap(2.5);

        float[] xData = new float[N];
        float[] yData = new float[N];
        double[] x = { 0.1, 0.1 };

        for (int i = 0; i < N; i++) {
            xData[i] = (float) ((x[0] < 0.0) ? (x[0] + MathConstants.TWO_PI)
                                             : x[0]);
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
