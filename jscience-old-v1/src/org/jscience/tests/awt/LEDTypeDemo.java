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

// LED Demonstration Program
// Written by: Craig A. Lindley
// Last Update: 03/16/99
package org.jscience.awt.leds;

import org.jscience.awt.blinker.Blinker;
import org.jscience.awt.buttons.SquareButton;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class LEDTypeDemo extends Frame {
    /** DOCUMENT ME! */
    public static final int NUMBEROFLEDS = 7;

    /** DOCUMENT ME! */
    public static final int LEDRADIUS = 7;

    /** DOCUMENT ME! */
    public static final Color PANELCOLOR = new Color(44, 148, 103);

    // Private class data
    /** DOCUMENT ME! */
    private Color[] colorArray = new Color[NUMBEROFLEDS];

    /** DOCUMENT ME! */
    private SquareButton ledButton;

/**
     * Creates a new LEDTypeDemo object.
     */
    public LEDTypeDemo() {
        super("LED Demonstration Program");

        // Initialize colorArray with colors for LEDs
        for (int i = 0; i < NUMBEROFLEDS; i++) {
            colorArray[i] = Color.getHSBColor((float) i / NUMBEROFLEDS,
                    (float) 1.0, (float) 1.0);
        }

        // Create the blinker for the LEDs
        Blinker blinker = new Blinker(200);

        // Create Display Panel
        Panel ledDemoPanel = new Panel();
        ledDemoPanel.setLayout(new BorderLayout());
        ledDemoPanel.setBackground(PANELCOLOR);

        Panel buttonPanel = createButtonPanel();

        Panel topPanel = new Panel();
        topPanel.setLayout(new GridLayout(1, 8));

        topPanel.add(createLabelPanel());
        topPanel.add(createLEDPanel(blinker, 0, ledButton));
        topPanel.add(createLEDPanel(blinker, 1, ledButton));
        topPanel.add(createLEDPanel(blinker, 2, ledButton));
        topPanel.add(createLEDPanel(blinker, 3, ledButton));
        topPanel.add(createLEDPanel(blinker, 4, ledButton));
        topPanel.add(createLEDPanel(blinker, 5, ledButton));
        topPanel.add(createLEDPanel(blinker, 6, ledButton));

        ledDemoPanel.add(topPanel, BorderLayout.CENTER);
        ledDemoPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add led panel to Frame
        add(ledDemoPanel);

        pack();
    }

    // Create the button panel
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Panel createButtonPanel() {
        // Create square button
        ledButton = new SquareButton(40, 20, // Sets the size
                "SanSerif", Font.BOLD, 10, // Sets the text font
                "Led Button", true, // Caption and caption at bottom flag
                true, false, // Sticky and state
                true, // Highlight
                PANELCOLOR, // Panel color
                Color.gray, // Button color
                Color.green); // Text color

        Panel p = new Panel();
        p.add(ledButton);

        return p;
    }

    // Create the label panel
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Panel createLabelPanel() {
        Panel p = new Panel();
        p.setLayout(new GridLayout(4, 1));

        p.add(new Label("Pulse"));
        p.add(new Label("Fast Blink"));
        p.add(new Label("Slow Blink"));
        p.add(new Label("Solid"));

        return p;
    }

    // Create the individual LED panels
    /**
     * DOCUMENT ME!
     *
     * @param blinker DOCUMENT ME!
     * @param colorIndex DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Panel createLEDPanel(Blinker blinker, int colorIndex, SquareButton b) {
        Panel p = new Panel();
        p.setLayout(new GridLayout(4, 1));

        // Create the pulse LED
        RoundLED led1 = new RoundLED();
        led1.setRadius(LEDRADIUS);
        led1.setPanelColor(PANELCOLOR);
        led1.setLEDColor(colorArray[colorIndex]);
        led1.setLEDMode(RoundLED.MODEPULSE);

        // Add the LED to the panel
        p.add(led1);

        // Connect this LED to the blinker for running its state machine
        blinker.addPropertyChangeListener(led1);

        // Connect the LED to the button to control its state
        b.addActionListener(led1);

        // Create the fast blinking LED
        RoundLED led2 = new RoundLED();
        led2.setRadius(LEDRADIUS);
        led2.setPanelColor(PANELCOLOR);
        led2.setLEDColor(colorArray[colorIndex]);
        led2.setLEDMode(RoundLED.MODEBLINK);
        led2.setLEDBlinkRate(true);

        // Add the LED to the panel
        p.add(led2);

        // Connect this LED to the blinker for running its state machine
        blinker.addPropertyChangeListener(led2);

        // Connect the LED to the button to control its state
        b.addActionListener(led2);

        // Create the slow blinking LED
        SquareLED led3 = new SquareLED();
        led3.setWidth(16);
        led3.setHeight(10);
        led3.setPanelColor(PANELCOLOR);
        led3.setLEDColor(colorArray[colorIndex]);
        led3.setLEDMode(RoundLED.MODEBLINK);
        led3.setLEDBlinkRate(false);

        // Add the LED to the panel
        p.add(led3);

        // Connect this LED to the blinker for running its state machine
        blinker.addPropertyChangeListener(led3);

        // Connect the LED to the button to control its state
        b.addActionListener(led3);

        // Create the solid LED
        RoundLED led4 = new RoundLED();
        led4.setRadius(LEDRADIUS);
        led4.setPanelColor(PANELCOLOR);
        led4.setLEDColor(colorArray[colorIndex]);
        led4.setLEDMode(RoundLED.MODESOLID);

        // Add the LED to the panel
        p.add(led4);

        // Connect this LED to the blinker for running its state machine
        blinker.addPropertyChangeListener(led4);

        // Connect the LED to the button to control its state
        b.addActionListener(led4);

        return p;
    }

    // Test application entry point
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        LEDTypeDemo btd = new LEDTypeDemo();
        btd.show();
    }
}
