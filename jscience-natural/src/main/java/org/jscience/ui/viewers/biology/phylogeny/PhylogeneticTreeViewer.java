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

package org.jscience.ui.viewers.biology.phylogeny;

import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.jscience.ui.ThemeManager;
import org.jscience.ui.i18n.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * Phylogenetic Tree Viewer.
 * Visualizes evolutionary relationships with simulated genetic marker data.
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

        // Real Genetic Markers (Normalized 0.0 - 1.0)
        // COI = Cytochrome c oxidase subunit I
        // 16S = 16S ribosomal RNA
        // CYTB = Cytochrome b
        double coi, rna16s, cytb;

        Node(String name) {
            this(name, 0, 0, 0);
        }

        Node(String name, double coi, double rna16s, double cytb) {
            this.name = name;
            this.coi = coi;
            this.rna16s = rna16s;
            this.cytb = cytb;
        }

        void add(Node n) {
            children.add(n);
        }
    }

    private Node selectedNode = null;
    private boolean radialMode = false; // Toggle state
    private int leafCounter = 0;
    private Node treeRoot;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dark-viewer-root");

        Canvas canvas = new Canvas(900, 700);
        root.setCenter(canvas);

        Label infoPanel = new Label(I18n.getInstance().get("phylogeny.info.default"));
        infoPanel.getStyleClass().add("dark-viewer-sidebar");

        javafx.scene.control.Button toggleBtn = new javafx.scene.control.Button(
                I18n.getInstance().get("phylogeny.toggle_view"));
        toggleBtn.setOnAction(e -> {
            radialMode = !radialMode;
            updateLayoutAndDraw(canvas);
        });

        HBox controls = new HBox(10, toggleBtn, infoPanel);
        controls.setPadding(new javafx.geometry.Insets(10));
        root.setBottom(controls);

        this.treeRoot = buildPrimateTree();
        updateLayoutAndDraw(canvas);

        canvas.setOnMouseClicked(e -> {
            Node clicked = findNode(treeRoot, e.getX(), e.getY());
            if (clicked != null) {
                selectedNode = clicked;
                String txt = I18n.getInstance().get("phylogeny.info.selected",
                        clicked.name, clicked.coi, clicked.rna16s, clicked.cytb);
                infoPanel.setText(txt);
                drawTree(canvas.getGraphicsContext2D(), treeRoot);
            } else {
                selectedNode = null;
                infoPanel.setText(I18n.getInstance().get("phylogeny.info.default"));
                drawTree(canvas.getGraphicsContext2D(), treeRoot);
            }
        });

        Scene scene = new Scene(root, 900, 750);
        ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(I18n.getInstance().get("viewer.phylogeny"));
        stage.setScene(scene);
        stage.show();
    }

    // --- Layout Logic ---

    private void updateLayoutAndDraw(Canvas canvas) {
        leafCounter = 0;
        if (radialMode) {
            layoutRadial(treeRoot, 0, Math.PI * 2, 50);
        } else {
            layoutLinear(treeRoot, 0);
        }
        drawTree(canvas.getGraphicsContext2D(), treeRoot);
    }

    private void layoutLinear(Node node, double depth) {
        if (node.children.isEmpty()) {
            node.x = 600; // Leave space for metadata
            node.y = 50 + leafCounter * 40; // More spacing for clearer text
            leafCounter++;
        } else {
            double minY = Double.MAX_VALUE;
            double maxY = Double.MIN_VALUE;
            for (Node child : node.children) {
                layoutLinear(child, depth + 1);
                minY = Math.min(minY, child.y);
                maxY = Math.max(maxY, child.y);
            }
            node.x = 50 + depth * 150;
            node.y = (minY + maxY) / 2;
        }
    }

    private void layoutRadial(Node node, double startAngle, double endAngle, double currentRadius) {
        if (node.children.isEmpty()) {
            node.angle = (startAngle + endAngle) / 2;
            node.radius = 300;
        } else {
            node.radius = currentRadius;
            double currentStart = startAngle;
            double totalSpan = endAngle - startAngle;
            int localLeaves = countLeaves(node);

            double myMinAngle = Double.MAX_VALUE;
            double myMaxAngle = Double.MIN_VALUE;

            for (Node child : node.children) {
                int childLeaves = countLeaves(child);
                double childSpan = totalSpan * ((double) childLeaves / localLeaves);
                double childEnd = currentStart + childSpan;
                layoutRadial(child, currentStart, childEnd, currentRadius + 60);
                myMinAngle = Math.min(myMinAngle, child.angle);
                myMaxAngle = Math.max(myMaxAngle, child.angle);
                currentStart = childEnd;
            }
            node.angle = (myMinAngle + myMaxAngle) / 2;
        }
        node.x = 450 + node.radius * Math.cos(node.angle);
        node.y = 350 + node.radius * Math.sin(node.angle);
    }

    private int countLeaves(Node n) {
        if (n.children.isEmpty())
            return 1;
        int sum = 0;
        for (Node c : n.children)
            sum += countLeaves(c);
        return sum;
    }

    private Node findNode(Node root, double mx, double my) {
        if (Math.abs(root.x - mx) < 15 && Math.abs(root.y - my) < 15)
            return root;
        for (Node child : root.children) {
            Node found = findNode(child, mx, my);
            if (found != null)
                return found;
        }
        return null;
    }

    // --- Rendering Logic ---

    private void drawTree(GraphicsContext gc, Node node) {
        if (node == treeRoot) {
            gc.clearRect(0, 0, 900, 700);
            if (!radialMode) {
                // Header for Markers
                gc.setFill(Color.web("#333333")); // Dark grey for headers on light background
                gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 12));
                // Shifted right to match heatmap (formerly 620 -> 760)
                gc.fillText(I18n.getInstance().get("phylogeny.marker.coi"), 760, 20);
                gc.fillText(I18n.getInstance().get("phylogeny.marker.rna16s"), 790, 20);
                gc.fillText(I18n.getInstance().get("phylogeny.marker.cytb"), 820, 20);
            }
        }

        gc.setStroke(Color.web("#666")); // Darker branch lines
        gc.setLineWidth(1.5);

        for (Node child : node.children) {
            if (radialMode) {
                gc.strokeLine(node.x, node.y, child.x, child.y);
            } else {
                gc.strokeLine(node.x, node.y, node.x, child.y);
                gc.strokeLine(node.x, child.y, child.x, child.y);
            }
            drawTree(gc, child);
        }

        // Draw Node
        if (node == selectedNode) {
            gc.setFill(Color.ORANGERED);
            gc.fillOval(node.x - 7, node.y - 7, 14, 14);
        } else {
            gc.setFill(node.children.isEmpty() ? Color.web("#4CAF50") : Color.web("#2196F3"));
            gc.fillOval(node.x - 5, node.y - 5, 10, 10);
        }

        // Draw Text - High Contrast Fix
        // Changed to Dark Grey for readability on light background (or default canvas
        // bg)
        Color textColor = Color.web("#111111"); // Almost black

        if (!radialMode || node == selectedNode || node.children.isEmpty()) {
            gc.setFill(textColor);
            gc.setTextAlign(TextAlignment.LEFT);
            gc.setTextBaseline(VPos.CENTER);
            gc.setFont(javafx.scene.text.Font.font("SansSerif", 12));

            double tx = node.x + 12;
            double ty = node.y;

            // In radial mode, rotate text to align with ray? Simplified for now: just draw.
            gc.fillText(node.name, tx, ty);
        }

        // Draw Heatmap
        if (node.children.isEmpty()) {
            if (radialMode) {
                drawRadialHeatmap(gc, node, 310, node.angle);
            } else {
                // Shifted linear heatmap to X=760 to avoid text overlap (Labels are around
                // 612-750)
                drawLinearHeatmap(gc, node, 760, node.y);
            }
        }
    }

    private void drawLinearHeatmap(GraphicsContext gc, Node n, double startX, double y) {
        drawCell(gc, startX, y - 9, getColor(n.coi));
        drawCell(gc, startX + 30, y - 9, getColor(n.rna16s));
        drawCell(gc, startX + 60, y - 9, getColor(n.cytb));
    }

    private void drawRadialHeatmap(GraphicsContext gc, Node n, double startRadius, double angle) {
        // Project dots outward
        drawDot(gc, 450 + (startRadius + 10) * Math.cos(angle), 350 + (startRadius + 10) * Math.sin(angle),
                getColor(n.coi));
        drawDot(gc, 450 + (startRadius + 25) * Math.cos(angle), 350 + (startRadius + 25) * Math.sin(angle),
                getColor(n.rna16s));
        drawDot(gc, 450 + (startRadius + 40) * Math.cos(angle), 350 + (startRadius + 40) * Math.sin(angle),
                getColor(n.cytb));
    }

    private void drawCell(GraphicsContext gc, double x, double y, Color c) {
        gc.setFill(c);
        gc.fillRect(x, y, 20, 18);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, y, 20, 18);
    }

    private void drawDot(GraphicsContext gc, double x, double y, Color c) {
        gc.setFill(c);
        gc.fillOval(x - 5, y - 5, 10, 10);
    }

    private Color getColor(double val) {
        // Heatmap: Blue (Low) -> Red (High)
        // 0.0 -> Blue, 0.5 -> Yellow, 1.0 -> Red
        return Color.hsb((1.0 - val) * 240, 0.8, 0.9);
    }

    // --- Data ---
    // Realistic dataset for Primates + Outgroups
    private Node buildPrimateTree() {
        Node root = new Node("Primates (Order)");

        Node strepsirrhini = new Node("Strepsirrhini");
        Node haplorhini = new Node("Haplorhini");
        root.add(strepsirrhini);
        root.add(haplorhini);

        // Strepsirrhini (Lemurs, etc)
        Node lemuriformes = new Node("Lemuriformes");
        Node lorisiformes = new Node("Lorisiformes");
        strepsirrhini.add(lemuriformes);
        strepsirrhini.add(lorisiformes);

        lemuriformes.add(new Node("Lemur catta", 0.72, 0.65, 0.45));
        lemuriformes.add(new Node("Microcebus murinus", 0.70, 0.63, 0.48));
        lorisiformes.add(new Node("Nycticebus coucang", 0.68, 0.60, 0.50));

        // Haplorhini (Tarsiers + Simians)
        Node tarsiiformes = new Node("Tarsiiformes");
        Node simiiformes = new Node("Simiiformes");
        haplorhini.add(tarsiiformes);
        haplorhini.add(simiiformes);

        tarsiiformes.add(new Node("Tarsius syrichta", 0.75, 0.68, 0.55));

        // Simians
        Node platyrrhini = new Node("Platyrrhini (New World)");
        Node catarrhini = new Node("Catarrhini (Old World)");
        simiiformes.add(platyrrhini);
        simiiformes.add(catarrhini);

        platyrrhini.add(new Node("Saimiri sciureus", 0.82, 0.75, 0.65));
        platyrrhini.add(new Node("Alouatta palliata", 0.80, 0.73, 0.62));

        // Catarrhini
        Node cercopithecoidea = new Node("Cercopithecoidea");
        Node hominoidea = new Node("Hominoidea (Apes)");
        catarrhini.add(cercopithecoidea);
        catarrhini.add(hominoidea);

        cercopithecoidea.add(new Node("Macaca mulatta", 0.88, 0.80, 0.75));
        cercopithecoidea.add(new Node("Papio anubis", 0.87, 0.79, 0.74));

        // Apes
        Node hylobatidae = new Node("Hylobatidae");
        Node hominidae = new Node("Hominidae");
        hominoidea.add(hylobatidae);
        hominoidea.add(hominidae);

        hylobatidae.add(new Node("Hylobates lar", 0.90, 0.85, 0.80));

        Node ponginae = new Node("Ponginae");
        Node homininae = new Node("Homininae");
        hominidae.add(ponginae);
        hominidae.add(homininae);

        ponginae.add(new Node("Pongo abelii", 0.92, 0.88, 0.82));

        homininae.add(new Node("Gorilla gorilla", 0.95, 0.92, 0.88));
        homininae.add(new Node("Pan troglodytes", 0.98, 0.96, 0.94));
        homininae.add(new Node("Homo sapiens", 0.99, 0.98, 0.97));

        return root;
    }

    public static void show(Stage stage) {
        new PhylogeneticTreeViewer().start(stage);
    }
}
