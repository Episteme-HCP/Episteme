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

package org.jscience.linguistics;

/**
 * Constants and enumerations for linguistics.
 * <p>
 * Provides classifications for phonetics, language families, 
 * writing systems, pragmatics, and speech acts.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class LinguisticsConstants {

    private LinguisticsConstants() {}

    /**
     * Place of articulation.
     */
    public enum ArticulationPlace {
        BILABIAL, LABIODENTAL, LINGUOLABIAL, DENTAL, ALVEOLAR, 
        POSTALVEOLAR, PALATAL, RETROFLEX, VELAR, UVULAR, 
        PHARYNGEAL, EPIGLOTTAL, GLOTTAL, UNKNOWN
    }

    /**
     * Manner of articulation.
     */
    public enum ArticulationManner {
        NASAL, PLOSIVE, FRICATIVE, APPROXIMANT, TAP, FLAP, 
        LATERAL, TRILL, EJECTIVE, IMPLOSIVE, CLICK, UNKNOWN
    }

    /**
     * Levels of linguistic analysis.
     */
    public enum AnalysisLevel {
        PHONETIC, LEXICAL, SYNTACTIC, SEMANTIC, PRAGMATIC
    }

    /**
     * Major language families.
     */
    public enum LanguageFamily {
        AUSTRIC, INDO_PACIFIC, URAL_ALTAIC, PONTIC, IBERO_CAUCASIAN, 
        ALARODIAN, AMERIND, MACRO_SIOUAN, KONGO_SAHARAN, EURASIATIC, 
        NOSTRATIC, PROTO_WORLD, INDO_EUROPEAN, AFROASIATIC, SINO_TIBETAN, 
        NIGER_CONGO, AUSTRONESIAN, DRAVIDIAN, OTHER
    }

    /**
     * Writing systems.
     */
    public enum WritingSystem {
        LOGOGRAPHIC, SYLLABIC, ALPHABETIC, ABUGIDA, ABJAD, FEATURAL, UNKNOWN
    }

    /**
     * Grice's Maxims of Pragmatics.
     */
    public enum PragmaticMaxim {
        QUALITY_TRUTH, QUANTITY_INFORMATION, RELATION_RELEVANCE, MANNER_CLARITY
    }

    /**
     * Austin's Speech Acts.
     */
    public enum SpeechAct {
        LOCUTIONARY, ILLOCUTIONARY, PERLOCUTIONARY
    }

    /**
     * Sentence types.
     */
    public enum SentenceType {
        DECLARATIVE, INTERROGATIVE, EXCLAMATORY, IMPERATIVE
    }

    /**
     * Kinesics (non-verbal communication).
     */
    public enum Kineme {
        EMBLEM, ILLUSTRATOR, AFFECT_DISPLAY, REGULATOR, ADAPTOR
    }
}
