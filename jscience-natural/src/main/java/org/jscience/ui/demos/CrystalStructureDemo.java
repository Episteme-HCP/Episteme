/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import javafx.application.Application;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.i18n.I18n;

/**
 * Demo provider for the Crystal Structure Explorer.
 */
public class CrystalStructureDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.chemistry");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("crystal.title");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("crystal.desc");
    }

    @Override
    public void show(Stage stage) {
        try {
            // Use reflection to avoid direct dependency on jscience-killer-apps module
            Class<?> appClass = Class.forName("org.jscience.apps.chemistry.CrystalStructureApp");
            Application app = (Application) appClass.getDeclaredConstructor().newInstance();
            app.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
            org.jscience.ui.demos.AlertHelper.showError("Error launching Crystal Demo", e.getMessage());
        }
    }
}
