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
 * Represents a link between a circuit node and a circuit element.
 * Each link identifies which terminal (post) of the element connects to the
 * node.
 * 
 * @author JScience
 */
public class CircuitNodeLink {

    /**
     * The terminal number on the element (0 = first post, 1 = second post, etc.)
     */
    public int num;

    /** The circuit element connected via this link */
    public CircuitElement elm;

    /**
     * Creates an empty node link.
     */
    public CircuitNodeLink() {
    }

    /**
     * Creates a node link with the specified element and terminal.
     * 
     * @param elm The circuit element
     * @param num The terminal number on the element
     */
    public CircuitNodeLink(CircuitElement elm, int num) {
        this.elm = elm;
        this.num = num;
    }
}
