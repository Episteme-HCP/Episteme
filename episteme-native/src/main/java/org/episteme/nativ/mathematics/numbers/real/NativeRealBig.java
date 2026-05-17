/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.numbers.real;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.ref.Cleaner;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;

import static org.episteme.nativ.mathematics.numbers.real.backends.NativeMPFRNumbers.*;

/**
 * High-precision real number backed by the native MPFR library.
 * This implementation provides maximum performance by keeping data in native memory
 * and performing all operations via the Panama API.
 *
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class NativeRealBig extends Real {

    private final MemorySegment ptr;
    private final Arena arena;
    private final long precision;

    @SuppressWarnings("unused")
    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("win");

    private static final Cleaner CLEANER = Cleaner.create();

    /**
     * Creates a new NativeRealBig with the specified value and precision.
     */
    public NativeRealBig(String value, long precision) {
        this.arena = Arena.ofAuto();
        this.precision = precision;
        this.ptr = NativeSafe.allocate(arena, MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, ptr, c_long(precision));
        
        // Ensure manual cleanup via MPFR_CLEAR if the arena is not automatically cleaning up correctly
        // but since we use Arena.ofAuto(), GC will handle the MemorySegment, but mpfr_t internal
        // memory (limbs) must be freed via mpfr_clear.
        CLEANER.register(this, () -> {
             try {
                 // Deterministic cleanup: mpfr_clear must be called to free internal limbs
                 // Even if the struct itself is in an Arena, the data it points to is not.
                 NativeSafe.invoke(MPFR_CLEAR, ptr);
             } catch (Exception e) {}
        });
        
        String mpfrValue = value;
        if (value.equalsIgnoreCase("nan")) mpfrValue = "@NaN@";
        else if (value.equalsIgnoreCase("infinity") || value.equalsIgnoreCase("inf")) mpfrValue = "@Inf@";
        else if (value.equalsIgnoreCase("-infinity") || value.equalsIgnoreCase("-inf")) mpfrValue = "-@Inf@";

        MemorySegment str = NativeSafe.allocateFrom(arena, mpfrValue);
        NativeSafe.invoke(MPFR_SET_STR, ptr, str, 10, 0); // 0 = RNDN
    }

    /**
     * Internal constructor for operations.
     */
    private NativeRealBig(long precision) {
        this.arena = Arena.ofAuto();
        this.precision = precision;
        this.ptr = NativeSafe.allocate(arena, MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, ptr, c_long(precision));
    }

    public static NativeRealBig of(String value) {
        long prec = org.episteme.core.mathematics.context.MathContext.getCurrent().getPrecisionBits();
        return new NativeRealBig(value, prec);
    }

    /**
     * Creates a new NativeRealBig with specified precision, initialized to zero.
     */
    public static NativeRealBig createEmpty(long precision) {
        return new NativeRealBig(precision);
    }

    /**
     * Creates a new NativeRealBig with specified value and precision.
     */
    public static NativeRealBig of(String value, long precision) {
        return new NativeRealBig(value, precision);
    }

    /**
     * Efficiently creates a new NativeRealBig by copying from an existing native mpfr_t.
     */
    public static NativeRealBig copyFrom(MemorySegment sourcePtr, long precision) {
        NativeRealBig res = new NativeRealBig(precision);
        NativeSafe.invoke(MPFR_SET, res.ptr, sourcePtr, 0);
        return res;
    }

    public static NativeRealBig of(BigDecimal value) {
        return of(value.toPlainString());
    }

    /**
     * Public access to the underlying mpfr_t pointer for native backends.
     */
    public MemorySegment getPtr() {
        return ptr;
    }

    @Override
    public Real add(Real other) {
        NativeRealBig result = new NativeRealBig(precision);
        NativeRealBig o = (other instanceof NativeRealBig n) ? n : NativeRealBig.of(other.bigDecimalValue());
        NativeSafe.invoke(MPFR_ADD, result.ptr, this.ptr, o.ptr, 0);
        return result;
    }

    @Override
    public Real subtract(Real other) {
        NativeRealBig result = new NativeRealBig(precision);
        NativeRealBig o = (other instanceof NativeRealBig n) ? n : NativeRealBig.of(other.bigDecimalValue());
        NativeSafe.invoke(MPFR_SUB, result.ptr, this.ptr, o.ptr, 0);
        return result;
    }

    @Override
    public Real multiply(Real other) {
        NativeRealBig result = new NativeRealBig(precision);
        NativeRealBig o = (other instanceof NativeRealBig n) ? n : NativeRealBig.of(other.bigDecimalValue());
        NativeSafe.invoke(MPFR_MUL, result.ptr, this.ptr, o.ptr, 0);
        return result;
    }

    @Override
    public Real divide(Real other) {
        NativeRealBig result = new NativeRealBig(precision);
        NativeRealBig o = (other instanceof NativeRealBig n) ? n : NativeRealBig.of(other.bigDecimalValue());
        NativeSafe.invoke(MPFR_DIV, result.ptr, this.ptr, o.ptr, 0);
        return result;
    }

    @Override
    public Real negate() {
        NativeRealBig result = new NativeRealBig(precision);
        NativeSafe.invoke(MPFR_NEG, result.ptr, this.ptr, 0);
        return result;
    }

    @Override
    public Real abs() {
        NativeRealBig result = new NativeRealBig(precision);
        NativeSafe.invoke(MPFR_ABS, result.ptr, this.ptr, 0);
        return result;
    }

    @Override
    public Real inverse() {
        NativeRealBig result = new NativeRealBig(precision);
        NativeRealBig one = NativeRealBig.of("1");
        NativeSafe.invoke(MPFR_DIV, result.ptr, one.ptr, this.ptr, 0);
        return result;
    }

    @Override
    public Real sqrt() {
        if (this.isNaN()) return NaN;
        if (this.isZero()) return ZERO;
        if (this.sign() < 0) return Real.NaN;
        NativeRealBig result = new NativeRealBig(precision);
        NativeSafe.invoke(MPFR_SQRT, result.ptr, this.ptr, 0);
        return result;
    }

    @Override
    public Real pow(int exp) {
        return pow((double) exp);
    }

    @Override
    public Real pow(Real exp) {
        NativeRealBig result = new NativeRealBig(precision);
        NativeRealBig o = (exp instanceof NativeRealBig n) ? n : NativeRealBig.of(exp.bigDecimalValue());
        NativeSafe.invoke(MPFR_POW, result.ptr, this.ptr, o.ptr, 0);
        return result;
    }

    @Override
    public boolean isZero() {
        return ((Number) NativeSafe.invoke(MPFR_ZERO_P, this.ptr)).intValue() != 0;
    }

    @Override
    public boolean isOne() {
        return bigDecimalValue().compareTo(BigDecimal.ONE) == 0;
    }

    @Override
    public boolean isNaN() {
        return ((Number) NativeSafe.invoke(MPFR_NAN_P, this.ptr)).intValue() != 0;
    }

    @Override
    public boolean isInfinite() {
        return ((Number) NativeSafe.invoke(MPFR_INF_P, this.ptr)).intValue() != 0;
    }

    @Override
    public double doubleValue() {
        if (isNaN()) return Double.NaN;
        if (isInfinite()) {
            return (((Number) NativeSafe.invoke(MPFR_CMP_SI, this.ptr, c_long(0L))).intValue() > 0) ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        return (double) NativeSafe.invoke(MPFR_GET_D, this.ptr, 0); // 0 is rounding mode (usually RNDN)
    }

    @Override
    public BigDecimal bigDecimalValue() {
        try (Arena local = Arena.ofConfined()) {
            MemorySegment expPtr = local.allocate(C_LONG);
            long bufSize = 10000;
            MemorySegment buf = local.allocate(bufSize);
            // Use n=0 to get the minimal number of digits required to reconstruct the value exactly.
            // This prevents "binary noise" from showing up as extra digits in decimal representation.
            MemorySegment strPtr = (MemorySegment) NativeSafe.invoke(MPFR_GET_STR, buf, expPtr, 10, 0L, ptr, 0); 
            
            if (strPtr.equals(MemorySegment.NULL)) return BigDecimal.ZERO;

            String digits = strPtr.reinterpret(bufSize).getString(0);
            long exp = (C_LONG.byteSize() == 4) ? expPtr.get(ValueLayout.JAVA_INT, 0L) : expPtr.get(ValueLayout.JAVA_LONG, 0L);
            
            if (digits == null || digits.isEmpty() || digits.equals("0")) return BigDecimal.ZERO;
            
            String sign = "";
            if (digits.startsWith("-")) {
                sign = "-";
                digits = digits.substring(1);
            }
            
            long effectiveScale = (long) digits.length() - exp;
            try {
                BigInteger unscaled = new BigInteger(sign + digits);
                return new BigDecimal(unscaled, (int) effectiveScale);
            } catch (NumberFormatException e) {
                // Fallback for any other unexpected MPFR strings
                return BigDecimal.ZERO;
            } finally {
                // mpfr_get_str uses the provided buffer if it's not NULL, but it's good practice 
                // to ensure we don't have dangling pointers.
            }
        } catch (Throwable t) {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public Real exp() {
        NativeRealBig res = new NativeRealBig(precision);
        NativeSafe.invoke(MPFR_EXP, res.ptr, this.ptr, 0);
        return res;
    }

    @Override
    public Real log() {
        if (this.isNaN()) return NaN;
        if (this.isZero()) return Real.NEGATIVE_INFINITY;
        if (this.sign() < 0) return Real.NaN;
        NativeRealBig res = new NativeRealBig(precision);
        NativeSafe.invoke(MPFR_LOG, res.ptr, this.ptr, 0);
        return res;
    }

    @Override
    public Real log10() {
        if (this.isNaN()) return NaN;
        if (this.isZero()) return Real.NEGATIVE_INFINITY;
        if (this.sign() < 0) return Real.NaN;
        NativeRealBig res = new NativeRealBig(precision);
        NativeSafe.invoke(MPFR_LOG10, res.ptr, this.ptr, 0);
        return res;
    }

    @Override
    public Real sin() {
        NativeRealBig res = new NativeRealBig(precision);
        NativeSafe.invoke(MPFR_SIN, res.ptr, this.ptr, 0);
        return res;
    }

    @Override
    public Real cos() {
        NativeRealBig res = new NativeRealBig(precision);
        NativeSafe.invoke(MPFR_COS, res.ptr, this.ptr, 0);
        return res;
    }

    @Override
    public Real tan() {
        NativeRealBig res = new NativeRealBig(precision);
        NativeSafe.invoke(MPFR_TAN, res.ptr, this.ptr, 0);
        return res;
    }

    @Override
    public Real asin() {
        NativeRealBig res = new NativeRealBig(precision);
        NativeSafe.invoke(MPFR_ASIN, res.ptr, this.ptr, 0);
        return res;
    }

    @Override
    public Real acos() {
        NativeRealBig res = new NativeRealBig(precision);
        NativeSafe.invoke(MPFR_ACOS, res.ptr, this.ptr, 0);
        return res;
    }

    @Override
    public Real atan() {
        NativeRealBig res = new NativeRealBig(precision);
        NativeSafe.invoke(MPFR_ATAN, res.ptr, this.ptr, 0);
        return res;
    }

    @Override
    public Real atan2(Real x) {
        NativeRealBig res = new NativeRealBig(precision);
        NativeRealBig nx = (x instanceof NativeRealBig n) ? n : NativeRealBig.of(x.bigDecimalValue());
        NativeSafe.invoke(MPFR_ATAN2, res.ptr, this.ptr, nx.ptr, 0);
        return res;
    }

    @Override
    public Real sinh() {
        NativeRealBig res = new NativeRealBig(precision);
        NativeSafe.invoke(MPFR_SINH, res.ptr, this.ptr, 0);
        return res;
    }

    @Override
    public Real cosh() {
        NativeRealBig res = new NativeRealBig(precision);
        NativeSafe.invoke(MPFR_COSH, res.ptr, this.ptr, 0);
        return res;
    }

    @Override
    public Real tanh() {
        NativeRealBig res = new NativeRealBig(precision);
        NativeSafe.invoke(MPFR_TANH, res.ptr, this.ptr, 0);
        return res;
    }

    @Override
    public Real asinh() {
        NativeRealBig res = new NativeRealBig(precision);
        NativeSafe.invoke(MPFR_ASINH, res.ptr, this.ptr, 0);
        return res;
    }

    @Override
    public Real acosh() {
        NativeRealBig res = new NativeRealBig(precision);
        NativeSafe.invoke(MPFR_ACOSH, res.ptr, this.ptr, 0);
        return res;
    }

    @Override
    public Real atanh() {
        NativeRealBig res = new NativeRealBig(precision);
        NativeSafe.invoke(MPFR_ATANH, res.ptr, this.ptr, 0);
        return res;
    }

    @Override
    public Real cbrt() {
        NativeRealBig res = new NativeRealBig(precision);
        NativeSafe.invoke(MPFR_CBRT, res.ptr, this.ptr, 0);
        return res;
    }

    @Override
    public Real hypot(Real y) {
        NativeRealBig res = new NativeRealBig(precision);
        NativeRealBig ny = (y instanceof NativeRealBig n) ? n : NativeRealBig.of(y.bigDecimalValue());
        NativeSafe.invoke(MPFR_HYPOT, res.ptr, this.ptr, ny.ptr, 0);
        return res;
    }

    @Override
    public Real pow(double exponent) {
        NativeRealBig res = new NativeRealBig(precision);
        NativeRealBig ne = NativeRealBig.of(String.valueOf(exponent));
        NativeSafe.invoke(MPFR_POW, res.ptr, this.ptr, ne.ptr, 0);
        return res;
    }

    @Override
    public Real ceil() {
        // mpfr_ceil (rop, op) -> mpfr_rint_ceil
        return NativeRealBig.of(bigDecimalValue().setScale(0, java.math.RoundingMode.CEILING));
    }
    @Override
    public Real floor() {
        return NativeRealBig.of(bigDecimalValue().setScale(0, java.math.RoundingMode.FLOOR));
    }
    @Override
    public Real round() {
        return NativeRealBig.of(bigDecimalValue().setScale(0, java.math.RoundingMode.HALF_UP));
    }
    
    @Override
    public int compareTo(Real other) {
        if (other instanceof NativeRealBig n) {
            return ((Number) NativeSafe.invoke(MPFR_CMP, this.ptr, n.ptr)).intValue();
        }
        return bigDecimalValue().compareTo(other.bigDecimalValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Real r) return compareTo(r) == 0;
        return false;
    }

    @Override
    public int hashCode() {
        return bigDecimalValue().hashCode();
    }

    @Override
    public String toString() {
        if (isNaN()) return "NaN";
        if (isInfinite()) {
            return (((Number) NativeSafe.invoke(MPFR_CMP_SI, this.ptr, c_long(0L))).intValue() > 0) ? "Infinity" : "-Infinity";
        }
        return bigDecimalValue().toPlainString();
    }
}


