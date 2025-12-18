/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.classical.waves.electromagnetism.circuits;

/**
 * Helper class for matrix row information during circuit analysis.
 * Used to track row types and simplifications in Modified Nodal Analysis.
 * 
 * @author JScience
 */
public class RowInfo {

    /** Normal row type - requires solving */
    public static final int ROW_NORMAL = 0;

    /** Constant row type - value is known */
    public static final int ROW_CONST = 1;

    /** Equal row type - value equals another node */
    public static final int ROW_EQUAL = 2;

    /** Row type (ROW_NORMAL, ROW_CONST, or ROW_EQUAL) */
    public int type = ROW_NORMAL;

    /** Column index after matrix mapping */
    public int mapCol;

    /** Row index after matrix mapping */
    public int mapRow;

    /** Constant value (if type is ROW_CONST) */
    public double value;

    /** Node index that this row equals (if type is ROW_EQUAL) */
    public int nodeEq;

    /** Whether the left side of this row changes during doStep() */
    public boolean lsChanges;

    /** Whether the right side of this row changes during doStep() */
    public boolean rsChanges;

    /** Whether this row should be dropped from the simplified matrix */
    public boolean dropRow;

    /**
     * Creates a new row info with default values.
     */
    public RowInfo() {
    }
}
