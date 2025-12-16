package org.jscience.ui.plotting;

import org.jscience.mathematics.analysis.Function;
import org.jscience.mathematics.numbers.real.Real;
import java.util.List;

/**
 * 2D plotting interface for JScience.
 * <p>
 * Backend-agnostic API. Supports XChart, JavaFX, JFreeChart backends.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public interface Plot2D {

    /**
     * Adds function to plot: y = f(x)
     */
    Plot2D addFunction(Function<Real, Real> function, Real xMin, Real xMax, String label);

    /**
     * Adds discrete data points.
     */
    Plot2D addData(List<Real> xData, List<Real> yData, String label);

    /**
     * Sets plot title.
     */
    Plot2D setTitle(String title);

    /**
     * Sets axis labels.
     */
    Plot2D setAxisLabels(String xLabel, String yLabel);

    /**
     * Sets grid visibility.
     */
    Plot2D setGrid(boolean enabled);

    /**
     * Sets legend visibility.
     */
    Plot2D setLegend(boolean enabled);

    /**
     * Sets axis ranges.
     */
    Plot2D setXRange(Real min, Real max);

    Plot2D setYRange(Real min, Real max);

    /**
     * Displays plot in window.
     */
    void show();

    /**
     * Saves plot to file (PNG, PDF, SVG supported).
     */
    void save(String filename, PlotFormat format);

    /**
     * Returns underlying backend implementation.
     */
    PlottingBackend getBackend();

    /**
     * Series style options.
     */
    public static class SeriesStyle {
        public enum Type {
            LINE, SCATTER, BAR, AREA
        }

        public enum MarkerStyle {
            CIRCLE, SQUARE, DIAMOND, NONE
        }

        public Type type = Type.LINE;
        public MarkerStyle marker = MarkerStyle.NONE;
        public int lineWidth = 2;
        public String color = null; // null = auto

        public static SeriesStyle line() {
            return new SeriesStyle();
        }

        public static SeriesStyle scatter() {
            SeriesStyle style = new SeriesStyle();
            style.type = Type.SCATTER;
            style.marker = MarkerStyle.CIRCLE;
            return style;
        }
    }

    /**
     * Adds styled series.
     */
    Plot2D addSeries(List<Real> xData, List<Real> yData, String label, SeriesStyle style);
}

