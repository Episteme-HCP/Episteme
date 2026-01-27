/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.physics.loaders.thermoml;

import org.jscience.chemistry.Molecule;
import org.jscience.chemistry.thermodynamics.ThermodynamicProperty;
import org.jscience.chemistry.thermodynamics.ThermodynamicDataset;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bridge for converting ThermoML DTOs to core JScience thermodynamics objects.
 * <p>
 * ThermoML is the IUPAC XML standard for thermophysical and thermochemical data.
 * This bridge converts parsed ThermoML to JScience chemistry and measurement structures.
 * </p>
 *
 * <h2>Architecture</h2>
 * <pre>
 * ThermoML → ThermoMLReader → ThermoML DTOs → ThermoMLBridge → Core Objects
 *                                                              ├── Molecule
 *                                                              ├── ThermodynamicProperty
 *                                                              └── ThermodynamicDataset
 * </pre>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ThermoMLBridge {

    /**
     * Converts ThermoML data container to JScience ThermodynamicDataset.
     *
     * @param thermoData the parsed ThermoML container
     * @return a ThermodynamicDataset with all compounds and properties
     */
    public ThermodynamicDataset toDataset(ThermoMLDataReport thermoData) {
        if (thermoData == null) {
            return null;
        }
        
        ThermodynamicDataset dataset = new ThermodynamicDataset(thermoData.getTitle());
        dataset.setTrait("thermoml.version", thermoData.getVersion());
        dataset.setTrait("thermoml.citation", thermoData.getCitation());
        
        // Build compound map
        Map<String, Molecule> compoundMap = new HashMap<>();
        if (thermoData.getCompounds() != null) {
            for (ThermoMLCompound comp : thermoData.getCompounds()) {
                Molecule mol = convertCompound(comp);
                if (mol != null) {
                    compoundMap.put(comp.getRegNum(), mol);
                    dataset.addCompound(mol);
                }
            }
        }
        
        // Convert pure compound properties
        if (thermoData.getPureCompoundProperties() != null) {
            for (ThermoMLPureCompoundProperty prop : thermoData.getPureCompoundProperties()) {
                ThermodynamicProperty tp = convertPureProperty(prop, compoundMap);
                if (tp != null) {
                    dataset.addProperty(tp);
                }
            }
        }
        
        // Convert mixture properties
        if (thermoData.getMixtureProperties() != null) {
            for (ThermoMLMixtureProperty prop : thermoData.getMixtureProperties()) {
                ThermodynamicProperty tp = convertMixtureProperty(prop, compoundMap);
                if (tp != null) {
                    dataset.addProperty(tp);
                }
            }
        }
        
        return dataset;
    }

    /**
     * Converts ThermoML compound to JScience Molecule.
     */
    public Molecule convertCompound(ThermoMLCompound compound) {
        if (compound == null) {
            return null;
        }
        
        String name = compound.getCommonName();
        if (name == null || name.isEmpty()) {
            name = compound.getStandardInChIKey();
        }
        
        Molecule mol = new Molecule(name);
        mol.setTrait("thermoml.regnum", compound.getRegNum());
        mol.setTrait("cas.number", compound.getCASRegistryNumber());
        mol.setTrait("formula", compound.getMolecularFormula());
        mol.setTrait("smiles", compound.getSmiles());
        mol.setTrait("inchi", compound.getStandardInChI());
        mol.setTrait("inchikey", compound.getStandardInChIKey());
        mol.setTrait("iupac.name", compound.getIUPACName());
        
        return mol;
    }

    /**
     * Converts ThermoML pure compound property.
     */
    public ThermodynamicProperty convertPureProperty(ThermoMLPureCompoundProperty prop, 
                                                      Map<String, Molecule> compoundMap) {
        if (prop == null) {
            return null;
        }
        
        Molecule compound = compoundMap.get(prop.getCompoundRegNum());
        ThermodynamicProperty tp = new ThermodynamicProperty(prop.getPropertyName());
        
        tp.setCompound(compound);
        tp.setTrait("thermoml.property.group", prop.getPropertyGroup());
        
        // Add measurement conditions
        tp.setTemperature(Quantity.of(prop.getTemperature(), Units.KELVIN));
        tp.setPressure(Quantity.of(prop.getPressure(), Units.PASCAL));
        
        // Add property value with uncertainty
        tp.setValue(prop.getValue());
        if (prop.getUncertainty() != null) {
            tp.setUncertainty(prop.getUncertainty());
        }
        tp.setUnit(prop.getUnit());
        
        return tp;
    }

    /**
     * Converts ThermoML mixture property.
     */
    public ThermodynamicProperty convertMixtureProperty(ThermoMLMixtureProperty prop,
                                                         Map<String, Molecule> compoundMap) {
        if (prop == null) {
            return null;
        }
        
        ThermodynamicProperty tp = new ThermodynamicProperty(prop.getPropertyName());
        tp.setTrait("thermoml.mixture", true);
        
        // Add all mixture components
        if (prop.getComponents() != null) {
            List<Molecule> components = new ArrayList<>();
            for (ThermoMLMixtureComponent mc : prop.getComponents()) {
                Molecule mol = compoundMap.get(mc.getCompoundRegNum());
                if (mol != null) {
                    components.add(mol);
                    tp.setTrait("mole.fraction." + mol.getName(), mc.getMoleFraction());
                }
            }
            tp.setMixtureComponents(components);
        }
        
        // Add measurement conditions and value
        tp.setTemperature(Quantity.of(prop.getTemperature(), Units.KELVIN));
        tp.setPressure(Quantity.of(prop.getPressure(), Units.PASCAL));
        tp.setValue(prop.getValue());
        tp.setUnit(prop.getUnit());
        
        return tp;
    }
}
