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
