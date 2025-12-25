package org.jscience.ui.biology.phylogeny;

import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Phylogenetic Tree Viewer.
 * Visualizes evolutionary relationships.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PhylogeneticTreeViewer extends Application {

    private static class Node {
        String name;
        List<Node> children = new ArrayList<>();
        double x, y; // Cartesian Position (Linear Layout)
        double angle, radius; // Polar Position (Radial Layout)
        // Metadata for heatmap (0.0 to 1.0)
        double trait1, trait2, trait3;

        Node(String name) {
            this.name = name;
            // Random traits for demo
            trait1 = Math.random();
            trait2 = Math.random();
            trait3 = Math.random();
        }

        void add(Node n) {
            children.add(n);
        }
    }

    private Node selectedNode = null;
    private boolean radialMode = false; // Toggle state

    // Layout Counters
    private int leafCounter = 0;

    // --- Linear Layout ---
    private void layoutLinear(Node node, double depth) {
        if (node.children.isEmpty()) {
            node.x = 600; // Leave space for metadata
            node.y = 50 + leafCounter * 30;
            leafCounter++;
        } else {
            double minY = Double.MAX_VALUE;
            double maxY = Double.MIN_VALUE;
            for (Node child : node.children) {
                layoutLinear(child, depth + 1);
                minY = Math.min(minY, child.y);
                maxY = Math.max(maxY, child.y);
            }
            node.x = 50 + depth * 100;
            node.y = (minY + maxY) / 2;
        }
    }

    // --- Radial Layout ---
    private void layoutRadial(Node node, double startAngle, double endAngle, double currentRadius) {
        if (node.children.isEmpty()) {
            node.angle = (startAngle + endAngle) / 2;
            node.radius = 300; // Fixed outer radius
        } else {
            node.radius = currentRadius;
            double currentStart = startAngle;
            double totalSpan = endAngle - startAngle;

            // Count total leaves under this node to allocate angle proportionally
            int localLeaves = countLeaves(node);

            double myMinAngle = Double.MAX_VALUE;
            double myMaxAngle = Double.MIN_VALUE;

            for (Node child : node.children) {
                int childLeaves = countLeaves(child);
                double childSpan = totalSpan * ((double) childLeaves / localLeaves);
                double childEnd = currentStart + childSpan;

                layoutRadial(child, currentStart, childEnd, currentRadius + 50);

                myMinAngle = Math.min(myMinAngle, child.angle);
                myMaxAngle = Math.max(myMaxAngle, child.angle);

                currentStart = childEnd;
            }
            node.angle = (myMinAngle + myMaxAngle) / 2;
            // Polarity fix: Nodes position themselves at mean angle of children
        }

        // Convert Polar to Cartesian for rendering convenience
        node.x = 400 + node.radius * Math.cos(node.angle);
        node.y = 300 + node.radius * Math.sin(node.angle);
    }

    private int countLeaves(Node n) {
        if (n.children.isEmpty())
            return 1;
        int sum = 0;
        for (Node c : n.children)
            sum += countLeaves(c);
        return sum;
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(900, 700);
        root.setCenter(canvas);

        Label infoPanel = new Label("Click Node for Details | Toggle View");
        infoPanel.setStyle("-fx-padding: 10; -fx-background-color: #16213e; -fx-text-fill: #aaa;");

        javafx.scene.control.Button toggleBtn = new javafx.scene.control.Button("Switch View (Linear/Radial)");
        toggleBtn.setOnAction(e -> {
            radialMode = !radialMode;
            updateLayoutAndDraw(canvas, infoPanel.getText());
        });

        javafx.scene.layout.HBox controls = new javafx.scene.layout.HBox(10, toggleBtn, infoPanel);
        controls.setPadding(new javafx.geometry.Insets(10));
        root.setBottom(controls);

        this.treeRoot = buildSampleTree();
        // totalLeaves = countLeaves(treeRoot); // Unused
        updateLayoutAndDraw(canvas, "");

        canvas.setOnMouseClicked(e -> {
            Node clicked = findNode(treeRoot, e.getX(), e.getY());
            if (clicked != null) {
                selectedNode = clicked;
                String txt = "Selected: " + clicked.name;
                infoPanel.setText(txt);
                drawTree(canvas.getGraphicsContext2D(), treeRoot);
            } else {
                selectedNode = null;
                infoPanel.setText("Click Node for Details | Toggle View");
                drawTree(canvas.getGraphicsContext2D(), treeRoot);
            }
        });

        Scene scene = new Scene(root, 900, 750);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle("JScience Phylogenetic Tree Viewer");
        stage.setScene(scene);
        stage.show();
    }

    private Node treeRoot;

    private void updateLayoutAndDraw(Canvas canvas, String info) {
        leafCounter = 0;
        if (radialMode) {
            layoutRadial(treeRoot, 0, Math.PI * 2, 50);
        } else {
            layoutLinear(treeRoot, 0);
        }
        drawTree(canvas.getGraphicsContext2D(), treeRoot);
    }

    private Node findNode(Node root, double mx, double my) {
        if (Math.abs(root.x - mx) < 10 && Math.abs(root.y - my) < 10)
            return root;
        for (Node child : root.children) {
            Node found = findNode(child, mx, my);
            if (found != null)
                return found;
        }
        return null; // Simplified: usually check heatmap area too
    }

    private void drawTree(GraphicsContext gc, Node node) {
        // Clear background
        if (node == treeRoot) {
            gc.setFill(Color.web("#1a1a2e"));
            gc.fillRect(0, 0, 900, 700);
        }

        gc.setStroke(Color.web("#666"));
        gc.setLineWidth(1);

        // Draw Links
        for (Node child : node.children) {
            if (radialMode) {
                // Curved lines for radial? Or straight for simplicity
                gc.strokeLine(node.x, node.y, child.x, child.y);
            } else {
                // Elbow
                gc.strokeLine(node.x, node.y, node.x, child.y);
                gc.strokeLine(node.x, child.y, child.x, child.y);
            }
            drawTree(gc, child);
        }

        // Draw Node
        if (node == selectedNode) {
            gc.setFill(Color.RED);
            gc.fillOval(node.x - 6, node.y - 6, 12, 12);
        } else {
            gc.setFill(node.children.isEmpty() ? Color.FORESTGREEN : Color.DARKBLUE);
            gc.fillOval(node.x - 4, node.y - 4, 8, 8);
        }

        // Draw Node Name (for non-leaves in linear, or selected in radial)
        if (!radialMode || node == selectedNode) {
            gc.setFill(Color.LIGHTGRAY);
            gc.setTextAlign(TextAlignment.LEFT);
            gc.setTextBaseline(VPos.CENTER);
            gc.fillText(node.name, node.x + 10, node.y);
        }

        // Draw Metadata Heatmap (Only leaves)
        if (node.children.isEmpty()) {
            if (radialMode) {
                // Draw outward along radius
                double ang = node.angle;
                drawRadialHeatmap(gc, node, 310, ang);
            } else {
                // Draw to the right
                drawLinearHeatmap(gc, node, 620, node.y);
            }
        }
    }

    // Helper to draw heatmap cells
    private void drawLinearHeatmap(GraphicsContext gc, Node n, double startX, double y) {
        // Text Label
        gc.setFill(Color.LIGHTGRAY);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(n.name, startX + 65, y);

        // 3 Cells
        drawCell(gc, startX, y - 10, getColor(n.trait1));
        drawCell(gc, startX + 20, y - 10, getColor(n.trait2));
        drawCell(gc, startX + 40, y - 10, getColor(n.trait3));
    }

    private void drawRadialHeatmap(GraphicsContext gc, Node n, double startRadius, double angle) {
        // Just dots extending outward
        double x1 = 400 + (startRadius + 10) * Math.cos(angle);
        double y1 = 300 + (startRadius + 10) * Math.sin(angle);
        drawDot(gc, x1, y1, getColor(n.trait1));

        double x2 = 400 + (startRadius + 25) * Math.cos(angle);
        double y2 = 300 + (startRadius + 25) * Math.sin(angle);
        drawDot(gc, x2, y2, getColor(n.trait2));

        // Label? Maybe too crowded in radial without rotation math
    }

    private void drawCell(GraphicsContext gc, double x, double y, Color c) {
        gc.setFill(c);
        gc.fillRect(x, y, 18, 18); // slight gap
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, y, 18, 18);
    }

    private void drawDot(GraphicsContext gc, double x, double y, Color c) {
        gc.setFill(c);
        gc.fillOval(x - 4, y - 4, 8, 8);
    }

    private Color getColor(double val) {
        // Red (high) to Blue (low)
        return Color.hsb((1.0 - val) * 240, 0.8, 0.9);
    }

    private Node buildSampleTree() {
        Node root = new Node("Life");
        Node euk = new Node("Eukaryota");
        Node bac = new Node("Bacteria");
        Node arc = new Node("Archaea");
        root.add(euk);
        root.add(bac);
        root.add(arc);

        Node ani = new Node("Animalia");
        Node pla = new Node("Plantae");
        Node fun = new Node("Fungi");
        euk.add(ani);
        euk.add(pla);
        euk.add(fun);

        ani.add(new Node("Homo sapiens"));
        ani.add(new Node("Pan troglodytes"));
        ani.add(new Node("Mus musculus"));
        ani.add(new Node("Danio rerio"));

        pla.add(new Node("Arabidopsis thaliana"));
        pla.add(new Node("Oryza sativa"));
        pla.add(new Node("Zea mays"));

        bac.add(new Node("E. coli"));
        bac.add(new Node("B. subtilis"));
        bac.add(new Node("S. aureus"));

        arc.add(new Node("M. jannaschii"));
        arc.add(new Node("H. salinarum"));

        return root;
    }

    public static void show(Stage stage) {
        new PhylogeneticTreeViewer().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
