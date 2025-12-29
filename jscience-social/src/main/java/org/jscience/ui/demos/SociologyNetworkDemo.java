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
import org.jscience.ui.DemoProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SociologyNetworkDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("category.sociology");
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
        double x, y, vx, vy;
        Color color;
        String name;
        int age;
        String city;

        Node(double x, double y, Color c, String name, int age, String city) {
            this.x = x;
            this.y = y;
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

        Label detailsTitle = new Label("Person Details");
        detailsTitle.setStyle("-fx-font-weight: bold;");

        Label details = new Label("Click on a person node to view details.");
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
            for (Node n : nodes) {
                double dist = Math.sqrt(Math.pow(n.x - mx, 2) + Math.pow(n.y - my, 2));
                if (dist < 10) { // Radius + margin
                    details.setText("Name: " + n.name + "\nAge: " + n.age + "\nCity: " + n.city);
                    found = true;
                    break;
                }
            }
            if (!found) {
                details.setText("Click on a person node to view details.");
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
                // Connect to previous in cluster
                edges.add(new Edge(nodes.get(startIdx + i), nodes.get(startIdx + r.nextInt(i))));
            }
        }
    }

    private void updatePhysics(List<Node> nodes, List<Edge> edges, double w, double h) {
        // Simple Force Directed layout
        double repulsion = 1000;
        double springLength = 50;
        double springK = 0.05;

        for (int i = 0; i < nodes.size(); i++) {
            Node n1 = nodes.get(i);
            // Repulsion
            for (int j = i + 1; j < nodes.size(); j++) {
                Node n2 = nodes.get(j);
                double dx = n1.x - n2.x;
                double dy = n1.y - n2.y;
                double distSq = dx * dx + dy * dy + 0.1;
                double force = repulsion / distSq;
                double dist = Math.sqrt(distSq);

                n1.vx += (dx / dist) * force;
                n1.vy += (dy / dist) * force;
                n2.vx -= (dx / dist) * force;
                n2.vy -= (dy / dist) * force;
            }
            // Center gravity
            n1.vx += (w / 2 - n1.x) * 0.005;
            n1.vy += (h / 2 - n1.y) * 0.005;
        }

        // Springs
        for (Edge e : edges) {
            double dx = e.n2.x - e.n1.x;
            double dy = e.n2.y - e.n1.y;
            double dist = Math.sqrt(dx * dx + dy * dy);
            double force = (dist - springLength) * springK;

            e.n1.vx += (dx / dist) * force;
            e.n1.vy += (dy / dist) * force;
            e.n2.vx -= (dx / dist) * force;
            e.n2.vy -= (dy / dist) * force;
        }

        // Move
        for (Node n : nodes) {
            n.vx *= 0.9; // damping
            n.vy *= 0.9;
            n.x += n.vx;
            n.y += n.vy;

            // Bounds
            if (n.x < 0)
                n.x = 0;
            if (n.x > w)
                n.x = w;
            if (n.y < 0)
                n.y = 0;
            if (n.y > h)
                n.y = h;
        }
    }

    private void draw(GraphicsContext gc, List<Node> nodes, List<Edge> edges, double w, double h) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, w, h);

        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);
        for (Edge e : edges) {
            gc.strokeLine(e.n1.x, e.n1.y, e.n2.x, e.n2.y);
        }

        for (Node n : nodes) {
            gc.setFill(n.color);
            gc.fillOval(n.x - 5, n.y - 5, 10, 10);
            gc.strokeOval(n.x - 5, n.y - 5, 10, 10);
        }
    }
}
