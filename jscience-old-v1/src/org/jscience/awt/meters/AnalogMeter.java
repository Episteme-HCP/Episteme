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

// AnalogMeter Class
// Written by: Craig A. Lindley
// Last Update: 03/28/99
package org.jscience.awt.meters;

import java.awt.*;

import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class AnalogMeter extends Meter {
    // Various constants used to control the meter's appearance
    /** DOCUMENT ME! */
    private static final int METERRANGEINDEGREES = 120;

    /** DOCUMENT ME! */
    private static final int DEFAULTLABELPERCENT = 65;

    /** DOCUMENT ME! */
    private static final int BORDERWIDTH = 3;

    /** DOCUMENT ME! */
    private static final int CAPTIONPERCENT = 30;

    /** DOCUMENT ME! */
    private static final int CORNERDIAMETER = 15;

    /** DOCUMENT ME! */
    private static final int WELLMAJDIAMETERPERCENT = 15;

    /** DOCUMENT ME! */
    private static final int WELLMINDIAMETERPERCENT = 5;

    /** DOCUMENT ME! */
    private static final int COLORSCALEPERCENT = 90;

    // Private class data
    /** DOCUMENT ME! */
    private double minValueAngle;

    /** DOCUMENT ME! */
    private double maxValueAngle;

    /** DOCUMENT ME! */
    private Vector colorZones = new Vector();

/**
     * Analog Meter Class Constructor with all agruments
     *
     * @param width        width is the width in pixels of the meter
     * @param height       height is the height in pixels of the meter
     * @param meterMode    meterMode is not currently used
     * @param fontName     fontName is the name of the font for labelling
     * @param fontStyle    fontStyle is the name of the font style for labelling
     * @param fontSize     fontSize is the size of the font for labelling
     * @param caption      caption is the caption to label the meter with
     * @param hasLabels    hasLabels is true if the meter has labels and it is
     *                     desired they are displayed.
     * @param labelsString labelsString is the string of comma separated label
     *                     strings used to label the meter. There can be any number
     *                     specified and the analog meter will spread them evenly across
     *                     the scale.
     * @param labelPercent labelPercent is the percentage relative to the
     *                     meter's height where the labels will be drawn. Labels are drawn
     *                     radially.
     * @param value        value is the value the meter should initially display
     * @param hasHighlight hasHighlight is true if highlighting should be used
     *                     for the meter's display. Currently unused here.
     * @param panelColor   panelColor is the color of the panel surrounding the
     *                     meter.
     * @param needleColor  needleColor is the color of the meter's needle
     * @param textColor    textColor is the color used for the labelling text
     */
    public AnalogMeter(int width, int height, int meterMode, String fontName,
        int fontStyle, int fontSize, String caption, boolean hasLabels,
        String labelsString, int labelPercent, int value, boolean hasHighlight,
        Color panelColor, Color needleColor, Color textColor) {
        super(width, height, meterMode, fontName, fontStyle, fontSize, caption,
            hasLabels, labelsString, value, hasHighlight, 0, panelColor,
            needleColor, textColor);

        setLabelPercent(labelPercent);

        // Calculate meter granularity
        meterGranularity = ((double) METERRANGEINDEGREES) / METERRANGE;

        // Calculate min and max angles of needle
        double halfAngle = METERRANGEINDEGREES / 2.0;
        minValueAngle = 90 + halfAngle;
        maxValueAngle = 90 - halfAngle;

        // Install a default meter color range. Full range of scale is
        // green.
        setColorRange(Color.green, 0, 100);
    }

/**
     * Analog Meter Class Constructor with reasonable defaults
     *
     * @param width   DOCUMENT ME!
     * @param height  DOCUMENT ME!
     * @param caption DOCUMENT ME!
     * @param value   DOCUMENT ME!
     */
    public AnalogMeter(int width, int height, String caption, int value) {
        this(width, height, MODEPEAK, DEFAULTFONTNAME, DEFAULTFONTSTYLE,
            DEFAULTFONTSIZE, caption, true, "", DEFAULTLABELPERCENT, value,
            true, Color.white, NEEDLECOLOR, Color.lightGray);
    }

/**
     * Analog Meter Class Constructor with zero arguments. Needed for use as a
     * bean.
     */
    public AnalogMeter() {
        this(110, 90, "Analog Meter", 50);
    }

    /**
     * Draw a text string using polar coordinates
     *
     * @param g g is the graphics context on which to render the text.
     * @param xCenter xCenter is the horizontal center from which the text is
     *        positioned.
     * @param yCenter yCenter is the vertical center from which the text is
     *        positioned.
     * @param angle angle is the angle in degrees at which the text should be
     *        rendered.
     * @param start start is the radial distance from the center where the text
     *        should be drawn centered.
     * @param text text is the text that gets drawn
     */
    protected void drawTextPolar(Graphics g, int xCenter, int yCenter,
        double angle, int start, String text) {
        double rads = (angle * Math.PI) / 180.0;

        // Get font specs
        FontMetrics fm = g.getFontMetrics();
        int halfHeight = fm.getAscent() / 2;
        int halfWidth = fm.stringWidth(text) / 2;

        double x = start * Math.cos(rads);
        double y = start * Math.sin(rads);
        y -= halfHeight;

        x += (xCenter - halfWidth);
        y = yCenter - y;

        // Draw the text
        g.drawString(text, round(x), round(y));
    }

    /**
     * Paint the Analog Meter onto the graphics context. Double
     * buffering is used to reduce flicker.
     *
     * @param g g is the graphics context on which to draw
     */
    public void paint(Graphics g) {
        // Get the size of the container and calculate important
        // values.
        int cwidth = getSize().width;
        int cheight = getSize().height;
        int xCenter = cwidth / 2;
        int captionHeight = (cheight * CAPTIONPERCENT) / 100;
        int captionYOffset = cheight - captionHeight;
        int captionXOffset = 0;
        int needleLength = captionYOffset - (2 * BORDERWIDTH);

        // Is there an image of the analog meter to work with ?
        if (meterImage == null) {
            // No, create the image for the meter
            meterImage = createImage(cwidth, cheight);

            // Get graphics context for the image
            Graphics gMeter = meterImage.getGraphics();

            // Fill complete meter
            gMeter.setColor(Color.black);
            gMeter.fillRoundRect(0, 0, cwidth, cheight, CORNERDIAMETER,
                CORNERDIAMETER);

            // Fill the panel
            int panelXOffset = BORDERWIDTH;
            int panelYOffset = BORDERWIDTH;
            int panelWidth = cwidth - (2 * BORDERWIDTH);
            int panelHeight = cheight - (2 * BORDERWIDTH);
            gMeter.setColor(panelColor);
            gMeter.fillRoundRect(panelXOffset, panelYOffset, panelWidth,
                panelHeight, CORNERDIAMETER, CORNERDIAMETER);

            // Draw color scale
            // First draw the filled arcs
            int xMaxOrg = xCenter - needleLength;
            int yMaxOrg = captionYOffset - needleLength;
            int arcMaxWidth = needleLength * 2;

            for (int colorZone = 0; colorZone < colorZones.size();
                    colorZone++) {
                MeterColorZone mz = (MeterColorZone) colorZones.elementAt(colorZone);

                // Set the colorZone color
                gMeter.setColor(mz.color);

                // Fill the arc
                gMeter.fillArc(xMaxOrg, yMaxOrg, arcMaxWidth, arcMaxWidth,
                    (int) mz.startAngle, (int) mz.spanAngle);
            }

            // Then clean out meter middle
            int bandLength = (needleLength * COLORSCALEPERCENT) / 100;
            int xMinOrg = xCenter - bandLength;
            int yMinOrg = captionYOffset - bandLength;
            int arcMinWidth = bandLength * 2;

            // Fill the arc with the panel color
            gMeter.setColor(panelColor);
            gMeter.fillArc(xMinOrg, yMinOrg, arcMinWidth, arcMinWidth,
                (int) maxValueAngle - 1, METERRANGEINDEGREES + 2);

            // Draw the major meter well
            int wellMajDiameter = (WELLMAJDIAMETERPERCENT * cwidth) / 100;
            int halfWellMajDiameter = wellMajDiameter / 2;
            int wellMaxXOrg = xCenter - halfWellMajDiameter;
            int wellMaxYOrg = captionYOffset - halfWellMajDiameter;
            gMeter.setColor(Color.black);
            gMeter.fillOval(wellMaxXOrg, wellMaxYOrg, wellMajDiameter,
                wellMajDiameter);

            // Draw the minor meter well
            int wellMinDiameter = (WELLMINDIAMETERPERCENT * cwidth) / 100;
            int halfWellMinDiameter = wellMinDiameter / 2;
            int wellMinXOrg = xCenter - halfWellMinDiameter;
            int wellMinYOrg = captionYOffset - halfWellMinDiameter;
            gMeter.setColor(Color.gray);
            gMeter.fillOval(wellMinXOrg, wellMinYOrg, wellMinDiameter,
                wellMinDiameter);

            // Fill caption portion
            gMeter.setColor(Color.darkGray);
            gMeter.fillRoundRect(captionXOffset, captionYOffset, cwidth,
                captionHeight, CORNERDIAMETER, CORNERDIAMETER);

            // Draw the caption
            gMeter.setFont(font);

            FontMetrics fm = gMeter.getFontMetrics();
            int labelWidth = fm.stringWidth(caption);
            int charHeight = fm.getAscent() / 2;
            int xText = xCenter - (labelWidth / 2);
            int yText = captionYOffset + (captionHeight / 2) + charHeight;
            gMeter.setColor(textColor);
            gMeter.drawString(caption, xText, yText);

            // Draw the labels
            int numberOfLabels = labels.size();
            String label;
            String label1;

            switch (numberOfLabels) {
            case 0:
                break;

            case 1:
                label = (String) labels.elementAt(0);
                drawTextPolar(gMeter, xCenter, captionYOffset, 90, labelDist,
                    label);

                break;

            default:

                double deltaAngle = ((double) METERRANGEINDEGREES) / (numberOfLabels -
                    1);

                for (int l = 0; l < numberOfLabels; l++) {
                    double angle = minValueAngle - (l * deltaAngle);
                    label = (String) labels.elementAt(l);
                    drawTextPolar(gMeter, xCenter, captionYOffset, angle,
                        labelDist, label);
                }
            }
        }

        // Render the meter into the device context
        g.drawImage(meterImage, 0, 0, null);

        // Setup to draw the needle
        g.setColor(needleColor);

        double valueAngle = minValueAngle - (value * meterGranularity);
        valueAngle = (valueAngle * Math.PI) / 180;

        int xOffset = xCenter + (int) (needleLength * Math.cos(valueAngle));
        int yOffset = captionYOffset -
            (int) (needleLength * Math.sin(valueAngle));

        // Draw the needle
        g.drawLine(xCenter, captionYOffset, xOffset, yOffset);
    }

    /**
     * Return the preferred size of this analog meter
     *
     * @return Dimension object containing the preferred size of the meter
     */
    public Dimension getPreferredSize() {
        // width and height define meter
        return new Dimension(width, height);
    }

    /**
     * Reset all of the color zones used for the meter. This allows the
     * zones to be changed at runtime if needed.
     */
    public void resetMeterColorZones() {
        colorZones.removeAllElements();
        meterImage = null;
        repaint();
    }

    /**
     * Set a color for a range of values on the meter's scale. Creates
     * a new MeterColorZone object to describe the range. NOTE: this isn't any
     * overlap checking done here so the most recent range set sticks.
     *
     * @param color color is the color for the specified range of values
     * @param minPercentValue minPercentValue is the percentage of full scale
     *        value where this color should begin
     * @param maxPercentValue maxPercentValue is the percentage of full scale
     *        value where this color should end
     */
    public void setColorRange(Color color, double minPercentValue,
        double maxPercentValue) {
        double spanAngleRange = maxPercentValue - minPercentValue;
        double spanAngle = spanAngleRange * meterGranularity;
        double startAngle = minValueAngle -
            (maxPercentValue * meterGranularity);

        colorZones.addElement(new MeterColorZone(startAngle, spanAngle, color));
        meterImage = null;
        repaint();
    }

    /**
     * Sets the distance as a percent of meter height where the meter
     * labels should be placed.
     *
     * @param percent percent is the percent value
     */
    public void setLabelPercent(int percent) {
        labelPercent = percent;

        // Calculate actual length
        labelDist = (percent * height) / 100;

        meterImage = null;
        repaint();
    }

    // Simple test program for Analog Meters
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        Frame f = new Frame("Test");
        AnalogMeter am = new AnalogMeter();
        f.add(am);

        Dimension d = am.getPreferredSize();
        f.setSize(d);
        f.setVisible(true);

        am.setColorRange(Color.green, 0, 33);
        am.setColorRange(Color.yellow, 33, 66);
        am.setColorRange(Color.red, 66, 100);

        for (int index = 0; index < 1000; index++) {
            int value = (int) (Math.random() * 100);
            am.setValue(value);

            try {
                Thread.sleep(200);
            } catch (Exception ignor) {
            }
        }
    }

    /**
     * Inner class that describes a color span on the meter's scale
     */
    private class MeterColorZone {
        // Class data
        /** DOCUMENT ME! */
        public double startAngle;

        /** DOCUMENT ME! */
        public double spanAngle;

        /** DOCUMENT ME! */
        public Color color;

/**
         * Creates a new MeterColorZone object.
         *
         * @param startAngle DOCUMENT ME!
         * @param spanAngle  DOCUMENT ME!
         * @param color      DOCUMENT ME!
         */
        MeterColorZone(double startAngle, double spanAngle, Color color) {
            // Save incoming
            this.startAngle = startAngle;
            this.spanAngle = spanAngle;
            this.color = color;
        }
    }
}
