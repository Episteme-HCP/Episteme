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

package org.jscience.ui;

import org.jscience.ui.i18n.I18n;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;

/**
 * Abstract base class for simulation demos that require playback controls.
 * It implements Simulatable by delegating to the viewer (if applicable).
 * Subclasses can override these methods if they manage simulation logic differently.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class AbstractSimulationDemo extends AbstractDemo implements Simulatable {

    @Override
    protected VBox createControlPanel() {
        VBox panel = super.createControlPanel();
        
        ToolBar toolbar = new ToolBar();
        Button btnPlay = new Button(I18n.getInstance().get("demo.control.play", "Play"));
        Button btnPause = new Button(I18n.getInstance().get("demo.control.pause", "Pause"));
        Button btnStop = new Button(I18n.getInstance().get("demo.control.stop", "Reset"));
        
        btnPlay.setOnAction(e -> play());
        btnPause.setOnAction(e -> pause());
        btnStop.setOnAction(e -> stop());
        
        toolbar.getItems().addAll(btnPlay, btnPause, btnStop);
        
        // Insert at top of control panel
        if (!panel.getChildren().isEmpty()) {
             panel.getChildren().add(0, new Separator());
             panel.getChildren().add(0, toolbar);
        } else {
             panel.getChildren().add(toolbar);
        }
        
        return panel;
    }

    // Default delegation to viewer
    @Override
    public void play() {
        if (viewer instanceof Simulatable) ((Simulatable) viewer).play();
    }

    @Override
    public void pause() {
        if (viewer instanceof Simulatable) ((Simulatable) viewer).pause();
    }

    @Override
    public void stop() {
        if (viewer instanceof Simulatable) ((Simulatable) viewer).stop();
    }
    
    @Override
    public void step() {
        if (viewer instanceof Simulatable) ((Simulatable) viewer).step();
    }

    @Override
    public boolean isPlaying() {
        if (viewer instanceof Simulatable) return ((Simulatable) viewer).isPlaying();
        return false;
    }

    @Override
    public void setSpeed(double speed) {
        if (viewer instanceof Simulatable) ((Simulatable) viewer).setSpeed(speed);
    }
}
