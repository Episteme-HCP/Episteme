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

package org.jscience.tests.computing.ai.swing;

import java.awt.*;

import javax.swing.*;


/**
 * This JPanel-derived class acts a simple way of displaying classes that
 * implement the Visualizable interface.
 *
 * @author James Matthews
 *
 * @see org.jscience.util.Visualizable
 */
public class VisualizationPanel extends JPanel {
    /** Boolean value to specify whether anti-aliasing is to be used. */
    protected boolean antiAliasing;

    /** DOCUMENT ME! */
    private java.awt.Color clrBackground;

    /** DOCUMENT ME! */
    private org.jscience.util.Visualizable visualizeContent;

/**
     * Default constructor
     */
    public VisualizationPanel() {
        clrBackground = new java.awt.Color(223, 223, 223);
    }

    /**
     * Sets the content to display. Note that the panel itself has no
     * control over how the content is rendered within itself, much beyond
     * passing the width and height of the display region to the content's
     * render function.
     *
     * @param content the content to render in the panel
     */
    public void setContent(org.jscience.util.Visualizable content) {
        visualizeContent = content;
    }

    /**
     * Paint the content on the panel.
     *
     * @param graphics the graphics interface
     *
     * @see org.jscience.util.Visualizable#render(Graphics,int,int)
     */
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (visualizeContent == null) {
            // todo: just use the null viz stuff?
        } else {
            int pw = getWidth();
            int ph = getHeight();
            graphics.setColor(clrBackground);
            graphics.fillRect(0, 0, pw, ph);

            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setClip(0, 0, pw, ph);

            if (antiAliasing == true) {
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            }

            visualizeContent.render(g2d, pw, ph);
        }
    }

    /**
     * Sets the background colour. This is the colour that is painted
     * across the visualization panel before the Visualizable content is
     * rendered. While many other classes also allow separate background
     * colour settings, their default settings are transparent.
     *
     * @param clr the background colour.
     */
    public void setBackground(java.awt.Color clr) {
        clrBackground = clr;
    }

    /**
     * Retrieve the background colour.
     *
     * @return the background colour.
     */
    public java.awt.Color getBackground() {
        return clrBackground;
    }

    /**
     * Set the anti-aliasing state.
     *
     * @param aa enables or disables anti-aliasing.
     */
    public void setAntiAliasing(boolean aa) {
        antiAliasing = aa;
    }
}
