package org.episteme.core.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.episteme.core.Episteme;
import org.episteme.core.io.UserPreferences;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.tensors.TensorProvider;
import org.episteme.core.mathematics.context.ComputeMode;
import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.mathematics.context.NumericalConfiguration;
import org.episteme.core.technical.algorithm.AlgorithmManager;
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.BackendDiscovery;
import org.episteme.core.ui.Viewer;
import org.episteme.core.ui.i18n.I18N;
import org.episteme.core.ui.ThemeManager;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Episteme Master Control - The central dashboard for the Episteme environment.
 * Provides a unified interface for system settings, library management, and application discovery.
 */
public class EpistemeMasterControl extends Application {

    private static final UserPreferences PREFS = UserPreferences.getInstance();
    private static final String PREF_SELECTED_TAB = "mastercontrol.selected_tab";
    private static final String PREF_SELECTED_DEVICE = "mastercontrol.selected_device";

    private Stage primaryStage;
    private int selectedIndex = 0;

    @Override
    public void start(Stage stage) {
        try {
            // Load persistent settings
            String lang = PREFS.getLanguage();
            org.episteme.core.ui.i18n.I18N.getInstance().setLocale(Locale.of(lang));

            this.primaryStage = stage;
            
            // Apply Episteme Application Icon universally
            try {
                stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/org/episteme/core/ui/icon.png")));
            } catch (Exception e) {}

            refreshUI();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void refreshUI() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        I18N i18n = I18N.getInstance();

        tabPane.getTabs().addAll(
                createGeneralTab(i18n),
                createI18NTab(i18n),
                createThemesTab(i18n),
                createComputingTab(i18n),
                createLibrariesTab(i18n),
                createAlgorithmsTab(i18n),
                createLoadersTab(i18n),
                createAppsTab(i18n),
                createDevicesTab(i18n));

        // Restore selected tab
        selectedIndex = PREFS.getInt(PREF_SELECTED_TAB, 0);
        if (selectedIndex < 0 || selectedIndex >= tabPane.getTabs().size()) selectedIndex = 0;
        tabPane.getSelectionModel().select(selectedIndex);

        tabPane.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) PREFS.setInt(PREF_SELECTED_TAB, newVal.intValue());
        });

        StackPane root = new StackPane(tabPane);
        if (primaryStage.getScene() == null) {
            Scene scene = new Scene(root, 1150, 800);
            primaryStage.setScene(scene);
        } else {
            primaryStage.getScene().setRoot(root);
        }
        
        ThemeManager.getInstance().applyTheme(primaryStage.getScene());
        primaryStage.setTitle(i18n.get("app.title", "Episteme Master Control"));
        
        // Add toast label to root
        primaryStage.show();

        // Initialize statusLabel if not done by previous logic
        if (this.statusLabel == null) {
            this.statusLabel = new Label();
            this.statusLabel.getStyleClass().add("status-toast");
            this.statusLabel.setVisible(false);
            this.statusLabel.setMouseTransparent(true);
            StackPane.setAlignment(this.statusLabel, Pos.BOTTOM_CENTER);
            StackPane.setMargin(this.statusLabel, new Insets(0, 0, 50, 0));
            root.getChildren().add(this.statusLabel);
        }
    }

    private VBox createTabHeader(String title, String subtitle) {
        VBox header = new VBox(5);
        header.getStyleClass().add("tab-header");
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("header-label");
        Label subLabel = new Label(subtitle);
        subLabel.getStyleClass().add("description-label");
        header.getChildren().addAll(titleLabel, subLabel);
        return header;
    }

    private void addPropertyRow(GridPane grid, int row, String labelText, Node control, String description) {
        Label label = new Label(labelText);
        label.getStyleClass().add("font-bold");
        label.setPadding(new Insets(5, 0, 5, 0));
        
        Label desc = new Label(description);
        desc.getStyleClass().add("description-label");
        desc.setMaxWidth(450);

        grid.add(label, 0, row);
        grid.add(control, 1, row);
        grid.add(desc, 2, row);
    }

    private Tab createGeneralTab(I18N i18n) {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        content.setAlignment(Pos.TOP_LEFT);

        VBox header = createTabHeader(
            i18n.get("mastercontrol.general.title", "Episteme Master Control"),
            i18n.get("mastercontrol.general.subtitle", "Universal Scientific Computing Environment & Control Panel")
        );

        // --- PROJECT LOGO & INFO ---
        HBox infoBox = new HBox(30);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.setPadding(new Insets(20, 0, 20, 0));

        javafx.scene.image.ImageView projectIcon = new javafx.scene.image.ImageView();
        try {
            javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("/org/episteme/core/ui/master_control_logo.png"));
            projectIcon.setImage(icon);
            projectIcon.setFitWidth(96);
            projectIcon.setPreserveRatio(true);
        } catch (Exception e) {}

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(20);
        infoGrid.setVgap(10);

        addInfoRow(infoGrid, 0, i18n.get("mastercontrol.general.version", "Version"), Episteme.VERSION);
        addInfoRow(infoGrid, 1, i18n.get("mastercontrol.general.build", "Build Date"), Episteme.BUILD_DATE);
        addInfoRow(infoGrid, 2, i18n.get("mastercontrol.general.java", "Java Version"), System.getProperty("java.version"));
        addInfoRow(infoGrid, 3, i18n.get("mastercontrol.general.authors", "Authors"), String.join(", ", Episteme.AUTHORS));

        infoBox.getChildren().addAll(projectIcon, infoGrid);

        Label status = new Label(i18n.get("mastercontrol.general.status.ok", "System Status: Operational"));
        status.getStyleClass().add("status-label-available");

        content.getChildren().addAll(header, infoBox, status);
        return new Tab(i18n.get("mastercontrol.tab.general", "General"), content);
    }

    private void addInfoRow(GridPane grid, int row, String label, String value) {
        Label l = new Label(label + ":");
        l.getStyleClass().add("font-bold");
        Label v = new Label(value);
        grid.add(l, 0, row);
        grid.add(v, 1, row);
    }

    private Tab createI18NTab(I18N i18n) {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));

        VBox header = createTabHeader(
            i18n.get("mastercontrol.tab.i18n", "Language & Region"),
            i18n.get("mastercontrol.i18n.desc", "Configure the global interface language and numerical formatting conventions.")
        );

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(25);

        ComboBox<LocaleItem> langCombo = new ComboBox<>();
        langCombo.getItems().addAll(
                new LocaleItem(i18n.get("mastercontrol.lang.en", "English"), Locale.US),
                new LocaleItem(i18n.get("mastercontrol.lang.fr", "Français"), Locale.FRANCE),
                new LocaleItem(i18n.get("mastercontrol.lang.de", "Deutsch"), Locale.GERMANY),
                new LocaleItem(i18n.get("mastercontrol.lang.es", "Español"), Locale.forLanguageTag("es-ES")),
                new LocaleItem(i18n.get("mastercontrol.lang.it", "Italiano"), Locale.ITALY),
                new LocaleItem(i18n.get("mastercontrol.lang.zh", "中文"), Locale.CHINA));

        Locale currentLocale = i18n.getLocale();
        for (LocaleItem item : langCombo.getItems()) {
            if (item.locale.getLanguage().equalsIgnoreCase(currentLocale.getLanguage())) {
                langCombo.setValue(item);
                break;
            }
        }

        langCombo.setOnAction(e -> {
            LocaleItem selected = langCombo.getValue();
            if (selected != null) {
                PREFS.setLanguage(selected.locale.getLanguage());
                i18n.setLocale(selected.locale);
                notifySaved(i18n);
                refreshUI();
            }
        });

        addPropertyRow(grid, 0, 
            i18n.get("mastercontrol.i18n.language", "Interface Language"), 
            langCombo, 
            i18n.get("mastercontrol.i18n.language.desc", "Select the primary language for the dashboard and all scientific viewers."));

        content.getChildren().addAll(header, grid);
        return new Tab(i18n.get("mastercontrol.tab.i18n", "I18N"), content);
    }

    private Tab createThemesTab(I18N i18n) {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));

        VBox header = createTabHeader(
            i18n.get("mastercontrol.tab.themes", "Appearance & Themes"),
            i18n.get("mastercontrol.themes.header.desc", "Customize the visual appearance of the Episteme environment.")
        );

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(25);

        ComboBox<String> themeCombo = new ComboBox<>();
        themeCombo.getItems().addAll("Modena", "Caspian", "High Contrast", "Dark");
        String currentTheme = ThemeManager.getInstance().getCurrentTheme();
        if ("HighContrast".equals(currentTheme)) currentTheme = "High Contrast";
        themeCombo.setValue(currentTheme);
        themeCombo.setPrefWidth(200);

        themeCombo.setOnAction(e -> {
            String selected = themeCombo.getValue().replace(" ", "");
            ThemeManager.getInstance().setTheme(selected);
            ThemeManager.getInstance().applyTheme(primaryStage.getScene());
            notifySaved(i18n);
        });

        addPropertyRow(grid, 0, 
            i18n.get("mastercontrol.themes.select", "Visual Style"), 
            themeCombo, 
            i18n.get("mastercontrol.themes.select.desc", "Select the global theme for the application. 'Dark' is recommended for high-performance computing."));

        VBox previewBox = new VBox(15);
        previewBox.setPadding(new Insets(20, 0, 0, 0));
        Label previewLabel = new Label(i18n.get("mastercontrol.themes.preview", "Theme Preview:"));
        previewLabel.getStyleClass().add("font-bold");
        
        HBox samples = new HBox(15);
        samples.setAlignment(Pos.CENTER_LEFT);
        Button sampleBtn = new Button(i18n.get("mastercontrol.themes.sample.button", "Sample Button"));
        CheckBox sampleCb = new CheckBox(i18n.get("mastercontrol.themes.sample.checkbox", "Sample CheckBox"));
        ProgressBar samplePb = new ProgressBar(0.6);
        samples.getChildren().addAll(sampleBtn, sampleCb, samplePb);
        
        previewBox.getChildren().addAll(previewLabel, samples);

        content.getChildren().addAll(header, grid, new Separator(), previewBox);
        return new Tab(i18n.get("mastercontrol.tab.themes", "Themes"), content);
    }

    private Tab createComputingTab(I18N i18n) {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("transparent-scroll");

        VBox content = new VBox(25);
        content.setPadding(new Insets(30));

        VBox header = createTabHeader(
            i18n.get("mastercontrol.tab.computing", "Computing & Numerical Engine"),
            i18n.get("mastercontrol.computing.header.desc", "Configure the global mathematics context, hardware acceleration, and precision thresholds.")
        );

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(20);

        NumericalConfiguration config = Episteme.getNumericalConfiguration();

        // --- SECTION 1: GLOBAL CONTEXT ---
        Label ctxLabel = new Label(i18n.get("mastercontrol.computing.section.context", "Global Computation Context"));
        ctxLabel.getStyleClass().add("font-bold");
        grid.add(ctxLabel, 0, 0, 3, 1);

        // Precision Mode
        ComboBox<MathContext.RealPrecision> precCombo = new ComboBox<>();
        precCombo.getItems().addAll(MathContext.RealPrecision.values());
        precCombo.setValue(config.getRealPrecision());
        precCombo.setOnAction(e -> config.setRealPrecision(precCombo.getValue()));
        addPropertyRow(grid, 1, i18n.get("mastercontrol.computing.precision", "Precision Mode"), precCombo, 
            i18n.get("mastercontrol.computing.precision.desc", "Determines the data types used for real numbers (float, double, or arbitrary precision)."));

        // Overflow Mode
        ComboBox<MathContext.OverflowMode> overflowCombo = new ComboBox<>();
        overflowCombo.getItems().addAll(MathContext.OverflowMode.values());
        overflowCombo.setValue(config.getOverflowMode());
        overflowCombo.setOnAction(e -> config.setOverflowMode(overflowCombo.getValue()));
        addPropertyRow(grid, 2, i18n.get("mastercontrol.computing.overflow", "Overflow Mode"), overflowCombo, 
            i18n.get("mastercontrol.computing.overflow.desc", "Controls how the engine handles numerical overflows (SAFE checks every op, UNSAFE is faster)."));

        // Compute Device (AUTO, CPU, OPENCL, CUDA)
        ComboBox<String> deviceCombo = new ComboBox<>();
        deviceCombo.getItems().addAll("AUTO", "CPU", "OPENCL", "CUDA");
        deviceCombo.setValue(config.getComputeMode().name());
        deviceCombo.setOnAction(e -> {
            try {
                config.applyComputeMode(ComputeMode.valueOf(deviceCombo.getValue()));
            } catch (Exception ex) {}
        });
        addPropertyRow(grid, 3, i18n.get("mastercontrol.computing.device", "Compute Device"), deviceCombo, 
            i18n.get("mastercontrol.computing.device.desc", "Select the hardware used for intensive calculations. 'AUTO' selects the fastest available GPU."));

        // --- SECTION 2: HIGH PRECISION (EXACT) ---
        grid.add(new Separator(), 0, 4, 3, 1);
        Label hpLabel = new Label(i18n.get("mastercontrol.computing.section.exact", "Arbitrary Precision (EXACT Mode)"));
        hpLabel.getStyleClass().add("font-bold");
        grid.add(hpLabel, 0, 5, 3, 1);

        Spinner<Integer> digitsSpinner = new Spinner<>(1, 10000, config.getMathContext().getPrecision());
        digitsSpinner.setEditable(true);
        digitsSpinner.valueProperty().addListener((obs, old, val) -> {
            config.setMathContext(new java.math.MathContext(val, config.getMathContext().getRoundingMode()));
        });
        addPropertyRow(grid, 6, i18n.get("mastercontrol.computing.precision_digits", "Precision Digits"), digitsSpinner, 
            i18n.get("mastercontrol.computing.precision_digits.desc", "Number of decimal digits to maintain when using EXACT mode."));

        ComboBox<java.math.RoundingMode> roundingCombo = new ComboBox<>();
        roundingCombo.getItems().addAll(java.math.RoundingMode.values());
        roundingCombo.setValue(config.getMathContext().getRoundingMode());
        roundingCombo.setOnAction(e -> {
            config.setMathContext(new java.math.MathContext(config.getMathContext().getPrecision(), roundingCombo.getValue()));
            notifySaved(i18n);
        });
        addPropertyRow(grid, 7, i18n.get("mastercontrol.computing.rounding", "Rounding Mode"), roundingCombo, 
            i18n.get("mastercontrol.computing.rounding.desc", "Strategy for rounding numbers when precision is lost."));

        // --- SECTION 3: BACKEND SELECTORS ---
        grid.add(new Separator(), 0, 8, 3, 1);
        Label backendLabel = new Label(i18n.get("mastercontrol.computing.section.backends", "Pluggable Backend Engines"));
        backendLabel.getStyleClass().add("font-bold");
        grid.add(backendLabel, 0, 9, 3, 1);

        ComboBox<String> mathCombo = createBackendComboBox(BackendDiscovery.TYPE_MATH, Episteme.getMathBackendId(), id -> {
            Episteme.setMathBackendId(id);
            Episteme.savePreferences();
        });
        addPropertyRow(grid, 10, i18n.get("mastercontrol.math.backend", "Mathematics Engine"), mathCombo, 
            i18n.get("mastercontrol.math.backend.desc", "Primary engine for linear algebra and algorithm execution."));

        ComboBox<String> plot2DCombo = createBackendComboBox(BackendDiscovery.TYPE_PLOTTING, PREFS.getPreferredBackend("plotting2d"), id -> {
            PREFS.setPreferredBackend("plotting2d", id);
            notifySaved(i18n);
        });
        addPropertyRow(grid, 11, i18n.get("mastercontrol.plotting.backend_2d", "2D Visualization"), plot2DCombo, 
            i18n.get("mastercontrol.plotting.backend_2d.desc", "Engine for rendering 2D charts and data plots."));

        ComboBox<String> plot3DCombo = createBackendComboBox(BackendDiscovery.TYPE_PLOTTING, PREFS.getPreferredBackend("plotting3d"), id -> {
            PREFS.setPreferredBackend("plotting3d", id);
            notifySaved(i18n);
        });
        addPropertyRow(grid, 12, i18n.get("mastercontrol.plotting.backend_3d", "3D Visualization"), plot3DCombo, 
            i18n.get("mastercontrol.plotting.backend_3d.desc", "Engine for rendering complex 3D surfaces and volumes."));

        // --- SECTION 4: LINEAR ALGEBRA THRESHOLDS ---
        grid.add(new Separator(), 0, 13, 3, 1);
        Label laLabel = new Label(i18n.get("mastercontrol.computing.section.la", "Linear Algebra & Solver Parameters"));
        laLabel.getStyleClass().add("font-bold");
        grid.add(laLabel, 0, 14, 3, 1);

        TextField epsilonField = new TextField(String.valueOf(config.getEpsilonDouble()));
        epsilonField.setOnAction(e -> {
            try { 
                config.setEpsilonDouble(Double.parseDouble(epsilonField.getText())); 
                notifySaved(i18n);
            } catch (Exception ex) {}
        });
        addPropertyRow(grid, 15, i18n.get("mastercontrol.computing.epsilon", "LA Epsilon"), epsilonField, 
            i18n.get("mastercontrol.computing.epsilon.desc", "The threshold below which a number is considered zero in linear algebra operations."));

        Spinner<Integer> iterSpinner = new Spinner<>(1, 100000, config.getMaxIterations());
        iterSpinner.valueProperty().addListener((obs, old, val) -> {
            config.setMaxIterations(val);
            notifySaved(i18n);
        });
        addPropertyRow(grid, 16, i18n.get("mastercontrol.computing.iterations", "Max Iterations"), iterSpinner, 
            i18n.get("mastercontrol.computing.iterations.desc", "Maximum number of iterations allowed for iterative solvers (e.g., GMRES, BiCGSTAB)."));

        Spinner<Integer> bitsSpinner = new Spinner<>(64, 4096, config.getPrecisionBits());
        bitsSpinner.valueProperty().addListener((obs, old, val) -> {
            config.setPrecisionBits(val);
            notifySaved(i18n);
        });
        addPropertyRow(grid, 17, i18n.get("mastercontrol.computing.bits", "Internal Precision (Bits)"), bitsSpinner, 
            i18n.get("mastercontrol.computing.bits.desc", "Bit-width for internal calculations in native high-precision backends."));

        content.getChildren().addAll(header, grid);
        scroll.setContent(content);
        return new Tab(i18n.get("mastercontrol.tab.computing", "Computing"), scroll);
    }

    private ComboBox<String> createBackendComboBox(String type, String currentId, java.util.function.Consumer<String> onSelect) {
        ComboBox<String> combo = new ComboBox<>();
        List<Backend> providers = BackendDiscovery.getInstance().getProvidersByType(type);
        
        java.util.Map<String, String> nameToId = new java.util.LinkedHashMap<>();
        nameToId.put("AUTO", null);
        for (Backend p : providers) nameToId.put(p.getName(), p.getId());
        
        combo.getItems().addAll(nameToId.keySet());
        
        String currentName = "AUTO";
        for (var entry : nameToId.entrySet()) {
            if (java.util.Objects.equals(entry.getValue(), currentId)) {
                currentName = entry.getKey();
                break;
            }
        }
        combo.setValue(currentName);
        combo.setOnAction(e -> onSelect.accept(nameToId.get(combo.getValue())));
        combo.setPrefWidth(250);
        return combo;
    }

    private Tab createLibrariesTab(I18N i18n) {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("transparent-scroll");

        VBox header = createTabHeader(
            i18n.get("mastercontrol.tab.libraries", "Project Libraries"),
            i18n.get("mastercontrol.libraries.header", "Available Libraries & Status")
        );

        Label explainText = new Label(i18n.get("mastercontrol.libraries.explain.text", 
            "Episteme is a modular ecosystem. These libraries are listed because they provide core functionality or optional hardware acceleration."));
        explainText.setWrapText(true);
        explainText.setOpacity(0.8);

        content.getChildren().addAll(header, explainText);

        // --- NEW: FRAMEWORK & STANDARDS ---
        content.getChildren().add(createManualLibraryCategory(i18n, 
            i18n.get("mastercontrol.libraries.cat.framework", "Framework Libraries"),
            new String[][] {
                {"lib.javalin.name", "io.javalin.Javalin", "lib.javalin.desc"},
                {"lib.jackson.name", "com.fasterxml.jackson.databind.ObjectMapper", "lib.jackson.desc"},
                {"lib.slf4j.name", "org.slf4j.Logger", "lib.slf4j.desc"},
                {"lib.grpc.name", "io.grpc.ManagedChannel", "lib.grpc.desc"}
            }
        ));
        content.getChildren().add(new Separator());

        content.getChildren().add(createManualLibraryCategory(i18n, 
            i18n.get("mastercontrol.libraries.cat.standards", "Standards"),
            new String[][] {
                {"lib.jsr385.name", "javax.measure.Unit", "lib.jsr385.desc"},
                {"lib.indriya.name", "tech.units.indriya.format.SimpleUnitFormat", "lib.indriya.desc"},
                {"lib.graphstream.name", "org.graphstream.graph.Graph", "lib.graphstream.desc"},
                {"lib.jgrapht.name", "org.jgrapht.Graph", "lib.jgrapht.desc"},
                {"lib.javacv.name", "org.bytedeco.javacv.FFmpegFrameGrabber", "lib.javacv.desc"}
            }
        ));
        content.getChildren().add(new Separator());

        // --- SPI Categories ---
        String[] types = {
            BackendDiscovery.TYPE_MATH, BackendDiscovery.TYPE_LINEAR_ALGEBRA, BackendDiscovery.TYPE_TENSOR,
            BackendDiscovery.TYPE_PLOTTING, BackendDiscovery.TYPE_AUDIO, 
            BackendDiscovery.TYPE_MOLECULAR, BackendDiscovery.TYPE_QUANTUM, BackendDiscovery.TYPE_NETWORK,
            BackendDiscovery.TYPE_ML, BackendDiscovery.TYPE_VISION, BackendDiscovery.TYPE_VIDEO,
            BackendDiscovery.TYPE_DISTRIBUTED, BackendDiscovery.TYPE_GRAPH, BackendDiscovery.TYPE_MAP
        };
        String[] labels = {
            i18n.get("category.math", "Mathematics"),
            i18n.get("category.la", "Linear Algebra"),
            i18n.get("category.tensor", "Tensor Computing"),
            i18n.get("category.plotting", "Visualization"),
            i18n.get("category.audio", "Audio Processing"),
            i18n.get("category.molecular", "Molecular Viewing"),
            i18n.get("category.quantum", "Quantum Computing"),
            i18n.get("category.network", "Network Analysis"),
            i18n.get("category.ml", "Machine Learning"),
            i18n.get("category.vision", "Computer Vision"),
            i18n.get("category.video", "Video Processing"),
            i18n.get("category.distributed", "Distributed Computing"),
            i18n.get("category.graph", "Graph Analysis"),
            i18n.get("category.map", "Geospatial Maps")
        };
        
        boolean first = true;
        for (int i = 0; i < types.length; i++) {
            // Check visibility using both SPI and Scanned results
            boolean hasProviders = !BackendDiscovery.getInstance().getProvidersByType(types[i]).isEmpty();
            if (!hasProviders) {
                List<MasterControlDiscovery.ClassInfo> scanned = MasterControlDiscovery.getInstance().findClasses("Backend");
                for (MasterControlDiscovery.ClassInfo info : scanned) {
                    try {
                        Class<?> cls = Class.forName(info.fullName, false, MasterControlDiscovery.class.getClassLoader());
                        if (Backend.class.isAssignableFrom(cls)) {
                            Backend instance = (Backend) cls.getDeclaredConstructor().newInstance();
                            if (instance.getType() != null && instance.getType().equalsIgnoreCase(types[i])) {
                                hasProviders = true;
                                break;
                            }
                        }
                    } catch (Exception e) {}
                }
            }

            if (hasProviders) {
                if (!first) content.getChildren().add(new Separator());
                content.getChildren().add(createBackendCategory(i18n, types[i], labels[i], ""));
                first = false;
            }
        }

        return new Tab(i18n.get("mastercontrol.tab.libraries", "Libraries"), scroll);
    }

    private VBox createManualLibraryCategory(I18N i18n, String title, String[][] libs) {
        VBox cat = new VBox(0); // Use 0 spacing for stripes
        Label titleLbl = new Label(title);
        titleLbl.getStyleClass().add("font-bold");
        titleLbl.setStyle("-fx-padding: 0 0 10 0;");
        cat.getChildren().add(titleLbl);

        for (int i = 0; i < libs.length; i++) {
            String[] lib = libs[i];
            boolean avail = false;
            try { Class.forName(lib[1]); avail = true; } catch (Exception e) {}
            
            String name = i18n.get(lib[0], lib[0].replace("lib.", "").replace(".name", ""));
            String desc = lib.length > 2 ? i18n.get(lib[2], "") : "";
            
            HBox row = createManualLibraryRow(i18n, name, avail, desc, i % 2 == 1);
            cat.getChildren().add(row);
        }
        return cat;
    }

    private HBox createManualLibraryRow(I18N i18n, String nameStr, boolean available, String descStr, boolean striped) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 15, 10, 15));
        if (striped) {
            row.setStyle("-fx-background-color: rgba(0,0,0,0.03); -fx-background-radius: 5;");
        }

        Label name = new Label(nameStr);
        name.setPrefWidth(250);
        name.getStyleClass().add("font-medium");

        Label status = new Label(available ? i18n.get("status.available", "AVAILABLE") : i18n.get("status.missing", "NOT FOUND"));
        status.setPrefWidth(120);
        status.getStyleClass().add(available ? "status-label-available" : "status-label-unavailable");
        status.setAlignment(Pos.CENTER);

        Label desc = new Label(descStr);
        desc.setOpacity(0.7);
        desc.setWrapText(true);
        HBox.setHgrow(desc, Priority.ALWAYS);

        row.getChildren().addAll(name, status, desc);
        return row;
    }

    private Tab createAlgorithmsTab(I18N i18n) {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox header = createTabHeader(
            i18n.get("mastercontrol.tab.algorithms", "Computational Algorithms"),
            i18n.get("mastercontrol.algorithms.desc", "Explore and configure modular compute engines available in the Episteme environment.")
        );

        Accordion accordion = new Accordion();
        
        // 1. High-level Algorithm Providers
        Map<String, Class<? extends AlgorithmProvider>> algoTypes = new LinkedHashMap<>();
        algoTypes.put(i18n.get("category.la", "Linear Algebra"), LinearAlgebraProvider.class);
        algoTypes.put(i18n.get("category.tensor", "Tensor Computing"), TensorProvider.class);
        
        // Add more types if they exist on classpath
        try { algoTypes.put(i18n.get("category.ml", "Machine Learning"), (Class<? extends AlgorithmProvider>) Class.forName("org.episteme.core.mathematics.ml.MLProvider")); } catch (Exception e) {}
        try { algoTypes.put(i18n.get("category.vision", "Computer Vision"), (Class<? extends AlgorithmProvider>) Class.forName("org.episteme.core.media.vision.VisionAlgorithmProvider")); } catch (Exception e) {}

        for (Map.Entry<String, Class<? extends AlgorithmProvider>> entry : algoTypes.entrySet()) {
            VBox catBox = createAlgorithmCategory(i18n, entry.getKey(), entry.getValue());
            if (!catBox.getChildren().isEmpty()) {
                TitledPane pane = new TitledPane(entry.getKey(), catBox);
                accordion.getPanes().add(pane);
            }
        }

        // 2. Low-level Backends (Legacy/Engines)
        String[] backendTypes = {
            BackendDiscovery.TYPE_MATH, BackendDiscovery.TYPE_AUDIO,
            BackendDiscovery.TYPE_DISTRIBUTED, BackendDiscovery.TYPE_GRAPH, BackendDiscovery.TYPE_MAP
        };
        String[] backendLabels = {
            i18n.get("category.math", "Mathematics"),
            i18n.get("category.audio", "Audio Processing"),
            i18n.get("category.distributed", "Distributed Systems"),
            i18n.get("category.graph", "Graph Analysis"),
            i18n.get("category.map", "Geospatial Analysis")
        };

        for (int i = 0; i < backendTypes.length; i++) {
            VBox catBox = createBackendListCategory(i18n, backendTypes[i]);
            if (!catBox.getChildren().isEmpty()) {
                TitledPane pane = new TitledPane(backendLabels[i], catBox);
                accordion.getPanes().add(pane);
            }
        }

        if (!accordion.getPanes().isEmpty()) accordion.getPanes().get(0).setExpanded(true);

        content.getChildren().addAll(header, accordion);
        return new Tab(i18n.get("mastercontrol.tab.algorithms", "Algorithms"), scroll);
    }

    private VBox createAlgorithmCategory(I18N i18n, String title, Class<? extends AlgorithmProvider> type) {
        VBox box = new VBox(0);
        List<? extends AlgorithmProvider> providers = AlgorithmManager.getProviders(type);
        if (providers.isEmpty()) return box;

        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 15, 10, 15));
        header.getStyleClass().add("table-header");
        
        Label hName = new Label(i18n.get("mastercontrol.algorithms.col.name", "Algorithm Engine"));
        hName.setPrefWidth(250);
        hName.getStyleClass().add("font-bold");
        
        Label hPriority = new Label(i18n.get("mastercontrol.algorithms.col.priority", "Priority"));
        hPriority.setPrefWidth(120);
        hPriority.getStyleClass().add("font-bold");
        hPriority.setAlignment(Pos.CENTER);
        
        Label hDesc = new Label(i18n.get("mastercontrol.algorithms.col.description", "Description"));
        hDesc.getStyleClass().add("font-bold");
        HBox.setHgrow(hDesc, Priority.ALWAYS);
        
        header.getChildren().addAll(hName, hPriority, hDesc);
        box.getChildren().add(header);

        for (int i = 0; i < providers.size(); i++) {
            box.getChildren().add(createAlgorithmRow(i18n, providers.get(i), i % 2 == 1));
        }
        return box;
    }

    private HBox createAlgorithmRow(I18N i18n, AlgorithmProvider p, boolean striped) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 15, 10, 15));
        if (striped) {
            row.setStyle("-fx-background-color: rgba(0,0,0,0.03); -fx-background-radius: 5;");
        }

        Label name = new Label(p.getName());
        name.setPrefWidth(250);
        name.getStyleClass().add("font-medium");

        Label priority = new Label(p.getPriority() > 0 ? String.valueOf(p.getPriority()) : "NA");
        priority.setPrefWidth(120);
        priority.getStyleClass().add("font-mono");
        priority.setAlignment(Pos.CENTER);

        Label desc = new Label(p.description());
        desc.setOpacity(0.7);
        desc.setWrapText(true);
        HBox.setHgrow(desc, Priority.ALWAYS);

        row.getChildren().addAll(name, priority, desc);
        return row;
    }

    private VBox createBackendListCategory(I18N i18n, String type) {
        VBox box = new VBox(0);
        List<Backend> providers = new ArrayList<>(BackendDiscovery.getInstance().getProvidersByType(type));
        
        // Add discovered via scanning
        List<MasterControlDiscovery.ClassInfo> discovered = MasterControlDiscovery.getInstance().findClasses("Backend");
        for (MasterControlDiscovery.ClassInfo info : discovered) {
            if (providers.stream().noneMatch(p -> p.getClass().getName().equals(info.fullName))) {
                try {
                    Class<?> cls = Class.forName(info.fullName, false, MasterControlDiscovery.class.getClassLoader());
                    if (Backend.class.isAssignableFrom(cls)) {
                        Backend instance = (Backend) cls.getDeclaredConstructor().newInstance();
                        if (instance.getType().equalsIgnoreCase(type)) providers.add(instance);
                    }
                } catch (Exception e) {}
            }
        }

        for (int i = 0; i < providers.size(); i++) {
            box.getChildren().add(createBackendRow(i18n, providers.get(i), i % 2 == 1, false));
        }
        return box;
    }

    private HBox createBackendRow(I18N i18n, Backend b, boolean striped, boolean showCheckbox) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 15, 10, 15));
        if (striped) {
            row.setStyle("-fx-background-color: rgba(0,0,0,0.03); -fx-background-radius: 5;");
        }

        if (showCheckbox) {
            CheckBox activeCb = new CheckBox();
            activeCb.setSelected(!UserPreferences.getInstance().isBackendDeactivated(b.getId()));
            activeCb.setOnAction(e -> {
                UserPreferences.getInstance().setBackendDeactivated(b.getId(), !activeCb.isSelected());
                notifySaved(i18n);
            });
            activeCb.setPadding(new Insets(0, 10, 0, 0));
            row.getChildren().add(activeCb);
        }

        Label name = new Label(b.getName());
        name.setPrefWidth(250);
        name.getStyleClass().add("font-medium");

        Label priority = new Label(String.valueOf(b.getPriority()));
        priority.setPrefWidth(120);
        priority.getStyleClass().add(b.isAvailable() ? "status-label-available" : "status-label-unavailable");
        priority.setAlignment(Pos.CENTER);

        Label desc = new Label(b.getDescription());
        desc.setOpacity(0.7);
        desc.setWrapText(true);
        HBox.setHgrow(desc, Priority.ALWAYS);

        row.getChildren().addAll(name, priority, desc);
        return row;
    }



    private VBox createBackendCategory(I18N i18n, String type, String title, String description) {
        VBox cat = new VBox(0);
        Label titleLbl = new Label(title);
        titleLbl.getStyleClass().add("font-bold");
        titleLbl.setStyle("-fx-padding: 0 0 10 0;");
        cat.getChildren().add(titleLbl);

        HBox tableHeader = new HBox(15);
        tableHeader.setAlignment(Pos.CENTER_LEFT);
        tableHeader.setPadding(new Insets(10, 15, 10, 15));
        tableHeader.getStyleClass().add("table-header");
        
        Label hName = new Label(i18n.get("mastercontrol.libraries.col.name", "Library / Backend"));
        hName.setPrefWidth(250);
        hName.getStyleClass().add("font-bold");
        
        Label hPriority = new Label(i18n.get("mastercontrol.libraries.col.priority", "Priority"));
        hPriority.setPrefWidth(120);
        hPriority.getStyleClass().add("font-bold");
        hPriority.setAlignment(Pos.CENTER);
        
        Label hDesc = new Label(i18n.get("mastercontrol.libraries.col.description", "Description"));
        hDesc.getStyleClass().add("font-bold");
        HBox.setHgrow(hDesc, Priority.ALWAYS);
        
        tableHeader.getChildren().addAll(hName, hPriority, hDesc);
        cat.getChildren().add(tableHeader);
        
        List<Backend> providers = BackendDiscovery.getInstance().getProvidersByType(type);
        for (int i = 0; i < providers.size(); i++) {
            cat.getChildren().add(createBackendRow(i18n, providers.get(i), i % 2 == 1, true));
        }
        return cat;
    }


    private Tab createLoadersTab(I18N i18n) {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));

        VBox header = createTabHeader(
            i18n.get("mastercontrol.loaders.header", "Data Loaders & Formats"),
            i18n.get("mastercontrol.loaders.desc", "Support for specialized scientific data formats and resource protocols.")
        );

        Accordion accordion = new Accordion();
        
        // Combine SPI discovered loaders and class-scanned loaders
        Map<String, List<AppEntry>> grouped = new TreeMap<>();
        
        // 1. SPI Loaders (ResourceReader/Writer)
        try {
            java.util.ServiceLoader.load(org.episteme.core.io.ResourceReader.class).forEach(r -> {
                String cat = r.getCategory();
                grouped.computeIfAbsent(cat, k -> new ArrayList<>()).add(new AppEntry(i18n.get(r.getName(), r.getName()), r.getClass().getName(), i18n.get(r.getDescription(), r.getDescription())));
            });
            java.util.ServiceLoader.load(org.episteme.core.io.ResourceWriter.class).forEach(w -> {
                String cat = w.getCategory();
                grouped.computeIfAbsent(cat, k -> new ArrayList<>()).add(new AppEntry(i18n.get(w.getName(), w.getName()), w.getClass().getName(), i18n.get(w.getDescription(), w.getDescription())));
            });
        } catch (Exception e) {}

        // 2. Scanned Loaders (Legacy/Discovery)
        List<MasterControlDiscovery.ClassInfo> discovered = MasterControlDiscovery.getInstance().findClasses("Loader");
        for (MasterControlDiscovery.ClassInfo info : discovered) {
            boolean exists = false;
            for (List<AppEntry> list : grouped.values()) {
                for (AppEntry e : list) if (e.className.equals(info.fullName)) { exists = true; break; }
            }
            if (!exists) {
                String catName = "Scientific Systems";
                if (info.fullName.contains(".chemistry.")) catName = "Chemistry";
                else if (info.fullName.contains(".physics.")) catName = "Physics";
                else if (info.fullName.contains(".biology.")) catName = "Biology";
                else if (info.fullName.contains(".mathematics.")) catName = "Mathematics";
                else if (info.fullName.contains(".geography.")) catName = "Geography";
                else if (info.fullName.contains(".social.")) catName = "Social Sciences";
                else if (info.fullName.contains(".native.")) catName = "Native Systems";
                
                String cat = i18n.get("category." + catName.toLowerCase().replace(" ", ""), catName);
                grouped.computeIfAbsent(cat, k -> new ArrayList<>()).add(new AppEntry(info.simpleName, info.fullName, info.description));
            }
        }

        for (Map.Entry<String, List<AppEntry>> entry : grouped.entrySet()) {
            accordion.getPanes().add(new TitledPane("(" + entry.getValue().size() + ") " + entry.getKey(), createAppList(false, entry.getValue().toArray(new AppEntry[0]))));
        }

        content.getChildren().addAll(header, accordion);
        return new Tab(i18n.get("mastercontrol.tab.loaders", "Loaders"), content);
    }

    private Tab createAppsTab(I18N i18n) {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));

        VBox header = createTabHeader(
            i18n.get("mastercontrol.apps.header", "Applications & Demos"),
            i18n.get("mastercontrol.apps.desc", "Explore and launch integrated scientific applications and interactive demonstrations.")
        );

        Accordion accordion = new Accordion();
        
        Map<MasterControlDiscovery.ProviderType, Map<String, List<Viewer>>> grouped = MasterControlDiscovery.getInstance().getProvidersByType();
        
        addAppPane(accordion, i18n, grouped, MasterControlDiscovery.ProviderType.APP, "mastercontrol.apps.category.apps", "Applications");
        addAppPane(accordion, i18n, grouped, MasterControlDiscovery.ProviderType.DISTRIBUTED_APP, "mastercontrol.apps.category.distributed", "Distributed Applications");
        addAppPane(accordion, i18n, grouped, MasterControlDiscovery.ProviderType.DEMO, "mastercontrol.apps.category.demos", "Demos");
        addAppPane(accordion, i18n, grouped, MasterControlDiscovery.ProviderType.VIEWER, "mastercontrol.apps.category.viewers", "Viewers");

        if (!accordion.getPanes().isEmpty()) accordion.getPanes().get(0).setExpanded(true);

        content.getChildren().addAll(header, accordion);
        return new Tab(i18n.get("mastercontrol.tab.apps", "Apps"), content);
    }

    private void addAppPane(Accordion accordion, I18N i18n, Map<MasterControlDiscovery.ProviderType, Map<String, List<Viewer>>> grouped, 
                             MasterControlDiscovery.ProviderType type, String key, String defaultTitle) {
        Map<String, List<Viewer>> typeMap = grouped.get(type);
        if (typeMap == null || typeMap.isEmpty()) return;

        int totalCount = typeMap.values().stream().mapToInt(List::size).sum();
        VBox container = new VBox(15);
        
        for (Map.Entry<String, List<Viewer>> entry : typeMap.entrySet()) {
            String catName = entry.getKey();
            List<Viewer> viewers = entry.getValue();
            
            VBox catBox = new VBox(5);
            Label catLabel = new Label(catName);
            catLabel.getStyleClass().add("font-bold");
            catLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5 0 5 0;");
            
            AppEntry[] entries = viewers.stream()
                .map(v -> new AppEntry(v.getName(), v.getClass().getName(), v.getDescription()))
                .toArray(AppEntry[]::new);
                
            catBox.getChildren().addAll(catLabel, createAppList(true, entries));
            container.getChildren().add(catBox);
        }

        accordion.getPanes().add(new TitledPane("(" + totalCount + ") " + i18n.get(key, defaultTitle), container));
    }

    private AppEntry[] convert(List<MasterControlDiscovery.ClassInfo> infos) {
        return infos.stream().map(i -> new AppEntry(i.simpleName, i.fullName, i.description)).toArray(AppEntry[]::new);
    }

    private VBox createAppList(boolean launch, AppEntry[] entries) {
        VBox box = new VBox(0);
        for (int i = 0; i < entries.length; i++) {
            box.getChildren().add(createAppRow(entries[i], i % 2 == 1, launch));
        }
        return box;
    }

    private HBox createAppRow(AppEntry app, boolean striped, boolean showLaunch) {
        HBox row = new HBox(15);
        row.getStyleClass().add("app-row");
        if (striped) row.getStyleClass().add("app-row-striped");

        Label name = new Label(app.name);
        name.setPrefWidth(250);
        name.getStyleClass().add("font-medium");

        boolean available = false;
        try { Class.forName(app.className); available = true; } catch (Exception e) {}

        Label status = new Label(available ? I18N.getInstance().get("status.available", "AVAILABLE") : I18N.getInstance().get("status.missing", "MISSING"));
        status.setPrefWidth(120);
        status.getStyleClass().add(available ? "status-label-available" : "status-label-unavailable");
        status.setAlignment(Pos.CENTER);

        Label desc = new Label(app.description);
        desc.setOpacity(0.7);
        desc.setWrapText(true);
        HBox.setHgrow(desc, Priority.ALWAYS);

        row.getChildren().addAll(name, status, desc);

        if (available) {
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    launchApp(app.className);
                }
            });
            row.setCursor(javafx.scene.Cursor.HAND);
        }

        return row;
    }

    private void launchApp(String className) {
        try {
            Class<?> cls = Class.forName(className);
            Stage stage = new Stage();
            if (Application.class.isAssignableFrom(cls)) {
                Application app = (Application) cls.getDeclaredConstructor().newInstance();
                app.start(stage);
            } else if (Viewer.class.isAssignableFrom(cls)) {
                Viewer v = (Viewer) cls.getDeclaredConstructor().newInstance();
                v.show(stage);
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to launch: " + e.getMessage()).show();
        }
    }

    private Tab createDevicesTab(I18N i18n) {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));

        VBox header = createTabHeader(
            i18n.get("mastercontrol.devices.header", "Hardware & Devices"),
            i18n.get("mastercontrol.devices.desc", "Monitor and configure physical or simulated laboratory instruments.")
        );

        Map<String, String> devices = new TreeMap<>();
        devices.put("Generic GPIB Device", "Simulated General Purpose Interface Bus");
        devices.put("Generic USB Device", "Simulated Universal Serial Bus");

        // Dynamic discovery
        List<MasterControlDiscovery.ClassInfo> discovered = MasterControlDiscovery.getInstance().findClasses("Device");
        // Also look for SimulatedDevice specifically to be sure
        List<MasterControlDiscovery.ClassInfo> simulated = MasterControlDiscovery.getInstance().findClasses("SimulatedDevice");
        
        for (MasterControlDiscovery.ClassInfo info : discovered) devices.put(info.simpleName, info.description);
        for (MasterControlDiscovery.ClassInfo info : simulated) devices.put(info.simpleName, info.description);

        ListView<String> deviceList = new ListView<>();
        deviceList.getItems().addAll(devices.keySet());
        
        TextArea details = new TextArea();
        details.setEditable(false);
        details.getStyleClass().add("font-mono");

        deviceList.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                details.setText(i18n.get("mastercontrol.devices.details", "Device: {0}\nStatus: Connected\nDetails: {1}", newV, devices.get(newV)));
                PREFS.set(PREF_SELECTED_DEVICE, newV);
            }
        });

        SplitPane split = new SplitPane(deviceList, details);
        split.setDividerPositions(0.3);

        int totalDevices = devices.size();
        content.getChildren().addAll(header, new Label("(" + totalDevices + ") " + i18n.get("mastercontrol.devices.list", "Detected Devices")), split);
        return new Tab(i18n.get("mastercontrol.tab.devices", "Devices"), content);
    }

    private static class AppEntry {
        String name, className, description;
        AppEntry(String n, String c, String d) { name = n; className = c; description = d; }
    }

    private static class LocaleItem {
        String name; Locale locale;
        LocaleItem(String n, Locale l) { name = n; locale = l; }
        @Override public String toString() { return name; }
    }

    private void showStatus(String message, boolean error) {
        Alert alert = new Alert(error ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(I18N.getInstance().get("mastercontrol.status.title", "System Status"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private Label statusLabel;

    private void notifySaved(I18N i18n) {
        Episteme.savePreferences();
        org.episteme.core.technical.algorithm.AlgorithmManager.refresh();
        if (statusLabel != null) {
            statusLabel.setText(i18n.get("mastercontrol.status.saved", "Settings saved."));
            statusLabel.setVisible(true);
            statusLabel.setOpacity(1.0);
            
            javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(javafx.util.Duration.seconds(2), statusLabel);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setDelay(javafx.util.Duration.seconds(1));
            fade.setOnFinished(e -> statusLabel.setVisible(false));
            fade.play();
        }
    }

    public static void main(String[] args) { launch(args); }
}
