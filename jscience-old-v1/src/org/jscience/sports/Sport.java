package org.jscience.sports;

import org.jscience.util.Named;

import java.util.Date;


/**
 * A class representing a sport.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//synthetic description is as follow:
//football, rugby BALL + FOOT + HEAD + TEAM + GROUND + HUMANS + ACTIVE
//tennis BALL + TOOL + GROUND + HUMANS + ACTIVE (sometimes there is also TEAM)
//baseball BALL + TOOL + GROUND + TEAM + HUMANS + ACTIVE
//hockey BALL + TOOL + TEAM + ICE + HUMANS + ACTIVE
//golf BALL + TOOL + GROUND + HUMANS + TACTIC
//snooker BALL + TOOL + HUMANS + TACTIC
//fishing TOOL + HUMANS + ANIMALS + TACTIC (sometimes there is also ACTIVE depending on what you fish)
//boxing HAND + GROUND + FIGHT
//horse, dog race FOOT + GROUND + ANIMALS + RACE
//human 100, 200, 400, 1500, 3000, meters or marathon FOOT + GROUND + HUMANS + RACE
//gun, arc ARROW + TOOL + GROUND + TACTIC
//dart ARROW + HAND + GROUND + TACTIC
//but we may consider many others like curling or weight lifting
public class Sport extends Object implements Named {
    //something to move
    /** DOCUMENT ME! */
    public final static int BALL = 1; //1<<0;

    /** DOCUMENT ME! */
    public final static int ARROW = 2; //1<<1;

    /** DOCUMENT ME! */
    public final static int VEHICLE = 4; //1<<2;

    /** DOCUMENT ME! */
    public final static int WEIGHT = 8;

    //what is used to move the thing
    /** DOCUMENT ME! */
    public final static int TOOL = 16;

    /** DOCUMENT ME! */
    public final static int FOOT = 32;

    /** DOCUMENT ME! */
    public final static int HAND = 64;

    /** DOCUMENT ME! */
    public final static int HEAD = 128;

    //alone or in a team
    /** DOCUMENT ME! */
    public final static int TEAM = 256;

    //where it is played
    /** DOCUMENT ME! */
    public final static int WATER = 512;

    /** DOCUMENT ME! */
    public final static int AIR = 1024;

    /** DOCUMENT ME! */
    public final static int ICE = 2048;

    /** DOCUMENT ME! */
    public final static int SNOW = 4096;

    /** DOCUMENT ME! */
    public final static int GROUND = 8192; //grass or earth soil

    //who is playing
    /** DOCUMENT ME! */
    public final static int HUMANS = 16384;

    /** DOCUMENT ME! */
    public final static int ANIMALS = 32768;

    //what is the kind of play
    /** DOCUMENT ME! */
    public final static int RACE = 65536;

    /** DOCUMENT ME! */
    public final static int FIGHT = 131072;

    /** DOCUMENT ME! */
    public final static int ACTIVE = 262144;

    /** DOCUMENT ME! */
    public final static int TACTIC = 524288; //you do not need a specific constitution

    /** DOCUMENT ME! */
    private String name; //the given name

    /** DOCUMENT ME! */
    private int description; //a synthetic way to describe sports

    /** DOCUMENT ME! */
    private String[] rules; //the tules as an array of string

    /** DOCUMENT ME! */
    private Date date; //the date at which this sport appeared, or this rules were applied

/**
     * Creates a new Sport object.
     *
     * @param name        DOCUMENT ME!
     * @param description DOCUMENT ME!
     * @param rules       DOCUMENT ME!
     * @param date        DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Sport(String name, int description, String[] rules, Date date) {
        if ((name != null) && (rules != null) && (date != null)) {
            this.name = name;
            this.description = description;
            this.rules = rules;
            this.date = date;
        } else {
            throw new IllegalArgumentException(
                "The Sport constructor can't have null arguments.");
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
    public int getDescription() {
        return description;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String[] getRules() {
        return rules;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getDate() {
        return date;
    }

    //public abstract Object getWinner(Match match);
    //returns either an Individual or a Team depending on the scores (that must be set) and a method to compute a winner
    //from the rules in the sport
}
