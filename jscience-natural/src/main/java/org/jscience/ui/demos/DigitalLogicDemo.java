/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.AbstractSimulationDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.computing.logic.DigitalLogicViewer;

/**
 * Digital Logic Circuit Simulator Demo.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DigitalLogicDemo extends AbstractSimulationDemo {

    private DigitalLogicViewer viewer;

    @Override
    public boolean isDemo() { return true; }

    @Override
    public String getCategory() { return I18n.getInstance().get("category.computing"); }

    @Override
    public String getName() { return I18n.getInstance().get("digital.title", "Digital Logic Simulator"); }

    @Override
    public String getDescription() { return I18n.getInstance().get("digital.desc", "Interactive Logic Circuit Simulator"); }
    
    @Override
    protected String getLongDescription() { 
        return "Design and simulate digital logic circuits using AND, OR, NOT, NAND gates."; 
    }

    @Override
    public Node createViewerNode() {
        if (viewer == null) viewer = new DigitalLogicViewer();
        return viewer;
    }

    // --- Simulatable Delegation ---

    @Override
    public void play() {
        if (viewer != null) viewer.play();
    }

    @Override
    public void pause() {
        if (viewer != null) viewer.pause();
    }

    @Override
    public void stop() {
        if (viewer != null) viewer.stop();
    }

    @Override
    public void step() {
        if (viewer != null) viewer.step();
    }

    @Override
    public void setSpeed(double speed) {
        if (viewer != null) viewer.setSpeed(speed);
    }

    @Override
    public boolean isPlaying() {
        return viewer != null && viewer.isPlaying();
    }
}
