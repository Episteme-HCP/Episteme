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

package org.jscience.util.identity;

import java.io.Serializable;
import java.util.Map;
import org.jscience.util.Commented;
import org.jscience.util.Named;

/**
 * An encompassing interface that groups identification, naming, commenting, and serialization.
 * This interface provides a comprehensive support for entities that need to be uniquely 
 * identified and carry descriptive metadata.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface ComprehensiveIdentification extends Identified<Identification>, Named, Commented, Serializable {

    /**
     * Returns the traits map for this entity.
     * @return the traits map
     */
    Map<String, Object> getTraits();

    /**
     * Returns a trait value by name.
     * @param name the trait name
     * @return the trait value, or null if not found
     */
    default Object getTrait(String name) {
        return getTraits().get(name);
    }

    /**
     * Sets a trait value.
     * @param name the trait name
     * @param value the trait value
     */
    default void setTrait(String name, Object value) {
        getTraits().put(name, value);
    }

    @Override
    default String getName() {
        String name = (String) getTrait("name");
        return name != null ? name : getId().toString();
    }

    /**
     * Sets the name of this entity.
     * @param name the name to set
     */
    default void setName(String name) {
        setTrait("name", name);
    }
}
