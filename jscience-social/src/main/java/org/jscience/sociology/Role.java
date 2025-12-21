/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.sociology;

/**
 * Represents a role a Person plays in a specific Situation. * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 
 */
public class Role {

    public final static int CLIENT = 1;
    public final static int SERVER = 2;
    public final static int SUPERVISOR = 4;
    public final static int OBSERVER = 8;

    private final Person person;
    private final String name;
    private Situation situation;
    private final int kind;

    public Role(Person person, String name, Situation situation, int kind) {
        this.person = person;
        this.name = name;
        this.situation = situation;
        this.kind = kind;

        // Linkage
        if (person != null) {
            person.addRole(this);
        }
        if (situation != null) {
            situation.addRole(this);
        }
    }

    public Person getPerson() {
        return person;
    }

    public String getName() {
        return name;
    }

    public Situation getSituation() {
        return situation;
    }

    public void setSituation(Situation situation) {
        this.situation = situation;
    }

    public int getKind() {
        return kind;
    }

    @Override
    public String toString() {
        return name + " (" + kindToString(kind) + ")";
    }

    private String kindToString(int k) {
        switch (k) {
            case CLIENT:
                return "Client";
            case SERVER:
                return "Server";
            case SUPERVISOR:
                return "Supervisor";
            case OBSERVER:
                return "Observer";
            default:
                return "Unknown";
        }
    }
}
