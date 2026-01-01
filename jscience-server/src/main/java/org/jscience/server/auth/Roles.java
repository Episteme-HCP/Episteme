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

package org.jscience.server.auth;

/**
 * Standard roles for JScience Grid role-based access control.
 */
public final class Roles {

    private Roles() {
    } // Prevent instantiation

    /** System administrator - full access to all operations */
    public static final String ADMIN = "ADMIN";

    /** Scientific researcher - can submit/manage computations */
    public static final String SCIENTIST = "SCIENTIST";

    /** Worker node - can request and complete tasks */
    public static final String WORKER = "WORKER";

    /** Read-only observer - can view status but not modify */
    public static final String VIEWER = "VIEWER";

    /** Anonymous/unauthenticated user - minimal access */
    public static final String ANONYMOUS = "ANONYMOUS";

    /**
     * Check if the given role has admin privileges.
     */
    public static boolean isAdmin(String role) {
        return ADMIN.equals(role);
    }

    /**
     * Check if the given role can submit tasks.
     */
    public static boolean canSubmitTasks(String role) {
        return ADMIN.equals(role) || SCIENTIST.equals(role);
    }

    /**
     * Check if the given role can manage workers.
     */
    public static boolean canManageWorkers(String role) {
        return ADMIN.equals(role);
    }

    /**
     * Check if the given role can view grid status.
     */
    public static boolean canViewStatus(String role) {
        return role != null && !ANONYMOUS.equals(role);
    }
}


