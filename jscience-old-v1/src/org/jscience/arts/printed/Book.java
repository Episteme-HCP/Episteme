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

import java.util.Date;
import java.util.Set;


/**
 * This is meant to be the basic text rather than a complete edition (with
 * pictures, and a specific way to present the text).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//we could use the class from org.jscience.linguistics.Text to store the text (although this would be quite inefficient)
//you should use the Identification to store the ISBN or ISSN
public class Book extends Artwork {
    /** DOCUMENT ME! */
    private String text;

/**
     * Creates a new Book object.
     *
     * @param name           DOCUMENT ME!
     * @param description    DOCUMENT ME!
     * @param producer       DOCUMENT ME!
     * @param productionDate DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param authors        DOCUMENT ME!
     */
    public Book(String name, String description, Community producer,
        Date productionDate, Identification identification, Set authors) {
        super(name, description, Amount.ZERO, producer, producer.getPosition(),
            productionDate, identification, Amount.valueOf(0, Currency.USD),
            authors, ArtsConstants.LITERATURE);
        text = new String();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getText() {
        return text;
    }

    /**
     * DOCUMENT ME!
     *
     * @param html DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setText(String html) {
        if (html != null) {
            text = html;
        } else {
            throw new IllegalArgumentException("You can't set a null text.");
        }
    }
}
