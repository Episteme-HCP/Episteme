/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import org.jscience.device.sensors.Oscilloscope;
import org.jscience.device.sim.SimulatedOscilloscope;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.devices.OscilloscopeViewer;
import org.jscience.ui.i18n.I18n;

/**
 * Demo application showcasing simulated sensors and actuators in a tabbed view.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SensorActuatorDemo extends AbstractDemo {

    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.engineering", "Engineering");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("sensor.demo.title", "Sensor & Actuator Demo");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("sensor.demo.desc", "Showcase of simulated device viewers");
    }

    @Override
    protected String getLongDescription() {
        return I18n.getInstance().get("sensor.demo.longdesc",
                "A comprehensive demonstration of various simulated sensors and actuators available in the JScience framework.");
    }

    @Override
    protected Node createViewerNode() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Tab 1: Oscilloscope
        try {
            Oscilloscope simScope = new SimulatedOscilloscope("SimScope");
            OscilloscopeViewer viewer = new OscilloscopeViewer(simScope);
            viewer.play();
            Tab oscTab = new Tab(I18n.getInstance().get("sensor.tab.oscilloscope", "Oscilloscope"), viewer);
            tabPane.getTabs().add(oscTab);
        } catch (Exception e) {
            tabPane.getTabs().add(new Tab("Oscilloscope (Error)", new Label("Failed: " + e.getMessage())));
        }

        // Tab 2: Sensors List
        Tab sensorsTab = new Tab(I18n.getInstance().get("sensor.tab.sensors", "Sensors List"),
                new Label("TODO: List all simulated devices from org.jscience.device.sim"));
        tabPane.getTabs().add(sensorsTab);

        return tabPane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
