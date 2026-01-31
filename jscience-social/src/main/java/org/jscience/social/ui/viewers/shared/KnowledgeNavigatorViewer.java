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

package org.jscience.social.ui.viewers.shared;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import org.jscience.core.ui.AbstractViewer;
import org.jscience.core.ui.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Knowledge Navigator Viewer.
 * Visualizes the hierarchical relationship between scientific domains and entities.
 * Supports navigation from subatomic to social scales.
 */
public final class KnowledgeNavigatorViewer extends AbstractViewer {

    private final Pane graphPane = new Pane();
    private final Label detailLabel = new Label("Select an entity to explore connections.");

    public KnowledgeNavigatorViewer() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #0b1018;");

        Label title = new Label("JSPI KNOWLEDGE NAVIGATOR");
        title.setStyle("-fx-text-fill: #00d4ff; -fx-font-size: 20px; -fx-font-weight: bold; -fx-letter-spacing: 2px;");

        SplitPane split = new SplitPane();
        split.setStyle("-fx-background: transparent;");

        graphPane.setStyle("-fx-background-color: #0f1724; -fx-border-color: #1a3050;");
        
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(10));
        sidebar.setMinWidth(200);
        sidebar.setStyle("-fx-background-color: #161e2b;");
        
        detailLabel.setWrapText(true);
        detailLabel.setStyle("-fx-text-fill: #a0acba;");
        
        sidebar.getChildren().addAll(new Label("ENTITY DETAILS"), detailLabel);
        
        split.getItems().addAll(graphPane, sidebar);
        split.setDividerPositions(0.75);

        root.getChildren().addAll(title, split);
        VBox.setVgrow(split, Priority.ALWAYS);
        setCenter(root);
        
        renderBaseGraph();
    }

    private void renderBaseGraph() {
        graphPane.getChildren().clear();
        
        // Mock nodes for scales
        double cw = 600, ch = 400;
        addNode("Particle", cw/2, ch/2, Color.GOLD, "Subatomic scale / Quantum mechanics");
        addNode("Atom", cw/2 - 100, ch/2 - 50, Color.PEACHPUFF, "Bohr model / Chemical elements");
        addNode("Molecule", cw/2 - 150, ch/2 + 50, Color.CRIMSON, "Covalent bonds / Bio-chemistry");
        addNode("Protein", cw/2 - 50, ch/2 + 100, Color.GREENYELLOW, "Amino acid chains / Folding");
        addNode("Cell", cw/2 + 100, ch/2 + 80, Color.AQUAMARINE, "Biological building blocks");
        addNode("Individual", cw/2 + 150, ch/2 - 20, Color.CORNFLOWERBLUE, "Person / Demographic unit");
        addNode("Organization", cw/2 + 100, ch/2 - 120, Color.ORCHID, "Social groups / Networks");

        // Simple connections
        addConnection("Particle", "Atom");
        addConnection("Atom", "Molecule");
        addConnection("Molecule", "Protein");
        addConnection("Protein", "Cell");
        addConnection("Cell", "Individual");
        addConnection("Individual", "Organization");
    }

    private void addNode(String name, double x, double y, Color color, String info) {
        VBox nodeBox = new VBox(5);
        nodeBox.setAlignment(javafx.geometry.Pos.CENTER);
        nodeBox.setLayoutX(x);
        nodeBox.setLayoutY(y);

        Circle circle = new Circle(15, color);
        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(2);
        
        Label lbl = new Label(name);
        lbl.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        
        nodeBox.getChildren().addAll(circle, lbl);
        nodeBox.setCursor(javafx.scene.Cursor.HAND);
        nodeBox.setOnMouseClicked(e -> detailLabel.setText("Entity: " + name + "\n\n" + info));

        graphPane.getChildren().add(nodeBox);
    }

    private void addConnection(String n1, String n2) {
        // Very simplified connection logic
        Line line = new Line();
        line.setStroke(Color.web("#304060"));
        line.setStrokeWidth(1);
        graphPane.getChildren().add(0, line);
    }

    @Override
    public List<Parameter<?>> getViewerParameters() {
        return new ArrayList<>();
    }

    @Override public String getName() { return "Knowledge Navigator"; }
    @Override public String getCategory() { return "General / Shared"; }
    @Override public String getDescription() { return "Cross-domain scientific scale navigator."; }
    @Override public String getLongDescription() { return "A multi-scale visualization tool that connects various scientific disciplines from particle physics to sociology."; }
}

