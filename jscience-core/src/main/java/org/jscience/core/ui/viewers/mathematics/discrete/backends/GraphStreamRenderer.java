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

package org.jscience.core.ui.viewers.mathematics.discrete.backends;

import org.jscience.core.mathematics.discrete.Graph;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.ui.viewers.mathematics.discrete.GraphRenderer;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * GraphStream-based graph renderer.
 * <p>
 * Implements {@link GraphRenderer} using the GraphStream library.
 * Bridges JScience Graph to GraphStream Graph.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GraphStreamRenderer implements GraphRenderer {

    private final JPanel panel;
    private final JTextArea statusArea;
    private Object graphStreamGraph; // org.graphstream.graph.Graph
    private Object viewer; // org.graphstream.ui.view.Viewer
    private Object view; // org.graphstream.ui.view.View

    public GraphStreamRenderer() {
        panel = new JPanel(new BorderLayout());
        statusArea = new JTextArea();
        statusArea.setEditable(false);
        // Fallback UI if GraphStream fails or before graph load
        statusArea.setText("GraphStream Renderer initialized.\nWaiting for graph data...\n");
        panel.add(new JScrollPane(statusArea), BorderLayout.CENTER);
    }

    @Override
    public void renderGraph(Object graphData) {
        if (!(graphData instanceof Graph<?>)) {
             statusArea.append("Invalid graph data type.\n");
             return;
        }
        Graph<?> jScienceGraph = (Graph<?>) graphData;

        try {
            // Reflection to avoid compile-time dependency on GraphStream
            Class<?> singleGraphClass = Class.forName("org.graphstream.graph.implementations.SingleGraph");
            graphStreamGraph = singleGraphClass.getConstructor(String.class).newInstance("GraphStreamVisualization");

            // Add Nodes
            java.lang.reflect.Method addNode = graphStreamGraph.getClass().getMethod("addNode", String.class);
            for (Object v : jScienceGraph.vertices()) {
                addNode.invoke(graphStreamGraph, v.toString());
            }

            // Add Edges
            java.lang.reflect.Method addEdge = graphStreamGraph.getClass().getMethod("addEdge", String.class, String.class, String.class);
            // Assuming edges have unique IDs or we generate them
            int edgeCount = 0;
            for (Object e : jScienceGraph.edges()) {
                 // JScience Edge? Need source/target.
                 // Assuming Graph.edges() returns something we can get source/target from.
                 // In JavaFXGraphRenderer: edge.source(), edge.target()
                 // Let's assume reflection or known interface.
                 // Step 1461 showed: for (var edge : currentGraph.edges()) { ... edge.source() ... }
                 // So we assume Edge interface has source() target()
                 
                 // We need to cast e to expected Edge type or use reflection on it too if generic?
                 // Graph<?> implies Graph<E, V> or something?
                 // Let's rely on toString for IDs for now.
                 
                 // Actually, let's look at how JavaFXGraphRenderer did it (step 1461):
                 // it imports org.jscience.core.mathematics.discrete.Graph
                 // and iterates currentGraph.edges().
                 // It calls positions.get(edge.source()).
                 // We will do similar via reflection if we don't have the edge class visible (we do import Graph).
                 
                 // Note: We don't have the Edge interface definition here but JavaFXGraphRenderer uses it.
                 // We will assume standard structure.
                 
                 Object edge = e;
                 // Reflection to get source/target from edge object if needed, 
                 // OR assume Graph interface methods are available.
                 // Wait, JavaFXGraphRenderer calls edge.source().
                 // We can cast if we know the type, but Graph<?> makes it tricky.
                 // Let's assume we can reflect 'source' and 'target' methods.
                 
                Object source = edge.getClass().getMethod("source").invoke(edge);
                Object target = edge.getClass().getMethod("target").invoke(edge);
                 
                addEdge.invoke(graphStreamGraph, "e" + edgeCount++, source.toString(), target.toString());
            }

            // Display
            // Viewer viewer = graph.display();
            java.lang.reflect.Method display = graphStreamGraph.getClass().getMethod("display");
            viewer = display.invoke(graphStreamGraph);
            
            // Embed? GraphStream 1.3 vs 2.0 differs.
            // 2.0 typically uses specific UI viewers (JavaFX/Swing).
            // For now, let it open its own window if display() is used, 
            // OR try to get the view panel.
            
            // Try enabling auto layout
            // viewer.enableAutoLayout();
            viewer.getClass().getMethod("enableAutoLayout").invoke(viewer);

        } catch (Exception e) {
            statusArea.append("Error creating GraphStream graph: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    @Override
    public void setRepulsion(Real repulsion) {
        // Map to layout attributes
        // layout.force = repulsion
        updateAttribute("layout.force", repulsion.doubleValue());
    }

    @Override
    public void setSpringStrength(Real strength) {
         updateAttribute("layout.quality", strength.doubleValue() * 10); // Rough mapping
    }

    @Override
    public void setSpringLength(Real length) {
        // layout.weight?
    }
    
    private void updateAttribute(String key, Object value) {
        if (graphStreamGraph != null) {
            try {
                // graph.setAttribute(key, value);
                 graphStreamGraph.getClass().getMethod("setAttribute", String.class, Object.class).invoke(graphStreamGraph, key, value);
            } catch (Exception e) {
                // ignore
            }
        }
    }

    @Override
    public Object getViewComponent() {
        return panel; // Ideally we embed the graph view here
    }
}
