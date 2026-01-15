// ToggleSwitchButton Class
// Written by: Craig A. Lindley
// Last Update: 03/14/99
package org.jscience.awt.buttons;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ToggleSwitchButton extends Button {
    /** DOCUMENT ME! */
    private static final int XPAD = 5;

    /** DOCUMENT ME! */
    private static final int YPAD = 5;

    /** DOCUMENT ME! */
    private static final double HEIGHTWIDTHRATIO = 2.75;

    /** DOCUMENT ME! */
    private static final double HIGHLIGHTPERCENT = 0.2;

    /** DOCUMENT ME! */
    private static final double CAPTIONPERCENT = 0.9;

    // Private class data
    /** DOCUMENT ME! */
    private String topCaption = "";

    /** DOCUMENT ME! */
    private String bottomCaption = "";

    /** DOCUMENT ME! */
    private int minWidth = 0;

    /** DOCUMENT ME! */
    private int minHeight = 0;

    /** DOCUMENT ME! */
    private int highlightOffset;

    /** DOCUMENT ME! */
    private int captionOffset;

    // Full strength constructor sets every property of button
    /**
     * Creates a new ToggleSwitchButton object.
     *
     * @param width DOCUMENT ME!
     * @param fontName DOCUMENT ME!
     * @param fontStyle DOCUMENT ME!
     * @param fontSize DOCUMENT ME!
     * @param topCaption DOCUMENT ME!
     * @param bottomCaption DOCUMENT ME!
     * @param sticky DOCUMENT ME!
     * @param state DOCUMENT ME!
     * @param panelColor DOCUMENT ME!
     * @param buttonColor DOCUMENT ME!
     * @param textColor DOCUMENT ME!
     */
    public ToggleSwitchButton(int width, String fontName, int fontStyle,
        int fontSize, String topCaption, String bottomCaption, boolean sticky,
        boolean state, Color panelColor, Color buttonColor, Color textColor) {
        // Allow the superclass constructor to do its thing
        super(width, (int) (width * HEIGHTWIDTHRATIO), fontName, fontStyle,
            fontSize, "", true, sticky, state, true, panelColor, buttonColor,
            textColor);

        this.topCaption = topCaption;
        this.bottomCaption = bottomCaption;
        this.highlightOffset = (int) (width * HIGHLIGHTPERCENT);
        this.captionOffset = (int) (height * CAPTIONPERCENT);

        onImage = null;
    }

    // Constructor with some reasonable defaults
    /**
     * Creates a new ToggleSwitchButton object.
     *
     * @param width DOCUMENT ME!
     * @param bottomCaption DOCUMENT ME!
     */
    public ToggleSwitchButton(int width, String bottomCaption) {
        this(width, DEFAULTFONTNAME, DEFAULTFONTSTYLE, DEFAULTFONTSIZE, "",
            bottomCaption, true, false, PANELCOLOR, BUTTONCOLOR, TEXTCOLOR);
    }

    // Constructor with some reasonable defaults
    /**
     * Creates a new ToggleSwitchButton object.
     *
     * @param width DOCUMENT ME!
     * @param topCaption DOCUMENT ME!
     * @param bottomCaption DOCUMENT ME!
     */
    public ToggleSwitchButton(int width, String topCaption, String bottomCaption) {
        this(width, DEFAULTFONTNAME, DEFAULTFONTSTYLE, DEFAULTFONTSIZE,
            topCaption, bottomCaption, true, false, PANELCOLOR, BUTTONCOLOR,
            TEXTCOLOR);
    }

    // Zero argument constructor
    /**
     * Creates a new ToggleSwitchButton object.
     */
    public ToggleSwitchButton() {
        this(10, "Bypass");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dimension getPreferredSize() {
        if (bottomCaption == null) {
            return new Dimension(width, height);
        }

        // Calculate the preferred size based on the label text
        FontMetrics fm = getFontMetrics(font);

        int captionWidth = Math.max(fm.stringWidth(topCaption),
                fm.stringWidth(bottomCaption));
        int maxAscent = fm.getMaxAscent();
        int maxDescent = fm.getMaxDescent();
        int maxCharHeight = maxAscent + maxDescent;

        minWidth = Math.max(width, captionWidth);
        minWidth += (2 * XPAD);

        int capOffset = Math.max(captionOffset, height / 2);

        minHeight = 2 * (capOffset + YPAD + maxCharHeight);

        return new Dimension(minWidth, minHeight);
    }

    // Override base method so caption offset can be calculated
    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     */
    public void setWidth(int width) {
        int newHeight = (int) (width * HEIGHTWIDTHRATIO);
        super.setWidth(width);
        super.setHeight(newHeight);
    }

    /**
     * DOCUMENT ME!
     *
     * @param topCaption DOCUMENT ME!
     */
    public void setTopCaption(String topCaption) {
        this.topCaption = topCaption;
        onImage = null;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTopCaption() {
        return topCaption;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bottomCaption DOCUMENT ME!
     */
    public void setBottomCaption(String bottomCaption) {
        this.bottomCaption = bottomCaption;
        onImage = null;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getBottomCaption() {
        return bottomCaption;
    }

    /**
     * DOCUMENT ME!
     *
     * @param isOn DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Image generateSwitchImage(boolean isOn) {
        // Calculate important values relative to dimensions
        // of bitmap image.
        int xCenter = minWidth / 2;
        int yCenter = minHeight / 2;

        // Create the toggle switch image
        Image toggleImage = createImage(minWidth, minHeight);

        // Get the graphics contexts
        Graphics gToggleImage = toggleImage.getGraphics();

        // Paint the panel
        gToggleImage.setColor(panelColor);
        gToggleImage.fillRect(0, 0, minWidth, minHeight);

        // Set font into the graphics context
        gToggleImage.setFont(font);

        FontMetrics fm = gToggleImage.getFontMetrics();

        // Draw the captions. Top then Bottom
        gToggleImage.setColor(textColor);

        int captionWidth = fm.stringWidth(topCaption);
        int captionXOffset = (minWidth - captionWidth) / 2;
        int captionYOffset = yCenter - captionOffset;
        gToggleImage.drawString(topCaption, captionXOffset, captionYOffset);

        captionWidth = fm.stringWidth(bottomCaption);
        captionXOffset = (minWidth - captionWidth) / 2;
        captionYOffset = yCenter + captionOffset + fm.getHeight();
        gToggleImage.drawString(bottomCaption, captionXOffset, captionYOffset);

        int hWidth = width / 2;
        int hHeight = height / 2;

        // Draw the outer switch well
        gToggleImage.setColor(Color.white);

        int xOrg = xCenter - hWidth;
        int yOrg = yCenter - hHeight;

        int largeWidth = width + highlightOffset;
        int largeHeight = height + highlightOffset;
        gToggleImage.fillRoundRect(xOrg, yOrg, largeWidth, largeHeight, width,
            width);

        // Draw the inner switch well
        gToggleImage.setColor(Color.darkGray);
        gToggleImage.fillRoundRect(xOrg, yOrg, width, height, width, width);

        // Now the switch shaft
        gToggleImage.setColor(buttonColor);

        int wsShaftWidth = (width * 3) / 4;
        int hsShaftWidth = wsShaftWidth / 2;
        int wlShaftWidth = width;
        int hlShaftWidth = wlShaftWidth / 2;
        int wShaftLength = (5 * height) / 4;
        int hShaftLength = wShaftLength / 2;

        Polygon p = new Polygon();
        p.addPoint(xCenter - hsShaftWidth, yCenter);
        p.addPoint(xCenter - hlShaftWidth,
            isOn ? (yCenter - hShaftLength) : (yCenter + hShaftLength));
        p.addPoint(xCenter + hlShaftWidth,
            isOn ? (yCenter - hShaftLength) : (yCenter + hShaftLength));
        p.addPoint(xCenter + hsShaftWidth, yCenter);
        gToggleImage.fillPolygon(p);

        // Now the shaft head
        gToggleImage.setColor(highlightBrighterColor);
        gToggleImage.fillOval(xCenter - hlShaftWidth,
            isOn ? (yCenter - (hShaftLength + hlShaftWidth))
                 : (yCenter + (hShaftLength - hlShaftWidth)), wlShaftWidth,
            wlShaftWidth);

        return toggleImage;
    }

    // Paint method
    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        int cwidth = getSize().width;
        int cheight = getSize().height;
        int xCenter = cwidth / 2;
        int yCenter = cheight / 2;

        // Calc position of switch in graphics context
        int toggleOrgX = (cwidth - minWidth) / 2;
        int toggleOrgY = (cheight - minHeight) / 2;

        // See if we have toggle switch images for displaying
        if (onImage == null) {
            onImage = generateSwitchImage(true);

            offImage = generateSwitchImage(false);
        }

        // State is reversed if not in sticky mode
        boolean newState = state;

        if (!getSticky()) {
            newState = !state;
        }

        // Render the switch into the device context
        if (newState) {
            g.drawImage(onImage, toggleOrgX, toggleOrgY, null);
        } else {
            g.drawImage(offImage, toggleOrgX, toggleOrgY, null);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public static void o(String s) {
        System.out.println(s);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        Frame f = new Frame("Test");
        ToggleSwitchButton tsb = new ToggleSwitchButton();
        tsb.setTopCaption("Work");
        tsb.setState(true);
        f.add(tsb);

        Dimension d = tsb.getPreferredSize();
        d.width *= 2;
        d.height *= 2;
        f.setSize(d);
        f.setVisible(true);
    }
}
