package org.jscience.mathematics.symbolic.compiler;

import org.jscience.mathematics.symbolic.parsing.ExpressionParser;
import org.jscience.mathematics.symbolic.parsing.ExpressionParser.Expr;

import javax.tools.*;
import java.io.File;
import java.io.FileWriter;

import java.net.URL;
import java.net.URLClassLoader;

import java.util.UUID;
import java.util.function.Function;

/**
 * Compiles symbolic expressions into Java Bytecode for high performance.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class FunctionCompiler {

    /**
     * Compiles a symbolic expression into a native Java Function.
     * 
     * @param expression   The symbolic expression string (e.g. "x^2 + sin(x)")
     * @param variableName The name of the variable (e.g. "x")
     * @return A standard Java Function<Double, Double>
     */
    public static Function<Double, Double> compile(String expression, String variableName) {
        Expr expr = ExpressionParser.parse(expression);
        // Translate expression to Java code
        String javaBody = toJavaCode(expr, variableName);

        return compileClass(javaBody);
    }

    private static String toJavaCode(Expr expr, String variableName) {
        if (expr instanceof ExpressionParser.Const c) {
            return String.valueOf(c.value);
        }
        if (expr instanceof ExpressionParser.Var v) {
            if (v.name.equals(variableName))
                return "x"; // Map variable to input 'x'
            throw new IllegalArgumentException("Unknown variable during compilation: " + v.name);
        }
        if (expr instanceof ExpressionParser.BinaryOp op) {
            String l = toJavaCode(op.left, variableName);
            String r = toJavaCode(op.right, variableName);
            switch (op.op) {
                case '+':
                    return "(" + l + " + " + r + ")";
                case '-':
                    return "(" + l + " - " + r + ")";
                case '*':
                    return "(" + l + " * " + r + ")";
                case '/':
                    return "(" + l + " / " + r + ")";
                case '^':
                    return "Math.pow(" + l + ", " + r + ")";
            }
        }
        if (expr instanceof ExpressionParser.FuncCall f) {
            String arg = toJavaCode(f.arg, variableName);
            switch (f.name) {
                case "sin":
                    return "Math.sin(" + arg + ")";
                case "cos":
                    return "Math.cos(" + arg + ")";
                case "exp":
                    return "Math.exp(" + arg + ")";
                case "ln":
                    return "Math.log(" + arg + ")";
                case "sqrt":
                    return "Math.sqrt(" + arg + ")";
                default:
                    throw new IllegalArgumentException("Unknown function: " + f.name);
            }
        }
        throw new UnsupportedOperationException("Compilation not supported for: " + expr.getClass().getSimpleName());
    }

    @SuppressWarnings("unchecked")
    private static Function<Double, Double> compileClass(String expressionJavaCode) {
        String className = "CompiledFunction_" + UUID.randomUUID().toString().replace("-", "");
        String sourceCode = "package org.jscience.mathematics.symbolic.compiler.dynamic;\n" +
                "import java.util.function.Function;\n" +
                "public class " + className + " implements Function<Double, Double> {\n" +
                "    @Override\n" +
                "    public Double apply(Double x) {\n" +
                "        return " + expressionJavaCode + ";\n" +
                "    }\n" +
                "}";

        // compilation logic
        try {
            File root = new File(System.getProperty("java.io.tmpdir"));
            File sourceFile = new File(root, className + ".java");
            try (FileWriter writer = new FileWriter(sourceFile)) {
                writer.write(sourceCode);
            }

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null)
                throw new IllegalStateException("No Java Compiler available. Ensure JDK is used (not JRE).");

            int result = compiler.run(null, null, null, sourceFile.getAbsolutePath());
            if (result != 0)
                throw new RuntimeException("Compilation failed");

            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
            Class<?> cls = Class.forName("org.jscience.mathematics.symbolic.compiler.dynamic." + className, true,
                    classLoader);

            return (Function<Double, Double>) cls.getDeclaredConstructor().newInstance();

        } catch (Exception e) {
            throw new RuntimeException("Dynamic compilation failed", e);
        }
    }
}
