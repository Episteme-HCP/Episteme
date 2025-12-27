package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.physics.thermodynamics.ThermodynamicsViewer;

import org.jscience.ui.i18n.I18n;

public class ThermodynamicsDemo implements DemoProvider {
    @Override
    public String getName() {
        return I18n.getInstance().get("Thermodynamics.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("thermo.desc");
    }

    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.physics");
    }

    @Override
    public void show(Stage stage) {
        new ThermodynamicsViewer().start(stage);
    }
}
