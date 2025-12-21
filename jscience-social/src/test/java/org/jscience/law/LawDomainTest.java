/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.law;

import org.jscience.geography.Place;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class LawDomainTest {

    @Test
    public void testContractLifecycle() {
        LocalDate today = LocalDate.now();
        Contract employment = new Contract(
                "C-001",
                Contract.Type.EMPLOYMENT,
                "Corp Inc.",
                "John Doe",
                today);

        assertEquals(Contract.Status.DRAFT, employment.getStatus());
        assertFalse(employment.isActive());

        employment.sign();
        assertEquals(Contract.Status.ACTIVE, employment.getStatus());
        assertTrue(employment.isActive());

        employment.terminate();
        assertEquals(Contract.Status.TERMINATED, employment.getStatus());
        assertFalse(employment.isActive());
    }

    @Test
    public void testJurisdictionAndStatutes() {
        Place usaPlace = new Place("USA", Place.Type.COUNTRY);
        Jurisdiction federal = new Jurisdiction("Federal", usaPlace);

        Place nyPlace = new Place("New York", Place.Type.REGION);
        Jurisdiction state = new Jurisdiction("NY State", nyPlace, federal);

        Statute constitution = new Statute(
                "CONST", "Constitution", Statute.Type.CONSTITUTION, "Federal", 1787, Statute.Status.ENACTED);
        Statute localLaw = new Statute(
                "NY-101", "Traffic", Statute.Type.STATE_LAW, "NY", 2000, Statute.Status.ENACTED);

        federal.addStatute(constitution);
        state.addStatute(localLaw);

        assertTrue(state.isApplicable(localLaw));
        assertTrue(state.isApplicable(constitution), "State should inherit federal statutes logic");
        assertFalse(federal.isApplicable(localLaw), "Federal should not inherit local laws");
    }
}
