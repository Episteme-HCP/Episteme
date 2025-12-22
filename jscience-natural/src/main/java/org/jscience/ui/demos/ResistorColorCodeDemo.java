package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.engineering.components.ResistorColorCodeViewer;

public class ResistorColorCodeDemo implements DemoProvider {
    @Override
    public String getName() {
        return "Resistor Color Codes";
    }

    @Override
    public String getDescription() {
        return "Calculates resistor values from color bands.";
    }

    @Override
    public String getCategory() {
        return "Engineering";
    }

    @Override
    public void show(Stage stage) {
        new ResistorColorCodeViewer().start(stage);
    }
}
