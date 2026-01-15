package org.jscience.earth.geosphere.sky;

/**
 *
 * @version 1.0
 * @author Silvere Martin-Michiellot
 */

//may be we should rename this class to DaylightManager
//how do we manage night sky: background, direct use of astronomy package ?...

import javax.media.j3d.Light;
import javax.vecmath.Vector3d;

//Describes the actual daylight at a given point resulting from Suns and Moons
//this class is mainly here because we cannot simulate the light received from a "real" sun that would be millions of kilometers away
//this class is also used to compute and launch eclipse events

public class Daylight extends Object {

    private long time;
    private Vector3d position;
    private Light[] lights;

    //day influence and basic naive observer impression
    private float positionInCycle;//assuming there is a repeated cycle we could call day, and 0 value is for full night (midnight), 50 value for mid day (noon)

    //this describes the basic cycle for planets with a tilt different than 90 degrees, that are relatively close to the stars
    //and an orbital period (year) not synchronous with the rotation period (day)
    //if not, the position is either in premanent full day light, twilight or full night
    public final static int NIGHT_BEGINNING = 0;
    public final static int FULL_NIGHT = 1;
    public final static int NIGHT_END = 2;
    public final static int DAWN = 3;
    public final static int MORNING = 4;
    public final static int MID_DAY = 5;
    public final static int AFTERNOON = 6;
    public final static int TWILIGHT = 7;

    //consider seasons
    private float dayDuration;//in percent of the rotation period around its own axis, when illumination is superior to 50 percent between full day and full night

    //day duration as simple value
    public final static int INEXISTENT = 0;
    public final static int VERY_SHORT = 1;
    public final static int SHORT = 2;
    public final static int MIDCYCLE = 3;
    public final static int LONG = 4;
    public final static int VERY_LONG = 5;
    public final static int CONTINUOUS = 6;

    //consider distance from sun
    private float lightIntensity;//joule per square meter

    //as a simple value
    public final static int STAR = 0;//light from the suns is almost like light from the stars
    public final static int VERY_WEAK = 1;
    public final static int WEAK = 2;
    public final static int NORMAL = 3;//from a human life point of view
    public final static int STRONG = 4;
    public final static int VERY_STRONG = 5;
    public final static int UNBEARABLE = 6;

    public Daylight(long time, Vector3d position, Light[] lights) {

        this.time = time;
        this.position = position;
        this.lights = lights;

    }

    public long getTime() {

        return time;

    }

    public Vector3d getPosition() {

        return position;

    }

    //the lights coming from the stars or pseudo reflections from the moon around that planet
    //with their correct parameters
    public Light[] getLights() {

        return lights;

    }

    //the geosphereBranchGroup will automatically launch the corresponding event
    XXX

    public abstract boolean isStarEclipsing() {

    }

    //the geosphereBranchGroup will automatically launch the corresponding event
    XXX

    //counted only if there is some daylight ?????????
    //in a multiple star system, all stars must be hidden by a moon for the eclipse to occur
    public abstract boolean isMoonEclipsing() {

    }

    //the suns actually in the sky whether eclipsing or not (nothing to do with the star background)
    public abstract int getVisibleStars() {

    }

    //the moons actually in the sky whether eclipsing or not
    public abstract int getVisibleMoons() {

    }

    //as values
    public abstract float getPositionInCycle() {

    }

    public abstract float getDayDuration() {

    }

    public abstract float getLightIntensity() {

    }

    //as parameters
    public int getPositionInCycleParameter() {

        getPositionInCycle
        return

    }

    public int getDayDurationParameter() {

        getDayDuration
        return

    }

    public int getLightIntensityParameter() {

        getLightIntensity
        return

    }

    //as string
    public String getPositionInCycleName(int name) {

        String result;

        switch (name) {
            case NIGHT_BEGINNING:
                result = "Night beginning";
                break;
            case FULL_NIGHT:
                result = "Full night";
                break;
            case NIGHT_END:
                result = "Night end";
                break;
            case DAWN:
                result = "Dawn";
                break;
            case MORNING:
                result = "Morning";
                break;
            case MID_DAY:
                result = "Mid day";
                break;
            case AFTERNOON:
                result = "Afternoon";
                break;
            case TWILIGHT:
                result = "Twilight";
                break;
            default:
                result = null;
                break;
        }

        return result;

    }

    public String getDayDurationName(int name) {

        String result;

        switch (name) {
            case INEXISTENT:
                result = "Inexistent";
                break;
            case VERY_SHORT:
                result = "Very short";
                break;
            case SHORT:
                result = "Short";
                break;
            case MIDCYCLE:
                result = "Equinox";
                break;
            case LONG:
                result = "Long";
                break;
            case VERY_LONG:
                result = "Very long";
                break;
            case CONTINUOUS:
                result = "Continuous";
                break;
            default:
                result = null;
                break;
        }

        return result;

    }

    public String getLightIntensityName(int name) {

        String result;

        switch (name) {
            case STAR:
                result = "Starlike";
                break;
            case VERY_WEAK:
                result = "Very weak";
                break;
            case WEAK:
                result = "Weak";
                break;
            case NORMAL:
                result = "Normal";
                break;
            case STRONG:
                result = "Strong";
                break;
            case VERY_STRONG:
                result = "Very strong";
                break;
            case UNBEARABLE:
                result = "Unbearable";
                break;
            default:
                result = null;
                break;
        }

        return result;

    }

}
