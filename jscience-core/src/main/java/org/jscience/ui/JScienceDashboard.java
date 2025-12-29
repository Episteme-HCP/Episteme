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
import java.lang.reflect.Modifier;
import java.util.stream.Collectors;
import org.jscience.ui.DashboardDiscovery;

/**
 * Main Dashboard for JScience Application.
 * Visualizes the state of the application, libraries, devices, and
 * configuration.
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

    /**
     * Recreates the scene to apply locale changes dynamically.
     */
    private void refreshUI() {
        int selectedIndex = 0;
        if (primaryStage.getScene() != null) {
            TabPane currentPane = (TabPane) ((StackPane) primaryStage.getScene().getRoot()).getChildren().get(0);
            selectedIndex = currentPane.getSelectionModel().getSelectedIndex();
        }

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().clear();

        tabPane.getTabs().addAll(
                createGeneralTab(),
                createI18nTab(),
                createThemesTab(),
                createComputingTab(),
                createLibrariesTab(),
                createLoadersTab(),
                createAppsTab(),
                createDevicesTab());

        tabPane.getSelectionModel().select(selectedIndex);

        StackPane root = new StackPane(tabPane);
        Scene scene = new Scene(root, 1100, 750);

        // Restore theme
        String currentTheme = System.getProperty("jscience.theme", "Modena");
        if ("Caspian".equalsIgnoreCase(currentTheme)) {
            Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
        } else {
            Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
        }

        primaryStage.setTitle(I18n.getInstance().get("app.title", "JScience Dashboard"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Tab createGeneralTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setAlignment(Pos.CENTER);

        // Simple Text/Logo
        Label title = new Label("JScience Dashboard");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitle = new Label("Scientific Library & Environment");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.addRow(0, new Label("Version:"), new Label("5.0.0-SNAPSHOT"));
        grid.addRow(1, new Label("Main Author:"), new Label("Silvere Martin-Michiellot"));
        grid.addRow(2, new Label("License:"), new Label("MIT"));

        content.getChildren().addAll(title, subtitle, new Separator(), grid);
        return new Tab(I18n.getInstance().get("dashboard.tab.general", "General"), content);
    }

    private Tab createI18nTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);

        Label header = new Label(I18n.getInstance().get("dashboard.i18n.select", "Language Selection"));
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        ListView<LocaleItem> langList = new ListView<>();
        langList.getItems().addAll(
                new LocaleItem("English", Locale.ENGLISH),
                new LocaleItem("Français", Locale.FRENCH),
                new LocaleItem("Deutsch", Locale.GERMAN),
                new LocaleItem("Español", new Locale("es")),
                new LocaleItem("中文", new Locale("zh")));

        langList.setMaxHeight(200);
        langList.setMaxWidth(300);

        // Select current
        Locale current = I18n.getInstance().getLocale();
        for (LocaleItem item : langList.getItems()) {
            if (item.locale.getLanguage().equals(current.getLanguage())) {
                langList.getSelectionModel().select(item);
                break;
            }
        }

        // Apply on selection
        langList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.locale.equals(current)) {
                I18n.getInstance().setLocale(newVal.locale);
                refreshUI();
            }
        });

        content.getChildren().addAll(header, langList);
        return new Tab(I18n.getInstance().get("dashboard.tab.i18n", "Language"), content);
    }

    private Tab createThemesTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label header = new Label("Theme Selection");
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        ListView<String> themeList = new ListView<>();
        themeList.getItems().addAll("Light (Modena)", "Dark (Caspian)", "High Contrast");
        themeList.setMaxHeight(150);
        themeList.setMaxWidth(300);

        themeList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                if (newVal.contains("Modena")) {
                    System.setProperty("jscience.theme", "Modena");
                    Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
                } else if (newVal.contains("Caspian")) {
                    System.setProperty("jscience.theme", "Caspian");
                    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
                }
                // High contrast logic could go here
            }
        });

        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(themeList);

        content.getChildren().addAll(header, box);
        return new Tab(I18n.getInstance().get("dashboard.tab.themes", "Themes"), content);
    }

    private Tab createComputingTab() {
        // ... (Keep existing simple)
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label header = new Label(I18n.getInstance().get("dashboard.computing.header", "Computing Context"));
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.addRow(0, new Label(I18n.getInstance().get("dashboard.computing.mode", "Compute Mode:")),
                new Label(JScience.getComputeMode().toString()));
        grid.addRow(1, new Label(I18n.getInstance().get("dashboard.computing.gpu", "GPU Available:")),
                new Label(JScience.isGpuAvailable() ? "Yes" : "No"));

        content.getChildren().addAll(header, grid);
        return new Tab(I18n.getInstance().get("dashboard.tab.computing", "Computing"), content);
    }

    private Tab createAppsTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label header = new Label(
                I18n.getInstance().get("dashboard.apps.header", "Applications & Ecosystem (Discovered)"));
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Accordion accordion = new Accordion();

        DashboardDiscovery discovery = DashboardDiscovery.getInstance();

        // 1. Applications
        TitledPane appsPane = new TitledPane("Applications", createAppList(discovery.findClasses("App")));

        // 2. Demos
        TitledPane demosPane = new TitledPane("Interactive Demos", createAppList(discovery.findClasses("Demo")));

        // 3. Viewers
        TitledPane viewersPane = new TitledPane("Viewers & Explorers", createAppList(discovery.findClasses("Viewer")));

        accordion.getPanes().addAll(appsPane, demosPane, viewersPane);
        appsPane.setExpanded(true);

        content.getChildren().addAll(header, accordion);
        return new Tab(I18n.getInstance().get("dashboard.tab.apps", "Apps"), content);
    }

    private ListView<DashboardDiscovery.ClassInfo> createAppList(java.util.List<DashboardDiscovery.ClassInfo> entries) {
        ListView<DashboardDiscovery.ClassInfo> list = new ListView<>();
        list.getItems().addAll(entries);

        list.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(DashboardDiscovery.ClassInfo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox box = new VBox(2);
                    Label name = new Label(item.simpleName);
                    name.setStyle("-fx-font-weight: bold;");
                    Label desc = new Label(item.fullName);
                    desc.setStyle("-fx-font-size: 0.8em; -fx-text-fill: gray;");
                    box.getChildren().addAll(name, desc);
                    setGraphic(box);
                }
            }
        });

        list.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                DashboardDiscovery.ClassInfo selected = list.getSelectionModel().getSelectedItem();
                if (selected != null)
                    launchApp(selected.fullName);
            }
        });

        return list;
    }

    private void launchApp(String className) {
        try {
            String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            String classpath = System.getProperty("java.class.path");

            // Collect current JVM arguments (includes -D, -X, --module-path, --add-modules,
            // etc.)
            java.util.List<String> vmArgs = java.lang.management.ManagementFactory.getRuntimeMXBean()
                    .getInputArguments();

            java.util.List<String> command = new java.util.ArrayList<>();
            command.add(javaBin);
            command.addAll(vmArgs); // Propagate all VM args
            command.add("-cp");
            command.add(classpath);
            command.add("org.jscience.ui.AppLauncher");
            command.add(className);

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.inheritIO();
            pb.start();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Could not launch app: " + className + "\n" + e.getMessage()).show();
        }
    }

    private Tab createLibrariesTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label header = new Label(I18n.getInstance().get("dashboard.libraries.header", "Loaded Libraries & Providers"));
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextArea libInfo = new TextArea();
        libInfo.setEditable(false);
        libInfo.setPrefHeight(400);
        libInfo.setStyle("-fx-font-family: 'Consolas', 'Monospace';");

        StringBuilder sb = new StringBuilder();
        sb.append("=== JScience Runtime Properties ===\n");
        // Inspect JScience class reflectively
        try {
            Class<?> cls = Class.forName("org.jscience.JScience");
            java.lang.reflect.Method[] methods = cls.getMethods();
            for (java.lang.reflect.Method m : methods) {
                if (Modifier.isStatic(m.getModifiers()) && m.getName().startsWith("get")
                        && m.getParameterCount() == 0 && !m.getName().equals("getClass")) {
                    Object val = m.invoke(null);
                    sb.append(m.getName().substring(3)).append(": ").append(val).append("\n");
                }
                // Check for "is" methods too like isGpuAvailable
                if (Modifier.isStatic(m.getModifiers()) && m.getName().startsWith("is")
                        && m.getParameterCount() == 0) {
                    Object val = m.invoke(null);
                    sb.append(m.getName()).append(": ").append(val).append("\n");
                }
            }
        } catch (Exception e) {
            sb.append("Error inspecting JScience properties: ").append(e.getMessage()).append("\n");
        }

        libInfo.setText(sb.toString());
        content.getChildren().addAll(header, libInfo);
        return new Tab(I18n.getInstance().get("dashboard.tab.libraries", "Libraries"), content);
    }

    private Tab createDevicesTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label header = new Label(
                I18n.getInstance().get("dashboard.devices.header", "Available Devices (Simulated & JNI)"));
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        SplitPane split = new SplitPane();
        ListView<String> deviceList = new ListView<>();
        deviceList.getItems().addAll(
                "Simulated Centrifuge", "Simulated Microscope", "Simulated Multimeter",
                "Simulated Oscilloscope", "Simulated PHMeter", "Simulated Spectrometer",
                "Simulated Telescope", "Simulated Temperature Probe", "Simulated PressureGauge",
                "Simulated Vital Signs Monitor", "Generic GPIB Device", "Generic USB Sensor");

        TextArea details = new TextArea("Select a device to view detailed properties.");
        details.setEditable(false);
        details.setWrapText(true);
        details.setStyle("-fx-font-family: 'Consolas', 'Monospace';");

        deviceList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Device: ").append(newVal).append("\n");
                sb.append("Status: Connected (Simulated)\n");
                sb.append("Driver: org.jscience.device.sim.").append(newVal.replace(" ", "")).append("\n");
                sb.append("ID: ").append(String.format("%08X", Math.abs(newVal.hashCode()))).append("\n");
                sb.append("Manufacturer: JScience Sims Inc.\n");
                sb.append("Firmware: v2.1.4\n\n");
                sb.append("=== Capabilities ===\n");
                sb.append(" [x] Data Logging\n");
                sb.append(" [x] Remote Control\n");
                sb.append(" [x] Asynchronous I/O\n");
                sb.append(" [ ] High Voltage Protection\n\n");
                sb.append("=== Current Readings ===\n");
                sb.append(" Power: ON\n");
                sb.append(" Uptime: 42s\n");
                sb.append(" Error Code: 0x00\n");

                details.setText(sb.toString());
            }
        });

        split.getItems().addAll(deviceList, details);
        split.setDividerPositions(0.35);
        content.getChildren().addAll(header, split);
        VBox.setVgrow(split, Priority.ALWAYS);
        return new Tab(I18n.getInstance().get("dashboard.tab.devices", "Devices"), content);
    }

    private Tab createLoadersTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label header = new Label(I18n.getInstance().get("dashboard.loaders.header", "Known Data Loaders & Formats"));
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextArea loaderList = new TextArea();
        loaderList.setEditable(false);
        loaderList.setStyle("-fx-font-family: 'Consolas', 'Monospace';");

        StringBuilder sb = new StringBuilder();
        sb.append("=== Geography & Earth Support ===\n");
        sb.append(" - GeoJSON (GeoJsonLoader)\n");
        sb.append(" - World Bank Data (WorldBankLoader)\n");
        sb.append(" - CIA World Factbook (FactbookLoader)\n");
        sb.append(" - OpenWeatherMap API (OpenWeatherLoader)\n");
        sb.append(" - Weather Data (WeatherDataLoader)\n\n");

        sb.append("=== Astronomy & Physics ===\n");
        sb.append(" - NASA Horizons (HorizonsEphemerisLoader)\n");
        sb.append(" - VizieR Catalog (VizieRLoader)\n");
        sb.append(" - SIMBAD Database (SimbadLoader)\n");
        sb.append(" - Star Catalog (StarLoader)\n");
        sb.append(" - Solar System Ephemeris (SolarSystemLoader)\n\n");

        sb.append("=== Biology & Chemistry ===\n");
        sb.append(" - PDB Protein Bank (PDBLoader, GenericPDBLoader)\n");
        sb.append(" - FASTA Sequences (FastaLoader)\n");
        sb.append(" - UniProt (UniProtLoader)\n");
        sb.append(" - NCBI Taxonomy (NCBITaxonomyLoader)\n");
        sb.append(" - PubChem (PubChemLoader)\n");
        sb.append(" - ChEBI (ChEBILoader)\n");
        sb.append(" - IUPAC Gold Book (IUPACGoldBookLoader)\n");
        sb.append(" - CIF Crystallography (CIFLoader)\n\n");

        sb.append("=== Social Sciences ===\n");
        sb.append(" - Financial Markets (FinancialMarketLoader)\n");
        sb.append(" - Time Series CSV (CSVTimeSeriesLoader)\n");

        sb.append("=== System & Assets ===\n");
        sb.append(" - 3D Models (ObjMeshLoader, StlMeshLoader)\n");
        sb.append(" - Resources (ResourceLoader, PropertiesLoader)\n");

        loaderList.setText(sb.toString());

        content.getChildren().addAll(header, loaderList);
        VBox.setVgrow(loaderList, Priority.ALWAYS);
        return new Tab("Loaders", content);
    }

    private boolean checkClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
