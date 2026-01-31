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
 * Standard social situations and activities.
 * This utility class provides a set of common pre-defined {@link Situation} instances
 * used across the sociology module for behavior modeling and simulation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class Situations implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The activity of sleeping. */
    public static final Situation SLEEPING = new Situation("Sleeping", "The physiological state of rest.");

    /** The activity of cleaning. */
    public static final Situation CLEANING = new Situation("Cleaning", "Removing dirt or clutter.");

    /** The activity of chatting. */
    public static final Situation CHATTING = new Situation("Chatting", "Informal social conversation.");

    /** The activity of arguing. */
    public static final Situation ARGUING = new Situation("Arguing", "Exchanging divergent or opposite views.");

    /** The activity of hunting. */
    public static final Situation HUNTING = new Situation("Hunting", "Pursuing and killing wild animals for food or sport.");

    /** The activity of cooking. */
    public static final Situation COOKING = new Situation("Cooking", "Preparing food by combining and heating ingredients.");

    /** The activity of dining. */
    public static final Situation DINING = new Situation("Dining", "Eating a meal, typically in a social context.");

    /** The activity of curing or healing. */
    public static final Situation CURING = new Situation("Curing", "Restoring health or relieving symptoms.");

    /** The activity of fighting or warfare. */
    public static final Situation FIGHTING = new Situation("Fighting", "Engaging in conflict or war.");

    /** The activity of praying. */
    public static final Situation PRAYING = new Situation("Praying", "Engaging in religious devotion or ritual.");

    /** The activity of caring, including early education. */
    public static final Situation CARING = new Situation("Caring", "Providing for the needs of others; includes schooling.");

    /** The activity of playing. */
    public static final Situation PLAYING = new Situation("Playing", "Engaging in activity for enjoyment and recreation.");

    /** The activity of counselling or decision making for others. */
    public static final Situation COUNSELLING = new Situation("Counselling", "Giving professional guidance or making collective decisions.");

    /** The activity of feasting. */
    public static final Situation FEASTING = new Situation("Feasting", "Eating and drinking sumptuously, often for a celebration.");

    /** The activity of building or constructing. */
    public static final Situation BUILDING = new Situation("Building", "Constructing something, typically a structure.");

    /** The activity of trading or commercial exchange. */
    public static final Situation TRADING = new Situation("Trading", "The act of buying and selling goods and services.");

    /** The activity of farming or agriculture. */
    public static final Situation FARMING = new Situation("Farming", "Cultivating land and raising livestock.");

    /** The activity of reproduction. */
    public static final Situation REPRODUCING = new Situation("Reproducing", "Biological and social process of creating offspring.");

    /** The activity of travelling. */
    public static final Situation TRAVELLING = new Situation("Travelling", "Moving from one place to another.");

    /** The activity of fleeing. */
    public static final Situation FLEEING = new Situation("Fleeing", "Running away from a place or situation of danger.");

    /** The activity of guarding or protecting. */
    public static final Situation GUARDING = new Situation("Guarding", "Watching over in order to protect or control.");

    /** The activity of waiting. */
    public static final Situation WAITING = new Situation("Waiting", "Staying where one is until a particular time or event.");

    /** General labor or working. */
    public static final Situation WORKING = new Situation("Working", "General productive effort; often a combination of building and trading.");

    private Situations() {
        // Prevent instantiation
    }
}

