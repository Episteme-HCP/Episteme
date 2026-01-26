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

package org.jscience.politics;


import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jscience.biology.Individual;
import org.jscience.earth.Place;
import org.jscience.psychology.social.Group;
import org.jscience.util.persistence.Persistent;

/**
 * Represents a localized, often primitive or community-level settlement (Chiefdom, Colony, Tribe site).
 * Settlement is used to model smaller human or social groups (including primate or social insect colonies) 
 * that lack complex modern state infrastructure.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public class Settlement extends Place {

    private static final long serialVersionUID = 1L;

    private final Group group;
    private final Set<Individual> leaders = new HashSet<>();

    /**
     * Initializes a new Settlement.
     *
     * @param name  the name of the settlement
     * @param group the primary social group occupying the settlement
     * @throws NullPointerException if any argument is null
     */
    public Settlement(String name, Group group) {
        super(Objects.requireNonNull(name, "Name cannot be null"), Place.Type.VILLAGE);
        this.group = Objects.requireNonNull(group, "Group cannot be null");
    }

    /**
     * Returns the social group associated with this settlement.
     * @return the group
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Returns an unmodifiable set of the settlement's leaders (e.g., chiefs, experts).
     * @return leaders set
     */
    public Set<Individual> getLeaders() {
        return Collections.unmodifiableSet(leaders);
    }

    /**
     * Adds an individual to the leadership of the settlement.
     * @param leader the individual to add
     */
    public void addLeader(Individual leader) {
        if (leader != null) {
            leaders.add(leader);
        }
    }

    /**
     * Removes an individual from leadership.
     * @param leader the individual to remove
     */
    public void removeLeader(Individual leader) {
        leaders.remove(leader);
    }

    @Override
    public String toString() {
        return getName() + " [" + group.getName() + " settlement]";
    }
}
