/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.apps.biology;

import org.jscience.apps.framework.FeaturedAppBase;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Species Browser Application.
 * Placeholder for GBIF API integration.
 */
public class SpeciesBrowserApp extends FeaturedAppBase {

    public SpeciesBrowserApp() {
        super();
        try {
            // No complex field initializations, but ensuring constructor exists for SPI safety
        } catch (Throwable t) {
            System.err.println("CRITICAL: Failed to initialize SpeciesBrowserApp: " + t.getMessage());
            t.printStackTrace();
        }
    }

    @Override
    protected String getAppTitle() {
        // Key uses space: "Species Browser.title" (escaped in properties file)
        return i18n.get("Species Browser.title");
    }

    @Override
    public String getDescription() {
        return i18n.get("Species Browser.desc");
    }

    @Override
    protected Region createMainContent() {
        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);
        Label title = new Label("Species Browser");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label status = new Label("GBIF API Integration Pending");
        status.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");
        
        box.getChildren().addAll(title, status);
        return box;
    }
}
