package org.jscience.computing.ai.expertsystem.conflict;

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

/**
 * An element present in the conflict set.
 *
 * @author Carlos Figueira Filho (<a href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.02  07 Apr 2000   The local declarations are now stored as a
 *          two-dimensional array, which stores their
 *          dependencies with the regular declarations.
 * @history 0.01  31 Mar 2000
 */
public class ConflictSetElement {

    /**
     * The index of the fireable rule.
     */
    private int ruleIndex;

    /**
     * The objects bound to the declared variables.
     */
    private Object[] objects;

    /**
     * A timestamp of this element. Useful for conflict resolution
     * policies based on object recency.
     */
    private long timestamp;

    /**
     * Class constructor.
     *
     * @param ruleIndex the index of the fireable rule.
     * @param objects   the objects bound to the declared variables.
     */
    public ConflictSetElement(int ruleIndex, Object[] objects) {
        this.ruleIndex = ruleIndex;
        this.objects = objects;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Auxiliar method used to compare two arrays of objects.
     *
     * @param array1 the first array to be compared.
     * @param array2 the second array to be compared.
     * @return <code>true</code> if all elements of the arrays are equals;
     *         <code>false</code> otherwise.
     */
    private static boolean compareArrays(Object[] array1, Object[] array2) {
        if (array1.length != array2.length) {
            return false;
        } else {
            for (int i = array1.length - 1; i >= 0; i--) {
                if (array1[i] == null) {
                    if (array2[i] != null) {
                        return false;
                    }
                } else if (!array1[i].equals(array2[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Compares this object with the given one.
     *
     * @param obj the object to be compared
     * @return <code>true</code> if the two objects represent the same
     *         conflict set element; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof ConflictSetElement) {
            ConflictSetElement temp = (ConflictSetElement) obj;
            if (temp.ruleIndex != this.ruleIndex) {
                return false;
            } else {
                return compareArrays(temp.objects, this.objects);
            }
        }
        return false;
    }

    /**
     * Returns the objects bound to the declared variables.
     *
     * @return the objects bound to the declared variables.
     */
    public Object[] getObjects() {
        return objects;
    }

    /**
     * Returns the index of the fireable rule.
     *
     * @return the index of the fireable rule.
     */
    public int getRuleIndex() {
        return ruleIndex;
    }

    /**
     * Returns the timestamp of this element.
     *
     * @return the timestamp of this element.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Returns a hash code for this object.
     *
     * @return a hash code for this object.
     */
    public int hashCode() {
        int result = ruleIndex;
        for (int i = 0; i < objects.length; i++) {
            result += objects[i].hashCode();
        }
        return result;
    }

    /**
     * Checks whether a given object is one of the objects bound to the
     * declared variables in this element.
     *
     * @param obj the object to be checked.
     * @return <code>true</code> if the given object bound to one of the
     *         declared variables in this element; <code>false</code>
     *         otherwise.
     */
    public boolean isDeclared(Object obj) {
        for (int i = objects.length - 1; i >= 0; i--) {
            if (obj.equals(objects[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a string representation of this object. Useful for
     * debugging.
     *
     * @return a string representation of this object.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("CSE[");
        sb.append(ruleIndex);
        sb.append(",{");
        for (int i = 0; i < objects.length; i++) {
            sb.append(objects[i]);
            if (i == objects.length - 1) {
                sb.append('}');
            } else {
                sb.append(',');
            }
        }
        sb.append(']');
        return sb.toString();
    }

}
