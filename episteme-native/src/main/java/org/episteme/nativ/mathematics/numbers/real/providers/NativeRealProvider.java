package org.episteme.nativ.mathematics.numbers.real.providers;

import java.math.BigDecimal;
import com.google.auto.service.AutoService;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealProvider;
import org.episteme.nativ.mathematics.numbers.real.NativeRealBig;
import org.episteme.nativ.mathematics.numbers.real.backends.NativeMPFRNumbers;

/**
 * Native MPFR-based implementation of {@link RealProvider}.
 * Using {@link NativeRealBig} backed by Project Panama.
 */
@AutoService(RealProvider.class)
public class NativeRealProvider implements RealProvider {

    @Override
    public String getName() {
        return "Native MPFR Provider";
    }

    @Override
    public int getPriority() {
        return 100; // Native implementation is preferred
    }

    @Override
    public boolean isAvailable() {
        return NativeMPFRNumbers.isAvailable();
    }

    @Override
    public Real create(BigDecimal value) {
        return NativeRealBig.of(value);
    }

    @Override
    public Real of(String value) {
        return NativeRealBig.of(value);
    }

    @Override
    public Real getConstant(String name) {
        if (!isAvailable()) return null;
        long precision = org.episteme.core.mathematics.context.MathContext.getCurrent().getPrecisionBits();
        return NativeMPFRNumbers.getConstant(name, precision);
    }
}
