/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.apps.anatomy;

import org.jscience.biology.loaders.StlMeshLoader;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.*;

public class StlMeshLoaderTest {

    @Test
    public void testLoadBinaryStl() throws IOException {
        // Create a minimal valid Binary STL for a single triangle using ByteBuffer

        // Header (80 bytes) + Count (4 bytes) + 1 Triangle (50 bytes)
        int totalSize = 80 + 4 + 50;
        ByteBuffer bb = ByteBuffer.allocate(totalSize);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        // Header (zeros or text, doesn't matter for binary except if it starts with
        // "solid")
        // StlLoader checks "solid" at start. Let's make sure it DOESN'T start with
        // "solid"
        // so it treats it as binary.
        byte[] header = new byte[80];
        header[0] = 'B';
        header[1] = 'I';
        bb.put(header); // 0-79

        // Count = 1
        bb.putInt(1); // 80-83

        // Triangle 1
        // Normal (0,0,1)
        bb.putFloat(0f);
        bb.putFloat(0f);
        bb.putFloat(1f);

        // V1 (0,0,0)
        bb.putFloat(0f);
        bb.putFloat(0f);
        bb.putFloat(0f);

        // V2 (1,0,0)
        bb.putFloat(1f);
        bb.putFloat(0f);
        bb.putFloat(0f);

        // V3 (0,1,0)
        bb.putFloat(0f);
        bb.putFloat(1f);
        bb.putFloat(0f);

        // Attribute byte count (0)
        bb.putShort((short) 0);

        ByteArrayInputStream in = new ByteArrayInputStream(bb.array());

        // Load
        MeshView view = StlMeshLoader.load(in);
        assertNotNull(view);
        assertTrue(view.getMesh() instanceof TriangleMesh);

        TriangleMesh mesh = (TriangleMesh) view.getMesh();
        assertEquals(3, mesh.getPoints().size() / 3); // 3 vertices * 3 coords
        assertEquals(1, mesh.getFaces().size() / 6); // 1 face * 6 indices
    }
}
