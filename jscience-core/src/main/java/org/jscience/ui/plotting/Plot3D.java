package org.jscience.ui.plotting;

import org.jscience.mathematics.analysis.Function;
import org.jscience.mathematics.numbers.real.Real;
import java.util.List;

/**
 * 3D plotting interface for JScience.
 * <p>
 * Surface plots, 3D scatter, mesh visualization.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public interface Plot3D {

    /**
     * Adds surface: z = f(x, y)
     */
    Plot3D addSurface(Function<Real[], Real> function,
            Real xMin, Real xMax,
            Real yMin, Real yMax,
            String label);

    /**
     * Adds 3D scatter data.
     */
    Plot3D addScatter(List<Real> xData, List<Real> yData, List<Real> zData, String label);

    /**
     * Adds parametric curve: (x(t), y(t), z(t))
     */
    Plot3D addParametricCurve(Function<Real, Real> xFunc,
            Function<Real, Real> yFunc,
            Function<Real, Real> zFunc,
            Real tMin, Real tMax,
            String label);

    /**
     * Sets plot title.
     */
    Plot3D setTitle(String title);

    /**
     * Sets axis labels.
     */
    Plot3D setAxisLabels(String xLabel, String yLabel, String zLabel);

    /**
     * Sets camera position (azimuth, elevation).
     */
    Plot3D setViewAngle(double azimuth, double elevation);

    /**
     * Enables/disables interactive rotation.
     */
    Plot3D setInteractive(boolean enabled);

    /**
     * Sets colormap for surface.
     */
    Plot3D setColormap(Colormap colormap);

    /**
     * Displays plot.
     */
    void show();

    /**
     * Saves plot.
     */
    void save(String filename, PlotFormat format);

    /**
     * Predefined colormaps.
     */
    public enum Colormap {
        JET, // Rainbow (blue -> red)
        VIRIDIS, // Perceptually uniform
        PLASMA, // High contrast
        GRAYSCALE, // Black to white
        HOT, // Black -> red -> yellow -> white
        COOL // Cyan to magenta
    }
}

