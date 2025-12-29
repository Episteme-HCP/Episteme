package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.SimulationDemo;
import org.jscience.ui.devices.VitalMonitorViewer;
import org.jscience.ui.i18n.I18n;

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
