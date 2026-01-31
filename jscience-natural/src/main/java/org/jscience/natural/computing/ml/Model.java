/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.computing.ml;

import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.UniversalDataModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class for all machine learning models.
 */
@Persistent
public class Model implements Serializable, UniversalDataModel {

    private static final long serialVersionUID = 1L;

    @Attribute
    private String name;

    @Attribute
    protected final Map<String, Object> traits = new HashMap<>();

    @Attribute
    private final List<DataField> dataFields = new ArrayList<>();
    
    @Attribute
    private final List<MiningField> miningFields = new ArrayList<>();

    public Model() {
    }

    public Model(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTrait(String key, Object value) {
        traits.put(key, value);
    }

    public Object getTrait(String key) {
        return traits.get(key);
    }
    
    public void addDataField(DataField field) {
        if (field != null) {
            dataFields.add(field);
        }
    }
    
    public void addMiningField(MiningField field) {
        if (field != null) {
            miningFields.add(field);
        }
    }
    
    public List<DataField> getDataFields() {
        return Collections.unmodifiableList(dataFields);
    }
    
    public List<MiningField> getMiningFields() {
        return Collections.unmodifiableList(miningFields);
    }

    // --- UniversalDataModel implementation ---

    @Override
    public String getModelType() {
        return "MACHINE_LEARNING_MODEL";
    }

    @Override
    public Map<String, Object> getMetadata() {
        Map<String, Object> meta = new HashMap<>(traits);
        meta.put("name", name);
        meta.put("data_field_count", dataFields.size());
        meta.put("mining_field_count", miningFields.size());
        return meta;
    }

    @Override
    public Map<String, org.jscience.core.measure.Quantity<?>> getQuantities() {
        Map<String, org.jscience.core.measure.Quantity<?>> q = new HashMap<>();
        q.put("field_count", org.jscience.core.measure.Quantities.create(dataFields.size() + miningFields.size(), org.jscience.core.measure.Units.ONE));
        return q;
    }
}

