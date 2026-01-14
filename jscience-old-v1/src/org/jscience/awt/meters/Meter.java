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

// Abstract Meter Base Class
// Written by: Craig A. Lindley
// Last Update: 04/01/99
package org.jscience.awt.meters;

import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import java.util.StringTokenizer;
import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public abstract class Meter extends Panel implements AdjustmentListener {
    /** DOCUMENT ME! */
    public static final int METERRANGE = 100;

    /** DOCUMENT ME! */
    public static final Color PANELCOLOR = Color.lightGray;

    /** DOCUMENT ME! */
    public static final Color NEEDLECOLOR = Color.darkGray;

    /** DOCUMENT ME! */
    public static final Color TEXTCOLOR = Color.white;

    /** DOCUMENT ME! */
    public static final String DEFAULTFONTNAME = "Dialog";

    /** DOCUMENT ME! */
    public static final int DEFAULTFONTSTYLE = Font.PLAIN;

    /** DOCUMENT ME! */
    public static final int DEFAULTFONTSIZE = 9;

    // Modes a meter might be in. Currently unused
    /** DOCUMENT ME! */
    public static final int MODENONE = 0;

    /** DOCUMENT ME! */
    public static final int MODEPEAK = 1;

    /** DOCUMENT ME! */
    public static final int MODEPEAKHOLD = 2;

    /** DOCUMENT ME! */
    public static final int MODEAVG = 3;

    /** DOCUMENT ME! */
    public static final int MODERMS = 4;

    /** DOCUMENT ME! */
    public static final int MODEVU = 5;

    // Class data
    /** DOCUMENT ME! */
    protected Image meterImage = null;

    /** DOCUMENT ME! */
    protected int width;

    /** DOCUMENT ME! */
    protected int height;

    /** DOCUMENT ME! */
    protected int meterMode;

    /** DOCUMENT ME! */
    protected double meterGranularity;

    /** DOCUMENT ME! */
    protected int value;

    /** DOCUMENT ME! */
    protected boolean hasHighlight;

    /** DOCUMENT ME! */
    protected boolean hasLabels;

    /** DOCUMENT ME! */
    protected int labelDist;

    /** DOCUMENT ME! */
    protected int labelPercent;

    /** DOCUMENT ME! */
    protected Vector labels = new Vector();

    /** DOCUMENT ME! */
    protected int numberOfSections = -1;

    /** DOCUMENT ME! */
    protected Font font;

    /** DOCUMENT ME! */
    protected String fontName = "Dialog";

    /** DOCUMENT ME! */
    protected int fontStyle = Font.PLAIN;

    /** DOCUMENT ME! */
    protected int fontSize = 9;

    /** DOCUMENT ME! */
    protected String caption = "";

    /** DOCUMENT ME! */
    protected Color panelColor;

    /** DOCUMENT ME! */
    protected Color highlightBrighterColor;

    /** DOCUMENT ME! */
    protected Color highlightDarkerColor;

    /** DOCUMENT ME! */
    protected Color needleColor;

    /** DOCUMENT ME! */
    protected Color textColor;

/**
     * Creates a new Meter object.
     *
     * @param width            DOCUMENT ME!
     * @param height           DOCUMENT ME!
     * @param meterMode        DOCUMENT ME!
     * @param fontName         DOCUMENT ME!
     * @param fontStyle        DOCUMENT ME!
     * @param fontSize         DOCUMENT ME!
     * @param caption          DOCUMENT ME!
     * @param hasLabels        DOCUMENT ME!
     * @param labelsString     DOCUMENT ME!
     * @param value            DOCUMENT ME!
     * @param hasHighlight     DOCUMENT ME!
     * @param numberOfSections DOCUMENT ME!
     * @param panelColor       DOCUMENT ME!
     * @param needleColor      DOCUMENT ME!
     * @param textColor        DOCUMENT ME!
     */
    public Meter(int width, int height, int meterMode, String fontName,
        int fontStyle, int fontSize, String caption, boolean hasLabels,
        String labelsString, int value, boolean hasHighlight,
        int numberOfSections, Color panelColor, Color needleColor,
        Color textColor) {
        super();

        // Process and save incoming
        setMeterMode(meterMode);
        setFontName(fontName);
        setFontStyle(fontStyle);
        setFontSize(fontSize);
        setCaption(caption);
        setHasLabels(hasLabels);
        setLabelsString(labelsString);
        setValue(value);
        setHighlight(hasHighlight);
        setNumberOfSections(numberOfSections);
        setPanelColor(panelColor);
        setNeedleColor(needleColor);
        setTextColor(textColor);
        setWidth(width);
        setHeight(height);
    }

    /**
     * DOCUMENT ME!
     */
    protected void sizeToFit() {
        // Resize to the preferred size
        Dimension d = getPreferredSize();
        setSize(d);

        Component p = getParent();

        if (p != null) {
            p.invalidate();
            p.validate();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void adjustmentValueChanged(AdjustmentEvent e) {
        setValue(e.getValue());
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected int round(double d) {
        return (int) Math.round(d);
    }

    // Start of property methods
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWidth() {
        return width;
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     */
    public void setWidth(int width) {
        this.width = width;
        meterImage = null;
        sizeToFit();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHeight() {
        return height;
    }

    /**
     * DOCUMENT ME!
     *
     * @param height DOCUMENT ME!
     */
    public void setHeight(int height) {
        this.height = height;
        meterImage = null;
        sizeToFit();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMeterMode() {
        return meterMode;
    }

    /**
     * DOCUMENT ME!
     *
     * @param meterMode DOCUMENT ME!
     */
    public void setMeterMode(int meterMode) {
        this.meterMode = meterMode;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Font getFont() {
        return font;
    }

    /**
     * DOCUMENT ME!
     *
     * @param font DOCUMENT ME!
     */
    public void setFont(Font font) {
        this.font = font;

        // Size to the text/font
        meterImage = null;
        sizeToFit();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFontName() {
        return fontName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param fontName DOCUMENT ME!
     */
    public void setFontName(String fontName) {
        this.fontName = fontName;

        font = new Font(fontName, fontStyle, fontSize);

        // Size to the text/font
        meterImage = null;
        sizeToFit();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getFontStyle() {
        return fontStyle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param fontStyle DOCUMENT ME!
     */
    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;

        font = new Font(fontName, fontStyle, fontSize);

        // Size to the text/font
        meterImage = null;
        sizeToFit();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @param fontSize DOCUMENT ME!
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;

        font = new Font(fontName, fontStyle, fontSize);

        // Size to the text/font
        meterImage = null;
        sizeToFit();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCaption() {
        return caption;
    }

    /**
     * DOCUMENT ME!
     *
     * @param caption DOCUMENT ME!
     */
    public void setCaption(String caption) {
        this.caption = new String(caption);

        // Size to the text/font
        meterImage = null;
        sizeToFit();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getHasLabels() {
        return hasLabels;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hasLabels DOCUMENT ME!
     */
    public void setHasLabels(boolean hasLabels) {
        this.hasLabels = hasLabels;
        meterImage = null;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLabelPercent() {
        return labelPercent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param percent DOCUMENT ME!
     */
    public void setLabelPercent(int percent) {
        if (percent < 100) {
            return;
        }

        this.labelPercent = percent;

        // Calculate actual length
        this.labelDist = (percent * width) / 100;

        meterImage = null;
        repaint();
    }

    // Return a CSV string containing the label strings
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLabelsString() {
        String retString = "";

        for (int index = 0; index < labels.size(); index++) {
            retString += (String) labels.elementAt(index);

            if (index != (labels.size() - 1)) {
                retString += ",";
            }
        }

        return retString;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public void setLabelsString(String s) {
        // Clear any existing labels
        labels.removeAllElements();

        // Prepare to take the string apart
        StringTokenizer st = new StringTokenizer(s, ",");

        // Add each part of string as a separate label
        while (st.hasMoreTokens())
            labels.addElement(st.nextToken());

        meterImage = null;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getValue() {
        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @param newValue DOCUMENT ME!
     */
    public void setValue(int newValue) {
        newValue = Math.min(METERRANGE, newValue);
        newValue = Math.max(0, newValue);
        this.value = newValue;

        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getHighlight() {
        return hasHighlight;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hasHighlight DOCUMENT ME!
     */
    public void setHighlight(boolean hasHighlight) {
        this.hasHighlight = hasHighlight;
        meterImage = null;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumberOfSections() {
        return numberOfSections;
    }

    /**
     * DOCUMENT ME!
     *
     * @param numberOfSections DOCUMENT ME!
     */
    public void setNumberOfSections(int numberOfSections) {
        this.numberOfSections = numberOfSections;

        // Calculate meter granularity
        meterGranularity = ((double) numberOfSections) / METERRANGE;
        meterImage = null;
        repaint();
    }

    /**
     * Gets the panel color
     *
     * @return Color - The panel's color
     */
    public Color getPanelColor() {
        return panelColor;
    }

    /**
     * Sets the panel color
     *
     * @param panelColor Meter panel color
     */
    public void setPanelColor(Color panelColor) {
        this.panelColor = panelColor;
        meterImage = null;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getNeedleColor() {
        return needleColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param needleColor DOCUMENT ME!
     */
    public void setNeedleColor(Color needleColor) {
        this.needleColor = needleColor;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getTextColor() {
        return textColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param textColor DOCUMENT ME!
     */
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        meterImage = null;
        repaint();
    }
}
