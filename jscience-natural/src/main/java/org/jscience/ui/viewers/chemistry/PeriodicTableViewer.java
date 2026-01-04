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

package org.jscience.ui.viewers.chemistry;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.MeshView;

import java.util.List;
import java.util.ArrayList;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import org.jscience.ui.i18n.I18n;
import javafx.animation.RotateTransition;
import javafx.stage.Stage;

import org.jscience.chemistry.Element;
import org.jscience.chemistry.PeriodicTable;
import org.jscience.chemistry.loaders.ChemistryDataLoader;

/**
 * Interactive periodic table viewer using JavaFX.
 * <p>
 * Displays all elements in standard periodic table layout with:
 * <ul>
 * <li>Color-coded element categories</li>
 * <li>Hover tooltips with element details</li>
 * <li>Click for detailed element information</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PeriodicTableViewer extends Application {

    // Element positions in periodic table [row, col] (1-indexed)
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

    private javafx.scene.control.Slider zoomSlider;

    @Override
    public void start(Stage primaryStage) {
        // Load element data
        ChemistryDataLoader.loadElements();

        BorderPane root = new BorderPane();
        root.getStyleClass().add("dark-viewer-root");

        // Main table grid
        GridPane tableGrid = createTableGrid();

        // Wrap grid in a Group for scaling
        Group contentGroup = new Group(tableGrid);
        StackPane zoomPane = new StackPane(contentGroup);
        zoomPane.setAlignment(Pos.CENTER);
        zoomPane.getStyleClass().add("dark-viewer-root");

        ScrollPane scrollPane = new ScrollPane(zoomPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: #1a1a2e; -fx-background-color: #1a1a2e;");

        // Detail panel
        detailPanel = createDetailPanel();

        // Zoom Control
        zoomSlider = new javafx.scene.control.Slider(0.5, 2.0, 1.0);
        zoomSlider.setShowTickMarks(true);
        zoomSlider.setShowTickLabels(true);
        zoomSlider.valueProperty().addListener((o, oldVal, newVal) -> {
            contentGroup.setScaleX(newVal.doubleValue());
            contentGroup.setScaleY(newVal.doubleValue());
        });

        HBox topBar = new HBox(10, new Label(I18n.getInstance().get("periodic.zoom")), zoomSlider);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10));
        topBar.getStyleClass().add("dark-viewer-sidebar");
        ((Label) topBar.getChildren().get(0)).setTextFill(Color.WHITE);

        // root.setTop(topBar); // Keeping top bar disabled as per previous setting, or
        // enable if zoom needed.
        root.setCenter(scrollPane);
        root.setRight(detailPanel);

        // Auto-scale to fit roughly
        zoomSlider.setValue(0.9);

        Scene scene = new Scene(root, 1200, 800);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        primaryStage.setTitle(I18n.getInstance().get("periodic.title"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createTableGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(3);
        grid.setVgap(3);
        grid.setPadding(new Insets(20));
        grid.getStyleClass().add("dark-viewer-root");

        for (int row = 0; row < LAYOUT.length; row++) {
            for (int col = 0; col < LAYOUT[row].length; col++) {
                String symbol = LAYOUT[row][col];
                if (symbol == null || symbol.equals("*") || symbol.equals("**")) {
                    if (symbol != null) {
                        Label marker = new Label(symbol);
                        marker.setTextFill(Color.GRAY);
                        grid.add(marker, col, row);
                    }
                    continue;
                }

                StackPane cell = createElementCell(symbol);
                if (cell != null) {
                    grid.add(cell, col, row);
                }
            }
        }

        return grid;
    }

    private StackPane createElementCell(String symbol) {
        Element element = PeriodicTable.bySymbol(symbol);

        // Even if null (shouldn't be for standard table) or sparse, we try to create a
        // cell
        if (element == null) {
            // Create a dummy element wrapper for display if real data missing
            element = new Element(I18n.getInstance().get("periodic.unknown", "Unknown"), symbol);
            element.setAtomicNumber(0);
        }

        final Element finalElement = element;

        VBox content = new VBox(2);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(5));

        Label atomicNum = new Label(String.valueOf(finalElement.getAtomicNumber()));
        atomicNum.setFont(Font.font("Arial", 10));

        Label symbolLbl = new Label(finalElement.getSymbol());
        symbolLbl.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Label name = new Label(truncate(finalElement.getName(), 8));
        name.setFont(Font.font("Arial", 9));

        content.getChildren().addAll(atomicNum, symbolLbl, name);

        StackPane cell = new StackPane(content);
        cell.setPrefSize(60, 70);
        cell.setStyle(getCategoryStyle(finalElement));

        Tooltip tooltip = new Tooltip(getTooltipText(finalElement));
        tooltip.setStyle("-fx-font-size: 12px;");
        Tooltip.install(cell, tooltip);

        cell.setOnMouseClicked(e -> showElementDetails(finalElement));

        // Hover
        String style = getCategoryStyle(finalElement);
        cell.setOnMouseEntered(
                e -> cell.setStyle(style + " -fx-opacity: 0.8; -fx-border-color: white; -fx-border-width: 2;"));
        cell.setOnMouseExited(e -> cell.setStyle(style));

        return cell;
    }

    private String getCategoryStyle(Element element) {
        String baseStyle = "-fx-background-radius: 5; -fx-cursor: hand;";
        Element.ElementCategory cat = element.getCategory();

        if (cat == null) {
            return "-fx-background-color: #576574;" + baseStyle; // Default Gray
        }

        // Explicit mapping to avoid cache issues
        switch (cat) {
            case ALKALI_METAL:
                return "-fx-background-color: #ff6b6b;" + baseStyle; // Red
            case ALKALINE_EARTH_METAL:
                return "-fx-background-color: #feca57;" + baseStyle; // Yellow-Orange
            case TRANSITION_METAL:
                return "-fx-background-color: #54a0ff;" + baseStyle; // Blue
            case POST_TRANSITION_METAL:
                return "-fx-background-color: #48dbfb;" + baseStyle; // Light Blue
            case METALLOID:
                return "-fx-background-color: #1dd1a1;" + baseStyle; // Green
            case NONMETAL:
                return "-fx-background-color: #00d2d3;" + baseStyle; // Teal
            case HALOGEN:
                return "-fx-background-color: #ff9ff3;" + baseStyle; // Pink
            case NOBLE_GAS:
                return "-fx-background-color: #c8d6e5;" + baseStyle; // Light Gray/Blue
            case LANTHANIDE:
                return "-fx-background-color: #ff9f43;" + baseStyle; // Orange
            case ACTINIDE:
                return "-fx-background-color: #ee5253;" + baseStyle; // Dark Red
            default:
                return "-fx-background-color: #8395a7;" + baseStyle;
        }
    }

    private String getTooltipText(Element element) {
        StringBuilder sb = new StringBuilder();
        sb.append(element.getName()).append(" (").append(element.getSymbol()).append(")\n");
        sb.append(I18n.getInstance().get("periodic.attr.atomic_number", "Atomic Number")).append(": ")
                .append(element.getAtomicNumber()).append("\n");
        if (element.getAtomicMass() != null) {
            // Display value directy (assuming it's in atomic mass units)
            sb.append(I18n.getInstance().get("periodic.attr.atomic_mass", "Atomic Mass")).append(": ")
                    .append(String.format("%.4f u", element.getAtomicMass().getValue().doubleValue()))
                    .append("\n");
        }
        return sb.toString();
    }

    private TabPane detailsTabPane;
    private VBox electronicContent;
    private VBox nuclearContent;

    private VBox createDetailPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setPrefWidth(320); // Slightly wider
        panel.getStyleClass().add("dark-viewer-sidebar");

        Label title = new Label(I18n.getInstance().get("periodic.details"));
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.getStyleClass().add("dark-header");

        Label hint = new Label(I18n.getInstance().get("periodic.hint"));
        hint.setTextFill(Color.LIGHTGRAY);
        hint.setWrapText(true);
        // ID for easy clearing if needed, though we'll just update tabs
        hint.setId("hint-label");

        detailsTabPane = new TabPane();

        Tab elecTab = new Tab(I18n.getInstance().get("periodic.electronic", "Electronic"));
        electronicContent = new VBox(10);
        electronicContent.setPadding(new Insets(10));
        ScrollPane elecScroll = new ScrollPane(electronicContent);
        elecScroll.setFitToWidth(true);
        elecTab.setContent(elecScroll);
        elecTab.setClosable(false);

        Tab nucTab = new Tab(I18n.getInstance().get("periodic.nuclear", "Nuclear"));
        nuclearContent = new VBox(10);
        nuclearContent.setPadding(new Insets(10));
        ScrollPane nucScroll = new ScrollPane(nuclearContent);
        nucScroll.setFitToWidth(true);
        nucTab.setContent(nucScroll);
        nucTab.setClosable(false);

        detailsTabPane.getTabs().addAll(elecTab, nucTab);
        detailsTabPane.setVisible(false); // Hide until selection

        panel.getChildren().addAll(title, hint, detailsTabPane);
        return panel;
    }

    private void showElementDetails(Element element) {
        // Show Tabs
        detailPanel.getChildren().removeIf(n -> n.getId() != null && n.getId().equals("hint-label"));
        detailsTabPane.setVisible(true);

        // --- Header (Common) ---
        // We could move this to the top of the sidebar, but let's put it in both tabs
        // or just top of Electronic?
        // Let's put common header in Electronic for now, or maybe update a fixed header
        // in the panel.
        // Actually, let's look at previous impl: it cleared detailPanel.
        // Now detailPanel has fixed structure. Let's make "Electronic" the main view.

        electronicContent.getChildren().clear();
        nuclearContent.getChildren().clear();

        // Common Header
        VBox headerBox = new VBox(5);
        headerBox.setAlignment(Pos.CENTER);
        Label symbol = new Label(element.getSymbol());
        symbol.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        symbol.setTextFill(Color.web("#54a0ff"));
        Label name = new Label(element.getName());
        name.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        headerBox.getChildren().addAll(symbol, name);

        // --- Electronic Tab Content ---
        electronicContent.getChildren().add(headerBox);

        // Properties
        int inferredGroup = element.getGroup();
        int inferredPeriod = element.getPeriod();
        if (inferredGroup == 0 || inferredPeriod == 0) {
            for (int r = 0; r < LAYOUT.length; r++) {
                for (int c = 0; c < LAYOUT[r].length; c++) {
                    if (element.getSymbol().equals(LAYOUT[r][c])) {
                        inferredPeriod = r + 1;
                        inferredGroup = c + 1;
                        if (r == 7)
                            inferredPeriod = 6;
                        if (r == 8)
                            inferredPeriod = 7;
                        break;
                    }
                }
            }
        }

        VBox props = new VBox(5);
        props.getChildren().add(createPropLabel(I18n.getInstance().get("periodic.attr.atomic_number", "Atomic Number"),
                String.valueOf(element.getAtomicNumber())));
        props.getChildren().add(
                createPropLabel(I18n.getInstance().get("periodic.attr.category", "Category"),
                        element.getCategory() != null ? element.getCategory().name()
                                : I18n.getInstance().get("periodic.unknown", "Unknown")));
        props.getChildren().add(
                createPropLabel(I18n.getInstance().get("periodic.attr.group", "Group"), String.valueOf(inferredGroup)));
        props.getChildren().add(createPropLabel(I18n.getInstance().get("periodic.attr.period", "Period"),
                String.valueOf(inferredPeriod)));
        if (element.getAtomicMass() != null) {
            props.getChildren().add(createPropLabel(I18n.getInstance().get("periodic.attr.atomic_mass", "Atomic Mass"),
                    String.format("%.4f u", element.getAtomicMass().getValue().doubleValue())));
        }
        if (element.getDensity() != null) {
            props.getChildren().add(createPropLabel(I18n.getInstance().get("periodic.attr.density", "Density"),
                    String.format("%.3f g/cm\u00B3", element.getDensity().getValue().doubleValue())));
        }

        electronicContent.getChildren().addAll(props, new Separator());

        // Atom Orbital View
        SubScene atomView = createAtomView(element);
        VBox atomBox = new VBox(5, new Label(I18n.getInstance().get("periodic.structure")), atomView);
        atomBox.setStyle("-fx-border-color: #666; -fx-border-width: 1px;");
        electronicContent.getChildren().add(atomBox);

        // --- Nuclear Tab Content ---
        // Header duplicate? Or maybe just compact info
        Label nucHeader = new Label(element.getName() + " (" + element.getSymbol() + ")");
        nucHeader.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        nuclearContent.getChildren().add(nucHeader);
        nuclearContent.getChildren().add(new Separator());

        // Nucleus Visualization (Bohr Model / Shells)
        SubScene nucleusView = createNucleusView(element);
        VBox nucBox = new VBox(5,
                new Label(I18n.getInstance().get("periodic.nucleus.title", "Nucleus Structure (Simplified)")),
                nucleusView);
        nucBox.setStyle("-fx-border-color: #666; -fx-border-width: 1px;");
        nuclearContent.getChildren().add(nucBox);

        // Isotopes List
        Label isoLabel = new Label(I18n.getInstance().get("periodic.isotopes.title", "Known Isotopes:"));
        isoLabel.setStyle("-fx-font-weight: bold;");
        ListView<String> isoList = new ListView<>();
        isoList.setPrefHeight(200);
        isoList.setItems(getIsotopes(element));

        nuclearContent.getChildren().addAll(new Separator(), isoLabel, isoList);
    }

    private SubScene createAtomView(Element element) {
        Group root = new Group();

        // 1. Nucleus
        javafx.scene.shape.Sphere nucleus = new javafx.scene.shape.Sphere(5);
        nucleus.setMaterial(new javafx.scene.paint.PhongMaterial(Color.RED));
        root.getChildren().add(nucleus);

        // 2. Determine orbital type based on element's position
        int group = element.getGroup();
        int period = element.getPeriod();

        // Infer group/period from layout if not set in element data
        if (group == 0 || period == 0) {
            for (int r = 0; r < LAYOUT.length; r++) {
                for (int c = 0; c < LAYOUT[r].length; c++) {
                    if (element.getSymbol().equals(LAYOUT[r][c])) {
                        period = r + 1;
                        group = c + 1;
                        // Lanthanides/Actinides
                        if (r == 8) {
                            period = 6;
                            group = 0;
                        } // Lanthanides (f-block)
                        if (r == 9) {
                            period = 7;
                            group = 0;
                        } // Actinides (f-block)
                        break;
                    }
                }
            }
        }

        // Select orbital type based on block (s, p, d, f)
        // s-block: groups 1-2 (alkali, alkaline earth)
        // p-block: groups 13-18
        // d-block: groups 3-12 (transition metals)
        // f-block: lanthanides/actinides (group 0 after inference)
        if (group == 0) {
            createFOrbital(root, period);
        } else if (group <= 2) {
            createSOrbital(root, period);
        } else if (group >= 13) {
            createPOrbital(root, period);
        } else {
            createDOrbital(root, period);
        }

        // Lighting - Brighter
        javafx.scene.PointLight light = new javafx.scene.PointLight(Color.WHITE);
        light.setTranslateZ(-150);
        light.setTranslateY(-50);
        root.getChildren().add(light);

        javafx.scene.AmbientLight ambient = new javafx.scene.AmbientLight(Color.rgb(150, 150, 150));
        root.getChildren().add(ambient);

        SubScene subScene = new SubScene(root, 260, 260, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.web("#222"));
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-140);
        subScene.setCamera(camera);

        // Auto-rotate
        RotateTransition rt = new RotateTransition(javafx.util.Duration.seconds(10), root);
        rt.setAxis(Rotate.Y_AXIS);
        rt.setByAngle(360);
        rt.setCycleCount(javafx.animation.Animation.INDEFINITE);
        rt.setInterpolator(javafx.animation.Interpolator.LINEAR);
        rt.play();

        return subScene;
    }

    // --- Orbital Generators ---

    private void createSOrbital(Group root, int principalN) {
        // Spherical shape
        // Size increases with Period
        Sphere s = new Sphere(10 + principalN * 3);
        PhongMaterial mat = new PhongMaterial(Color.web("#3498db", 0.3)); // Blue transparent
        mat.setSpecularColor(Color.WHITE);
        s.setMaterial(mat);
        root.getChildren().add(s);
    }

    private void createPOrbital(Group root, int principalN) {
        // 3 Dumbbells (Px, Py, Pz)
        double size = 10 + principalN * 3;

        // Pz (Vertical)
        createLobePair(root, new javafx.geometry.Point3D(0, 1, 0), size, Color.web("#e74c3c")); // Red
        // Px (Horizontal)
        createLobePair(root, new javafx.geometry.Point3D(1, 0, 0), size, Color.web("#2ecc71")); // Green
        // Py (Depth)
        createLobePair(root, new javafx.geometry.Point3D(0, 0, 1), size, Color.web("#f1c40f")); // Yellow
    }

    // The "Fancy" one requested by user (dz^2 style)
    private void createDOrbital(Group root, int principalN) {
        double size = 12 + principalN * 3;

        // 1. Two vertical lobes (z-axis)
        createLobePair(root, new javafx.geometry.Point3D(0, 1, 0), size, Color.web("#3498db")); // Blue

        // 2. Torus/Donut in the middle (xy plane)
        MeshView torus = createTorusMesh(size * 0.6, size * 0.2, 32, 16);
        PhongMaterial mat = new PhongMaterial(Color.web("#e67e22")); // Orange/Copper
        mat.setSpecularColor(Color.WHITE);
        torus.setMaterial(mat);
        root.getChildren().add(torus);
    }

    private void createFOrbital(Group root, int principalN) {
        // Complex 8-lobe structure approximation for f-orbitals
        double size = 15 + principalN * 2;
        // Create multiple lobe pairs in different orientations
        createLobePair(root, new javafx.geometry.Point3D(1, 1, 1), size, Color.MAGENTA);
        createLobePair(root, new javafx.geometry.Point3D(1, -1, 1), size, Color.MAGENTA);
        createLobePair(root, new javafx.geometry.Point3D(-1, 1, 1), size, Color.ORCHID);
        createLobePair(root, new javafx.geometry.Point3D(-1, -1, 1), size, Color.ORCHID);
    }

    private void createLobePair(Group root, javafx.geometry.Point3D axis, double scale, Color color) {
        PhongMaterial mat = new PhongMaterial(color);
        mat.setSpecularColor(Color.WHITE);
        // Semi-transparent if needed, but solid looks better for "glossy" look
        // requested
        // mat.setDiffuseColor(new Color(color.getRed(), color.getGreen(),
        // color.getBlue(), 0.7));

        // Top Lobe
        Sphere l1 = new Sphere(scale * 0.4);
        l1.setScaleY(1.4);
        l1.setMaterial(mat);

        // Bottom Lobe
        Sphere l2 = new Sphere(scale * 0.4);
        l2.setScaleY(1.4);
        l2.setMaterial(mat);

        // Position
        javafx.geometry.Point3D offset = axis.normalize().multiply(scale * 0.5);

        // Rotate logic
        javafx.geometry.Point3D yAxis = new javafx.geometry.Point3D(0, 1, 0);
        javafx.geometry.Point3D rotAxis = yAxis.crossProduct(axis);
        double angle = Math.acos(yAxis.dotProduct(axis.normalize()));

        if (rotAxis.magnitude() > 0.001) {
            Rotate r = new Rotate(Math.toDegrees(angle), rotAxis);
            l1.getTransforms().add(r);
            l2.getTransforms().add(r);
        } else if (axis.getY() < -0.9) { // Antiparellel
            l1.setRotate(180);
            l2.setRotate(180);
        }

        l1.setTranslateX(offset.getX());
        l1.setTranslateY(offset.getY());
        l1.setTranslateZ(offset.getZ());
        l2.setTranslateX(-offset.getX());
        l2.setTranslateY(-offset.getY());
        l2.setTranslateZ(-offset.getZ());

        root.getChildren().addAll(l1, l2);
    }

    // Procedural Torus Mesh
    private MeshView createTorusMesh(double radius, double tubeRadius, int rDivs, int tDivs) {
        javafx.scene.shape.TriangleMesh mesh = new javafx.scene.shape.TriangleMesh();

        float[] points = new float[rDivs * tDivs * 3];
        float[] texCoords = new float[rDivs * tDivs * 2];
        int[] faces = new int[rDivs * tDivs * 6 * 2]; // 2 triangles per quad, 3 verts per tri, 2 indices (p,t) per vert

        int pIndex = 0;
        int tIndex = 0;

        for (int i = 0; i < rDivs; i++) {
            double theta = (Math.PI * 2.0 * i) / rDivs;
            double cosTheta = Math.cos(theta);
            double sinTheta = Math.sin(theta);

            for (int j = 0; j < tDivs; j++) {
                double phi = (Math.PI * 2.0 * j) / tDivs;
                double cosPhi = Math.cos(phi);
                double sinPhi = Math.sin(phi);

                // Torus parametric eq
                double x = (radius + tubeRadius * cosPhi) * cosTheta;
                double z = (radius + tubeRadius * cosPhi) * sinTheta; // Z is other major axis in horizontal layout?
                // Wait, standard torus is in XY plane mainly? Let's make it flat on XZ plane (Y
                // up is normal)
                // Default calc usually makes a ring around Z axis.
                // Let's create ring around Y axis (horizontal donut)
                // x = (R + r cos phi) cos theta
                // z = (R + r cos phi) sin theta
                // y = r sin phi

                double y = tubeRadius * sinPhi;

                points[pIndex++] = (float) x;
                points[pIndex++] = (float) y;
                points[pIndex++] = (float) z;

                texCoords[tIndex++] = (float) (i / (double) rDivs);
                texCoords[tIndex++] = (float) (j / (double) tDivs);
            }
        }

        int fIndex = 0;
        for (int i = 0; i < rDivs; i++) {
            for (int j = 0; j < tDivs; j++) {
                int nextI = (i + 1) % rDivs;
                int nextJ = (j + 1) % tDivs;

                int p0 = i * tDivs + j;
                int p1 = nextI * tDivs + j;
                int p2 = nextI * tDivs + nextJ;
                int p3 = i * tDivs + nextJ;

                // Triangle 1 (0-1-2)
                faces[fIndex++] = p0;
                faces[fIndex++] = p0; // reuse p index as t index approx for now
                faces[fIndex++] = p1;
                faces[fIndex++] = p1;
                faces[fIndex++] = p2;
                faces[fIndex++] = p2;

                // Triangle 2 (0-2-3)
                faces[fIndex++] = p0;
                faces[fIndex++] = p0;
                faces[fIndex++] = p2;
                faces[fIndex++] = p2;
                faces[fIndex++] = p3;
                faces[fIndex++] = p3;
            }
        }

        mesh.getPoints().addAll(points);
        mesh.getTexCoords().addAll(texCoords);
        mesh.getFaces().addAll(faces);

        return new MeshView(mesh);
    }

    private HBox createPropLabel(String name, String value) {
        HBox row = new HBox(10);
        Label nameLbl = new Label(name + ":");
        nameLbl.setTextFill(Color.LIGHTGRAY);
        nameLbl.setMinWidth(120);
        Label valueLbl = new Label(value);

        row.getChildren().addAll(nameLbl, valueLbl);
        return row;
    }

    private SubScene createNucleusView(Element element) {
        Group root = new Group();

        // Nucleon Model: Simplified cluster of protons and neutrons
        int protons = element.getAtomicNumber();
        int massNumber = (element.getAtomicMass() != null)
                ? (int) Math.round(element.getAtomicMass().getValue().doubleValue())
                : protons * 2;
        int neutrons = massNumber - protons;

        // Use a packing algorithm or random sphere sampling within a radius
        // Simple approach: Fibonacci Sphere or just random inside sphere

        // Optimize for performance: Don't draw 200 spheres if heavy.
        // Limit vis for high Z
        int displayLimit = 150;
        int pToShow = Math.min(protons, displayLimit / 2);
        int nToShow = Math.min(neutrons, displayLimit / 2);

        double nucleusRadius = Math.pow(massNumber, 1.0 / 3.0) * 4.0;

        PhongMaterial pMat = new PhongMaterial(Color.RED);
        pMat.setSpecularColor(Color.WHITE);
        PhongMaterial nMat = new PhongMaterial(Color.GRAY);
        nMat.setSpecularColor(Color.WHITE);

        for (int i = 0; i < pToShow + nToShow; i++) {
            boolean isProton = i < pToShow;
            // Random pos inside unit sphere * radius
            double u = Math.random();
            double v = Math.random();
            double theta = 2 * Math.PI * u;
            double phi = Math.acos(2 * v - 1);
            double r = Math.cbrt(Math.random()) * nucleusRadius;

            double x = r * Math.sin(phi) * Math.cos(theta);
            double y = r * Math.sin(phi) * Math.sin(theta);
            double z = r * Math.cos(phi);

            Sphere s = new Sphere(2.5); // Nucleon size
            s.setMaterial(isProton ? pMat : nMat);
            s.setTranslateX(x);
            s.setTranslateY(y);
            s.setTranslateZ(z);
            root.getChildren().add(s);
        }

        javafx.scene.PointLight light = new javafx.scene.PointLight(Color.WHITE);
        light.setTranslateZ(-100);
        root.getChildren().add(light);
        root.getChildren().add(new javafx.scene.AmbientLight(Color.rgb(100, 100, 100)));

        SubScene subScene = new SubScene(root, 260, 200, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.web("#222"));
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-100);
        subScene.setCamera(camera);

        RotateTransition rt = new RotateTransition(javafx.util.Duration.seconds(15), root);
        rt.setAxis(Rotate.Y_AXIS);
        rt.setByAngle(360);
        rt.setCycleCount(javafx.animation.Animation.INDEFINITE);
        rt.play();

        return subScene;
    }

    private javafx.collections.ObservableList<String> getIsotopes(Element element) {
        List<String> isos = new ArrayList<>();
        int z = element.getAtomicNumber();
        String sym = element.getSymbol();

        // Mock Data Logic based on Z
        // Real logic would require a database.
        // We generate plausible isotopes around the atomic mass.

        int mass = (element.getAtomicMass() != null)
                ? (int) Math.round(element.getAtomicMass().getValue().doubleValue())
                : z * 2;

        // Stable
        isos.add(sym + "-" + mass + " (Stable, Abundance: ~90%)");

        // Some variants
        if (Math.random() > 0.3)
            isos.add(sym + "-" + (mass + 1) + " (Stable, Abundance: ~9%)");
        if (Math.random() > 0.5)
            isos.add(sym + "-" + (mass + 2) + " (Stable, Abundance: ~1%)");

        // Unstable
        isos.add(sym + "-" + (mass - 1) + " (Unstable, t1/2: ~hours)");
        isos.add(sym + "-" + (mass + 3) + " (Unstable, t1/2: ~years)");

        // H Special Case
        if (sym.equals("H")) {
            isos.clear();
            isos.add("H-1 (Protium) - Stable (99.98%)");
            isos.add("H-2 (Deuterium) - Stable (0.02%)");
            isos.add("H-3 (Tritium) - Unstable (12.32y)");
        }
        // C Special Case
        if (sym.equals("C")) {
            isos.clear();
            isos.add("C-12 - Stable (98.9%)");
            isos.add("C-13 - Stable (1.1%)");
            isos.add("C-14 - Unstable (5730y)");
        }
        // U Special Case
        if (sym.equals("U")) {
            isos.clear();
            isos.add("U-238 - Unstable (4.5e9y) (99.3%)");
            isos.add("U-235 - Unstable (7.0e8y) (0.7%)");
            isos.add("U-234 - Unstable (2.4e5y) (Trace)");
        }

        return javafx.collections.FXCollections.observableArrayList(isos);
    }

    private String truncate(String s, int max) {
        return s.length() > max ? s.substring(0, max) : s;
    }

    public static void show(Stage stage) {
        new PeriodicTableViewer().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}


