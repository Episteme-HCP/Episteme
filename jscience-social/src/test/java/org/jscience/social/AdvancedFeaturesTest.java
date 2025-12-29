/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.social;

import org.jscience.economics.*;
import org.jscience.sociology.*;
import org.jscience.geography.*;
import org.jscience.linguistics.*;
import org.jscience.mathematics.numbers.real.Real;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class AdvancedFeaturesTest {

    @Test
    public void testFinancialMath() {
        Money principal = Money.usd(1000.0);
        Money fv = FinancialMath.calculateFutureValue(principal, Real.of(0.05), 10);
        // 1000 * (1.05)^10 = 1628.89
        assertEquals(1628.89, fv.getAmount().doubleValue(), 0.01);

        Money loan = Money.usd(100000.0);
        // 30 years at 5%
        Money monthly = FinancialMath.calculateMonthlyPayment(loan, Real.of(0.05), 30);
        assertEquals(536.82, monthly.getAmount().doubleValue(), 0.01);
    }

    @Test
    public void testSocialNetwork() {
        SocialNetwork sn = new SocialNetwork();
        Person alice = new Person("P1", "Alice", Person.Gender.FEMALE, LocalDate.of(1990, 1, 1), "USA");
        Person bob = new Person("P2", "Bob", Person.Gender.MALE, LocalDate.of(1992, 2, 2), "UK");
        Person charlie = new Person("P3", "Charlie", Person.Gender.MALE, LocalDate.of(1985, 3, 3), "France");

        sn.addConnection(alice, bob);
        sn.addConnection(bob, charlie);

        assertTrue(sn.areConnected(alice, bob));
        assertFalse(sn.areConnected(alice, charlie));

        assertEquals(1, sn.getDegreesOfSeparation(alice, bob));
        assertEquals(2, sn.getDegreesOfSeparation(alice, charlie));
    }

    @Test
    public void testGISProfile() {
        Coordinate paris = new Coordinate(48.8566, 2.3522);
        Coordinate london = new Coordinate(51.5074, -0.1278);

        double distance = GISProfile.calculateDistanceMeters(paris, london);
        // Approx 344 km
        assertEquals(343000, distance, 5000); // 5km tolerance

        GISProfile gp = new GISProfile(GISProfile.Projection.MERCATOR);
        org.jscience.mathematics.linearalgebra.Vector<Real> proj = gp.project(paris);
        assertNotNull(proj);
        assertEquals(2, proj.dimension());
    }

    @Test
    public void testCorpus() {
        Corpus corpus = new Corpus();
        corpus.addDocument("Hello world");
        corpus.addDocument("Hello Gemini");

        java.util.Map<String, Integer> freq = corpus.getWordFrequency();
        assertEquals(2, freq.get("hello"));
        assertEquals(1, freq.get("world"));
        assertEquals(1, freq.get("gemini"));
    }
}
