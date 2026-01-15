package org.jscience.tests.computing.ai.expertsystem.factorial;

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

import org.jscience.tests.computing.ai.expertsystem.RuleEvent;
import org.jscience.tests.computing.ai.expertsystem.RuleFireListener;

/**
 * Class used to test the Factorial example for org.jscience.tests.computing.ai.expertsystem.
 *
 * @author Carlos Figueira Filho (<a href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  07 Apr 2000
 */
public class TestFact {

    /**
     * Starts the application.
     *
     * @param args a command-line arguments array. None is needed,
     *             but one can pass the number whose factorial is wanted.
     */
    public static void main(java.lang.String[] args) {
        int n;
        if (args.length == 0) {
            n = 5;
        } else {
            n = Integer.parseInt(args[0]);
        }
        Fact f = new Fact(n);
        FactBase kb = new FactBase();
        kb.addRuleFireListener(new RuleFireListener() {
            public void ruleFiring(RuleEvent e) {
                int i = e.getRuleIndex();
                String name = e.getKnowledgeBase().getRuleBase().getRuleNames()[i];
                System.out.println("Firing rule " + name);
                System.out.println("Before firing: decl = " + e.getValues());
            }

            public void ruleFired(RuleEvent e) {
                System.out.println("After firing: decl = " + e.getValues());
            }
        });
        try {
            kb.
            assert (f);
            kb.run();
            System.out.println("Factorial of " + f.getN() + " = " + f.getResult());
        } catch (Exception e) {
            System.out.println("e = " + e);
            e.printStackTrace();
        }
    }
}
