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

package org.jscience.sports;

import java.util.Objects;
import org.jscience.util.identity.AbstractIdentifiedEntity;
import org.jscience.util.identity.SimpleIdentification;

/**
 * Represents a specific sport or athletic discipline.
 * Extends AbstractIdentifiedEntity to support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Sport extends AbstractIdentifiedEntity {

    private static final long serialVersionUID = 1L;

    private final boolean teamSport;
    private final Category category;

    /**
     * Initializes a new Sport.
     * 
     * @param name      the official name of the sport
     * @param teamSport whether it is primarily played in teams
     * @param category  the broad classification (e.g., Combat, Ball)
     * @throws NullPointerException if name is null
     */
    public Sport(String name, boolean teamSport, Category category) {
        super(new SimpleIdentification(name));
        setName(name);
        this.teamSport = teamSport;
        this.category = category;
    }

    public boolean isTeamSport() {
        return teamSport;
    }
    
    public Category getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return getName();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sport sport)) return false;
        return teamSport == sport.teamSport && Objects.equals(getName(), sport.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), teamSport);
    }
}
