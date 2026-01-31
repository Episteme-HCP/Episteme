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

package org.jscience.social;

import org.jscience.social.economics.*;
import org.jscience.social.economics.models.MacroModel;
import org.jscience.social.sociology.*;
import org.jscience.natural.biology.BiologicalSex;
import org.jscience.social.politics.*;
import org.jscience.social.geography.*;
import org.jscience.core.mathematics.numbers.real.Real;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.*;

public class SimulationEnginesTest {

    @Test
    public void testMacroModel() {
        Economy eco = Economy.usa();
        MacroModel model = new MacroModel("US Economy Simulator", eco);

        double initialGDP = eco.getGDP().getAmount().doubleValue();
        model.simulateYear();

        assertTrue(eco.getInflationRate().doubleValue() >= 0);
        assertTrue(eco.getUnemploymentRate().doubleValue() >= 0);

        double prediction = model.predictGDP(10, Real.of(0.03)).getAmount().doubleValue();
        assertTrue(prediction > initialGDP);
    }

    @Test
    public void testCensus() {
        List<Person> people = new ArrayList<>();
        people.add(new Person("1", "A", BiologicalSex.FEMALE, LocalDate.of(2000, 1, 1), "US"));
        people.add(new Person("2", "B", BiologicalSex.MALE, LocalDate.of(1950, 1, 1), "US"));

        Census census = new Census(people);

        assertEquals(2, census.getTotalPopulation());

        double avgAge = census.getAverageAge();
        assertTrue(avgAge > 20 && avgAge < 100);

        Map<Gender, Long> genderDist = census.getGenderDistribution();
        assertEquals(1L, genderDist.get(Gender.FEMALE));
        assertEquals(1L, genderDist.get(Gender.MALE));
    }

    @Test
    public void testVotingSystem() {
        Map<String, Long> votes = new HashMap<>();
        votes.put("Party A", 100L);
        votes.put("Party B", 50L);
        votes.put("Party C", 25L);

        List<String> fptpWinner = VotingSystem.determineWinners(votes, VotingMethod.FIRST_PAST_THE_POST, 1);
        assertEquals(1, fptpWinner.size());
        assertEquals("Party A", fptpWinner.get(0));

        List<String> propWinners = VotingSystem.determineWinners(votes, VotingMethod.PROPORTIONAL, 10);
        assertEquals(3, propWinners.size());
        assertTrue(propWinners.stream().anyMatch(w -> w.contains("Party A")));
    }

    @Test
    public void testClimateZone() {
        ClimateZone tropical = new ClimateZone(ClimateZone.Type.TROPICAL, Real.of(28.0), Real.of(2000.0));
        assertTrue(tropical.isHabitable());
        assertTrue(tropical.supportsAgriculture());

        ClimateZone polar = new ClimateZone(ClimateZone.Type.POLAR, Real.of(-20.0), Real.of(100.0));
        assertFalse(polar.isHabitable());
    }
}


