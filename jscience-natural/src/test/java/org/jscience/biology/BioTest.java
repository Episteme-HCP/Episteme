package org.jscience.biology;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class BioTest {

    @Test
    public void testDNA() {
        DNA dna = new DNA("ATCG");
        assertEquals("ATCG", dna.toString());
        assertEquals(4, dna.getLength());

        DNA compl = dna.getComplementary();
        assertEquals("TAGC", compl.toString());

        RNA rna = dna.transcribe();
        assertEquals("UAGC", rna.toString());
    }

    @Test
    public void testRNA() {
        RNA rna = new RNA("AUCG");
        assertEquals("AUCG", rna.toString());

        RNA compl = rna.getComplementary();
        assertEquals("UAGC", compl.toString());
    }
}
