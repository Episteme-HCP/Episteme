package mypackage.validators;

/**
 * This class is the FieldValidator implementation for the validation of
 * Registration Form's "name", "country", "city" and "street" field.
 */
public class ForNameCountryCityStreet implements org.jscience.sociology.forms.FieldValidator {
    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getErrorMessage(String value) {
        TextRestrictions textRestrictions = new TextRestrictions();
        textRestrictions.setNoSpace(false);
        textRestrictions.setCharAtLeast(2);
        textRestrictions.setCharMax(40);
        textRestrictions.setProhibitedChars(new char[] { '\'', '\"' });

        return textRestrictions.getErrorMessage(value);
    }
}
