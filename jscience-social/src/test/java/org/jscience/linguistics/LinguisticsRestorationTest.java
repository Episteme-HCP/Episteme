package org.jscience.linguistics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LinguisticsRestorationTest {

    @Test
    public void testLanguageAndPhonemes() {
        Language english = new Language("English");
        Phoneme p1 = new Phoneme(english, "th");
        Phoneme p2 = new Phoneme(english, "ng");

        assertTrue(english.getPhonemes().contains(p1));
        assertTrue(english.getPhonemes().contains(p2));
        assertEquals(2, english.getPhonemes().size());
        assertEquals("English", p1.getLanguage().getName());
    }

    @Test
    public void testSentenceTypes() {
        Sentence s1 = new Sentence("Hello world.");
        assertEquals(Sentence.Type.DECLARATIVE, s1.getType());

        Sentence s2 = new Sentence("Are you there?");
        assertEquals(Sentence.Type.INTERROGATIVE, s2.getType());

        Sentence s3 = new Sentence("Go away!");
        assertEquals(Sentence.Type.EXCLAMATORY, s3.getType());

        Sentence s4 = new Sentence("Plain text");
        assertEquals(Sentence.Type.UNKNOWN, s4.getType());
    }
}
