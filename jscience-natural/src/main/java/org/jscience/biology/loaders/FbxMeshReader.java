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

package org.jscience.biology.loaders;

import javafx.scene.Group;

import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;


import org.jscience.io.AbstractResourceReader;
import org.jscience.io.MiniCatalog;

import com.lukaseichberg.fbxloader.FBXFile;
import com.lukaseichberg.fbxloader.FBXLoader;
import com.lukaseichberg.fbxloader.FBXNode;
import com.lukaseichberg.fbxloader.FBXProperty;
import com.lukaseichberg.fbxloader.FBXDataType;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * FBX loader for JavaFX 3D using the jfbx library.
 * <p>
 * Parses binary FBX files and creates JavaFX TriangleMesh objects.
 * Supports basic geometry (Vertices, PolygonVertexIndex).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FbxMeshReader extends AbstractResourceReader<Group> {

    @Override
    public String getResourcePath() {
        return "/";
    }

    @Override
    public Class<Group> getResourceType() {
        return Group.class;
    }

    @Override
    protected Group loadFromSource(String id) throws Exception {
        if (id.startsWith("http") || id.startsWith("file:")) {
            return load(java.net.URI.create(id).toURL());
        }
        return loadResource(id);
    }

    @Override
    protected MiniCatalog<Group> getMiniCatalog() {
        return new MiniCatalog<>() {
            @Override
            public List<Group> getAll() {
                return List.of(new Group());
            }

            @Override
            public Optional<Group> findByName(String name) {
                return Optional.of(new Group());
            }

            @Override
            public int size() {
                return 0;
            }
        };
    }

    /**
     * Loads an FBX model from a file.
     */
    public static Group load(File file) throws IOException {
        FBXFile fbxFile = FBXLoader.loadFBXFile(file.getAbsolutePath());
        return parseJfbx(fbxFile);
    }

    /**
     * Loads an FBX model from a URL (resource or http).
     * For URL resources, we first copy to a temp file since jfbx requires a file path.
     */
    public static Group load(URL url) throws IOException {
        // jfbx requires a file path, so we copy URL content to a temp file
        File tempFile = File.createTempFile("jscience_fbx_", ".fbx");
        tempFile.deleteOnExit();
        
        try (InputStream in = url.openStream();
             OutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        
        return load(tempFile);
    }

    /**
     * Loads an FBX model from classpath resources.
     */
    public static Group loadResource(String resourcePath) throws IOException {
        URL url = FbxMeshReader.class.getResource(resourcePath);
        if (url == null) {
            throw new IOException("FBX resource not found: " + resourcePath);
        }
        return load(url);
    }

    /**
     * Parses an FBX file loaded by jfbx and creates JavaFX meshes.
     */
    private static Group parseJfbx(FBXFile fbxFile) {
        // Root container for the entire FBX scene
        Group fbxRoot = new Group();
        
        FBXNode root = fbxFile.getRootNode();
        FBXNode objectsNode = root.getChildByName("Objects");
        if (objectsNode == null) return fbxRoot;
        
        System.out.println("DEBUG: Starting parseJfbx (Hierarchy + Raw Transforms)");

        // Inner classes for parsing
        class TransformData { 
            // Lcl (Local) Transforms
            double tx=0, ty=0, tz=0; 
            double rx=0, ry=0, rz=0; 
            double sx=1, sy=1, sz=1;
            
            // Pre-Rotation
            double prx=0, pry=0, prz=0;

            // Pivots
            double rpx=0, rpy=0, rpz=0; // Rotation Pivot
            double spx=0, spy=0, spz=0; // Scaling Pivot
            
            // Geometric Transforms (Mesh Offset)
            double gtx=0, gty=0, gtz=0;
            double grx=0, gry=0, grz=0;
            double gsx=1, gsy=1, gsz=1;
        }

        class ModelData {
            long id;
            String name;
            Group group = new Group(); // JavaFX Group representing this model
            TransformData transform = new TransformData();
        }
        
        // 1. Parse Models Types and Transforms
        java.util.Map<Long, ModelData> models = new java.util.HashMap<>();
        
        List<FBXNode> modelNodes = objectsNode.getChildrenByName("Model");
        for (FBXNode m : modelNodes) {
            Object prop0 = m.getProperty(0).getData();
            if (!(prop0 instanceof Number)) continue;
            long id = ((Number) prop0).longValue();
            
            ModelData md = new ModelData();
            md.id = id;
            md.name = sanitizeID((String) m.getProperty(1).getData());
            
            // Parse Properties70 
            FBXNode props = m.getChildByName("Properties70");
            if (props != null) {
                for (int i=0; i<props.getNumChildren(); i++) {
                    FBXNode p = props.getChild(i); // "P"
                    if (p.getNumProperties() >= 5) {
                        String pName = (String) p.getProperty(0).getData();
                        
                        // Local Transforms
                        if ("Lcl Translation".equals(pName)) {
                            md.transform.tx = ((Number) p.getProperty(4).getData()).doubleValue();
                            md.transform.ty = ((Number) p.getProperty(5).getData()).doubleValue();
                            md.transform.tz = ((Number) p.getProperty(6).getData()).doubleValue();
                        } else if ("Lcl Rotation".equals(pName)) {
                            md.transform.rx = ((Number) p.getProperty(4).getData()).doubleValue();
                            md.transform.ry = ((Number) p.getProperty(5).getData()).doubleValue();
                            md.transform.rz = ((Number) p.getProperty(6).getData()).doubleValue();
                        } else if ("Lcl Scaling".equals(pName)) {
                            md.transform.sx = ((Number) p.getProperty(4).getData()).doubleValue();
                            md.transform.sy = ((Number) p.getProperty(5).getData()).doubleValue();
                            md.transform.sz = ((Number) p.getProperty(6).getData()).doubleValue();
                        } else if ("PreRotation".equals(pName)) {
                            md.transform.prx = ((Number) p.getProperty(4).getData()).doubleValue();
                            md.transform.pry = ((Number) p.getProperty(5).getData()).doubleValue();
                            md.transform.prz = ((Number) p.getProperty(6).getData()).doubleValue();
                        } else if ("RotationPivot".equals(pName)) {
                            md.transform.rpx = ((Number) p.getProperty(4).getData()).doubleValue();
                            md.transform.rpy = ((Number) p.getProperty(5).getData()).doubleValue();
                            md.transform.rpz = ((Number) p.getProperty(6).getData()).doubleValue();
                        } else if ("ScalingPivot".equals(pName)) {
                            md.transform.spx = ((Number) p.getProperty(4).getData()).doubleValue();
                            md.transform.spy = ((Number) p.getProperty(5).getData()).doubleValue();
                            md.transform.spz = ((Number) p.getProperty(6).getData()).doubleValue();
                        }
                        
                        // Geometric Transforms
                        else if ("GeometricTranslation".equals(pName)) {
                            md.transform.gtx = ((Number) p.getProperty(4).getData()).doubleValue();
                            md.transform.gty = ((Number) p.getProperty(5).getData()).doubleValue();
                            md.transform.gtz = ((Number) p.getProperty(6).getData()).doubleValue();
                        } else if ("GeometricRotation".equals(pName)) {
                            md.transform.grx = ((Number) p.getProperty(4).getData()).doubleValue();
                            md.transform.gry = ((Number) p.getProperty(5).getData()).doubleValue();
                            md.transform.grz = ((Number) p.getProperty(6).getData()).doubleValue();
                        } else if ("GeometricScaling".equals(pName)) {
                            md.transform.gsx = ((Number) p.getProperty(4).getData()).doubleValue();
                            md.transform.gsy = ((Number) p.getProperty(5).getData()).doubleValue();
                            md.transform.gsz = ((Number) p.getProperty(6).getData()).doubleValue();
                        }
                    }
                }
            }
            
            // Apply Raw Local Transforms to Group
            // Chain: T * Rp * Rpre * R * Rp-1 * Sp * S * Sp-1
            
            // 1. Translation (T)
            if (md.transform.tx != 0 || md.transform.ty != 0 || md.transform.tz != 0) {
                md.group.getTransforms().add(new javafx.scene.transform.Translate(md.transform.tx, md.transform.ty, md.transform.tz));
            }

            // 2. Rotation Pivot (Rp)
            if (md.transform.rpx != 0 || md.transform.rpy != 0 || md.transform.rpz != 0) {
                 md.group.getTransforms().add(new javafx.scene.transform.Translate(md.transform.rpx, md.transform.rpy, md.transform.rpz));
            }
            
            // 3. Pre-Rotation (Rpre)
            if (md.transform.prx != 0) md.group.getTransforms().add(new javafx.scene.transform.Rotate(md.transform.prx, javafx.scene.transform.Rotate.X_AXIS));
            if (md.transform.pry != 0) md.group.getTransforms().add(new javafx.scene.transform.Rotate(md.transform.pry, javafx.scene.transform.Rotate.Y_AXIS));
            if (md.transform.prz != 0) md.group.getTransforms().add(new javafx.scene.transform.Rotate(md.transform.prz, javafx.scene.transform.Rotate.Z_AXIS));

            // 4. Rotation (R)
            if (md.transform.rx != 0) md.group.getTransforms().add(new javafx.scene.transform.Rotate(md.transform.rx, javafx.scene.transform.Rotate.X_AXIS));
            if (md.transform.ry != 0) md.group.getTransforms().add(new javafx.scene.transform.Rotate(md.transform.ry, javafx.scene.transform.Rotate.Y_AXIS));
            if (md.transform.rz != 0) md.group.getTransforms().add(new javafx.scene.transform.Rotate(md.transform.rz, javafx.scene.transform.Rotate.Z_AXIS));

            // 5. Inverse Rotation Pivot (Rp-1)
            if (md.transform.rpx != 0 || md.transform.rpy != 0 || md.transform.rpz != 0) {
                 md.group.getTransforms().add(new javafx.scene.transform.Translate(-md.transform.rpx, -md.transform.rpy, -md.transform.rpz));
            }

            // 6. Scaling Pivot (Sp)
            if (md.transform.spx != 0 || md.transform.spy != 0 || md.transform.spz != 0) {
                 md.group.getTransforms().add(new javafx.scene.transform.Translate(md.transform.spx, md.transform.spy, md.transform.spz));
            }

            // 7. Scaling (S)
            if (md.transform.sx != 1 || md.transform.sy != 1 || md.transform.sz != 1) {
                md.group.getTransforms().add(new javafx.scene.transform.Scale(md.transform.sx, md.transform.sy, md.transform.sz));
            }

            // 8. Inverse Scaling Pivot (Sp-1)
            if (md.transform.spx != 0 || md.transform.spy != 0 || md.transform.spz != 0) {
                 md.group.getTransforms().add(new javafx.scene.transform.Translate(-md.transform.spx, -md.transform.spy, -md.transform.spz));
            }
            
            models.put(id, md);
        }
        
        System.out.println("DEBUG: Parsed " + models.size() + " Models");
        
        // 2. Parse Connections to build Hierarchy and Link Geometries
        // Map: ChildID -> ParentID
        java.util.Map<Long, Long> childToParent = new java.util.HashMap<>();
        // List of Geometry-Model connections (support instancing - same geom used by multiple models)
        class GeomModelLink {
            long geomId;
            long modelId;
            GeomModelLink(long g, long m) { geomId = g; modelId = m; }
        }
        java.util.List<GeomModelLink> geomModelLinks = new java.util.ArrayList<>();
        
        FBXNode connections = root.getChildByName("Connections");
        if (connections != null) {
            for (int i=0; i<connections.getNumChildren(); i++) {
                FBXNode c = connections.getChild(i);
                // C: "OO", ChildID, ParentID
                if (c.getNumProperties() >= 3) {
                    if ("OO".equals(c.getProperty(0).getData())) {
                         long childId = ((Number) c.getProperty(1).getData()).longValue();
                         long parentId = ((Number) c.getProperty(2).getData()).longValue();
                         
                         // Check if links Model->Model or Geometry->Model
                         if (models.containsKey(childId) && models.containsKey(parentId)) {
                             childToParent.put(childId, parentId);
                         } else if (models.containsKey(parentId)) {
                             // Geometry -> Model link (support multiple models using same geometry)
                             geomModelLinks.add(new GeomModelLink(childId, parentId));
                         }
                    }
                }
            }
        }
        
        System.out.println("DEBUG: Found " + childToParent.size() + " Model hierarchy links");
        System.out.println("DEBUG: Found " + geomModelLinks.size() + " Geometry-Model links");
        
        // 3. Assemble Scene Graph
        for (ModelData md : models.values()) {
            Long parentId = childToParent.get(md.id);
            if (parentId != null && models.containsKey(parentId)) {
                // Link to parent
                models.get(parentId).group.getChildren().add(md.group);
            } else {
                // No parent -> Add to Root
                fbxRoot.getChildren().add(md.group);
            }
        }
        
        // 4. Load Geometries and Attach to Models
        List<FBXNode> geometryNodes = objectsNode.getChildrenByName("Geometry");
        java.util.Map<Long, FBXNode> geomMap = new java.util.HashMap<>();
        for (FBXNode g : geometryNodes) {
            // Check if it's a valid Mesh
            if (g.getNumProperties() > 2) {
                String type = (String) g.getProperty(2).getData();
                if (!"Mesh".equals(type)) {
                    // Skip non-mesh geometries (lines, curves, shapes) which cause "spikes" artifacts
                    continue;
                }
            }
            
            Object prop0 = g.getProperty(0).getData();
            if (prop0 instanceof Number) {
                geomMap.put(((Number) prop0).longValue(), g);
            }
        }
        
        // Attach Meshes
        int attachedCount = 0;
        for (GeomModelLink link : geomModelLinks) {
            ModelData md = models.get(link.modelId);
            FBXNode geomNode = geomMap.get(link.geomId);
            
            if (md != null && geomNode != null) {
                // Load Mesh with Raw Scale (1.0) and Raw Coordinates
                // Create MeshView
                MeshView mv = parseGeometry(geomNode, 1.0, 0, 0, 0); // Scale handled by Model Transform
                if (mv != null) {
                    // Set ID to Model Name (Crucial for linking with Definitions)
                    String modelName = md.name;
                    // Sanitize name if needed (e.g. remove "Model::")
                    if (modelName.startsWith("Model::")) modelName = modelName.substring(7);
                    mv.setId(modelName);
                    
                    // Apply GEOMETRIC Transforms to the MeshView (or a wrapper)
                    // These are offsets applied to the mesh vertices relative to the node origin
                    boolean hasGeoTransform = md.transform.gtx != 0 || md.transform.gty != 0 || md.transform.gtz != 0 ||
                                              md.transform.grx != 0 || md.transform.gry != 0 || md.transform.grz != 0 ||
                                              md.transform.gsx != 1 || md.transform.gsy != 1 || md.transform.gsz != 1;
                    
                    // Add to Model Group
                    if (hasGeoTransform) {
                        Group meshWrapper = new Group(mv);
                        meshWrapper.setId(modelName + "_Wrapper"); // Optional
                        meshWrapper.setTranslateX(md.transform.gtx);
                        meshWrapper.setTranslateY(md.transform.gty);
                        meshWrapper.setTranslateZ(md.transform.gtz);
                        
                        if (md.transform.grx != 0) meshWrapper.getTransforms().add(new javafx.scene.transform.Rotate(md.transform.grx, javafx.scene.transform.Rotate.X_AXIS));
                        if (md.transform.gry != 0) meshWrapper.getTransforms().add(new javafx.scene.transform.Rotate(md.transform.gry, javafx.scene.transform.Rotate.Y_AXIS));
                        if (md.transform.grz != 0) meshWrapper.getTransforms().add(new javafx.scene.transform.Rotate(md.transform.grz, javafx.scene.transform.Rotate.Z_AXIS));
                        
                        if (md.transform.gsx != 1 || md.transform.gsy != 1 || md.transform.gsz != 1) {
                             meshWrapper.setScaleX(md.transform.gsx);
                             meshWrapper.setScaleY(md.transform.gsy);
                             meshWrapper.setScaleZ(md.transform.gsz);
                        }
                        md.group.getChildren().add(meshWrapper);
                    } else {
                        md.group.getChildren().add(mv);
                    }
                    
                    attachedCount++;
                }
            }
        }
        
        System.out.println("DEBUG: Attached " + attachedCount + " meshes to models");
        System.out.println("DEBUG: fbxRoot has " + fbxRoot.getChildren().size() + " root children");
        
        // 5. Global Coordinate System Fix
        Group worldGroup = new Group(fbxRoot);
        
        // Scale: User reported 10.0 was too close. Try 1.0.
        double globalScale = 1.0; 
        
        // Initial setup
        worldGroup.setScaleX(globalScale);
        worldGroup.setScaleY(-globalScale); // Y-Flip is ESSENTIAL for Z-up/Maya-Y-up -> JavaFX Y-down
        worldGroup.setScaleZ(globalScale);
        
        // DISABLED AUTO-CENTER:
        // Why? Because loading multiple FBX files (Skeleton.fbx, Muscles.fbx) separately means 
        // they each have different bounding boxes. If we center each one, they will not align with each other.
        // We assume the FBX files share a common global origin (0,0,0).
        
        /* 
        javafx.geometry.Bounds bounds = fbxRoot.getBoundsInLocal();
        double cx = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double cy = (bounds.getMinY() + bounds.getMaxY()) / 2;
        double cz = (bounds.getMinZ() + bounds.getMaxZ()) / 2;
        fbxRoot.setTranslateX(-cx);
        fbxRoot.setTranslateY(-cy);
        fbxRoot.setTranslateZ(-cz);
        */
        
        System.out.println("DEBUG: Applied global scale: " + globalScale + " with Y-Flip. Auto-Center DISABLED for alignment.");

        return worldGroup;
    }
    
    /**
     * Parses a single Geometry node into a MeshView.
     */
    private static MeshView parseGeometry(FBXNode geomNode, double scale, double centerX, double centerY, double centerZ) {
        // Get Vertices
        FBXNode verticesNode = geomNode.getChildByName("Vertices");
        if (verticesNode == null || verticesNode.getNumProperties() == 0) return null;
        
        FBXProperty verticesProp = verticesNode.getProperty(0);
        if (verticesProp.getDataType() != FBXDataType.DOUBLE_ARRAY) return null;
        double[] verticesData = (double[]) verticesProp.getData();
        
        // Get Faces
        FBXNode indicesNode = geomNode.getChildByName("PolygonVertexIndex");
        if (indicesNode == null || indicesNode.getNumProperties() == 0) return null;
        
        FBXProperty indicesProp = indicesNode.getProperty(0);
        if (indicesProp.getDataType() != FBXDataType.INT_ARRAY) return null;
        int[] indicesData = (int[]) indicesProp.getData();
        
        TriangleMesh mesh = new TriangleMesh(VertexFormat.POINT_TEXCOORD);
        
        // Raw vertices, no axis swap (handled by Root Group), only base scale (passed as 1.0)
        float[] points = new float[verticesData.length];
        for (int i = 0; i < verticesData.length; i += 3) {
            points[i] = (float) (verticesData[i] * scale);     // X
            points[i+1] = (float) (verticesData[i+1] * scale); // Y
            points[i+2] = (float) (verticesData[i+2] * scale); // Z
        }
        mesh.getPoints().addAll(points);
        
        // Dummy UVs
        mesh.getTexCoords().addAll(0f, 0f);
        
        // Faces logic (Triangle Fan)
        List<Integer> currentFace = new ArrayList<>();
        for (int idx : indicesData) {
            boolean endOfFace = idx < 0;
            int realIdx = endOfFace ? ~idx : idx;
            currentFace.add(realIdx);
            
            if (endOfFace) {
                if (currentFace.size() >= 3) {
                    int v0 = currentFace.get(0);
                    for (int i = 1; i < currentFace.size() - 1; i++) {
                        int v1 = currentFace.get(i);
                        int v2 = currentFace.get(i + 1);
                        mesh.getFaces().addAll(v0, 0, v1, 0, v2, 0);
                    }
                }
                currentFace.clear();
            }
        }
        
        if (mesh.getFaces().size() == 0) return null;
        
        MeshView meshView = new MeshView(mesh);
        PhongMaterial material = new PhongMaterial(javafx.scene.paint.Color.LIGHTGRAY);
        material.setSpecularColor(javafx.scene.paint.Color.WHITE);
        meshView.setMaterial(material);
        
        return meshView;
    }
    private static String sanitizeID(String raw) {
        if (raw == null) return "Unknown";
        
        // Remove "Model::" prefix standard in FBX
        if (raw.startsWith("Model::")) {
            raw = raw.substring(7);
        }
        
        // Split by non-printable characters or null bytes to strip garbage suffixes
        // The FBX parser sometimes reads past the string into the next field (e.g. "Model" type)
        String[] parts = raw.split("[^\\x20-\\x7E]+");
        if (parts.length > 0) {
            raw = parts[0];
        }
        
        return raw.trim();
    }
}
