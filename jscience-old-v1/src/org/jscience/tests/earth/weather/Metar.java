package org.jscience.tests.earth.weather;

import org.jscience.earth.weather.metar.MetarObservation;
import org.jscience.earth.weather.metar.MetarObservationParser;

import java.io.IOException;


//this code is rebundled after:
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Metar extends Object {
    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static void main(String[] arg) throws IOException {
        MetarObservationParser parser = new MetarObservationParser();

        parser.setCode("EGCC");

        MetarObservation ob = parser.retrieve();
        System.out.println("Remote Stuff:");
        System.out.println(ob);
    }
}
