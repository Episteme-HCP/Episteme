package org.jscience.tests.earth.weather;

import org.jscience.earth.weather.cityforecast.CityForecast;
import org.jscience.earth.weather.cityforecast.CityForecastParser;

import java.io.IOException;


//this code is rebundled after:
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class CityForecastTest extends Object {
    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static void main(String[] arg) throws IOException {
        CityForecastParser parser = new CityForecastParser();
        parser.setState("va");
        parser.setCity("washington_national_airport");

        CityForecast fc = parser.retrieve();
        System.out.println(fc);
    }
}
