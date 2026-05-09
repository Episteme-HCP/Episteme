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
import org.episteme.core.io.ResourceIO;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.BackendDiscovery;
import org.episteme.core.ui.i18n.I18N;
import org.episteme.core.ui.viewers.Viewer;

import java.util.*;

/**
 * Episteme Master Control - The central dashboard for the Episteme environment.
 * Provides a unified interface for system settings, library management, and application discovery.
 */
public class EpistemeMasterControl extends Application {

    private static final EpistemePreferences PREFS = EpistemePreferences.getInstance();
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
        primaryStage.show();
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

        VBox textInfo = new VBox(10);
        Label version = new Label("Version: 1.0.0-beta2");
        version.getStyleClass().add("font-bold");
        Label status = new Label("System Status: Operational");
        status.getStyleClass().add("status-label-available");
        textInfo.getChildren().addAll(version, status);

        infoBox.getChildren().addAll(projectIcon, textInfo);

        content.getChildren().addAll(header, infoBox);
        return new Tab(i18n.get("mastercontrol.tab.general", "General"), content);
    }

    private Tab createI18NTab(I18N i18n) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        VBox header = createTabHeader(
            i18n.get("mastercontrol.tab.i18n", "Language & Region"),
            i18n.get("mastercontrol.i18n.desc", "Configure the global interface language and numerical formatting conventions.")
        );

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(20);

        ComboBox<LocaleItem> langCombo = new ComboBox<>();
        langCombo.getItems().addAll(
                new LocaleItem("English (US)", Locale.US),
                new LocaleItem("Français (France)", Locale.FRANCE),
                new LocaleItem("Deutsch (Deutschland)", Locale.GERMANY),
                new LocaleItem("Español (España)", Locale.forLanguageTag("es-ES")));

        Locale currentLocale = i18n.getLocale();
        for (LocaleItem item : langCombo.getItems()) {
            if (item.locale.getLanguage().equals(currentLocale.getLanguage())) {
                langCombo.setValue(item);
                break;
            }
        }

        langCombo.setOnAction(e -> {
            LocaleItem selected = langCombo.getValue();
            if (selected != null) {
                PREFS.setLanguage(selected.locale.getLanguage());
                i18n.setLocale(selected.locale);
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
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        VBox header = createTabHeader(
            i18n.get("mastercontrol.tab.themes", "Appearance & Themes"),
            i18n.get("mastercontrol.themes.header.desc", "Customize the visual appearance of the Episteme environment.")
        );

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(20);

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
        });

        addPropertyRow(grid, 0, 
            i18n.get("mastercontrol.themes.select", "Visual Style"), 
            themeCombo, 
            i18n.get("mastercontrol.themes.select.desc", "Select the global theme for the application. 'Dark' is recommended for high-performance computing."));

        content.getChildren().addAll(header, grid);
        return new Tab(i18n.get("mastercontrol.tab.themes", "Themes"), content);
    }

    private Tab createComputingTab(I18N i18n) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        VBox header = createTabHeader(
            i18n.get("mastercontrol.tab.computing", "Computing & Engines"),
            i18n.get("mastercontrol.computing.desc", "Select the underlying engines for mathematical analysis, 2D plotting, and 3D visualization.")
        );

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(20);

        // --- Math Engine ---
        ComboBox<String> mathCombo = createBackendComboBox(BackendDiscovery.TYPE_MATH, Episteme.getMathBackendId(), id -> {
            Episteme.setMathBackendId(id);
            Episteme.savePreferences();
        });
        addPropertyRow(grid, 0, i18n.get("mastercontrol.math.backend", "Mathematics Engine"), mathCombo, 
            i18n.get("mastercontrol.math.backend.desc", "Primary engine for linear algebra and algorithm execution."));

        // --- 2D Plotting ---
        ComboBox<String> plot2DCombo = createBackendComboBox(BackendDiscovery.TYPE_PLOTTING, PREFS.getPreferredBackend("plotting2d"), id -> {
            PREFS.setPreferredBackend("plotting2d", id);
        });
        addPropertyRow(grid, 1, i18n.get("mastercontrol.plotting.backend_2d", "2D Visualization"), plot2DCombo, 
            i18n.get("mastercontrol.plotting.backend_2d.desc", "Engine for rendering 2D charts and data plots."));

        // --- 3D Plotting ---
        ComboBox<String> plot3DCombo = createBackendComboBox(BackendDiscovery.TYPE_PLOTTING, PREFS.getPreferredBackend("plotting3d"), id -> {
            PREFS.setPreferredBackend("plotting3d", id);
        });
        addPropertyRow(grid, 2, i18n.get("mastercontrol.plotting.backend_3d", "3D Visualization"), plot3DCombo, 
            i18n.get("mastercontrol.plotting.backend_3d.desc", "Engine for rendering complex 3D surfaces and volumes."));

        content.getChildren().addAll(header, grid);
        return new Tab(i18n.get("mastercontrol.tab.computing", "Computing"), content);
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
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox header = createTabHeader(
            i18n.get("mastercontrol.tab.libraries", "Project Libraries"),
            i18n.get("mastercontrol.libraries.desc", "Detailed status of all scientific and technical libraries integrated into the Episteme ecosystem.")
        );

        content.getChildren().add(header);

        // --- Categories ---
        String[] types = {BackendDiscovery.TYPE_MATH, BackendDiscovery.TYPE_PLOTTING, BackendDiscovery.TYPE_AUDIO, 
                         BackendDiscovery.TYPE_MOLECULAR, BackendDiscovery.TYPE_QUANTUM, BackendDiscovery.TYPE_NETWORK};
        String[] labels = {"Mathematics", "Visualization", "Audio Processing", "Molecular Viewing", "Quantum Computing", "Network Analysis"};
        
        for (int i = 0; i < types.length; i++) {
            content.getChildren().add(createBackendCategory(i18n, types[i], labels[i], ""));
            if (i < types.length - 1) content.getChildren().add(new Separator());
        }

        return new Tab(i18n.get("mastercontrol.tab.libraries", "Libraries"), scroll);
    }

    private Tab createAlgorithmsTab(I18N i18n) {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox header = createTabHeader(
            i18n.get("mastercontrol.tab.algorithms", "Computational Algorithms"),
            i18n.get("mastercontrol.algorithms.desc", "Configuration and availability of specific scientific algorithm implementations.")
        );

        content.getChildren().addAll(header, createBackendCategory(i18n, BackendDiscovery.TYPE_ALGORITHM, "", ""));
        return new Tab(i18n.get("mastercontrol.tab.algorithms", "Algorithms"), scroll);
    }

    private VBox createBackendCategory(I18N i18n, String type, String title, String description) {
        VBox box = new VBox(15);
        if (title != null && !title.isEmpty()) {
            Label header = new Label(title);
            header.getStyleClass().add("font-bold");
            box.getChildren().add(header);
        }

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(2);

        List<Backend> providers = BackendDiscovery.getInstance().getProvidersByType(type);
        int r = 0;
        for (Backend provider : providers) {
            addBackendRow(grid, r++, provider, i18n);
        }

        if (providers.isEmpty()) {
            Label none = new Label(i18n.get("status.none", "No providers discovered for this category."));
            none.getStyleClass().add("description-label");
            grid.add(none, 0, 0);
        }

        box.getChildren().add(grid);
        return box;
    }

    private void addBackendRow(GridPane grid, int row, Backend provider, I18N i18n) {
        Region bg = new Region();
        bg.getStyleClass().add(row % 2 == 0 ? "zebra-row-even" : "zebra-row-odd");
        GridPane.setColumnSpan(bg, 4);
        GridPane.setHgrow(bg, Priority.ALWAYS);
        grid.add(bg, 0, row);

        Label name = new Label(provider.getName());
        name.getStyleClass().add("font-bold");
        name.setPadding(new Insets(8, 10, 8, 15));

        Label status = new Label(provider.isAvailable() ? i18n.get("status.available", "AVAILABLE") : i18n.get("status.missing", "MISSING"));
        status.getStyleClass().add(provider.isAvailable() ? "status-label-available" : "status-label-unavailable");
        status.setPadding(new Insets(8, 10, 8, 10));

        Label info = new Label(provider.getInfo());
        info.getStyleClass().add("description-label");
        info.setPadding(new Insets(8, 10, 8, 10));
        info.setWrapText(true);
        info.setMaxWidth(500);

        grid.add(name, 0, row);
        grid.add(status, 1, row);
        grid.add(info, 2, row);
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
                String cat = info.fullName.contains(".chemistry.") ? "Chemistry" : (info.fullName.contains(".physics.") ? "Physics" : "General");
                grouped.computeIfAbsent(cat, k -> new ArrayList<>()).add(new AppEntry(info.simpleName, info.fullName, info.description));
            }
        }

        for (Map.Entry<String, List<AppEntry>> entry : grouped.entrySet()) {
            String title = i18n.get(entry.getKey(), entry.getKey());
            TitledPane pane = new TitledPane(title + " (" + entry.getValue().size() + ")", createAppList(false, entry.getValue().toArray(new AppEntry[0])));
            accordion.getPanes().add(pane);
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
        
        List<MasterControlDiscovery.ClassInfo> apps = MasterControlDiscovery.getInstance().findClasses("App");
        List<MasterControlDiscovery.ClassInfo> demos = MasterControlDiscovery.getInstance().findClasses("Demo");
        List<MasterControlDiscovery.ClassInfo> viewers = MasterControlDiscovery.getInstance().findClasses("Viewer");

        if (!apps.isEmpty()) accordion.getPanes().add(new TitledPane("Applications", createAppList(true, convert(apps))));
        if (!demos.isEmpty()) accordion.getPanes().add(new TitledPane("Demos", createAppList(true, convert(demos))));
        if (!viewers.isEmpty()) accordion.getPanes().add(new TitledPane("Viewers", createAppList(true, convert(viewers))));

        if (!accordion.getPanes().isEmpty()) accordion.getPanes().get(0).setExpanded(true);

        content.getChildren().addAll(header, accordion);
        return new Tab(i18n.get("mastercontrol.tab.apps", "Apps"), content);
    }

    private AppEntry[] convert(List<MasterControlDiscovery.ClassInfo> infos) {
        return infos.stream().map(i -> new AppEntry(i.simpleName, i.fullName, i.description)).toArray(AppEntry[]::new);
    }

    private ListView<AppEntry> createAppList(boolean launch, AppEntry[] entries) {
        ListView<AppEntry> list = new ListView<>();
        list.getItems().addAll(entries);
        list.setCellFactory(p -> new ListCell<>() {
            @Override protected void updateItem(AppEntry item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setGraphic(null); return; }
                VBox box = new VBox(2);
                Label n = new Label(item.name); n.getStyleClass().add("font-bold");
                Label d = new Label(item.description); d.getStyleClass().add("description-label");
                box.getChildren().addAll(n, d);
                setGraphic(box);
            }
        });
        if (launch) {
            list.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    AppEntry s = list.getSelectionModel().getSelectedItem();
                    if (s != null) launchApp(s.className);
                }
            });
        }
        return list;
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
        for (MasterControlDiscovery.ClassInfo info : discovered) {
            devices.put(info.simpleName, info.description);
        }

        ListView<String> deviceList = new ListView<>();
        deviceList.getItems().addAll(devices.keySet());
        
        TextArea details = new TextArea();
        details.setEditable(false);
        details.getStyleClass().add("font-mono");

        deviceList.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                details.setText("Device: " + newV + "\nStatus: Connected\nDetails: " + devices.get(newV));
                PREFS.set(PREF_SELECTED_DEVICE, newV);
            }
        });

        SplitPane split = new SplitPane(deviceList, details);
        split.setDividerPositions(0.3);

        content.getChildren().addAll(header, split);
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

    public static void main(String[] args) { launch(args); }
}
