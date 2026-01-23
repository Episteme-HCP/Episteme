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

import java.io.Serializable;
import java.util.*;
import org.jscience.geography.Boundary;
import org.jscience.geography.Place;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents an archaeological excavation site where artifacts and structures are studied.
 * Tracks the excavation start date, site description, and all discovered items.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 * @since 1.0
 */
@Persistent
public class Site extends Place implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The date when excavation activities first began. */
    @Attribute
    private final Date startDate;

    /** General characteristics and historical context of the site. */
    @Attribute
    private String description = "";

    /** The set of artifacts and historical items discovered at this site. */
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Set<Item> items = new HashSet<>();

    /**
     * Creates a new Site.
     *
     * @param name      the identifying name of the site
     * @param startDate the start date of excavation
     * @param limits    geographical boundaries of the site
     * @throws NullPointerException if any argument is null
     */
    public Site(String name, Date startDate, Boundary limits) {
        super(name, limits);
        this.startDate = Objects.requireNonNull(startDate, "Start date cannot be null");
    }

    /**
     * Returns the excavation start date.
     *
     * @return the start date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Returns the site description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the site description.
     *
     * @param description characterization string
     * @throws NullPointerException if description is null
     */
    public void setDescription(String description) {
        this.description = Objects.requireNonNull(description, "Description cannot be null");
    }

    /**
     * Returns an unmodifiable view of the discovered items.
     *
     * @return the set of items
     */
    public Set<Item> getItems() {
        return Collections.unmodifiableSet(items);
    }

    /**
     * Adds an item found at this site.
     *
     * @param item the discovered artifact
     * @throws NullPointerException if item is null
     */
    public void addItem(Item item) {
        Objects.requireNonNull(item, "Item cannot be null");
        items.add(item);
    }

    /**
     * Removes an item from the site records.
     *
     * @param item the item to remove
     */
    public void removeItem(Item item) {
        items.remove(item);
    }

    /**
     * Replaces the entire set of items.
     * 
     * @param items the new set of items
     * @throws NullPointerException if items is null
     */
    public void setItems(Set<Item> items) {
        this.items = new HashSet<>(Objects.requireNonNull(items, "Items set cannot be null"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Site site)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(startDate, site.startDate) && 
               Objects.equals(description, site.description) && 
               Objects.equals(items, site.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), startDate, description, items);
    }

    @Override
    public String toString() {
        return String.format("Archaeological Site: %s (Started %s, %d items)", 
            getName(), startDate, items.size());
    }
}
