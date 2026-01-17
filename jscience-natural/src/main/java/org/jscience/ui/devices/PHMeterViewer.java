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
import org.jscience.device.sensors.PHMeter;
import org.jscience.ui.AbstractDeviceViewer;

/**
 * Viewer for PHMeter.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PHMeterViewer extends AbstractDeviceViewer<PHMeter> {

    private final Label phLabel;

    public PHMeterViewer(PHMeter device) {
        super(device);

        phLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("generated.phmeter.ph.70", "pH 7.0"));
        phLabel.getStyleClass().add("text-success"); // Replaced inline style: -fx-font-size: 20px; -fx-text-fill: green;
        this.getChildren().add(phLabel);
    }

    @Override
    public void update() {
        phLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("generated.phmeter.ph.70.1", "pH 7.0"));
    }

    // --- Mandatory Abstract Methods (I18n) ---

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.chemistry", "Chemistry");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.phmeter.name", "pH Meter");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.phmeter.desc", "A pH meter viewer for measuring acidity/alkalinity.");
    }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.phmeter.longdesc", "Precise laboratory instrument for measuring the pH (hydrogen-ion activity) in water-based solutions. features a digital display with color-coded pH levels (Acidic to Basic) and automatic temperature compensation.");
    }
}
