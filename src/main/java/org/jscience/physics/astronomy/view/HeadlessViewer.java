package org.jscience.physics.astronomy.view;

import org.jscience.physics.astronomy.StarSystem;

/**
 * Headless implementation of StarSystemViewer (for testing/server
 * environments).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class HeadlessViewer implements StarSystemViewer {

    private StarSystem system;

    @Override
    public void display(StarSystem system) {
        this.system = system;
        System.out.println("Displaying StarSystem: " + system.getName());
        System.out.println("Number of bodies: " + system.getBodies().size());
    }

    @Override
    public void update() {
        if (system != null) {
            // In a real viewer, this would render the frame.
            // Here we just acknowledge the update.
        }
    }

    @Override
    public void setTimeScale(double scale) {
        System.out.println("Time scale set to: " + scale);
    }
}
