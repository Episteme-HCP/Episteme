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

package org.netbeans.lib.awtextra;

import java.awt.Dimension;
import java.awt.Point;


/**
 * An object that encapsulates position and (optionally) size for Absolute
 * positioning of components.
 *
 * @version 1.01, Aug 19, 1998
 * @see AbsoluteLayout
 */
public class AbsoluteConstraints implements java.io.Serializable {
    /**
     * generated Serialized Version UID
     */
    static final long serialVersionUID = 5261460716622152494L;

    /**
     * The X position of the component
     */
    public int x;

    /**
     * The Y position of the component
     */
    public int y;

    /**
     * The width of the component or -1 if the component's preferred width
     * should be used
     */
    public int width = -1;

    /**
     * The height of the component or -1 if the component's preferred height
     * should be used
     */
    public int height = -1;

    /**
     * Creates a new AbsoluteConstraints for specified position.
     *
     * @param pos The position to be represented by this AbsoluteConstraints
     */
    public AbsoluteConstraints(Point pos) {
        this(pos.x, pos.y);
    }

    /**
     * Creates a new AbsoluteConstraints for specified position.
     *
     * @param x The X position to be represented by this AbsoluteConstraints
     * @param y The Y position to be represented by this AbsoluteConstraints
     */
    public AbsoluteConstraints(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a new AbsoluteConstraints for specified position and size.
     *
     * @param pos  The position to be represented by this AbsoluteConstraints
     * @param size The size to be represented by this AbsoluteConstraints or
     *             null if the component's preferred size should be used
     */
    public AbsoluteConstraints(Point pos, Dimension size) {
        this.x = pos.x;
        this.y = pos.y;

        if (size != null) {
            this.width = size.width;
            this.height = size.height;
        }
    }

    /**
     * Creates a new AbsoluteConstraints for specified position and size.
     *
     * @param x      The X position to be represented by this AbsoluteConstraints
     * @param y      The Y position to be represented by this AbsoluteConstraints
     * @param width  The width to be represented by this AbsoluteConstraints or
     *               -1 if the  component's preferred width should be used
     * @param height The height to be represented by this AbsoluteConstraints
     *               or -1 if the component's preferred height should be used
     */
    public AbsoluteConstraints(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * DOCUMENT ME!
     *
     * @return The X position represented by this AbsoluteConstraints
     */
    public int getX() {
        return x;
    }

    /**
     * DOCUMENT ME!
     *
     * @return The Y position represented by this AbsoluteConstraints
     */
    public int getY() {
        return y;
    }

    /**
     * DOCUMENT ME!
     *
     * @return The width represented by this AbsoluteConstraints or -1 if the
     *         component's preferred width should be used
     */
    public int getWidth() {
        return width;
    }

    /**
     * DOCUMENT ME!
     *
     * @return The height represented by this AbsoluteConstraints or -1 if the
     *         component's preferred height should be used
     */
    public int getHeight() {
        return height;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return super.toString() + " [x=" + x + ", y=" + y + ", width=" + width +
                ", height=" + height + "]";
    }
}
