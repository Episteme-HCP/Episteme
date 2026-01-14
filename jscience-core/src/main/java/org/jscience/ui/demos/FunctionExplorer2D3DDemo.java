/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.ui.demos;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.Parameter;
import org.jscience.ui.viewers.mathematics.analysis.real.FunctionExplorer2DViewer;
import org.jscience.ui.viewers.mathematics.analysis.real.FunctionExplorer3DViewer;
import org.jscience.ui.i18n.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * Unified Demo for 2D and 3D Function Exploration.
 */
public class FunctionExplorer2D3DDemo extends AbstractDemo {

    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("plotting.title", "Function Explorer");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("plotting.desc", "Explore 2D and 3D mathematical functions.");
    }

    @Override
    protected javafx.scene.Node createViewerNode() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab tab2D = new Tab(I18n.getInstance().get("funcexplorer.tab.2d", "2D Explorer"));
        FunctionExplorer2DViewer v2D = new FunctionExplorer2DViewer();
        tab2D.setContent(v2D);

        Tab tab3D = new Tab(I18n.getInstance().get("funcexplorer.tab.3d", "3D Explorer"));
        FunctionExplorer3DViewer v3D = new FunctionExplorer3DViewer();
        tab3D.setContent(v3D);

        tabPane.getTabs().addAll(tab2D, tab3D);

        // Set the primary viewer for parameter panel delegation (optional)
        this.viewer = v2D;

        return tabPane;
    }

    @Override
    protected String getLongDescription() {
        return getDescription();
    }


}
