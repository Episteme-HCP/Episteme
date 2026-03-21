package org.episteme.core.technical.backend;

import org.junit.jupiter.api.Test;
import java.util.*;

public class DebugDiscovery {
    @Test
    public void testDiscovery() {
        System.out.println("Starting DebugDiscovery...");
        ServiceLoader<Backend> loader = ServiceLoader.load(Backend.class);
        Iterator<Backend> iterator = loader.iterator();
        int count = 0;
        while (true) {
            try {
                if (!iterator.hasNext()) break;
                Backend b = iterator.next();
                System.out.println("Discovered: " + b.getName() + " [" + b.getClass().getName() + "]");
                count++;
            } catch (Throwable t) {
                System.out.println("FAILED during discovery: " + t.getClass().getName() + " - " + t.getMessage());
                t.printStackTrace();
                // Continue to next if possible, but some errors might stop the iterator
            }
        }
        System.out.println("Total discovered: " + count);
    }
}
