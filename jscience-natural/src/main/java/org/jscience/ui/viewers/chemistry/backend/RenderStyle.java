package org.jscience.ui.viewers.chemistry.backend;

/**
 * Standard styles for molecular visualization.
 */
public enum RenderStyle {
    /**
     * Atoms are rendered as large spheres representing van der Waals radii.
     * Bonds are usually hidden or implicit.
     */
    SPACEFILL,

    /**
     * Atoms are smaller spheres.
     * Bonds are cylinders connecting them.
     */
    BALL_AND_STICK,

    /**
     * Atoms are not rendered or very small.
     * Bonds are thin lines.
     */
    WIREFRAME
}
