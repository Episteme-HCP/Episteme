package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.engineering.components.ResistorColorCodeViewer;

import org.jscience.ui.i18n.I18n;

public class ResistorColorCodeDemo implements DemoProvider {
    @Override
    public String getName() {
        return I18n.getInstance().get("ResistorColorCode.title");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("ResistorColorCode.desc");
    }

    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.engineering");
    }

    @Override
    public void show(Stage stage) {
        new ResistorColorCodeViewer().start(stage);
    }
}
