package mypackage.validators;

/**
 * This class is the FieldValidator implementation for the validation of
 * Registration Form's "phone" field.
 */
public class ForPhone implements org.jscience.sociology.forms.FieldValidator {
    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getErrorMessage(String value) {
        TextRestrictions textRestrictions = new TextRestrictions();
        textRestrictions.setCharAtLeast(10);
        textRestrictions.setCharMax(15);
        textRestrictions.setNumbersOnly(true);

        return textRestrictions.getErrorMessage(value);
    }
}
