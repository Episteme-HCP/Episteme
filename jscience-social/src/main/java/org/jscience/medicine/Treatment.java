package org.jscience.medicine;

import java.io.Serializable;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.Identified;
import org.jscience.util.identity.UUIDIdentification;

public class Treatment implements Identified<Identification>, Serializable {
    private Identification id = new UUIDIdentification(java.util.UUID.randomUUID().toString());
    private String description;
    
    public Treatment(String description) {
        this.description = description;
    }
    
    public Identification getId() { return id; }
    public String getDescription() { return description; }
}
