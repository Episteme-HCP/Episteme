/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Importer for Metamath database files (.mm).
 * <p>
 * Parses Metamath databases and extracts:
 * <ul>
 * <li>Constants ($c statements)</li>
 * <li>Variables ($v statements)</li>
 * <li>Axioms ($a statements)</li>
 * <li>Provable statements ($p statements)</li>
 * </ul>
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MetamathImporter implements FormalSystemImporter {

    private static final Pattern CONSTANT_PATTERN = Pattern.compile("\\$c\\s+(.+?)\\s*\\$\\.");
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$v\\s+(.+?)\\s*\\$\\.");
    private static final Pattern AXIOM_PATTERN = Pattern.compile("(\\w+)\\s+\\$a\\s+(.+?)\\s*\\$\\.");
    private static final Pattern PROVABLE_PATTERN = Pattern.compile("(\\w+)\\s+\\$p\\s+(.+?)\\s*\\$\\.");

    @Override
    public Map<String, Object> importSystem(Reader reader) throws IOException, ParseException {
        Map<String, Object> result = new HashMap<>();
        BufferedReader br = new BufferedReader(reader);
        StringBuilder content = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            // Skip comments (lines starting with $()
            if (!line.trim().startsWith("$(")) {
                content.append(line).append(" ");
            }
        }

        String text = content.toString();

        // Extract constants
        List<String> constants = new ArrayList<>();
        Matcher constantMatcher = CONSTANT_PATTERN.matcher(text);
        while (constantMatcher.find()) {
            String[] tokens = constantMatcher.group(1).trim().split("\\s+");
            for (String token : tokens) {
                constants.add(token);
            }
        }
        result.put("constants", constants);

        // Extract variables
        List<String> variables = new ArrayList<>();
        Matcher variableMatcher = VARIABLE_PATTERN.matcher(text);
        while (variableMatcher.find()) {
            String[] tokens = variableMatcher.group(1).trim().split("\\s+");
            for (String token : tokens) {
                variables.add(token);
            }
        }
        result.put("variables", variables);

        // Extract axioms
        Map<String, String> axioms = new HashMap<>();
        Matcher axiomMatcher = AXIOM_PATTERN.matcher(text);
        while (axiomMatcher.find()) {
            axioms.put(axiomMatcher.group(1), axiomMatcher.group(2).trim());
        }
        result.put("axioms", axioms);

        // Extract provable statements (theorems)
        Map<String, String> theorems = new HashMap<>();
        Matcher provableMatcher = PROVABLE_PATTERN.matcher(text);
        while (provableMatcher.find()) {
            theorems.put(provableMatcher.group(1), provableMatcher.group(2).trim());
        }
        result.put("theorems", theorems);

        return result;
    }

    @Override
    public String[] getSupportedExtensions() {
        return new String[] { ".mm" };
    }
}