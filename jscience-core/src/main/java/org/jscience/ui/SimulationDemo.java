/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 */
public abstract class SimulationDemo extends AbstractDemo {

    protected Button playBtn;
    protected Button pauseBtn;
    protected Button stopBtn;
    protected Button stepBtn;

    @Override
    protected VBox createControlPanel() {
        VBox panel = super.createControlPanel();

        if (viewer instanceof Simulatable simulatable) {
            panel.getChildren().add(new Separator());

            Label simTitle = new Label(I18n.getInstance().get("demo.simulation.title", "Simulation Control"));
            simTitle.setStyle("-fx-font-weight: bold;");
            panel.getChildren().add(simTitle);

            HBox vcrControls = new HBox(5);
            vcrControls.setAlignment(Pos.CENTER);

            playBtn = new Button("▶");
            pauseBtn = new Button("⏸");
            stopBtn = new Button("⏹");
            stepBtn = new Button("⏭");

            playBtn.setOnAction(e -> simulatable.play());
            pauseBtn.setOnAction(e -> simulatable.pause());
            stopBtn.setOnAction(e -> simulatable.stop());
            stepBtn.setOnAction(e -> simulatable.step());

            vcrControls.getChildren().addAll(playBtn, pauseBtn, stopBtn, stepBtn);
            panel.getChildren().add(vcrControls);

            // Speed control
            Label speedLbl = new Label("Speed:");
            Slider speedSlider = new Slider(0.1, 5.0, 1.0);
            speedSlider.valueProperty()
                    .addListener((obs, oldVal, newVal) -> simulatable.setSpeed(newVal.doubleValue()));
            panel.getChildren().addAll(speedLbl, speedSlider);
        }

        return panel;
    }
}
