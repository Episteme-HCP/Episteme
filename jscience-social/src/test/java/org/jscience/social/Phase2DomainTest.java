package org.jscience.social;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.jscience.law.*;
import org.jscience.linguistics.*;
import org.jscience.philosophy.*;
import org.jscience.psychology.*;
import org.jscience.sociology.Person;
import org.jscience.geography.Place;

import java.time.LocalDate;
import java.util.Set;

public class Phase2DomainTest {

    @Test
    public void testLawDomain() {
        Code civilCode = new Code("Civil Code");
        Article art1 = new Article("Art. 1", "All human beings are born free.");
        civilCode.addArticle(art1);

        assertEquals("Civil Code", civilCode.getId());
        assertEquals(art1, civilCode.getArticle("Art. 1"));

        Contract contract = new Contract("Employment Contract", LocalDate.now());
        Person employer = new Person("ACME Corp", Person.Gender.MALE); // Simplified Person usage
        Person employee = new Person("John Doe", Person.Gender.MALE);

        contract.addParty(employer);
        contract.addParty(employee);
        contract.addClause("Salary: 50000");

        assertTrue(contract.isValid());
        assertEquals(2, contract.getParties().size());
        assertNotNull(contract.getId());
    }

    @Test
    public void testLinguisticsDomain() {
        Language en = Language.ENGLISH;
        assertEquals("en", en.getId());
        assertEquals("English", en.getName());

        Language custom = new Language("xx", "CustomLang");
        assertEquals("xx", custom.getCode());
    }

    @Test
    public void testPhilosophyDomain() {
        Concept freedom = new Concept("Freedom", "Power to act, speak, or think as one wants");
        assertEquals("freedom", freedom.getId());

        Belief karma = new Belief("Karma", "Cause and effect");
        karma.addRelatedConcept(new Concept("Rebirth", "Being born again"));

        assertEquals(1, karma.getRelatedConcepts().size());

        Religion buddhism = Religion.BUDDHISM;
        buddhism.addBelief(karma);

        assertTrue(buddhism.getCoreBeliefs().contains(karma));
    }

    @Test
    public void testPsychologyDomain() {
        Behavior instinct = new Behavior("Survival", Behavior.Type.INSTINCTIVE, "Basic survival mechanism");

        assertEquals("Survival", instinct.getId());
        assertEquals(Behavior.Type.INSTINCTIVE, instinct.getType());
    }
}
