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

package org.jscience.tests.computing.ai.expertsystem.monkeys;

import java.awt.*;

/**
 * Class used to test the monkey and bananas implementation.
 *
 * @author Carlos Figueira Filho (<a href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 */
public class TestMonkeys implements Constants {

    /**
     * The knowledge base.
     */
    static MonkeysBase mb;

    /**
     * Generates the general instance of the problem.
     */
    private static void general() {
        Couch c = new Couch(new Point(7, 7));
        c.setOn(FLOOR);
        Ladder l = new Ladder(new Point(4, 3));
        l.setOn(FLOOR);
        Banana b = new Banana(new Point(9, 9));
        b.setOn(CEILING);
        Blanket bl = new Blanket(new Point(7, 7));
        Monkey m = new Monkey(new Point(7, 7));
        m.setOn(c);
        m.setObjectHeld(bl);
        Goal g = new Goal(HOLD);
        g.setObject(b);
        mb.
        assert (l);
        mb.
        assert (b);
        mb.
        assert (m);
        mb.
        assert (c);
        mb.
        assert (bl);
        mb.
        assert (g);
    }

    /**
     * Generates a general instance of the problem, where the monkey is on a ladder.
     */
    private static void generalOnLadder() {
        Couch c = new Couch(new Point(7, 7));
        c.setOn(FLOOR);
        Ladder l = new Ladder(new Point(5, 5));
        l.setOn(FLOOR);
        Banana b = new Banana(new Point(7, 7));
        b.setOn(CEILING);
        Blanket bl = new Blanket(new Point(5, 5));
        bl.setOn(FLOOR);
        Monkey m = new Monkey(new Point(5, 5));
        m.setOn(l);
        Goal g = new Goal(HOLD);
        g.setObject(b);
        mb.
        assert (l);
        mb.
        assert (b);
        mb.
        assert (m);
        mb.
        assert (c);
        mb.
        assert (bl);
        mb.
        assert (g);
    }

    /**
     * Generates a second general instance of the problem,
     * where the monkey is on a ladder.
     */
    private static void generalOnLadder2() {
        // With 2 monkeys
        Couch c = new Couch(new Point(7, 7));
        c.setOn(FLOOR);
        Ladder l = new Ladder(new Point(5, 5));
        l.setOn(FLOOR);
        Banana b = new Banana(new Point(7, 7));
        b.setOn(CEILING);
        Blanket bl = new Blanket(new Point(5, 5));
        bl.setOn(FLOOR);
        Monkey m = new Monkey("monkey1", new Point(5, 5));
        m.setOn(l);
        Monkey m2 = new Monkey("monkey2", new Point(5, 5));
        m2.setOn(l);
        Goal g = new Goal(HOLD);
        g.setObject(b);
        mb.
        assert (l);
        mb.
        assert (b);
        mb.
        assert (m);
        mb.
        assert (m2);
        mb.
        assert (c);
        mb.
        assert (bl);
        mb.
        assert (g);
    }

    /**
     * Generates an impossible goal.
     */
    private static void generalOnLadderImpossible() {
        Couch c = new Couch(new Point(7, 7));
        c.setOn(FLOOR);
        Ladder l = new Ladder(new Point(5, 5));
        l.setOn(FLOOR);
        Banana b = new Banana(new Point(7, 7));
        b.setOn(CEILING);
        Blanket bl = new Blanket(new Point(5, 5));
        bl.setOn(FLOOR);
        Monkey m = new Monkey(new Point(5, 5));
        m.setOn(l);
        Goal g = new Goal(100);
        g.setObject(b);
        mb.
        assert (l);
        mb.
        assert (b);
        mb.
        assert (m);
        mb.
        assert (c);
        mb.
        assert (bl);
        mb.
        assert (g);
    }

    /**
     * Tests the application.
     */
    public static void main(String[] args) {
        mb = new MonkeysBase(new MEAConflictSet());

        testHoldsObjectCeilingAtObj();

        mb.addRuleFireListener(new org.jscience.tests.computing.ai.expertsystem.RuleFireListener() {
            public void ruleFiring(org.jscience.tests.computing.ai.expertsystem.RuleEvent e) {
                int i = e.getRuleIndex();
                String name = e.getKnowledgeBase().getRuleBase().getRuleNames()[i];
                System.out.println("Firing rule " + name);
                String[] decls = e.getKnowledgeBase().getRuleBase().getDeclaredIdentifiers(i);
                Object[] values = e.getKnowledgeBase().getRuleBase().getObjects(i);
                for (int j = 0; j < decls.length; j++) {
                    System.out.println("  " + decls[j] + " = " + values[j]);
                }
            }

            public void ruleFired(org.jscience.tests.computing.ai.expertsystem.RuleEvent e) {
            }
        });

        mb.run();

        java.util.Vector v = mb.objects("org.jscience.tests.computing.ai.expertsystem.monkeys.Goal");
        boolean allSatisfied = true;
        for (int i = 0; i < v.size(); i++) {
            Goal g = (Goal) v.elementAt(i);
            if (!g.isSatisfied()) {
                System.out.println("impossible, goal " + g + " cannot be satisfied!");
                allSatisfied = false;
            }
        }
        if (allSatisfied) {
            System.out.println("congratulations, all goals are satisfied");
        }
    }

    /**
     * Instance of the problem used to test rule "atMonkey"
     */
    private static void testAtMonkey() {
        Ladder l = new Ladder(new Point(7, 7));
        l.setOn(FLOOR);
        Monkey m = new Monkey(new Point(5, 7));
        m.setOn(FLOOR);
        Goal g = new Goal(AT);
        g.setObject(null);
        g.setTo(new Point(7, 7));
        mb.
        assert (l);
        mb.
        assert (m);
        mb.
        assert (g);
    }

    /**
     * Instance of the problem used to test rule "atMonkeyObject"
     */
    private static void testAtMonkeyObject() {
        Ladder l = new Ladder(new Point(7, 7));
        l.setOn(FLOOR);
        Blanket b = new Blanket(new Point(5, 5));
        Monkey m = new Monkey(new Point(5, 5));
        m.setOn(FLOOR);
        m.setObjectHeld(b);
        Goal g = new Goal(AT);
        g.setObject(null);
        g.setTo(new Point(7, 7));
        mb.
        assert (l);
        mb.
        assert (m);
        mb.
        assert (g);
        mb.
        assert (b);
    }

    /**
     * Instance of the problem used to test rule "atObject"
     */
    private static void testAtObject() {
        Ladder l = new Ladder(new Point(5, 5));
        l.setOn(null);
        Monkey m = new Monkey(new Point(5, 5));
        m.setOn(FLOOR);
        m.setObjectHeld(l);
        Goal g = new Goal(AT);
        g.setObject(l);
        g.setTo(new Point(7, 7));
        mb.
        assert (l);
        mb.
        assert (m);
        mb.
        assert (g);
    }

    /**
     * Instance of the problem used to test rule "holdsNil"
     */
    private static void testHoldsNil() {
        Blanket b = new Blanket(new Point(5, 5));
        Monkey m = new Monkey(new Point(5, 5));
        m.setOn(FLOOR);
        m.setObjectHeld(b);
        Goal g = new Goal(HOLD);
        g.setObject(null);
        mb.
        assert (m);
        mb.
        assert (g);
        mb.
        assert (b);
    }

    /**
     * Instance of the problem used to test rule "holdsObjectCeiling"
     */
    private static void testHoldsObjectCeiling() {
        Ladder l = new Ladder(new Point(5, 5));
        l.setOn(FLOOR);
        Banana b = new Banana(new Point(5, 5));
        b.setOn(CEILING);
        Monkey m = new Monkey(new Point(5, 5));
        m.setOn(FLOOR);
        Goal g = new Goal(HOLD);
        g.setObject(b);
        mb.
        assert (m);
        mb.
        assert (g);
        mb.
        assert (b);
        mb.
        assert (l);
    }

    /**
     * Second instance of the problem used to test rule "holdsObjectCeiling"
     */
    private static void testHoldsObjectCeiling2() {
        Ladder l = new Ladder(new Point(5, 5));
        Banana b = new Banana(new Point(5, 5));
        b.setOn(CEILING);
        Monkey m = new Monkey(new Point(5, 5));
        m.setOn(FLOOR);
        m.setObjectHeld(l);
        Goal g = new Goal(HOLD);
        g.setObject(b);
        mb.
        assert (m);
        mb.
        assert (g);
        mb.
        assert (b);
        mb.
        assert (l);
    }

    /**
     * Instance of the problem used to test rule "holdsObjectCeilingAtObj"
     */
    private static void testHoldsObjectCeilingAtObj() {
        Ladder l = new Ladder(new Point(5, 5));
        l.setOn(FLOOR);
        Banana b = new Banana(new Point(7, 7));
        b.setOn(CEILING);
        Blanket bl = new Blanket(new Point(5, 5));
        Monkey m = new Monkey(new Point(5, 5));
        m.setOn(l);
        m.setObjectHeld(bl);
        Goal g = new Goal(HOLD);
        g.setObject(b);
        mb.
        assert (m);
        mb.
        assert (g);
        mb.
        assert (b);
        mb.
        assert (l);
        mb.
        assert (bl);
    }

    /**
     * Instance of the problem used to test rule "holdsObjectNotCeilingOnLadder"
     */
    private static void testHoldsObjectNotCeilingOnLadder() {
        Ladder l = new Ladder(new Point(5, 5));
        l.setOn(FLOOR);
        Monkey m = new Monkey(new Point(5, 5));
        m.setOn(l);
        Goal g = new Goal(HOLD);
        g.setObject(l);
        mb.
        assert (l);
        mb.
        assert (m);
        mb.
        assert (g);
    }

    /**
     * Instance of the problem used to test an ON goal
     */
    private static void testOn() {
        Ladder l = new Ladder(new Point(5, 5));
        l.setOn(FLOOR);
        Monkey m = new Monkey(new Point(5, 5));
        m.setOn(FLOOR);
        Goal g = new Goal(ON);
        g.setObject(FLOOR);
        mb.
        assert (l);
        mb.
        assert (m);
        mb.
        assert (g);
    }

    /**
     * Instance of the problem used to test rule "onFloor"
     */
    private static void testOnFloor() {
        Ladder l = new Ladder(new Point(5, 5));
        l.setOn(FLOOR);
        Monkey m = new Monkey(new Point(5, 5));
        m.setOn(l);
        Goal g = new Goal(ON);
        g.setObject(FLOOR);
        mb.
        assert (l);
        mb.
        assert (m);
        mb.
        assert (g);
    }

    /**
     * Instance of the problem used to test rule "onFloorSatisfied"
     */
    private static void testOnFloorSatisfied() {
        Ladder l = new Ladder(new Point(5, 7));
        l.setOn(FLOOR);
        Monkey m = new Monkey(new Point(5, 7));
        m.setOn(FLOOR);
        Goal g = new Goal(ON);
        g.setObject(FLOOR);
        mb.
        assert (l);
        mb.
        assert (m);
        mb.
        assert (g);
    }

    /**
     * Instance of the problem used to test rule "onPhysicalObject"
     */
    private static void testOnPhysicalObject() {
        Ladder l = new Ladder(new Point(5, 7));
        l.setOn(FLOOR);
        Monkey m = new Monkey(new Point(5, 7));
        m.setOn(FLOOR);
        Goal g = new Goal(ON);
        g.setObject(l);
        mb.
        assert (l);
        mb.
        assert (m);
        mb.
        assert (g);
    }

    /**
     * Instance of the problem used to test rule "onPhysicalObjectAtMonkey"
     */
    private static void testOnPhysicalObjectAtMonkey() {
        Ladder l = new Ladder(new Point(6, 5));
        l.setOn(FLOOR);
        Monkey m = new Monkey(new Point(3, 3));
        m.setOn(FLOOR);
        Goal g = new Goal(ON);
        g.setObject(l);
        mb.
        assert (l);
        mb.
        assert (m);
        mb.
        assert (g);
    }

    /**
     * Instance of the problem used to test rule "onPhysicalObjectHold"
     */
    private static void testOnPhysicalObjectHold() {
        Ladder l = new Ladder(new Point(5, 5));
        l.setOn(FLOOR);
        Monkey m = new Monkey(new Point(5, 5));
        m.setOn(FLOOR);
        m.setObjectHeld(l);
        Goal g = new Goal(ON);
        g.setObject(l);
        mb.
        assert (l);
        mb.
        assert (m);
        mb.
        assert (g);
    }

    /**
     * Instance of the problem used to test rule "onPhysicalObjectSatisfied"
     */
    private static void testOnPhysicalObjectSatisfied() {
        Ladder l = new Ladder(new Point(5, 5));
        l.setOn(FLOOR);
        Monkey m = new Monkey(new Point(5, 5));
        m.setOn(l);
        Goal g = new Goal(ON);
        g.setObject(l);
        mb.
        assert (l);
        mb.
        assert (m);
        mb.
        assert (g);
    }
}
