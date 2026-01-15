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
public class Test {

    public static void main(String[] args) {
        BaseHanoi base = new BaseHanoi();
        Hanoi h = new Hanoi(5, 1, 3);
        base.
        assert (h);
        base.addRuleFireListener(new RuleFireListener() {
            public void ruleFiring(RuleEvent e) {
                int i = e.getRuleIndex();
                String name = e.getKnowledgeBase().getRuleBase().getRuleNames()[i];
                System.out.println("Firing rule " + name);
                String[] decls = e.getKnowledgeBase().getRuleBase().
                        getDeclaredIdentifiers(i);
                Object[] objs = e.getKnowledgeBase().getRuleBase().getObjects(i);
                System.out.println("Before firing:");
                for (int j = 0; j < decls.length; j++) {
                    System.out.println("  " + decls[j] + " = " + objs[j]);
                }
            }

            public void ruleFired(RuleEvent e) {
                int i = e.getRuleIndex();
                String[] decls = e.getKnowledgeBase().getRuleBase().
                        getDeclaredIdentifiers(i);
                Object[] objs = e.getKnowledgeBase().getRuleBase().getObjects(i);
                System.out.println("After firing:");
                for (int j = 0; j < decls.length; j++) {
                    System.out.println("  " + decls[j] + " = " + objs[j]);
                }
            }
        });
        base.run();
        h.printSolution();
    }

}
