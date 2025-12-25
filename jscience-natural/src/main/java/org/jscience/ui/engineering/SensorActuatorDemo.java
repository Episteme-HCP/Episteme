package org.jscience.ui.engineering;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jscience.ui.devices.OscilloscopeDisplay;
import org.jscience.devices.sim.SimulatedOscilloscope;
import org.jscience.natural.i18n.I18n;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.Slider;
import javafx.geometry.Insets;

/**
 * Demo application showcasing simulated sensors and actuators in a tabbed view.
 */
public class SensorActuatorDemo extends Application {

    @Override
    public void start(Stage stage) {
        TabPane tabPane = new TabPane();

        // Tab 1: Oscilloscope
        Tab oscTab = new Tab(I18n.getInstance().get("sensor.tab.oscilloscope"), createOscilloscopeView());
        oscTab.setClosable(false);

        // Tab 2: Sensors List (Placeholder for now)
        Tab sensorsTab = new Tab(I18n.getInstance().get("sensor.tab.sensors"),
                new Label("TODO: List all simulated devices from org.jscience.devices.sim"));
        sensorsTab.setClosable(false);

        tabPane.getTabs().addAll(oscTab, sensorsTab);

        BorderPane root = new BorderPane();
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 900, 600);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(I18n.getInstance().get("sensor.demo.title"));
        stage.setScene(scene);
        stage.show();
    }

    private BorderPane createOscilloscopeView() {
        OscilloscopeDisplay display = new OscilloscopeDisplay();
        display.setPrefSize(800, 400);

        SimulatedOscilloscope simScope = new SimulatedOscilloscope("SimScope");

        // Controls
        Slider freqSlider = new Slider(0.1, 10.0, 1.0);
        freqSlider.setShowTickLabels(true);
        freqSlider.setShowTickMarks(true);
        Label freqLabel = new Label(
                I18n.getInstance().get("sensor.freq.label") + ": 1.0 " + I18n.getInstance().get("sensor.freq.unit"));
        freqSlider.valueProperty().addListener((obs, old, newV) -> {
            // In a real sim, we'd set this on a SignalGenerator connected to the Scope
            // For now, simpler demo
            freqLabel.setText(String.format(I18n.getInstance().get("sensor.freq.label") + ": %.1f "
                    + I18n.getInstance().get("sensor.freq.unit"), newV.doubleValue()));
        });

        VBox controls = new VBox(10, new Label(I18n.getInstance().get("sensor.freq.label")), freqSlider, freqLabel);
        controls.setPadding(new Insets(10));

        BorderPane pane = new BorderPane();
        pane.setCenter(display);
        pane.setBottom(controls);

        // Capture Loop
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                double[] wave = simScope.captureWaveform(0);
                if (wave != null) {
                    for (double v : wave) {
                        display.addData(v);
                    }
                }
            }
        }.start();

        return pane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
