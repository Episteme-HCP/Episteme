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

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jscience.core.mathematics.discrete.Graph;
import org.jscience.core.mathematics.numbers.real.Real;

import java.util.*;

/**
 * High-performance JavaFX-based network/graph renderer.
 * Features an interactive force-directed layout using Real precision.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class JavaFXNetworkRenderer {

    private final Canvas canvas;
    private Graph<?> currentGraph;
    private final Map<Object, NodePos> positions = new HashMap<>();
    private final Random random = new Random();
    private final AnimationTimer timer;

    // Forces (Adjustable via viewer parameters)
    private Real repulsion = Real.of(1000.0);
    private Real springStrength = Real.of(0.05);
    private Real springLength = Real.of(100.0);
    private Real friction = Real.of(0.95);

    private static class NodePos {
        Real x, y, vx, vy;
        Object vertex;
        NodePos(Real x, Real y, Object vertex) {
            this.x = x; this.y = y; this.vx = Real.ZERO; this.vy = Real.ZERO;
            this.vertex = vertex;
        }
    }

    public JavaFXNetworkRenderer() {
        this.canvas = new Canvas(800, 600);
        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateLayout();
                draw();
            }
        };
        timer.start();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * Renders network/graph data.
     * 
     * @param graphData Graph data to render
     */
    public void renderNetwork(Object graphData) {
        if (graphData instanceof Graph<?> graph) {
            this.currentGraph = graph;
            initializePositions(graph);
        }
    }

    private void initializePositions(Graph<?> graph) {
        positions.clear();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        for (Object v : graph.vertices()) {
            positions.put(v, new NodePos(
                Real.of(random.nextDouble() * w), 
                Real.of(random.nextDouble() * h), v));
        }
    }

    private void updateLayout() {
        if (currentGraph == null || positions.isEmpty()) return;

        var nodeList = new ArrayList<>(positions.values());
        
        // Repulsion (Electrons)
        for (int i = 0; i < nodeList.size(); i++) {
            for (int j = i + 1; j < nodeList.size(); j++) {
                NodePos n1 = nodeList.get(i);
                NodePos n2 = nodeList.get(j);
                Real dx = n2.x.subtract(n1.x);
                Real dy = n2.y.subtract(n1.y);
                double dSqVal = dx.multiply(dx).add(dy.multiply(dy)).doubleValue() + 0.01;
                Real distSq = Real.of(dSqVal);
                Real force = repulsion.divide(distSq);
                Real dist = Real.of(Math.sqrt(dSqVal));
                Real fx = force.multiply(dx).divide(dist);
                Real fy = force.multiply(dy).divide(dist);
                n1.vx = n1.vx.subtract(fx); n1.vy = n1.vy.subtract(fy);
                n2.vx = n2.vx.add(fx); n2.vy = n2.vy.add(fy);
            }
        }

        // Springs (Links)
        for (var edge : currentGraph.edges()) {
            NodePos n1 = positions.get(edge.source());
            NodePos n2 = positions.get(edge.target());
            if (n1 != null && n2 != null) {
                Real dx = n2.x.subtract(n1.x);
                Real dy = n2.y.subtract(n1.y);
                double dVal = Math.sqrt(dx.multiply(dx).add(dy.multiply(dy)).doubleValue()) + 0.01;
                Real dist = Real.of(dVal);
                Real force = dist.subtract(springLength).multiply(springStrength);
                Real fx = force.multiply(dx).divide(dist);
                Real fy = force.multiply(dy).divide(dist);
                n1.vx = n1.vx.add(fx); n1.vy = n1.vy.add(fy);
                n2.vx = n2.vx.subtract(fx); n2.vy = n2.vy.subtract(fy);
            }
        }

        // Apply and Friction
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        for (var n : positions.values()) {
            n.x = n.x.add(n.vx);
            n.y = n.y.add(n.vy);
            n.vx = n.vx.multiply(friction);
            n.vy = n.vy.multiply(friction);

            // Containment
            if (n.x.doubleValue() < 50) n.vx = n.vx.add(Real.ONE);
            if (n.x.doubleValue() > w - 50) n.vx = n.vx.subtract(Real.ONE);
            if (n.y.doubleValue() < 50) n.vy = n.vy.add(Real.ONE);
            if (n.y.doubleValue() > h - 50) n.vy = n.vy.subtract(Real.ONE);
        }
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (currentGraph == null) return;

        // Edges
        gc.setStroke(Color.web("#808080", 0.5));
        gc.setLineWidth(1.0);
        for (var edge : currentGraph.edges()) {
            NodePos n1 = positions.get(edge.source());
            NodePos n2 = positions.get(edge.target());
            if (n1 != null && n2 != null) {
                gc.strokeLine(n1.x.doubleValue(), n1.y.doubleValue(), n2.x.doubleValue(), n2.y.doubleValue());
            }
        }

        // Nodes
        for (var n : positions.values()) {
            gc.setFill(Color.DODGERBLUE);
            gc.fillOval(n.x.doubleValue() - 6, n.y.doubleValue() - 6, 12, 12);
            gc.setFill(Color.BLACK);
            gc.fillText(n.vertex.toString(), n.x.doubleValue() + 10, n.y.doubleValue() + 4);
        }
    }

    public void setRepulsion(Real repulsion) { this.repulsion = repulsion; }
    public void setSpringStrength(Real strength) { this.springStrength = strength; }
    public void setSpringLength(Real length) { this.springLength = length; }
}

