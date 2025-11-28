package org.jscience.visualization;

/**
 * Supported plotting backends.
 */
public enum PlottingBackend {
    /**
     * XChart - Modern, lightweight (recommended).
     */
    XCHART,

    /**
     * JavaFX - Built-in, always available (fallback).
     */
    JAVAFX,

    /**
     * JFreeChart - Feature-rich, publication quality (heavy).
     */
    JFREECHART,

    /**
     * Auto-detect best available backend.
     */
    AUTO
}
