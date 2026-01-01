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

package org.jscience.ui.devices;

import org.jscience.device.sim.SimulatedTelescope;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import org.jscience.ui.i18n.I18n;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * JavaFX viewer for telescope control and position visualization.
 * <p>
 * Displays current pointing coordinates (RA/Dec), status, and provides
 * controls for slewing to targets.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TelescopeViewer extends BorderPane {

    private final SimulatedTelescope telescope;

    // Display elements
    private Label raLabel;
    private Label decLabel;
    private Label statusLabel;
    private Canvas skyCanvas;

    // Input fields
    private TextField raInput;
    private TextField decInput;

    // Current position for star field
    private double displayRA = 0;
    private double displayDec = 0;

    public TelescopeViewer() {
        this(new SimulatedTelescope());
    }

    public TelescopeViewer(SimulatedTelescope telescope) {
        this.telescope = telescope;
        initializeUI();
        connectTelescope();
    }

    private void initializeUI() {
        setPadding(new Insets(15));
        getStyleClass().add("dark-viewer-root");

        // Title
        Label title = new Label(I18n.getInstance().get("telescope.title"));
        title.setFont(Font.font("System", FontWeight.BOLD, 18));

        // Status panel
        HBox statusBox = createStatusPanel();

        VBox topPanel = new VBox(10, title, statusBox);
        topPanel.setAlignment(Pos.CENTER);
        setTop(topPanel);

        // Sky view canvas
        skyCanvas = new Canvas(300, 300);
        drawSkyView();

        StackPane canvasPane = new StackPane(skyCanvas);
        canvasPane.getStyleClass().add("dark-viewer-root");
        setCenter(canvasPane);
        BorderPane.setMargin(canvasPane, new Insets(15));

        // Control panel
        VBox controlPanel = createControlPanel();
        setBottom(controlPanel);
    }

    private HBox createStatusPanel() {
        // RA display
        VBox raBox = new VBox(2);
        Label raTitle = new Label(I18n.getInstance().get("telescope.ra.title"));

        raTitle.setFont(Font.font("System", 10));
        raLabel = new Label(I18n.getInstance().get("telescope.ra.default"));

        raLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 14));
        raBox.getChildren().addAll(raTitle, raLabel);
        raBox.setAlignment(Pos.CENTER);

        // Dec display
        VBox decBox = new VBox(2);
        Label decTitle = new Label(I18n.getInstance().get("telescope.dec.title"));

        decTitle.setFont(Font.font("System", 10));
        decLabel = new Label(I18n.getInstance().get("telescope.dec.default"));

        decLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 14));
        decBox.getChildren().addAll(decTitle, decLabel);
        decBox.setAlignment(Pos.CENTER);

        // Status
        VBox statusBox = new VBox(2);
        Label statusTitle = new Label(I18n.getInstance().get("telescope.status.title"));

        statusTitle.setFont(Font.font("System", 10));
        statusLabel = new Label(I18n.getInstance().get("telescope.status.disconnected"));
        statusLabel.setTextFill(Color.ORANGE);
        statusLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        statusBox.getChildren().addAll(statusTitle, statusLabel);
        statusBox.setAlignment(Pos.CENTER);

        HBox panel = new HBox(30, raBox, decBox, statusBox);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(10));
        panel.getStyleClass().add("dark-viewer-sidebar");

        return panel;
    }

    private VBox createControlPanel() {
        // Target input
        Label targetLabel = new Label(I18n.getInstance().get("telescope.target"));

        targetLabel.setFont(Font.font("System", FontWeight.BOLD, 12));

        HBox inputRow = new HBox(10);
        inputRow.setAlignment(Pos.CENTER);

        raInput = new TextField("12.0");
        raInput.setPrefWidth(80);
        raInput.setPromptText("RA (h)");

        decInput = new TextField("45.0");
        decInput.setPrefWidth(80);
        decInput.setPromptText("Dec (Ã‚Â°)");

        Button slewBtn = new Button(I18n.getInstance().get("telescope.btn.slew"));
        slewBtn.getStyleClass().add("accent-button-blue");
        slewBtn.setOnAction(e -> slewToTarget());

        Button stopBtn = new Button(I18n.getInstance().get("telescope.btn.stop"));
        stopBtn.getStyleClass().add("accent-button-red");
        stopBtn.setOnAction(e -> stopSlew());

        inputRow.getChildren().addAll(
                new Label("RA:"), raInput,
                new Label("Dec:"), decInput,
                slewBtn, stopBtn);
        inputRow.getChildren().filtered(n -> n instanceof Label)
                .forEach(n -> ((Label) n).setTextFill(Color.LIGHTGRAY));

        // Preset buttons
        HBox presetRow = new HBox(10);
        presetRow.setAlignment(Pos.CENTER);

        presetRow.getChildren().addAll(
                createPresetButton("Polaris", 2.53, 89.26),
                createPresetButton("Vega", 18.62, 38.78),
                createPresetButton("Betelgeuse", 5.92, 7.41),
                createPresetButton("Sirius", 6.75, -16.72));

        VBox panel = new VBox(10, targetLabel, inputRow, presetRow);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(15));
        panel.getStyleClass().add("dark-viewer-sidebar");

        return panel;
    }

    private Button createPresetButton(String name, double ra, double dec) {
        Button btn = new Button(name);
        btn.getStyleClass().add("accent-button-blue");
        btn.setOnAction(e -> {
            raInput.setText(String.format("%.2f", ra));
            decInput.setText(String.format("%.2f", dec));
            slewToTarget();
        });
        return btn;
    }

    private void connectTelescope() {
        try {
            telescope.connect();
            telescope.setPositionCallback(this::updatePosition);
            updateStatusDisplay();
        } catch (Exception e) {
            statusLabel.setText(I18n.getInstance().get("telescope.status.error"));
            statusLabel.setTextFill(Color.RED);
        }
    }

    private void slewToTarget() {
        try {
            double ra = Double.parseDouble(raInput.getText().trim());
            double dec = Double.parseDouble(decInput.getText().trim());
            telescope.slewTo(ra, dec);
            updateStatusDisplay();
        } catch (NumberFormatException e) {
            statusLabel.setText(I18n.getInstance().get("telescope.status.invalid"));
            statusLabel.setTextFill(Color.RED);
        } catch (Exception e) {
            statusLabel.setText(I18n.getInstance().get("telescope.status.slew_error"));
            statusLabel.setTextFill(Color.RED);
        }
    }

    private void stopSlew() {
        try {
            telescope.abort();
            updateStatusDisplay();
        } catch (Exception e) {
            // Ignore
        }
    }

    private void updatePosition(Double ra, Double dec) {
        Platform.runLater(() -> {
            displayRA = ra;
            displayDec = dec;
            raLabel.setText(SimulatedTelescope.formatRA(ra));
            decLabel.setText(SimulatedTelescope.formatDec(dec));
            updateStatusDisplay();
            drawSkyView();
        });
    }

    private void updateStatusDisplay() {
        String status = telescope.getStatus();
        statusLabel.setText(status);
        switch (status) {
            case "TRACKING":
                statusLabel.setTextFill(Color.LIME);
                break;
            case "SLEWING":
                statusLabel.setTextFill(Color.YELLOW);
                break;
            default:
                statusLabel.setTextFill(Color.ORANGE);
        }
    }

    private void drawSkyView() {
        GraphicsContext gc = skyCanvas.getGraphicsContext2D();
        double w = skyCanvas.getWidth();
        double h = skyCanvas.getHeight();
        double cx = w / 2;
        double cy = h / 2;

        // Dark sky background
        gc.setFill(Color.rgb(0, 0, 20));
        gc.fillOval(0, 0, w, h);

        // Draw some simulated stars
        gc.setFill(Color.WHITE);
        java.util.Random rand = new java.util.Random((long) (displayRA * 1000 + displayDec));
        for (int i = 0; i < 50; i++) {
            double x = rand.nextDouble() * w;
            double y = rand.nextDouble() * h;
            double size = 1 + rand.nextDouble() * 2;
            gc.fillOval(x, y, size, size);
        }

        // Crosshair
        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        gc.strokeLine(cx - 20, cy, cx - 5, cy);
        gc.strokeLine(cx + 5, cy, cx + 20, cy);
        gc.strokeLine(cx, cy - 20, cx, cy - 5);
        gc.strokeLine(cx, cy + 5, cx, cy + 20);

        // Outer circle
        gc.setStroke(Color.rgb(50, 50, 100));
        gc.strokeOval(2, 2, w - 4, h - 4);
    }

    /**
     * Returns the underlying telescope instance.
     */
    public SimulatedTelescope getTelescope() {
        return telescope;
    }
}


