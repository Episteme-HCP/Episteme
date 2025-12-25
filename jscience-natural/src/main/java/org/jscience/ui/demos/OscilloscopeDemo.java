package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.devices.OscilloscopeViewer;

public class OscilloscopeDemo implements DemoProvider {
    @Override
    public String getName() {
        return org.jscience.natural.i18n.I18n.getInstance().get("Oscilloscope.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.natural.i18n.I18n.getInstance().get("Oscilloscope.desc");
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
