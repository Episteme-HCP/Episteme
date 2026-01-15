package mypackage.validators;

/**
 * This class is the FieldValidator implementation for the validation of
 * Registration Form's "postCode" field.
 */
public class ForPostalCode implements org.jscience.sociology.forms.FieldValidator {
    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getErrorMessage(String value) {
        TextRestrictions textRestrictions = new TextRestrictions();
        textRestrictions.setCharAtLeast(3);
        textRestrictions.setCharMax(7);
        textRestrictions.setProhibitedChars(new char[] { '\'', '\"', '&', '#' });

        return textRestrictions.getErrorMessage(value);
    }
}
