package org.jscience.sociology.loaders;

import org.jscience.sociology.DemographicData;
import org.jscience.sociology.Group;
import org.jscience.sociology.Person;
import org.jscience.io.AbstractResourceReader;
import org.jscience.io.MiniCatalog;
import org.jscience.io.BasicMiniCatalog;
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
        
        Group youth = new Group("Youth Sample", org.jscience.sociology.GroupKind.COMMUNITY);
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            Person p = new Person("Student " + i, rand.nextBoolean() ? org.jscience.biology.BiologicalSex.MALE : org.jscience.biology.BiologicalSex.FEMALE);
            p.setGender(rand.nextBoolean() ? org.jscience.sociology.Gender.MALE : org.jscience.sociology.Gender.FEMALE);
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
