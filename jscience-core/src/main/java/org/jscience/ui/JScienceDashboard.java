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
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().clear();

        tabPane.getTabs().addAll(
                createGeneralTab(),
                createI18nTab(),
                createThemesTab(),
                createComputingTab(),
                createLibrariesTab(),
                createAppsTab(),
                createDevicesTab());

        StackPane root = new StackPane(tabPane);
        Scene scene = new Scene(root, 1000, 700);

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

        Button applyBtn = new Button(I18n.getInstance().get("dashboard.i18n.apply", "Apply Language"));
        applyBtn.setOnAction(e -> {
            LocaleItem selected = langList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                I18n.getInstance().setLocale(selected.locale);
                refreshUI();
            }
        });

        content.getChildren().addAll(header, langList, applyBtn);
        return new Tab(I18n.getInstance().get("dashboard.tab.i18n", "Language"), content);
    }

    private Tab createThemesTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);

        Label header = new Label("Theme Selection");
        header.setStyle("-fx-font-weight: bold;");

        HBox btnBox = new HBox(15);
        btnBox.setAlignment(Pos.CENTER);

        Button lightBtn = new Button("Light (Modena)");
        lightBtn.setOnAction(e -> Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA));

        Button darkBtn = new Button("Dark (Caspian/Custom)");
        darkBtn.setOnAction(e -> Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN));

        btnBox.getChildren().addAll(lightBtn, darkBtn);
        content.getChildren().addAll(header, btnBox);

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

        Label header = new Label(I18n.getInstance().get("dashboard.apps.header", "Applications & Ecosystem"));
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Accordion accordion = new Accordion();

        // 1. Applications (Killer Apps)
        TitledPane appsPane = new TitledPane("Applications (Complete Solutions)", createAppList(
                new AppEntry("CRISPR Design", "org.jscience.apps.biology.CrisprDesignApp", "Genomic editing tool"),
                new AppEntry("Pandemic Forecaster", "org.jscience.apps.biology.PandemicForecasterApp",
                        "SEIR disease modeling"),
                new AppEntry("Crystal Structure", "org.jscience.apps.chemistry.CrystalStructureApp",
                        "3D lattice visualization"),
                new AppEntry("Titration Lab", "org.jscience.apps.chemistry.TitrationApp", "Acid-Base simulation"),
                new AppEntry("Trajectory Planner", "org.jscience.apps.physics.trajectory.InterplanetaryTrajectoryApp",
                        "Hohmann transfer"),
                new AppEntry("Market Crash Sim", "org.jscience.apps.economics.MarketCrashApp", "Agent-based economics"),
                new AppEntry("Smart Grid", "org.jscience.apps.engineering.SmartGridApp", "Power distribution"),
                new AppEntry("Civilization Dynamics", "org.jscience.apps.sociology.CivilizationApp",
                        "Societal simulation")));

        // 2. Interactive Demos
        TitledPane demosPane = new TitledPane("Interactive Demos", createAppList(
                new AppEntry("Car Traffic", "org.jscience.apps.engineering.traffic.CarTrafficDemo",
                        "Traffic flow simulation"),
                new AppEntry("Sports Results", "org.jscience.apps.sociology.sports.SportsResultsDemo", "League stats"),
                new AppEntry("Politics & Voting", "org.jscience.ui.demos.PoliticsVotingDemo", "Election systems"),
                new AppEntry("Matrix Operations", "org.jscience.ui.demos.MatrixDemo", "Linear Algebra viz"),
                new AppEntry("History Timeline", "org.jscience.ui.demos.HistoryTimelineDemo", "Historical events"),
                new AppEntry("Game of Life", "org.jscience.ui.computing.ai.GameOfLifeViewer",
                        "Cellular Automata (Viewer)")));

        // 3. Data Viewers
        TitledPane viewersPane = new TitledPane("Data Viewers & Tools", createAppList(
                new AppEntry("Periodic Table", "org.jscience.ui.chemistry.PeriodicTableViewer", "Element properties"),
                new AppEntry("Function Explorer", "org.jscience.ui.mathematics.numbers.real.FunctionExplorerViewer",
                        "2D/3D Plotter"),
                new AppEntry("Stellar Sky", "org.jscience.ui.physics.astronomy.StellarSkyViewer", "Zenith projection"),
                new AppEntry("Bio Motion", "org.jscience.ui.biology.anatomy.BioMotionViewer", "Anatomy simulation"),
                new AppEntry("Fluid Dynamics", "org.jscience.ui.physics.fluids.FluidDynamicsViewer",
                        "Lattice Boltzmann"),
                new AppEntry("Matrix Viewer", "org.jscience.ui.mathematics.vector.MatrixViewer",
                        "Dense/Sparse Matrix")));

        accordion.getPanes().addAll(appsPane, demosPane, viewersPane);
        appsPane.setExpanded(true);

        content.getChildren().addAll(header, accordion);
        return new Tab(I18n.getInstance().get("dashboard.tab.apps", "Apps"), content);
    }

    private ListView<AppEntry> createAppList(AppEntry... entries) {
        ListView<AppEntry> list = new ListView<>();
        for (AppEntry entry : entries)
            list.getItems().add(entry);

        list.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(AppEntry item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox box = new VBox(2);
                    Label name = new Label(item.name);
                    name.setStyle("-fx-font-weight: bold;");
                    Label desc = new Label(item.description);
                    desc.setStyle("-fx-font-size: 0.9em; -fx-text-fill: gray;");
                    box.getChildren().addAll(name, desc);
                    setGraphic(box);
                }
            }
        });

        list.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                AppEntry selected = list.getSelectionModel().getSelectedItem();
                if (selected != null)
                    launchApp(selected.className);
            }
        });

        return list;
    }

    private void launchApp(String className) {
        try {
            // Use internal AppLauncher to handle diverse entry points (Application,
            // DemoProvider, main)
            String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            String classpath = System.getProperty("java.class.path");

            // Using AppLauncher wrapper
            ProcessBuilder pb = new ProcessBuilder(javaBin, "-cp", classpath, "org.jscience.ui.AppLauncher", className);
            pb.inheritIO(); // redirect output to parent console for debugging
            pb.start();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Could not launch app: " + className + "\n" + e.getMessage()).show();
        }
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

    private Tab createLibrariesTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label header = new Label(I18n.getInstance().get("dashboard.libraries.header", "Loaded Libraries & Providers"));
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextArea libInfo = new TextArea();
        libInfo.setEditable(false);
        libInfo.setPrefHeight(400); // Taller as requested
        libInfo.setStyle("-fx-font-family: 'Consolas', 'Monospace';");

        StringBuilder sb = new StringBuilder();
        sb.append("=== Linear Algebra Providers ===\n");
        sb.append("CPUDenseLinearAlgebraProvider: Active\n");
        sb.append("CPUSparseLinearAlgebraProvider: Available\n");
        sb.append("CUDADenseLinearAlgebraProvider: Input Required\n");
        sb.append("GRPCLinearAlgebraProvider: Disconnected\n");

        sb.append("\n=== Native Library Support ===\n");
        sb.append("ND4J Support: ").append(checkClass("org.nd4j.linalg.factory.Nd4j") ? "Available" : "Not Found")
                .append("\n");
        sb.append("OpenCL (JOCL): ").append(checkClass("org.jocl.CL") ? "Available" : "Not Found").append("\n");
        sb.append("CUDA (JCuda): ").append(checkClass("jcuda.Pointer") ? "Available" : "Not Found").append("\n");
        sb.append("JNI Bridge: ").append(checkClass("org.jscience.jni.JNIBridge") ? "Available" : "Not Found")
                .append("\n");

        sb.append("\n=== Biology Packages ===\n");
        sb.append("BioJava (Embedded): Partial\n");

        sb.append("\n=== Social Data ===\n");
        sb.append("GeoNames: Offline Mode\n");

        libInfo.setText(sb.toString());
        content.getChildren().addAll(header, libInfo);
        return new Tab(I18n.getInstance().get("dashboard.tab.libraries", "Libraries"), content);
    }

    private Tab createDevicesTab() {
        // Keep existing logic
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
                "Simulated Telescope", "Simulated Temperature Probe");

        TextArea details = new TextArea("Select a device.");
        details.setEditable(false);
        details.setWrapText(true);

        deviceList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                details.setText(
                        "Device: " + newVal + "\nStatus: Connected (Simulatd)\nDetails: Generic driver loaded.");
            }
        });

        split.getItems().addAll(deviceList, details);
        split.setDividerPositions(0.4);
        content.getChildren().addAll(header, split);
        VBox.setVgrow(split, Priority.ALWAYS);
        return new Tab(I18n.getInstance().get("dashboard.tab.devices", "Devices"), content);
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
