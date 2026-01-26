package org.jscience.medicine;

import java.io.Serializable;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.Identified;
import org.jscience.util.identity.UUIDIdentification;

public class Pathology implements Identified<Identification>, Serializable {
    private Identification id = new UUIDIdentification(java.util.UUID.randomUUID().toString());
    private String name;
    
    public Pathology(String name) {
        this.name = name;
    }
    
    public Identification getId() { return id; }
    public String getName() { return name; }
}
