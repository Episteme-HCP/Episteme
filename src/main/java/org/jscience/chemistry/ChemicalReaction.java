package org.jscience.chemistry;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.AmountOfSubstance;
import org.jscience.measure.quantity.Energy;
import org.jscience.measure.quantity.Frequency;
import org.jscience.measure.quantity.Temperature;

/**
 * Chemical reaction with equation balancing and stoichiometry.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class ChemicalReaction {

    private final Map<String, Integer> reactants; // Compound -> coefficient
    private final Map<String, Integer> products; // Compound -> coefficient
    private final String equation;
    private Quantity<Energy> enthalpy; // ΔH
    private Quantity<Energy> gibbsFreeEnergy; // ΔG
    private Quantity<Energy> activationEnergy; // Ea
    private Quantity<Frequency> preExponentialFactor; // A (Arrhenius) - simplified unit inverse time

    private ChemicalReaction(Map<String, Integer> reactants, Map<String, Integer> products, String equation) {
        this.reactants = reactants;
        this.products = products;
        this.equation = equation;
        this.enthalpy = Quantities.create(0, Units.JOULE);
        this.gibbsFreeEnergy = Quantities.create(0, Units.JOULE);
        this.activationEnergy = Quantities.create(0, Units.JOULE);
        this.preExponentialFactor = Quantities.create(0, Units.HERTZ); // 1/s
    }

    // ... parse methods ...

    // --- Thermodynamics & Kinetics ---

    public void setGibbsFreeEnergy(Quantity<Energy> gibbs) {
        this.gibbsFreeEnergy = gibbs;
    }

    public Quantity<Energy> getGibbsFreeEnergy() {
        return gibbsFreeEnergy;
    }

    public void setKinetics(Quantity<Energy> activationEnergy, Quantity<Frequency> preExponentialFactor) {
        this.activationEnergy = activationEnergy;
        this.preExponentialFactor = preExponentialFactor;
    }

    /**
     * Calculates the reaction rate constant k using the Arrhenius equation.
     * k = A * exp(-Ea / (R * T))
     * 
     * @param temperature The temperature T.
     * @return The rate constant k (in 1/s for first order).
     */
    public Quantity<Frequency> calculateRateConstant(Quantity<Temperature> temperature) {
        double R = 8.314462618; // J/(mol*K)
        double T = temperature.to(Units.KELVIN).getValue().doubleValue();
        double Ea = activationEnergy.to(Units.JOULE).getValue().doubleValue(); // J (usually per mol in context, but
                                                                               // here absolute?)
        // Assuming Ea is J/mol (standard chemistry context). If Quantity is just Energy
        // (J), we assume per mole implicitly for the single reaction event?
        // Standard Arrhenius uses Ea in J/mol.

        double exponent = -Ea / (R * T);
        double expVal = Math.exp(exponent);

        return preExponentialFactor.multiply(expVal);
    }

    /**
     * Parse a chemical equation string.
     * Format: "2H2 + O2 -> 2H2O" or "CH4 + 2O2 = CO2 + 2H2O"
     */
    public static ChemicalReaction parse(String equation) {
        String[] sides = equation.split("->|=|→");
        if (sides.length != 2) {
            throw new IllegalArgumentException("Invalid equation format: " + equation);
        }

        Map<String, Integer> reactants = parseCompounds(sides[0].trim());
        Map<String, Integer> products = parseCompounds(sides[1].trim());

        return new ChemicalReaction(reactants, products, equation);
    }

    private static Map<String, Integer> parseCompounds(String side) {
        Map<String, Integer> result = new HashMap<>();
        Pattern pattern = Pattern.compile("(\\d*)\\s*([A-Z][a-zA-Z0-9()]+)");

        for (String term : side.split("\\+")) {
            term = term.trim();
            Matcher matcher = pattern.matcher(term);
            if (matcher.matches()) {
                int coeff = matcher.group(1).isEmpty() ? 1 : Integer.parseInt(matcher.group(1));
                String compound = matcher.group(2);
                result.put(compound, coeff);
            } else if (!term.isEmpty()) {
                // Simple compound without coefficient
                result.put(term.replaceAll("\\s+", ""), 1);
            }
        }
        return result;
    }

    /**
     * Count atoms of each element on reactant side.
     */
    public Map<String, Integer> countReactantAtoms() {
        return countAtoms(reactants);
    }

    /**
     * Count atoms of each element on product side.
     */
    public Map<String, Integer> countProductAtoms() {
        return countAtoms(products);
    }

    private Map<String, Integer> countAtoms(Map<String, Integer> compounds) {
        Map<String, Integer> atoms = new HashMap<>();
        Pattern elementPattern = Pattern.compile("([A-Z][a-z]?)(\\d*)");

        for (Map.Entry<String, Integer> entry : compounds.entrySet()) {
            String formula = entry.getKey();
            int coeff = entry.getValue();

            Matcher matcher = elementPattern.matcher(formula);
            while (matcher.find()) {
                String element = matcher.group(1);
                int count = matcher.group(2).isEmpty() ? 1 : Integer.parseInt(matcher.group(2));
                atoms.merge(element, coeff * count, (a, b) -> a + b);
            }
        }
        return atoms;
    }

    /**
     * Check if the equation is balanced.
     */
    public boolean isBalanced() {
        Map<String, Integer> reactantAtoms = countReactantAtoms();
        Map<String, Integer> productAtoms = countProductAtoms();

        if (!reactantAtoms.keySet().equals(productAtoms.keySet())) {
            return false;
        }

        for (String element : reactantAtoms.keySet()) {
            if (!reactantAtoms.get(element).equals(productAtoms.get(element))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculate moles of product given moles of a limiting reactant.
     */
    public Quantity<AmountOfSubstance> stoichiometry(String reactant, Quantity<AmountOfSubstance> molesReactant,
            String product) {
        Integer reactantCoeff = reactants.get(reactant);
        Integer productCoeff = products.get(product);

        if (reactantCoeff == null) {
            throw new IllegalArgumentException("Reactant not found: " + reactant);
        }
        if (productCoeff == null) {
            throw new IllegalArgumentException("Product not found: " + product);
        }

        double moleRatio = (double) productCoeff / reactantCoeff;
        return molesReactant.multiply(moleRatio);
    }

    /**
     * Calculate mass of product from mass of reactant.
     */
    public Quantity<Mass> massToMass(String reactant, Quantity<Mass> massReactant, Quantity<Mass> reactantMW,
            String product, Quantity<Mass> productMW) {

        // Moles = Mass / MW
        // Units.UNIFIED_ATOMIC_MASS is usually for single molecules. Molar mass often
        // in g/mol.
        // Assuming MW is provided in g/mol or compatible unit.

        double molesValue = massReactant.to(Units.GRAM).getValue().doubleValue()
                / reactantMW.to(Units.GRAM).getValue().doubleValue();
        Quantity<AmountOfSubstance> molesReactant = Quantities.create(molesValue, Units.MOLE);

        Quantity<AmountOfSubstance> molesProduct = stoichiometry(reactant, molesReactant, product);

        double productMassValue = molesProduct.getValue().doubleValue()
                * productMW.to(Units.GRAM).getValue().doubleValue();
        return Quantities.create(productMassValue, Units.GRAM);
    }

    public void setEnthalpy(Quantity<Energy> enthalpy) {
        this.enthalpy = enthalpy;
    }

    public Quantity<Energy> getEnthalpy() {
        return enthalpy;
    }

    // --- Accessors ---

    public Map<String, Integer> getReactants() {
        return new HashMap<>(reactants);
    }

    public Map<String, Integer> getProducts() {
        return new HashMap<>(products);
    }

    public String getEquation() {
        return equation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for (Map.Entry<String, Integer> e : reactants.entrySet()) {
            if (!first)
                sb.append(" + ");
            if (e.getValue() > 1)
                sb.append(e.getValue());
            sb.append(e.getKey());
            first = false;
        }

        sb.append(" → ");

        first = true;
        for (Map.Entry<String, Integer> e : products.entrySet()) {
            if (!first)
                sb.append(" + ");
            if (e.getValue() > 1)
                sb.append(e.getValue());
            sb.append(e.getKey());
            first = false;
        }

        if (enthalpy.getValue().doubleValue() != 0) {
            sb.append(" (ΔH = ").append(enthalpy).append(")");
        }

        return sb.toString();
    }

    // Removed hardcoded factory methods (combustionMethane, etc.) to encourage data
    // loading.
}
