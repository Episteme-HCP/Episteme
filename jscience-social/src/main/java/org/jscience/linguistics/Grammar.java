package org.jscience.linguistics;

import java.util.Collections;
import java.util.Set;


/**
 * The Grammar class holds the information about the rules needed to parse
 * a language.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Grammar extends Object {
    /** DOCUMENT ME! */
    private Language language;

    /** DOCUMENT ME! */
    private Set rules;

/**
     * Creates a new Grammar object.
     *
     * @param language DOCUMENT ME!
     */
    public Grammar(Language language) {
        if (language != null) {
            this.language = language;
            rules = Collections.EMPTY_SET;
        } else {
            throw new IllegalArgumentException(
                "The Grammar constructor doesn't accept null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getRules() {
        return rules;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rule DOCUMENT ME!
     */
    public void addRule(Rule rule) {
        if (rule != null) {
            rules.add(rule);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param rule DOCUMENT ME!
     */
    public void removeRule(Rule rule) {
        rules.remove(rule);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Language getLanguage() {
        return language;
    }
}
