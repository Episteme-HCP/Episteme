package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.devices.OscilloscopeViewer;

import org.jscience.ui.i18n.I18n;

public class OscilloscopeDemo implements DemoProvider {
    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("Oscilloscope.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("Oscilloscope.desc");
    }

    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.engineering");
    }

    @Override
    public void show(Stage stage) {
        new OscilloscopeViewer().start(stage);
    }
}
