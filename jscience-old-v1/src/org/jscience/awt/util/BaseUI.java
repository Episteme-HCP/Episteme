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

// Base UI Class
// Written by: Craig A. Lindley
// Last Update: 12/05/99
package org.jscience.awt.util;

import org.jscience.awt.AwtConstants;
import org.jscience.awt.leds.RoundLED;
import org.jscience.awt.pots.IntValuedPot;
import org.jscience.awt.pots.Pot;
import org.jscience.awt.pots.RealValuedPot;

import org.jscience.media.audio.dsp.AbstractAudio;

import java.awt.*;


/**
 * This class provides some base functionality required by many of the
 * audio processor devices of section two. It provides storage for an
 * AbstractAudio device instance, extends CloseableFrame to give the UI a
 * closeable window in which run, provides various methods for creating simple
 * controls and indicators with a common look and a GridBagLayout layout
 * manager helper function to aid in the simulated front panel layout process.
 */
public abstract class BaseUI extends CloseableFrame {
    // Class data
    /** DOCUMENT ME! */
    protected AbstractAudio aa;

/**
     * BaseUI Class Constructor
     *
     * @param title title is the title to be placed in the window in which the
     *              UI runs.
     * @param aa    aa is the instance of the AbstractAudio device associated with
     *              the UI.
     */
    public BaseUI(String title, AbstractAudio aa) {
        super(title);

        // Save incoming
        this.aa = aa;

        // Set the panel color
        setBackground(AwtConstants.PANELCOLOR);
    }

    /**
     * GridBagLayout Helper Function This method is called when adding
     * a component to a UI using a GridBagLayout.
     *
     * @param p p is the panel onto which the component is added
     * @param c c is the component being added
     * @param gbl gbl is the instance of the layout manager
     * @param gbc gbc is the constraint associated with adding this component
     * @param x x is the x position within the panel to add the component
     * @param y y is the y position within the panel to add the component
     * @param w w is the width the added component should take up in the layout
     * @param h h is the height the added component should take up in the
     *        layout.
     */
    public static void addDefaultComponent(Panel p, Component c,
        GridBagLayout gbl, GridBagConstraints gbc, int x, int y, int w, int h) {
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        gbl.setConstraints(c, gbc);
        p.add(c);
    }

    /**
     * Create and configure a Pot for use in the UI
     *
     * @param knobSize knobSize is the radius of the knob in pixels
     * @param label label is the caption to label the pot with
     * @param labelsString labelsString is the String of comma delimited
     *        strings used to label the pot tic marks
     *
     * @return Pot configured as specified
     */
    protected Pot createPot(int knobSize, String label, String labelsString) {
        Pot p = new Pot();
        configPot(p, knobSize, label, labelsString);

        return p;
    }

    /**
     * Create and configure a RealValuedPot for use in the UI
     *
     * @param knobSize knobSize is the radius of the knob in pixels
     * @param label label is the caption to label the pot with
     * @param labelsString labelsString is the String of comma delimited
     *        strings used to label the pot tic marks
     * @param maxValue maxValue is the value the pot should return at the
     *        maximum position.
     * @param minValue minValue is the value the pot should return at the
     *        minimum position.
     *
     * @return RealValuedPot configured as specified
     */
    protected RealValuedPot createPot(int knobSize, String label,
        String labelsString, double maxValue, double minValue) {
        RealValuedPot p = new RealValuedPot(maxValue, minValue);
        configPot(p, knobSize, label, labelsString);

        return p;
    }

    /**
     * Create and configure an IntValuedPot for use in the UI
     *
     * @param knobSize knobSize is the radius of the knob in pixels
     * @param label label is the caption to label the pot with
     * @param labelsString labelsString is the String of comma delimited
     *        strings used to label the pot tic marks
     * @param maxValue maxValue is the value the pot should return at the
     *        maximum position.
     * @param minValue minValue is the value the pot should return at the
     *        minimum position.
     *
     * @return IntValuedPot configured as specified
     */
    protected IntValuedPot createPot(int knobSize, String label,
        String labelsString, int maxValue, int minValue) {
        IntValuedPot p = new IntValuedPot(maxValue, minValue);
        configPot(p, knobSize, label, labelsString);

        return p;
    }

    /**
     * Configure the Pot instance passed in to a come look
     *
     * @param p p is the Pot instance to configure
     * @param knobSize knobSize is the radius of the knob in pixels
     * @param label label is the caption to label the pot with
     * @param labelsString labelsString is the String of comma delimited
     *        strings used to label the pot tic marks
     */
    protected void configPot(Pot p, int knobSize, String label,
        String labelsString) {
        p.setPanelColor(AwtConstants.PANELCOLOR);
        p.setKnobColor(AwtConstants.KNOBCOLOR);
        p.setCaption(label);
        p.setKnobUseTics(true);
        p.setGradUseTics(true);
        p.setGradLengthPercent(40);
        p.setCaptionAtBottom(true);
        p.setNumberOfSections(10);
        p.setLabelPercent(200);
        p.setLabelsString(labelsString);
        p.setTicColor(Color.white);
        p.setGradColor(Color.white);
        p.setRadius(knobSize);
    }

    /**
     * Create and configure a RoundLED for use in a UI
     *
     * @param color color is the color the LED should be
     * @param mode mode is the mode to set the LED in. See LEDBase.java.
     * @param state state is the on/off state of the LED. True turns the LED
     *        on, false turns it off.
     *
     * @return DOCUMENT ME!
     */
    protected RoundLED createLED(Color color, int mode, boolean state) {
        RoundLED rl = new RoundLED();
        rl.setRadius(7);
        rl.setPanelColor(AwtConstants.PANELCOLOR);
        rl.setLEDMode(mode);
        rl.setLEDState(state);
        rl.setLEDColor(color);

        return rl;
    }

    /**
     * Position all devices so that they do not overlay on the screen
     */
    public static void positionAudioDevices() {
        // Get the dimensions of the screen
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();

        int screenWidth = d.width;
        int screenHeight = d.height;

        // Get a list of the all UI's spawned by AudioTest application
        Frame[] frames = Frame.getFrames();

        int borderPad = 5;
        int currentXExtent = 0;
        int currentYExtent = 0;
        int maxYExtent = 0;

        // Move each UI to a unique position
        for (int f = 0; f < frames.length; f++) {
            // Get a device's UI Frame to position
            Frame frame = frames[f];

            // Get the dimensions of the UI
            Rectangle rect = frame.getBounds();
            int deviceWidth = rect.width;
            int deviceHeight = rect.height;

            int newX = 0;
            int newY = 0;

            if ((currentXExtent + borderPad + deviceWidth) < screenWidth) {
                // This device will fit horizontally
                newX = currentXExtent + borderPad;
                newY = currentYExtent + borderPad;

                // Update extents
                currentXExtent = newX + deviceWidth;
                maxYExtent = ((deviceHeight + borderPad) > maxYExtent)
                    ? (deviceHeight + borderPad) : maxYExtent;
            } else {
                // This device won't fit hornzontally. Move down vertically
                newX = borderPad;
                newY = maxYExtent + borderPad;

                currentXExtent = newX + deviceWidth;
                currentYExtent = maxYExtent;
            }

            // Set the new position of the UI
            frame.setLocation(newX, newY);
        }
    }
}
