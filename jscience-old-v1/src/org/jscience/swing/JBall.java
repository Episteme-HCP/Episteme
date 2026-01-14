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

package org.jscience.swing;

import java.awt.*;

import java.io.Serializable;

import javax.swing.*;


/**
 * This class models a ball that has a specified ball color, text color,
 * size, value, and state. These parameters can be changed. The state of the
 * ball is drawn or not drawn. If drawn, the ball is shown with its value and
 * color. If not drawn, the ball is shown without its value and in the
 * background color. The Ball object can be used as a metaphor for an element
 * in a population, in a variety of simulations that involve sampling. In
 * particular this object is useful for simulations that involve drawing balls
 * from an urn..
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public class JBall extends JComponent implements Serializable {
    /** DOCUMENT ME! */
    private int value;

    /** DOCUMENT ME! */
    private boolean drawn;

    /** DOCUMENT ME! */
    private Color ballColor = Color.red;

    /** DOCUMENT ME! */
    private Color textColor = Color.white;

/**
     * This general constructor creates a new ball with a specified value,
     * size, ball color, and text color.
     *
     * @param x  the value of the ball
     * @param s  the size of the ball
     * @param bc the ball color.
     * @param tc the text color
     */
    public JBall(int x, int s, Color bc, Color tc) {
        setValue(x);
        setPreferredSize(new Dimension(s, s));
        setColors(bc, tc);
        setToolTipText("Ball");
    }

/**
     * This special constructor creates a new ball with a specified value amd
     * size, and with default ball color red and text color white
     *
     * @param x the value of the ball.
     * @param s the size of the ball
     */
    public JBall(int x, int s) {
        this(x, s, Color.red, Color.white);
    }

/**
     * This special constructor creates a new ball with a specified value and
     * with the default size 24 and default ball color red.
     *
     * @param x the value of the ball.
     */
    public JBall(int x) {
        this(x, 32);
    }

/**
     * This default constructor creates a new ball with value 0, size 24, and
     * ball color red.
     */
    public JBall() {
        this(0);
    }

    /**
     * This method paints the ball.
     *
     * @param g the graphics context.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int size = (int) Math.min(getSize().height, getSize().width);

        if (drawn) {
            g.setColor(ballColor);
        } else {
            g.setColor(getBackground());
        }

        g.fillOval(2, 2, size - 4, size - 4);
        g.setColor(Color.black);
        g.drawOval(2, 2, size - 4, size - 4);

        if (drawn) {
            Font font = new Font("Arial", Font.BOLD, (size - 4) / 2);
            g.setFont(font);
            g.setColor(textColor);

            String label = String.valueOf(value);
            int x = g.getFontMetrics(font).stringWidth(label);
            int y = g.getFontMetrics(font).getAscent();
            g.drawString(label, (size / 2) - (x / 2), (size / 2) + (y / 2));
        }
    }

    /**
     * This method sets the ball to a specified value.
     *
     * @param x the value of the ball.
     */
    public void setValue(int x) {
        value = x;
    }

    /**
     * This method gets the value of the ball.
     *
     * @return the value of the ball.
     */
    public int getValue() {
        return value;
    }

    /**
     * This method sets the colors of the ball and text.
     *
     * @param bc the ball color
     * @param tc the text color
     */
    public void setColors(Color bc, Color tc) {
        ballColor = bc;
        textColor = tc;
    }

    /**
     * This method sets the color of the ball.
     *
     * @param c the color of the ball.
     */
    public void setBallColor(Color c) {
        setColors(c, textColor);
    }

    /**
     * This method gets the ball color.
     *
     * @return The color of the ball.
     */
    public Color getBallColor() {
        return ballColor;
    }

    /**
     * This method sets the color of the text.
     *
     * @param c the color of the ball.
     */
    public void setTextColor(Color c) {
        setColors(ballColor, c);
    }

    /**
     * This method gets the text color.
     *
     * @return the color of the ball.
     */
    public Color getTextColor() {
        return textColor;
    }

    /**
     * This method sets the state of the ball. If drawn, the ball is
     * shown with its ballColor and value. If not drawn, the ball is shown
     * without its value and in the background ballColor.
     *
     * @param b true if drawn
     */
    public void setDrawn(boolean b) {
        drawn = b;
        repaint();
    }

    /**
     * This method returns the state of the ball. If drawn, the ball is
     * shown with its ballColor and value. If not drawn, the ball is shown
     * without its value and in the background ballColor.
     *
     * @return true if drawn
     */
    public boolean isDrawn() {
        return drawn;
    }
}
