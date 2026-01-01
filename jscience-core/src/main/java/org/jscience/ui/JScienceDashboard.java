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

import java.io.File;
import java.util.Locale;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Main Dashboard for JScience Application.
 * Visualizes the state of the application, libraries, devices, and
 * configuration.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class JScienceDashboard extends Application {

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
        this.primaryStage = stage;
        refreshUI();
    }

    private void refreshUI() {
        int selectedIndex = 0;
        if (primaryStage.getScene() != null) {
            TabPane currentPane = (TabPane) ((StackPane) primaryStage.getScene().getRoot()).getChildren().get(0);
            selectedIndex = currentPane.getSelectionModel().getSelectedIndex();
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
                createLibrariesTab(i18n),
                createLoadersTab(i18n),
                createAppsTab(i18n),
                createDevicesTab(i18n));

        tabPane.getTabs().get(0).setId("tab-general");
        tabPane.getTabs().get(1).setId("tab-i18n");
        tabPane.getTabs().get(2).setId("tab-themes");
        tabPane.getTabs().get(3).setId("tab-computing");
        tabPane.getTabs().get(4).setId("tab-libraries");
        tabPane.getTabs().get(5).setId("tab-loaders");
        tabPane.getTabs().get(6).setId("tab-apps");
        tabPane.getTabs().get(7).setId("tab-devices");

        tabPane.getSelectionModel().select(selectedIndex);

        tabPane.getSelectionModel().select(selectedIndex);

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

        primaryStage.setTitle(i18n.get("app.title", "JScience Dashboard"));
        // primaryStage.setScene(scene); // Removed as setScene/setRoot handled above
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
        Label title = new Label(i18n.get("dashboard.general.title", "JScience Dashboard"));
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

        List<String> resources = DashboardDiscovery.getInstance().findResources("messages_core_");
        for (String res : resources) {
            try {
                // messages_core_fr.properties -> fr
                String filename = new File(res).getName();
                String code = filename.substring("messages_core_".length(), filename.indexOf('.'));
                Locale loc = Locale.of(code);
                String displayName = loc.getDisplayLanguage(loc);
                // Capitalize first letter
                if (displayName.length() > 0)
                    displayName = displayName.substring(0, 1).toUpperCase() + displayName.substring(1);

                boolean exists = false;
                for (LocaleItem item : items) {
                    if (item.locale.getLanguage().equals(loc.getLanguage()))
                        exists = true;
                }

                if (!exists) {
                    items.add(new LocaleItem(displayName + " (" + code + ")", loc));
                }
            } catch (Exception e) {
                // ignore invalid filenames
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
                refreshUI();
            }
        });

        langList.setId("lang-list");
        content.getChildren().addAll(header, langList);
        return new Tab(i18n.get("dashboard.tab.i18n", "Languages"), content);
    }

    private Tab createComputingTab(I18n i18n) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label header = new Label(i18n.get("dashboard.computing.header", "Computing Management"));
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
        });
        VBox modeInfo = createInfoBox(i18n.get("dashboard.computing.mode", "Mode de calcul"),
                i18n.get("dashboard.computing.desc.mode",
                        "AUTO prÃƒÂ©fÃƒÂ¨re le GPU, sinon utilise le CPU si indisponible."));

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
        VBox floatInfo = createInfoBox(i18n.get("dashboard.computing.float_precision", "Float Precision"),
                i18n.get("dashboard.computing.desc.precision", "FLOAT (32-bit) / DOUBLE (64-bit)"));

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
        VBox intInfo = createInfoBox(i18n.get("dashboard.computing.int_precision", "Integer Precision"),
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
        VBox precInfo = createInfoBox(i18n.get("dashboard.computing.precision", "PrÃƒÂ©cision dÃƒÂ©cimale"),
                i18n.get("dashboard.computing.desc.precision_digits", "Chiffres (0 = IllimitÃƒÂ©)"));

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
        VBox roundInfo = createInfoBox(i18n.get("dashboard.computing.rounding", "Mode d'arrondi"),
                i18n.get("dashboard.computing.desc.rounding", "StratÃƒÂ©gie d'arrondi"));

        Label gpuVal = new Label(JScience.isGpuAvailable() ? i18n.get("dashboard.libraries.available", "Available")
                : i18n.get("dashboard.libraries.not_available", "Not Available"));
        gpuVal.setStyle(JScience.isGpuAvailable() ? "-fx-text-fill: #27ae60; -fx-font-weight: bold;"
                : "-fx-text-fill: #c0392b;");

        grid.addRow(0, createHeaderLabel(i18n.get("dashboard.computing.mode", "Compute Mode")), modeBox, modeInfo);
        grid.addRow(1, createHeaderLabel(i18n.get("dashboard.computing.float_precision", "Float Precision")), floatBox,
                floatInfo);
        grid.addRow(2, createHeaderLabel(i18n.get("dashboard.computing.int_precision", "Integer Precision")), intBox,
                intInfo);
        grid.addRow(3, createHeaderLabel(i18n.get("dashboard.computing.gpu", "GPU Support")), gpuVal);
        grid.addRow(4, createHeaderLabel(i18n.get("dashboard.computing.precision", "Decimal Precision")), precSpinner,
                precInfo);
        grid.addRow(5, createHeaderLabel(i18n.get("dashboard.computing.rounding", "Rounding Mode")), roundBox,
                roundInfo);

        content.getChildren().addAll(header, grid);
        return new Tab(i18n.get("dashboard.tab.computing", "Computing"), content);
    }

    private VBox createInfoBox(String title, String tooltipText) {
        VBox box = new VBox();
        Label help = new Label("(?)");
        help.setTooltip(new Tooltip(tooltipText));
        help.getStyleClass().add("dashboard-help");
        Label text = new Label(tooltipText);
        text.getStyleClass().add("dashboard-description");
        box.getChildren().add(text);
        return box;
    }

    private Tab createLibrariesTab(I18n i18n) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        Label headerTitle = new Label(i18n.get("dashboard.libraries.header", "Available Libraries & Status"));
        headerTitle.getStyleClass().add("header-title");
        content.getChildren().add(headerTitle);

        // --- HELP SECTION ---
        Label helpText = new Label(i18n.get("dashboard.libraries.explain.text",
                "Ces bibliothÃƒÂ¨ques sont listÃƒÂ©es pour les raisons suivantes :\n" +
                        "1. Elles sont nÃƒÂ©cessaires au fonctionnement de JScience\n" +
                        "2. Elles offrent des fonctionnalitÃƒÂ©s ÃƒÂ©tendues (GPU, Formats, etc.)"));
        helpText.setStyle("-fx-text-fill: black;");
        helpText.setWrapText(true);
        content.getChildren().add(helpText);

        // --- STANDARDS ---
        content.getChildren().add(createLibCategory(i18n, "standards", List.of(
                new LibInfo("jsr385", true),
                new LibInfo("indriya", isClassAvailable("tech.units.indriya.format.SimpleUnitFormat"))), i18n));

        // --- MATH ENGINES ---
        content.getChildren().add(createLibCategory(i18n, "math", List.of(
                new LibInfo("commonsmath", isClassAvailable("org.apache.commons.math3.util.Precision")),
                new LibInfo("ejml", isClassAvailable("org.ejml.EjmlParameters")),
                new LibInfo("colt", isClassAvailable("cern.colt.Version")),
                new LibInfo("jblas", isClassAvailable("org.jblas.NativeBlas")),
                new LibInfo("jsci_core", true)), i18n));

        // --- HARDWARE ACCELERATION ---
        content.getChildren().add(createLibCategory(i18n, "hardware", List.of(
                new LibInfo("jcuda", JScience.isCudaAvailable()),
                new LibInfo("jocl", JScience.isOpenCLAvailable()),
                new LibInfo("nd4j", JScience.isND4JAvailable())), i18n));

        // --- VISUALIZATION ---
        content.getChildren().add(createLibCategory(i18n, "vis", List.of(
                new LibInfo("jfreechart", isClassAvailable("org.jfree.chart.JFreeChart")),
                new LibInfo("javafx", isClassAvailable("javafx.application.Application"))), i18n));

        // --- INFRASTRUCTURE ---
        content.getChildren().add(createLibCategory(i18n, "framework", List.of(
                new LibInfo("javalin", isClassAvailable("io.javalin.Javalin")),
                new LibInfo("jackson", isClassAvailable("com.fasterxml.jackson.databind.ObjectMapper")),
                new LibInfo("slf4j", isClassAvailable("org.slf4j.LoggerFactory")),
                new LibInfo("grpc", isClassAvailable("io.grpc.ManagedChannel"))), i18n));

        return new Tab(i18n.get("dashboard.tab.libraries", "Libraries"), scroll);
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
        Label header = new Label(i18n.get("dashboard.libraries.cat." + catKey, catKey));
        header.getStyleClass().add("header-title");

        Label desc = new Label(i18n.get("dashboard.libraries.cat." + catKey + ".desc", ""));
        desc.getStyleClass().add("dashboard-description");

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
        String desc = i18n.get("lib." + nameKey + ".desc", descKey);

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black;");

        Label descLabel = new Label(desc);
        descLabel.getStyleClass().add("dashboard-description");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(500);

        Label statusLabel = new Label(available ? i18n.get("dashboard.libraries.available", "Available")
                : i18n.get("dashboard.libraries.not_available", "Not Available"));
        statusLabel.setStyle("-fx-text-fill: " + (available ? "#27ae60" : "#c0392b") + "; -fx-font-weight: bold;");

        grid.addRow(row, nameLabel, statusLabel, descLabel);
    }

    private Tab createLoadersTab(I18n i18n) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label header = new Label(i18n.get("dashboard.loaders.header", "Known Data Loaders & Formats"));
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label hint = new Label(i18n.get("dashboard.loaders.discovering", "Searching for loaders..."));
        hint.setStyle("-fx-font-style: italic; -fx-text-fill: #777;");

        Accordion accordion = new Accordion();
        // VBox.setVgrow(accordion, Priority.ALWAYS); // Removed to prevent empty space
        // at bottom

        // Discover loaders dynamically
        List<DashboardDiscovery.ClassInfo> loaders = DashboardDiscovery.getInstance().findClasses("Loader");

        // Group by package/domain
        Map<String, List<DashboardDiscovery.ClassInfo>> grouped = new LinkedHashMap<>();
        for (DashboardDiscovery.ClassInfo info : loaders) {
            String category = getCategoryFromPackage(info.fullName);
            grouped.computeIfAbsent(category, k -> new ArrayList<>()).add(info);
        }

        for (Map.Entry<String, List<DashboardDiscovery.ClassInfo>> entry : grouped.entrySet()) {
            List<AppEntry> entries = new ArrayList<>();
            for (DashboardDiscovery.ClassInfo info : entry.getValue()) {
                String pkg = "";
                int lastDot = info.fullName.lastIndexOf('.');
                if (lastDot > 0)
                    pkg = info.fullName.substring(0, lastDot);
                entries.add(new AppEntry(info.simpleName, info.fullName, pkg));
            }
            // Loaders are not launchable (false)
            // Use createAppList for consistent look and feel
            TitledPane pane = new TitledPane(entry.getKey() + " (" + entries.size() + ")",
                    createAppList(false, entries.toArray(new AppEntry[0])));
            accordion.getPanes().add(pane);
        }

        if (!accordion.getPanes().isEmpty()) {
            accordion.setExpandedPane(accordion.getPanes().get(0));
        }

        hint.setText(loaders.size() + " chargeurs trouvÃƒÂ©s");

        content.getChildren().addAll(header, hint, accordion);
        return new Tab(i18n.get("dashboard.tab.loaders", "Loaders"), content);
    }

    private String getCategoryFromPackage(String fullName) {
        if (fullName.contains(".geography.") || fullName.contains(".earth."))
            return "Geography & Earth";
        if (fullName.contains(".physics.") || fullName.contains(".astronomy."))
            return "Astronomy & Physics";
        if (fullName.contains(".biology."))
            return "Biology & Biochemistry";
        if (fullName.contains(".chemistry."))
            return "Chemistry";
        if (fullName.contains(".economics.") || fullName.contains(".history."))
            return "Social Sciences";
        if (fullName.contains(".device.") || fullName.contains(".nmea"))
            return "Devices & Sensors";
        if (fullName.contains(".mathematics.") || fullName.contains(".io."))
            return "Core & Mathematics";
        if (fullName.contains(".engineering."))
            return "Engineering";
        return "Other";
    }

    private Label createHeaderLabel(String text) {
        Label l = new Label(text + ":");
        l.getStyleClass().add("form-label"); // Use CSS
        return l;
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

        Label header = new Label(i18n.get("dashboard.themes.header", "Visual Theme Selection"));
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

        VBox themeSelector = new VBox(10);
        Label comboLabel = new Label(i18n.get("dashboard.themes.select", "Choose Interface Style:"));
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
        return new Tab(i18n.get("dashboard.tab.themes", "Themes"), content);
    }

    private void applyDashboardTheme(String theme) {
        System.setProperty("jscience.theme", theme);
        refreshUI();
    }

    private Tab createAppsTab(I18n i18n) {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label header = new Label(i18n.get("dashboard.apps.header", "Applications, Demos & Viewers"));
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label hint = new Label(i18n.get("dashboard.apps.discovering", "Discovering from SPI..."));
        hint.setStyle("-fx-font-style: italic; -fx-text-fill: #777;");

        Accordion accordion = new Accordion();

        // Use new SPI Discovery
        Map<DashboardDiscovery.ProviderType, Map<String, List<ViewerProvider>>> grouped = DashboardDiscovery
                .getInstance().getProvidersByType();

        List<AppEntry> apps = flattenProviders(grouped.get(DashboardDiscovery.ProviderType.APP));
        List<AppEntry> demos = flattenProviders(grouped.get(DashboardDiscovery.ProviderType.DEMO));
        List<AppEntry> viewers = flattenProviders(grouped.get(DashboardDiscovery.ProviderType.VIEWER));

        // Create panes
        TitledPane appsPane = new TitledPane(
                i18n.get("dashboard.apps.category.apps", "Scientific Applications") + " (" + apps.size() + ")",
                createAppList(true, apps.toArray(new AppEntry[0])));
        TitledPane demosPane = new TitledPane(
                i18n.get("dashboard.apps.category.demos", "Interactive Demos") + " (" + demos.size() + ")",
                createAppList(true, demos.toArray(new AppEntry[0])));
        TitledPane viewersPane = new TitledPane(
                i18n.get("dashboard.apps.category.viewers", "Data Viewers & Tools") + " (" + viewers.size() + ")",
                createAppList(true, viewers.toArray(new AppEntry[0])));

        accordion.getPanes().addAll(appsPane, demosPane, viewersPane);

        if (!apps.isEmpty())
            appsPane.setExpanded(true);
        else if (!demos.isEmpty())
            demosPane.setExpanded(true);
        else if (!viewers.isEmpty())
            viewersPane.setExpanded(true);

        int validCount = apps.size() + demos.size() + viewers.size();
        hint.setText(i18n.get("dashboard.apps.discovered", "Apps Found:") + " " + validCount);

        content.getChildren().addAll(header, hint, accordion);
        return new Tab(i18n.get("dashboard.tab.apps", "Apps"), content);
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
                    desc.getStyleClass().add("dashboard-description");
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

        Label header = new Label(i18n.get("dashboard.devices.header", "Available Devices (Simulated & JNI)"));
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Create simulated device instances
        Map<String, org.jscience.device.sim.SimulatedDevice> devices = new LinkedHashMap<>();
        devices.put("Generic GPIB Device", new org.jscience.device.sim.SimulatedGPIBDevice());
        devices.put("Generic USB Device", new org.jscience.device.sim.SimulatedUSBDevice());

        // Also discover any other devices
        List<DashboardDiscovery.ClassInfo> discoveredDevices = DashboardDiscovery.getInstance().findClasses("Device");
        for (DashboardDiscovery.ClassInfo info : discoveredDevices) {
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

        TextArea details = new TextArea(
                i18n.get("dashboard.devices.select_hint", "Select a device to view detailed properties."));
        details.setEditable(false);
        details.setWrapText(true);
        details.setStyle("-fx-font-family: 'Consolas', 'Monospace';");

        deviceList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                org.jscience.device.sim.SimulatedDevice device = devices.get(newVal);
                if (device != null) {
                    try {
                        device.connect();
                        details.setText(device.getFormattedInfo());
                    } catch (Exception e) {
                        details.setText(
                                i18n.get("dashboard.devices.error", "Error connecting to device: ") + e.getMessage());
                    }
                }
            }
        });

        split.getItems().addAll(deviceList, details);
        split.setDividerPositions(0.35);
        content.getChildren().addAll(header, split);
        VBox.setVgrow(split, Priority.ALWAYS);
        return new Tab(i18n.get("dashboard.tab.devices", "Devices"), content);
    }

    public static void main(String[] args) {
        launch(args);
    }
}


