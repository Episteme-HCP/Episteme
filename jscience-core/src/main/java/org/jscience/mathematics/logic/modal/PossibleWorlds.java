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

package org.jscience.mathematics.logic.modal;

import java.util.*;

/**
 * Represents possible worlds semantics for modal logic.
 * <p>
 * In possible worlds semantics, modal operators are interpreted relative to
 * accessibility relations between worlds. A proposition is necessarily true if
 * it's true in all accessible worlds, and possibly true if it's true in at
 * least one accessible world.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PossibleWorlds<W, P> {

    private final Set<W> worlds;
    private final Map<W, Set<W>> accessibilityRelation;
    private final Map<W, Map<P, Boolean>> valuation;

    /**
     * Creates an empty possible worlds model.
     */
    public PossibleWorlds() {
        this.worlds = new HashSet<>();
        this.accessibilityRelation = new HashMap<>();
        this.valuation = new HashMap<>();
    }

    /**
     * Adds a world to the model.
     * 
     * @param world the world to add
     */
    public void addWorld(W world) {
        worlds.add(world);
        accessibilityRelation.putIfAbsent(world, new HashSet<>());
        valuation.putIfAbsent(world, new HashMap<>());
    }

    /**
     * Adds an accessibility relation between two worlds.
     * 
     * @param from the source world
     * @param to   the target world
     */
    public void addAccessibility(W from, W to) {
        if (!worlds.contains(from) || !worlds.contains(to)) {
            throw new IllegalArgumentException("Both worlds must exist in the model");
        }
        accessibilityRelation.get(from).add(to);
    }

    /**
     * Sets the truth value of a proposition in a world.
     * 
     * @param world       the world
     * @param proposition the proposition
     * @param value       the truth value
     */
    public void setValue(W world, P proposition, boolean value) {
        if (!worlds.contains(world)) {
            throw new IllegalArgumentException("World must exist in the model");
        }
        valuation.get(world).put(proposition, value);
    }

    /**
     * Gets the truth value of a proposition in a world.
     * 
     * @param world       the world
     * @param proposition the proposition
     * @return the truth value, or false if not set
     */
    public boolean getValue(W world, P proposition) {
        if (!worlds.contains(world)) {
            return false;
        }
        return valuation.get(world).getOrDefault(proposition, false);
    }

    /**
     * Checks if a proposition is necessarily true in a world (true in all
     * accessible worlds).
     * 
     * @param world       the world
     * @param proposition the proposition
     * @return true if necessarily true
     */
    public boolean isNecessary(W world, P proposition) {
        if (!worlds.contains(world)) {
            return false;
        }

        Set<W> accessible = accessibilityRelation.get(world);
        if (accessible.isEmpty()) {
            return true; // Vacuously true if no accessible worlds
        }

        for (W accessibleWorld : accessible) {
            if (!getValue(accessibleWorld, proposition)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a proposition is possibly true in a world (true in at least one
     * accessible world).
     * 
     * @param world       the world
     * @param proposition the proposition
     * @return true if possibly true
     */
    public boolean isPossible(W world, P proposition) {
        if (!worlds.contains(world)) {
            return false;
        }

        Set<W> accessible = accessibilityRelation.get(world);
        for (W accessibleWorld : accessible) {
            if (getValue(accessibleWorld, proposition)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns all worlds accessible from the given world.
     * 
     * @param world the world
     * @return the set of accessible worlds
     */
    public Set<W> getAccessibleWorlds(W world) {
        return new HashSet<>(accessibilityRelation.getOrDefault(world, Collections.emptySet()));
    }

    /**
     * Returns all worlds in the model.
     * 
     * @return the set of all worlds
     */
    public Set<W> getAllWorlds() {
        return new HashSet<>(worlds);
    }

    /**
     * Checks if the accessibility relation is reflexive (every world accesses
     * itself).
     * 
     * @return true if reflexive
     */
    public boolean isReflexive() {
        for (W world : worlds) {
            if (!accessibilityRelation.get(world).contains(world)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the accessibility relation is symmetric (if w1 accesses w2, then
     * w2 accesses w1).
     * 
     * @return true if symmetric
     */
    public boolean isSymmetric() {
        for (W w1 : worlds) {
            for (W w2 : accessibilityRelation.get(w1)) {
                if (!accessibilityRelation.get(w2).contains(w1)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the accessibility relation is transitive (if w1 accesses w2 and w2
     * accesses w3, then w1 accesses w3).
     * 
     * @return true if transitive
     */
    public boolean isTransitive() {
        for (W w1 : worlds) {
            for (W w2 : accessibilityRelation.get(w1)) {
                for (W w3 : accessibilityRelation.get(w2)) {
                    if (!accessibilityRelation.get(w1).contains(w3)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "PossibleWorlds(worlds=" + worlds.size() + ", relations=" + countRelations() + ")";
    }

    private int countRelations() {
        int count = 0;
        for (Set<W> accessible : accessibilityRelation.values()) {
            count += accessible.size();
        }
        return count;
    }
}

