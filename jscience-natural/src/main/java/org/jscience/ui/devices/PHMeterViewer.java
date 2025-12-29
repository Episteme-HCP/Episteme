package org.jscience.ui.devices;

import javafx.scene.control.Label;
import org.jscience.device.sensors.PHMeter;

/**
 * Viewer for PHMeter.
 */
public class PHMeterViewer extends AbstractDeviceViewer<PHMeter> {

    private final Label phLabel;

    public PHMeterViewer(PHMeter device) {
        super(device);

        phLabel = new Label("pH 7.0");
        phLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: green;");
        this.getChildren().add(phLabel);
    }

    @Override
    public void update() {
        phLabel.setText("pH 7.0");
    }
}
