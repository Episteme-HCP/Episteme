package org.jscience.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.jscience.chemistry.Molecule;
import org.jscience.chemistry.loaders.ChemistryDataLoader;
import org.jscience.physics.astronomy.SolarSystemLoader;
import org.jscience.physics.astronomy.StarSystem;
import org.jscience.ui.chemistry.MolecularViewer;
import org.jscience.ui.chemistry.PeriodicTableViewer;
import org.jscience.ui.physics.astronomy.StarSystemViewer;

/**
 * Unified JScience Demo Application.
 * <p>
 * Provides a tabbed interface to explore all JScience visualization panels.
 * Each panel demonstrates actual JScience functionality using real data.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class JScienceDemoApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize chemistry data
        ChemistryDataLoader.loadElements();

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Chemistry Tab
        Tab chemistryTab = new Tab("Chemistry");
        VBox chemistryPane = new VBox(15);
        chemistryPane.setPadding(new Insets(15));
        chemistryPane.getChildren().addAll(
                new Label("Interactive Periodic Table"),
                createLaunchButton("Periodic Table", this::launchPeriodicTable),
                new Separator(),
                new Label("Molecular Viewer - Water (H₂O)"),
                createLaunchButton("Water Molecule", () -> launchMolecule(ChemistryDataLoader.getMolecule("Water"))),
                new Separator(),
                new Label("Molecular Viewer - Methane (CH₄)"),
                createLaunchButton("Methane Molecule",
                        () -> launchMolecule(ChemistryDataLoader.getMolecule("Methane"))));
        chemistryTab.setContent(new ScrollPane(chemistryPane));

        // Astronomy Tab
        Tab astronomyTab = new Tab("Astronomy");
        VBox astronomyPane = new VBox(15);
        astronomyPane.setPadding(new Insets(15));
        astronomyPane.getChildren().addAll(
                new Label("Solar System - 3D Interactive View"),
                createLaunchButton("Solar System", this::launchSolarSystem));
        astronomyTab.setContent(astronomyPane);

        // Thermodynamics Tab
        Tab thermoTab = new Tab("Thermodynamics");
        VBox thermoPane = new VBox(15);
        thermoPane.setPadding(new Insets(15));
        thermoPane.getChildren().addAll(
                new Label("Thermodynamics Dashboard - Ideal Gas PV Diagram"),
                createLaunchButton("Dashboard", this::launchThermodynamics));
        thermoTab.setContent(thermoPane);

        // Electronics Tab
        Tab electronicsTab = new Tab("Electronics");
        VBox electronicsPane = new VBox(15);
        electronicsPane.setPadding(new Insets(15));
        electronicsPane.getChildren().addAll(
                new Label("Oscilloscope - Signal Generator"),
                createLaunchButton("Oscilloscope", this::launchOscilloscope));
        electronicsTab.setContent(electronicsPane);

        tabPane.getTabs().addAll(chemistryTab, astronomyTab, thermoTab, electronicsTab);

        BorderPane root = new BorderPane();
        root.setCenter(tabPane);

        Label footer = new Label("JScience Demo Application v5.0 - Using Real Scientific Data");
        footer.setStyle("-fx-padding: 8px; -fx-background-color: #2d3436; -fx-text-fill: white;");
        root.setBottom(footer);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("JScience Demo Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createLaunchButton(String text, Runnable action) {
        Button btn = new Button("Launch " + text);
        btn.setStyle("-fx-background-color: #0984e3; -fx-text-fill: white; -fx-padding: 8 16;");
        btn.setOnAction(e -> action.run());
        return btn;
    }

    private void launchPeriodicTable() {
        try {
            Stage stage = new Stage();
            PeriodicTableViewer viewer = new PeriodicTableViewer();
            viewer.start(stage);
        } catch (Exception e) {
            showError("Periodic Table", e);
        }
    }

    private void launchMolecule(Molecule mol) {
        try {
            Stage stage = new Stage();
            MolecularViewer viewer = new MolecularViewer(mol);

            BorderPane root = new BorderPane();
            root.setCenter(viewer.getView3D(600, 400));

            Scene scene = new Scene(root, 600, 400, true);
            scene.setFill(javafx.scene.paint.Color.DARKGRAY);
            stage.setTitle("Molecular Viewer: " + mol.getName());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showError("Molecular Viewer", e);
        }
    }

    private void launchSolarSystem() {
        try {
            Stage stage = new Stage();
            StarSystem system = SolarSystemLoader.load("/org/jscience/astronomy/solarsystem.json");
            StarSystemViewer.show(stage, system);
        } catch (Exception e) {
            showError("Solar System", e);
        }
    }

    private void launchThermodynamics() {
        try {
            Stage stage = new Stage();
            new org.jscience.ui.physics.thermodynamics.ThermodynamicsDashboard().start(stage);
        } catch (Exception e) {
            showError("Thermodynamics Dashboard", e);
        }
    }

    private void launchOscilloscope() {
        try {
            Stage stage = new Stage();
            new org.jscience.ui.physics.electronics.OscilloscopeViewer().start(stage);
        } catch (Exception e) {
            showError("Oscilloscope", e);
        }
    }

    private void showError(String component, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Failed to launch " + component);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
        e.printStackTrace();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
