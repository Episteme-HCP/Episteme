package org.jscience.ui;

import javafx.stage.Stage;

/**
 * Interface for modules to provide their own demos to the Master Demo
 * application. * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 
 */
public interface DemoProvider {
    /**
     * Returns the name of the section (e.g., "Social Sciences", "Economics").
     */
    String getCategory();

    /**
     * Returns the name of the demo.
     */
    String getName();

    /**
     * Launches the demo.
     */
    void show(Stage stage);
}
