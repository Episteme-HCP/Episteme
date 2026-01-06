/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.client.earth.climatesim;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jscience.io.AbstractResourceWriter;
import java.io.File;

/**
 * Writer for Climate Model Data (JSON Format).
 */
public class ClimateDataWriter extends AbstractResourceWriter<ClimateDataReader.ClimateState> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String getResourcePath() {
        return "/";
    }

    @Override
    public Class<ClimateDataReader.ClimateState> getResourceType() {
        return ClimateDataReader.ClimateState.class;
    }

    @Override
    public void save(ClimateDataReader.ClimateState state, String destination) throws Exception {
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(destination), state);
    }
}
