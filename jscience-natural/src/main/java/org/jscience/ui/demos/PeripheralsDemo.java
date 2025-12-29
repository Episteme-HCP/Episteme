/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.devices.OscilloscopeViewer;
import org.jscience.ui.devices.VitalMonitorViewer;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.physics.waves.SpectrographViewer;

/**
 * Unified demonstration of all scientific viewers and devices.
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
            OscilloscopeViewer osc = new OscilloscopeViewer(
                    new org.jscience.device.sim.SimulatedOscilloscope("DemoScope"));
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
