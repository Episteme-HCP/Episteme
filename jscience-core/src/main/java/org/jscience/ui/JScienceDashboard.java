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

        Label header = new Label(I18n.getInstance().get("dashboard.apps.header", "Applications & Ecosystem"));
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Accordion accordion = new Accordion();

        // 1. Killer Apps
        TitledPane appsPane = new TitledPane("Applications (Complete Solutions)", createAppList(
                new AppEntry("Civilization Dynamics", "org.jscience.apps.sociology.CivilizationApp",
                        "Societal simulation"),
                new AppEntry("Interplanetary Trajectory",
                        "org.jscience.apps.physics.trajectory.InterplanetaryTrajectoryApp", "Hohmann transfer"),
                new AppEntry("Relativistic Flight", "org.jscience.apps.physics.relativity.RelativisticFlightApp",
                        "Special relativity sim"),
                new AppEntry("Quantum Circuit", "org.jscience.apps.physics.QuantumCircuitApp",
                        "Quantum computing simulator"),
                new AppEntry("Smart Grid", "org.jscience.apps.engineering.SmartGridApp", "Power distribution"),
                new AppEntry("Market Crash Sim", "org.jscience.apps.economics.MarketCrashApp", "Agent-based economics"),
                new AppEntry("Titration Lab", "org.jscience.apps.chemistry.TitrationApp", "Acid-Base simulation"),
                new AppEntry("Crystal Structure", "org.jscience.apps.chemistry.CrystalStructureApp",
                        "3D lattice visualization"),
                new AppEntry("Pandemic Forecaster", "org.jscience.apps.biology.PandemicForecasterApp",
                        "SEIR disease modeling"),
                new AppEntry("CRISPR Design", "org.jscience.apps.biology.CrisprDesignApp", "Genomic editing tool")));

        // 2. Interactive Demos
        TitledPane demosPane = new TitledPane("Interactive Demos", createAppList(
                // Physics
                new AppEntry("Newtonian Lab", "org.jscience.ui.physics.mechanics.NewtonianMechanicsLabViewer",
                        "Springs & Pendulums (Viewer)"),
                new AppEntry("Rigid Body", "org.jscience.ui.demos.RigidBodyDemo", "Physics Engine"),
                new AppEntry("Thermodynamics", "org.jscience.ui.demos.ThermodynamicsDemo", "Heat & Energy"),
                new AppEntry("Spectrograph", "org.jscience.ui.demos.SpectrographDemo", "Wave Analysis"),
                // Biology
                new AppEntry("Species Browser", "org.jscience.ui.demos.SpeciesBrowserDemo", "Taxonomy Explorer"),
                new AppEntry("Sequence Alignment", "org.jscience.ui.demos.SequenceAlignmentDemo", "DNA Comparison"),
                new AppEntry("Phylogenetic Tree", "org.jscience.ui.demos.PhylogeneticTreeDemo", "Evolutionary History"),
                // Engineering
                new AppEntry("Car Traffic", "org.jscience.apps.engineering.traffic.CarTrafficDemo", "Traffic IDM Sim"),
                new AppEntry("Resistor Colors", "org.jscience.ui.demos.ResistorColorCodeDemo", "Component ID"),
                new AppEntry("Sensor/Actuator", "org.jscience.ui.demos.SensorActuatorDemo", "Device Interaction"),
                // Mathematics
                new AppEntry("Matrix Operations", "org.jscience.ui.demos.MatrixDemo", "Linear Algebra"),
                new AppEntry("CSG Geometry", "org.jscience.ui.demos.CSGDemo", "Constructive Solid Geometry"),
                new AppEntry("Surface 3D", "org.jscience.ui.demos.Surface3DDemo", "3D Plotting"),
                new AppEntry("Distributions", "org.jscience.ui.demos.DistributionsDemo", "Statistical Models"),
                // Social
                new AppEntry("Politics & Voting", "org.jscience.ui.demos.PoliticsVotingDemo", "Election Systems"),
                new AppEntry("Economics Market", "org.jscience.ui.demos.EconomicsMarketDemo", "Supply/Demand"),
                new AppEntry("GDP Visualizer", "org.jscience.ui.demos.EconomicsGDPDemo", "Macroeconomics"),
                new AppEntry("Geography GIS", "org.jscience.ui.demos.GeographyGISDemo", "Spatial Data"),
                new AppEntry("History Timeline", "org.jscience.ui.demos.HistoryTimelineDemo", "Historical Events"),
                new AppEntry("Linguistics", "org.jscience.ui.demos.LinguisticsWordFreqDemo", "Word Frequency"),
                new AppEntry("Psychology Reaction", "org.jscience.ui.demos.PsychologyReactionTestDemo",
                        "Response Time"),
                new AppEntry("Sociology Network", "org.jscience.ui.demos.SociologyNetworkDemo", "Social Graph"),
                new AppEntry("Arts Color Theory", "org.jscience.ui.demos.ArtsColorTheoryDemo", "Color Models"),
                new AppEntry("Architecture", "org.jscience.ui.demos.ArchitectureStabilityDemo", "Structural Stability"),
                // General
                new AppEntry("Unit Converter", "org.jscience.ui.demos.UnitConverterDemo", "Quantity Conversion")));

        // 3. Data Viewers
        TitledPane viewersPane = new TitledPane("Viewers & Explorers", createAppList(
                // Physics / Astronomy
                new AppEntry("Stellar Sky", "org.jscience.ui.physics.astronomy.StellarSkyViewer", "Night Sky Map"),
                new AppEntry("Star System", "org.jscience.ui.physics.astronomy.StarSystemViewer", "Orbits & Planets"),
                new AppEntry("Galaxy Viewer", "org.jscience.ui.physics.astronomy.GalaxyViewer", "Galactic Structure"),
                new AppEntry("Fluid Dynamics", "org.jscience.ui.physics.fluids.FluidDynamicsViewer",
                        "Lattice Boltzmann"),
                new AppEntry("Thermodynamics Viewer", "org.jscience.ui.physics.thermodynamics.ThermodynamicsViewer",
                        "PV Diagrams"),
                new AppEntry("Mechanics Viewer", "org.jscience.ui.physics.mechanics.MechanicsViewer",
                        "Motion Analysis"),
                // System
                new AppEntry("Cloth Sim", "org.jscience.ui.computing.simulation.ClothSimulationViewer",
                        "Physics Simulation"),
                new AppEntry("Game of Life", "org.jscience.ui.computing.ai.GameOfLifeViewer", "Cellular Automata"),
                new AppEntry("Genetic Algorithm", "org.jscience.ui.computing.ai.GeneticAlgorithmViewer",
                        "Evolution Sim"),
                // Chemistry
                new AppEntry("Periodic Table", "org.jscience.ui.chemistry.PeriodicTableViewer", "Elements"),
                new AppEntry("Molecular Viewer", "org.jscience.ui.chemistry.MolecularViewer", "3D Molecules"),
                new AppEntry("Chemical Reaction", "org.jscience.ui.chemistry.ChemicalReactionViewer", "Stoichiometry"),
                // Biology
                new AppEntry("Human Body", "org.jscience.ui.biology.anatomy.HumanBodyViewer", "Anatomy Explorer"),
                new AppEntry("Bio Motion", "org.jscience.ui.biology.anatomy.BioMotionViewer", "Kinematics"),
                new AppEntry("L-Systems", "org.jscience.ui.biology.lsystems.LSystemViewer", "Plant Growth"),
                new AppEntry("Genetics Viewer", "org.jscience.ui.biology.genetics.GeneticsViewer", "DNA/RNA"),
                new AppEntry("Species Browser", "org.jscience.ui.biology.ecology.SpeciesBrowserViewer", "Biodiversity"),
                new AppEntry("Lotka-Volterra", "org.jscience.ui.biology.ecology.LotkaVolterraViewer", "Predator-Prey"),
                // Math
                new AppEntry("Function Explorer", "org.jscience.ui.mathematics.numbers.real.FunctionExplorerViewer",
                        "Plotting Tool"),
                new AppEntry("Matrix Viewer", "org.jscience.ui.mathematics.vector.MatrixViewer", "Data Viz"),
                // Devices
                new AppEntry("Circuit Simulator", "org.jscience.ui.engineering.circuit.CircuitSimulatorViewer",
                        "Electronics"),
                new AppEntry("Earthquake Map", "org.jscience.ui.earth.EarthquakeMapViewer", "Seismology"),
                new AppEntry("Vital Monitor", "org.jscience.ui.devices.VitalMonitorViewer", "Medical Device"),
                new AppEntry("Telescope Feed", "org.jscience.ui.devices.TelescopeViewer", "Device Interface"),
                new AppEntry("Spectrometer", "org.jscience.ui.devices.SpectrometerViewer", "Device Interface")));

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
