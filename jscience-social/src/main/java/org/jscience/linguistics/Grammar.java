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

import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents the grammar rules for parsing a language.
 * <p>
 * A grammar defines the syntactic structure of a language through
 * a set of production rules.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see <a href="https://en.wikipedia.org/wiki/Grammar">Grammar (Wikipedia)</a>
 */
@Persistent
public class Grammar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Language language;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Rule> rules;

    @Attribute
    private String name;

    @Attribute
    private String description;

    /**
     * Creates a grammar for a language.
     *
     * @param language the language
     * @throws NullPointerException if language is null
     */
    public Grammar(Language language) {
        this.language = Objects.requireNonNull(language, "Language cannot be null");
        this.rules = new HashSet<>();
    }

    /**
     * Creates a named grammar.
     *
     * @param language the language
     * @param name     the grammar name
     */
    public Grammar(Language language, String name) {
        this(language);
        this.name = name;
    }

    /**
     * Returns the language.
     * @return the language
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Returns the grammar name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the grammar name.
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the description.
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns an unmodifiable set of rules.
     * @return the rules
     */
    public Set<Rule> getRules() {
        return Collections.unmodifiableSet(rules);
    }

    /**
     * Adds a rule to the grammar.
     * @param rule the rule to add
     */
    public void addRule(Rule rule) {
        if (rule != null) {
            rules.add(rule);
        }
    }

    /**
     * Removes a rule from the grammar.
     * @param rule the rule to remove
     * @return true if removed
     */
    public boolean removeRule(Rule rule) {
        return rules.remove(rule);
    }

    /**
     * Returns the number of rules.
     * @return rule count
     */
    public int getRuleCount() {
        return rules.size();
    }

    /**
     * Clears all rules.
     */
    public void clearRules() {
        rules.clear();
    }

    @Override
    public String toString() {
        return String.format("Grammar[%s, %s, %d rules]", 
            name != null ? name : "unnamed", language.getName(), rules.size());
    }
}
