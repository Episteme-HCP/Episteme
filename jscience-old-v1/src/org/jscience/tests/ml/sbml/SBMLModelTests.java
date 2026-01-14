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

package org.jscience.ml.sbml.tests;

import junit.framework.TestCase;
import org.jscience.ml.sbml.*;

import java.util.Iterator;
import java.util.List;

/**
 * A collection of test methods for pieces of an SBML model.
 * <p/>
 * <p/>
 * This code is licensed under the DARPA BioCOMP Open Source License.  See LICENSE for more details.
 * </p>
 *
 * @author Nicholas Allen
 */

public abstract class SBMLModelTests extends TestCase {

    protected Model model;

    /**
     * Creates a new test for an SBML model.
     *
     * @param name Test name
     */

    public SBMLModelTests(String name) {
        super(name);
    }

    protected abstract void setUp() throws Exception;

    protected Compartment testCompartment(String name, boolean constant, String outside, double size, int dimensions, String units) {
        for (Iterator iterator = model.getCompartments().iterator(); iterator.hasNext();) {
            Compartment compartment = (Compartment) iterator.next();
            if (!compartment.getName().equals(name))
                continue;
            assertTrue(compartment.isConstant() == constant);
            assertTrue(stringEquals(compartment.getOutside(), outside));
            assertTrue(Double.isNaN(size) && compartment.getSize() == null || size == Double.parseDouble(compartment.getSize()));
            assertTrue(dimensions == Integer.parseInt(compartment.getSpatialDimensions()));
            assertTrue(stringEquals(compartment.getUnits(), units));
            return compartment;
        }
        fail();
        return null;
    }

    protected FunctionDefinition testFunctionDefinition(String name) {
        for (Iterator iterator = model.getFunctionDefinitions().iterator(); iterator.hasNext();) {
            FunctionDefinition definition = (FunctionDefinition) iterator.next();
            if (!definition.getName().equals(name))
                continue;
            return definition;
        }
        fail();
        return null;
    }

    protected Parameter testParameter(String name, double value) {
        return testParameter(model.getParameters().iterator(), name, value, null, true);
    }

    protected Parameter testParameter(String name, double value, String unit, boolean constant) {
        return testParameter(model.getParameters().iterator(), name, value, unit, constant);
    }

    protected Parameter testParameter(Reaction reaction, String name, double value) {
        return testParameter(reaction.getKineticLaw().getParameter().iterator(), name, value, null, true);
    }

    protected Reaction testReaction(String name, boolean fast, boolean reversible) {
        for (Iterator iterator = model.getReactions().iterator(); iterator.hasNext();) {
            Reaction reaction = (Reaction) iterator.next();
            if (!reaction.getName().equals(name))
                continue;
            assertTrue(reaction.isFast() == fast);
            assertTrue(reaction.isReversible() == reversible);
            return reaction;
        }
        fail();
        return null;
    }

    protected ModifierSpeciesReference testReactionModifier(List modifiers, String name) {
        for (Iterator iterator = modifiers.iterator(); iterator.hasNext();) {
            ModifierSpeciesReference reference = (ModifierSpeciesReference) iterator.next();
            if (!reference.getSpecies().equals(name))
                continue;
            return reference;
        }
        fail();
        return null;
    }

    protected SpeciesReference testReactionModifier(List modifiers, String name, double stoichiometry) {
        for (Iterator iterator = modifiers.iterator(); iterator.hasNext();) {
            SpeciesReference reference = (SpeciesReference) iterator.next();
            if (!reference.getSpecies().equals(name))
                continue;
            assertTrue(stoichiometry == Double.parseDouble(reference.getStoichiometry()));
            return reference;
        }
        fail();
        return null;
    }

    protected Species testSpecies(String name, String compartment, double initialConcentration, boolean constant, boolean boundaryCondition) {
        for (Iterator iterator = model.getSpecies().iterator(); iterator.hasNext();) {
            Species species = (Species) iterator.next();
            if (!species.getName().equals(name))
                continue;
            assertTrue(stringEquals(species.getCompartment(), compartment));
            assertTrue(doubleEquals(species.getInitialConcentration(), initialConcentration));
            assertTrue(species.isConstant() == constant);
            assertTrue(species.isBoundaryCondition() == boundaryCondition);
            return species;
        }
        fail();
        return null;
    }

    protected void testUnit(Unit unit, int exponent, String kindName, double multiplier, double offset, int scale) {
        assertTrue(unit.getExponent() == exponent);
        assertTrue(unit.getKindName().equals(kindName));
        assertTrue(unit.getMultiplier() == multiplier);
        assertTrue(unit.getOffset() == offset);
        assertTrue(unit.getScale() == scale);
    }

    protected UnitDefinition testUnitDefinition(String name, int size) {
        for (Iterator iterator = model.getUnitDefinitions().iterator(); iterator.hasNext();) {
            UnitDefinition definition = (UnitDefinition) iterator.next();
            if (definition.getName().equals(name)) {
                assertTrue(definition.getUnits().size() == size);
                return definition;
            }
        }
        fail();
        return null;
    }

    private boolean doubleEquals(double d1, double d2) {
        return d1 == d2 || Double.isNaN(d1) && Double.isNaN(d2);
    }

    private boolean stringEquals(String s1, String s2) {
        return s1 == s2 || s1.equals(s2);
    }

    private Parameter testParameter(Iterator iterator, String name, double value, String unit, boolean constant) {
        while (iterator.hasNext()) {
            Parameter parameter = (Parameter) iterator.next();
            if (!parameter.getName().equals(name))
                continue;
            assertTrue(doubleEquals(parameter.getValue(), value));
            assertTrue(stringEquals(parameter.getUnits(), unit));
            assertTrue(parameter.isConstant() == constant);
            return parameter;
        }
        fail();
        return null;
    }
}
