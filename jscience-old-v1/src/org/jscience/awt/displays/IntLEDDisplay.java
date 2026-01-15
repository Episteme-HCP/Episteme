// IntLEDDisplay Class using 7 segment displays
// Written by: Craig A. Lindley
// Last Update: 03/21/99
package org.jscience.awt.displays;

import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class IntLEDDisplay extends LEDDisplayBase implements AdjustmentListener {
    // Private class data
    /** DOCUMENT ME! */
    private int separatorMode;

    /** DOCUMENT ME! */
    private int value;

/**
     * Creates a new IntLEDDisplay object.
     *
     * @param width           DOCUMENT ME!
     * @param height          DOCUMENT ME!
     * @param numberOfDigits  DOCUMENT ME!
     * @param value           DOCUMENT ME!
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
    public IntLEDDisplay(int width, int height, int numberOfDigits, int value,
        boolean raised, String fontName, int fontStyle, int fontSize,
        String caption, boolean captionAtBottom, Color panelColor,
        Color ledColor, Color ledBGColor, Color textColor) {
        // Allow the superclass constructor to do its thing
        super(width, height, numberOfDigits, raised, fontName, fontStyle,
            fontSize, caption, captionAtBottom, panelColor, ledColor,
            ledBGColor, textColor);

        setValue(value);
    }

    // Constructor with reasonable defaults
/**
     * Creates a new IntLEDDisplay object.
     *
     * @param width          DOCUMENT ME!
     * @param height         DOCUMENT ME!
     * @param numberOfDigits DOCUMENT ME!
     * @param value          DOCUMENT ME!
     */
    public IntLEDDisplay(int width, int height, int numberOfDigits, int value) {
        this(width, height, numberOfDigits, value, false, DEFAULTFONTNAME,
            DEFAULTFONTSTYLE, DEFAULTFONTSIZE, "LEDDisplay", true,
            DEFAULTPANELCOLOR, DEFAULTLEDCOLOR, DEFAULTLEDBGCOLOR,
            DEFAULTTEXTCOLOR);
    }

    // Zero argument constructor for the bean box
/**
     * Creates a new IntLEDDisplay object.
     */
    public IntLEDDisplay() {
        this(90, 40, 3, 234);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        int cwidth = getSize().width;
        int cheight = getSize().height;

        // Paint the panel color
        g.setColor(panelColor);
        g.fillRect(0, 0, cwidth, cheight);

        // Set font into the graphics context to get font metrics
        g.setFont(font);

        FontMetrics fm = g.getFontMetrics();

        // Get various text attributes
        int charHeight = fm.getHeight();
        int charOffset = charHeight / 2;
        int textWidth = fm.stringWidth(caption);

        // Calculate position of first digit
        int xOrg = (cwidth - calcDisplayWidth()) / 2;
        int yOrg = captionAtBottom ? YPAD : (YPAD + charHeight);

        // Fill the background around segments
        g.setColor(ledBGColor);
        g.fillRect(xOrg, yOrg, width, height);

        // Determine if digits have been generated
        if (!digitsValid) {
            // Render the digits
            renderDigits();

            // Digit images are now valid for this size 7 segment display
            digitsValid = true;
        }

        // Draw minus sign if required
        if (value < 0) {
            // Draw the minus sign
            int halfHeight = digitHeight / 2;
            int halfSignHeight = MINUSSIGNHEIGHT / 2;
            int x = xOrg;
            int y = (yOrg + halfHeight) - halfSignHeight;
            int width = separatorWidth - 1;
            int height = MINUSSIGNHEIGHT;

            g.setColor(ledOnColor);
            g.fillRect(x, y, width, height);
        }

        // Display absolute value
        int displayValue = Math.abs(value);

        // Draw the digits. MSD to LSD
        int xDigitPos = xOrg + separatorWidth;

        for (int i = 0; i < numberOfDigits; i++) {
            int div = (int) Math.pow(10.0, (double) (numberOfDigits - i - 1));

            g.drawImage(digitImages[(displayValue / div) % 10], xDigitPos,
                yOrg, this);

            xDigitPos += (digitWidth + separatorWidth);
        }

        // Draw the caption
        g.setColor(textColor);

        int textXOffset = (cwidth - textWidth) / 2;
        int textYOffset = captionAtBottom ? (YPAD + height + charHeight)
                                          : (YPAD + charOffset);
        g.drawString(caption, textXOffset, textYOffset);
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
     * @param value DOCUMENT ME!
     */
    public void setValue(int value) {
        this.value = value;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void adjustmentValueChanged(AdjustmentEvent e) {
        setValue(e.getValue());
    }
}
