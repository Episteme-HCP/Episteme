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

package org.jscience.ui.devices;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import org.jscience.device.actuators.Centrifuge;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.ui.AbstractDeviceViewer;

/**
 * Viewer for Centrifuge.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CentrifugeViewer extends AbstractDeviceViewer<Centrifuge> {

    private final Slider rpmSlider;
    private final Label rpmLabel;

    public CentrifugeViewer(Centrifuge device) {
        super(device);

        rpmLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("generated.centrifuge.0.rpm", "0 RPM"));

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

    // --- Mandatory Abstract Methods (I18n) ---

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.physics");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.centrifuge.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.centrifuge.desc");
    }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.centrifuge.longdesc");
    }
}
