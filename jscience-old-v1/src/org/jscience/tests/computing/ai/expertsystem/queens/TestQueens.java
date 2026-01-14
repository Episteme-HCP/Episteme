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

