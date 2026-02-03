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

package org.jscience.core.util.persistence;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Verification of the persistence backbone.
 */
public class PersistenceTest {

    @Persistent
    public static class TestEntity {
        @Id
        private String id;
        
        @Attribute
        private String name;

        public TestEntity() {}

        public TestEntity(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() { return id; }
        public String getName() { return name; }
    }

    @Test
    public void testSaveAndLoad() {
        PersistenceManager pm = PersistenceManager.getInstance();
        
        TestEntity entity = new TestEntity("E1", "Test Scientific Entity");
        pm.save(entity);
        
        TestEntity loaded = pm.load(TestEntity.class, "E1");
        
        assertNotNull(loaded, "Entity should be loaded");
        assertEquals("E1", loaded.getId());
        assertEquals("Test Scientific Entity", loaded.getName());
    }

    @Test
    public void testDelete() {
        PersistenceManager pm = PersistenceManager.getInstance();
        
        TestEntity entity = new TestEntity("E2", "To be deleted");
        pm.save(entity);
        assertNotNull(pm.load(TestEntity.class, "E2"));
        
        pm.delete(entity);
        assertNull(pm.load(TestEntity.class, "E2"), "Entity should be null after deletion");
    }
}
