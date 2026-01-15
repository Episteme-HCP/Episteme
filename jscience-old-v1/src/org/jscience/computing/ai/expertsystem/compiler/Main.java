package org.jscience.computing.ai.expertsystem.compiler;

/*
* JEOPS - The Java Embedded Object Production System
* Copyright (c) 2000   Carlos Figueira Filho
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*
* Contact: Carlos Figueira Filho (csff@cin.ufpe.br)
*/

import org.jscience.computing.ai.expertsystem.compiler.parser.Scanner;
import org.jscience.computing.ai.expertsystem.compiler.parser.Token;
import org.jscience.computing.ai.expertsystem.compiler.parser.TokenConstants;

import java.io.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Main class in the Jeops phase of converting rule files into Java
 * classes.<p>
 * When invoked, the method <code>convert()</code> will create a java file
 * in the same directory as the rule file (with the extension changed from
 * <code>.rules</code> to <code>.java</code>). In this way, rule files can
 * be grouped into packages in the same way as java classes.
 *
 * @author Carlos Figueira Filho (<a href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.02  14.01.2000 Grouping of the conditions according to the
 *          appearance of the declaration.
 * @history 0.01  06.01.2000 First version
 */
public class Main implements TokenConstants {

    /**
     * The writer used to create the java file.
     */
    private PrintWriter writer;

    /**
     * The name of the rules file.
     */
    private String ruleFileName;

    /**
     * The name of the generated java file.
     */
    private String javaFileName;

    /**
     * The name of this rule base.
     */
    private String ruleBaseName;

    /**
     * The scanner used to read from the input file.
     */
    private Scanner scanner;

    /**
     * The current token.
     */
    private Token token;

    /**
     * The list of declaration of instance variables of the generated
     * java file.
     */
    private Vector variables;

    /**
     * The list of the identifiers of the instance variables of the
     * generated java file.
     */
    private Vector variableIdents;

    /**
     * The classes of the identifiers generated in the new java file.
     */
    private Vector declaredClassNames;

    /**
     * The name of the rule being converted.
     */
    private String ruleName;

    /**
     * The number of declarations in each rule.
     */
    private Vector numberOfDeclarations;

    /**
     * The number of local declarations in each rule.
     */
    private Vector numberOfLocalDeclarations;

    /**
     * The number of conditions in each rule.
     */
    private Vector numberOfConditions;

    /**
     * The names of the rules.
     */
    private Vector ruleNames;

    /**
     * The import list of this rule base.
     */
    private ImportList importList;

    /**
     * The declarations of the current rule.
     */
    private Vector ruleDeclarations;

    /**
     * The converted declarations of the current rule.
     */
    private Vector convertedRuleDeclarations;

    /**
     * The local declarations of the current rule.
     */
    private Vector ruleLocalDeclarations;

    /**
     * The converted local declarations of the current rule.
     */
    private Vector convertedRuleLocalDeclarations;

    /**
     * The index of the last declared identifier used by some
     * rule local declaration.
     */
    private Vector lastDeclForLocalDecl;

    /**
     * The index of all declared identifiers used by some rule local
     * declaration. It's a vector of Integer Vectors.
     */
    private Vector allDeclForLocalDecl;

    /**
     * Flag that indicates whether a rule base is declared in the file.
     */
    private boolean thereIsRuleBase = false;

//	/**
//	 * Vector (of vectors) of the classes of the local declarations.
//	 */
//	private Vector localDeclClasses;

    /**
     * Class constructor.
     *
     * @param ruleFileName the name of the rules file.
     * @throws IOException if some IO error occurs.
     */
    public Main(String ruleFileName) throws IOException {
        scanner = new Scanner(ruleFileName);
        this.ruleFileName = ruleFileName;
        variables = new Vector();
        variableIdents = new Vector();
        numberOfConditions = new Vector();
        numberOfDeclarations = new Vector();
        numberOfLocalDeclarations = new Vector();
//		localDeclClasses = new Vector();
        ruleNames = new Vector();
        importList = new ImportList();
        File aux = new File(ruleFileName);
        if (!aux.exists()) {
            throw new FileNotFoundException("File not found: " + ruleFileName);
        }
        ruleFileName = aux.getAbsolutePath();
        int i = ruleFileName.lastIndexOf(".rules");
        if (i != -1) {
            ruleBaseName = ruleFileName.substring(0, i);
        } else {
            ruleBaseName = ruleFileName;
        }
        javaFileName = ruleBaseName + ".java";
    }

    /**
     * Checks whether a token is one of several given types, throwing an
     * exception if it's not.
     *
     * @param token   the token to be checked.
     * @param types   the desired token types.
     * @param message the message in the exception.
     * @throws JeopsException if the token is of an unexpected type.
     */
    private void checkToken(Token token, int[] tokenTypes,
                            String message) throws JeopsException {
        boolean ok = false;
        for (int i = 0; !ok && i < tokenTypes.length; i++) {
            if (token.getTokenType() == tokenTypes[i]) {
                ok = true;
            }
        }
        if (!ok) {
            message = message.concat(" at line " +
                    scanner.getCurrentLine() + ", column " +
                    scanner.getCurrentColumn());
            throw new JeopsException(message, scanner.getCurrentLine(),
                    scanner.getCurrentColumn());
        }
    }

    /**
     * Checks whether a token is of a given type, throwing an exception
     * if it's not.
     *
     * @param token   the token to be checked.
     * @param type    the desired token type.
     * @param message the message in the exception.
     * @throws JeopsException if the token is of an unexpected type.
     */
    private void checkToken(Token token, int tokenType,
                            String message) throws JeopsException {
        if (token.getTokenType() != tokenType) {
            message = message.concat(" at line " +
                    scanner.getCurrentLine() + ", column " +
                    scanner.getCurrentColumn());
            throw new JeopsException(message, scanner.getCurrentLine(),
                    scanner.getCurrentColumn());
        }
    }

    /**
     * Attempts to find a source file and compile it into a .class file.
     *
     * @param className the name of the class to be compiled.
     * @return <code>true</code> if the compilation could be performed;
     *         <code>false</code> otherwise.
     * @throws JeopsException if some error occurred during the compilation.
     */
    private boolean compileSourceFile(String className) throws JeopsException {
        String[] possibleNames = importList.possibleFileNames(className);
        boolean result = false;
        for (int i = 0; i < possibleNames.length; i++) {
            String fileName = possibleNames[i].replace('.', File.separatorChar) + ".java";
            File f = new File(fileName);
            if (f.exists()) {
                if (JavaCompiler.compileJavaFile(fileName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Creates a java source file that behaves as defined in the rules file.
     *
     * @throws IOException    if some IO error occurs.
     * @throws JeopsException if some error occurs while converting the rule.
     */
    public void convert() throws IOException, JeopsException {
        File file = File.createTempFile("org.jscience.computing.ai.expertsystem", ".tmp");
        FileWriter fw = new FileWriter(file);
        PrintWriter pw = new PrintWriter(fw);

        int openBracketCount = 0;
        boolean toPrint;
        boolean inRuleBaseDeclaration = false;
        boolean inRuleDeclaration = false;
        boolean inRuleBase = false;
        boolean inRule = false;
        boolean thereIsExtends = false;
        boolean thereIsPublic = false;
        token = readNextToken();
        while (token.getTokenType() != EOF) {
            toPrint = false;
            switch (token.getTokenType()) {
                case ERROR: {
                    throw new JeopsException("Error: " + token.getLexeme(),
                            scanner.getCurrentLine(),
                            scanner.getCurrentColumn());
                }
                case OPEN_CURLY_BRACKET: {
                    openBracketCount++;
                    if (openBracketCount == 1) {
                        inRuleBase = true;
                    }
                    if (inRuleBaseDeclaration) {
                        if (!thereIsExtends) {
                            pw.print("extends org.jscience.computing.ai.expertsystem.AbstractRuleBase ");
                        }
                        inRuleBaseDeclaration = false;
                    }
                    toPrint = true;
                    break;
                }
                case CLOSE_CURLY_BRACKET: {
                    openBracketCount--;
                    if (openBracketCount == 0) {
                        inRuleBase = false;
                        processEndOfRuleBase(pw);
                    } else if (openBracketCount == 1) {
                        inRule = false;
                    }
                    toPrint = true;
                    break;
                }
                case IDENT: {
                    toPrint = true;
                    break;
                }
                case RULE_BASE: {
                    inRuleBaseDeclaration = true;
                    thereIsRuleBase = true;
                    token = readNextToken();
                    skipWhiteSpace(pw, true);
                    checkToken(token, IDENT, "identifier expected");
                    ruleBaseName = token.getLexeme();
                    pw.print("class Jeops_RuleBase_" + ruleBaseName);
                    break;
                }
                case PUBLIC: {
                    toPrint = false; // So' ha' uma classe publica: a base de regras.
                    break;
                }
                case RULE: {
                    convertRule(pw);
                    break;
                }
                case EXTENDS: {
                    toPrint = true;
                    thereIsExtends = true;
                    break;
                }
                case IMPORT: {
                    toPrint = false;
                    processImport(pw);
                    break;
                }
                case PACKAGE: {
                    toPrint = false;
                    processPackage(pw);
                    break;
                }
                case IMPLEMENTS: {
                    if (!thereIsExtends) {
                        thereIsExtends = true;
                        pw.print("extends org.jscience.computing.ai.expertsystem.AbstractRuleBase ");
                    }
                    toPrint = true;
                    break;
                }
                default: {
                    toPrint = true;
                    break;
                }
            }
            if (toPrint) {
                pw.print(token.getLexeme());
            }
            token = readNextToken();
        }

        // We're at the end of the file; now that the rule base class is
        // already defined, let's define the knowledge base itself.
        pw.println();
        pw.println("/**");
        String name = ruleFileName;
        int aux = name.lastIndexOf(File.separatorChar);
        if (aux != -1) {
            name = name.substring(aux + 1);
        }
        pw.println(" * Knowledge base created by JEOPS from file " + name);
        pw.println(" *");
        Date now = new Date();
        DateFormat dateFormat = DateFormat.getDateInstance();
        pw.println(" * @version " + dateFormat.format(now));
        pw.println(" */");
        pw.println("public class " + ruleBaseName + " extends org.jscience.computing.ai.expertsystem.AbstractKnowledgeBase {");
        pw.println();
        pw.println("    /**");
        pw.println("     * Creates a new knowledge base with the specified conflict set with the");
        pw.println("     * desired conflict resolution policy.");
        pw.println("     *");
        pw.println("     * @param conflictSet a conflict set with the desired resolution policy");
        pw.println("     */");
        pw.println("    public " + ruleBaseName + "(org.jscience.computing.ai.expertsystem.conflict.ConflictSet conflictSet) {");
        pw.println("        super(conflictSet);");
        pw.println("    }");
        pw.println();
        pw.println("    /**");
        pw.println("     * Creates a new knowledge base, using the default conflict resolution");
        pw.println("     * policy.");
        pw.println("     */");
        pw.println("    public " + ruleBaseName + "() {");
        pw.println("        this(new org.jscience.computing.ai.expertsystem.conflict.DefaultConflictSet());");
        pw.println("    }");
        pw.println();
        pw.println("    /**");
        pw.println("     * Factory method used to instantiate the rule base.");
        pw.println("     */");
        pw.println("    protected org.jscience.computing.ai.expertsystem.AbstractRuleBase createRuleBase() {");
        pw.println("        return new Jeops_RuleBase_" + ruleBaseName + "(this);");
        pw.println("    }");
        pw.println();
        pw.println("}");

        pw.flush();
        pw.close();
        fw.close();

        if (!thereIsRuleBase) {
            throw new JeopsException("There should be a rule base in the input file!",
                    scanner.getCurrentLine(),
                    scanner.getCurrentColumn());
        }
        int separatorIndex = javaFileName.lastIndexOf(File.separatorChar);
        javaFileName = javaFileName.substring(0, separatorIndex + 1) +
                ruleBaseName + ".java";
        File finalFile = new File(javaFileName);
        if (finalFile.exists()) {
            finalFile.delete();
        }
        file.renameTo(finalFile);
        JavaCompiler.compileJavaFile(finalFile.getAbsolutePath());
    }

    /**
     * Converts a rule into a set of java methods.
     *
     * @param writer the print writer used to write to the generated file.
     * @throws IOException    if some IO error occurs.
     * @throws JeopsException if some error occurs while converting the rule.
     */
    private void convertRule(PrintWriter writer)
            throws IOException, JeopsException {

        // We expect the next token to be the rule name...
        token = readNextToken();
        skipWhiteSpace(writer, false);
        checkToken(token, IDENT, "Ident expected");
        ruleName = token.getLexeme();
        if (ruleNames.contains(ruleName)) {
            String message = "Duplicate rule declaration: " + ruleName +
                    " at line " + scanner.getCurrentLine() +
                    ", column " + scanner.getCurrentColumn();
            throw new JeopsException(message,
                    scanner.getCurrentLine(),
                    scanner.getCurrentColumn());
        }
        ruleNames.addElement(ruleName);

        token = readNextToken();
        skipWhiteSpace(writer, false);
        checkToken(token, OPEN_CURLY_BRACKET, "'{' expected");

        token = readNextToken();
        skipWhiteSpace(writer, false);
        checkToken(token, DECLARATIONS, "'declarations' expected");
        convertRuleDeclarations(writer);
        convertRuleConditions(writer);
        convertRuleActions(writer);

    }

    /**
     * Processes the actions part of a rule. This method creates one void
     * method for the whole body of actions of the rule. The method will
     * have the same name of the rule.<p>
     * In order to be correctly invoked, the current token
     * type must be <code>ACTIONS</code>. After successful completion,
     * the token type is <code>CLOSE_CURLY_BRACKET</code>, indicating
     * the end of the rule.
     *
     * @param writer the print writer used to write to the generated file.
     * @throws IOException    if some IO error occurs.
     * @throws JeopsException if some error occurs while converting the rule.
     * @precondition token.getTokenType() == ACTIONS
     * @postcondition token.getTokenType() == CLOSE_CURLY_BRACKET
     */
    private void convertRuleActions(PrintWriter writer)
            throws IOException, JeopsException {

        int bracketCount = 1; // The number of opened brackets.

        token = readNextToken();

        writer.println();
        writer.println("    /**");
        writer.println("     * Executes the action part of the rule " + ruleName);
        writer.println("     */");
        writer.print("    private void " + ruleName + "() {");
        while (bracketCount > 0) {
            String lex = token.getLexeme();
            String converted = lex;
            Object map = null;
            if (token.getTokenType() == IDENT &&
                    scanner.getLastNonWhiteSpaceToken().getTokenType() != DOT) {
                int indDecl = ruleDeclarations.indexOf(lex);
                if (indDecl != -1) {
                    converted = (String) convertedRuleDeclarations.elementAt(indDecl);
                } else {
                    int indLocalDecl = ruleLocalDeclarations.indexOf(lex);
                    if (indLocalDecl != -1) {
                        converted = (String) convertedRuleLocalDeclarations.elementAt(indLocalDecl);
                    }
                }
            }
            writer.print(converted);
            token = readNextToken();
            if (token.getTokenType() == EOF) {
                String message = "EOF unexpected at line " +
                        scanner.getCurrentLine() + ", column " +
                        scanner.getCurrentColumn();
                throw new EOFException(message);
            } else if (token.getTokenType() == CLOSE_CURLY_BRACKET) {
                bracketCount--;
            } else if (token.getTokenType() == OPEN_CURLY_BRACKET) {
                bracketCount++;
            }
        }
        writer.println("    }");
        writer.println();
    }

    /**
     * Processes the conditions part of a rule. This method creates one
     * method (returning a boolean value) for every condition of the
     * rule. Each method will be named as the name of the rule followed
     * by "_cond_<n>", where n is the index of the condition.
     * Additionally, another method will be created, named as the name
     * of the rule followed only by "_cond", that will receive an integer
     * used to check the correct condition.<p>
     * In order to be correctly invoked, the current token
     * type must be <code>CONDITIONS</code>. After successful completion,
     * the token type is <code>ACTIONS</code>.
     *
     * @param writer the print writer used to write to the generated file.
     * @throws IOException    if some IO error occurs.
     * @throws JeopsException if some error occurs while converting the rule.
     * @precondition token.getTokenType() == CONDITIONS
     * @postcondition token.getTokenType() == ACTIONS
     */
    private void convertRuleConditions(PrintWriter writer)
            throws IOException, JeopsException {

        int condNumber = 0; // The number of the current condition.
        StringBuffer originalExpression;
        StringBuffer convertedExpression;

        Vector condGrouping = new Vector();
        Vector condsOfOnlyOneDeclaration = new Vector(); // A vector of (Integer) vectors.

        for (int i = 0; i < ruleDeclarations.size(); i++) {
            condsOfOnlyOneDeclaration.addElement(new Vector());
            condGrouping.addElement(new Vector());
        }

        token = readNextToken();
        skipWhiteSpace(writer, false);

        while (token.getTokenType() != ACTIONS) {
            originalExpression = new StringBuffer();
            convertedExpression = new StringBuffer();
            int maximumDeclaration = 0; // The last declared identifier
            // used in this condition, not
            // taking local declarations into
            // account.

            int numberOfUsedDeclarations = 0;
            int indexOfUsedDeclaration = 0;

            while (token.getTokenType() != SEMICOLON) {
                String lex = token.getLexeme();
                originalExpression.append(lex);
                String convertedLex = lex;
                int indDecl = -1;
                if (token.getTokenType() == IDENT &&
                        scanner.getLastNonWhiteSpaceToken().getTokenType() != DOT) {
                    indDecl = ruleDeclarations.indexOf(lex);

                    if (indDecl != -1) {  // It's a rule declaration
                        convertedLex = (String) convertedRuleDeclarations.elementAt(indDecl);
                    } else {
                        int localIndDecl = ruleLocalDeclarations.indexOf(lex);
                        if (localIndDecl != -1) { // It's a rule local declaration
                            convertedLex = (String) convertedRuleLocalDeclarations.elementAt(localIndDecl);
                            indDecl = ((Integer) lastDeclForLocalDecl.elementAt(localIndDecl)).intValue();
                        }
                    }
                }

                if (indDecl != -1) {
                    if (numberOfUsedDeclarations == 0) {
                        maximumDeclaration = indexOfUsedDeclaration = indDecl;
                        numberOfUsedDeclarations = 1;
                    } else {
                        if (indDecl != maximumDeclaration) {
                            numberOfUsedDeclarations++;
                            if (maximumDeclaration < indDecl) {
                                maximumDeclaration = indDecl;
                            }
                        }
                    }
                }

                convertedExpression.append(convertedLex);
                token = readNextToken();
                if (token.getTokenType() == EOF) {
                    String message = "EOF unexpected at line " +
                            scanner.getCurrentLine() + ", column " +
                            scanner.getCurrentColumn();
                    throw new EOFException(message);
                }
            }
            token = readNextToken();
            writer.println();
            writer.println("    /**");
            writer.println("     * Condition " + condNumber +
                    " of rule " + ruleName + ".<p>");
            writer.println("     * The original expression was:<br>");
            writer.println("     * <code>" + originalExpression.toString() + "</code>");
            writer.println("     *");
            writer.println("     * @return <code>true</code> if the condition is satisfied;");
            writer.println("     *          <code>false</code> otherwise.");
            writer.println("     */");
            writer.println("    private boolean " + ruleName + "_cond_" + condNumber + "() {");
            writer.println("        return (" + convertedExpression.toString() + ");");
            writer.println("    }");
            Vector v;
            if (numberOfUsedDeclarations > 1) {
                v = (Vector) condGrouping.elementAt(maximumDeclaration);
            } else {
                v = (Vector) condsOfOnlyOneDeclaration.elementAt(indexOfUsedDeclaration);
            }
            v.addElement(new Integer(condNumber));
            condNumber++;
            skipWhiteSpace(writer, false);
        }
        writer.println();
        writer.println("    /**");
        writer.println("     * Checks whether some conditions of rule " + ruleName + " is satisfied.");
        writer.println("     *");
        writer.println("     * @param index the index of the condition to be checked.");
        writer.println("     * @return <code>true</code> if the condition is satisfied;");
        writer.println("     *          <code>false</code> otherwise.");
        writer.println("     */");
        writer.println("    private boolean " + ruleName + "_cond(int index) {");
        writer.println("        switch (index) {");
        for (int i = 0; i < condNumber; i++) {
            writer.println("            case " + i + ": return " +
                    ruleName + "_cond_" + i + "();");
        }
        writer.println("            default: return false;");
        writer.println("        }");
        writer.println("    }");

        writer.println();
        writer.println("    /**");
        writer.println("     * Checks whether all conditions of rule " + ruleName + " that depend only on");
        writer.println("     * the given object are satisfied.");
        writer.println("     *");
        writer.println("     * @param declIndex the index of the declaration to be checked");
        writer.println("     * @return <code>true</code> if all corresponding conditions for");
        writer.println("     *          this rule are satisfied; <code>false</code> otherwise.");
        writer.println("     */");
        writer.println("    private boolean checkConditionsOnlyOf_" + ruleName + "(int declIndex) {");
        writer.println("        switch (declIndex) {");
        for (int i = 0; i < ruleDeclarations.size(); i++) {
            writer.println("            case " + i + ":");
            Vector aux = (Vector) condsOfOnlyOneDeclaration.elementAt(i);
            for (int j = 0; j < aux.size(); j++) {
                int num = ((Integer) aux.elementAt(j)).intValue();
                writer.println("                if (!" + ruleName + "_cond_" + num + "()) return false;");
            }
            writer.println("                return true;");
        }
        writer.println("            default: return false;");
        writer.println("        }");
        writer.println("    }");

        // Method for checking the conditions of a rule that use some
        // declared identifiers.
        writer.println();
        writer.println("    /**");
        writer.println("     * Checks whether all the conditions of a rule which");
        writer.println("     * reference some declared element of the declarations are");
        writer.println("     * true.");
        writer.println("     *");
        writer.println("     * @param declIndex the index of the declared element.");
        writer.println("     * @return <code>true</code> if the conditions that reference");
        writer.println("     *          up to the given declaration are true;");
        writer.println("     *          <code>false</code> otherwise.");
        writer.println("     */");
        writer.println("    private boolean checkCondForDeclaration_" +
                ruleName + "(int declIndex) {");
        writer.println("        switch (declIndex) {");
        for (int i = 0; i < condGrouping.size(); i++) {
            writer.println("            case " + i + ":");
            Vector v = (Vector) condGrouping.elementAt(i);
            for (int j = 0; j < v.size(); j++) {
                int condNo = ((Integer) v.elementAt(j)).intValue();
                writer.println("                if (!" + ruleName + "_cond_" + condNo + "()) return false;");
            }
            writer.println("                return true;");
        }
        writer.println("            default: return false;");
        writer.println("        }");
        writer.println("    }");

        numberOfConditions.addElement(new Integer(condNumber));
    }

    /**
     * Processes the declaration part of a rule. This method fills the
     * <code>ruleDeclaration</code> and
     * <code>convertedRuleDeclaration</code> fields with the declared
     * variables and their counterpart in the generated java file. It
     * will also fill the <code>condGrouping</code> field.<p>
     * This method also deals with the optional section of local
     * declarations of the rule, so that it guarantees that after this
     * method is successfully invoked, the token type is
     * <code>CONDITIONS</code><p>.
     * In order to be correctly invoked, the current token
     * type must be <code>DECLARATIONS</code>. After successful completion,
     * the token type is <code>CONDITIONS</code>.
     *
     * @param writer the print writer used to write to the generated file.
     * @throws IOException    if some IO error occurs.
     * @throws JeopsException if some error occurs while converting the rule.
     * @precondition token.getTokenType() == DECLARATIONS
     * @postcondition token.getTokenType() == CONDITIONS
     */
    private void convertRuleDeclarations(PrintWriter writer)
            throws IOException, JeopsException {
        // Auxiliar variable used to indicate the number of declared
        // variables of each type (class).
        Hashtable quantities = new Hashtable();
        token = readNextToken();
        skipWhiteSpace(writer, false);
        ruleDeclarations = new Vector();
        convertedRuleDeclarations = new Vector();
        convertedRuleLocalDeclarations = new Vector();
        ruleLocalDeclarations = new Vector();

//		localDeclClasses.addElement(new Vector());

        declaredClassNames = new Vector();

        while (token.getTokenType() != CONDITIONS &&
                token.getTokenType() != LOCALDECL) {
            checkToken(token, IDENT, "identifier expected");
            String declaredType = token.getLexeme();
            token = readNextToken();
            while (token.getTokenType() == DOT) {
                declaredType = declaredType.concat(token.getLexeme());
                token = readNextToken();
                checkToken(token, IDENT, "identifier expected");
                declaredType = declaredType.concat(token.getLexeme());
                token = readNextToken();
            }
            skipWhiteSpace(writer, false);
            checkToken(token, IDENT, "identifier expected");

            try {
                Class c = importList.getRepresentingClass(declaredType);
                declaredType = c.getName();
            } catch (ClassNotFoundException e) {
                if (compileSourceFile(declaredType)) {
                    try {
                        Class c2 = importList.getRepresentingClass(declaredType);
                        declaredType = c2.getName();
                    } catch (ClassNotFoundException e2) {
                        throw new JeopsException("ClassNotFound: " + e.getMessage(),
                                scanner.getCurrentLine(),
                                scanner.getCurrentColumn());
                    } catch (ImportException e2) {
                        throw new JeopsException(e.getMessage(),
                                scanner.getCurrentLine(),
                                scanner.getCurrentColumn());
                    }
                } else {
                    throw new JeopsException("ClassNotFound: " + e.getMessage(),
                            scanner.getCurrentLine(),
                            scanner.getCurrentColumn());
                }
            } catch (ImportException e) {
                throw new JeopsException(e.getMessage(),
                        scanner.getCurrentLine(),
                        scanner.getCurrentColumn());
            }

            // We've found a declared variable.
            String declaredIdent = token.getLexeme();

            while (declaredIdent != null) {
                ruleDeclarations.addElement(declaredIdent);
                declaredClassNames.addElement(declaredType);
                Object numberOfOccurrences = quantities.get(declaredType);
                if (numberOfOccurrences == null) {
                    numberOfOccurrences = new Integer(1);
                } else {
                    int aux = ((Integer) numberOfOccurrences).intValue();
                    numberOfOccurrences = new Integer(aux + 1);
                }
                quantities.put(declaredType, numberOfOccurrences);
                String convertedDeclaration = declaredType.replace('.', '_') + '_' + numberOfOccurrences;
                convertedRuleDeclarations.addElement(convertedDeclaration);

                if (!variableIdents.contains(convertedDeclaration)) {
                    variables.addElement(declaredType + " " + convertedDeclaration + ";");
                    variableIdents.addElement(convertedDeclaration);
                }

                token = readNextToken();
                skipWhiteSpace(writer, false);

                // Checking whether there is more than one variable
                // in the same declaration.
                checkToken(token, new int[]{SEMICOLON, COMMA}, "\"" +
                        token.getLexeme() + "\" unexpected");

                if (token.getTokenType() == SEMICOLON) {
                    declaredIdent = null;
                    token = readNextToken();
                } else { // It's a COMMA
                    token = readNextToken();
                    while (token.getTokenType() == WHITE_SPACE ||
                            token.getTokenType() == COMMENT) {
                        token = readNextToken();
                    }
                    checkToken(token, IDENT, "identifier expected");
                    // We've found a declared variable.
                    declaredIdent = token.getLexeme();
                }
            }
            skipWhiteSpace(writer, false);
        }

        lastDeclForLocalDecl = new Vector();
        allDeclForLocalDecl = new Vector();

        if (token.getTokenType() == LOCALDECL) {
            // There are local declarations...
            convertRuleLocalDeclarations(writer);
        } else {
            numberOfLocalDeclarations.addElement(new Integer(0));
        }

        // Writes the function that returns the identifiers of the
        // declarations.
        writer.println();
        writer.println("    /**");
        writer.println("     * Identifiers of rule " + ruleName);
        writer.println("     */");
        writer.println("    private String[] identifiers_" + ruleName + " = {");
        for (int i = 0; i < ruleDeclarations.size(); i++) {
            String declared = (String) ruleDeclarations.elementAt(i);
            writer.print("        \"" + declared + "\"");
            if (i != ruleDeclarations.size() - 1) {
                writer.println(",");
            } else {
                writer.println();
            }
        }
        writer.println("    };");
        writer.println();
        writer.println("    /**");
        writer.println("     * Returns the identifiers declared in rule " + ruleName);
        writer.println("     *");
        writer.println("     * @return the identifiers declared in rule " + ruleName);
        writer.println("     */");
        writer.println("    private String[] getDeclaredIdentifiers_" + ruleName + "() {");
        writer.println("         return identifiers_" + ruleName + ";");
        writer.println("    }");

        numberOfDeclarations.addElement(new Integer(ruleDeclarations.size()));

        // Writes a function that returns the class names of the declared objects
        // in this rule.
        writer.println();
        writer.println("    /**");
        writer.println("     * Returns the name of the class of one declared object for");
        writer.println("     * rule " + ruleName + ".");
        writer.println("     *");
        writer.println("     * @param index the index of the declaration");
        writer.println("     * @return the name of the class of the declared objects for");
        writer.println("     *          this rule.");
        writer.println("     */");
        writer.println("    private String getDeclaredClassName_" + ruleName + "(int index) {");
        writer.println("        switch (index) {");
        for (int i = 0; i < declaredClassNames.size(); i++) {
            writer.println("            case " + i + ": return \"" +
                    declaredClassNames.elementAt(i) + "\";");
        }
        writer.println("            default: return null;");
        writer.println("        }");
        writer.println("    }");

        // Writes a function that returns the class of the declared objects
        // in this rule.
        writer.println();
        writer.println("    /**");
        writer.println("     * Returns the class of one declared object for rule " + ruleName + ".");
        writer.println("     *");
        writer.println("     * @param index the index of the declaration");
        writer.println("     * @return the class of the declared objects for this rule.");
        writer.println("     */");
        writer.println("    private Class getDeclaredClass_" + ruleName + "(int index) {");
        writer.println("        switch (index) {");
        for (int i = 0; i < declaredClassNames.size(); i++) {
            writer.println("            case " + i + ": return " +
                    declaredClassNames.elementAt(i) + ".class;");
        }
        writer.println("            default: return null;");
        writer.println("        }");
        writer.println("    }");

        // Writes a method that defines the value of an object in this rule.
        writer.println();
        writer.println("    /**");
        writer.println("     * Sets an object declared in the rule " + ruleName + ".");
        writer.println("     *");
        writer.println("     * @param index the index of the declared object");
        writer.println("     * @param value the value of the object being set.");
        writer.println("     */");
        writer.println("    private void setObject_" + ruleName + "(int index, Object value) {");
        writer.println("        switch (index) {");
        for (int i = 0; i < ruleDeclarations.size(); i++) {
            String declaration = (String) convertedRuleDeclarations.elementAt(i);
            writer.println("            case " + i + ": this." + declaration + " = (" +
                    declaredClassNames.elementAt(i) + ") value; break;");
        }
        writer.println("        }");
        writer.println("    }");

        // Writes a method that returns the value of an object in this rule.
        writer.println();
        writer.println("    /**");
        writer.println("     * Returns an object declared in the rule " + ruleName + ".");
        writer.println("     *");
        writer.println("     * @param index the index of the declared object");
        writer.println("     * @return the value of the corresponding object.");
        writer.println("     */");
        writer.println("    private Object getObject_" + ruleName + "(int index) {");
        writer.println("        switch (index) {");
        for (int i = 0; i < convertedRuleDeclarations.size(); i++) {
            String declaration = (String) convertedRuleDeclarations.elementAt(i);
            writer.println("            case " + i + ": return " + declaration + ";");
        }
        writer.println("            default: return null;");
        writer.println("        }");
        writer.println("    }");

        // Method that returns all the declared objects of this rule.
        writer.println();
        writer.println("    /**");
        writer.println("     * Returns all variables bound to the declarations ");
        writer.println("     * of rule " + ruleName);
        writer.println("     *");
        writer.println("     * @return an object array of the variables bound to the");
        writer.println("     *          declarations of this rule.");
        writer.println("     */");
        writer.println("    private Object[] getObjects_" + ruleName + "() {");
        writer.println("        return new Object[] {");
        for (int i = 0; i < convertedRuleDeclarations.size(); i++) {
            String decl = (String) convertedRuleDeclarations.elementAt(i);
            writer.print("                            " + decl);
            if (i != convertedRuleDeclarations.size() - 1) {
                writer.println(",");
            } else {
                writer.println();
            }
        }
        writer.println("                            };");
        writer.println("    }");

        // Method that sets all the declared objects of this rule.
        writer.println();
        writer.println("    /**");
        writer.println("     * Defines all variables bound to the declarations ");
        writer.println("     * of rule " + ruleName);
        writer.println("     *");
        writer.println("     * @param objects an object array of the variables bound to the");
        writer.println("     *          declarations of this rule.");
        writer.println("     */");
        writer.println("    private void setObjects_" + ruleName + "(Object[] objects) {");
        for (int i = 0; i < convertedRuleDeclarations.size(); i++) {
            String decl = (String) convertedRuleDeclarations.elementAt(i);
            String className = (String) declaredClassNames.elementAt(i);
            writer.println("        " + decl + " = (" + className + ") objects[" + i + "];");
        }
        writer.println("    }");

    }

    /**
     * Processes the local declarations part of a rule. This method
     * continues filling the <code>ruleLocalDeclaration</code> and
     * <code>convertedRuleLocalDeclaration</code> fields with the
     * locally declared variables and their counterpart in the
     * generated java file.<p>
     * In order to be correctly invoked, the current token
     * type must be <code>LOCALDECL</code>. After successful
     * completion, the token type is <code>CONDITIONS</code>.
     *
     * @param writer the print writer used to write to the generated file.
     * @throws IOException    if some IO error occurs.
     * @throws JeopsException if some error occurs while converting the rule.
     * @precondition token.getTokenType() == LOCALDECL
     * @postcondition token.getTokenType() == CONDITIONS
     */
    private void convertRuleLocalDeclarations(PrintWriter writer)
            throws IOException, JeopsException {
        token = readNextToken();
        skipWhiteSpace(writer, false);
        int localDeclNumber = 0;

//		Vector locallyDeclaredClasses = (Vector) localDeclClasses.elementAt(localDeclClasses.size() - 1);

        while (token.getTokenType() != CONDITIONS) {
            checkToken(token, IDENT, "identifier expected");
            String declaredType = token.getLexeme();
            token = readNextToken();
            while (token.getTokenType() == DOT) {
                declaredType = declaredType.concat(token.getLexeme());
                token = readNextToken();
                checkToken(token, IDENT, "identifier expected");
                declaredType = declaredType.concat(token.getLexeme());
                token = readNextToken();
            }
            skipWhiteSpace(writer, false);
            checkToken(token, IDENT, "identifier expected");

            try {
                String[] primitiveTypes = new String[]
                        {"boolean", "byte", "short", "char",
                                "int", "long", "float", "double"};
                boolean isPrimitive = false;
                for (int i = 0; !isPrimitive && i < primitiveTypes.length; i++) {
                    if (declaredType.equals(primitiveTypes[i])) {
                        isPrimitive = true;
                    }
                }
                if (!isPrimitive) {
                    Class c = importList.getRepresentingClass(declaredType);
                    declaredType = c.getName();
                }
            } catch (ClassNotFoundException e) {
                throw new JeopsException("ClassNotFound: " + e.getMessage(),
                        scanner.getCurrentLine(),
                        scanner.getCurrentColumn());
            } catch (ImportException e) {
                throw new JeopsException(e.getMessage(),
                        scanner.getCurrentLine(),
                        scanner.getCurrentColumn());
            }

            // We've found a locally declared variable.
            String declaredIdent = token.getLexeme();

            token = readNextToken();
            skipWhiteSpace(writer, false);
            checkToken(token, EQUALS, "'=' expected");
            token = readNextToken();
            skipWhiteSpace(writer, false);

            Vector allDecls = new Vector(); // Stores all declarations used by
            // this locally declared variable.]

            StringBuffer convertedExpression = new StringBuffer();
            StringBuffer originalExpression = new StringBuffer();

            originalExpression.append(declaredType);
            originalExpression.append(' ');
            originalExpression.append(declaredIdent);
            originalExpression.append(" = ");

            int lastDecl = 0;
            while (token.getTokenType() != SEMICOLON) {

                String lex = token.getLexeme();
                originalExpression.append(lex);

                String converted = lex;
                if (token.getTokenType() == IDENT &&
                        scanner.getLastNonWhiteSpaceToken().getTokenType() != DOT) {
                    int indDecl = ruleDeclarations.indexOf(lex);
                    if (indDecl == -1) {
                        int indLocalDecl = ruleLocalDeclarations.indexOf(lex);
                        if (indLocalDecl != -1) {  // it's a local declaration
                            converted = (String) convertedRuleLocalDeclarations.elementAt(indDecl);
                            Vector aux = (Vector) allDeclForLocalDecl.elementAt(indLocalDecl);
                            for (int i = 0; i < aux.size(); i++) {
                                Integer declIndex = (Integer) aux.elementAt(i);
                                if (!allDecls.contains(declIndex)) {
                                    allDecls.addElement(declIndex);
                                }
                            }
                            indDecl = ((Integer) lastDeclForLocalDecl.elementAt(indLocalDecl)).intValue();
                        }
                    } else { // it's a (normal) declaration
                        Integer declIndex = new Integer(indDecl);
                        converted = (String) convertedRuleDeclarations.elementAt(indDecl);
                        if (!allDecls.contains(declIndex)) {
                            allDecls.addElement(declIndex);
                        }
                    }
                    if (indDecl > lastDecl) {
                        lastDecl = indDecl;
                    }
                }
                convertedExpression.append(converted);
                token = readNextToken();
                if (token.getTokenType() == EOF) {
                    String message = "EOF unexpected at line " +
                            scanner.getCurrentLine() + ", column " +
                            scanner.getCurrentColumn();
                    throw new EOFException(message);
                }
            }
            ruleLocalDeclarations.addElement(declaredIdent);
            lastDeclForLocalDecl.addElement(new Integer(lastDecl));
            allDeclForLocalDecl.addElement(allDecls);

            token = readNextToken();
            skipWhiteSpace(writer, false);

            convertedExpression.insert(0, '(');
            convertedExpression.append(')');

            String convertedDeclaration = convertedExpression.toString();

            convertedRuleLocalDeclarations.addElement(convertedDeclaration);

//			locallyDeclaredClasses.addElement(declaredType);

            localDeclNumber++;
        }

        numberOfLocalDeclarations.addElement(new Integer(ruleLocalDeclarations.size()));
    }

    /**
     * Test method for this class.
     *
     * @param args command-line arguments. None is needed, but
     *             one can pass the rule file name for the converting.
     */
    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                System.out.println("JEOPS v2.1 beta 1");
                System.out.println("Usage: java org.jscience.computing.ai.expertsystem.compiler.Main <file.rules>");
                return;
            }
            String fileName = args[0];
            if (fileName.lastIndexOf(".rules") == -1) {
                System.out.println("Error: the file must have a \".rules\" extension.");
                return;
            }
            System.out.println("JEOPS v2.1 beta 1");
            System.out.println("Creating java file from " + fileName);
            Main j = new Main(fileName);
            j.convert();
            System.out.println("Done. File " + j.javaFileName + " created.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (JeopsException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Processes the end of the rule base, adding control information.
     *
     * @param writer the print writer used to write to the generated file.
     * @throws IOException if some IO error occurs.
     */
    private void processEndOfRuleBase(PrintWriter writer) throws IOException {

        // Method used to return the names of the rules
        writer.println();
        writer.println("    /**");
        writer.println("     * The names of the rules in this class file");
        writer.println("     */");
        writer.println("    private static final String[] File_ruleNames = {");
        for (int i = 0; i < ruleNames.size(); i++) {
            String name = (String) ruleNames.elementAt(i);
            writer.print("        \"" + name + "\"");
            if (i != ruleNames.size() - 1) {
                writer.println(",");
            } else {
                writer.println();
            }
        }
        writer.println("    };");
        writer.println();
        writer.println("    /**");
        writer.println("     * Returns the name of the rules in this class file.");
        writer.println("     *");
        writer.println("     * @return the name of the rules in this class file.");
        writer.println("     */");
        writer.println("    public String[] getRuleNames() {");
        writer.println("        return File_ruleNames;");
        writer.println("    }");

        // Method used to return the number of declarations of the rules
        writer.println();
        writer.println("    /**");
        writer.println("     * The number of declarations of the rules in this class file.");
        writer.println("     */");
        writer.println("    private static final int[] File_numberOfDeclarations = {");
        for (int i = 0; i < numberOfDeclarations.size(); i++) {
            int number = ((Integer) numberOfDeclarations.elementAt(i)).intValue();
            writer.print("        " + number);
            if (i != numberOfDeclarations.size() - 1) {
                writer.println(",");
            } else {
                writer.println();
            }
        }
        writer.println("    };");
        writer.println();
        writer.println("    /**");
        writer.println("     * Returns the number of declarations of the rules in this class file.");
        writer.println("     *");
        writer.println("     * @return the number of declarations  of the rules in this class file.");
        writer.println("     */");
        writer.println("    public int[] getNumberOfDeclarations() {");
        writer.println("        return File_numberOfDeclarations;");
        writer.println("    }");

        // Method used to return the number of conditions of the rules
        writer.println();
        writer.println("    /**");
        writer.println("     * The number of conditions of the rules in this class file.");
        writer.println("     */");
        writer.println("    private static final int[] File_numberOfConditions = {");
        for (int i = 0; i < numberOfConditions.size(); i++) {
            int number = ((Integer) numberOfConditions.elementAt(i)).intValue();
            writer.print("        " + number);
            if (i != numberOfConditions.size() - 1) {
                writer.println(",");
            } else {
                writer.println();
            }
        }
        writer.println("    };");
        writer.println();
        writer.println("    /**");
        writer.println("     * Returns the number of conditions of the rules in this class file.");
        writer.println("     *");
        writer.println("     * @return the number of conditions  of the rules in this class file.");
        writer.println("     */");
        writer.println("    public int[] getNumberOfConditions() {");
        writer.println("        return File_numberOfConditions;");
        writer.println("    }");

        // Method used as an entry point for the verification of the rules' conditions
        writer.println();
        writer.println("    /**");
        writer.println("     * Checks whether a condition of some rule is satisfied.");
        writer.println("     *");
        writer.println("     * @param ruleIndex the index of the rule to be checked");
        writer.println("     * @param condIndex the index of the condition to be checked");
        writer.println("     * @return <code>true</code> if the corresponding condition for the");
        writer.println("     *          given rule is satisfied. <code>false</code> otherwise.");
        writer.println("     */");
        writer.println("    public boolean checkCondition(int ruleIndex, int condIndex) {");
        writer.println("        switch (ruleIndex) {");
        for (int i = 0; i < ruleNames.size(); i++) {
            String name = (String) ruleNames.elementAt(i);
            writer.println("            case " + i + ": return " + name + "_cond(condIndex);");
        }
        writer.println("            default: return false;");
        writer.println("        }");
        writer.println("    }");

        // Method used as an entry point for the checking of conditions
        // that use only one of the declared objects.
        writer.println();
        writer.println("    /**");
        writer.println("     * Checks whether all conditions of some rule that depend only on");
        writer.println("     * the given object are satisfied.");
        writer.println("     *");
        writer.println("     * @param ruleIndex the index of the rule to be checked");
        writer.println("     * @param declIndex the index of the declaration to be checked");
        writer.println("     * @return <code>true</code> if all corresponding conditions for");
        writer.println("     *          the given rule are satisfied;");
        writer.println("     *           <code>false</code> otherwise.");
        writer.println("     */");
        writer.println("    public boolean checkConditionsOnlyOf(int ruleIndex, int declIndex) {");
        writer.println("        switch (ruleIndex) {");
        for (int i = 0; i < ruleNames.size(); i++) {
            String name = (String) ruleNames.elementAt(i);
            writer.println("            case " + i + ": return checkConditionsOnlyOf_" + name + "(declIndex);");
        }
        writer.println("            default: return false;");
        writer.println("        }");
        writer.println("    }");

        // Method used as an entry point for the verification of the rules'
        // conditions based on the declared identifiers.
        writer.println();
        writer.println("    /**");
        writer.println("     * Checks whether all the conditions of a rule which");
        writer.println("     * reference only the elements declared up to the given index");
        writer.println("     * are true.");
        writer.println("     *");
        writer.println("     * @param ruleIndex the index of the rule to be checked");
        writer.println("     * @param declIndex the index of the declaration to be checked");
        writer.println("     * @return <code>true</code> if all the conditions of a rule which");
        writer.println("     *          reference only the elements declared up to the given index");
        writer.println("     *          are satisfied; <code>false</code> otherwise.");
        writer.println("     */");
        writer.println("    public boolean checkCondForDeclaration(int ruleIndex, int declIndex) {");
        writer.println("        switch (ruleIndex) {");
        for (int i = 0; i < ruleNames.size(); i++) {
            String name = (String) ruleNames.elementAt(i);
            writer.println("            case " + i + ": return checkCondForDeclaration_" + name + "(declIndex);");
        }
        writer.println("            default: return false;");
        writer.println("        }");
        writer.println("    }");

        // Method used to return the class name of an object declared in a rule.
        writer.println();
        writer.println("    /**");
        writer.println("     * Returns the class name of an object declared in a rule.");
        writer.println("     *");
        writer.println("     * @param ruleIndex the index of the rule");
        writer.println("     * @param declIndex the index of the declaration");
        writer.println("     * @return the class name of the declared object.");
        writer.println("     */");
        writer.println("    public String getDeclaredClassName(int ruleIndex, int declIndex) {");
        writer.println("        switch (ruleIndex) {");
        for (int i = 0; i < ruleNames.size(); i++) {
            String name = (String) ruleNames.elementAt(i);
            writer.println("            case " + i + ": return getDeclaredClassName_" + name + "(declIndex);");
        }
        writer.println("            default: return null;");
        writer.println("        }");
        writer.println("    }");

        // Method used to return the class of an object declared in a rule.
        writer.println();
        writer.println("    /**");
        writer.println("     * Returns the class of an object declared in a rule.");
        writer.println("     *");
        writer.println("     * @param ruleIndex the index of the rule");
        writer.println("     * @param declIndex the index of the declaration");
        writer.println("     * @return the class of the declared object.");
        writer.println("     */");
        writer.println("    public Class getDeclaredClass(int ruleIndex, int declIndex) {");
        writer.println("        switch (ruleIndex) {");
        for (int i = 0; i < ruleNames.size(); i++) {
            String name = (String) ruleNames.elementAt(i);
            writer.println("            case " + i + ": return getDeclaredClass_" + name + "(declIndex);");
        }
        writer.println("            default: return null;");
        writer.println("        }");
        writer.println("    }");

        // Method used as an entry point for firing the rules
        writer.println();
        writer.println("    /**");
        writer.println("     * Fires one of the rules in this rule base.");
        writer.println("     *");
        writer.println("     * @param ruleIndex the index of the rule to be fired");
        writer.println("     */");
        writer.println("    protected void internalFireRule(int ruleIndex) {");
        writer.println("        switch (ruleIndex) {");
        for (int i = 0; i < ruleNames.size(); i++) {
            String name = (String) ruleNames.elementAt(i);
            writer.println("            case " + i + ": " + name + "(); break;");
        }
        writer.println("        }");
        writer.println("    }");

        // Auxiliar method that returns the number of rules
        writer.println();
        writer.println("    /**");
        writer.println("     * Returns the number of rules.");
        writer.println("     *");
        writer.println("     * @return the number of rules.");
        writer.println("     */");
        writer.println("    public int getNumberOfRules() {");
        writer.println("        return " + ruleNames.size() + ";");
        writer.println("    }");

        // Method used to return the identifiers of the declarations of a rule.
        writer.println();
        writer.println("    /**");
        writer.println("     * Returns the identifiers declared in a given rule.");
        writer.println("     *");
        writer.println("     * @param ruleIndex the index of the rule.");
        writer.println("     * @return an array with the identifiers of the rule declarations.");
        writer.println("     */");
        writer.println("    public String[] getDeclaredIdentifiers(int ruleIndex) {");
        writer.println("        switch (ruleIndex) {");
        for (int i = 0; i < ruleNames.size(); i++) {
            String name = (String) ruleNames.elementAt(i);
            writer.println("            case " + i + ": return getDeclaredIdentifiers_" + name + "();");
        }
        writer.println("            default: return new String[0];");
        writer.println("        }");
        writer.println("    }");

        // Method used to set the value of the declared objects.
        writer.println();
        writer.println("    /**");
        writer.println("     * Sets an object that represents a declaration of some rule.");
        writer.println("     *");
        writer.println("     * @param ruleIndex the index of the rule");
        writer.println("     * @param declIndex the index of the declaration in the rule.");
        writer.println("     * @param value the value of the object being set.");
        writer.println("     */");
        writer.println("    public void setObject(int ruleIndex, int declIndex, Object value) {");
        writer.println("        switch (ruleIndex) {");
        for (int i = 0; i < ruleNames.size(); i++) {
            String name = (String) ruleNames.elementAt(i);
            writer.println("            case " + i + ": setObject_" + name + "(declIndex, value); break;");
        }
        writer.println("        }");
        writer.println("    }");

        // Method used to get the value of some declared object.
        writer.println();
        writer.println("    /**");
        writer.println("     * Returns an object that represents a declaration of some rule.");
        writer.println("     *");
        writer.println("     * @param ruleIndex the index of the rule");
        writer.println("     * @param declIndex the index of the declaration in the rule.");
        writer.println("     * @return the value of the corresponding object.");
        writer.println("     */");
        writer.println("    public Object getObject(int ruleIndex, int declIndex) {");
        writer.println("        switch (ruleIndex) {");
        for (int i = 0; i < ruleNames.size(); i++) {
            String name = (String) ruleNames.elementAt(i);
            writer.println("            case " + i + ": return getObject_" + name + "(declIndex);");
        }
        writer.println("            default: return null;");
        writer.println("        }");
        writer.println("    }");

        // Method used by the knwoledge base to get the value all
        // declared objects for some rule.
        writer.println();
        writer.println("    /**");
        writer.println("     * Returns all variables bound to the declarations of");
        writer.println("     * some rule.");
        writer.println("     *");
        writer.println("     * @param ruleIndex the index of the rule");
        writer.println("     * @return an object array of the variables bound to the");
        writer.println("     *          declarations of some rule.");
        writer.println("     */");
        writer.println("    public Object[] getObjects(int ruleIndex) {");
        writer.println("        switch (ruleIndex) {");
        for (int i = 0; i < ruleNames.size(); i++) {
            String name = (String) ruleNames.elementAt(i);
            writer.println("            case " + i + ": return getObjects_" + name + "();");
        }
        writer.println("            default: return null;");
        writer.println("        }");
        writer.println("    }");

        // Method used by the knwoledge base to set the value all
        // declared objects for some rule.
        writer.println("    /**");
        writer.println("     * Defines all variables bound to the declarations of");
        writer.println("     * some rule.");
        writer.println("     *");
        writer.println("     * @param ruleIndex the index of the rule");
        writer.println("     * @param objects an object array of the variables bound to the");
        writer.println("     *          declarations of some rule.");
        writer.println("     */");
        writer.println("    public void setObjects(int ruleIndex, Object[] objects) {");
        writer.println("        switch (ruleIndex) {");
        for (int i = 0; i < ruleNames.size(); i++) {
            String name = (String) ruleNames.elementAt(i);
            writer.println("            case " + i + ": setObjects_" + name + "(objects); break;");
        }
        writer.println("        }");
        writer.println("    }");

        // Adds the declared variables.
        writer.println();
        writer.println("    /*");
        writer.println("     * The variables declared in the rules.");
        writer.println("     */");
        for (int i = 0; i < variables.size(); i++) {
            writer.print("    private ");
            writer.println((String) variables.elementAt(i));
        }

        // Adds the constructor.
        writer.println();
        writer.println("    /**");
        writer.println("     * Class constructor.");
        writer.println("     *");
        writer.println("     * @param knowledgeBase the knowledge base that contains this rule base.");
        writer.println("     */");
        writer.println("    public Jeops_RuleBase_" + ruleBaseName + "(org.jscience.computing.ai.expertsystem.AbstractKnowledgeBase knowledgeBase) {");
        writer.println("        super(knowledgeBase);");
        writer.println("    }");

        // A last empty line...
        writer.println();
    }

    /**
     * Processes an import statement of a rule base.
     *
     * @param writer the print writer used to write to the generated file.
     * @throws IOException    if some IO error occurs.
     * @throws JeopsException if some error occurs while converting the rule.
     */
    private void processImport(PrintWriter writer)
            throws IOException, JeopsException {
        StringBuffer statement = new StringBuffer();
        writer.print(token.getLexeme());
        token = readNextToken();
        skipWhiteSpace(writer, true);
        while (token.getTokenType() == IDENT ||
                token.getTokenType() == DOT ||
                token.getTokenType() == ASTERISK) {
            writer.print(token.getLexeme());
            statement.append(token.getLexeme());
            token = readNextToken();
        }
        skipWhiteSpace(writer, true);
        checkToken(token, SEMICOLON, "\";\" expected");
        importList.addImport(statement.toString());
        writer.print(token.getLexeme());
    }

    /**
     * Processes the package statement of a rule base.
     *
     * @param writer the print writer used to write to the generated file.
     * @throws IOException    if some IO error occurs.
     * @throws JeopsException if some error occurs while converting the rule.
     */
    private void processPackage(PrintWriter writer)
            throws IOException, JeopsException {
        StringBuffer packageName = new StringBuffer();
        writer.print(token.getLexeme());
        token = readNextToken();
        skipWhiteSpace(writer, true);
        while (token.getTokenType() == IDENT ||
                token.getTokenType() == DOT) {
            writer.print(token.getLexeme());
            packageName.append(token.getLexeme());
            token = readNextToken();
        }
        skipWhiteSpace(writer, true);
        checkToken(token, SEMICOLON, "\";\" expected");
        importList = new ImportList(packageName.toString());
        writer.print(token.getLexeme());
    }

    /**
     * Reads the next token from the scanner, throwing an exception in
     * case of an error.
     *
     * @return the token read from the scanner.
     * @throws IOException    if some IO error occurs.
     * @throws JeopsException is the token read is an error.
     */
    private Token readNextToken() throws IOException, JeopsException {
        Token result = scanner.nextToken();
        if (result.getTokenType() == ERROR) {
            throw new JeopsException(token.getLexeme(),
                    scanner.getCurrentLine(),
                    scanner.getCurrentColumn());
        }
        return result;
    }

    /**
     * Auxiliar method used to advance through white space and comments.
     * The caller must indicate whether the comments should be written to
     * the output file. This method throws an error if EOF is found.
     *
     * @param writer         the print writer used to write to the generated file.
     * @param outputComments if the white space and comments should be
     *                       written to the output file
     * @throws IOException if some IO error occurs.
     */
    private void skipWhiteSpace(PrintWriter writer, boolean outputComments)
            throws IOException {
        while (token.getTokenType() == WHITE_SPACE ||
                token.getTokenType() == COMMENT) {
            if (outputComments) {
                writer.print(token.getLexeme());
            }
            token = scanner.nextToken();
        }
        if (token.getTokenType() == EOF) {
            String message = "EOF unexpected at line " +
                    scanner.getCurrentLine() + ", column " +
                    scanner.getCurrentColumn();
            throw new EOFException(message);
        }
    }
}
