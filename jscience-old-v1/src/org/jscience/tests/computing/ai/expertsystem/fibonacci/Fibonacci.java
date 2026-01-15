package org.jscience.tests.computing.ai.expertsystem.fibonacci;


/*
 * org.jscience.tests.computing.ai.expertsystem - The Java Embedded Object Production System
 * Copyright (c) 2000   Carlos Figueira Filho
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Contact: Carlos Figueira Filho (csff@cin.ufpe.br)
 */
/**
 * This class models an encapsulation for a solution for the Fibonacci
 * series.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  15 Mar 1998
 */
public class Fibonacci {
    /** The order of this element of the series. */
    public int n;

    /** The value of the element in the series. */
    public int value;

    /** The first subproblem used to solve this recursion. */
    public Fibonacci son1;

    /** The second subproblem used to solve this recursion. */
    public Fibonacci son2;

/**
     * Class constructor.
     *
     * @param n the order of the series.
     */
    public Fibonacci(int n) {
        this.n = n;
        this.value = -1;
    }

    /**
     * Returns the order of this element of the series.
     *
     * @return the order of this element of the series.
     */
    public int getN() {
        return n;
    }

    /**
     * Returns the first subproblem used to solve this recursion.
     *
     * @return the first subproblem used to solve this recursion
     */
    public Fibonacci getSon1() {
        return son1;
    }

    /**
     * Returns the second subproblem used to solve this recursion.
     *
     * @return the second subproblem used to solve this recursion
     */
    public Fibonacci getSon2() {
        return son2;
    }

    /**
     * Returns the value of this element of the series.
     *
     * @return the value of this element of the series, or -1 if the value
     *         hasb't been calculated yet.
     */
    public int getValue() {
        return value;
    }

    /**
     * Defines the first subproblem used to solve this recursion.
     *
     * @param newValue the first subproblem used to solve this recursion.
     */
    public void setSon1(Fibonacci newValue) {
        this.son1 = newValue;
    }

    /**
     * Defines the second subproblem used to solve this recursion.
     *
     * @param newValue the second subproblem used to solve this recursion.
     */
    public void setSon2(Fibonacci newValue) {
        this.son2 = newValue;
    }

    /**
     * Defines the value of this element in the series.
     *
     * @param newValue the value of this element in the series.
     */
    public void setValue(int newValue) {
        this.value = newValue;
    }
}
