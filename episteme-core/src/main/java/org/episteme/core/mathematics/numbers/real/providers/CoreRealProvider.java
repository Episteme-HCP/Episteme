package org.episteme.core.mathematics.numbers.real.providers;

import java.math.BigDecimal;
import com.google.auto.service.AutoService;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.numbers.real.RealProvider;

/**
 * Standard Java-based implementation of {@link RealProvider}.
 * Using {@link RealBig} backed by {@code BigDecimalMath}.
 */
@AutoService(RealProvider.class)
public class CoreRealProvider implements RealProvider {

    @Override
    public String getName() {
        return "Core Real Provider (BigMath)";
    }

    @Override
    public int getPriority() {
        return 10; // Standard Java implementation
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public Real create(BigDecimal value) {
        return RealBig.create(value);
    }

    @Override
    public Real of(String value) {
        return RealBig.of(value);
    }
}
