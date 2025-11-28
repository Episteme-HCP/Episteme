package org.jscience.visualization;

import org.jscience.visualization.backends.JavaFXPlot2D;

/**
 * Factory for creating plots with specified backend.
 * <p>
 * Auto-detects best available backend if not specified.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class PlotFactory {

    private static PlottingBackend defaultBackend = PlottingBackend.AUTO;

    /**
     * Sets default backend for future plots.
     */
    public static void setDefaultBackend(PlottingBackend backend) {
        defaultBackend = backend;
    }

    /**
     * Creates 2D plot with default backend.
     */
    public static Plot2D create2D(String title) {
        return create2D(title, defaultBackend);
    }

    /**
     * Creates 2D plot with specified backend.
     */
    public static Plot2D create2D(String title, PlottingBackend backend) {
        if (backend == PlottingBackend.AUTO) {
            backend = detectBestBackend();
        }

        switch (backend) {
            case XCHART:
                return createXChartPlot(title);
            case JFREECHART:
                return createJFreeChartPlot(title);
            case JAVAFX:
            default:
                return new JavaFXPlot2D(title);
        }
    }

    /**
     * Creates 3D plot with default backend.
     */
    public static Plot3D create3D(String title) {
        return create3D(title, PlottingBackend.AUTO);
    }

    /**
     * Creates 3D plot with specified backend.
     */
    public static Plot3D create3D(String title, PlottingBackend backend) {
        // For now, 3D defaults to JavaFX or JZY3D if available
        // TODO: Implement JZY3D backend
        throw new UnsupportedOperationException("3D plotting coming soon");
    }

    /**
     * Auto-detects best available plotting backend.
     */
    private static PlottingBackend detectBestBackend() {
        // Try XChart first (modern, lightweight)
        if (isClassAvailable("org.knowm.xchart.XYChart")) {
            return PlottingBackend.XCHART;
        }

        // Try JFreeChart (feature-rich)
        if (isClassAvailable("org.jfree.chart.JFreeChart")) {
            return PlottingBackend.JFREECHART;
        }

        // Fallback to JavaFX (always available in Java 8+)
        return PlottingBackend.JAVAFX;
    }

    /**
     * Checks if class is available (backend library present).
     */
    private static boolean isClassAvailable(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static Plot2D createXChartPlot(String title) {
        try {
            Class<?> clazz = Class.forName("org.jscience.visualization.backends.XChartPlot2D");
            return (Plot2D) clazz.getConstructor(String.class).newInstance(title);
        } catch (Exception e) {
            System.err.println("XChart backend not available, falling back to JavaFX");
            return new JavaFXPlot2D(title);
        }
    }

    private static Plot2D createJFreeChartPlot(String title) {
        try {
            Class<?> clazz = Class.forName("org.jscience.visualization.backends.JFreeChartPlot2D");
            return (Plot2D) clazz.getConstructor(String.class).newInstance(title);
        } catch (Exception e) {
            System.err.println("JFreeChart backend not available, falling back to JavaFX");
            return new JavaFXPlot2D(title);
        }
    }
}
