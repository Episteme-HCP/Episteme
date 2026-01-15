package org.jscience.tests.computing.ai.expertsystem.fibonacci;

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
 * Test class for the fibonacci example using org.jscience.tests.computing.ai.expertsystem.
 *
 * @author Carlos Figueira Filho (<a href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  15 Mar 2000  Class adapted from previous version of org.jscience.tests.computing.ai.expertsystem.
 */
public class TestFibonacci {

    /**
     * Starts the application.
     *
     * @param args a command-line arguments array. None is needed,
     *             but one can pass the index of the fibonacci series.
     */
    public static void main(java.lang.String[] args) {
        int n;
        if (args.length == 0) {
            n = 15;
        } else {
            n = Integer.parseInt(args[0]);
        }
        System.out.println("Running Fib(" + n + ")");
        long t0 = System.currentTimeMillis();
        FibonacciBase kb = new FibonacciBase();
        long t1 = System.currentTimeMillis();
        Fibonacci f = new Fibonacci(n);
//		kb.addRuleFireListener(new org.jscience.tests.computing.ai.expertsystem.RuleFireListener() {
//			public void ruleFiring(org.jscience.tests.computing.ai.expertsystem.RuleEvent e) {
//				int i = e.getRuleIndex();
//				String name = e.getKnowledgeBase().getRuleBase().getRuleNames()[i];
//				System.out.println("Firing rule " + name);
//			}
//			public void ruleFired(org.jscience.tests.computing.ai.expertsystem.RuleEvent e) {}
//		});
        kb.
        assert (f);
        kb.run();
        long t2 = System.currentTimeMillis();
        System.out.println(f.getN() +
                "th number of the fibonacci series = " +
                f.getValue());
        System.out.println("Time creating kb: " + (t1 - t0) + "ms");
        System.out.println("Running time: " + (t2 - t1) + "ms");
        System.out.println("Total time: " + (t2 - t0) + "ms");
    }
}
