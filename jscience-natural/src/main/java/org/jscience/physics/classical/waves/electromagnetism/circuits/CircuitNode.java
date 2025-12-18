/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.classical.waves.electromagnetism.circuits;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node (connection point) in an electrical circuit.
 * Nodes are the points where circuit elements connect.
 * Each node is assigned a voltage during simulation.
 * 
 * @author JScience
 */
public class CircuitNode {

    /** X coordinate (for topological purposes, not physics) */
    public int x;

    /** Y coordinate (for topological purposes, not physics) */
    public int y;

    /** Whether this is an internal node (not an external terminal) */
    public boolean internal;

    /** Links to circuit elements connected to this node */
    public final List<CircuitNodeLink> links = new ArrayList<>();

    /**
     * Creates a new circuit node at the origin.
     */
    public CircuitNode() {
        this(0, 0);
    }

    /**
     * Creates a new circuit node at the specified coordinates.
     * 
     * @param x X coordinate
     * @param y Y coordinate
     */
    public CircuitNode(int x, int y) {
        this.x = x;
        this.y = y;
        this.internal = false;
    }
}
