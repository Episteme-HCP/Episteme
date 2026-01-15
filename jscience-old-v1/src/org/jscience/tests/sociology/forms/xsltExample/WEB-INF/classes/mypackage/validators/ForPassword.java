package mypackage.validators;

/**
 * This class is the FieldValidator implementation for the validation of
 * Registration Form's "password" and "passwordAgain" field.
 */
public class ForPassword implements org.jscience.sociology.forms.FieldValidator {
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
        textRestrictions.setCharAtLeast(6);
        textRestrictions.setCharMax(30);
        textRestrictions.setProhibitedChars(new char[] { '\'', '\"' });

        return textRestrictions.getErrorMessage(value);
    }
}
