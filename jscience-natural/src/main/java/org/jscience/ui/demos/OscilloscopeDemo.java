package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.engineering.instruments.OscilloscopeViewer;

public class OscilloscopeDemo implements DemoProvider {
    @Override
    public String getName() {
        return "Oscilloscope";
    }

    @Override
    public String getDescription() {
        return "Simulates an electronic oscilloscope displaying waveforms.";
    }

    @Override
    public String getCategory() {
        return "Engineering";
    }

    @Override
    public void show(Stage stage) {
        new OscilloscopeViewer().start(stage);
    }
}
