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

import org.jscience.linguistics.loaders.tigerxml.core.TBuilder;
import org.jscience.linguistics.loaders.tigerxml.tools.StringTools;

import org.w3c.dom.Element;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Represents a terminal node in a syntax tree.
 * *
 * @see GraphNode
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class T extends GraphNode {

    private static final long serialVersionUID = 1L;

    /** DOCUMENT ME! */
    private String pos;

    /** DOCUMENT ME! */
    private String morph;

    /** DOCUMENT ME! */
    private String word;

    /** DOCUMENT ME! */
    private String lemma;

    /** DOCUMENT ME! */
    private int position;

    /**
     * Creates a new T object.
     */
    public T() {
        super();
        init();
    }

    /**
     * Creates a new T object.
     *
     * @param tElement The DOM element
     * @param sent     The parent sentence
     * @param posIndex The position index in the sentence
     */
    public T(Element tElement, Sentence sent, int posIndex) {
        super();
        init();
        TBuilder.buildT(this, sent, tElement, posIndex);
    }

    /**
     * Creates a new T object with verbosity.
     */
    public T(Element tElement, Sentence sent, int posIndex, int verbosity) {
        super();
        init();
        this.verbosity = verbosity;
        TBuilder.buildT(this, sent, tElement, posIndex);
    }

    private void init() {
        this.pos = "";
        this.morph = "";
        this.word = "";
        this.lemma = "";
        this.position = -1;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public String getText() {
        return word;
    }

    @Override
    protected void buildTreeString(int depth, StringBuilder sb) {
        for (int i = 0; i < depth; i++) {
            sb.append("|   ");
        }
        sb.append("|--").append(getEdge2Mother()).append("--");
        sb.append("\"").append(word).append("\" <").append(getId()).append(", ").append(pos).append(">\n");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLemma() {
        return this.lemma;
    }

    /**
     * DOCUMENT ME!
     *
     * @param lemma DOCUMENT ME!
     */
    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPos() {
        return this.pos;
    }

    /**
     * DOCUMENT ME!
     *
     * @param newpos DOCUMENT ME!
     */
    public void setPos(String newpos) {
        this.pos = newpos;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getMorph() {
        return this.morph;
    }

    /**
     * DOCUMENT ME!
     *
     * @param newmorph DOCUMENT ME!
     */
    public void setMorph(String newmorph) {
        this.morph = newmorph;
    }

    /**
     * Returns the String of this Terminal.
     *
     * @return The String representation of this Terminal.
     */
    public String getWord() {
        return this.word;
    }

    /**
     * DOCUMENT ME!
     *
     * @param newWord DOCUMENT ME!
     */
    public void setWord(String newWord) {
        this.word = newWord;
    }

    /**
     * DOCUMENT ME!
     *
     * @param position DOCUMENT ME!
     */
    public void setPosition(int position) {
        this.position = position;
        setIndex(position);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPosition() {
        return this.position;
    }

    /**
     * DOCUMENT ME!
     *
     * @param out_xml DOCUMENT ME!
     */
    protected void print2XML(FileWriter out_xml) {
        try {
            out_xml.write(" <t ");
            out_xml.write("id=\"" + this.getId() + "\" ");
            out_xml.write("word=\"" +
                StringTools.cleanXMLString(this.getWord()) + "\" ");
            out_xml.write("pos=\"" + this.getPos() + "\" ");

            if (!this.getMorph().equals("")) {
                out_xml.write("morph=\"" + this.getMorph() + "\" ");
            }

            if (!this.getLemma().equals("")) {
                out_xml.write("lemma=\"" + this.getLemma() + "\" ");
            }

            out_xml.write("/>\n");
        } catch (IOException e) {
            System.err.println("Error occurred while writing: " + e.toString());
            e.printStackTrace();
        }
    }
}
