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

package org.jscience.tests.computing.ai.expertsystem.queens;

import org.jscience.computing.game.puzzle.EightQueens;

/**
 * Test class used to test the eight queens problem solved using org.jscience.tests.computing.ai.expertsystem.
 *
 * @author Carlos Figueira Filho (<a href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 */
public class TestQueens {

    /**
     * Main entry point of the application.
     *
     * @param args command line arguments. None is needed.
     */
    public static void main(String[] args) {

        EightQueens kb = new EightQueens();
        long l1 = System.currentTimeMillis();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                kb.
                assert (new Queen(i, j));
            }
        }
        long l2 = System.currentTimeMillis();
        System.out.println("Asserting time: " + (l2 - l1) + "ms");
        kb.run();
        long l3 = System.currentTimeMillis();
        System.out.println("Running time: " + (l3 - l2) + "ms");
    }

}

