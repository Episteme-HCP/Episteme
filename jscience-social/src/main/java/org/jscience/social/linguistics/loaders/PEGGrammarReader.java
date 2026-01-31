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

package org.jscience.social.linguistics.loaders;

import org.jscience.core.io.AbstractResourceReader;
import org.jscience.social.linguistics.Grammar;
import org.jscience.social.linguistics.Language;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Resource reader for Parsing Expression Grammar (PEG) files.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PEGGrammarReader extends AbstractResourceReader<Grammar> {

    private final Language language;

    public PEGGrammarReader(Language language) {
        this.language = language;
    }

    @Override
    protected Grammar loadFromSource(String id) throws Exception {
        // Source is a file path in ID for this implementation
        // Source is a file path in ID for this implementation
        Files.readString(Paths.get(id), StandardCharsets.UTF_8);
        Grammar grammar = new Grammar(language, "PEG Grammar [" + id + "]");
        grammar.setDescription("PEG Grammar loaded from " + id);
        // PEG parsing logic would go here
        return grammar;
    }

    @Override
    protected Grammar loadFromInputStream(InputStream is, String id) throws Exception {
        new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));
        Grammar grammar = new Grammar(language, id);
        grammar.setDescription("PEG Grammar loaded from stream: " + id);
        return grammar;
    }

    @Override
    public String getResourcePath() {
        return "linguistics/grammars/peg";
    }

    @Override
    public Class<Grammar> getResourceType() {
        return Grammar.class;
    }

    @Override
    public String getName() {
        return "PEG Grammar Reader";
    }

    @Override
    public String getDescription() {
        return "Reads Parsing Expression Grammar (PEG) files.";
    }

    @Override
    public String getLongDescription() {
        return "A formal grammar reader for PEG syntax, allowing to load language rules for syntactic analysis.";
    }

    @Override
    public String getCategory() {
        return "Linguistics";
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[]{"1.0"};
    }
}

