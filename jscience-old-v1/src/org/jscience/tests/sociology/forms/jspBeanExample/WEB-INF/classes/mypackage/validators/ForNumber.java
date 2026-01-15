package mypackage.validators;

/**
 * This class is the FieldValidator implementation for the validation of
 * Registration Form's "number" field.
 */
public class ForNumber implements org.jscience.sociology.forms.FieldValidator {
    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getErrorMessage(String value) {
        TextRestrictions textRestrictions = new TextRestrictions();
        textRestrictions.setNoSpace(true);
        textRestrictions.setCharAtLeast(1);
        textRestrictions.setCharMax(7);
        textRestrictions.setNumbersOnly(true);

        return textRestrictions.getErrorMessage(value);
    }
}
