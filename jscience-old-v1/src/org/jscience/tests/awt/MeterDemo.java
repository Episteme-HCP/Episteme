// Meter Demonstration Program
// Written by: Craig A. Lindley
// Last Update: 04/01/99
package org.jscience.awt.meters;

import org.jscience.awt.blinker.Blinker;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class MeterDemo extends Frame {
    /** DOCUMENT ME! */
    public static final Color PANELCOLOR = new Color(44, 148, 103);

    /** DOCUMENT ME! */
    public static final Color TEXTCOLOR = Color.green;

    /** DOCUMENT ME! */
    public static final int NUMBEROFSECTIONS = 12;

    /** DOCUMENT ME! */
    public static final String SECTIONLABELS = "12,11,10,9,8,7,6,5,4,3,2,1";

    /** DOCUMENT ME! */
    public static final String REVSECTIONLABELS = "1,2,3,4,5,6,7,8,9,10,11,12";

    // Class variables
    /** DOCUMENT ME! */
    private Color[] colorArray = new Color[NUMBEROFSECTIONS];

    /** DOCUMENT ME! */
    private LEDMeter ledMeter;

    /** DOCUMENT ME! */
    private AnalogMeter analogMeter;

    /** DOCUMENT ME! */
    private RoundLEDMeter roundLEDMeter;

/**
     * Creates a new MeterDemo object.
     */
    public MeterDemo() {
        super("Meter Demo");

        // Initialize colorArray with colors for meter sections
        for (int i = 0; i < NUMBEROFSECTIONS; i++) {
            colorArray[i] = Color.getHSBColor((float) i / NUMBEROFSECTIONS,
                    (float) 1.0, (float) 1.0);
        }

        double colorPercent = 100.0 / NUMBEROFSECTIONS;

        Panel meterPanel = new Panel();
        meterPanel.setLayout(new GridLayout(2, 1));

        Panel topPanel = new Panel();
        meterPanel.add(topPanel);
        topPanel.setLayout(new GridLayout(1, 2));
        topPanel.setBackground(PANELCOLOR);

        // Create the bar graph meter
        ledMeter = new LEDMeter(12, 140, "Led Meter", 0);
        ledMeter.setPanelColor(PANELCOLOR);
        ledMeter.setTextColor(TEXTCOLOR);
        ledMeter.setNumberOfSections(NUMBEROFSECTIONS);
        ledMeter.setHasLabels(true);
        ledMeter.setLabelsString(SECTIONLABELS);
        topPanel.add(ledMeter);

        // Create the analog meter
        analogMeter = new AnalogMeter();
        analogMeter.setTextColor(TEXTCOLOR);
        analogMeter.setLabelsString(REVSECTIONLABELS);
        analogMeter.setWidth(195);
        analogMeter.setHeight(50);
        analogMeter.setLabelPercent(160);
        analogMeter.setCaption("Analog Meter");
        topPanel.add(analogMeter);

        Panel bottomPanel = new Panel();
        meterPanel.add(bottomPanel);
        bottomPanel.setLayout(new GridLayout(3, 1));
        bottomPanel.setBackground(PANELCOLOR);

        // Create the round led meter
        Blinker blinker = new Blinker(100);
        roundLEDMeter = new RoundLEDMeter(7, false, Meter.MODEPEAK,
                Meter.DEFAULTFONTNAME, Meter.DEFAULTFONTSTYLE,
                Meter.DEFAULTFONTSIZE, "Leds", true, true, SECTIONLABELS, true,
                0, NUMBEROFSECTIONS, PANELCOLOR, TEXTCOLOR, blinker);
        bottomPanel.add(roundLEDMeter);

        Label l = new Label("Round LED Meter", Label.CENTER);
        l.setForeground(TEXTCOLOR);
        bottomPanel.add(l);
        bottomPanel.add(new Label(""));

        add(meterPanel);

        // Now set the meter color ranges
        // Set full range value
        ledMeter.setColorRange(Color.white, 0, 100);
        analogMeter.setColorRange(Color.white, 0, 100);
        roundLEDMeter.setColorRange(Color.white, 0, 100);

        // Now set actual color values
        for (int section = 0; section < NUMBEROFSECTIONS; section++) {
            int startPercent = (int) (section * colorPercent);
            int endPercent = (int) ((section + 1) * colorPercent);

            Color color = colorArray[section];

            // Now set each meter
            ledMeter.setColorRange(color, startPercent, endPercent);
            analogMeter.setColorRange(color, startPercent, endPercent);
            roundLEDMeter.setColorRange(color, startPercent, endPercent);
        }

        pack();
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    public void setValue(int value) {
        ledMeter.setValue(value);
        analogMeter.setValue(value);
        roundLEDMeter.setValue(value);
    }

    // Test application entry point
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        MeterDemo md = new MeterDemo();
        md.show();

        // Generate 1000 random data values and display them
        for (int index = 0; index < 1000; index++) {
            int value = (int) (Math.random() * 100);
            md.setValue(value);

            try {
                Thread.sleep(250);
            } catch (Exception ignor) {
            }
        }
    }
}
