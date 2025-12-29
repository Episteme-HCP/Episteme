package org.jscience.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.jscience.JScience;
import org.jscience.JScienceVersion;
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
        tabPane.getTabs().clear(); // Clear existing tabs before adding new ones

        tabPane.getTabs().addAll(
                createGeneralTab(),
                // createI18nTab() is now integrated into createGeneralTab()
                createThemesTab(),
                createComputingTab(),
                createLibrariesTab(),
                createAppsTab(),
                createDevicesTab());

        StackPane root = new StackPane(tabPane);
        Scene scene = new Scene(root, 900, 600);

        // Apply theme
        // ThemeManager.getInstance().applyTheme(scene); // Removed as per new theme
        // handling

        primaryStage.setTitle(I18n.getInstance().get("app.title", "JScience Dashboard"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Tab createGeneralTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setAlignment(Pos.CENTER);

        Label title = new Label(I18n.getInstance().get("app.header.title", "JScience Dashboard"));
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label subtitle = new Label(
                I18n.getInstance().get("dashboard.general.subtitle", "Advanced Scientific Library for Java"));
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");

        // Info Grid
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.addRow(0, new Label(I18n.getInstance().get("dashboard.general.version", "Version:")),
                new Label("5.0.0-SNAPSHOT"));
        grid.addRow(1, new Label(I18n.getInstance().get("dashboard.general.authors", "Authors:")),
                new Label("Silvere Martin-Michiellot"));
        grid.addRow(2, new Label(I18n.getInstance().get("dashboard.general.year", "Year:")), new Label("2025"));

        // Language Selector (Moved here)
        HBox langBox = new HBox(10);
        langBox.setAlignment(Pos.CENTER);
        Label langLabel = new Label(I18n.getInstance().get("dashboard.i18n.select", "Select Language:"));

        ComboBox<LocaleItem> langCombo = new ComboBox<>();
        langCombo.getItems().addAll(
                new LocaleItem("English", Locale.ENGLISH),
                new LocaleItem("Français", Locale.FRENCH),
                new LocaleItem("Deutsch", Locale.GERMAN),
                new LocaleItem("Español", new Locale("es")),
                new LocaleItem("中文", new Locale("zh")));

        // Select current
        Locale current = I18n.getInstance().getLocale();
        for (LocaleItem item : langCombo.getItems()) {
            if (item.locale.getLanguage().equals(current.getLanguage())) {
                langCombo.getSelectionModel().select(item);
                break;
            }
        }

        langCombo.setOnAction(e -> {
            LocaleItem selected = langCombo.getValue();
            if (selected != null) {
                I18n.getInstance().setLocale(selected.locale);
                refreshUI(); // Live update
            }
        });

        langBox.getChildren().addAll(langLabel, langCombo);

        content.getChildren().addAll(title, subtitle, new Separator(), grid, new Separator(), langBox);
        return new Tab(I18n.getInstance().get("dashboard.tab.general", "General"), content);
    }

    private Tab createThemesTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label header = new Label(I18n.getInstance().get("dashboard.themes.header", "Theme Settings"));
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox themeBox = new HBox(10);
        Button lightBtn = new Button(I18n.getInstance().get("dashboard.themes.light", "Light"));
        Button darkBtn = new Button(I18n.getInstance().get("dashboard.themes.dark", "Dark"));

        lightBtn.setOnAction(e -> Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA));
        darkBtn.setOnAction(e -> Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN));

        // Correcting:
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(header, themeBox);
        themeBox.getChildren().addAll(lightBtn, darkBtn);
        return new Tab(I18n.getInstance().get("dashboard.tab.themes", "Themes"), root);
    }

    private Tab createComputingTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label header = new Label(I18n.getInstance().get("dashboard.computing.header", "Computing Context"));
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.addRow(0, new Label(I18n.getInstance().get("dashboard.computing.mode", "Compute Mode:")),
                new Label(JScience.getComputeMode().toString()));

        String gpuStatus = JScience.isGpuAvailable() ? "Yes" : "No";
        if (JScience.isGpuAvailable()) {
            gpuStatus += " (" + I18n.getInstance().get("dashboard.computing.gpu.desc", "OpenCL/CUDA runtime detected")
                    + ")";
        }
        grid.addRow(1, new Label(I18n.getInstance().get("dashboard.computing.gpu", "GPU Available:")),
                new Label(gpuStatus));

        grid.addRow(2, new Label(I18n.getInstance().get("dashboard.computing.precision.float", "Float Precision:")),
                new Label(JScience.getFloatPrecisionMode().toString()));
        grid.addRow(3, new Label(I18n.getInstance().get("dashboard.computing.precision.int", "Integer Precision:")),
                new Label(JScience.getIntPrecisionMode().toString()));

        content.getChildren().addAll(header, grid);
        return new Tab(I18n.getInstance().get("dashboard.tab.computing", "Computing"), content);
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
                "Simulated Centrifuge",
                "Simulated Microscope",
                "Simulated Multimeter",
                "Simulated Oscilloscope",
                "Simulated PHMeter",
                "Simulated PressureGauge",
                "Simulated Spectrometer",
                "Simulated Telescope",
                "Simulated Temperature Probe",
                "Simulated Vital Signs Monitor");

        TextArea details = new TextArea();
        details.setEditable(false);
        details.setWrapText(true);
        details.setText(I18n.getInstance().get("dashboard.devices.desc", "Select a device to view details."));

        deviceList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Device: ").append(newVal).append("\n");
                sb.append("Status: Connected (Simulated)\n");
                sb.append("Driver: org.jscience.device.sim.").append(newVal.replace(" ", "")).append("\n");
                sb.append("ID: ").append(Math.abs(newVal.hashCode())).append("\n");
                sb.append("Manufacturer: JScience Sims Inc.\n\n");
                sb.append("Capabilities:\n");
                sb.append(" - Data Logging: Supported\n");
                sb.append(" - Remote Control: Supported\n");
                sb.append(" - Calibration: Auto\n");
                details.setText(sb.toString());
            }
        });

        split.getItems().addAll(deviceList, details);
        split.setDividerPositions(0.4);

        content.getChildren().addAll(header, split);
        VBox.setVgrow(split, Priority.ALWAYS);
        return new Tab(I18n.getInstance().get("dashboard.tab.devices", "Devices"), content);
    }

    private Tab createAppsTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label header = new Label(I18n.getInstance().get("dashboard.apps.header", "Applications & Demos"));
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Accordion accordion = new Accordion();

        // Killer Apps
        TitledPane appsPane = new TitledPane(
                I18n.getInstance().get("dashboard.apps.category.killer", "Scientific Applications"), createAppList(
                        new AppEntry("CRISPR Design", "org.jscience.apps.biology.CrisprDesignApp",
                                "Genomic editing tool"),
                        new AppEntry("Pandemic Forecaster", "org.jscience.apps.biology.PandemicForecasterApp",
                                "SEIR disease modeling"),
                        new AppEntry("Crystal Structure", "org.jscience.apps.chemistry.CrystalStructureApp",
                                "3D lattice visualization"),
                        new AppEntry("Titration Lab", "org.jscience.apps.chemistry.TitrationApp",
                                "Acid-Base simulation"),
                        new AppEntry("Trajectory Planner",
                                "org.jscience.apps.physics.trajectory.InterplanetaryTrajectoryApp",
                                "Hohmann transfer visualizer"),
                        new AppEntry("Quantum Circuit", "org.jscience.apps.physics.QuantumCircuitApp",
                                "Quantum computing simulator")));

        // Demos
        TitledPane demosPane = new TitledPane(
                I18n.getInstance().get("dashboard.apps.category.demos", "Interactive Demos"), createAppList(
                        new AppEntry("Matrix Viewer", "org.jscience.ui.mathematics.vector.MatrixViewer",
                                "Matrix operations & heatmap"),
                        new AppEntry("Chaos Theory", "org.jscience.ui.mathematics.fractal.LorenzAttractorViewer",
                                "Lorenz attractor visualization"),
                        new AppEntry("Mandelbrot Explorer", "org.jscience.ui.mathematics.fractal.MandelbrotViewer",
                                "Fractal zoomer"),
                        new AppEntry("Galton Board", "org.jscience.ui.mathematics.statistics.GaltonBoardViewer",
                                "Normal distribution sim"),
                        new AppEntry("Periodic Table", "org.jscience.ui.chemistry.PeriodicTableViewer",
                                "Chemical elements explorer")));

        accordion.getPanes().addAll(appsPane, demosPane);
        appsPane.setExpanded(true);

        content.getChildren().addAll(header, accordion);
        return new Tab(I18n.getInstance().get("dashboard.tab.apps", "Apps"), content);
    }

    private ListView<AppEntry> createAppList(AppEntry... entries) {
        ListView<AppEntry> list = new ListView<>();
        for (AppEntry entry : entries) {
            list.getItems().add(entry);
        }
        list.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(AppEntry item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox box = new VBox(3);
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
            Class<?> clazz = Class.forName(className);
            // Assume it has a main method or extends Application
            // For JavaFX Applications, it's tricky to launch multiple in same JVM if they
            // call launch()
            // Using a separate process is safer for stability, but we can try reflection
            // first if designed well.
            // But standard JavaFX Application.launch() can strictly be called only once.
            // Best bet: Try to instantiate and show if it's a Viewer/Stage, or run main in
            // new thread if safe.
            // However, JScience apps seem to extend Application. launching them directly
            // might crash if launch() called twice.
            // Workaround: Run in separate process.
            String javaHome = System.getProperty("java.home");
            String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
            String classpath = System.getProperty("java.class.path");
            new ProcessBuilder(javaBin, "-cp", classpath, className).start();
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
        libInfo.setStyle("-fx-font-family: 'Consolas', 'Monospace';");

        StringBuilder sb = new StringBuilder();
        sb.append("=== Linear Algebra Providers ===\n");
        // Scan for providers in the known package (simple class names for now as
        // checking validity requires loading)
        sb.append("CPUDenseLinearAlgebraProvider\n");
        sb.append("CPUSparseLinearAlgebraProvider\n");
        sb.append("CUDADenseLinearAlgebraProvider\n");
        sb.append("CUDASparseLinearAlgebraProvider\n");
        sb.append("OpenCLDenseLinearAlgebraProvider\n");
        sb.append("OpenCLSparseLinearAlgebraProvider\n");
        sb.append("GRPCLinearAlgebraProvider\n");

        sb.append("\n=== Native Library Support ===\n");
        sb.append("ND4J Support: ").append(checkClass("org.nd4j.linalg.factory.Nd4j") ? "Available" : "Not Found")
                .append("\n");
        sb.append("OpenCL (JOCL): ").append(checkClass("org.jocl.CL") ? "Available" : "Not Found").append("\n");
        sb.append("CUDA (JCuda): ").append(checkClass("jcuda.Pointer") ? "Available" : "Not Found").append("\n");
        sb.append("JNI Bridge: ").append(checkClass("org.jscience.jni.JNIBridge") ? "Available" : "Not Found")
                .append("\n");

        libInfo.setText(sb.toString());

        content.getChildren().addAll(header, libInfo);
        return new Tab(I18n.getInstance().get("dashboard.tab.libraries", "Libraries"), content);
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
