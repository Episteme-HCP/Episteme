/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
import org.jscience.biology.Taxon;
import org.jscience.biology.io.PhylogeneticTreeReader;

import java.util.ArrayList;
import java.util.List;

/**
 * Phylogenetic Tree Viewer.
 * Visualizes evolutionary relationships with simulated genetic marker data.
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
    public String getName() { return I18n.getInstance().get("viewer.phylogeny"); }
    
    @Override
    public String getCategory() { return "Biology"; }

    private void initUI() {
        this.getStyleClass().add("dark-viewer-root");

        canvas = new Canvas(900, 700);
        this.setCenter(canvas);
        
        this.widthProperty().addListener(e -> { canvas.setWidth(getWidth()); updateLayoutAndDraw(canvas); });
        this.heightProperty().addListener(e -> { canvas.setHeight(getHeight()); updateLayoutAndDraw(canvas); });

        Label infoPanel = new Label(I18n.getInstance().get("phylogeny.info.default"));
        infoPanel.getStyleClass().add("dark-viewer-sidebar");

        javafx.scene.control.Button toggleBtn = new javafx.scene.control.Button(I18n.getInstance().get("phylogeny.toggle_view"));
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
                String txt = I18n.getInstance().get("phylogeny.info.selected", clicked.getName(), clicked.getCoi(), clicked.getRna16s(), clicked.getCytb());
                infoPanel.setText(txt);
                drawTree(canvas.getGraphicsContext2D(), treeRoot);
            } else {
                selectedNode = null;
                infoPanel.setText(I18n.getInstance().get("phylogeny.info.default"));
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
        
        Label ncbiLabel = new Label(I18n.getInstance().get("phylogeny.ncbi.title", "NCBI Query"));
        ncbiLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 11px;");
        
        javafx.scene.control.TextField searchField = new javafx.scene.control.TextField();
        searchField.setPromptText(I18n.getInstance().get("phylogeny.ncbi.prompt", "Enter species name..."));
        searchField.setPrefWidth(180);
        
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 10px;");
        statusLabel.setWrapText(true);
        statusLabel.setPrefWidth(180);
        
        javafx.scene.control.Button queryButton = new javafx.scene.control.Button(I18n.getInstance().get("phylogeny.ncbi.search", "Search"));
        queryButton.setStyle("-fx-font-size: 10px;");
        queryButton.setOnAction(e -> {
            String searchTerm = searchField.getText().trim();
            if (searchTerm.isEmpty()) {
                statusLabel.setText(I18n.getInstance().get("phylogeny.ncbi.empty", "Enter a species name"));
                return;
            }
            
            statusLabel.setText(I18n.getInstance().get("phylogeny.ncbi.querying", "Querying NCBI..."));
            queryButton.setDisable(true);
            
            // Query in background thread
            new Thread(() -> {
                try {
                    org.jscience.biology.loaders.NCBITaxonomyReader reader = 
                        new org.jscience.biology.loaders.NCBITaxonomyReader();
                    java.util.List<Long> taxIds = reader.searchByName(searchTerm);
                    
                    javafx.application.Platform.runLater(() -> {
                        if (taxIds != null && !taxIds.isEmpty()) {
                            statusLabel.setText(I18n.getInstance().get("phylogeny.ncbi.found", 
                                "Found " + taxIds.size() + " results!"));
                            // Here we could load the first result and visualize it
                            // For now, just show feedback
                        } else {
                            statusLabel.setText(I18n.getInstance().get("phylogeny.ncbi.notfound", 
                                "No results found"));
                        }
                        queryButton.setDisable(false);
                    });
                } catch (Exception ex) {
                    javafx.application.Platform.runLater(() -> {
                        statusLabel.setText("Error: " + ex.getMessage());
                        queryButton.setDisable(false);
                    });
                }
            }).start();
        });
        
        section.getChildren().addAll(ncbiLabel, searchField, queryButton, statusLabel);
        return section;
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
                gc.fillText(I18n.getInstance().get("phylogeny.marker.coi"), 760, 20);
                gc.fillText(I18n.getInstance().get("phylogeny.marker.rna16s"), 790, 20);
                gc.fillText(I18n.getInstance().get("phylogeny.marker.cytb"), 820, 20);
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
        drawCell(gc, startX, y - 9, getColor(n.getCoi()));
        drawCell(gc, startX + 30, y - 9, getColor(n.getRna16s()));
        drawCell(gc, startX + 60, y - 9, getColor(n.getCytb()));
    }

    private void drawRadialHeatmap(GraphicsContext gc, Taxon n, double startRadius, double angle) {
        double cx = canvas.getWidth()/2, cy = canvas.getHeight()/2;
        drawDot(gc, cx + (startRadius + 10) * Math.cos(angle), cy + (startRadius + 10) * Math.sin(angle), getColor(n.getCoi()));
        drawDot(gc, cx + (startRadius + 25) * Math.cos(angle), cy + (startRadius + 25) * Math.sin(angle), getColor(n.getRna16s()));
        drawDot(gc, cx + (startRadius + 40) * Math.cos(angle), cy + (startRadius + 40) * Math.sin(angle), getColor(n.getCytb()));
    }

    private void drawCell(GraphicsContext gc, double x, double y, Color c) {
        gc.setFill(c); gc.fillRect(x, y, 20, 18); gc.setStroke(Color.BLACK); gc.strokeRect(x, y, 20, 18);
    }

    private void drawDot(GraphicsContext gc, double x, double y, Color c) {
        gc.setFill(c); gc.fillOval(x - 5, y - 5, 10, 10);
    }

    private Color getColor(double val) { return Color.hsb((1.0 - val) * 240, 0.8, 0.9); }

    @Override public String getDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictree.desc"); }
    @Override public String getLongDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.phylogenetictree.longdesc"); }
    @Override public java.util.List<org.jscience.ui.Parameter<?>> getViewerParameters() { return new java.util.ArrayList<>(); }
}
