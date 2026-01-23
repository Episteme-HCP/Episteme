package org.jscience.apps.biology;

import javafx.animation.AnimationTimer;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.jscience.apps.framework.FeaturedAppBase;
import org.jscience.ui.viewers.shared.scientific.ScientificProcessViewer;
import org.jscience.ui.viewers.shared.SpatialFluxViewer;
import org.jscience.biology.loaders.BiologicalResourceReader;
import org.jscience.biology.ProteinFolding;
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
