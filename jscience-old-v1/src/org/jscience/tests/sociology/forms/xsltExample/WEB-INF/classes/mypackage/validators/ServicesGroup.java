package mypackage.validators;

import java.util.Hashtable;


/**
 * This class is the GroupValidator implementation for the validation in
 * group of "totalService", "services", "basicService", "extendedService", and
 * "specialService" fields of Questionnaire Form.
 */
public class ServicesGroup implements org.jscience.sociology.forms.GroupValidator {
    /**
     * DOCUMENT ME!
     *
     * @param groupErrorMessage DOCUMENT ME!
     */
    public void setGroupErrorMessage(String groupErrorMessage) {
        // empty implementation
    }

    /**
     * DOCUMENT ME!
     *
     * @param nameValuePairs DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Hashtable getErrorMessages(Hashtable nameValuePairs) {
        int totalService = convertToInt((String[]) nameValuePairs.get(
                    "totalService"));
        int basicService = convertToInt((String[]) nameValuePairs.get(
                    "basicService"));
        int extendedService = convertToInt((String[]) nameValuePairs.get(
                    "extendedService"));
        int specialService = convertToInt((String[]) nameValuePairs.get(
                    "specialService"));
        String[] services = (String[]) (nameValuePairs.get("services"));

        Hashtable toReturn = new Hashtable();

        // if the user chooses a service, he must also give
        // information on - how many years he got that particular service
        for (int i = 0; i < services.length; i++) {
            if (services[i].equals("Basic Service") && (basicService == 0)) {
                toReturn.put("basicService", "you must fill here");
            } else if (services[i].equals("Extended Service") &&
                    (extendedService == 0)) {
                toReturn.put("extendedService", "you must fill here");
            } else if (services[i].equals("Special Service") &&
                    (specialService == 0)) {
                toReturn.put("specialService", "you must fill here");
            }
        }

        // the sum of all services should equal the total number of years
        if (totalService < (basicService + extendedService + specialService)) {
            toReturn.put("totalService", "there should be more years here");
        } else if (totalService > (basicService + extendedService +
                specialService)) {
            if (basicService > 0) {
                toReturn.put("basicService", "check number of years here");
            }

            if (extendedService > 0) {
                toReturn.put("extendedService", "check number of years here");
            }

            if (specialService > 0) {
                toReturn.put("specialService", "check number of years here");
            }
        }

        if (toReturn.size() == 0) {
            return null;
        } else {
            return toReturn;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param parameter DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int convertToInt(String[] parameter) {
        if (parameter[0].equals("")) {
            return 0;
        }

        String param = parameter[0].substring(0, 1);

        return Integer.parseInt(param);
    }
}
