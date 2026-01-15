package org.jscience.computing.ai.expertsystem;

/*
 * JEOPS - The Java Embedded Object Production System
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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * This class models the facts over which the inference engine will act.
 * By facts we mean any object that is stored in this base - there's no
 * notion of truth or falseness. A fact simply exists or doesn't.
 *
 * @author Carlos Figueira Filho (<a href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.03  18 Sep 2000   Implementation of the methods transferred
 *          from class ObjectHashTable to this class.
 * @history 0.01  12 Mar 2000   Class adapted from previous version of JEOPS.
 * @history 0.02  17 Jul 2000   Cache removed, as the object base won't
 * be used intensively at the merging
 * process anymore. The method insert now
 * returns a boolean indicating whether the
 * object being inserted was already in this
 * base.
 */
public class ObjectBase {

    /**
     * The superclasses/subclasses mapping. Its elements will be Vectors,
     * as there can be more than one subclass per class. Actually, this word
     * is a little misused, as we consider interfaces implemented by a class
     * as its superclasses too.
     */
    private Map subClasses;

    /**
     * The class/objects mapping. Its elements will be Vectors, as there can
     * be more than one element per class.
     */
    private Map objects;

    /**
     * Class constructor. Creates a new object base, initially empty.
     */
    public ObjectBase() {
        flush();
    }

    /**
     * Inserts a new object into this object base.
     *
     * @param obj the object to be inserted.
     * @return <code>true</code> if the insertion was successful
     *         (i.e., the object wasn't in the base;
     *         <code>false</code> otherwise.
     */
    public boolean insert(Object obj) {
        String className = obj.getClass().getName();
        Object objectsOfClass = objects.get(className);
        boolean hierarchyAlreadyPresent = false;
        Vector aux;
        if (objectsOfClass == null) {
            aux = new Vector();
            objects.put(className, aux);
        } else {
            aux = (Vector) objectsOfClass;
            hierarchyAlreadyPresent = true;
        }
        boolean result = !aux.contains(obj);
        if (result) {
            aux.addElement(obj);
        }
        if (!hierarchyAlreadyPresent) {
            Class classObject = null;
            try {
                classObject = Class.forName(className);
            } catch (Exception e) {
            }
            Class classAux = classObject;
            while (classAux != null) {
                classAux = classAux.getSuperclass();
                if (classAux != null) {
                    insertInheritancePair(classAux.getName(), className);
                }
            }
            while (classObject != null) {
                Class[] interfaces = classObject.getInterfaces();
                for (int i = 0; i < interfaces.length; i++) {
                    insertImplementedInterfaces(interfaces[i], className);
                }
                classObject = classObject.getSuperclass();
            }
        }
        return result;
    }

    /**
     * Inserts a pairs <interface, implementingClass> into the subClass
     * map for the given class, as well as the corresponding pairs for the
     * superinterfaces of the given one.
     *
     * @param interfaceClass the class object representing the interface.
     * @param className      the name of the implementing class.
     */
    private void insertImplementedInterfaces(Class interfaceClass,
                                             String className) {
        insertInheritancePair(interfaceClass.getName(), className);
        Class[] superInterfaces = interfaceClass.getInterfaces();
        for (int i = 0; i < superInterfaces.length; i++) {
            insertImplementedInterfaces(superInterfaces[i], className);
        }
    }

    /**
     * Inserts a new pair <superclass, subclass>.
     *
     * @param superclass the name of the superclass.
     * @param subclass   the name of the subclass.
     */
    private void insertInheritancePair(String superclass, String subclass) {
        Object obj = subClasses.get(superclass);
        if (obj != null) {
            Vector v = (Vector) obj;
            if (!v.contains(subclass)) {
                v.addElement(subclass);
            }
        } else {
            Vector v = new Vector();
            v.addElement(subclass);
            subClasses.put(superclass, v);
        }
    }

    /**
     * Removes all objects of this base.
     */
    public void flush() {
        subClasses = new HashMap();
        objects = new HashMap();
    }

    /**
     * Returns the objects of the given class.
     *
     * @param className the name of the class whose objects are
     *                  being removed from this base.
     * @return all objects that are instances of the given
     *         class.
     */
    public Vector objects(String className) {
        Object obj = objects.get(className);
        Vector v = new Vector();
        Vector aux;
        if (obj != null) {
            aux = (Vector) obj;
            for (Enumeration e = aux.elements(); e.hasMoreElements();) {
                v.addElement(e.nextElement());
            }
        }
        obj = subClasses.get(className);
        if (obj != null) {
            aux = (Vector) obj;
            for (Enumeration e = aux.elements(); e.hasMoreElements();) {
                String subClassName = (String) e.nextElement();
                obj = objects.get(subClassName);
                if (obj != null) {
                    Vector aux2 = (Vector) obj;
                    for (Enumeration e2 = aux2.elements(); e2.hasMoreElements();) {
                        v.addElement(e2.nextElement());
                    }
                }
            }
        }
        return v;
    }

    /**
     * Removes an object from this object base.
     *
     * @param obj the object to be removed from this base.
     * @return <code>true</code> if the remotion was successful
     *         (i.e., the given object belonged to this base);
     *         <code>false</code> otherwise.
     */
    public boolean remove(Object obj) {
        boolean result = false;
        String className = obj.getClass().getName();
        Object objectsOfClass = objects.get(className);
        if (objectsOfClass != null) {
            Vector v = (Vector) objectsOfClass;
            result = v.removeElement(obj);
        }
        return result;
    }
}
