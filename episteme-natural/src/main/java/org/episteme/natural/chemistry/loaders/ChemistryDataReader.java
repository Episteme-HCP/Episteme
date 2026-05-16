/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.chemistry.loaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.episteme.natural.chemistry.Element;
import org.episteme.natural.chemistry.ElementCategory;
import org.episteme.natural.chemistry.PeriodicTable;
import org.episteme.core.io.AbstractResourceReader;
import org.episteme.core.io.MiniCatalog;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;
import org.episteme.core.measure.quantity.MassDensity;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads chemistry data (elements, molecules) from JSON.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ChemistryDataReader extends AbstractResourceReader<Object> {

    @Override
    public String getCategory() {
        return org.episteme.core.ui.i18n.I18N.getInstance().get("category.chemistry", "Chemistry");
    }

    @Override
    public String getDescription() {
        return org.episteme.core.ui.i18n.I18N.getInstance().get("reader.chemistrydatareader.desc", "Generic Chemistry Data Reader (JSON).");
    }

    @Override
    public String getLongDescription() {
        return org.episteme.core.ui.i18n.I18N.getInstance().get("reader.chemistrydatareader.longdesc", "Reads chemistry data including elements and molecules from JSON resources.");
    }

    @Override
    public String getResourcePath() {
        return "/org/episteme/chemistry/";
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[] { "1.0" };
    }

    @Override
    public Class<Object> getResourceType() {
        return Object.class;
    }

    @Override
    protected Object loadFromSource(String id) throws Exception {
        if ("elements".equals(id)) {
            loadElements();
            return java.util.Collections.emptyList(); // PeriodicTable.getElements() is not exposed
        } else if ("molecules".equals(id)) {
            loadMolecules();
            return MOLECULE_DATA_CACHE.keySet();
        }
        return null;
    }

    @Override
    protected MiniCatalog<Object> getMiniCatalog() {
        return new MiniCatalog<>() {
            @Override
            public List<Object> getAll() {
                return List.of();
            }

            @Override
            public Optional<Object> findByName(String name) {
                return Optional.empty();
            }

            @Override
            public int size() {
                return 0;
            }
        };
    }

    private static final Logger logger = LoggerFactory.getLogger(ChemistryDataReader.class);
    private static ObjectMapper MAPPER;

    static {
        try {
            MAPPER = new ObjectMapper();
        } catch (Throwable t) {
            logger.error("CRITICAL ERROR: Could not initialize Jackson ObjectMapper. Check dependencies!", t);
            MAPPER = null;
        }
    }

    private static final java.util.Map<String, MoleculeData> MOLECULE_DATA_CACHE = new java.util.HashMap<>();

    public static void loadElements() {
        if (MAPPER == null) {
            logger.error("ChemistryDataReader cannot function because ObjectMapper failed to initialize.");
            return;
        }

        try (InputStream is = ChemistryDataReader.class.getResourceAsStream("/org/episteme/chemistry/elements.json")) {
            if (is == null) {
                logger.warn("elements.json not found in /org/episteme/chemistry/");
                return;
            }

            ElementListWrapper wrapper = MAPPER.readValue(is, ElementListWrapper.class);
            List<ElementData> elements = wrapper.elements;

            for (ElementData data : elements) {
                ElementCategory cat = ElementCategory.UNKNOWN;
                String catStr = data.category.toUpperCase().replace(" ", "_").replace("-", "_");
                if (catStr.contains("DIATOMIC") || catStr.contains("POLYATOMIC")) {
                    catStr = "NONMETAL";
                }
                try {
                    cat = ElementCategory.valueOf(catStr);
                } catch (Exception e) {
                    if (catStr.contains("NOBLE"))
                        cat = ElementCategory.NOBLE_GAS;
                    else if (catStr.contains("ALKALI") && !catStr.contains("EARTH"))
                        cat = ElementCategory.ALKALI_METAL;
                    else if (catStr.contains("ALKALINE"))
                        cat = ElementCategory.ALKALINE_EARTH_METAL;
                    else if (catStr.contains("TRANSITION"))
                        cat = ElementCategory.TRANSITION_METAL;
                    else if (catStr.contains("LANTHANIDE"))
                        cat = ElementCategory.LANTHANIDE;
                    else if (catStr.contains("ACTINIDE"))
                        cat = ElementCategory.ACTINIDE;
                    else if (catStr.contains("METALLOID"))
                        cat = ElementCategory.METALLOID;
                    else if (catStr.contains("HALOGEN"))
                        cat = ElementCategory.HALOGEN;
                    else if (catStr.contains("NONMETAL"))
                        cat = ElementCategory.NONMETAL;
                }

                Element element = new Element(data.name, data.symbol);
                element.setAtomicNumber(data.atomicNumber);
                element.setAtomicMass(Quantities.create(data.atomicMass * 1.66053906660e-27, Units.KILOGRAM));

                element.setGroup(data.group);
                element.setPeriod(data.period);
                element.setCategory(cat);
                element.setElectronegativity(data.electronegativity);

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

                element.setStandardState(data.standardState);
                element.setElectronConfiguration(data.electronConfig);
                element.setOxidationStates(data.oxidationStates);
                element.setYearDiscovered(data.yearDiscovered);

                if (data.atomicRadius != null) {
                    element.setAtomicRadius(Quantities.create(data.atomicRadius * 1e-12, Units.METER));
                }
                if (data.ionization != null) {
                    element.setIonizationEnergy(Quantities.create(data.ionization, Units.ELECTRON_VOLT));
                }
                if (data.affinity != null) {
                    element.setElectronAffinity(Quantities.create(data.affinity, Units.ELECTRON_VOLT));
                }

                PeriodicTable.registerElement(element);
                logger.debug("Registered element: {}", data.symbol);
            }

            loadMolecules();

        } catch (Exception e) {
            logger.error("Failed to load elements.json: {}", e.getMessage(), e);
        }
    }

    public static void loadMolecules() {
        if (MAPPER == null)
            return;

        try (InputStream is = ChemistryDataReader.class.getResourceAsStream("/org/episteme/chemistry/molecules.json")) {
            if (is == null) {
                logger.warn("molecules.json not found in /org/episteme/chemistry/");
                return;
            }
            MoleculeListWrapper wrapper = MAPPER.readValue(is, MoleculeListWrapper.class);
            for (MoleculeData md : wrapper.molecules) {
                boolean valid = true;
                for (AtomData ad : md.atoms) {
                    if (PeriodicTable.bySymbol(ad.symbol) == null) {
                        logger.warn("Unknown element symbol in molecule {}: {}", md.name, ad.symbol);
                        valid = false;
                        break;
                    }
                }
                if (valid) {
                    MOLECULE_DATA_CACHE.put(md.name, md);
                    logger.debug("Registered molecule data: {}", md.name);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to load molecules.json", e);
        }
    }

    /**
     * Gets a new instance (deep clone) of a loaded molecule.
     */
    public static org.episteme.natural.chemistry.Molecule getMolecule(String name) {
        MoleculeData md = MOLECULE_DATA_CACHE.get(name);
        if (md == null)
            return null;
        return createMoleculeFromData(md);
    }

    private static org.episteme.natural.chemistry.Molecule createMoleculeFromData(MoleculeData md) {
        org.episteme.natural.chemistry.Molecule mol = new org.episteme.natural.chemistry.Molecule(md.name);
        java.util.List<org.episteme.natural.chemistry.Atom> createdAtoms = new java.util.ArrayList<>();

        for (AtomData ad : md.atoms) {
            Element el = PeriodicTable.bySymbol(ad.symbol);
            org.episteme.core.mathematics.linearalgebra.Vector<org.episteme.core.mathematics.numbers.real.Real> pos = org.episteme.core.mathematics.linearalgebra.Vector
                    .of(
                            java.util.Arrays.asList(
                                    org.episteme.core.mathematics.numbers.real.Real.of(ad.x),
                                    org.episteme.core.mathematics.numbers.real.Real.of(ad.y),
                                    org.episteme.core.mathematics.numbers.real.Real.of(ad.z)),
                            org.episteme.core.mathematics.sets.Reals.getInstance());

            org.episteme.natural.chemistry.Atom atom = new org.episteme.natural.chemistry.Atom(el, pos);
            mol.addAtom(atom);
            createdAtoms.add(atom);
        }

        for (BondData bd : md.bonds) {
            if (bd.from >= 0 && bd.from < createdAtoms.size() &&
                    bd.to >= 0 && bd.to < createdAtoms.size()) {

                org.episteme.natural.chemistry.BondType order = org.episteme.natural.chemistry.BondType.SINGLE;
                try {
                    order = org.episteme.natural.chemistry.BondType.valueOf(bd.order.toUpperCase());
                } catch (Exception ignore) {
                }

                mol.addBond(new org.episteme.natural.chemistry.Bond(
                        createdAtoms.get(bd.from),
                        createdAtoms.get(bd.to),
                        order));
            }
        }
        return mol;
    }

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
        public String standardState;
        public String electronConfig;
        public String oxidationStates;
        public Double atomicRadius;
        public Double ionization;
        public Double affinity;
        public int yearDiscovered;
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

    @Override public String getName() { return org.episteme.core.ui.i18n.I18N.getInstance().get("reader.chemistrydatareader.name", "Chemistry Data Reader"); }
}


