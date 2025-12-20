package org.jscience.social;

import org.jscience.economics.*;
import org.jscience.sociology.*;
import org.jscience.geography.*;
import org.jscience.linguistics.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class AdvancedFeaturesTest {

    @Test
    public void testFinancialMath() {
        Money principal = new Money(1000.0, Currency.USD);
        Money fv = FinancialMath.calculateFutureValue(principal, 0.05, 10);
        // 1000 * (1.05)^10 = 1628.89
        assertEquals(1628.89, fv.getAmount().doubleValue(), 0.01);

        Money loan = new Money(100000.0, Currency.USD);
        // 30 years at 5%
        Money monthly = FinancialMath.calculateMonthlyPayment(loan, 0.05, 30);
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

        double distance = GISProfile.calculateDistance(paris, london);
        // Approx 344 km
        assertEquals(343000, distance, 5000); // 5km tolerance

        GISProfile gp = new GISProfile(GISProfile.Projection.MERCATOR);
        double[] proj = gp.project(paris);
        assertNotNull(proj);
        assertEquals(2, proj.length);
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
