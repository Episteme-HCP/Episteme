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

package org.jscience.social.sociology.loaders;

import org.jscience.social.sociology.DemographicData;
import org.jscience.social.sociology.Group;
import org.jscience.social.sociology.Person;
import org.jscience.core.io.AbstractResourceReader;
import org.jscience.core.io.MiniCatalog;
import org.jscience.core.io.BasicMiniCatalog;
import java.util.*;

/**
 * Resource reader for demographic data models.
 * Uses real Group/Person objects to generate sample data.
 */
public final class DemographicResourceReader extends AbstractResourceReader<DemographicData> {

    @Override public String getCategory() { return "Sociology"; }
    @Override public String getName() { return "Demographic Resource Reader"; }
    @Override public String getDescription() { return "Loads population structures and census data."; }
    @Override public String getLongDescription() { return "Supports individual group analysis and aggregated demographic pyramids."; }
    @Override public String getResourcePath() { return "data/sociology"; }
    @Override public Class<DemographicData> getResourceType() { return DemographicData.class; }
    @Override public String[] getSupportedVersions() { return new String[] {"1.0"}; }

    @Override
    protected DemographicData loadFromSource(String id) throws Exception {
        return null; 
    }

    @Override
    protected MiniCatalog<DemographicData> getMiniCatalog() {
        BasicMiniCatalog<DemographicData> catalog = new BasicMiniCatalog<>();
        catalog.register("World2025", createWorldDemographics());
        return catalog;
    }

    private DemographicData createWorldDemographics() {
        DemographicData dd = new DemographicData();
        dd.setPopulationName("World 2025 (Integrated Sample)");
        
        Group youth = new Group("Youth Sample", org.jscience.social.sociology.GroupKind.COMMUNITY);
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            Person p = new Person("Student " + i, rand.nextBoolean() ? org.jscience.natural.biology.BiologicalSex.MALE : org.jscience.natural.biology.BiologicalSex.FEMALE);
            p.setGender(rand.nextBoolean() ? org.jscience.social.sociology.Gender.MALE : org.jscience.social.sociology.Gender.FEMALE);
            youth.addMember(p);
        }
        dd.addGroup(youth);
        
        dd.addGroup(0, 14, 980000000L, 930000000L);
        dd.addGroup(15, 24, 610000000L, 590000000L);
        dd.addGroup(25, 54, 1500000000L, 1450000000L);
        dd.addGroup(55, 64, 450000000L, 470000000L);
        dd.addGroup(65, 100, 380000000L, 440000000L);
        
        return dd;
    }
}

