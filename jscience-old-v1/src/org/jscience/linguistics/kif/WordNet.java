/* This code is copyrighted by Teknowledge (c) 2003.
It is released underthe GNU Public License <http://www.gnu.org/copyleft/gpl.html>.
Users ofthis code also consent, by use of this code, to credit Teknowledge in any
writings, briefings,publications, presentations, or other representations of any
software which incorporates, builds on, or uses this code.*/
package org.jscience.linguistics.kif;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This program finds and displays SUMO terms that are related in meaning
 * to the English expressions that are entered as input.  Note that this
 * program uses four WordNet data files, "NOUN.IDX" and "NOUN.EXC", "VERB.IDX"
 * and "VERB.EXC", as well as two WordNet two SUMO mappings files called
 * "MergeMappings.txt" and "WordNetMappings-verbs.txt" The main part of the
 * program prompts the user for an English term and then returns associated
 * SUMO concepts.  There are two public methods: initOnce() and page().
 *
 * @author Ian Niles
 * @author Adam Pease
 */
public class WordNet {
    /** DOCUMENT ME! */
    public static WordNet wn;

    /** DOCUMENT ME! */
    public static boolean initNeeded = true;

    /** DOCUMENT ME! */
    public static final int NOUN = 1;

    /** DOCUMENT ME! */
    public static final int VERB = 2;

    /** DOCUMENT ME! */
    public static final int ADJECTIVE = 3;

    /** DOCUMENT ME! */
    public static final int ADVERB = 4;

    /** DOCUMENT ME! */
    private Hashtable nounSynsetHash = new Hashtable(); // Words in roots form are keys, values are synset lists.

    /** DOCUMENT ME! */
    private Hashtable verbSynsetHash = new Hashtable();

    /** DOCUMENT ME! */
    private Hashtable adjectiveSynsetHash = new Hashtable();

    /** DOCUMENT ME! */
    private Hashtable adverbSynsetHash = new Hashtable();

    /** DOCUMENT ME! */
    private Hashtable verbDocumentationHash = new Hashtable(); // Keys are synsets, values are documentation strings.

    /** DOCUMENT ME! */
    private Hashtable adjectiveDocumentationHash = new Hashtable();

    /** DOCUMENT ME! */
    private Hashtable adverbDocumentationHash = new Hashtable();

    /** DOCUMENT ME! */
    private Hashtable nounDocumentationHash = new Hashtable();

    /** DOCUMENT ME! */
    private Hashtable nounSUMOHash = new Hashtable(); // Keys are synsets, values are SUMO terms.

    /** DOCUMENT ME! */
    private Hashtable verbSUMOHash = new Hashtable();

    /** DOCUMENT ME! */
    private Hashtable adjectiveSUMOHash = new Hashtable();

    /** DOCUMENT ME! */
    private Hashtable adverbSUMOHash = new Hashtable();

    /** DOCUMENT ME! */
    private Hashtable exceptionNounHash = new Hashtable();

    /** DOCUMENT ME! */
    private Hashtable exceptionVerbHash = new Hashtable();

    /**
     * a HashMap of HashMaps where the key is a word sense of the form
     * word_POS_num signifying the word, part of speech and number of the
     * sense in WordNet.  The value is a HashMap of words and the number of
     * times that word cooccurs in sentences with the word sense given in the
     * key.
     */
    private HashMap wordFrequencies = new HashMap();

    /**
     * A HashMap were words are the keys and values are 8 digit WordNet
     * synset byte offsets.
     */
    private HashMap senseIndex = new HashMap();

    /**
     * A HashMap with words as keys and ArrayList as values.  The
     * ArrayList contains word senses which are Strings of the form
     * word_POS_num signifying the word, part of speech and number of the
     * sense in WordNet.
     */
    private HashMap wordsToSenses = new HashMap();

    /** DOCUMENT ME! */
    private Pattern p;

    /** DOCUMENT ME! */
    private Matcher m;

    /**
     * Create the hashtables nounSynsetHash, nounDocumentationHash,
     * nounSUMOhash and exceptionNounHash that contain the WordNet noun
     * synsets, word definitions, mappings to SUMO, and plural exception
     * forms, respectively. Throws an IOException if the files are not found.
     */
    private void readNouns() throws java.io.IOException {
        System.out.println(
            "INFO in WordNet.readNouns().  Reading WordNet nouns.");

        FileReader r = new FileReader(KBmanager.getMgr().getPref("kbDir") +
                File.separator + "NOUN.IDX");
        LineNumberReader lr = new LineNumberReader(r);
        String line;

        while ((line = lr.readLine()) != null) {
            p = Pattern.compile("^(\\S+)\\sn\\s[\\S\\s]+?([0-9]{8}[\\S\\s]*)");
            m = p.matcher(line);

            if (m.matches()) {
                nounSynsetHash.put(m.group(1), m.group(2));
            }
        }

        r = new FileReader(KBmanager.getMgr().getPref("kbDir") +
                File.separator + "MergeMappings.txt");
        lr = new LineNumberReader(r);

        while ((line = lr.readLine()) != null) {
            p = Pattern.compile(
                    "^([0-9]{8})[\\S\\s]+\\|\\s([\\S\\s]+?)\\s(\\(?\\&\\%\\S+[\\S\\s]+)$");
            m = p.matcher(line);

            if (m.matches()) {
                nounDocumentationHash.put(m.group(1), m.group(2));
                nounSUMOHash.put(m.group(1), m.group(3));
            } else {
                p = Pattern.compile("^([0-9]{8})[\\S\\s]+\\|\\s([\\S\\s]+)$");
                m = p.matcher(line);

                if (m.matches()) {
                    nounDocumentationHash.put(m.group(1), m.group(2));
                }
            }
        }

        r = new FileReader(KBmanager.getMgr().getPref("kbDir") +
                File.separator + "NOUN.EXC");
        lr = new LineNumberReader(r);

        while ((line = lr.readLine()) != null) {
            p = Pattern.compile("(\\S+)\\s+(\\S+)");
            m = p.matcher(line);

            if (m.matches()) {
                exceptionNounHash.put(m.group(1), m.group(2));
            }
        }
    }

    /**
     * Create the hashtables verbSynsetHash, verbDocumentationHash,
     * verbSUMOhash and exceptionVerbHash that contain the WordNet verb
     * synsets, word definitions, mappings to SUMO, and plural exception
     * forms, respectively. Throws an IOException if the files are not found.
     */
    private void readVerbs() {
        System.out.println(
            "INFO in WordNet.readVerbs().  Reading WordNet verbs.");

        try {
            FileReader r = new FileReader(KBmanager.getMgr().getPref("kbDir") +
                    File.separator + "VERB.IDX");
            LineNumberReader lr = new LineNumberReader(r);
            String line;

            while ((line = lr.readLine()) != null) {
                p = Pattern.compile(
                        "^(\\S+)\\sv\\s[\\S\\s]+?([0-9]{8}[\\S\\s]*)");
                m = p.matcher(line);

                if (m.matches()) {
                    verbSynsetHash.put(m.group(1), m.group(2));
                }
            }
        } catch (IOException i) {
            System.err.println("error reading file VERB.IDX <P>\n");
        }

        try {
            FileReader r = new FileReader(KBmanager.getMgr().getPref("kbDir") +
                    File.separator + "WordNetMappings-verbs.txt");
            LineNumberReader lr = new LineNumberReader(r);
            String line;

            while ((line = lr.readLine()) != null) {
                p = Pattern.compile(
                        "^([0-9]{8})[^\\|]+\\|\\s([\\S\\s]+?)\\s(\\(?\\&\\%\\S+[\\S\\s]+)$");
                m = p.matcher(line);

                if (m.matches()) {
                    verbDocumentationHash.put(m.group(1), m.group(2));
                    verbSUMOHash.put(m.group(1), m.group(3));
                } else {
                    p = Pattern.compile("^([0-9]{8})[^\\|]+\\|\\s([\\S\\s]+)$");
                    m = p.matcher(line);

                    if (m.matches()) {
                        verbDocumentationHash.put(m.group(1), m.group(2));
                    }
                }
            }
        } catch (IOException i) {
            System.err.println(
                "error reading file WordNetMappings-verbs.txt <P>\n");
        }

        try {
            FileReader r = new FileReader(KBmanager.getMgr().getPref("kbDir") +
                    File.separator + "VERB.EXC");
            LineNumberReader lr = new LineNumberReader(r);
            String line;

            while ((line = lr.readLine()) != null) {
                p = Pattern.compile("(\\S+)\\s+(\\S+)");
                m = p.matcher(line);

                if (m.matches()) {
                    exceptionVerbHash.put(m.group(1), m.group(2));
                }
            }
        } catch (IOException i) {
            System.err.println("error reading file VERB.EXC <P>\n");
        }
    }

    /**
     * Create the hashtables adjectiveSynsetHash,
     * adjectiveDocumentationHash, and adjectiveSUMOhash that contain the
     * WordNet adjective synsets, word definitions, and mappings to SUMO,
     * respectively. Throws an IOException if the files are not found.
     */
    private void readAdjectives() {
        System.out.println(
            "INFO in WordNet.readAdjectives().  Reading WordNet adjectives.");

        try {
            FileReader r = new FileReader(KBmanager.getMgr().getPref("kbDir") +
                    File.separator + "ADJ.IDX");
            LineNumberReader lr = new LineNumberReader(r);
            String line;

            while ((line = lr.readLine()) != null) {
                p = Pattern.compile(
                        "^(\\S+)\\sa\\s[\\S\\s]+?([0-9]{8}[\\S\\s]*)");
                m = p.matcher(line);

                if (m.matches()) {
                    adjectiveSynsetHash.put(m.group(1), m.group(2));
                }
            }
        } catch (IOException i) {
            System.err.println("error reading file ADJ.IDX <P>\n");
        }

        try {
            FileReader r = new FileReader(KBmanager.getMgr().getPref("kbDir") +
                    File.separator + "WordNetMappings-adj.txt");
            LineNumberReader lr = new LineNumberReader(r);
            String line;

            while ((line = lr.readLine()) != null) {
                p = Pattern.compile(
                        "^([0-9]{8})[\\S\\s]+\\|\\s([\\S\\s]+?)\\s(\\(?\\&\\%\\S+[\\S\\s]+)$");
                m = p.matcher(line);

                if (m.matches()) {
                    adjectiveDocumentationHash.put(m.group(1), m.group(2));
                    adjectiveSUMOHash.put(m.group(1), m.group(3));
                } else {
                    p = Pattern.compile(
                            "^([0-9]{8})[\\S\\s]+\\|\\s([\\S\\s]+)$");
                    m = p.matcher(line);

                    if (m.matches()) {
                        adjectiveDocumentationHash.put(m.group(1), m.group(2));
                    }
                }
            }
        } catch (IOException i) {
            System.err.println(
                "error reading file WordNetMappings-adjectives.txt <P>\n");
        }
    }

    /**
     * Create the hashtables adverbSynsetHash, adverbDocumentationHash,
     * and adverbSUMOhash that contain the WordNet adverb synsets, word
     * definitions, and mappings to SUMO, respectively. Throws an IOException
     * if the files are not found.
     */
    private void readAdverbs() {
        System.out.println(
            "INFO in WordNet.readAdverbs().  Reading WordNet adverbs.");

        try {
            FileReader r = new FileReader(KBmanager.getMgr().getPref("kbDir") +
                    File.separator + "ADV.IDX");
            LineNumberReader lr = new LineNumberReader(r);
            String line;

            while ((line = lr.readLine()) != null) {
                p = Pattern.compile(
                        "^(\\S+)\\sr\\s[\\S\\s]+?([0-9]{8}[\\S\\s]*)");
                m = p.matcher(line);

                if (m.matches()) {
                    adverbSynsetHash.put(m.group(1), m.group(2));
                }
            }
        } catch (IOException i) {
            System.err.println("error reading file ADJ.IDX <P>\n");
        }

        try {
            FileReader r = new FileReader(KBmanager.getMgr().getPref("kbDir") +
                    File.separator + "WordNetMappings-adv.txt");
            LineNumberReader lr = new LineNumberReader(r);
            String line;

            while ((line = lr.readLine()) != null) {
                p = Pattern.compile(
                        "^([0-9]{8})[\\S\\s]+\\|\\s([\\S\\s]+?)\\s(\\(?\\&\\%\\S+[\\S\\s]+)$");
                m = p.matcher(line);

                if (m.matches()) {
                    adverbDocumentationHash.put(m.group(1), m.group(2));
                    adverbSUMOHash.put(m.group(1), m.group(3));
                } else {
                    p = Pattern.compile(
                            "^([0-9]{8})[\\S\\s]+\\|\\s([\\S\\s]+)$");
                    m = p.matcher(line);

                    if (m.matches()) {
                        adverbDocumentationHash.put(m.group(1), m.group(2));
                    }
                }
            }
        } catch (IOException i) {
            System.err.println(
                "error reading file WordNetMappings-adverbs.txt <P>\n");
        }
    }

    /**
     * Return a HashMap of HashMaps where the key is a word sense of
     * the form word_POS_num signifying the word, part of speech and number of
     * the sense in WordNet.  The value is a HashMap of words and the number
     * of times that word cooccurs in sentences with the word sense given in
     * the key.
     */
    public void readWordFrequencies() {
        int counter = 0;

        System.out.println(
            "INFO in WordNet.readWordFrequencies().  Reading WordNet word frequencies.");

        try {
            FileReader r = new FileReader(KBmanager.getMgr().getPref("kbDir") +
                    File.separator + "wordFrequencies.txt");
            LineNumberReader lr = new LineNumberReader(r);
            String line;

            while ((line = lr.readLine()) != null) {
                Pattern p = Pattern.compile("^Word: ([^ ]+) Values: (.*)");
                Matcher m = p.matcher(line);

                if (m.matches()) {
                    String key = m.group(1);
                    String values = m.group(2);
                    String[] words = values.split(" ");
                    HashMap frequencies = new HashMap();

                    for (int i = 0; i < words.length; i++) {
                        String word = words[i].substring(0,
                                words[i].indexOf("_"));
                        String freq = words[i].substring(words[i].lastIndexOf(
                                    "_") + 1, words[i].length());

                        //System.out.println("INFO in WordNet.readWordFrequencies().  word and frequeny: " + word + " " + freq);
                        frequencies.put(word.intern(), Integer.decode(freq));
                    }

                    wordFrequencies.put(key.intern(), frequencies);
                    counter++;

                    if (counter == 1000) {
                        System.out.println(
                            "INFO in WordNet.readWordFrequencies().  Read word sense: " +
                            key);
                        counter = 0;
                    }
                }
            }
        } catch (IOException i) {
            System.err.println("error reading file wordFrequencies.txt <P>\n");
        }
    }

    /**
     * 
     */
    public void readSenseIndex() {
        int counter = 0;

        System.out.println(
            "INFO in WordNet.readSenseIndex().  Reading WordNet sense index.");

        try {
            FileReader r = new FileReader(KBmanager.getMgr().getPref("kbDir") +
                    File.separator + "SENSE.IDX");
            LineNumberReader lr = new LineNumberReader(r);
            System.out.println(
                "INFO in WordNet.readSenseIndex().  Opened file.");

            String line;

            while ((line = lr.readLine()) != null) {
                Pattern p = Pattern.compile(
                        "([^%]+)%([^:]*):[^:]*:[^:]*:[^:]*:[^ ]* ([^ ]+) ([^ ]+) .*");
                Matcher m = p.matcher(line);

                if (m.matches()) {
                    String word = m.group(1);
                    String pos = m.group(2);
                    String synset = m.group(3);
                    String sensenum = m.group(4);
                    String posString = "NN";

                    if (pos.equalsIgnoreCase("1")) {
                        posString = "NN";
                    }

                    if (pos.equalsIgnoreCase("2")) {
                        posString = "VB";
                    }

                    if (pos.equalsIgnoreCase("3")) {
                        posString = "JJ";
                    }

                    if (pos.equalsIgnoreCase("4")) {
                        posString = "RB";
                    }

                    String key = word + "_" + posString + "_" + sensenum;

                    if (wordsToSenses.containsKey(word.intern())) {
                        ArrayList al = (ArrayList) wordsToSenses.get(word.intern());
                        al.add(key);
                    } else {
                        ArrayList al = new ArrayList();
                        al.add(key);
                        wordsToSenses.put(word.intern(), al);
                    }

                    senseIndex.put(key, synset);
                    counter++;

                    if (counter == 1000) {
                        System.out.println(
                            "INFO in WordNet.readSenseIndex().  Read word sense: " +
                            key);
                        System.out.println(word + " " + pos + " " + synset +
                            " " + sensenum);
                        counter = 0;
                    }
                }
            }
        } catch (IOException i) {
            System.err.println("error reading file SENSE.IDX <P>\n");
        }
    }

    /**
     * Take a WordNet sense identifier, and return the integer part of
     * speech code.
     *
     * @param sense DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int sensePOS(String sense) {
        if (sense.indexOf("_NN_") != -1) {
            return NOUN;
        }

        if (sense.indexOf("_VB_") != -1) {
            return VERB;
        }

        if (sense.indexOf("_JJ_") != -1) {
            return ADJECTIVE;
        }

        if (sense.indexOf("_RB_") != -1) {
            return ADVERB;
        }

        System.out.println(
            "Error in WordNet.sensePOS(): Unknown part of speech type in sense code: " +
            sense);

        return 0;
    }

    /**
     * Return the best guess at the synset for the given word in the
     * context of the sentence.  Returns an 8-digit WordNet synset file byte
     * offset as a string.
     *
     * @param word DOCUMENT ME!
     * @param words DOCUMENT ME!
     * @param POS DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String findSUMOWordSense(String word, ArrayList words, int POS) {
        String SUMOterm = null;

        System.out.println("WordNet.findWordSense(): for word " + word +
            " and part of speech " + POS);

        ArrayList senses = (ArrayList) wordsToSenses.get(word.intern());

        if (senses == null) {
            System.out.println("Error in WordNet.findWordSense(): Word " +
                word + " not in lexicon.");

            return null;
        }

        int firstSense = -1;
        int bestSense = -1;
        int bestTotal = -1;

        for (int i = 0; i < senses.size(); i++) {
            String sense = (String) senses.get(i);

            if (sensePOS(sense) == POS) {
                System.out.println("WordNet.findWordSense(): Examining sense: " +
                    sense);

                if (firstSense == -1) {
                    firstSense = i;
                }

                HashMap senseAssoc = (HashMap) wordFrequencies.get(sense.intern());

                if (senseAssoc != null) {
                    int total = 0;

                    for (int j = 0; j < words.size(); j++) {
                        String lowercase = ((String) words.get(j)).toLowerCase()
                                            .intern();

                        if (senseAssoc.containsKey(lowercase)) {
                            total = total +
                                ((Integer) senseAssoc.get(lowercase)).intValue();
                        }
                    }

                    if (total > bestTotal) {
                        bestTotal = total;
                        bestSense = i;
                    }

                    System.out.print("WordNet.findWordSense(): Total: ");
                    System.out.println(total + " " + bestTotal + " " +
                        bestSense);
                }
            }
        }

        if (bestSense == -1) { // if no word cooccurrances have been found

            if (firstSense == -1) { // if there were no words of the right part of speech
                System.out.println("Error in WordNet.findWordSense(): Word " +
                    word + " and part of speech " + POS +
                    " has no matching SUMO term.");

                return null;
            }

            bestSense = firstSense;
        }

        String senseValue = (String) senses.get(bestSense);
        System.out.println("WordNet.findWordSense(): senseValue: " +
            senseValue);

        String synset = (String) senseIndex.get(senseValue.intern());
        System.out.println("WordNet.findWordSense(): synset: " + synset);

        switch (POS) {
        case NOUN:
            SUMOterm = (String) nounSUMOHash.get(synset.intern());

            break;

        case VERB:
            SUMOterm = (String) verbSUMOHash.get(synset.intern());

            break;

        case ADJECTIVE:
            SUMOterm = (String) adjectiveSUMOHash.get(synset.intern());

            break;

        case ADVERB:
            SUMOterm = (String) adverbSUMOHash.get(synset.intern());

            break;
        }

        System.out.println("WordNet.findWordSense(): SUMO term: " + SUMOterm);

        if (SUMOterm != null) { // Remove SUMO-WordNet mapping characters
            SUMOterm = SUMOterm.replaceAll("&%", "");
            SUMOterm = SUMOterm.replaceAll("[+=@]", "");
        }

        return SUMOterm;
    }

    /**
     * Read the WordNet files only on initialization of the class.
     */
    public static void initOnce() throws java.io.IOException {
        if (initNeeded == true) {
            wn = new WordNet();
            wn.readNouns();
            wn.readVerbs();
            wn.readAdjectives();
            wn.readAdverbs();
            initNeeded = false;
        }
    }

    /**
     * A utility function that mimics the functionality of the perl
     * substitution feature (s/match/replacement/).  Note that only one
     * replacement is made, not a global replacement.
     *
     * @param result is the string on which the substitution is performed.
     * @param match is the substring to be found and replaced.
     * @param subst is the string replacement for match.
     *
     * @return is a String containing the result of the substitution.
     */
    private String subst(String result, String match, String subst) {
        Pattern p = Pattern.compile(match);
        Matcher m = p.matcher(result);

        if (m.find()) {
            result = m.replaceFirst(subst);
        }

        return result;
    }

    /**
     * A utility function that mimics the functionality of the perl
     * substitution feature (s/match/replacement/) but rather thanf returning
     * the result of the substitution, just tests whether the result is a key
     * in a hashtable.  Note that only one replacement is made, not a global
     * replacement.
     *
     * @param result is the string on which the substitution is performed.
     * @param match is the substring to be found and replaced.
     * @param subst is the string replacement for match.
     * @param hash is a hashtable to be checked against the result.
     *
     * @return is a boolean indicating whether the result of the substitution
     *         was found in the hashtable.
     */
    private boolean substTest(String result, String match, String subst,
        Hashtable hash) {
        Pattern p = Pattern.compile(match);
        Matcher m = p.matcher(result);

        if (m.find()) {
            result = m.replaceFirst(subst);

            if (hash.containsKey(result)) {
                return true;
            }

            return false;
        } else {
            return false;
        }
    }

    /**
     * The main routine which looks up the search word in the
     * hashtables to find the relevant word definitions and SUMO mappings.
     *
     * @param synsetBlock
     * @param word is the word the user is asking to search for.
     * @param type is whether the word is a noun or verb (we need to add
     *        capability for adjectives and adverbs.
     * @param sumokbname DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String sumoDisplay(String synsetBlock, String word, String type,
        String sumokbname) {
        StringBuffer result = new StringBuffer();

        // Split apart the block of synsets, and store the separated values as an array.
        String synset;
        String documentation = new String();
        String sumoEquivalent = new String();
        int listLength;
        String[] synsetList = null;

        if (synsetBlock != null) {
            synsetList = synsetBlock.split("\\s+");
        }

        if (synsetList != null) {
            listLength = synsetList.length;
        } else {
            listLength = 0;
        }

        int count = 1;
        result.append("<h4>According to WordNet, the " + type + "\"" + word +
            "\" has ");
        result.append(String.valueOf(listLength) + " sense(s).\n\n</h4>");

        // Split apart the SUMO concepts, and store them as an associative array.
        for (int i = 0; i < listLength; i++) {
            synset = synsetList[i];
            synset.trim();

            if (type.compareTo("noun") == 0) {
                documentation = (String) nounDocumentationHash.get(synset);
                result.append(String.valueOf(count) + ". " + documentation +
                    ".\n");
                sumoEquivalent = (String) nounSUMOHash.get(synset);
            } else {
                if (type.compareTo("verb") == 0) {
                    documentation = (String) verbDocumentationHash.get(synset);
                    result.append(String.valueOf(count) + ". " + documentation +
                        ".\n");
                    sumoEquivalent = (String) verbSUMOHash.get(synset);
                } else {
                    if (type.compareTo("adverb") == 0) {
                        documentation = (String) adverbDocumentationHash.get(synset);
                        result.append(String.valueOf(count) + ". " +
                            documentation + ".\n");
                        sumoEquivalent = (String) adverbSUMOHash.get(synset);
                    } else {
                        if (type.compareTo("adjective") == 0) {
                            documentation = (String) adjectiveDocumentationHash.get(synset);
                            result.append(String.valueOf(count) + ". " +
                                documentation + ".\n");
                            sumoEquivalent = (String) adjectiveSUMOHash.get(synset);
                        }
                    }
                }
            }

            if (sumoEquivalent == null) {
                result.append("<P><ul><li>" + word + " not yet mapped to SUMO");
            } else {
                String[] sumoList = sumoEquivalent.split("\\s+");

                if (sumoList.length == 0) {
                    result.append("<P><ul><li>" + word +
                        " not yet mapped to SUMO");
                } else {
                    result.append("<P><ul><li>\tSUMO Mappings:  ");

                    for (int j = 0; j < sumoList.length; j++) {
                        System.out.println(
                            "INFO in WordNet.sumoDisplay(): SUMO equivalent: " +
                            sumoEquivalent);
                        sumoEquivalent = sumoList[j];
                        sumoEquivalent.trim();

                        Pattern p = Pattern.compile("\\&\\%");
                        Matcher m = p.matcher(sumoEquivalent);
                        sumoEquivalent = m.replaceFirst("");
                        p = Pattern.compile("[\\=\\|\\+\\@]");
                        m = p.matcher(sumoEquivalent);

                        String symbol = String.valueOf(sumoEquivalent.charAt(sumoEquivalent.length() -
                                    1));
                        sumoEquivalent = m.replaceFirst("");
                        result.append("<a href=\"Browse.jsp?term=");
                        result.append(sumoEquivalent + "&kb=" + sumokbname +
                            "\">" + sumoEquivalent + "</a>  ");

                        if (symbol != null) {
                            if (symbol.equalsIgnoreCase("+")) {
                                result.append(" (subsuming mapping)");
                            }

                            if (symbol.equalsIgnoreCase("@")) {
                                result.append(" (instance mapping)");
                            }

                            if (symbol.equalsIgnoreCase("=")) {
                                result.append(" (equivalent mapping)");
                            }
                        }
                    }
                }
            }

            result.append("\n\n</li></ul>");
            count = count + 1;
        }

        result.append(
            "<hr>Explore the word <a href=\"http://www.cogsci.princeton.edu/cgi-bin/webwn/?stage=1&word=");
        result.append(word + "\">" + word + "</a> on the WordNet web site.\n");

        return result.toString();
    }

    /**
     * Return the root form of the noun, or null if it's not in the
     * lexicon.
     *
     * @param mixedCase DOCUMENT ME!
     * @param input DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String nounRootForm(String mixedCase, String input) {
        String result = null;

        System.out.println("INFO in WordNet.nounRootForm: Checking word : " +
            mixedCase + " and " + input);

        if ((exceptionNounHash.containsKey(mixedCase)) ||
                (exceptionNounHash.containsKey(input))) {
            if (exceptionNounHash.containsKey(mixedCase)) {
                result = (String) exceptionNounHash.get(mixedCase);
            } else {
                result = (String) exceptionNounHash.get(input);
            }
        } else {
            // Test all regular plural forms, and correct to singular.
            if (substTest(input, "s$", "", nounSynsetHash)) {
                result = subst(input, "s$", "");
            } else {
                if (substTest(input, "ses$", "s", nounSynsetHash)) {
                    result = subst(input, "ses$", "s");
                } else {
                    if (substTest(input, "xes$", "x", nounSynsetHash)) {
                        result = subst(input, "xes$", "x");
                    } else {
                        if (substTest(input, "zes$", "z", nounSynsetHash)) {
                            result = subst(input, "zes$", "z");
                        } else {
                            if (substTest(input, "ches$", "ch", nounSynsetHash)) {
                                result = subst(input, "ches$", "ch");
                            } else {
                                if (substTest(input, "shes$", "sh",
                                            nounSynsetHash)) {
                                    result = subst(input, "shes$", "sh");
                                } else {
                                    if (nounSynsetHash.containsKey(mixedCase)) {
                                        result = mixedCase;
                                    } else {
                                        if (nounSynsetHash.containsKey(input)) {
                                            result = input;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * This routine converts a noun to its singular form and gets the
     * synsets for it, then passes those synsets to sumoDisplay() for
     * processing. First check to see if the input value or its lower-case
     * version are entered in the WordNet exception list (NOUN.EXC).  If so,
     * then use the regular form in the exception list to find the synsets in
     * the NOUN.DAT file. If the word is not in the exception list, check to
     * see if the lower case version of the input value is a plural and search
     * over NOUN.DAT in the singular form if it is.
     *
     * @param sumokbname DOCUMENT ME!
     * @param mixedCase DOCUMENT ME!
     * @param input DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String processNoun(String sumokbname, String mixedCase, String input) {
        String regular = null;
        String synsetBlock;

        regular = nounRootForm(mixedCase, input);

        if (regular != null) {
            synsetBlock = (String) nounSynsetHash.get(regular);

            return sumoDisplay(synsetBlock, mixedCase, "noun", sumokbname);
        } else {
            return "<P>There are no associated SUMO terms for the noun \"" +
            mixedCase + "\".<P>\n";
        }
    }

    /**
     * Return the present tense singular form of the verb, or null if
     * it's not in the lexicon.
     *
     * @param mixedCase DOCUMENT ME!
     * @param input DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String verbRootForm(String mixedCase, String input) {
        String result = null;

        if ((exceptionVerbHash.containsKey(mixedCase)) ||
                (exceptionVerbHash.containsKey(input))) {
            if (exceptionVerbHash.containsKey(mixedCase)) {
                result = (String) exceptionVerbHash.get(mixedCase);
            } else {
                result = (String) exceptionVerbHash.get(input);
            }
        } else {
            // Test all regular forms and convert to present tense singular.
            if (substTest(input, "s$", "", verbSynsetHash)) {
                result = subst(input, "s$", "");
            } else {
                if (substTest(input, "es$", "", verbSynsetHash)) {
                    result = subst(input, "es$", "");
                } else {
                    if (substTest(input, "ies$", "y", verbSynsetHash)) {
                        result = subst(input, "ies$", "y");
                    } else {
                        if (substTest(input, "ed$", "", verbSynsetHash)) {
                            result = subst(input, "ed$", "");
                        } else {
                            if (substTest(input, "ed$", "e", verbSynsetHash)) {
                                result = subst(input, "ed$", "e");
                            } else {
                                if (substTest(input, "ing$", "e", verbSynsetHash)) {
                                    result = subst(input, "ing$", "e");
                                } else {
                                    if (substTest(input, "ing$", "",
                                                verbSynsetHash)) {
                                        result = subst(input, "ing$", "");
                                    } else {
                                        if (verbSynsetHash.containsKey(
                                                    mixedCase)) {
                                            result = mixedCase;
                                        } else {
                                            if (verbSynsetHash.containsKey(
                                                        input)) {
                                                result = input;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * This routine converts a verb to its present tense singular form
     * and gets the synsets for it, then passes those synsets to sumoDisplay()
     * for processing. First check to see if the input value or its lower-case
     * version are entered in the WordNet exception list (VERB.EXC).  If so,
     * then use the regular form in the exception list to find the synsets in
     * the VERB.DAT file. If the word is not in the exception list, check to
     * see if the lower case version of the input value is a singular form and
     * search over VERB.DAT with the infinitive form if it is.
     *
     * @param sumokbname DOCUMENT ME!
     * @param mixedCase DOCUMENT ME!
     * @param input DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String processVerb(String sumokbname, String mixedCase, String input) {
        String regular = null;
        String synsetBlock;

        regular = verbRootForm(mixedCase, input);

        if (regular != null) {
            synsetBlock = (String) verbSynsetHash.get(regular);

            return sumoDisplay(synsetBlock, mixedCase, "verb", sumokbname);
        } else {
            return "<P>There are no associated SUMO terms for the verb \"" +
            mixedCase + "\".<P>\n";
        }
    }

    /**
     * This routine gets the synsets for an adverb, then passes those
     * synsets to sumoDisplay() for processing.
     *
     * @param sumokbname DOCUMENT ME!
     * @param mixedCase DOCUMENT ME!
     * @param input DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String processAdverb(String sumokbname, String mixedCase,
        String input) {
        StringBuffer result = new StringBuffer();
        String synsetBlock;

        synsetBlock = (String) adverbSynsetHash.get(input);
        result.append(sumoDisplay(synsetBlock, mixedCase, "adverb", sumokbname));

        return (result.toString());
    }

    /**
     * This routine gets the synsets for an adjective, then passes
     * those synsets to sumoDisplay() for processing.
     *
     * @param sumokbname DOCUMENT ME!
     * @param mixedCase DOCUMENT ME!
     * @param input DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String processAdjective(String sumokbname, String mixedCase,
        String input) {
        StringBuffer result = new StringBuffer();
        String synsetBlock;

        synsetBlock = (String) adjectiveSynsetHash.get(input);
        result.append(sumoDisplay(synsetBlock, mixedCase, "adjective",
                sumokbname));

        return (result.toString());
    }

    /**
     * Get the SUMO term for the given root form word and part of
     * speech.
     *
     * @param word DOCUMENT ME!
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSUMOterm(String word, int pos) {
        String synsetBlock = null; // A String of synsets, which are 8 digit numbers, separated by spaces.

        System.out.println("INFO in WordNet.getSUMOterm: Checking word : " +
            word);

        if (pos == NOUN) {
            synsetBlock = (String) nounSynsetHash.get(word);
        }

        if (pos == VERB) {
            synsetBlock = (String) verbSynsetHash.get(word);
        }

        if (pos == ADJECTIVE) {
            synsetBlock = (String) adjectiveSynsetHash.get(word);
        }

        if (pos == ADVERB) {
            synsetBlock = (String) adverbSynsetHash.get(word);
        }

        int listLength;
        String synset;
        String[] synsetList = null;

        if (synsetBlock != null) {
            synsetList = synsetBlock.split("\\s+");
        }

        String term = null;

        if (synsetList != null) {
            synset = synsetList[0]; // Just get the first synset.  This needs to be changed to a word sense disambiguation algorithm.
            synset.trim();

            if (pos == NOUN) {
                term = (String) nounSUMOHash.get(synset);
            }

            if (pos == VERB) {
                term = (String) verbSUMOHash.get(synset);
            }

            if (pos == ADJECTIVE) {
                term = (String) adjectiveSUMOHash.get(synset);
            }

            if (pos == ADVERB) {
                term = (String) adverbSUMOHash.get(synset);
            }
        }

        if (term != null) {
            return term.trim().substring(2, term.trim().length() - 1);
        } else {
            return null;
        }
    }

    /**
     * Does WordNet contain the given word.
     *
     * @param word DOCUMENT ME!
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean containsWord(String word, int pos) {
        System.out.println("INFO in WordNet.containsWord: Checking word : " +
            word);

        if ((pos == NOUN) && nounSynsetHash.containsKey(word)) {
            return true;
        }

        if ((pos == VERB) && verbSynsetHash.containsKey(word)) {
            return true;
        }

        if ((pos == ADJECTIVE) && adjectiveSynsetHash.containsKey(word)) {
            return true;
        }

        if ((pos == ADVERB) && adverbSynsetHash.containsKey(word)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This is the regular point of entry for this class.  It takes the
     * word the user is searching for, and the part of speech index, does the
     * search, and returns the string with HTML formatting codes to present to
     * the user.  The part of speech codes must be the same as in the menu
     * options in WordNet.jsp and Browse.jsp
     *
     * @param inp The string the user is searching for.
     * @param pos The part of speech of the word 1=noun, 2=verb, 3=adjective,
     *        4=adverb
     * @param sumokbname DOCUMENT ME!
     *
     * @return A string contained the HTML formatted search result.
     */
    public String page(String inp, int pos, String sumokbname) {
        String input = inp;
        StringBuffer buf = new StringBuffer();

        String mixedCase = input;
        String[] s = input.split("\\s+");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < s.length; i++) {
            sb.append(s[i]);

            if ((i + 1) < s.length) {
                sb.append("_");
            }
        }

        input = sb.toString().toLowerCase();

        if (pos == NOUN) {
            buf.append(processNoun(sumokbname, mixedCase, input));
        }

        if (pos == VERB) {
            buf.append(processVerb(sumokbname, mixedCase, input));
        }

        if (pos == ADJECTIVE) {
            buf.append(processAdjective(sumokbname, mixedCase, input));
        }

        if (pos == ADVERB) {
            buf.append(processAdverb(sumokbname, mixedCase, input));
        }

        buf.append("\n");

        return buf.toString();
    }

    /**
     * A main method, used only for testing.  It should not be called
     * during normal operation.
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        String termName = "quickly";
        int POS = 4;
        String sumokbname = "SUMO";

        try {
            WordNet.initOnce();
        } catch (IOException ioe) {
            System.out.println("Error in WordNet.main(): IOException: " +
                ioe.getMessage());
        }

        WordNet.wn.page(termName, POS, sumokbname);
    }
}
