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

package org.jscience.ui.viewers.biology.phylogeny;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import org.jscience.ui.Parameter;
import org.jscience.ui.BooleanParameter;
import org.jscience.ui.StringParameter;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.i18n.I18n;
import org.jscience.biology.Taxon;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.biology.io.PhylogeneticTreeReader;

import javafx.application.Platform;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Phylogenetic Tree Viewer.
 * Refactored to be parameter-based.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PhylogeneticTreeViewer extends AbstractViewer {

    private Taxon selectedNode = null;
    private boolean radialMode = false;
    private int leafCounter = 0;
    private Taxon treeRoot;
    private Canvas canvas;
    private Label infoPanel;
    private String ncbiQuery = "";
    
    private final List<Parameter<?>> parameters = new ArrayList<>();

    public PhylogeneticTreeViewer() {
        setupParameters();
        initUI();
        loadData();
    }
    
    @Override public String getName() { return I18n.getInstance().get("viewer.phylogenetictreeviewer.name", "Phylogenetic Tree Viewer"); }
    @Override public String getCategory() { return I18n.getInstance().get("category.biology", "Biology"); }

    private void setupParameters() {
        parameters.add(new BooleanParameter("viewer.phylogenetictreeviewer.radial", I18n.getInstance().get("viewer.phylogenetictreeviewer.radial", "Radial Mode"), radialMode, v -> {
            radialMode = v;
            updateLayoutAndDraw(canvas);
        }));

        parameters.add(new StringParameter("viewer.phylogenetictreeviewer.ncbi.query", I18n.getInstance().get("viewer.phylogenetictreeviewer.ncbi.query", "NCBI Query"), ncbiQuery, v -> ncbiQuery = v));
        
        parameters.add(new BooleanParameter("viewer.phylogenetictreeviewer.ncbi.search", I18n.getInstance().get("viewer.phylogenetictreeviewer.ncbi.search", "Search NCBI"), false, v -> {
            if (v) performNCBISearch();
        }));
        
        parameters.add(new BooleanParameter("viewer.phylogenetictreeviewer.reset", I18n.getInstance().get("viewer.phylogenetictreeviewer.reset", "Reset Tree"), false, v -> {
            if (v) {
                loadData();
                updateLayoutAndDraw(canvas);
            }
        }));
    }

    private void initUI() {
        getStyleClass().add("viewer-root");
        canvas = new Canvas(900, 700);
        setCenter(canvas);
        
        widthProperty().addListener(e -> { canvas.setWidth(getWidth() - 250); updateLayoutAndDraw(canvas); });
        heightProperty().addListener(e -> { canvas.setHeight(getHeight()); updateLayoutAndDraw(canvas); });

        infoPanel = new Label(I18n.getInstance().get("viewer.phylogenetictreeviewer.info.default", "Select a node."));
        infoPanel.setWrapText(true);
        infoPanel.setPrefWidth(220);
        
        VBox sidebar = new VBox(20, infoPanel);
        sidebar.setPadding(new javafx.geometry.Insets(10));
        sidebar.getStyleClass().add("viewer-sidebar");
        setRight(sidebar);

        canvas.setOnMouseClicked(e -> {
            if (treeRoot == null) return;
            Taxon clicked = findNode(treeRoot, e.getX(), e.getY());
            if (clicked != null) {
                selectedNode = clicked;
                infoPanel.setText(clicked.getName() + "\nCOI: " + clicked.getCoi());
            } else {
                selectedNode = null;
                infoPanel.setText("Select a node.");
            }
            drawTree(canvas.getGraphicsContext2D(), treeRoot);
        });
    }
    
    private void performNCBISearch() {
        if (ncbiQuery.isEmpty()) return;
        new Thread(() -> {
            try {
                org.jscience.biology.loaders.NCBITaxonomyReader reader = new org.jscience.biology.loaders.NCBITaxonomyReader();
                List<Long> taxIds = reader.searchByName(ncbiQuery);
                if (taxIds != null && !taxIds.isEmpty()) {
                     Optional<org.jscience.biology.taxonomy.Species> speciesOpt = reader.fetchByTaxId(taxIds.get(0));
                     if (speciesOpt.isPresent()) {
                         String lineage = speciesOpt.get().getAttribute("lineage");
                         if (lineage != null) {
                             Platform.runLater(() -> {
                                 this.treeRoot = buildLineageTree(lineage, speciesOpt.get().getScientificName());
                                 updateLayoutAndDraw(canvas);
                             });
                         }
                     }
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        }).start();
    }

    private void loadData() {
        try {
            PhylogeneticTreeReader reader = new PhylogeneticTreeReader();
            java.io.InputStream is = getClass().getResourceAsStream("/org/jscience/biology/data/primates.csv");
            if (is != null) this.treeRoot = reader.read(is);
        } catch (Exception e) {}
    }
    
    private Taxon buildLineageTree(String lineage, String terminalName) {
        String[] levels = lineage.split("; ");
        Taxon root = null, current = null;
        for (int i = 0; i < levels.length; i++) {
            Taxon t = new Taxon("ncbi_" + i, (current == null ? "" : current.getId()), levels[i].trim(), Real.of(Math.random()), Real.of(Math.random()), Real.of(Math.random()));
            if (root == null) root = t;
            if (current != null) current.addChild(t);
            current = t;
        }
        return root;
    }

    private void updateLayoutAndDraw(Canvas canvas) {
        if (treeRoot == null) return;
        leafCounter = 0;
        if (radialMode) layoutRadial(treeRoot, 0, Math.PI * 2, 50, canvas.getWidth()/2, canvas.getHeight()/2);
        else layoutLinear(treeRoot, 0);
        drawTree(canvas.getGraphicsContext2D(), treeRoot);
    }

    private void layoutLinear(Taxon node, double depth) {
        if (node.getChildren().isEmpty()) {
            node.x = 600; node.y = 50 + leafCounter * 40; leafCounter++;
        } else {
            double minY = 10000, maxY = -10000;
            for (Taxon child : node.getChildren()) {
                layoutLinear(child, depth + 1);
                minY = Math.min(minY, child.y); maxY = Math.max(maxY, child.y);
            }
            node.x = 50 + depth * 150; node.y = (minY + maxY) / 2;
        }
    }

    private void layoutRadial(Taxon node, double startAngle, double endAngle, double radius, double cx, double cy) {
        if (node.getChildren().isEmpty()) {
            node.angle = (startAngle + endAngle) / 2; node.radius = 200;
        } else {
            node.radius = radius;
            double currentStart = startAngle;
            int totalLeaves = countLeaves(node);
            double minA = 10, maxA = -10;
            for (Taxon child : node.getChildren()) {
                double span = (endAngle - startAngle) * countLeaves(child) / totalLeaves;
                layoutRadial(child, currentStart, currentStart + span, radius + 60, cx, cy);
                minA = Math.min(minA, child.angle); maxA = Math.max(maxA, child.angle);
                currentStart += span;
            }
            node.angle = (minA + maxA) / 2;
        }
        node.x = cx + node.radius * Math.cos(node.angle); node.y = cy + node.radius * Math.sin(node.angle);
    }

    private int countLeaves(Taxon n) {
        if (n.getChildren().isEmpty()) return 1;
        int s = 0; for (Taxon c : n.getChildren()) s += countLeaves(c); return s;
    }

    private Taxon findNode(Taxon root, double mx, double my) {
        if (Math.abs(root.x - mx) < 15 && Math.abs(root.y - my) < 15) return root;
        for (Taxon child : root.getChildren()) {
            Taxon f = findNode(child, mx, my); if (f != null) return f;
        }
        return null;
    }

    private void drawTree(GraphicsContext gc, Taxon node) {
        if (node == treeRoot) gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.GRAY);
        for (Taxon child : node.getChildren()) {
            if (radialMode) gc.strokeLine(node.x, node.y, child.x, child.y);
            else { gc.strokeLine(node.x, node.y, node.x, child.y); gc.strokeLine(node.x, child.y, child.x, child.y); }
            drawTree(gc, child);
        }
        gc.setFill(node == selectedNode ? Color.RED : Color.BLUE);
        gc.fillOval(node.x - 5, node.y - 5, 10, 10);
        if (node.getChildren().isEmpty() || node == selectedNode) {
            gc.setFill(Color.BLACK); gc.fillText(node.getName(), node.x + 12, node.y);
        }
    }

    @Override public String getDescription() { return I18n.getInstance().get("viewer.phylogenetictreeviewer.desc", "Phylogenetic tree browser."); }
    @Override public String getLongDescription() { return I18n.getInstance().get("viewer.phylogenetictreeviewer.longdesc", "Tree of Life viewer."); }
    @Override public List<Parameter<?>> getViewerParameters() { return parameters; }
}
