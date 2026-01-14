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

// Button Type and Configuration Test Program
// Written by: Craig A. Lindley
// Last Update: 03/14/99
package org.jscience.awt.buttons;

import org.jscience.awt.blinker.Blinker;
import org.jscience.awt.leds.RoundLED;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ButtonTypeDisplay extends Frame {
    /** DOCUMENT ME! */
    public static final Color PANELCOLOR = new Color(44, 148, 103);

    /** DOCUMENT ME! */
    public static final int LEDRADIUS = 7;

/**
     * Creates a new ButtonTypeDisplay object.
     */
    public ButtonTypeDisplay() {
        super("Button Classes and Configurations");

        // Create the blinker for the LEDs
        Blinker blinker = new Blinker(50);

        // Create Display Panel
        Panel buttonPanel = new Panel();
        buttonPanel.setBackground(PANELCOLOR);
        buttonPanel.setLayout(new GridLayout(1, 5));

        buttonPanel.add(createPanel1(blinker));
        buttonPanel.add(createPanel2(blinker));
        buttonPanel.add(createPanel3(blinker));
        buttonPanel.add(createPanel4(blinker));
        buttonPanel.add(createPanel5(blinker));

        // Add button panel to Frame
        add(buttonPanel);

        pack();
    }

    // Create the first round button panel
    /**
     * DOCUMENT ME!
     *
     * @param blinker DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Panel createPanel1(Blinker blinker) {
        Panel p = new Panel();
        p.setLayout(new GridLayout(2, 1));

        // Create the LED to monitor the switch
        RoundLED led = new RoundLED();
        led.setRadius(LEDRADIUS);
        led.setPanelColor(PANELCOLOR);
        led.setLEDMode(RoundLED.MODESOLID);

        // Add the LED to this panel
        p.add(led);

        // Connect this LED to the blinker for running its state machine
        blinker.addPropertyChangeListener(led);

        // Create round button
        RoundButton b = new RoundButton(30, 20, // Sets the size
                "Serif", Font.PLAIN, 10, // Sets the text font
                "Button 1", false, // Caption and caption at bottom flag
                true, false, // Sticky and state
                true, // Highlight
                PANELCOLOR, // Panel color
                Color.green, // Button color
                Color.black); // Text color

        // Add it to this panel
        p.add(b);

        // Connect the LED to the button to control its state
        b.addActionListener(led);

        return p;
    }

    // Create the second round button panel
    /**
     * DOCUMENT ME!
     *
     * @param blinker DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Panel createPanel2(Blinker blinker) {
        Panel p = new Panel();
        p.setLayout(new GridLayout(2, 1));

        // Create the LED to monitor the switch
        RoundLED led = new RoundLED();
        led.setRadius(LEDRADIUS);
        led.setPanelColor(PANELCOLOR);
        led.setLEDMode(RoundLED.MODESOLID);

        // Add the LED to this panel
        p.add(led);

        // Connect this LED to the blinker for running its state machine
        blinker.addPropertyChangeListener(led);

        // Create round button
        RoundButton b = new RoundButton(20, 20, // Sets the size
                "Serif", Font.ITALIC, 10, // Sets the text font
                "Button 2", true, // Caption and caption at bottom flag
                false, false, // Sticky and state
                false, // Highlight
                PANELCOLOR, // Panel color
                Color.blue, // Button color
                Color.white); // Text color

        // Add it to this panel
        p.add(b);

        // Connect the LED to the button to control its state
        b.addActionListener(led);

        return p;
    }

    // Create the first square button panel
    /**
     * DOCUMENT ME!
     *
     * @param blinker DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Panel createPanel3(Blinker blinker) {
        Panel p = new Panel();
        p.setLayout(new GridLayout(2, 1));

        // Create the LED to monitor the switch
        RoundLED led = new RoundLED();
        led.setRadius(LEDRADIUS);
        led.setPanelColor(PANELCOLOR);
        led.setLEDMode(RoundLED.MODESOLID);
        led.setLEDState(true);

        // Add the LED to this panel
        p.add(led);

        // Connect this LED to the blinker for running its state machine
        blinker.addPropertyChangeListener(led);

        // Create square button
        SquareButton b = new SquareButton(40, 20, // Sets the size
                "SanSerif", Font.BOLD, 10, // Sets the text font
                "Button 3", false, // Caption and caption at bottom flag
                true, true, // Sticky and state
                true, // Highlight
                PANELCOLOR, // Panel color
                Color.gray, // Button color
                Color.green); // Text color

        // Add it to this panel
        p.add(b);

        // Connect the LED to the button to control its state
        b.addActionListener(led);

        return p;
    }

    // Create the second square button panel
    /**
     * DOCUMENT ME!
     *
     * @param blinker DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Panel createPanel4(Blinker blinker) {
        Panel p = new Panel();
        p.setLayout(new GridLayout(2, 1));

        // Create the LED to monitor the switch
        RoundLED led = new RoundLED();
        led.setRadius(LEDRADIUS);
        led.setPanelColor(PANELCOLOR);
        led.setLEDMode(RoundLED.MODESOLID);

        // Add the LED to this panel
        p.add(led);

        // Connect this LED to the blinker for running its state machine
        blinker.addPropertyChangeListener(led);

        // Create square button
        SquareButton b = new SquareButton(20, 40, // Sets the size
                "SanSerif", Font.PLAIN, 12, // Sets the text font
                "Button 4", true, // Caption and caption at bottom flag
                false, false, // Sticky and state
                true, // Highlight
                PANELCOLOR, // Panel color
                Color.lightGray, // Button color
                Color.red); // Text color

        // Add it to this panel
        p.add(b);

        // Connect the LED to the button to control its state
        b.addActionListener(led);

        return p;
    }

    // Create the toggle switch panel
    /**
     * DOCUMENT ME!
     *
     * @param blinker DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Panel createPanel5(Blinker blinker) {
        Panel p = new Panel();
        p.setLayout(new GridLayout(2, 1));

        // Create the LED to monitor the switch
        RoundLED led = new RoundLED();
        led.setRadius(LEDRADIUS);
        led.setPanelColor(PANELCOLOR);
        led.setLEDMode(RoundLED.MODESOLID);

        // Add the LED to this panel
        p.add(led);

        // Connect this LED to the blinker for running its state machine
        blinker.addPropertyChangeListener(led);

        // Create toggle switch button
        ToggleSwitchButton b = new ToggleSwitchButton(13, // Sets the width
                "SanSerif", Font.ITALIC | Font.BOLD, 12, // Sets the text font
                "On", "Off", // top and bottom caption
                true, false, // Sticky and state
                PANELCOLOR, // Panel color
                Color.lightGray, // Button color
                Color.blue); // Text color

        // Add it to this panel
        p.add(b);

        // Connect the LED to the button to control its state
        b.addActionListener(led);

        return p;
    }

    // Test application entry point
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        ButtonTypeDisplay btd = new ButtonTypeDisplay();
        btd.show();
    }
}
