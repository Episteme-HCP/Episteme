package org.jscience.mathematics.symbolic;

import org.jscience.mathematics.algebra.Ring;
import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.sets.Reals;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class SymbolicTest {

    private static final double EPSILON = 1e-10;
    private static final Ring<Real> REALS = Reals.getInstance();

    // ========== Original Polynomial Tests ==========

    @Test
    public void testBasicArithmetic() {
        Variable<Real> x = new Variable<>("x", Reals.getInstance());
        Variable<Real> y = new Variable<>("y", Reals.getInstance());

        // x + x = 2x
        Expression<Real> expr1 = x.add(x);
        assertEquals("2.0*x", expr1.toString());

        // x * x = x^2
        Expression<Real> expr2 = x.multiply(x);
        assertEquals("x^2", expr2.toString());

        // x * y = x*y
        Expression<Real> expr3 = x.multiply(y);
        assertEquals("x*y", expr3.toString()); // Lexicographic order x, y
    }

    @Test
    public void testDifferentiation() {
        Variable<Real> x = new Variable<>("x", Reals.getInstance());
        Variable<Real> y = new Variable<>("y", Reals.getInstance());

        // f = x^2 + y^2
        Expression<Real> f = x.multiply(x).add(y.multiply(y));

        // df/dx = 2x
        Expression<Real> dfdx = f.differentiate(x);
        assertEquals("2.0*x", dfdx.toString());

        // df/dy = 2y
        Expression<Real> dfdy = f.differentiate(y);
        assertEquals("2.0*y", dfdy.toString());
    }

    @Test
    public void testEvaluation() {
        Variable<Real> x = new Variable<>("x", Reals.getInstance());

        // f = x^2 + 2x + 1
        Expression<Real> f = x.multiply(x)
                .add(x.multiply(PolynomialExpression.ofConstant(Real.of(2), Reals.getInstance())))
                .add(PolynomialExpression.ofConstant(Real.of(1), Reals.getInstance()));

        Map<Variable<Real>, Real> values = new HashMap<>();
        values.put(x, Real.of(3));

        // 3^2 + 2*3 + 1 = 9 + 6 + 1 = 16
        Real result = f.evaluate(values);
        assertEquals(16.0, result.doubleValue(), 0.0001);
    }

    // ========== FunctionExpression Tests ==========

    @Test
    public void testFunctionExpressionSin() {
        Variable<Real> x = new Variable<>("x", REALS);
        FunctionExpression<Real> sinX = FunctionExpression.sin(x);

        Map<Variable<Real>, Real> assignments = new HashMap<>();
        assignments.put(x, Real.ZERO);
        Real result = sinX.evaluate(assignments);
        assertEquals(0.0, result.doubleValue(), EPSILON, "sin(0) should be 0");

        assignments.put(x, Real.of(Math.PI / 2));
        result = sinX.evaluate(assignments);
        assertEquals(1.0, result.doubleValue(), EPSILON, "sin(π/2) should be 1");
    }

    @Test
    public void testFunctionExpressionCos() {
        Variable<Real> x = new Variable<>("x", REALS);
        FunctionExpression<Real> cosX = FunctionExpression.cos(x);

        Map<Variable<Real>, Real> assignments = new HashMap<>();
        assignments.put(x, Real.ZERO);
        Real result = cosX.evaluate(assignments);
        assertEquals(1.0, result.doubleValue(), EPSILON, "cos(0) should be 1");

        assignments.put(x, Real.of(Math.PI));
        result = cosX.evaluate(assignments);
        assertEquals(-1.0, result.doubleValue(), EPSILON, "cos(π) should be -1");
    }

    @Test
    public void testFunctionExpressionExp() {
        Variable<Real> x = new Variable<>("x", REALS);
        FunctionExpression<Real> expX = FunctionExpression.exp(x);

        Map<Variable<Real>, Real> assignments = new HashMap<>();
        assignments.put(x, Real.ZERO);
        Real result = expX.evaluate(assignments);
        assertEquals(1.0, result.doubleValue(), EPSILON, "exp(0) should be 1");

        assignments.put(x, Real.ONE);
        result = expX.evaluate(assignments);
        assertEquals(Math.E, result.doubleValue(), EPSILON, "exp(1) should be e");
    }

    @Test
    public void testFunctionExpressionLog() {
        Variable<Real> x = new Variable<>("x", REALS);
        FunctionExpression<Real> logX = FunctionExpression.log(x);

        Map<Variable<Real>, Real> assignments = new HashMap<>();
        assignments.put(x, Real.ONE);
        Real result = logX.evaluate(assignments);
        assertEquals(0.0, result.doubleValue(), EPSILON, "log(1) should be 0");

        assignments.put(x, Real.of(Math.E));
        result = logX.evaluate(assignments);
        assertEquals(1.0, result.doubleValue(), EPSILON, "log(e) should be 1");
    }

    // ========== ConstantExpression Tests ==========

    @Test
    public void testConstantExpression() {
        ConstantExpression<Real> five = new ConstantExpression<>(Real.of(5), REALS);
        ConstantExpression<Real> three = new ConstantExpression<>(Real.of(3), REALS);

        Expression<Real> sum = five.add(three);
        Real result = sum.evaluate(new HashMap<>());
        assertEquals(8.0, result.doubleValue(), EPSILON, "5 + 3 should be 8");

        Expression<Real> product = five.multiply(three);
        result = product.evaluate(new HashMap<>());
        assertEquals(15.0, result.doubleValue(), EPSILON, "5 * 3 should be 15");
    }

    @Test
    public void testConstantDifferentiation() {
        ConstantExpression<Real> five = new ConstantExpression<>(Real.of(5), REALS);
        Variable<Real> x = new Variable<>("x", REALS);

        Expression<Real> derivative = five.differentiate(x);
        Real result = derivative.evaluate(new HashMap<>());
        assertEquals(0.0, result.doubleValue(), EPSILON, "Derivative of constant should be 0");
    }

    // ========== Simplifier Tests ==========

    @Test
    public void testSimplifierZeroAddition() {
        ConstantExpression<Real> five = new ConstantExpression<>(Real.of(5), REALS);
        ConstantExpression<Real> zero = new ConstantExpression<>(Real.ZERO, REALS);

        Expression<Real> sum = new SumExpression<>(five, zero, REALS);
        Expression<Real> simplified = Simplifier.simplify(sum);

        Real result = simplified.evaluate(new HashMap<>());
        assertEquals(5.0, result.doubleValue(), EPSILON, "5 + 0 should simplify to 5");
    }

    @Test
    public void testSimplifierZeroMultiplication() {
        ConstantExpression<Real> five = new ConstantExpression<>(Real.of(5), REALS);
        ConstantExpression<Real> zero = new ConstantExpression<>(Real.ZERO, REALS);

        Expression<Real> product = new ProductExpression<>(five, zero, REALS);
        Expression<Real> simplified = Simplifier.simplify(product);

        Real result = simplified.evaluate(new HashMap<>());
        assertEquals(0.0, result.doubleValue(), EPSILON, "5 * 0 should simplify to 0");
    }

    @Test
    public void testSimplifierOneMultiplication() {
        ConstantExpression<Real> five = new ConstantExpression<>(Real.of(5), REALS);
        ConstantExpression<Real> one = new ConstantExpression<>(Real.ONE, REALS);

        Expression<Real> product = new ProductExpression<>(five, one, REALS);
        Expression<Real> simplified = Simplifier.simplify(product);

        Real result = simplified.evaluate(new HashMap<>());
        assertEquals(5.0, result.doubleValue(), EPSILON, "5 * 1 should simplify to 5");
    }

    @Test
    public void testSimplifierConstantFolding() {
        ConstantExpression<Real> two = new ConstantExpression<>(Real.of(2), REALS);
        ConstantExpression<Real> three = new ConstantExpression<>(Real.of(3), REALS);

        Expression<Real> sum = new SumExpression<>(two, three, REALS);
        Expression<Real> simplified = Simplifier.simplify(sum);
        Real result = simplified.evaluate(new HashMap<>());
        assertEquals(5.0, result.doubleValue(), EPSILON, "2 + 3 should fold to 5");

        Expression<Real> product = new ProductExpression<>(two, three, REALS);
        simplified = Simplifier.simplify(product);
        result = simplified.evaluate(new HashMap<>());
        assertEquals(6.0, result.doubleValue(), EPSILON, "2 * 3 should fold to 6");
    }

    // ========== Series Tests ==========

    @Test
    public void testSeriesExp() {
        Series<Real> expSeries = Series.exp(10);

        Real result = expSeries.evaluate(Real.ZERO);
        assertEquals(1.0, result.doubleValue(), EPSILON, "exp(0) should be 1");

        result = expSeries.evaluate(Real.ONE);
        assertEquals(Math.E, result.doubleValue(), 1e-6, "exp(1) should be approximately e");
    }

    @Test
    public void testSeriesSin() {
        Series<Real> sinSeries = Series.sin(15);

        Real result = sinSeries.evaluate(Real.ZERO);
        assertEquals(0.0, result.doubleValue(), EPSILON, "sin(0) should be 0");

        result = sinSeries.evaluate(Real.of(Math.PI / 2));
        assertEquals(1.0, result.doubleValue(), 1e-6, "sin(π/2) should be approximately 1");
    }

    @Test
    public void testSeriesCos() {
        Series<Real> cosSeries = Series.cos(15);

        Real result = cosSeries.evaluate(Real.ZERO);
        assertEquals(1.0, result.doubleValue(), EPSILON, "cos(0) should be 1");

        result = cosSeries.evaluate(Real.of(Math.PI));
        assertEquals(-1.0, result.doubleValue(), 1e-6, "cos(π) should be approximately -1");
    }

    @Test
    public void testSeriesAddition() {
        Series<Real> series1 = Series.exp(5);
        Series<Real> series2 = Series.exp(5);

        Series<Real> sum = series1.add(series2);
        Real result = sum.evaluate(Real.ZERO);
        assertEquals(2.0, result.doubleValue(), EPSILON, "exp(0) + exp(0) should be 2");
    }

    @Test
    public void testSeriesMultiplication() {
        java.util.List<Real> coeffs1 = java.util.Arrays.asList(Real.ONE, Real.ONE);
        Series<Real> series1 = Series.maclaurin(coeffs1, REALS);

        java.util.List<Real> coeffs2 = java.util.Arrays.asList(Real.ONE, Real.ONE);
        Series<Real> series2 = Series.maclaurin(coeffs2, REALS);

        Series<Real> product = series1.multiply(series2);
        Real result = product.evaluate(Real.of(2));
        assertEquals(9.0, result.doubleValue(), EPSILON, "(1+x)² at x=2 should be 9");
    }

    @Test
    public void testSeriesDerivative() {
        java.util.List<Real> coeffs = java.util.Arrays.asList(Real.ZERO, Real.ZERO, Real.ONE);
        Series<Real> series = Series.maclaurin(coeffs, REALS);

        Series<Real> derivative = series.derivative();
        Real result = derivative.evaluate(Real.of(3));
        assertEquals(6.0, result.doubleValue(), EPSILON, "Derivative of x² at x=3 should be 6");
    }

    @Test
    public void testSeriesCoefficients() {
        Series<Real> expSeries = Series.exp(5);

        assertEquals(1.0, expSeries.getCoefficient(0).doubleValue(), EPSILON, "a₀ should be 1");
        assertEquals(1.0, expSeries.getCoefficient(1).doubleValue(), EPSILON, "a₁ should be 1");
        assertEquals(0.5, expSeries.getCoefficient(2).doubleValue(), EPSILON, "a₂ should be 1/2");
        assertEquals(1.0 / 6, expSeries.getCoefficient(3).doubleValue(), EPSILON, "a₃ should be 1/6");
    }

    // ========== Integration Tests ==========

    @Test
    public void testProductRule() {
        Variable<Real> x = new Variable<>("x", REALS);
        ConstantExpression<Real> two = new ConstantExpression<>(Real.of(2), REALS);

        ProductExpression<Real> product = new ProductExpression<>(two, x, REALS);
        Expression<Real> derivative = product.differentiate(x);

        Map<Variable<Real>, Real> assignments = new HashMap<>();
        assignments.put(x, Real.of(5));

        Real result = derivative.evaluate(assignments);
        assertEquals(2.0, result.doubleValue(), EPSILON, "Derivative of 2x should be 2");
    }

    @Test
    public void testSumRule() {
        Variable<Real> x = new Variable<>("x", REALS);
        ConstantExpression<Real> three = new ConstantExpression<>(Real.of(3), REALS);

        SumExpression<Real> sum = new SumExpression<>(three, x, REALS);
        Expression<Real> derivative = sum.differentiate(x);

        Map<Variable<Real>, Real> assignments = new HashMap<>();
        assignments.put(x, Real.of(10));

        Real result = derivative.evaluate(assignments);
        assertEquals(1.0, result.doubleValue(), EPSILON, "Derivative of (3 + x) should be 1");
    }
}
