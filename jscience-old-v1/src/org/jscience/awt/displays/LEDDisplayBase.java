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

// LEDDisplayBase Class using 7 segment displays
// Written by: Craig A. Lindley
// Last Update: 04/04/99
package org.jscience.awt.displays;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public abstract class LEDDisplayBase extends Canvas {
    // Misc space padding values
    /** DOCUMENT ME! */
    public static final int XPAD = 5;

    /** DOCUMENT ME! */
    public static final int YPAD = 7;

    /** DOCUMENT ME! */
    public static final int MINUSSIGNHEIGHT = 3;

    // Separator width between digits
    /** DOCUMENT ME! */
    public static final double SEPARATORWIDTHPERCENT = .3;

    // Misc default values
    /** DOCUMENT ME! */
    public static final Color DEFAULTPANELCOLOR = Color.lightGray;

    /** DOCUMENT ME! */
    public static final Color DEFAULTTEXTCOLOR = Color.white;

    /** DOCUMENT ME! */
    public static final Color DEFAULTLEDCOLOR = Color.red;

    /** DOCUMENT ME! */
    public static final Color DEFAULTLEDBGCOLOR = Color.gray;

    /** DOCUMENT ME! */
    public static final String DEFAULTFONTNAME = "Dialog";

    /** DOCUMENT ME! */
    public static final int DEFAULTFONTSTYLE = Font.PLAIN;

    /** DOCUMENT ME! */
    public static final int DEFAULTFONTSIZE = 9;

    // Private class data
    /** DOCUMENT ME! */
    protected int width;

    /** DOCUMENT ME! */
    protected int height;

    /** DOCUMENT ME! */
    protected int digitWidth;

    /** DOCUMENT ME! */
    protected int digitHeight;

    /** DOCUMENT ME! */
    protected int separatorWidth;

    /** DOCUMENT ME! */
    protected int numberOfDigits;

    /** DOCUMENT ME! */
    protected int separatorMode;

    /** DOCUMENT ME! */
    protected boolean raised;

    /** DOCUMENT ME! */
    protected Color ledOnColor;

    /** DOCUMENT ME! */
    protected Color ledOffColor;

    /** DOCUMENT ME! */
    protected Color ledBGColor;

    /** DOCUMENT ME! */
    protected Color panelColor;

    /** DOCUMENT ME! */
    protected Color textColor;

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
    protected boolean captionAtBottom;

    /** DOCUMENT ME! */
    protected Image[] digitImages;

    /** DOCUMENT ME! */
    protected boolean digitsValid = false;

/**
     * Creates a new LEDDisplayBase object.
     *
     * @param width           DOCUMENT ME!
     * @param height          DOCUMENT ME!
     * @param numberOfDigits  DOCUMENT ME!
     * @param raised          DOCUMENT ME!
     * @param fontName        DOCUMENT ME!
     * @param fontStyle       DOCUMENT ME!
     * @param fontSize        DOCUMENT ME!
     * @param caption         DOCUMENT ME!
     * @param captionAtBottom DOCUMENT ME!
     * @param panelColor      DOCUMENT ME!
     * @param ledColor        DOCUMENT ME!
     * @param ledBGColor      DOCUMENT ME!
     * @param textColor       DOCUMENT ME!
     */
    public LEDDisplayBase(int width, int height, int numberOfDigits,
        boolean raised, String fontName, int fontStyle, int fontSize,
        String caption, boolean captionAtBottom, Color panelColor,
        Color ledColor, Color ledBGColor, Color textColor) {
        // Allow the superclass constructor to do its thing
        super();

        // Save incoming
        setRaised(raised);
        setFontName(fontName);
        setFontStyle(fontStyle);
        setFontSize(fontSize);
        setCaption(caption);
        setCaptionAtBottom(captionAtBottom);
        setPanelColor(panelColor);
        setLEDColor(ledColor);
        setLEDBGColor(ledBGColor);
        setTextColor(textColor);
        setNumberOfDigits(numberOfDigits);
        setWidth(width);
        setHeight(height);

        // Create array for holding digit images 0 ..9
        digitImages = new Image[10];
    }

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
     * @return DOCUMENT ME!
     */
    public Dimension getPreferredSize() {
        // Calculate the preferred size based on the caption text
        FontMetrics fm = getFontMetrics(font);
        int charHeight = fm.getMaxAscent() + fm.getMaxDescent();
        int charWidth = fm.charWidth('0');

        int minHeight = YPAD + height + charHeight;
        int minWidth = (2 * XPAD) + width;
        int captionWidth = getCaption().length() * charWidth;
        minWidth = Math.max(minWidth, captionWidth);

        return new Dimension(minWidth, minHeight);
    }

    /**
     * DOCUMENT ME!
     */
    public void renderDigits() {
        // Generate the polygons for the 7 segment display segments
        Polygon[] segments = SevenSegmentDisplay.generateSegments(digitWidth,
                digitHeight);

        // Generate an image for each digit
        for (int digit = 0; digit < 10; digit++) {
            // Create an offscreen image for the digit
            Image digitImage = createImage(digitWidth, digitHeight);

            // Store the digit image into array of digits
            digitImages[digit] = digitImage;

            // Get graphic context for offscreen digit image
            Graphics dg = digitImage.getGraphics();

            // Render the digit into the offscreen image
            SevenSegmentDisplay.drawDigit(dg, segments, digit, digitWidth,
                digitHeight, ledBGColor, ledOnColor, ledOffColor);
        }
    }

    // Accessor methods
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getRaised() {
        return raised;
    }

    /**
     * DOCUMENT ME!
     *
     * @param raised DOCUMENT ME!
     */
    public void setRaised(boolean raised) {
        this.raised = raised;
        repaint();
    }

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

        // Calculate width of digits and separators
        double dw = ((double) width) / (numberOfDigits * (1 +
            SEPARATORWIDTHPERCENT));
        double sw = dw * SEPARATORWIDTHPERCENT;

        // Truncate to integers
        digitWidth = (int) dw;
        separatorWidth = (int) sw;

        digitsValid = false;
        sizeToFit();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int calcDisplayWidth() {
        return (digitWidth + separatorWidth) * numberOfDigits;
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
        digitHeight = height;
        digitsValid = false;
        sizeToFit();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumberOfDigits() {
        return numberOfDigits;
    }

    /**
     * DOCUMENT ME!
     *
     * @param numberOfDigits DOCUMENT ME!
     */
    public void setNumberOfDigits(int numberOfDigits) {
        this.numberOfDigits = numberOfDigits;
        digitsValid = false;
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
        this.panelColor = panelColor;
        repaint();
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

        // Go 4 shades darker for the off color
        this.ledOffColor = ledOnColor.darker().darker().darker().darker();

        digitsValid = false;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getLEDBGColor() {
        return ledBGColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ledBGColor DOCUMENT ME!
     */
    public void setLEDBGColor(Color ledBGColor) {
        // Save the on color
        this.ledBGColor = ledBGColor;

        digitsValid = false;
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

        // Size the display to the text/font
        sizeToFit();
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
        repaint();
    }
}
