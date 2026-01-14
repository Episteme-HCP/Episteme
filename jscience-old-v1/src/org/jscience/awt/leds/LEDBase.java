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

// LEDBase Abstract Class
// Written by: Craig A. Lindley
// Last Update: 03/16/99
package org.jscience.awt.leds;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public abstract class LEDBase extends Canvas implements PropertyChangeListener,
    ActionListener {
    // Modes for the LEDs
    /** DOCUMENT ME! */
    public static final int MODESOLID = 0;

    /** DOCUMENT ME! */
    public static final int MODEBLINK = 1;

    /** DOCUMENT ME! */
    public static final int MODEPULSE = 2;

    // Private class data
    /** DOCUMENT ME! */
    protected Color panelColor;

    /** DOCUMENT ME! */
    protected Color ledOnColor;

    /** DOCUMENT ME! */
    protected Color ledOffColor;

    /** DOCUMENT ME! */
    private int mode; // solid, blinking or pulse

    /** DOCUMENT ME! */
    private boolean rate; // true = fast:false = slow

    /** DOCUMENT ME! */
    private boolean state; // true = on:false = off

    /** DOCUMENT ME! */
    private boolean halfPulse = false;

    /** DOCUMENT ME! */
    private boolean offOnce = false;

    /** DOCUMENT ME! */
    private boolean onOnce = false;

    /** DOCUMENT ME! */
    protected boolean ledState = false;

/**
     * Creates a new LEDBase object.
     *
     * @param ledColor   DOCUMENT ME!
     * @param panelColor DOCUMENT ME!
     * @param mode       DOCUMENT ME!
     * @param rate       DOCUMENT ME!
     * @param state      DOCUMENT ME!
     */
    public LEDBase(Color ledColor, Color panelColor, int mode, boolean rate,
        boolean state) {
        // Allow the superclass constructor to do its thing
        super();

        // Save incoming
        setLEDColor(ledColor);
        setPanelColor(panelColor);
        setLEDMode(mode);
        setLEDBlinkRate(rate);
        setLEDState(state);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getLEDColor() {
        return ledOnColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ledOnColor DOCUMENT ME!
     */
    public void setLEDColor(Color ledOnColor) {
        // Save the on color
        this.ledOnColor = ledOnColor;
        this.ledOffColor = ledOnColor.darker().darker().darker();
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getPanelColor() {
        return panelColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param panelColor DOCUMENT ME!
     */
    public void setPanelColor(Color panelColor) {
        // Save the panel color
        this.panelColor = panelColor;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLEDMode() {
        return mode;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mode DOCUMENT ME!
     */
    public void setLEDMode(int mode) {
        this.mode = mode;
    }

    /**
     * True means the LED will blink at a fast rate. False means the
     * LED will blink at 1/2 the fast rate. LED will only blink, however, if
     * mode is MODEBLINK and state is true.
     *
     * @return DOCUMENT ME!
     */
    public boolean getLEDBlinkRate() {
        return rate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rate DOCUMENT ME!
     */
    public void setLEDBlinkRate(boolean rate) {
        this.rate = rate;
    }

    /**
     * True means LED is not off. That is, it is either on or blinking.
     * False means the LED is off
     *
     * @return DOCUMENT ME!
     */
    public boolean getLEDState() {
        return state;
    }

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void setLEDState(boolean state) {
        this.state = state;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("ON")) {
            state = true;
        } else {
            state = false;
        }

        repaint();
    }

    /**
     * A small state machine to control the LED. It is meant to limit
     * the number of repaints getting generated so that the LED does not
     * studder as much as it would otherwise.
     *
     * @param evt DOCUMENT ME!
     */
    public void propertyChange(PropertyChangeEvent evt) {
        // Make sure this is a blink property change.
        // Ignore all other types.
        if (!evt.getPropertyName().equals("blink")) {
            return;
        }

        // Pulse is set by blinker property value
        boolean pulse = ((Boolean) evt.getNewValue()).booleanValue();

        // halfPulse toggles at 1/2 the blinker rate
        if (pulse) {
            halfPulse = !halfPulse;
        }

        if (state) {
            // LED needs to be on
            offOnce = false;

            if (mode == MODESOLID) {
                // LED on solid
                if (!onOnce) {
                    ledState = true;
                    repaint();
                    onOnce = true;
                }
            } else if (mode == MODEBLINK) {
                // LED is blinking
                onOnce = false;

                if (rate) {
                    // Rate is fast
                    ledState = pulse;
                } else {
                    // Rate is slow
                    ledState = halfPulse;
                }

                repaint();
            } else {
                // LED needs to pulse
                ledState = true;
                repaint();
                state = false;
            }
        } else {
            // LED needs to be off
            onOnce = false;

            if (!offOnce) {
                ledState = false;
                offOnce = true;
                repaint();
            }
        }
    }

    // Derived classes must implement these methods
    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public abstract void paint(Graphics g);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Dimension getPreferredSize();

    // Private support methods
    /**
     * DOCUMENT ME!
     */
    private void sizeToFit() {
        // Resize to the preferred size
        Dimension d = getPreferredSize();
        setSize(d);

        Component p = getParent();

        if (p != null) {
            p.invalidate();
            p.validate();
        }
    }
}
