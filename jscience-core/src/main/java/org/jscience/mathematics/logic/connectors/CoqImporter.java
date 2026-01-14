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

package org.jscience.mathematics.logic.connectors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Importer for Coq Proof Assistant files (.v).
 * <p>
 * Parses Coq vernacular files and extracts:
 * <ul>
 * <li>Module definitions</li>
 * <li>Axioms and Parameters</li>
 * <li>Theorems and Lemmas</li>
 * <li>Definitions</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CoqImporter implements FormalSystemImporter {

    private static final Pattern MODULE_PATTERN = Pattern.compile("Module\\s+(\\w+)\\s*\\.");
    private static final Pattern AXIOM_PATTERN = Pattern.compile("(?:Axiom|Parameter)\\s+(\\w+)\\s*:\\s*(.+?)\\.");
    private static final Pattern THEOREM_PATTERN = Pattern.compile("(?:Theorem|Lemma)\\s+(\\w+)\\s*:\\s*(.+?)\\.");
    private static final Pattern DEFINITION_PATTERN = Pattern.compile("Definition\\s+(\\w+)\\s*:=\\s*(.+?)\\.");

    @Override
    public Map<String, Object> importSystem(Reader reader) throws IOException, ParseException {
        Map<String, Object> result = new HashMap<>();
        BufferedReader br = new BufferedReader(reader);
        StringBuilder content = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            // Skip comments
            if (!line.trim().startsWith("(*")) {
                content.append(line).append("\n");
            }
        }

        String text = content.toString();

        // Extract module name
        Matcher moduleMatcher = MODULE_PATTERN.matcher(text);
        if (moduleMatcher.find()) {
            result.put("module", moduleMatcher.group(1));
        }

        // Extract axioms
        Map<String, String> axioms = new HashMap<>();
        Matcher axiomMatcher = AXIOM_PATTERN.matcher(text);
        while (axiomMatcher.find()) {
            axioms.put(axiomMatcher.group(1), axiomMatcher.group(2));
        }
        result.put("axioms", axioms);

        // Extract theorems
        Map<String, String> theorems = new HashMap<>();
        Matcher theoremMatcher = THEOREM_PATTERN.matcher(text);
        while (theoremMatcher.find()) {
            theorems.put(theoremMatcher.group(1), theoremMatcher.group(2));
        }
        result.put("theorems", theorems);

        // Extract definitions
        Map<String, String> definitions = new HashMap<>();
        Matcher defMatcher = DEFINITION_PATTERN.matcher(text);
        while (defMatcher.find()) {
            definitions.put(defMatcher.group(1), defMatcher.group(2));
        }
        result.put("definitions", definitions);

        return result;
    }

    @Override
    public String[] getSupportedExtensions() {
        return new String[] { ".v", ".vo" };
    }
}

