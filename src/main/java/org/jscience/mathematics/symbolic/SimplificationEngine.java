package org.jscience.mathematics.symbolic;

import org.jscience.mathematics.symbolic.parsing.ExpressionParser.BinaryOp;
import org.jscience.mathematics.symbolic.parsing.ExpressionParser.Const;
import org.jscience.mathematics.symbolic.parsing.ExpressionParser.Expr;
import org.jscience.mathematics.symbolic.parsing.ExpressionParser.FuncCall;
import org.jscience.mathematics.symbolic.parsing.ExpressionParser.Negate;

import java.util.Map;

/**
 * Engine for simplifying mathematical expressions.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class SimplificationEngine {

    public static Expr simplify(Expr expr) {
        if (expr instanceof BinaryOp op) {
            Expr left = simplify(op.left);
            Expr right = simplify(op.right);

            // Constant folding
            if (left instanceof Const l && right instanceof Const r) {
                return new Const(new BinaryOp(op.op, l, r).eval(Map.of()));
            }

            // Identity rules
            switch (op.op) {
                case '+':
                    if (isZero(left))
                        return right;
                    if (isZero(right))
                        return left;
                    if (left instanceof Const l && right instanceof Const r)
                        return new Const(l.value + r.value);
                    // Grouping: x + x = 2x
                    if (left.toText().equals(right.toText()))
                        return new BinaryOp('*', new Const(2), left);
                    break;
                case '-':
                    if (isZero(right))
                        return left;
                    if (left.toText().equals(right.toText()))
                        return new Const(0);
                    break;
                case '*':
                    if (isZero(left) || isZero(right))
                        return new Const(0);
                    if (isOne(left))
                        return right;
                    if (isOne(right))
                        return left;
                    if (isMinusOne(left))
                        return new Negate(right);
                    if (isMinusOne(right))
                        return new Negate(left);
                    break;
                case '/':
                    if (isZero(left))
                        return new Const(0);
                    if (isOne(right))
                        return left;
                    if (left.toText().equals(right.toText()))
                        return new Const(1);
                    break;
                case '^':
                    if (isZero(right))
                        return new Const(1); // x^0 = 1
                    if (isOne(right))
                        return left; // x^1 = x
                    if (isZero(left))
                        return new Const(0); // 0^x = 0
                    break;
            }

            return new BinaryOp(op.op, left, right);
        }

        if (expr instanceof Negate n) {
            Expr s = simplify(n.arg);
            if (s instanceof Const c)
                return new Const(-c.value);
            if (s instanceof Negate n2)
                return n2.arg; // --x = x
            return new Negate(s);
        }

        if (expr instanceof FuncCall f) {
            Expr s = simplify(f.arg);
            // Evaluate known functions on constants
            if (s instanceof Const c) {
                return new Const(new FuncCall(f.name, c).eval(Map.of()));
            }
            return new FuncCall(f.name, s);
        }

        return expr;
    }

    private static boolean isZero(Expr e) {
        return e instanceof Const c && c.value == 0;
    }

    private static boolean isOne(Expr e) {
        return e instanceof Const c && c.value == 1;
    }

    private static boolean isMinusOne(Expr e) {
        return e instanceof Const c && c.value == -1;
    }
}
