/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.ui.demos;

import org.jscience.ui.i18n.I18n;
import org.jscience.ui.devices.OscilloscopeViewer;
import org.jscience.ui.devices.PressureGaugeViewer;
import org.jscience.ui.devices.TelescopeViewer;
import org.jscience.ui.devices.ThermometerViewer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Unified demonstration application for all device viewers.
 * <p>
 * Provides a tabbed interface to explore different simulated devices
 * including thermometers, pressure gauges, oscilloscopes, and telescopes.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
import org.jscience.ui.DemoProvider;

public class DeviceDemo extends Application implements DemoProvider {

    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.engineering");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("device.title");
    }

    @Override
    public String getDescription() {
        return "Demonstration of simulated devices.";
    }

    @Override
    public void show(Stage stage) {
        start(stage);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(I18n.getInstance().get("device.title"));

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getStyleClass().add("demo-tab-pane");

        // Thermometer Tab
        Tab thermometerTab = new Tab(I18n.getInstance().get("device.tab.thermometer"));
        thermometerTab.setContent(createThermometerPanel());
        tabPane.getTabs().add(thermometerTab);

        // Pressure Gauge Tab
        Tab pressureTab = new Tab(I18n.getInstance().get("device.tab.pressure"));
        pressureTab.setContent(createPressureGaugePanel());
        tabPane.getTabs().add(pressureTab);

        // Oscilloscope Tab
        Tab scopeTab = new Tab(I18n.getInstance().get("device.tab.oscilloscope"));
        scopeTab.setContent(createOscilloscopePanel());
        tabPane.getTabs().add(scopeTab);

        // Telescope Tab
        Tab telescopeTab = new Tab(I18n.getInstance().get("device.tab.telescope"));
        telescopeTab.setContent(createTelescopePanel());
        tabPane.getTabs().add(telescopeTab);

        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BorderPane createThermometerPanel() {
        BorderPane panel = new BorderPane();
        panel.setPadding(new Insets(20));
        panel.getStyleClass().add("demo-panel");

        try {
            ThermometerViewer viewer = new ThermometerViewer();
            panel.setCenter(viewer.getView());

            // Controls
            VBox controls = new VBox(10);
            controls.setPadding(new Insets(10));
            controls.getStyleClass().add("demo-controls");

            Label infoLabel = new Label(I18n.getInstance().get("device.thermometer.info"));
            infoLabel.getStyleClass().add("dark-label");

            Slider tempSlider = new Slider(-20, 50, 20);
            tempSlider.setShowTickLabels(true);
            tempSlider.setShowTickMarks(true);
            tempSlider.setMajorTickUnit(10);

            Label sliderLabel = new Label(I18n.getInstance().get("device.thermometer.label"));
            sliderLabel.getStyleClass().add("dark-label-muted");

            controls.getChildren().addAll(infoLabel, sliderLabel, tempSlider);
            panel.setBottom(controls);

        } catch (Exception e) {
            panel.setCenter(new Label(I18n.getInstance().get("device.thermometer.error") + " " + e.getMessage()));
        }

        return panel;
    }

    private BorderPane createPressureGaugePanel() {
        BorderPane panel = new BorderPane();
        panel.setPadding(new Insets(20));
        panel.getStyleClass().add("demo-panel");

        try {
            PressureGaugeViewer viewer = new PressureGaugeViewer();
            panel.setCenter(viewer.getView());

            // Controls
            VBox controls = new VBox(10);
            controls.setPadding(new Insets(10));
            controls.getStyleClass().add("demo-controls");

            Label infoLabel = new Label(I18n.getInstance().get("device.pressure.info"));
            infoLabel.getStyleClass().add("dark-label");

            Slider pressureSlider = new Slider(0, 10, 5);
            pressureSlider.setShowTickLabels(true);
            pressureSlider.setShowTickMarks(true);
            pressureSlider.setMajorTickUnit(2);

            Label sliderLabel = new Label(I18n.getInstance().get("device.pressure.label"));
            sliderLabel.getStyleClass().add("dark-label-muted");

            controls.getChildren().addAll(infoLabel, sliderLabel, pressureSlider);
            panel.setBottom(controls);

        } catch (Exception e) {
            panel.setCenter(new Label(I18n.getInstance().get("device.pressure.error") + " " + e.getMessage()));
        }

        return panel;
    }

    private BorderPane createOscilloscopePanel() {
        BorderPane panel = new BorderPane();
        panel.setPadding(new Insets(20));
        panel.getStyleClass().add("demo-panel");

        try {
            OscilloscopeViewer viewer = new OscilloscopeViewer();
            panel.setCenter(viewer.getView());

            // Controls
            VBox controls = new VBox(10);
            controls.setPadding(new Insets(10));
            controls.getStyleClass().add("demo-controls");

            Label infoLabel = new Label(I18n.getInstance().get("device.oscilloscope.info"));
            infoLabel.getStyleClass().add("dark-label");

            HBox waveformControls = new HBox(10);
            ComboBox<String> waveformType = new ComboBox<>();
            waveformType.getItems().addAll("Sine", "Square", "Sawtooth", "Triangle");
            waveformType.setValue("Sine");

            Slider freqSlider = new Slider(1, 100, 10);
            freqSlider.setShowTickLabels(true);
            Label freqLabel = new Label(I18n.getInstance().get("device.oscilloscope.freq"));
            freqLabel.getStyleClass().add("dark-label-muted");

            waveformControls.getChildren().addAll(new Label(I18n.getInstance().get("device.oscilloscope.waveform")),
                    waveformType, freqLabel, freqSlider);
            waveformControls.getChildren().filtered(n -> n instanceof Label)
                    .forEach(n -> ((Label) n).getStyleClass().add("dark-label-muted"));

            controls.getChildren().addAll(infoLabel, waveformControls);
            panel.setBottom(controls);

        } catch (Exception e) {
            panel.setCenter(new Label(I18n.getInstance().get("device.oscilloscope.error") + " " + e.getMessage()));
        }

        return panel;
    }

    private BorderPane createTelescopePanel() {
        BorderPane panel = new BorderPane();
        panel.setPadding(new Insets(10));
        panel.getStyleClass().add("demo-panel");

        try {
            TelescopeViewer viewer = new TelescopeViewer();
            panel.setCenter(viewer);
        } catch (Exception e) {
            Label errorLabel = new Label(I18n.getInstance().get("device.telescope.error") + " " + e.getMessage());
            errorLabel.getStyleClass().add("dark-label-accent");
            panel.setCenter(errorLabel);
        }

        return panel;
    }

    /**
     * Main entry point.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
