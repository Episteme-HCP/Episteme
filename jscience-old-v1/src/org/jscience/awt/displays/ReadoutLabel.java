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
