package org.jscience.ui.devices;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import org.jscience.device.actuators.Centrifuge;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Viewer for Centrifuge.
 */
public class CentrifugeViewer extends AbstractDeviceViewer<Centrifuge> {

    private final Slider rpmSlider;
    private final Label rpmLabel;

    public CentrifugeViewer(Centrifuge device) {
        super(device);

        rpmLabel = new Label("0 RPM");

        rpmSlider = new Slider(0, 5000, 0); // Mock max
        try {
            rpmSlider.setMax(device.getMaxRPM().doubleValue());
        } catch (Exception e) {
        }

        rpmSlider.setShowTickLabels(true);
        rpmSlider.setShowTickMarks(true);

        rpmSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            device.start(Real.of(newVal.doubleValue()));
            rpmLabel.setText(String.format("%.0f RPM", newVal.doubleValue()));
        });

        this.getChildren().addAll(rpmLabel, rpmSlider);
    }

    @Override
    public void update() {
        // Update current RPM from device if needed
    }
}
