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

package org.jscience.arts.printed;

import org.jscience.economics.Community;
import org.jscience.economics.money.Currency;

import org.jscience.measure.Amount;
import org.jscience.measure.Identification;

import org.jscience.arts.ArtsConstants;
import org.jscience.arts.Artwork;

import java.awt.*;

import java.util.Date;
import java.util.Set;


/**
 * This is  the basic visual element class.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//could be a painting a photo, a drawing
public class Picture extends Artwork {
    /** DOCUMENT ME! */
    public final static int UNKNOWN = 0;

    /** DOCUMENT ME! */
    public final static int OIL = 1;

    /** DOCUMENT ME! */
    public final static int WATERPAINT = 2;

    /** DOCUMENT ME! */
    public final static int CHARCOAL = 3;

    /** DOCUMENT ME! */
    public final static int PENCIL = 3;

    /** DOCUMENT ME! */
    public final static int CANVAS = 1;

    /** DOCUMENT ME! */
    public final static int WOOD = 2;

    /** DOCUMENT ME! */
    public final static int FRESCO = 3;

    /** DOCUMENT ME! */
    public final static int PAPER = 4;

    /** DOCUMENT ME! */
    public final static int OTHER = 100;

    /** DOCUMENT ME! */
    private float width;

    /** DOCUMENT ME! */
    private float height;

    /** DOCUMENT ME! */
    private int pencil;

    /** DOCUMENT ME! */
    private int material;

    /** DOCUMENT ME! */
    private Image image;

/**
     * Creates a new Picture object.
     *
     * @param name           DOCUMENT ME!
     * @param description    DOCUMENT ME!
     * @param producer       DOCUMENT ME!
     * @param productionDate DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param authors        DOCUMENT ME!
     */
    public Picture(String name, String description, Community producer,
        Date productionDate, Identification identification, Set authors) {
        super(name, description, Amount.ZERO, producer, producer.getPosition(),
            productionDate, identification, Amount.valueOf(0, Currency.USD),
            authors, ArtsConstants.PAINTING);
        this.width = -1;
        this.height = -1;
        this.pencil = UNKNOWN;
        this.material = UNKNOWN;
        this.image = null;
    }

    //may return negative value
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getWidth() {
        return width;
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     */
    public void setWidth(float width) {
        this.width = width;
    }

    //may return negative value
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getHeight() {
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
    public int getPencil() {
        return pencil;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pencil DOCUMENT ME!
     */
    public void setPencil(int pencil) {
        this.pencil = pencil;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMaterial() {
        return material;
    }

    /**
     * DOCUMENT ME!
     *
     * @param material DOCUMENT ME!
     */
    public void setMaterial(int material) {
        this.material = material;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Image getImage() {
        return image;
    }

    /**
     * DOCUMENT ME!
     *
     * @param image DOCUMENT ME!
     */
    public void setImage(Image image) {
        this.image = image;
    }
}
