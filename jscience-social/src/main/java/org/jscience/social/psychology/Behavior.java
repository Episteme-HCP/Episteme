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

package org.jscience.social.psychology;

import java.io.Serializable;
import java.util.Objects;
import org.jscience.natural.biology.Individual;
import org.jscience.core.util.Commented;

/**
 * Represents a specific behavior that an individual can exhibit.
 * Behaviors are categorized into reflexes, self-oriented behaviors, or social behaviors.
 * This class provides a comprehensive ethogram of common biological and social behaviors.
 * * @version 1.5
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Behavior implements Commented, org.jscience.natural.biology.Behavior, Serializable {

    private static final long serialVersionUID = 1L;

    /** Nested type constants for legacy support and cleaner API. */
    public static class Type {
        public static final int REFLEX = 1;
        public static final int SELF = 2;
        public static final int SOCIAL = 3;
        public static final int INSTINCTIVE = 4;
    }

    /** Categorization for behaviors of unknown nature. */
    public final static int UNKNOWN = 0;

    /** Categorization for innate, involuntary reflex actions. */
    public final static int REFLEX = Type.REFLEX;

    /** Categorization for individual-oriented behaviors (e.g., feeding, sleeping). */
    public final static int SELF = Type.SELF;

    /** Categorization for interaction-oriented behaviors with other individuals. */
    public final static int SOCIAL = Type.SOCIAL;

    /** Categorization for biological instincts. */
    public final static int INSTINCTIVE = Type.INSTINCTIVE;

    // Standard biological reflex behaviors
    /** Vital reflex for gas exchange. */
    public final static Behavior BREATH = new Behavior("Breath", Behavior.REFLEX);
    
    /** Primitive reflex for biological transformation. */
    public final static Behavior MUTATE = new Behavior("Mutate", Behavior.REFLEX);

    // Standard self-oriented behaviors
    /** State of inactivity or resting. */
    public final static Behavior NONE = new Behavior("None", Behavior.SELF);
    
    /** Hygiene and self-maintenance behavior. */
    public final static Behavior CLEAN = new Behavior("Clean", Behavior.SELF);
    
    /** Biological maintenance and wound recovery. */
    public final static Behavior HEAL = new Behavior("Heal", Behavior.SELF);
    
    /** Mating or reproductive activity. */
    public final static Behavior REPRODUCE = new Behavior("Reproduce", Behavior.SELF);
    
    /** Formation of a shelter or reproductive site. */
    public final static Behavior NEST_MAKING = new Behavior("Make a nest", Behavior.SELF);
    
    /** Defensive or offensive combat behavior. */
    public final static Behavior FIGHT = new Behavior("Fight", Behavior.SELF);
    
    /** Restorative dormant state. */
    public final static Behavior SLEEP = new Behavior("Sleep", Behavior.SELF);
    
    /** Predatory or resource gathering search. */
    public final static Behavior HUNT = new Behavior("Hunt", Behavior.SELF);
    
    /** Nutrient ingestion behavior. */
    public final static Behavior FEED = new Behavior("Feed", Behavior.SELF);
    
    /** Waste elimination behavior. */
    public final static Behavior DEFECATE = new Behavior("Defecate", Behavior.SELF);
    
    /** Information gathering through senses. */
    public final static Behavior OBSERVE = new Behavior("Observe", Behavior.SELF);
    
    /** Locomotion behavior. */
    public final static Behavior MOVE = new Behavior("Move", Behavior.SELF);
    
    /** Gestural communication or basic signaling. */
    public final static Behavior WAVE = new Behavior("Wave", Behavior.SELF);
    
    /** Acoustic signaling behavior. */
    public final static Behavior MAKE_NOISE = new Behavior("Make some noise", Behavior.SELF);
    
    /** Dropping or letting go of an item. */
    public final static Behavior RELEASE = new Behavior("Release", Behavior.SELF);
    
    /** Grasping or maintaining possession of an item. */
    public final static Behavior HOLD = new Behavior("Hold", Behavior.SELF);

    // Complex tool-oriented behaviors
    /** Construction of implements. */
    public final static Behavior TOOLMAKING = new Behavior("Make a tool", Behavior.SELF);
    
    /** Application of implements for a task. */
    public final static Behavior TOOLUSING = new Behavior("Use a tool", Behavior.SELF);
    
    /** Separation behavior, often with tools. */
    public final static Behavior CUT = new Behavior("Cut", Behavior.SELF);
    
    /** Thermal manipulation behavior. */
    public final static Behavior BURN = new Behavior("Burn", Behavior.SELF);
    
    /** Transportation of items. */
    public final static Behavior CARRY = new Behavior("Carry", Behavior.SELF);
    
    /** Environmental mapping and discovery behavior. */
    public final static Behavior EXPLORE = new Behavior("Explore", Behavior.SELF);

    // Human-specific or high-order cognitive behaviors
    /** Information decoding from text. */
    public final static Behavior READ = new Behavior("Read", Behavior.SOCIAL);
    
    /** Information encoding into symbols. */
    public final static Behavior WRITE = new Behavior("Write", Behavior.SOCIAL);

    // Social behaviors
    /** Competitive reproductive display. */
    public final static Behavior LEK = new Behavior("Lek", Behavior.SOCIAL);
    
    /** Social bonding through physical maintenance. */
    public final static Behavior GROOMING = new Behavior("Grooming", Behavior.SOCIAL);
    
    /** Information exchange between individuals. */
    public final static Behavior COMMUNICATE = new Behavior("Communicate", Behavior.SOCIAL);
    
    /** Learning by observing and repeating others. */
    public final static Behavior IMITATE = new Behavior("Imitate", Behavior.SOCIAL);
    
    /** Non-utilitarian recreational interaction. */
    public final static Behavior PLAY = new Behavior("Play", Behavior.SOCIAL);
    
    /** Brood raising and juvenile maintenance. */
    public final static Behavior PARENTAL_CARE = new Behavior("Parental care", Behavior.SOCIAL);
    
    /** Signal of imminent danger to a group. */
    public final static Behavior ALERT = new Behavior("Alert", Behavior.SOCIAL);
    
    /** Joint effort toward a shared goal. */
    public final static Behavior COOPERATE = new Behavior("Cooperate", Behavior.SOCIAL);
    
    /** Social conflict or hostile signaling. */
    public final static Behavior AGRESSION = new Behavior("Agression", Behavior.SOCIAL);
    
    /** Recreational performance for others. */
    public final static Behavior ENTERTAIN = new Behavior("Entertain", Behavior.SOCIAL);
    
    /** Directing attention to an object or location. */
    public final static Behavior POINT = new Behavior("Point", Behavior.SOCIAL);
    
    /** Reciprocal transfer of goods or services. */
    public final static Behavior EXCHANGE = new Behavior("Exchange", Behavior.SOCIAL);
    
    /** Standardized recreational physical competition. */
    public final static Behavior SPORT = new Behavior("Sport", Behavior.SOCIAL);

    private final String name;
    private final int kind;
    private String comments;
    private int attitude;

    /**
     * Creates a new Behavior with a given name and kind.
     *
     * @param name identifying name of the behavior
     * @param kind categorization (REFLEX, SELF, SOCIAL)
     * @throws IllegalArgumentException if name is null or empty
     */
    public Behavior(String name, int kind) {
        if ((name != null) && (name.length() > 0)) {
            this.name = name;
            this.kind = kind;
            this.attitude = UNKNOWN;
            this.comments = "";
        } else {
            throw new IllegalArgumentException(
                "The Behavior constructor can't have null or empty arguments.");
        }
    }

    /**
     * Creates a new Behavior with name, kind, and comments.
     */
    public Behavior(String name, int kind, String comments) {
        this(name, kind);
        setComments(comments);
    }

    @Override
    public void execute(Individual individual) {
        // Default implementation: specific behaviors can override this to perform actions
    }

    /**
     * Returns the name of the behavior.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the categorization kind of the behavior.
     * @return the kind (REFLEX, SELF, SOCIAL)
     */
    public int getKind() {
        return kind;
    }

    /** Legacy getter for kind. */
    public int getType() {
        return kind;
    }

    /** Legacy getter for ID. */
    public String getId() {
        return name;
    }

    /**
     * Returns the current emotional or practical attitude modulating this behavior.
     * @return the attitude constant
     */
    public int getAttitude() {
        return attitude;
    }

    /**
     * Sets the attitude modulating this behavior.
     * @param attitude the new attitude state
     */
    public void setAttitude(int attitude) {
        this.attitude = attitude;
    }

    @Override
    public String getComments() {
        return comments;
    }

    @Override
    public void setComments(String comments) {
        this.comments = Objects.requireNonNull(comments, "You can't set a null comment.");
    }

    private java.util.Map<String, Object> traits = new java.util.HashMap<>();

    @Override
    public java.util.Map<String, Object> getTraits() {
        return traits;
    }

    @Override
    public String toString() {
        return name;
    }
}

