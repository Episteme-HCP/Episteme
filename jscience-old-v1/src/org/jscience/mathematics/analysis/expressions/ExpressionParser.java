package org.jscience.mathematics.analysis.expressions;

import org.jscience.mathematics.analysis.expressions.comparison.*;
import org.jscience.mathematics.analysis.expressions.logical.*;
import org.jscience.mathematics.analysis.expressions.symbolic.*;
import org.jscience.mathematics.analysis.taylor.*;

import java.io.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Expression parser for mathematical expressions. The parser has some
 * predefined constants: pi and e that are available if source code is parsed.
 * If only single expressions are parsed pi and e are not available. Known
 * bugs (may not be fixed anytime soon): Using an incorrect number of
 * arguments for functions may lead to incorrect expressions, such as
 * <CODE>sin(x,y)+</CODE> which results in code generation of the expression
 * <CODE>x+sin(y)</CODE>. Similarly, it is possible to use stack oriented
 * syntax (reverse polish notation syntax) like in the example
 * <CODE>x,y,sin()+</CODE> which also generates the code
 * <CODE>x+sin(y)</CODE>. Hint: Do not use functions with associated bugs
 * until bug comments have been removed. <BR><B>Examples of use:</B><BR>
 * <PRE>
 * ExpressionParser parser = new ExpressionParser();
 * Expression cnst = parser.parse( "1+sin(3/2.0)" );
 * if ( cnst != null ) {
 * double doubleValue = cnst.eval();
 * System.out.println( "value=" + doubleValue );
 * }
 * </PRE>
 * which generates the output <CODE>value=1.9974949866040543</CODE>.<BR> A
 * slightly more complicated example is the following which include a single
 * independent variable <CODE>x</CODE>.
 * <PRE>
 * ExpressionParser parser = new ExpressionParser();
 * java.util.List variableNames = new java.util.ArrayList();
 * java.util.List variables = new java.util.ArrayList();
 * variableNames.add( "x" );
 * Variable x = new Variable( "x", 2.3 );
 * variables.add( x );
 * parser.setVariableNames( variableNames );
 * parser.setVariables( variables );
 * Expression expr = parser.parse( "x+sin(3*x)" );
 * Expression derivative = expr.diff( x );
 * Expression optderivative = derivative.optimize();
 * System.out.println( "expr=" + expr );
 * System.out.println( "derivative=" + derivative );
 * System.out.println( "optimized derivative=" + optderivative );
 * </PRE>
 * which generates the following output.
 * <PRE>
 * expr=(x+sin((3.0*x)))
 * derivative=(1.0+(cos((3.0*x))*((0.0*x)+(3.0*1.0))))
 * optderivative=(1.0+(cos((3.0*x))*3.0))
 * </PRE>
 * An even more complicated example which includes both variables and
 * parameters is seen below.
 * <PRE>
 * import org.jscience.mathematics.analysis.*;
 * import java.util.Vector;
 * public class TestExp {
 * public static void main( String[] args ) {
 * String[] vars = new String[] { "x", "y" };
 * Vector variableNames  = new Vector();
 * Vector variables = new Vector();
 * for (int i = 0; i < vars.length; i++) {
 * variableNames.add( vars[ i ] );
 * variables.add( new Variable( vars[ i ], 0 ) );
 * }
 * String[] pars = new String[] { "a", "b" };
 * Vector parameterNames = new Vector();
 * Vector parameters = new Vector();
 * for (int i = 0; i < pars.length; i++) {
 * parameterNames.add( pars[ i ] );
 * parameters.add( new Parameter( pars[ i ], 0 ) );
 * }
 * Vector constantNames  = new Vector();
 * Vector auxiliaryNames = new Vector();
 * ExpressionParser parser = new ExpressionParser();
 * parser.setVariableNames(  variableNames );
 * parser.setVariables( variables );
 * parser.setParameterNames( parameterNames );
 * parser.setParameters( parameters  );
 * parser.setConstantNames(  constantNames );
 * parser.setAuxiliaryNames( auxiliaryNames );
 * Expression expr = (Expression)parser.parse( "a*x-x*a+b*sin(4*y*1+0+b/1)" );
 * System.out.println( "Expression = " + expr );
 * expr = expr.optimize();
 * System.out.println( "Expression = " + expr );
 * }
 * }
 * <p/>
 * </PRE>
 * The above code generates something like the following output:<BR>
 * <PRE>
 * Expression = (((a*x)-(x*a))+(b*sin(((((4.0*y)*1.0)+0.0)+(b/1.0)))))
 * Expression = (b*sin(((4.0*y)+b)))
 * </PRE>
 *
 * @author Carsten Knudsen
 * @version 1.0
 */

//this class is an expression parser from Janet merged in with the minimal possible modifications
//it defines keywords originally used in Janet but not anymore here
//you should be very cautious when using this file
public class ExpressionParser implements Serializable {
    /**
     * DOCUMENT ME!
     */
    private static final String namePattern = "\\p{Alpha}\\w*";

    /**
     * DOCUMENT ME!
     */
    private static final String[] reservedNames = {

            // reserved (key)words
            "variable", "parameter", "auxiliary", "function", "process",
            "constant", "input", "output", "param", "type",
            ExpressionsStandardNames.TIME_CONTINUOUS_NAME,
            ExpressionsStandardNames.DISCRETE_NAME,

            // predefined constants
            "pi", "e",
            // words that just shouldn't be used because it isn't proper
            "sin", "asin", "cos", "acos", "tan", "atan", "atan2", "log", "exp",
            "pow", "add", "sub", "mul", "div", "abs", "step", "clip", "mod",
            "max", "min", "diff", "rand", "gauss", "sign",
            // reserved Java keywords
            "abstract", "boolean", "break", "byte", "case", "catch", "char",
            "class", "const", "continue", "default", "do", "double", "else",
            "extends", "false", "final", "finally", "float", "for", "goto", "if",
            "implements", "import", "instanceof", "int", "interface", "long",
            "native", "new", "null", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super", "switch",
            "synchronized", "this", "throw", "throws", "transient", "true",
            "try", "void", "volatile", "while",
            // reserved org.jscience.mathematics.analysis keywords (not yet a complete list)
            "Adouble", "TaylorDouble", "Complex", "ComplexVector", "Domain",
            "Vector", "Matrix", "VectorType", "MatrixType", "Map",
            "DifferentiableMap", "ParameterDifferentiableMap", "Autonomous",
            "NonAutonomous", "TimeDiscrete", "TimeContinuous", "Invertible",
            "NonDifferentiable", "StateInitializer", "ParameterInitializer",
            "StateRangeInitializer", "ParameterRangeInitializer",
            "AutonomousDiffusive", "NonAutonomousDiffusive",
            // and as they are written in EquationEditor
            "map", "ode", "sde", "invertible", "differentiable",
            "nondifferentiable", "stateinitializer", "parameterinitializer",
            "staterangeinitializer", "parameterrangeinitializer"
    };

    /**
     * DOCUMENT ME!
     */
    private static final java.util.Vector reservedNamesVector = new java.util.Vector();

    static {
        for (int i = 0; i < reservedNames.length; i++) {
            reservedNamesVector.add(reservedNames[i]);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private static final String[] unaryOperatorNames = {
            "sin", "cos", "tan", "asin", "acos", "atan", "atan2", "exp", "log",
            "pow", "sqrt", "minus", "plus", "abs", "step", "clip", "mod", "max",
            "min", "diff", "rand", "gauss", "sign"
    };

    /**
     * DOCUMENT ME!
     */
    private static final String operatorString = " +-*/^()";

    /**
     * DOCUMENT ME!
     */
    private static String[] binaryOperatorNames = {"+", "-", "*", "/", "^"};

    // symbols accepted as denoting derivative

    /**
     * DOCUMENT ME!
     */
    private static String primes = "'�`";

    // symbols accepted as denoting inverse

    /**
     * DOCUMENT ME!
     */
    private static String inverses = "~*#";

    /**
     * DOCUMENT ME!
     */
    private static final java.util.List functionNames = new java.util.ArrayList();

    /**
     * DOCUMENT ME!
     */
    private static final String CR = System.getProperty("line.separator");

    /**
     * DOCUMENT ME!
     */
    Pattern typeDeclarationPattern = Pattern.compile(
            "\\s*type\\s+(\\s*(ode|map|sde|stateinitializer|staterangeinitializer|parameterinitializer|parameterrangeinitializer|nondifferentiable|invertible|event))(\\s*,\\s*(ode|map|sde|stateinitializer|staterangeinitializer|parameterinitializer|parameterrangeinitializer|nondifferentiable|invertible|event))*");

    /**
     * DOCUMENT ME!
     */
    Pattern variableDeclarationPattern = Pattern.compile("\\s*variable\\s+(" +
            namePattern + ")(\\s*,\\s*" + namePattern + "\\s*)*");

    /**
     * DOCUMENT ME!
     */
    Pattern processDeclarationPattern = Pattern.compile("\\s*process\\s+(" +
            namePattern + ")(\\s*,\\s*" + namePattern + "\\s*)*");

    /**
     * DOCUMENT ME!
     */
    Pattern parameterDeclarationPattern = Pattern.compile("\\s*parameter\\s+(" +
            namePattern + ")(\\s*,\\s*" + namePattern + "\\s*)*");

    /**
     * DOCUMENT ME!
     */
    Pattern auxiliaryDeclarationPattern = Pattern.compile("\\s*auxiliary\\s+(" +
            namePattern + ")(\\s*,\\s*" + namePattern + "\\s*)*");

    /**
     * DOCUMENT ME!
     */
    Pattern functionDeclarationPattern = Pattern.compile("\\s*function\\s+(" +
            namePattern + ")\\s*(\\s*,\\s*" + namePattern + "\\s*)*");

    /**
     * DOCUMENT ME!
     */
    Pattern eventDeclarationPattern = Pattern.compile("\\s*event\\s+(" +
            namePattern + ")(\\s*,\\s*" + namePattern + "\\s*)*");

    /**
     * DOCUMENT ME!
     */
    Pattern variableDeclarationAndAssignmentPattern = Pattern.compile(
            "\\s*variable\\s+(" + namePattern +
                    ")\\s*\\=\\s*(\\S+(.*\\S+)*)\\s*");

    /**
     * DOCUMENT ME!
     */
    Pattern parameterDeclarationAndAssignmentPattern = Pattern.compile(
            "\\s*parameter\\s+(" + namePattern +
                    ")\\s*\\=\\s*(\\S+(.*\\S+)*)\\s*");

    /**
     * DOCUMENT ME!
     */
    Pattern functionDeclarationAndDefinitionPattern = Pattern.compile(
            "\\s*function\\s+(" + namePattern + ")\\((\\s*(" + namePattern +
                    ")(\\s*,\\s*(" + namePattern +
                    "))*)\\)\\s*\\=\\s*(\\S+(.*\\S+)*)\\s*");

    /**
     * DOCUMENT ME!
     */
    Pattern functionDefinitionPattern = Pattern.compile("\\s*(" + namePattern +
            ")\\((\\s*(" + namePattern + ")(\\s*,\\s*(" + namePattern +
            "))*)\\)\\s*\\=.*");

    /**
     * DOCUMENT ME!
     */
    Pattern assignmentPattern = Pattern.compile("\\s*(" + namePattern +
            ")\\s*\\=\\s*(\\S+(.*\\S+)*)\\s*");

    /**
     * DOCUMENT ME!
     */
    Pattern rangeAssignmentPattern = Pattern.compile("\\s*(" + namePattern +
            ")\\s*\\=\\s*(\\S+(.*\\S+)*)\\s*:\\s*(\\S+(.*\\S+)*)\\s*");

    /**
     * DOCUMENT ME!
     */
    Pattern dynamicEquationPattern = Pattern.compile("\\s*(" + namePattern +
            ")\\s*['|`]=\\s*(\\S+(.*\\S+)*)\\s*");

    /**
     * DOCUMENT ME!
     */
    Pattern inverseEquationPattern = Pattern.compile("\\s*(" + namePattern +
            ")\\s*[~|\\*|#]=\\s*(\\S+(.*\\S+)*)\\s*");

    /**
     * We make no assumptions about the type of system being read;
     */
    private boolean isAutonomous = true;

    /**
     * DOCUMENT ME!
     */
    private boolean isTimeDiscrete = false;

    /**
     * DOCUMENT ME!
     */
    private boolean isTimeContinuous = false;

    /**
     * DOCUMENT ME!
     */
    private boolean isInvertible = false;

    /**
     * DOCUMENT ME!
     */
    private boolean isDifferentiable = false;

    /**
     * DOCUMENT ME!
     */
    private boolean isNondifferentiable = false;

    /**
     * DOCUMENT ME!
     */
    private boolean isParameterDifferentiable = false;

    /**
     * DOCUMENT ME!
     */
    private boolean isExpandable = false;

    /**
     * DOCUMENT ME!
     */
    private boolean isPoincareSection = false;

    /**
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     */

    /**
     * DOCUMENT ME!
     */
    private boolean containsTypes;

    /**
     * DOCUMENT ME!
     */
    private boolean containsVariables;

    /**
     * DOCUMENT ME!
     */
    private boolean containsParameters;

    /**
     * DOCUMENT ME!
     */
    private boolean containsConstants;

    /**
     * DOCUMENT ME!
     */
    private boolean containsAuxillaries;

    /**
     * DOCUMENT ME!
     */
    private boolean containsUserFunctions;

    /**
     * DOCUMENT ME!
     */
    private boolean containsLogicals;

    /**
     * DOCUMENT ME!
     */
    private boolean containsStateInit;

    /**
     * DOCUMENT ME!
     */
    private boolean containsStateRange;

    /**
     * DOCUMENT ME!
     */
    private boolean containsParameterInit;

    /**
     * DOCUMENT ME!
     */
    private boolean containsParameterRange;

    /**
     * DOCUMENT ME!
     */
    private boolean containsPeriod;

    /**
     * DOCUMENT ME!
     */
    private boolean containsInitialComment;

    /**
     * DOCUMENT ME!
     */
    private boolean containsEvents;

    /**
     * DOCUMENT ME!
     */
    private boolean containsProcesses;

    /**
     * DOCUMENT ME!
     */
    private boolean typeKeywordMissing = false;

    /**
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     */

    /**
     * DOCUMENT ME!
     */
    private java.util.List initstates = new java.util.ArrayList();

    /**
     * DOCUMENT ME!
     */
    private java.util.List paramstates = new java.util.ArrayList();

    /**
     * DOCUMENT ME!
     */
    private java.util.List initrangemin = new java.util.ArrayList();

    /**
     * DOCUMENT ME!
     */
    private java.util.List initrangemax = new java.util.ArrayList();

    /**
     * DOCUMENT ME!
     */
    private java.util.List paramrangemin = new java.util.ArrayList();

    /**
     * DOCUMENT ME!
     */
    private java.util.List paramrangemax = new java.util.ArrayList();

    /**
     * Contains the names of the variables read from source code.
     */
    private java.util.List variableNames = new java.util.ArrayList();

    /**
     * Contains the <B>Variable</B>s.
     */
    private java.util.List variables = new java.util.ArrayList();

    /**
     * Contains the names of the parameters read from source code.
     */
    private java.util.List parameterNames = new java.util.ArrayList();

    /**
     * Contains the <B>Parameter</B>s.
     */
    private java.util.List parameters = new java.util.ArrayList();

    /**
     * Contains the names of the constants read from source code.
     */
    private java.util.List constantNames = new java.util.ArrayList();

    /**
     * Contains the <B>Constant</B>s that are not literal.
     */
    private java.util.List constants = new java.util.ArrayList();

    /**
     * Contains the names of the auxillaries read from source code.
     */
    private java.util.List auxiliaryNames = new java.util.ArrayList();

    /**
     * Contains the auxillaries.
     */
    private java.util.List auxillaries = new java.util.ArrayList();

    /**
     * Contains the names of the functions read from source code.
     */
    private java.util.List userFunctionNames = new java.util.ArrayList();

    /**
     * Contains the user defined functions.
     */
    private java.util.List userFunctions = new java.util.ArrayList();

    /**
     * Contains the formal parameter names for each user function.
     */
    private java.util.List userFunctionParameters = new java.util.ArrayList();

    /**
     * Contains the names of the logicals read from source code.
     */
    private java.util.List logicalNames = new java.util.ArrayList();

    /**
     * Contains the <B>Logical</B>s.
     */
    private java.util.List logicals = new java.util.ArrayList();

    /**
     * Contains the <B>Expressions</B>s representing the right hand side.
     */
    private java.util.List expressions;

    /**
     * Contains the <B>Expressions</B>s representing the inverse functions
     */
    private java.util.List inverseexpressions;

    /**
     * DOCUMENT ME!
     */
    private java.util.List eventNames;

    /**
     * DOCUMENT ME!
     */
    private java.util.List eventConditions;

    /**
     * DOCUMENT ME!
     */
    private java.util.List eventTargets;

    /**
     * DOCUMENT ME!
     */
    private java.util.List eventValues;

    /**
     * DOCUMENT ME!
     */
    private java.util.List processNames;

    /**
     * The <B>String</B><I>informationString</I> will after compilation contain
     * diagnostic information about the compilation process. This may be
     * retrieved by the method <I>getInformation</I> which may help
     * optimization of code and to located syntax errors in source code.
     */
    private String informationString;

    /**
     * The <B>String</B><I>errorString</I> will after attempted compilation
     * contain any error message resulting in compilation being abandoned.
     * This may be retrieved by the method <I>getError</I> which may help
     * optimization of code and to located syntax errors in source code.
     */
    private String errorString;

    /**
     * The <B>String</B><I>javaSource</I> will after a call to the method
     * <I>generateJavaSource</I> contain the Java source code for the right
     * hand side. The <B>String</B> is retrieved with <I>getjavaSource</I>.
     */
    private String javaSource;

    /**
     * The <B>boolean</B> has the value of <B>false</B> if the source code has
     * not been compiled. After compilation the <B>boolean</B> has the value
     * <B>true</B>.
     */
    private boolean compiled = false;

    /**
     * optimizationLevel indicates how many times the optimize method is called
     * on each expression.  Its value is set by setOptimizationLevel.
     */
    private int optimizationLevel = 3;

    /**
     * RHS will upon compilation contain the (optimized) right hand side.
     */
    private Expression[] RHS;

    /**
     * inverseRHS will upon compilation contain the (optimized) inverse
     * provided that it exists.
     */
    private Expression[] inverseRHS;

    /**
     * JacobiMatrix will upon compilation contain the (optimized) Jacobi
     * matrix.
     */
    private Expression[][] JacobiMatrix;

    /**
     * parameterJacobiMatrix will upon compilation contain the (optimized)
     * parameter Jacobi matrix.
     */
    private Expression[][] parameterJacobiMatrix;

    /**
     * DOCUMENT ME!
     */
    private Expression[][] diffusionMatrix;

    /**
     * Boolean variable isStrict signals whether all Java code generated should
     * use strict floating point computations.
     */
    private boolean isStrict = false;

    /**
     * String accessLevel indicates the kind of access to Java fields declared
     * in generated code.
     */
    private String accessLevel = "private";

    /**
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     */

    /**
     * DOCUMENT ME!
     */
    private String initialComment;

    /**
     * Public constructor for the <B>ExpressionParser</B> class. Initialises
     * the vector operatorVector containing the names of all unary and binary
     * operators allowed.
     */
    public ExpressionParser() {
        for (int i = 0; i < unaryOperatorNames.length; i++) {
            functionNames.add(unaryOperatorNames[i]);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param level DOCUMENT ME!
     */
    public void setOptimizationLevel(int level) {
        optimizationLevel = level;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public void setStrict(boolean s) {
        isStrict = s;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public void setAccessLevel(String s) {
        if (s.equals("public") || s.equals("protected") || s.equals("private")) {
            accessLevel = s;
        }
    }

    /**
     **/
    public String getInformation() {
        return informationString;
    }

    /**
     **/
    public String getError() {
        return errorString;
    }

    /**
     **/
    public String getJavaSource() {
        return javaSource;
    }

    /**
     **/
    public void setJavaSource(String source) {
        javaSource = source;
    }

    /**
     **/
    public boolean isCompiled() {
        return compiled;
    }

    /**
     **/
    public boolean isDifferentiable() {
        return isDifferentiable;
    }

    /**
     **/
    public boolean isNondifferentiable() {
        return isNondifferentiable;
    }

    /**
     **/
    public boolean isParameterDifferentiable() {
        return isParameterDifferentiable;
    }

    /**
     **/
    public boolean isExpandable() {
        return isExpandable;
    }

    /**
     **/
    public boolean isInvertible() {
        return isInvertible;
    }

    /**
     **/
    public int getNumberOfVariables() {
        return variableNames.size();
    }

    /**
     **/
    public int getNumberOfParameters() {
        return parameterNames.size();
    }

    /**
     **/
    public int getNumberOfConstants() {
        return constantNames.size();
    }

    /**
     **/
    public int getNumberOfAuxillaries() {
        return auxiliaryNames.size();
    }

    /**
     **/
    public int getNumberOfEvents() {
        return eventNames.size();
    }

    /**
     **/
    public int getNumberOfProcesses() {
        return processNames.size();
    }

    /**
     **/
    public int getNumberOfUserFunctions() {
        return userFunctionNames.size();
    }

    /**
     **/
    public java.util.List getVariableNames() {
        return variableNames;
    }

    /**
     **/
    public java.util.List getParameterNames() {
        return parameterNames;
    }

    /**
     **/
    public java.util.List getConstantNames() {
        return constantNames;
    }

    /**
     **/
    public java.util.List getAuxiliaryNames() {
        return auxiliaryNames;
    }

    /**
     **/
    public java.util.List getUserFunctionNames() {
        return userFunctionNames;
    }

    /**
     **/
    public java.util.List getLogicalNames() {
        return logicalNames;
    }

    /**
     **/
    public void setVariableNames(java.util.List variableNames) {
        this.variableNames = variableNames;
    }

    /**
     **/
    public void setAuxiliaryNames(java.util.List auxiliaryNames) {
        this.auxiliaryNames = auxiliaryNames;
    }

    /**
     **/
    public void setParameterNames(java.util.List parameterNames) {
        this.parameterNames = parameterNames;
    }

    /**
     **/
    public void setConstantNames(java.util.List constantNames) {
        this.constantNames = constantNames;
    }

    /**
     **/
    public void setLogicalNames(java.util.List logicalNames) {
        this.logicalNames = logicalNames;
    }

    /**
     **/
    public java.util.List getVariables() {
        return variables;
    }

    /**
     **/
    public java.util.List getAuxiliaries() {
        return auxillaries;
    }

    /**
     **/
    public void setAuxiliaries(java.util.List auxs) {
        auxillaries = auxs;
    }

    /**
     **/
    public void setVariables(java.util.List variables) {
        this.variables = variables;
    }

    /**
     **/
    public java.util.List getParameters() {
        return parameters;
    }

    /**
     **/
    public void setParameters(java.util.List parameters) {
        this.parameters = parameters;
    }

    /**
     **/
    public java.util.List getConstants() {
        return constants;
    }

    /**
     **/
    public void setConstants(java.util.List constants) {
        this.constants = constants;
    }

    /**
     **/
    public java.util.List getLogicals() {
        return logicals;
    }

    /**
     **/
    public void setLogicals(java.util.List logicals) {
        this.logicals = logicals;
    }

    /**
     **/
    public Expression[] getRHS() {
        return RHS;
    }

    /**
     **/
    public Expression[] getInverseRHS() {
        return inverseRHS;
    }

    /**
     **/
    public Expression[][] getJacobiMatrix() {
        return JacobiMatrix;
    }

    /**
     **/
    public Expression[][] getParameterJacobiMatrix() {
        return parameterJacobiMatrix;
    }

    /**
     **/
    public boolean isTimeDiscrete() {
        return isTimeDiscrete;
    } // isTimeDiscrete

    /**
     **/
    public boolean isTimeContinuous() {
        return isTimeContinuous;
    } // isTimeContinuous

    /**
     * Returns true if the String argument s is legal keyword.
     *
     * @param s DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    private boolean isLegalKeyword(String s) {
        if (s == null) {
            return false;
        }

        if (isJavaKeyword(s)) {
            return false;
        }

        if (isTypeKeyword(s)) {
            return false;
        }

        if (isVariablesKeyword(s)) {
            return false;
        }

        if (isParameter(s)) {
            return false;
        }

        if (isConstantsKeyword(s)) {
            return false;
        }

        if (isVariablesKeyword(s)) {
            return false;
        }

        if (isAuxillariesKeyword(s)) {
            return false;
        }

        return isLegalIdentifier(s);
    }

    /**
     * Returns true if the argument is a legal identifier, and false otherwise.
     *
     * @param s DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    private boolean isLegalIdentifier(String s) {
        if (s == null) {
            return false;
        }

        if (!(Character.isJavaIdentifierStart(s.charAt(0)))) {
            return false;
        }

        for (int i = 1; i < s.length(); i++)
            if (!((Character.isJavaIdentifierPart(s.charAt(i))))) {
                return false;
            }

        return true;
    } // isLegalIdentifier

    /**
     **/
    private boolean isTypeKeyword(String s) {
        if (s == null) {
            return false;
        }

        if (s.equals("map")) {
            return true;
        }

        if (s.equals("ode")) {
            return true;
        }

        if (s.equals("sde")) {
            return true;
        }

        if (s.equals("differentiable")) {
            return true;
        }

        if (s.equals("nondifferentiable")) {
            return true;
        }

        if (s.equals("invertible")) {
            return true;
        }

        if (s.equals("stateinitializer")) {
            return true;
        }

        if (s.equals("parameterinitializer")) {
            return true;
        }

        if (s.equals("staterangeinitializer")) {
            return true;
        }

        if (s.equals("parameterrangeinitializer")) {
            return true;
        }

        if (s.equals("event")) {
            return true;
        }

        return false;
    }

    /**
     **/
    private boolean isVariablesKeyword(String s) {
        if (s == null) {
            return false;
        }

        if (s.equals("variable")) {
            return true;
        }

        return false;
    }

    /**
     **/
    private boolean isParametersKeyword(String s) {
        if (s == null) {
            return false;
        }

        if (s.equals("parameter")) {
            return true;
        }

        return false;
    }

    /**
     **/
    private boolean isConstantsKeyword(String s) {
        if (s == null) {
            return false;
        }

        if (s.equals("constant")) {
            return true;
        }

        return false;
    }

    /**
     **/
    private boolean isAuxillariesKeyword(String s) {
        if (s == null) {
            return false;
        }

        if (s.equals("auxiliary")) {
            return true;
        }

        return false;
    }

    /**
     **/
    private boolean isEventsKeyword(String s) {
        if (s == null) {
            return false;
        }

        if (s.equals("event")) {
            return true;
        }

        return false;
    }

    /**
     **/
    private boolean isProcessKeyword(String s) {
        if (s == null) {
            return false;
        }

        if (s.equals("process")) {
            return true;
        }

        return false;
    }

    /**
     **/
    private boolean isUserFunctionKeyword(String s) {
        if (s == null) {
            return false;
        }

        if (s.equals("function")) {
            return true;
        }

        return false;
    }

    /**
     **/
    private boolean isLogicalsKeyword(String s) {
        if (s == null) {
            return false;
        }

        if (s.equals("logical")) {
            return true;
        }

        return false;
    }

    /**
     *
     **/
    private boolean isJavaKeyword(String s) {
        if (s == null) {
            return false;
        } else if (s.equals("import")) {
            return true;
        } else if (s.equals("class")) {
            return true;
        } else if (s.equals("extends")) {
            return true;
        } else if (s.equals("implements")) {
            return true;
        } else if (s.equals("public")) {
            return true;
        } else if (s.equals("private")) {
            return true;
        } else if (s.equals("protected")) {
            return true;
        } else if (s.equals("new")) {
            return true;
        } else if (s.equals("return")) {
            return true;
        } else if (s.equals("final")) {
            return true;
        } else if (s.equals("byte")) {
            return true;
        } else if (s.equals("boolean")) {
            return true;
        } else if (s.equals("int")) {
            return true;
        } else if (s.equals("long")) {
            return true;
        } else if (s.equals("double")) {
            return true;
        } else if (s.equals("float")) {
            return true;
        } else if (s.equals("String")) {
            return true;
        } else if (s.equals("if")) {
            return true;
        } else if (s.equals("void")) {
            return true;
        } else if (s.equals("switch")) {
            return true;
        } else if (s.equals("case")) {
            return true;
        } else if (s.equals("break")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * The method returns true if the String should be highlighted when
     * presented as a HTML text.
     *
     * @param s DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    private boolean isJanetKeyword(String s) {
        if (s.equals("org.jscience.mathematics.analysis.*")) {
            return true;
        } else if (s.equals("Vector")) {
            return true;
        } else if (s.equals("VectorType")) {
            return true;
        } else if (s.equals("org.jscience.mathematics.analysis.Vector")) {
            return true;
        } else if (s.equals("org.jscience.mathematics.analysis.VectorType")) {
            return true;
        } else if (s.equals("Matrix")) {
            return true;
        } else if (s.equals("MatrixType")) {
            return true;
        } else if (s.equals("org.jscience.mathematics.analysis.Matrix")) {
            return true;
        } else if (s.equals("org.jscience.mathematics.analysis.MatrixType")) {
            return true;
        } else if (s.equals("StateConscious")) {
            return true;
        } else if (s.equals("StateNamed")) {
            return true;
        } else if (s.equals("ParameterNamed")) {
            return true;
        } else if (s.equals("TimeDiscrete")) {
            return true;
        } else if (s.equals("TimeContinuous")) {
            return true;
        } else if (s.equals("Autonomous")) {
            return true;
        } else if (s.equals("NonAutonomous")) {
            return true;
        } else if (s.equals("Invertible")) {
            return true;
        } else if (s.equals("ParameterDependent")) {
            return true;
        } else if (s.equals("Differentiable")) {
            return true;
        } else if (s.equals("AutonomousDifferentiable")) {
            return true;
        } else if (s.equals("NonAutonomousDifferentiable")) {
            return true;
        } else if (s.equals("AutonomousParameterDifferentiable")) {
            return true;
        } else if (s.equals("NonAutonomousParameterDifferentiable")) {
            return true;
        } else if (s.equals("AutonomousInvertible")) {
            return true;
        } else if (s.equals("NonAutonomousInvertible")) {
            return true;
        } else if (s.equals("StateInitializer")) {
            return true;
        } else if (s.equals("ParameterInitializer")) {
            return true;
        } else if (s.equals("StateRangeInitializer")) {
            return true;
        } else if (s.equals("ParameterRangeInitializer")) {
            return true;
        } else if (s.equals("PeriodicallyForced")) {
            return true;
        } else if (s.equals("Describable")) {
            return true;
        } else if (s.equals("Expandable")) {
            return true;
        } else if (s.equals("TaylorMap")) {
            return true;
        } else if (s.equals("AuxiliaryDependent")) {
            return true;
        } else if (s.equals("AuxiliaryDependent")) {
            return true;
        } else if (s.equals("AuxiliaryNamed")) {
            return true;
        } else if (s.equals("AuxiliaryNamed")) {
            return true;
        } else if (s.equals("java.io.Serializable")) {
            return true;
        } else if (s.equals("Serializable")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Parses the <B>String</B> and attempts to generate symbolic expressions
     * for the right hand side(s). See examples above. Diagnostic information
     * about what happende during parsing can be retrieved with the method
     * <I>getInformation</I>. If no error then null is returned.
     *
     * @param s DOCUMENT ME!
     * @return String representing error message.
     */
    public String parseSource(String s) {
        try {
            if (0 == 0) {
                return oldparseSource(s);
            }

            //
            compiled = false;

            // Create/recreate space for variables etc.
            // Deletes old versions (if any) which is important.
            variables = new java.util.ArrayList();
            variableNames = new java.util.ArrayList();
            parameters = new java.util.ArrayList();
            parameterNames = new java.util.ArrayList();
            constants = new java.util.ArrayList();
            constantNames = new java.util.ArrayList();
            auxillaries = new java.util.ArrayList();
            auxiliaryNames = new java.util.ArrayList();
            userFunctions = new java.util.ArrayList();
            userFunctionNames = new java.util.ArrayList();
            userFunctionParameters = new java.util.ArrayList();
            logicals = new java.util.ArrayList();
            logicalNames = new java.util.ArrayList();
            expressions = new java.util.ArrayList();
            inverseexpressions = new java.util.ArrayList();
            eventNames = new java.util.ArrayList();
            eventConditions = new java.util.ArrayList();
            eventTargets = new java.util.ArrayList();
            eventValues = new java.util.ArrayList();
            processNames = new java.util.ArrayList();

            // Various declarations and initialisations.
            boolean lastTokenWasOperator = false;
            informationString = "Compilation starting." + CR;

            // Is there any source code available?
            if ((s == null) || s.equals("")) {
                return "No source code.";
            }

            // There is some source code.
            // Check for initial comment which will be used to implement
            // interface Describable
            int commentStart;

            // There is some source code.
            // Check for initial comment which will be used to implement
            // interface Describable
            int commentEnd;
            commentStart = s.indexOf("/*");
            commentEnd = s.indexOf("*/");

            if (containsInitialComment = (commentStart >= 0) &&
                    (commentEnd > (commentStart + 1))) {
                informationString += ("Initial comment found." + CR);
                initialComment = s.substring(commentStart + 2, commentEnd);

                // remove a white space if found at first position
                if (initialComment.indexOf(" ") == 0) {
                    initialComment = initialComment.substring(1,
                            initialComment.length());
                }

                int i;

                if (initialComment.indexOf("\"") >= 0) {
                    return "Initial comment contains double quote (\").";
                }

                while ((i = initialComment.indexOf(CR)) >= 0) {
                    initialComment = initialComment.substring(0, i) +
                            "\" + System.getProperty(\"line.separator\") + " +
                            "\"" +
                            initialComment.substring(i + 1, initialComment.length());
                }
            }

// Now we remove all comments.
            cleaning:
            while ((commentStart = s.indexOf("/*")) >= 0) {
                if ((commentEnd = s.indexOf("*/")) >= commentStart) {
                    s = s.substring(0, commentStart) +
                            s.substring(commentEnd + 2, s.length());
                } else {
                    break cleaning;
                }
            }

            // Experimental code CK 11082002
            // Remove all carriage returns from the source code.
            int pos;

            while ((pos = s.indexOf(CR)) >= 0) {
                s = s.substring(0, pos) +
                        s.substring(pos + CR.length(), s.length());
            } // while

            // Remove all ISO control characters; experimental code 16032001 CK
            pos = 0;

            while (pos < s.length()) {
                if (Character.isISOControl(s.charAt(pos))) {
                    s = s.substring(0, pos) + s.substring(pos + 1, s.length());
                } // if
                else {
                    pos++;
                } // else
            } // while

            // lets look at it all
            StringTokenizer tokenizer = new StringTokenizer(s, ";");

            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                System.out.println("Analyzing:" + token);

                if (typeDeclarationPattern.matcher(token).matches()) {
                    System.out.println("typeDeclarationPattern");
                }
                //variableDeclarationPattern
                else if (variableDeclarationPattern.matcher(token).matches()) {
                    System.out.println("variableDeclarationPattern");

                    Matcher matcher = variableDeclarationPattern.matcher(token);
                    matcher.matches();

                    String t = matcher.group(1) + matcher.group(2);
                    System.out.println("t=<" + t + ">");

                    StringTokenizer localTokenizer = new StringTokenizer(t, " ,");

                    while (localTokenizer.hasMoreTokens()) {
                        String v = localTokenizer.nextToken().trim();
                        System.out.println("v=<" + v + ">");

                        // Check validity of variable name.
                        String msg = checkName(v);

                        if (msg != null) {
                            return msg;
                        } // if
                        else {
                            variableNames.add(v);
                            variables.add(new Variable(v, 0));
                            informationString += ("Variable " + v + " added." +
                                    CR);
                            System.out.println("Variable " + v + " added.");
                        } // else
                    } // while
                } else if (processDeclarationPattern.matcher(token).matches()) {
                    System.out.println("processDeclarationPattern");
                } else if (variableDeclarationAndAssignmentPattern.matcher(
                        token).matches()) {
                    System.out.println(
                            "variableDeclarationAndAssignmentPattern");
                } else if (dynamicEquationPattern.matcher(token).matches()) {
                    System.out.println("dynamicEquationPattern");
                } else if (inverseEquationPattern.matcher(token).matches()) {
                    System.out.println("inverseEquationPattern");
                } else if (parameterDeclarationPattern.matcher(token).matches()) {
                    System.out.println("parameterDeclarationPattern");

                    Matcher matcher = parameterDeclarationPattern.matcher(token);
                    matcher.matches();

                    String t = matcher.group(1) + matcher.group(2);
                    System.out.println("t=<" + t + ">");

                    StringTokenizer localTokenizer = new StringTokenizer(t, " ,");

                    while (localTokenizer.hasMoreTokens()) {
                        String p = localTokenizer.nextToken().trim();

                        //System.out.println("p=<"+p+">");
                        // Check validity of parameter name.
                        String msg = checkName(p);

                        if (msg != null) {
                            return msg;
                        } // if
                        else {
                            parameterNames.add(p);
                            parameters.add(new Parameter(p, 0));
                            informationString += ("Parameter " + p + " added." +
                                    CR);
                            System.out.println("Parameter " + p + " added.");
                        } // else
                    } // while
                } else if (auxiliaryDeclarationPattern.matcher(token).matches()) {
                    System.out.println("auxiliaryDeclarationPattern");
                } else if (functionDeclarationPattern.matcher(token).matches()) {
                    System.out.println("functionDeclarationPattern");
                } else if (functionDefinitionPattern.matcher(token).matches()) {
                    System.out.println("functionDefinitionPattern");
                } else if (rangeAssignmentPattern.matcher(token).matches()) {
                    System.out.println("rangeAssignmentPattern");
                } else if (assignmentPattern.matcher(token).matches()) {
                    System.out.println("assignmentPattern");
                } else {
                    System.out.println("Unrecognized command: " + token);
                }
            }

            return oldparseSource(s);
        } // try
        catch (Exception e) { // Very serious situation. A bug is present in ExpressionParser!
            e.printStackTrace();

            return "An unhandled syntax error was detected.";
        }
    } // parseSource

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public String oldparseSource(String s) {
        try {
            //
            compiled = false;

            // Create/recreate space for variables etc.
            // Deletes old versions (if any) which is important.
            variables = new java.util.ArrayList();
            variableNames = new java.util.ArrayList();
            parameters = new java.util.ArrayList();
            parameterNames = new java.util.ArrayList();
            constants = new java.util.ArrayList();
            constantNames = new java.util.ArrayList();
            auxillaries = new java.util.ArrayList();
            auxiliaryNames = new java.util.ArrayList();
            userFunctions = new java.util.ArrayList();
            userFunctionNames = new java.util.ArrayList();
            userFunctionParameters = new java.util.ArrayList();
            logicals = new java.util.ArrayList();
            logicalNames = new java.util.ArrayList();
            expressions = new java.util.ArrayList();
            inverseexpressions = new java.util.ArrayList();
            eventNames = new java.util.ArrayList();
            eventConditions = new java.util.ArrayList();
            eventTargets = new java.util.ArrayList();
            eventValues = new java.util.ArrayList();
            processNames = new java.util.ArrayList();

            // Various declarations and initialisations.
            boolean lastTokenWasOperator = false;
            informationString = "Compilation starting." + CR;

            // Is there any source code available?
            if ((s == null) || s.equals("")) {
                return "No source code.";
            }

            // There is some source code.
            // Check for initial comment which will be used to implement
            // interface Describable
            int commentStart;

            // There is some source code.
            // Check for initial comment which will be used to implement
            // interface Describable
            int commentEnd;
            commentStart = s.indexOf("/*");
            commentEnd = s.indexOf("*/");

            if (containsInitialComment = (commentStart >= 0) &&
                    (commentEnd > (commentStart + 1))) {
                informationString += ("Initial comment found." + CR);
                initialComment = s.substring(commentStart + 2, commentEnd);

                // remove a white space if found at first position
                if (initialComment.indexOf(" ") == 0) {
                    initialComment = initialComment.substring(1,
                            initialComment.length());
                }

                int i;

                if (initialComment.indexOf("\"") >= 0) {
                    return "Initial comment contains double quote (\").";
                }

                while ((i = initialComment.indexOf(CR)) >= 0) {
                    initialComment = initialComment.substring(0, i) +
                            "\" + System.getProperty(\"line.separator\") + " +
                            "\"" +
                            initialComment.substring(i + 1, initialComment.length());
                }
            }

// Now we remove all comments.
            cleaning:
            while ((commentStart = s.indexOf("/*")) >= 0) {
                if ((commentEnd = s.indexOf("*/")) >= commentStart) {
                    s = s.substring(0, commentStart) +
                            s.substring(commentEnd + 2, s.length());
                } else {
                    break cleaning;
                }
            }

            // Experimental code CK 11082002
            // Remove all carriage returns from the source code.
            int pos;

            while ((pos = s.indexOf(CR)) >= 0) {
                s = s.substring(0, pos) +
                        s.substring(pos + CR.length(), s.length());
            } // while

            // Remove all ISO control characters; experimental code 16032001 CK
            pos = 0;

            while (pos < s.length()) {
                if (Character.isISOControl(s.charAt(pos))) {
                    s = s.substring(0, pos) + s.substring(pos + 1, s.length());
                } // if
                else {
                    pos++;
                } // else
            } // while

            Pattern pattern = Pattern.compile("\\s*(type)\\s*(.*?;)(.*)");
            Matcher matcher = pattern.matcher(s);
            boolean matches = matcher.matches();
            String keys;

            if (matches) {
                String type = matcher.group(1);
                keys = matcher.group(2);
                s = matcher.group(3);

                if (!type.equals("type")) {
                    return "Type statement missing type keyword.";
                } // if

                containsStateInit = keys.indexOf("stateinitializer") >= 0;
                containsParameterInit = keys.indexOf("parameterinitializer") >= 0;
                containsStateRange = keys.indexOf("staterangeinitializer") >= 0;
                containsParameterRange = keys.indexOf(
                        "parameterrangeinitializer") >= 0;
                containsEvents = keys.indexOf("event") >= 0;
                containsProcesses = keys.indexOf("sde") >= 0;

                //  		containsTypes          = s.indexOf( "type "                     ) >= 0;
                containsVariables = s.indexOf("variable ") >= 0;
                containsParameters = s.indexOf("parameter ") >= 0;
                containsConstants = s.indexOf("constant ") >= 0;
                containsAuxillaries = s.indexOf("auxiliary ") >= 0;
                containsUserFunctions = s.indexOf("function ") >= 0;
                containsLogicals = s.indexOf("logical ") >= 0;
            } // if
            else {
                return "Initial statement is not type statement.";
            } // else

            // Look for the presence of keywords.
            // Note that this is not a good idea, but at the moment it works
            // (and it is difficult to cheat it!).
            //  	    containsTypes          = s.indexOf( "type "                     ) >= 0;
            //  	    containsVariables      = s.indexOf( "variable "                 ) >= 0;
            //  	    containsParameters     = s.indexOf( "parameter "                ) >= 0;
            //  	    containsConstants      = s.indexOf( "constant "                 ) >= 0;
            //  	    containsAuxillaries    = s.indexOf( "auxiliary "                ) >= 0;
            //  	    containsUserFunctions  = s.indexOf( "function "                 ) >= 0;
            //  	    containsLogicals       = s.indexOf( "logical "                  ) >= 0;
            //  	    containsStateInit      = s.indexOf( "stateinitializer"          ) >= 0;
            //  	    containsParameterInit  = s.indexOf( "parameterinitializer"      ) >= 0;
            //  	    containsStateRange     = s.indexOf( "staterangeinitializer"     ) >= 0;
            //  	    containsParameterRange = s.indexOf( "parameterrangeinitializer" ) >= 0;
            //  	    containsEvents         = s.indexOf( "event"                     ) >= 0;
            //  	    containsProcesses      = s.indexOf( "sde"                       ) >= 0;
            if (containsEvents) {
                informationString += ("Source contains events." + CR);
            }

            if (containsParameters) {
                informationString += ("Source contains parameters." + CR);
            }

            if (containsConstants) {
                informationString += ("Source contains constants." + CR);
            }

            if (containsAuxillaries) {
                informationString += ("Source contains auxiliaries." + CR);
            }

            if (containsUserFunctions) {
                informationString += ("Source contains user functions." + CR);
            }

            if (containsProcesses) {
                informationString += ("Source contains processes." + CR);
            }

            // Is keyword <type> present?
            //  	    if ( ! containsTypes  ) {
            //  		return "Keyword type is missing.";
            //  	    }
            // Is keyword <variable> present?
            if (!containsVariables) {
                return "Keyword variable is missing.";
            }

            //  	    // Remove all carriage returns from the source code.
            //    	    int pos;
            //  	    while ( ( pos = s.indexOf( CR ) ) >= 0 ) {
            //  		s = s.substring( 0, pos ) +
            //  		    s.substring( pos + CR.length(), s.length() );
            //  	    } // while
            //  	    // Remove all ISO control characters; experimental code 16032001 CK
            //  	    pos = 0;
            //  	    while ( pos < s.length() ) {
            //  		if ( Character.isISOControl( s.charAt( pos ) ) ) {
            //  		    s = s.substring( 0, pos ) + s.substring( pos + 1, s.length() );
            //  		} // if
            //  		else {
            //  		    pos++;
            //  		} // else
            //  	    } // while
            // Prepare tokenizer
            StringTokenizer source = new StringTokenizer(s, ";");
            String r = ""; //source.nextToken();
            StringTokenizer tars = new StringTokenizer(keys, " ,;");

            // Read token <type>
            String keyword = ""; //tars.nextToken();

            //  	    if ( ! keyword.equals( "type" ) ) { // if keyword type is missing assume defaults
            //  		typeKeywordMissing = true;
            //  		//return "Token " + keyword + " read where keyword type was expected.";
            //  	    }
            // Read all types.
            while (tars.hasMoreTokens()) {
                String v = tars.nextToken();

                // Check validity of type name.
                if (isTypeKeyword(v)) {
                    if (v.equals("map")) {
                        isTimeDiscrete = true;
                        informationString += ("System is declared discrete." +
                                CR);
                    } else if (v.equals("ode")) {
                        isTimeContinuous = true;
                        informationString += ("System is declared timecontinuous." +
                                CR);
                    } else if (v.equals("sde")) {
                        isTimeContinuous = true;
                        informationString += ("System is declared timecontinuous." +
                                CR);
                    } else if (v.equals("invertible")) {
                        isInvertible = true;
                        informationString += ("System is declared invertible." +
                                CR);
                    } else if (v.equals("differentiable")) {
                        isDifferentiable = true;
                        informationString += ("System is declared differentiable." +
                                CR);
                    } else if (v.equals("nondifferentiable")) {
                        isNondifferentiable = true;
                        informationString += ("System is declared nondifferentiable." +
                                CR);
                    } else if (v.equals("stateinitializer")) {
                        informationString += ("System is declared initializable." +
                                CR);
                    } else if (v.equals("parameterinitializer")) {
                        informationString += ("System is declared parameter initializable." +
                                CR);
                    } else if (v.equals("staterangeinitializer")) {
                        informationString += ("System is declared range initializable." +
                                CR);
                    } else if (v.equals("parameterrangeinitializer")) {
                        informationString += ("System is declared parameter range initializable." +
                                CR);
                    }
                } else {
                    return "" + v + " is not a type.";
                }
            }

            // Check for inconsistencies in type declarations
            if (!(isTimeDiscrete || isTimeContinuous)) {
                return "System has been neither been declared discrete (map) or time continuous (ode)." +
                        CR;
            }

            if (isTimeDiscrete && isTimeContinuous) {
                return "System has been declared both discrete (map) and time continuous (ode)." +
                        CR;
            }

            // Prepare tokenizer
            if (!typeKeywordMissing) {
                r = source.nextToken();
            }

            StringTokenizer vars = new StringTokenizer(r, " ,");
            informationString += ("Reading variable declarations." + CR);

            // Read token variables.
            keyword = vars.nextToken();

            if (!isVariablesKeyword(keyword)) {
                return "Token " + keyword +
                        " read where keyword variable was expected.";
            }

            if (!vars.hasMoreTokens()) {
                return "The source code has no variables." + CR;
            } else {
                // Read all variables.
                while (vars.hasMoreTokens()) {
                    String v = vars.nextToken();

                    // Check validity of variable name.
                    String msg = checkName(v);

                    if (msg != null) {
                        return msg;
                    } // if

                    //  		    if ( ! isLegalKeyword( v ) ) {
                    //  			return "" + v + " is not a legal name.";
                    //  		    }
                    //  		    else if ( reservedNamesVector.contains( v ) ) {
                    //  			return "" + v + " is a reserved word.";
                    //  		    }
                    //  		    else if ( variableNames.contains( v ) ) {
                    //  			informationString += "Variable " + v + " already detected (not an error)." + CR;
                    //  		    }
                    // v contains a new variable name so let us add it.
                    else {
                        variableNames.add(v);
                        variables.add(new Variable(v, 0));
                        informationString += ("Variable " + v + " added." + CR);
                    }
                }
            }

            if (containsProcesses) {
                informationString += ("Reading process declarations." + CR);

                // Read token process.
                String q = source.nextToken();
                StringTokenizer procs = new StringTokenizer(q, " ,;");
                keyword = procs.nextToken();

                if (!isProcessKeyword(keyword)) {
                    return "Token " + keyword +
                            " read where keyword process was expected.";
                }

                if (!procs.hasMoreTokens()) {
                    return "The source code has no processes." + CR;
                } else {
                    // Read all processes.
                    while (procs.hasMoreTokens()) {
                        String p = procs.nextToken();

                        // Check validity of process name.
                        String msg = checkName(p);

                        if (msg != null) {
                            return msg;
                        } // if

                        //  			if ( ! isLegalKeyword( p ) ) {
                        //  			    return "" + p + " is not a legal name.";
                        //  			}
                        //  			else if ( reservedNamesVector.contains( p ) ) {
                        //  			    return "" + p + " is a reserved word.";
                        //  			}
                        //  			else if ( variableNames.contains( p ) ) {
                        //  			    return "Process name " + p + " is illegal;  there is a variable with this name.";
                        //  			}
                        //  			else if ( processNames.contains( p ) ) {
                        //  			    informationString += "Process " + p + " already detected (not an error)." + CR;
                        //  			}
                        // p contains a new process name so let us add it.
                        else {
                            processNames.add(p);
                            variableNames.add(p);
                            variables.add(new Variable(p, 0));
                            informationString += ("Process " + p + " added." +
                                    CR);
                        }
                    }
                }
            }

            if (containsParameters) {
                informationString += ("Reading parameter declarations." + CR);

                // Read token parameters
                String q = source.nextToken();
                StringTokenizer pars = new StringTokenizer(q, " ,;");
                keyword = pars.nextToken();

                if (!isParametersKeyword(keyword)) {
                    return "Token " + keyword +
                            " read where keyword parameter was expected.";
                }

                if (!pars.hasMoreTokens()) {
                    containsParameters = false;
                    informationString += ("System declared parameter dependent but no parameters were defined." +
                            CR);
                } else {
                    // Read all parameters.
                    while (pars.hasMoreTokens()) {
                        String p = pars.nextToken();

                        // Check validity of parameter name.
                        String msg = checkName(p);

                        if (msg != null) {
                            return msg;
                        } // if

                        //  			if ( ! isLegalKeyword( p ) ) {
                        //  			    return "" + p + " is not a legal name.";
                        //  			}
                        //  			else if ( reservedNamesVector.contains( p ) ) {
                        //  			    return "" + p + " is a reserved word.";
                        //  			}
                        //  			else if ( parameterNames.contains( p ) ) {
                        //  			    informationString += "Parameter " + p + " already detected (not an error)." + CR;
                        //  			}
                        //  			else if ( processNames.contains( p ) ) {
                        //  			    return "Parameter name " + p + " is illegal;  there is a process with this name.";
                        //  			}
                        //  			else if ( variableNames.contains( p ) ) {
                        //  			    return "Parameter name " + p + " is illegal;  there is a variable with this name.";
                        //  			}
                        // p contains a new parameter name so let us add it.
                        else {
                            parameterNames.add(p);
                            parameters.add(new Parameter(p, 0));
                            informationString += ("Parameter " + p + " added." +
                                    CR);
                        }
                    }
                }
            }

            //For testing purposes we may sometimes want to throw an error CK
            //if ( 0 == 0 ) throw new Exception( "Unfortunate error." );
            // Add predefined constant names
            constantNames.add("pi");
            constantNames.add("e");

            // Add predefined constant values
            constants.add(new Constant(4.0 * Math.atan(1.0)));
            constants.add(new Constant(Math.exp(1.0)));

            if (containsConstants) {
                informationString += ("Reading constant declarations." + CR);

                // Read token constant
                String q = source.nextToken();
                StringTokenizer consts = new StringTokenizer(q, " ,;");
                keyword = consts.nextToken();

                if (!isConstantsKeyword(keyword)) {
                    return "Token " + keyword +
                            " read where keyword constant was expected.";
                }

                if (!consts.hasMoreTokens()) {
                    containsConstants = false;
                    informationString += ("System declared constant dependent but no constants were defined." +
                            CR);
                } else {
                    // Read all constants.
                    while (consts.hasMoreTokens()) {
                        String c = consts.nextToken();

                        // Check validity of constant name.
                        String msg = checkName(c);

                        if (msg != null) {
                            return msg;
                        } // if

                        //  			if ( ! isLegalKeyword( c ) ) {
                        //  			    return "" + c + " is not a legal name.";
                        //  			}
                        //  			else if ( reservedNamesVector.contains( c ) ) {
                        //  			    return "" + c + " is a reserved word.";
                        //  			}
                        //  			else if ( constantNames.contains( c ) ) {
                        //  			    informationString += "Constant " + c + " already detected (not an error)." + CR;
                        //  			}
                        //  			else if ( processNames.contains( c ) ) {
                        //  			    return "Constant name " + c + " is illegal;  there is a process with this name.";
                        //  			}
                        //  			else if ( variableNames.contains( c ) ) {
                        //  			    return "Constant name " + c + " is illegal;  there is a variable with this name.";
                        //  			}
                        //  			else if ( parameterNames.contains( c ) ) {
                        //  			    return "Constant name " + c + " is illegal;  there is a parameter with this name.";
                        //  			}
                        // c contains a new constant name so let us add it.
                        else {
                            constantNames.add(c);
                            informationString += ("Constant " + c + " added." +
                                    CR);
                        }
                    }
                }
            }

            if (containsAuxillaries) {
                informationString += ("Reading auxiliary declarations." + CR);

                // Read token auxiliary
                String q = source.nextToken();
                StringTokenizer auxs = new StringTokenizer(q, " ,;");
                keyword = auxs.nextToken();

                if (!isAuxillariesKeyword(keyword)) {
                    return "Token " + keyword +
                            " read where keyword auxiliary was expected.";
                }

                if (!auxs.hasMoreTokens()) {
                    containsAuxillaries = false;
                    informationString += ("System declared auxiliary dependent but no auxiliaries were defined." +
                            CR);
                } else {
                    // Read all auxillaries.
                    while (auxs.hasMoreTokens()) {
                        String a = auxs.nextToken();

                        // Check validity of auxiliary name.
                        String msg = checkName(a);

                        if (msg != null) {
                            return msg;
                        } // if

                        //  			if ( ! isLegalKeyword( a ) ) {
                        //  			    return "" + a + " is not a legal name.";
                        //  			}
                        //  			else if ( reservedNamesVector.contains( a ) ) {
                        //  			    return "" + a + " is a reserved word.";
                        //  			}
                        //  			else if ( auxiliaryNames.contains( a ) ) {
                        //  			    informationString += "Auxiliary " + a + " already detected (not an error)." + CR;
                        //  			}
                        //  			else if ( processNames.contains( a ) ) {
                        //  			    return "Auxiliary name " + a + " is illegal;  there is a process with this name.";
                        //  			}
                        //  			else if ( variableNames.contains( a ) ) {
                        //  			    return "Auxiliary name " + a + " is illegal;  there is a variable with this name.";
                        //  			}
                        //  			else if ( parameterNames.contains( a ) ) {
                        //  			    return "Auxiliary name " + a + " is illegal;  there is a parameter with this name.";
                        //  			}
                        //  			else if ( constantNames.contains( a ) ) {
                        //  			    return "Auxiliary name " + a + " is illegal;  there is a constant with this name.";
                        //  			}
                        // a contains a new auxiliary name so let us add it.
                        else {
                            auxiliaryNames.add(a);
                            informationString += ("Auxiliary " + a + " added." +
                                    CR);
                            containsPeriod |= a.equals("period");
                        }
                    } // while
                }
            }

            if (containsUserFunctions) {
                informationString += ("Reading user function declarations." +
                        CR);

                // Read token function
                String q = source.nextToken();
                StringTokenizer userfuncs = new StringTokenizer(q, " ,;");
                keyword = userfuncs.nextToken();

                if (!isUserFunctionKeyword(keyword)) {
                    return "Token " + keyword +
                            " read where keyword function was expected.";
                }

                if (!userfuncs.hasMoreTokens()) {
                    containsUserFunctions = false;
                    informationString += ("System declared user function dependent but no user functions were defined." +
                            CR);
                } else {
                    // Read all user functions.
                    while (userfuncs.hasMoreTokens()) {
                        String f = userfuncs.nextToken();

                        // Check validity of user function name.
                        String msg = checkName(f);

                        if (msg != null) {
                            return msg;
                        } // if

                        //  			if ( ! isLegalKeyword( a ) ) {
                        //  			    return "" + a + " is not a legal name.";
                        //  			}
                        //  			else if ( reservedNamesVector.contains( a ) ) {
                        //  			    return "" + a + " is a reserved word.";
                        //  			}
                        //  			else if ( userFunctionNames.contains( a ) ) {
                        //  			    informationString += "User function " + a + " already detected (not an error)." + CR;
                        //  			}
                        //  			else if ( variableNames.contains( a ) ) {
                        //  			    return "User function name " + a + " is illegal;  there is a variable with this name.";
                        //  			}
                        //  			else if ( parameterNames.contains( a ) ) {
                        //  			    return "User function name " + a + " is illegal;  there is a parameter with this name.";
                        //  			}
                        //  			else if ( constantNames.contains( a ) ) {
                        //  			    return "User function name " + a + " is illegal;  there is a constant with this name.";
                        //  			}
                        //  			else if ( auxiliaryNames.contains( a ) ) {
                        //  			    return "User function name " + a + " is illegal;  there is an auxiliary with this name.";
                        //  			}
                        // a contains a new user function name so let us add it.
                        else {
                            userFunctionNames.add(f);
                            informationString += ("User function " + f +
                                    " added." + CR);
                        }
                    } // while
                }
            }

            // Read the definition of events.
            if (containsEvents) {
                informationString += ("Reading event declarations." + CR);

                // Read token event
                String q = source.nextToken();
                StringTokenizer t = new StringTokenizer(q, " ,;");
                keyword = t.nextToken();

                if (!isEventsKeyword(keyword)) {
                    return "Token " + keyword +
                            " read where keyword event was expected.";
                }

                if (!t.hasMoreTokens()) {
                    informationString += ("System declared event dependent but no events were defined." +
                            CR);
                    containsEvents = false;
                } else {
                    // Read all events.
                    while (t.hasMoreTokens()) {
                        String e = t.nextToken();

                        // Check validity of auxiliary name.
                        String msg = checkName(e);

                        if (msg != null) {
                            return msg;
                        } // if

                        //  			if ( ! isLegalKeyword( a ) ) {
                        //  			    return "" + a + " is not a legal name.";
                        //  			}
                        //  			else if ( reservedNamesVector.contains( a ) ) {
                        //  			    return "" + a + " is a reserved word.";
                        //  			}
                        // a contains a new event name so let us add it.
                        else {
                            eventNames.add(e);
                            informationString += ("Event " + e + " added." +
                                    CR);
                        }
                    } // while
                }
            } // if

            // Read the definition of constants.
            if (containsConstants) {
                informationString += ("Reading constant definitions." + CR);

                for (int k = 2; k < getNumberOfConstants(); k++) {
                    // Read all constants.
                    // Read one line.
                    if (!source.hasMoreTokens()) {
                        return "No more tokens while reading constant definitions.";
                    }

                    String sourceLine = source.nextToken();
                    informationString += ("Processing: " + sourceLine + CR);

                    StringTokenizer tokens = new StringTokenizer(sourceLine,
                            "=", true);

                    if (!tokens.hasMoreTokens()) {
                        return "No more tokens while reading constant definitions.";
                    }

                    String lhs = tokens.nextToken();
                    int index;

                    while ((index = lhs.indexOf(" ")) >= 0) {
                        lhs = lhs.substring(0, index) +
                                lhs.substring(index + 1, lhs.length());
                    }

                    // Is it a proper left hand side?
                    if (!constantNames.contains(lhs)) {
                        return "" + lhs + " is not a constant name.";
                    }

                    // Does the constant definition occur in the right place?
                    if (constantNames.indexOf(lhs) != k) {
                        return "Constant definitions in wrong order.";
                    }

                    // Read tokens until a "=" is encountered or we run out of tokens.
                    String nothing = "?";

                    if (tokens.hasMoreTokens()) {
                        while (!nothing.equals("=") && tokens.hasMoreTokens()) {
                            nothing = tokens.nextToken();
                        }
                    } else {
                        return "No more tokens where = was expected.";
                    }

                    // Did we find an "="?
                    if (!nothing.equals("=")) {
                        return "Token " + nothing + " where = was expected.";
                    }

                    String line = "";

                    while (tokens.hasMoreTokens())
                        line += tokens.nextToken();

                    if (line.equals("")) {
                        return "No more tokens while reading constant definition.";
                    }

                    // Remove trailing ";"s if present.
                    if (line.substring(line.length() - 1, line.length() - 1)
                            .equals(";")) {
                        line = line.substring(0, line.length() - 1);
                    }

                    // new code being tested 18/11/2000 CK; in full use 05032001 CK
                    ExpressionParser constantparser = new ExpressionParser();
                    constantparser.setConstantNames(constantNames);
                    constantparser.setConstants(constants);

                    try {
                        Expression cnst = (Expression) constantparser.parse(line);

                        if (cnst != null) {
                            double doubleValue = cnst.eval();
                            constants.add(new Constant(doubleValue));
                            informationString += (constantNames.get(k) + "=" +
                                    doubleValue + CR);
                        } // if
                        else {
                            return "Non-constant " + line +
                                    " encountered in constant definition.";
                        } // else
                    } // try
                    catch (ArrayIndexOutOfBoundsException aioobe) {
                        return "Attempt to use undefined constant";
                    } // catch
                    catch (ClassCastException cce) {
                        return "Logical expression was not expected";
                    } // catch
                } // for
            } // if

            // Read the definitions of auxillaries.
            if (containsAuxillaries) {
                informationString += ("Reading auxiliary definitions." + CR);

                for (int k = 0; k < getNumberOfAuxillaries(); k++) {
                    // Read all auxiliary equations.
                    String sourceLine = source.nextToken();

                    // Remove all superflous white spaces.
                    int index;

                    while ((index = sourceLine.indexOf(" ")) >= 0) {
                        sourceLine = sourceLine.substring(0, index) +
                                sourceLine.substring(index + 1, sourceLine.length());
                    }

                    informationString += ("Processing: " + sourceLine + CR);

                    StringTokenizer tokens = new StringTokenizer(sourceLine,
                            operatorString + "=", true);
                    String auxEquation = tokens.nextToken();

                    // Is it a proper left hand side?
                    if (!auxiliaryNames.contains(auxEquation)) {
                        return "" + auxEquation + " is not an auxiliary name.";
                    }

                    // Does the differential/difference equation occur in the right place?
                    if (auxiliaryNames.indexOf(auxEquation) != k) {
                        return "Auxiliary equations in wrong order.";
                    }

                    if (isTimeContinuous && auxEquation.equals("cutValue")) {
                        informationString += ("System defines Poincare section." +
                                CR);
                        isPoincareSection = true;
                    }

                    // Read tokens until a "=" is encountered or we run out of tokens.
                    String nothing = "?";

                    if (tokens.hasMoreTokens()) {
                        while ((!nothing.equals("=")) &&
                                tokens.hasMoreTokens()) {
                            nothing = tokens.nextToken();
                        }
                    } else {
                        return "No more tokens where = was expected.";
                    }

                    // Did we find an "="?
                    if (!nothing.equals("=")) {
                        return "Token " + nothing +
                                " read where = was expected.";
                    }

                    String line = "";

                    while (tokens.hasMoreTokens())
                        line += tokens.nextToken();

                    if (line.equals("")) {
                        return "No more tokens while reading auxiliary definition.";
                    }

                    Object e;

                    try {
                        e = parse(line);
                    } // try
                    catch (ArrayIndexOutOfBoundsException aioobe) {
                        return "Attempt to use undefined auxiliary.";
                    } // catch

                    // Was parsing one line successful?
                    if (e == null) {
                        return getError();
                    } // if

                    if (e instanceof Expression) {
                        auxillaries.add(new Auxiliary(
                                (String) auxiliaryNames.get(k), (Expression) e));
                    } // if
                    else {
                        auxillaries.add(e);
                    } // else

                    informationString += ("Auxiliary " + auxiliaryNames.get(k) +
                            "=" + e + CR);
                } // for
            }

            // Read the definitions of user functions.
            if (containsUserFunctions) {
                informationString += ("Reading user function definitions." +
                        CR);

                for (int k = 0; k < getNumberOfUserFunctions(); k++) {
                    // Read all user function equations.
                    String sourceLine = source.nextToken();

                    // Remove all superflous white spaces.
                    int index;

                    while ((index = sourceLine.indexOf(" ")) >= 0) {
                        sourceLine = sourceLine.substring(0, index) +
                                sourceLine.substring(index + 1, sourceLine.length());
                    }

                    informationString += ("Processing: " + sourceLine + CR);

                    StringTokenizer alltokens = new StringTokenizer(sourceLine,
                            "=", true);

                    // split sourceLine into left hand side, = and right hand side
                    String lhs;

                    // split sourceLine into left hand side, = and right hand side
                    String equality;

                    // split sourceLine into left hand side, = and right hand side
                    String rhs;

                    if (alltokens.hasMoreTokens()) {
                        lhs = alltokens.nextToken();
                    } else {
                        return "No more tokens where user function left hand side was expected.";
                    }

                    if (alltokens.hasMoreTokens()) {
                        equality = alltokens.nextToken();
                    } else {
                        return "No more tokens where = was expected.";
                    }

                    if (alltokens.hasMoreTokens()) {
                        rhs = alltokens.nextToken();
                    }
                    // parse left hand side
                    else {
                        return "No more tokens where user function right hand side was expected.";
                    }

                    StringTokenizer lefttokens = new StringTokenizer(lhs,
                            "(,)", false);

                    if (!lefttokens.hasMoreTokens()) {
                        return "No more tokens where user function name was expected.";
                    }

                    String funEquation = lefttokens.nextToken(); // user function name
                    java.util.List formalNames = new java.util.ArrayList();

                    // read all the formal parameter names
                    while (lefttokens.hasMoreTokens()) {
                        String var = lefttokens.nextToken(); // formal parameter name

                        if (constantNames.contains(var)) {
                            return "Name " + var +
                                    " is already in use for a constant.";
                        } else if (variableNames.contains(var)) {
                            return "Name " + var +
                                    " is already in use for a variable.";
                        } else if (parameterNames.contains(var)) {
                            return "Name " + var +
                                    " is already in use for a parameter.";
                        } else if (auxiliaryNames.contains(var)) {
                            return "Name " + var +
                                    " is already in use for an auxiliary.";
                        } else if (!formalNames.contains(var)) {
                            formalNames.add(var);
                        } else {
                            return "Name " + var +
                                    " is already in use in user function " +
                                    funEquation + ".";
                        }
                    }

                    userFunctionParameters.add(formalNames);

                    // Is it a proper left hand side?
                    if (!userFunctionNames.contains(funEquation)) {
                        return "" + funEquation +
                                " is not a user function name.";
                    }

                    // Does the differential/difference equation occur in the right place?
                    if (userFunctionNames.indexOf(funEquation) != k) {
                        return "User function equations in wrong order.";
                    }

                    // return to parsing right hand side
                    if (!equality.equals("=")) {
                        return "Token " + equality +
                                " read where = was expected.";
                    }

                    StringTokenizer tokens = new StringTokenizer(rhs,
                            operatorString, true);
                    String line = "";

                    while (tokens.hasMoreTokens())
                        line += tokens.nextToken();

                    if (line.equals("")) {
                        return "No more tokens while reading user function definition.";
                    } // if

                    // add formal parameters temporarily
                    for (int m = 0; m < formalNames.size(); m++) {
                        String param = (String) formalNames.get(m);
                        variableNames.add(param);
                        variables.add(new Variable(param));
                    } // for

                    Expression e;

                    try {
                        e = (Expression) parse(line);
                    } // try
                    catch (ArrayIndexOutOfBoundsException aioobe) {
                        return "Attempt to use undefined user function.";
                    } // catch
                    catch (ClassCastException cce) {
                        return "Logical expression was not expected";
                    } // catch

                    // remove formal parameters
                    for (int m = 0; m < formalNames.size(); m++) {
                        variableNames.remove(variableNames.size() - 1);
                        variables.remove(variables.size() - 1);
                    } // for

                    // Was parsing one line successful?
                    if (e == null) {
                        return getError();
                    } // if

                    userFunctions.add(e);
                    informationString += ("User function " +
                            userFunctionNames.get(k) + "(");

                    for (int i = 0; i < formalNames.size(); i++) {
                        informationString += (String) formalNames.get(i);

                        if (i < (formalNames.size() - 1)) {
                            informationString += ",";
                        }
                    }

                    informationString += (")=" + e + CR);
                }
            }

            // Should we read some events definitions now?
            if (containsEvents) {
                informationString += ("Reading event definitions." + CR);

                for (int k = 0; k < getNumberOfEvents(); k++) {
                    // Read all event definitions.
                    String sourceLine = source.nextToken();

                    // Remove all superflous white spaces.
                    int index;

                    while ((index = sourceLine.indexOf(" ")) >= 0) {
                        sourceLine = sourceLine.substring(0, index) +
                                sourceLine.substring(index + 1, sourceLine.length());
                    }

                    informationString += ("Processing: " + sourceLine + CR);

                    StringTokenizer tokens = new StringTokenizer(sourceLine,
                            operatorString + "=", true);
                    String auxEquation = tokens.nextToken();

                    // Is it a proper left hand side?
                    if (!eventNames.contains(auxEquation)) {
                        return "" + auxEquation + " is not an event name.";
                    }

                    // Does the event definition occur in the right place?
                    if (eventNames.indexOf(auxEquation) != k) {
                        return "Event definitions in wrong order.";
                    }

                    // Read tokens until a "=" is encountered or we run out of tokens.
                    String nothing = "?";

                    if (tokens.hasMoreTokens()) {
                        while ((!nothing.equals("=")) &&
                                tokens.hasMoreTokens()) {
                            nothing = tokens.nextToken();
                        }
                    } else {
                        return "No more tokens where = was expected.";
                    }

                    // Did we find an "="?
                    if (!nothing.equals("=")) {
                        return "Token " + nothing +
                                " read where = was expected.";
                    }

                    String line = "";

                    while (tokens.hasMoreTokens())
                        line += tokens.nextToken();

                    if (line.equals("")) {
                        return "No more tokens while reading event definition.";
                    }

                    StringTokenizer p = new StringTokenizer(line, ":", false);
                    String s1;
                    String s2;
                    String s3;

                    if (p.hasMoreTokens()) {
                        s1 = p.nextToken();

                        if (p.hasMoreTokens()) {
                            s2 = p.nextToken();

                            if (p.hasMoreTokens()) {
                                s3 = p.nextToken();

                                if (p.hasMoreTokens()) {
                                    return "Too many tokens in event definition.";
                                } // if
                                else { // we have what we need

                                    Object o1 = parse(s1);

                                    if (o1 == null) {
                                        return "Error when parsing event condition " +
                                                s1;
                                    } // if

                                    Expression e1 = (Expression) o1;
                                    Object o2 = parse(s2);

                                    if (o2 == null) {
                                        return "Error when parsing event target " +
                                                s2;
                                    } // if

                                    Expression e2 = (Expression) o2;

                                    if (!(e2 instanceof Variable ||
                                            e2 instanceof Parameter)) {
                                        return "Event target must be variable or parameter.";
                                    } // if

                                    Object o3 = parse(s3);

                                    if (o3 == null) {
                                        return "Error when parsing event value " +
                                                s3;
                                    } // if

                                    Expression e3 = (Expression) o3;

                                    for (int i = 0; i < optimizationLevel;
                                         i++) {
                                        e1 = e1.optimize();
                                        e3 = e3.optimize();
                                    } // for

                                    //this part of Janet was never merged in.
                                    //Event e = new SimpleChangeEvent(e1,
                                    //        (NamedDataExpression) e2, e3);
                                    //e.setDescription(eventNames.get(k).toString());
                                } // else
                            } // if
                            else {
                                return "Missing one argument in event definition.";
                            } // else
                        } // if
                        else {
                            return "Missing two arguments in event definition.";
                        } // else
                    } // if
                    else {
                        return "No rhs in event definition.";
                    } // else

                    /*
                    Object e;
                    try {
                        e = parse( line );
                    } // try
                    catch ( ArrayIndexOutOfBoundsException aioobe ) {
                        return "Attempt to use undefined auxiliary.";
                    } // catch
                    // Was parsing one line successful?
                    if ( e == null ) {
                        return getError();
                    } // if
                    if ( e instanceof Expression ) {
                        auxillaries.add( new Auxiliary( (String)auxiliaryNames.get(k),
                                                        (Expression)e ) );
                    } // if
                    else {
                        auxillaries.add( e );
                    } // else
                    informationString += "Auxiliary " + auxiliaryNames.get( k ) +
                        "=" + e + CR;
                    */
                } // for
            } // if

            // While more tokens read on.
            informationString += ("Reading dynamic equations." + CR);

            while (source.hasMoreTokens()) {
                // Read all differential/difference equations.
                // Read one line (hopefully) containing a right hand side.
                String sourceLine = source.nextToken();

                // Remove all superflous white spaces.
                int index;

                while ((index = sourceLine.indexOf(" ")) >= 0) {
                    sourceLine = sourceLine.substring(0, index) +
                            sourceLine.substring(index + 1, sourceLine.length());
                }

                informationString += ("Processing: " + sourceLine + CR);

                if (sourceLine.equals("")) {
                    continue;
                }

                StringTokenizer tokens = new StringTokenizer(sourceLine,
                        operatorString + "=", true);
                String diffEquation = tokens.nextToken();

                // Is it a proper left hand side?
                if (!variableNames.contains(diffEquation.substring(0,
                        diffEquation.length() - 1))) {
                    return "" + diffEquation +
                            " is not derivative of variable.";
                }

                if (!(primes.indexOf(diffEquation.substring(diffEquation.length() -
                        1, diffEquation.length())) >= 0)) {
                    return "" + diffEquation +
                            " is not derivative of variable (prime symbol is not right).";
                }

                // Does the differential/difference equation occur in the right place?
                if (variableNames.indexOf(diffEquation.substring(0,
                        diffEquation.length() - 1)) != expressions.size()) {
                    return "Dynamic equations in wrong order.";
                }

                // Read tokens until a "=" is encountered or we run out of tokens.
                String nothing = "?";

                if (tokens.hasMoreTokens()) {
                    while ((!nothing.equals("=")) && tokens.hasMoreTokens()) {
                        nothing = tokens.nextToken();
                    }
                } else {
                    return "No more tokens where = was expected.";
                }

                // Did we find an "="?
                if (!nothing.equals("=")) {
                    return "Token " + nothing + " where = was expected.";
                }

                String line = "";

                while (tokens.hasMoreTokens())
                    line += tokens.nextToken();

                if (line.equals("")) {
                    return "No more tokens while reading dynamic equation definition.";
                }

                Expression e;

                try {
                    e = (Expression) parse(line);

                    // Was parsing one line successful?
                    if (e == null) {
                        return getError();
                    } // if
                } // try
                catch (ClassCastException cce) {
                    return "Logical expression was not expected";
                } // catch

                expressions.add(e);
                informationString += (diffEquation.substring(0,
                        diffEquation.length() - 1) + "'=" + e + CR);
                Auxiliary.deepDefinition = true; // don't use this CK 21102003
                informationString += (diffEquation.substring(0,
                        diffEquation.length() - 1) + "'=" + e + CR);
                Auxiliary.deepDefinition = false;

                // Have we read all differential/difference equations?
                if (expressions.size() == (variables.size() -
                        processNames.size())) {
                    break;
                }
            } // while

            // If the system is invertible we expect the inverses to appear next
            if (isInvertible) {
                informationString += ("Reading inverse dynamic equations." +
                        CR);

                while (source.hasMoreTokens()) {
                    // Read all inverse equations.
                    // Read one line (hopefully) containing a right hand side.
                    String sourceLine = source.nextToken();
                    informationString += ("Processing: " + sourceLine + CR);

                    // Remove all superflous white spaces.
                    int index;

                    while ((index = sourceLine.indexOf(" ")) >= 0) {
                        sourceLine = sourceLine.substring(0, index) +
                                sourceLine.substring(index + 1, sourceLine.length());
                    }

                    if (sourceLine.equals("")) {
                        continue;
                    }

                    StringTokenizer tokens = new StringTokenizer(sourceLine,
                            operatorString + "=", true);
                    String invEquation = tokens.nextToken();

                    // Is it a proper left hand side?
                    if (!variableNames.contains(invEquation.substring(0,
                            invEquation.length() - 1))) {
                        return "" + invEquation + " is not inverse function.";
                    }

                    if (!(inverses.indexOf(invEquation.substring(invEquation.length() -
                            1, invEquation.length())) >= 0)) {
                        return "" + invEquation +
                                " is not inverse function (inverse symbol is not right).";
                    }

                    // Does the inverse function occur in the right place?
                    if (variableNames.indexOf(invEquation.substring(0,
                            invEquation.length() - 1)) != inverseexpressions.size()) {
                        return "Inverse equations in wrong order.";
                    }

                    // Read tokens until a "=" is encountered or we run out of tokens.
                    String nothing = "?";

                    if (tokens.hasMoreTokens()) {
                        while ((!nothing.equals("=")) &&
                                tokens.hasMoreTokens()) {
                            nothing = tokens.nextToken();
                        }
                    } else {
                        return "No more tokens where = was expected.";
                    }

                    // Did we find an "="?
                    if (!nothing.equals("=")) {
                        return "Token " + nothing + " where = was expected.";
                    }

                    String line = "";

                    while (tokens.hasMoreTokens())
                        line += tokens.nextToken();

                    Expression e;

                    try {
                        e = (Expression) parse(line);

                        // Was parsing one line successful?
                        if (e == null) {
                            return getError();
                        } // if
                    } // try
                    catch (ClassCastException cce) {
                        return "Logical expression was not expected";
                    } // catch

                    inverseexpressions.add(e);
                    informationString += (invEquation.substring(0,
                            invEquation.length() - 1) + "~=" + e + CR);

                    // Have we read all inverses?
                    if (inverseexpressions.size() == variables.size()) {
                        break;
                    }
                } // while
            } // if

            // does the code contain initial state
            if (containsStateInit) {
                informationString += ("Reading initial state." + CR);

                for (int k = 0;
                     k < (getNumberOfVariables() - processNames.size());
                     k++) {
                    // Read all initial values.
                    // Read one line.
                    if (!source.hasMoreTokens()) {
                        return "No more tokens while reading initial state definitions.";
                    }

                    String sourceLine = source.nextToken();
                    informationString += ("Processing: " + sourceLine + CR);

                    StringTokenizer tokens = new StringTokenizer(sourceLine,
                            "=", true);

                    if (!tokens.hasMoreTokens()) {
                        return "No more tokens while reading initial state definitions.";
                    }

                    String lhs = tokens.nextToken();
                    int index;

                    while ((index = lhs.indexOf(" ")) >= 0) {
                        lhs = lhs.substring(0, index) +
                                lhs.substring(index + 1, lhs.length());
                    }

                    // Is it a proper left hand side?
                    if (!variableNames.contains(lhs)) {
                        return "" + lhs + " is not a variable name.";
                    }

                    // Does the initial state definition occur in the right place?
                    if (variableNames.indexOf(lhs) != k) {
                        return "Initial state definitions in wrong order.";
                    }

                    // Read tokens until a "=" is encountered or we run out of tokens.
                    String nothing = "?";

                    if (tokens.hasMoreTokens()) {
                        while (!nothing.equals("=") && tokens.hasMoreTokens()) {
                            nothing = tokens.nextToken();
                        }
                    } else {
                        return "No more tokens where = was expected.";
                    }

                    // Did we find an "="?
                    if (!nothing.equals("=")) {
                        return "Token " + nothing + " where = was expected.";
                    }

                    String line = "";

                    while (tokens.hasMoreTokens())
                        line += tokens.nextToken();

                    if (line.equals("")) {
                        return "No more tokens while reading dynamic equations.";
                    }

                    // Remove trailing ";"s if present.
                    if (line.substring(line.length() - 1, line.length() - 1)
                            .equals(";")) {
                        line = line.substring(0, line.length() - 1);
                    }

                    if (0 == 0) {
                        ExpressionParser constantparser = new ExpressionParser();
                        constantparser.setConstantNames(constantNames);
                        constantparser.setConstants(constants);

                        try {
                            Expression cnst = (Expression) constantparser.parse(line);

                            if (cnst != null) {
                                double doubleValue = cnst.eval();
                                initstates.add(new Double(doubleValue));
                                informationString += (variableNames.get(k) +
                                        "=" + doubleValue + CR);
                                ((Variable) variables.get(k)).setValue(doubleValue);
                            } // if
                            else {
                                return "Non-constant " + line +
                                        " encountered in initial state definition.";
                            } // else
                        } // try
                        catch (ClassCastException cce) {
                            return "Logical expression was not expected";
                        } // catch
                    } // if
                    else {
                        try {
                            double doubleValue = Double.parseDouble(line);
                            initstates.add(new Double(doubleValue));
                            informationString += (variableNames.get(k) + "=" +
                                    doubleValue + CR);
                        } // try
                        catch (NumberFormatException e) {
                            return "Non-constant " + line +
                                    " encountered in initial state definition.";
                        } // catch
                    }
                } // for
            } // if

            // does the code contain initial parameters
            if (containsParameters && containsParameterInit) {
                informationString += ("Reading initial parameters." + CR);

                for (int k = 0; k < getNumberOfParameters(); k++) {
                    // Read all initial parameter values.
                    // Read one line.
                    if (!source.hasMoreTokens()) {
                        return "No more tokens while reading initial parameter definitions.";
                    }

                    String sourceLine = source.nextToken();
                    informationString += ("Processing: " + sourceLine + CR);

                    StringTokenizer tokens = new StringTokenizer(sourceLine,
                            "=", true);

                    if (!tokens.hasMoreTokens()) {
                        return "No more tokens while reading initial parameter definitions.";
                    }

                    String lhs = tokens.nextToken();
                    int index;

                    while ((index = lhs.indexOf(" ")) >= 0) {
                        lhs = lhs.substring(0, index) +
                                lhs.substring(index + 1, lhs.length());
                    }

                    // Is it a proper left hand side?
                    if (!parameterNames.contains(lhs)) {
                        return "" + lhs + " is not a parameter name.";
                    }

                    // Does the initial state definition occur in the right place?
                    if (parameterNames.indexOf(lhs) != k) {
                        return "Initial parameter definitions in wrong order.";
                    }

                    // Read tokens until a "=" is encountered or we run out of tokens.
                    String nothing = "?";

                    if (tokens.hasMoreTokens()) {
                        while (!nothing.equals("=") && tokens.hasMoreTokens()) {
                            nothing = tokens.nextToken();
                        }
                    } else {
                        return "No more tokens where = was expected.";
                    }

                    // Did we find an "="?
                    if (!nothing.equals("=")) {
                        return "Token " + nothing + " where = was expected.";
                    }

                    String line = "";

                    while (tokens.hasMoreTokens())
                        line += tokens.nextToken();

                    if (line.equals("")) {
                        return "No right hand side while reading parameter value.";
                    } // if

                    // Remove trailing ";"s if present.
                    if (line.substring(line.length() - 1, line.length() - 1)
                            .equals(";")) {
                        line = line.substring(0, line.length() - 1);
                    } // if

                    if (line.equals("")) {
                        return "No right hand side while reading parameter value.";
                    } // if

                    if (0 == 0) {
                        ExpressionParser constantparser = new ExpressionParser();
                        constantparser.setConstantNames(constantNames);
                        constantparser.setConstants(constants);

                        try {
                            Expression cnst = (Expression) constantparser.parse(line);

                            if (cnst != null) {
                                double doubleValue = cnst.eval();
                                paramstates.add(new Double(doubleValue));
                                informationString += (parameterNames.get(k) +
                                        "=" + doubleValue + CR);
                                ((Parameter) parameters.get(k)).setValue(doubleValue);
                            } // if
                            else {
                                return "Non-constant " + line +
                                        " encountered in initial parameter definition.";
                            } // else
                        } // try
                        catch (ClassCastException cce) {
                            return "Logical expression was not expected";
                        } // catch
                    } // if
                    else {
                        try {
                            double doubleValue = Double.parseDouble(line);
                            paramstates.add(new Double(doubleValue));
                            informationString += (parameterNames.get(k) + "=" +
                                    doubleValue + CR);
                        } // try
                        catch (NumberFormatException e) {
                            return "Non-constant " + line +
                                    " encountered in initial parameter definition.";
                        } // catch
                    }
                } // for
            } // if

            // does the code contain initial state ranges
            if (containsStateRange) {
                informationString += ("Reading state ranges." + CR);

                for (int k = 0;
                     k < (getNumberOfVariables() - processNames.size());
                     k++) {
                    // Read all initial values.
                    // Read one line.
                    if (!source.hasMoreTokens()) {
                        return "No more tokens while reading initial state range definitions.";
                    }

                    String sourceLine = source.nextToken();
                    informationString += ("Processing: " + sourceLine + CR);

                    StringTokenizer tokens;

                    if (sourceLine.indexOf(":") >= 0) {
                        tokens = new StringTokenizer(sourceLine, "=:", true);
                    } else {
                        tokens = new StringTokenizer(sourceLine, "=,", true);
                    }

                    if (!tokens.hasMoreTokens()) {
                        return "No more tokens while reading initial state range definitions.";
                    }

                    String lhs = tokens.nextToken();
                    int index;

                    while ((index = lhs.indexOf(" ")) >= 0) {
                        lhs = lhs.substring(0, index) +
                                lhs.substring(index + 1, lhs.length());
                    }

                    // Is it a proper left hand side?
                    if (!variableNames.contains(lhs)) {
                        return "" + lhs + " is not a variable name.";
                    }

                    // Does the initial state definition occur in the right place?
                    if (variableNames.indexOf(lhs) != k) {
                        return "Initial state definitions in wrong order.";
                    }

                    // Read tokens until a "=" is encountered or we run out of tokens.
                    String nothing = "?";

                    if (tokens.hasMoreTokens()) {
                        while (!nothing.equals("=") && tokens.hasMoreTokens()) {
                            nothing = tokens.nextToken();
                        }
                    } else {
                        return "No more tokens where = was expected.";
                    }

                    // Did we find an "="?
                    if (!nothing.equals("=")) {
                        return "Token " + nothing + " where = was expected.";
                    }

                    String line = "";
                    String min = "";
                    String comma = "";
                    String max = "";

                    if (tokens.hasMoreTokens()) {
                        min = tokens.nextToken();
                    } else {
                        return "Encountered " + min +
                                " where minimum range value was expected.";
                    }

                    if (tokens.hasMoreTokens()) {
                        comma = tokens.nextToken();
                    } else {
                        return "Encountered " + comma +
                                " where separator , was expected.";
                    }

                    if (tokens.hasMoreTokens()) {
                        max = tokens.nextToken();
                    } else {
                        return "Encountered " + max +
                                " where maximum range value was expected.";
                    }

                    // Remove trailing ";"s if present.
                    if (max.substring(max.length() - 1, max.length() - 1)
                            .equals(";")) {
                        max = max.substring(0, max.length() - 1);
                    } // if

                    double doubleMinValue;
                    double doubleMaxValue;

                    if (0 == 0) {
                        ExpressionParser constantparser = new ExpressionParser();
                        constantparser.setConstantNames(constantNames);
                        constantparser.setConstants(constants);

                        Expression cnst;

                        try {
                            cnst = (Expression) constantparser.parse(min);

                            if (cnst != null) {
                                doubleMinValue = cnst.eval();
                                initrangemin.add(new Double(doubleMinValue));
                            } // if
                            else {
                                return "Non-constant " + line +
                                        " encountered in initial state range definition.";
                            } // else
                        } // try
                        catch (ClassCastException cce) {
                            return "Logical expression was not expected";
                        } // catch

                        try {
                            cnst = (Expression) constantparser.parse(max);

                            if (cnst != null) {
                                doubleMaxValue = cnst.eval();
                                initrangemax.add(new Double(doubleMaxValue));
                            } // if
                            else {
                                return "Non-constant " + line +
                                        " encountered in initial state range definition.";
                            } // else
                        } // try
                        catch (ClassCastException cce) {
                            return "Logical expression was not expected";
                        } // catch

                        informationString += ("" + doubleMinValue + "<=" +
                                variableNames.get(k) + "<=" + doubleMaxValue + CR);
                    } // if
                    else {
                        try {
                            doubleMinValue = Double.parseDouble(min);
                            initrangemin.add(new Double(doubleMinValue));
                            doubleMaxValue = Double.parseDouble(max);
                            initrangemax.add(new Double(doubleMaxValue));
                            informationString += ("" + doubleMinValue + "<=" +
                                    variableNames.get(k) + "<=" + doubleMaxValue + CR);
                        } // try
                        catch (NumberFormatException e) {
                            return "Non-constant encountered in initial state range definition.";
                        } // catch
                    }
                } // for
            } // if

            // does the code contain initial state ranges
            if (containsParameters && containsParameterRange) {
                informationString += ("Reading parameter ranges." + CR);

                for (int k = 0; k < getNumberOfParameters(); k++) {
                    // Read all initial values.
                    // Read one line.
                    if (!source.hasMoreTokens()) {
                        return "No more tokens while reading parameter range definitions.";
                    }

                    String sourceLine = source.nextToken();
                    informationString += ("Processing: " + sourceLine + CR);

                    StringTokenizer tokens;

                    if (sourceLine.indexOf(":") >= 0) {
                        tokens = new StringTokenizer(sourceLine, "=:", true);
                    } else {
                        tokens = new StringTokenizer(sourceLine, "=,", true);
                    }

                    if (!tokens.hasMoreTokens()) {
                        return "No more tokens while reading parameter range definitions.";
                    }

                    String lhs = tokens.nextToken();
                    int index;

                    while ((index = lhs.indexOf(" ")) >= 0) {
                        lhs = lhs.substring(0, index) +
                                lhs.substring(index + 1, lhs.length());
                    }

                    // Is it a proper left hand side?
                    if (!parameterNames.contains(lhs)) {
                        return "" + lhs + " is not a parameter name.";
                    }

                    // Does the initial state definition occur in the right place?
                    if (parameterNames.indexOf(lhs) != k) {
                        return "Initial parameter range definitions in wrong order.";
                    }

                    // Read tokens until a "=" is encountered or we run out of tokens.
                    String nothing = "?";

                    if (tokens.hasMoreTokens()) {
                        while (!nothing.equals("=") && tokens.hasMoreTokens()) {
                            nothing = tokens.nextToken();
                        }
                    } else {
                        return "No more tokens where = was expected.";
                    }

                    // Did we find an "="?
                    if (!nothing.equals("=")) {
                        return "Token " + nothing + " where = was expected.";
                    }

                    String line = "";
                    String min = "";
                    String comma = "";
                    String max = "";

                    if (tokens.hasMoreTokens()) {
                        min = tokens.nextToken();
                    } else {
                        return "Encountered " + min +
                                " where minimum range value was expected.";
                    }

                    if (tokens.hasMoreTokens()) {
                        comma = tokens.nextToken();
                    } else {
                        return "Encountered " + comma +
                                " where separator , was expected.";
                    } // else

                    if (tokens.hasMoreTokens()) {
                        max = tokens.nextToken();
                    } else {
                        return "Encountered " + max +
                                " where maximum range value was expected.";
                    } // else

                    // Remove trailing ";"s if present.
                    if (max.substring(max.length() - 1, max.length() - 1)
                            .equals(";")) {
                        max = max.substring(0, max.length() - 1);
                    } // if

                    if (0 == 0) {
                        ExpressionParser constantparser = new ExpressionParser();
                        double doubleMinValue;
                        double doubleMaxValue;
                        constantparser.setConstantNames(constantNames);
                        constantparser.setConstants(constants);

                        Expression cnst;

                        try {
                            cnst = (Expression) constantparser.parse(min);

                            if (cnst != null) {
                                doubleMinValue = cnst.eval();
                                paramrangemin.add(new Double(doubleMinValue));
                            } // if
                            else {
                                return "Non-constant " + line +
                                        " encountered in parameter range definition.";
                            } // else
                        } // try
                        catch (ClassCastException cce) {
                            return "Logical expression was not expected";
                        } // catch

                        try {
                            cnst = (Expression) constantparser.parse(max);

                            if (cnst != null) {
                                doubleMaxValue = cnst.eval();
                                paramrangemax.add(new Double(doubleMaxValue));
                            } // if
                            else {
                                return "Non-constant " + line +
                                        " encountered in parameter range definition.";
                            } // else
                        } // try
                        catch (ClassCastException cce) {
                            return "Logical expression was not expected";
                        } // catch

                        informationString += ("" + doubleMinValue + "<=" +
                                parameterNames.get(k) + "<=" + doubleMaxValue + CR);
                    } else {
                        try {
                            double doubleMinValue = Double.parseDouble(min);
                            paramrangemin.add(new Double(doubleMinValue));

                            double doubleMaxValue = Double.parseDouble(max);
                            paramrangemax.add(new Double(doubleMaxValue));
                            informationString += ("" + doubleMinValue + "<=" +
                                    parameterNames.get(k) + "<=" + doubleMaxValue + CR);
                        } catch (NumberFormatException e) {
                            return "Non-constant encountered in parameter range definition.";
                        }
                    }
                }
            }

            // At this point the source may not be exhausted but since
            // we have read everything we need to read we do not consider
            // this as a syntax error
            // Perform various checks on the result (some are superfluous no doubt).
            if (expressions.size() > getNumberOfVariables()) {
                return "Too many dynamic equations.";
            } // if

            if (expressions.size() < (getNumberOfVariables() -
                    processNames.size())) {
                return "Too few dynamic equations.";
            } // if

            if (isInvertible) {
                if (inverseexpressions.size() > getNumberOfVariables()) {
                    return "Too many inverse equations.";
                } // if

                if (inverseexpressions.size() < (getNumberOfVariables() -
                        processNames.size())) {
                    return "Too few inverse equations.";
                } // if
            } // if

            if (containsProcesses) {
                informationString += ("Analyzing diffusion processes." + CR);

                // compute the diffusion matrix and remove the stochastic terms from the dynamic equations
                diffusionMatrix = new Expression[expressions.size()][processNames.size()];

                for (int i = 0; i < expressions.size(); i++) {
                    Expression e = (Expression) expressions.get(i);
                    informationString += ("Analyzing " + e + CR);

                    for (int j = 0; j < processNames.size(); j++) {
                        diffusionMatrix[i][j] = new Constant(0);

                        String name = (String) processNames.get(j);

                        if (e.contains(new Variable(name))) {
                            List list = e.getElements();
                            boolean found = false;
                            Expression sub = null;
                            Expression factor = null;

                            for (int k = 0; (k < list.size()) && !found; k++) {
                                sub = (Expression) list.get(k);
                                factor = sub.factor(new Variable(name));
                                found = factor != null;
                            } // for

                            if (found) {
                                e = e.replace(sub, new Constant(0));
                                expressions.set(i, e);
                                diffusionMatrix[i][j] = factor;
                                informationString += ("diff(" + i + "," + j +
                                        ")=" + diffusionMatrix[i][j] + CR);
                            } // if
                            else {
                                informationString += ("diff(" + i + "," + j +
                                        ")=" + diffusionMatrix[i][j] + CR);
                            } // else
                        }
                    } // for
                } // for

                for (int i = 0; i < expressions.size(); i++) {
                    boolean found = false;

                    for (int j = 0; (j < processNames.size()) && !found; j++) {
                        found = ((Expression) expressions.get(i)).contains(new Variable(
                                (String) processNames.get(j)));
                    } // for

                    if (found) {
                        informationString += ("Illegal process found in " +
                                expressions.get(i));

                        return "Illegal process found in " +
                                expressions.get(i);
                    } // if

                    for (int level = 1; level <= optimizationLevel; level++) {
                        expressions.set(i,
                                ((Expression) expressions.get(i)).optimize());
                    } // for
                } // for

                for (int i = 0; i < expressions.size(); i++) {
                    for (int j = 0; j < processNames.size(); j++) {
                        for (int level = 1; level <= optimizationLevel;
                             level++) {
                            diffusionMatrix[i][j] = diffusionMatrix[i][j].optimize();
                        } // for
                    } // for
                } // for

                for (int j = 0; j < processNames.size(); j++) {
                    variables.remove(variables.size() - 1);
                    variableNames.remove(variableNames.size() - 1);
                } // for
            } // if

            /////////////////////////////////////////////////
            // At this point we are done with the parsing. //
            /////////////////////////////////////////////////
            if (optimizationLevel > 0) {
                informationString += ("Optimization level is " +
                        optimizationLevel + "." + CR);
            }

            if (containsAuxillaries) {
                // use the shallow definition of auxillaries
                //XXXAuxiliary.deepDefinition = false;
                if (optimizationLevel > 0) {
                    informationString += ("Auxiliaries after optimization:" +
                            CR);
                } else {
                    informationString += ("Auxiliaries (no optimization):" +
                            CR);
                }

                for (int i = 0; i < auxillaries.size(); i++) {
                    if (auxillaries.get(i) instanceof Expression) {
                        for (int level = 1; level <= optimizationLevel;
                             level++) {
                            Expression opt = ((Auxiliary) auxillaries.get(i)).getExpression()
                                    .optimize();
                            auxillaries.set(i,
                                    new Auxiliary((String) auxiliaryNames.get(i),
                                            opt));
                        } // for

                        informationString += ((String) auxiliaryNames.get(i) +
                                "=" + ((Auxiliary) auxillaries.get(i)).getExpression() +
                                CR);
                    } // if
                } // for
            } // if

            RHS = new Expression[expressions.size()];

            if (!isDifferentiable) { // system is not forced differentiable
                isDifferentiable = true;

                // do not use the definition of auxillaries
                //XXXAuxiliary.deepDefinition = false;
                informationString += ("Dynamic eqations:" + CR);

                for (int i = 0; i < expressions.size(); i++) {
                    RHS[i] = (Expression) expressions.get(i);

                    for (int level = 1; level <= optimizationLevel; level++) {
                        RHS[i] = RHS[i].optimize();
                    } // for

                    expressions.set(i, RHS[i]);
                    informationString += (variableNames.get(i) + "'=" + RHS[i] +
                            CR);
                    isDifferentiable = isDifferentiable &&
                            RHS[i].isDifferentiable();
                } // for

                // check whether some of the auxiliaries are nondifferentiable
                for (int i = 0; (i < auxillaries.size()) && isDifferentiable;
                     i++) {
                    if (auxillaries.get(i) instanceof Auxiliary) {
                        Auxiliary aux = (Auxiliary) auxillaries.get(i);
                        isDifferentiable = isDifferentiable &&
                                aux.getExpression().isDifferentiable();
                    } //if
                    else {
                        isDifferentiable = false;
                    } // else
                } // for
            } // if, ! isDifferentiable
            else { // system is forced differentiable

                // do not use the definition of auxillaries
                //XXXAuxiliary.deepDefinition = false;
                informationString += ("Dynamic eqations:" + CR);

                for (int i = 0; i < expressions.size(); i++) {
                    RHS[i] = (Expression) expressions.get(i);

                    for (int level = 1; level <= optimizationLevel; level++) {
                        RHS[i] = RHS[i].optimize();
                    } // for

                    expressions.set(i, RHS[i]);
                    informationString += (variableNames.get(i) + "'=" + RHS[i] +
                            CR);
                } // for
            } // else, ! isDifferentiable

            isParameterDifferentiable = isDifferentiable && containsParameters &&
                    (!isNondifferentiable);

            if (isDifferentiable) {
                if (isNondifferentiable) {
                    informationString += ("System is differentiable but derivatives will not be generated." +
                            CR);
                } else {
                    informationString += ("System is differentiable." + CR);
                }
            } else {
                informationString += ("System is not differentiable." + CR);
            }

            isDifferentiable = isDifferentiable && (!isNondifferentiable);

            if (isInvertible) {
                inverseRHS = new Expression[inverseexpressions.size()];

                for (int i = 0; i < inverseexpressions.size(); i++) {
                    inverseRHS[i] = (Expression) inverseexpressions.get(i);

                    for (int level = 1; level <= optimizationLevel; level++) {
                        inverseRHS[i] = inverseRHS[i].optimize();
                    } // for

                    inverseexpressions.set(i, inverseRHS[i]);
                    informationString += (variableNames.get(i) + "~=" +
                            inverseRHS[i] + CR);
                } // for
            } // if

            if (isDifferentiable) {
                informationString += ("Computing Jacobi matrix." + CR);
                JacobiMatrix = new Expression[expressions.size()][getNumberOfVariables()];

                for (int i = 0; i < expressions.size(); i++) {
                    if (!((Expression) expressions.get(i)).isDifferentiable()) {
                        return "System is declared differentiable but isn't.";
                    } // if

                    for (int j = 0; j < getNumberOfVariables(); j++) {
                        JacobiMatrix[i][j] = ((Expression) expressions.get(i)).diff((Variable) variables.get(
                                j));

                        for (int level = 1; level <= optimizationLevel;
                             level++) {
                            JacobiMatrix[i][j] = JacobiMatrix[i][j].optimize();
                        }

                        informationString += ("d" + variableNames.get(i) +
                                "'/d" + variableNames.get(j) + "=" +
                                JacobiMatrix[i][j] + CR);
                    } // for
                } // for

                if (containsParameters) {
                    informationString += ("System is parameterdifferentiable." +
                            CR);
                    informationString += ("Computing parameter Jacobi matrix." +
                            CR);
                    parameterJacobiMatrix = new Expression[expressions.size()][getNumberOfParameters()];

                    for (int i = 0; i < expressions.size(); i++) {
                        if (!((Expression) expressions.get(i)).isDifferentiable()) {
                            return "System is declared differentiable but isn't parameterdifferentiable.";
                        } // if

                        for (int j = 0; j < getNumberOfParameters(); j++) {
                            parameterJacobiMatrix[i][j] = ((Expression) expressions.get(i)).diff((Parameter) parameters.get(
                                    j));

                            for (int level = 1; level <= optimizationLevel;
                                 level++) {
                                parameterJacobiMatrix[i][j] = parameterJacobiMatrix[i][j].optimize();
                            }

                            informationString += ("d" + variableNames.get(i) +
                                    "'/d" + parameterNames.get(j) + "=" +
                                    parameterJacobiMatrix[i][j] + CR);
                        } // for
                    } // for
                } // if
            } // if

            // use the definition of auxillaries
            //XXXAuxiliary.deepDefinition = true;
            isExpandable = true;

            for (int i = 0; i < expressions.size(); i++) {
                isExpandable = isExpandable && RHS[i].isExpandable();
            } // for

            for (int i = 0; (i < auxillaries.size()) && isExpandable; i++) {
                if (auxillaries.get(i) instanceof Auxiliary) {
                    Auxiliary aux = (Auxiliary) auxillaries.get(i);
                    isExpandable = isExpandable &&
                            aux.getExpression().isExpandable();
                } // if
                else {
                    isExpandable = false;
                } // else
            } // for

            if (isExpandable) {
                informationString += ("System is expandable." + CR);
            } else {
                informationString += ("System is not expandable." + CR);
            }

            informationString += ("Compilation done." + CR);

            // Signal that the source code has been compiled.
            compiled = true;

            // Return null indicating that compilation was succesful
            return null;
        } // try
        catch (Exception e) { // Very serious situation. A bug is present in ExpressionParser!
            e.printStackTrace();

            return "An unhandled syntax error was detected.";
        }
    } // parseSource

    /**
     * The method checks whether the passed argument may be used as a new
     * identifier, and returns null if it may. If the name is not a legal
     * identifier or has been declared elsewhere, it returns an error message.
     *
     * @param name DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    private String checkName(String name) {
        if (!isLegalIdentifier(name)) {
            return "Name " + name + " is not a legal identifier.";
        } // if
        else if (reservedNamesVector.contains(name)) {
            return "Name " + name + " is a reserved name.";
        } // else if
        else if (processNames.contains(name)) {
            return "Name " + name + " is in use as a process name.";
        } // else if
        else if (variableNames.contains(name)) {
            return "Name " + name + " is in use as a variable name.";
        } // else if
        else if (parameterNames.contains(name)) {
            return "Name " + name + " is in use as a parameter name.";
        } // else if
        else if (constantNames.contains(name)) {
            return "Name " + name + " is in use as a constant name.";
        } // else if
        else if (auxiliaryNames.contains(name)) {
            return "Name " + name + " is in use as an auxiliary name.";
        } // else if
        else if (functionNames.contains(name)) {
            return "Name " + name + " is in use as a function name.";
        } // else if
        else if (eventNames.contains(name)) {
            return "Name " + name + " is in use as an event name.";
        } // else if
        else {
            return null;
        } // else
    } // checkName

    /**
     * Generates Java source code for computing the right hand side. The source
     * code is placed in the <B>private</B><B>String</B> which can be
     * retrieved by the method <I>getJavaSource</I>. If the String argument to
     * the method differs from the empty String (""), say, is "w.ode" the
     * generated source will be saved in a file "w.java" if possible.
     *
     * @param fileName DOCUMENT ME!
     */
    public void generateJavaSource(String fileName) {
        // Attempt to generate Java source code.
        if (compiled) {
            String className = (new File(fileName)).getName();

            // Get the current time and date
            Date now = new Date();
            DateFormat format = DateFormat.getDateInstance();
            DateFormat longTimestamp = DateFormat.getDateTimeInstance(DateFormat.FULL,
                    DateFormat.FULL);

            // Set javaSource equal to the source code.
            javaSource = "import org.jscience.mathematics.analysis.*;" + CR +
                    "import org.jscience.mathematics.analysis.noise.*;" + CR +
                    "import java.util.Random;" + CR +
                    "import java.io.Serializable;" + CR +
                    "/** This Java source code has been automatically generated by <I>ExpressionParser</I>." +
                    CR + " * Generated by " + System.getProperty("user.name") +
                    " from the source file " + className + CR +
                    " * Date of generation is " + longTimestamp.format(new Date()) +
                    "." + CR +
                    " * Please exercise great care in modifying the source." + CR +
                    " * @author <I>ExpressionParser</I>." + CR + " * @version 1.0" +
                    CR + " **/" + CR +
                    ((isStrict) ? ("public strictfp class ") : ("public class ")) +
                    className.substring(0, className.length() - 4) +
                    " implements StateConscious, StateNamed";

            if (isTimeDiscrete) {
                javaSource += ", TimeDiscrete";
            }

            if (isTimeContinuous) {
                javaSource += ", TimeContinuous";
            }

            if (isAutonomous) {
                javaSource += ", Autonomous";
            }

            if (!isAutonomous) {
                javaSource += ", NonAutonomous";
            }

            if (containsPeriod) {
                javaSource += ", PeriodicallyForced";
            }

            if (containsParameters) {
                javaSource += ", ParameterDependent, ParameterNamed";
            }

            if (containsStateInit) {
                javaSource += ", MutableStateInitializer";
            }

            if (containsParameters && containsParameterInit) {
                javaSource += ", MutableParameterInitializer";
            }

            if (containsStateRange) {
                javaSource += ", StateRangeInitializer";
            }

            if (containsParameters && containsParameterRange) {
                javaSource += ", ParameterRangeInitializer";
            }

            if (isInvertible) {
                if (isAutonomous) {
                    javaSource += ", AutonomousInvertible";
                } else {
                    javaSource += ", NonAutonomousInvertible";
                }
            }

            if (isDifferentiable) {
                if (isAutonomous) {
                    javaSource += ", AutonomousDifferentiable";
                } else {
                    javaSource += ", NonAutonomousDifferentiable";
                }
            }

            if (containsParameters && isDifferentiable) {
                if (isAutonomous) {
                    javaSource += ", AutonomousParameterDifferentiable";
                } else {
                    javaSource += ", NonAutonomousParameterDifferentiable";
                }
            }

            if (isTimeContinuous && isExpandable) {
                javaSource += ", TaylorMap";
            }

            if (containsAuxillaries) {
                javaSource += ", AuxiliaryDependent, AuxiliaryNamed";
            }

            if (isPoincareSection) { // && isAutonomous )
                javaSource += ", PoincareSection";
            }

            if (containsProcesses && isAutonomous) {
                javaSource += ", AutonomousDiffusive";
            }

            if (containsProcesses && !isAutonomous) {
                javaSource += ", NonAutonomousDiffusive";
            }

            javaSource += ", Describable";
            javaSource += ", Serializable";
            javaSource += (" {" + CR + CR);
            javaSource += ("  /**" + CR);
            javaSource += ("   * Random number generator." + CR);
            javaSource += ("   **/" + CR);
            javaSource += ("  private Random random = new Random( 1 );" + CR +
                    CR);
            javaSource += ("  /**" + CR);
            javaSource += ("   * Declaration of the dynamic variables of the model." +
                    CR);
            javaSource += ("   **/" + CR);
            javaSource += ("  " + accessLevel + " double ");

            for (int i = 0; i < variables.size(); i++) {
                javaSource += variables.get(i);

                if (i < (variables.size() - 1)) {
                    javaSource += ", ";
                }
            }

            javaSource += (";" + CR + CR);

            javaSource += ("  /**" + CR);
            javaSource += ("   * Method dimension returns the number of state variables in the model." +
                    CR);
            javaSource += ("   **/" + CR);
            javaSource += ("  public int dimension() {" + CR);
            javaSource += ("    return " + variables.size() + ";" + CR);
            javaSource += ("  }" + CR + CR);

            javaSource += ("  /**" + CR);
            javaSource += ("   * The method sets the state of the system as required by StateConscious." +
                    CR);
            javaSource += ("   **/" + CR);
            javaSource += ("  public void setState( VectorType _x ) {" + CR);

            for (int i = 1; i <= variables.size(); i++) {
                javaSource += ("    " + variables.get(i - 1) + " = _x.get( " +
                        i + " );" + CR);
            } // for

            javaSource += ("  }" + CR + CR);
            javaSource += ("  /**" + CR);
            javaSource += ("   * The method returns the state of the system as required by StateConscious." +
                    CR);
            javaSource += ("   **/" + CR);
            javaSource += ("  public VectorType getState() {" + CR);
            javaSource += "    return new org.jscience.mathematics.analysis.Vector( new double[] { ";

            for (int i = 0; i < variables.size(); i++) {
                if (i < (variables.size() - 1)) {
                    javaSource += (variables.get(i) + ", ");
                } else {
                    javaSource += variables.get(i);
                }
            } // for

            javaSource += (" } );" + CR);
            javaSource += ("  }" + CR + CR);

            javaSource += ("  /**" + CR);
            javaSource += ("   * The method returns the index of the named variable." +
                    CR);
            javaSource += ("   * If no such variable exists zero is returned." +
                    CR);
            javaSource += ("   * @param _name is the name of the variable of interest." +
                    CR);
            javaSource += ("   * @return the index of the variable named in the argument list," +
                    CR);
            javaSource += ("   * and zero if no such variable exists." + CR);
            javaSource += ("   **/" + CR);
            javaSource += ("  public int getIndexOf( String _name ) {" + CR);
            javaSource += ("    for (int _i = 1; _i <= " + variables.size() +
                    "; _i++)" + CR);
            javaSource += ("      if ( _name.equals( nameOfState( _i ) ) ) return _i;" +
                    CR);
            javaSource += ("    return 0;" + CR);
            javaSource += ("  }" + CR + CR);

            if (containsParameters) {
                javaSource += ("  /**" + CR);
                javaSource += ("   * Declaration of the model parameters." +
                        CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  " + accessLevel + " double");

                for (int i = 0; i < parameters.size(); i++) {
                    javaSource += (" " + parameters.get(i));

                    if (containsParameterInit) {
                        javaSource += (" = " + paramstates.get(i));
                    }

                    if (i < (parameters.size() - 1)) {
                        javaSource += ",";
                    }
                }

                javaSource += (";" + CR + CR);

                javaSource += ("  /**" + CR);
                javaSource += ("   * Method parameterDimension returns the number of parameters in the model." +
                        CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public int parameterDimension() { return " +
                        parameters.size() + "; }" + CR + CR);

                javaSource += ("  /**" + CR);
                javaSource += ("   * The method sets the <CODE>i</CODE>th parameter to value." +
                        CR);
                javaSource += ("   * @param _i     The number of the parameter to change to value." +
                        CR);
                javaSource += ("   * @param _value The value to be assigned to the parameter." +
                        CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public void setParameter( int _i, double _value ) {" +
                        CR);
                javaSource += ("    switch( _i ) {" + CR);

                for (int i = 0; i < parameters.size(); i++) {
                    javaSource += ("      case " + (i + 1) + ": " +
                            parameters.get(i) + " = _value; break;" + CR);
                }

                javaSource += ("    }" + CR);
                javaSource += ("  }" + CR + CR);

                javaSource += ("  /**" + CR);
                javaSource += ("   * Returns the value of the <CODE>i</CODE>th parameter." +
                        CR);
                javaSource += ("   * @param _i The parameter number." + CR);
                javaSource += ("   * @return The value of the <CODE>i</CODE>th parameter." +
                        CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public double getParameter( int _i ) {" + CR);
                javaSource += ("    switch( _i ) {" + CR);

                for (int i = 0; i < parameters.size(); i++) {
                    javaSource += ("      case " + (i + 1) + ": return " +
                            parameters.get(i) + ";" + CR);
                }

                javaSource += ("    }" + CR);
                javaSource += ("    // the next line should never be executed" +
                        CR);
                javaSource += ("    return 0;" + CR);
                javaSource += ("  }" + CR + CR);

                javaSource += ("  /**" + CR);
                javaSource += ("   * The method sets the named parameter to value." +
                        CR);
                javaSource += ("   * The method does nothing if no such parameter exists." +
                        CR);
                javaSource += ("   * @param name  is the name of the parameter to set." +
                        CR);
                javaSource += ("   * @param value is the value to be assigned to the named parameter." +
                        CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public void setNamedParameter( String _name, double _value ) {" +
                        CR);

                for (int i = 0; i < parameters.size(); i++) {
                    javaSource += ("    if ( _name.equals( \"" +
                            parameters.get(i) + "\" ) ) " + parameters.get(i) +
                            " = _value;" + CR);
                }

                javaSource += ("  }" + CR + CR);

                javaSource += ("  /**" + CR);
                javaSource += ("   * The method returns the value of the named parameter." +
                        CR);
                javaSource += ("   * The method returns 0 when no such parameter exists." +
                        CR);
                javaSource += ("   * @param _name is the name of the parameter to return." +
                        CR);
                javaSource += ("   * @return The value of the named parameter or zero if no such parameter exists." +
                        CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public double getNamedParameter( String _name ) {" +
                        CR);

                for (int i = 0; i < parameters.size(); i++) {
                    javaSource += ("    if ( _name.equals( \"" +
                            parameters.get(i) + "\" ) ) return " + parameters.get(i) +
                            ";" + CR);
                } // for

                javaSource += ("    return 0;" + CR);
                javaSource += ("  }" + CR + CR);
            } // if

            if (containsProcesses) {
                javaSource += ("  /**" + CR);
                javaSource += ("   * Returns the number of stochastic processes." +
                        CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public int dim() {" + CR);
                javaSource += ("    return " + processNames.size() + ";" + CR);
                javaSource += ("  }" + CR + CR);

                javaSource += ("  /**" + CR);
                javaSource += ("   * The method defines the diffusion matrix of the stochastic differential equations." +
                        CR);

                if (!isAutonomous) {
                    javaSource += ("   * @param " +
                            ((isTimeDiscrete) ? (ExpressionsStandardNames.DISCRETE_NAME)
                                    : (ExpressionsStandardNames.TIME_CONTINUOUS_NAME)) +
                            " Represents the time." + CR);
                }

                javaSource += ("   * @param input VectorType containing the state." +
                        CR +
                        "   * @return MatrixType containing the diffusion matrix of the stochastic differential equations." +
                        CR);
                javaSource += ("   **/" + CR);

                if (isAutonomous) {
                    javaSource += ("  public MatrixType getDiffusionMatrix( VectorType _input ) {" +
                            CR);
                } else {
                    javaSource += ("  public MatrixType getDiffusionMatrix( VectorType _input, double " +
                            ((isTimeDiscrete) ? (ExpressionsStandardNames.DISCRETE_NAME)
                                    : (ExpressionsStandardNames.TIME_CONTINUOUS_NAME)) +
                            " ) {" + CR);
                }

                if (containsAuxillaries) {
                    if (isAutonomous) {
                        javaSource += ("    computeAuxiliaries( _input, 0 );" +
                                CR);
                    } else {
                        javaSource += ("    computeAuxiliaries( _input, " +
                                ((isTimeDiscrete)
                                        ? (ExpressionsStandardNames.DISCRETE_NAME)
                                        : (ExpressionsStandardNames.TIME_CONTINUOUS_NAME)) +
                                " );" + CR);
                    }
                } else {
                    for (int i = 0; i < variables.size(); i++)
                        javaSource += ("    " + variables.get(i) +
                                " = _input.get( " + (i + 1) + " );" + CR);
                }

                javaSource += ("    MatrixType _J = _input.getMatrixType().getMatrixType( " +
                        diffusionMatrix.length + ", " + diffusionMatrix[0].length +
                        " );" + CR);

                for (int i = 0; i < diffusionMatrix.length; i++) {
                    for (int j = 0; j < diffusionMatrix[0].length; j++) {
                        javaSource += ("    _J.set( " + (i + 1) + ", " +
                                (j + 1) + ", " + diffusionMatrix[i][j].toJava() +
                                " );" + CR);
                    }
                }

                javaSource += ("    return _J;" + CR);
                javaSource += ("  }" + CR + CR);
            } // if

            if (containsAuxillaries) {
                javaSource += ("  /**" + CR);
                javaSource += ("   * Declaration of auxiliaries in model." +
                        CR);
                javaSource += ("   **/" + CR);

                for (int i = 0; i < auxillaries.size(); i++) {
                    if (auxillaries.get(i) instanceof Expression) {
                        javaSource += ("  " + accessLevel + " double " +
                                auxiliaryNames.get(i) + ";" + CR);
                    } // if
                    else {
                        javaSource += ("  " + accessLevel + " boolean " +
                                auxiliaryNames.get(i) + ";" + CR);
                    } // else
                } // for

                javaSource += (";" + CR + CR);
                javaSource += ("  /**" + CR);
                javaSource += ("   * The method returns the number of auxiliaries in the model." +
                        CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public int auxiliaryDimension() {" + CR);
                javaSource += ("    return " + auxillaries.size() + ";" + CR);
                javaSource += ("  }" + CR + CR);
                javaSource += ("  /**" + CR);
                javaSource += ("   * Returns the value of the <CODE>i</CODE>th parameter." +
                        CR);
                javaSource += ("   * @param _i The parameter number." + CR);
                javaSource += ("   * @return The value of the <CODE>i</CODE>th parameter." +
                        CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public double getAuxiliary( int _i ) {" + CR);
                javaSource += ("    switch( _i ) {" + CR);

                for (int i = 0; i < auxillaries.size(); i++) {
                    if (auxillaries.get(i) instanceof Expression) {
                        javaSource += ("      case " + (i + 1) + ": return " +
                                auxiliaryNames.get(i) + ";" + CR);
                    } // if
                    else {
                        javaSource += ("      case " + (i + 1) + ": return (" +
                                auxiliaryNames.get(i) + ")?(1):(0);" + CR);
                    } // else
                }

                javaSource += ("    }" + CR);
                javaSource += ("    // the next line should never be executed" +
                        CR);
                javaSource += ("    return 0;" + CR);
                javaSource += ("  }" + CR + CR);

                javaSource += ("  /**" + CR);
                javaSource += ("   * The method returns the value of the named auxiliary." +
                        CR);
                javaSource += ("   * The method returns 0 when no such auxiliary exists." +
                        CR);
                javaSource += ("   * @param _name is the name of the auxiliary to return." +
                        CR);
                javaSource += ("   * @return The value of the named auxiliary or zero if no such auxiliary exists." +
                        CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public double getNamedAuxiliary( String _name ) {" +
                        CR);

                for (int i = 0; i < auxillaries.size(); i++) {
                    if (auxillaries.get(i) instanceof Expression) {
                        javaSource += ("    if ( _name.equals( \"" +
                                auxiliaryNames.get(i) + "\" ) ) return " +
                                auxiliaryNames.get(i) + ";" + CR);
                    } //
                    else {
                        javaSource += ("    if ( _name.equals( \"" +
                                auxiliaryNames.get(i) + "\" ) ) return (" +
                                auxiliaryNames.get(i) + ")?(1):(0);" + CR);
                    } // else
                }

                javaSource += ("    return 0;" + CR);
                javaSource += ("  }" + CR + CR);
                javaSource += ("  /**" + CR);
                javaSource += ("   * Method computeAuxiliaries computes the values of the auxiliaries based on the" +
                        CR);
                javaSource += ("   * values of the dynamic variables and parameters, as well as time." +
                        CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public void computeAuxiliaries( VectorType _input, double " +
                        ((isTimeDiscrete) ? (ExpressionsStandardNames.DISCRETE_NAME)
                                : (ExpressionsStandardNames.TIME_CONTINUOUS_NAME)) +
                        " ) {" + CR);

                for (int i = 0; i < variables.size(); i++)
                    javaSource += ("    " + variables.get(i) +
                            " = _input.get( " + (i + 1) + " );" + CR);

                // use the shallow definition of auxillaries
                //XXXAuxiliary.deepDefinition = false;
                for (int i = 0; i < auxillaries.size(); i++) {
                    if (auxillaries.get(i) instanceof Expression) {
                        javaSource += ("    " + auxiliaryNames.get(i) + " = " +
                                ((Auxiliary) auxillaries.get(i)).getExpression().toJava() +
                                ";" + CR);
                    } // if
                    else {
                        javaSource += ("    " + auxiliaryNames.get(i) + " = " +
                                ((Logical) auxillaries.get(i)).toJava() + ";" + CR);
                    } // else
                }

                javaSource += ("  }" + CR + CR);
            }

            if (containsPeriod) {
                javaSource += ("  /**" + CR);
                javaSource += ("   * Returns the period of the forcing for periodically forced systems." +
                        CR);
                javaSource += ("   * @return The period of the forcing." + CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public double getPeriod() {" + CR);
                javaSource += "    return ";

                for (int k = 0; k < auxillaries.size(); k++)
                    if (((String) auxiliaryNames.get(k)).equals("period")) {
                        javaSource += ((Auxiliary) auxillaries.get(k)).getExpression()
                                .toJava();
                    }

                javaSource += (";" + CR);
                javaSource += ("  }" + CR + CR);
            }

            javaSource += ("  /**" + CR);
            javaSource += ("   * The method defines the dynamic equations for the model." +
                    CR);

            if (!isAutonomous) {
                javaSource += ("   * @param " +
                        ((isTimeDiscrete) ? (ExpressionsStandardNames.DISCRETE_NAME)
                                : (ExpressionsStandardNames.TIME_CONTINUOUS_NAME)) +
                        " Represents the time." + CR);
            }

            javaSource += ("   * @param _input VectorType containing the state." +
                    CR +
                    "   * @return VectorType containing the right hand side of the dynamic equations." +
                    CR);
            javaSource += ("   **/" + CR);

            if (isAutonomous) {
                javaSource += ("  public VectorType eval( VectorType _input ) {" +
                        CR);
            } else {
                javaSource += ("  public VectorType eval( VectorType _input, double " +
                        ((isTimeDiscrete) ? (ExpressionsStandardNames.DISCRETE_NAME)
                                : (ExpressionsStandardNames.TIME_CONTINUOUS_NAME)) +
                        " ) {" + CR);
            }

            if (containsAuxillaries) {
                if (isAutonomous) {
                    javaSource += ("    computeAuxiliaries( _input, 0 );" + CR);
                } else {
                    javaSource += ("    computeAuxiliaries( _input, " +
                            ((isTimeDiscrete) ? (ExpressionsStandardNames.DISCRETE_NAME)
                                    : (ExpressionsStandardNames.TIME_CONTINUOUS_NAME)) +
                            " );" + CR);
                }
            } else {
                for (int i = 0; i < variables.size(); i++)
                    javaSource += ("    " + variables.get(i) +
                            " = _input.get( " + (i + 1) + " );" + CR);
            }

            javaSource += ("    VectorType _output = _input.getVectorType( " +
                    variables.size() + " );" + CR);

            // do not use the deep definition of auxillaries
            //XXXAuxiliary.deepDefinition = false;
            for (int i = 0; i < expressions.size(); i++) {
                javaSource += ("    _output.set(" + (i + 1) + "," +
                        RHS[i].toJava() + ");" + CR);
            }

            javaSource += ("    return _output;" + CR);
            javaSource += ("  }" + CR + CR);

            if (isInvertible) {
                javaSource += ("  /**" + CR);

                if (!isAutonomous) {
                    javaSource += ("   * @param " +
                            ((isTimeDiscrete) ? (ExpressionsStandardNames.DISCRETE_NAME)
                                    : (ExpressionsStandardNames.TIME_CONTINUOUS_NAME)) +
                            " Represents the time." + CR);
                }

                javaSource += ("   * @param _input VectorType containing the state." +
                        CR +
                        "   * @return VectorType containing the right hand side of the dynamic equations." +
                        CR);
                javaSource += ("   **/" + CR);

                if (isAutonomous) {
                    javaSource += ("  public VectorType evalInverse( VectorType _input ) {" +
                            CR);
                } else {
                    javaSource += ("  public VectorType evalInverse( VectorType _input, double " +
                            ((isTimeDiscrete) ? (ExpressionsStandardNames.DISCRETE_NAME)
                                    : (ExpressionsStandardNames.TIME_CONTINUOUS_NAME)) +
                            " ) {" + CR);
                }

                if (containsAuxillaries) {
                    if (isAutonomous) {
                        javaSource += ("    computeAuxiliaries( _input, 0 );" +
                                CR);
                    } else {
                        javaSource += ("    computeAuxiliaries( _input, " +
                                ((isTimeDiscrete)
                                        ? (ExpressionsStandardNames.DISCRETE_NAME)
                                        : (ExpressionsStandardNames.TIME_CONTINUOUS_NAME)) +
                                " );" + CR);
                    }
                } else {
                    for (int i = 0; i < variables.size(); i++)
                        javaSource += ("    " + variables.get(i) +
                                " = _input.get( " + (i + 1) + " );" + CR);
                }

                javaSource += ("    VectorType _output = _input.getVectorType( " +
                        variables.size() + " );" + CR);

                for (int i = 0; i < inverseexpressions.size(); i++) {
                    javaSource += ("    _output.set(" + (i + 1) + "," +
                            inverseRHS[i].toJava() + ");" + CR);
                }

                javaSource += ("    return _output;" + CR);
                javaSource += ("  }" + CR + CR);
            }

            //
            if (isDifferentiable) {
                javaSource += ("  /**" + CR);
                javaSource += ("   * The method defines the Jacobi matrix of the dynamic equations." +
                        CR);

                if (!isAutonomous) {
                    javaSource += ("   * @param " +
                            ((isTimeDiscrete) ? (ExpressionsStandardNames.DISCRETE_NAME)
                                    : (ExpressionsStandardNames.TIME_CONTINUOUS_NAME)) +
                            " Represents the time." + CR);
                }

                javaSource += ("   * @param input VectorType containing the state." +
                        CR +
                        "   * @return MatrixType containing the Jacobi matrix of the dynamic equations." +
                        CR);
                javaSource += ("   **/" + CR);

                if (isAutonomous) {
                    javaSource += ("  public MatrixType JacobiMatrix( VectorType _input ) {" +
                            CR);
                } else {
                    javaSource += ("  public MatrixType JacobiMatrix( VectorType _input, double " +
                            ((isTimeDiscrete) ? (ExpressionsStandardNames.DISCRETE_NAME)
                                    : (ExpressionsStandardNames.TIME_CONTINUOUS_NAME)) +
                            " ) {" + CR);
                }

                if (containsAuxillaries) {
                    if (isAutonomous) {
                        javaSource += ("    computeAuxiliaries( _input, 0 );" +
                                CR);
                    } else {
                        javaSource += ("    computeAuxiliaries( _input, " +
                                ((isTimeDiscrete)
                                        ? (ExpressionsStandardNames.DISCRETE_NAME)
                                        : (ExpressionsStandardNames.TIME_CONTINUOUS_NAME)) +
                                " );" + CR);
                    }
                } else {
                    for (int i = 0; i < variables.size(); i++)
                        javaSource += ("    " + variables.get(i) +
                                " = _input.get( " + (i + 1) + " );" + CR);
                }

                javaSource += ("    MatrixType _J = _input.getMatrixType().getMatrixType( " +
                        variables.size() + ", " + variables.size() + " );" + CR);

                for (int i = 0; i < variables.size(); i++) {
                    for (int j = 0; j < variables.size(); j++) {
                        javaSource += ("    _J.set( " + (i + 1) + ", " +
                                (j + 1) + ", " + JacobiMatrix[i][j].toJava() + " );" +
                                CR);
                    }
                }

                javaSource += ("    return _J;" + CR);
                javaSource += ("  }" + CR + CR);

                if (containsParameters) {
                    javaSource += ("  /**" + CR);
                    javaSource += ("   * The method defines the parameter Jacobi matrix of the dynamic equations." +
                            CR);

                    if (!isAutonomous) {
                        javaSource += ("   * @param " +
                                ((isTimeDiscrete)
                                        ? (ExpressionsStandardNames.DISCRETE_NAME)
                                        : (ExpressionsStandardNames.TIME_CONTINUOUS_NAME)) +
                                " Represents the time" + CR);
                    }

                    javaSource += ("   * @param _input VectorType containing the state" +
                            CR +
                            "   * @return MatrixType containing the parameter Jacobi matrix of the dynamic equations" +
                            CR);
                    javaSource += ("   **/" + CR);

                    if (isAutonomous) {
                        javaSource += ("  public MatrixType parameterJacobiMatrix( VectorType _input ) {" +
                                CR);
                    } else {
                        javaSource += ("  public MatrixType parameterJacobiMatrix( VectorType _input, double " +
                                ((isTimeDiscrete)
                                        ? (ExpressionsStandardNames.DISCRETE_NAME)
                                        : (ExpressionsStandardNames.TIME_CONTINUOUS_NAME)) +
                                " ) {" + CR);
                    }

                    if (containsAuxillaries) {
                        if (isAutonomous) {
                            javaSource += ("    computeAuxiliaries( _input, 0 );" +
                                    CR);
                        } else {
                            javaSource += ("    computeAuxiliaries( _input, " +
                                    ((isTimeDiscrete)
                                            ? (ExpressionsStandardNames.DISCRETE_NAME)
                                            : (ExpressionsStandardNames.TIME_CONTINUOUS_NAME)) +
                                    " );" + CR);
                        }
                    } else {
                        for (int i = 0; i < variables.size(); i++)
                            javaSource += ("    " + variables.get(i) +
                                    " = _input.get( " + (i + 1) + " );" + CR);
                    }

                    javaSource += ("    MatrixType _J = _input.getMatrixType().getMatrixType( " +
                            variables.size() + ", " + parameters.size() + " );" + CR);

                    for (int i = 0; i < variables.size(); i++) {
                        for (int j = 0; j < parameters.size(); j++) {
                            javaSource += ("    _J.set( " + (i + 1) + ", " +
                                    (j + 1) + ", " +
                                    parameterJacobiMatrix[i][j].toJava() + " );" + CR);
                        }
                    }

                    javaSource += ("    return _J;" + CR);
                    javaSource += ("  }" + CR + CR);
                }
            }

            // Change variable names and parameter names for code generation.
            for (int i = 0; i < variables.size(); i++) {
                ((Variable) variables.get(i)).setName("_input.get(" + (i + 1) +
                        ")");
            }

            // Reset variable names and parameter names.
            for (int i = 0; i < variables.size(); i++) {
                ((Variable) variables.get(i)).setName((String) variableNames.get(
                        i));
            }

            // We have real variable names and can define them in the Java source
            javaSource += "  private final String[] _variableNames = {";

            for (int i = 0; i < variables.size(); i++) {
                javaSource += (" \"" + variables.get(i) + "\"");

                if (i < (variables.size() - 1)) {
                    javaSource += ",";
                }
            }

            javaSource += (" };" + CR + CR);
            javaSource += ("  /**" + CR);
            javaSource += ("   * Returns the name of the <CODE>_i</CODE>th variable." +
                    CR);
            javaSource += ("   **/" + CR);
            javaSource += ("  public String nameOfState( int _i ) {" + CR);
            javaSource += ("    return _variableNames[ _i - 1 ];" + CR);
            javaSource += ("  }" + CR + CR);

            // We have real parameter names and can define them in the Java source
            if (containsParameters) {
                javaSource += "  private final String[] _parameterNames = {";

                for (int i = 0; i < parameters.size(); i++) {
                    javaSource += (" \"" + parameters.get(i) + "\"");

                    if (i < (parameters.size() - 1)) {
                        javaSource += ",";
                    }
                }

                javaSource += (" };" + CR + CR);
                javaSource += ("  /**" + CR);
                javaSource += ("   * Returns the name of the <CODE>_i</CODE>th parameter." +
                        CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public String nameOfParameter( int _i ) {" +
                        CR);
                javaSource += ("    return _parameterNames[ _i - 1 ];" + CR);
                javaSource += ("  }" + CR + CR);
            }

            if (containsAuxillaries) {
                javaSource += "  private final String[] _auxiliaryNames = {";

                for (int i = 0; i < auxillaries.size(); i++) {
                    javaSource += (" \"" + auxiliaryNames.get(i) + "\"");

                    if (i < (auxillaries.size() - 1)) {
                        javaSource += ",";
                    }
                }

                javaSource += (" };" + CR + CR);
                javaSource += ("  /**" + CR);
                javaSource += ("   * Returns the name of the <CODE>_i</CODE>th auxiliary." +
                        CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public String nameOfAuxiliary( int _i ) {" +
                        CR);
                javaSource += ("    return _auxiliaryNames[ _i - 1 ];" + CR);
                javaSource += ("  }" + CR + CR);
            }

            //
            if (containsStateInit) {
                javaSource += "  private double[] _state = { ";

                for (int i = 0; i < getNumberOfVariables(); i++) {
                    javaSource += initstates.get(i);

                    if (i < (getNumberOfVariables() - 1)) {
                        javaSource += ", ";
                    }
                }

                javaSource += (" };" + CR + CR);
                javaSource += ("  /**" + CR);
                javaSource += ("   * Returns the initial state." + CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public VectorType getInitialState() {" + CR);
                javaSource += ("    return Settings.Vector.getVectorType( _state );" +
                        CR);
                javaSource += ("  }" + CR + CR);
                javaSource += ("  /**" + CR);
                javaSource += ("   * Sets the initial state." + CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public void setInitialState( VectorType x ) {" +
                        CR);
                javaSource += ("    double[] array = x.getArray();" + CR);
                javaSource += ("    if ( array.length == _state.length ) {" +
                        CR);
                javaSource += ("       for (int i = 0; i < array.length; i++)" +
                        CR);
                javaSource += ("           _state[ i ] = array[ i ];" + CR);
                javaSource += ("    } // if" + CR);
                javaSource += ("  }" + CR + CR);
                javaSource += ("  { setState( getInitialState() ); }" + CR +
                        CR);
            } // if

            //
            if (containsParameters && containsParameterInit) {
                javaSource += "  private double[] _parameters = { ";

                for (int i = 0; i < getNumberOfParameters(); i++) {
                    javaSource += paramstates.get(i);

                    if (i < (getNumberOfParameters() - 1)) {
                        javaSource += ", ";
                    }
                }

                javaSource += (" };" + CR + CR);
                javaSource += ("  /**" + CR);
                javaSource += ("   * Returns the initial parameters." + CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public VectorType getInitialParameters() {" +
                        CR);
                javaSource += ("    return Settings.Vector.getVectorType( _parameters );" +
                        CR);
                javaSource += ("  }" + CR + CR);
                javaSource += ("  /**" + CR);
                javaSource += ("   * Sets the initial parameters." + CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public void setInitialParameters( VectorType x ) {" +
                        CR);
                javaSource += ("    double[] array = x.getArray();" + CR);
                javaSource += ("    if ( array.length == _parameters.length ) {" +
                        CR);
                javaSource += ("       for (int i = 0; i < array.length; i++)" +
                        CR);
                javaSource += ("           _parameters[ i ] = array[ i ];" +
                        CR);
                javaSource += ("    } // if" + CR);
                javaSource += ("  }" + CR + CR);
            }

            //
            if (containsStateRange) {
                javaSource += "  private final double[] _minstate = { ";

                for (int i = 0; i < getNumberOfVariables(); i++) {
                    javaSource += initrangemin.get(i);

                    if (i < (getNumberOfVariables() - 1)) {
                        javaSource += ", ";
                    }
                }

                javaSource += (" };" + CR + CR);
                javaSource += ("  /**" + CR);
                javaSource += ("   * Returns the minimum initial state." + CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public VectorType getStateMinimum() {" + CR);
                javaSource += ("    return Settings.Vector.getVectorType( _minstate );" +
                        CR);
                javaSource += ("  }" + CR + CR);
                javaSource += "  private final double[] _maxstate = { ";

                for (int i = 0; i < getNumberOfVariables(); i++) {
                    javaSource += initrangemax.get(i);

                    if (i < (getNumberOfVariables() - 1)) {
                        javaSource += ", ";
                    }
                }

                javaSource += (" };" + CR + CR);
                javaSource += ("  /**" + CR);
                javaSource += ("   * Returns the maximum initial state." + CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public VectorType getStateMaximum() {" + CR);
                javaSource += ("    return Settings.Vector.getVectorType( _maxstate );" +
                        CR);
                javaSource += ("  }" + CR + CR);
            }

            //
            if (containsParameters && containsParameterRange) {
                javaSource += "  private final double[] _minparameters = { ";

                for (int i = 0; i < getNumberOfParameters(); i++) {
                    javaSource += paramrangemin.get(i);

                    if (i < (getNumberOfParameters() - 1)) {
                        javaSource += ", ";
                    }
                }

                javaSource += (" };" + CR + CR);
                javaSource += ("  /**" + CR);
                javaSource += ("   * Returns the minimum parameters." + CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public VectorType getParameterMinimum() {" +
                        CR);
                javaSource += ("    return Settings.Vector.getVectorType( _minparameters );" +
                        CR);
                javaSource += ("  }" + CR + CR);

                //
                javaSource += "  private final double[] _maxparameters = { ";

                for (int i = 0; i < getNumberOfParameters(); i++) {
                    javaSource += paramrangemax.get(i);

                    if (i < (getNumberOfParameters() - 1)) {
                        javaSource += ", ";
                    }
                }

                javaSource += (" };" + CR + CR);
                javaSource += ("  /**" + CR);
                javaSource += ("   * Returns the maximum parameters." + CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public VectorType getParameterMaximum() {" +
                        CR);
                javaSource += ("    return Settings.Vector.getVectorType( _maxparameters );" +
                        CR);
                javaSource += ("  }" + CR + CR);
            }

            javaSource += ("  /**" + CR);
            javaSource += ("   * Returns a description of the dynamical system." +
                    CR);
            javaSource += ("   **/" + CR);
            javaSource += ("  public String getDescription() {" + CR);

            if (containsInitialComment) {
                javaSource += ("    String _s = \"" + initialComment + "\"" +
                        " + System.getProperty(\"line.separator\");" + CR);
            } // if
            else {
                javaSource += ("    String _s = \"\";" + CR);
            } // else

            javaSource += ("    _s += \"Dynamic variables\" + System.getProperty(\"line.separator\");" +
                    CR);

            for (int i = 0; i < getNumberOfVariables(); i++) {
                javaSource += ("" + "    _s += \"" + variableNames.get(i) +
                        ((i < (getNumberOfVariables() - 1)) ? (",") : (";")) + "\";" +
                        CR);
            } // for

            javaSource += ("    _s += \"\" + System.getProperty(\"line.separator\");" +
                    CR);

            if (containsParameters) {
                javaSource += ("    _s += \"Parameters\" + System.getProperty(\"line.separator\");" +
                        CR);

                for (int i = 0; i < getNumberOfParameters(); i++) {
                    javaSource += ("" + "    _s += \"" + parameterNames.get(i) +
                            ((i < (getNumberOfParameters() - 1)) ? (",") : (";")) +
                            "\";" + CR);
                } // for

                javaSource += ("    _s += \"\" + System.getProperty(\"line.separator\");" +
                        CR);
            } // if

            if (containsAuxillaries) {
                // use the deep definition of auxillaries
                //XXXAuxiliary.deepDefinition = true;
                javaSource += ("    _s += \"Auxiliaries\" + System.getProperty(\"line.separator\");" +
                        CR);

                for (int i = 0; i < auxillaries.size(); i++) {
                    javaSource += ("" + "    _s += \"" + auxiliaryNames.get(i) +
                            "=" +
                            ((auxillaries.get(i) instanceof Expression)
                                    ? (((Auxiliary) auxillaries.get(i)).getExpression())
                                    : (auxillaries.get(i))) + ";\"" +
                            " + System.getProperty(\"line.separator\");" + CR);
                } // for
            } // if

            javaSource += ("    _s += \"Dynamic equations\" + System.getProperty(\"line.separator\");" +
                    CR);

            // do not use the deep definition of auxillaries
            //XXXAuxiliary.deepDefinition = false;
            for (int i = 0; i < expressions.size(); i++) {
                javaSource += ("" + "    _s += \"" + variables.get(i) + "'=" +
                        expressions.get(i) + ";\"" +
                        " + System.getProperty(\"line.separator\");" + CR);
            } // for

            javaSource += ("    return _s;" + CR);
            javaSource += ("  }" + CR + CR);

            //
            if (isTimeContinuous && isExpandable) {
                //XXXAuxiliary.deepDefinition = false;
                javaSource += ("  /**" + CR);
                javaSource += ("   * Method evaluate sets up Taylor tree." +
                        CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public void evaluate( TaylorDependant[] Tvariable, TaylorIndependant t ) {" +
                        CR);

                if (containsParameters) {
                    for (int i = 0; i < getNumberOfParameters(); i++)
                        javaSource += ("    TaylorParameter Tpar_" +
                                parameters.get(i) + " = new TaylorParameter( " +
                                parameters.get(i) + " );" + CR);
                } // if

                // Change variable names for code generation.
                for (int i = 0; i < variables.size(); i++) {
                    ((Variable) variables.get(i)).setName("Tvariable[" + i +
                            "]");
                } // for

                // new code 01022001 CK
                //XXXAuxiliary.deepDefinition = true;
                if (containsAuxillaries) {
                    for (int i = 0; i < getNumberOfAuxillaries(); i++)
                        javaSource += ("    TaylorDouble Tpar_" +
                                auxiliaryNames.get(i) + " = " +
                                ((Auxiliary) auxillaries.get(i)).getExpression()
                                        .toTaylorMap() + ";" + CR);
                } // if

                //XXXAuxiliary.deepDefinition = false;
                for (int i = 0; i < expressions.size(); i++) {
                    javaSource += ("" + "    Tvariable[" + i + "].setOde(" +
                            ((Expression) expressions.get(i)).toTaylorMap() + ");" +
                            CR);
                } // for

                javaSource += ("  }" + CR + CR);

                // Reset variable names.
                for (int i = 0; i < variables.size(); i++) {
                    ((Variable) variables.get(i)).setName((String) variableNames.get(
                            i));
                } // for
            } // if

            if (isPoincareSection) {
                javaSource += ("  /**" + CR);
                javaSource += ("   * Returns the cutValue defining the Poincare section." +
                        CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public double cutValue( VectorType _x, double t ) {" +
                        CR);
                javaSource += ("    computeAuxiliaries( _x, t );" + CR);
                javaSource += ("    return cutValue;" + CR);
                javaSource += ("  }" + CR + CR);
                javaSource += ("  /**" + CR);
                javaSource += ("   * Returns the initial time for the Poincare section." +
                        CR);
                javaSource += ("   **/" + CR);
                javaSource += ("  public double getT0() {" + CR);
                javaSource += ("    return 0.0;" + CR);
                javaSource += ("  }" + CR + CR);
            } // if

            // Final line of Java source code.
            javaSource += ("}" + CR);

            //XXX
            //System.out.println(toXML());
            // Save Java source code.
            if (!fileName.equals("")) {
                try {
                    File file = new File(fileName.substring(0,
                            fileName.length() - 3) + "java");
                    PrintWriter outfile = new PrintWriter(new FileWriter(file));
                    outfile.print(javaSource);
                    outfile.close();
                } catch (IOException e) {
                    System.out.println(
                            "Serious error (IOException) in generating Java source code." +
                                    CR + "Exception=" + e);
                }
            }
        }
    }

    /**
     * The method returns an xml representation of the system.
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        StringBuffer buffer = new StringBuffer(16 * 1024);
        buffer.append("<model>" + System.getProperty("line.separator"));
        buffer.append(" <variables>" + System.getProperty("line.separator"));

        for (int i = 0; i < getNumberOfVariables(); i++) {
            buffer.append("  <variable>" +
                    System.getProperty("line.separator"));
            buffer.append("   " + variableNames.get(i) +
                    System.getProperty("line.separator"));
            buffer.append("  </variable>" +
                    System.getProperty("line.separator"));
        } // for

        buffer.append(" </variables>" + System.getProperty("line.separator"));

        if (containsProcesses) {
            buffer.append(" <processes>" +
                    System.getProperty("line.separator"));

            for (int i = 0; i < getNumberOfProcesses(); i++) {
                buffer.append("  <process>" +
                        System.getProperty("line.separator"));
                buffer.append("   <name>" +
                        System.getProperty("line.separator"));
                buffer.append("    " + processNames.get(i) +
                        System.getProperty("line.separator"));
                buffer.append("   </name>" +
                        System.getProperty("line.separator"));
                buffer.append("  </process>" +
                        System.getProperty("line.separator"));
            } // for

            buffer.append(" </processes>" +
                    System.getProperty("line.separator"));
        } // if

        if (containsParameters) {
            buffer.append(" <parameters>" +
                    System.getProperty("line.separator"));

            for (int i = 0; i < getNumberOfParameters(); i++) {
                buffer.append("  <parameter>" +
                        System.getProperty("line.separator"));
                buffer.append("   " + parameterNames.get(i) +
                        System.getProperty("line.separator"));
                buffer.append("  </parameter>" +
                        System.getProperty("line.separator"));
            } // for

            buffer.append(" </parameters>" +
                    System.getProperty("line.separator"));
        } // if

        if (containsAuxillaries) {
            buffer.append(" <auxiliaries>" +
                    System.getProperty("line.separator"));

            for (int i = 0; i < getNumberOfAuxillaries(); i++) {
                buffer.append("  <auxiliary>" +
                        System.getProperty("line.separator"));
                buffer.append("   <name>" +
                        System.getProperty("line.separator"));
                buffer.append("    " + auxiliaryNames.get(i) +
                        System.getProperty("line.separator"));
                buffer.append("   </name>" +
                        System.getProperty("line.separator"));
                buffer.append("    " +
                        ((Expression) auxillaries.get(i)).toXML() +
                        System.getProperty("line.separator"));
                buffer.append("  </auxiliary>" +
                        System.getProperty("line.separator"));
            } // for

            buffer.append(" </auxiliaries>" +
                    System.getProperty("line.separator"));
        } // if

        buffer.append(" <equations>" + System.getProperty("line.separator"));

        for (int i = 0; i < getNumberOfVariables(); i++) {
            buffer.append("  <lhs>" + System.getProperty("line.separator"));
            buffer.append("   " + variableNames.get(i) +
                    System.getProperty("line.separator"));
            buffer.append("  </lhs>" + System.getProperty("line.separator"));
            buffer.append("  <rhs>" + System.getProperty("line.separator"));
            buffer.append("   <expression>" +
                    System.getProperty("line.separator"));
            buffer.append("    " + ((Expression) expressions.get(i)).toXML() +
                    System.getProperty("line.separator"));
            buffer.append("   </expression>" +
                    System.getProperty("line.separator"));
            buffer.append("  </rhs>" + System.getProperty("line.separator"));
        }

        buffer.append(" </equations>" + System.getProperty("line.separator"));

        if (containsProcesses) {
            buffer.append(" <diffusionmatrix>" +
                    System.getProperty("line.separator"));

            for (int i = 0; i < expressions.size(); i++) {
                buffer.append("  <row>");

                for (int j = 0; j < processNames.size(); j++) {
                    buffer.append("  <element>");
                    buffer.append("   <expression>");
                    buffer.append(diffusionMatrix[i][j].toXML());
                    buffer.append("   </expression>");
                    buffer.append("  </element>");
                } // for

                buffer.append("  </row>");
            } // for

            buffer.append(" </diffusionmatrix>" +
                    System.getProperty("line.separator"));
        } // if

        buffer.append("</model>");

        return buffer.toString();
    } // toXML

    /**
     **/
    private boolean isNumber(String s) {
        try {
            double d = Double.parseDouble(s);

            return true;
        } // try
        catch (NumberFormatException e) {
            return false;
        } // catch
    } // isNumber

    /**
     **/
    private boolean isConstant(String s) {
        return constantNames.contains(s);
    }

    /**
     **/
    private boolean isVariable(String s) {
        return variableNames.contains(s);
    }

    /**
     **/
    private boolean isLogical(String s) {
        if (s.equals("not")) {
            return true;
        } else if (s.equals("and")) {
            return true;
        } else if (s.equals("or")) {
            return true;
        } else if (s.equals("xor")) {
            return true;
        } else if (s.equals("ifte")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     **/
    private boolean isComparison(String s) {
        if (s.equals("gt")) {
            return true;
        }

        if (s.equals("ge")) {
            return true;
        }

        if (s.equals("lt")) {
            return true;
        }

        if (s.equals("le")) {
            return true;
        }

        if (s.equals("eq")) {
            return true;
        }

        if (s.equals("neq")) {
            return true;
        }

        return false;
    }

    /**
     **/
    private boolean isAuxiliary(String s) {
        return auxiliaryNames.contains(s);
    }

    /**
     **/
    private boolean isUserFunction(String s) {
        return userFunctionNames.contains(s);
    }

    /**
     **/
    private boolean isParameter(String s) {
        return parameterNames.contains(s);
    }

    /**
     **/
    private boolean isFunction(String s) {
        return functionNames.contains(s);
    }

    /**
     **/
    private boolean isOperator(String s) {
        if (s.equals("+")) {
            return true;
        } else if (s.equals("-")) {
            return true;
        } else if (s.equals("*")) {
            return true;
        } else if (s.equals("/")) {
            return true;
        } else if (s.equals("^")) {
            return true;
        } else if (s.equals("minus")) {
            return true;
        } else if (s.equals("plus")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the precedence of an operator. The <B>int</B> returned coincides
     * with the standard Java operator precedence. The power operator (^) is
     * assigned the precedence of one higher than that for multipication ().
     * For undefined operators zero is returned, that is, a lower precedence
     * than any Java operator.
     *
     * @param s DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    private int precedence(String s) {
        if (s.equals("^")) {
            return 13;
        } else if (s.equals("*")) {
            return 12;
        } else if (s.equals("/")) {
            return 12;
        } else if (s.equals("+")) {
            return 11;
        } else if (s.equals("-")) {
            return 11;
        } else if (s.equals("minus")) {
            return 14;
        } else if (s.equals("plus")) {
            return 14;
        } else {
            return 0;
        }
    }

    /**
     **/
    private boolean isLeftParanthesis(String s) {
        return s.equals("(");
    }

    /**
     **/
    private boolean isRightParanthesis(String s) {
        return s.equals(")");
    }

    /**
     * The method parses the argument {@link java.lang.String} using the method
     * uses the methods {@link #infixToPostfix(java.lang.String)} and {@link
     * #postfixToExpression(java.lang.String)} to generate a {@link
     * org.jscience.mathematics.analysis.expressions.Expression}. Before these
     * methods are invoked, it is determined whether the argument is a number
     * that can be handled by the static parseDouble(java.lang.String) method
     * from the java.lang.Double class. If this is the case a {@link
     * org.jscience.mathematics.analysis.expressions.Constant} with the value
     * is returned. Syntax errors are signalled by the return of a null
     * pointer. The concrete syntax error detected can be determined by the
     * method call {@link #getError()} which returns a {@link
     * java.lang.String}.
     *
     * @param symbolic DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Object parse(String symbolic) {
        StringTokenizer tokenizer = new StringTokenizer(symbolic, " ", false);
        StringBuffer buffer = new StringBuffer(1024);

        while (tokenizer.hasMoreTokens()) {
            buffer.append(tokenizer.nextToken());
        } // while

        symbolic = buffer.toString();

        try {
            double doubleValue = Double.parseDouble(symbolic);

            return new Constant(doubleValue);
        } // try
        catch (Exception e) {
            return postfixToExpression(infixToPostfix(symbolic));
        } // catch
    } // parse

    /**
     * The method converts a mathematical expression in postfix format to an
     * <B>Expression</B> in symbolic format.
     *
     * @param postfix Mathematical expressioin in postfix format.
     * @return <B>null</B> if a syntax error is found.
     */
    public Object postfixToExpression(String postfix) {
        if (postfix == null) {
            return null;
        }

        errorString = "Serious error: Error occured but no error message was issued.";

        String symbol = null; // will contain the current token being analyzed
        Stack stack = new Stack();
        StringTokenizer expr = new StringTokenizer(postfix, "()+-*/^ ", true);

        while (expr.hasMoreTokens()) {
            symbol = expr.nextToken(); // read next token

            while (symbol.equals(" ")) { // skip any blank spaces (" ")

                if (expr.hasMoreTokens()) {
                    symbol = expr.nextToken();
                } else { // we have analyzed all tokens

                    if (stack.size() > 1) {
                        errorString = "Error in postfixToExpression: Too many objects on Stack." +
                                CR + "Stack=" + stack + CR +
                                "Happened while parsing: " + postfix;

                        return null;
                    } else {
                        return stack.pop();
                    }
                }
            } // while

            // new code CK 11122000 not checked yet
            // the code has been in effect for some time and seems to work CK 12012001
            if ((symbol.length() > 1) && expr.hasMoreTokens()) {
                if ((symbol.charAt(symbol.length() - 1) == 'e') ||
                        (symbol.charAt(symbol.length() - 1) == 'E')) {
                    try {
                        int i = Integer.parseInt(symbol.substring(0, 1));
                        symbol += expr.nextToken(); // add + or - (sign of exponent)

                        if (expr.hasMoreTokens()) {
                            symbol += expr.nextToken(); // add exponent (without sign)
                        } // if
                    } // try
                    catch (NumberFormatException nfe) {
                    } // catch
                } // if
            } // if

            if (isNumber(symbol)) {
                try {
                    double doubleValue = Double.parseDouble(symbol);
                    stack.push(new Constant(doubleValue));
                } // try
                catch (NumberFormatException e) { // cannot happen
                } // catch
            } // if
            else if (isConstant(symbol)) {
                stack.push(constants.get(constantNames.indexOf(symbol)));
            } // else if
            else if (isVariable(symbol)) {
                stack.push(variables.get(variableNames.indexOf(symbol)));
            } // else if
            else if (isParameter(symbol)) {
                stack.push(parameters.get(parameterNames.indexOf(symbol)));
            } // else if
            else if (isAuxiliary(symbol)) {
                int i = auxiliaryNames.indexOf(symbol);

                if (auxillaries.get(i) instanceof Expression) {
                    stack.push(auxillaries.get(i));
                } // if
                else {
                    stack.push(auxillaries.get(i));
                } // else
            } // else if
            else if (isTimeContinuous &&
                    symbol.equals(ExpressionsStandardNames.TIME_CONTINUOUS_NAME)) {
                isAutonomous = false;
                stack.push(new Variable(
                        ExpressionsStandardNames.TIME_CONTINUOUS_NAME, 0));
            } // else if
            else if (isTimeDiscrete &&
                    symbol.equals(ExpressionsStandardNames.DISCRETE_NAME)) {
                isAutonomous = false;
                stack.push(new Variable(
                        ExpressionsStandardNames.DISCRETE_NAME, 0));
            } // else if
            else if (isFunction(symbol)) {
                if (!stack.isEmpty()) {
                    Expression op = (Expression) stack.pop();

                    if (symbol.equals("sin")) {
                        stack.push(new Sin(op));
                    } else if (symbol.equals("cos")) {
                        stack.push(new Cos(op));
                    } else if (symbol.equals("tan")) {
                        stack.push(new Tan(op));
                    } // else if
                    else if (symbol.equals("asin")) {
                        stack.push(new Asin(op));
                    } // else if
                    else if (symbol.equals("acos")) {
                        stack.push(new Acos(op));
                    } // else if
                    else if (symbol.equals("atan")) {
                        stack.push(new Atan(op));
                    } // else if
                    else if (symbol.equals("log")) {
                        stack.push(new Log(op));
                    } // else if
                    else if (symbol.equals("exp")) {
                        stack.push(new Exp(op));
                    } // else if
                    else if (symbol.equals("sqrt")) {
                        stack.push(new Sqrt(op));
                    } // else if
                    else if (symbol.equals("minus")) {
                        stack.push(new Minus(op));
                    } // else if
                    else if (symbol.equals("plus")) {
                        stack.push(op);
                    } // else if
                    else if (symbol.equals("abs")) {
                        stack.push(new Abs(op));
                    } // else if
                    else if (symbol.equals("sign")) {
                        stack.push(new Sign(op));
                    } // else if

                    // Note that pow is a binary operator.
                    else if (symbol.equals("pow")) {
                        if (!stack.isEmpty()) {
                            Expression op2 = (Expression) stack.pop();
                            stack.push(new Pow(op2, op));
                        } else {
                            errorString = "Only one operator for function " +
                                    symbol + " in postfixToExpression.";

                            return null;
                        }
                    } // else if
                    else if (symbol.equals("atan2")) {
                        if (!stack.isEmpty()) {
                            Expression op2 = (Expression) stack.pop();
                            stack.push(new Atan2(op2, op));
                        } else {
                            errorString = "Only one operator for function " +
                                    symbol + " in postfixToExpression.";

                            return null;
                        }
                    } // else if
                    else if (symbol.equals("max")) {
                        if (!stack.isEmpty()) {
                            Expression op2 = (Expression) stack.pop();
                            stack.push(new Max(op2, op));
                        } else {
                            errorString = "Only one operator for function " +
                                    symbol + " in postfixToExpression.";

                            return null;
                        }
                    } // else if
                    else if (symbol.equals("min")) {
                        if (!stack.isEmpty()) {
                            Expression op2 = (Expression) stack.pop();
                            stack.push(new Min(op2, op));
                        } else {
                            errorString = "Only one operator for function " +
                                    symbol + " in postfixToExpression.";

                            return null;
                        }
                    } // else if

                    // experimental code for noise CK 31012001
                    else if (symbol.equals("rand")) {
                        if (!stack.isEmpty()) {
                            Expression op2 = (Expression) stack.pop();
                            stack.push(new Rand(op2, op));
                        } else {
                            errorString = "Only one operator for function " +
                                    symbol + " in postfixToExpression.";

                            return null;
                        }
                    } // else if

                    // experimental code for noise CK 04022001
                    else if (symbol.equals("gauss")) {
                        if (!stack.isEmpty()) {
                            Expression op2 = (Expression) stack.pop();
                            stack.push(new Gauss(op2, op));
                        } else {
                            errorString = "Only one operator for function " +
                                    symbol + " in postfixToExpression.";

                            return null;
                        }
                    } // else if

                    // code for differentiation
                    else if (symbol.equals("diff")) {
                        if (!stack.isEmpty()) {
                            Expression op2 = (Expression) stack.pop();

                            if (op instanceof NamedDataExpression) {
                                if (!op2.isDifferentiable()) {
                                    errorString = "Expression is not differentiable.";

                                    return null;
                                } else {
                                    stack.push(op2.diff(
                                            (NamedDataExpression) op));
                                }
                            } else {
                                errorString = "Differentiation has to be wrt. variable or parameter.";

                                return null;
                            }
                        } else {
                            errorString = "Only one operator for function " +
                                    symbol + " in postfixToExpression.";

                            return null;
                        }
                    } // else if

                    // Note that mod is a ternary operator
                    else if (symbol.equals("mod")) {
                        if (!stack.isEmpty()) {
                            Expression op2 = (Expression) stack.pop();

                            if (!stack.isEmpty()) {
                                Expression op3 = (Expression) stack.pop();
                                stack.push(new Mod(op3, op2, op));
                            } else {
                                errorString = "Third operator missing for function " +
                                        symbol + " in postfixToExpression.";

                                return null;
                            }
                        } else {
                            errorString = "Second operator missing for function " +
                                    symbol + " in postfixToExpression.";

                            return null;
                        }
                    } // else if
                    else if (symbol.equals("step")) {
                        if (!stack.isEmpty()) {
                            Expression op2 = (Expression) stack.pop();

                            if (!stack.isEmpty()) {
                                Expression op3 = (Expression) stack.pop();
                                stack.push(new Step(op3, op2, op));
                            } else {
                                errorString = "Third operator missing for function " +
                                        symbol + " in postfixToExpression.";

                                return null;
                            }
                        } else {
                            errorString = "Second operator missing for function " +
                                    symbol + " in postfixToExpression.";

                            return null;
                        }
                    } // else if

                    // Note that clip is a quartary operator
                    else if (symbol.equals("clip")) {
                        if (!stack.isEmpty()) {
                            Expression op2 = (Expression) stack.pop();

                            if (!stack.isEmpty()) {
                                Expression op3 = (Expression) stack.pop();

                                if (!stack.isEmpty()) {
                                    Expression op4 = (Expression) stack.pop();
                                    stack.push(new Clip(op4, op3, op2, op));
                                } // if
                                else {
                                    errorString = "Fourth operator missing for function " +
                                            symbol + " in postfixToExpression.";

                                    return null;
                                } // else
                            } else {
                                errorString = "Third operator missing for function " +
                                        symbol + " in postfixToExpression.";

                                return null;
                            } // else
                        } // if
                        else {
                            errorString = "Second operator missing for function " +
                                    symbol + " in postfixToExpression.";

                            return null;
                        } // else
                    } // else if
                } // if
                else {
                    errorString = "No operator for function " + symbol +
                            " in postfixToExpression.";

                    return null;
                } // else
            } // isFunction
            else if (isLogical(symbol)) {
                if (symbol.equals("not")) {
                    if (!stack.isEmpty()) {
                        Object o = stack.pop();

                        if (o instanceof Logical) {
                            stack.push(new Not((Logical) o));
                        } // if
                        else {
                            errorString = "Argument to not was not logical.";

                            return null;
                        } // else
                    } // if
                    else {
                        errorString = "Missing argument for not.";

                        return null;
                    } // else
                } // not
                else if (symbol.equals("and")) {
                    if (stack.size() > 1) {
                        Object o2 = stack.pop();
                        Object o1 = stack.pop();

                        if (o1 instanceof Logical && o2 instanceof Logical) {
                            stack.push(new And((Logical) o1, (Logical) o2));
                        } // if
                        else {
                            errorString = "Argument to and was not logical.";

                            return null;
                        } // else
                    } // if
                    else {
                        errorString = "Missing argument for and.";

                        return null;
                    } // else
                } // and
                else if (symbol.equals("or")) {
                    if (stack.size() > 1) {
                        Object o2 = stack.pop();
                        Object o1 = stack.pop();

                        if (o1 instanceof Logical && o2 instanceof Logical) {
                            stack.push(new Or((Logical) o1, (Logical) o2));
                        } // if
                        else {
                            errorString = "Argument to or was not logical.";

                            return null;
                        } // else
                    } // if
                    else {
                        errorString = "Missing argument for or.";

                        return null;
                    } // else
                } // or
                else if (symbol.equals("xor")) {
                    if (stack.size() > 1) {
                        Object o2 = stack.pop();
                        Object o1 = stack.pop();

                        if (o1 instanceof Logical && o2 instanceof Logical) {
                            stack.push(new Xor((Logical) o1, (Logical) o2));
                        } // if
                        else {
                            errorString = "Argument to xor was not logical.";

                            return null;
                        } // else
                    } // if
                    else {
                        errorString = "Missing argument for xor.";

                        return null;
                    } // else
                } // xor
                else if (symbol.equals("ifte")) {
                    if (stack.size() > 2) {
                        Object o3 = stack.pop();
                        Object o2 = stack.pop();
                        Object o1 = stack.pop();

                        if (o1 instanceof Logical && o2 instanceof Expression &&
                                o3 instanceof Expression) {
                            stack.push(new IFTE((Logical) o1, (Expression) o2,
                                    (Expression) o3));
                        } // if
                        else {
                            errorString = "Argument to ifte was wrong.";

                            return null;
                        } // else
                    } // if
                    else {
                        errorString = "Missing argument to ifte.";

                        return null;
                    } // else
                } // ifte
                else {
                    errorString = "Did not recognize logical " + symbol;

                    return null;
                } // else
            } // isLogical
            else if (isComparison(symbol)) {
                if (symbol.equals("gt")) {
                    if (stack.size() > 1) {
                        Object o2 = stack.pop();
                        Object o1 = stack.pop();

                        if (o1 instanceof Expression &&
                                o2 instanceof Expression) {
                            stack.push(new GT((Expression) o1, (Expression) o2));
                        } // if
                        else {
                            errorString = "Wrong argument to gt.";

                            return null;
                        } // else
                    } // if
                    else {
                        errorString = "Missing argument for gt.";

                        return null;
                    } // else
                } // gt
                else if (symbol.equals("ge")) {
                    if (stack.size() > 1) {
                        Object o2 = stack.pop();
                        Object o1 = stack.pop();

                        if (o1 instanceof Expression &&
                                o2 instanceof Expression) {
                            stack.push(new GE((Expression) o1, (Expression) o2));
                        } // if
                        else {
                            errorString = "Wrong argument to ge.";

                            return null;
                        } // else
                    } // if
                    else {
                        errorString = "Missing argument for ge.";

                        return null;
                    } // else
                } // ge
                else if (symbol.equals("lt")) {
                    if (stack.size() > 1) {
                        Object o2 = stack.pop();
                        Object o1 = stack.pop();

                        if (o1 instanceof Expression &&
                                o2 instanceof Expression) {
                            stack.push(new LT((Expression) o1, (Expression) o2));
                        } // if
                        else {
                            errorString = "Wrong argument to lt.";

                            return null;
                        } // else
                    } // if
                    else {
                        errorString = "Missing argument for lt.";

                        return null;
                    } // else
                } // lt
                else if (symbol.equals("le")) {
                    if (stack.size() > 1) {
                        Object o2 = stack.pop();
                        Object o1 = stack.pop();

                        if (o1 instanceof Expression &&
                                o2 instanceof Expression) {
                            stack.push(new LE((Expression) o1, (Expression) o2));
                        } // if
                        else {
                            errorString = "Wrong argument to le.";

                            return null;
                        } // else
                    } // if
                    else {
                        errorString = "Missing argument for le.";

                        return null;
                    } // else
                } // le
                else if (symbol.equals("eq")) {
                    if (stack.size() > 1) {
                        Object o2 = stack.pop();
                        Object o1 = stack.pop();

                        if (o1 instanceof Expression &&
                                o2 instanceof Expression) {
                            stack.push(new EQ((Expression) o1, (Expression) o2));
                        } // if
                        else {
                            errorString = "Wrong argument to eq.";

                            return null;
                        } // else
                    } // if
                    else {
                        errorString = "Missing argument for eq.";

                        return null;
                    } // else
                } // eq
                else if (symbol.equals("neq")) {
                    if (stack.size() > 1) {
                        Object o2 = stack.pop();
                        Object o1 = stack.pop();

                        if (o1 instanceof Expression &&
                                o2 instanceof Expression) {
                            stack.push(new NEQ((Expression) o1, (Expression) o2));
                        } // if
                        else {
                            errorString = "Wrong argument to neq.";

                            return null;
                        } // else
                    } // if
                    else {
                        errorString = "Missing argument for neq.";

                        return null;
                    } // else
                } // neq
                else {
                    errorString = "Did not recognize comparison " + symbol;

                    return null;
                } // else
            } // isComparison

            // new code 03032001 CK
            else if (isUserFunction(symbol)) {
                //System.out.println("function used " +symbol);
                int functionindex = 0;

                for (int i = 0; i < userFunctionNames.size(); i++) {
                    if (symbol.equals((String) userFunctionNames.get(i))) {
                        functionindex = i;
                    }
                }

                //System.out.println("function index "+functionindex);
                java.util.List formalparams = ((java.util.List) userFunctionParameters.get(functionindex));

                //System.out.println("formal parameters "+formalparams);
                int numparams = formalparams.size();

                //System.out.println("number of parameters "+numparams);
                Expression e = (Expression) userFunctions.get(functionindex);

                //System.out.println("function "+symbol+"="+e);
                for (int j = 0; j < numparams; j++) {
                    if (!stack.isEmpty()) {
                        Expression formalarg = new Variable((String) formalparams.get(numparams -
                                1 - j));

                        //System.out.println("formal arg "+formalarg);
                        Expression arg = (Expression) stack.pop();

                        //System.out.println("arg "+arg);
                        e = e.replace(formalarg, arg);

                        //System.out.println("*function "+symbol+"="+e);
                    } else {
                        errorString = "Not enough arguments on stack for user function " +
                                symbol + ": " + numparams + " excepted.";

                        return null;
                    }
                }

                stack.push(e);
            } else if (isOperator(symbol)) {
                if (stack.size() > 1) {
                    Expression op2 = (Expression) stack.pop();
                    Expression op1 = (Expression) stack.pop();

                    if (symbol.equals("+")) {
                        stack.push(new Addition(op1, op2));
                    } else if (symbol.equals("-")) {
                        stack.push(new Subtraction(op1, op2));
                    } else if (symbol.equals("*")) {
                        stack.push(new Multiplication(op1, op2));
                    } else if (symbol.equals("/")) {
                        stack.push(new Division(op1, op2));
                    } else if (symbol.equals("^")) {
                        stack.push(new Pow(op1, op2));
                    }
                } // if
                else {
                    errorString = "Not enough operands on Stack for operator " +
                            symbol + " Stack=" + stack;

                    return null;
                } // else
            } // else if
            else {
                errorString = "Unknown symbol " + symbol +
                        " received in postfixToExpression";

                return null;
            } // else
        }

        if (stack.size() > 1) {
            errorString = "Error in postfixToExpression: Too many objects on Stack." +
                    CR + "Stack=" + stack + CR + "Happened while parsing: " +
                    postfix;

            return null;
        } // if
        else {
            return (Expression) stack.pop();
        }
    } // postfixToExpression

    /**
     * The method converts a mathematical expression in postfix format to a
     * <B>TaylorMap</B> in symbolic format.
     *
     * @param postfix Mathematical expressioin in postfix format.
     * @return <B>null</B> if a syntax error is found.
     */
    public TaylorDouble postfixToTaylorDouble(String postfix) {
        if (postfix == null) {
            return null;
        }

        String symbol = null; // will contain the current token being analyzed
        Stack stack = new Stack();
        StringTokenizer expr = new StringTokenizer(postfix, "()+-*/^ ", true);

        while (expr.hasMoreTokens()) {
            symbol = expr.nextToken(); // read next token

            while (symbol.equals(" ")) { // skip any blank spaces (" ")

                if (expr.hasMoreTokens()) {
                    symbol = expr.nextToken();
                } else { // we have analyzed all tokens

                    if (stack.size() > 1) {
                        errorString = "Error in postfixToTaylorDouble: Too many objects on Stack." +
                                CR + "Stack=" + stack + CR +
                                "Happened while parsing: " + postfix;

                        return null;
                    } else {
                        return (TaylorDouble) stack.pop();
                    }
                }
            }

            if (isNumber(symbol)) {
                try {
                    double doubleValue = Double.parseDouble(symbol);
                    stack.push(new TaylorConstant(doubleValue));
                } catch (NumberFormatException e) {
                    System.out.println(
                            "NumberFormatException in postfixToTaylorDouble: e=" +
                                    e);
                }
            } else if (isConstant(symbol)) {
                stack.push(constants.get(constantNames.indexOf(symbol)));
            } else if (isVariable(symbol)) {
                stack.push(variables.get(variableNames.indexOf(symbol)));
            } else if (isParameter(symbol)) {
                stack.push(parameters.get(parameterNames.indexOf(symbol)));
            } else if (isAuxiliary(symbol)) {
                stack.push((TaylorDouble) auxillaries.get(
                        auxiliaryNames.indexOf(symbol)));
            } else if (isTimeContinuous &&
                    symbol.equals(ExpressionsStandardNames.TIME_CONTINUOUS_NAME)) {
                stack.push(new TaylorIndependant(0));
            } else if (isFunction(symbol)) {
                if (!stack.isEmpty()) {
                    TaylorDouble op = (TaylorDouble) stack.pop();

                    if (symbol.equals("sin")) {
                        stack.push(new TaylorSin(op));
                    } else if (symbol.equals("cos")) {
                        stack.push(new TaylorCos(op));
                    } else if (symbol.equals("tan")) {
                        stack.push(new TaylorDivide(new TaylorSin(op),
                                new TaylorCos(op)));
                    } else if (symbol.equals("log")) {
                        stack.push(new TaylorLog(op));
                    } else if (symbol.equals("exp")) {
                        stack.push(new TaylorExp(op));
                    } else if (symbol.equals("sqrt")) {
                        stack.push(new TaylorSqrt(op));
                    } else if (symbol.equals("minus")) {
                        stack.push(new TaylorNegate(op));
                    } else if (symbol.equals("plus")) {
                        stack.push(op);
                    }
                    // Note that pow is a binary operator.
                    else if (symbol.equals("pow")) {
                        if (!stack.isEmpty()) {
                            TaylorDouble op2 = (TaylorDouble) stack.pop();

                            if (op instanceof TaylorConstant) {
                                stack.push(new TaylorPow(op2,
                                        ((TaylorConstant) op).getCoeff(0)));
                            } else {
                                errorString = "Second argument to pow function not a double in postfixToTaylorDouble (1).";

                                return null;
                            }
                        } else {
                            errorString = "Only one operator for function " +
                                    symbol + " in postfixToTaylorDouble.";

                            return null;
                        }
                    }
                } else {
                    errorString = "No operator for function " + symbol +
                            " in postfixToTaylorDouble.";

                    return null;
                }
            } else if (isOperator(symbol)) {
                if (stack.size() > 1) {
                    TaylorDouble op2 = (TaylorDouble) stack.pop();
                    TaylorDouble op1 = (TaylorDouble) stack.pop();

                    if (symbol.equals("+")) {
                        stack.push(new TaylorAdd(op1, op2));
                    } // if
                    else if (symbol.equals("-")) {
                        stack.push(new TaylorSubtract(op1, op2));
                    } // else if
                    else if (symbol.equals("*")) {
                        stack.push(new TaylorMultiply(op1, op2));
                    } // else if
                    else if (symbol.equals("/")) {
                        stack.push(new TaylorDivide(op1, op2));
                    } // else if
                    else if (symbol.equals("^")) {
                        if (op2 instanceof TaylorConstant) {
                            stack.push(new TaylorPow(op1,
                                    ((TaylorConstant) op2).getCoeff(0)));
                        } else {
                            errorString = "Second argument to pow function not a double in postfixToTaylorDouble (2).";

                            return null;
                        }
                    } // else if
                } // if
                else {
                    errorString = "Not enough operands on Stack for operator " +
                            symbol + " Stack=" + stack;

                    return null;
                } // else
            } // else if
            else {
                System.out.println(
                        "Unknown symbol received in postfixToTaylorDouble");
            } // else
        } // while

        if (stack.size() > 1) {
            errorString = "Error in postfixToTaylorDouble: Too many objects on Stack." +
                    CR + "Stack=" + stack + CR + "Happened while parsing: " +
                    postfix;

            return null;
        } // if
        else {
            return (TaylorDouble) stack.pop();
        }
    } // postfixToTaylorDouble

    /**
     * Converts a mathematical expression in the <B>String</B><I>infix</I> in
     * infix format to a mathematical expression in postfix format. E.g.
     * "x+y(z-4)" is converted to "x y z 4 -  +." In case of a syntax error in
     * <I>postfix</I> the method returns <B>null</B> and sets the
     * <B>String</B><I>errorString</I> to a descriptive error message. This
     * error description can be retrieved with the <B>public</B> method
     * <I>getError</I>.
     *
     * @param infix DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public String infixToPostfix(String infix) {
        boolean nextMinusIsUnary = true; // Indicates whether the next "-" is unary or binary
        String postfix = "";
        String symbol = null;
        Stack stack = new Stack();
        stack.push("(");
        infix += ")";

        StringTokenizer expr = new StringTokenizer(infix, "()+-*/^,", true);

        while (!stack.isEmpty() && expr.hasMoreTokens()) {
            symbol = expr.nextToken();

            if ((symbol.length() > 1) && expr.hasMoreTokens()) {
                if ((symbol.charAt(symbol.length() - 1) == 'e') ||
                        (symbol.charAt(symbol.length() - 1) == 'E')) {
                    try {
                        int i = Integer.parseInt(symbol.substring(0, 1));
                        symbol += expr.nextToken(); // add + or - (sign of exponent)

                        if (expr.hasMoreTokens()) {
                            symbol += expr.nextToken(); // add exponent (without sign)
                        }
                    } catch (NumberFormatException nfe) {
                    }
                }
            }

            if (isNumber(symbol)) {
                postfix += (symbol + " ");
                nextMinusIsUnary = false;
            } else if (isConstant(symbol)) {
                postfix += (symbol + " ");
                nextMinusIsUnary = false;
            } else if (isVariable(symbol)) {
                postfix += (symbol + " ");
                nextMinusIsUnary = false;
            } else if (isParameter(symbol)) {
                postfix += (symbol + " ");
                nextMinusIsUnary = false;
            } else if (isAuxiliary(symbol)) {
                postfix += (symbol + " ");
                nextMinusIsUnary = false;
            } else if (isTimeContinuous &&
                    symbol.equals(ExpressionsStandardNames.TIME_CONTINUOUS_NAME)) {
                postfix += (symbol + " ");
                nextMinusIsUnary = false;
            } else if (isTimeDiscrete &&
                    symbol.equals(ExpressionsStandardNames.DISCRETE_NAME)) {
                postfix += (symbol + " ");
                nextMinusIsUnary = false;
            } else if (isLeftParanthesis(symbol)) {
                stack.push("(");
                nextMinusIsUnary = true;
            } else if (isRightParanthesis(symbol)) {
                if (!stack.isEmpty()) {
                    while (isOperator((String) stack.peek()) ||
                            isFunction((String) stack.peek()) ||
                            isLogical((String) stack.peek()) ||
                            isComparison((String) stack.peek()) ||
                            isUserFunction((String) stack.peek())) {
                        postfix += ((String) stack.pop() + " ");
                    } // while

                    if (((String) stack.peek()).equals("(")) {
                        stack.pop(); // remove "("
                    }

                    while (!stack.isEmpty() &&
                            (isFunction((String) stack.peek()) ||
                                    isLogical((String) stack.peek()) ||
                                    isComparison((String) stack.peek()) ||
                                    isUserFunction((String) stack.peek()))) {
                        postfix += ((String) stack.pop() + " ");
                    } // while
                } // if
                else {
                    errorString = "Stack empty in infixToPostfix.";

                    return null;
                } // else

                nextMinusIsUnary = false;
            } // else if
            else if (isFunction(symbol)) {
                stack.push(symbol);
            } // else if
            else if (isUserFunction(symbol)) {
                stack.push(symbol);
            } // else if
            else if (isLogical(symbol)) {
                stack.push(symbol);
            } // else if
            else if (isComparison(symbol)) {
                stack.push(symbol);
            } // else if
            else if (isOperator(symbol)) {
                if (symbol.equals("-") && nextMinusIsUnary) {
                    stack.push("minus");
                    nextMinusIsUnary = true;
                } // if

                // new code 04022001 CK implements unary plus
                else if (symbol.equals("+") && nextMinusIsUnary) {
                    stack.push("plus");
                    nextMinusIsUnary = true;
                } // if
                else {
                    while (isOperator((String) stack.peek())) {
                        if (precedence((String) stack.peek()) >= precedence(
                                symbol)) {
                            postfix += ((String) stack.pop() + " ");
                        } else {
                            break;
                        }
                    } // while

                    stack.push(symbol);
                    nextMinusIsUnary = true;
                } // else
            } // else if
            else if (symbol.equals(",")) {
                if (!stack.isEmpty()) {
                    while (isOperator((String) stack.peek())) {
                        postfix += ((String) stack.pop() + " ");
                    } // while
                } // if

                // bug fix 02022001 CK; the next line was absent before
                nextMinusIsUnary = true;
            } // else if
            else {
                errorString = "Unknown token " + symbol +
                        " in infixToPostfix.";

                return null;
            } // else
        }

        // new addition CK 11122000;  not checked for logic
        while (!stack.isEmpty()) {
            postfix += ((String) stack.pop() + " ");
        }

        if (!stack.isEmpty()) {
            errorString = "Stack not empty in infixToPostfix: Stack=" + stack;

            return null;
        } // if

        return postfix;
    } // infixToPostfix

    /**
     * Converts a postfix expression to it corresponding infix form. Is not
     * used in <I>ExpressionParser</I>. It is not tested and almost certainly
     * will not work with anything but one argument functions. CK 03032001
     *
     * @param postfix DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public String postfixToInfix(String postfix) {
        String symbol = null;
        Stack stack = new Stack();
        StringTokenizer expr = new StringTokenizer(postfix, "()+-*/^ ", true);

        while (expr.hasMoreTokens()) {
            symbol = expr.nextToken();

            while (symbol.equals(" ")) {
                if (expr.hasMoreTokens()) {
                    symbol = expr.nextToken();
                } else {
                    if (!stack.isEmpty()) {
                        if (stack.size() == 1) {
                            return (String) stack.pop();
                        } else {
                            errorString = "Stack is not empty in postfixToInfix." +
                                    CR + "Stack=" + stack;

                            return null;
                        }
                    } else {
                        errorString = "Stack is empty in postfixToInfix.";

                        return null;
                    }
                }
            }

            if (isNumber(symbol)) {
                stack.push(symbol);
            } else if (isVariable(symbol)) {
                stack.push(symbol);

                //stack.push( variables.get( variableNames.indexOf( symbol ) ) );
            } else if (isParameter(symbol)) {
                stack.push(symbol);

                //stack.push( parameters.get( parameterNames.indexOf( symbol ) ) );
            } else if (isFunction(symbol)) {
                stack.push(symbol + "(" + (String) stack.pop() + ")");
            } else if (isOperator(symbol)) {
                String op2 = (String) stack.pop();
                String op1 = (String) stack.pop();
                stack.push("(" + op1 + ")" + symbol + "(" + op2 + ")");
            } else {
                System.out.println("Unknown token in postfixToInfix : " +
                        symbol);
            }
        }

        return (String) stack.pop();
    }

    /**
     *
     **/
    public static void main(String[] args) {
        ExpressionParser parser = new ExpressionParser();
        Expression cnst = (Expression) parser.parse("1+sin(3/2.0)");

        if (cnst != null) {
            double doubleValue = cnst.eval();
            System.out.println("value=" + doubleValue);
        }

        parser = new ExpressionParser();

        java.util.List variableNames = new java.util.ArrayList();
        java.util.List variables = new java.util.ArrayList();
        variableNames.add("x");

        Variable x = new Variable("x", 2.3);
        variables.add(x);
        parser.setVariableNames(variableNames);
        parser.setVariables(variables);

        Expression expr = (Expression) parser.parse("x+sin(3*x)");
        Expression derivative = expr.diff(x);
        Expression optderivative = derivative.optimize();
        System.out.println("expr=" + expr);
        System.out.println("derivative=" + derivative);
        System.out.println("optderivative=" + optderivative);
    } // main, ExpressionParser

    /**
     * The static class Compiler is intended for command line compilation of
     * source files defining dynamical systems. The static {@link
     * #main(String[])} method takes as argument the names of files containing
     * a system definition, and will generate Java source code files, as well
     * as class files with bytecode, provided that no syntax errors are
     * detected, and that the Java compiler can be located automatically.
     *
     * @see #main(String[])
     */
    public static class Compiler implements Serializable {
        /**
         * The main method allows for command line compilation of dynamical
         * systems files. Any number of names of files can be given on the
         * command line; both Java and class files will be generated.
         *
         * @param args DOCUMENT ME!
         */
        public static void main(String[] args) {
            for (int i = 0; i < args.length; i++) {
                ExpressionParser parser = new ExpressionParser();
                String filename = args[i];
                String source = "";
                File file = new File(filename);

                if (!file.exists()) {
                    System.out.println("The file " + filename +
                            " could not be found.");

                    return;
                } // if

                try {
                    FileReader fr = new FileReader(file);
                    BufferedReader reader = new BufferedReader(fr);
                    String line;

                    while ((line = reader.readLine()) != null) {
                        source += line;
                    } // while

                    reader.close();
                } // try
                catch (FileNotFoundException fnfe) {
                    System.out.println("File " + filename +
                            " could not be found.");
                } // catch
                catch (IOException ioe) {
                    System.out.println("Error occured while reading file:" +
                            CR + "Error is " + ioe.getMessage());
                } // catch

                String s = parser.parseSource(source);

                if (s != null) {
                    System.out.println("Syntax error: " + s);

                    return;
                } // if
                else {
                    parser.generateJavaSource(filename);

                    String javaSource = parser.getJavaSource();

                    // Save Java source
                    file = new File(filename.substring(0, filename.length() -
                            3) + "java");

                    try {
                        FileWriter fw = new FileWriter(file);
                        PrintWriter outfile = new PrintWriter(fw);
                        outfile.print(javaSource);
                        outfile.close();
                    } // try
                    catch (IOException ioe) {
                        System.out.println("Error occured while writing file:" +
                                CR + "Error is " + ioe.getMessage());
                    } // catch
                } // else

                String javaCompiler;

                {
                    javaCompiler = System.getProperty("java.home") +
                            System.getProperty("file.separator") + ".." +
                            System.getProperty("file.separator") + "bin" +
                            System.getProperty("file.separator") + "javac";

                    String osname = System.getProperty("os.name");

                    if (osname.equals("Linux")) {
                    } // if
                    else if (osname.equals("SunOs")) {
                    } // else if
                    else if (osname.equals("Windows")) {
                        javaCompiler += ".exe";
                    } // else if
                } // javaCompiler initializer

                File javac = new File(javaCompiler);
                String javaOptions = " -classpath " +
                        System.getProperty("java.class.path") + " ";
                final String cmd = javac.getAbsolutePath() + " " + javaOptions +
                        " " + args[i].substring(0, args[i].length() - 3) + "java";
                Thread t = new Thread() {
                    public void run() {
                        try {
                            Process p = Runtime.getRuntime().exec(cmd);
                            InputStream pin = p.getErrorStream();
                            InputStreamReader cin = new InputStreamReader(pin);
                            BufferedReader in = new BufferedReader(cin);
                            String s;

                            if ((s = in.readLine()) != null) {
                                System.out.println(
                                        "Syntax error(s) detected in Java source code:");
                                System.out.println(s);

                                while ((s = in.readLine()) != null) {
                                    System.out.println(s);
                                } // if
                            } // if

                            in.close();
                        } // try
                        catch (IOException ioe) {
                            System.out.println("Error (" +
                                    ioe.getMessage() + ")" + CR);
                        } // catch
                    } // run
                }; // Thread

                t.start();
            } // for

            if (args.length == 0) {
                System.out.println(
                        "Usage: java ExpressionParser$Compiler filename(s)");
            } // if
        } // main, ExpressionParser.Compiler
    } // Compiler
} // ExpressionParser
