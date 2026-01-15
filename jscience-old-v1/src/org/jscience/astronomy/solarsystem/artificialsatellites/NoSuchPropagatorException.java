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
 * 
 */
public class NoSuchPropagatorException extends Exception {
/**
     *
     */
    public NoSuchPropagatorException() {
    }

/**
     * @param message
     */
    public NoSuchPropagatorException(String message) {
        super(message);
    }

/**
     * @param message
     * @param cause
     */
    public NoSuchPropagatorException(String message, Throwable cause) {
        super(message, cause);
    }

/**
     * @param cause
     */
    public NoSuchPropagatorException(Throwable cause) {
        super(cause);
    }
}
