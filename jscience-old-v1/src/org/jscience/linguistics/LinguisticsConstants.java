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
 * The LinguisticsConstants class provides several useful constatnts for
 * linguistics.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class LinguisticsConstants extends Object {
    //http://www.phon.ucl.ac.uk/home/sampa/home.htm
    //for phonetics (voyels, consonants, approximants...)
    //Unless and until ISO 10646/Unicode is implemented internationally, SAMPA and the proposed X-SAMPA (Extended SAMPA) constitute the best international collaborative basis for a standard machine-readable encoding of phonetic notation.
    //also see IPACodes.java
    /** DOCUMENT ME! */
    public final static int UNKNOWN = 0;

    //place of articulation, http://en.wikipedia.org/wiki/Place_of_articulation
    /** DOCUMENT ME! */
    public final static int BILABIAL = 1; //1<<0;

    /** DOCUMENT ME! */
    public final static int LABIODENTAL = 2; //1<<1;

    /** DOCUMENT ME! */
    public final static int LINGUOLABIAL = 4; //1<<2;

    /** DOCUMENT ME! */
    public final static int DENTAL = 8; //1<<3;

    /** DOCUMENT ME! */
    public final static int ALVEOLAR = 16; //1<<4;

    /** DOCUMENT ME! */
    public final static int POSTALVEOLAR = 32;

    /** DOCUMENT ME! */
    public final static int PALATAL = 64;

    /** DOCUMENT ME! */
    public final static int RETROFLEX = 128;

    /** DOCUMENT ME! */
    public final static int VELAR = 256;

    /** DOCUMENT ME! */
    public final static int UVULAR = 512;

    /** DOCUMENT ME! */
    public final static int NASAL = 1024;

    /** DOCUMENT ME! */
    public final static int PHARYNGEAL = 2048;

    /** DOCUMENT ME! */
    public final static int EPIGLOTTAL = 4096;

    /** DOCUMENT ME! */
    public final static int GLOTTAL = 8192;

    //manner of articulation? http://en.wikipedia.org/wiki/Manner_of_articulation
    /** DOCUMENT ME! */
    public final static int NASALS = 1;

    /** DOCUMENT ME! */
    public final static int PLOSIVES = 2;

    /** DOCUMENT ME! */
    public final static int FRICATIVES = 4;

    /** DOCUMENT ME! */
    public final static int APPROXIMANTS = 8;

    /** DOCUMENT ME! */
    public final static int TAPS = 16;

    /** DOCUMENT ME! */
    public final static int LATERALS = 32;

    /** DOCUMENT ME! */
    public final static int TRILLS = 64;

    /** DOCUMENT ME! */
    public final static int EJECTIVES = 128;

    /** DOCUMENT ME! */
    public final static int IMPLOSIVES = 256;

    /** DOCUMENT ME! */
    public final static int CLICKS = 512;

    //analysis levels
    /** DOCUMENT ME! */
    public final static int PHONETIC = 1; //accoustic

    /** DOCUMENT ME! */
    public final static int LEXICAL = 2; //also accounts for prosodic

    /** DOCUMENT ME! */
    public final static int SYNTACTIC = 3;

    /** DOCUMENT ME! */
    public final static int SEMANTIC = 4;

    /** DOCUMENT ME! */
    public final static int PRAGMATIC = 5;

    //Languages super families
    /** DOCUMENT ME! */
    public final static int AUSTRIC = 1;

    /** DOCUMENT ME! */
    public final static int INDO_PACIFIC = 2;

    /** DOCUMENT ME! */
    public final static int URAL_ALTAIC = 4;

    /** DOCUMENT ME! */
    public final static int PONTIC = 8;

    /** DOCUMENT ME! */
    public final static int IBERO_CAUCASIAN = 16;

    /** DOCUMENT ME! */
    public final static int ALARODIAN = 32;

    /** DOCUMENT ME! */
    public final static int AMERIND = 64;

    /** DOCUMENT ME! */
    public final static int MACRO_SIOUAN = 128;

    /** DOCUMENT ME! */
    public final static int KONGO_SAHARAN = 256;

    //Super-Families that would include Indo-European
    /** DOCUMENT ME! */
    public final static int EURASIATIC = 512;

    /** DOCUMENT ME! */
    public final static int NOSTRATIC = 1024;

    /** DOCUMENT ME! */
    public final static int PROTO_WORLD = 2048;

    //written languages systems
    /** DOCUMENT ME! */
    public final static int ARABIC_SCRIPT = 1;

    /** DOCUMENT ME! */
    public final static int ARMENIAN_ALPHABET = 2;

    /** DOCUMENT ME! */
    public final static int BENGALI_SCRIPT = 3; //Brahmic family

    /** DOCUMENT ME! */
    public final static int DEVANAGARI_SCRIPT = 4; //Brahmic family

    /** DOCUMENT ME! */
    public final static int CYRILLIC_ALPHABET = 5;

    /** DOCUMENT ME! */
    public final static int GEORGIAN_ALPHABET = 6;

    /** DOCUMENT ME! */
    public final static int GREEK_ALPHABET = 7;

    /** DOCUMENT ME! */
    public final static int HAN_CHARACTERS_AND_DERIVATIVES = 8;

    /** DOCUMENT ME! */
    public final static int HEBREW_ALPHABET = 9;

    /** DOCUMENT ME! */
    public final static int LATIN_ALPHABET_AND_DERIVATIVES = 10;

    //http://en.wikipedia.org/wiki/Writing_systems
    //other coding (perhaps we should remove the previous one)
    /** DOCUMENT ME! */
    public final static int LOGOGRAPHIC = 1; //morpheme 	Chinese hanzi

    /** DOCUMENT ME! */
    public final static int SYLLABIC = 2; //syllable 	Japanese kana

    /** DOCUMENT ME! */
    public final static int ALPHABETIC = 3; //phoneme (consonant or vowel) 	Latin alphabet

    /** DOCUMENT ME! */
    public final static int ABUGIDA = 4; //phoneme (consonant+vowel) 	Indian Devan?gar?

    /** DOCUMENT ME! */
    public final static int ABJAD = 5; //phoneme (consonant) 	Arabic alphabet

    /** DOCUMENT ME! */
    public final static int FEATURAL = 6; //phonetic feature 	Korean hangul

    //Paul Grice pragmatics maxims:
    /**
     * DOCUMENT ME!
     */
    public final static int TRUTH_MAXIM = 1; //Maxim of Quality: Truth

    /**
     * DOCUMENT ME!
     */
    public final static int INFORMATION_MAXIM = 2; //Maxim of Quantity: Information

    /**
     * DOCUMENT ME!
     */
    public final static int RELEVANCE_MAXIM = 4; //Maxim of Relation: Relevance

    /**
     * DOCUMENT ME!
     */
    public final static int CLARITY_MAXIM = 8; //Maxim of Manner: Clarity

    //Austin's speech acts
    /**
     * DOCUMENT ME!
     */
    public final static int ILLOCUTIONARY_ACT = 1;

    /**
     * DOCUMENT ME!
     */
    public final static int PERLOCUTIONARY_ACT = 2;

    /**
     * DOCUMENT ME!
     */
    public final static int LOCUTIONARY_ACT = 4;

    //sentences classifications:
    /**
     * DOCUMENT ME!
     */
    public final static int DECLARATIVE = 1; //A declarative sentence or declaration, the most common type, commonly makes a statement: I am going home.

    /**
     * DOCUMENT ME!
     */
    public final static int INTERROGATIVE = 2; //An interrogative sentence or question is commonly used to request information � When are you going to work? � but sometimes not; see rhetorical question.

    /**
     * DOCUMENT ME!
     */
    public final static int EXCLAMATORY = 4; //An exclamatory sentence or exclamation is generally a more emphatic form of statement: What a wonderful day this is!

    /**
     * DOCUMENT ME!
     */
    public final static int IMPERATIVE = 8; //An imperative sentence or command is ordinarily used to make a demand or request: Go do your homework.

    //http://en.wikipedia.org/wiki/Kinesics
    //may be we should have a Kineme class along with kinesics constants:
    //    * Emblems a substitute for words and phrases
    //    * Illustrators accompany or reinforce verbal messages
    //    * Affect Displays Show emotion
    //    * Regulators Control the flow and pace of communication
    //    * Adaptors Release physical or emotional tension
}
