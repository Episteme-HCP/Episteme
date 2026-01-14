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

package org.jscience.sociology;

/**
 * A class defining some basic activities or situations.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//below are personal choices, you may not agree with
public class Situations extends Object {
    /** DOCUMENT ME! */
    public final static Situation SLEEPING = new Situation("Sleeping", "");

    /** DOCUMENT ME! */
    public final static Situation CLEANING = new Situation("Cleaning", "");

    /** DOCUMENT ME! */
    public final static Situation CHATTING = new Situation("Chatting", "");

    /** DOCUMENT ME! */
    public final static Situation ARGUING = new Situation("Arguing", "");

    /** DOCUMENT ME! */
    public final static Situation HUNTING = new Situation("Hunting", "");

    /** DOCUMENT ME! */
    public final static Situation COOKING = new Situation("Cooking", "");

    /** DOCUMENT ME! */
    public final static Situation DINING = new Situation("Dining", "");

    /** DOCUMENT ME! */
    public final static Situation CURING = new Situation("Curing", "");

    /** DOCUMENT ME! */
    public final static Situation FIGHTING = new Situation("Fighting", ""); //making war against others

    /** DOCUMENT ME! */
    public final static Situation PRAYING = new Situation("Praying", "");

    /** DOCUMENT ME! */
    public final static Situation CARING = new Situation("Caring", ""); //includes SHOOLING

    /** DOCUMENT ME! */
    public final static Situation PLAYING = new Situation("Playing", "");

    /** DOCUMENT ME! */
    public final static Situation COUNSELLING = new Situation("counselling", ""); //deciding about people's future

    /** DOCUMENT ME! */
    public final static Situation FEASTING = new Situation("Feasting", "");

    /** DOCUMENT ME! */
    public final static Situation BUILDING = new Situation("Building", "");

    /** DOCUMENT ME! */
    public final static Situation TRADING = new Situation("Trading", "");

    /** DOCUMENT ME! */
    public final static Situation FARMING = new Situation("Farming", "");

    /** DOCUMENT ME! */
    public final static Situation REPRODUCING = new Situation("Reproducing", "");

    /** DOCUMENT ME! */
    public final static Situation TRAVELLING = new Situation("travelling", "");

    /** DOCUMENT ME! */
    public final static Situation FLEEING = new Situation("Fleeing", "");

    /** DOCUMENT ME! */
    public final static Situation GUARDING = new Situation("Guarding", "");

    /** DOCUMENT ME! */
    public final static Situation WAITING = new Situation("Waiting", "");

    /** DOCUMENT ME! */
    public final static Situation WORKING = new Situation("Working", ""); //WORKING is a degenerate form of building and trading

    //give birth, marry, bury, cry...
}
