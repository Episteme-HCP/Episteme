package org.jscience.mathematics.symbolic;

import org.jscience.mathematics.number.Real;
import java.util.Map;
import java.util.HashMap;

/**
 * Symbolic expression representation and manipulation.
 * <p>
 * Computer algebra system (CAS) for symbolic mathematics.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public abstract class Expression {

    /**
     * Evaluates expression with given variable values.
     */
    public abstract Real evaluate(Map<String, Real> variables);

    /**
     * Symbolic differentiation with respect to variable.
     */
    public abstract Expression differentiate(String variable);

    /**
     * Simplifies expression algebraically.
     */
    public abstract Expression simplify();

    /**
     * Returns LaTeX representation.
     */
    public abstract String toLatex();

    // Constant expression
    public static class Constant extends Expression {
        private final Real value;

        public Constant(Real value) {
            this.value = value;
        }

        @Override
        public Real evaluate(Map<String, Real> variables) {
            return value;
        }

        @Override
        public Expression differentiate(String variable) {
            return new Constant(Real.ZERO);
        }

        @Override
        public Expression simplify() {
            return this;
        }

        @Override
        public String toLatex() {
            return value.toString();
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    // Variable expression
    public static class Variable extends Expression {
        private final String name;

        public Variable(String name) {
            this.name = name;
        }

        @Override
        public Real evaluate(Map<String, Real> variables) {
            if (!variables.containsKey(name)) {
                throw new IllegalArgumentException("Variable " + name + " not provided");
            }
            return variables.get(name);
        }

        @Override
        public Expression differentiate(String variable) {
            return variable.equals(name) ? new Constant(Real.ONE) : new Constant(Real.ZERO);
        }

        @Override
        public Expression simplify() {
            return this;
        }

        @Override
        public String toLatex() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }

        public String getName() {
            return name;
        }
    }

    // Addition: a + b
    public static class Add extends Expression {
        private final Expression left, right;

        public Add(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public Real evaluate(Map<String, Real> variables) {
            return left.evaluate(variables).add(right.evaluate(variables));
        }

        @Override
        public Expression differentiate(String variable) {
            return new Add(left.differentiate(variable), right.differentiate(variable));
        }

        @Override
        public Expression simplify() {
            Expression l = left.simplify();
            Expression r = right.simplify();

            // 0 + x = x
            if (l instanceof Constant && ((Constant) l).value.isZero())
                return r;
            if (r instanceof Constant && ((Constant) r).value.isZero())
                return l;

            // c1 + c2 = c3
            if (l instanceof Constant && r instanceof Constant) {
                return new Constant(((Constant) l).value.add(((Constant) r).value));
            }

            return new Add(l, r);
        }

        @Override
        public String toLatex() {
            return "(" + left.toLatex() + " + " + right.toLatex() + ")";
        }

        @Override
        public String toString() {
            return "(" + left + " + " + right + ")";
        }
    }

    // Multiplication: a * b
    public static class Multiply extends Expression {
        private final Expression left, right;

        public Multiply(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public Real evaluate(Map<String, Real> variables) {
            return left.evaluate(variables).multiply(right.evaluate(variables));
        }

        @Override
        public Expression differentiate(String variable) {
            // Product rule: (fg)' = f'g + fg'
            return new Add(
                    new Multiply(left.differentiate(variable), right),
                    new Multiply(left, right.differentiate(variable)));
        }

        @Override
        public Expression simplify() {
            Expression l = left.simplify();
            Expression r = right.simplify();

            // 0 * x = 0
            if (l instanceof Constant && ((Constant) l).value.isZero())
                return new Constant(Real.ZERO);
            if (r instanceof Constant && ((Constant) r).value.isZero())
                return new Constant(Real.ZERO);

            // 1 * x = x
            if (l instanceof Constant && ((Constant) l).value.isOne())
                return r;
            if (r instanceof Constant && ((Constant) r).value.isOne())
                return l;

            // c1 * c2 = c3
            if (l instanceof Constant && r instanceof Constant) {
                return new Constant(((Constant) l).value.multiply(((Constant) r).value));
            }

            return new Multiply(l, r);
        }

        @Override
        public String toLatex() {
            return "(" + left.toLatex() + " \\cdot " + right.toLatex() + ")";
        }

        @Override
        public String toString() {
            return "(" + left + " * " + right + ")";
        }
    }

    // Power: a^b
    public static class Power extends Expression {
        private final Expression base, exponent;

        public Power(Expression base, Expression exponent) {
            this.base = base;
            this.exponent = exponent;
        }

        @Override
        public Real evaluate(Map<String, Real> variables) {
            return base.evaluate(variables).pow(exponent.evaluate(variables));
        }

        @Override
        public Expression differentiate(String variable) {
            // Power rule: (x^n)' = n*x^(n-1) for constant n
            if (exponent instanceof Constant) {
                Real n = ((Constant) exponent).value;
                return new Multiply(
                        new Multiply(new Constant(n), new Power(base, new Constant(n.subtract(Real.ONE)))),
                        base.differentiate(variable));
            }
            // General: (f^g)' = f^g * (g'*ln(f) + g*f'/f)
            return new Multiply(
                    this,
                    new Add(
                            new Multiply(exponent.differentiate(variable), new Ln(base)),
                            new Multiply(
                                    new Multiply(exponent, base.differentiate(variable)),
                                    new Power(base, new Constant(Real.of(-1))))));
        }

        @Override
        public Expression simplify() {
            Expression b = base.simplify();
            Expression e = exponent.simplify();

            // x^0 = 1
            if (e instanceof Constant && ((Constant) e).value.isZero()) {
                return new Constant(Real.ONE);
            }

            // x^1 = x
            if (e instanceof Constant && ((Constant) e).value.isOne()) {
                return b;
            }

            return new Power(b, e);
        }

        @Override
        public String toLatex() {
            return base.toLatex() + "^{" + exponent.toLatex() + "}";
        }

        @Override
        public String toString() {
            return base + "^" + exponent;
        }
    }

    // Natural logarithm
    public static class Ln extends Expression {
        private final Expression argument;

        public Ln(Expression argument) {
            this.argument = argument;
        }

        @Override
        public Real evaluate(Map<String, Real> variables) {
            return argument.evaluate(variables).log();
        }

        @Override
        public Expression differentiate(String variable) {
            // (ln f)' = f'/f
            return new Multiply(
                    argument.differentiate(variable),
                    new Power(argument, new Constant(Real.of(-1))));
        }

        @Override
        public Expression simplify() {
            return new Ln(argument.simplify());
        }

        @Override
        public String toLatex() {
            return "\\ln(" + argument.toLatex() + ")";
        }

        @Override
        public String toString() {
            return "ln(" + argument + ")";
        }
    }

    // Sine function
    public static class Sin extends Expression {
        private final Expression argument;

        public Sin(Expression argument) {
            this.argument = argument;
        }

        @Override
        public Real evaluate(Map<String, Real> variables) {
            return argument.evaluate(variables).sin();
        }

        @Override
        public Expression differentiate(String variable) {
            // (sin f)' = cos(f) * f'
            return new Multiply(new Cos(argument), argument.differentiate(variable));
        }

        @Override
        public Expression simplify() {
            return new Sin(argument.simplify());
        }

        @Override
        public String toLatex() {
            return "\\sin(" + argument.toLatex() + ")";
        }

        @Override
        public String toString() {
            return "sin(" + argument + ")";
        }
    }

    // Cosine function
    public static class Cos extends Expression {
        private final Expression argument;

        public Cos(Expression argument) {
            this.argument = argument;
        }

        @Override
        public Real evaluate(Map<String, Real> variables) {
            return argument.evaluate(variables).cos();
        }

        @Override
        public Expression differentiate(String variable) {
            // (cos f)' = -sin(f) * f'
            return new Multiply(
                    new Constant(Real.of(-1)),
                    new Multiply(new Sin(argument), argument.differentiate(variable)));
        }

        @Override
        public Expression simplify() {
            return new Cos(argument.simplify());
        }

        @Override
        public String toLatex() {
            return "\\cos(" + argument.toLatex() + ")";
        }

        @Override
        public String toString() {
            return "cos(" + argument + ")";
        }
    }
}
