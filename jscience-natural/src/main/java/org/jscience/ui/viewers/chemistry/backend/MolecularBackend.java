package org.jscience.ui.viewers.chemistry.backend;

/**
 * Enumeration of available molecular rendering backends.
 * Similar to {@link org.jscience.ui.plotting.PlottingBackend}
 */
public enum MolecularBackend {
    /**
     * JavaFX - Native 3D rendering (Default).
     * Always available.
     */
    JAVAFX(true, true),

    /**
     * Jmol - Advanced chemical viewer (Optional).
     * Requires Jmol libraries.
     */
    JMOL(true, true),

    /**
     * PyMOL - External integration (Optional).
     */
    PYMOL(true, true),

    /**
     * Auto-detect best available backend.
     */
    AUTO(true, true);

    private final boolean support3D;
    private final boolean supportInteractive;

    MolecularBackend(boolean support3D, boolean supportInteractive) {
        this.support3D = support3D;
        this.supportInteractive = supportInteractive;
    }

    public boolean isSupport3D() {
        return support3D;
    }

    public boolean isSupportInteractive() {
        return supportInteractive;
    }
}
