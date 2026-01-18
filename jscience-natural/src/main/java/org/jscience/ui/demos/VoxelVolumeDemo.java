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

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.mathematics.geometry.volume.ProceduralVoxelModel;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.viewers.shared.volume.VoxelViewer;

/**
 * Procedural Voxel Demo using mathematical models.
 */
public class VoxelVolumeDemo extends AbstractDemo {

    @Override public String getCategory() { return "Computing"; }
    @Override public String getName() { return "Procedural Voxel Volume"; }
    @Override public String getDescription() { return "Mathematical visualization of 3D scalar fields."; }

    @Override
    public Node createViewerNode() {
        ProceduralVoxelModel model = new ProceduralVoxelModel("Octahedral Noise", 128, 128, 128);
        return new VoxelViewer(model);
    }

    @Override public String getLongDescription() { return getDescription(); }
}
