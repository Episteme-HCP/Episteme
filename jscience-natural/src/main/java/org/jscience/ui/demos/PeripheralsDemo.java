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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.physics.waves.SpectrographViewer;
import org.jscience.device.sim.SimulatedOscilloscope;
import org.jscience.ui.devices.OscilloscopeViewer;
import org.jscience.ui.devices.VitalMonitorViewer;

/**
 * Unified demonstration of all scientific viewers and devices.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PeripheralsDemo extends AbstractDemo {

    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.engineering", "Engineering");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("demo.peripherals.title", "Peripherals");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("demo.peripherals.desc", "Explore all scientific instruments in one place");
    }

    @Override
    protected String getLongDescription() {
        return "The Peripherals panel provides a centralized interface to interact with all scientific viewers " +
                "available in JScience. Use the tabs below to switch between instruments. " +
                "Each instrument exposes its own parameters and simulation controls for real-time analysis.";
    }

    @Override
    protected Node createViewerNode() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Spectrograph
        try {
            SpectrographViewer spectro = new SpectrographViewer();
            spectro.play();
            Tab spectroTab = new Tab(I18n.getInstance().get("spectrograph.title", "Spectrograph"), spectro);
            tabPane.getTabs().add(spectroTab);
        } catch (Exception e) {
            e.printStackTrace();
            tabPane.getTabs().add(new Tab("Spectrograph (Error)",
                    new javafx.scene.control.Label("Failed to load: " + e.getMessage())));
        }

        // Vital Monitor
        try {
            VitalMonitorViewer vital = new VitalMonitorViewer();
            vital.play();
            Tab vitalTab = new Tab(I18n.getInstance().get("vital.title", "Vital Monitor"), vital);
            tabPane.getTabs().add(vitalTab);
        } catch (Exception e) {
            e.printStackTrace();
            tabPane.getTabs().add(new Tab("Vital Monitor (Error)",
                    new javafx.scene.control.Label("Failed to load: " + e.getMessage())));
        }

        // Oscilloscope
        try {
            OscilloscopeViewer osc = new OscilloscopeViewer(new SimulatedOscilloscope("DemoScope"));
            osc.play();
            Tab oscTab = new Tab(I18n.getInstance().get("oscilloscope.title", "Oscilloscope"), osc);
            tabPane.getTabs().add(oscTab);
        } catch (Exception e) {
            e.printStackTrace();
            tabPane.getTabs().add(new Tab("Oscilloscope (Error)",
                    new javafx.scene.control.Label("Failed to load: " + e.getMessage())));
        }

        return tabPane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}


