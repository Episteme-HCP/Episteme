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

// Round Potentiometer Class
// Written by: Craig A. Lindley
// Last Update: 03/20/99
package org.jscience.awt.pots;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Pot extends PotBase {
    // Constants for defaults
    /** DOCUMENT ME! */
    public static final int DEFAULTRADIUS = 28;

    /** DOCUMENT ME! */
    public static final int MAXPOTTRAVELDEGREES = 270;

    /** DOCUMENT ME! */
    public static final int POTZEROOFFSET = 45;

    /** DOCUMENT ME! */
    public static final int TICLENGTHPERCENT = 80;

    /** DOCUMENT ME! */
    public static final int TICSTARTPERCENT = 20;

    /** DOCUMENT ME! */
    public static final int NUMBEROFSECTIONS = 2;

    /** DOCUMENT ME! */
    public static final String SECTIONLABELS = "0,50,100";

    /** DOCUMENT ME! */
    public static final int GRADGAPPERCENT = 7;

    /** DOCUMENT ME! */
    public static final int GRADLENGTHPERCENT = 20;

    /** DOCUMENT ME! */
    public static final int LABELPERCENT = 140;

    /** DOCUMENT ME! */
    public static final int PAD = 5;

    // Private class data
    /** DOCUMENT ME! */
    private double potGranularity; // Rotational degrees per unit

    /** DOCUMENT ME! */
    private int radius; // Radius of knob

    /** DOCUMENT ME! */
    private int ticLengthPercent;

    /** DOCUMENT ME! */
    private int ticLength;

    /** DOCUMENT ME! */
    private int ticStartPercent;

    /** DOCUMENT ME! */
    private int ticStart;

    /** DOCUMENT ME! */
    private int gradGapPercent;

    /** DOCUMENT ME! */
    private int gradGap;

    /** DOCUMENT ME! */
    private int gradLengthPercent;

    /** DOCUMENT ME! */
    private int gradLength;

    /** DOCUMENT ME! */
    private int labelPercent;

    /** DOCUMENT ME! */
    private int labelRadius;

    /** DOCUMENT ME! */
    private int minWidth;

    /** DOCUMENT ME! */
    private int minHeight;

    /** DOCUMENT ME! */
    private boolean knobUseTics;

    /** DOCUMENT ME! */
    private boolean gradUseTics;

    /** DOCUMENT ME! */
    private boolean captionAtBottom;

    // Constructors
/**
     * Creates a new Pot object.
     *
     * @param radius            DOCUMENT ME!
     * @param fontName          DOCUMENT ME!
     * @param fontStyle         DOCUMENT ME!
     * @param fontSize          DOCUMENT ME!
     * @param caption           DOCUMENT ME!
     * @param hasLabels         DOCUMENT ME!
     * @param labelsString      DOCUMENT ME!
     * @param captionAtBottom   DOCUMENT ME!
     * @param value             DOCUMENT ME!
     * @param knobUseTics       DOCUMENT ME!
     * @param gradUseTics       DOCUMENT ME!
     * @param hasHighlight      DOCUMENT ME!
     * @param ticLengthPercent  DOCUMENT ME!
     * @param ticStartPercent   DOCUMENT ME!
     * @param numberOfSections  DOCUMENT ME!
     * @param gradGapPercent    DOCUMENT ME!
     * @param gradLengthPercent DOCUMENT ME!
     * @param labelPercent      DOCUMENT ME!
     * @param panelColor        DOCUMENT ME!
     * @param knobColor         DOCUMENT ME!
     * @param textColor         DOCUMENT ME!
     * @param ticColor          DOCUMENT ME!
     * @param gradColor         DOCUMENT ME!
     */
    public Pot(int radius, String fontName, int fontStyle, int fontSize,
        String caption, boolean hasLabels, String labelsString,
        boolean captionAtBottom, int value, boolean knobUseTics,
        boolean gradUseTics, boolean hasHighlight, int ticLengthPercent,
        int ticStartPercent, int numberOfSections, int gradGapPercent,
        int gradLengthPercent, int labelPercent, Color panelColor,
        Color knobColor, Color textColor, Color ticColor, Color gradColor) {
        // Pass most of the stuff to the base class
        super(fontName, fontStyle, fontSize, caption, hasLabels, labelsString,
            value, hasHighlight, numberOfSections, panelColor, knobColor,
            textColor, ticColor, gradColor);

        // Process and save incoming
        setRadius(radius);
        setCaptionAtBottom(captionAtBottom);
        setKnobUseTics(knobUseTics);
        setGradUseTics(gradUseTics);
        setTicLengthPercent(ticLengthPercent);
        setTicStartPercent(ticStartPercent);
        setGradGapPercent(gradGapPercent);
        setGradLengthPercent(gradLengthPercent);
        setLabelPercent(labelPercent);

        // Calculate pots granularity
        potGranularity = (double) MAXPOTTRAVELDEGREES / POTRANGE;
    }

    // Constructor with some reasonable defaults
/**
     * Creates a new Pot object.
     *
     * @param radius  DOCUMENT ME!
     * @param caption DOCUMENT ME!
     * @param value   DOCUMENT ME!
     */
    public Pot(int radius, String caption, int value) {
        this(radius, DEFAULTFONTNAME, DEFAULTFONTSTYLE, DEFAULTFONTSIZE,
            caption, true, SECTIONLABELS, true, value, true, false, true,
            TICLENGTHPERCENT, TICSTARTPERCENT, NUMBEROFSECTIONS,
            GRADGAPPERCENT, GRADLENGTHPERCENT, LABELPERCENT, PANELCOLOR,
            KNOBCOLOR, TEXTCOLOR, TICCOLOR, GRADCOLOR);
    }

    // Zero argument constructor for testing
/**
     * Creates a new Pot object.
     */
    public Pot() {
        this(DEFAULTRADIUS, DEFAULTFONTNAME, DEFAULTFONTSTYLE, DEFAULTFONTSIZE,
            "Test Pot", true, SECTIONLABELS, true, 50, true, false, true,
            TICLENGTHPERCENT, TICSTARTPERCENT, NUMBEROFSECTIONS,
            GRADGAPPERCENT, GRADLENGTHPERCENT, LABELPERCENT, PANELCOLOR,
            KNOBCOLOR, TEXTCOLOR, TICCOLOR, GRADCOLOR);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dimension getPreferredSize() {
        // Calculate the preferred size based on the caption text
        FontMetrics fm = getFontMetrics(font);
        int charHeight = fm.getMaxAscent() + fm.getMaxDescent();
        int charWidth = fm.charWidth('0');

        minHeight = 2 * (labelRadius + charHeight + PAD);
        minWidth = 2 * (labelRadius + charWidth + PAD);

        int captionWidth = getCaption().length() * charWidth;
        minWidth = Math.max(minWidth, captionWidth);

        return new Dimension(minWidth, minHeight);
    }

    // Draw a line using polar coordinates
    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param xCenter DOCUMENT ME!
     * @param yCenter DOCUMENT ME!
     * @param angle DOCUMENT ME!
     * @param start DOCUMENT ME!
     * @param length DOCUMENT ME!
     */
    protected void drawLinePolar(Graphics g, int xCenter, int yCenter,
        double angle, int start, int length) {
        double rads = (angle * Math.PI) / 180.0;

        double x1 = xCenter + (start * Math.cos(rads));
        double y1 = yCenter - (start * Math.sin(rads));

        double x2 = xCenter + ((start + length) * Math.cos(rads));
        double y2 = yCenter - ((start + length) * Math.sin(rads));

        // Draw the line
        g.drawLine(round(x1), round(y1), round(x2), round(y2));
    }

    // Draw a dot using polar coordinates
    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param xCenter DOCUMENT ME!
     * @param yCenter DOCUMENT ME!
     * @param angle DOCUMENT ME!
     * @param start DOCUMENT ME!
     * @param length DOCUMENT ME!
     */
    protected void drawDotPolar(Graphics g, int xCenter, int yCenter,
        double angle, int start, int length) {
        double rads = (angle * Math.PI) / 180.0;

        int rc = start + (length / 2);
        double halfLength = length / 2.0;
        double xc = xCenter + (rc * Math.cos(rads));
        double yc = yCenter - (rc * Math.sin(rads));

        // Draw the dot
        g.fillOval(round(xc - halfLength), round(yc - halfLength), length,
            length);
    }

    // Draw a text string using polar coordinates
    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param xCenter DOCUMENT ME!
     * @param yCenter DOCUMENT ME!
     * @param angle DOCUMENT ME!
     * @param start DOCUMENT ME!
     * @param text DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param inValue DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected double getAngleFromValue(int inValue) {
        return (MAXPOTTRAVELDEGREES - POTZEROOFFSET) -
        (inValue * potGranularity);
    }

    // Paint the pot
    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        // Get the size of the container
        int width = getSize().width;
        int height = getSize().height;

        // Set font into the graphics context
        g.setFont(font);

        // Get the font metrics
        FontMetrics fm = g.getFontMetrics();

        // Get char height
        int charHeight = fm.getAscent();

        // Calculate center of knob
        xCenter = width / 2;

        int adjustedHeight = height - charHeight;
        int halfAdjustedHeight = adjustedHeight / 2;

        if (captionAtBottom) {
            yCenter = halfAdjustedHeight;
        } else {
            yCenter = halfAdjustedHeight + charHeight;
        }

        // See if we have a knob bitmap image for displaying
        if (potImage == null) {
            // No, so create the image area for the knob and surroundings
            potImage = createImage(width, height);

            // Get graphics context for the image
            Graphics gKnob = potImage.getGraphics();

            // Set font into the graphics context
            gKnob.setFont(font);

            // Do some calculations
            int knobWidth = radius * 2;

            // Calc position of knob in graphics context
            int knobOrgX = xCenter - radius;
            int knobOrgY = yCenter - radius;

            // Paint the panel area
            gKnob.setColor(panelColor);
            gKnob.fillRect(0, 0, width, height);

            if (hasHighlight) {
                // The knob is drawn so that it has a highlight towards
                // the upper left and the lower right portion of the knob
                // is darker.
                final int hlw = 3;
                int thlw = hlw * 2;
                final int highlightSpanAngleDegrees = 20;
                int halfSpanAngle = highlightSpanAngleDegrees / 2;

                // Draw the bright highlight color
                gKnob.setColor(highlightBrighterColor);
                gKnob.fillOval(knobOrgX, knobOrgY, knobWidth, knobWidth);

                // Draw the darker arc
                gKnob.setColor(highlightDarkerColor);
                gKnob.fillArc(knobOrgX, knobOrgY, knobWidth, knobWidth, 225, 180);

                // Draw the white highlight arc
                gKnob.setColor(Color.white);
                gKnob.fillArc(knobOrgX, knobOrgY, knobWidth, knobWidth,
                    135 - halfSpanAngle, highlightSpanAngleDegrees);

                // Now fill in the middle of the knob with the
                // knob color
                gKnob.setColor(knobColor);
                gKnob.fillOval(knobOrgX + hlw, hlw + knobOrgY,
                    knobWidth - thlw, knobWidth - thlw);
            } else {
                // Draw the knob without a highlight
                gKnob.setColor(knobColor);
                gKnob.fillOval(knobOrgX, knobOrgY, knobWidth, knobWidth);
            }

            // Now draw the graduations and labels, if required
            if (numberOfSections != 0) {
                gKnob.setColor(gradColor);

                for (int grad = 0; grad < (numberOfSections + 1); grad++) {
                    double gradValue = ((double) POTRANGE * grad) / numberOfSections;

                    if (gradUseTics) {
                        // Draw the tics
                        drawLinePolar(gKnob, xCenter, yCenter,
                            getAngleFromValue(round(gradValue)),
                            getGradStart(), gradLength);
                    } else {
                        drawDotPolar(gKnob, xCenter, yCenter,
                            getAngleFromValue(round(gradValue)),
                            getGradStart(), gradLength);
                    }
                }

                // Now the labels
                if (hasLabels) {
                    gKnob.setColor(textColor);

                    for (int grad = 0; grad < (numberOfSections + 1); grad++) {
                        double gradValue = ((double) POTRANGE * grad) / numberOfSections;

                        // Get the label
                        String label = (String) labels.elementAt(grad);

                        // Draw the label
                        drawTextPolar(gKnob, xCenter, yCenter,
                            getAngleFromValue(round(gradValue)), labelRadius,
                            label);
                    }
                }
            }

            // Now, draw the centered caption either above or below the pot
            int stringWidth = fm.stringWidth(caption);
            int xPos = (width - stringWidth) / 2;
            int yPos = fm.getAscent() / 2;

            if (captionAtBottom) {
                yPos += (yCenter + labelRadius);
            } else {
                yPos += (yCenter - labelRadius);
            }

            gKnob.setColor(textColor);
            gKnob.drawString(caption, xPos, yPos);
        }

        // Render the knob and surroundings onto the device context
        g.drawImage(potImage, 0, 0, null);

        // Draw the tic on the knob. Position determined from value
        g.setColor(ticColor);

        if (knobUseTics) {
            // Draw the tic
            drawLinePolar(g, xCenter, yCenter, getAngleFromValue(value),
                ticStart, ticLength);
        } else {
            // Draw the dot
            drawDotPolar(g, xCenter, yCenter, getAngleFromValue(value),
                ticStart, ticLength);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    protected void processMouseEvent(MouseEvent e) {
        // Track mouse presses/releases
        switch (e.getID()) {
        case MouseEvent.MOUSE_PRESSED:
            requestFocus();
            mouseDown = true;

            // Figure out where mouse is
            downPt = e.getPoint();

            // Was mouse clicked within the knob ?
            int deltaX = Math.abs(downPt.x - xCenter);
            int deltaY = Math.abs(downPt.y - yCenter);

            if ((deltaX <= radius) && (deltaY <= radius)) {
                mouseInKnob = true;
            } else {
                mouseInKnob = false;
            }

            break;

        case MouseEvent.MOUSE_RELEASED:
            mouseDown = false;

            // If mouse was clicked outside of knob
            if (!mouseInKnob) {
                // If mouse clicked on left side of knob
                if (downPt.x <= xCenter) {
                    setValue(value - blockIncrement);
                    fireAdjustmentEvent();
                } else {
                    setValue(value + blockIncrement);
                    fireAdjustmentEvent();
                }
            }

            break;
        }

        // Let the superclass continue delivery
        super.processMouseEvent(e);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    protected void processMouseMotionEvent(MouseEvent e) {
        // Track mouse drags
        if (e.getID() == MouseEvent.MOUSE_DRAGGED) {
            // Only interested in movement when mouse is down
            if (mouseDown && mouseInKnob) {
                // Figure out where mouse is
                Point pt = e.getPoint();

                // See if movement
                int deltaX = pt.x - downPt.x;

                if (deltaX > 0) {
                    // Move by positive unit increment
                    setValue(value + unitIncrement);
                } else {
                    // Move by negative unit increment
                    setValue(value - unitIncrement);
                }

                fireAdjustmentEvent();

                // Update current position
                downPt.x = pt.x;
                downPt.y = pt.y;
            }
        }

        // Let the superclass continue delivery
        super.processMouseMotionEvent(e);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    protected void processKeyEvent(KeyEvent e) {
        // Simulate a mouse click for certain keys
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_RIGHT) {
            setValue(value + unitIncrement);
            fireAdjustmentEvent();
        } else if (keyCode == KeyEvent.VK_LEFT) {
            setValue(value - unitIncrement);
            fireAdjustmentEvent();
        }

        // Let the superclass continue delivery
        super.processKeyEvent(e);
    }

    // Property methods
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRadius() {
        return radius;
    }

    /**
     * DOCUMENT ME!
     *
     * @param radius DOCUMENT ME!
     */
    public void setRadius(int radius) {
        this.radius = radius;

        // Update values based on radius
        setTicLengthPercent(ticLengthPercent);
        setTicStartPercent(ticStartPercent);
        setGradGapPercent(gradGapPercent);
        setGradLengthPercent(gradLengthPercent);
        setLabelPercent(labelPercent);

        potImage = null;
        sizeToFit();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getTicLengthPercent() {
        return ticLengthPercent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param percent DOCUMENT ME!
     */
    public void setTicLengthPercent(int percent) {
        if ((percent < 0) || (percent > 100)) {
            return;
        }

        ticLengthPercent = percent;

        // Calculate actual length
        ticLength = (percent * radius) / 100;
        potImage = null;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getTicStartPercent() {
        return ticStartPercent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param percent DOCUMENT ME!
     */
    public void setTicStartPercent(int percent) {
        if ((percent < 0) || (percent > 100)) {
            return;
        }

        ticStartPercent = percent;

        // Calculate actual start value
        ticStart = (percent * (radius - ticLength)) / 100;
        potImage = null;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getGradGapPercent() {
        return gradGapPercent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param percent DOCUMENT ME!
     */
    public void setGradGapPercent(int percent) {
        if ((percent < 0) || (percent > 100)) {
            return;
        }

        gradGapPercent = percent;

        // Calculate actual start value
        gradGap = (percent * radius) / 100;
        potImage = null;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getGradLengthPercent() {
        return gradLengthPercent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param percent DOCUMENT ME!
     */
    public void setGradLengthPercent(int percent) {
        if ((percent < 0) || (percent > 100)) {
            return;
        }

        gradLengthPercent = percent;

        // Calculate actual length
        gradLength = (percent * radius) / 100;
        potImage = null;
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

        labelPercent = percent;

        // Calculate actual length
        labelRadius = (percent * radius) / 100;
        potImage = null;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int getGradStart() {
        return radius + gradGap;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int getGradEnd() {
        return radius + gradGap + gradLength;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getKnobUseTics() {
        return knobUseTics;
    }

    /**
     * DOCUMENT ME!
     *
     * @param useTics DOCUMENT ME!
     */
    public void setKnobUseTics(boolean useTics) {
        this.knobUseTics = useTics;
        potImage = null;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getGradUseTics() {
        return gradUseTics;
    }

    /**
     * DOCUMENT ME!
     *
     * @param useTics DOCUMENT ME!
     */
    public void setGradUseTics(boolean useTics) {
        this.gradUseTics = useTics;
        potImage = null;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getCaptionAtBottom() {
        return captionAtBottom;
    }

    /**
     * DOCUMENT ME!
     *
     * @param captionAtBottom DOCUMENT ME!
     */
    public void setCaptionAtBottom(boolean captionAtBottom) {
        this.captionAtBottom = captionAtBottom;
        potImage = null;
        repaint();
    }
}
