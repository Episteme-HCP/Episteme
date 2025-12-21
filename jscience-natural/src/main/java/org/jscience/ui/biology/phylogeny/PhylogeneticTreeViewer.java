package org.jscience.ui.biology.phylogeny;

import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
        double x, y; // Position for rendering

        Node(String name) {
            this.name = name;
        }

        void add(Node n) {
            children.add(n);
        }
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(800, 600);
        root.setCenter(canvas);

        Node tree = buildSampleTree();
        layoutTree(tree, 50, 0, 750, 0); // Recursive layout
        drawTree(canvas.getGraphicsContext2D(), tree);

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("JScience Phylogenetic Tree Viewer");
        stage.setScene(scene);
        stage.show();
    }

    // Simple vertical dendrogram layout
    private int leafCounter = 0;

    private void layoutTree(Node node, double x, double y, double h, double depth) {
        if (node.children.isEmpty()) {
            node.x = 700; // Right side
            node.y = 50 + leafCounter * 40;
            leafCounter++;
        } else {
            double minY = Double.MAX_VALUE;
            double maxY = Double.MIN_VALUE;
            for (Node child : node.children) {
                layoutTree(child, x, y, h, depth + 1);
                minY = Math.min(minY, child.y);
                maxY = Math.max(maxY, child.y);
            }
            node.x = 50 + depth * 100;
            node.y = (minY + maxY) / 2;
        }
    }

    private void drawTree(GraphicsContext gc, Node node) {
        gc.setFill(Color.BLACK);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.CENTER);

        // Draw links to children
        for (Node child : node.children) {
            // Elbow connector
            gc.setStroke(Color.BLACK);
            gc.strokeLine(node.x, node.y, node.x, child.y); // Vertical segment
            gc.strokeLine(node.x, child.y, child.x, child.y); // Horizontal segment

            drawTree(gc, child);
        }

        // Draw Node
        gc.setFill(node.children.isEmpty() ? Color.FORESTGREEN : Color.DARKBLUE);
        gc.fillOval(node.x - 4, node.y - 4, 8, 8);

        if (node.children.isEmpty()) {
            gc.setFill(Color.BLACK);
            gc.fillText(node.name, node.x + 10, node.y);
        }
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

        pla.add(new Node("Arabidopsis thaliana"));
        pla.add(new Node("Oryza sativa"));

        bac.add(new Node("E. coli"));
        bac.add(new Node("B. subtilis"));

        return root;
    }

    public static void show(Stage stage) {
        new PhylogeneticTreeViewer().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
