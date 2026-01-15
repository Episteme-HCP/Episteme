package org.jscience.earth.weather.cityforecast;


//this code is rebundled after:
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class CityForecastEntry {
    /** DOCUMENT ME! */
    private String time = null;

    /** DOCUMENT ME! */
    private String prediction = null;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPrediction() {
        return prediction;
    }

    /**
     * DOCUMENT ME!
     *
     * @param newPrediction DOCUMENT ME!
     */
    public void setPrediction(String newPrediction) {
        prediction = newPrediction;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTime() {
        return time;
    }

    /**
     * DOCUMENT ME!
     *
     * @param newTime DOCUMENT ME!
     */
    public void setTime(String newTime) {
        time = newTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Forecast: " + time + " - " + prediction;
    }
}
