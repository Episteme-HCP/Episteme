package org.jscience.linguistics;

import java.util.*;
import java.util.regex.*;

/**
 * Poetry metric analysis (prosody).
 */
public final class MetricScanner {

    private MetricScanner() {}

    public enum MetricalFoot {
        IAMB("u/"),           // unstressed-stressed
        TROCHEE("/u"),        // stressed-unstressed
        DACTYL("/uu"),        // stressed-unstressed-unstressed
        ANAPEST("uu/"),       // unstressed-unstressed-stressed
        SPONDEE("//"),        // stressed-stressed
        PYRRHIC("uu");        // unstressed-unstressed

        private final String pattern;

        MetricalFoot(String pattern) {
            this.pattern = pattern;
        }

        public String getPattern() {
            return pattern;
        }
    }

    public record MetricalAnalysis(
        String line,
        String stressPattern,     // / for stressed, u for unstressed
        MetricalFoot dominantFoot,
        int footCount,
        String meterName,
        List<String> scanningNotes
    ) {}

    public record RhymeAnalysis(
        List<String> lines,
        String rhymeScheme,       // e.g., "ABAB"
        Map<Character, List<String>> rhymeGroups
    ) {}

    // Simple syllable stress dictionary (very simplified)
    private static final Map<String, String> STRESS_DICT = Map.ofEntries(
        Map.entry("the", "u"), Map.entry("a", "u"), Map.entry("an", "u"),
        Map.entry("and", "u"), Map.entry("but", "u"), Map.entry("or", "u"),
        Map.entry("in", "u"), Map.entry("on", "u"), Map.entry("at", "u"),
        Map.entry("to", "u"), Map.entry("of", "u"), Map.entry("for", "u"),
        Map.entry("is", "u"), Map.entry("was", "u"), Map.entry("be", "/"),
        Map.entry("love", "/"), Map.entry("heart", "/"), Map.entry("night", "/"),
        Map.entry("day", "/"), Map.entry("light", "/"), Map.entry("dark", "/"),
        Map.entry("shall", "/"), Map.entry("compare", "u/"),
        Map.entry("summer", "/u"), Map.entry("lovely", "/u"),
        Map.entry("temperate", "/uu"), Map.entry("eternal", "u/u"),
        Map.entry("beauty", "/u"), Map.entry("forever", "u/u")
    );

    /**
     * Scans a line of poetry for metrical pattern.
     */
    public static MetricalAnalysis scanLine(String line) {
        List<String> notes = new ArrayList<>();
        StringBuilder pattern = new StringBuilder();
        
        String[] words = line.toLowerCase().replaceAll("[^a-z' ]", "").split("\\s+");
        
        for (String word : words) {
            if (word.isEmpty()) continue;
            
            if (STRESS_DICT.containsKey(word)) {
                pattern.append(STRESS_DICT.get(word));
            } else {
                // Heuristic: estimate stress from word structure
                String estimated = estimateStress(word);
                pattern.append(estimated);
                notes.add(word + " (estimated: " + estimated + ")");
            }
        }
        
        String stressPattern = pattern.toString();
        MetricalFoot foot = identifyDominantFoot(stressPattern);
        int footCount = countFeet(stressPattern, foot);
        String meterName = getMeterName(foot, footCount);
        
        return new MetricalAnalysis(line, stressPattern, foot, footCount, meterName, notes);
    }

    /**
     * Analyzes rhyme scheme of a stanza.
     */
    public static RhymeAnalysis analyzeRhymeScheme(List<String> lines) {
        Map<String, Character> endingToLetter = new HashMap<>();
        StringBuilder scheme = new StringBuilder();
        Map<Character, List<String>> groups = new HashMap<>();
        char nextLetter = 'A';
        
        for (String line : lines) {
            String ending = extractRhymeEnding(line);
            
            // Check if this ending rhymes with an existing one
            Character letter = null;
            for (Map.Entry<String, Character> entry : endingToLetter.entrySet()) {
                if (rhymes(ending, entry.getKey())) {
                    letter = entry.getValue();
                    break;
                }
            }
            
            if (letter == null) {
                letter = nextLetter++;
                endingToLetter.put(ending, letter);
            }
            
            scheme.append(letter);
            groups.computeIfAbsent(letter, k -> new ArrayList<>()).add(line.trim());
        }
        
        return new RhymeAnalysis(lines, scheme.toString(), groups);
    }

    /**
     * Identifies poetic form from metrics and rhyme.
     */
    public static String identifyForm(List<String> lines) {
        RhymeAnalysis rhyme = analyzeRhymeScheme(lines);
        String scheme = rhyme.rhymeScheme();
        int lineCount = lines.size();
        
        // Check iambic pentameter
        boolean iambicPentameter = true;
        for (String line : lines) {
            MetricalAnalysis m = scanLine(line);
            if (m.dominantFoot() != MetricalFoot.IAMB || m.footCount() != 5) {
                iambicPentameter = false;
                break;
            }
        }
        
        // Identify form
        if (lineCount == 14 && iambicPentameter) {
            if (scheme.equals("ABABCDCDEFEFGG")) {
                return "Shakespearean Sonnet";
            } else if (scheme.matches("ABBAABBA.*")) {
                return "Petrarchan Sonnet";
            }
            return "Sonnet";
        }
        
        if (lineCount == 3 && scheme.equals("ABA")) {
            return "Tercet";
        }
        
        if (lineCount == 4) {
            if (scheme.equals("ABAB")) return "Quatrain (alternate rhyme)";
            if (scheme.equals("ABBA")) return "Quatrain (envelope rhyme)";
            if (scheme.equals("AABB")) return "Couplet pair";
        }
        
        if (scheme.matches("(AABB)+")) return "Heroic Couplets";
        
        // Check for limerick (AABBA)
        if (lineCount == 5 && scheme.equals("AABBA")) {
            return "Limerick";
        }
        
        return "Free verse or unknown form";
    }

    /**
     * Counts syllables in a word.
     */
    public static int countSyllables(String word) {
        word = word.toLowerCase().replaceAll("[^a-z]", "");
        if (word.isEmpty()) return 0;
        
        int count = 0;
        boolean prevVowel = false;
        String vowels = "aeiouy";
        
        for (int i = 0; i < word.length(); i++) {
            boolean isVowel = vowels.indexOf(word.charAt(i)) >= 0;
            if (isVowel && !prevVowel) count++;
            prevVowel = isVowel;
        }
        
        // Silent e adjustment
        if (word.endsWith("e") && count > 1) count--;
        if (word.endsWith("le") && word.length() > 2 && 
            vowels.indexOf(word.charAt(word.length() - 3)) < 0) count++;
        
        return Math.max(1, count);
    }

    private static String estimateStress(String word) {
        int syllables = countSyllables(word);
        if (syllables == 1) return "/";  // Assume monosyllables stressed
        if (syllables == 2) return "/u"; // Default trochaic
        if (syllables == 3) return "/uu"; // Default dactylic
        return "/u".repeat(syllables / 2);
    }

    private static MetricalFoot identifyDominantFoot(String pattern) {
        int iambs = countPattern(pattern, "u/");
        int trochees = countPattern(pattern, "/u");
        int dactyls = countPattern(pattern, "/uu");
        int anapests = countPattern(pattern, "uu/");
        
        int max = Math.max(Math.max(iambs, trochees), Math.max(dactyls, anapests));
        if (max == iambs) return MetricalFoot.IAMB;
        if (max == trochees) return MetricalFoot.TROCHEE;
        if (max == dactyls) return MetricalFoot.DACTYL;
        return MetricalFoot.ANAPEST;
    }

    private static int countPattern(String str, String pattern) {
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(pattern, idx)) != -1) {
            count++;
            idx += pattern.length();
        }
        return count;
    }

    private static int countFeet(String pattern, MetricalFoot foot) {
        return countPattern(pattern, foot.name().equals("IAMB") ? "u/" : 
                          foot.name().equals("TROCHEE") ? "/u" : "u/");
    }

    private static String getMeterName(MetricalFoot foot, int count) {
        String prefix = switch (count) {
            case 1 -> "Mono";
            case 2 -> "Di";
            case 3 -> "Tri";
            case 4 -> "Tetra";
            case 5 -> "Penta";
            case 6 -> "Hexa";
            case 7 -> "Hepta";
            case 8 -> "Octa";
            default -> count + "-";
        };
        
        String footName = switch (foot) {
            case IAMB -> "iambic";
            case TROCHEE -> "trochaic";
            case DACTYL -> "dactylic";
            case ANAPEST -> "anapestic";
            default -> "meter";
        };
        
        return prefix + footName + " meter";
    }

    private static String extractRhymeEnding(String line) {
        String[] words = line.trim().toLowerCase().split("\\s+");
        if (words.length == 0) return "";
        
        String lastWord = words[words.length - 1].replaceAll("[^a-z]", "");
        
        // Get last stressed vowel and everything after
        String vowels = "aeiouy";
        int lastVowel = -1;
        for (int i = lastWord.length() - 1; i >= 0; i--) {
            if (vowels.indexOf(lastWord.charAt(i)) >= 0) {
                lastVowel = i;
                break;
            }
        }
        
        return lastVowel >= 0 ? lastWord.substring(lastVowel) : lastWord;
    }

    private static boolean rhymes(String ending1, String ending2) {
        if (ending1.equals(ending2)) return true;
        
        // Simple rhyme: same ending sounds
        int minLen = Math.min(ending1.length(), ending2.length());
        return minLen >= 2 && 
               ending1.substring(ending1.length() - minLen)
                      .equals(ending2.substring(ending2.length() - minLen));
    }
}
