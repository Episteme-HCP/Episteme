/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2014 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.economics.resources;

/**
 * A class representing useful constants defining artifacts.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.2
 */
public final class ArtifactsConstants {

    /** 
     * Default constructor (private).
     */
    private ArtifactsConstants() {
        // Prevents instantiation
    }

    /** Constant for unknown or undefined artifact type. */
    public final static int UNKNOWN = 0;

    //places
    /** Shelter artifact type (e.g., home, castle). */
    public final static int SHELTER = 1;

    /** Gathering place artifact type (e.g., city center, forum, administrative place). */
    public final static int GATHER = 2;

    /** Leisure artifact type (e.g., feasts, amusement parks). */
    public final static int LEISURE = 3;

    /** Dining artifact type (e.g., restaurants, bars). */
    public final static int EAT = 4;

    /** Spiritual or intellectual artifact type (e.g., churches). */
    public final static int THINK = 5;

    /** Trade artifact type (e.g., markets). */
    public final static int TRADE = 6;
}
