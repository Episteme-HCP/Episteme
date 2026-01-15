package mypackage.validators;

/**
 * This class is the FieldValidator implementation for the validation of
 * Registration Form's "login" field.
 */
public class ForLogin implements org.jscience.sociology.forms.FieldValidator {
    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getErrorMessage(String value) {
        // this is where you would communicate with DataBase
        if (value.equals("tiger") || value.equals("scott") ||
                value.equals("blah")) {
            return "sorry, this login already exist";
        }

        // checking the text
        TextRestrictions textRestrictions = new TextRestrictions();
        textRestrictions.setNoSpace(true);
        textRestrictions.setCharAtLeast(6);
        textRestrictions.setCharMax(15);
        textRestrictions.setProhibitedChars(new char[] { '\'', '\"', '&', '#' });

        return textRestrictions.getErrorMessage(value);
    }
}
