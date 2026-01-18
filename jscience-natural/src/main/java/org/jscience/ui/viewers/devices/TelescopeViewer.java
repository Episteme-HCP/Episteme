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

package org.jscience.ui.viewers.devices;

import org.jscience.device.sim.SimulatedTelescope;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import org.jscience.ui.AbstractDeviceViewer;
import org.jscience.device.transducers.Telescope;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.Parameter;
import org.jscience.ui.NumericParameter;
import org.jscience.ui.ChoiceParameter;
import org.jscience.ui.BooleanParameter;
import java.util.ArrayList;
import java.util.List;

/**
 * JavaFX viewer for telescope control and position visualization.
 * Refactored to be 100% parameter-based.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TelescopeViewer extends AbstractDeviceViewer<Telescope> {

    // Display elements
    private Label raLabel;
    private Label decLabel;
    private Label statusLabel;
    private Canvas skyCanvas;

    // Parameters
    private double targetRA = 12.0;
    private double targetDec = 45.0;
    private String selectedPreset = "None";
    
    private final List<Parameter<?>> parameters = new ArrayList<>();

    // Current position for star field
    private double displayRA = 0;
    private double displayDec = 0;

    public TelescopeViewer(Telescope telescope) {
        super(telescope);
        setupParameters();
        initializeUI();
        connectTelescope();
        update();
    }

    private void setupParameters() {
        parameters.add(new NumericParameter("telescope.target.ra", I18n.getInstance().get("generated.telescope.target.ra.h", "Target RA (h)"), 0, 24, 0.01, targetRA, v -> targetRA = v));
        parameters.add(new NumericParameter("telescope.target.dec", I18n.getInstance().get("generated.telescope.target.dec", "Target Dec (°)"), -90, 90, 0.1, targetDec, v -> targetDec = v));
        
        List<String> presets = List.of("None", "Polaris", "Vega", "Betelgeuse", "Sirius");
        parameters.add(new ChoiceParameter("telescope.preset", I18n.getInstance().get("telescope.target", "Preset Target"), presets, "None", v -> {
            selectedPreset = v;
            switch(v) {
                case "Polaris" -> { targetRA = 2.53; targetDec = 89.26; }
                case "Vega" -> { targetRA = 18.62; targetDec = 38.78; }
                case "Betelgeuse" -> { targetRA = 5.92; targetDec = 7.41; }
                case "Sirius" -> { targetRA = 6.75; targetDec = -16.72; }
            }
        }));

        parameters.add(new BooleanParameter("telescope.slew", I18n.getInstance().get("telescope.btn.slew", "Slew to Target"), false, v -> {
            if (v) slewToTarget();
            else stopSlew();
        }));
    }

    private void initializeUI() {
        this.setPadding(new Insets(15));
        this.getStyleClass().add("viewer-root");

        // Status panel
        HBox statusBox = createStatusPanel();
        VBox topPanel = new VBox(10, statusBox);
        topPanel.setAlignment(Pos.CENTER);
        this.setTop(topPanel);

        // Sky view canvas
        skyCanvas = new Canvas(400, 400);
        drawSkyView();

        StackPane canvasPane = new StackPane(skyCanvas);
        canvasPane.getStyleClass().add("content-dark");
        setCenter(canvasPane);
        
        VBox infoPanel = new VBox(10);
        infoPanel.setPadding(new Insets(10));
        infoPanel.setPrefWidth(200);
        infoPanel.getStyleClass().add("viewer-sidebar");
        Label helpLabel = new Label(I18n.getInstance().get("viewer.telescope.desc", "Use parameters to slew."));
        helpLabel.setWrapText(true);
        infoPanel.getChildren().add(helpLabel);
        setRight(infoPanel);
    }

    private HBox createStatusPanel() {
        VBox raBox = new VBox(2);
        Label raTitle = new Label(I18n.getInstance().get("telescope.ra.title", "Current RA"));
        raTitle.setFont(Font.font("System", 10));
        raLabel = new Label("0.00h");
        raLabel.getStyleClass().add("font-bold");
        raBox.getChildren().addAll(raTitle, raLabel);
        raBox.setAlignment(Pos.CENTER);

        VBox decBox = new VBox(2);
        Label decTitle = new Label(I18n.getInstance().get("telescope.dec.title", "Current Dec"));
        decTitle.setFont(Font.font("System", 10));
        decLabel = new Label("0.00°");
        decLabel.getStyleClass().add("font-bold");
        decBox.getChildren().addAll(decTitle, decLabel);
        decBox.setAlignment(Pos.CENTER);

        statusLabel = new Label("Initializing...");
        statusLabel.getStyleClass().add("font-bold");

        HBox panel = new HBox(30, raBox, decBox, statusLabel);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(10));
        panel.getStyleClass().add("viewer-sidebar");

        return panel;
    }

    private void connectTelescope() {
        try {
            device.connect();
            if (device instanceof SimulatedTelescope simulated) {
                simulated.setPositionCallback(this::updatePosition);
            }
            updateStatusDisplay();
        } catch (Exception e) {
            statusLabel.setText("ERROR");
        }
    }

    private void slewToTarget() {
        try {
            device.slewTo(targetRA, targetDec);
            updateStatusDisplay();
        } catch (Exception e) {
            statusLabel.setText("SLEW ERROR");
        }
    }

    private void stopSlew() {
        try {
            device.abort();
            updateStatusDisplay();
        } catch (Exception e) {}
    }

    private void updatePosition(Double ra, Double dec) {
        Platform.runLater(() -> {
            displayRA = ra;
            displayDec = dec;
            raLabel.setText(String.format("%.2fh", ra));
            decLabel.setText(String.format("%.2f°", dec));
            updateStatusDisplay();
            drawSkyView();
        });
    }

    @Override
    public void update() {
        updateStatusDisplay();
        drawSkyView();
    }

    private void updateStatusDisplay() {
        String status = device.getStatus();
        statusLabel.setText(status);
        if ("TRACKING".equals(status)) statusLabel.setTextFill(Color.LIME);
        else if ("SLEWING".equals(status)) statusLabel.setTextFill(Color.CYAN);
        else statusLabel.setTextFill(Color.ORANGE);
    }

    private void drawSkyView() {
        GraphicsContext gc = skyCanvas.getGraphicsContext2D();
        double w = skyCanvas.getWidth(), h = skyCanvas.getHeight(), cx = w / 2, cy = h / 2;

        gc.setFill(Color.rgb(0, 0, 20));
        gc.fillOval(0, 0, w, h);

        gc.setFill(Color.WHITE);
        java.util.Random rand = new java.util.Random((long) (displayRA * 1000 + displayDec));
        for (int i = 0; i < 50; i++) {
            double x = rand.nextDouble() * w, y = rand.nextDouble() * h;
            double size = 1 + rand.nextDouble() * 2;
            gc.fillOval(x, y, size, size);
        }

        gc.setStroke(Color.RED);
        gc.strokeLine(cx - 20, cy, cx - 5, cy);
        gc.strokeLine(cx + 5, cy, cx + 20, cy);
        gc.strokeLine(cx, cy - 20, cx, cy - 5);
        gc.strokeLine(cx, cy + 5, cx, cy + 20);

        gc.setStroke(Color.rgb(50, 50, 100));
        gc.strokeOval(2, 2, w - 4, h - 4);
    }

    @Override public String getCategory() { return I18n.getInstance().get("category.physics", "Physics"); }
    @Override public String getName() { return I18n.getInstance().get("viewer.telescope.name", "Telescope"); }
    @Override public String getDescription() { return I18n.getInstance().get("viewer.telescope.desc", "Telescope control viewer."); }
    @Override public String getLongDescription() { return I18n.getInstance().get("viewer.telescope.longdesc", "Telescope control and monitoring."); }
    
    @Override public List<Parameter<?>> getViewerParameters() { return parameters; }
}
