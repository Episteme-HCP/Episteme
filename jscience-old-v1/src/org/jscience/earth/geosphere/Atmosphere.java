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

package org.jscience.earth.geosphere.sky;

/**
 */
import javax.vecmath.Color3f;


//Describes the atmosphere (the blue halo around earth)
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class Atmosphere extends Object {
    /** DOCUMENT ME! */
    private float height; //relative to the soil level in meters

    /** DOCUMENT ME! */
    private Color3f color;

    /** DOCUMENT ME! */
    private String composition; //a string of the main gaz with percentage in parenthesis separated by commas.

/**
     * Creates a new Atmosphere object.
     */
    public Atmosphere() {
        height = 0;
        color = new Color3f();
        composition = new String();
    }

/**
     * Creates a new Atmosphere object.
     *
     * @param height      DOCUMENT ME!
     * @param composition DOCUMENT ME!
     */
    public Atmosphere(float height, String composition) {
        this.height = height;
        this.composition = composition;
    }

    /**
     * DOCUMENT ME!
     *
     * @param height DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getHeight(float height) {
        return height;
    }

    /**
     * DOCUMENT ME!
     *
     * @param height DOCUMENT ME!
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color3f getColor() {
        return color;
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     */
    public void setColor(Color3f color) {
        this.color = color;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getComposition() {
        return composition;
    }

    /**
     * DOCUMENT ME!
     *
     * @param composition DOCUMENT ME!
     */
    public void setComposition(String composition) {
        this.composition = composition;
    }
}
