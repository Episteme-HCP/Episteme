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

package org.jscience.measure.random;

//
// RngPack 1.1a by Paul Houle
// http://www.honeylocust.com/RngPack/
//

/**
 * RandomShuffle uses one random number generator to shuffle the numbers
 * produced by another to obliterate sequential correlations.
 * To initialize a RandomShuffle,  pass it two RandomElements.  The
 * first RandomElement is used to generate a table of random numbers
 * and the second is used to choose one from the table.  An example of
 * usage is,
 * <p/>
 * <PRE>
 * RandomElement markov=new RandomShuffle(new Ranecu(),new Ranmar(),32)
 * </PRE>
 * <p/>
 * which would generate a deck of 32 numbers from <TT>RANECU</TT> and
 * use <TT>RANMAR</TT> to choose from the deck.
 * <p/>
 * <BR>
 * <B>References:</B>
 * <UL>
 * <LI> F. James; <CITE>Comp. Phys. Comm.</CITE> <STRONG>60</STRONG> (1990) p 329-344
 * <LI> D. Knuth; <CITE>The Art of Computer Programming</CITE> vol. 2, sec 3.2.2
 * </UL>
 * <p/>
 * <p/>
 * <A HREF="/RngPack/src/edu/cornell/lassp/houle/RngPack/RandomShuffle.java">
 * Source code </A> is available.
 *
 * @author <A HREF="http://www.honeylocust.com/"> Paul Houle </A> (E-mail: <A HREF="paul@honeylocust.com">paul@honeylocust.com</A>)
 * @version 1.1a
 */
public class RandomShuffle extends RandomElement {

    RandomElement generatorA;
    RandomElement generatorB;
    int decksize;
    double[] deck;

    /**
     * @param ga generator to fill shuffle deck
     * @param gb geberator to choose from shuffle deck
     * @param ds the size of the shuffle deck
     */
    public RandomShuffle(RandomElement ga, RandomElement gb, int ds) {
        generatorA = ga;
        generatorB = gb;
        decksize = ds;

        stackdeck();
    }

    /**
     * The generator.
     *
     * @see RandomElement#nextDouble
     */
    public double nextDouble() {
        double random;
        int i;

        i = generatorB.choose(0, decksize - 1);
        random = deck[i];
        deck[i] = generatorA.nextDouble();

        return random;
    }

    private void stackdeck() {
        int i;
        deck = new double[decksize];

        for (i = 0; i < decksize; i++)
            deck[i] = generatorA.nextDouble();
    }

}


