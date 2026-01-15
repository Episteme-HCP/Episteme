package org.jscience.swing;

import javax.swing.*;
import java.awt.*;


/**
 * The JDoubleBufferedComponent class provides double buffering functionality.
 * Painting events simply cause the offscreen buffer to be painted. It is the
 * responsibility of sub-classes to explicitly update the offscreen buffer.
 * The offscreen buffer can be updated in two ways.
 * <p/>
 * <ol>
 * <li>
 * Override the {@link #offscreenPaint(Graphics) offscreenPaint} method and use
 * the {@link #redraw() redraw} method. Passive rendering.
 * </li>
 * <li>
 * Draw to the graphics context returned by the {@link #getOffscreenGraphics()
 * getOffscreenGraphics} method and use the {@link
 * java.awt.Component#repaint() repaint} method. Active rendering.
 * </li>
 * </ol>
 * <p/>
 * The first way alone should be sufficient for most purposes.
 *
 * @author Mark Hale
 * @version 1.3
 */
public abstract class JDoubleBufferedComponent extends JComponent {
    /**
     * DOCUMENT ME!
     */
    private Image buffer = null;

    /**
     * DOCUMENT ME!
     */
    private boolean doRedraw = true;

    /**
     * Constructs a double buffered canvas.
     */
    public JDoubleBufferedComponent() {
        super.setDoubleBuffered(false);
    }

    /**
     * Paints the canvas using double buffering.
     *
     * @see #offscreenPaint
     */
    public final void paintComponent(Graphics g) {
        Insets insets = getInsets();

        if (doRedraw) {
            doRedraw = false;

            final int width = getWidth() - insets.left - insets.right;
            final int height = getHeight() - insets.top - insets.bottom;
            buffer = createImage(width, height);

            if (buffer == null) {
                return;
            }

            final Graphics graphics = buffer.getGraphics();

            /* save original color */
            Color oldColor = graphics.getColor();
            graphics.setColor(getBackground());
            graphics.fillRect(insets.left, insets.top, width, height);

            /* restore original color */
            graphics.setColor(oldColor);
            offscreenPaint(graphics);
        }

        g.drawImage(buffer, insets.left, insets.top, null);
    }

    /**
     * Updates the canvas.
     *
     * @param g DOCUMENT ME!
     */
    public final void update(Graphics g) {
        paint(g);
    }

    /**
     * Prints the canvas.
     *
     * @param g DOCUMENT ME!
     */
    public final void printComponent(Graphics g) {
        offscreenPaint(g);
    }

    /**
     * Double buffering cannot be controlled for this component. This method
     * always throws an exception.
     *
     * @param flag DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public final void setDoubleBuffered(boolean flag) {
        throw new IllegalArgumentException();
    }

    /**
     * Double buffering is always enabled.
     *
     * @return true.
     */
    public final boolean isDoubleBuffered() {
        return true;
    }

    /**
     * Redraws the canvas. This method may safely be called from outside the
     * event-dispatching thread.
     */
    public final void redraw() {
        doRedraw = true;
        repaint();
    }

    /**
     * Returns the offscreen graphics context or <code>null</code> if not
     * available.
     *
     * @return DOCUMENT ME!
     */
    protected final Graphics getOffscreenGraphics() {
        return (buffer != null) ? buffer.getGraphics() : null;
    }

    /**
     * Paints the canvas off-screen. Override this method instead of
     * paint(Graphics g).
     *
     * @param g DOCUMENT ME!
     */
    protected abstract void offscreenPaint(Graphics g);
}
