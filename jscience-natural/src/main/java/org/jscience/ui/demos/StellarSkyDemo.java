package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.physics.astronomy.StellarSkyViewer;

public class StellarSkyDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return "Physics";
    }

    @Override
    public String getName() {
        return org.jscience.natural.i18n.I18n.getInstance().get("sky.window.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.natural.i18n.I18n.getInstance().get("sky.title");
    }

    @Override
    public void show(Stage stage) {
        StellarSkyViewer.show(stage);
    }
}
