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

package org.jscience.tests.ml.tigerxml;

import org.jscience.ml.tigerxml.*;
import org.jscience.ml.tigerxml.theories.hobbs78.Hobbs78;
import org.jscience.ml.tigerxml.tools.IndexFeatures;
import org.jscience.ml.tigerxml.tools.SyncMMAX;
import org.jscience.ml.tigerxml.tools.SyntaxTools;

import java.util.ArrayList;


/**
 * This static class demonstrates the usage of the org.jscience.ml.tigerxml
 * package. It also aims to test the org.jscience.ml.tigerxml completely by
 * instantiating every available class and calling every available method.
 *
 * @author <a href="mailto:oeze@coli.uni-sb.de">Oezguer Demir</a>
 * @version 1.84
 */
public class TestTigerAPI {
    /**
     * Tests a given Sentence in a given Corpus.
     *
     * @param sent DOCUMENT ME!
     */
    private static void testSentence(Sentence sent) {
        Corpus corpus = sent.getCorpus();
        System.out.println("Sentence: " + sent);
        System.out.println("\t Sentence.getId() = " + sent.getId());
        System.out.println("\t Sentence.getCorpus() = " + sent.getCorpus());
        System.out.println("\t Sentence.getIndex() = " + sent.getIndex());
        System.out.println("\t Sentence.getNextSentence() = " +
            sent.getNextSentence());
        System.out.println("\t Sentence.getPrevSentence() = " +
            sent.getPrevSentence());
        System.out.println("\t Sentence.getNextSentence(2) = " +
            sent.getNextSentence(2));
        System.out.println("\t Sentence.getPrevSentence(2) = " +
            sent.getPrevSentence(2));

        NT rootNode = sent.getRootNT();

        if (sent.hasRootNT()) {
            System.out.println("\t Sentence.getRootNT() = " + rootNode);
        } else {
            System.out.println("\t Sentence " + sent.getId() +
                " has no root NT.");
        }

        System.out.println("\t Sentence.getSpan() = " + sent.getSpan());
        System.out.println("\t Sentence.getNTCount() = " + sent.getNTCount());
        System.out.println("\t Corpus.getSentence(\"" + sent.getId() +
            "\") = " + corpus.getSentence(sent.getId()));
        System.out.println("\t Sentence.getNTs() = " + sent.getNTs());
        System.out.println("\t Sentence.getTs() = " + sent.getTs());
        System.out.println("\t Sentence.getText() = " + sent.getText());
        System.out.println("\t Sentence.printTree() = ");
        sent.printTree();
        System.out.println("\t NonTerminals");

        /* VROOT */
        NT vroot = sent.getVROOT();
        System.out.println("\t\t vroot.getId() = " + vroot.getId());
        System.out.println("\t\t sent.getById(" + vroot.getId() + ") = " +
            sent.getById(vroot.getId()));
        System.out.println("\t\t vroot.getCat() = " + vroot.getCat());
        System.out.println("\t\t vroot.hasMother() = " + vroot.hasMother());
        System.out.println("\t\t vroot.getMother() = " + vroot.getMother());
        System.out.println("\t\t vroot.getEdge2Mother() = " +
            vroot.getEdge2Mother());
        System.out.println("\t\t vroot.hasSecMothers() = " +
            vroot.hasSecMothers());
        System.out.println("\t\t vroot.getMother() = " + vroot.getSecMothers());

        System.out.println("\t\t vroot.getCorpus() = " + vroot.getCorpus());
        System.out.println("\t\t vroot.getSentence() = " + vroot.getSentence());
        System.out.println("\t\t vroot.getIndex() = " + vroot.getIndex());
        System.out.println("\t\t vroot.isTerminal() = " + vroot.isTerminal());
        System.out.println("\t\t vroot.equals(itself) = " +
            vroot.equals(vroot));
        System.out.println("\t\t vroot.equals(first_nt_in_sent) = " +
            vroot.equals(sent.getNT(0)));
        System.out.println("\t\t vroot.getLeftMostTerminal() = " +
            vroot.getLeftmostTerminal());
        System.out.println("\t\t Corpus.getNT(\"" + vroot.getId() + "\") = " +
            corpus.getNT(vroot.getId()));
        System.out.println("\t\t Corpus.getGraphNode(\"" + vroot.getId() +
            "\") = " + corpus.getGraphNode(vroot.getId()));
        System.out.println("\t\t Sentence.getGraphNode(\"" + vroot.getId() +
            "\") = " + sent.getGraphNode(vroot.getId()));
        System.out.println("\t\t Sentence.getNT(\"" + vroot.getId() + "\") = " +
            sent.getNT(vroot.getId()));
        System.out.println("\t\t vroot.getSpan() = " + vroot.getSpan());
        System.out.println("\t\t tools.SyncMMAX.parseSpan(\"" +
            vroot.getSpan() + "\"): " + SyncMMAX.parseSpan(vroot.getSpan()));
        System.out.println("\t\t vroot.getDaughters() = " +
            vroot.getDaughters());
        System.out.println("\t\t vroot.getSecDaughters() = " +
            vroot.getSecDaughters());
        System.out.print("\t\t vroot.getTerminals() = ");

        ArrayList vTerminals = vroot.getTerminals();

        for (int k = 0; k < vTerminals.size(); k++) {
            System.out.print(((T) vTerminals.get(k)) + " ");
        }

        System.out.println("");
        System.out.println("\t\t vroot.getWords() = " + vroot.getWords());

        System.out.println("\t\t IndexFeatures(" + vroot + ").getPerson = " +
            (new IndexFeatures(vroot)).getPerson());
        System.out.println("\t\t IndexFeatures(" + vroot + ").getGender = " +
            (new IndexFeatures(vroot)).getGender());
        System.out.println("\t\t IndexFeatures(" + vroot + ").getNumber = " +
            (new IndexFeatures(vroot)).getNumber());
        System.out.println("\t\t Hobbs78: ");
        System.out.println("\t\t\t Hobbs78.isAppositions(" + vroot + ") = " +
            SyntaxTools.isApposition(vroot));
        System.out.println("\t\t\t Hobbs78.npLikeNode(" + vroot + ") = " +
            SyntaxTools.isNpLikeNode(vroot));
        System.out.println("\t\t\t Hobbs78.sLikeNode(" + vroot + ") = " +
            SyntaxTools.isSLikeNode(vroot));
        System.out.println("\t\t\t SyncMMAX.isMarkable(" + vroot + ") = " +
            SyncMMAX.isMarkable(vroot));
        System.out.println("\t\t\t Hobbs78.hobbsSearch(" + vroot + ") = " +
            Hobbs78.hobbsSearch(vroot));
        System.out.print("\t\t vroot.printNode(\"[comment]\") = ");
        vroot.printNode("[comment]");
        System.out.println("\t\t vroot.before(...) = ");

        ArrayList vSentGraphNodes = new ArrayList(sent.getNTs());
        vSentGraphNodes.addAll(sent.getTs());

        for (int n = 0; n < vSentGraphNodes.size(); n++) {
            GraphNode gn = (GraphNode) vSentGraphNodes.get(n);
            System.out.println("\t\t\t " + gn + " (" +
                gn.getLeftmostTerminal() + ") before " + vroot + " (" +
                vroot.getLeftmostTerminal() + ") = " + gn.before(vroot));
        }

        System.out.println("\t\t vroot.printTree() = ");
        vroot.printTree();

        /*---------------------- NTs -----------------------------*/
        for (int j = 0; j < sent.getNTCount(); j++) {
            NT nt = sent.getNT(j);
            System.out.println("\t\t NT.getId() = " + nt.getId());
            System.out.println("\t\t NT.getById(" + nt.getId() + ") = " +
                sent.getById(nt.getId()).getId());
            System.out.println("\t\t NT.getCat() = " + nt.getCat());
            System.out.println("\t\t NT.getMother() = " + nt.getMother());
            System.out.println("\t\t NT.getEdge2Mother() = " +
                nt.getEdge2Mother());
            System.out.println("\t\t NT.hasMother() = " + nt.hasMother());
            System.out.println("\t\t NT.getCorpus() = " + nt.getCorpus());
            System.out.println("\t\t NT.getSentence() = " + nt.getSentence());
            System.out.println("\t\t NT.getIndex() = " + nt.getIndex());
            System.out.println("\t\t NT.isTerminal() = " + nt.isTerminal());
            System.out.println("\t\t NT.equals(itself) = " + nt.equals(nt));
            System.out.println("\t\t NT.equals(first_nt_in_sent) = " +
                nt.equals(sent.getNT(0)));
            System.out.println("\t\t NT.getLeftMostTerminal() = " +
                nt.getLeftmostTerminal());
            System.out.println("\t\t Corpus.getNT(\"" + nt.getId() + "\") = " +
                corpus.getNT(nt.getId()));
            System.out.println("\t\t Corpus.getGraphNode(\"" + nt.getId() +
                "\") = " + corpus.getGraphNode(nt.getId()));
            System.out.println("\t\t Sentence.getGraphNode(\"" + nt.getId() +
                "\") = " + sent.getGraphNode(nt.getId()));
            System.out.println("\t\t Sentence.getNT(\"" + nt.getId() +
                "\") = " + sent.getNT(nt.getId()));
            System.out.println("\t\t NT.getSpan() = " + nt.getSpan());
            System.out.println("\t\t tools.SyncMMAX.parseSpan(\"" +
                nt.getSpan() + "\"): " + SyncMMAX.parseSpan(nt.getSpan()));
            System.out.println("\t\t NT.getDaughters() = " + nt.getDaughters());
            System.out.print("\t\t NT.getTerminals() = ");

            ArrayList terminals = nt.getTerminals();

            for (int k = 0; k < terminals.size(); k++) {
                System.out.print(((T) terminals.get(k)) + " ");
            }

            System.out.println("");
            System.out.println("\t\t NT.getWords() = " + nt.getWords());

            System.out.println("\t\t IndexFeatures(" + nt + ").getPerson = " +
                (new IndexFeatures(nt)).getPerson());
            System.out.println("\t\t IndexFeatures(" + nt + ").getGender = " +
                (new IndexFeatures(nt)).getGender());
            System.out.println("\t\t IndexFeatures(" + nt + ").getNumber = " +
                (new IndexFeatures(nt)).getNumber());
            System.out.println("\t\t Hobbs78: ");
            System.out.println("\t\t\t Hobbs78.isAppositions(" + nt + ") = " +
                SyntaxTools.isApposition(nt));
            System.out.println("\t\t\t Hobbs78.npLikeNode(" + nt + ") = " +
                SyntaxTools.isNpLikeNode(nt));
            System.out.println("\t\t\t Hobbs78.sLikeNode(" + nt + ") = " +
                SyntaxTools.isSLikeNode(nt));
            System.out.println("\t\t\t SyncMMAX.isMarkable(" + nt + ") = " +
                SyncMMAX.isMarkable(nt));
            System.out.println("\t\t\t Hobbs78.hobbsSearch(" + nt + ") = " +
                Hobbs78.hobbsSearch(nt));
            System.out.print("\t\t NT.printNode(\"[comment]\") = ");
            nt.printNode("[comment]");
            System.out.println("\t\t NT.before(...) = ");

            ArrayList sentGraphNodes = new ArrayList(sent.getNTs());
            sentGraphNodes.addAll(sent.getTs());

            for (int n = 0; n < sentGraphNodes.size(); n++) {
                GraphNode gn = (GraphNode) sentGraphNodes.get(n);
                System.out.println("\t\t\t " + gn + " (" +
                    gn.getLeftmostTerminal() + ") before " + nt + " (" +
                    nt.getLeftmostTerminal() + ") = " + gn.before(nt));
            }

            System.out.println("\t\t NT.printTree() = ");
            nt.printTree();
            System.out.println("\t\t Path.getPathToTop(nt) = ");

            Path path = Path.getPathToTop(nt);
            System.out.println(path);
        } // for j

        /* ----------------------- Ts -------------------------------- */
        System.out.println("\t Terminals");

        for (int j = 0; j < sent.getTCount(); j++) {
            T t = sent.getT(j);
            System.out.println("\t\t T.getId() " + t.getId());
            //         System.out.println("\t\t T.getWord() = " + t.getWord());
            System.out.println("\t\t T.getWord() = " + t.getWord());
            System.out.println("\t\t T.getPos() = " + t.getPos());
            System.out.println("\t\t T.getMorph() = " + t.getMorph());
            System.out.println("\t\t T.getMother() = " + t.getMother());
            System.out.println("\t\t T.getEdge2Mother() = " +
                t.getEdge2Mother());
            System.out.println("\t\t T.hasMother() = " + t.hasMother());
            System.out.println("\t\t T.getCorpus() = " + t.getCorpus());
            System.out.println("\t\t T.getSentence() = " + t.getSentence());
            System.out.println("\t\t T.getIndex() = " + t.getIndex());
            System.out.println("\t\t T.getPosition() = " + t.getPosition());
            System.out.println("\t\t T.toString() = " + t.toString());
            System.out.println("\t\t T.isTerminal() = " + t.isTerminal());
            System.out.println("\t\t T.equals(itself) = " + t.equals(t));
            System.out.println("\t\t T.equals(first_nt_in_sent) = " +
                t.equals(sent.getNT(0)));
            System.out.println("\t\t T.getLeftMostTerminal() = " +
                t.getLeftmostTerminal());
            System.out.println("\t\t Corpus.getTerminal(\"" + t.getId() +
                "\") = " + corpus.getTerminal(t.getId()));
            System.out.println("\t\t Sentence.getTerminal(\"" + t.getId() +
                "\") = " + sent.getTerminal(t.getId()));
            System.out.println("\t\t Corpus.getGraphNode(\"" + t.getId() +
                "\") = " + corpus.getGraphNode(t.getId()));
            System.out.println("\t\t Sentence.getGraphNode(\"" + t.getId() +
                "\") = " + sent.getGraphNode(t.getId()));

            IndexFeatures ind_feat = new IndexFeatures(t);
            System.out.println("\t\t IndexFeatures(" + t + ").getPerson = " +
                (new IndexFeatures(t)).getPerson());
            System.out.println("\t\t IndexFeatures(" + t + ").getGender = " +
                (new IndexFeatures(t)).getGender());
            System.out.println("\t\t IndexFeatures(" + t + ").getNumber = " +
                (new IndexFeatures(t)).getNumber());
            System.out.println("\t\t Hobbs78: ");
            System.out.println("\t\t\t Hobbs78.isApposition(" + t + ") = " +
                SyntaxTools.isApposition(t));
            System.out.println("\t\t\t Hobbs78.isNoun(" + t + ") = " +
                SyntaxTools.isNoun(t));
            System.out.println("\t\t\t SyncMMAX.isMarkable(" + t + ") = " +
                SyncMMAX.isMarkable(t));
            System.out.println("\t\t\t Hobbs78.hobbsSearch(" + t + ") = " +
                Hobbs78.hobbsSearch(t));
            System.out.print("\t\t T.printNode(\"[comment]\") = ");
            t.printNode("[comment]");

            //  System.out.println("\t\t T.before(...) = ");
            ArrayList sentGraphNodes = new ArrayList(sent.getNTs());
            sentGraphNodes.addAll(sent.getTs());

            for (int n = 0; n < sentGraphNodes.size(); n++) {
                GraphNode gn = (GraphNode) sentGraphNodes.get(n);
                System.out.println("\t\t\t " + gn + " (" +
                    gn.getLeftmostTerminal() + ") before " + t + " (" +
                    t.getLeftmostTerminal() + ") = " + gn.before(t));
            } //for n

            System.out.println("\t\t T.printTree() = ");
            t.printTree();
        } // for j
    } // testSentece

    /**
     * Run an exhaustive test on the given <code>Corpus</code>
     *
     * @param corpus The <code>Corpus</code> object to be tested.
     */
    private static void testAll(Corpus corpus) {
        System.err.println("TestTigerAPI: Processing Corpus " + corpus);

        /* Use the corpus object to print the whole structure */
        System.out.println("Corpus.getId() = " + corpus.getId());
        System.out.println("Corpus = " + corpus);

        /* All-sentences-loop */
        for (int i = 0; i < corpus.getSentenceCount(); i++) {
            System.err.print("\rTestTigerAPI: Testing sentence " + (i + 1) +
                " of " + corpus.getSentenceCount() + ".");
            System.err.flush();

            Sentence sent = corpus.getSentence(i);
            testSentence(sent);
        } // for i

        /* -------------------------- Corpus ------------------------------- */
        System.out.println("Corpus ID: " + corpus.getId());
        System.out.println("Corpus HashCode: " + corpus.hashCode());
        System.out.println("Number of Sentences in Corpus: " +
            corpus.getSentenceCount());
        System.out.println("Number of GraphNodes in Corpus: " +
            corpus.getAllGraphNodes().size());
        System.out.println("Number of NTs in Corpus: " +
            corpus.getAllNTs().size());
        System.out.println("Number of Ts in Corpus: " +
            corpus.getAllTs().size());
        System.out.println("");
        System.out.println("Corpus.getText() = ");
        System.out.println("");
        System.out.println(corpus.getText());
        System.err.println("\rTestTigerAPI: Tested " +
            corpus.getSentenceCount() + " sentences." +
            "                                                 ");
    } // testAll()

    /**
     * Run an short test on the given <code>Corpus</code>
     *
     * @param corpus The <code>Corpus</code> object to be tested.
     */
    private static void testShort(Corpus corpus) {
        System.out.println(corpus.getSentence(98));
    } // testShort()

    /**
     * Run an short test on the given <code>Corpus</code>
     *
     * @param corpus The <code>Corpus</code> object to be tested.
     * @param fileName DOCUMENT ME!
     */
    private static void testXML(Corpus corpus, String fileName) {
        corpus.print2xml(fileName);
    } //testXML

    /**
     * Main
     *
     * @param args One command line arg: The name of the XML corpus to be
     *        parsed.
     */
    public static void main(String[] args) {
        int verbosity = -1;

        if ((args.length < 2)) {
            System.err.println(
                "Usage: java TestTigerAPI XMLfilename (int)verbosity");
            System.exit(1);
        }

        try {
            verbosity = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            System.err.println("TestTigerAPI: Argument '" + args[1] +
                "' is not an integer");
            System.err.println(
                "Usage: java TestTigerAPI XMLfilename (int)verbosity");
            System.exit(1);
        }

        /* Create a Corpus object by parsing the given xml file */
        Corpus corpus = new Corpus(args[0], verbosity);
        testAll(corpus);

        //testShort(corpus);
        //testSentence(corpus.getSentence(571-1));
        //testXML(corpus, "_testXMLout.xml");
    } // main
} // class
