package org.jscience.tests.computing.ai.expertsystem.hanoi;

/*
 * org.jscience.tests.computing.ai.expertsystem - The Java Embedded Object Production System
 * Copyright (c) 2000   Carlos Figueira Filho
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Contact: Carlos Figueira Filho (csff@cin.ufpe.br)
 */

/**
 * Class used to test the Towers of Hanoi example for org.jscience.tests.computing.ai.expertsystem.
 *
 * @author Carlos Figueira Filho (<a href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  06 Apr 2000
 */
public class TestHanoi {

    /**
     * Starts the application.
     *
     * @param args a command-line arguments array. None is needed,
     *             but one can pass the number of discs to be moved.
     */
    public static void main(java.lang.String[] args) {
        int noDiscs;
        if (args.length == 0) {
            noDiscs = 5;
        } else {
            noDiscs = Integer.parseInt(args[0]);
        }
        Hanoi h = new Hanoi(noDiscs, 1, 3);
        BaseHanoi kb = new BaseHanoi();
        kb.addRuleFireListener(new org.jscience.tests.computing.ai.expertsystem.RuleFireListener() {
            public void ruleFiring(org.jscience.tests.computing.ai.expertsystem.RuleEvent e) {
                int i = e.getRuleIndex();
                String name = e.getKnowledgeBase().getRuleBase().getRuleNames()[i];
                System.out.println("Firing rule " + name);
            }

            public void ruleFired(org.jscience.tests.computing.ai.expertsystem.RuleEvent e) {
            }
        });
        kb.
        assert (h);
        kb.run();
        System.out.println("Problem solution:");
        h.printSolution();
    }
}
