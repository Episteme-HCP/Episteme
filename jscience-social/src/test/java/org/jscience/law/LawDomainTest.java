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
