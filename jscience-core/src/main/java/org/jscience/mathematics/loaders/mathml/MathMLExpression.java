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

package org.jscience.mathematics.loaders.mathml;

import java.util.Arrays;
import org.jscience.mathematics.structures.sets.FiniteSet;
import org.jscience.mathematics.util.MathUtils;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.structures.rings.Ring;
import org.jscience.mathematics.linearalgebra.matrices.*;
import org.jscience.mathematics.linearalgebra.vectors.*;
import org.jscience.mathematics.numbers.complex.Complex;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.structures.groups.GroupElement;
import org.jscience.mathematics.structures.rings.RingElement;
import org.jscience.mathematics.structures.rings.FieldElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Modernized MathMLExpression class for JScience 6.0.
 * Encapsulates math expressions described by the &lt;apply&gt; tag.
 *
 * @author Mark Hale
 * @author Silvere Martin-Michiellot (Modernization)
 * @version 1.1
 */
public final class MathMLExpression {
    private String operation;
    private final List<Object> args = new ArrayList<>();

    public MathMLExpression() {
    }

    public void setOperation(String op) {
        operation = op;
    }

    public String getOperation() {
        return operation;
    }

    public void addArgument(Object obj) {
        args.add(obj);
    }

    public Object getArgument(int n) {
        return args.get(n);
    }

    public int length() {
        return args.size();
    }

    public MathMLExpression substitute(String var, Object value) {
        MathMLExpression subExpr = new MathMLExpression();
        subExpr.operation = operation;

        for (int i = 0; i < length(); i++) {
            Object arg = getArgument(i);

            if (arg instanceof MathMLExpression) {
                arg = ((MathMLExpression) arg).substitute(var, value);
            } else if (arg.equals(var)) {
                arg = value;
            }

            subExpr.addArgument(arg);
        }

        return subExpr;
    }

    public MathMLExpression substitute(Map<String, Object> vars) {
        MathMLExpression subExpr = new MathMLExpression();
        subExpr.operation = operation;

        for (int i = 0; i < length(); i++) {
            Object arg = getArgument(i);

            if (arg instanceof MathMLExpression) {
                arg = ((MathMLExpression) arg).substitute(vars);
            } else if (vars.containsKey(arg)) {
                arg = vars.get(arg);
            }

            subExpr.addArgument(arg);
        }

        return subExpr;
    }

    public Object evaluate() {
        if (length() == 1) {
            return unaryEvaluate();
        } else if (length() == 2) {
            return binaryEvaluate();
        } else {
            return nAryEvaluate();
        }
    }

    private Object unaryEvaluate() {
        Object value = getArgument(0);

        if (value instanceof MathMLExpression) {
            value = ((MathMLExpression) value).evaluate();
        }

        if (operation.equals("abs")) {
            if (value instanceof Real) {
                return ((Real) value).abs();
            } else if (value instanceof Complex) {
                return ((Complex) value).abs();
            }
        } else if (operation.equals("arg")) {
            if (value instanceof Real) {
                return ((Real) value).sign() >= 0 ? Real.ZERO : Real.PI;
            } else if (value instanceof Complex) {
                return ((Complex) value).arg();
            }
        } else if (operation.equals("real")) {
            if (value instanceof Complex) {
                return ((Complex) value).getReal();
            }
        } else if (operation.equals("imaginary")) {
            if (value instanceof Complex) {
                return ((Complex) value).getImaginary();
            }
        } else if (operation.equals("conjugate")) {
            if (value instanceof Complex) {
                return ((Complex) value).conjugate();
            }
        } else if (operation.equals("transpose")) {
            if (value instanceof Matrix) {
                return ((Matrix<?>) value).transpose();
            }
        } else if (operation.equals("exp")) {
            if (value instanceof Real) {
                return ((Real) value).exp();
            } else if (value instanceof Complex) {
                return ((Complex) value).exp();
            }
        } else if (operation.equals("ln")) {
            if (value instanceof Real) {
                return ((Real) value).log();
            } else if (value instanceof Complex) {
                return ((Complex) value).log();
            }
        } else if (operation.equals("sin")) {
            if (value instanceof Real) {
                return ((Real) value).sin();
            } else if (value instanceof Complex) {
                return ((Complex) value).sin();
            }
        } else if (operation.equals("cos")) {
            if (value instanceof Real) {
                return ((Real) value).cos();
            } else if (value instanceof Complex) {
                return ((Complex) value).cos();
            }
        } else if (operation.equals("tan")) {
            if (value instanceof Real) {
                return ((Real) value).tan();
            } else if (value instanceof Complex) {
                return ((Complex) value).tan();
            }
        } else if (operation.equals("arcsin")) {
            if (value instanceof Real) {
                return ((Real) value).asin();
            } else if (value instanceof Complex) {
                return ((Complex) value).asin();
            }
        } else if (operation.equals("arccos")) {
            if (value instanceof Real) {
                return ((Real) value).acos();
            } else if (value instanceof Complex) {
                return ((Complex) value).acos();
            }
        } else if (operation.equals("arctan")) {
            if (value instanceof Real) {
                return ((Real) value).atan();
            } else if (value instanceof Complex) {
                return ((Complex) value).atan();
            }
        } else if (operation.equals("sinh")) {
            if (value instanceof Real) {
                return ((Real) value).sinh();
            } else if (value instanceof Complex) {
                return ((Complex) value).sinh();
            }
        } else if (operation.equals("cosh")) {
            if (value instanceof Real) {
                return ((Real) value).cosh();
            } else if (value instanceof Complex) {
                return ((Complex) value).cosh();
            }
        } else if (operation.equals("tanh")) {
            if (value instanceof Real) {
                return ((Real) value).tanh();
            } else if (value instanceof Complex) {
                return ((Complex) value).tanh();
            }
        } else if (operation.equals("factorial")) {
            if (value instanceof Real) {
                return Real.of(MathUtils.factorial(((Real) value).doubleValue()));
            }
        } else if (operation.equals("not")) {
            if (value instanceof Boolean) {
                return !((Boolean) value);
            }
        }

        return value;
    }

    @SuppressWarnings("unchecked")
    private Object binaryEvaluate() {
        Object value = getArgument(0);
        if (value instanceof MathMLExpression) {
            value = ((MathMLExpression) value).evaluate();
        }

        Object next = getArgument(1);
        if (next instanceof MathMLExpression) {
            next = ((MathMLExpression) next).evaluate();
        }

        if (operation.equals("minus")) {
            if (value instanceof RingElement) {
                return ((RingElement) value).subtract((RingElement) next);
            }
        } else if (operation.equals("divide")) {
            if (value instanceof FieldElement) {
                return ((FieldElement) value).divide((FieldElement) next);
            }
        } else if (operation.equals("power")) {
            if (value instanceof Real) {
                return ((Real) value).pow(((Real) next).doubleValue());
            } else if (value instanceof Complex) {
                return ((Complex) value).pow((Complex) next);
            }
        } else if (operation.equals("neq")) {
            return !value.equals(next);
        } else {
            return nAryEvaluate();
        }

        return value;
    }

    @SuppressWarnings("unchecked")
    private Object nAryEvaluate() {
        Object value = getArgument(0);
        if (value instanceof MathMLExpression) {
            value = ((MathMLExpression) value).evaluate();
        }

        if (operation.equals("plus")) {
            if (value instanceof RingElement) {
                RingElement ans = (RingElement) value;
                for (int i = 1; i < length(); i++) {
                    Object next = getArgument(i);
                    if (next instanceof MathMLExpression) {
                        next = ((MathMLExpression) next).evaluate();
                    }
                    ans = (RingElement) ans.add((RingElement) next);
                }
                return ans;
            }
        } else if (operation.equals("times")) {
            if (value instanceof RingElement) {
                RingElement ans = (RingElement) value;
                for (int i = 1; i < length(); i++) {
                    Object next = getArgument(i);
                    if (next instanceof MathMLExpression) {
                        next = ((MathMLExpression) next).evaluate();
                    }
                    ans = (RingElement) ans.multiply((RingElement) next);
                }
                return ans;
            }
        } else if (operation.equals("min")) {
            Comparable ans = (Comparable) value;
            for (int i = 1; i < length(); i++) {
                Object next = getArgument(i);
                if (next instanceof MathMLExpression) {
                    next = ((MathMLExpression) next).evaluate();
                }
                if (ans.compareTo(next) > 0) {
                    ans = (Comparable) next;
                }
            }
            return ans;
        } else if (operation.equals("max")) {
            Comparable ans = (Comparable) value;
            for (int i = 1; i < length(); i++) {
                Object next = getArgument(i);
                if (next instanceof MathMLExpression) {
                    next = ((MathMLExpression) next).evaluate();
                }
                if (ans.compareTo(next) < 0) {
                    ans = (Comparable) next;
                }
            }
            return ans;
        } else if (operation.equals("mean")) {
            return Real.of(mean());
        } else if (operation.equals("median")) {
            double[] nums = new double[length()];
            for (int i = 0; i < nums.length; i++) {
                Object arg = getArgument(i);
                if (arg instanceof MathMLExpression) arg = ((MathMLExpression) arg).evaluate();
                nums[i] = ((Number) arg).doubleValue();
            }
            Arrays.sort(nums);
            double median;
            if (nums.length % 2 == 0)
                median = (nums[nums.length / 2] + nums[nums.length / 2 - 1]) / 2.0;
            else
                median = nums[nums.length / 2];
            return Real.of(median);
        } else if (operation.equals("eq")) {
            Object arg1 = value;
            for (int i = 1; i < length(); i++) {
                Object arg2 = getArgument(i);
                if (arg2 instanceof MathMLExpression) arg2 = ((MathMLExpression) arg2).evaluate();
                if (!arg1.equals(arg2)) return false;
                arg1 = arg2;
            }
            return true;
        }

        return value;
    }

    private double mean() {
        double sum = 0;
        for (int i = 0; i < length(); i++) {
            Object arg = getArgument(i);
            if (arg instanceof MathMLExpression) arg = ((MathMLExpression) arg).evaluate();
            sum += ((Number) arg).doubleValue();
        }
        return sum / length();
    }
}
