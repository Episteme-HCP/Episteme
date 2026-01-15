import org.jscience.awt.Graph2DModel;
import org.jscience.awt.LineTrace;

import org.jscience.mathematics.polynomials.RealPolynomial;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * Sample program demonstrating use of LinearMath.leastSquaresFit method
 * and the LineTrace graph class.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class CurveFitter extends Frame {
    /**
     * DOCUMENT ME!
     */
    private Label fnLabel = new Label("P(x) = ?", Label.CENTER);

    /**
     * DOCUMENT ME!
     */
    private LineTrace graph = new LineTrace(-10.0f, 10.0f, -10.0f, 10.0f);

    /**
     * DOCUMENT ME!
     */
    private TextField polyDegreeField = new TextField("4");

    /**
     * DOCUMENT ME!
     */
    private Button fitButton = new Button("Fit");

    /**
     * DOCUMENT ME!
     */
    private Button clearButton = new Button("Clear");

    /**
     * Creates a new CurveFitter object.
     */
    public CurveFitter() {
        super("Curve Fitter");
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                    dispose();
                    System.exit(0);
                }
            });
        fitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    fitCurve();
                }
            });
        clearButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    graph.clear();
                }
            });
        add(fnLabel, "North");
        add(graph, "Center");

        Panel buttonPanel = new Panel();
        buttonPanel.add(new Label("Polynomial degree:"));
        buttonPanel.add(polyDegreeField);
        buttonPanel.add(fitButton);
        buttonPanel.add(clearButton);
        add(buttonPanel, "South");
        setSize(500, 400);
        setVisible(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        new CurveFitter();
    }

    /**
     * DOCUMENT ME!
     */
    private void fitCurve() {
        Graph2DModel model = graph.getModel();
        model.firstSeries();

        double[][] data = new double[2][model.seriesLength()];

        for (int i = 0; i < data[0].length; i++) {
            data[0][i] = model.getXCoord(i);
            data[1][i] = model.getYCoord(i);
        }

        int degree = Integer.parseInt(polyDegreeField.getText());
        RealPolynomial poly = LinearMath.leastSquaresFit(degree, data);
        fnLabel.setText(poly.toString());
    }
}
