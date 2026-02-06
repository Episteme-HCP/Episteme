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

package org.jscience.social.ui.viewers.sociology;

import org.jscience.core.ui.viewers.mathematics.discrete.GraphViewer;
import org.jscience.core.ui.i18n.I18N;
import org.jscience.core.ui.Parameter;
import org.jscience.core.ui.RealParameter;
import java.util.List;

/**
 * Specialized Network Viewer for Social Dynamics.
 * Visualizes genealogies, political alliances, and opinion interaction graphs.
 */
public final class NetworkDynamicsViewer extends GraphViewer {

    public NetworkDynamicsViewer() {
        super();
        getStyleClass().add("network-dynamics-viewer");
    }

    @Override
    public List<Parameter<?>> getViewerParameters() {
        List<Parameter<?>> params = super.getViewerParameters();
        params.add(new RealParameter(
            I18N.getInstance().get("viewer.social.opinion_threshold", "Opinion Influence Threshold"),
            "Minimum relation strength to show interaction", 0.0, 1.0, 0.05, 0.5, val -> {
                // Future: add filtering logic to renderer
            }));
        return params;
    }

    @Override public String getCategory() { return I18N.getInstance().get("category.social", "Social Sciences"); }
    @Override public String getName() { return I18N.getInstance().get("viewer.social.network.name", "Network Dynamics Viewer"); }
    @Override public String getDescription() { return I18N.getInstance().get("viewer.social.network.desc", "Universal graph motor for opinion and social relation networks."); }
    @Override public String getLongDescription() { return I18N.getInstance().get("viewer.social.network.longdesc", "Advanced graph visualization for social dynamics."); }
}

