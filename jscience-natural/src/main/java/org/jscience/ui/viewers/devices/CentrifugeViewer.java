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

import javafx.scene.control.Label;
import org.jscience.device.actuators.Centrifuge;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.ui.AbstractDeviceViewer;
import org.jscience.ui.Parameter;
import org.jscience.ui.NumericParameter;
import org.jscience.ui.i18n.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * Viewer for Centrifuge.
 * Refactored to be parameter-based.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CentrifugeViewer extends AbstractDeviceViewer<Centrifuge> {

    private final Label rpmLabel;
    private final List<Parameter<?>> parameters = new ArrayList<>();

    public CentrifugeViewer(Centrifuge device) {
        super(device);

        rpmLabel = new Label("0 RPM");
        setupParameters();

        this.getChildren().add(rpmLabel);
    }

    private void setupParameters() {
        double maxRPM = 5000;
        try {
            maxRPM = device.getMaxRPM().doubleValue();
        } catch (Exception e) {}

        parameters.add(new NumericParameter("centrifuge.rpm", I18n.getInstance().get("centrifuge.rpm", "Target RPM"), 0, maxRPM, 10, 0, v -> {
            device.start(Real.of(v));
            rpmLabel.setText(String.format("%.0f RPM", v));
        }));
    }

    @Override
    public void update() {
    }

    @Override public String getCategory() { return I18n.getInstance().get("category.measure", "Measurement"); }
    @Override public String getName() { return I18n.getInstance().get("viewer.centrifuge.name", "Centrifuge"); }
    @Override public String getDescription() { return I18n.getInstance().get("viewer.centrifuge.desc", "A centrifuge viewer."); }
    @Override public String getLongDescription() { return I18n.getInstance().get("viewer.centrifuge.longdesc", "Monitors and controls laboratory centrifuges."); }
    
    @Override public List<Parameter<?>> getViewerParameters() { return parameters; }
}
