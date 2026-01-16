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

package org.jscience.ui.viewers.geography;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

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
        this.getStyleClass().add("viewer-root");
        
        // Discover backend
        String backendId = org.jscience.JScience.getMapBackendId();
        java.util.Optional<org.jscience.technical.backend.BackendProvider> provider;
        
        if (backendId == null) {
            provider = org.jscience.technical.backend.BackendDiscovery.getInstance()
                .getBestProvider(org.jscience.technical.backend.BackendDiscovery.TYPE_MAP);
        } else {
            provider = org.jscience.technical.backend.BackendDiscovery.getInstance()
                .getProvider(org.jscience.technical.backend.BackendDiscovery.TYPE_MAP, backendId);
        }
        
        if (provider.isPresent()) {
            Object backend = provider.get().createBackend();
            if (backend instanceof org.jscience.ui.viewers.geography.backend.JavaFXMapRenderer) {
                this.getChildren().add(((org.jscience.ui.viewers.geography.backend.JavaFXMapRenderer)backend).getCanvas());
            } else {
                VBox info = new VBox(10);
                info.setAlignment(javafx.geometry.Pos.CENTER);
                info.getChildren().addAll(
                    new Label("Active Map Backend: " + provider.get().getName()),
                    new Label(provider.get().getDescription())
                );
                this.getChildren().add(info);
            }
        } else {
            this.getChildren().add(new Label(org.jscience.ui.i18n.I18n.getInstance().get("generated.map.no.map.backend.avail", "No Map Backend Available (Install Unfolding, GeoTools, etc.)")));
        }
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

    @Override
    public String getLongDescription() {
        return getDescription();
    }
}
