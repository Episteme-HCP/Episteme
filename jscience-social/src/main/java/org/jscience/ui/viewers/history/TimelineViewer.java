/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.ui.viewers.history;

import javafx.scene.layout.BorderPane;
import org.jscience.ui.AbstractViewer;

/**
 * Generic Viewer for Timeline data.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TimelineViewer extends BorderPane implements org.jscience.ui.Viewer {

    public TimelineViewer() {
        // Initial stub logic for timeline
        // In a full implementation, this would layout events on a time axis.
        this.setStyle("-fx-background-color: white;");
    }

    @Override
    public String getCategory() {
        return "History";
    }

    @Override
    public String getName() {
        return "Timeline Viewer";
    }
    
    @Override
    public String getDescription() {
        return "Visualizes events on a timeline.";
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
