package org.jscience.psychology;

import org.jscience.util.Commented;
import org.jscience.util.Named;


//useful to build up an ethogram
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
  */

//also see http://en.wikipedia.org/wiki/Hand_gesture
//http://en.wikipedia.org/wiki/Gesture

public class Behavior extends Object implements Named, Commented {
    //behaviors are either on instinct, self or social
    //mutually exclusive
    /** DOCUMENT ME! */
    public final static int UNKNOWN = 0;

    /** DOCUMENT ME! */
    public final static int REFLEX = 1;

    /** DOCUMENT ME! */
    public final static int SELF = 2;

    /** DOCUMENT ME! */
    public final static int SOCIAL = 3;

    //attitude: modulates behavior: example careful, aggressive, afraid
    //this internal state is usually displayed for social species on the face, and is called emotion
    //use the emotion constants from PsychologyConstants
    //instinct (such as eyes closing)
    /** DOCUMENT ME! */
    public final static Behavior BREATH = new Behavior("Breath", Behavior.REFLEX);

    /** DOCUMENT ME! */
    public final static Behavior MUTATE = new Behavior("Mutate", Behavior.REFLEX); //how to become a butterfly

    //Human reflexes
    //Reflex actions seen in adult humans include:
    //Accommodation reflex
    //Achilles reflex
    //Anocutaneous reflex
    //Babinski reflex
    //Biceps stretch reflex
    //Brachioradialis reflex
    //Crossed extensor reflex
    //Mammalian diving reflex
    //Gag reflex
    //Gastroc-Soleus reflex
    //H-reflex
    //Patellar reflex (knee-jerk reflex)
    //Photic sneeze reflex
    //Pupillary reflex
    //Quadriceps reflex
    //Salivation
    //Scratch reflex
    //Sneeze
    //Tendon reflex
    //Triceps stretch reflex
    //Vestibulo-ocular reflex
    //Withdrawal reflex
    //Processes such as breathing, digestion and the maintenance of the heartbeat can also be regarded as reflex actions, according to some definitions of the term.
    //Newborn babies have a number of other reflexes which are not seen in adults, including:
    //suckling
    //hand-to-mouth reflex
    //Moro reflex, also known as the startle reflex
    //grasp reflex
    //Asymmetrical tonic neck reflex (ATNR)
    //Symmetrical tonic neck reflex (STNR)
    //Tonic labyrinthine reflex (TLR)
    //self, instincts/innate:
    /** DOCUMENT ME! */
    public final static Behavior NONE = new Behavior("None", Behavior.SELF); //inactive, drowsy

    /** DOCUMENT ME! */
    public final static Behavior CLEAN = new Behavior("Clean", Behavior.SELF);

    /** DOCUMENT ME! */
    public final static Behavior HEAL = new Behavior("Heal", Behavior.SELF);

    /** DOCUMENT ME! */
    public final static Behavior REPRODUCE = new Behavior("Reproduce",
            Behavior.SELF); //or mate

    /** DOCUMENT ME! */
    public final static Behavior NEST_MAKING = new Behavior("Make a nest",
            Behavior.SELF);

    /** DOCUMENT ME! */
    public final static Behavior FIGHT = new Behavior("Fight", Behavior.SELF); //not to feed, fight or flight against enemy, a response to a hunt or an agression

    /** DOCUMENT ME! */
    public final static Behavior SLEEP = new Behavior("Sleep", Behavior.SELF);

    /** DOCUMENT ME! */
    public final static Behavior HUNT = new Behavior("Hunt", Behavior.SELF); //includes stalking, hiding ; sometimes collective behavior

    /** DOCUMENT ME! */
    public final static Behavior FEED = new Behavior("Feed", Behavior.SELF);

    /** DOCUMENT ME! */
    public final static Behavior DEFECATE = new Behavior("Defecate",
            Behavior.SELF);

    /** DOCUMENT ME! */
    public final static Behavior OBSERVE = new Behavior("Observe", Behavior.SELF); //generally with head moving, also accounts for hearing, sniffing the air

    /** DOCUMENT ME! */
    public final static Behavior MOVE = new Behavior("Move", Behavior.SELF); //go to somewhere else, includes hunt or move away (may be we need something different for flight or avoid) or gathering or jump, crawl, run, walk, swim, fly...

    /** DOCUMENT ME! */
    public final static Behavior WAVE = new Behavior("Wave", Behavior.SELF); //don't move but do some gesture or change facial expression

    /** DOCUMENT ME! */
    public final static Behavior MAKE_NOISE = new Behavior("Make some noise",
            Behavior.SELF); //verbal gesture, sing, peck, etc

    /** DOCUMENT ME! */
    public final static Behavior RELEASE = new Behavior("Release", Behavior.SELF);

    /** DOCUMENT ME! */
    public final static Behavior HOLD = new Behavior("Hold", Behavior.SELF);

    //the following may also be considered as activities:
    //self, higher order, exhibited only by some species:
    /** DOCUMENT ME! */
    public final static Behavior TOOLMAKING = new Behavior("Make a tool",
            Behavior.SELF);

    /** DOCUMENT ME! */
    public final static Behavior TOOLUSING = new Behavior("Use a tool",
            Behavior.SELF); //like it is expected to be used

    //self, task oriented: cut, burn, carry, explore, protect one's territory... usually species dependant, sometimes using a tool
    /** DOCUMENT ME! */
    public final static Behavior CUT = new Behavior("Cut", Behavior.SELF);

    /** DOCUMENT ME! */
    public final static Behavior BURN = new Behavior("Burn", Behavior.SELF); // can also be done with acid, for example by ants

    /** DOCUMENT ME! */
    public final static Behavior CARRY = new Behavior("Carry", Behavior.SELF);

    /** DOCUMENT ME! */
    public final static Behavior EXPLORE = new Behavior("Explore", Behavior.SELF); //includes moving around or touching something unkwnown, map territory which is not "going to" but rather "moving around" a central place

    //self, higest order, human only:
    /** DOCUMENT ME! */
    public final static Behavior READ = new Behavior("Read", Behavior.SOCIAL);

    /** DOCUMENT ME! */
    public final static Behavior WRITE = new Behavior("Write", Behavior.SOCIAL);

    //list is almost unlimited : pray, study, drive...
    //one could argue about rather abstract behaviors to be included here: love, adapt
    //social, instincts/innate:
    /** DOCUMENT ME! */
    public final static Behavior LEK = new Behavior("Lek", Behavior.SOCIAL); //rut, etc

    /** DOCUMENT ME! */
    public final static Behavior GROOMING = new Behavior("Grooming",
            Behavior.SOCIAL);

    /** DOCUMENT ME! */
    public final static Behavior COMMUNICATE = new Behavior("Communicate",
            Behavior.SOCIAL); //gesture or verbal or sight, for example bee dance

    /** DOCUMENT ME! */
    public final static Behavior IMITATE = new Behavior("Imitate",
            Behavior.SOCIAL);

    /** DOCUMENT ME! */
    public final static Behavior PLAY = new Behavior("Play", Behavior.SOCIAL); //includes pretend, act

    /** DOCUMENT ME! */
    public final static Behavior PARENTAL_CARE = new Behavior("Parental care",
            Behavior.SOCIAL); //care about others and especially youngs, raise brood, extended grooming, may be confused

    /** DOCUMENT ME! */
    public final static Behavior ALERT = new Behavior("Alert", Behavior.SOCIAL);

    /** DOCUMENT ME! */
    public final static Behavior COOPERATE = new Behavior("Cooperate",
            Behavior.SOCIAL); //towards a common goal which has to be specified apart

    /** DOCUMENT ME! */
    public final static Behavior AGRESSION = new Behavior("Agression",
            Behavior.SOCIAL); //perhpas we need something different for berserk, panic attack (and even something different for panic itself)

    //the following may also be considered as activities:
    //Animals which have passed the mirror test are Common Chimpanzees, Bonobos, orangutans, dolphins, pigeons and humans. //Surprisingly, gorillas have not passed the test, although at least one specific gorilla, Koko, has passed the test
    //social, higher order, exhibited only by some species, usually needs a notion of self:
    /** DOCUMENT ME! */
    public final static Behavior ENTERTAIN = new Behavior("Entertain",
            Behavior.SOCIAL);

    /** DOCUMENT ME! */
    public final static Behavior POINT = new Behavior("Point", Behavior.SOCIAL); //show something

    /** DOCUMENT ME! */
    public final static Behavior EXCHANGE = new Behavior("Exchange",
            Behavior.SOCIAL); //bargain, give (yet expecting return), etc... perhaps different from sharing

    //social, higest order, human only:
    /** DOCUMENT ME! */
    public final static Behavior SPORT = new Behavior("Sport", Behavior.SOCIAL); //practicing a sport

    //list is almost unlimited
    //one could argue about rather abstract behaviors to be included here
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private int kind;

    /** DOCUMENT ME! */
    private String comments;

    /** DOCUMENT ME! */
    private int attitude; //modulates behavior: example careful, aggressive, afraid... and this gives also the notion of emergency which makes the difference between walking and running

/**
     * Creates a new Behavior object.
     *
     * @param name DOCUMENT ME!
     * @param kind DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Behavior(String name, int kind) {
        if ((name != null) && (name.length() > 0)) {
            this.name = name;
            this.kind = kind;
            this.attitude = UNKNOWN;
            this.comments = new String();
        } else {
            throw new IllegalArgumentException(
                "The Behavior constructor can't have null or empty arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getKind() {
        return kind;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getAttitude() {
        return attitude;
    }

    /**
     * DOCUMENT ME!
     *
     * @param attitude DOCUMENT ME!
     */

    //multiple behaviors triggering at the same time may produce conflicting emotions
    public void setAttitude(int attitude) {
        this.attitude = attitude;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getComments() {
        return comments;
    }

    /**
     * DOCUMENT ME!
     *
     * @param comments DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setComments(String comments) {
        if (comments != null) {
            this.comments = comments;
        } else {
            throw new IllegalArgumentException("You can't set a null comment.");
        }
    }

    //may be we should provide support for the sartDate and endDate for which this behavior is activated
}
