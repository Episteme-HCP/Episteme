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

package org.jscience.core.ui.viewers.mathematics.discrete;

import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import org.jscience.core.ui.Viewer;
import com.google.auto.service.AutoService;
import org.jscience.core.ui.AbstractViewer;
import org.jscience.core.ui.Parameter;
import org.jscience.core.ui.RealParameter;
import org.jscience.core.ui.i18n.I18N;
import org.jscience.core.ui.viewers.mathematics.discrete.backends.JavaFXGraphRenderer;

import java.util.*;

/**
 * Universal Graph Viewer.
 * Visualizes complex graphs using native force-directed layout or external backends (GraphStream, JGraphT).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService(Viewer.class)
public class GraphViewer extends AbstractViewer {

    private JavaFXGraphRenderer nativeRenderer;
    private Object activeBackend;


    public GraphViewer() {
        // Discover backend using standardized static pattern
        GraphBackend backend = GraphBackendManager.staticDefault();
        
        if (backend != null) {
            activeBackend = backend.createBackend();
            if (activeBackend instanceof JavaFXGraphRenderer renderer) {
                this.nativeRenderer = renderer;
                setCenter(new Pane(renderer.getCanvas()));
                renderer.getCanvas().widthProperty().bind(((Pane)getCenter()).widthProperty());
                renderer.getCanvas().heightProperty().bind(((Pane)getCenter()).heightProperty());
            } else {
                javafx.scene.layout.VBox info = new javafx.scene.layout.VBox(10);
                info.setAlignment(javafx.geometry.Pos.CENTER);
                info.getChildren().addAll(
                    new Label(I18N.getInstance().get("viewer.graph.active", "Active Backend: ") + backend.getName()),
                    new Label(backend.getDescription())
                );
                setCenter(info);
            }
        } else {
            Pane container = new Pane();
            container.getStyleClass().add("viewer-root");
            container.getChildren().add(new Label(org.jscience.core.ui.i18n.I18N.getInstance().get("viewer.graphviewer.error.nobackend", "No Graph Backend Available (Install GraphStream, JGraphT, etc.)")));
            setCenter(container);
        }
    }

    /**
     * Sets the graph data to be rendered by the active backend.
     */
    public void setData(Object graphData) {
        if (nativeRenderer != null) {
            nativeRenderer.renderGraph(graphData);
        } else if (activeBackend != null) {
            // Logic for other backends (if they support a standard render method)
            try {
                activeBackend.getClass().getMethod("renderGraph", Object.class).invoke(activeBackend, graphData);
            } catch (Exception ignored) {}
        }
    }

    @Override
    public List<Parameter<?>> getViewerParameters() {
        List<Parameter<?>> params = new ArrayList<>();
        params.add(new RealParameter(
            I18N.getInstance().get("viewer.graph.repulsion", "Repulsion Strength"), 
            "Strength of node repulsion (force physics)", 0.0, 5000.0, 100.0, 1000.0, val -> {
                if (nativeRenderer != null) nativeRenderer.setRepulsion(val);
            }));
        params.add(new RealParameter(
            I18N.getInstance().get("viewer.graph.spring", "Spring Strength"), 
            "Strength of edge attraction", 0.0, 1.0, 0.01, 0.05, val -> {
                if (nativeRenderer != null) nativeRenderer.setSpringStrength(val);
            }));
        return params;
    }

    @Override public String getCategory() { return I18N.getInstance().get("category.mathematics", "Mathematics"); }
    @Override public String getName() { return I18N.getInstance().get("viewer.graph.name", "Universal Graph Viewer"); }
    @Override public String getDescription() { return I18N.getInstance().get("viewer.graph.desc", "Visualizes graphs with multiple backends."); }
    @Override public String getLongDescription() { 
        return I18N.getInstance().get("viewer.graph.longdesc", "Advanced graph theory visualization tool. Supports high-performance native force-directed layout and external providers like GraphStream or JGraphT."); 
    }
}


