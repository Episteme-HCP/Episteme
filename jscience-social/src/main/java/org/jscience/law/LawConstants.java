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
package org.jscience.law;

/**
 * Defines constants representing different domains of law and legal systems.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 */
public final class LawConstants {

    private LawConstants() {
        // Utility class
    }

    /** Unknown or undefined legal domain. */
    public final static int UNKNOWN = 0;

    /** Civil law domain. */
    public final static int CIVIL = 1;

    /** Military law domain. */
    public final static int MILITARY = 2;

    /** Business and corporate law domain. */
    public final static int BUSINESS = 4;

    /** Labor and employment law domain. */
    public final static int LABOR = 8;

    /** Family and matrimonial law domain. */
    public final static int FAMILY = 16;

    /** Penal or criminal law domain. */
    public final static int PENAL = 32;

    /** Government and administrative law domain. */
    public final static int GOVERNMENT = 64;

    /** Vehicle and traffic law domain. */
    public final static int VEHICLES = 128;

    /** Election and voting law domain. */
    public final static int ELECTION = 256;

    /** Health and medical law domain. */
    public final static int HEALTH = 512;

    /** Institutional law domain. */
    public final static int INSTITUTIONS = 1024;

    /** Education and school law domain. */
    public final static int EDUCATION = 2048;

    /** Financial and banking law domain. */
    public final static int FINANCIAL = 4096;

    /** Commercial and trade law domain. */
    public final static int COMMERCIAL = 8192;

    /** Insurance law domain. */
    public final static int INSURANCE = 16384;

    /** Taxation and fiscal law domain. */
    public final static int TAXATION = 32768;
}
