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

package org.jscience.architecture.traffic.infrastructure;

import java.awt.*;


/**
 * Cycling through our world on two wheels. Aint it great to be alive?
 *
 * @author Group Datastructures
 * @version 1.0
 */
public class Bicycle extends Roaduser {
    /** DOCUMENT ME! */
    protected final int type = RoaduserFactory.getTypeByDesc("Bicycle");

    /** DOCUMENT ME! */
    protected final int length = 1;

    /** DOCUMENT ME! */
    protected final int speed = 1;

    /** DOCUMENT ME! */
    protected final int passengers = 1;

/**
     * Creates a new Bicycle object.
     *
     * @param new_startNode DOCUMENT ME!
     * @param new_destNode  DOCUMENT ME!
     * @param pos           DOCUMENT ME!
     */
    public Bicycle(Node new_startNode, Node new_destNode, int pos) {
        super(new_startNode, new_destNode, pos);

        // make color little bit more random
        color = RoaduserFactory.getColorByType(type);

        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        if (r == 0) {
            r = (int) (Math.random() * 160);
        }

        if (g == 0) {
            g = (int) (Math.random() * 160);
        }

        if (b == 0) {
            b = (int) (Math.random() * 160);
        }

        color = new Color(r, g, b);
    }

/**
     * Empty constructor for loading
     */
    public Bicycle() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "Bicycle";
    }

    /**
     * Returns the speed of this Roaduser in blocks per cycle
     *
     * @return DOCUMENT ME!
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLength() {
        return length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumPassengers() {
        return passengers;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param zf DOCUMENT ME!
     */
    public void paint(Graphics g, int x, int y, float zf) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param zf DOCUMENT ME!
     * @param dlangle DOCUMENT ME!
     */
    public void paint(Graphics g, int x, int y, float zf, double dlangle) {
        g.setColor(color);

        double angle = dlangle - Math.toRadians(45.0);
        int[] cx = new int[4];
        cx[0] = (int) (Math.round((double) x + Math.sin(angle)));
        cx[1] = (int) (Math.round((double) x +
                Math.sin(angle + Math.toRadians(90.0))));
        cx[2] = (int) (Math.round((double) x +
                (Math.sin(angle + Math.toRadians(180.0) + Math.atan(0.5)) * 2)));
        cx[3] = (int) (Math.round((double) x +
                (Math.sin((angle + Math.toRadians(270.0)) - Math.atan(0.5)) * 2)));

        int[] cy = new int[4];
        cy[0] = (int) (Math.round((double) y + Math.cos(angle)));
        cy[1] = (int) (Math.round((double) y +
                Math.cos(angle + Math.toRadians(90.0))));
        cy[2] = (int) (Math.round((double) y +
                (Math.cos(angle + Math.toRadians(180.0) + Math.atan(0.5)) * 2)));
        cy[3] = (int) (Math.round((double) y +
                (Math.cos((angle + Math.toRadians(270.0)) - Math.atan(0.5)) * 2)));

        g.fillPolygon(cx, cy, 4);
    }

    // Specific XMLSerializable implementation 
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXMLName() {
        return parentName + ".roaduser-bicycle";
    }
}
