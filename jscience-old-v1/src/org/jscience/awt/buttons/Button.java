// Button Base Class
// Written by: Craig A. Lindley
// Last Update: 06/28/98
package org.jscience.awt.buttons;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public abstract class Button extends Canvas {
    /** DOCUMENT ME! */
    public static final Color PANELCOLOR = Color.lightGray;

    /** DOCUMENT ME! */
    public static final Color BUTTONCOLOR = Color.darkGray;

    /** DOCUMENT ME! */
    public static final Color TEXTCOLOR = Color.white;

    /** DOCUMENT ME! */
    public static final String DEFAULTFONTNAME = "Dialog";

    /** DOCUMENT ME! */
    public static final int DEFAULTFONTSTYLE = Font.PLAIN;

    /** DOCUMENT ME! */
    public static final int DEFAULTFONTSIZE = 9;

    // Class data
    /** DOCUMENT ME! */
    protected int width;

    /** DOCUMENT ME! */
    protected int height;

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
    protected boolean sticky;

    /** DOCUMENT ME! */
    protected boolean state;

    /** DOCUMENT ME! */
    protected boolean hasHighlight;

    /** DOCUMENT ME! */
    protected Color panelColor;

    /** DOCUMENT ME! */
    protected Color buttonColor;

    /** DOCUMENT ME! */
    protected Color textColor;

    /** DOCUMENT ME! */
    protected Color highlightBrighterColor;

    /** DOCUMENT ME! */
    protected Color highlightDarkerColor;

    /** DOCUMENT ME! */
    protected transient ActionListener actionListener = null;

    /** DOCUMENT ME! */
    protected Image onImage = null;

    /** DOCUMENT ME! */
    protected Image offImage = null;

    // Full strength constructor sets every property of button
    /**
     * Creates a new Button object.
     *
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     * @param fontName DOCUMENT ME!
     * @param fontStyle DOCUMENT ME!
     * @param fontSize DOCUMENT ME!
     * @param caption DOCUMENT ME!
     * @param captionAtBottom DOCUMENT ME!
     * @param sticky DOCUMENT ME!
     * @param state DOCUMENT ME!
     * @param hasHighlight DOCUMENT ME!
     * @param panelColor DOCUMENT ME!
     * @param buttonColor DOCUMENT ME!
     * @param textColor DOCUMENT ME!
     */
    public Button(int width, int height, String fontName, int fontStyle,
        int fontSize, String caption, boolean captionAtBottom, boolean sticky,
        boolean state, boolean hasHighlight, Color panelColor,
        Color buttonColor, Color textColor) {
        // Allow the superclass constructor to do its thing
        super();

        // Save incoming
        setFontName(fontName);
        setFontStyle(fontStyle);
        setFontSize(fontSize);
        setWidth(width);
        setHeight(height);
        setCaption(caption);
        setCaptionAtBottom(captionAtBottom);
        setSticky(sticky);
        setState(state);
        setHighlight(hasHighlight);
        setPanelColor(panelColor);
        setButtonColor(buttonColor);
        setTextColor(textColor);

        // Enable event processing
        enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Dimension getPreferredSize();

    /**
     * DOCUMENT ME!
     */
    private void sizeToFit() {
        // Resize to the preferred size
        Dimension d = getPreferredSize();
        setSize(d);

        Component p = getParent();

        if (p != null) {
            p.invalidate();
            p.validate();
        }
    }

    // Paint method
    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public abstract void paint(Graphics g);

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

        // Force generation of new bitmaps if any
        onImage = null;

        // Size the knob to the text/font
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

        // Force generation of new bitmaps if any
        onImage = null;

        // Size the knob to the text/font
        sizeToFit();
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

        // Force generation of new bitmaps if any
        onImage = null;

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

        // Force generation of new bitmaps if any
        onImage = null;

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

        // Force generation of new bitmaps if any
        onImage = null;

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

        // Force generation of new bitmaps if any
        onImage = null;

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

        // Force generation of new bitmaps if any
        onImage = null;

        // Size the knob to the text/font
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

        // Force generation of new bitmaps if any
        onImage = null;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getSticky() {
        return sticky;
    }

    /**
     * DOCUMENT ME!
     *
     * @param sticky DOCUMENT ME!
     */
    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getState() {
        return state;
    }

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void setState(boolean state) {
        this.state = state;
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

        // Force generation of new bitmaps if any
        onImage = null;

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

        // Force generation of new bitmaps if any
        onImage = null;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getButtonColor() {
        return buttonColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param buttonColor DOCUMENT ME!
     */
    public void setButtonColor(Color buttonColor) {
        this.buttonColor = buttonColor;

        // Calculate highlight colors
        this.highlightBrighterColor = buttonColor.brighter();
        this.highlightDarkerColor = buttonColor.darker();

        // Force generation of new bitmaps if any
        onImage = null;

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

        // Force generation of new bitmaps if any
        onImage = null;
        repaint();
    }

    // Event processing methods
    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public synchronized void addActionListener(ActionListener l) {
        actionListener = AWTEventMulticaster.add(actionListener, l);
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public synchronized void removeActionListener(ActionListener l) {
        actionListener = AWTEventMulticaster.remove(actionListener, l);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    protected void processActionEvent(ActionEvent e) {
        // Deliver the event to all registered action event listeners
        if (actionListener != null) {
            actionListener.actionPerformed(e);
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
            state = !state;
            repaint();
            fireActionEvent();

            break;

        case MouseEvent.MOUSE_RELEASED:

            if (state && !sticky) {
                state = false;
                fireActionEvent();
                repaint();
            }

            break;
        }

        // Let the superclass continue delivery
        super.processMouseEvent(e);
    }

    // Due to the duration of the key event,  using keyboard keys in
    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    protected void processKeyEvent(KeyEvent e) {
        // Simulate a mouse click for certain keys
        if ((e.getKeyChar() == KeyEvent.VK_ENTER) ||
                (e.getKeyChar() == KeyEvent.VK_SPACE)) {
            if (sticky) {
                state = !state;
                repaint();
            } else {
                state = true;
                repaint();
                state = false;
                repaint();
            }

            fireActionEvent();
        }

        // Let the superclass continue delivery
        super.processKeyEvent(e);
    }

    /**
     * DOCUMENT ME!
     */
    private void fireActionEvent() {
        processActionEvent(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                state ? "ON" : "OFF"));
    }
}
