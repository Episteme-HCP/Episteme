/*
 * T.java
 *
 * Created on July 6, 2003, 1:42 AM
 *
 * Copyright (C) 2003 Oezguer Demir <oeze@coli.uni-sb.de>,
 *                    Vaclav Nemcik <vicky@coli.uni-sb.de>,
 *                    Hajo Keffer <hajokeffer@coli.uni-sb.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.jscience.linguistics.loaders.tigerxml;

import org.jscience.linguistics.loaders.tigerxml.core.TBuilder;
import org.jscience.linguistics.loaders.tigerxml.tools.StringTools;

import org.w3c.dom.Element;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Represents a terminal node in a syntax tree.
 *
 * @author <a href="mailto:oeze@coli.uni-sb.de">Oezguer Demir</a>
 * @author <a href="mailto:vicky@coli.uni-sb.de">Vaclav Nemcik</a>
 * @author <a href="mailto:hajokeffer@coli.uni-sb.de">Hajo Keffer</a>
 * @version 1.84 $Id: T.java,v 1.3 2007-10-23 18:21:34 virtualcall Exp $
 *
 * @see GraphNode
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
    protected void print2Xml(FileWriter out_xml) {
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
