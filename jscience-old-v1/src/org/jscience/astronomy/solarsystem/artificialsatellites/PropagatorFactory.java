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

package org.jscience.astronomy.solarsystem.artificialsatellites;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.util.HashMap;
import java.util.Map;


/**
 * Instances of this factory construct instances of AbstractPropagator
 * based on
 */
public final class PropagatorFactory {
    /** Map of propgator class names keyed by the name of the propagator. */
    private Map propagatorClasses = null;

/**
     * Constructs an instance of this factory.
     */
    public PropagatorFactory() {
        propagatorClasses = new HashMap();
        propagatorClasses.put("1",
            "org.jscience.astronomy.solarsystem.artificialsatellites.SGP");
        propagatorClasses.put("SGP",
            "org.jscience.astronomy.solarsystem.artificialsatellites.SGP");
        propagatorClasses.put("2",
            "org.jscience.astronomy.solarsystem.artificialsatellites.SGP4");
        propagatorClasses.put("SGP4",
            "org.jscience.astronomy.solarsystem.artificialsatellites.SGP4");
        propagatorClasses.put("3",
            "org.jscience.astronomy.solarsystem.artificialsatellites.SDP4");
        propagatorClasses.put("SDP4",
            "org.jscience.astronomy.solarsystem.artificialsatellites.SDP4");
        propagatorClasses.put("4",
            "org.jscience.astronomy.solarsystem.artificialsatellites.SGP8");
        propagatorClasses.put("SGP8",
            "org.jscience.astronomy.solarsystem.artificialsatellites.SGP8");
        propagatorClasses.put("5",
            "org.jscience.astronomy.solarsystem.artificialsatellites.SDP8");
        propagatorClasses.put("SDP8",
            "org.jscience.astronomy.solarsystem.artificialsatellites.SDP8");
    }

/**
     * Constructs an instance of this factory.
     */
    public PropagatorFactory(Map propagatorClasses) {
        this.propagatorClasses = propagatorClasses;
    }

    /**
     * Constructs an instance of AbstractPropagator
     *
     * @param key DOCUMENT ME!
     * @param es DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchPropagatorException DOCUMENT ME!
     */
    public Propagator newInstance(String key, ElementSet es)
        throws NoSuchPropagatorException {
        String propagatorClass = null;

        try {
            propagatorClass = (String) propagatorClasses.get(key.toUpperCase());

            if (propagatorClass == null) {
                throw new NullPointerException();
            }

            Class pClass = Class.forName(propagatorClass);
            Constructor pConstructor = pClass.getConstructor(new Class[] {
                        ElementSet.class
                    });

            return (Propagator) pConstructor.newInstance(new Object[] { es });
        } catch (NullPointerException e) {
            throw new NoSuchPropagatorException("Unrecognized propagator key: " +
                key);
        } catch (ClassNotFoundException e) {
            throw new NoSuchPropagatorException("Unknown propagator class: " +
                propagatorClass, e);
        } catch (NoSuchMethodException e) {
            throw new NoSuchPropagatorException(
                "No ElementSet constructor for propagator class: " +
                propagatorClass, e);
        } catch (IllegalAccessException e) {
            throw new NoSuchPropagatorException(
                "Cannot access ElementSet constructor for propagator class: " +
                propagatorClass, e);
        } catch (InvocationTargetException e) {
            throw new NoSuchPropagatorException(
                "Problem invoking ElementSet constructor for propagator class: " +
                propagatorClass, e);
        } catch (InstantiationException e) {
            throw new NoSuchPropagatorException(
                "Problem instantiating instance of propagator class: " +
                propagatorClass, e);
        }
    }
}
