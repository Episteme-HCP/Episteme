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

package org.jscience.ui.viewers.mathematics.discrete;

import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import org.jscience.ui.AbstractViewer;

/**
 * Generic Network/Graph Viewer.
 * Supports visualization of nodes and edges for discrete mathematics and social network analysis.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NetworkViewer extends AbstractViewer {

    public NetworkViewer() {
        // Discover backend
        String backendId = org.jscience.JScience.getNetworkBackendId();
        java.util.Optional<org.jscience.technical.backend.BackendProvider> provider;
        
        if (backendId == null) {
            provider = org.jscience.technical.backend.BackendDiscovery.getInstance()
                .getBestProvider(org.jscience.technical.backend.BackendDiscovery.TYPE_NETWORK);
        } else {
            provider = org.jscience.technical.backend.BackendDiscovery.getInstance()
                .getProvider(org.jscience.technical.backend.BackendDiscovery.TYPE_NETWORK, backendId);
        }
        
        if (provider.isPresent()) {
            Object backend = provider.get().createBackend();
            if (backend instanceof org.jscience.ui.viewers.mathematics.discrete.backend.JavaFXNetworkRenderer) {
                getChildren().add(((org.jscience.ui.viewers.mathematics.discrete.backend.JavaFXNetworkRenderer)backend).getCanvas());
            } else {
                javafx.scene.layout.VBox info = new javafx.scene.layout.VBox(10);
                info.setAlignment(javafx.geometry.Pos.CENTER);
                info.getChildren().addAll(
                    new Label("Active Network Backend: " + provider.get().getName()),
                    new Label(provider.get().getDescription())
                );
                getChildren().add(info);
            }
        } else {
            Pane container = new Pane();
            container.getStyleClass().add("viewer-root");
            container.getChildren().add(new Label(org.jscience.ui.i18n.I18n.getInstance().get("viewer.networkviewer.error.nobackend", "No Network Backend Available (Install GraphStream, JUNG, etc.)")));
            getChildren().add(container);
        }
    }

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.mathematics", "Mathematics");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.networkviewer.name", "Network Viewer");
    }
    
    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.networkviewer.desc", "Visualizes networks and graphs.");
    }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.networkviewer.longdesc", "Graph theory and network visualization tool. Supports visualization of nodes and edges for discrete mathematics and social network analysis with various backend providers.");
    }
}
