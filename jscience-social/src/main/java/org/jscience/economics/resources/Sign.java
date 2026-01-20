package org.jscience.economics.resources;

import java.util.Locale;


/**
 * A class representing an intelligent made sign.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//describes the fact that there are human readable signs associated to this thing
//the sign is then converted using localized knowledge when actually displaying the sign
//this could also be an audible sign, repeating over again at a location, (for example street crossing)
//you could provide sublasses: AudibleSign and VisualSign
public interface Sign {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Locale[] getSupportedLocales();

    //what the sign means as a string, represented in the given locale
    /**
     * DOCUMENT ME!
     *
     * @param locale DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSignMeaning(Locale locale);
}
