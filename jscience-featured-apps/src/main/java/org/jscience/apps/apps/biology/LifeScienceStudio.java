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

package org.jscience.apps.apps.biology;

import javafx.animation.AnimationTimer;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.jscience.apps.apps.framework.FeaturedAppBase;
import org.jscience.natural.ui.viewers.shared.scientific.ScientificProcessViewer;
import org.jscience.social.ui.viewers.geography.SpatialFluxViewer;
import org.jscience.natural.biology.loaders.BiologicalResourceReader;
import org.jscience.natural.biology.ProteinFolding;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Life Science Studio: A consolidated environment for biological and ecological simulation.
 */
public final class LifeScienceStudio extends FeaturedAppBase {

    private ScientificProcessViewer energyViewer;
    private SpatialFluxViewer hazardViewer;
    private ProteinFolding simulation;
    private double timeStep = 0;

    @Override
    protected String getAppTitle() {
        return "Life Science Studio - JScience";
    }

    @Override
    protected Region createMainContent() {
        TabPane tabs = new TabPane();

        // Tab 1: Folding Monitor
        energyViewer = new ScientificProcessViewer();
        Tab foldingTab = new Tab("Protein Folding Energy", energyViewer);
        foldingTab.setClosable(false);

        // Tab 2: Ecological Hazard
        hazardViewer = new SpatialFluxViewer();
        Tab hazardTab = new Tab("Ecological Risk Map", hazardViewer);
        hazardTab.setClosable(false);

        tabs.getTabs().addAll(foldingTab, hazardTab);
        return tabs;
    }

    @Override
    protected void onAppReady() {
        setStatus("Initializing Simulation Engines...");
        
        try {
            BiologicalResourceReader bioReader = new BiologicalResourceReader();
            simulation = bioReader.load("ProteinAlpha");
            
            // For now, hazardViewer will be empty or we'll add a reader later
        } catch (Exception e) {
             Logger.getGlobal().log(Level.SEVERE, "Failed to load biological data", e);
        }
        
        setStatus("Ready.");
    }

    @Override
    public void onRun() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                simulation.step(0.1); // Simulated Annealing step
                energyViewer.addTrace("Energy", "Potential Energy", timeStep++, simulation.calculateTotalEnergy());
                if (timeStep > 100) stop(); // Automated demo run for 100 steps
            }
        }.start();
        setStatus("Simulation Streaming...");
    }

    @Override
    public String getCategory() {
        return "Natural Sciences";
    }

    @Override
    public String getDescription() {
        return "Integrated biological and ecological simulation suite.";
    }
}
