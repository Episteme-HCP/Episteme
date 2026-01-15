/*
 * NamedDouble.java
 *
 * Created on December 17, 2005, 12:18 AM
 *
    Copyright (C) __YEAR__  Brandon Wegge and Herb Smith

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 *
 *  Utilility class that allows a double to be named.
 */
package org.jscience.physics.solids.gui.util;

/**
 * 
DOCUMENT ME!
 *
 * @author Wegge
 */
public class NamedDouble {
    /**
     * DOCUMENT ME!
     */
    public String name;

    /**
     * DOCUMENT ME!
     */
    public double value;

/**
     * Creates a new instance of NamedDouble
     */
    public NamedDouble(String name) {
        this.name = name;
    }

/**
     * Creates a new instance of NamedDouble
     */
    public NamedDouble(String name, double value) {
        this.name = name;
        this.value = value;
    }
}
