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

package org.jscience.chemistry.loaders.animl;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import jakarta.xml.bind.JAXBElement;
import org.astm.animl.schema.core.draft._0.*;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Analytical Information Markup Language (AnIML) Reader.
 * <p>
 * Parses AnIML files containing spectroscopic, chromatographic, and other
 * analytical chemistry data. AnIML is an ASTM standard (E2077) for representing
 * analytical instrument data.
 * </p>
 * <p>
 * <b>Supported Data Types:</b>
 * <ul>
 *   <li>Spectroscopy data (UV-Vis, IR, NMR, MS)</li>
 *   <li>Chromatography data (GC, HPLC, IC)</li>
 *   <li>Electrochemical data</li>
 *   <li>Thermal analysis data (DSC, TGA)</li>
 * </ul>
 * </p>
 * <p>
 * <b>Example Usage:</b>
 * <pre>{@code
 * AnIMLReader reader = new AnIMLReader();
 * AnIMLDocument doc = reader.read(new FileInputStream("spectrum.animl"));
 * 
 * for (AnIMLSample sample : doc.getSamples()) {
 *     System.out.println("Sample: " + sample.getName());
 * }
 * 
 * for (AnIMLExperimentStep step : doc.getExperimentSteps()) {
 *     for (AnIMLSeriesData series : step.getSeriesData()) {
 *         double[] xData = series.getXValues();
 *         double[] yData = series.getYValues();
 *         // Process spectral/chromatographic data
 *     }
 * }
 * }</pre>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see <a href="https://www.astm.org/e2077-22.html">ASTM E2077 AnIML Standard</a>
 */
public class AnIMLReader {
    
    private static final String ANIML_PACKAGE = "org.astm.animl.schema.core.draft._0";

    private JAXBContext jaxbContext;
    private boolean initialized = false;

    /**
     * Creates a new AnIMLReader.
     */
    public AnIMLReader() {
    }

    /**
     * Initializes the JAXB context for AnIML parsing.
     */
    private void initializeContext() throws AnIMLException {
        if (initialized) {
            return;
        }
        try {
            jaxbContext = JAXBContext.newInstance(ANIML_PACKAGE);
            initialized = true;
        } catch (JAXBException e) {
            throw new AnIMLException("Failed to initialize JAXB context for AnIML", e);
        }
    }

    /**
     * Reads AnIML data from an input stream.
     *
     * @param input the input stream containing AnIML data
     * @return an AnIML document containing parsed analytical data
     * @throws AnIMLException if parsing fails
     */
    public AnIMLDocument read(InputStream input) throws AnIMLException {
        initializeContext();
        
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Object root = unmarshaller.unmarshal(input);
            return convertToDocument(root);
        } catch (JAXBException e) {
            throw new AnIMLException("Failed to parse AnIML data", e);
        }
    }

    /**
     * Reads AnIML data from a file.
     *
     * @param file the file containing AnIML data
     * @return an AnIML document containing parsed analytical data
     * @throws AnIMLException if parsing fails
     */
    public AnIMLDocument read(File file) throws AnIMLException {
        initializeContext();
        
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Object root = unmarshaller.unmarshal(file);
            return convertToDocument(root);
        } catch (JAXBException e) {
            throw new AnIMLException("Failed to parse AnIML data from file: " + file, e);
        }
    }

    /**
     * Converts JAXB-generated AnIML objects to JScience domain objects.
     */
    private AnIMLDocument convertToDocument(Object jaxbRoot) throws AnIMLException {
        AnIMLDocument doc = new AnIMLDocument();
        
        if (jaxbRoot instanceof AnIMLType) {
            AnIMLType animl = (AnIMLType) jaxbRoot;
            
            // Convert samples
            SampleSetType sampleSet = animl.getSampleSet();
            if (sampleSet != null) {
                for (SampleType sample : sampleSet.getSample()) {
                    doc.addSample(convertSample(sample));
                }
            }
            
            // Convert experiment steps
            ExperimentStepSetType experimentSteps = animl.getExperimentStepSet();
            if (experimentSteps != null) {
                for (ExperimentStepType step : experimentSteps.getExperimentStep()) {
                    doc.addExperimentStep(convertExperimentStep(step));
                }
            }
        }
        
        return doc;
    }

    /**
     * Converts a JAXB sample to an AnIMLSample.
     */
    private AnIMLSample convertSample(SampleType sample) {
        AnIMLSample result = new AnIMLSample();
        result.setId(sample.getSampleID());
        result.setName(sample.getName());
        
        // Extract container info if present
        if (sample.getContainerID() != null) {
            result.setContainerId(sample.getContainerID());
        }
        
        return result;
    }

    /**
     * Converts a JAXB experiment step to an AnIMLExperimentStep.
     */
    private AnIMLExperimentStep convertExperimentStep(ExperimentStepType step) {
        AnIMLExperimentStep result = new AnIMLExperimentStep();
        result.setId(step.getExperimentStepID());
        result.setName(step.getName());
        
        // Convert technique info
        TechniqueType technique = step.getTechnique();
        if (technique != null) {
            result.setTechniqueName(technique.getName());
            result.setTechniqueUri(technique.getUri());
        }
        
        // Convert results containing series data
        for (ResultType resultType : step.getResult()) {
            SeriesSetType seriesSet = resultType.getSeriesSet();
            if (seriesSet != null) {
                for (SeriesType series : seriesSet.getSeries()) {
                    result.addSeriesData(convertSeries(series));
                }
            }
        }
        
        return result;
    }

    /**
     * Converts a JAXB series to AnIMLSeriesData.
     */
    private AnIMLSeriesData convertSeries(SeriesType series) {
        AnIMLSeriesData result = new AnIMLSeriesData();
        result.setName(series.getName());
        result.setSeriesId(series.getSeriesID());
        
        // Extract series type (dependent/independent)
        if (series.getDependency() != null) {
            result.setDependency(series.getDependency().value());
        }
        
        // Extract plot scale type
        if (series.getPlotScale() != null) {
            result.setPlotScale(series.getPlotScale().value());
        }
        
        // Extract unit if present
        UnitType unit = series.getUnit();
        if (unit != null) {
            result.setUnitLabel(unit.getLabel());
            result.setUnitQuantity(unit.getQuantity());
        }
        
        // Extract data values
        extractSeriesValues(series, result);
        
        return result;
    }

    /**
     * Extracts numerical values from a series.
     */
    private void extractSeriesValues(SeriesType series, AnIMLSeriesData result) {
        // Check for individual values
        if (!series.getIndividualValueSet().isEmpty()) {
            IndividualValueSetType individualValues = series.getIndividualValueSet().get(0);
            List<Double> values = new ArrayList<>();
            for (JAXBElement<?> element : individualValues.getIOrLOrF()) {
                Object val = element.getValue();
                if (val instanceof Number) {
                    values.add(((Number) val).doubleValue());
                } else if (val instanceof String) {
                    try {
                        values.add(Double.parseDouble((String) val));
                    } catch (NumberFormatException e) {
                        /* ignore */
                    }
                }
            }
            result.setValues(toDoubleArray(values));
        }
        
        // Check for encoded values (Base64 encoded binary data)
        if (!series.getEncodedValueSet().isEmpty()) {
            EncodedValueSetType encodedValues = series.getEncodedValueSet().get(0);
            byte[] decoded = Base64.getDecoder().decode(encodedValues.getValue());
            result.setEncodedData(decoded);
        }
        
        // Check for auto-incremented values (start, increment, end)
        if (!series.getAutoIncrementedValueSet().isEmpty()) {
            AutoIncrementedValueSetType autoIncrementedValues = series.getAutoIncrementedValueSet().get(0);
            Double start = extractFirstDouble(autoIncrementedValues.getStartValue());
            Double increment = extractFirstDouble(autoIncrementedValues.getIncrement());
            
            if (start != null && increment != null) {
                int count = result.getValues() != null ? result.getValues().length : 0;
                
                // Generate values if we know the count
                if (count > 0) {
                    double[] generated = new double[count];
                    for (int i = 0; i < count; i++) {
                        generated[i] = start + i * increment;
                    }
                    result.setAutoIncrementedValues(generated);
                }
                
                result.setStartValue(start);
                result.setIncrement(increment);
            }
        }
    }

    private Double extractFirstDouble(UnboundedValueType valueSet) {
        if (valueSet == null) return null;
        for (JAXBElement<?> element : valueSet.getIOrLOrF()) {
            Object val = element.getValue();
            if (val instanceof Number) return ((Number) val).doubleValue();
            if (val instanceof String) {
                try {
                    return Double.parseDouble((String) val);
                } catch (NumberFormatException e) {
                    /* continue */
                }
            }
        }
        return null;
    }

    /**
     * Converts a list of Double to a primitive array.
     */
    private double[] toDoubleArray(List<Double> list) {
        double[] array = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }
}
