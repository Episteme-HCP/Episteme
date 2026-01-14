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
 * DOCUMENT ME!
 *
 * @author <small>baylor</small>
 */
public class FilterCriterion {
    //-----------------------------------------------------------------------------
    // Public constants
    //-----------------------------------------------------------------------------
    /** DOCUMENT ME! */
    public static final int OPERATOR_UNKNOWN = 0;

    /** DOCUMENT ME! */
    public static final int OPERATOR_EQUALS = 1;

    /** DOCUMENT ME! */
    public static final int OPERATOR_NOT_EQUAL = 2;

    /** DOCUMENT ME! */
    public static final int OPERATOR_GREATER_THAN = 3;

    /** DOCUMENT ME! */
    public static final int OPERATOR_GREATER_THAN_OR_EQUAL = 4;

    /** DOCUMENT ME! */
    public static final int OPERATOR_LESS_THAN = 5;

    /** DOCUMENT ME! */
    public static final int OPERATOR_LESS_THAN_OR_EQUAL = 6;

    //-----------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------
    /** DOCUMENT ME! */
    protected String fieldName;

    /** DOCUMENT ME! */
    protected int operator;

    /** DOCUMENT ME! */
    protected TraitValue value;

    //-----------------------------------------------------------------------------
    /**
     * Creates a new FilterCriterion object.
     *
     * @param fieldName DOCUMENT ME!
     * @param operator DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    protected FilterCriterion(String fieldName, String operator, String value) {
        this.fieldName = fieldName;

        this.operator = stringToOperator(operator);

        this.value = new TraitValue(value);
    } //--- constructor

    //-----------------------------------------------------------------------------
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getOperator() {
        return operator;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getOperatorAsString() {
        return operatorToString(operator);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TraitValue getValue() {
        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @param item DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean itemViolates(Item item) {
        boolean violationFound = true;

        int type = item.getTraitDataType(this.getFieldName());

        int operator = this.getOperator();

        TraitValue itemValue = item.getTraitValue(this.getFieldName());

        //--- Ugly messy code
        //--- Switch statements aren't great, nested ones are a sure sign
        //---   that someone (like me) didn't think this through very well
        //--- Better fix this once i get this out of the quick and dirty stage
        switch (operator) {
        case OPERATOR_EQUALS:

            switch (type) {
            case TraitDescriptor.TYPE_BOOLEAN:
                violationFound = (!(itemValue.toBoolean() == this.value.toBoolean()));

                break;

            case TraitDescriptor.TYPE_FLOAT:
                violationFound = (!(itemValue.toFloat() == this.value.toFloat()));

                break;

            case TraitDescriptor.TYPE_INTEGER:
                violationFound = (!(itemValue.toInteger() == this.value.toInteger()));

                break;

            case TraitDescriptor.TYPE_STRING:
                violationFound = (!(this.value.toString()
                                              .equals(itemValue.toString())));

                break;
            }

            break;

        case OPERATOR_NOT_EQUAL:

            switch (type) {
            case TraitDescriptor.TYPE_BOOLEAN:
                violationFound = (!(itemValue.toBoolean() != this.value.toBoolean()));

                break;

            case TraitDescriptor.TYPE_FLOAT:
                violationFound = (!(itemValue.toFloat() != this.value.toFloat()));

                break;

            case TraitDescriptor.TYPE_INTEGER:
                violationFound = (!(itemValue.toInteger() != this.value.toInteger()));

                break;

            case TraitDescriptor.TYPE_STRING:
                violationFound = (this.value.toString()
                                            .equals(itemValue.toString()));

                break;
            }

            break;

        case OPERATOR_GREATER_THAN:

            switch (type) {
            case TraitDescriptor.TYPE_FLOAT:
                violationFound = (!(itemValue.toFloat() > this.value.toFloat()));

                break;

            case TraitDescriptor.TYPE_INTEGER:
                violationFound = (!(itemValue.toInteger() > this.value.toInteger()));

                break;
            }

            break;

        case OPERATOR_GREATER_THAN_OR_EQUAL:

            switch (type) {
            case TraitDescriptor.TYPE_FLOAT:
                violationFound = (!(itemValue.toFloat() >= this.value.toFloat()));

                break;

            case TraitDescriptor.TYPE_INTEGER:
                violationFound = (!(itemValue.toInteger() >= this.value.toInteger()));

                break;
            }

            break;

        case OPERATOR_LESS_THAN:

            switch (type) {
            case TraitDescriptor.TYPE_FLOAT:
                violationFound = (!(itemValue.toFloat() < this.value.toFloat()));

                break;

            case TraitDescriptor.TYPE_INTEGER:
                violationFound = (!(itemValue.toInteger() < this.value.toInteger()));

                break;
            }

            break;

        case OPERATOR_LESS_THAN_OR_EQUAL:

            switch (type) {
            case TraitDescriptor.TYPE_FLOAT:
                violationFound = (!(itemValue.toFloat() <= this.value.toFloat()));

                break;

            case TraitDescriptor.TYPE_INTEGER:
                violationFound = (!(itemValue.toInteger() <= this.value.toInteger()));

                break;
            }

            break;
        } //--- switch( operator )

        return violationFound;
    } //--- itemViolates

    /**
     * DOCUMENT ME!
     *
     * @param operator DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String operatorToString(int operator) {
        String stringValue = null;

        if (operator == OPERATOR_EQUALS) {
            stringValue = "=";
        } else if (operator == OPERATOR_NOT_EQUAL) {
            stringValue = "!=";
        } else if (operator == OPERATOR_GREATER_THAN) {
            stringValue = ">";
        } else if (operator == OPERATOR_GREATER_THAN_OR_EQUAL) {
            stringValue = ">=";
        } else if (operator == OPERATOR_LESS_THAN) {
            stringValue = "<";
        } else if (operator == OPERATOR_LESS_THAN_OR_EQUAL) {
            stringValue = "<=";
        } //--- figure out the operator

        return stringValue;
    } //--- operatorToString

    /**
     * DOCUMENT ME!
     *
     * @param operator DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int stringToOperator(String operator) {
        int numericValue = OPERATOR_UNKNOWN;

        if (operator.equals("=")) {
            numericValue = OPERATOR_EQUALS;
        } else if (operator.equals("!=")) {
            numericValue = OPERATOR_NOT_EQUAL;
        } else if (operator.equals(">")) {
            numericValue = OPERATOR_GREATER_THAN;
        } else if (operator.equals(">=")) {
            numericValue = OPERATOR_GREATER_THAN_OR_EQUAL;
        } else if (operator.equals("<")) {
            numericValue = OPERATOR_LESS_THAN;
        } else if (operator.equals("<=")) {
            numericValue = OPERATOR_LESS_THAN_OR_EQUAL;
        } //--- figure out the operator

        return numericValue;
    } //--- stringToDataType
} //--- FilterCriterion
