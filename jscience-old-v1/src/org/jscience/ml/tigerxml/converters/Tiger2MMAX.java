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

package org.jscience.ml.tigerxml.converters;

import org.jscience.ml.tigerxml.*;
import org.jscience.ml.tigerxml.theories.strube02.Strube02;
import org.jscience.ml.tigerxml.tools.GeneralTools;
import org.jscience.ml.tigerxml.tools.SyncMMAX;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Converts a TiGerXML corpus to several different MMAX annotations.
 * <p/>
 * This converter produces three independent MMAX annotations for each
 * given TiGerXML corpus (see command line arguments) and puts them
 * according to the name of the respective TiGerXML source corpus into
 * a designated output folder under the general output folder "output".
 * <p/>
 * You can also specify an alternative root output path by using the -o option.
 * <p/>
 * Example usage:<p>
 * <code>
 * java -cp org.jscience.ml.tigerxml.jar org.jscience.ml.tigerxml.converters.Tiger2MMAX inputTIGER.xml
 * -o myOutputFolder
 * </code><p>
 * The specification of the output folder is not mandatory. The default
 * is "output" in the current directory.
 * <p/>
 * Note that this converter is still extendable by implementing the empty
 * method sceletons in org.jscience.ml.tigerxml.theories.Strube02 and by implementing and
 * referencing a new class for a MULI annotation which contains data
 * specified in the muli-scheme, extracted or derived from the TIGER
 * annotation. (For example org.jscience.ml.tigerxml.theories.MuliAnnotator). In this
 * version no data for the attribute values in the MMAX annotation are
 * extracted from the TiGerXML corpus.
 * <p/>
 *
 * @author <a href="mailto:oeze@coli.uni-sb.de">Oezguer Demir</a>
 * @version 1.84
 *          $Id: Tiger2MMAX.java,v 1.2 2007-10-21 17:47:04 virtualcall Exp $
 */
public class Tiger2MMAX {

    /**
     * The current version of this implementation (if not forgot to update)
     */
    private static final String version = "v1.84";

    /**
     * This is the directory separator (eg "/" on Linux, "\" on Windows etc)
     */
    private static final String SLASH =
            System.getProperties().getProperty("file.separator");

    /**
     * This is the global output directory (default="output")
     */
    private static String outputDirNameBase = "output";
    private static String outputDirName;

    /**
     * Checks the command line arguments, sets the global constants
     * (verbose, validate, stats, ...) and returns a list of file names.
     *
     * @param args The command line arguments as received from the OS
     * @return fileNames The files to be processed.
     */
    private static ArrayList parseCmdLineArgs(String[] args) {
        ArrayList fileNames = new ArrayList();
        /* Check if enough cmd line args are given, exit if not */
        if (args.length == 0) {
            System.err.println("Usage: java Tiger2MMAX XMLfilename[s] -o [outputPath]\n" +
                    "Help:  java Tiger2MMAX -h");
            System.exit(-1);
        } // if
        /* Iterate through all args, set the global options,create the file list */
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-h")) {
                printHelp();
                System.exit(-1);
            }
            if (args[i].endsWith(".xml")) {
                fileNames.add(args[i]);
            }
            if (args[i].startsWith("-o")) {
                if (args.length > (i + 1)) {
                    outputDirNameBase = args[i + 1];
                }
            } else {
                outputDirName = "output";
            }
        } // for i
        /* Return the files as an ArrayList */
        return fileNames;
    } // parseCmdLineArgs

    private static void printHelp() {
        System.err.println("Converts a TiGerXML corpus to several "
                + "different MMAX annotations.");
        System.err.println("");
        System.err.println("This converter produces three independent MMAX "
                + "annotations for each");
        System.err.println("given TiGerXML corpus (see command line arguments) and "
                + "puts them");
        System.err.println("according to the name of the repsective TiGerXML source "
                + "corpus into");
        System.err.println("a designated output folder under the general output "
                + "folder \"output\".");
        System.err.println(" ");
        System.err.println("You can also specify an alternative root output path by "
                + "using the -o option.");
        System.err.println("");
        System.err.println("Example usage:");
        System.err.println("");
        System.err.println("java -cp org.jscience.ml.tigerxml.jar org.jscience.ml.tigerxml.converters.Tiger2MMAX "
                + "inputTIGER.xml \\");
        System.err.println("-o myOutputFolder");
        System.err.println("");
        System.err.println("The specification of the output folder is not mandatory. "
                + "The default");
        System.err.println("is \"output\" in the current directory.");
        System.err.println("");
        System.err.println("Note that this converter is still extendable by "
                + "implementing the empty");
        System.err.println("method sceletons in org.jscience.ml.tigerxml.theories.Strube02 and by "
                + "implementing and");
        System.err.println("referencing a new class for the MULI annotation. (For "
                + "example");
        System.err.println("org.jscience.ml.tigerxml.theories.MuliAnnotator). In this version no "
                + "data");
        System.err.println("for the attribute values in the MMAX annotation are "
                + "extracted from the");
        System.err.println("TiGerXML corpus.");

    }

    private static void writeHeaderFiles() {
        writeMarkablesDTD();
        writeSchemeDTD();
        writeTextDTD();
        writeTextXSL();
        writeWordsDTD();
        writeSynScheme();
        writeSemScheme();
        writeMuliScheme();
        writeSynAnnoFile();
        writeSemAnnoFile();
        writeMuliAnnoFile();
        writeHobbsMuliAnnoFile();
    }

    private static void writeMarkablesDTD() {
        FileWriter outXML = null;
        String outFileName = outputDirName + SLASH + "markables.dtd";
        /* Open "markables.dtd" and write the data */
        try {
            outXML = new FileWriter(outFileName);
            //outXML.write("<?xml version='1.0' encoding='ISO-8859-1'?>\n");
            outXML.write(getComment());
            outXML.write("<!ELEMENT markables (markable*)>\n");
            outXML.write("<!ATTLIST markable id ID #REQUIRED>\n");
            outXML.write("<!ATTLIST markable span CDATA #REQUIRED>\n");
            outXML.write("<!ATTLIST markable type CDATA #REQUIRED>\n");
            outXML.write("<!ATTLIST markable member CDATA #IMPLIED>\n");
            outXML.write("<!ATTLIST markable pointer IDREF #IMPLIED>\n");
            outXML.close();
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } // catch
        System.err.println("org.jscience.ml.tigerxml.converters.Tiger2MMAX: Wrote " + outFileName);
    } // writeMarkablesDTD()

    private static void writeSchemeDTD() {
        FileWriter outXML = null;
        String outFileName = outputDirName + SLASH + "scheme.dtd";
        /* Open "syntax_scheme.dtd" and write the data */
        try {
            outXML = new FileWriter(outFileName);
            //outXML.write("<?xml version='1.0' encoding='ISO-8859-1'?>\n");
            outXML.write(getComment());
            outXML.write("<!--\n");
            outXML.write("    Typical usage:\n");
            outXML.write("\n");
            outXML.write("    <?xml version=\"1.0\"?>\n");
            outXML.write("\n");
            outXML.write("    <!DOCTYPE annotationscheme SYSTEM \"scheme.dtd\">\n");
            outXML.write("\n");
            outXML.write("    <annotationscheme>\n");
            outXML.write("    ...\n");
            outXML.write("    </annotationscheme>\n");
            outXML.write("-->\n");
            outXML.write("\n");
            outXML.write("<!ELEMENT annotationscheme (attribute)*>\n");
            outXML.write("\n");
            outXML.write("<!ELEMENT attribute (value)*>\n");
            outXML.write("<!ATTLIST attribute\n");
            outXML.write("    type CDATA #IMPLIED\n");
            outXML.write("    name CDATA #IMPLIED\n");
            outXML.write("    id CDATA #IMPLIED\n");
            outXML.write("  >\n");
            outXML.write("\n");
            outXML.write("<!ELEMENT value EMPTY>\n");
            outXML.write("<!ATTLIST value\n");
            outXML.write("    next CDATA #IMPLIED\n");
            outXML.write("    name CDATA #IMPLIED\n");
            outXML.write("    id CDATA #IMPLIED\n");
            outXML.write("  >\n");
            outXML.close();
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } // catch
        System.err.println("org.jscience.ml.tigerxml.converters.Tiger2MMAX: Wrote " + outFileName);
    }

    private static void writeSynScheme() {
        FileWriter outXML = null;
        String outFileName = outputDirName + SLASH + "syn_scheme.xml";
        /* Open "syntax_scheme.dtd" and write the data */
        try {
            outXML = new FileWriter(outFileName);
            outXML.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
            outXML.write("\n");
            outXML.write("<!DOCTYPE annotationscheme SYSTEM \"scheme.dtd\">\n");
            outXML.write("\n");
            outXML.write(getComment());
            outXML.write("<annotationscheme>\n");
            outXML.write("\n");
            outXML.write("  <attribute id=\"level_class\" name=\"class\">\n");
            outXML.write("    <value id=\"value_1\" name=\"none\"/>\n");
            outXML.write("    <value id=\"value_2\" name=\"T\"\n");
            outXML.write("      next=\"level_pos,level_morph,level_edge,level_member,level_pointer\"/>\n");
            outXML.write("    <value id=\"value_3\" name=\"NT\"\n");
            outXML.write("      next=\"level_cat,level_edge,level_member,level_pointer\"/>\n");
            outXML.write("  </attribute>\n");
            outXML.write("\n");
            outXML.write("  <attribute id=\"level_pos\" name=\"pos\" type=\"freetext\">\n");
            outXML.write("    <value id=\"value_pos\" name=\"pos\"/>\n");
            outXML.write("  </attribute>\n");
            outXML.write("\n");
            outXML.write("  <attribute id=\"level_morph\" name=\"morph\" type=\"freetext\">\n");
            outXML.write("    <value id=\"value_morph\" name=\"morph\"/>\n");
            outXML.write("  </attribute>\n");
            outXML.write("\n");
            outXML.write("  <attribute id=\"level_cat\" name=\"cat\" type=\"freetext\">\n");
            outXML.write("    <value id=\"value_cat\" name=\"cat\"/>\n");
            outXML.write("  </attribute>\n");
            outXML.write("\n");
            outXML.write("  <attribute id=\"level_edge\" name=\"edge\" type=\"freetext\">\n");
            outXML.write("    <value id=\"value_edge\" name=\"edge\"/>\n");
            outXML.write("  </attribute>\n");
            outXML.write("\n");
            outXML.write("  <attribute id=\"level_member\" name=\"Member\" type=\"id\">\n");
            outXML.write("    <value id=\"value_member\" name=\"Member\"/>\n");
            outXML.write("  </attribute>\n");
            outXML.write("\n");
            outXML.write("  <attribute id=\"level_pointer\" name=\"Pointer\" type=\"id\">\n");
            outXML.write("    <value id=\"value_pointer\" name=\"Pointer\"/>\n");
            outXML.write("  </attribute>\n");
            outXML.write("\n");
            outXML.write("</annotationscheme>\n");
            outXML.close();
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } // catch
        System.err.println("org.jscience.ml.tigerxml.converters.Tiger2MMAX: Wrote " + outFileName);
    }

    private static void writeSemScheme() {
        FileWriter outXML = null;
        String outFileName = outputDirName + SLASH + "sem_scheme.xml";
        /* Open "syntax_scheme.dtd" and write the data */
        try {
            outXML = new FileWriter(outFileName);
            outXML.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
            outXML.write("\n");
            outXML.write("<!DOCTYPE annotationscheme SYSTEM \"scheme.dtd\">\n");
            outXML.write("\n");
            outXML.write(getComment());
            outXML.write("<annotationscheme>\n");
            outXML.write("  <attribute id=\"level_1\" name=\"comment\" text=\"Add your comment here!\" type=\"freetext\">\n");
            outXML.write("    <value id=\"value_1\" name=\"comment\"/>\n");
            outXML.write("  </attribute>  \n");
            outXML.write("  <attribute id=\"level_2\" name=\"np_form\" text=\"\">\n");
            outXML.write("    <value id=\"value_2\" name = \"none\"/>\n");
            outXML.write("    <value id=\"value_99\" name = \"NE\" text=\"Proper Name\"/>\n");
            outXML.write("    <value id=\"value_3\" name = \"defNP\" text=\"definite noun phrase\"/>\n");
            outXML.write("    <value id=\"value_4\" name = \"indefNP\" text=\"indefinite noun phrase\"/>\n");
            outXML.write("    <value id=\"value_5\" name = \"PPER\"/>\n");
            outXML.write("    <value id=\"value_6\" name = \"PPOS\"/>\n");
            outXML.write("    <value id=\"value_7\" name = \"PDS\"/>\n");
            outXML.write("  </attribute>  \n");
            outXML.write("  <attribute id=\"level_3\" name=\"grammatical_role\" text=\"\">\n");
            outXML.write("    <value id=\"value_8\" name = \"none\"/>\n");
            outXML.write("    <value id=\"value_9\" name = \"SBJ\"/>\n");
            outXML.write("    <value id=\"value_10\" name = \"OBJ\"/>\n");
            outXML.write("    <value id=\"value_11\" name = \"other\"/>\n");
            outXML.write("  </attribute>  \n");
            outXML.write("  <attribute id=\"level_4\" name=\"agreement\" text=\"\">\n");
            outXML.write("    <value id=\"value_12\" name = \"none\"/>\n");
            outXML.write("    <value id=\"value_13\" name = \"3M\"/>\n");
            outXML.write("    <value id=\"value_14\" name = \"3F\"/>\n");
            outXML.write("    <value id=\"value_15\" name = \"3N\"/>\n");
            outXML.write("    <value id=\"value_16\" name = \"3P\"/>\n");
            outXML.write("    <value id=\"value_17\" name = \"1S\"/>\n");
            outXML.write("    <value id=\"value_18\" name = \"2S\"/>\n");
            outXML.write("    <value id=\"value_19\" name = \"1P\"/>\n");
            outXML.write("    <value id=\"value_20\" name = \"2P\"/>\n");
            outXML.write("  </attribute>  \n");
            outXML.write("\n");
            outXML.write("  <attribute id=\"level_member\" name=\"member\" type=\"id\">\n");
            outXML.write("    <value id=\"value_member\" name = \"member\"/>\n");
            outXML.write("  </attribute>  \n");
            outXML.write("\n");
            outXML.write("  <attribute id=\"level_pointer\" name=\"pointer\" type=\"id\">\n");
            outXML.write("    <value id=\"value_pointer\" name = \"pointer\"/>\n");
            outXML.write("  </attribute>  \n");
            outXML.write("\n");
            outXML.write("  <attribute id=\"level_5\" name=\"exp_type\" text=\"Is the expression an anaphor, bridging expression, or none of both?\">\n");
            outXML.write("    <value id=\"value_21\" name = \"none\"/>\n");
            outXML.write("    <value id=\"value_22\" name = \"anaphoric\" next=\"level_6\"/>\n");
            outXML.write("    <value id=\"value_22\" name = \"bridging\" next=\"level_7\"/>\n");
            outXML.write("  </attribute>  \n");
            outXML.write("  <attribute id=\"level_6\" name=\"ante_sub_anaphoric\" text=\"Which kind of relation holds between the anaphor and its antecedent?\">\n");
            outXML.write("    <value id=\"value_23\" name = \"none\"/>\n");
            outXML.write("    <value id=\"value_24\" name = \"direct\"/>\n");
            outXML.write("    <value id=\"value_25\" name = \"pronominal\"/>\n");
            outXML.write("    <value id=\"value_26\" name = \"IS-A\"/>\n");
            outXML.write("    <value id=\"value_27\" name = \"other\"/>\n");
            outXML.write("  </attribute>  \n");
            outXML.write("  <attribute id=\"level_7\" name=\"ante_sub_bridging\" text=\"Which kind of relation holds between the bridging expression and its antecedent?\">\n");
            outXML.write("    <value id=\"value_28\" name = \"none\"/>\n");
            outXML.write("    <value id=\"value_29\" name = \"part-whole\"/>\n");
            outXML.write("    <value id=\"value_30\" name = \"cause-effect\"/>\n");
            outXML.write("    <value id=\"value_31\" name = \"entity-attribute\"/>\n");
            outXML.write("    <value id=\"value_32\" name = \"other\"/>\n");
            outXML.write("  </attribute>\n");
            outXML.write("</annotationscheme>\n");
            outXML.close();
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } // catch
        System.err.println("org.jscience.ml.tigerxml.converters.Tiger2MMAX: Wrote " + outFileName);
    }

    private static void writeMuliScheme() {
        FileWriter outXML = null;
        String outFileName = outputDirName + SLASH + "muli_scheme.xml";
        /* Open "syntax_scheme.dtd" and write the data */
        try {
            outXML = new FileWriter(outFileName);
            outXML.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
            outXML.write(getComment());
            outXML.write("<annotationscheme>\n");
            outXML.write("\n");
            outXML.write("<!-- Version: 16.7.2003, extended by 'pointer' and 'set' -->\n");
            outXML.write("\n");
            outXML.write("<attribute id=\"comment_level\" name=\"Comment\" type=\"freetext\">\n");
            outXML.write("  <value id=\"val_comm_0\" name=\"comment_text\" />\n");
            outXML.write(" </attribute>    \n");
            outXML.write("-->\n");
            outXML.write("\n");
            outXML.write("<attribute id=\"type_level\" name=\"Type\" >\n");
            outXML.write("     <value id=\"val_type_0\" name=\"none\"           next=\"level_pointer,level_member\" />\n");
            outXML.write("     <value id=\"val_type_1\" name=\"object\"       next=\"obj_subtype_level,obj_semsort_level,delim_level,quant_level,info_level,level_pointer,level_member\"/>\n");
            outXML.write("     <value id=\"val_type_2\" name=\"property\"     next=\"prop_subtype_level,level_pointer,level_member\"/>\n");
            outXML.write("     <value id=\"val_type_3\" name=\"eventuality\"  next=\"event_semsort_level,event_iter_level,level_pointer,level_member\" />\n");
            outXML.write("     <value id=\"val_type_4\" name=\"textuality\"   next=\"text_semsort_level,level_pointer,level_member\" />\n");
            outXML.write("</attribute>\n");
            outXML.write(" \n");
            outXML.write("<attribute id=\"obj_semsort_level\" name=\"Semantic_Sort_of_Object\" >\n");
            outXML.write("     <value id=\"val_obj_semsort_0\" name=\"none\"/>\n");
            outXML.write("     <value id=\"val_obj_semsort_1\" name=\"human\"/>\n");
            outXML.write("     <value id=\"val_obj_semsort_2\" name=\"profession\"/>\n");
            outXML.write("     <value id=\"val_obj_semsort_3\" name=\"organization\"/>\n");
            outXML.write("     <value id=\"val_obj_semsort_4\" name=\"animal\"/>\n");
            outXML.write("     <value id=\"val_obj_semsort_5\" name=\"plant\"/>\n");
            outXML.write("     <value id=\"val_obj_semsort_6\" name=\"artifact\"/>\n");
            outXML.write("     <value id=\"val_obj_semsort_7\" name=\"amount\"/>\n");
            outXML.write("     <value id=\"val_obj_semsort_8\" name=\"date_or_time\"/>\n");
            outXML.write("     <value id=\"val_obj_semsort_9\" name=\"location\"/>\n");
            outXML.write("     <value id=\"val_obj_semsort_10\" name=\"collection\"/>\n");
            outXML.write("     <value id=\"val_obj_semsort_11\" name=\"abstract\"/>\n");
            outXML.write("     <value id=\"val_obj_semsort_12\" name=\"other\"/>\n");
            outXML.write(" </attribute>\n");
            outXML.write("\n");
            outXML.write("<attribute id=\"event_semsort_level\" name=\"Semantic_Sort_of_Eventuality\" >\n");
            outXML.write("     <value id=\"val_event_semsort_0\" name=\"none\"/>\n");
            outXML.write("     <!--     <value id=\"val_event_semsort_1\" name=\"habit\"/>-->\n");
            outXML.write("     <value id=\"val_event_semsort_2\" name=\"state\"/>\n");
            outXML.write("     <value id=\"val_event_semsort_3\" name=\"activity\"/>\n");
            outXML.write("     <value id=\"val_event_semsort_4\" name=\"accomplishment\"/>\n");
            outXML.write("     <value id=\"val_event_semsort_5\" name=\"achievement\"/>\n");
            outXML.write(" </attribute>\n");
            outXML.write("\n");
            outXML.write("<attribute id=\"text_semsort_level\" name=\"Semantic_Sort_of_Textuality\" >\n");
            outXML.write("     <value id=\"val_text_semsort_0\" name=\"none\"/>\n");
            outXML.write("     <value id=\"val_text_semsort_1\" name=\"proposition\"/>\n");
            outXML.write("     <value id=\"val_text_semsort_2\" name=\"speech_act\"/>\n");
            outXML.write("     <value id=\"val_text_semsort_3\" name=\"other\"/>\n");
            outXML.write(" </attribute>\n");
            outXML.write("\n");
            outXML.write("\n");
            outXML.write("<attribute id=\"obj_subtype_level\" name=\"Object_Subtype\" >\n");
            outXML.write("     <value id=\"val_obj_subtype_0\" name=\"none\"/>\n");
            outXML.write("     <value id=\"val_obj_subtype_1\" name=\"extensional\"/>\n");
            outXML.write("     <value id=\"val_obj_subtype_2\" name=\"intensional\"/>\n");
            outXML.write(" </attribute>\n");
            outXML.write("\n");
            outXML.write("<attribute id=\"prop_subtype_level\" name=\"Property_Subtype\" >\n");
            outXML.write("     <value id=\"val_prop_subtype_0\" name=\"none\"/>\n");
            outXML.write("     <value id=\"val_prop_subtype_1\" name=\"temporal\"/>\n");
            outXML.write("     <value id=\"val_prop_subtype_2\" name=\"permanent\"/>\n");
            outXML.write(" </attribute>\n");
            outXML.write("\n");
            outXML.write("<attribute id=\"event_iter_level\" name=\"Iteration_of_Eventuality\" >\n");
            outXML.write("     <value id=\"val_iter_0\" name=\"none\"/>\n");
            outXML.write("     <value id=\"val_iter_1\" name=\"noniterated\"/>\n");
            outXML.write("     <value id=\"val_iter_2\" name=\"iterated-unspecific\"/>\n");
            outXML.write("     <value id=\"val_iter_3\" name=\"iterated-specific\" />\n");
            outXML.write(" </attribute>\n");
            outXML.write("\n");
            outXML.write("<attribute id=\"delim_level\" name=\"Delimitation_of_Object\" >\n");
            outXML.write("     <value id=\"val_delim_0\" name=\"none\"/>\n");
            outXML.write("     <value id=\"val_delim_1\" name=\"unique\"/>\n");
            outXML.write("     <value id=\"val_delim_2\" name=\"existential\"/>\n");
            outXML.write("     <value id=\"val_delim_3\" name=\"variable\"/>\n");
            outXML.write("     <value id=\"val_delim_4\" name=\"non-denotational\"/>\n");
            outXML.write(" </attribute>\n");
            outXML.write("\n");
            outXML.write("<attribute id=\"quant_level\" name=\"Quantification_of_Object\" >\n");
            outXML.write("     <value id=\"val_quant_0\" name=\"none\"/>\n");
            outXML.write("     <value id=\"val_quant_1\" name=\"uncountable\"/>\n");
            outXML.write("     <value id=\"val_quant_2\" name=\"unspecific_multiple\"/>\n");
            outXML.write("     <value id=\"val_quant_3\" name=\"specific_multiple\" />\n");
            outXML.write("     <value id=\"val_quant_4\" name=\"specific_single\"/>\n");
            outXML.write(" </attribute>\n");
            outXML.write("\n");
            outXML.write("\n");
            outXML.write("<attribute id=\"form_level\" name=\"Linguistic_Form\" >\n");
            outXML.write("     <value id=\"val_delim_0\" name=\"none\"/>\n");
            outXML.write("     <value id=\"val_delim_1\" name=\"nominal\"/>\n");
            outXML.write("     <value id=\"val_delim_2\" name=\"clitic\"/>\n");
            outXML.write("     <value id=\"val_delim_3\" name=\"zero_arg\"/>\n");
            outXML.write("     <value id=\"val_delim_4\" name=\"clause\"/>\n");
            outXML.write("     <value id=\"val_delim_5\" name=\"text\"/>\n");
            outXML.write(" </attribute>\n");
            outXML.write("\n");
            outXML.write("<attribute id=\"info_level\" name=\"Information_Status_of_Object\" >\n");
            outXML.write("     <value id=\"val_delim_0\" name=\"none\"/>\n");
            outXML.write("     <value id=\"val_delim_1\" name=\"text_evoked\"/>\n");
            outXML.write("     <value id=\"val_delim_2\" name=\"situation_evoked\"/>\n");
            outXML.write("     <value id=\"val_delim_3\" name=\"unused\"/>\n");
            outXML.write("     <value id=\"val_delim_4\" name=\"inferrable\"/>\n");
            outXML.write("     <value id=\"val_delim_5\" name=\"brand_new\"/>\n");
            outXML.write(" </attribute>\n");
            outXML.write("\n");
            outXML.write("\n");
            outXML.write("<attribute id=\"reflink_level\" name=\"Referential_Link\" >\n");
            outXML.write("     <value id=\"val_delim_0\" name=\"none\"/>\n");
            outXML.write("     <value id=\"val_delim_1\" name=\"identity_of_referent\"/>\n");
            outXML.write("     <value id=\"val_delim_2\" name=\"identity_of_sense\"/>\n");
            outXML.write("     <value id=\"val_delim_3\" name=\"bridging\" next=\"bridge_subtype_level\"/>\n");
            outXML.write(" </attribute>\n");
            outXML.write("\n");
            outXML.write("<attribute id=\"reflink_level\" name=\"Referential_Link\" >\n");
            outXML.write("     <value id=\"val_reflink_0\" name=\"none\"/>\n");
            outXML.write("     <value id=\"val_reflink_1\" name=\"identity_of_referent\"/>\n");
            outXML.write("     <value id=\"val_reflink_2\" name=\"identity_of_sense\"/>\n");
            outXML.write("     <value id=\"val_reflink_3\" name=\"bridging\" next=\"bridge_link_level\"/>\n");
            outXML.write(" </attribute>\n");
            outXML.write("\n");
            outXML.write("<attribute id=\"bridge_link_level\" name=\"Bridging_Link\" >\n");
            outXML.write("     <value id=\"val_brilink_0\" name=\"none\"/>\n");
            outXML.write("     <value id=\"val_brilink_1\" name=\"containment\"     next=\"cont_link_level\"/>\n");
            outXML.write("     <value id=\"val_brilink_2\" name=\"composition\"     next=\"comp_link_level\" />\n");
            outXML.write("     <value id=\"val_brilink_3\" name=\"attribution\"     next=\"attr_link_level\" />\n");
            outXML.write("     <value id=\"val_brilink_6\" name=\"lexical_role\"    next=\"lexr_link_level\" />\n");
            outXML.write("     <value id=\"val_brilink_5\" name=\"causal_relation\" next=\"caus_link_level\" />\n");
            outXML.write("     <value id=\"val_brilink_4\" name=\"appurtenance\"  />\n");
            outXML.write(" </attribute>\n");
            outXML.write("\n");
            outXML.write("\n");
            outXML.write("<attribute id=\"cont_link_level\" name=\"Containment_Link\" >\n");
            outXML.write("     <value id=\"val_contlink_0\" name=\"none\"/>\n");
            outXML.write("     <value id=\"val_contlink_1\" name=\"subset\"/>\n");
            outXML.write("     <value id=\"val_contlink_2\" name=\"superset\"/>\n");
            outXML.write("     <value id=\"val_contlink_3\" name=\"sibling-subset\"/>\n");
            outXML.write(" </attribute>\n");
            outXML.write("\n");
            outXML.write("<attribute id=\"comp_link_level\" name=\"Composition_Link\" >\n");
            outXML.write("     <value id=\"val_contlink_0\" name=\"none\"/>\n");
            outXML.write("     <value id=\"val_contlink_1\" name=\"part\"/>\n");
            outXML.write("     <value id=\"val_contlink_2\" name=\"whole\"/>\n");
            outXML.write("     <value id=\"val_contlink_3\" name=\"sibling-part\"/>\n");
            outXML.write(" </attribute>\n");
            outXML.write("\n");
            outXML.write("<attribute id=\"attr_link_level\" name=\"Attribution_Link\" >\n");
            outXML.write("     <value id=\"val_attrlink_0\" name=\"none\"/>\n");
            outXML.write("     <value id=\"val_attrlink_1\" name=\"attribute\"/>\n");
            outXML.write("     <value id=\"val_attrlink_2\" name=\"attribute-carrier\"/>\n");
            outXML.write("     <value id=\"val_attrlink_3\" name=\"sibling-attribute\"/>\n");
            outXML.write(" </attribute>\n");
            outXML.write("\n");
            outXML.write("<attribute id=\"cous_link_level\" name=\"Causal_Link\" >\n");
            outXML.write("     <value id=\"val_causlink_0\" name=\"none\"/>\n");
            outXML.write("     <value id=\"val_causlink_1\" name=\"cause\"/>\n");
            outXML.write("     <value id=\"val_causlink_3\" name=\"sibling-effect\"/>\n");
            outXML.write("     <value id=\"val_causlink_4\" name=\"sibling-cause\"/>\n");
            outXML.write(" </attribute>\n");
            outXML.write("<attribute id=\"level_member\" name=\"member\" type=\"id\">\n");
            outXML.write("<value id=\"value_member\" name =\"member\"/>\n");
            outXML.write("</attribute>\n");
            outXML.write("\n");
            outXML.write("<attribute id=\"level_pointer\" name=\"pointer\" type=\"id\">\n");
            outXML.write("<value id=\"value_pointer\" name =\"pointer\"/>\n");
            outXML.write("</attribute>\n");
            outXML.write("\n");
            outXML.write("</annotationscheme>\n");
            outXML.close();
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } // catch
        System.err.println("org.jscience.ml.tigerxml.converters.Tiger2MMAX: Wrote " + outFileName);
    }

    private static void writeTextDTD() {
        FileWriter outXML = null;
        String outFileName = outputDirName + SLASH + "text.dtd";
        /* Open "text.dtd" and write the data */
        try {
            outXML = new FileWriter(outFileName);
            outXML.write(getComment());
            outXML.write("<!ELEMENT text (headline?,sentence+)>\n");
            outXML.write("<!ATTLIST text id ID #REQUIRED>\n");
            outXML.write("<!ELEMENT headline (sentence)>\n");
            outXML.write("<!ELEMENT sentence EMPTY>\n");
            outXML.write("<!ATTLIST sentence id ID #REQUIRED>\n");
            outXML.write("<!ATTLIST sentence span CDATA #REQUIRED>\n");
            outXML.close();
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } // catch
        System.err.println("org.jscience.ml.tigerxml.converters.Tiger2MMAX: Wrote " + outFileName);
    }

    private static void writeWordsDTD() {
        FileWriter outXML = null;
        String outFileName = outputDirName + SLASH + "words.dtd";
        /* Open "words.dtd" and write the data */
        try {
            outXML = new FileWriter(outFileName);
            outXML.write(getComment());
            outXML.write("<!ELEMENT words (word*)>\n");
            outXML.write("<!ELEMENT word (#PCDATA)>\n");
            outXML.write("<!ATTLIST word id ID #REQUIRED>\n");
            outXML.write("<!ATTLIST word starttime CDATA #IMPLIED>\n");
            outXML.write("<!ATTLIST word endtime CDATA #IMPLIED>\n");
            outXML.close();
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } // catch
        System.err.println("org.jscience.ml.tigerxml.converters.Tiger2MMAX: Wrote " + outFileName);
    }

    private static void writeTextXSL() {
        FileWriter outXML = null;
        String outFileName = outputDirName + SLASH + "text.xsl";
        /* Open "text.xsl" and write the data */
        try {
            outXML = new FileWriter(outFileName);
            outXML.write("<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">\n");
            outXML.write("  <xsl:output method=\"text\" indent=\"no\" omit-xml-declaration=\"yes\"/>\n");
            outXML.write("<xsl:strip-space elements=\"sentence paragraph text\"/>\n");
            outXML.write("<xsl:template match=\"text\">\n");
            outXML.write("<xsl:apply-templates/>\n");
            outXML.write("</xsl:template>\n");
            outXML.write("\n");
            outXML.write("<xsl:template match=\"headline\">\n");
            outXML.write("<!--xsl:text>&lt;bold&gt;</xsl:text-->\n");
            outXML.write("<xsl:apply-templates/>\n");
            outXML.write("<!--xsl:text>&lt;/bold&gt;</xsl:text-->\n");
            outXML.write("<xsl:text>\n");
            outXML.write("\n");
            outXML.write("</xsl:text>\n");
            outXML.write("</xsl:template>\n");
            outXML.write("\n");
            outXML.write("\n");
            outXML.write("<xsl:template match=\"paragraph\">\n");
            outXML.write("<xsl:text>&lt;bold&gt;</xsl:text><xsl:value-of select=\"@id\"/><xsl:text>&lt;/bold&gt;</xsl:text>\n");
            outXML.write("<xsl:apply-templates/>\n");
            outXML.write("<xsl:text>\n");
            outXML.write("</xsl:text>\n");
            outXML.write("</xsl:template>\n");
            outXML.write("\n");
            outXML.write("<xsl:template match=\"sentence\">\n");
            outXML.write("<!--<xsl:value-of select=\"@id\"/>-->\n");
            outXML.write("<xsl:apply-templates/>\n");
            outXML.write("<xsl:text></xsl:text>\n");
            outXML.write("</xsl:template>\n");
            outXML.write("\n");
            outXML.write("\n");
            outXML.write("<xsl:template match=\"utterance\">\n");
            outXML.write("<xsl:value-of select=\"@dialogue_act\"/>\n");
            outXML.write("<xsl:text> </xsl:text>\n");
            outXML.write("</xsl:template>\n");
            outXML.write("\n");
            outXML.write("\n");
            outXML.write("<xsl:template match=\"signal\">\n");
            outXML.write("<xsl:text>&lt;</xsl:text><xsl:value-of select=\"@id\"/><xsl:text>&gt;</xsl:text><xsl:apply-templates/><xsl:text>&lt;/signal&gt; </xsl:text>\n");
            outXML.write("</xsl:template>\n");
            outXML.write("\n");
            outXML.write("<xsl:template match=\"word\">\n");
            outXML.write("        <xsl:apply-templates/>\n");
            outXML.write("</xsl:template>\n");
            outXML.write("\n");
            outXML.write("<xsl:template match=\"gesture\">\n");
            outXML.write("<xsl:value-of select=\"@specifies\"/>\n");
            outXML.write("        <xsl:apply-templates/>\n");
            outXML.write("</xsl:template>\n");
            outXML.write("\n");
            outXML.write("</xsl:stylesheet>\n");
            outXML.write("\n");
            outXML.close();
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } // catch
        System.err.println("org.jscience.ml.tigerxml.converters.Tiger2MMAX: Wrote " + outFileName);
    }

    private static void writeSynAnnoFile() {
        FileWriter outXML = null;
        String outFileName = outputDirName + SLASH
                + "syn_"
                + outputDirName.substring(outputDirName.lastIndexOf(SLASH) + 1)
                + ".anno";
        /* Open the current .anno file and write the data */
        try {
            outXML = new FileWriter(outFileName);
            outXML.write("\"words words.xml\"\n");
            outXML.write("\"text text.xml\"\n");
            outXML.write("\"markables syn_markables.xml\"\n");
            outXML.write("\"stylesheet text.xsl\"\n");
            outXML.write("\"scheme syn_scheme.xml\"\n");
            outXML.close();
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } // catch
        System.err.println("org.jscience.ml.tigerxml.converters.Tiger2MMAX: Wrote " + outFileName);
    }

    private static void writeSemAnnoFile() {
        FileWriter outXML = null;
        String outFileName = outputDirName + SLASH
                + "sem_"
                + outputDirName.substring(outputDirName.lastIndexOf(SLASH) + 1)
                + ".anno";
        /* Open the current .anno file and write the data */
        try {
            outXML = new FileWriter(outFileName);
            outXML.write("\"words words.xml\"\n");
            outXML.write("\"text text.xml\"\n");
            outXML.write("\"markables sem_markables.xml\"\n");
            outXML.write("\"stylesheet text.xsl\"\n");
            outXML.write("\"scheme sem_scheme.xml\"\n");
            outXML.close();
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } // catch
        System.err.println("org.jscience.ml.tigerxml.converters.Tiger2MMAX: Wrote " + outFileName);
    }

    private static void writeMuliAnnoFile() {
        FileWriter outXML = null;
        String outFileName = outputDirName + SLASH
                + "muli_"
                + outputDirName.substring(outputDirName.lastIndexOf(SLASH) + 1)
                + ".anno";
        /* Open the current .anno file and write the data */
        try {
            outXML = new FileWriter(outFileName);
            outXML.write("\"words words.xml\"\n");
            outXML.write("\"text text.xml\"\n");
            outXML.write("\"markables muli_markables.xml\"\n");
            outXML.write("\"stylesheet text.xsl\"\n");
            outXML.write("\"scheme muli_scheme.xml\"\n");
            outXML.close();
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } // catch
        System.err.println("org.jscience.ml.tigerxml.converters.Tiger2MMAX: Wrote " + outFileName);
    }

    private static void writeHobbsMuliAnnoFile() {
        FileWriter outXML = null;
        String outFileName = outputDirName + SLASH
                + "hobbs78muli_"
                + outputDirName.substring(outputDirName.lastIndexOf(SLASH) + 1)
                + ".anno";
        /* Open the current .anno file and write the data */
        try {
            outXML = new FileWriter(outFileName);
            outXML.write("\"words words.xml\"\n");
            outXML.write("\"text text.xml\"\n");
            outXML.write("\"markables hobbsMuli_markables.xml\"\n");
            outXML.write("\"stylesheet text.xsl\"\n");
            outXML.write("\"scheme muli_scheme.xml\"\n");
            outXML.close();
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } // catch
        System.err.println("org.jscience.ml.tigerxml.converters.Tiger2MMAX: Wrote " + outFileName);
    }

    /**
     * Extract and write all words with their IDs to "output/words.xml"
     *
     * @param corp A <code>Corpus</code> instance
     */
    private static void writeWordsFile(Corpus corp) {

        FileWriter outXML = null;
        String outFileName = outputDirName + SLASH + "words.xml";

        /* Open "words.xml" and write the header */
        try {

            outXML = new FileWriter(outFileName);
            outXML.write("<?xml version='1.0' encoding='ISO-8859-1'?>\n");
            outXML.write("<!DOCTYPE words SYSTEM \"words.dtd\">\n");
            outXML.write(getComment());
            outXML.write("<words>\n");

            for (int i = 0; i < corp.getSentenceCount(); i++) {
                Sentence tmpSent = corp.getSentence(i);
                for (int j = 0; j < tmpSent.getTCount(); j++) {
                    T tmpT = tmpSent.getT(j);
                    String w = tmpT.getWord();
                    if (w.equals("&")) {
                        w = "&amp;";
                    }
                    outXML.write("  <word id=\"" + tmpT.getId() + "\">"
                            + w + "</word>\n");
                } // for j
            } // for i

            outXML.write("</words>\n");
            outXML.close();
        } // try
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } // catch
        System.err.println("org.jscience.ml.tigerxml.converters.Tiger2MMAX: Wrote " + outFileName);
    } // writeWordsFile()

    /**
     * Extract and write all sentence spans to "output/text.xml"
     *
     * @param corp A <code>Corpus</code> instance
     */
    private static void writeTextFile(Corpus corp) {

        FileWriter outXML = null;
        String outFileName = outputDirName + SLASH + "text.xml";

        /* Open "output/text.xml" and write the header */
        try {

            outXML = new FileWriter(outFileName);
            outXML.write("<?xml version='1.0' encoding='ISO-8859-1'?>\n");
            outXML.write("<!DOCTYPE words SYSTEM \"text.dtd\">\n");
            outXML.write(getComment());
            outXML.write("<text>\n");

            for (int i = 0; i < corp.getSentenceCount(); i++) {
                Sentence tmpSent = corp.getSentence(i);
                outXML.write("  <sentence id=\"" + tmpSent.getId() + "\""
                        + " span=\"" + tmpSent.getSpan() + "\"/>\n");
            } // for i

            outXML.write("</text>\n");
            outXML.close();
        } // try
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } // catch
        System.err.println("org.jscience.ml.tigerxml.converters.Tiger2MMAX: Wrote " + outFileName);
    } // writeTextFile()

    /**
     * Produce and write all markables to "output/syn_markables.xml"
     *
     * @param corp A <code>Corpus</code> instance
     */
    private static void writeSynMarkablesFile(Corpus corp) {

        FileWriter outXML = null;
        String outFileName = outputDirName + SLASH + "syn_markables.xml";

        /* Open "output/words.xml" and write the header */
        try {

            outXML = new FileWriter(outFileName);
            outXML.write("<?xml version='1.0' encoding='ISO-8859-1'?>\n");
            outXML.write(getComment());
            outXML.write("<markables>\n");

            for (int i = 0; i < corp.getSentenceCount(); i++) {
                Sentence tmpSent = corp.getSentence(i);

                /* Write all terminals */
                for (int j = 0; j < tmpSent.getTCount(); j++) {
                    T t = tmpSent.getT(j);
                    outXML.write("  <markable "
                            + "id=\"synmark_" + calcNumID(t) + "\" "
                            + "span=\"" + t.getId() + "\" "
                            + "class=\"t\" "
                            + "pos=\"" + t.getPos() + "\" "
                            + "morph=\"" + t.getMorph() + "\" "
                            + "edge=\"" + t.getEdge2Mother() + "\" ");
                    if (t.getMother() != null) {
                        outXML.write("pointer=\"synmark_" + calcNumID(t.getMother()) + "\"");
                    }
                    outXML.write("></markable>\n");
                } // for j

                /* Write all non-terminals */
                for (int j = 0; j < tmpSent.getNTCount(); j++) {
                    NT nt = tmpSent.getNT(j);
                    // calculate a unique numeric identifier for this non-terminal
                    int numID = ((tmpSent.getIndex() + 1) * 1000) + 500 + nt.getIndex();
                    outXML.write("  <markable "
                            + "id=\"synmark_" + numID + "\" "
                            + "span=\"" + nt.getSpan() + "\" "
                            + "class=\"nt\" "
                            + "cat=\"" + nt.getCat() + "\" "
                            + "edge=\"" + nt.getEdge2Mother() + "\" ");
                    if (nt.getMother() != null) {
                        outXML.write("pointer=\"synmark_" + calcNumID(nt.getMother()) +
                                "\"");
                    }
                    outXML.write("></markable>\n");
                } // for j
            } // for i

            outXML.write("</markables>\n");
            outXML.close();
        } // try
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } // catch
        System.err.println("org.jscience.ml.tigerxml.converters.Tiger2MMAX: Wrote " + outFileName);
    } // writeSynMarkablesFile()

    private static void writeSemMarkablesFile(Corpus corp) {

        FileWriter outXML = null;
        String outFileName = outputDirName + SLASH + "sem_markables.xml";

        /* Open "output/words.xml" and write the header */
        try {

            outXML = new FileWriter(outFileName);
            outXML.write("<?xml version='1.0' encoding='ISO-8859-1'?>\n");
            outXML.write(getComment());
            outXML.write("<markables>\n");

            int runningID = 0;

            /* Check all GraphNodes, write all markables (filtered) */
            ArrayList allGraphNodes = corp.getAllGraphNodes();
            for (int i = 0; i < allGraphNodes.size(); i++) {
                String span;
                GraphNode gn = (GraphNode) allGraphNodes.get(i);
                if (SyncMMAX.isMarkable(gn)) {
                    if (gn.isTerminal()) {
                        span = ((T) gn).getId();
                    } else {
                        span = ((NT) gn).getSpan();
                    }
                    outXML.write("  <markable "
                            + "id=\"markable_" + runningID++ + "\" "
                            + "span=\"" + span + "\" "
                            + "np_form=\"" + Strube02.getNpForm(gn) + "\" "
                            + "grammatical_role=\"" + Strube02.getGramRole(gn)
                            + "\" "
                            + "agreement=\"" + Strube02.getAgreement(gn) + "\"");
                    outXML.write("></markable>\n");
                } // if
            } // for i

            outXML.write("</markables>\n");
            outXML.close();
        } // try
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } // catch
        System.err.println("org.jscience.ml.tigerxml.converters.Tiger2MMAX: Wrote " + outFileName);
    } // writeSemMarkablesFile()

    private static void writeMuliMarkablesFile(Corpus corp) {

        FileWriter outXML = null;
        String outFileName = outputDirName + SLASH + "muli_markables.xml";

        /* Open "output/words.xml" and write the header */
        try {

            outXML = new FileWriter(outFileName);
            outXML.write("<?xml version='1.0' encoding='ISO-8859-1'?>\n");
            outXML.write(getComment());
            outXML.write("<markables>\n");

            int runningID = 0;

            /* Check all GraphNodes, write all markables (filtered) */
            ArrayList allGraphNodes = corp.getAllGraphNodes();
            for (int i = 0; i < allGraphNodes.size(); i++) {
                String span;
                GraphNode gn = (GraphNode) allGraphNodes.get(i);
                if (SyncMMAX.isMarkable(gn)) {
                    if (gn.isTerminal()) {
                        span = ((T) gn).getId();
                    } else {
                        span = ((NT) gn).getSpan();
                    }
                    outXML.write("  <markable "
                            + "id=\"markable_" + runningID++ + "\" "
                            + "span=\"" + span + "\"");
                    outXML.write("></markable>\n");
                } // if
            } // for i

            outXML.write("</markables>\n");
            outXML.close();
        } // try
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } // catch
        System.err.println("org.jscience.ml.tigerxml.converters.Tiger2MMAX: Wrote " + outFileName);
    } // writeMuliMarkablesFile()

    private static void writeHobbsMuliMarkablesFile(Corpus corp) {

        FileWriter outXML = null;
        String outFileName = outputDirName + SLASH + "hobbsMuli_markables.xml";

        /* Open "output/words.xml" and write the header */
        try {

            outXML = new FileWriter(outFileName);
            outXML.write("<?xml version='1.0' encoding='ISO-8859-1'?>\n");
            outXML.write(getComment());
            outXML.write("<markables>\n");

            int runningID = 0;

            /* Check all GraphNodes, write all markables (filtered) */
            ArrayList allGraphNodes = corp.getAllGraphNodes();
            for (int i = 0; i < allGraphNodes.size(); i++) {
                String span;
                GraphNode gn = (GraphNode) allGraphNodes.get(i);
                if (SyncMMAX.isMarkable(gn)) {
                    if (gn.isTerminal()) {
                        span = ((T) gn).getId();
                    } else {
                        span = ((NT) gn).getSpan();
                    }
                    outXML.write("  <markable "
                            + "id=\"markable_" + runningID++ + "\" "
                            + "span=\"" + span + "\"");
                    outXML.write("></markable>\n");
                } // if
            } // for i

            outXML.write("</markables>\n");
            outXML.close();
        } // try
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } // catch
        System.err.println("org.jscience.ml.tigerxml.converters.Tiger2MMAX: Wrote " + outFileName);
    } // writeMuliMarkablesFile()

    private static void writeEmptyMarkablesFile() {

        FileWriter outXML = null;
        String outFileName = outputDirName + SLASH + "empty_markables.xml";

        /* Open "output/words.xml" and write the header */
        try {
            outXML = new FileWriter(outFileName);
            outXML.write("<?xml version='1.0' encoding='ISO-8859-1'?>\n");
            outXML.write(getComment());
            outXML.write("<markables>\n");
            outXML.write("</markables>\n");
            outXML.close();
        } // try
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } // catch
        System.err.println("org.jscience.ml.tigerxml.converters.Tiger2MMAX: Wrote " + outFileName);
    } // writeEmptyMarkablesFile()

    /**
     * Returns a String similar to this:
     * <pre>
     * "<!-- Automatically generated ... -->"
     * <pre>
     * including the current time stamp and the name of this module.
     *
     * @return An XML comment "automatically ..." including the current time.
     */
    private static String getComment() {
        StringBuffer out = new StringBuffer();
        out.append("<!-- ");
        out.append("Automatically generated by org.jscience.ml.tigerxml.converters.Tiger2MMAX "
                + "(" + version + ")" + "\n");
        out.append("     Created on " + GeneralTools.getTimeStamp());
        out.append(" -->\n");
        return out.toString();
    }

    /**
     * Calculates a unique numeric identifier for the given GraphNode.
     *
     * @param gn The GraphNode for which to calculate a unique numeric ID.
     * @return The unique numeric identifier for the given GraphNode.
     */
    private static int calcNumID(GraphNode gn) {
        int nt_bonus = 0;
        if (!gn.isTerminal()) {
            nt_bonus += 500;
        }
        return (((gn.getSentence().getIndex() + 1) * 1000) + nt_bonus
                + gn.getIndex());
    }

    /**
     * Main
     *
     * @param args The (path and) name of the TiGerXML file to convert
     */
    public static void main(String[] args) {

        /* Run the main loop (for all files in args) */
        ArrayList allFilePaths = parseCmdLineArgs(args);
        System.err.println("This is org.jscience.ml.tigerxml.converters.Tiger2MMAX.");
        for (int i = 0; i < allFilePaths.size(); i++) {

            /* File preparations */
            // Get the complete path to the current input xml file
            String inputFilePath = (String) allFilePaths.get(i);
            // Set the file name without the path
            String inputFileName =
                    inputFilePath.substring(inputFilePath.lastIndexOf(SLASH) + 1);
            // Set and create the output directory based on the input file name
            outputDirName = outputDirNameBase + SLASH
                    + inputFileName.substring(0, inputFileName.lastIndexOf("."));
            File outputDir = new File(outputDirName);
            outputDir.mkdirs();

            /* Parse the XML file and return the corpus object */
            Corpus corpus = new Corpus(inputFilePath);

            /* Write all header files (DTD files, XSL files) */
            writeHeaderFiles();

            /* Write "words.xml" */
            writeWordsFile(corpus);

            /* Write "text.xml" */
            writeTextFile(corpus);

            /* Write "empty_markables.xml" */
            writeEmptyMarkablesFile();

            /* Write "syn_markables.xml" */
            writeSynMarkablesFile(corpus);

            /* Write "sem_markables.xml" */
            writeSemMarkablesFile(corpus);

            /* Write "muli_markables.xml" */
            writeMuliMarkablesFile(corpus);

            /* Write "hobbsMuli_markables.xml" */
            writeHobbsMuliMarkablesFile(corpus);

        } // for i
    } // main
} // class
