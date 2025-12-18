package org.jscience.physics.astronomy.view;

import org.jscience.physics.astronomy.StarSystem;

/**
 * Interface for 3D visualization of Star Systems.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public interface StarSystemViewer {

    /**
     * Display the given StarSystem.
     * 
     * @param system the system to view
     */
    void display(StarSystem system);

    /**
     * Updates the view (e.g. for animation steps).
     */
    void update();

    /**
     * Sets the time scale for simulation visualization.
     * 
     * @param scale 1.0 = real time
     */
    void setTimeScale(double scale);
}
