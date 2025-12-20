package org.jscience.linguistics;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents the grammar of a language.
 */
public class Grammar {

    private final Language language;
    private final Set<String> rules; // String rules for simplicity in V1

    public Grammar(Language language) {
        this.language = language;
        this.rules = new HashSet<>();
    }

    public Language getLanguage() {
        return language;
    }

    public void addRule(String rule) {
        rules.add(rule);
    }

    public Set<String> getRules() {
        return Collections.unmodifiableSet(rules);
    }
}
