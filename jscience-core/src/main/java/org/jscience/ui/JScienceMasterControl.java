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

package org.jscience.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jscience.JScience;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.plotting.PlottingBackend;
import org.jscience.technical.backend.BackendDiscovery;
import org.jscience.technical.backend.BackendProvider;
import org.jscience.io.ResourceIO;

import java.util.Locale;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.prefs.Preferences;

/**
 * Master Control Dashboard for JScience Application.
 * Visualizes the state of the application, libraries, devices, and
 * configuration.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class JScienceMasterControl extends Application {

    private static final Preferences PREFS = Preferences.userNodeForPackage(JScienceMasterControl.class);
    private static final String PREF_SELECTED_TAB = "dashboard_selected_tab";
    private static final String PREF_SELECTED_DEVICE = "dashboard_selected_device";

    private static class LocaleItem {
        String name;
        Locale locale;

        LocaleItem(String name, Locale locale) {
            this.name = name;
            this.locale = locale;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        try {
            // Load persistent settings
            String lang = PREFS.get("language", Locale.getDefault().getLanguage());
            I18n.getInstance().setLocale(Locale.of(lang));

            String theme = PREFS.get("theme", "Modena");
            System.setProperty("jscience.theme", theme);

            this.primaryStage = stage;
            refreshUI();
        } catch (Throwable t) {
            t.printStackTrace();
            System.err.println("CRITICAL ERROR IN JScienceMasterControl.start(): " + t.getMessage());
        }
    }

    private void refreshUI() {
        int selectedIndex = 0;
        if (primaryStage.getScene() != null) {
            try {
                TabPane currentPane = (TabPane) ((StackPane) primaryStage.getScene().getRoot()).getChildren().get(0);
                selectedIndex = currentPane.getSelectionModel().getSelectedIndex();
            } catch (Exception e) {
                // Ignore layout changes
            }
        }

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().clear();

        I18n i18n = I18n.getInstance();

        tabPane.getTabs().addAll(
                createGeneralTab(i18n),
                createI18nTab(i18n),
                createThemesTab(i18n),
                createComputingTab(i18n),
                createPlottingTab(i18n), // Added Plotting Tab
                createLibrariesTab(i18n),
                createLoadersTab(i18n),
                createAppsTab(i18n),
                createDevicesTab(i18n));

        tabPane.getTabs().get(0).setId("tab-general");
        tabPane.getTabs().get(1).setId("tab-i18n");
        tabPane.getTabs().get(2).setId("tab-themes");
        tabPane.getTabs().get(3).setId("tab-computing");
        tabPane.getTabs().get(4).setId("tab-plotting");
        tabPane.getTabs().get(5).setId("tab-libraries");
        tabPane.getTabs().get(6).setId("tab-loaders");
        tabPane.getTabs().get(7).setId("tab-apps");
        tabPane.getTabs().get(8).setId("tab-devices");

        // Restore selected tab from preferences if not preserving current state
        if (selectedIndex == 0) {
            selectedIndex = PREFS.getInt(PREF_SELECTED_TAB, 0);
        }
        // Ensure index is valid
        if (selectedIndex < 0 || selectedIndex >= tabPane.getTabs().size()) {
            selectedIndex = 0;
        }

        tabPane.getSelectionModel().select(selectedIndex);

        // Save selected tab on change
        tabPane.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                PREFS.putInt(PREF_SELECTED_TAB, newVal.intValue());
            }
        });

        StackPane root = new StackPane(tabPane);

        if (primaryStage.getScene() == null) {
            Scene scene = new Scene(root, 1100, 750);
            applyCurrentTheme(scene); // Apply to new scene
            primaryStage.setScene(scene);
        } else {
            Scene scene = primaryStage.getScene();
            scene.setRoot(root);
            applyCurrentTheme(scene); // Re-apply theme to ensure styles match
        }

        primaryStage.setTitle(i18n.get("app.title", "JScience Master Control"));
        primaryStage.show();
    }

    private void applyCurrentTheme(Scene scene) {
        String currentTheme = System.getProperty("jscience.theme", "Modena");
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/org/jscience/ui/main.css").toExternalForm());

        if ("Caspian".equalsIgnoreCase(currentTheme)) {
            Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
        } else if ("HighContrast".equalsIgnoreCase(currentTheme)) {
            Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
            scene.getStylesheets().add(getClass().getResource("/org/jscience/ui/high-contrast.css").toExternalForm());
        } else {
            Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
        }
    }

    private Tab createGeneralTab(I18n i18n) {
        VBox content = new VBox(25);
        content.setPadding(new Insets(40));
        content.setAlignment(Pos.CENTER);

        // --- ATOM GRAPHIC ---
        StackPane atomIcon = new StackPane();
        javafx.scene.shape.Circle nucleus = new javafx.scene.shape.Circle(15, javafx.scene.paint.Color.ORANGERED);
        nucleus.setEffect(new javafx.scene.effect.DropShadow(10, javafx.scene.paint.Color.ORANGERED));

        atomIcon.getChildren().addAll(createOrbit(60, 20, 0), createOrbit(60, 20, 60), createOrbit(60, 20, 120),
                nucleus);

        // --- TITLE ---
        Label title = new Label(i18n.get("dashboard.general.title", "JScience Master Control"));
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        Label subtitle = new Label(
                i18n.get("dashboard.general.subtitle", "Universal Scientific Computing Environment"));
        subtitle.getStyleClass().add("dashboard-subtitle");

        GridPane infoGrid = new GridUtils.Builder()
                .addRow(i18n.get("dashboard.general.version", "Version") + ":", org.jscience.JScienceVersion.VERSION)
                .addRow(i18n.get("dashboard.general.build", "Build Date") + ":",
                        org.jscience.JScienceVersion.BUILD_DATE)
                .addRow(i18n.get("dashboard.general.java", "Java Version") + ":", System.getProperty("java.version"))
                .build();
        infoGrid.setAlignment(Pos.CENTER);

        // --- AUTHORS ---
        VBox authorsBox = new VBox(5);
        authorsBox.setAlignment(Pos.CENTER);
        Label authorsHeader = new Label(i18n.get("dashboard.general.authors", "Authors"));
        authorsHeader.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        authorsBox.getChildren().add(authorsHeader);

        for (String author : org.jscience.JScienceVersion.AUTHORS) {
            authorsBox.getChildren().add(new Label(author));
        }

        content.getChildren().addAll(atomIcon, title, subtitle, new Separator(), infoGrid, new Separator(), authorsBox);
        return new Tab(i18n.get("dashboard.tab.general", "General"), content);
    }

    private javafx.scene.Node createOrbit(double rx, double ry, double rotate) {
        javafx.scene.shape.Ellipse orbit = new javafx.scene.shape.Ellipse(rx, ry);
        orbit.setFill(null);
        orbit.setStroke(javafx.scene.paint.Color.LIGHTBLUE);
        orbit.setStrokeWidth(2);
        orbit.setRotate(rotate);
        return orbit;
    }

    private static class GridUtils {
        static class Builder {
            GridPane grid = new GridPane();
            int row = 0;

            Builder() {
                grid.setHgap(20);
                grid.setVgap(10);
                grid.setAlignment(Pos.CENTER);
            }

            Builder addRow(String label, String value) {
                Label l = new Label(label);
                l.setStyle("-fx-font-weight: bold;");
                grid.addRow(row++, l, new Label(value));
                return this;
            }

            GridPane build() {
                return grid;
            }
        }
    }

    private Tab createI18nTab(I18n i18n) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_LEFT);

        Label header = new Label(i18n.get("dashboard.i18n.header", "Language Selection"));
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

        ListView<LocaleItem> langList = new ListView<>();

        // Dynamic discovery of languages
        List<LocaleItem> items = new ArrayList<>();
        // Default languages
        items.add(new LocaleItem(i18n.get("dashboard.lang.en", "English"), Locale.ENGLISH));

        for (Locale locale : i18n.getSupportedLocales()) {
            String displayName = locale.getDisplayLanguage(locale);
            if (displayName.length() > 0) {
                displayName = displayName.substring(0, 1).toUpperCase() + displayName.substring(1);
            }
            // Avoid duplicates if English was added manually above (it was, lines 273)
            // Actually, best to clear items and rebuild from getSupportedLocales
            boolean exists = false;
            for (LocaleItem item : items) {
                if (item.locale.getLanguage().equals(locale.getLanguage())) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                items.add(new LocaleItem(displayName + " (" + locale.getLanguage() + ")", locale));
            }
        }

        langList.getItems().addAll(items);
        langList.setMaxHeight(250);
        langList.setMaxWidth(350);

        Locale current = i18n.getLocale();
        for (LocaleItem item : langList.getItems()) {
            if (item.locale.getLanguage().equals(current.getLanguage())) {
                langList.getSelectionModel().select(item);
                break;
            }
        }

        langList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.locale.equals(current)) {
                i18n.setLocale(newVal.locale);
                PREFS.put("language", newVal.locale.getLanguage()); // Save persistence
                refreshUI();
            }
        });

        langList.setId("lang-list");
        content.getChildren().addAll(header, langList);
        return new Tab(i18n.get("mastercontrol.tab.i18n", "Languages"), content);
    }

    private Tab createComputingTab(I18n i18n) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label header = new Label(i18n.get("mastercontrol.computing.header", "Computing Management"));
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(25);

        // --- Compute Mode ---
        ComboBox<org.jscience.mathematics.context.ComputeMode> modeBox = new ComboBox<>();
        modeBox.getItems().addAll(org.jscience.mathematics.context.ComputeMode.values());
        modeBox.setValue(JScience.getComputeMode());
        modeBox.setOnAction(e -> {
            JScience.setComputeMode(modeBox.getValue());
            JScience.savePreferences();
            refreshUI(); // Update UI to reflect mode change
        });
        VBox modeInfo = createInfoBox(i18n.get("mastercontrol.computing.mode", "Compute Mode"),
                i18n.get("mastercontrol.computing.desc.mode",
                        "AUTO prefers GPU, otherwise uses CPU if unavailable."));

        // --- Float Precision ---
        ComboBox<org.jscience.ComputeContext.FloatPrecision> floatBox = new ComboBox<>();
        floatBox.getItems().addAll(org.jscience.ComputeContext.FloatPrecision.values());
        floatBox.setValue(JScience.getFloatPrecisionMode());
        floatBox.setOnAction(e -> {
            if (floatBox.getValue() == org.jscience.ComputeContext.FloatPrecision.FLOAT)
                JScience.setFloatPrecision();
            else
                JScience.setDoublePrecision();
            JScience.savePreferences();
        });
        VBox floatInfo = createInfoBox(i18n.get("mastercontrol.computing.float_precision", "Float Precision"),
                i18n.get("mastercontrol.computing.desc.precision", "FLOAT (32-bit) / DOUBLE (64-bit)"));

        // --- Integer Precision ---
        ComboBox<org.jscience.ComputeContext.IntPrecision> intBox = new ComboBox<>();
        intBox.getItems().addAll(org.jscience.ComputeContext.IntPrecision.values());
        intBox.setValue(JScience.getIntPrecisionMode());
        intBox.setOnAction(e -> {
            if (intBox.getValue() == org.jscience.ComputeContext.IntPrecision.INT)
                JScience.setIntPrecision();
            else
                JScience.setLongPrecision();
            JScience.savePreferences();
        });
        VBox intInfo = createInfoBox(i18n.get("mastercontrol.computing.int_precision", "Integer Precision"),
                "INT (32-bit) vs LONG (64-bit)");

        // --- MathContext Precision ---
        Spinner<Integer> precSpinner = new Spinner<>(0, 10000, JScience.getMathContext().getPrecision());
        precSpinner.setEditable(true);
        precSpinner.setPrefWidth(150);
        precSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                java.math.MathContext old = JScience.getMathContext();
                JScience.setMathContext(new java.math.MathContext(newVal, old.getRoundingMode()));
                JScience.savePreferences();
            }
        });
        VBox precInfo = createInfoBox(i18n.get("mastercontrol.computing.precision", "Decimal Precision"),
                i18n.get("mastercontrol.computing.desc.precision_digits", "Digits (0 = Unlimited)"));

        // --- MathContext Rounding ---
        ComboBox<java.math.RoundingMode> roundBox = new ComboBox<>();
        roundBox.getItems().addAll(java.math.RoundingMode.values());
        roundBox.setValue(JScience.getMathContext().getRoundingMode());
        roundBox.setPrefWidth(150);
        roundBox.setOnAction(e -> {
            java.math.MathContext old = JScience.getMathContext();
            JScience.setMathContext(new java.math.MathContext(old.getPrecision(), roundBox.getValue()));
            JScience.savePreferences();
        });
        VBox roundInfo = createInfoBox(i18n.get("mastercontrol.computing.rounding", "Rounding Mode"),
                i18n.get("mastercontrol.computing.desc.rounding", "Rounding strategy"));

        Label gpuVal = new Label(JScience.isGpuAvailable() ? i18n.get("mastercontrol.libraries.available", "Available")
                : i18n.get("mastercontrol.libraries.not_available", "Not Available"));
        gpuVal.setStyle(JScience.isGpuAvailable() ? "-fx-text-fill: #27ae60; -fx-font-weight: bold;"
                : "-fx-text-fill: #c0392b;");

        // --- Linear Algebra Provider ---
        ComboBox<String> linAlgBox = new ComboBox<>();
        // Get available Linear Algebra providers
        java.util.List<BackendProvider> linAlgProviders = BackendDiscovery.getInstance().getAvailableProvidersByType(BackendDiscovery.TYPE_LINEAR_ALGEBRA);
        
        linAlgBox.getItems().addAll(linAlgProviders.stream().map(BackendProvider::getId).collect(java.util.stream.Collectors.toList()));
        
        // Add default/fallback if empty or not found
        if (!linAlgBox.getItems().contains(JScience.getLinearAlgebraProviderId())) {
             linAlgBox.getItems().add(JScience.getLinearAlgebraProviderId());
        }
        
        linAlgBox.setValue(JScience.getLinearAlgebraProviderId());
        linAlgBox.setOnAction(e -> {
            JScience.setLinearAlgebraProviderId(linAlgBox.getValue());
            JScience.savePreferences();
        });
        
        VBox linAlgInfo = createInfoBox(i18n.get("mastercontrol.computing.linalg", "Linear Algebra Provider"),
                i18n.get("mastercontrol.computing.desc.linalg", "Select the backend for dense matrix operations (CPU, GPU, Native)."));

        grid.addRow(0, createHeaderLabel(i18n.get("mastercontrol.computing.mode", "Compute Mode")), modeBox, modeInfo);
        grid.addRow(1, createHeaderLabel(i18n.get("mastercontrol.computing.float_precision", "Float Precision")),
                floatBox,
                floatInfo);
        grid.addRow(2, createHeaderLabel(i18n.get("mastercontrol.computing.int_precision", "Integer Precision")),
                intBox,
                intInfo);
        grid.addRow(3, createHeaderLabel(i18n.get("mastercontrol.computing.gpu", "GPU Support")), gpuVal);
        grid.addRow(4, createHeaderLabel(i18n.get("mastercontrol.computing.precision", "Decimal Precision")),
                precSpinner,
                precInfo);
        grid.addRow(5, createHeaderLabel(i18n.get("mastercontrol.computing.rounding", "Rounding Mode")), roundBox,
                roundInfo);
        grid.addRow(6, createHeaderLabel(i18n.get("mastercontrol.computing.linalg", "Linear Algebra")), linAlgBox,
                linAlgInfo);

        content.getChildren().addAll(header, grid);
        return new Tab(i18n.get("mastercontrol.tab.computing", "Computing"), content);
    }

    private Label createHeaderLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-weight: bold;");
        return l;
    }

    private Tab createPlottingTab(I18n i18n) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label header = new Label(i18n.get("mastercontrol.plotting.header", "Plotting Configuration"));
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(25);

        // --- Backend Selection ---
        // --- Backend Selection ---
        ComboBox<PlottingBackend> backendBox = new ComboBox<>();
        backendBox.getItems().addAll(java.util.Arrays.stream(PlottingBackend.values())
                .filter(PlottingBackend::isSupported2D)
                .collect(java.util.stream.Collectors.toList()));
        backendBox.setValue(JScience.getPlottingBackend2D());
        backendBox.setOnAction(e -> {
            JScience.setPlottingBackend2D(backendBox.getValue());
            JScience.savePreferences(); // Save persistence
        });

        VBox backendInfo = createInfoBox("2D Plotting Backend",
                "Choose the library used for rendering 2D charts.\n" +
                        "XCHART: Modern, lightweight.\n" +
                        "JFREECHART: Feature-rich, traditional.\n" +
                        "JAVAFX: Native JavaFX charts.");

        // --- 3D Backend Selection ---
        ComboBox<PlottingBackend> backend3DBox = new ComboBox<>();
        backend3DBox.getItems().addAll(java.util.Arrays.stream(PlottingBackend.values())
                .filter(PlottingBackend::isSupported3D)
                .collect(java.util.stream.Collectors.toList()));
        backend3DBox.setValue(JScience.getPlottingBackend3D());
        backend3DBox.setOnAction(e -> {
            JScience.setPlottingBackend3D(backend3DBox.getValue());
            JScience.savePreferences(); // Save persistence
        });

        VBox backend3DInfo = createInfoBox("3D Plotting Backend",
                "Choose the library used for rendering 3D charts.\n" +
                        "JZY3D: High performance OpenGL 3D charts.");

        grid.addRow(0, createHeaderLabel(i18n.get("mastercontrol.plotting.active_backend", "Active 2D Backend")),
                backendBox,
                backendInfo);
        grid.addRow(1, createHeaderLabel(i18n.get("mastercontrol.plotting.active_backend_3d", "Active 3D Backend")),
                backend3DBox,
                backend3DInfo);

        content.getChildren().addAll(header, grid);
        return new Tab(i18n.get("mastercontrol.tab.plotting", "Plotting"), content);
    }

    private VBox createInfoBox(String title, String tooltipText) {
        VBox box = new VBox();
        Label help = new Label("(?)");
        help.setTooltip(new Tooltip(tooltipText));
        help.getStyleClass().add("mastercontrol-help");
        Label text = new Label(tooltipText);
        text.getStyleClass().add("mastercontrol-description");
        box.getChildren().add(text);
        return box;
    }

    private Tab createLibrariesTab(I18n i18n) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        Label headerTitle = new Label(i18n.get("mastercontrol.libraries.header", "Available Libraries & Status"));
        headerTitle.getStyleClass().add("header-title");
        content.getChildren().add(headerTitle);

        Label helpText = new Label(i18n.get("mastercontrol.libraries.explain.text",
                "These libraries are listed for the following reasons:\n" +
                        "1. They are necessary for JScience to function\n" +
                        "2. They provide extended functionality (GPU, Formats, etc.)"));
        helpText.setStyle("-fx-text-fill: black;");
        helpText.setWrapText(true);
        content.getChildren().add(helpText);

        content.getChildren().add(createLibCategory(i18n, "standards", List.of(
                new LibInfo("jsr385", true),
                new LibInfo("indriya", isClassAvailable("tech.units.indriya.format.SimpleUnitFormat"))), i18n));

        // Mathematics & Algorithms - uses SPI
        content.getChildren().add(createMathCategory(i18n));

        // Hardware Acceleration & Tensors - uses SPI
        content.getChildren().add(createTensorCategory(i18n));

        // Visualization & Plotting - uses SPI discovery
        content.getChildren().add(createPlottingCategory(i18n));

        // Molecular Viewing - uses SPI discovery
        // Molecular Viewing - uses SPI discovery
        content.getChildren().add(createChemistryCategory(i18n));
        
        // Quantum Computing - uses SPI discovery
        content.getChildren().add(createQuantumCategory(i18n));
        
        content.getChildren().add(createLibCategory(i18n, "framework", List.of(
                new LibInfo("javalin", isClassAvailable("io.javalin.Javalin")),
                new LibInfo("jackson", isClassAvailable("com.fasterxml.jackson.databind.ObjectMapper")),
                new LibInfo("slf4j", isClassAvailable("org.slf4j.LoggerFactory")),
                new LibInfo("grpc", isClassAvailable("io.grpc.ManagedChannel"))), i18n));

        return new Tab(i18n.get("mastercontrol.tab.libraries", "Libraries"), scroll);
    }

    private VBox createChemistryCategory(I18n i18n) {
        VBox box = new VBox(12);
        Label header = new Label(i18n.get("mastercontrol.chemistry.title", "Chemistry & Biology"));
        header.getStyleClass().add("header-title");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setPadding(new Insets(10, 0, 10, 0));

        // Molecular Rendering Backend
        ComboBox<String> backendBox = new ComboBox<>();
        // Fetch available molecular backends via SPI
        java.util.List<org.jscience.technical.backend.BackendProvider> providers = 
            org.jscience.technical.backend.BackendDiscovery.getInstance()
                .getProvidersByType(org.jscience.technical.backend.BackendDiscovery.TYPE_MOLECULAR);

        backendBox.getItems().add("AUTO"); // Default option
        for (org.jscience.technical.backend.BackendProvider p : providers) {
            backendBox.getItems().add(p.getId());
        }

        String current = JScience.getMolecularBackendId();
        backendBox.setValue(current == null ? "AUTO" : current);

        backendBox.setOnAction(e -> {
            String val = backendBox.getValue();
            JScience.setMolecularBackendId("AUTO".equals(val) ? null : val);
            JScience.savePreferences();
        });

        VBox backendInfo = createInfoBox("Molecular Renderer", 
            "Select the engine used to render 3D molecular structures.\n" +
            "Requires restart of the viewer window to take effect.");

        grid.addRow(0, createHeaderLabel(i18n.get("mastercontrol.chemistry.backend", "Molecular Renderer")),
                backendBox,
                backendInfo);

        box.getChildren().addAll(header, grid, new Separator());
        return box;
    }
    
    private VBox createQuantumCategory(I18n i18n) {
        VBox box = new VBox(12);
        Label header = new Label(i18n.get("mastercontrol.quantum.title", "Quantum Computing"));
        header.getStyleClass().add("header-title");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setPadding(new Insets(10, 0, 10, 0));

        // Quantum Backend
        ComboBox<String> backendBox = new ComboBox<>();
        // Fetch available quantum backends via SPI
        java.util.List<org.jscience.technical.backend.BackendProvider> providers = 
            org.jscience.technical.backend.BackendDiscovery.getInstance()
                .getProvidersByType(org.jscience.technical.backend.BackendDiscovery.TYPE_QUANTUM);

        backendBox.getItems().add("AUTO"); // Default option
        for (org.jscience.technical.backend.BackendProvider p : providers) {
            backendBox.getItems().add(p.getId());
        }

        String current = JScience.getQuantumBackendId();
        backendBox.setValue(current == null ? "AUTO" : current);

        backendBox.setOnAction(e -> {
            String val = backendBox.getValue();
            JScience.setQuantumBackendId("AUTO".equals(val) ? null : val);
            JScience.savePreferences();
        });

        VBox backendInfo = createInfoBox("Quantum Provider", 
            "Select the quantum simulation backend (e.g., Strange, Quantum4J, Braket).\n" +
            "Determines execution environment for quantum circuits.");

        grid.addRow(0, createHeaderLabel(i18n.get("mastercontrol.quantum.backend", "Quantum Provider")),
                backendBox,
                backendInfo);

        box.getChildren().addAll(header, grid, new Separator());
        return box;
    }

    private static class LibInfo {
        final String key;
        final boolean available;

        LibInfo(String key, boolean available) {
            this.key = key;
            this.available = available;
        }
    }

    private VBox createLibCategory(I18n i18n, String catKey, List<LibInfo> libs, I18n i18nRef) {
        VBox box = new VBox(12);
        Label header = new Label(i18n.get("mastercontrol.libraries.cat." + catKey, catKey));
        header.getStyleClass().add("header-title");

        Label desc = new Label(i18n.get("mastercontrol.libraries.cat." + catKey + ".desc", ""));
        desc.getStyleClass().add("mastercontrol-description");

        GridPane grid = new GridPane();
        grid.setHgap(35);
        grid.setVgap(12);
        int r = 0;
        for (LibInfo lib : libs) {
            addLibRow(grid, r++, lib.key, lib.available, "", i18n);
        }

        box.getChildren().addAll(header, desc, grid, new Separator());
        return box;
    }

    private void addLibRow(GridPane grid, int row, String nameKey, boolean available, String descKey, I18n i18n) {
        String name = i18n.get("lib." + nameKey + ".name", nameKey);
        String desc = i18n.get("lib." + nameKey + ".desc", descKey); // Could fallback to hardcoded if needed

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black;");

        Label descLabel = new Label(desc);
        descLabel.getStyleClass().add("mastercontrol-description");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(500);

        Label statusLabel = new Label(available ? i18n.get("mastercontrol.libraries.available", "Available")
                : i18n.get("mastercontrol.libraries.not_available", "Not Available"));
        statusLabel.setStyle("-fx-text-fill: " + (available ? "#27ae60" : "#c0392b") + "; -fx-font-weight: bold;");

        grid.addRow(row, nameLabel, statusLabel, descLabel);
    }

    private VBox createMathCategory(I18n i18n) {
        return createBackendCategory(i18n, BackendDiscovery.TYPE_MATH,
                i18n.get("mastercontrol.libraries.cat.math", "Mathematics & Algorithms"),
                i18n.get("mastercontrol.libraries.cat.math.desc", "Mathematical engines and algorithm providers."));
    }

    private VBox createTensorCategory(I18n i18n) {
        return createBackendCategory(i18n, BackendDiscovery.TYPE_TENSOR,
                i18n.get("mastercontrol.libraries.cat.hardware", "Hardware Acceleration & Tensors"),
                i18n.get("mastercontrol.libraries.cat.hardware.desc",
                        "GPU and hardware-accelerated tensor computation backends."));
    }

    /**
     * Creates the Visualization & Plotting category using SPI discovery.
     */
    private VBox createPlottingCategory(I18n i18n) {
        return createBackendCategory(i18n, BackendDiscovery.TYPE_PLOTTING,
                i18n.get("mastercontrol.libraries.cat.vis", "Visualization & Plotting"),
                i18n.get("mastercontrol.libraries.cat.vis.desc", "Graphics, charting and plotting backends."));
    }

    /**
     * Creates the Molecular Viewing category using SPI discovery.
     */
    @SuppressWarnings("unused") // Reserved for future layout options
    private VBox createMolecularCategory(I18n i18n) {
        return createBackendCategory(i18n, BackendDiscovery.TYPE_MOLECULAR,
                i18n.get("mastercontrol.libraries.cat.molecular", "Molecular Viewing"),
                i18n.get("mastercontrol.libraries.cat.molecular.desc",
                        "3D molecular visualization backends for chemistry applications."));
    }

    /**
     * Reusable method to create a category from discovered SPI backends.
     */
    private VBox createBackendCategory(I18n i18n, String type, String title, String description) {
        VBox box = new VBox(12);
        Label header = new Label(title);
        header.getStyleClass().add("header-title");

        Label desc = new Label(description);
        desc.getStyleClass().add("mastercontrol-description");

        GridPane grid = new GridPane();
        grid.setHgap(35);
        grid.setVgap(12);

        List<BackendProvider> providers = BackendDiscovery.getInstance().getProvidersByType(type);

        int r = 0;
        for (BackendProvider provider : providers) {
            String name = i18n.get("lib." + provider.getId() + ".name", provider.getName());
            String providerDesc = i18n.get("lib." + provider.getId() + ".desc", provider.getDescription());
            boolean available = provider.isAvailable();

            Label nameLabel = new Label(name);
            nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black;");

            Label descLabel = new Label(providerDesc);
            descLabel.getStyleClass().add("mastercontrol-description");
            descLabel.setWrapText(true);
            descLabel.setMaxWidth(500);

            Label statusLabel = new Label(available
                    ? i18n.get("mastercontrol.libraries.available", "Available")
                    : i18n.get("mastercontrol.libraries.not_available", "Not Available"));
            statusLabel.setStyle("-fx-text-fill: " + (available ? "#27ae60" : "#c0392b") + "; -fx-font-weight: bold;");

            grid.addRow(r++, nameLabel, statusLabel, descLabel);
        }

        if (providers.isEmpty()) {
            Label noProviders = new Label("No " + type + " backends discovered.");
            noProviders.setStyle("-fx-font-style: italic;");
            grid.add(noProviders, 0, 0);
        }

        box.getChildren().addAll(header, desc, grid, new Separator());
        return box;
    }

    /**
     * Creates the Molecular Viewing category using SPI discovery.
     */

    private Tab createLoadersTab(I18n i18n) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label header = new Label(i18n.get("mastercontrol.loaders.header", "Known Data Loaders & Formats"));
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label hint = new Label(i18n.get("mastercontrol.loaders.discovering", "Searching for loaders..."));
        hint.setStyle("-fx-font-style: italic; -fx-text-fill: #777;");

        Accordion accordion = new Accordion();

        // Discover loaders dynamically
        List<MasterControlDiscovery.ClassInfo> loaders = MasterControlDiscovery.getInstance().findClasses("Loader");

        // Instantiate and Group by Category
        Map<String, List<ResourceIO<?>>> grouped = new TreeMap<>(); // Sorted categories (by key)

        for (MasterControlDiscovery.ClassInfo info : loaders) {
            try {
                Class<?> clazz = Class.forName(info.fullName);
                // Ensure it's a ResourceIO and has a no-arg constructor
                if (ResourceIO.class.isAssignableFrom(clazz)) {
                    // Try to instantiate
                    ResourceIO<?> loader;
                    try {
                        loader = (ResourceIO<?>) clazz.getDeclaredConstructor().newInstance();
                    } catch (NoSuchMethodException e) {
                        // Fallback for loaders without public no-arg constructor?
                        // Just skip or wrap? Use default metadata.
                        continue;
                    }

                    // Get metadata
                    String category = loader.getCategory();

                    // Fallback to package heuristic if category is "category.other" (default)
                    // and we want to preserve previous logic for existing loaders that don't
                    // override it yet?
                    // User asked to USE the loader category.
                    // But currently all loaders use the default "category.other".
                    // So I should probably keep the heuristic AS THE DEFAULT implementation in
                    // ResourceLoader?
                    // No, I can't modify all 40 loaders now.
                    // So I will apply the heuristic HERE if the loader returns "category.other".

                    if ("category.other".equals(category)) {
                        category = getCategoryFromPackage(info.fullName);
                    }

                    grouped.computeIfAbsent(category, k -> new ArrayList<>()).add(loader);
                }
            } catch (Exception e) {
                // Log and continue
                e.printStackTrace();
            }
        }

        // Create UI
        for (Map.Entry<String, List<ResourceIO<?>>> entry : grouped.entrySet()) {
            List<ResourceIO<?>> categoryLoaders = entry.getValue();

            // Sort by Name (Internationalized?)
            // We'll sort by the raw name first, or translated name?
            // User said "triés par ordre alphabétique".
            // Let's sort by the display name.
            categoryLoaders.sort((l1, l2) -> {
                String n1 = l1.getName();
                n1 = i18n.get(n1, n1);
                String n2 = l2.getName();
                n2 = i18n.get(n2, n2);
                return n1.compareToIgnoreCase(n2);
            });

            List<AppEntry> entries = new ArrayList<>();
            for (ResourceIO<?> loader : categoryLoaders) {
                String nameKey = loader.getName();
                String displayName = i18n.get(nameKey, nameKey);

                String descKey = loader.getDescription();
                String displayDesc = i18n.get(descKey, descKey);

                // Add Input/Output tags if requested?
                // User said "rajouter ... input et en output".
                // Maybe as part of description or name?
                StringBuilder fullDesc = new StringBuilder(displayDesc);
                if (loader.isInput())
                    fullDesc.append(" [Import]");
                if (loader.isOutput())
                    fullDesc.append(" [Export]");

                // We need package for the icon generation/grouping logic in AppEntry if used?
                // AppEntry takes (name, className, package).
                entries.add(new AppEntry(displayName, loader.getClass().getName(), loader.getClass().getPackageName()));
            }

            String categoryKey = entry.getKey();
            String categoryName = i18n.get(categoryKey, categoryKey);
            TitledPane pane = new TitledPane(categoryName + " (" + entries.size() + ")",
                    createAppList(false, entries.toArray(new AppEntry[0])));
            accordion.getPanes().add(pane);
        }

        String countMsg = java.text.MessageFormat.format(
                i18n.get("mastercontrol.loaders.count", "{0} loaders found"), loaders.size());
        hint.setText(countMsg);

        content.getChildren().addAll(header, hint, accordion);
        return new Tab(i18n.get("mastercontrol.tab.loaders", "Loaders"), content);
    }

    private String getCategoryFromPackage(String fullName) {
        if (fullName.contains(".geography."))
            return "category.geography";
        if (fullName.contains(".earth."))
            return "category.earth_sciences";
        if (fullName.contains(".astronomy."))
            return "category.astronomy";
        if (fullName.contains(".biochemistry."))
            return "category.biochemistry";
        if (fullName.contains(".biology."))
            return "category.biology";
        if (fullName.contains(".chemistry."))
            return "category.chemistry";
        if (fullName.contains(".economics."))
            return "category.economics";
        if (fullName.contains(".history."))
            return "category.history";
        if (fullName.contains(".politics."))
            return "category.politics";
        if (fullName.contains(".sociology."))
            return "category.sociology";
        if (fullName.contains(".device.") || fullName.contains(".nmea"))
            return "category.device";
        if (fullName.contains(".mathematics.") || fullName.contains(".io."))
            return "category.mathematics";
        if (fullName.contains(".engineering."))
            return "category.engineering";

        return "category.other";
    }


    private boolean isClassAvailable(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private Tab createThemesTab(I18n i18n) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_LEFT);

        Label header = new Label(i18n.get("mastercontrol.themes.header", "Visual Theme Selection"));
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

        VBox themeSelector = new VBox(10);
        Label comboLabel = new Label(i18n.get("mastercontrol.themes.select", "Choose Interface Style:"));
        comboLabel.setStyle("-fx-font-weight: bold;");

        ComboBox<String> themeCombo = new ComboBox<>();
        themeCombo.getItems().addAll("Modena", "Caspian", "High Contrast");
        String currentTheme = System.getProperty("jscience.theme", "Modena");
        if ("HighContrast".equals(currentTheme))
            currentTheme = "High Contrast";
        themeCombo.setValue(currentTheme);
        themeCombo.setPrefWidth(250);

        themeCombo.setOnAction(e -> {
            String selected = themeCombo.getValue().replace(" ", "");
            applyDashboardTheme(selected);
        });

        themeSelector.getChildren().addAll(comboLabel, themeCombo);

        VBox previewBox = new VBox(15);
        previewBox.setPadding(new Insets(20));
        previewBox.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-color: #fdfdfd;");
        previewBox.getChildren().addAll(
                new Label("Theme Preview Components:"),
                new Button("Sample Button"),
                new CheckBox("Sample CheckBox"),
                new ProgressBar(0.6));

        content.getChildren().addAll(header, themeSelector, previewBox);
        return new Tab(i18n.get("mastercontrol.tab.themes", "Themes"), content);
    }

    private void applyDashboardTheme(String theme) {
        System.setProperty("jscience.theme", theme);
        PREFS.put("theme", theme); // Save persistence
        refreshUI();
    }

    private Tab createAppsTab(I18n i18n) {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label header = new Label(i18n.get("mastercontrol.apps.header", "Applications, Demos & Viewers"));
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label hint = new Label(i18n.get("mastercontrol.apps.discovering", "Discovering from SPI..."));
        hint.setStyle("-fx-font-style: italic; -fx-text-fill: #777;");

        Accordion accordion = new Accordion();

        // Use new SPI Discovery
        Map<MasterControlDiscovery.ProviderType, Map<String, List<ViewerProvider>>> grouped = MasterControlDiscovery
                .getInstance().getProvidersByType();

        List<AppEntry> apps = flattenProviders(grouped.get(MasterControlDiscovery.ProviderType.APP));
        List<AppEntry> demos = flattenProviders(grouped.get(MasterControlDiscovery.ProviderType.DEMO));
        List<AppEntry> viewers = flattenProviders(grouped.get(MasterControlDiscovery.ProviderType.VIEWER));

        // Create panes
        TitledPane appsPane = new TitledPane(
                i18n.get("mastercontrol.apps.category.apps", "Scientific Applications") + " (" + apps.size() + ")",
                createAppList(true, apps.toArray(new AppEntry[0])));
        TitledPane demosPane = new TitledPane(
                i18n.get("mastercontrol.apps.category.demos", "Interactive Demos") + " (" + demos.size() + ")",
                createAppList(true, demos.toArray(new AppEntry[0])));
        TitledPane viewersPane = new TitledPane(
                i18n.get("mastercontrol.apps.category.viewers", "Data Viewers & Tools") + " (" + viewers.size() + ")",
                createAppList(true, viewers.toArray(new AppEntry[0])));

        accordion.getPanes().addAll(appsPane, demosPane, viewersPane);

        if (!apps.isEmpty())
            appsPane.setExpanded(true);
        else if (!demos.isEmpty())
            demosPane.setExpanded(true);
        else if (!viewers.isEmpty())
            viewersPane.setExpanded(true);

        int validCount = apps.size() + demos.size() + viewers.size();
        hint.setText(i18n.get("mastercontrol.apps.discovered", "Apps Found:") + " " + validCount);

        content.getChildren().addAll(header, hint, accordion);
        return new Tab(i18n.get("mastercontrol.tab.apps", "Apps"), content);
    }

    private List<AppEntry> flattenProviders(Map<String, List<ViewerProvider>> categoryMap) {
        List<AppEntry> result = new ArrayList<>();
        if (categoryMap == null)
            return result;

        for (List<ViewerProvider> list : categoryMap.values()) {
            for (ViewerProvider p : list) {
                result.add(new AppEntry(p.getName(), p.getClass().getName(), p.getDescription()));
            }
        }
        // Sort by name
        result.sort((e1, e2) -> e1.name.compareToIgnoreCase(e2.name));
        return result;
    }

    private ListView<AppEntry> createAppList(boolean enableLaunch, AppEntry... entries) {
        ListView<AppEntry> list = new ListView<>();
        for (AppEntry entry : entries)
            list.getItems().add(entry);

        list.setFixedCellSize(52);
        list.prefHeightProperty()
                .bind(javafx.beans.binding.Bindings.size(list.getItems()).multiply(list.getFixedCellSize()).add(2));

        list.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(AppEntry item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    VBox box = new VBox(2);
                    box.setPadding(new Insets(5));
                    Label name = new Label(item.name);
                    name.setStyle("-fx-font-weight: bold;");
                    Label desc = new Label(item.description);
                    desc.getStyleClass().add("mastercontrol-description");
                    box.getChildren().addAll(name, desc);
                    setGraphic(box);
                }
            }
        });

        if (enableLaunch) {
            list.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    AppEntry selected = list.getSelectionModel().getSelectedItem();
                    if (selected != null)
                        launchApp(selected.className);
                }
            });
        }

        return list;
    }

    private static class AppEntry {
        String name;
        String className;
        String description;

        AppEntry(String n, String c, String d) {
            this.name = n;
            this.className = c;
            this.description = d;
        }
    }

    private void launchApp(String className) {
        try {
            Class<?> cls = Class.forName(className);
            if (Application.class.isAssignableFrom(cls)) {
                // Launch in new Stage
                Stage stage = new Stage();
                Application app = (Application) cls.getDeclaredConstructor().newInstance();
                app.start(stage);
            } else if (ViewerProvider.class.isAssignableFrom(cls)) {
                // Launch ViewerProvider
                Stage stage = new Stage();
                ViewerProvider demo = (ViewerProvider) cls.getDeclaredConstructor().newInstance();
                demo.show(stage);
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to launch app: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    private Tab createDevicesTab(I18n i18n) {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label header = new Label(i18n.get("mastercontrol.devices.header", "Available Devices (Simulated & JNI)"));
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Create simulated device instances (Sorted)
        Map<String, org.jscience.device.sim.SimulatedDevice> devices = new TreeMap<>();
        devices.put("Generic GPIB Device", new org.jscience.device.sim.SimulatedGPIBDevice());
        devices.put("Generic USB Device", new org.jscience.device.sim.SimulatedUSBDevice());

        // Also discover any other devices
        List<MasterControlDiscovery.ClassInfo> discoveredDevices = MasterControlDiscovery.getInstance()
                .findClasses("Device");
        for (MasterControlDiscovery.ClassInfo info : discoveredDevices) {
            if (!devices.containsKey(info.simpleName)) {
                try {
                    Class<?> cls = Class.forName(info.fullName);
                    if (org.jscience.device.sim.SimulatedDevice.class.isAssignableFrom(cls)) {
                        org.jscience.device.sim.SimulatedDevice dev = (org.jscience.device.sim.SimulatedDevice) cls
                                .getDeclaredConstructor().newInstance();
                        devices.put(info.simpleName, dev);
                    }
                } catch (Exception e) {
                    // Ignore devices that can't be instantiated
                }
            }
        }

        SplitPane split = new SplitPane();
        ListView<String> deviceList = new ListView<>();
        deviceList.getItems().addAll(devices.keySet());

        TextArea details = new TextArea();
        details.setEditable(false);

        deviceList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                org.jscience.device.sim.SimulatedDevice dev = devices.get(newVal);
                if (dev != null)
                    details.setText(dev.getFormattedInfo()); // Rich info using metadata
                // Persist selection
                PREFS.put(PREF_SELECTED_DEVICE, newVal);
            }
        });

        // Restore last selected device
        String lastDevice = PREFS.get(PREF_SELECTED_DEVICE, null);
        if (lastDevice != null && devices.containsKey(lastDevice)) {
            deviceList.getSelectionModel().select(lastDevice);
        }

        split.getItems().addAll(deviceList, details);
        split.setDividerPositions(0.3);

        content.getChildren().addAll(header, split);
        return new Tab(i18n.get("mastercontrol.tab.devices", "Devices"), content);
    }
}
