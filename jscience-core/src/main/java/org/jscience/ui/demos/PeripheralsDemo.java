/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
import javafx.scene.control.Label;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.MasterControlDiscovery;
import java.util.List;

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
        return I18n.getInstance().get("demo.peripherals.name", "Peripherals Demo");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("demo.peripherals.desc", "Explore all scientific instruments in one place");
    }

    @Override
    public String getLongDescription() {
        return I18n.getInstance().get("demo.peripherals.longdesc", "Interact with various simulated peripherals and sensors including Oscilloscopes, Vital Monitors, and Spectrographs.");
    }

    @Override
    protected Node createViewerNode() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        MasterControlDiscovery discovery = MasterControlDiscovery.getInstance();
        List<MasterControlDiscovery.ClassInfo> viewerClasses = discovery.findClasses("Viewer");

        for (MasterControlDiscovery.ClassInfo info : viewerClasses) {
            // Only include viewers from the devices package
            if (info.fullName.contains(".ui.viewers.devices.")) {
                try {
                    Class<?> cls = Class.forName(info.fullName);
                    Object instance = null;
                    
                    // Try to find a default constructor
                    try {
                        instance = cls.getDeclaredConstructor().newInstance();
                    } catch (NoSuchMethodException e) {
                        // Special case: OscilloscopeViewer (using reflection to avoid dependency)
                        if (cls.getSimpleName().equals("OscilloscopeViewer")) {
                            try {
                                Class<?> scopeCls = Class.forName("org.jscience.device.sim.SimulatedOscilloscope");
                                Object scope = scopeCls.getConstructor(String.class).newInstance("DefaultScope");
                                instance = cls.getConstructor(scopeCls).newInstance(scope);
                            } catch (Exception ex) {
                                // Skip if SimulatedOscilloscope not found
                                continue;
                            }
                        } else {
                            continue; // Skip viewers we don't know how to instantiate
                        }
                    }

                    if (instance instanceof org.jscience.ui.Viewer v) {
                        Tab tab = new Tab(v.getName(), v instanceof Node n ? n : new Label("Not a Node"));
                        tabPane.getTabs().add(tab);
                        if (instance instanceof org.jscience.ui.Simulatable s) {
                            s.play();
                        }
                    }
                } catch (Exception e) {
                    logger.error("Failed to load discovered viewer: " + info.fullName, e);
                }
            }
        }

        // Add Spectrograph as a special case via discovery if not manually added
        List<MasterControlDiscovery.ClassInfo> specInfo = discovery.findClasses("SpectrographViewer");
        if (!specInfo.isEmpty()) {
            try {
                Class<?> cls = Class.forName(specInfo.get(0).fullName);
                Object instance = cls.getDeclaredConstructor().newInstance();
                if (instance instanceof org.jscience.ui.Viewer v) {
                    Tab tab = new Tab(v.getName(), (Node)instance);
                    tabPane.getTabs().add(tab);
                    if (instance instanceof org.jscience.ui.Simulatable s) {
                        s.play();
                    }
                }
            } catch (Exception e) {
                // Ignore
            }
        }

        return tabPane;
    }

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PeripheralsDemo.class);

    public static void main(String[] args) {
        launch(args);
    }
}
