/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.social.sociology;

import java.io.Serializable;

/**
 * A utility class defining common archetypal social roles across various contexts.
 * These predefined roles serve as templates for creating specific role instances for individuals.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class Roles implements Serializable {

    private static final long serialVersionUID = 1L;

    private Roles() {
        // Prevent instantiation
    }

    // Health context
    /** Individual seeking medical care. */
    public final static Role PATIENT = new Role("Patient", Role.CLIENT);
    /** Individual providing medical care. */
    public final static Role DOCTOR = new Role("Doctor", Role.SERVER);

    // Relationship context
    /** Junior individual in a SociologicalFamily structure. */
    public final static Role CHILD = new Role("Child", Role.CLIENT);
    /** Senior/caregiver individual in a SociologicalFamily structure. */
    public final static Role PARENT = new Role("Parent", Role.SERVER);
    /** Nearby resident in a social environment. */
    public final static Role NEIGHBOR = new Role("Neighbor", Role.OBSERVER);
    /** Unfamiliar individual in a social context. */
    public final static Role STRANGER = new Role("Stranger", Role.OBSERVER);
    /** Peer in a voluntary social bond. */
    public final static Role FRIEND = new Role("Friend", Role.SUPERVISOR);
    /** Adversary in a social or physical conflict. */
    public final static Role ENEMY = new Role("Enemy", Role.OBSERVER);

    // Public safety / Conflict context
    /** Individual harmed during a conflict. */
    public final static Role VICTIM = new Role("Victim", Role.CLIENT);
    /** Individual initiating harm or law violation. */
    public final static Role FELON = new Role("Felon", Role.SERVER);
    /** Individual enforcing public order. */
    public final static Role POLICEMAN = new Role("Policeman", Role.SUPERVISOR);
    /** Potential perpetrator under investigation. */
    public final static Role SUSPECT = new Role("Suspect", Role.OBSERVER);
    /** Individual confined following a law violation. */
    public final static Role PRISONER = new Role("Prisoner", Role.OBSERVER);

    // Judicial context
    /** Legal advocate for the state or plaintiff. */
    public final static Role PROSECUTOR = new Role("Prosecutor", Role.CLIENT);
    /** Legal advocate for the defendant. */
    public final static Role DEFENDER = new Role("Defender", Role.CLIENT);
    /** Impartial mediator in a legal conflict. */
    public final static Role JUDGE = new Role("Judge", Role.SUPERVISOR);
    /** SociologicalGroup of peers providing a collective verdict. */
    public final static Role JURY = new Role("Jury", Role.SERVER);

    // Education context
    /** Individual receiving instruction. */
    public final static Role STUDENT = new Role("Student", Role.CLIENT);
    /** Individual providing instruction. */
    public final static Role TEACHER = new Role("Teacher", Role.SERVER);
    /** Individual managing an educational institution. */
    public final static Role HEADMASTER = new Role("Headmaster", Role.SUPERVISOR);

    // Market context
    /** Individual purchasing goods or services. */
    public final static Role CONSUMER = new Role("Consumer", Role.CLIENT);
    /** Individual selling goods or services. */
    public final static Role SALESMAN = new Role("Salesman", Role.SERVER);
    /** Individual managing commercial operations. */
    public final static Role MANAGER = new Role("Manager", Role.SUPERVISOR);

    // Business/Economy context
    /** Participant in productive labor. */
    public final static Role WORKER = new Role("Worker", Role.SERVER);
    /** Decision-maker in a productive hierarchy. */
    public final static Role BOSS = new Role("Boss", Role.SUPERVISOR);
    /** Individual active in society but without formal employment. */
    public final static Role UNEMPLOYED = new Role("Unemployed", Role.OBSERVER);

    // Historical context
    /** Individual in forced labor. */
    public final static Role SLAVE = new Role("Slave", Role.SERVER);
    /** Individual in possession of or power over others. */
    public final static Role MASTER = new Role("Master", Role.CLIENT);

    // Political context
    /** Formal or informal head of a political SociologicalGroup. */
    public final static Role LEADER = new Role("Leader", Role.SUPERVISOR);
    /** Individual adhering to a leader's direction. */
    public final static Role FOLLOWER = new Role("Follower", Role.SERVER);
    /** The general citizenry or collective mass. */
    public final static Role PEOPLE = new Role("People", Role.CLIENT);

    // Faith/Religious context
    /** Adherent to a belief system. */
    public final static Role BELIEVER = new Role("Believer", Role.CLIENT);
    /** Facilitator of religious rites. */
    public final static Role PRIEST = new Role("Priest", Role.SUPERVISOR);

    // Competition/Games context
    /** Passive participant supporting a side. */
    public final static Role SUPPORTER = new Role("Supporter", Role.CLIENT);
    /** Active participant in a game or sport. */
    public final static Role PLAYER = new Role("Player", Role.SERVER);
    /** Impartial enforcer of game rules. */
    public final static Role REFEREE = new Role("Referee", Role.SUPERVISOR);

    // Performance context
    /** Creative participant in an art or performance. */
    public final static Role ARTIST = new Role("Artist", Role.SERVER);
    /** SociologicalGroup of observers for a performance. */
    public final static Role AUDIENCE = new Role("Audience", Role.CLIENT);
    /** Individual providing technical support for a show. */
    public final static Role TECHNICIAN = new Role("Technician", Role.SERVER);
    /** Commercial manager of a performance. */
    public final static Role PRODUCER = new Role("Producer", Role.SERVER);
    /** Creative manager directing the performance. */
    public final static Role DIRECTOR = new Role("Director", Role.SUPERVISOR);
    /** Individual ensuring order during a performance. */
    public final static Role SECURITY = new Role("Security", Role.SERVER);

    // Ecological context
    /** Target of a hunt. */
    public final static Role PREY = new Role("Prey", Role.SERVER);
    /** Initiator of a hunt. */
    public final static Role PREDATOR = new Role("Predator", Role.CLIENT);
    /** Scavenger or beneficiary of a hunt outcome. */
    public final static Role OPPORTUNIST = new Role("Opportunist", Role.OBSERVER);
    /** Symbiotic participant helping another role. */
    public final static Role HELPER = new Role("Symbiot", Role.SERVER);
}

