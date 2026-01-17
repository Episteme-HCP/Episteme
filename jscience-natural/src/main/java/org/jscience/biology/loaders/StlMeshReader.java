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

package org.jscience.biology.loaders;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import org.jscience.io.AbstractResourceReader;
import org.jscience.io.MiniCatalog;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Optional;

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
public class StlMeshReader extends AbstractResourceReader<MeshView> {

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.biology", "Biology");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.stlmeshreader.desc", "STL 3D Mesh Reader.");
    }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.stlmeshreader.longdesc", "Loader for STL (Stereolithography) 3D mesh files in binary format.");
    }

    @Override
    public String getResourcePath() {
        return "/";
    }

    @Override
    public Class<MeshView> getResourceType() {
        return MeshView.class;
    }

    @Override
    protected MeshView loadFromSource(String id) throws Exception {
        if (id.startsWith("http")) {
            return loadFromUrl(id);
        }
        return loadFromPath(id);
    }

    @Override
    protected MiniCatalog<MeshView> getMiniCatalog() {
        return new MiniCatalog<>() {
            @Override
            public List<MeshView> getAll() {
                return List.of(new MeshView());
            }

            @Override
            public Optional<MeshView> findByName(String name) {
                return Optional.of(new MeshView());
            }

            @Override
            public int size() {
                return 0;
            }
        };
    }

    /**
     * Loads an STL mesh from a file path.
     */
    public static MeshView loadFromPath(String filePath) throws IOException {
        return load(new File(filePath));
    }

    /**
     * Loads an STL mesh from a File object.
     */
    public static MeshView load(File file) throws IOException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
            return load(is);
        }
    }

    /**
     * Loads an STL mesh from a URL (for downloading from remote sources).
     */
    public static MeshView loadFromUrl(String url) throws IOException {
        try (InputStream is = new BufferedInputStream(java.net.URI.create(url).toURL().openStream())) {
            return load(is);
        }
    }

    /**
     * Loads an STL mesh from an InputStream.
     */
    public static MeshView load(InputStream in) throws IOException {
        byte[] data = in.readAllBytes();
        ByteBuffer bb = ByteBuffer.wrap(data);
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
            bb.getFloat();
            bb.getFloat();
            bb.getFloat();

            points[pIdx++] = bb.getFloat();
            points[pIdx++] = bb.getFloat();
            points[pIdx++] = bb.getFloat();

            points[pIdx++] = bb.getFloat();
            points[pIdx++] = bb.getFloat();
            points[pIdx++] = bb.getFloat();

            points[pIdx++] = bb.getFloat();
            points[pIdx++] = bb.getFloat();
            points[pIdx++] = bb.getFloat();

            bb.getShort();

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

    @Override public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("reader.stlmeshreader.name", "STL Mesh Reader"); }
}

