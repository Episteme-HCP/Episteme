// Potentiometer Base Class
// Written by: Craig A. Lindley
// Last Update: 08/22/98
package org.jscience.awt.pots;

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
public abstract class PotBase extends Canvas implements Adjustable {
    // Constants for defaults
    /** DOCUMENT ME! */
    public static final int POTRANGE = 100;

    /** DOCUMENT ME! */
    public static final Color PANELCOLOR = Color.lightGray;

    /** DOCUMENT ME! */
    public static final Color KNOBCOLOR = Color.darkGray;

    /** DOCUMENT ME! */
    public static final Color TEXTCOLOR = Color.white;

    /** DOCUMENT ME! */
    public static final Color TICCOLOR = Color.white;

    /** DOCUMENT ME! */
    public static final Color GRADCOLOR = Color.black;

    /** DOCUMENT ME! */
    public static final String DEFAULTFONTNAME = "Dialog";

    /** DOCUMENT ME! */
    public static final int DEFAULTFONTSTYLE = Font.PLAIN;

    /** DOCUMENT ME! */
    public static final int DEFAULTFONTSIZE = 9;

    // Private class data
    /** DOCUMENT ME! */
    protected int blockIncrement; // Inc of adjustment

    /** DOCUMENT ME! */
    protected int unitIncrement; // Inc of adjustment

    /** DOCUMENT ME! */
    protected Color panelColor;

    /** DOCUMENT ME! */
    protected Color brighterPanelColor;

    /** DOCUMENT ME! */
    protected Color knobColor;

    /** DOCUMENT ME! */
    protected Color textColor;

    /** DOCUMENT ME! */
    protected Color ticColor;

    /** DOCUMENT ME! */
    protected Color gradColor;

    /** DOCUMENT ME! */
    protected Color highlightBrighterColor;

    /** DOCUMENT ME! */
    protected Color highlightDarkerColor;

    /** DOCUMENT ME! */
    protected boolean hasHighlight;

    /** DOCUMENT ME! */
    protected boolean hasLabels;

    /** DOCUMENT ME! */
    protected transient int value;

    /** DOCUMENT ME! */
    protected int numberOfSections;

    /** DOCUMENT ME! */
    protected String fontName = "Dialog";

    /** DOCUMENT ME! */
    protected int fontStyle = Font.PLAIN;

    /** DOCUMENT ME! */
    protected int fontSize = 8;

    /** DOCUMENT ME! */
    protected Font font;

    /** DOCUMENT ME! */
    protected String caption = "";

    /** DOCUMENT ME! */
    protected Vector labels = new Vector();

    /** DOCUMENT ME! */
    protected AdjustmentListener adjustmentListener = null;

    /** DOCUMENT ME! */
    protected int xCenter;

    /** DOCUMENT ME! */
    protected int yCenter;

    /** DOCUMENT ME! */
    protected boolean mouseDown = false;

    /** DOCUMENT ME! */
    protected boolean mouseInKnob = false;

    /** DOCUMENT ME! */
    protected Point downPt;

    /** DOCUMENT ME! */
    protected boolean hasFocus = false;

    /** DOCUMENT ME! */
    protected Image potImage = null;

    // Class constructor
/**
     * Creates a new PotBase object.
     *
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
     * @param knobColor        DOCUMENT ME!
     * @param textColor        DOCUMENT ME!
     * @param ticColor         DOCUMENT ME!
     * @param gradColor        DOCUMENT ME!
     */
    public PotBase(String fontName, int fontStyle, int fontSize,
        String caption, boolean hasLabels, String labelsString, int value,
        boolean hasHighlight, int numberOfSections, Color panelColor,
        Color knobColor, Color textColor, Color ticColor, Color gradColor) {
        super();

        // Process and save incoming
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
        setKnobColor(knobColor);
        setTextColor(textColor);
        setTicColor(ticColor);
        setGradColor(gradColor);

        // Calculate pots granularity
        blockIncrement = POTRANGE / 10;
        unitIncrement = 1;

        // Enable event processing
        enableEvents(AWTEvent.MOUSE_EVENT_MASK |
            AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);
    }

    // Abstract methods for subclass
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Dimension getPreferredSize();

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public abstract void paint(Graphics g);

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
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected int round(double d) {
        return (int) Math.round(d);
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
        this.panelColor = panelColor;
        this.brighterPanelColor = panelColor.brighter();
        potImage = null;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getKnobColor() {
        return knobColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param knobColor DOCUMENT ME!
     */
    public void setKnobColor(Color knobColor) {
        this.knobColor = knobColor;

        // Calculate highlight colors
        this.highlightBrighterColor = knobColor.brighter();
        this.highlightDarkerColor = knobColor.darker();

        potImage = null;

        // Cause a repaint
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
        potImage = null;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getTicColor() {
        return ticColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ticColor DOCUMENT ME!
     */
    public void setTicColor(Color ticColor) {
        this.ticColor = ticColor;
        potImage = null;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getGradColor() {
        return gradColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param gradColor DOCUMENT ME!
     */
    public void setGradColor(Color gradColor) {
        this.gradColor = gradColor;
        potImage = null;
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
        potImage = null;
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
        newValue = Math.min(POTRANGE, newValue);
        newValue = Math.max(0, newValue);
        this.value = newValue;

        repaint();
    }

    // Simulate a pot with an audio taper. Audio taper pots
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAttenuation() {
        if (value == 0) {
            return 0.0;
        } else if (value == 100) {
            return 1.0;
        } else {
            // Calculate log atteuation
            double exp = -(100.0 - (double) value) / (POTRANGE / 2.0);

            return Math.pow(10, exp);
        }
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
        potImage = null;
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

        potImage = null;
        repaint();
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

        // Size the knob to the text/font
        sizeToFit();
        potImage = null;
        repaint();
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

        // Size the knob to the text/font
        sizeToFit();
        potImage = null;
        repaint();
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

        // Size the knob to the text/font
        sizeToFit();
        potImage = null;
        repaint();
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

        // Size the knob to the text/font
        sizeToFit();
        potImage = null;
        repaint();
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

        // Size the knob to the text/font
        sizeToFit();
        potImage = null;
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
        blockIncrement = POTRANGE / numberOfSections;
        potImage = null;
        repaint();
    }

    // Methods required for Adjustable interface
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getBlockIncrement() {
        return blockIncrement;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void setBlockIncrement(int b) {
        this.blockIncrement = b;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMaximum() {
        return POTRANGE;
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    public void setMaximum(int m) {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMinimum() {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    public void setMinimum(int m) {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getOrientation() {
        return Adjustable.VERTICAL;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getUnitIncrement() {
        return unitIncrement;
    }

    /**
     * DOCUMENT ME!
     *
     * @param unitIncrement DOCUMENT ME!
     */
    public void setUnitIncrement(int unitIncrement) {
        this.unitIncrement = unitIncrement;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getVisibleAmount() {
        return 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     */
    public void setVisibleAmount(int a) {
    }

    /*
            // Could be used to show the knob has input focus
            protected void processFocusEvent(FocusEvent e) {
    
                    // Get the new focus state and repaint
                    switch(e.getID()) {
                            case FocusEvent.FOCUS_GAINED:
                                    hasFocus = true;
                                    repaint();
                                    break;
    
                            case FocusEvent.FOCUS_LOST:
                                    hasFocus = false;
                                    repaint();
                                    break;
                    }
                    // Let the superclass continue delivery
                    super.processFocusEvent(e);
            }
    */

    // Add an adjustment listener
    public synchronized void addAdjustmentListener(AdjustmentListener l) {
        adjustmentListener = AWTEventMulticaster.add(adjustmentListener, l);
    }

    // Remove adjustment listener
    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public synchronized void removeAdjustmentListener(AdjustmentListener l) {
        adjustmentListener = AWTEventMulticaster.remove(adjustmentListener, l);
    }

    // Indicate to all listeners that the pot's adjustment has changed
    /**
     * DOCUMENT ME!
     */
    public void fireAdjustmentEvent() {
        // Synchronously notify the listeners so that they are
        // guaranteed to be up-to-date with the Adjustable before
        // it is mutated again.
        AdjustmentEvent e = new AdjustmentEvent(this,
                AdjustmentEvent.ADJUSTMENT_VALUE_CHANGED,
                AdjustmentEvent.TRACK, value);

        // Send it out if there is a listener registered
        if (adjustmentListener != null) {
            adjustmentListener.adjustmentValueChanged(e);
        }
    }
}
