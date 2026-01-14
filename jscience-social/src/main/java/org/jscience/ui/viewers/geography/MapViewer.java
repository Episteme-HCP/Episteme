/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.ui.viewers.geography;

import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import org.jscience.ui.AbstractViewer;

/**
 * Generic Map Viewer for Geographic Information Systems (GIS).
 * Designed to support pluggable providers and layers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MapViewer extends StackPane implements org.jscience.ui.Viewer {

    public MapViewer() {
        this.setStyle("-fx-background-color: #e0f0ff;");
        this.getChildren().add(new Label("Generic Map Viewer (Placeholder)"));
    }

    @Override
    public String getCategory() {
        return "Geography";
    }

    @Override
    public String getName() {
        return "Map Viewer";
    }
    
    @Override
    public String getDescription() {
        return "Displays geographic data.";
    }

    @Override
    public void show(javafx.stage.Stage stage) {
         javafx.scene.Scene scene = new javafx.scene.Scene(this);
         org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
         stage.setTitle(getName());
         stage.setScene(scene);
         stage.show();
    }
    
    @Override
    public java.util.List<org.jscience.ui.Parameter<?>> getViewerParameters() {
        return java.util.Collections.emptyList();
    }
}
