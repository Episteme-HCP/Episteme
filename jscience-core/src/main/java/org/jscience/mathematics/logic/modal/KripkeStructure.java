/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.logic.modal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a Kripke structure for modal logic semantics.
 * <p>
 * A Kripke structure consists of a set of worlds and an accessibility relation
 * between them.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class KripkeStructure<T> {

    private final Set<String> worlds = new HashSet<>();
    private final Map<String, Set<String>> accessibility = new HashMap<>();
    private final Map<String, Set<T>> valuations = new HashMap<>();

    /**
     * Adds a possible world to the structure.
     * 
     * @param worldName the name of the world
     */
    public void addWorld(String worldName) {
        worlds.add(worldName);
        accessibility.putIfAbsent(worldName, new HashSet<>());
        valuations.putIfAbsent(worldName, new HashSet<>());
    }

    /**
     * Adds an accessibility relation from one world to another.
     * 
     * @param fromWorld source world
     * @param toWorld   destination world
     */
    public void addRelation(String fromWorld, String toWorld) {
        if (!worlds.contains(fromWorld) || !worlds.contains(toWorld)) {
            throw new IllegalArgumentException("Worlds must exist before adding relation");
        }
        accessibility.get(fromWorld).add(toWorld);
    }

    /**
     * Sets a proposition as true in a specific world.
     * 
     * @param worldName   the world
     * @param proposition the proposition
     */
    public void setTrue(String worldName, T proposition) {
        if (!worlds.contains(worldName)) {
            throw new IllegalArgumentException("World must exist");
        }
        valuations.get(worldName).add(proposition);
    }

    /**
     * Checks if a proposition is true in a specific world.
     * 
     * @param worldName   the world
     * @param proposition the proposition
     * @return true if the proposition holds
     */
    public boolean isTrue(String worldName, T proposition) {
        return valuations.getOrDefault(worldName, new HashSet<>()).contains(proposition);
    }

    /**
     * Returns the set of worlds accessible from the given world.
     * 
     * @param worldName the source world
     * @return set of accessible world names
     */
    public Set<String> getAccessibleWorlds(String worldName) {
        return accessibility.getOrDefault(worldName, new HashSet<>());
    }
}

