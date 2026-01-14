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

package org.jscience.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jscience.ui.i18n.I18n;

/**
 * Base class for simulation-based demos with VCR controls.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class AbstractSimulationDemo extends AbstractDemo implements Simulatable {

    protected Button playBtn;
    protected Button pauseBtn;
    protected Button stopBtn;
    protected Button stepBtn;
    @Override
    public void play() {
        if (viewer instanceof Simulatable simulatable) {
            simulatable.play();
        }
    }

    @Override
    public void pause() {
        if (viewer instanceof Simulatable simulatable) {
            simulatable.pause();
        }
    }

    @Override
    public void stop() {
        if (viewer instanceof Simulatable simulatable) {
            simulatable.stop();
        }
    }

    @Override
    public void step() {
        if (viewer instanceof Simulatable simulatable) {
            simulatable.step();
        }
    }

    @Override
    public void setSpeed(double multiplier) {
        if (viewer instanceof Simulatable simulatable) {
            simulatable.setSpeed(multiplier);
        }
    }

    @Override
    public boolean isPlaying() {
        if (viewer instanceof Simulatable simulatable) {
            return simulatable.isPlaying();
        }
        return false;
    }
    protected VBox createControlPanel() {
        VBox panel = super.createControlPanel();

        if (viewer instanceof Simulatable simulatable) {
            panel.getChildren().add(new Separator());

            Label simTitle = new Label(I18n.getInstance().get("demo.simulation.title", "Simulation Control"));
            simTitle.setStyle("-fx-font-weight: bold;");
            panel.getChildren().add(simTitle);

            HBox vcrControls = new HBox(5);
            vcrControls.setAlignment(Pos.CENTER);

            playBtn = new Button("Ã¢â€“Â¶");
            pauseBtn = new Button("Ã¢ÂÂ¸");
            stopBtn = new Button("Ã¢ÂÂ¹");
            stepBtn = new Button("Ã¢ÂÂ­");

            playBtn.setOnAction(e -> simulatable.play());
            pauseBtn.setOnAction(e -> simulatable.pause());
            stopBtn.setOnAction(e -> simulatable.stop());
            stepBtn.setOnAction(e -> simulatable.step());

            vcrControls.getChildren().addAll(playBtn, pauseBtn, stopBtn, stepBtn);
            panel.getChildren().add(vcrControls);

            // Speed control
            Label speedLbl = new Label(I18n.getInstance().get("demo.simulation.speed", "Speed:"));
            Slider speedSlider = new Slider(0.1, 5.0, 1.0);
            speedSlider.valueProperty()
                    .addListener((obs, oldVal, newVal) -> simulatable.setSpeed(newVal.doubleValue()));
            panel.getChildren().addAll(speedLbl, speedSlider);
        }

        return panel;
    }
}
