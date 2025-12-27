/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.biology.loaders;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Loader for STL (Stereolithography) 3D mesh files.
 * <p>
 * Supports binary STL format commonly used for anatomical models.
 * Can load from file, stream, or URL (for downloading anatomical datasets).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class StlMeshLoader {

    /**
     * Loads an STL mesh from a file path.
     *
     * @param filePath Path to the STL file
     * @return MeshView containing the loaded geometry
     * @throws IOException if file cannot be read
     */
    public static MeshView load(String filePath) throws IOException {
        return load(new File(filePath));
    }

    /**
     * Loads an STL mesh from a File object.
     *
     * @param file The STL file
     * @return MeshView containing the loaded geometry
     * @throws IOException if file cannot be read
     */
    public static MeshView load(File file) throws IOException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
            return load(is);
        }
    }

    /**
     * Loads an STL mesh from a URL (for downloading from remote sources).
     *
     * @param url URL to the STL file
     * @return MeshView containing the loaded geometry
     * @throws IOException if URL cannot be accessed
     */
    public static MeshView loadFromUrl(String url) throws IOException {
        try (InputStream is = new BufferedInputStream(java.net.URI.create(url).toURL().openStream())) {
            return load(is);
        }
    }

    /**
     * Loads an STL mesh from an InputStream.
     *
     * @param in Input stream containing STL data
     * @return MeshView containing the loaded geometry
     * @throws IOException if stream cannot be read
     */
    public static MeshView load(InputStream in) throws IOException {
        byte[] allBytes = in.readAllBytes();

        if (isAscii(allBytes)) {
            throw new UnsupportedOperationException("ASCII STL not yet implemented. Please use Binary STL.");
        }

        return loadBinary(allBytes);
    }

    private static boolean isAscii(byte[] bytes) {
        if (bytes.length < 5)
            return false;
        String start = new String(bytes, 0, 5);
        return "solid".equalsIgnoreCase(start);
    }

    private static MeshView loadBinary(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        if (bb.remaining() < 84)
            return new MeshView();

        bb.position(80);
        int triangleCount = bb.getInt();

        if (triangleCount <= 0)
            return new MeshView();

        float[] points = new float[triangleCount * 3 * 3];
        int[] faces = new int[triangleCount * 6];
        float[] texCoords = new float[] { 0, 0 };

        int pIdx = 0;
        int fIdx = 0;

        for (int i = 0; i < triangleCount; i++) {
            // Skip normal (3 floats) - JavaFX calculates face normals
            bb.getFloat();
            bb.getFloat();
            bb.getFloat();

            // Vertex 1
            points[pIdx++] = bb.getFloat();
            points[pIdx++] = bb.getFloat();
            points[pIdx++] = bb.getFloat();

            // Vertex 2
            points[pIdx++] = bb.getFloat();
            points[pIdx++] = bb.getFloat();
            points[pIdx++] = bb.getFloat();

            // Vertex 3
            points[pIdx++] = bb.getFloat();
            points[pIdx++] = bb.getFloat();
            points[pIdx++] = bb.getFloat();

            // Skip attribute byte count
            bb.getShort();

            // Add face indices
            int baseV = (pIdx / 3) - 3;
            faces[fIdx++] = baseV;
            faces[fIdx++] = 0;
            faces[fIdx++] = baseV + 1;
            faces[fIdx++] = 0;
            faces[fIdx++] = baseV + 2;
            faces[fIdx++] = 0;
        }

        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().addAll(points);
        mesh.getTexCoords().addAll(texCoords);
        mesh.getFaces().addAll(faces);

        return new MeshView(mesh);
    }
}
