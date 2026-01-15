/*
 * Copyright (C) 2006 Matthew Funk
 * Licensed under the Academic Free License version 1.2
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Academic Free License version 1.2 for more details.
 */
package org.jscience.astronomy.solarsystem.artificialsatellites;

/**
 * Used to model the FORTRAN pass-by-reference of an double precision value
 * to a function.
 */
class DoubleRef {
    /**
     * DOCUMENT ME!
     */
    double value = 0;

/**
     * Constructs an instance of this class which has the given value.
     */
    DoubleRef(double value) {
        this.value = value;
    }
}
