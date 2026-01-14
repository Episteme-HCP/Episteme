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

import java.util.Iterator;

/**
 * @author <small>baylor</small>
 */
public class Item {
    //-----------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------
    Traits traits = new Traits();
    TraitDescriptors traitDescriptors;

    //-----------------------------------------------------------------------------
    // Constructors
    //-----------------------------------------------------------------------------
    protected Item(TraitDescriptors traitDescriptors) {
        this.traitDescriptors = traitDescriptors;
    }

    //-----------------------------------------------------------------------------
    // Methods
    //-----------------------------------------------------------------------------
    public int getTraitDataType(String traitName) {
        TraitDescriptor traitDescriptor = traitDescriptors.get(traitName);

        return traitDescriptor.getDataType();
    } //--- getTraitValue

    /**
     * DOCUMENT ME!
     *
     * @param traitName DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public TraitValue getTraitValue(String traitName) {
        Trait trait = traits.get(traitName);

        return trait.getValue();
    } //--- getTraitValue

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator() {
        return traits.iterator();
    }

    /**
     * Assumption: good data is passed in.
     * <p/>
     * This method does not currently do any error checking like
     * <p/>
     * making sure the number of fields matches the number of
     * <p/>
     * | delimited items in the data
     * <p/>
     * Hey, it's prototype code, what do you expect?
     */
    protected void loadFromDelimitedString(TraitDescriptors traitDescriptors,
                                           String encodedData) {
        try {
            traits.loadFromDelimitedString(traitDescriptors, encodedData);
        } //--- try
        catch (Exception e) {
            e.printStackTrace();
        } //--- catch
    } //--- loadFromDelimitedString
} //--- Item
