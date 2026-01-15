package org.jscience.computing.ai.casebasedreasoning;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class SimilarityCriterion {
    //-----------------------------------------------------------------------------
    // Public constants
    //-----------------------------------------------------------------------------
    /** DOCUMENT ME! */
    public static final int OPERATOR_UNKNOWN = 0;

    /** DOCUMENT ME! */
    public static final int OPERATOR_SIMILAR = 1;

    /** DOCUMENT ME! */
    public static final int OPERATOR_NOT_SIMILAR = 2;

    /** DOCUMENT ME! */
    public static final int OPERATOR_AROUND = 3;

    //-----------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------
    /** DOCUMENT ME! */
    private String fieldName;

    /** DOCUMENT ME! */
    private int operator;

    /** DOCUMENT ME! */
    private TraitValue value;

    //-----------------------------------------------------------------------------
    /**
     * Creates a new SimilarityCriterion object.
     *
     * @param fieldName DOCUMENT ME!
     * @param operator DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    protected SimilarityCriterion(String fieldName, String operator,
        String value) {
        this.fieldName = fieldName;

        this.operator = stringToOperator(operator);

        this.value = new TraitValue(value);
    }

    //-----------------------------------------------------------------------------
    /**
     * DOCUMENT ME!
     *
     * @param operator DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String operatorToString(int operator) {
        String stringValue = null;

        if (operator == OPERATOR_SIMILAR) {
            stringValue = "%";
        } else if (operator == OPERATOR_NOT_SIMILAR) {
            stringValue = "!%";
        } else if (operator == OPERATOR_AROUND) {
            stringValue = "~";
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

        if (operator.equals("%")) {
            numericValue = OPERATOR_SIMILAR;
        } else if (operator.equals("!%")) {
            numericValue = OPERATOR_NOT_SIMILAR;
        } else if (operator.equals("~")) {
            numericValue = OPERATOR_AROUND;
        } //--- figure out the operator

        return numericValue;
    } //--- stringToDataType

    //-----------------------------------------------------------------------------
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFieldName() {
        return this.fieldName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getID() {
        return "[" + fieldName + "|" + getOperatorAsString() + "|" + value +
        "]";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getOperator() {
        return this.operator;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getOperatorAsString() {
        return operatorToString(this.operator);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TraitValue getValue() {
        return this.value;
    }
} //--- SimilarityCriterion
