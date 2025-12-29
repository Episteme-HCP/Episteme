package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.SimulationDemo;
import org.jscience.ui.physics.waves.SpectrographViewer;
import org.jscience.ui.i18n.I18n;

public class SpectrographDemo extends SimulationDemo {
    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.physics");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("spectrograph.title");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("spectrograph.short_desc", "Real-time frequency analysis");
    }

    @Override
    protected String getLongDescription() {
        return "This demonstration showcases a real-time spectrograph that performs frequency analysis on various signal patterns. "
                +
                "It features two visualizations: a standard frequency spectrum (top) and a scrolling spectrogram (bottom) which "
                +
                "displays frequency intensity over time using a color heatmap. You can switch between a primitive mathematical "
                +
                "engine and a physics-based engine using the parameters on the right.";
    }

    @Override
    protected Node createViewerNode() {
        SpectrographViewer sv = new SpectrographViewer();
        sv.play(); // Start by default
        return sv;
    }
}
