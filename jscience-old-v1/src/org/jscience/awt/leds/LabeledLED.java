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

// LabeledLED Class
// Written by: Craig A. Lindley
// Last Update: 04/01/99
package org.jscience.awt.leds;

import org.jscience.awt.blinker.Blinker;

import java.awt.*;


/**
 * A LabeledLED consists of a label and a RoundLED. This combination can be
 * arranged vertically or horizontally and the position of the label in
 * relation to the LED can be controlled.
 */
public class LabeledLED extends Panel {
    // Private class data
    /** DOCUMENT ME! */
    private RoundLED rl;

/**
     * Class Constructor for a Labeled LED
     *
     * @param blinker     blinker is the data source for the LED that allows it to
     *                    change states
     * @param ledColor    panelColor is the color of the panel surrounding the
     *                    round LED.
     * @param ledColor    ledColor is color of the LED
     * @param ledColor    textColor is the color of the labeling text
     * @param font        font is the font used for the labeling text
     * @param radius      radius is the side of the LED to create
     * @param label       label is the labeling string for the LED. This is a static
     *                    string that cannot be changed at runtime.
     * @param isVertical  isVertical if true indicates the label and the LED
     *                    will be aligned vertically. If false the label and the LED will
     *                    be aligned horizontally.
     * @param topLeftMode topLeftMode if true indicates the LED will be the top
     *                    or the left component of the pair. If false, the LED will be the
     *                    bottom or the right component of the pair.
     */
    public LabeledLED(Blinker blinker, Color panelColor, Color ledColor,
        Color textColor, Font font, int radius, String label,
        boolean isVertical, boolean topLeftMode) {
        if (isVertical) {
            setLayout(new GridLayout(2, 1));
        } else {
            setLayout(new GridLayout(1, 2));
        }

        setBackground(panelColor);

        // Create the LED
        rl = new RoundLED();
        rl.setLEDColor(ledColor);
        rl.setRadius(radius);
        rl.setPanelColor(panelColor);
        rl.setLEDMode(LEDBase.MODESOLID);

        // Assign LED to a blinker
        blinker.addPropertyChangeListener(rl);

        // Create the label from string
        Label l = new Label(label, Label.CENTER);
        l.setForeground(textColor);
        l.setBackground(panelColor);
        l.setFont(font);

        if (topLeftMode) {
            add(rl);
            add(l);
        } else {
            add(l);
            add(rl);
        }
    }

    /**
     * Controls the state of the labeled LED
     *
     * @param state state if true LED is on. If false it is off.
     */
    public void setLEDState(boolean state) {
        rl.setLEDState(state);
    }

    /**
     * Set the color of the labeled LED
     *
     * @param color color is the color to set the LED to
     */
    public void setLEDColor(Color color) {
        rl.setLEDColor(color);
    }
}
