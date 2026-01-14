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

// Readoutlabel Class
// Written by: Craig A. Lindley
// Last Update: 03/21/99
package org.jscience.awt.displays;

import org.jscience.awt.util.EtchedBorder;

import java.awt.*;


// Simple label with etched border
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ReadoutLabel extends EtchedBorder {
    // Default colors
    /** DOCUMENT ME! */
    private static final Color DEFAULTFOREGROUNDCOLOR = Color.blue;

    /** DOCUMENT ME! */
    private static final Color DEFAULTBACKGROUNDCOLOR = Color.gray;

    // Private class data
    /** DOCUMENT ME! */
    private String unitString;

    /** DOCUMENT ME! */
    private Label l;

/**
     * ReadoutLabel Class Constructor Create a display device for displaying
     * numeric data
     *
     * @param color          color is the color to be used for the display
     * @param unitString     unitString is the possibly null string to be used to
     *                       label the numeric data
     * @param extraCharCount extraCharCount adds width to the label
     */
    public ReadoutLabel(Color color, String unitString, int extraCharCount) {
        super(new Label(BlankString.createBlankString(unitString.length() +
                    extraCharCount)));

        // Save incoming
        if (unitString != null) {
            this.unitString = unitString;
        } else {
            this.unitString = "";
        }

        l = (Label) getComponent();

        l.setBackground(DEFAULTBACKGROUNDCOLOR);
        l.setForeground(color);

        l.setAlignment(Label.CENTER);
    }

/**
     * ReadoutLabel Class Constructor Create a display device for displaying
     * numeric data
     *
     * @param color      color is the color to be used for the display
     * @param unitString unitString is the possibly null string to be used to
     *                   label the numeric data
     */
    public ReadoutLabel(Color color, String unitString) {
        this(color, unitString, 0);
    }

/**
     * ReadoutLabel Class Constructor Create a display device for displaying
     * numeric data
     *
     * @param unitString unitString is the possibly null string to be used to
     *                   label the numeric data
     */
    public ReadoutLabel(String unitString) {
        this(DEFAULTFOREGROUNDCOLOR, unitString);
    }

/**
     * ReadoutLabel Class Constructor Create a display device for displaying
     * numeric data
     */
    public ReadoutLabel() {
        this(DEFAULTFOREGROUNDCOLOR, null);
    }

    /**
     * Sets the value of the display with integer data
     *
     * @param value Value to set the display to
     */
    public void setValue(int value) {
        l.setText(Integer.toString(value) + " " + unitString);
    }

    /**
     * Sets the value of the display with floating point data
     *
     * @param value Value to set the display to
     * @param places places is the count of converted string digits to display
     */
    public void setValue(double value, int places) {
        String s = Double.toString(value);

        if (s.length() > places) {
            s = s.substring(0, places);
        }

        l.setText(s + " " + unitString);
    }

    /**
     * Sets the value of the display with a string
     *
     * @param value value to set the display to
     */
    public void setValue(String value) {
        l.setText(value + " " + unitString);
    }
}


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class BlankString {
    // Create a blank string of specified length
    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String createBlankString(int n) {
        String s = "";

        for (int i = 0; i < n; i++)
            s = s.concat(" ");

        return s;
    }
}
