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
 * A class representing the justice useful constants.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class LawConstants extends Object {
    /** DOCUMENT ME! */
    public final static int UNKNOWN = 0;

    /** DOCUMENT ME! */
    public final static int CIVIL = 1; //1<<0;

    /** DOCUMENT ME! */
    public final static int MILITARY = 2; //1<<1;

    /** DOCUMENT ME! */
    public final static int BUSINESS = 4; //1<<2;

    /** DOCUMENT ME! */
    public final static int LABOR = 8; //1<<3;

    /** DOCUMENT ME! */
    public final static int FAMILY = 16;

    /** DOCUMENT ME! */
    public final static int PENAL = 32;

    /** DOCUMENT ME! */
    public final static int GOVERNMENT = 64;

    /** DOCUMENT ME! */
    public final static int VEHICLES = 128;

    /** DOCUMENT ME! */
    public final static int ELECTION = 256;

    /** DOCUMENT ME! */
    public final static int HEALTH = 512;

    /** DOCUMENT ME! */
    public final static int INSTITUTIONS = 1024;

    /** DOCUMENT ME! */
    public final static int EDUCATION = 2048;

    /** DOCUMENT ME! */
    public final static int FINANCIAL = 4096;

    /** DOCUMENT ME! */
    public final static int COMMERCIAL = 8192;

    /** DOCUMENT ME! */
    public final static int INSURANCE = 16384;

    /** DOCUMENT ME! */
    public final static int TAXATION = 32768;
    
    //also could be interesting: http://en.wikipedia.org/wiki/Legal_systems_of_the_world
    //civil law
    //state law
    //common law
    //religious law
}
