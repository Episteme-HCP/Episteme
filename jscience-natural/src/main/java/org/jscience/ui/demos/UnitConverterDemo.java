/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.mathematics.units.UnitConverterViewer;

public class UnitConverterDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public String getName() {
        return org.jscience.natural.i18n.I18n.getInstance().get("unit.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.natural.i18n.I18n.getInstance().get("unit.desc");
    }

    @Override
    public void show(Stage stage) {
        UnitConverterViewer.show(stage);
    }
}
