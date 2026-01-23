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
package org.jscience.economics;

/**
 * A class representing some useful constants for economics.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 */
public final class EconomicsConstants {

    /** 
     * Default constructor (private).
     */
    private EconomicsConstants() {
        // Prevents instantiation.
    }

    //could put the standard currencies here although they are in the money subpackage
    /** Constant for unknown or undefined value. */
    public final static int UNKNOWN = 0;

    /** Primary industry focused on raw material extraction. */
    public final static int PRIMARY_INDUSTRY = 1;

    /** Secondary industry focused on manufacturing and processing. */
    public final static int SECONDARY_INDUSTRY = 2;

    /** Tertiary industry focused on services. */
    public final static int TERTIARY_INDUSTRY = 4;

    /** Quaternary industry focused on information and technology. */
    public final static int QUATERNARY_INDUSTRY = 8;

    /** Process involving raw extraction and primary production. */
    public final static int PRODUCTION_PROCESS = 1;

    /** Process involving factory production and transformation. */
    public final static int TRANSFORMATION_PROCESS = 2;

    /** Process involving logistics and distribution. */
    public final static int DISTRIBUTION_PROCESS = 4;

    /** Process involving consumption by the end user. */
    public final static int CONSUMPTION_PROCESS = 8;

    /** Industry sector for transport. */
    public final static int TRANSPORT = 1;

    /** Industry sector for hygiene. */
    public final static int HYGIENE = 2;

    /** Industry sector for food and alimentation. */
    public final static int ALIMENTATION = 4;

    /** Industry sector for housing and habitat. */
    public final static int HABITAT = 8;

    /** Industry sector for technology. */
    public final static int TECHNOLOGY = 16;

    /** Industry sector for energy. */
    public final static int ENERGY = 32;

    /** Industry sector for societal services. */
    public final static int SOCIETY = 64;

    /** Department for human resource management. */
    public final static int HUMAN_RESOURCES = 1;

    /** Department for sales activities. */
    public final static int SALES = 2;

    /** Department for marketing and communication. */
    public final static int MARKETING = 4;

    /** Department for accounting and financial tracking. */
    public final static int ACCOUNTING = 8;

    /** Department for research and development. */
    public final static int RESEARCH = 16;

    /** Department for production operations. */
    public final static int PRODUCTION = 32;

    /** Department for distribution and shipping. */
    public final static int DISTRIBUTION = 64;

    /** Department for procurement and storage. */
    public final static int BUYING_CENTRE = 128;

    /** Management level for supervision and board oversight. */
    public final static int SUPERVISORY = 256;

    /** Department for financing and capital management. */
    public final static int FINANCING = 512;

    /** Department for legal and administrative compliance. */
    public final static int LEGAL = 1024;

    /** Department for infrastructure maintenance and security. */
    public final static int INFRASTRUCTURE = 2048;

    /** Department for information systems and IT. */
    public final static int INFORMATION = 4096;

}
