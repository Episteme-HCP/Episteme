package org.jscience.ui.engineering;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jscience.devices.sensors.Oscilloscope;
import org.jscience.devices.sim.SimulatedOscilloscope;
import org.jscience.ui.devices.OscilloscopeViewer;
import org.jscience.ui.i18n.I18n;
import javafx.scene.control.Label;

/**
 * Demo application showcasing simulated sensors and actuators in a tabbed view.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SensorActuatorDemo extends Application {

    @Override
    public void start(Stage stage) {
        TabPane tabPane = new TabPane();

        // Tab 1: Oscilloscope - uses the refactored OscilloscopeViewer with interface
        Oscilloscope simScope = new SimulatedOscilloscope("SimScope");
        OscilloscopeViewer viewer = new OscilloscopeViewer(simScope);
        Tab oscTab = new Tab(I18n.getInstance().get("sensor.tab.oscilloscope"), viewer.getView());
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

    public static void main(String[] args) {
        launch(args);
    }
}
