package org.jscience.chemistry.loaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.jscience.chemistry.Element;
import org.jscience.chemistry.PeriodicTable;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.MassDensity;

import java.io.InputStream;
import java.util.List;

import java.util.logging.Logger;

/**
 * Loads chemistry data (elements, molecules) from JSON.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class ChemistryDataLoader {

    private static final Logger LOGGER = Logger.getLogger(ChemistryDataLoader.class.getName());
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final java.util.Map<String, org.jscience.chemistry.Molecule> MOLECULE_CACHE = new java.util.HashMap<>();

    public static void loadElements() {
        System.out.println("DEBUG: ChemistryDataLoader.loadElements() called.");
        try (InputStream is = ChemistryDataLoader.class.getResourceAsStream("/org/jscience/chemistry/elements.json")) {
            if (is == null) {
                System.out.println("DEBUG: elements.json NOT FOUND in /org/jscience/chemistry/");
                LOGGER.warning("elements.json not found in /org/jscience/chemistry/");
                return;
            }
            System.out.println("DEBUG: elements.json found. Parsing...");

            ElementListWrapper wrapper = MAPPER.readValue(is, ElementListWrapper.class);
            List<ElementData> elements = wrapper.elements;

            // Populate PeriodicTable
            for (ElementData data : elements) {
                Element.ElementCategory cat = Element.ElementCategory.UNKNOWN;
                String catStr = data.category.toUpperCase().replace(" ", "_").replace("-", "_");
                if (catStr.contains("DIATOMIC") || catStr.contains("POLYATOMIC")) {
                    catStr = "NONMETAL"; // Simplify
                }
                try {
                    cat = Element.ElementCategory.valueOf(catStr);
                } catch (Exception e) {
                    // Try primitive mapping
                    if (catStr.contains("NOBLE"))
                        cat = Element.ElementCategory.NOBLE_GAS;
                    else if (catStr.contains("ALKALI") && !catStr.contains("EARTH"))
                        cat = Element.ElementCategory.ALKALI_METAL;
                    else if (catStr.contains("ALKALINE"))
                        cat = Element.ElementCategory.ALKALINE_EARTH_METAL;
                    else if (catStr.contains("TRANSITION"))
                        cat = Element.ElementCategory.TRANSITION_METAL;
                    else if (catStr.contains("LANTHANIDE"))
                        cat = Element.ElementCategory.LANTHANIDE;
                    else if (catStr.contains("ACTINIDE"))
                        cat = Element.ElementCategory.ACTINIDE;
                    else if (catStr.contains("METALLOID"))
                        cat = Element.ElementCategory.METALLOID;
                    else if (catStr.contains("HALOGEN"))
                        cat = Element.ElementCategory.HALOGEN;
                    else if (catStr.contains("NONMETAL"))
                        cat = Element.ElementCategory.NONMETAL;
                }

                Element element = new Element(data.name, data.symbol);
                element.setAtomicNumber(data.atomicNumber);
                // Creating Mass Quantity using local Quantities factory
                // data.atomicMass is in 'u' (Daltons). 1 u = 1.66053906660e-27 kg.
                element.setAtomicMass(Quantities.create(data.atomicMass * 1.66053906660e-27, Units.KILOGRAM));

                element.setGroup(data.group);
                element.setPeriod(data.period);
                element.setCategory(cat);
                element.setElectronegativity(data.electronegativity);

                // Melting/Boiling
                if (data.melt != null) {
                    element.setMeltingPoint(Quantities.create(data.melt, Units.KELVIN));
                }
                if (data.boil != null) {
                    element.setBoilingPoint(Quantities.create(data.boil, Units.KELVIN));
                }
                if (data.density != null) {
                    element.setDensity(Quantities.create(data.density,
                            Units.GRAM.divide(Units.CENTIMETER.pow(3)).asType(MassDensity.class)));
                }

                PeriodicTable.registerElement(element);
                LOGGER.info("Registered element: " + data.symbol);
            }

            // Load molecules after elements
            loadMolecules();

        } catch (Exception e) {
            System.out.println("DEBUG: Exception loading elements: " + e);
            e.printStackTrace();
            LOGGER.severe("Failed to load elements.json: " + e.getMessage());
        }
    }

    public static void loadMolecules() {
        System.out.println("DEBUG: ChemistryDataLoader.loadMolecules() called.");
        try (InputStream is = ChemistryDataLoader.class.getResourceAsStream("/org/jscience/chemistry/molecules.json")) {
            if (is == null) {
                LOGGER.warning("molecules.json not found in /org/jscience/chemistry/");
                return;
            }
            MoleculeListWrapper wrapper = MAPPER.readValue(is, MoleculeListWrapper.class);
            for (MoleculeData md : wrapper.molecules) {
                org.jscience.chemistry.Molecule mol = new org.jscience.chemistry.Molecule(md.name);
                List<org.jscience.chemistry.Atom> createdAtoms = new java.util.ArrayList<>();

                for (AtomData ad : md.atoms) {
                    Element el = PeriodicTable.bySymbol(ad.symbol);
                    if (el == null) {
                        LOGGER.warning("Unknown element symbol in molecule " + md.name + ": " + ad.symbol);
                        continue;
                    }
                    // Coordinate conversion done here or in constructor?
                    // We need Vector<Real>.
                    org.jscience.mathematics.linearalgebra.vectors.DenseVector<org.jscience.mathematics.numbers.real.Real> pos = org.jscience.mathematics.linearalgebra.vectors.DenseVector
                            .of(
                                    java.util.Arrays.asList(
                                            org.jscience.mathematics.numbers.real.Real.of(ad.x),
                                            org.jscience.mathematics.numbers.real.Real.of(ad.y),
                                            org.jscience.mathematics.numbers.real.Real.of(ad.z)),
                                    org.jscience.mathematics.sets.Reals.getInstance());

                    org.jscience.chemistry.Atom atom = new org.jscience.chemistry.Atom(el, pos);
                    mol.addAtom(atom);
                    createdAtoms.add(atom);
                }

                for (BondData bd : md.bonds) {
                    // 0-based indices matching creation order
                    if (bd.from >= 0 && bd.from < createdAtoms.size() &&
                            bd.to >= 0 && bd.to < createdAtoms.size()) {

                        org.jscience.chemistry.Bond.BondOrder order = org.jscience.chemistry.Bond.BondOrder.SINGLE;
                        try {
                            order = org.jscience.chemistry.Bond.BondOrder.valueOf(bd.order.toUpperCase());
                        } catch (Exception ignore) {
                        }

                        mol.addBond(new org.jscience.chemistry.Bond(
                                createdAtoms.get(bd.from),
                                createdAtoms.get(bd.to),
                                order));
                    }
                }

                MOLECULE_CACHE.put(md.name, mol);
                LOGGER.info("Registered molecule: " + md.name);
            }
        } catch (Exception e) {
            System.out.println("DEBUG: Exception loading molecules: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Gets a new instance (clone-like) of a loaded molecule.
     * Note: Currently returns the cached instance which IS mutable.
     * TODO: Implement deep clone if simulation modifies structure.
     */
    public static org.jscience.chemistry.Molecule getMolecule(String name) {
        // Return a fresh build if possible, or assume caller will not mutate structure
        // irreversibly
        // For now return cached. (Ideally we should parse again or clone)
        // Since Atom positions are mutable in MolecularDynamics, we MUST clone.
        // Simplest clone: Reload from JSON or serialize/deserialize logic.
        // Or just re-instantiate from cache data (if we stored data DTOs).
        // Let's keep access to DTOs or just re-run creation logic?
        // Optimization: Store MoleculeData in cache and build on demand.
        return MOLECULE_CACHE.get(name); // WARNING: Shared instance for now.
    }

    // DTO for JSON mapping
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ElementData {
        public String name;
        public String symbol;
        public int atomicNumber;
        public double atomicMass;
        public String category;
        public double electronegativity;
        public int group;
        public int period;
        public Double melt;
        public Double boil;
        public Double density;
    }

    public static class ElementListWrapper {
        public List<ElementData> elements;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MoleculeData {
        public String name;
        public List<AtomData> atoms;
        public List<BondData> bonds;
    }

    public static class AtomData {
        public String symbol;
        public double x, y, z;
    }

    public static class BondData {
        public int from;
        public int to;
        public String order;
    }

    public static class MoleculeListWrapper {
        public List<MoleculeData> molecules;
    }
}
