package org.jscience.ui.devices;

import javafx.scene.control.Label;
import org.jscience.device.sensors.Microscope;

/**
 * Viewer for Microscope.
 */
public class MicroscopeViewer extends AbstractDeviceViewer<Microscope> {

    private final Label magLabel;

    public MicroscopeViewer(Microscope device) {
        super(device);

        magLabel = new Label("Magnification: -");
        this.getChildren().add(magLabel);

        update();
    }

    @Override
    public void update() {
        if (device != null) {
            magLabel.setText("Magnification: " + device.getCurrentMagnification().toString());
        }
    }
}
