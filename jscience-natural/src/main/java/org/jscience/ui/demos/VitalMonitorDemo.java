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

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.SimulationDemo;
import org.jscience.ui.devices.VitalMonitorViewer;
import org.jscience.ui.i18n.I18n;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VitalMonitorDemo extends SimulationDemo {
    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.biology");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("vital.title");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("vital.short_desc", "Medical vital signs monitoring");
    }

    @Override
    protected String getLongDescription() {
        return "This professional vital signs monitor simulates a real-world medical device. " +
                "It displays real-time waveforms for ECG (Electrocardiogram) and Plethysmograph (SpO2), " +
                "along with numerical values for heart rate, blood pressure, oxygen saturation, " +
                "respiration rate, and temperature. You can adjust patient parameters in real-time " +
                "to see how the monitor reacts.";
    }

    @Override
    protected Node createViewerNode() {
        VitalMonitorViewer vmv = new VitalMonitorViewer();
        vmv.play();
        return vmv;
    }
}


