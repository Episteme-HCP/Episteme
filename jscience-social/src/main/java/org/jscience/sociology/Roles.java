package org.jscience.sociology;

/**
 * A class defining some basic roles.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//below are personal choices, you may not agree with
public class Roles extends Object {
    //health
    /** DOCUMENT ME! */
    public final static Role PATIENT = new Role("Patient", Role.CLIENT);

    /** DOCUMENT ME! */
    public final static Role DOCTOR = new Role("Doctor", Role.SERVER);

    //relations
    /** DOCUMENT ME! */
    public final static Role CHILD = new Role("Child", Role.CLIENT);

    /** DOCUMENT ME! */
    public final static Role PARENT = new Role("Parent", Role.SERVER);

    /** DOCUMENT ME! */
    public final static Role NEIGHBOR = new Role("Neighbor", Role.OBSERVER);

    /** DOCUMENT ME! */
    public final static Role STRANGER = new Role("Stranger", Role.OBSERVER);

    /** DOCUMENT ME! */
    public final static Role FRIEND = new Role("Friend", Role.SUPERVISOR);

    /** DOCUMENT ME! */
    public final static Role ENEMY = new Role("Enemy", Role.OBSERVER);

    //street conflicts
    /** DOCUMENT ME! */
    public final static Role VICTIM = new Role("Victim", Role.CLIENT);

    /** DOCUMENT ME! */
    public final static Role FELON = new Role("Felon", Role.SERVER);

    /** DOCUMENT ME! */
    public final static Role POLICEMAN = new Role("Policeman", Role.SUPERVISOR);

    /** DOCUMENT ME! */
    public final static Role SUSPECT = new Role("Suspect", Role.OBSERVER);

    /** DOCUMENT ME! */
    public final static Role PRISONER = new Role("Prisoner", Role.OBSERVER);

    //justice
    /** DOCUMENT ME! */
    public final static Role PROSECUTOR = new Role("Prosecutor", Role.CLIENT);

    /** DOCUMENT ME! */
    public final static Role DEFENDER = new Role("Defender", Role.CLIENT);

    /** DOCUMENT ME! */
    public final static Role JUDGE = new Role("Judge", Role.SUPERVISOR); //mediator

    /** DOCUMENT ME! */
    public final static Role JURY = new Role("Jury", Role.SERVER);

    //school
    /** DOCUMENT ME! */
    public final static Role STUDENT = new Role("Student", Role.CLIENT);

    /** DOCUMENT ME! */
    public final static Role TEACHER = new Role("Teacher", Role.SERVER);

    /** DOCUMENT ME! */
    public final static Role HEADMASTER = new Role("Headmaster", Role.SUPERVISOR);

    //market
    /** DOCUMENT ME! */
    public final static Role CONSUMER = new Role("Consumer", Role.CLIENT);

    /** DOCUMENT ME! */
    public final static Role SALESMAN = new Role("Salesman", Role.SERVER); //also see worker

    /** DOCUMENT ME! */
    public final static Role MANAGER = new Role("Manager", Role.SUPERVISOR); //also see boss

    //business
    /** DOCUMENT ME! */
    public final static Role WORKER = new Role("Worker", Role.SERVER);

    /** DOCUMENT ME! */
    public final static Role BOSS = new Role("Boss", Role.SUPERVISOR); //supervisor, administrator, manager...

    /** DOCUMENT ME! */
    public final static Role UNEMPLOYED = new Role("Unemployed", Role.OBSERVER);

    //antique business
    /** DOCUMENT ME! */
    public final static Role SLAVE = new Role("Slave", Role.SERVER);

    /** DOCUMENT ME! */
    public final static Role MASTER = new Role("Master", Role.CLIENT); //chief, king

    //politics
    /** DOCUMENT ME! */
    public final static Role LEADER = new Role("Leader", Role.SUPERVISOR); //active, sadist

    /** DOCUMENT ME! */
    public final static Role FOLLOWER = new Role("Follow", Role.SERVER); //passive, masochist

    /** DOCUMENT ME! */
    public final static Role PEOPLE = new Role("People", Role.CLIENT); //passive, masochist

    //faith
    /** DOCUMENT ME! */
    public final static Role BELIEVER = new Role("Believer", Role.CLIENT);

    /** DOCUMENT ME! */
    public final static Role PRIEST = new Role("Priest", Role.SUPERVISOR);

    //games
    /** DOCUMENT ME! */
    public final static Role SUPPORTER = new Role("Supporter", Role.CLIENT);

    /** DOCUMENT ME! */
    public final static Role PLAYER = new Role("Player", Role.SERVER);

    /** DOCUMENT ME! */
    public final static Role REFEREE = new Role("Referee", Role.SUPERVISOR);

    //show
    /** DOCUMENT ME! */
    public final static Role ARTIST = new Role("Artist", Role.SERVER);

    /** DOCUMENT ME! */
    public final static Role AUDIENCE = new Role("Audience", Role.CLIENT);

    /** DOCUMENT ME! */
    public final static Role TECHNICIAN = new Role("Technician", Role.SERVER);

    /** DOCUMENT ME! */
    public final static Role PRODUCER = new Role("Producer", Role.SERVER);

    /** DOCUMENT ME! */
    public final static Role DIRECTOR = new Role("Director", Role.SUPERVISOR);

    /** DOCUMENT ME! */
    public final static Role SECURITY = new Role("Security", Role.SERVER);

    //hunt
    /** DOCUMENT ME! */
    public final static Role PREY = new Role("Prey", Role.SERVER);

    /** DOCUMENT ME! */
    public final static Role PREDATOR = new Role("Predator", Role.CLIENT);

    /** DOCUMENT ME! */
    public final static Role OPPORTUNIST = new Role("Opportunist", Role.OBSERVER);

    /** DOCUMENT ME! */
    public final static Role HELPER = new Role("Symbiot", Role.SERVER); //helps the hunt such as dogs tracking the trail
}
