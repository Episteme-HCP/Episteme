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
