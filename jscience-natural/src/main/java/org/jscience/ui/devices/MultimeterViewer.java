package org.jscience.ui.devices;

import javafx.scene.control.Label;
import org.jscience.device.sensors.Multimeter;

/**
 * Viewer for Multimeter.
 */
public class MultimeterViewer extends AbstractDeviceViewer<Multimeter> {

    private final Label valueLabel;

    public MultimeterViewer(Multimeter device) {
        super(device);

        valueLabel = new Label("0.00 V");
        valueLabel.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 24px; -fx-text-fill: #222;");
        this.getChildren().add(valueLabel);

        update();
    }

    @Override
    public void update() {
        // Mock update
        valueLabel.setText("0.00 V"); // device.readValue() needs Exception handling
    }
}
