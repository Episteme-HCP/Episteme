/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.ui.demos;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.ui.AppProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.jscience.mathematics.geometry.Vector2D;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Sociology Network Demo using JScience Vector2D types.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SociologyNetworkDemo implements AppProvider {

    @Override
    public boolean isDemo() {
        return true;
    }

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.sociology", "Sociology");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("SociologyNetwork.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("SociologyNetwork.desc");
    }

    private static class Node {
        Vector2D pos;
        Vector2D vel;
        Color color;
        String name;
        int age;
        String city;

        Node(double x, double y, Color c, String name, int age, String city) {
            this.pos = Vector2D.of(x, y);
            this.vel = Vector2D.of(0, 0);
            this.color = c;
            this.name = name;
            this.age = age;
            this.city = city;
        }
    }

    private static class Edge {
        Node n1, n2;

        Edge(Node n1, Node n2) {
            this.n1 = n1;
            this.n2 = n2;
        }
    }

    @Override
    public void show(Stage stage) {
        BorderPane root = new BorderPane();

        // Canvas Center
        Canvas canvas = new Canvas(800, 600);
        Pane canvasPane = new Pane(canvas);
        canvasPane.setPrefSize(800, 600);
        root.setCenter(canvasPane);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Right Sidebar
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(15));
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #ddd; -fx-border-width: 0 0 0 1;");

        Label title = new Label(getName());
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label desc = new Label(getDescription());
        desc.setWrapText(true);

        Separator sep = new Separator();

        Label detailsTitle = new Label(
                org.jscience.ui.i18n.I18n.getInstance().get("demo.sociology.details.title", "Person Details"));
        detailsTitle.setStyle("-fx-font-weight: bold;");

        Label details = new Label(org.jscience.ui.i18n.I18n.getInstance().get("demo.sociology.details.hint",
                "Click on a person node to view details."));
        details.setWrapText(true);

        sidebar.getChildren().addAll(title, desc, sep, detailsTitle, details);
        root.setRight(sidebar);

        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        Random rand = new Random();

        // Mouse Interaction
        canvas.setOnMouseClicked(e -> {
            boolean found = false;
            double mx = e.getX();
            double my = e.getY();
            Vector2D mousePos = Vector2D.of(mx, my);
            for (Node n : nodes) {
                double dist = n.pos.minus(mousePos).norm().doubleValue();
                if (dist < 10) {
                    org.jscience.ui.i18n.I18n i18n = org.jscience.ui.i18n.I18n.getInstance();
                    details.setText(i18n.get("demo.sociology.details.name", "Name:") + " " + n.name + "\n" +
                            i18n.get("demo.sociology.details.age", "Age:") + " " + n.age + "\n" +
                            i18n.get("demo.sociology.details.city", "City:") + " " + n.city);
                    found = true;
                    break;
                }
            }
            if (!found) {
                details.setText(org.jscience.ui.i18n.I18n.getInstance().get("demo.sociology.details.hint",
                        "Click on a person node to view details."));
            }
        });

        // Create Clusters
        createCluster(nodes, edges, 15, 200, 200, Color.BLUE, rand);
        createCluster(nodes, edges, 15, 600, 400, Color.RED, rand);
        createCluster(nodes, edges, 10, 400, 300, Color.GREEN, rand);

        // Inter-cluster connections
        for (int i = 0; i < 5; i++) {
            edges.add(new Edge(nodes.get(rand.nextInt(nodes.size())), nodes.get(rand.nextInt(nodes.size()))));
        }

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updatePhysics(nodes, edges, 800, 600);
                draw(gc, nodes, edges, 800, 600);
            }
        };
        timer.start();

        Scene scene = new Scene(root, 1050, 600);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(getName());
        stage.setScene(scene);
        stage.show();
    }

    private static final String[] NAMES = { "Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Heidi",
            "Ivan", "Judy" };
    private static final String[] CITIES = { "Paris", "New York", "London", "Tokyo", "Berlin", "Sydney", "Toronto" };

    private void createCluster(List<Node> nodes, List<Edge> edges, int count, double cx, double cy, Color color,
            Random r) {
        int startIdx = nodes.size();
        for (int i = 0; i < count; i++) {
            String name = NAMES[r.nextInt(NAMES.length)] + " " + (nodes.size() + 1);
            int age = 20 + r.nextInt(40);
            String city = CITIES[r.nextInt(CITIES.length)];
            nodes.add(new Node(cx + r.nextDouble() * 100 - 50, cy + r.nextDouble() * 100 - 50, color, name, age, city));
            if (i > 0) {
                edges.add(new Edge(nodes.get(startIdx + i), nodes.get(startIdx + r.nextInt(i))));
            }
        }
    }

    private void updatePhysics(List<Node> nodes, List<Edge> edges, double w, double h) {
        Real repulsion = Real.of(1000.0);
        Real springLength = Real.of(50.0);
        Real springK = Real.of(0.05);
        Vector2D center = Vector2D.of(w / 2, h / 2);

        for (int i = 0; i < nodes.size(); i++) {
            Node n1 = nodes.get(i);
            Vector2D totalForce = Vector2D.of(0, 0);

            // Repulsion
            for (int j = 0; j < nodes.size(); j++) {
                if (i == j)
                    continue;
                Node n2 = nodes.get(j);
                Vector2D diff = n1.pos.minus(n2.pos);
                Real distSq = diff.normSquared().add(Real.of(0.1));
                if (distSq.doubleValue() < 40000) { // Limit range
                    Real forceMag = repulsion.divide(distSq);
                    totalForce = totalForce.plus(diff.direction().times(forceMag));
                }
            }

            // Center gravity
            Vector2D toCenter = center.minus(n1.pos);
            totalForce = totalForce.plus(toCenter.times(Real.of(0.005)));

            n1.vel = n1.vel.plus(totalForce);
        }

        // Springs
        for (Edge e : edges) {
            Vector2D diff = e.n2.pos.minus(e.n1.pos);
            Real dist = diff.norm();
            Real forceMag = dist.subtract(springLength).multiply(springK);
            Vector2D springForce = diff.direction().times(forceMag);

            e.n1.vel = e.n1.vel.plus(springForce);
            e.n2.vel = e.n2.vel.minus(springForce);
        }

        // Move
        Real damping = Real.of(0.9);
        for (Node n : nodes) {
            n.vel = n.vel.times(damping);
            n.pos = n.pos.plus(n.vel);

            // Bounds (Simplified)
            double x = n.pos.getX().doubleValue();
            double y = n.pos.getY().doubleValue();
            if (x < 0)
                x = 0;
            if (x > w)
                x = w;
            if (y < 0)
                y = 0;
            if (y > h)
                y = h;
            n.pos = Vector2D.of(x, y);
        }
    }

    private void draw(GraphicsContext gc, List<Node> nodes, List<Edge> edges, double w, double h) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, w, h);

        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);
        for (Edge e : edges) {
            gc.strokeLine(e.n1.pos.getX().doubleValue(), e.n1.pos.getY().doubleValue(),
                    e.n2.pos.getX().doubleValue(), e.n2.pos.getY().doubleValue());
        }

        for (Node n : nodes) {
            gc.setFill(n.color);
            double x = n.pos.getX().doubleValue();
            double y = n.pos.getY().doubleValue();
            gc.fillOval(x - 5, y - 5, 10, 10);
            gc.setStroke(Color.BLACK);
            gc.strokeOval(x - 5, y - 5, 10, 10);
        }
    }
}
