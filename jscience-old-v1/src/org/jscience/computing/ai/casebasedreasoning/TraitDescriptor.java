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

/**
 * This class is the meta data for the traits
 * <p/>
 * That means the trait name and data type
 */
public class TraitDescriptor {
    //--- Public constants

    /**
     * DOCUMENT ME!
     */
    public static final int TYPE_UNKNOWN = 0;

    /**
     * DOCUMENT ME!
     */
    public static final int TYPE_BOOLEAN = 1;

    /**
     * DOCUMENT ME!
     */
    public static final int TYPE_FLOAT = 2;

    /**
     * DOCUMENT ME!
     */
    public static final int TYPE_INTEGER = 3;

    /**
     * DOCUMENT ME!
     */
    public static final int TYPE_STRING = 4;

    //-----------------------------------------------------------------------------
    //--- Private constants
    private static final String BOOLEAN_ABBREVIATION = "b";
    private static final String FLOAT_ABBREVIATION = "f";
    private static final String INTEGER_ABBREVIATION = "i";
    private static final String STRING_ABBREVIATION = "s";

    //-----------------------------------------------------------------------------
    //--- Attributes
    private String name = null;
    private int dataType = TYPE_UNKNOWN;

    //-----------------------------------------------------------------------------
    TraitDescriptor(String encodedData) {
        loadFromDelimitedString(encodedData);
    } //--- constructor

    //-----------------------------------------------------------------------------
    // Methods
    //-----------------------------------------------------------------------------
    public int getDataType() {
        return dataType;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param encodedData DOCUMENT ME!
     */
    public void loadFromDelimitedString(String encodedData) {
        try {
            //--- encodedData will be in the format
            //---   s:val
            //--- where the first character is the data type,
            //---   the second character is the delimiter
            //---   and the rest is the field name
            //--- Trim the spaces off the data they passed us
            String field = encodedData.trim();

            //--- The name is in positions 3-string length
            //--- Position 1 is the datatype and 2 is the delimiter
            name = field.substring(2).trim();

            //--- Convert the datatype character to a known constant
            //---   for that type
            String dataTypeAbbreviation = field.substring(0, 1).toLowerCase();

            dataType = stringToDataType(dataTypeAbbreviation);
        } //--- try
        catch (Exception e) {
            String methodName = "Trait::loadFromDelimitedString";

            System.out.println(methodName + " error: " + e);
        } //--- catch
    } //--- loadFromDelimitedString

    private int stringToDataType(String dataType) {
        int numericValue = TYPE_UNKNOWN;

        if (dataType.equals(BOOLEAN_ABBREVIATION)) {
            numericValue = TYPE_BOOLEAN;
        } else if (dataType.equals(FLOAT_ABBREVIATION)) {
            numericValue = TYPE_FLOAT;
        } else if (dataType.equals(INTEGER_ABBREVIATION)) {
            numericValue = TYPE_INTEGER;
        } else if (dataType.equals(STRING_ABBREVIATION)) {
            numericValue = TYPE_STRING;
        } //--- figure out the datatype

        return numericValue;
    } //--- stringToDataType

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return getName();
    }
} //--- TraitDescriptor
