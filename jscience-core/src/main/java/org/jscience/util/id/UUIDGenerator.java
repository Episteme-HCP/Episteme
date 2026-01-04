package org.jscience.util.id;

import java.util.UUID;

public class UUIDGenerator implements IdGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
