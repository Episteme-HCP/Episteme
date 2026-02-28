/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.linguistics;

import java.io.Serializable;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

/**
 * Graph structure for tracking word derivations and linguistic ancestry across languages.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class EtymologyGraph implements Serializable {

    private static final long serialVersionUID = 2L;

    public record EtymologyNode(String word, Language language, String meaning) implements Serializable {}

    public record Derivation(EtymologyNode source, EtymologyNode target, String type) implements Serializable {}

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Map<String, List<Derivation>> derivationMap = new HashMap<>();

    public EtymologyGraph() {}

    public void addDerivation(EtymologyNode source, EtymologyNode target, String type) {
        Objects.requireNonNull(source, "Source cannot be null");
        Objects.requireNonNull(target, "Target cannot be null");
        Derivation d = new Derivation(source, target, type);
        derivationMap.computeIfAbsent(target.word().toLowerCase(), k -> new ArrayList<>()).add(d);
    }

    public List<EtymologyNode> traceEtymology(String word, Language language) {
        List<EtymologyNode> path = new ArrayList<>();
        traceRecursive(word.toLowerCase(), language, path, new HashSet<>());
        return path;
    }

    private void traceRecursive(String word, Language language, List<EtymologyNode> path, Set<String> visited) {
        String key = word + "_" + language.getIsoCode();
        if (visited.contains(key)) return;
        visited.add(key);
        
        List<Derivation> derivations = derivationMap.get(word);
        if (derivations == null) return;
        
        for (Derivation d : derivations) {
            if (d.target().language().equals(language)) {
                path.add(d.target());
                path.add(d.source());
                traceRecursive(d.source().word(), d.source().language(), path, visited);
            }
        }
    }

    public List<EtymologyNode> findCognates(String word, Language language) {
        List<EtymologyNode> path = traceEtymology(word, language);
        if (path.isEmpty()) return Collections.emptyList();
        
        EtymologyNode root = path.get(path.size() - 1);
        List<EtymologyNode> cognates = new ArrayList<>();
        
        for (List<Derivation> derivations : derivationMap.values()) {
            for (Derivation d : derivations) {
                if (d.source().equals(root)) {
                    cognates.add(d.target());
                }
            }
        }
        return cognates;
    }
}

