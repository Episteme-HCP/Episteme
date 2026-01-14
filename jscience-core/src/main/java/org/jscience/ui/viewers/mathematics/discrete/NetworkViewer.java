/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
            container.setStyle("-fx-background-color: white; -fx-border-color: #ccc;");
            container.getChildren().add(new Label("No Network Backend Available (Install GraphStream, JUNG, etc.)"));
            getChildren().add(container);
        }
    }

    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public String getName() {
        return "Network Viewer";
    }
    
    @Override
    public String getDescription() {
        return "Visualizes networks and graphs.";
    }
}
