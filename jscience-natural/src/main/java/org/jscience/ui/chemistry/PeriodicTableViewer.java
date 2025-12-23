/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.ui.chemistry;

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
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.MeshView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
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
 * * @author Silvere Martin-Michiellot
 * 
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
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Main table grid
        GridPane tableGrid = createTableGrid();

        // Wrap grid in a Group for scaling
        Group contentGroup = new Group(tableGrid);
        StackPane zoomPane = new StackPane(contentGroup);
        zoomPane.setAlignment(Pos.CENTER);
        zoomPane.setStyle("-fx-background-color: #1a1a2e;");

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

        HBox topBar = new HBox(10, new Label("Zoom:"), zoomSlider);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #16213e; -fx-text-fill: white;");
        ((Label) topBar.getChildren().get(0)).setTextFill(Color.WHITE);

        root.setTop(null); // No top bar - zoom removed per request
        root.setCenter(scrollPane);
        root.setRight(detailPanel);

        // Auto-scale to fit roughly
        zoomSlider.setValue(0.9);

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("JScience Periodic Table Viewer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createTableGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(3);
        grid.setVgap(3);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: #1a1a2e;");

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
        if (element == null) {
            // Create placeholder for missing elements
            StackPane placeholder = new StackPane();
            Label lbl = new Label(symbol);
            lbl.setTextFill(Color.GRAY);
            placeholder.getChildren().add(lbl);
            placeholder.setPrefSize(60, 70);
            placeholder.setStyle("-fx-background-color: #2d2d44; -fx-background-radius: 5;");
            return placeholder;
        }

        VBox content = new VBox(2);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(5));

        Label atomicNum = new Label(String.valueOf(element.getAtomicNumber()));
        atomicNum.setFont(Font.font("Arial", 10));
        atomicNum.setTextFill(Color.WHITE);

        Label symbolLbl = new Label(element.getSymbol());
        symbolLbl.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        symbolLbl.setTextFill(Color.WHITE);

        Label name = new Label(truncate(element.getName(), 8));
        name.setFont(Font.font("Arial", 9));
        name.setTextFill(Color.LIGHTGRAY);

        content.getChildren().addAll(atomicNum, symbolLbl, name);

        StackPane cell = new StackPane(content);
        cell.setPrefSize(60, 70);
        cell.setStyle(getCategoryStyle(element));

        // Tooltip
        Tooltip tooltip = new Tooltip(getTooltipText(element));
        tooltip.setStyle("-fx-font-size: 12px;");
        Tooltip.install(cell, tooltip);

        // Click handler
        cell.setOnMouseClicked(e -> showElementDetails(element));

        // Hover effect
        cell.setOnMouseEntered(e -> cell.setStyle(getCategoryStyle(element) + " -fx-opacity: 0.8;"));
        cell.setOnMouseExited(e -> cell.setStyle(getCategoryStyle(element)));

        return cell;
    }

    private String getCategoryStyle(Element element) {
        String baseStyle = "-fx-background-radius: 5; -fx-cursor: hand;";
        if (element.getCategory() == null) {
            return "-fx-background-color: #4a4a5a;" + baseStyle;
        }
        return switch (element.getCategory()) {
            case ALKALI_METAL -> "-fx-background-color: #ff6b6b;" + baseStyle;
            case ALKALINE_EARTH_METAL -> "-fx-background-color: #ffa94d;" + baseStyle;
            case TRANSITION_METAL -> "-fx-background-color: #74b9ff;" + baseStyle;
            case POST_TRANSITION_METAL -> "-fx-background-color: #81ecec;" + baseStyle;
            case METALLOID -> "-fx-background-color: #a29bfe;" + baseStyle;
            case NONMETAL -> "-fx-background-color: #55efc4;" + baseStyle;
            case HALOGEN -> "-fx-background-color: #fdcb6e;" + baseStyle;
            case NOBLE_GAS -> "-fx-background-color: #fd79a8;" + baseStyle;
            case LANTHANIDE -> "-fx-background-color: #e17055;" + baseStyle;
            case ACTINIDE -> "-fx-background-color: #d63031;" + baseStyle;
            default -> "-fx-background-color: #636e72;" + baseStyle;
        };
    }

    private String getTooltipText(Element element) {
        StringBuilder sb = new StringBuilder();
        sb.append(element.getName()).append(" (").append(element.getSymbol()).append(")\n");
        sb.append("Atomic Number: ").append(element.getAtomicNumber()).append("\n");
        if (element.getAtomicMass() != null) {
            sb.append("Atomic Mass: ").append(String.format("%.4f", element.getAtomicMass().getValue().doubleValue()))
                    .append(" kg\n");
        }
        if (element.getMeltingPoint() != null) {
            sb.append("Melting Point: ").append(element.getMeltingPoint().getValue()).append(" K\n");
        }
        if (element.getBoilingPoint() != null) {
            sb.append("Boiling Point: ").append(element.getBoilingPoint().getValue()).append(" K\n");
        }
        return sb.toString();
    }

    private VBox createDetailPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));
        panel.setPrefWidth(300);
        panel.setStyle("-fx-background-color: #16213e;");

        Label title = new Label("Element Details");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.WHITE);

        Label hint = new Label("Click an element to see details");
        hint.setTextFill(Color.LIGHTGRAY);
        hint.setWrapText(true);

        panel.getChildren().addAll(title, hint);
        return panel;
    }

    private void showElementDetails(Element element) {
        detailPanel.getChildren().clear();

        Label title = new Label(element.getName());
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        Label symbol = new Label(element.getSymbol());
        symbol.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        symbol.setTextFill(Color.LIGHTBLUE);

        // Infer Group/Period if missing (Logic based on Layout)
        int inferredGroup = element.getGroup();
        int inferredPeriod = element.getPeriod();
        if (inferredGroup == 0 || inferredPeriod == 0) {
            // Locate in layout
            for (int r = 0; r < LAYOUT.length; r++) {
                for (int c = 0; c < LAYOUT[r].length; c++) {
                    if (element.getSymbol().equals(LAYOUT[r][c])) {
                        inferredPeriod = r + 1;
                        inferredGroup = c + 1; // Simplistic group mapping
                        // Adjust for lanthanides/actinides if needed, but for now this is better than 0
                        if (r == 7)
                            inferredPeriod = 6; // Lanthanides (displayed row 8)
                        if (r == 8)
                            inferredPeriod = 7; // Actinides (displayed row 9)
                        break;
                    }
                }
            }
        }

        VBox props = new VBox(5);
        props.getChildren().add(createPropLabel("Atomic Number", String.valueOf(element.getAtomicNumber())));
        props.getChildren().add(createPropLabel("Group", String.valueOf(inferredGroup)));
        props.getChildren().add(createPropLabel("Period", String.valueOf(inferredPeriod)));

        if (element.getCategory() != null) {
            props.getChildren().add(createPropLabel("Category", element.getCategory().name()));
        }
        if (element.getAtomicMass() != null) {
            props.getChildren().add(createPropLabel("Atomic Mass",
                    String.format("%.4f u", element.getAtomicMass().getValue().doubleValue() / 1.66053906660e-27))); // Convert
                                                                                                                     // back
                                                                                                                     // to
                                                                                                                     // u
        }
        if (element.getElectronegativity() != null) {
            props.getChildren().add(createPropLabel("Electronegativity",
                    String.format("%.2f", element.getElectronegativity().doubleValue())));
        }
        if (element.getStandardState() != null) {
            props.getChildren().add(createPropLabel("Standard State", element.getStandardState()));
        }
        if (element.getElectronConfiguration() != null) {
            props.getChildren().add(createPropLabel("Electron Config", element.getElectronConfiguration()));
        }
        if (element.getOxidationStates() != null) {
            props.getChildren().add(createPropLabel("Oxidation States", element.getOxidationStates()));
        }
        if (element.getAtomicRadius() != null) {
            props.getChildren().add(createPropLabel("Atomic Radius",
                    String.format("%.0f pm", element.getAtomicRadius().getValue().doubleValue() * 1e12)));
        }
        if (element.getIonizationEnergy() != null) {
            props.getChildren().add(createPropLabel("Ionization Energy",
                    String.format("%.3f eV", element.getIonizationEnergy().getValue().doubleValue())));
        }
        if (element.getElectronAffinity() != null) {
            props.getChildren().add(createPropLabel("Electron Affinity",
                    String.format("%.3f eV", element.getElectronAffinity().getValue().doubleValue())));
        }
        if (element.getMeltingPoint() != null) {
            props.getChildren().add(createPropLabel("Melting Point",
                    String.format("%.2f K", element.getMeltingPoint().getValue().doubleValue())));
        }
        if (element.getBoilingPoint() != null) {
            props.getChildren().add(createPropLabel("Boiling Point",
                    String.format("%.2f K", element.getBoilingPoint().getValue().doubleValue())));
        }
        if (element.getDensity() != null) {
            props.getChildren().add(createPropLabel("Density",
                    String.format("%.3f g/cm³", element.getDensity().getValue().doubleValue())));
        }
        if (element.getYearDiscovered() > 0) {
            props.getChildren().add(createPropLabel("Year Discovered", String.valueOf(element.getYearDiscovered())));
        }

        // 3D Atom Structure (Protons + Neutrons + Electrons)
        SubScene atomView = createAtomView(element);
        VBox atomBox = new VBox(5, new Label("Atomic Structure (3D):"), atomView);
        atomBox.setStyle("-fx-border-color: #444; -fx-border-width: 1px;");

        detailPanel.getChildren().addAll(title, symbol, props, new Separator(), atomBox);
    }

    private SubScene createAtomView(Element element) {
        Group root = new Group();

        // 1. Nucleus
        javafx.scene.shape.Sphere nucleus = new javafx.scene.shape.Sphere(4);
        nucleus.setMaterial(new javafx.scene.paint.PhongMaterial(Color.RED));
        root.getChildren().add(nucleus);

        // 2. Determine Orbital Type based on Block
        // Simplified Logic:
        // Group 1-2: s-block (Sphere)
        // Group 13-18: p-block (Dumbbells)
        // Group 3-12: d-block (Cloverleaf / Torus+Lobe)
        // Lanthanides/Actinides: f-block (Complex)

        int group = element.getGroup();
        int period = element.getPeriod();

        // Infer block if group is 0 (lanthanides/actinides)
        if (group == 0) {
            // f-block
            createFOrbital(root);
        } else if (group <= 2) {
            // s-block
            createSOrbital(root, period);
        } else if (group >= 13) {
            // p-block
            createPOrbital(root, period);
        } else {
            // d-block
            createDOrbital(root, period);
        }

        // Lighting
        javafx.scene.PointLight light = new javafx.scene.PointLight(Color.WHITE);
        light.setTranslateZ(-100);
        light.setTranslateY(-50);
        root.getChildren().add(light);

        javafx.scene.AmbientLight ambient = new javafx.scene.AmbientLight(Color.rgb(30, 30, 30));
        root.getChildren().add(ambient);

        SubScene subScene = new SubScene(root, 200, 200, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.web("#1a1a2e"));
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-120);
        subScene.setCamera(camera);

        // Auto-rotate the view
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

    private void createFOrbital(Group root) {
        // Complex 8-lobe structure approximation
        double size = 20;
        // Cubic arrangement of lobes
        createLobePair(root, new javafx.geometry.Point3D(1, 1, 1), size, Color.MAGENTA);
        createLobePair(root, new javafx.geometry.Point3D(1, -1, 1), size, Color.MAGENTA);
        createLobePair(root, new javafx.geometry.Point3D(-1, 1, 1), size, Color.MAGENTA);
        createLobePair(root, new javafx.geometry.Point3D(-1, -1, 1), size, Color.MAGENTA);
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
        valueLbl.setTextFill(Color.WHITE);
        row.getChildren().addAll(nameLbl, valueLbl);
        return row;
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
