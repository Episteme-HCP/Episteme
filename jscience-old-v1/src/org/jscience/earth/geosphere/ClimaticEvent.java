package org.jscience.earth.geosphere;

/**
 */
import javax.media.j3d.*;


//this is a system for events on a human timescale, NOT for very fast (sub second events) and NOT for very slow (geological scale) events (plate tectonics...)
//we should include here Ball ligtning, gravitational waves, electromagnetic anomaly
//rain should include wind and falling insects or frogs, could also be colored (black, red)
//tsunami should account for waves coming up through rivers
//mist should also be accounted for
//as well as dust storm 
//brinicle
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public abstract class ClimaticEvent extends Behavior {
    //mutually exclusive
    /** DOCUMENT ME! */
    public final static int BARELY_NOTICABLE = 0; //for rain: some humidity

    /** DOCUMENT ME! */
    public final static int LIGHT_EVENT = 1; //for rain: some light rain

    /** DOCUMENT ME! */
    public final static int NORMAL_EVENT = 2; //for rain: normal rain for which we feel like saying "there is rain"

    /** DOCUMENT ME! */
    public final static int HEAVY_EVENT = 3; //for rain: heavy rain

    /** DOCUMENT ME! */
    public final static int MAJOR_EVENT = 4; //for rain: storm

    /** DOCUMENT ME! */
    public final static int HISTORICAL_EVENT = 5; //for rain: tempest

    /** DOCUMENT ME! */
    private long time;

    /** DOCUMENT ME! */
    private Transform3D position;

    /** DOCUMENT ME! */
    private Bounds bounds; //not the graphical bounds but the geographical bounds that may be affected by the events consequences

    /** DOCUMENT ME! */
    private long duration;

    /** DOCUMENT ME! */
    private int strength;

    /** DOCUMENT ME! */
    private Group effect;

    /** DOCUMENT ME! */
    private GeosphereBranchGroup geosphereBranchGroup; //the branchGroup this Event is added to

    //you may prefer this constructor and subclass getGroup() to design your effect
    /**
     * Creates a new ClimaticEvent object.
     *
     * @param time DOCUMENT ME!
     * @param position DOCUMENT ME!
     * @param bounds DOCUMENT ME!
     * @param duration DOCUMENT ME!
     * @param strength DOCUMENT ME!
     * @param geosphereBranchGroup DOCUMENT ME!
     */
    public ClimaticEvent(long time, Transform3D position, Bounds bounds,
        long duration, int strength, GeosphereBranchGroup geosphereBranchGroup) {
        this.time = time;
        this.position = position;
        this.bounds = bounds;
        this.duration = duration;
        this.strength = strength;
        this.geosphereBranchGroup = geosphereBranchGroup;
    }

/**
     * Creates a new ClimaticEvent object.
     *
     * @param time                 DOCUMENT ME!
     * @param position             DOCUMENT ME!
     * @param bounds               DOCUMENT ME!
     * @param duration             DOCUMENT ME!
     * @param strength             DOCUMENT ME!
     * @param effect               DOCUMENT ME!
     * @param geosphereBranchGroup DOCUMENT ME!
     */
    public ClimaticEvent(long time, Transform3D position, Bounds bounds,
        long duration, int strength, Group effect,
        GeosphereBranchGroup geosphereBranchGroup) {
        this.time = time;
        this.position = position;
        this.bounds = bounds;
        this.duration = duration;
        this.strength = strength;
        this.effect = effect;
        this.geosphereBranchGroup = geosphereBranchGroup;
    }

    //occurs at a given time
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long getTime() {
        return time;
    }

    //given place
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Transform3D getPosition() {
        return position;
    }

    //given area of effect
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Bounds getGeographyBounds() {
        return bounds;
    }

    //has some duration
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long getDuration() {
        return duration;
    }

    //has a strength
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getStrength() {
        return strength;
    }

    //shows something
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Group getGroup() {
        return effect;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GeosphereBranchGroup getGeosphereBranchGroup() {
        return geosphereBranchGroup;
    }

    //from the behavior class
    /**
     * DOCUMENT ME!
     */
    public void initialize() {
        wakeupOn(new WakeupOnBehaviorPost(getGeosphereBranchGroup()
                                              .getScheduler(),
                GeosphereBranchGroup.SCHEDULER));
        setEnable(true);
    }

    //do something with the Group
    /**
     * DOCUMENT ME!
     *
     * @param criteria DOCUMENT ME!
     */
    public void processStimulus(java.util.Enumeration criteria) {
        //do something to children of Group
        //there should be no Event resheduling as they are meant to happen once (at a specific time)
        if (getGeosphereBranchGroup().getGeologicalTime() > (time + duration)) {
            //isPassive ???
            wakeupOn(new WakeupOnElapsedFrames(0, false));
        } else {
            setEnable(false);
        }
    }
}
