// RoundButton Class
// Written by: Craig A. Lindley
// Last Update: 04/18/98
package org.jscience.awt.buttons;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class RoundButton extends Button {
    /** DOCUMENT ME! */
    private static final int XPAD = 10;

    /** DOCUMENT ME! */
    private static final int YPAD = 10;

    /** DOCUMENT ME! */
    private static final int INNERBUTTONPERCENT = 60;

    // Full strength constructor sets every property of button
/**
     * Creates a new RoundButton object.
     *
     * @param width           DOCUMENT ME!
     * @param height          DOCUMENT ME!
     * @param fontName        DOCUMENT ME!
     * @param fontStyle       DOCUMENT ME!
     * @param fontSize        DOCUMENT ME!
     * @param caption         DOCUMENT ME!
     * @param captionAtBottom DOCUMENT ME!
     * @param sticky          DOCUMENT ME!
     * @param state           DOCUMENT ME!
     * @param hasHighlight    DOCUMENT ME!
     * @param panelColor      DOCUMENT ME!
     * @param buttonColor     DOCUMENT ME!
     * @param textColor       DOCUMENT ME!
     */
    public RoundButton(int width, int height, String fontName, int fontStyle,
        int fontSize, String caption, boolean captionAtBottom, boolean sticky,
        boolean state, boolean hasHighlight, Color panelColor,
        Color buttonColor, Color textColor) {
        // Allow the superclass constructor to do its thing
        super(width, height, fontName, fontStyle, fontSize, caption,
            captionAtBottom, sticky, state, hasHighlight, panelColor,
            buttonColor, textColor);
    }

    // Constructor with some reasonable defaults
/**
     * Creates a new RoundButton object.
     *
     * @param width   DOCUMENT ME!
     * @param height  DOCUMENT ME!
     * @param caption DOCUMENT ME!
     */
    public RoundButton(int width, int height, String caption) {
        this(width, height, DEFAULTFONTNAME, DEFAULTFONTSTYLE, DEFAULTFONTSIZE,
            caption, true, true, false, true, PANELCOLOR, BUTTONCOLOR, TEXTCOLOR);
    }

    // Zero argument constructor
/**
     * Creates a new RoundButton object.
     */
    public RoundButton() {
        this(20, 20, "Press Me");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dimension getPreferredSize() {
        // Calculate the preferred size based on the label text
        FontMetrics fm = getFontMetrics(font);

        int captionWidth = fm.stringWidth(caption);
        int maxAscent = fm.getMaxAscent();
        int maxDescent = fm.getMaxDescent();
        int maxCharHeight = maxAscent + maxDescent;

        int minWidth = Math.max(width, captionWidth);
        minWidth += (2 * XPAD);

        int minHeight = height + YPAD + maxCharHeight;

        return new Dimension(minWidth, minHeight);
    }

    // Paint method
    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        // Get dimensions of component
        int cwidth = getSize().width;
        int cheight = getSize().height;

        // Paint the panel
        g.setColor(panelColor);
        g.fillRect(0, 0, cwidth, cheight);

        // Set font into the graphics context
        g.setFont(font);

        FontMetrics fm = g.getFontMetrics();

        // Calculate important dimensions
        int xCenter = cwidth / 2;
        int halfHeight = height / 2;
        int yCenter = captionAtBottom ? (YPAD + halfHeight)
                                      : (fm.getMaxDescent() + YPAD +
            halfHeight);
        int xLargeOrg = xCenter - (width / 2);
        int yLargeOrg = yCenter - (height / 2);

        int smallWidth = (width * INNERBUTTONPERCENT) / 100;
        int smallHeight = (height * INNERBUTTONPERCENT) / 100;

        int xSmallOrg = xCenter - (smallWidth / 2);
        int ySmallOrg = yCenter - (smallHeight / 2);

        // Draw the button
        g.setColor(buttonColor);
        g.fillOval(xLargeOrg, yLargeOrg, width, height);

        if (state) {
            // Button is on
            g.setColor(Color.white);
            g.fillOval(xSmallOrg, ySmallOrg, smallWidth, smallHeight);
        }

        // Draw the caption
        int captionWidth = fm.stringWidth(caption);
        int captionXOffset = (cwidth - captionWidth) / 2;
        int captionYOffset = captionAtBottom
            ? (height + YPAD + fm.getMaxAscent()) : YPAD;
        g.setColor(textColor);
        g.drawString(caption, captionXOffset, captionYOffset);
    }
}
