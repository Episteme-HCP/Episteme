// Display Demonstration Program
// Written by: Craig A. Lindley
// Last Update: 03/21/99
package org.jscience.awt.displays;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class DisplayDemo extends Frame {
    /** DOCUMENT ME! */
    public static final Color PANELCOLOR = Color.black;

    /** DOCUMENT ME! */
    public static final Color LEDCOLOR = Color.green;

    /** DOCUMENT ME! */
    public static final Color LEDBGCOLOR = Color.black;

    // Class variables
    /** DOCUMENT ME! */
    private static IntLEDDisplay display1;

    /** DOCUMENT ME! */
    private static ReadoutLabel display2;

/**
     * Creates a new DisplayDemo object.
     */
    public DisplayDemo() {
        super("Display Demo");

        Panel displayPanel = new Panel();
        displayPanel.setLayout(new GridLayout(1, 2));

        // Create the seven segment display
        display1 = new IntLEDDisplay(130, 150, 1, 0);
        display1.setPanelColor(PANELCOLOR);
        display1.setLEDColor(LEDCOLOR);
        display1.setLEDBGColor(LEDBGCOLOR);
        display1.setTextColor(LEDCOLOR);
        display1.setCaption("Seven Segment Display");
        displayPanel.add(display1);

        Panel blankPanel = new Panel();
        blankPanel.setBackground(Color.black);
        blankPanel.setLayout(new GridLayout(3, 1));
        blankPanel.add(new Label(""));

        // Create a readout label
        display2 = new ReadoutLabel(Color.green, "count", 5);
        blankPanel.add(display2);
        blankPanel.add(new Label(""));
        displayPanel.add(blankPanel);

        add(displayPanel);

        pack();
    }

    // Test application entry point
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        DisplayDemo btd = new DisplayDemo();
        btd.show();

        Thread t = new Thread() {
                public void run() {
                    int count = -9;

                    while (true) {
                        try {
                            sleep(700);
                        } catch (InterruptedException e) {
                        }

                        // Update both displays
                        display1.setValue(count);
                        display2.setValue(count++);

                        if (count >= 10) {
                            count = -9;
                        }
                    }
                }
            };

        t.start();
    }
}
