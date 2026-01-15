
import os

BASE_JAVA = r"c:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java\org\jscience"
BASE_RES = r"c:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\resources\org\jscience"

# 1. Taxon.java
taxon_code = r'''package org.jscience.biology;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a taxonomic group in a phylogenetic tree.
 */
public class Taxon {
    private String id;
    private String parentId;
    private String name;
    private List<Taxon> children = new ArrayList<>();
    
    // Genetic Markers (Simulated)
    private double coi;
    private double rna16s;
    private double cytb;
    
    // Layout properties (transient)
    public transient double x, y, angle, radius;

    public Taxon(String id, String parentId, String name, double coi, double rna16s, double cytb) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.coi = coi;
        this.rna16s = rna16s;
        this.cytb = cytb;
    }

    public void addChild(Taxon t) {
        children.add(t);
    }
    
    public List<Taxon> getChildren() { return children; }
    public String getName() { return name; }
    public String getId() { return id; }
    public String getParentId() { return parentId; }
    public double getCoi() { return coi; }
    public double getRna16s() { return rna16s; }
    public double getCytb() { return cytb; }
}
'''
p_taxon = os.path.join(BASE_JAVA, "biology", "Taxon.java")
os.makedirs(os.path.dirname(p_taxon), exist_ok=True)
with open(p_taxon, 'w', encoding='utf-8') as f: f.write(taxon_code)

# 2. PhylogeneticTreeReader.java
reader_code = r'''package org.jscience.biology.io;

import org.jscience.biology.Taxon;
import org.jscience.io.ResourceReader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhylogeneticTreeReader implements ResourceReader<Taxon> {

    @Override
    public Taxon read(InputStream input) throws Exception {
        Map<String, Taxon> taxons = new HashMap<>();
        Taxon root = null;
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"))) {
            String line;
            boolean header = true;
            while ((line = reader.readLine()) != null) {
                if (header) { header = false; continue; }
                String[] p = line.split(",");
                if (p.length < 6) continue;
                
                String id = p[0].trim();
                String pid = p[1].trim();
                String name = p[2].trim();
                double coi = Double.parseDouble(p[3]);
                double rna = Double.parseDouble(p[4]);
                double cytb = Double.parseDouble(p[5]);
                
                Taxon t = new Taxon(id, pid, name, coi, rna, cytb);
                taxons.put(id, t);
                
                if (pid.isEmpty()) {
                    root = t;
                }
            }
        }
        
        // Build Hierarchy
        for (Taxon t : taxons.values()) {
            if (t.getParentId() != null && !t.getParentId().isEmpty()) {
                Taxon parent = taxons.get(t.getParentId());
                if (parent != null) {
                    parent.addChild(t);
                }
            }
        }
        
        return root;
    }
    
    @Override
    public String getName() {
        return "Phylogenetic CSV Reader";
    }

    @Override
    public List<String> getSupportedExtensions() {
        return List.of("csv");
    }
}
'''
p_reader = os.path.join(BASE_JAVA, "biology", "io", "PhylogeneticTreeReader.java")
os.makedirs(os.path.dirname(p_reader), exist_ok=True)
with open(p_reader, 'w', encoding='utf-8') as f: f.write(reader_code)

# 3. primates.csv
csv_data = """Id,Parent,Name,COI,RNA16S,CYTB
1,,Primates (Order),0,0,0
2,1,Strepsirrhini,0,0,0
3,1,Haplorhini,0,0,0
4,2,Lemuriformes,0,0,0
5,2,Lorisiformes,0,0,0
6,4,Lemur catta,0.72,0.65,0.45
7,4,Microcebus murinus,0.70,0.63,0.48
8,5,Nycticebus coucang,0.68,0.60,0.50
9,3,Tarsiiformes,0,0,0
10,3,Simiiformes,0,0,0
11,9,Tarsius syrichta,0.75,0.68,0.55
12,10,Platyrrhini (New World),0,0,0
13,10,Catarrhini (Old World),0,0,0
14,12,Saimiri sciureus,0.82,0.75,0.65
15,12,Alouatta palliata,0.80,0.73,0.62
16,13,Cercopithecoidea,0,0,0
17,13,Hominoidea (Apes),0,0,0
18,16,Macaca mulatta,0.88,0.80,0.75
19,16,Papio anubis,0.87,0.79,0.74
20,17,Hylobatidae,0,0,0
21,17,Hominidae,0,0,0
22,20,Hylobates lar,0.90,0.85,0.80
23,21,Ponginae,0,0,0
24,21,Homininae,0,0,0
25,23,Pongo abelii,0.92,0.88,0.82
26,24,Gorilla gorilla,0.95,0.92,0.88
27,24,Pan troglodytes,0.98,0.96,0.94
28,24,Homo sapiens,0.99,0.98,0.97"""

p_csv = os.path.join(BASE_RES, "biology", "data", "primates.csv")
os.makedirs(os.path.dirname(p_csv), exist_ok=True)
with open(p_csv, 'w', encoding='utf-8') as f: f.write(csv_data)

# 4. Update Viewer
viewer_code = r'''/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.viewers.biology.phylogeny;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
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
'''
p_viewer = os.path.join(BASE_JAVA, "ui", "viewers", "biology", "phylogeny", "PhylogeneticTreeViewer.java")
with open(p_viewer, 'w', encoding='utf-8') as f: f.write(viewer_code)
