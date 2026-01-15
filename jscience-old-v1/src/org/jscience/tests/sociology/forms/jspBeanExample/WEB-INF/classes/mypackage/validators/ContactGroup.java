package mypackage.validators;

import java.util.Hashtable;


/**
 * This class is the GroupValidator implementation for the validation in
 * group of "contact", "phone", and "contactTime" fields of Registration Form.
 */
public class ContactGroup implements org.jscience.sociology.forms.GroupValidator {
    /**
     * DOCUMENT ME!
     */
    private String groupErrorMessage;

    /**
     * DOCUMENT ME!
     *
     * @param groupErrorMessage DOCUMENT ME!
     */
    public void setGroupErrorMessage(String groupErrorMessage) {
        this.groupErrorMessage = groupErrorMessage;
    }

    /**
     * DOCUMENT ME!
     *
     * @param nameValuePairs DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Hashtable getErrorMessages(Hashtable nameValuePairs) {
        String contact = (String) (nameValuePairs.get("contact"));
        String phone = (String) (nameValuePairs.get("phone"));
        String[] contactTime = (String[]) (nameValuePairs.get("contactTime"));

        Hashtable toReturn = new Hashtable();

        // if the user wants to be contacted, he must also give
        // his phone number and his prefered time.
        if (contact.equals("yes")) {
            if (phone.equals("")) {
                toReturn.put("phone", groupErrorMessage);
            }

            if (contactTime[0].equals("")) {
                toReturn.put("contactTime", groupErrorMessage);
            }
        }

        if (toReturn.size() == 0) {
            return null;
        } else {
            return toReturn;
        }
    }
}
