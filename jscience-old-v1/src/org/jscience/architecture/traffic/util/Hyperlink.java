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

package org.jscience.architecture.traffic.util;

import java.awt.*;
import java.awt.event.*;

import java.util.Vector;


/**
 * 
 */
public class Hyperlink extends Component {
    /** Indicates that the label should be left justified. */
    public static final int LEFT = 1;

    /** Indicates that the label should be centered. */
    public static final int CENTER = 2;

    /** Indicates that the label should be right justified. */
    public static final int RIGHT = 3;

    /** DOCUMENT ME! */
    protected String label;

    /** DOCUMENT ME! */
    protected boolean underline;

    /** DOCUMENT ME! */
    protected int alignment;

    /** DOCUMENT ME! */
    protected Vector listeners;

    /** DOCUMENT ME! */
    protected Rectangle textBounds;

/**
     * Constructs an empty hyperlink.
     */
    public Hyperlink() {
        this("unlabeled", LEFT);
    }

/**
     * Constructs a new hyperlink with the specified string of text, left
     * justified.
     *
     * @param text The lavel of the new hyperlink.
     */
    public Hyperlink(String text) {
        this(text, LEFT);
    }

/**
     * Constructs a new hyperlink that presents the specified string of text
     * with the specified alignment.
     *
     * @param text The label of the new hyperlink.
     * @param al   The alignment of the label.
     */
    public Hyperlink(String text, int al) {
        super();
        setForeground(Color.blue);
        textBounds = null;
        listeners = new Vector(1);

        Listener listener = new Listener();
        addFocusListener(listener);
        addKeyListener(listener);
        addMouseListener(listener);
        addMouseMotionListener(listener);

        label = text;
        underline = true;
        alignment = al;
    }

    /**
     * Returns the text of this hyperlink.
     *
     * @return DOCUMENT ME!
     */
    public String getText() {
        return label;
    }

    /**
     * Sets the text of this hyperlink to the specified text.
     *
     * @param text DOCUMENT ME!
     */
    public void setText(String text) {
        label = text;
        repaint();
    }

    /**
     * Checks if the text of this hyperlink is underlined.
     *
     * @return DOCUMENT ME!
     */
    public boolean getUnderline() {
        return underline;
    }

    /**
     * Underlines the text of this hyperlink if the specified value is
     * true.
     *
     * @param ul DOCUMENT ME!
     */
    public void setUnderline(boolean ul) {
        underline = ul;
        repaint();
    }

    /**
     * Returns the alignment of this hyperlink.
     *
     * @return DOCUMENT ME!
     */
    public int getAlignment() {
        return alignment;
    }

    /**
     * Sets the alignment of this hyperlink to the specified alignment.
     *
     * @param al DOCUMENT ME!
     */
    public void setAlignment(int al) {
        alignment = al;
        repaint();
    }

    /**
     * Adds the specified action listener to receive action events from
     * this hyperlink. If l is null, no exception is thrown and no action is
     * performed.
     *
     * @param l The action listener to add.
     */
    public void addActionListener(ActionListener l) {
        if (l != null) {
            listeners.add(l);
            enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);
        }
    }

    /**
     * Removes the specified action listener so that it no longer
     * receives action events from this hyperlink. Action events occur when a
     * user releases the left mouse button when the mouse cursor is over this
     * hyperlink. If l is null, no exception is thrown and no action is
     * performed.
     *
     * @param l The action listener to remove.
     */
    public void removeActionListener(ActionListener l) {
        if (l != null) {
            listeners.remove(l);

            if (listeners.isEmpty()) {
                disableEvents(AWTEvent.MOUSE_EVENT_MASK |
                    AWTEvent.KEY_EVENT_MASK);
            }
        }
    }

    /**
     * Removes all action listeners.
     */
    public void removeAllActionListeners() {
        listeners.clear();
    }

    /**
     * Returns an array of the action listeners.
     *
     * @return DOCUMENT ME!
     */
    public ActionListener[] getActionListeners() {
        return (ActionListener[]) listeners.toArray(new ActionListener[1]);
    }

    /**
     * Returns true to allow hyperlinks to receive focus.
     *
     * @return DOCUMENT ME!
     */
    public boolean isFocusTraversable() {
        return false;
    }

    /**
     * Paints this hyperlink on the given Graphics object. It uses the
     * current font and color of the Graphics object to draw the label and
     * possible line under the label. You should use Component.setForeground()
     * and Component.setFont() to change the font and/or color that is used to
     * paint the label. See the AWT docs about the Component class for more
     * information.
     *
     * @param gr The Graphics object to paint this hyperlink on.
     */
    public void paint(Graphics gr) {
        Graphics2D g = (Graphics2D) gr;

        FontMetrics fm = g.getFontMetrics(g.getFont());
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        int height = ascent + descent;
        int width = fm.stringWidth(label);
        int x = 0;
        int y = (int) ((getHeight() - height) / 2);

        if (alignment == CENTER) {
            x = (int) ((getWidth() - width) / 2);
        }

        if (alignment == RIGHT) {
            x = getWidth() - width - 1;
        }

        g.drawString(label, x, (y + ascent) - 1);

        if (underline) {
            g.drawLine(x, (y + height) - 1, x + width, (y + height) - 1);
        }

        if (hasFocus()) {
            float[] pattern = { 1.0f, 1.0f };
            g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 10.0f, pattern, 0.0f));
            g.setColor(Color.black);
            g.drawRect(x, y, width, height);
        }

        textBounds = new Rectangle(x, y, width, height);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paintAll(Graphics g) {
        paint(g);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class Listener implements FocusListener, MouseListener, KeyListener,
        MouseMotionListener {
        /**
         * Dispatches ActionEvents to the registered listeners and
         * requests focus if the user clicked this hyperlink.
         *
         * @param e The mouse event.
         */
        public void mouseClicked(MouseEvent e) {
            if (((e.getModifiers() & MouseEvent.BUTTON1_MASK) == MouseEvent.BUTTON1_MASK) &&
                    isEnabled()) {
                requestFocus();

                ActionEvent ae = new ActionEvent(e.getSource(), e.getID(),
                        label, e.getModifiers());

                for (int i = 0; i < listeners.size(); i++)
                    ((ActionListener) (listeners.get(i))).actionPerformed(ae);
            }
        }

        /**
         * Dispatches ActionEvents to the registered listeners if
         * the user pressed the return or space key.
         *
         * @param e The key event.
         */
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();

            if (((key == KeyEvent.VK_SPACE) || (key == KeyEvent.VK_ENTER)) &&
                    isEnabled()) {
                ActionEvent ae = new ActionEvent(e.getSource(), e.getID(),
                        label, e.getModifiers());

                for (int i = 0; i < listeners.size(); i++)
                    ((ActionListener) (listeners.get(i))).actionPerformed(ae);
            }
        }

        /**
         * Changes the cursor into a hand if the cursor position
         * lies within the bounds of the text
         *
         * @param e DOCUMENT ME!
         */
        public void mouseMoved(MouseEvent e) {
            if ((textBounds != null) && textBounds.contains(e.getPoint()) &&
                    isEnabled()) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                setCursor(null);
            }
        }

        /**
         * Calls repaint to show this hyperlink received focus.
         *
         * @param e DOCUMENT ME!
         */
        public void focusGained(FocusEvent e) {
            repaint();
        }

        /**
         * Calls repaint to show this hyperlink lost focus.
         *
         * @param e DOCUMENT ME!
         */
        public void focusLost(FocusEvent e) {
            repaint();
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void mousePressed(MouseEvent e) {
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void mouseReleased(MouseEvent e) {
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void mouseEntered(MouseEvent e) {
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void mouseExited(MouseEvent e) {
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void mouseDragged(MouseEvent e) {
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void keyPressed(KeyEvent e) {
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void keyTyped(KeyEvent e) {
        }
    }
}
