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

package org.jscience.social.linguistics.loaders.tigerxml;

import org.jscience.social.linguistics.Language;
import org.jscience.social.linguistics.Phrase;
import org.jscience.social.linguistics.Sentence;
import org.jscience.social.linguistics.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Bridge between TigerXML data structures and the JScience Linguistics ontology.
 * Parses hierarchical graph structures from TigerXML into standard Sentences and Phrases.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public final class TigerBridge {

    private TigerBridge() {}

    /**
     * Converts a TigerXML Sentence into an ontology Sentence.
     *
     * @param tigerSent the source TigerXML sentence.
     * @param language  the language of the sentence.
     * @return a mapped JScience Linguistics Sentence.
     */
    public static Sentence convert(org.jscience.social.linguistics.loaders.tigerxml.Sentence tigerSent, Language language) {
        Sentence result = new Sentence();
        result.setText(tigerSent.getText());
        
        // Map top-level non-terminals to Phrases
        // Use raw ArrayList but cast carefully or use generic-safe methods
        List<?> nts = tigerSent.getNTs();
        for (Object obj : nts) {
            if (obj instanceof org.jscience.social.linguistics.loaders.tigerxml.NT nt) {
                if (!nt.hasMother()) {
                    result.addPhrase(convertNT(nt, language));
                }
            }
        }
        
        // If there are orphaned terminals (not covered by NTs), add them as a phrase
        List<org.jscience.social.linguistics.loaders.tigerxml.T> orphaned = new ArrayList<>();
        List<?> ts = tigerSent.getTs();
        for (Object obj : ts) {
            if (obj instanceof org.jscience.social.linguistics.loaders.tigerxml.T t) {
                if (!t.hasMother()) {
                    orphaned.add(t);
                }
            }
        }
        
        if (!orphaned.isEmpty()) {
            Phrase p = new Phrase();
            for (org.jscience.social.linguistics.loaders.tigerxml.T t : orphaned) {
                p.addWord(convertT(t, language));
            }
            result.addPhrase(p);
        }

        return result;
    }

    /**
     * Converts a TigerXML NT (Non-Terminal) to a Phrase.
     */
    public static Phrase convertNT(org.jscience.social.linguistics.loaders.tigerxml.NT nt, Language language) {
        Phrase phrase = new Phrase();
        phrase.setType(mapCatToType(nt.getCat()));
        
        // Recursively add words from terminals
        for (org.jscience.social.linguistics.loaders.tigerxml.T t : nt.getTerminals()) {
            phrase.addWord(convertT(t, language));
        }
        
        return phrase;
    }

    /**
     * Converts a TigerXML T (Terminal) to a Word.
     */
    public static Word convertT(org.jscience.social.linguistics.loaders.tigerxml.T t, Language language) {
        Word word = new Word(language, t.getWord());
        word.setPartOfSpeech(mapPosToEnum(t.getPos()));
        word.setLemma(t.getLemma());
        return word;
    }

    private static Word.PartOfSpeech mapPosToEnum(String pos) {
        if (pos == null) return Word.PartOfSpeech.UNKNOWN;
        // Mapping STTS (Stuttgart-TÃ¼bingen-Tagset) often used in TigerXML
        return switch (pos.toUpperCase()) {
            case "NN", "NE" -> Word.PartOfSpeech.NOUN;
            case "VVFIN", "VVIMP", "VVINF", "VVIZU", "VVPP", "VAFIN", "VAIMP", "VAINF", "VAPP", "VMFIN", "VMINF", "VMPP" -> Word.PartOfSpeech.VERB;
            case "ADJA", "ADJD" -> Word.PartOfSpeech.ADJECTIVE;
            case "ADV", "PAV" -> Word.PartOfSpeech.ADVERB;
            case "PPER", "PDS", "PDAT", "PIS", "PIAT", "PIDAT", "PPOSS", "PPOSAT", "PRELS", "PRELAT", "PRF", "PWS", "PWAT", "PWAV" -> Word.PartOfSpeech.PRONOUN;
            case "APPR", "APPRART", "APPO", "APZR" -> Word.PartOfSpeech.PREPOSITION;
            case "KON", "KOUI", "KOUS", "KOKAS" -> Word.PartOfSpeech.CONJUNCTION;
            case "ITJ" -> Word.PartOfSpeech.INTERJECTION;
            case "ART" -> Word.PartOfSpeech.ARTICLE;
            default -> Word.PartOfSpeech.UNKNOWN;
        };
    }

    private static Phrase.Type mapCatToType(String cat) {
        if (cat == null) return Phrase.Type.UNKNOWN;
        return switch (cat.toUpperCase()) {
            case "NP" -> Phrase.Type.NOUN_PHRASE;
            case "VP" -> Phrase.Type.VERB_PHRASE;
            case "AP", "ADJP" -> Phrase.Type.ADJECTIVE_PHRASE;
            case "AVP", "ADVP" -> Phrase.Type.ADVERB_PHRASE;
            case "PP" -> Phrase.Type.PREPOSITIONAL_PHRASE;
            case "S", "CS" -> Phrase.Type.CLAUSE;
            default -> Phrase.Type.UNKNOWN;
        };
    }
}

