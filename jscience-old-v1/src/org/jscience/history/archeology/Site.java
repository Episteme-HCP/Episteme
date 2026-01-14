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

package org.jscience.history.archeology;

import org.jscience.geography.Boundary;
import org.jscience.geography.Place;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a place where archeologists have looked for
 * something, and usually found items and drawn a map.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//usually many peoples actually dig at a site and there also can be many different teams
//the Site may also never be really closed or on the opposite destroyed after mapping
//although we not not account for such information, you may need to consider these
public class Site extends Place {
    /** DOCUMENT ME! */
    private Date startDate; //when archeologist first dig

    /** DOCUMENT ME! */
    private String description; //main characteristics

    /** DOCUMENT ME! */
    private Set contents; //a set of items

/**
     * Creates a new Site object.
     *
     * @param name      DOCUMENT ME!
     * @param startDate DOCUMENT ME!
     * @param limits    DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Site(String name, Date startDate, Boundary limits) {
        super(name, limits);

        if (startDate != null) {
            this.startDate = startDate;
            this.description = new String();
        } else {
            throw new IllegalArgumentException(
                "The Site constructor can't have null arguments (and name can't be empty).");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDescription() {
        return description;
    }

    /**
     * DOCUMENT ME!
     *
     * @param description DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setDescription(String description) {
        if (description != null) {
            this.description = description;
        } else {
            throw new IllegalArgumentException("The description can't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getItems() {
        return contents;
    }

    /**
     * DOCUMENT ME!
     *
     * @param item DOCUMENT ME!
     */
    public void addItem(Item item) {
        contents.add(item);
    }

    /**
     * DOCUMENT ME!
     *
     * @param item DOCUMENT ME!
     */
    public void removeItem(Item item) {
        contents.remove(item);
    }

    //a Set of Items
    /**
     * DOCUMENT ME!
     *
     * @param contents DOCUMENT ME!
     */
    public void setItems(Set contents) {
        Iterator iterator;
        boolean valid;

        if (contents != null) {
            iterator = contents.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Item;
            }

            if (valid) {
                this.contents = contents;
            } else {
                throw new IllegalArgumentException(
                    "The Set of contents should contain only Items.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of contents can't be null.");
        }
    }
}
