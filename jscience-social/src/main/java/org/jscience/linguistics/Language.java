package org.jscience.linguistics;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a natural language.
 */
public class Language {

        private final String name;
        private final Set<Phoneme> phonemes;
        private final Set<Character> graphemes;

        public Language(String name) {
                this.name = name;
                this.phonemes = new HashSet<>();
                this.graphemes = new HashSet<>();
        }

        public String getName() {
                return name;
        }

        public void addPhoneme(Phoneme phoneme) {
                phonemes.add(phoneme);
        }

        public Set<Phoneme> getPhonemes() {
                return Collections.unmodifiableSet(phonemes);
        }

        public void addGrapheme(char c) {
                graphemes.add(c);
        }

        public Set<Character> getGraphemes() {
                return Collections.unmodifiableSet(graphemes);
        }
}
