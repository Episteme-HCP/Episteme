package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.physics.thermodynamics.ThermodynamicsViewer;

public class ThermodynamicsDemo implements DemoProvider {
    @Override
    public String getName() {
        return "Thermodynamics";
    }

    @Override
    public String getDescription() {
        return "Visualizes thermodynamic cycles (Carnot, Otto, Diesel) and efficiency.";
    }

    @Override
    public String getCategory() {
        return "Physics";
    }

    @Override
    public void show(Stage stage) {
        new ThermodynamicsViewer().start(stage);
    }
}
