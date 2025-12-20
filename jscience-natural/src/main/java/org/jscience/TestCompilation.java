package org.jscience;

import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;
import javax.measure.Quantity;
import javax.measure.quantity.Length;

public class TestCompilation {
    public void test() {
        // Try array (varargs overload explictly?)
        // getQuantity(Number[], Unit...)
        Quantity<Length> q1 = Quantities.getQuantity(new Number[] { 10.0 }, Units.METRE);

        // Try single arg if possible? No.
    }
}
