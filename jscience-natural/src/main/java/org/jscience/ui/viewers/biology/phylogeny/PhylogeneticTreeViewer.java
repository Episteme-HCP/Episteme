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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.Parameter;
import org.jscience.biology.Taxon;
import org.jscience.biology.io.PhylogeneticTreeReader;

import javafx.application.Platform;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.text.MessageFormat;

/**
 * Phylogenetic Tree Viewer.
 * Visualizes evolutionary relationships with simulated genetic marker data.
 
 * <p>
 * <b>Reference:</b><br>
 * Holland, J. H. (1975). <i>Adaptation in Natural and Artificial Systems</i>. University of Michigan Press.
 * </p>
 *
 */
public class PhylogeneticTreeViewer extends AbstractViewer {

    private Taxon selectedNode = null;
    private boolean radialMode = false;
    private int leafCounter = 0;
    private Taxon treeRoot;
    private Canvas canvas;

    public PhylogeneticTreeViewer() {
        initUI();
    }
    
    @Override
    public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictreeviewer.name", "Phylogenetic Tree Viewer"); }
    
    @Override
    public String getCategory() { return org.jscience.ui.i18n.I18n.getInstance().get("category.biology", "Biology"); }

    private void initUI() {
        this.getStyleClass().add("viewer-root");

        canvas = new Canvas(900, 700);
        this.setCenter(canvas);
        
        this.widthProperty().addListener(e -> { canvas.setWidth(getWidth()); updateLayoutAndDraw(canvas); });
        this.heightProperty().addListener(e -> { canvas.setHeight(getHeight()); updateLayoutAndDraw(canvas); });

        Label infoPanel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictreeviewer.info.default", "Select a node to view details."));
        infoPanel.getStyleClass().add("viewer-sidebar");

        javafx.scene.control.Button toggleBtn = new javafx.scene.control.Button(org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictreeviewer.toggle_view", "Toggle View"));
        toggleBtn.setOnAction(e -> {
            radialMode = !radialMode;
            updateLayoutAndDraw(canvas);
        });

        HBox controls = new HBox(10, toggleBtn, infoPanel);
        controls.setPadding(new javafx.geometry.Insets(10));
        
        // Add NCBI Taxonomy query section
        VBox ncbiSection = createNCBIQuerySection();
        controls.getChildren().add(0, ncbiSection);
        
        this.setBottom(controls);

        loadData();
        
        canvas.setOnMouseClicked(e -> {
            Taxon clicked = findNode(treeRoot, e.getX(), e.getY());
            if (clicked != null) {
                selectedNode = clicked;
                String txt = MessageFormat.format(org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictreeviewer.info.selected", "{0} (COI: {1}, 16S: {2}, Cytb: {3})"), 
                    clicked.getName(), clicked.getCoi(), clicked.getRna16s(), clicked.getCytb());
                infoPanel.setText(txt);
                drawTree(canvas.getGraphicsContext2D(), treeRoot);
            } else {
                selectedNode = null;
                infoPanel.setText(org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictreeviewer.info.default", "Select a node to view details."));
                drawTree(canvas.getGraphicsContext2D(), treeRoot);
            }
        });
    }
    
    private void loadData() {
        try {
            PhylogeneticTreeReader reader = new PhylogeneticTreeReader();
            // Load via classloader
            java.io.InputStream is = getClass().getResourceAsStream("/org/jscience/biology/data/primates.csv");
            if (is != null) {
                this.treeRoot = reader.read(is);
            } else {
                System.err.println("Could not find primates.csv");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Creates NCBI Taxonomy query section for browsing real species data.
     */
    private VBox createNCBIQuerySection() {
        VBox section = new VBox(5);
        section.setPadding(new javafx.geometry.Insets(5));
        
        Label ncbiLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictreeviewer.ncbi.title", "NCBI Query"));
        ncbiLabel.getStyleClass().add("font-bold"); // Replaced inline style: -fx-font-weight: bold; -fx-font-size: 11px;
        
        javafx.scene.control.TextField searchField = new javafx.scene.control.TextField();
        searchField.setPromptText(org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictreeviewer.ncbi.prompt", "Enter species name..."));
        searchField.setPrefWidth(180);
        
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 10px;");
        statusLabel.setWrapText(true);
        statusLabel.setPrefWidth(180);
        
        javafx.scene.control.Button queryButton = new javafx.scene.control.Button(org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictreeviewer.ncbi.search", "Search"));
        queryButton.setStyle("-fx-font-size: 10px;");
        
        javafx.scene.control.Button resetTreeBtn = new javafx.scene.control.Button(org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictreeviewer.reset", "Reset Tree"));
        resetTreeBtn.setStyle("-fx-font-size: 10px;");
        resetTreeBtn.setOnAction(e -> {
            loadData();
            updateLayoutAndDraw(canvas);
            statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictreeviewer.ncbi.reset", "Tree reset to default"));
        });

        queryButton.setOnAction(e -> {
            String searchTerm = searchField.getText().trim();
            if (searchTerm.isEmpty()) {
                statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictreeviewer.ncbi.empty", "Enter a species name"));
                return;
            }
            
            statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictreeviewer.ncbi.querying", "Querying NCBI..."));
            queryButton.setDisable(true);
            
            // Query in background thread
            new Thread(() -> {
                try {
                    org.jscience.biology.loaders.NCBITaxonomyReader reader = 
                        new org.jscience.biology.loaders.NCBITaxonomyReader();
                    java.util.List<Long> taxIds = reader.searchByName(searchTerm);
                    
                    if (taxIds != null && !taxIds.isEmpty()) {
                         long taxId = taxIds.get(0);
                         Optional<org.jscience.biology.taxonomy.Species> speciesOpt = reader.fetchByTaxId(taxId);

                         Platform.runLater(() -> {
                            if (speciesOpt.isPresent()) {
                                org.jscience.biology.taxonomy.Species species = speciesOpt.get();
                                String lineage = species.getAttribute("lineage");
                                if (lineage != null) {
                                    this.treeRoot = buildLineageTree(lineage, species.getScientificName());
                                    updateLayoutAndDraw(canvas);
                                    statusLabel.setText(MessageFormat.format(org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictreeviewer.ncbi.loaded", "Loaded: {0}"), species.getScientificName()));
                                } else {
                                    statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictreeviewer.ncbi.nolineage", "No lineage data available."));
                                }
                            } else {
                                statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictreeviewer.ncbi.ferror", "Failed to fetch taxon details"));
                            }
                            queryButton.setDisable(false);
                         });
                    } else {
                        Platform.runLater(() -> {
                            statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictreeviewer.ncbi.notfound", "No results found"));
                            queryButton.setDisable(false);
                        });
                    }
                } catch (Exception ex) {
                    Platform.runLater(() -> {
                        statusLabel.setText(MessageFormat.format(org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictreeviewer.ncbi.error", "Error: {0}"), ex.getMessage()));
                        queryButton.setDisable(false);
                    });
                }
            }).start();
        });
        
        HBox btnBox = new HBox(5, queryButton, resetTreeBtn);
        section.getChildren().addAll(ncbiLabel, searchField, btnBox, statusLabel);
        return section;
    }

    private Taxon buildLineageTree(String lineage, String terminalName) {
        String[] levels = lineage.split("; ");
        Taxon root = null;
        Taxon current = null;
        for (int i = 0; i < levels.length; i++) {
            String name = levels[i].trim();
            if (name.isEmpty()) continue;
            Taxon t = new Taxon("ncbi_" + i, (current == null ? "" : current.getId()), name, org.jscience.mathematics.numbers.real.Real.of(Math.random()), org.jscience.mathematics.numbers.real.Real.of(Math.random()), org.jscience.mathematics.numbers.real.Real.of(Math.random()));
            if (root == null) root = t;
            if (current != null) current.addChild(t);
            current = t;
        }
        // Add the searched species as the leaf if it's not the last level
        if (current != null && !levels[levels.length - 1].equalsIgnoreCase(terminalName)) {
             Taxon t = new Taxon("ncbi_leaf", current.getId(), terminalName, org.jscience.mathematics.numbers.real.Real.of(Math.random()), org.jscience.mathematics.numbers.real.Real.of(Math.random()), org.jscience.mathematics.numbers.real.Real.of(Math.random()));
             current.addChild(t);
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
            node.x = 600; 
            node.y = 50 + leafCounter * 40;
            leafCounter++;
        } else {
            double minY = Double.MAX_VALUE;
            double maxY = Double.MIN_VALUE;
            for (Taxon child : node.getChildren()) {
                layoutLinear(child, depth + 1);
                minY = Math.min(minY, child.y);
                maxY = Math.max(maxY, child.y);
            }
            node.x = 50 + depth * 150;
            node.y = (minY + maxY) / 2;
        }
    }

    private void layoutRadial(Taxon node, double startAngle, double endAngle, double currentRadius, double cx, double cy) {
        if (node.getChildren().isEmpty()) {
            node.angle = (startAngle + endAngle) / 2;
            node.radius = Math.min(cx, cy) - 50; 
        } else {
            node.radius = currentRadius;
            double currentStart = startAngle;
            double totalSpan = endAngle - startAngle;
            int localLeaves = countLeaves(node);

            double myMinAngle = Double.MAX_VALUE;
            double myMaxAngle = Double.MIN_VALUE;

            for (Taxon child : node.getChildren()) {
                int childLeaves = countLeaves(child);
                double childSpan = totalSpan * ((double) childLeaves / localLeaves);
                double childEnd = currentStart + childSpan;
                layoutRadial(child, currentStart, childEnd, currentRadius + 60, cx, cy);
                myMinAngle = Math.min(myMinAngle, child.angle);
                myMaxAngle = Math.max(myMaxAngle, child.angle);
                currentStart = childEnd;
            }
            node.angle = (myMinAngle + myMaxAngle) / 2;
        }
        node.x = cx + node.radius * Math.cos(node.angle);
        node.y = cy + node.radius * Math.sin(node.angle);
    }

    private int countLeaves(Taxon n) {
        if (n.getChildren().isEmpty()) return 1;
        int sum = 0;
        for (Taxon c : n.getChildren()) sum += countLeaves(c);
        return sum;
    }

    private Taxon findNode(Taxon root, double mx, double my) {
        if (Math.abs(root.x - mx) < 15 && Math.abs(root.y - my) < 15) return root;
        for (Taxon child : root.getChildren()) {
            Taxon found = findNode(child, mx, my);
            if (found != null) return found;
        }
        return null;
    }

    private void drawTree(GraphicsContext gc, Taxon node) {
        if (node == treeRoot) {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            if (!radialMode) {
                gc.setFill(Color.web("#333333")); 
                gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 12));
                gc.fillText(org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictreeviewer.marker.coi", "COI (Mitochondrial)"), 760, 20);
                gc.fillText(org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictreeviewer.marker.rna16s", "16S RNA"), 790, 20);
                gc.fillText(org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictreeviewer.marker.cytb", "CytB"), 820, 20);
            }
        }

        gc.setStroke(Color.web("#666")); 
        gc.setLineWidth(1.5);

        for (Taxon child : node.getChildren()) {
            if (radialMode) {
                gc.strokeLine(node.x, node.y, child.x, child.y);
            } else {
                gc.strokeLine(node.x, node.y, node.x, child.y);
                gc.strokeLine(node.x, child.y, child.x, child.y);
            }
            drawTree(gc, child);
        }

        if (node == selectedNode) {
            gc.setFill(Color.ORANGERED);
            gc.fillOval(node.x - 7, node.y - 7, 14, 14);
        } else {
            gc.setFill(node.getChildren().isEmpty() ? Color.web("#4CAF50") : Color.web("#2196F3"));
            gc.fillOval(node.x - 5, node.y - 5, 10, 10);
        }

        Color textColor = Color.web("#111111"); 

        if (!radialMode || node == selectedNode || node.getChildren().isEmpty()) {
            gc.setFill(textColor);
            gc.setTextAlign(TextAlignment.LEFT);
            gc.setTextBaseline(VPos.CENTER);
            gc.setFont(javafx.scene.text.Font.font("SansSerif", 12));
            gc.fillText(node.getName(), node.x + 12, node.y);
        }

        if (node.getChildren().isEmpty()) {
            if (radialMode) {
                drawRadialHeatmap(gc, node, Math.min(canvas.getWidth(), canvas.getHeight())/2 - 40, node.angle);
            } else {
                drawLinearHeatmap(gc, node, 760, node.y);
            }
        }
    }

    private void drawLinearHeatmap(GraphicsContext gc, Taxon n, double startX, double y) {
        drawCell(gc, startX, y - 9, getColor(n.getCoi().doubleValue()));
        drawCell(gc, startX + 30, y - 9, getColor(n.getRna16s().doubleValue()));
        drawCell(gc, startX + 60, y - 9, getColor(n.getCytb().doubleValue()));
    }

    private void drawRadialHeatmap(GraphicsContext gc, Taxon n, double startRadius, double angle) {
        double cx = canvas.getWidth()/2, cy = canvas.getHeight()/2;
        drawDot(gc, cx + (startRadius + 10) * Math.cos(angle), cy + (startRadius + 10) * Math.sin(angle), getColor(n.getCoi().doubleValue()));
        drawDot(gc, cx + (startRadius + 25) * Math.cos(angle), cy + (startRadius + 25) * Math.sin(angle), getColor(n.getRna16s().doubleValue()));
        drawDot(gc, cx + (startRadius + 40) * Math.cos(angle), cy + (startRadius + 40) * Math.sin(angle), getColor(n.getCytb().doubleValue()));
    }

    private void drawCell(GraphicsContext gc, double x, double y, Color c) {
        gc.setFill(c); gc.fillRect(x, y, 20, 18); gc.setStroke(Color.BLACK); gc.strokeRect(x, y, 20, 18);
    }

    private void drawDot(GraphicsContext gc, double x, double y, Color c) {
        gc.setFill(c); gc.fillOval(x - 5, y - 5, 10, 10);
    }

    private Color getColor(double val) { return Color.hsb((1.0 - val) * 240, 0.8, 0.9); }

    @Override public String getDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictreeviewer.desc", "Interactive evolutionary tree browser showing taxonomic relationships."); }
    @Override public String getLongDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictreeviewer.longdesc", "Visualize and explore the 'Tree of Life' with support for linear and radial layouts. Features interactive node selection, genetic marker alignment (COI, 16S, CytB), and NCBI taxonomy integration."); }
    @Override public java.util.List<Parameter<?>> getViewerParameters() { return new java.util.ArrayList<>(); }
}
