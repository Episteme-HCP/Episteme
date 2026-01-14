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

package org.jscience.sociology;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Situation {

    private final String name;
    private final String description;
    private final Set<Role> roles;

    public Situation(String name, String description) {
        this.name = name;
        this.description = description;
        this.roles = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public void addRole(Role role) {
        if (role != null) {
            role.setSituation(this);
            roles.add(role);
        }
    }

    public void removeRole(Role role) {
        if (roles.contains(role)) {
            roles.remove(role);
            role.setSituation(null); // Or keep it loose?
        }
    }
}


