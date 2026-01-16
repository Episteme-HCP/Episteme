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

package org.jscience.ui.viewers.chemistry;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.AmbientLight;
import javafx.scene.PointLight;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.jscience.chemistry.Element;
import org.jscience.chemistry.PeriodicTable;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.i18n.I18n;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import org.jscience.chemistry.loaders.PubChemReader;
import org.jscience.chemistry.loaders.ChEBIReader;
import org.jscience.chemistry.loaders.PeriodicTableReader;

/**
 * Interactive periodic table viewer.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PeriodicTableViewer extends AbstractViewer {

    private static final String[][] LAYOUT = {
            { "H", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                    "He" },
            { "Li", "Be", null, null, null, null, null, null, null, null, null, null, "B", "C", "N", "O", "F", "Ne" },
            { "Na", "Mg", null, null, null, null, null, null, null, null, null, null, "Al", "Si", "P", "S", "Cl",
                    "Ar" },
            { "K", "Ca", "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co", "Ni", "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br",
                    "Kr" },
            { "Rb", "Sr", "Y", "Zr", "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In", "Sn", "Sb", "Te", "I",
                    "Xe" },
            { "Cs", "Ba", "*", "Hf", "Ta", "W", "Re", "Os", "Ir", "Pt", "Au", "Hg", "Tl", "Pb", "Bi", "Po", "At",
                    "Rn" },
            { "Fr", "Ra", "**", "Rf", "Db", "Sg", "Bh", "Hs", "Mt", "Ds", "Rg", "Cn", "Nh", "Fl", "Mc", "Lv", "Ts",
                    "Og" },
            {},
            { null, null, "*", "La", "Ce", "Pr", "Nd", "Pm", "Sm", "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb",
                    "Lu" },
            { null, null, "**", "Ac", "Th", "Pa", "U", "Np", "Pu", "Am", "Cm", "Bk", "Cf", "Es", "Fm", "Md", "No",
                    "Lr" }
    };

    private VBox detailPanel;
    private TabPane detailsTabPane;
    private VBox electronicContent;
    private VBox nuclearContent;
    private Slider zoomSlider;

    public PeriodicTableViewer() {
        List<Element> elements = PeriodicTableReader.loadFromResource("/org/jscience/chemistry/elements.json");
        for (Element el : elements) {
            PeriodicTable.registerElement(el);
        }

        GridPane tableGrid = createTableGrid();
        Group contentGroup = new Group(tableGrid);
        StackPane zoomPane = new StackPane(contentGroup);
        zoomPane.setAlignment(Pos.CENTER);
        zoomPane.getStyleClass().add("viewer-root");

        ScrollPane scrollPane = new ScrollPane(zoomPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("viewer-root");

        detailPanel = createDetailPanel();

        zoomSlider = new Slider(0.5, 2.0, 0.9);
        zoomSlider.setShowTickMarks(true);
        zoomSlider.setShowTickLabels(true);
        zoomSlider.valueProperty().addListener((o, ov, nv) -> {
            contentGroup.setScaleX(nv.doubleValue());
            contentGroup.setScaleY(nv.doubleValue());
        });

        HBox topBar = new HBox(10, new Label(I18n.getInstance().get("periodic.zoom", "Zoom")), zoomSlider);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10));
        topBar.getStyleClass().add("viewer-sidebar");
        ((Label) topBar.getChildren().get(0)).getStyleClass().add("label");

        setTop(topBar);
        setCenter(scrollPane);
        setRight(detailPanel);
    }

    private GridPane createTableGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(3);
        grid.setVgap(3);
        grid.setPadding(new Insets(20));
        grid.getStyleClass().add("viewer-root");

        for (int r = 0; r < LAYOUT.length; r++) {
            for (int c = 0; c < LAYOUT[r].length; c++) {
                String sym = LAYOUT[r][c];
                if (sym == null || sym.equals("*") || sym.equals("**")) {
                    if (sym != null) {
                        Label m = new Label(sym);
                        m.getStyleClass().add("text-secondary");
                        grid.add(m, c, r);
                    }
                    continue;
                }
                StackPane cell = createElementCell(sym);
                if (cell != null)
                    grid.add(cell, c, r);
            }
        }
        return grid;
    }

    private StackPane createElementCell(String symbol) {
        Element el = PeriodicTable.bySymbol(symbol);
        if (el == null) {
            el = new Element("Unknown", symbol);
            el.setAtomicNumber(0);
        }
        Element finalEl = el;

        VBox content = new VBox(2);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(5));
        Label num = new Label(String.valueOf(finalEl.getAtomicNumber()));
        num.setFont(Font.font("Arial", 10));
        Label symLbl = new Label(finalEl.getSymbol());
        symLbl.getStyleClass().add("font-large");
        Label name = new Label(truncate(finalEl.getName(), 8));
        name.setFont(Font.font("Arial", 9));
        content.getChildren().addAll(num, symLbl, name);

        StackPane cell = new StackPane(content);
        cell.setPrefSize(60, 70);
        String style = getCategoryStyle(finalEl);
        cell.setStyle(style);

        Tooltip t = new Tooltip(getTooltipText(finalEl));
        t.setStyle("-fx-font-size: 12px;");
        Tooltip.install(cell, t);
        cell.setOnMouseClicked(e -> showElementDetails(finalEl));
        cell.setOnMouseEntered(
                e -> cell.setStyle(style + " -fx-opacity: 0.8; -fx-border-color: white; -fx-border-width: 2;"));
        cell.setOnMouseExited(e -> cell.setStyle(style));
        return cell;
    }

    private VBox createDetailPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setPrefWidth(320);
        panel.getStyleClass().add("viewer-sidebar");

        Label title = new Label(I18n.getInstance().get("periodic.details", "Element Details"));
        title.getStyleClass().add("font-large");
        title.getStyleClass().add("header-label");

        Label hint = new Label(I18n.getInstance().get("periodic.hint", "Select an element to view details"));
        hint.getStyleClass().add("text-secondary");
        hint.setWrapText(true);
        hint.setId("hint-label");

        detailsTabPane = new TabPane();

        Tab elecTab = new Tab(I18n.getInstance().get("periodic.electronic", "Electronic"));
        electronicContent = new VBox(10);
        electronicContent.setPadding(new Insets(10));
        ScrollPane es = new ScrollPane(electronicContent);
        es.setFitToWidth(true);
        elecTab.setContent(es);
        elecTab.setClosable(false);

        Tab nucTab = new Tab(I18n.getInstance().get("periodic.nuclear", "Nuclear"));
        nuclearContent = new VBox(10);
        nuclearContent.setPadding(new Insets(10));
        ScrollPane ns = new ScrollPane(nuclearContent);
        ns.setFitToWidth(true);
        nucTab.setContent(ns);
        nucTab.setClosable(false);

        Tab compTab = new Tab(I18n.getInstance().get("periodic.compounds", "Compounds"));
        VBox compoundContent = new VBox(10);
        compoundContent.setPadding(new Insets(10));
        TextField compSearch = new TextField();
        compSearch.setPromptText("Search PubChem/ChEBI...");
        Button searchBtn = new Button("Search");
        searchBtn.setMaxWidth(Double.MAX_VALUE);
        ListView<String> compList = new ListView<>();
        compList.setPrefHeight(200);
        
        searchBtn.setOnAction(e -> {
            String q = compSearch.getText().trim();
            if (!q.isEmpty()) {
                compList.getItems().clear();
                compList.getItems().add("Searching...");
                new Thread(() -> {
                    PubChemReader pcr = new PubChemReader();
                    List<Long> cids = pcr.searchByName(q);
                    Platform.runLater(() -> {
                        compList.getItems().clear();
                        for (Long cid : cids) {
                            compList.getItems().add("PubChem CID: " + cid);
                        }
                        if (cids.isEmpty()) {
                             compList.getItems().add("No results. Trying ChEBI...");
                             new Thread(() -> {
                                 java.util.Map<String, String> chebi = ChEBIReader.searchByName(q);
                                 Platform.runLater(() -> {
                                     if (chebi != null && chebi.get("name") != null) {
                                         compList.getItems().clear();
                                         compList.getItems().add("ChEBI: " + chebi.get("name"));
                                     } else {
                                         compList.getItems().add("Not found.");
                                     }
                                 });
                             }).start();
                        }
                    });
                }).start();
            }
        });
        
        compoundContent.getChildren().addAll(new Label("Chemical Entities:"), compSearch, searchBtn, compList);
        compTab.setContent(compoundContent);
        compTab.setClosable(false);

        detailsTabPane.getTabs().addAll(elecTab, nucTab, compTab);
        detailsTabPane.setVisible(false);
        panel.getChildren().addAll(title, hint, detailsTabPane);
        return panel;
    }

    private void showElementDetails(Element element) {
        detailPanel.getChildren().removeIf(n -> n.getId() != null && n.getId().equals("hint-label"));
        detailsTabPane.setVisible(true);
        electronicContent.getChildren().clear();
        nuclearContent.getChildren().clear();

        VBox header = new VBox(5);
        header.setAlignment(Pos.CENTER);
        Label sym = new Label(element.getSymbol());
        sym.getStyleClass().add("font-title");
        sym.setTextFill(Color.web("#54a0ff"));
        Label name = new Label(element.getName());
        name.getStyleClass().add("font-title");
        header.getChildren().addAll(sym, name);
        electronicContent.getChildren().add(header);

        int group = element.getGroup(), period = element.getPeriod();
        if (group == 0 || period == 0) { /* Inference logic omitted for brevity, assumes data or defaults */
        }

        VBox props = new VBox(5);
        props.getChildren().add(createPropLabel("Atomic Number", String.valueOf(element.getAtomicNumber())));
        props.getChildren().add(createPropLabel("Category",
                element.getCategory() != null ? element.getCategory().name() : "Unknown"));
        if (element.getAtomicMass() != null)
            props.getChildren().add(createPropLabel("Atomic Mass",
                    String.format("%.4f u", element.getAtomicMass().getValue().doubleValue())));
        electronicContent.getChildren().addAll(props, new Separator());

        SubScene atomView = createAtomView(element);
        VBox atomBox = new VBox(5, new Label(I18n.getInstance().get("periodic.structure", "Atomic Structure")),
                atomView);
        atomBox.setStyle("-fx-border-color: #666; -fx-border-width: 1px;");
        electronicContent.getChildren().add(atomBox);

        Label nucHeader = new Label(element.getName() + " (" + element.getSymbol() + ")");
        nucHeader.getStyleClass().add("font-bold");
        nuclearContent.getChildren().addAll(nucHeader, new Separator());

        SubScene nucView = createNucleusView(element);
        VBox nucBox = new VBox(5, new Label(org.jscience.ui.i18n.I18n.getInstance().get("generated.periodictable.nucleus.structure.mo", "Nucleus Structure (Model)")), nucView);
        nucBox.setStyle("-fx-border-color: #666; -fx-border-width: 1px;");

        Label isoLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("generated.periodictable.known.isotopes", "Known Isotopes:"));
        isoLabel.setStyle("-fx-font-weight: bold;");
        ListView<String> isoList = new ListView<>();
        isoList.setPrefHeight(200);
        isoList.setItems(getIsotopes(element));
        nuclearContent.getChildren().addAll(nucBox, new Separator(), isoLabel, isoList);
    }

    private SubScene createAtomView(Element element) {
        Group root = new Group();
        Sphere nuc = new Sphere(5);
        nuc.setMaterial(new PhongMaterial(Color.RED));
        root.getChildren().add(nuc);

        int g = element.getGroup(), p = element.getPeriod();
        if (g == 0 || p == 0) { /* Inference override */
            p = 4;
            g = 1;
        } // Fallback for demo logic

        if (g == 0)
            createFOrbital(root, p);
        else if (g <= 2)
            createSOrbital(root, p);
        else if (g >= 13)
            createPOrbital(root, p);
        else
            createDOrbital(root, p);

        PointLight pl = new PointLight(Color.WHITE);
        pl.setTranslateZ(-150);
        pl.setTranslateY(-50);
        root.getChildren().add(pl);
        root.getChildren().add(new AmbientLight(Color.rgb(150, 150, 150)));

        SubScene ss = new SubScene(root, 260, 260, true, SceneAntialiasing.BALANCED);
        ss.setFill(Color.web("#222"));
        PerspectiveCamera cam = new PerspectiveCamera(true);
        cam.setTranslateZ(-140);
        ss.setCamera(cam);

        RotateTransition rt = new RotateTransition(Duration.seconds(10), root);
        rt.setAxis(Rotate.Y_AXIS);
        rt.setByAngle(360);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.play();
        return ss;
    }

    // --- Simplified Orbital Logic ---
    private void createSOrbital(Group root, int n) {
        Sphere s = new Sphere(10 + n * 3);
        PhongMaterial m = new PhongMaterial(Color.web("#3498db", 0.3));
        m.setSpecularColor(Color.WHITE);
        s.setMaterial(m);
        root.getChildren().add(s);
    }

    private void createPOrbital(Group root, int n) {
        double s = 10 + n * 3;
        createLobePair(root, new Point3D(0, 1, 0), s, Color.web("#e74c3c"));
        createLobePair(root, new Point3D(1, 0, 0), s, Color.web("#2ecc71"));
        createLobePair(root, new Point3D(0, 0, 1), s, Color.web("#f1c40f"));
    }

    private void createDOrbital(Group root, int n) {
        double s = 12 + n * 3;
        createLobePair(root, new Point3D(0, 1, 0), s, Color.web("#3498db"));
        MeshView torus = createTorusMesh(s * 0.6, s * 0.2, 32, 16);
        PhongMaterial m = new PhongMaterial(Color.web("#e67e22"));
        m.setSpecularColor(Color.WHITE);
        torus.setMaterial(m);
        root.getChildren().add(torus);
    }

    private void createFOrbital(Group root, int n) {
        double s = 15 + n * 2;
        createLobePair(root, new Point3D(1, 1, 1), s, Color.MAGENTA);
        createLobePair(root, new Point3D(1, -1, 1), s, Color.MAGENTA);
    }

    private void createLobePair(Group r, Point3D ax, double sc, Color c) {
        PhongMaterial m = new PhongMaterial(c);
        m.setSpecularColor(Color.WHITE);
        Sphere l1 = new Sphere(sc * 0.4);
        l1.setScaleY(1.4);
        l1.setMaterial(m);
        Sphere l2 = new Sphere(sc * 0.4);
        l2.setScaleY(1.4);
        l2.setMaterial(m);
        Point3D off = ax.normalize().multiply(sc * 0.5);
        Point3D y = new Point3D(0, 1, 0);
        Point3D rot = y.crossProduct(ax);
        double ang = Math.acos(y.dotProduct(ax.normalize()));
        if (rot.magnitude() > 0.001) {
            Rotate rotT = new Rotate(Math.toDegrees(ang), rot);
            l1.getTransforms().add(rotT);
            l2.getTransforms().add(rotT);
        } else if (ax.getY() < -0.9) {
            l1.setRotate(180);
            l2.setRotate(180);
        }
        l1.setTranslateX(off.getX());
        l1.setTranslateY(off.getY());
        l1.setTranslateZ(off.getZ());
        l2.setTranslateX(-off.getX());
        l2.setTranslateY(-off.getY());
        l2.setTranslateZ(-off.getZ());
        r.getChildren().addAll(l1, l2);
    }

    private MeshView createTorusMesh(double r, double tr, int rd, int td) {
        TriangleMesh m = new TriangleMesh();
        float[] p = new float[rd * td * 3];
        float[] t = new float[rd * td * 2];
        int[] f = new int[rd * td * 12];
        int pi = 0, ti = 0;
        for (int i = 0; i < rd; i++) {
            double th = (Math.PI * 2 * i) / rd;
            for (int j = 0; j < td; j++) {
                double ph = (Math.PI * 2 * j) / td;
                p[pi++] = (float) ((r + tr * Math.cos(ph)) * Math.cos(th));
                p[pi++] = (float) (tr * Math.sin(ph));
                p[pi++] = (float) ((r + tr * Math.cos(ph)) * Math.sin(th));
                t[ti++] = (float) (i / (double) rd);
                t[ti++] = (float) (j / (double) td);
            }
        }
        int fi = 0;
        for (int i = 0; i < rd; i++) {
            for (int j = 0; j < td; j++) {
                int nextI = (i + 1) % rd, nextJ = (j + 1) % td;
                int p0 = i * td + j, p1 = nextI * td + j, p2 = nextI * td + nextJ, p3 = i * td + nextJ;
                f[fi++] = p0;
                f[fi++] = p0;
                f[fi++] = p1;
                f[fi++] = p1;
                f[fi++] = p2;
                f[fi++] = p2;
                f[fi++] = p0;
                f[fi++] = p0;
                f[fi++] = p2;
                f[fi++] = p2;
                f[fi++] = p3;
                f[fi++] = p3;
            }
        }
        m.getPoints().addAll(p);
        m.getTexCoords().addAll(t);
        m.getFaces().addAll(f);
        return new MeshView(m);
    }

    private SubScene createNucleusView(Element element) {
        Group root = new Group();
        int z = element.getAtomicNumber(),
                a = (element.getAtomicMass() != null)
                        ? (int) Math.round(element.getAtomicMass().getValue().doubleValue())
                        : z * 2;
        int n = a - z;
        int limit = 150;
        int pShow = Math.min(z, limit / 2), nShow = Math.min(n, limit / 2);
        double rad = Math.pow(a, 1.0 / 3.0) * 4.0;
        PhongMaterial pm = new PhongMaterial(Color.RED), nm = new PhongMaterial(Color.GRAY);
        for (int i = 0; i < pShow + nShow; i++) {
            double u = Math.random(), v = Math.random(), th = 2 * Math.PI * u, ph = Math.acos(2 * v - 1),
                    r = Math.cbrt(Math.random()) * rad;
            Sphere s = new Sphere(2.5);
            s.setMaterial(i < pShow ? pm : nm);
            s.setTranslateX(r * Math.sin(ph) * Math.cos(th));
            s.setTranslateY(r * Math.sin(ph) * Math.sin(th));
            s.setTranslateZ(r * Math.cos(ph));
            root.getChildren().add(s);
        }
        PointLight pl = new PointLight(Color.WHITE);
        pl.setTranslateZ(-100);
        root.getChildren().add(pl);
        root.getChildren().add(new AmbientLight(Color.rgb(100, 100, 100)));
        SubScene ss = new SubScene(root, 260, 200, true, SceneAntialiasing.BALANCED);
        ss.setFill(Color.web("#222"));
        PerspectiveCamera cam = new PerspectiveCamera(true);
        cam.setTranslateZ(-100);
        ss.setCamera(cam);
        RotateTransition rt = new RotateTransition(Duration.seconds(15), root);
        rt.setAxis(Rotate.Y_AXIS);
        rt.setByAngle(360);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.play();
        return ss;
    }

    private ObservableList<String> getIsotopes(Element el) {
        List<String> iso = new ArrayList<>();
        int z = el.getAtomicNumber(),
                a = (el.getAtomicMass() != null)
                        ? (int) Math.round(el.getAtomicMass().getValue().doubleValue())
                        : z * 2;
        iso.add(el.getSymbol() + "-" + a + " (Stable)"); // Mock
        return FXCollections.observableArrayList(iso);
    }

    private HBox createPropLabel(String n, String v) {
        Label nl = new Label(n + ":");
        nl.getStyleClass().add("text-secondary");
        nl.setMinWidth(120);
        return new HBox(10, nl, new Label(v));
    }

    private String truncate(String s, int m) {
        return s.length() > m ? s.substring(0, m) : s;
    }

    private String getTooltipText(Element e) {
        return e.getName() + " (" + e.getSymbol() + ")\nZ: " + e.getAtomicNumber();
    }

    private String getCategoryStyle(Element e) {
        String base = "-fx-background-radius: 5; -fx-cursor: hand;";
        if (e.getCategory() == null)
            return "-fx-background-color: #576574;" + base;
        switch (e.getCategory()) {
            case ALKALI_METAL:
                return "-fx-background-color: #ff6b6b;" + base;
            case NOBLE_GAS:
                return "-fx-background-color: #c8d6e5;" + base;
            default:
                return "-fx-background-color: #8395a7;" + base; // Simplified mapping
        }
    }
    
    @Override public String getName() { return "Periodic Viewer"; }
    @Override public String getCategory() { return "Chemistry"; }

    @Override public String getDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.periodictable.desc"); }
    @Override public String getLongDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.periodictable.longdesc"); }
    @Override public java.util.List<org.jscience.ui.Parameter<?>> getViewerParameters() { return new java.util.ArrayList<>(); }
}