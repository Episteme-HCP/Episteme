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

package org.jscience.awt;

import java.awt.*;


/**
 * The DoubleBufferedCanvas class provides double buffering functionality.
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
public abstract class DoubleBufferedCanvas extends Canvas {
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
    public DoubleBufferedCanvas() {
    }

    /**
     * Paints the canvas using double buffering.
     *
     * @see #offscreenPaint
     */
    public final void paint(Graphics g) {
        if (doRedraw) {
            doRedraw = false;

            final int width = getSize().width;
            final int height = getSize().height;
            buffer = createImage(width, height);

            if (buffer == null) {
                return;
            }

            final Graphics graphics = buffer.getGraphics();

            /* save original color */
            Color oldColor = graphics.getColor();
            graphics.setColor(getBackground());
            graphics.fillRect(0, 0, width, height);

            /* restore original color */
            graphics.setColor(oldColor);
            offscreenPaint(graphics);
        }

        g.drawImage(buffer, 0, 0, null);
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
    public final void print(Graphics g) {
        offscreenPaint(g);
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
