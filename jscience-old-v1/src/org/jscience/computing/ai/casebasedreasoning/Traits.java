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

package org.jscience.computing.ai.casebasedreasoning;

import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * @author baylor
 */
public class Traits {
    //-----------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------
    private HashMap traits = new HashMap();

    //-----------------------------------------------------------------------------
    // Methods
    //-----------------------------------------------------------------------------
    protected void add(Trait newTrait) {
        String key = newTrait.getName();

        traits.put(key, newTrait);
    }

    /**
     * DOCUMENT ME!
     *
     * @param traitName DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Trait get(String traitName) {
        return (Trait) traits.get(traitName);
    } //--- get

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEmpty() {
        return traits.isEmpty();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator() {
        return traits.values().iterator();
    }

    /**
     * Load data from a string
     * <p/>
     * The string will have the data fields in the same
     * <p/>
     * order as the array of field definitions
     * <p/>
     * The string is | delimited
     */
    protected void loadFromDelimitedString(TraitDescriptors traitDescriptors,
                                           String encodedData) {
        try {
            int fieldNumber = 0;

            //--- Break the string into pieces
            StringTokenizer st = new StringTokenizer(encodedData, "|");

            //--- Get each field of data
            while (st.hasMoreTokens()) {
                String key = traitDescriptors.get(fieldNumber).toString();

                String value = st.nextToken().trim();

                Trait trait = new Trait(key, value);

                add(trait);

                fieldNumber++;
            } //--- while hasMoreTokens
        } //--- try
        catch (Exception e) {
            e.printStackTrace();
        } //--- catch
    } //--- loadFromDelimitedString
} //--- Traits
