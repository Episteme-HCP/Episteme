package org.jscience.chemistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The Periodic Table of Elements.
 * <p>
 * Provides access to all 118 elements by symbol, name, or atomic number.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class PeriodicTable {

    private static final Map<Integer, Element> BY_NUMBER = new HashMap<>();
    private static final Map<String, Element> BY_SYMBOL = new HashMap<>();
    private static final Map<String, Element> BY_NAME = new HashMap<>();

    static {
        loadElements();
    }

    private static void loadElements() {
        try {
            // Load from org/jscience/chemistry/elements.json
            for (Element e : PeriodicTableLoader.load("/org/jscience/chemistry/elements.json")) {
                registerElement(e);
            }
        } catch (Exception e) {
            java.util.logging.Logger.getLogger("PeriodicTable")
                    .warning("Could not load elements.json: " + e.getMessage());
            // Fallback could be hardcoded logic if crucial, or just empty.
        }
    }

    public static void registerElement(Element e) {
        BY_NUMBER.put(e.getAtomicNumber(), e);
        BY_SYMBOL.put(e.getSymbol().toUpperCase(), e);
        BY_NAME.put(e.getName().toUpperCase(), e);
    }

    // --- Lookup methods ---

    public static Optional<Element> byNumber(int atomicNumber) {
        return Optional.ofNullable(BY_NUMBER.get(atomicNumber));
    }

    public static Optional<Element> bySymbol(String symbol) {
        return Optional.ofNullable(BY_SYMBOL.get(symbol.toUpperCase()));
    }

    public static Optional<Element> byName(String name) {
        return Optional.ofNullable(BY_NAME.get(name.toUpperCase()));
    }

    // --- Common elements as constants ---
    // Note: These will only be non-null after class loading if logic succeeds.
    public static final Element HYDROGEN = BY_NUMBER.get(1);
    public static final Element HELIUM = BY_NUMBER.get(2);
    public static final Element CARBON = BY_NUMBER.get(6);
    public static final Element NITROGEN = BY_NUMBER.get(7);
    public static final Element OXYGEN = BY_NUMBER.get(8);
    public static final Element SODIUM = BY_NUMBER.get(11);
    public static final Element CHLORINE = BY_NUMBER.get(17);
    public static final Element IRON = BY_NUMBER.get(26);
    public static final Element COPPER = BY_NUMBER.get(29);
    public static final Element SILVER = BY_NUMBER.get(47);
    public static final Element GOLD = BY_NUMBER.get(79);
    public static final Element URANIUM = BY_NUMBER.get(92);

    /**
     * Number of elements in the table.
     */
    public static int size() {
        return BY_NUMBER.size();
    }

    /**
     * All elements.
     */
    public static java.util.Collection<Element> all() {
        return BY_NUMBER.values();
    }
}
