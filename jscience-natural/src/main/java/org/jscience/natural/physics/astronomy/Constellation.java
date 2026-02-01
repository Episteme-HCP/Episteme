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

package org.jscience.natural.physics.astronomy;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a constellation, consisting of a name and a set of star pairs (lines).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Constellation {

    private final String name;
    private final Color color;
    private final List<StarConnection> connections;

    public Constellation(String name, Color color) {
        this.name = name;
        this.color = color;
        this.connections = new ArrayList<>();
    }

    public void addConnection(Star start, Star end) {
        connections.add(new StarConnection(start, end));
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public List<StarConnection> getConnections() {
        return Collections.unmodifiableList(connections);
    }

    @Override
    public String toString() {
        return "Constellation: " + name + " (" + connections.size() + " connections)";
    }

    public static class StarConnection {
        private final Star start;
        private final Star end;

        public StarConnection(Star start, Star end) {
            this.start = start;
            this.end = end;
        }

        public Star getStart() {
            return start;
        }

        public Star getEnd() {
            return end;
        }
    }
}
