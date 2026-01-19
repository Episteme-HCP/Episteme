package org.jscience.linguistics;

import java.util.*;

/**
 * Phonetic transcription to International Phonetic Alphabet (IPA).
 */
public final class PhoneticTranscriber {

    private PhoneticTranscriber() {}

    public enum Language {
        ENGLISH, FRENCH, GERMAN, SPANISH, ITALIAN
    }

    // English grapheme-to-phoneme rules (simplified)
    private static final Map<String, String> ENGLISH_IPA = Map.ofEntries(
        // Consonants
        Map.entry("th", "θ"), Map.entry("ch", "tʃ"), Map.entry("sh", "ʃ"),
        Map.entry("ng", "ŋ"), Map.entry("ph", "f"), Map.entry("wh", "w"),
        Map.entry("ck", "k"), Map.entry("dg", "dʒ"),
        // Single consonants
        Map.entry("b", "b"), Map.entry("c", "k"), Map.entry("d", "d"),
        Map.entry("f", "f"), Map.entry("g", "g"), Map.entry("h", "h"),
        Map.entry("j", "dʒ"), Map.entry("k", "k"), Map.entry("l", "l"),
        Map.entry("m", "m"), Map.entry("n", "n"), Map.entry("p", "p"),
        Map.entry("q", "kw"), Map.entry("r", "ɹ"), Map.entry("s", "s"),
        Map.entry("t", "t"), Map.entry("v", "v"), Map.entry("w", "w"),
        Map.entry("x", "ks"), Map.entry("y", "j"), Map.entry("z", "z"),
        // Vowels (simplified)
        Map.entry("ee", "iː"), Map.entry("ea", "iː"), Map.entry("oo", "uː"),
        Map.entry("ou", "aʊ"), Map.entry("ow", "aʊ"), Map.entry("ai", "eɪ"),
        Map.entry("ay", "eɪ"), Map.entry("oi", "ɔɪ"), Map.entry("oy", "ɔɪ"),
        Map.entry("a", "æ"), Map.entry("e", "ɛ"), Map.entry("i", "ɪ"),
        Map.entry("o", "ɒ"), Map.entry("u", "ʌ")
    );

    // French IPA rules
    private static final Map<String, String> FRENCH_IPA = Map.ofEntries(
        Map.entry("ou", "u"), Map.entry("au", "o"), Map.entry("eau", "o"),
        Map.entry("ai", "ɛ"), Map.entry("ei", "ɛ"), Map.entry("eu", "ø"),
        Map.entry("oi", "wa"), Map.entry("on", "ɔ̃"), Map.entry("an", "ɑ̃"),
        Map.entry("in", "ɛ̃"), Map.entry("un", "œ̃"), Map.entry("ch", "ʃ"),
        Map.entry("gn", "ɲ"), Map.entry("j", "ʒ"), Map.entry("r", "ʁ"),
        Map.entry("c", "s"), Map.entry("ç", "s"),
        Map.entry("a", "a"), Map.entry("e", "ə"), Map.entry("i", "i"),
        Map.entry("o", "o"), Map.entry("u", "y")
    );

    /**
     * Transcribes text to IPA.
     */
    public static String transcribe(String text, Language language) {
        Map<String, String> rules = switch (language) {
            case ENGLISH -> ENGLISH_IPA;
            case FRENCH -> FRENCH_IPA;
            default -> ENGLISH_IPA;
        };
        
        StringBuilder result = new StringBuilder();
        String lower = text.toLowerCase();
        int i = 0;
        
        while (i < lower.length()) {
            boolean found = false;
            
            // Try longest matches first (3, 2, 1 characters)
            for (int len = 3; len >= 1 && !found; len--) {
                if (i + len <= lower.length()) {
                    String sub = lower.substring(i, i + len);
                    if (rules.containsKey(sub)) {
                        result.append(rules.get(sub));
                        i += len;
                        found = true;
                    }
                }
            }
            
            if (!found) {
                char c = lower.charAt(i);
                if (c == ' ') {
                    result.append(" ");
                } else if (Character.isLetter(c)) {
                    result.append(c); // Keep unknown letters
                }
                i++;
            }
        }
        
        return "/" + result.toString().trim() + "/";
    }

    /**
     * Gets IPA symbol description.
     */
    public static String describeSymbol(String ipaSymbol) {
        return switch (ipaSymbol) {
            case "θ" -> "voiceless dental fricative (English 'th' in 'think')";
            case "ð" -> "voiced dental fricative (English 'th' in 'this')";
            case "ʃ" -> "voiceless postalveolar fricative (English 'sh')";
            case "ʒ" -> "voiced postalveolar fricative (French 'j')";
            case "tʃ" -> "voiceless postalveolar affricate (English 'ch')";
            case "dʒ" -> "voiced postalveolar affricate (English 'j')";
            case "ŋ" -> "velar nasal (English 'ng')";
            case "ɹ" -> "alveolar approximant (English 'r')";
            case "ʁ" -> "voiced uvular fricative (French 'r')";
            case "ɲ" -> "palatal nasal (French 'gn')";
            case "iː" -> "close front unrounded vowel, long";
            case "ɪ" -> "near-close near-front unrounded vowel";
            case "uː" -> "close back rounded vowel, long";
            case "ʊ" -> "near-close near-back rounded vowel";
            case "ə" -> "schwa (unstressed neutral vowel)";
            case "æ" -> "near-open front unrounded vowel";
            case "ɛ" -> "open-mid front unrounded vowel";
            case "ɔ" -> "open-mid back rounded vowel";
            case "ɑ" -> "open back unrounded vowel";
            default -> "IPA symbol: " + ipaSymbol;
        };
    }

    /**
     * Counts syllables in transcribed IPA.
     */
    public static int countSyllables(String ipaTranscription) {
        // Count vowel nuclei
        String vowels = "aeiouɪʊɛɔæʌəɑøyœ";
        String cleaned = ipaTranscription.replace("/", "").replace("ː", "");
        
        int count = 0;
        boolean inVowel = false;
        
        for (char c : cleaned.toCharArray()) {
            boolean isVowel = vowels.indexOf(c) >= 0;
            if (isVowel && !inVowel) {
                count++;
            }
            inVowel = isVowel;
        }
        
        return Math.max(1, count);
    }
}
