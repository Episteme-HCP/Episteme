import org.jscience.awt.ContourPlot;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * Sample program demonstrating use of the Swing/AWT contour plot
 * component.
 *
 * @author Mark Hale
 * @version 1.0
 */
public class ContourPlotDemo extends Frame {
    /**
     * Creates a new ContourPlotDemo object.
     */
    public ContourPlotDemo() {
        super("JScience Contour Plot Demo");
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                    dispose();
                    System.exit(0);
                }
            });
        setSize(250, 250);
        add(new ContourPlot(createContourData()));
        setVisible(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        new ContourPlotDemo();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static double[][] createContourData() {
        double[][] data = new double[50][50];
        double x;
        double y;

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                x = ((i - (data.length / 2.0)) * 3.0) / data.length;
                y = ((j - (data[0].length / 2.0)) * 3.0) / data[0].length;
                data[i][j] = Math.exp((-x * x) - (y * y));
            }
        }

        return data;
    }
}
