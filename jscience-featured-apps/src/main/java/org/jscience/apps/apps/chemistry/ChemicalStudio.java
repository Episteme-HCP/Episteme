/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.apps.apps.chemistry;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.jscience.apps.apps.framework.FeaturedAppBase;
import org.jscience.natural.ui.viewers.chemistry.MolecularViewer;

/**
 * Chemical Intelligence Studio.
 * A professional environment for molecular analysis and reaction simulation.
 */
public final class ChemicalStudio extends FeaturedAppBase {

    private MolecularViewer molecularViewer;
    private ReactionKineticsViewer kineticsViewer;

    @Override
    protected String getAppTitle() {
        return "CHEMICAL INTELLIGENCE STUDIO";
    }

    @Override
    protected Region createMainContent() {
        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("premium-tab-pane");

        molecularViewer = new MolecularViewer();
        Tab molTab = new Tab("Molecular Structure", molecularViewer);
        molTab.setClosable(false);

        kineticsViewer = new ReactionKineticsViewer();
        Tab kinTab = new Tab("Reaction Kinetics", kineticsViewer);
        kinTab.setClosable(false);

        tabPane.getTabs().addAll(molTab, kinTab);

        VBox root = new VBox(10, tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);
        return root;
    }

    @Override
    protected void onAppReady() {
        // Initialization logic
    }
}
