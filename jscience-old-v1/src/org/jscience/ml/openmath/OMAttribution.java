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

package org.jscience.ml.openmath;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Models an OpenMath attribution object. <p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.2 $
 * @see "The OpenMath standard 2.0, 2.1.3"
 */
public class OMAttribution extends OMObject {
    /**
     * Stores the constructor. <p>
     */
    protected OMObject constructor;

    /**
     * Stores the attributions. <p>
     */
    protected Hashtable attributions = new Hashtable();

    /**
     * Constructor. <p>
     */
    public OMAttribution() {
        super();
    }

    /**
     * Constructor. <p>
     *
     * @param newAttributions the table of attributions.
     * @param newConstructor  the attribution constructor.
     */
    public OMAttribution(Hashtable newAttributions, OMObject newConstructor) {
        super();

        attributions = newAttributions;
        constructor = newConstructor;
    }

    /**
     * Gets the type. <p>
     */
    public String getType() {
        return "OMATTR";
    }

    /**
     * Get the attributions. <p>
     *
     * @return the hashtable of attributions.
     */
    public Hashtable getAttributions() {
        return attributions;
    }

    /**
     * Set the attributions. <p>
     *
     * @param newAttributions the hashtable of attributions
     */
    public void setAttributions(Hashtable newAttributions) {
        attributions = newAttributions;
    }

    /**
     * Get the attribution constructor. <p>
     *
     * @return the attribution constructor.
     */
    public OMObject getConstructor() {
        return constructor;
    }

    /**
     * Set the attribution constructor. <p>
     *
     * @param newConstructor the attribution constructor.
     */
    public void setConstructor(OMObject newConstructor) {
        constructor = newConstructor;
    }

    /**
     * Puts an attribution. <p>
     *
     * @param key   the key of the attribution.
     * @param value the value of the attribution.
     */
    public void put(OMObject key, OMObject value) {
        attributions.put(key, value);
    }

    /**
     * Removes an attribution. <p>
     *
     * @param key the attribution to remove by key.
     */
    public void remove(OMObject key) {
        attributions.remove(key);
    }

    /**
     * Gets the value of the attribution. <p>
     *
     * @param key the attribution to look for by key.
     * @return the attribution value found, or <b>null</b> if not found.
     */
    public OMObject get(OMObject key) {
        return (OMObject) attributions.get(key);
    }

    /**
     * Has symbol. <p>
     * <p/>
     * <p/>
     * <i>Note: this method does NOT do a (cd, name)-pair match. It does
     * the comparison on object level. So an OMSymbol with the same
     * CD and same Name is not necessarily the same object. This
     * is done, because the standard allows multiple attribute-pairs
     * with the same symbol. </i>
     * </p>
     *
     * @param key the key to look for.
     * @return <b>true</b> if it contains the given key, <b>false</b> if not.
     */
    public boolean hasKey(OMObject key) {
        return attributions.containsKey(key);
    }

    /**
     * Has value. <p>
     *
     * @param value the value to look for.
     * @return <b>true</b> if it contains the given value,
     *         <b>false</b> otherwise.
     */
    public boolean hasValue(OMObject value) {
        return attributions.containsValue(value);
    }

    /**
     * Get the symbol (keys). <p>
     *
     * @return an enumeration of the keys.
     */
    public Enumeration getKeys() {
        return attributions.keys();
    }

    /**
     * Get the values. <p>
     *
     * @return an enumeration of the values.
     */
    public Enumeration getValues() {
        return attributions.elements();
    }

    /**
     * Returns a string representation of the object. <p>
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        Enumeration enumeration = attributions.keys();
        result.append("<OMATTR><OMATP>");

        for (; enumeration.hasMoreElements();) {
            OMObject key = (OMObject) enumeration.nextElement();
            OMObject value = (OMObject) attributions.get(key);

            result.append(key.toString());
            result.append(value.toString());
        }

        result.append("</OMATP>");
        result.append(constructor.toString());
        result.append("</OMATTR>");

        return result.toString();
    }

    /**
     * Flatten the attribution. <p>
     *
     * @return the flattened attribution.
     */
    public OMAttribution flatten() {
        if (constructor instanceof OMAttribution) {
            OMAttribution newConstructor = ((OMAttribution) constructor).flatten();
            OMAttribution newAttribution = new OMAttribution();

            newAttribution.setConstructor(newConstructor.getConstructor());

            Enumeration enumeration = newConstructor.getKeys();

            for (; enumeration.hasMoreElements();) {
                OMObject key = (OMObject) enumeration.nextElement();
                OMObject value = (OMObject) newConstructor.get(key);
                newAttribution.put((OMObject) key, (OMObject) value);
            }

            enumeration = getKeys();

            for (; enumeration.hasMoreElements();) {
                OMObject key = (OMObject) enumeration.nextElement();
                OMObject value = (OMObject) get(key);
                newAttribution.put((OMObject) key, (OMObject) value);
            }

            return newAttribution;
        } else {
            return (OMAttribution) clone();
        }
    }

    /**
     * Strip the attribution. <p>
     *
     * @return the stripped OpenMath object.
     */
    public OMObject strip() {
        return (OMObject) constructor.copy();
    }

    /**
     * Clones the object (shallow copy).
     */
    public Object clone() {
        OMAttribution attribution = new OMAttribution();
        attribution.constructor = constructor;
        attribution.attributions = (Hashtable) attributions.clone();
        return attribution;
    }

    /**
     * Copies the object (full copy).
     */
    public Object copy() {
        OMAttribution attribution = new OMAttribution();
        attribution.constructor = (OMObject) constructor.copy();
        attribution.attributions = (Hashtable) attributions.clone();
        return attribution;
    }

    /**
     * Are we a composite object.
     */
    public boolean isComposite() {
        return true;
    }

    /**
     * Are we an atom object.
     */
    public boolean isAtom() {
        return false;
    }

    /**
     * Determines if this is the same object. <p>
     */
    public boolean isSame(OMObject object) {
        if (object instanceof OMAttribution) {
            OMAttribution attribution = (OMAttribution) object;
            if (attribution.constructor.isSame(constructor))
                return true;
        }
        return false;
    }

    /**
     * Determines if this is a valid object. <p>
     */
    public boolean isValid() {
        if (constructor != null) {
            if (attributions.size() >= 1)
                return true;
        }
        return false;
    }

    /**
     * Replace any occurrence of source to destination. <p>
     *
     * @param source the source object.
     * @param dest   the destination object.
     * @return the application with the replacing.
     */
    public OMObject replace(OMObject source, OMObject dest) {
        if (!constructor.isSame(source)) {
            if (constructor instanceof OMApplication) {
                OMApplication application = (OMApplication) constructor;
                constructor = application.replace(source, dest);
            }
            if (constructor instanceof OMAttribution) {
                OMAttribution attribution = (OMAttribution) constructor;
                constructor = attribution.replace(source, dest);
            }
            if (constructor instanceof OMBinding) {
                OMBinding binding = (OMBinding) constructor;
                constructor = binding.replace(source, dest);
            }
            if (constructor instanceof OMError) {
                OMError error = (OMError) constructor;
                constructor = error.replace(source, dest);
            }
        } else {
            constructor = dest;
        }
        return this;
    }
}
