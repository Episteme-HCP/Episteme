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

package org.jscience.social.economics;

import org.jscience.core.util.Named;

/**
 * Defines various manufacturing and industrial processes.
 * Used to categorize activities in the production of goods.
 * Based on the taxonomy of manufacturing processes.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Process implements Named {

    /** Fermentation process. */
    public final static Process FERMENTATION = new Process("Fermentation");

    /** Heat process (includes melt, burn). */
    public final static Process HEAT = new Process("Heat"); 

    /** Split process (cut, separate, filter, dig). */
    public final static Process SPLIT = new Process("Split"); 

    /** Hit process (push firmly, press, nail, pedal). */
    public final static Process HIT = new Process("Hit"); 

    /** Mix process. */
    public final static Process MIX = new Process("Mix"); 

    /** Dry process. */
    public final static Process DRY = new Process("Dry");

    /** Humidify process. */
    public final static Process HUMIDIFY = new Process("Humidify");

    /** PsychologicalGroup process (centralize). */
    public final static Process PsychologicalGroup = new Process("PsychologicalGroup"); 

    /** Clean process. */
    public final static Process CLEAN = new Process("Clean");

    /** Store process (keep for later use). */
    public final static Process STORE = new Process("Store"); 

    /** Agitate process. */
    public final static Process AGITATE = new Process("Agitate");

    /** Transport process (moving, lifting, pushing). */
    public final static Process TRANSPORT = new Process("Transport"); 

    /** Mould process. */
    public final static Process MOULD = new Process("Mould");

    /** Spray process (paint, spill). */
    public final static Process SPRAY = new Process("Spray"); 

    /** Parallelize process. */
    public final static Process PARALLELIZE = new Process("Parallelize"); 

    /** Freeze process. */
    public final static Process FREEZE = new Process("Freeze"); 

    /** Pour process (fill). */
    public final static Process POUR = new Process("Pour"); 

    /** Select process (grab). */
    public final static Process SELECT = new Process("Select"); 

    /** Activate process (power on). */
    public final static Process ACTIVATE = new Process("Activate"); 

    /** Ventilate process. */
    public final static Process VENTILATE = new Process("Ventilate");

    /** Inflate process. */
    public final static Process INFLATE = new Process("Inflate");

    /** Assemble process (glue, screw, etc.). */
    public final static Process ASSEMBLE = new Process("Assemble"); 

    private final String name;
    private String comments;

    /**
     * Creates a new Process object.
     *
     * @param name the name of the process
     * @throws IllegalArgumentException if name is null or empty
     */
    public Process(String name) {
        if ((name != null) && (name.length() > 0)) {
            this.name = name;
            this.comments = "";
        } else {
            throw new IllegalArgumentException("Process name cannot be null or empty.");
        }
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the comments associated with this process.
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the comments for this process.
     * @param comments the comments to set
     * @throws IllegalArgumentException if comments is null
     */
    public void setComments(String comments) {
        if (comments != null) {
            this.comments = comments;
        } else {
            throw new IllegalArgumentException("Comments cannot be null.");
        }
    }
}

