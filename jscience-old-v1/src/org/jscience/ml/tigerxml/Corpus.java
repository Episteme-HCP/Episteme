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

package org.jscience.ml.tigerxml;

import org.jscience.ml.tigerxml.core.CorpusBuilder;
import org.jscience.ml.tigerxml.core.TigerXmlDocument;
import org.jscience.ml.tigerxml.tools.GeneralTools;
import org.jscience.ml.tigerxml.tools.SyncMMAX;
import org.w3c.dom.Element;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Represents the corpus including all syntax trees in the TIGER annotation.
 * <code>Corpus</code> objects contain all data structures contained in the
 * represented TIGER corpus.
 * <p/>
 * There is one instance of this class for each corpus. It contains all
 * <code>Sentence</code> objects of the corpus as an <code>ArrayList</code>,
 * which you can access using {@link #getSentences()}. You can also access
 * single Sentences directly or other elements of the Corpus by using the
 * methods implemented in this class. Sentences again have methods for
 * accessing structural elements such as non-terminal nodes and terminal
 * nodes.
 * <p/>
 * To obtain an <code>Corpus</code> object call the constructor
 * {@link #Corpus(StringcorpusFileName)} giving it the name of the TiGerXML
 * file from which to build the <code>Corpus</code> object.
 * <p/>
 * <b>Sample Usage</b>
 * <p/>
 * <code><pre>
 * import java.util.*;
 * <p/>
 * import org.jscience.ml.tigerxml.*;
 * <p/>
 * public class TestTigerAPI {
 * <p/>
 *   public static void main(String[] args) {
 * <p/>
 *     // Create a Corpus object by parsing the given xml file
 *     Corpus corpus = new Corpus("sample_TIGER.xml");
 * <p/>
 *     // Use the corpus object to print parts of the structure
 *     System.out.println("Corpus.getId: " + corpus.getId());
 * <p/>
 *     // All-sentences-loop
 *     for (int i = 0; i < corpus.getSentenceCount(); i++) {
 *       Sentence sent = corpus.getSentence(i);
 *       System.out.println("Sentence ID: " + sent.getId());
 *       System.out.println("NonTerminals: ");
 * <p/>
 *       // All-NTs-loop
 *       for (int j = 0; j < sent.getNTCount(); j++) {
 *         NT nt = sent.getNT(j);
 *         System.out.println("NT ID: " + nt.getId());
 *         System.out.println("   CAT: " + nt.getCat());
 *         System.out.println("   MOTHER: " + nt.getMother());
 *         System.out.println("   Edge2Mother: " + nt.getEdge2Mother());
 *       } // for j
 *     } // for i
 *   } // main
 * } // class
 * </pre>
 * </code>
 *
 * @author <a href="mailto:oeze@coli.uni-sb.de"> Oezguer Demir </a>
 * @author <a href="mailto:vicky@coli.uni-sb.de"> Vaclav Nemcik </a>
 * @author <a href="mailto:hajokeffer@coli.uni-sb.de"> Hajo Keffer </a>
 * @version 1.84
 *          $Id: Corpus.java,v 1.3 2007-10-21 21:08:31 virtualcall Exp $
 * @see Sentence
 * @see GraphNode
 * @see NT
 * @see T
 */
public class Corpus
        implements Serializable {

    /**
     * The ID of this corpus. Ex: "TIGERRelease1"
     */
    private String id;

    /**
     * The list of Sentence objects contained in this Corpus.
     */
    private ArrayList sentences;

    /**
     * The flat text string with no annotations.
     */
    private String text;

    /**
     * The annotation specification of this corpus as read from the header
     * <annotation> <feature...> <feature...> ... </annotation>
     * All attributes read from the <corpus> Element. <corpus att="val" ...>
     * <p/>
     * All attributes read from the <corpus> Element. <corpus att="val" ...>
     */
    ///  private header/////FOOO

    /**
     * All attributes read from the <corpus> Element. <corpus att="val" ...>
     */
    private HashMap corpusAttrs;

    /**
     * A unique integer value identifying this Corpus instance.
     */
    private int hashCode;

    /**
     * The higher this value the more process and debug information will
     * written to stderr.
     */
    private int verbosity = 0;

    /**
     * Creates an empty <code>Corpus</code> object. This constructor is only
     * useful for parsing a corpus file by hand instead of letting the org.jscience.ml.tigerxml
     * build it.
     * <p/>
     * Use  {@link #Corpus(StringcorpusFile)} instead.
     */
    public Corpus() {
        init();
    }

    /**
     * Creates a <code>Corpus</code> object and builds all data structures
     * parsing the given TiGerXML file using a DOM parser.
     *
     * @param corpusFileName The TiGerXML file to be parsed for building
     *                       the Corpus.
     */
    public Corpus(String corpusFileName) {
        init();
        TigerXmlDocument tigerDoc =
                new TigerXmlDocument(corpusFileName, this.verbosity);
        Element rootElement = tigerDoc.getDocumentRoot();
        CorpusBuilder.buildCorpus(this, rootElement);
        tigerDoc.reset();
        rootElement = null;
        tigerDoc = null;
    }

    private void init() {
        this.id = "";
        this.sentences = new ArrayList();
        this.text = null;
        this.hashCode = 0;
        this.corpusAttrs = new HashMap();
    }

    /**
     * Creates a <code>Corpus</code> object and builds all data structures
     * parsing the given TiGerXML file using a DOM parser. Additionally, the
     * level of verbosity for this <code>Corpus</code> instance is specified.
     *
     * @param corpusFileName The TiGerXML file to be parsed for building
     *                       the Corpus.
     * @param verbosity      The higher this value the more process and debug
     *                       information will written to stderr.
     */
    public Corpus(String corpusFileName, int verbosity) {
        init();
        this.verbosity = verbosity;
        TigerXmlDocument tigerDoc =
                new TigerXmlDocument(corpusFileName, this.verbosity);
        Element rootElement = tigerDoc.getDocumentRoot();
        CorpusBuilder.buildCorpus(this, rootElement);
        tigerDoc.reset();
        rootElement = null;
        tigerDoc = null;
    }

    /**
     * Creates a <code>Corpus</code> object from a root <code>DOM Element</code>.
     * Use this constructor if the XML file itself is already parsed and the
     * document is available as a DOM object.
     *
     * @param rootElement The root <code>Element</code> of
     *                    the corpus XML document
     */
    public Corpus(Element rootElement) {
        CorpusBuilder.buildCorpus(this, rootElement);
    } // Corpus()

    /**
     * Returns the ID of this corpus as parsed from the XML file. The ID of the
     * corpus is specified by the attribute <code>"id"</code> in the element
     * </code>"corpus"</code> of the corpus document (TiGerXML).
     *
     * @return The ID (String) of this <code>Corpus</code>.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Sets the ID of this corpus.
     *
     * @param passId The ID of this corpus.
     */
    public void setId(String passId) {
        this.id = passId;
    }

    /**
     * Gets the currently set level of verbosity of this instance. The higher
     * the value the more information is written to stderr.
     *
     * @return The level of verbosity.
     */
    public int getVerbosity() {
        return this.verbosity;
    }

    /**
     * Sets the currently set level of verbosity of this instance. The higher
     * the value the more information is written to stderr:
     * 0: Only error messages<br/>
     * 1: + Basic progress information and warnings<br/>
     * 2: + More progress information<br/>
     * 3: + Time stats<br/>
     * 4: + Detailed progress information<br/>
     * 5: + Debugging messages<br/>
     *
     * @param verbosity The level of verbosity.
     */
    public void setVerbosity(int verbosity) {
        this.verbosity = verbosity;
    }

    /**
     * Returns the number of sentences objects in this corpus. The
     * returned number is equal to the length of the <code>ArrayList</code>
     * containing the <code>Sentence</code> objects of this <code>Corpus</code>
     * object.
     *
     * @return An integer denoting the number of sentences in this corpus.
     */
    public int getSentenceCount() {
        return this.sentences.size();
    }

    /**
     * Returns the number of sentences objects in this corpus. The
     * returned number is equal to the length of the <code>ArrayList</code>
     * containing the <code>Sentence</code> objects of this <code>Corpus</code>
     * object.
     *
     * @return An integer denoting the number of sentences in this corpus.
     * @deprecated As of org.jscience.ml.tigerxml 1.1 - use {@link #getSentenceCount()} instead.
     */
    public int getNoOfSentences() {
        return this.sentences.size();
    }

    /**
     * Returns the <code>T</code> which has the given ID. Returns null if the
     * search fails. If the sentence of the <code>T</code> is known,
     * {@link Sentence#getTerminal(Stringid)} can be used to retrieve the
     * wanted <code>NT</code>.
     *
     * @param id The ID of the T to be found.
     * @return The T that is identified by ID or <code>null</code> if the search
     *         fails.
     */
    public T getTerminal(String id) {
        return this.getT(id);
    }

    /**
     * Returns the <code>GraphNode</code> which has the given ID. Returns null if
     * the search fails. If the sentence of the <code>GraphNode</code> is known,
     * {@link Sentence#getGraphNode(Stringid)} can be used to retrieve the
     * wanted <code>GraphNode</code>.
     *
     * @param id The ID of the <code>GraphNode</code> to be found.
     * @return The <code>GraphNode</code> that is identified by ID or
     *         <code>null</code> if the search fails.
     */
    public GraphNode getGraphNode(String id) {
        GraphNode out;
        out = this.getNT(id);
        if (out != null) {
            return out;
        }
        out = this.getT(id);
        if (out != null) {
            return out;
        }
        return null;
    }

    /**
     * Finds the <code>GraphNode</code> which has the most similar span to
     * the given span. This method is useful for finding a <code>GraphNode</code>
     * corresponding to a Markable given from another anotation of the same
     * text corpus, for example by an MMAX annotation.<p>
     * <p/>
     * As a measure for similarity the Minimum Edit Distance is used.
     *
     * @param span The span to be approximated by the returned
     *             <code>GraphNode</code>.
     * @return The <code>GraphNode</code> approximating the given span the
     *         closest.
     * @see org.jscience.ml.tigerxml.tools.GeneralTools#minEditDistance(ArrayListlistA,ArrayListlistB)
     */
    public GraphNode getGraphNodeBySpan(String span) {
        ArrayList allGraphNodes = this.getAllGraphNodes();
        ArrayList argSpanList = SyncMMAX.parseSpan(span);
        GraphNode closestGraphNode = null; // the closest GraphNode so far
        int min = 32768; // the smallest MED so far
        for (int i = 0; i < allGraphNodes.size(); i++) {
            GraphNode gn = (GraphNode) allGraphNodes.get(i);
            if (gn.isTerminal() == false) {
                ArrayList currentSpanList = SyncMMAX.parseSpan(((NT) gn).getSpan());
                int med = GeneralTools.minEditDistance(currentSpanList, argSpanList);
                if (med < min) {
                    min = med;
                    closestGraphNode = gn;
                } // if med
            } // if gn
            else {
                ArrayList currentSpanList = SyncMMAX.parseSpan(((T) gn).getId());
                int med = GeneralTools.minEditDistance(currentSpanList, argSpanList);
                if (med < min) {
                    min = med;
                    closestGraphNode = gn;
                } // if med
            } // else
        } // for i
        return closestGraphNode;
    } // getGraphNodeBySpan

    /**
     * Finds and returns the <code<GraphNode</code> which has the most similar
     * span to the given <code>Markable</code>. This method allows to map MMAX
     * <code>Markable</code>s to NEGRA <code>GraphNode</code>s.<p>
     *
     * @param mark The <code>Markable</code> to match.
     * @param id   The ID of the NT to be found.
     * @param id   The ID of the NT to be found.
     * @return The NT that is identified by ID or <code>null</code> if the search
     *         fails.
     * @see #getGraphNodeBySpan(String)
     *      <p/>
     *      Returns the <code>NT</code> which has the given ID. Returns
     *      <code>null</code> if the search fails. If the sentence of the
     *      <code>NT</code> is known,
     *      {@link Sentence#getNT(Stringid)} can be used to retrieve the
     *      wanted <code>NT</code>. This might save some runtime.
     *      <p/>
     *      Returns the <code>NT</code> which has the given ID. Returns
     *      <code>null</code> if the search fails. If the sentence of the
     *      <code>NT</code> is known,
     *      {@link Sentence#getNT(Stringid)} can be used to retrieve the
     *      wanted <code>NT</code>. This might save some runtime.
     */
//  public GraphNode getGraphNode(Markable mark) {
//    String span = mark.getSpan();
//    return this.getGraphNodeBySpan(span);
//  }

    /**
     * Returns the <code>NT</code> which has the given ID. Returns
     * <code>null</code> if the search fails. If the sentence of the
     * <code>NT</code> is known,
     * {@link Sentence#getNT(Stringid)} can be used to retrieve the
     * wanted <code>NT</code>. This might save some runtime.
     *
     * @param id The ID of the NT to be found.
     * @return The NT that is identified by ID or <code>null</code> if the search
     *         fails.
     */
    public NT getNT(String id) {
        // try to get the T directly from its sentence
        NT nt = null;
        String guessedSentID = id.substring(0, id.indexOf("_"));
        Sentence guessedSent = this.getSentence(guessedSentID);
        if (guessedSent != null) {
            nt = guessedSent.getNT(id);
        }
        if (nt != null) {
            return nt;
        } else {
            // trying to get NT directly failed: search corpus exhaustively
            for (int i = 0; i < this.sentences.size(); i++) {
                Sentence currentSentence = (Sentence) this.sentences.get(i);
                if (currentSentence.getNT(id) != null) {
                    return currentSentence.getNT(id);
                } // if
            } // for i
        } // else
        return null;
    } // getNT()

    /**
     * Returns all <code>NT</code> objects contained in this corpus. The returned
     * NTs in the order of the XML corpus file. In order to have the list ordered
     * by linear precedence, use
     * {@link org.jscience.ml.tigerxml.tools.GeneralTools#sortNodes(ArrayListnodes)}.
     *
     * @return All NTs contained in this <code>Corpus</code>.
     */
    public ArrayList getAllNTs() {
        ArrayList allNTs = new ArrayList();
        for (int i = 0; i < this.sentences.size(); i++) {
            Sentence currentSent = (Sentence) this.sentences.get(i);
            allNTs.addAll(currentSent.getNTs());
        } // for i
        return allNTs;
    } // getAllNTs()

    /**
     * Returns all <code>T</code> objects contained in this corpus. The returned
     * Ts in the order of the XML corpus file. In order to have the list ordered
     * by linear precedence, use {@link
     * org.jscience.ml.tigerxml.tools.GeneralTools#sortTerminals(ArrayListunsortedTerminals)}.
     *
     * @return All Ts contained in this <code>Corpus</code>.
     */
    public ArrayList getAllTs() {
        ArrayList allTs = new ArrayList();
        for (int i = 0; i < this.sentences.size(); i++) {
            Sentence currentSent = (Sentence) this.sentences.get(i);
            allTs.addAll(currentSent.getTs());
        } // for i
        return allTs;
    } // getAllTs()

    /**
     * Returns all <code>GraphNode</code> objects contained in this corpus. The
     * returned GraphNodes are in the order of the XML corpus file. In order
     * to have the list ordered by linear precedence, use
     * {@link org.jscience.ml.tigerxml.tools.GeneralTools#sortNodes(ArrayListnodes)}.
     * <p/>
     * The returned list does not contain the VROOT.<p>
     * Ordering by class:<br>
     * All <code>NT</code> objects of the corpus are followed by all
     * <code>T</code> object of the corpus.
     *
     * @return All Ts contained in this <code>Corpus</code>.
     */
    public ArrayList getAllGraphNodes() {
        ArrayList allGraphNodes = new ArrayList();
        allGraphNodes.addAll(this.getAllNTs());
        allGraphNodes.addAll(this.getAllTs());
        return allGraphNodes;
    }

    /**
     * Returns the <code>T</code> which has the given ID. Returns null if the
     * search fails. If the sentence of the <code>T</code> is known,
     * {@link Sentence#getTerminal(Stringid)} can be used to retrieve the
     * wanted <code>T</code>.
     *
     * @param id The ID of the <code>T</code> to be found.
     * @return The <code>T</code> that is identified by ID or <code>null</code>
     *         if the search fails.
     */
    public T getT(String id) {
        // try to get the T directly from its sentence
        T t = null;
        String guessedSentID = id.substring(0, id.indexOf("_"));
        Sentence guessedSent = this.getSentence(guessedSentID);
        if (guessedSent != null) {
            t = guessedSent.getT(id);
        }
        if (t != null) {
            return t;
        } else {
            // trying to get T directly failed: search corpus exhaustively
            for (int i = 0; i < this.sentences.size(); i++) {
                Sentence currentSentence = (Sentence) this.sentences.get(i);
                ArrayList ts = currentSentence.getTs();
                for (int j = 0; j < ts.size(); j++) {
                    T currentT = (T) ts.get(j);
                    if (currentT.getId().equalsIgnoreCase(id)) {
                        return currentT;
                    } // if
                } // for j
            } // for i
        } // else
        return null;
    }

    /**
     * Returns all Sentences of this Corpus as an ArrayList.
     *
     * @return All Sentences of this Corpus as an ArrayList.
     */
    public ArrayList getSentences() {
        return this.sentences;
    }

    /**
     * Returns the <code>Sentence</code> object with index <code>i</code>.
     * Sentence indices start with index 0 in the <code>Corpus</code>.
     *
     * @param i The index of the <code>Sentence</code>.
     * @return The Sentence with index <code>i</code>.
     */
    public Sentence getSentence(int i) {
        return (Sentence) this.sentences.get(i);
    }

    /**
     * Returns the Sentence identified by id. Returns <code>null</code> if
     * look-up fails.
     *
     * @param id The id (String) of the Sentence.
     * @return The Sentence with ID id. Return null if look-up fails.
     */
    public Sentence getSentence(String id) {
        // Try to guess
        try {
            int index = Integer.parseInt(id.substring(1)) - 1;
            Sentence currentSentence = (Sentence) this.sentences.get(index);
            if (currentSentence.getId().equalsIgnoreCase(id)) {
                return currentSentence;
            }
        } catch (NumberFormatException e) {
        } catch (IndexOutOfBoundsException e) {
        }
        // Try thoroughly
        for (int i = 0; i < this.sentences.size(); i++) {
            Sentence currentSentence = (Sentence) this.sentences.get(i);
            if (currentSentence.getId().equalsIgnoreCase(id)) {
                return currentSentence;
            }
        } // for i
        return null;
    }

    /**
     * Appends a given <code>Sentence</code> to this instances sentence list.
     *
     * @param sent The <code>Sentence</code> to be appended.
     */
    public void addSentence(Sentence sent) {
        this.sentences.add(sent);
    }

    /**
     * Add an attribute to this <code>Corpus</code> instance. For example,
     * the ID.
     *
     * @param name  The name of the attribute.
     * @param value The value of the attribute.
     */
    public void addAttribute(String name, String value) {
        this.corpusAttrs.put(name, value);
    }

    /**
     * Returns the value of this <code>Corpus</code> instance's attribute stored
     * under <code>key</code>.
     *
     * @param name The name of the attribute.
     * @return The value of the requested attribute.
     */
    public String getAttribute(String name) {
        return (String) this.corpusAttrs.get(name);
    }

    /**
     * Returns all keys in this <code>Corpus</code> instance's attribute hash map
     * as an <code>ArrayList</code>
     *
     * @return All keys of the attribute map.
     */
    public ArrayList getAttributeNames() {
        return new ArrayList(this.corpusAttrs.keySet());
    }

    /**
     * Returns true if there is an attribute &quot;<code>name</code>&quot; in this
     * instances's attribute map.
     *
     * @param name The name of the attribute to be checked.
     * @return True if there is an attribure &quot;<code>name</code>&quot;.
     */
    public boolean hasAttribute(String name) {
        return this.corpusAttrs.containsKey(name);
    }

    /**
     * Returns the whole corpus text as a String. Note that punctuation marks
     * are treated as words - there is a space after each punctuation mark.
     *
     * @return The corpus text as a String.
     */
    public String getText() {
        if (this.text == null) {
            StringBuffer textBuffer = new StringBuffer();
            for (int i = 0; i < this.sentences.size(); i++) {
                ArrayList currentTermList = ((Sentence) this.sentences.get(i)).getTs();
                for (int j = 0; j < currentTermList.size(); j++) {
                    textBuffer.append(((T) currentTermList.get(j)).getWord());
                    textBuffer.append(" ");
                }
                textBuffer.append("\n");
            }
            this.text = textBuffer.toString();
        }
        return this.text;
    }

    /**
     * Returns the String representation of this Corpus - the ID.
     *
     * @return The ID as a String object.
     * @see #getId()
     */
    public String toString() {
        return this.id;
    }

    /**
     * Serializes and writes this <code>Corpus</code> instance to disk.
     * This is useful for large corpora that take very long to build or
     * where building consumes much memory. The written object can be
     * loaded using the static method
     * {@link #readSerializedFromDisk(StringfileName)} like this:
     * <code><pre>
     * Corpus corpus = Corpus.readSerializedFromDisk("corp.obj");
     * </pre></code>
     * Note that loading a serialized <code>Corpus</code> instance is not
     * necessarily faster than parsing the corresponding TIGER-XML file. But
     * it consumes only about half of the memory it would take to parse the
     * TIGER-XML file. Besides, it is conceivable that there are operations
     * on parsed <code>Corpus</code> instances which consume more ressources
     * than loading a previously built and serialized <corpus>Corpus</code>
     * instance.
     *
     * @param fileName The name of the file where the serialized
     *                 <code>Corpus</code> object will be stored.
     */
    public void serializeToDisk(String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    } // serializeToDisk()

    /**
     * Reads a previously serialized <code>Corpus</code> instance from disk.
     * This is useful for large corpora that take very long to parse or
     * consume much memory. The serialized object file can be written using
     * {@link #serializeToDisk(StringfileName)}
     *
     * @param fileName The name of the file where the serialized J48 object is
     *                 stored.
     * @return An instance of <code>Corpus</code> as deserialized from disk.
     */
    public static Corpus readSerializedFromDisk(String fileName) {
        Corpus corp = null;
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            corp = (Corpus) ois.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return corp;
    } // readFromDisk()

    /**
     * Returns true if the object is identical to this <code>Corpus</code>
     * object. Identity is determined by comparing the corpus IDs.
     *
     * @param obj The Java <code>Object</code> to which this is to be compared
     *            to.
     * @return True if the corpora are identical.
     * @see #getId()
     */
    public boolean equals(Object obj) {
        try {
            Corpus otherCorpus = (Corpus) obj;
            String thisID = this.id;
            String otherID = otherCorpus.getId();
            return thisID.equals(otherID);
        } catch (ClassCastException e) {
            return false;
        }
    } // method equals

    /**
     * Calculates and returns the hash code of this instance as an integer.
     *
     * @return The hash code of this instance as an integer.
     */
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = (this.toString()).hashCode();
        }
        return hashCode;
    }

    /**
     * Overides this intance's hash code by setting it to <code>code</code>.
     *
     * @param code The new hash code.
     */
    public void setHashCode(int code) {
        this.hashCode = code;
    }

    /**
     * Prints this corpus to the xml file named filename.
     *
     * @param xmlFileName The name of the XML file to be written.
     */
    public void print2xml(String xmlFileName) {
        this.print2Xml(xmlFileName, 0, this.getSentenceCount() - 1);
    }

    /**
     * Prints a range of this corpus to the xml file named filename.
     *
     * @param xmlFileName The name of the XML file to be written.
     * @param from        Starting from sentence with index <code>from</code>
     * @param to          Ending with sentence with index <code>to</code>
     */
    public void print2Xml(String xmlFileName, int from, int to) {
        if ((this.getSentenceCount() <= to) || (from > to)) {
            if (this.verbosity > 0) {
                System.err.println("org.jscience.ml.tigerxml.Corpus#print2Xml: Cannot write sentences "
                        + from + " to " + to + " (Corpus only contains "
                        + this.getSentenceCount() + " sentences).");
            }
        }
        if (this.verbosity > 0) {
            System.err.println("org.jscience.ml.tigerxml.Corpus: Writing XML file " + xmlFileName +
                    " ...");
        }
        try {
            FileWriter outXML = new FileWriter(xmlFileName);
            outXML.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
            outXML.write("\n");
            outXML.write("<corpus");
            outXML.write(this.getCorpusAttrs());
            outXML.write(">\n");
            /////outXML.write(Corpus.getHeader());
            outXML.write("<body>\n");
            for (int i = from; i <= to; i++) {
                (this.getSentence(i)).print2Xml(outXML);
            }
            outXML.write("</body>\n");
            outXML.write("</corpus>\n");
            outXML.close();
        } catch (IOException e) {
            System.err.println
                    ("Error occurred while writing: " +
                            e.toString());
        }
    } //Method print2Xml

    private String getCorpusAttrs() {
        String returnString = "";
        if (this.corpusAttrs != null) {
            Iterator attrs =
                    (corpusAttrs.keySet()).iterator();
            while (attrs.hasNext()) {
                String attr = (String) attrs.next();
                String value = (String) corpusAttrs.get(attr);
                returnString = returnString +
                        " " + attr + "=\"" + value + "\"";
            }
        }
        return returnString;
    }

    private static String getHeader() {
        return "<head>\n\n  <meta>\n    <format>NeGra format, version 3</format>\n  </meta>\n\n  <annotation>\n\n    <feature name=\"word\" domain=\"T\" />\n\n    <feature name=\"pos\" domain=\"T\">\n      <value name=\"$(\">Sonstige Satzzeichen; satzintern</value>\n      <value name=\"$,\">Komma</value>\n      <value name=\"$.\">Satzbeendende Interpunktion</value>\n      <value name=\"--\">&lt;Nicht zugeordnet&gt;</value>\n      <value name=\"ADJA\">Attributives Adjektiv</value>\n      <value name=\"ADJD\">Adverbiales oder praedikatives Adjektiv</value>\n      <value name=\"ADV\">Adverb</value>\n      <value name=\"APPO\">Postposition</value>\n      <value name=\"APPR\">Praeposition; Zirkumposition links</value>\n      <value name=\"APPRART\">Praeposition mit Artikel</value>\n      <value name=\"APZR\">Zirkumposition rechts</value>\n      <value name=\"ART\">Bestimmter oder unbestimmter Artikel</value>\n      <value name=\"CARD\">Kardinalzahl</value>\n      <value name=\"FM\">Fremdsprachliches Material</value>\n      <value name=\"ITJ\">Interjektion</value>\n      <value name=\"KOKOM\">Vergleichspartikel, ohne Satz</value>\n      <value name=\"KON\">Nebenordnende Konjunktion</value>\n      <value name=\"KOUI\">Unterordnende Konjunktion mit zu und Infinitiv</value>\n      <value name=\"KOUS\">Unterordnende Konjunktion mit Satz</value>\n      <value name=\"NE\">Eigennamen</value>\n      <value name=\"NN\">Normales Nomen</value>\n      <value name=\"NNE\">Verbindung aus Eigennamen und normalen Nomen</value>\n      <value name=\"PDAT\">Attribuierendes Demonstrativpronomen</value>\n      <value name=\"PDS\">Substituierendes Demonstrativpronomen</value>\n      <value name=\"PIAT\">Attribuierendes Indefinitpronomen</value>\n      <value name=\"PIDAT\">Attribuierendes Indefinitpronomen mit Determiner</value>\n      <value name=\"PIS\">Substituierendes Indefinitpronomen</value>\n      <value name=\"PPER\">Irreflexives Personalpronomen</value>\n      <value name=\"PPOSAT\">Attribuierendes Possessivpronomen</value>\n      <value name=\"PPOSS\">Substituierendes Possessivpronomen</value>\n      <value name=\"PRELAT\">Attribuierendes Relativpronomen</value>\n      <value name=\"PRELS\">Substituierendes Relativpronomen</value>\n      <value name=\"PRF\">Reflexives Personalpronomen</value>\n      <value name=\"PROAV\">Pronominaladverb</value>\n      <value name=\"PTKA\">Partikel bei Adjektiv oder Adverb</value>\n      <value name=\"PTKANT\">Antwortpartikel</value>\n      <value name=\"PTKNEG\">Negationspartikel</value>\n      <value name=\"PTKVZ\">Abgetrennter Verbzusatz</value>\n      <value name=\"PTKZU\">zu vor Infinitiv</value>\n      <value name=\"PWAT\">Attribuierendes Interrogativpronomen</value>\n      <value name=\"PWAV\">Adverbiales Interrogativ- oder Relativpronomen</value>\n      <value name=\"PWS\">Substituierendes Interrogativpronomen</value>\n      <value name=\"TRUNC\">Kompositions-Erstglied</value>\n      <value name=\"UNKNOWN\">Unbekanntes Tag aus Einlesen aus Korpusdatei</value>\n      <value name=\"VAFIN\">Finites Verb, aux</value>\n      <value name=\"VAIMP\">Imperativ, aux</value>\n      <value name=\"VAINF\">Infinitiv, aux</value>\n      <value name=\"VAPP\">Partizip Perfekt, aux</value>\n      <value name=\"VMFIN\">Finites Verb, modal</value>\n      <value name=\"VMINF\">Infinitiv, modal</value>\n      <value name=\"VMPP\">Partizip Perfekt, modal</value>\n      <value name=\"VVFIN\">Finites Verb, voll</value>\n      <value name=\"VVIMP\">Imperativ, voll</value>\n      <value name=\"VVINF\">Infinitiv, voll</value>\n      <value name=\"VVIZU\">Infinitiv mit zu, voll</value>\n      <value name=\"VVPP\">Partizip Perfekt, voll</value>\n      <value name=\"XY\">Nichtwort, Sonderzeichen</value>\n    </feature>\n\n    <feature name=\"morph\" domain=\"T\">\n      <value name=\"*\"/>\n      <value name=\"*.*.*\"/>\n      <value name=\"*.*.*.*.*\"/>\n      <value name=\"*.*.Akk.Pl.St\"/>\n      <value name=\"*.*.Akk.Pl.Sw\"/>\n      <value name=\"*.*.Dat.Pl.St\"/>\n      <value name=\"*.*.Dat.Pl.Sw\"/>\n      <value name=\"*.*.Gen.Pl.St\"/>\n      <value name=\"*.*.Gen.Pl.Sw\"/>\n      <value name=\"*.*.Nom.Pl.St\"/>\n      <value name=\"*.*.Nom.Pl.Sw\"/>\n      <value name=\"*.Akk.Pl\"/>\n      <value name=\"*.Akk.Pl.*\"/>\n      <value name=\"*.Akk.Pl.St\"/>\n      <value name=\"*.Akk.Pl.Sw\"/>\n      <value name=\"*.Akk.Sg\"/>\n      <value name=\"*.Akk.Sg.St\"/>\n      <value name=\"*.Dat.Pl\"/>\n      <value name=\"*.Dat.Pl.*\"/>\n      <value name=\"*.Dat.Pl.St\"/>\n      <value name=\"*.Dat.Pl.Sw\"/>\n      <value name=\"*.Dat.Sg\"/>\n      <value name=\"*.Fem.Akk.Sg.Mix\"/>\n      <value name=\"*.Fem.Akk.Sg.St\"/>\n      <value name=\"*.Fem.Akk.Sg.Sw\"/>\n      <value name=\"*.Fem.Dat.Sg.Mix\"/>\n      <value name=\"*.Fem.Dat.Sg.St\"/>\n      <value name=\"*.Fem.Dat.Sg.Sw\"/>\n      <value name=\"*.Fem.Gen.Sg.Sw\"/>\n      <value name=\"*.Fem.Nom.Sg.St\"/>\n      <value name=\"*.Fem.Nom.Sg.Sw\"/>\n      <value name=\"*.Gen.Pl\"/>\n      <value name=\"*.Gen.Pl.*\"/>\n      <value name=\"*.Gen.Pl.St\"/>\n      <value name=\"*.Gen.Pl.Sw\"/>\n      <value name=\"*.Gen.Sg\"/>\n      <value name=\"*.Masc.Akk.Sg.Mix\"/>\n      <value name=\"*.Masc.Akk.Sg.Sw\"/>\n      <value name=\"*.Masc.Dat.Sg.Mix\"/>\n      <value name=\"*.Masc.Dat.Sg.Sw\"/>\n      <value name=\"*.Masc.Gen.Sg.Mix\"/>\n      <value name=\"*.Masc.Gen.Sg.Sw\"/>\n      <value name=\"*.Masc.Nom.Sg.Mix\"/>\n      <value name=\"*.Masc.Nom.Sg.St\"/>\n      <value name=\"*.Masc.Nom.Sg.Sw\"/>\n      <value name=\"*.Neut.Akk.Sg.Sw\"/>\n      <value name=\"*.Neut.Dat.Sg.Mix\"/>\n      <value name=\"*.Neut.Dat.Sg.Sw\"/>\n      <value name=\"*.Neut.Gen.Sg.Sw\"/>\n      <value name=\"*.Neut.Nom.Sg.Mix\"/>\n      <value name=\"*.Neut.Nom.Sg.St\"/>\n      <value name=\"*.Neut.Nom.Sg.Sw\"/>\n      <value name=\"*.Nom.Pl\"/>\n      <value name=\"*.Nom.Pl.*\"/>\n      <value name=\"*.Nom.Pl.Mix\"/>\n      <value name=\"*.Nom.Pl.St\"/>\n      <value name=\"*.Nom.Pl.Sw\"/>\n      <value name=\"*.Nom.Sg\"/>\n      <value name=\"--\">not bound</value>\n      <value name=\"1.Akk.Pl\"/>\n      <value name=\"1.Akk.Sg\"/>\n      <value name=\"1.Dat.Pl\"/>\n      <value name=\"1.Dat.Sg\"/>\n      <value name=\"1.Pl.*.Akk\"/>\n      <value name=\"1.Pl.*.Dat\"/>\n      <value name=\"1.Pl.*.Nom\"/>\n      <value name=\"1.Pl.Past.Ind\"/>\n      <value name=\"1.Pl.Past.Konj\"/>\n      <value name=\"1.Pl.Pres.Ind\"/>\n      <value name=\"1.Sg.*.Akk\"/>\n      <value name=\"1.Sg.*.Dat\"/>\n      <value name=\"1.Sg.*.Nom\"/>\n      <value name=\"1.Sg.Past.Ind\"/>\n      <value name=\"1.Sg.Past.Konj\"/>\n      <value name=\"1.Sg.Pres.Ind\"/>\n      <value name=\"1.Sg.Pres.Konj\"/>\n      <value name=\"2.Akk.Sg\"/>\n      <value name=\"2.Dat.Sg\"/>\n      <value name=\"2.Pl.*.Nom\"/>\n      <value name=\"2.Pl.Pres.Ind\"/>\n      <value name=\"2.Sg.*.Akk\"/>\n      <value name=\"2.Sg.*.Dat\"/>\n      <value name=\"2.Sg.*.Nom\"/>\n      <value name=\"2.Sg.Past.Ind\"/>\n      <value name=\"2.Sg.Pres.Ind\"/>\n      <value name=\"2.Sg.Pres.Konj\"/>\n      <value name=\"3.Akk.Pl\"/>\n      <value name=\"3.Akk.Sg\"/>\n      <value name=\"3.Dat.Pl\"/>\n      <value name=\"3.Dat.Sg\"/>\n      <value name=\"3.Pl.*.Akk\"/>\n      <value name=\"3.Pl.*.Dat\"/>\n      <value name=\"3.Pl.*.Nom\"/>\n      <value name=\"3.Pl.Past.Ind\"/>\n      <value name=\"3.Pl.Past.Konj\"/>\n      <value name=\"3.Pl.Pres.Ind\"/>\n      <value name=\"3.Pl.Pres.Konj\"/>\n      <value name=\"3.Sg.Fem.Akk\"/>\n      <value name=\"3.Sg.Fem.Dat\"/>\n      <value name=\"3.Sg.Fem.Nom\"/>\n      <value name=\"3.Sg.Masc.Akk\"/>\n      <value name=\"3.Sg.Masc.Dat\"/>\n      <value name=\"3.Sg.Masc.Nom\"/>\n      <value name=\"3.Sg.Neut.Akk\"/>\n      <value name=\"3.Sg.Neut.Nom\"/>\n      <value name=\"3.Sg.Past.Ind\"/>\n      <value name=\"3.Sg.Past.Konj\"/>\n      <value name=\"3.Sg.Pres.Ind\"/>\n      <value name=\"3.Sg.Pres.Konj\"/>\n      <value name=\"Akk\"/>\n      <value name=\"Akk.Neut\"/>\n      <value name=\"Comp\"/>\n      <value name=\"Comp.*.Akk.Pl.Mix\"/>\n      <value name=\"Comp.*.Akk.Pl.St\"/>\n      <value name=\"Comp.*.Dat.Pl.St\"/>\n      <value name=\"Comp.*.Dat.Pl.Sw\"/>\n      <value name=\"Comp.*.Gen.Pl.St\"/>\n      <value name=\"Comp.*.Nom.Pl.St\"/>\n      <value name=\"Comp.*.Nom.Pl.Sw\"/>\n      <value name=\"Comp.Fem.Akk.Sg.Mix\"/>\n      <value name=\"Comp.Fem.Akk.Sg.St\"/>\n      <value name=\"Comp.Fem.Akk.Sg.Sw\"/>\n      <value name=\"Comp.Fem.Dat.Sg.Mix\"/>\n      <value name=\"Comp.Fem.Dat.Sg.St\"/>\n      <value name=\"Comp.Fem.Dat.Sg.Sw\"/>\n      <value name=\"Comp.Fem.Gen.Sg.Mix\"/>\n      <value name=\"Comp.Fem.Gen.Sg.Sw\"/>\n      <value name=\"Comp.Fem.Nom.Sg.Mix\"/>\n      <value name=\"Comp.Fem.Nom.Sg.Sw\"/>\n      <value name=\"Comp.Masc.Akk.Sg.Mix\"/>\n      <value name=\"Comp.Masc.Akk.Sg.St\"/>\n      <value name=\"Comp.Masc.Akk.Sg.Sw\"/>\n      <value name=\"Comp.Masc.Dat.Sg.Mix\"/>\n      <value name=\"Comp.Masc.Dat.Sg.St\"/>\n      <value name=\"Comp.Masc.Dat.Sg.Sw\"/>\n      <value name=\"Comp.Masc.Nom.Sg.Mix\"/>\n      <value name=\"Comp.Masc.Nom.Sg.St\"/>\n      <value name=\"Comp.Masc.Nom.Sg.Sw\"/>\n      <value name=\"Comp.Neut.Akk.Sg.Mix\"/>\n      <value name=\"Comp.Neut.Akk.Sg.St\"/>\n      <value name=\"Comp.Neut.Akk.Sg.Sw\"/>\n      <value name=\"Comp.Neut.Dat.Sg.Mix\"/>\n      <value name=\"Comp.Neut.Dat.Sg.St\"/>\n      <value name=\"Comp.Neut.Nom.Sg.Mix\"/>\n      <value name=\"Comp.Neut.Nom.Sg.Sw\"/>\n      <value name=\"Dat\"/>\n      <value name=\"Dat-Akk\">Dativ order Akkusativ</value>\n      <value name=\"Dat.Fem\"/>\n      <value name=\"Dat.Masc\"/>\n      <value name=\"Dat.Neut\"/>\n      <value name=\"Def.*.Akk.Pl\"/>\n      <value name=\"Def.*.Dat.Pl\"/>\n      <value name=\"Def.*.Gen.Pl\"/>\n      <value name=\"Def.*.Nom.Pl\"/>\n      <value name=\"Def.Fem.Akk.Sg\"/>\n      <value name=\"Def.Fem.Dat.Sg\"/>\n      <value name=\"Def.Fem.Gen.Sg\"/>\n      <value name=\"Def.Fem.Nom.Sg\"/>\n      <value name=\"Def.Masc.Akk.Sg\"/>\n      <value name=\"Def.Masc.Dat.Sg\"/>\n      <value name=\"Def.Masc.Gen.Sg\"/>\n      <value name=\"Def.Masc.Nom.Sg\"/>\n      <value name=\"Def.Neut.Akk.Sg\"/>\n      <value name=\"Def.Neut.Dat.Sg\"/>\n      <value name=\"Def.Neut.Gen.Sg\"/>\n      <value name=\"Def.Neut.Nom.Sg\"/>\n      <value name=\"Fem.Akk.Pl.*\"/>\n      <value name=\"Fem.Akk.Sg\"/>\n      <value name=\"Fem.Akk.Sg.*\"/>\n      <value name=\"Fem.Akk.Sg.St\"/>\n      <value name=\"Fem.Dat.Pl\"/>\n      <value name=\"Fem.Dat.Pl.*\"/>\n      <value name=\"Fem.Dat.Sg\"/>\n      <value name=\"Fem.Dat.Sg.*\"/>\n      <value name=\"Fem.Dat.Sg.Mix\"/>\n      <value name=\"Fem.Dat.Sg.Sw\"/>\n      <value name=\"Fem.Gen.Pl.*\"/>\n      <value name=\"Fem.Gen.Sg\"/>\n      <value name=\"Fem.Gen.Sg.*\"/>\n      <value name=\"Fem.Gen.Sg.Mix\"/>\n      <value name=\"Fem.Nom.Pl.*\"/>\n      <value name=\"Fem.Nom.Sg\"/>\n      <value name=\"Fem.Nom.Sg.*\"/>\n      <value name=\"Fem.Nom.Sg.St\"/>\n      <value name=\"Fem.Nom.Sg.Sw\"/>\n      <value name=\"Gen\"/>\n      <value name=\"Gen-Dat\">Dativ oder Genitiv</value>\n      <value name=\"Indef.Fem.Akk.Sg\"/>\n      <value name=\"Indef.Fem.Dat.Sg\"/>\n      <value name=\"Indef.Fem.Gen.Sg\"/>\n      <value name=\"Indef.Fem.Nom.Sg\"/>\n      <value name=\"Indef.Masc.Akk.Sg\"/>\n      <value name=\"Indef.Masc.Dat.Sg\"/>\n      <value name=\"Indef.Masc.Gen.Sg\"/>\n      <value name=\"Indef.Masc.Nom.Sg\"/>\n      <value name=\"Indef.Neut.Akk.Sg\"/>\n      <value name=\"Indef.Neut.Dat.Sg\"/>\n      <value name=\"Indef.Neut.Gen.Sg\"/>\n      <value name=\"Indef.Neut.Nom.Sg\"/>\n      <value name=\"Masc.Akk.Pl\"/>\n      <value name=\"Masc.Akk.Pl.*\"/>\n      <value name=\"Masc.Akk.Pl.St\"/>\n      <value name=\"Masc.Akk.Sg\"/>\n      <value name=\"Masc.Akk.Sg.*\"/>\n      <value name=\"Masc.Akk.Sg.Mix\"/>\n      <value name=\"Masc.Akk.Sg.Sw\"/>\n      <value name=\"Masc.Dat.Pl\"/>\n      <value name=\"Masc.Dat.Pl.*\"/>\n      <value name=\"Masc.Dat.Pl.St\"/>\n      <value name=\"Masc.Dat.Sg\"/>\n      <value name=\"Masc.Dat.Sg.*\"/>\n      <value name=\"Masc.Dat.Sg.Sw\"/>\n      <value name=\"Masc.Gen.Pl\"/>\n      <value name=\"Masc.Gen.Pl.*\"/>\n      <value name=\"Masc.Gen.Sg\"/>\n      <value name=\"Masc.Gen.Sg.*\"/>\n      <value name=\"Masc.Gen.Sg.Mix\"/>\n      <value name=\"Masc.Gen.Sg.Sw\"/>\n      <value name=\"Masc.Nom.Pl.*\"/>\n      <value name=\"Masc.Nom.Sg\"/>\n      <value name=\"Masc.Nom.Sg.*\"/>\n      <value name=\"Masc.Nom.Sg.Mix\"/>\n      <value name=\"Masc.Nom.Sg.St\"/>\n      <value name=\"Masc.Nom.Sg.Sw\"/>\n      <value name=\"Neut.Akk.Pl.*\"/>\n      <value name=\"Neut.Akk.Sg\"/>\n      <value name=\"Neut.Akk.Sg.*\"/>\n      <value name=\"Neut.Akk.Sg.St\"/>\n      <value name=\"Neut.Akk.Sg.Sw\"/>\n      <value name=\"Neut.Dat.Pl.*\"/>\n      <value name=\"Neut.Dat.Sg\"/>\n      <value name=\"Neut.Dat.Sg.*\"/>\n      <value name=\"Neut.Dat.Sg.St\"/>\n      <value name=\"Neut.Dat.Sg.Sw\"/>\n      <value name=\"Neut.Gen.Pl.*\"/>\n      <value name=\"Neut.Gen.Sg\"/>\n      <value name=\"Neut.Gen.Sg.*\"/>\n      <value name=\"Neut.Gen.Sg.Mix\"/>\n      <value name=\"Neut.Gen.Sg.Sw\"/>\n      <value name=\"Neut.Nom.Pl.*\"/>\n      <value name=\"Neut.Nom.Sg\"/>\n      <value name=\"Neut.Nom.Sg.*\"/>\n      <value name=\"Neut.Nom.Sg.St\"/>\n      <value name=\"Neut.Nom.Sg.Sw\"/>\n      <value name=\"Nom\">Nominativ</value>\n      <value name=\"Nom-Akk\">Akkusativ oder Nominativ</value>\n      <value name=\"Nom-Dat-Akk\">Dativ oder Akkusativ oder Nominativ</value>\n      <value name=\"Pl\"/>\n      <value name=\"Pos\"/>\n      <value name=\"Pos.*.*.*.*\"/>\n      <value name=\"Pos.*.Akk.Pl.Mix\"/>\n      <value name=\"Pos.*.Akk.Pl.St\"/>\n      <value name=\"Pos.*.Akk.Pl.Sw\"/>\n      <value name=\"Pos.*.Dat.Pl.Mix\"/>\n      <value name=\"Pos.*.Dat.Pl.St\"/>\n      <value name=\"Pos.*.Dat.Pl.Sw\"/>\n      <value name=\"Pos.*.Gen.Pl.Mix\"/>\n      <value name=\"Pos.*.Gen.Pl.St\"/>\n      <value name=\"Pos.*.Gen.Pl.Sw\"/>\n      <value name=\"Pos.*.Nom.Pl.Mix\"/>\n      <value name=\"Pos.*.Nom.Pl.St\"/>\n      <value name=\"Pos.*.Nom.Pl.Sw\"/>\n      <value name=\"Pos.Fem.Akk.Sg.Mix\"/>\n      <value name=\"Pos.Fem.Akk.Sg.St\"/>\n      <value name=\"Pos.Fem.Akk.Sg.Sw\"/>\n      <value name=\"Pos.Fem.Dat.Sg.Mix\"/>\n      <value name=\"Pos.Fem.Dat.Sg.St\"/>\n      <value name=\"Pos.Fem.Dat.Sg.Sw\"/>\n      <value name=\"Pos.Fem.Gen.Sg.Mix\"/>\n      <value name=\"Pos.Fem.Gen.Sg.St\"/>\n      <value name=\"Pos.Fem.Gen.Sg.Sw\"/>\n      <value name=\"Pos.Fem.Nom.Sg.Mix\"/>\n      <value name=\"Pos.Fem.Nom.Sg.St\"/>\n      <value name=\"Pos.Fem.Nom.Sg.Sw\"/>\n      <value name=\"Pos.Masc.Akk.Sg.Mix\"/>\n      <value name=\"Pos.Masc.Akk.Sg.St\"/>\n      <value name=\"Pos.Masc.Akk.Sg.Sw\"/>\n      <value name=\"Pos.Masc.Dat.Sg.Mix\"/>\n      <value name=\"Pos.Masc.Dat.Sg.St\"/>\n      <value name=\"Pos.Masc.Dat.Sg.Sw\"/>\n      <value name=\"Pos.Masc.Gen.Sg.Mix\"/>\n      <value name=\"Pos.Masc.Gen.Sg.St\"/>\n      <value name=\"Pos.Masc.Gen.Sg.Sw\"/>\n      <value name=\"Pos.Masc.Nom.Sg.Mix\"/>\n      <value name=\"Pos.Masc.Nom.Sg.St\"/>\n      <value name=\"Pos.Masc.Nom.Sg.Sw\"/>\n      <value name=\"Pos.Neut.Akk.Sg.Mix\"/>\n      <value name=\"Pos.Neut.Akk.Sg.St\"/>\n      <value name=\"Pos.Neut.Akk.Sg.Sw\"/>\n      <value name=\"Pos.Neut.Dat.Sg.Mix\"/>\n      <value name=\"Pos.Neut.Dat.Sg.St\"/>\n      <value name=\"Pos.Neut.Dat.Sg.Sw\"/>\n      <value name=\"Pos.Neut.Gen.Sg.Mix\"/>\n      <value name=\"Pos.Neut.Gen.Sg.St\"/>\n      <value name=\"Pos.Neut.Gen.Sg.Sw\"/>\n      <value name=\"Pos.Neut.Nom.Sg.Mix\"/>\n      <value name=\"Pos.Neut.Nom.Sg.St\"/>\n      <value name=\"Pos.Neut.Nom.Sg.Sw\"/>\n      <value name=\"Sg\"/>\n      <value name=\"Sup\"/>\n      <value name=\"Sup.*.Akk.Pl.St\"/>\n      <value name=\"Sup.*.Akk.Pl.Sw\"/>\n      <value name=\"Sup.*.Dat.Pl.St\"/>\n      <value name=\"Sup.*.Dat.Pl.Sw\"/>\n      <value name=\"Sup.*.Gen.Pl.Sw\"/>\n      <value name=\"Sup.*.Nom.Pl.Mix\"/>\n      <value name=\"Sup.*.Nom.Pl.Sw\"/>\n      <value name=\"Sup.Fem.Akk.Sg.St\"/>\n      <value name=\"Sup.Fem.Akk.Sg.Sw\"/>\n      <value name=\"Sup.Fem.Dat.Sg.Mix\"/>\n      <value name=\"Sup.Fem.Dat.Sg.St\"/>\n      <value name=\"Sup.Fem.Dat.Sg.Sw\"/>\n      <value name=\"Sup.Fem.Gen.Sg.St\"/>\n      <value name=\"Sup.Fem.Gen.Sg.Sw\"/>\n      <value name=\"Sup.Fem.Nom.Sg.Mix\"/>\n      <value name=\"Sup.Fem.Nom.Sg.St\"/>\n      <value name=\"Sup.Fem.Nom.Sg.Sw\"/>\n      <value name=\"Sup.Masc.Akk.Sg.St\"/>\n      <value name=\"Sup.Masc.Akk.Sg.Sw\"/>\n      <value name=\"Sup.Masc.Dat.Sg.Mix\"/>\n      <value name=\"Sup.Masc.Dat.Sg.Sw\"/>\n      <value name=\"Sup.Masc.Gen.Sg.Sw\"/>\n      <value name=\"Sup.Masc.Nom.Sg.Mix\"/>\n      <value name=\"Sup.Masc.Nom.Sg.St\"/>\n      <value name=\"Sup.Masc.Nom.Sg.Sw\"/>\n      <value name=\"Sup.Neut.Akk.Sg.Mix\"/>\n      <value name=\"Sup.Neut.Akk.Sg.Sw\"/>\n      <value name=\"Sup.Neut.Dat.Sg.Sw\"/>\n      <value name=\"Sup.Neut.Gen.Sg.St\"/>\n      <value name=\"Sup.Neut.Nom.Sg.St\"/>\n      <value name=\"Sup.Neut.Nom.Sg.Sw\"/>\n      <value name=\"UNKNOWN\">unknown tag</value>\n    </feature>\n\n    <feature name=\"cat\" domain=\"NT\">\n      <value name=\"--\">&lt;not bound&gt;</value>\n      <value name=\"AA\">superlative phrase with &quot;am&quot;</value>\n      <value name=\"AP\">adjektive phrase</value>\n      <value name=\"AVP\">adverbial phrase</value>\n      <value name=\"CAC\">coordinated adposition</value>\n      <value name=\"CAP\">coordinated adjektive phrase</value>\n      <value name=\"CAVP\">coordinated adverbial phrase</value>\n      <value name=\"CCP\">coordinated complementiser</value>\n      <value name=\"CH\">chunk</value>\n      <value name=\"CNP\">coordinated noun phrase</value>\n      <value name=\"CO\">coordination</value>\n      <value name=\"CPP\">coordinated adpositional phrase</value>\n      <value name=\"CS\">coordinated sentence</value>\n      <value name=\"CVP\">coordinated verb phrase (non-finite)</value>\n      <value name=\"CVZ\">coordinated zu-marked infinitive</value>\n      <value name=\"DL\">discourse level constituent</value>\n      <value name=\"ISU\">idiosyncratis unit</value>\n      <value name=\"MPN\">multi-word proper noun</value>\n      <value name=\"MTA\">multi-token adjective</value>\n      <value name=\"NM\">multi-token number</value>\n      <value name=\"NP\">noun phrase</value>\n      <value name=\"PP\">adpositional phrase</value>\n      <value name=\"QL\">quasi-languag</value>\n      <value name=\"S\">sentence</value>\n      <value name=\"VP\">verb phrase (non-finite)</value>\n      <value name=\"VZ\">zu-marked infinitive</value>\n    </feature>\n\n    <edgelabel>\n      <value name=\"--\">--</value>\n      <value name=\"AC\">adpositional case marker</value>\n      <value name=\"ADC\">adjective component</value>\n      <value name=\"AMS\">measure argument of adj</value>\n      <value name=\"APP\">apposition</value>\n      <value name=\"AVC\">adverbial phrase component</value>\n      <value name=\"CC\">comparative complement</value>\n      <value name=\"CD\">coordinating conjunction</value>\n      <value name=\"CJ\">conjunct</value>\n      <value name=\"CM\">comparative concjunction</value>\n      <value name=\"CP\">complementizer</value>\n      <value name=\"DA\">dative</value>\n      <value name=\"DH\">discourse-level head</value>\n      <value name=\"DM\">discourse marker</value>\n      <value name=\"GL\">prenominal genitive</value>\n      <value name=\"GR\">postnominal genitive</value>\n      <value name=\"HD\">head</value>\n      <value name=\"JU\">junctor</value>\n      <value name=\"MC\">comitative</value>\n      <value name=\"MI\">instrumental</value>\n      <value name=\"ML\">locative</value>\n      <value name=\"MNR\">postnominal modifier</value>\n      <value name=\"MO\">modifier</value>\n      <value name=\"MR\">rhetorical modifier</value>\n      <value name=\"MW\">way (directional modifier)</value>\n      <value name=\"NG\">negation</value>\n      <value name=\"NK\">noun kernel modifier</value>\n      <value name=\"NMC\">numerical component</value>\n      <value name=\"OA\">accusative object</value>\n      <value name=\"OA2\">second accusative object</value>\n      <value name=\"OC\">clausal object</value>\n      <value name=\"OG\">genitive object</value>\n      <value name=\"PD\">predicate</value>\n      <value name=\"PG\">pseudo-genitive</value>\n      <value name=\"PH\">placeholder</value>\n      <value name=\"PM\">morphological particle</value>\n      <value name=\"PNC\">proper noun component</value>\n      <value name=\"RC\">relative clause</value>\n      <value name=\"RE\">repeated element</value>\n      <value name=\"RS\">reported speech</value>\n      <value name=\"SB\">subject</value>\n      <value name=\"SBP\">passivised subject (PP)</value>\n      <value name=\"SP\">subject or predicate</value>\n      <value name=\"SVP\">separable verb prefix</value>\n      <value name=\"UC\">(idiosyncratic) unit component</value>\n      <value name=\"VO\">vocative</value>\n    </edgelabel>\n    <secedgelabel>\n      <value name=\"OA2\">second accusative object</value>\n      <value name=\"ADC\">adjective component</value>\n      <value name=\"NK\">noun kernel modifier</value>  \n      <value name=\"SP\">subject or predicate</value>\n      <value name=\"GL\">prenominal genitive</value>\n      <value name=\"SB\">subject</value>\n      <value name=\"SBP\">passivised subject (PP)</value>\n      <value name=\"OC\">clausal object</value>\n      <value name=\"ML\">locative</value>\n      <value name=\"GR\">postnominal genitive</value>\n      <value name=\"RE\">repeated element</value>\n      <value name=\"MC\">comitative</value>\n      <value name=\"AVC\">adverbial phrase component</value>\n      <value name=\"CVC\">collocational verb construction (Funktionsverbgefuege)</value>\n      <value name=\"NG\">negation</value>\n      <value name=\"SVP\">separable verb prefix</value>\n      <value name=\"PNC\">proper noun component</value>\n      <value name=\"HD\">head</value>\n      <value name=\"MNR\">postnominal modifier</value>\n      <value name=\"CM\">comparative concjunction</value>\n      <value name=\"OG\">genitive object</value>\n      <value name=\"AMS\">measure argument of adj</value>\n      <value name=\"VO\">vocative</value>\n      <value name=\"DA\">dative</value>\n      <value name=\"--\">--</value>\n      <value name=\"NMC\">numerical component</value>\n      <value name=\"JU\">junctor</value>\n      <value name=\"CD\">coordinating conjunction</value>\n      <value name=\"RS\">reported speech</value>\n      <value name=\"CJ\">conjunct</value>\n      <value name=\"MW\">way (directional modifier)</value>\n      <value name=\"PM\">morphological particle</value>\n      <value name=\"OA\">accusative object</value>\n      <value name=\"OP\">prepositional object</value>\n      <value name=\"MI\">instrumental</value>\n      <value name=\"PG\">pseudo-genitive</value>\n      <value name=\"CP\">complementizer</value>\n      <value name=\"MR\">rhetorical modifier</value>\n      <value name=\"PH\">placeholder</value>\n      <value name=\"EP\">expletive es</value>\n      <value name=\"DH\">discourse-level head</value>\n      <value name=\"APP\">apposition</value>\n      <value name=\"CC\">comparative complement</value>\n      <value name=\"UC\">(idiosyncratic) unit component</value>\n      <value name=\"RC\">relative clause</value>\n      <value name=\"MO\">modifier</value>\n      <value name=\"DM\">discourse marker</value>\n      <value name=\"AC\">adpositional case marker</value>\n      <value name=\"PD\">predicate</value>\n    </secedgelabel>\n\n  </annotation>\n\n</head>\n"

                ;
    }
} // class
