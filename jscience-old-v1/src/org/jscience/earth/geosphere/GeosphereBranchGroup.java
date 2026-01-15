package org.jscience.earth.geosphere;

/**
 *
 * @version 1.0
 * @author Silvere Martin-Michiellot
 */

import org.jscience.astronomy.StarSystem;

import javax.media.j3d.Behavior;
import javax.media.j3d.BranchGroup;
import javax.vecmath.Vector3d;
import java.util.Enumeration;
import java.util.Set;

//you should use different geospheres if you simulate a world at different eras as geography as well as planetary parameters evolve from time to time

public class GeosphereBranchGroup extends BranchGroup {

    public final static int SCHEDULER = 1;

    public final static int UNKNOWN = 0;

    //get climatic conditions in a given area
    //multiple can happen at the same time
    public final static int EARTHQUAKE = 1;
    public final static int ERUPTION = 2;//usually found with earthquake, fire
    public final static int FIRE = 4;
    public final static int SNOWFALL = 8;//usually found with lightning, clouds, daylight fading
    public final static int RAINFALL = 16;//usually found with  lightning, clouds, daylight fading
    public final static int ICEFALL = 32;//usually found with lightning, clouds, daylight fading
    public final static int TORNADO = 64;//usually found with found with rainfall
    public final static int TYPHOON = 128;//usually found with found with rainfall
    public final static int AVALANCHE = 256;
    public final static int FLOW = 512;
    public final static int WAVE = 1024;
    public final static int COLLAPSE = 2048;//usually found with earthquake
    public final static int ECLIPSE = 4096;
    public final static int SMOKE = 8192;
    public final static int UNDERWATER_ERUPTION = 16384;
    public final static int LAND_SLIDE = 32768;
    public final static int WIND = 65536;
    public final static int RAINBOW = 65536 * 2;
    public final static int FOG = 65536 * 4;
    public final static int METEOR = 65536 * 8;
    public final static int LIGHTNING = 65536 * 16;
    public final static int HAIL = 65536 * 32;
    public final static int CLOUD = 65536 * 64;
    public final static int AURORA = 65536 * 128;

    public final static int OTHER = 65536 * 256;//user supplied class

    //and land features
    //multiple can happen at the same time
    public final static int UNDERWATER_GAZ_SOURCE = 1;
    public final static int HYDROTHERMAL_VENT = 2;
    public final static int ICEBERG = 4;
    public final static int GAZ_SOURCE = 8;
    public final static int LAKE = 16;
    public final static int RIVER = 32;
    public final static int SEA = 64;
    public final static int SOIL = 128;

    //real time weather (fog, rain, snow, clouds, ice, tornado, lightning)

    //real time earthquakes
    //real time avalanches
    //real time eruptions
    //real time fire
    //real time mud river and inondations

    //real time sea weather (tsunami)

    //store virtual planet from which we see things
    //we should not render moons of other planets or moons as they are normally not seen although we do

    //also contains all the "things"

    //shadow over rings

    //eclipse rendering

    public void setStarSystem(StarSystem system) {

        this.starSystem = system;

    }

    public setHomePlanet() {
        XXX
    }

    //get realtime data or use simulator using planetary data (sun illumination, seasons)

    //coallesce events into bigger events: exemple tempest

    //pass events to creature to update

    //the clock shared by all AstralBodies in seconds (system time/1000)

    public long getUniversalTime() {

        return System.currentTimeMillis() / 1000;

    }

    //return the land characterization as a Vector of int
    //if you want to get only the different land characters for this ecosystem but not the bounds
    public Enumeration getLandCharacters() {

        landCharacterizations.keys();

    }

    //return the land characterization as a Hashtable of LandCharacterization
    public Set getLandCharacterizations() {

        return landCharacterizations;

    }

    //divides the bounds covered by this branchgroup into smaller areas each of which with one and only one land characterization
    //parameter is a Hashtable of LandCharacterizations
    //union of bounds must correspond to the branchgroup bounds
    public void setLandCharacterizations(Set landCharacterizations) {

        XXX check, and
        tweak at
        runtime
        this.landCharacterizations = landCharacterizations;

    }

    //in a given radius
    public int getClimaticConditionsReport(Vector3d position, float radius) {

        return XXX;

    }

    public Vector3d getClimaticConditions(Vector3d position, float radius) {

        return XXX;

    }

    public int getLandFeaturesReport(Vector3d position, float radius) {

        return XXX;

    }

    public Vector3d getLandFeatures(Vector3d position, float radius) {

        return XXX;

    }

    public Vector3d getDaylight(Vector3d position) {

        return XXX;

    }

    //use a UNIQUE FAKE serial number and XML description
    public long computeVirtualSerialNumber() {

        XXX

    }

    public void addThing() {

        XXX

    }

    public void removeThing() {

        XXX

    }

    public BranchGroup getEcosystemBranchGroup() {

        return XXX;

    }

    public Behavior getScheduler() {

        return XXX;

    }

    //Scheduler should kill all old events;

    //Scheduler should grab new Events from generators

}
