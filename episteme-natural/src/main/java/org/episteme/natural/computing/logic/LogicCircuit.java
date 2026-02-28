/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.computing.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a collection of logic gates and their connections.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LogicCircuit {

    public static class Component {
        public LogicGate gate;
        public String typeName;
        public double x, y;
        public List<Port> inputs = new ArrayList<>();
        public List<Port> outputs = new ArrayList<>();
        public LogicState currentState = LogicState.LOW;

        public Component(LogicGate gate, String typeName, double x, double y) {
            this.gate = gate;
            this.typeName = typeName;
            this.x = x;
            this.y = y;
        }

        public void evaluate(List<Connection> connections) {
            if (gate == null) return; // For Input/Output placeholders
            LogicState[] inputStates = new LogicState[inputs.size()];
            for (int i = 0; i < inputs.size(); i++) {
                inputStates[i] = LogicState.LOW;
                for (Connection c : connections) {
                    if (c.end == inputs.get(i)) {
                        inputStates[i] = c.start.parent.currentState;
                        break;
                    }
                }
            }
            currentState = gate.evaluate(inputStates);
        }
    }

    public static class Port {
        public Component parent;
        public boolean isInput;
        public Port(Component p, boolean in) { parent = p; isInput = in; }
    }

    public static class Connection {
        public Port start, end;
        public Connection(Port s, Port e) { start = s; end = e; }
    }

    private final List<Component> components = new ArrayList<>();
    private final List<Connection> connections = new ArrayList<>();

    public void addComponent(Component c) { components.add(c); }
    public void removeComponent(Component c) {
        connections.removeIf(conn -> conn.start.parent == c || conn.end.parent == c);
        components.remove(c);
    }
    public void addConnection(Port s, Port e) { connections.add(new Connection(s, e)); }
    public void removeConnection(Connection c) { connections.remove(c); }

    public void simulate() {
        boolean changed = true;
        int limit = 0;
        while (changed && limit < 100) {
            changed = false;
            for (Component c : components) {
                LogicState old = c.currentState;
                c.evaluate(connections);
                if (c.currentState != old) changed = true;
            }
            limit++;
        }
    }

    public List<Component> getComponents() { return components; }
    public List<Connection> getConnections() { return connections; }
    public void clear() { components.clear(); connections.clear(); }
}

