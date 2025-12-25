package org.jscience.ui.demos;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SociologyNetworkDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return "Sociology";
    }

    @Override
    public String getName() {
        return org.jscience.social.i18n.I18n.getInstance().get("SociologyNetwork.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.social.i18n.I18n.getInstance().get("SociologyNetwork.desc");
    }

    private static class Node {
        double x, y, vx, vy;
        Color color;

        Node(double x, double y, Color c) {
            this.x = x;
            this.y = y;
            this.color = c;
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
        Group root = new Group();
        Canvas canvas = new Canvas(800, 600);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        Random rand = new Random();

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

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle(getName());
        stage.setScene(scene);
        stage.show();
    }

    private void createCluster(List<Node> nodes, List<Edge> edges, int count, double cx, double cy, Color color,
            Random r) {
        int startIdx = nodes.size();
        for (int i = 0; i < count; i++) {
            nodes.add(new Node(cx + r.nextDouble() * 100 - 50, cy + r.nextDouble() * 100 - 50, color));
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
