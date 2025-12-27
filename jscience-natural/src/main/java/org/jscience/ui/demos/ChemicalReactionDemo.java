/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.chemistry.ChemicalReactionViewer;

/**
 * Demo provider for the Chemical Reaction Parser.
 */
import org.jscience.ui.i18n.I18n;

public class ChemicalReactionDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.chemistry");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("ChemicalReaction.title");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("ChemicalReaction.desc");
    }

    @Override
    public void show(Stage stage) {
        ChemicalReactionViewer.show(stage);
    }
}
