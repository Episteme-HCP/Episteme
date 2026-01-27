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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.jscience.history.CounterfactualHistoryRunner;
import org.jscience.history.CounterfactualSimulator;
import org.jscience.history.time.FuzzyTimePoint;
import org.jscience.politics.VotingMethod;
import org.jscience.sociology.Group;
import org.jscience.sociology.GroupKind;
import org.jscience.sociology.Person;
import org.jscience.sociology.Society;
import org.jscience.biology.BiologicalSex;
import java.util.Arrays;
import java.util.List;

/**
 * Integration test for the complete Counterfactual History and Social Simulation pipeline.
 */
public class SocialSimulationIntegrationTest {

    @Test
    public void testFullSimulationPipeline() {
        // 1. Setup Society
        Society society = new Society("Experimental Nations");
        Group politicalElite = new Group("Elite", GroupKind.ORGANIZATION);
        
        for (int i = 0; i < 20; i++) {
            Person h = new Person("person-" + i, BiologicalSex.MALE);
            politicalElite.addMember(h);
        }
        society.addInstitution(politicalElite);

        // 2. Define History
        CounterfactualSimulator.ContingencyEvent baseEvent = new CounterfactualSimulator.ContingencyEvent(
            "monarchy_founding", "Founding of the Monarchy", 
            FuzzyTimePoint.circaBce(100), "Political", 0.9, List.of());
        
        CounterfactualSimulator.CounterfactualScenario scenario = new CounterfactualSimulator.CounterfactualScenario(
            "Republic Alternative", "What if the Monarchy was never founded?",
            baseEvent, "Republic founded instead", List.of(), java.util.Map.of());

        // 3. Run Simulation
        CounterfactualHistoryRunner runner = new CounterfactualHistoryRunner(society);
        runner.addScenario(scenario);

        String report = runner.runDivergenceAnalysis(
            "2026 National Vote", 
            Arrays.asList("Conservative", "Liberal", "Radical"), 
            VotingMethod.FIRST_PAST_THE_POST);

        assertNotNull(report);
        assertTrue(report.contains("Republic Alternative"));
        assertTrue(report.contains("Post-Divergence Election Winner"));
    }
}
