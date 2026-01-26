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

package org.jscience.linguistics.loaders.tigerxml;

import org.jscience.linguistics.loaders.tigerxml.core.CorpusBuilder;
import org.jscience.linguistics.loaders.tigerxml.core.TigerXmlDocument;
import org.jscience.linguistics.loaders.tigerxml.tools.GeneralTools;
import org.jscience.linguistics.loaders.tigerxml.tools.SyncMMAX;
import org.w3c.dom.Element;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a linguistic corpus in TIGER-XML format.
 * <p>
 * A corpus contains a sequence of annotated sentences, each with its own
 * syntactic tree structure (composed of terminals and non-terminals).
 * It also stores metadata and annotation specifications from the {@code <head>} section.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @version 2.0 (Modernized)
 * @see Sentence
 * @see AnnotationMetadata
 */
public class Corpus implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The unique identifier of the corpus. */
    private String id;

    /** The list of sentences in the corpus. */
    private final List<Sentence> sentences;

    /** Cached flat text representation. */
    private String text;

    /** Metadata from the {@code <corpus>} tag. */
    private final Map<String, String> corpusAttrs;

    /** Annotation specifications from the {@code <head>} section. */
    private AnnotationMetadata annotationMetadata;

    /** Identification hash code. */
    private int hashCode;

    /** Diagnostic verbosity level. */
    private int verbosity = 0;

    /**
     * Creates an empty Corpus.
     */
    public Corpus() {
        this.sentences = new ArrayList<>();
        this.corpusAttrs = new HashMap<>();
        this.id = "";
    }

    /**
     * Loads a corpus from a TIGER-XML file.
     *
     * @param fileName the XML file path.
     */
    public Corpus(String fileName) {
        this();
        load(fileName);
    }

    /**
     * Loads a corpus from a TIGER-XML file with specified verbosity.
     *
     * @param fileName  the XML file path.
     * @param verbosity verbosity level (0-5).
     */
    public Corpus(String fileName, int verbosity) {
        this();
        this.verbosity = verbosity;
        load(fileName);
    }

    /**
     * Creates a corpus from a DOM root element.
     *
     * @param root the {@code <corpus>} element.
     */
    public Corpus(Element root) {
        this();
        CorpusBuilder.buildCorpus(this, root);
    }

    private void load(String fileName) {
        TigerXmlDocument doc = new TigerXmlDocument(fileName, verbosity);
        CorpusBuilder.buildCorpus(this, doc.getDocumentRoot());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = Objects.requireNonNull(id);
    }

    public int getVerbosity() {
        return verbosity;
    }

    public void setVerbosity(int verbosity) {
        this.verbosity = verbosity;
    }

    public int getSentenceCount() {
        return sentences.size();
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public Sentence getSentence(int index) {
        return sentences.get(index);
    }

    public Sentence getSentence(String id) {
        return sentences.stream()
                .filter(s -> s.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public void addSentence(Sentence sentence) {
        sentences.add(sentence);
    }

    public T getTerminal(String id) {
        for (Sentence s : sentences) {
            T t = s.getT(id);
            if (t != null) return t;
        }
        return null;
    }

    public NT getNT(String id) {
        for (Sentence s : sentences) {
            NT nt = s.getNT(id);
            if (nt != null) return nt;
        }
        return null;
    }

    public GraphNode getGraphNode(String id) {
        NT nt = getNT(id);
        return (nt != null) ? nt : getTerminal(id);
    }

    /**
     * Finds a node by its MMAX span string.
     */
    public GraphNode getGraphNodeBySpan(String span) {
        List<String> argSpanList = SyncMMAX.parseSpan(span);
        GraphNode best = null;
        int minMed = Integer.MAX_VALUE;

        for (Sentence s : sentences) {
            for (NT nt : s.getNTs()) {
                List<String> currentSpan = SyncMMAX.parseSpan(nt.getSpan());
                int med = GeneralTools.minEditDistance(new ArrayList<>(currentSpan), new ArrayList<>(argSpanList));
                if (med < minMed) {
                    minMed = med;
                    best = nt;
                }
            }
            for (T t : s.getTs()) {
                List<String> currentSpan = List.of(t.getId().toString());
                int med = GeneralTools.minEditDistance(new ArrayList<>(currentSpan), new ArrayList<>(argSpanList));
                if (med < minMed) {
                    minMed = med;
                    best = t;
                }
            }
        }
        return best;
    }

    public List<NT> getAllNTs() {
        return sentences.stream()
                .flatMap(s -> s.getNTs().stream())
                .map(nt -> (NT)nt)
                .collect(Collectors.toList());
    }

    public List<T> getAllTs() {
        return sentences.stream()
                .flatMap(s -> s.getTs().stream())
                .map(t -> (T)t)
                .collect(Collectors.toList());
    }

    public List<GraphNode> getAllGraphNodes() {
        List<GraphNode> all = new ArrayList<>(getAllNTs());
        all.addAll(getAllTs());
        return all;
    }

    public void addAttribute(String name, String value) {
        corpusAttrs.put(name, value);
    }

    public String getAttribute(String name) {
        return corpusAttrs.get(name);
    }

    public AnnotationMetadata getAnnotationMetadata() {
        return annotationMetadata;
    }

    public void setAnnotationMetadata(AnnotationMetadata metadata) {
        this.annotationMetadata = metadata;
    }

    public String getText() {
        if (text == null) {
            text = sentences.stream()
                    .map(Sentence::getText)
                    .collect(Collectors.joining("\n"));
        }
        return text;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = Objects.hash(id);
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Corpus)) return false;
        Corpus corpus = (Corpus) o;
        return Objects.equals(id, corpus.id);
    }

    public void serializeToDisk(String fileName) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(this);
        }
    }

    public static Corpus loadSerialized(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Corpus) ois.readObject();
        }
    }
}
